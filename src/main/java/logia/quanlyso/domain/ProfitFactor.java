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
 */
@Entity
@Table(name = "profit_factor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProfitFactor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate")
    private Float rate;

    @ManyToOne
    private Factor factors;

    @ManyToOne
    private Style styles;

    @ManyToOne
    private Types types;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRate() {
        return rate;
    }

    public ProfitFactor rate(Float rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Factor getFactors() {
        return factors;
    }

    public ProfitFactor factors(Factor factor) {
        this.factors = factor;
        return this;
    }

    public void setFactors(Factor factor) {
        this.factors = factor;
    }

    public Style getStyles() {
        return styles;
    }

    public ProfitFactor styles(Style style) {
        this.styles = style;
        return this;
    }

    public void setStyles(Style style) {
        this.styles = style;
    }

    public Types getTypes() {
        return types;
    }

    public ProfitFactor types(Types types) {
        this.types = types;
        return this;
    }

    public void setTypes(Types types) {
        this.types = types;
    }

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

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfitFactor{" +
            "id=" + getId() +
            ", rate='" + getRate() + "'" +
            "}";
    }
}
