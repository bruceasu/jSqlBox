/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.drinkjava2.jsqlbox.gtx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.github.drinkjava2.jsqlbox.SqlBoxContext;
import com.github.drinkjava2.jtransactions.ConnectionManager;
import com.github.drinkjava2.jtransactions.TransactionsException;

/**
 * DataSourceManager determine how to get or release connection from DataSource,
 * it can be different transaction strategies like JDBC/SpringManaged/JTA..
 * 
 * @author Yong Zhu
 * @since 1.0.0
 */
public class GtxConnectionManager implements ConnectionManager {

	private int transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
	private DataSource[] dataSources;
	private SqlBoxContext lockServer;

	public GtxConnectionManager(SqlBoxContext lockServer, DataSource... dataSources) {
		this.lockServer = lockServer;
		this.dataSources = dataSources;
	}

	public GtxConnectionManager(SqlBoxContext lockServer, Integer transactionIsolation, DataSource... dataSources) {
		this.lockServer = lockServer;
		this.transactionIsolation = transactionIsolation;
		this.dataSources = dataSources;
	}

	private ThreadLocal<String> gtxLockId = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return null;
		}
	};

	private ThreadLocal<Map<DataSource, Connection>> threadLocalConnections = new ThreadLocal<Map<DataSource, Connection>>() {
		@Override
		protected Map<DataSource, Connection> initialValue() {
			return new HashMap<DataSource, Connection>();
		}
	};

	public int insertGtxLockId(String gtxLock) {
		return lockServer.nUpdate("insert into gtxlock (id) values(?)", gtxLock);
	}

	public int deleteGtxLockId(String gtxLock) {
		return lockServer.nUpdate("delete from gtxlock where id=?", gtxLock);
	}

	public String getGtxLockId() {
		return gtxLockId.get();
	}

	@Override
	public boolean isInTransaction(DataSource ds) {
		return gtxLockId.get() != null;
	}

	public void startGtx(String _gtxLockId) {
		gtxLockId.set(_gtxLockId);
	}

	public void endGtx() {
		gtxLockId.set(null);
		Map<DataSource, Connection> map = threadLocalConnections.get();
		if (map == null)
			return;
		SQLException lastExp = null;

		for (Entry<DataSource, Connection> entry : map.entrySet())
			try {
				entry.getValue().setAutoCommit(true); // restore auto commit
			} catch (SQLException e) {
				if (lastExp != null)
					e.setNextException(lastExp);
				lastExp = e;
			}

		for (Entry<DataSource, Connection> entry : map.entrySet())
			try {
				entry.getValue().close(); // Close
			} catch (SQLException e) {
				if (lastExp != null)
					e.setNextException(lastExp);
				lastExp = e;
			}

		map.clear();
		if (lastExp != null)
			throw new TransactionsException(lastExp);
	}

	public void commitGtx() {// TODO

	}

	public void rollbackGtx() {// TODO

	}

	@Override
	public Connection getConnection(DataSource ds) throws SQLException {
		TransactionsException.assureNotNull(ds, "DataSource can not be null");
		Connection conn = null;
		if (isInTransaction(ds)) {// TODO
			conn = threadLocalConnections.get().get(ds);
			if (conn == null) {
				conn = ds.getConnection(); // NOSONAR Have to get a new connection
				TransactionsException.assureNotNull(conn, "Can not obtain a connection from DataSource");
				conn.setTransactionIsolation(transactionIsolation);
				conn.setAutoCommit(false); // start real transaction
				threadLocalConnections.get().put(ds, conn);
			}
		} else {
			conn = ds.getConnection(); // Have to get a new connection
		}
		TransactionsException.assureNotNull(conn, "Fail to get a connection from DataSource");
		return conn;
	}

	@Override
	public void releaseConnection(Connection conn, DataSource ds) throws SQLException {
		if (isInTransaction(ds)) {
			// Do nothing, because this connection is used in a current thread's transaction
		} else {
			if (conn != null)
				conn.close();
		}
	}

	public DataSource[] getDataSources() {
		return dataSources;
	}

	public void setDataSources(DataSource[] dataSources) {
		this.dataSources = dataSources;
	}

}