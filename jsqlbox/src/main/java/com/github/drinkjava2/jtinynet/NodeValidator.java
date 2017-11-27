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
package com.github.drinkjava2.jtinynet;

/**
 * Checker used to check if a node allowed to be put into input list or output
 * list
 * 
 * @author Yong Zhu (Yong9981@gmail.com)
 * @since 1.0.0
 */
public interface NodeValidator {

	/**
	 * Check if a node allow be selected
	 * 
	 * @param node The node to be check
	 * @param selectLevel Current search level
	 * @param selectedSize Current already selected node size
	 * @param path Current path
	 * @return True or false depends validate result
	 */
	public abstract boolean validateNode(Node node, int selectLevel, int selectedSize, Path path);

}
