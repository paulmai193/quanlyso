package logia.quanlyso.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProfitFactor.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "profit_factor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProfitFactor implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The rate. */
    @Column(name = "rate")
    private Float rate;

    /** The factors. */
    @ManyToOne
    private Factor factors;

    /** The styles. */
    @ManyToOne
    private Style styles;

    /** The types. */
    @ManyToOne
    private Types types;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
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
        return rate;
    }

    /**
     * Rate.
     *
     * @param rate the rate
     * @return the profit factor
     */
    public ProfitFactor rate(Float rate) {
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
     * Gets the factors.
     *
     * @return the factors
     */
    public Factor getFactors() {
        return factors;
    }

    /**
     * Factors.
     *
     * @param factor the factor
     * @return the profit factor
     */
    public ProfitFactor factors(Factor factor) {
        this.factors = factor;
        return this;
    }

    /**
     * Sets the factors.
     *
     * @param factor the new factors
     */
    public void setFactors(Factor factor) {
        this.factors = factor;
    }

    /**
     * Gets the styles.
     *
     * @return the styles
     */
    public Style getStyles() {
        return styles;
    }

    /**
     * Styles.
     *
     * @param style the style
     * @return the profit factor
     */
    public ProfitFactor styles(Style style) {
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
        return types;
    }

    /**
     * Types.
     *
     * @param types the types
     * @return the profit factor
     */
    public ProfitFactor types(Types types) {
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

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProfitFactor profitFactor = (ProfitFactor) o;
        if (profitFactor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profitFactor.getId());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProfitFactor{" +
            "id=" + getId() +
            ", rate='" + getRate() + "'" +
            "}";
    }
}
