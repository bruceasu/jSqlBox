package test.config.entity;

import com.github.drinkjava2.jdialects.model.Table;
import com.github.drinkjava2.jsqlbox.EntityBase;

/**
 * User class is not a POJO, need extends from EntityBase(For JAVA7-) or
 * implements EntityInterface interface(for JAVA8+)<br/>
 * 
 * Default database table equal to entity name or add a "s" suffix , in this
 * example it will use "users" as table name
 * 
 * @author Yong Zhu
 *
 * @version 1.0.0
 * @since 1.0.0
 */

public class User extends EntityBase {
	private String id;
	private String userName;
	private String phoneNumber;
	private String address;
	private Integer age;
	private Boolean active;

	public static Table model() {
		Table t = new Table("users");
		t.column("id").VARCHAR(32);
		t.column("username").VARCHAR(50).defaultValue("'aaa'");
		t.column("Phone_Number").VARCHAR(50).index("IDX_PhoneNM");
		t.column("Address").VARCHAR(50).index();
		t.column("active").BOOLEAN();
		t.column("Age").INTEGER().check("Age > 0");
		return t;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	// Below methods are for refactor support, not compulsory but suggest have
	// Hope in future JAVA version can support access private field name
	// directly
	//@formatter:off 
	public String ID(Object... option)             {return box().getColumnName("id", option);	}
	public String USERNAME(Object... option)       {return box().getColumnName("userName", option);}
	public String PHONENUMBER(Object... option)    {return box().getColumnName("phoneNumber", option);}
	public String ADDRESS(Object... option)        {return box().getColumnName("address", option);	}
	public String AGE(Object... option)            {return box().getColumnName("age", option);}
	public String ACTIVE(Object... option)         {return box().getColumnName("active", option);	}
}