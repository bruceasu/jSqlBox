/*
 * Copyright (C) 2016 Yong Zhu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.drinkjava2.jsqlbox;

import com.github.drinkjava2.jdialects.model.TableModel;

/**
 * SqlBox is the configuration of a POJO class
 * 
 * @author Yong Zhu (Yong9981@gmail.com)
 * @since 1.0.0
 */
public class SqlBox {
	/** A TableModel instance */
	TableModel tableModel;

	/** A SqlBoxContext instance */
	SqlBoxContext context;

	/** Bind to which entity Bean instance */
	Object entityBean;

	// Shortcut method
	public String table() {
		return tableModel.getTableName();
	}

	// getter & setter ========
	public SqlBoxContext getContext() {
		return context;
	}

	public void setContext(SqlBoxContext context) {
		this.context = context;
	}

	public Object getEntityBean() {
		return entityBean;
	}

	public void setEntityBean(Object entityBean) {
		this.entityBean = entityBean;
	}

	public TableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}

}
