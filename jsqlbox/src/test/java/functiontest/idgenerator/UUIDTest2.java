/**
 * Copyright (C) 2016 Yong Zhu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package functiontest.idgenerator;

import org.junit.Test;

import com.github.drinkjava2.jdialects.annotation.jdia.UUID25;
import com.github.drinkjava2.jdialects.annotation.jdia.UUID32;
import com.github.drinkjava2.jdialects.annotation.jdia.UUID36;
import com.github.drinkjava2.jdialects.annotation.jdia.UUIDAny;
import com.github.drinkjava2.jdialects.annotation.jpa.GeneratedValue;
import com.github.drinkjava2.jdialects.annotation.jpa.GenerationType;
import com.github.drinkjava2.jdialects.model.TableModel;
import com.github.drinkjava2.jdialects.utils.DialectUtils;

import config.TestBase;

/**
 * Unit test for SortedUUIDGenerator
 */
public class UUIDTest2 extends TestBase {

	public static class UUID25Pojo {
		@GeneratedValue(strategy = GenerationType.UUID25)
		private String id1;
		@UUID25
		private String id2;

		private String id3;

		public static void config(TableModel t) {
			t.column("id3").uuid25();
		}

		public String getId1() {
			return id1;
		}

		public void setId1(String id1) {
			this.id1 = id1;
		}

		public String getId2() {
			return id2;
		}

		public void setId2(String id2) {
			this.id2 = id2;
		}

		public String getId3() {
			return id3;
		}

		public void setId3(String id3) {
			this.id3 = id3;
		}

	}

	@Test
	public void testUUID25() {
		ctx.setAllowShowSQL(true);
		dropAndCreateDatabase(DialectUtils.pojos2Models(UUID25Pojo.class));
		UUID25Pojo pojo = new UUID25Pojo();
		ctx.insert(pojo);
	}

	public static class UUID32Pojo {
		@GeneratedValue(strategy = GenerationType.UUID32)
		private String id1;
		@UUID32
		private String id2;

		private String id3;

		public static void config(TableModel t) {
			t.column("id3").uuid32();
		}

		public String getId1() {
			return id1;
		}

		public void setId1(String id1) {
			this.id1 = id1;
		}

		public String getId2() {
			return id2;
		}

		public void setId2(String id2) {
			this.id2 = id2;
		}

		public String getId3() {
			return id3;
		}

		public void setId3(String id3) {
			this.id3 = id3;
		}

	}

	@Test
	public void testUUID32() {
		ctx.setAllowShowSQL(true);
		dropAndCreateDatabase(DialectUtils.pojos2Models(UUID32Pojo.class));
		UUID32Pojo pojo = new UUID32Pojo();
		ctx.insert(pojo);
	}

	public static class UUID36Pojo {
		@GeneratedValue(strategy = GenerationType.UUID36)
		private String id1;
		@UUID36
		private String id2;

		private String id3;

		public static void config(TableModel t) {
			t.column("id3").uuid36();
		}

		public String getId1() {
			return id1;
		}

		public void setId1(String id1) {
			this.id1 = id1;
		}

		public String getId2() {
			return id2;
		}

		public void setId2(String id2) {
			this.id2 = id2;
		}

		public String getId3() {
			return id3;
		}

		public void setId3(String id3) {
			this.id3 = id3;
		}

	}

	@Test
	public void testUUID36() {
		ctx.setAllowShowSQL(true);
		dropAndCreateDatabase(DialectUtils.pojos2Models(UUID36Pojo.class));
		UUID36Pojo pojo = new UUID36Pojo();
		ctx.insert(pojo);
	}

	@UUIDAny(name = "uuidany10", length = 10)
	public static class UUIDAnyPojo {
		@GeneratedValue(strategy = GenerationType.UUID_ANY, generator = "uuidany10")
		private String id1;

		@UUIDAny(name = "uuidany30", length = 30)
		@GeneratedValue(strategy = GenerationType.UUID_ANY, generator = "uuidany30")
		private String id2;

		private String id3;

		public static void config(TableModel t) {
			t.column("id3").uuidAny("uuid50", 50);
		}

		public String getId1() {
			return id1;
		}

		public void setId1(String id1) {
			this.id1 = id1;
		}

		public String getId2() {
			return id2;
		}

		public void setId2(String id2) {
			this.id2 = id2;
		}

		public String getId3() {
			return id3;
		}

		public void setId3(String id3) {
			this.id3 = id3;
		}
	}

	@Test
	public void testUUIDAny() {
		ctx.setAllowShowSQL(true);
		dropAndCreateDatabase(DialectUtils.pojos2Models(UUIDAnyPojo.class));
		UUIDAnyPojo pojo = new UUIDAnyPojo();
		ctx.insert(pojo);
	}
}
