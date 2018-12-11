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
package com.github.drinkjava2.jtransactions.tinytx;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.github.drinkjava2.jtransactions.TransactionsException;

/**
 * A transaction MethodInterceptor
 * 
 * @author Yong Zhu
 * @since 1.0.0
 */
public class TinyTx implements MethodInterceptor {
	private static final TinyTxConnectionManager cm = TinyTxConnectionManager.instance();

	private int transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
	private DataSource ds;

	public TinyTx() {
	}

	public TinyTx(DataSource ds) {
		this.ds = ds;
	}

	public TinyTx(DataSource ds, Integer transactionIsolation) {
		this.ds = ds;
		this.transactionIsolation = transactionIsolation;
	}

	@Override
	public Object invoke(MethodInvocation caller) {// NOSONAR
		if (cm.isInTransaction(ds)) {
			try {
				return caller.proceed();
			} catch (Throwable t) {
				throw new TransactionsException(t);
			}
		} else {
			Connection conn;
			try {
				conn = cm.getConnection(ds);
				TransactionsException.assureNotNull(conn, "Connection can not get from DataSource in invoke method");
			} catch (Exception e) {
				throw new TransactionsException(e);
			}
			Object invokeResult = null;
			try {
				cm.startTransaction(ds, conn);
				conn.setTransactionIsolation(transactionIsolation);
				conn.setAutoCommit(false);
				invokeResult = caller.proceed();
				conn.commit();
			} catch (Throwable t) {
				if (conn != null)
					try {
						conn.rollback();
					} catch (Exception e1) {
						throw new TransactionsException("TinyTx fail to rollback a transaction.", t);
					}
				throw new TransactionsException("TinyTx found a runtime Exception, transaction rollbacked.", t);
			} finally {
				cm.endTransaction(ds);
				SQLException closeExp = null;
				try {
					cm.releaseConnection(conn, ds);
				} catch (SQLException e) {
					closeExp = e;
				}
				if (closeExp != null)
					throw new TransactionsException("Exception happen when release connection.", closeExp);// NOSONAR
			}
			return invokeResult;
		}
	}

}