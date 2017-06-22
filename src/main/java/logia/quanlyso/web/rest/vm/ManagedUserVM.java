package logia.quanlyso.web.rest.vm;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.validation.constraints.Size;

import logia.quanlyso.service.dto.UserDTO;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 *
 * @author Dai Mai
 */
public class ManagedUserVM extends UserDTO {

	/** The Constant PASSWORD_MIN_LENGTH. */
	public static final int PASSWORD_MIN_LENGTH = 4;

	/** The Constant PASSWORD_MAX_LENGTH. */
	public static final int PASSWORD_MAX_LENGTH = 100;

	/** The password. */
	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password;

	/**
	 * Instantiates a new managed user VM.
	 */
	public ManagedUserVM() {
		// Empty constructor needed for Jackson.
	}

	/**
	 * Instantiates a new managed user VM.
	 *
	 * @param id the id
	 * @param login the login
	 * @param password the password
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param email the email
	 * @param activated the activated
	 * @param imageUrl the image url
	 * @param langKey the lang key
	 * @param createdBy the created by
	 * @param createdDate the created date
	 * @param lastModifiedBy the last modified by
	 * @param lastModifiedDate the last modified date
	 * @param authorities the authorities
	 * @param grantAccessDate the grant access date
	 * @param revokeAccessDate the revoke access date
	 */    
	public ManagedUserVM(Long id, String login, String password, String firstName, String lastName, String email, boolean activated,
			String imageUrl, String langKey, String createdBy, Instant createdDate, String lastModifiedBy,
			Instant lastModifiedDate, Set<String> authorities, ZonedDateTime grantAccessDate,
			ZonedDateTime revokeAccessDate) {
		super(id, login, firstName, lastName, email, activated, imageUrl, langKey, createdBy, createdDate,
				lastModifiedBy, lastModifiedDate, authorities, grantAccessDate, revokeAccessDate);
		this.password = password;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/* (non-Javadoc)
	 * @see logia.quanlyso.service.dto.UserDTO#toString()
	 */
	@Override
	public String toString() {
		return "ManagedUserVM{" +
				"} " + super.toString();
	}
}
