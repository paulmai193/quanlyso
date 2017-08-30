package logia.quanlyso.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CostFactor.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "cost_factor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CostFactor implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long				id;

	/** The rate. */
	@Column(name = "rate")
	private Float				rate;

	/** The styles. */
	@ManyToOne
	private Style				styles;

	/** The types. */
	@ManyToOne
	private Types				types;

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
	 * Rate.
	 *
	 * @param rate the rate
	 * @return the cost factor
	 */
	public CostFactor rate(Float rate) {
		this.rate = rate;
		return this;
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
	 * Gets the styles.
	 *
	 * @return the styles
	 */
	public Style getStyles() {
		return this.styles;
	}

	/**
	 * Styles.
	 *
	 * @param style the style
	 * @return the cost factor
	 */
	public CostFactor styles(Style style) {
		this.styles = style;
		return this;
	}

	/**
	 * Sets the styles.
	 *
	 * @param style the new styles
	 */
	public void setStyles(Style style) {
		this.styles = style;
	}

	/**
	 * Gets the types.
	 *
	 * @return the types
	 */
	public Types getTypes() {
		return this.types;
	}

	/**
	 * Types.
	 *
	 * @param types the types
	 * @return the cost factor
	 */
	public CostFactor types(Types types) {
		this.types = types;
		return this;
	}

	/**
	 * Sets the types.
	 *
	 * @param types the new types
	 */
	public void setTypes(Types types) {
		this.types = types;
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
		CostFactor costFactor = (CostFactor) o;
		if (costFactor.getId() == null || this.getId() == null) {
			return false;
		}
		return Objects.equals(this.getId(), costFactor.getId());
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
		return "CostFactor{" + "id=" + this.getId() + ", rate='" + this.getRate() + "'" + "}";
	}
}
