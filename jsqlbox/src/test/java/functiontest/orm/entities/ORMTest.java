package functiontest.orm.entities;

import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Before;
import org.junit.Test;

import com.github.drinkjava2.jdialects.model.TableModel;
import com.github.drinkjava2.jdialects.utils.DialectUtils;
import com.github.drinkjava2.jsqlbox.SqlBoxContextUtils;

import config.TestBase;

public class ORMTest extends TestBase {
	@Before
	public void init() {
		super.init();
		TableModel[] models = DialectUtils.pojos2Models(User.class, Email.class, Address.class, Role.class,
				Privilege.class, UserRole.class, RolePrivilege.class);
		dropAndCreateDatabase(models);
		ctx.refreshMetaData();
		ctx.nExecute("insert into usertb values('u1','user1')");
		ctx.nExecute("insert into usertb values('u2','user2')");
		ctx.nExecute("insert into usertb values('u3','user3')");
		ctx.nExecute("insert into usertb values('u4','user4')");
		ctx.nExecute("insert into usertb values('u5','user5')");

		ctx.nExecute("insert into address values('a1','address1','u1')");
		ctx.nExecute("insert into address values('a2','address2','u2')");
		ctx.nExecute("insert into address values('a3','address3','u3')");
		ctx.nExecute("insert into address values('a4','address4','u4')");
		ctx.nExecute("insert into address values('a5','address5','u5')");

		ctx.nExecute("insert into email values('e1','email1','u1')");
		ctx.nExecute("insert into email values('e2','email2','u1')");
		ctx.nExecute("insert into email values('e3','email3','u2')");
		ctx.nExecute("insert into email values('e4','email4','u2')");
		ctx.nExecute("insert into email values('e5','email5','u3')");

		ctx.nExecute("insert into roles values('r1','role1')");
		ctx.nExecute("insert into roles values('r2','role2')");
		ctx.nExecute("insert into roles values('r3','role3')");
		ctx.nExecute("insert into roles values('r4','role4')");
		ctx.nExecute("insert into roles values('r5','role5')");

		ctx.nExecute("insert into privilegetb values('p1','privilege1')");
		ctx.nExecute("insert into privilegetb values('p2','privilege2')");
		ctx.nExecute("insert into privilegetb values('p3','privilege3')");
		ctx.nExecute("insert into privilegetb values('p4','privilege4')");
		ctx.nExecute("insert into privilegetb values('p5','privilege5')");

		ctx.nExecute("insert into userroletb values('u1','r1')");
		ctx.nExecute("insert into userroletb values('u2','r1')");
		ctx.nExecute("insert into userroletb values('u2','r2')");
		ctx.nExecute("insert into userroletb values('u2','r3')");
		ctx.nExecute("insert into userroletb values('u3','r4')");
		ctx.nExecute("insert into userroletb values('u4','r1')");

		ctx.nExecute("insert into roleprivilege values('r1','p1')");
		ctx.nExecute("insert into roleprivilege values('r2','p1')");
		ctx.nExecute("insert into roleprivilege values('r2','p2')");
		ctx.nExecute("insert into roleprivilege values('r2','p3')");
		ctx.nExecute("insert into roleprivilege values('r3','p3')");
		ctx.nExecute("insert into roleprivilege values('r4','p1')");
	}

	@Test
	public void test() {
		System.out.println(SqlBoxContextUtils.explainDoubleStarSql(ctx,
				"select u.**, e.** from usertb u, email e where u.id=e.userId"));

		List<Map<String, Object>> listMap = ctx.nQuery("select u.**, e.** from usertb u, email e where u.id=e.userId",
				new MapListHandler());
		// Assert.assertEquals(5, listMap.size());
		// EntityNet net = new EntityNet(ctx, User.class, Email.class);
		// EntityNet net = new EntityNet(ctx, new SqlBox(User.class).alias("u"), Email.class);
		//List<Map<String, Object>> listMap = net.nQuery("select u.**, e.** from usertb u, email e where u.id=e.userId", new MapListHandler());
		// net.weave(listMap);
		
		// EntityNet net = ctx.weave(listMap, User.class, Email.class);
		// EntityNet net = ctx.weave(listMap, ctx.box(User.class).alias('u'),
		// Email.class);
		// List<User> users = net.getList(User.class);
		// for (User user : users) {
		// List<Email> emails = user.getChildEntities(Email.class);
		// List<Email> emails = user.getChildEntities(); //if only have 1 child
		// List<Email> emails = ctx.getChildEntities(user, Email.class);

		// User u = email.getParentNode(User.class);
		// User u = email.getParentNode() //if only have one master;

		// List<Email> emails = User.getChildNodes(); //if only has 1 child
		// List<Email> emails = user.getRelatedNodes(Email.class);
		// List<Email> emails = user.getRelatedNodes("P", Path1.class, "C",
		// Path2.class,...,"C",Email.class);

		// List<User> users = email.getRelatedEntity(User.class);
		// }
	}

}