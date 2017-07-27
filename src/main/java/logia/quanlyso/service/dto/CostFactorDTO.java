package logia.quanlyso.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CostFactor entity.
 *
 * @author Dai Mai
 */
public class CostFactorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long	id;

	/** The rate. */
	private Float	rate;

	/** The factors id. */
	private Long	factorsId;

	/** The styles id. */
	private Long	stylesId;

	/** The types id. */
	private Long	typesId;

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
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the rate.
	 *
	 * @return the rate
	 */
	public Float getRate() {
		return this.rate;
	}

	/**
	 * Sets the rate.
	 *
	 * @param rate the new rate
	 */
	public void setRate(Float rate) {
		this.rate = rate;
	}

	/**
	 * Gets the factors id.
	 *
	 * @return the factors id
	 */
	public Long getFactorsId() {
		return this.factorsId;
	}

	/**
	 * Sets the factors id.
	 *
	 * @param factorId the new factors id
	 */
	public void setFactorsId(Long factorId) {
		this.factorsId = factorId;
	}

	/**
	 * Gets the styles id.
	 *
	 * @return the styles id
	 */
	public Long getStylesId() {
		return this.stylesId;
	}

	/**
	 * Sets the styles id.
	 *
	 * @param styleId the new styles id
	 */
	public void setStylesId(Long styleId) {
		this.stylesId = styleId;
	}

	/**
	 * Gets the types id.
	 *
	 * @return the types id
	 */
	public Long getTypesId() {
		return this.typesId;
	}

	/**
	 * Sets the types id.
	 *
	 * @param typesId the new types id
	 */
	public void setTypesId(Long typesId) {
		this.typesId = typesId;
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

		CostFactorDTO costFactorDTO = (CostFactorDTO) o;
		if (costFactorDTO.getId() == null || this.getId() == null) {
			return false;
		}
		return Objects.equals(this.getId(), costFactorDTO.getId());
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
		return "CostFactorDTO{" + "id=" + this.getId() + ", rate='" + this.getRate() + "'" + "}";
	}
}
