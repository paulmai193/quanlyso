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

	/** The minRate. */
	private Float minRate;

	private Float maxRate;

	/** The styles id. */
	private Long	stylesId;

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
	 * Gets the minRate.
	 *
	 * @return the minRate
	 */
	public Float getMinRate() {
		return this.minRate;
	}

	/**
	 * Sets the minRate.
	 *
	 * @param minRate the new minRate
	 */
	public void setMinRate(Float minRate) {
		this.minRate = minRate;
	}

    public Float getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(Float maxRate) {
        this.maxRate = maxRate;
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

    @Override
    public String toString() {
        return "CostFactorDTO{" +
            "id=" + id +
            ", minRate=" + minRate +
            ", maxRate=" + maxRate +
            ", stylesId=" + stylesId +
            '}';
    }
}
