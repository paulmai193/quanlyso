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
 * A TransactionDetails.
 */
@Entity
@Table(name = "transaction_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "profit")
    private Float profit;

    @Column(name = "costs")
    private Float costs;

    @ManyToOne
    private Transactions transactions;

    @ManyToOne
    private Channel channels;

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

    public Float getAmount() {
        return amount;
    }

    public TransactionDetails amount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getProfit() {
        return profit;
    }

    public TransactionDetails profit(Float profit) {
        this.profit = profit;
        return this;
    }

    public void setProfit(Float profit) {
        this.profit = profit;
    }

    public Float getCosts() {
        return costs;
    }

    public TransactionDetails costs(Float costs) {
        this.costs = costs;
        return this;
    }

    public void setCosts(Float costs) {
        this.costs = costs;
    }

    public Transactions getTransactions() {
        return transactions;
    }

    public TransactionDetails transactions(Transactions transactions) {
        this.transactions = transactions;
        return this;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    public Channel getChannels() {
        return channels;
    }

    public TransactionDetails channels(Channel channel) {
        this.channels = channel;
        return this;
    }

    public void setChannels(Channel channel) {
        this.channels = channel;
    }

    public Factor getFactors() {
        return factors;
    }

    public TransactionDetails factors(Factor factor) {
        this.factors = factor;
        return this;
    }

    public void setFactors(Factor factor) {
        this.factors = factor;
    }

    public Style getStyles() {
        return styles;
    }

    public TransactionDetails styles(Style style) {
        this.styles = style;
        return this;
    }

    public void setStyles(Style style) {
        this.styles = style;
    }

    public Types getTypes() {
        return types;
    }

    public TransactionDetails types(Types types) {
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
        TransactionDetails transactionDetails = (TransactionDetails) o;
        if (transactionDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionDetails{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", profit='" + getProfit() + "'" +
            ", costs='" + getCosts() + "'" +
            "}";
    }
}
