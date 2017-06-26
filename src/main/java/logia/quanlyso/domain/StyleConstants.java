package logia.quanlyso.domain;

/**
 * The Enum StyleConstants.
 *
 * @author Dai Mai
 */
public enum StyleConstants {
	
	/** The two num. */
	TWO_NUM(1), 
	/** The three num. */
	THREE_NUM(2), 
	/** The four num. */
	FOUR_NUM(3);
	
	/** The style id. */
	long styleId;
	
	/**
	 * Instantiates a new style constants.
	 *
	 * @param id the id
	 */
	StyleConstants(long id) {
		this.styleId = id;
	}
	
	/**
	 * Gets the style id.
	 *
	 * @return the style id
	 */
	public Long getId() {
		return styleId;
	}
}
