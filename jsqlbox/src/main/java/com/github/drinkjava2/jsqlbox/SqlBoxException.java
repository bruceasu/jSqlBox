
package com.github.drinkjava2.jsqlbox;

/**
 * @author Yong Zhu
 * @version 1.0.0
 * @since 1.0.0
 */
public class SqlBoxException extends RuntimeException {
	private static final SqlBoxLogger log = SqlBoxLogger.getLog(SqlBoxException.class);

	private static final long serialVersionUID = 1L;

	public SqlBoxException() {
		// Default public constructor
	}

	public SqlBoxException(String message) {
		super(message);
	}

	/**
	 * Transfer all Exceptions to RuntimeException SqlBoxException. The only place throw Exception in this project
	 */
	public static void throwEX(Exception e, String errorMsg) {
		log.error(errorMsg + "\r\n" + e.getMessage());
		throw new SqlBoxException(errorMsg);
	}

	/**
	 * Eat exception to avoid SONAR warning
	 */
	public static void eatException(Exception e) {
		// do nothing
	}
}