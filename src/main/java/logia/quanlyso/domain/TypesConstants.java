package logia.quanlyso.domain;

/**
 * The Enum TypesConstants.
 *
 * @author Dai Mai
 */
public enum TypesConstants {

	/** The top. */
	TOP(1),
	/** The bottom. */
	BOTTOM(2),
	/** The both. */
	BOTH(3),
	/** The roll. */
	ROLL(4);

	/** The types id. */
	long typesId;

	/**
	 * Instantiates a new types constants.
	 *
	 * @param id the id
	 */
	TypesConstants(long id) {
		this.typesId = id;
	}

	/**
	 * Gets the types id.
	 *
	 * @return the types id
	 */
	public long getId() {
		return this.typesId;
	}
}
