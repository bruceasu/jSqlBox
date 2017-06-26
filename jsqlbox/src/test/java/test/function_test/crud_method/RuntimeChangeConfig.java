package test.function_test.crud_method;

import org.junit.Assert;
import org.junit.Test;

import com.github.drinkjava2.jdialects.Dialect;
import com.github.drinkjava2.jdialects.model.Table;
import com.github.drinkjava2.jsqlbox.Dao;

import test.TestBase;
import test.config.entity.User;

public class RuntimeChangeConfig extends TestBase {

	@Test
	public void normal() {
		User u = new User();
		u.setUserName("Sam");
		u.insert();
		Assert.assertEquals(1, (int) Dao.queryForInteger("select count(*) from users"));
	}

	@Test
	public void changeTable() {
		Dao.executeQuiet("drop table users2");
		Dialect d = Dao.getDialect();
		Table tb2 = new Table("users2");
		tb2.column("id").VARCHAR(32);
		tb2.column("username").VARCHAR(50);
		Dao.executeManyQuiet(d.toDropDDL(tb2));
		Dao.executeMany(d.toCreateDDL(tb2));

		Dao.refreshMetaData();

		User u = new User();
		u.box().configTable("users2");
		u.setUserName("Sam");
		u.insert();
		Assert.assertEquals(0, (int) Dao.queryForInteger("select count(*) from users"));
		Assert.assertEquals(1, (int) Dao.queryForInteger("select count(*) from users2"));
		Dao.executeQuiet("drop table users2");
	}

	@Test
	public void changeColumnName() {
		User u = new User();
		u.box().configColumnName(u.fieldID(u.USERNAME()), u.ADDRESS());
		u.box().configColumnName(u.fieldID(u.ADDRESS()), u.PHONENUMBER());
		u.setUserName("Sam");
		u.setPhoneNumber("111");
		u.insert();
		// below line, sql is "select Address from users"
		Assert.assertEquals("Sam", Dao.queryForString("select ", u.USERNAME(), " from ", u.table()));

		// below line sql is "select phoneNumber from users"
		Assert.assertEquals("111", Dao.queryForString("select ", u.ADDRESS(), " from ", u.table()));
	}

}