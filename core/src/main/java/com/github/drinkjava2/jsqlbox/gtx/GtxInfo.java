/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.drinkjava2.jsqlbox.gtx;

/**
 * GtxInfo is a POJO store global transaction info
 * 
 * @author Yong Zhu
 * @since 2.0.7
 */
public class GtxInfo {
	public static final String INSERT = "INSERT";
	public static final String DELETE = "DELETE";
	public static final String BEFORE_UPDATE = "BEFORE";
	public static final String AFTER_UPDATE = "AFTER";
	public static final String LOAD = "LOAD";
	public static final String EXIST = "EXIST";
	public static final String NOT_EXIST = "NOT_EXIST";

	private Boolean gtxOpen = false;
	private String gtxid = null;

	public Boolean getGtxOpen() {
		return gtxOpen;
	}

	public void setGtxOpen(Boolean gtxOpen) {
		this.gtxOpen = gtxOpen;
	}

	public String getGtxid() {
		return gtxid;
	}

	public void setGtxid(String gtxid) {
		this.gtxid = gtxid;
	}

}