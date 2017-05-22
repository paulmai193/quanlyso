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
 * A Types.
 */
@Entity
@Table(name = "types")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Types implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "types")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProfitFactor> profitFactors = new HashSet<>();

    @OneToMany(mappedBy = "types")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CostFactor> costFactors = new HashSet<>();

    @OneToMany(mappedBy = "types")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TransactionDetails> transactionDetails = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Types name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProfitFactor> getProfitFactors() {
        return profitFactors;
    }

    public Types profitFactors(Set<ProfitFactor> profitFactors) {
        this.profitFactors = profitFactors;
        return this;
    }

    public Types addProfitFactors(ProfitFactor profitFactor) {
        this.profitFactors.add(profitFactor);
        profitFactor.setTypes(this);
        return this;
    }

    public Types removeProfitFactors(ProfitFactor profitFactor) {
        this.profitFactors.remove(profitFactor);
        profitFactor.setTypes(null);
        return this;
    }

    public void setProfitFactors(Set<ProfitFactor> profitFactors) {
        this.profitFactors = profitFactors;
    }

    public Set<CostFactor> getCostFactors() {
        return costFactors;
    }

    public Types costFactors(Set<CostFactor> costFactors) {
        this.costFactors = costFactors;
        return this;
    }

    public Types addCostFactors(CostFactor costFactor) {
        this.costFactors.add(costFactor);
        costFactor.setTypes(this);
        return this;
    }

    public Types removeCostFactors(CostFactor costFactor) {
        this.costFactors.remove(costFactor);
        costFactor.setTypes(null);
        return this;
    }

    public void setCostFactors(Set<CostFactor> costFactors) {
        this.costFactors = costFactors;
    }

    public Set<TransactionDetails> getTransactionDetails() {
        return transactionDetails;
    }

    public Types transactionDetails(Set<TransactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
        return this;
    }

    public Types addTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails.add(transactionDetails);
        transactionDetails.setTypes(this);
        return this;
    }

    public Types removeTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails.remove(transactionDetails);
        transactionDetails.setTypes(null);
        return this;
    }

    public void setTransactionDetails(Set<TransactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Types types = (Types) o;
        if (types.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), types.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Types{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
