/*
 * Copyright (C) 2016 Original Author
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package jsqlboxtx;

import static com.github.drinkjava2.jsqlbox.DB.*;

import java.util.Properties;

import javax.transaction.SystemException;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosSQLException;
import com.github.drinkjava2.jbeanbox.BeanBox;
import com.github.drinkjava2.jbeanbox.JBEANBOX;
import com.github.drinkjava2.jdialects.Dialect;
import com.github.drinkjava2.jdialects.TableModelUtils;
import com.github.drinkjava2.jdialects.annotation.jdia.ShardDatabase;
import com.github.drinkjava2.jdialects.annotation.jpa.Id;
import com.github.drinkjava2.jdialects.model.TableModel;
import com.github.drinkjava2.jsqlbox.ActiveRecord;
import com.github.drinkjava2.jsqlbox.DbContext;

/**
 * Atomikos Transaction test, H2 + jBeanBox + jSqlBox + Spring XA + Atomikos
 * 
 * @author Yong Zhu
 * @since 1.0.0
 */

public class XATransactionTest {
	static int DATABASE_QTY = 3;
	static DbContext[] masters = new DbContext[DATABASE_QTY];
	static AtomikosDataSourceBean[] xaDataSources = new AtomikosDataSourceBean[DATABASE_QTY];
	static UserTransactionManager um;

	public static class SpringTxIBox extends BeanBox {
		public TransactionInterceptor create() {
			JtaTransactionManager springJM = new JtaTransactionManager();
			springJM.setUserTransaction(new UserTransactionImp());
			um = new UserTransactionManager();
			um.setForceShutdown(true);
			try {
				um.init();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			springJM.setTransactionManager(um);
			springJM.setAllowCustomIsolationLevels(true);
			Properties props = new Properties();
			props.put("*", "PROPAGATION_REQUIRED, ISOLATION_READ_COMMITTED");
			return new TransactionInterceptor(springJM, props);
		}
	}

	@Before
	public void init() {
		JBEANBOX.ctx().addContextAop(SpringTxIBox.class, XATransactionTest.class, "insert*");
		JBEANBOX.getBean(SpringTxIBox.class);// Force initialize

		DbContext.setGlobalNextDialect(Dialect.MySQL57Dialect);
		for (int i = 0; i < DATABASE_QTY; i++) {
			JdbcDataSource ds = new JdbcDataSource();
			ds.setUrl("jdbc:h2:mem:H2DB" + i + ";MODE=MYSQL;DB_CLOSE_DELAY=-1;TRACE_LEVEL_SYSTEM_OUT=0");
			ds.setUser("sa");
			ds.setPassword("");
			xaDataSources[i] = new AtomikosDataSourceBean();
			xaDataSources[i].setUniqueResourceName("xads" + i);
			xaDataSources[i].setXaDataSource(ds);
			xaDataSources[i].setPoolSize(5);
			try {
				xaDataSources[i].init();
			} catch (AtomikosSQLException e) {
				e.printStackTrace();
			}
			masters[i] = new DbContext(xaDataSources[i]);
			masters[i].setMasters(masters);
			masters[i].setConnectionManager(null);

		}
		DbContext.setGlobalDbContext(masters[0]);// random choose 1
		TableModel model = TableModelUtils.entity2Model(Bank.class);
		for (int i = 0; i < DATABASE_QTY; i++)
			for (String ddl : masters[i].toCreateDDL(model))
				masters[i].exe(ddl);
	}

	@After
	public void cleanup() {
		// Usually cleanup job need done by IOC tool's preDestroy callback, but I'm lazy
		for (int i = 0; i < DATABASE_QTY; i++)
			xaDataSources[i].close();
		um.close();
	}

	public void insertAccountsBad() {
		new Bank().putField("bankId", 0L, "balance", 100L).insert();
		Assert.assertEquals(1, qryLongValue("select count(*) from bank", masters[0]));
		System.out.println("In insertAccountsBad() method, 1 record inserted in database0, but will rollback");
		new Bank().putField("bankId", 1L, "balance", 100L).insert();
		new Bank().putField("bankId", 2L, "balance", 1 / 0).insert();// div 0!
	}

	public void insertAccountsGood() {
		new Bank().putField("bankId", 0L, "balance", 100L).insert();
		new Bank().putField("bankId", 1L, "balance", 100L).insert();
		new Bank().putField("bankId", 2L, "balance", 100L).insert();
	}

	@Test
	public void testXATransaction() {
		XATransactionTest tester = JBEANBOX.getBean(XATransactionTest.class);
		try {
			tester.insertAccountsBad();
		} catch (Exception e) {
			System.out.println("Div 0 RuntimeException caused no records be inserted into any database.");
		}
		Assert.assertEquals(0, qryLongValue("select count(*) from bank", masters[0]));
		Assert.assertEquals(0, qryLongValue("select count(*) from bank", masters[1]));
		Assert.assertEquals(0, qryLongValue("select count(*) from bank", masters[2]));

		tester.insertAccountsGood();
		Assert.assertEquals(1, qryLongValue("select count(*) from bank", masters[0]));
		Assert.assertEquals(1, qryLongValue("select count(*) from bank", masters[1]));
		Assert.assertEquals(1, qryLongValue("select count(*) from bank", masters[2]));
	}

	//@formatter:off 
	public static class Bank extends ActiveRecord<Bank> {
		@ShardDatabase({ "MOD", "3" })
		@Id
		private Long bankId;
		private Long balance; 
		public Long getBankId() {return bankId;}
		public void setBankId(Long bankId) {this.bankId = bankId;}
		public Long getBalance() {return balance;}
		public void setBalance(Long balance) {this.balance = balance;}
	}	
}