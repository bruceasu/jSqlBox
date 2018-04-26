/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.drinkjava2.jdbpro;

/**
 * SqlItemType type is an enum type
 * 
 * @author Yong Zhu
 * @since 1.7.0.3
 */
public enum SqlItemType {
	SQL, // Append a SQL String piece

	PARAM, // Append a parameter or parameter array

	PUT, // Append [key1,value1, key2,value2...] parameters array (for SqlTemplateEngine)

	QUESTION_PARAM, // Append a "?" at end of SQL, append a parameter or parameter array

	NOT_NULL, // Usage: NOT_NUL("user_name=?", name), only effect when name is not null

	VALUES_QUESTIONS // Append a " values(?,?,?....?)" String at end of SQL
}