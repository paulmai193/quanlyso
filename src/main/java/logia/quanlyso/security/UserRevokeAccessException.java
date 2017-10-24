/*
 * 
 */
package logia.quanlyso.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of user trying to authenticate after be
 * revoked access. Require admin grants another access
 *
 * @author Dai Mai
 */
public class UserRevokeAccessException extends AuthenticationException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new user revoke access exception.
	 *
	 * @param message
	 *            the message
	 */
	public UserRevokeAccessException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new user revoke access exception.
	 *
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public UserRevokeAccessException(String message, Throwable t) {
		super(message, t);
	}
}
