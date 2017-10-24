/*
 * 
 */
package logia.quanlyso.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Channel entity.
 *
 * @author Dai Mai
 */
public class ChannelDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;

	/** The name. */
	private String name;

	/** The code. */
	private String code;

	/** The sunday. */
	private Boolean sunday;

	/** The monday. */
	private Boolean monday;

	/** The tuesday. */
	private Boolean tuesday;

	/** The wednesday. */
	private Boolean wednesday;

	/** The thursday. */
	private Boolean thursday;

	/** The friday. */
	private Boolean friday;

	/** The saturday. */
	private Boolean saturday;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Checks if is sunday.
	 *
	 * @return the boolean
	 */
	public Boolean isSunday() {
		return this.sunday;
	}

	/**
	 * Sets the sunday.
	 *
	 * @param sunday
	 *            the new sunday
	 */
	public void setSunday(Boolean sunday) {
		this.sunday = sunday;
	}

	/**
	 * Checks if is monday.
	 *
	 * @return the boolean
	 */
	public Boolean isMonday() {
		return this.monday;
	}

	/**
	 * Sets the monday.
	 *
	 * @param monday
	 *            the new monday
	 */
	public void setMonday(Boolean monday) {
		this.monday = monday;
	}

	/**
	 * Checks if is tuesday.
	 *
	 * @return the boolean
	 */
	public Boolean isTuesday() {
		return this.tuesday;
	}

	/**
	 * Sets the tuesday.
	 *
	 * @param tuesday
	 *            the new tuesday
	 */
	public void setTuesday(Boolean tuesday) {
		this.tuesday = tuesday;
	}

	/**
	 * Checks if is wednesday.
	 *
	 * @return the boolean
	 */
	public Boolean isWednesday() {
		return this.wednesday;
	}

	/**
	 * Sets the wednesday.
	 *
	 * @param wednesday
	 *            the new wednesday
	 */
	public void setWednesday(Boolean wednesday) {
		this.wednesday = wednesday;
	}

	/**
	 * Checks if is thursday.
	 *
	 * @return the boolean
	 */
	public Boolean isThursday() {
		return this.thursday;
	}

	/**
	 * Sets the thursday.
	 *
	 * @param thursday
	 *            the new thursday
	 */
	public void setThursday(Boolean thursday) {
		this.thursday = thursday;
	}

	/**
	 * Checks if is friday.
	 *
	 * @return the boolean
	 */
	public Boolean isFriday() {
		return this.friday;
	}

	/**
	 * Sets the friday.
	 *
	 * @param friday
	 *            the new friday
	 */
	public void setFriday(Boolean friday) {
		this.friday = friday;
	}

	/**
	 * Checks if is saturday.
	 *
	 * @return the boolean
	 */
	public Boolean isSaturday() {
		return this.saturday;
	}

	/**
	 * Sets the saturday.
	 *
	 * @param saturday
	 *            the new saturday
	 */
	public void setSaturday(Boolean saturday) {
		this.saturday = saturday;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		ChannelDTO channelDTO = (ChannelDTO) o;
		if (channelDTO.getId() == null || this.getId() == null) {
			return false;
		}
		return Objects.equals(this.getId(), channelDTO.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ChannelDTO{" + "id=" + this.getId() + ", name='" + this.getName() + "'" + ", code='" + this.getCode()
				+ "'" + ", sunday='" + this.isSunday() + "'" + ", monday='" + this.isMonday() + "'" + ", tuesday='"
				+ this.isTuesday() + "'" + ", wednesday='" + this.isWednesday() + "'" + ", thursday='"
				+ this.isThursday() + "'" + ", friday='" + this.isFriday() + "'" + ", saturday='" + this.isSaturday()
				+ "'" + "}";
	}
}
