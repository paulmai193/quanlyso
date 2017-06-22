package logia.quanlyso.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Style.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "style")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Style implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name. */
    @Column(name = "name")
    private String name;

    /** The profit factors. */
    @OneToMany(mappedBy = "styles")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProfitFactor> profitFactors = new HashSet<>();

    /** The cost factors. */
    @OneToMany(mappedBy = "styles")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CostFactor> costFactors = new HashSet<>();

    /** The transaction details. */
    @OneToMany(mappedBy = "styles")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TransactionDetails> transactionDetails = new HashSet<>();

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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Name.
     *
     * @param name the name
     * @return the style
     */
    public Style name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the profit factors.
     *
     * @return the profit factors
     */
    public Set<ProfitFactor> getProfitFactors() {
        return profitFactors;
    }

    /**
     * Profit factors.
     *
     * @param profitFactors the profit factors
     * @return the style
     */
    public Style profitFactors(Set<ProfitFactor> profitFactors) {
        this.profitFactors = profitFactors;
        return this;
    }

    /**
     * Adds the profit factors.
     *
     * @param profitFactor the profit factor
     * @return the style
     */
    public Style addProfitFactors(ProfitFactor profitFactor) {
        this.profitFactors.add(profitFactor);
        profitFactor.setStyles(this);
        return this;
    }

    /**
     * Removes the profit factors.
     *
     * @param profitFactor the profit factor
     * @return the style
     */
    public Style removeProfitFactors(ProfitFactor profitFactor) {
        this.profitFactors.remove(profitFactor);
        profitFactor.setStyles(null);
        return this;
    }

    /**
     * Sets the profit factors.
     *
     * @param profitFactors the new profit factors
     */
    public void setProfitFactors(Set<ProfitFactor> profitFactors) {
        this.profitFactors = profitFactors;
    }

    /**
     * Gets the cost factors.
     *
     * @return the cost factors
     */
    public Set<CostFactor> getCostFactors() {
        return costFactors;
    }

    /**
     * Cost factors.
     *
     * @param costFactors the cost factors
     * @return the style
     */
    public Style costFactors(Set<CostFactor> costFactors) {
        this.costFactors = costFactors;
        return this;
    }

    /**
     * Adds the cost factors.
     *
     * @param costFactor the cost factor
     * @return the style
     */
    public Style addCostFactors(CostFactor costFactor) {
        this.costFactors.add(costFactor);
        costFactor.setStyles(this);
        return this;
    }

    /**
     * Removes the cost factors.
     *
     * @param costFactor the cost factor
     * @return the style
     */
    public Style removeCostFactors(CostFactor costFactor) {
        this.costFactors.remove(costFactor);
        costFactor.setStyles(null);
        return this;
    }

    /**
     * Sets the cost factors.
     *
     * @param costFactors the new cost factors
     */
    public void setCostFactors(Set<CostFactor> costFactors) {
        this.costFactors = costFactors;
    }

    /**
     * Gets the transaction details.
     *
     * @return the transaction details
     */
    public Set<TransactionDetails> getTransactionDetails() {
        return transactionDetails;
    }

    /**
     * Transaction details.
     *
     * @param transactionDetails the transaction details
     * @return the style
     */
    public Style transactionDetails(Set<TransactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
        return this;
    }

    /**
     * Adds the transaction details.
     *
     * @param transactionDetails the transaction details
     * @return the style
     */
    public Style addTransactionDetails(TransactionDetails transactionDetails) {
        return this.addTransactionDetails(transactionDetails, true);
    }
    
    /**
     * Adds the transaction details.
     *
     * @param transactionDetails the transaction details
     * @param isSetToTarget the is set to target detail
     * @return the style
     */
    Style addTransactionDetails(TransactionDetails transactionDetails, boolean isSetToTarget) {
        this.transactionDetails.add(transactionDetails);
        if (isSetToTarget) {
        	transactionDetails.setStyles(this, false);	
		}        
        return this;
    }

    /**
     * Removes the transaction details.
     *
     * @param transactionDetails the transaction details
     * @return the style
     */
    public Style removeTransactionDetails(TransactionDetails transactionDetails) {
        return this.removeTransactionDetails(transactionDetails, true);
    }
    
    /**
     * Removes the transaction details.
     *
     * @param transactionDetails the transaction details
     * @param isSetToTarget the is set to target detail
     * @return the style
     */
    Style removeTransactionDetails(TransactionDetails transactionDetails, boolean isSetToTarget) {
        this.transactionDetails.remove(transactionDetails);
        if (isSetToTarget) {
        	transactionDetails.setStyles(null, false);	
		}        
        return this;
    }

    /**
     * Sets the transaction details.
     *
     * @param transactionDetails the new transaction details
     */
    public void setTransactionDetails(Set<TransactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
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
        Style style = (Style) o;
        if (style.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), style.getId());
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
        return "Style{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
