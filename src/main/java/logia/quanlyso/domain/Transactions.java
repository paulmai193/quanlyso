package logia.quanlyso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Transactions.
 */
@Entity
@Table(name = "transactions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transactions extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chosen_number")
    private Integer chosenNumber;

    @Column(name = "net_value")
    private Float netValue;

    @OneToMany(mappedBy = "transactions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TransactionDetails> transactionDetails = new HashSet<>();

    @ManyToOne
    private Client clients;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getChosenNumber() {
        return chosenNumber;
    }

    public Transactions chosenNumber(Integer chosenNumber) {
        this.chosenNumber = chosenNumber;
        return this;
    }

    public void setChosenNumber(Integer chosenNumber) {
        this.chosenNumber = chosenNumber;
    }

    public Float getNetValue() {
        return netValue;
    }

    public Transactions netValue(Float netValue) {
        this.netValue = netValue;
        return this;
    }

    public void setNetValue(Float netValue) {
        this.netValue = netValue;
    }

    public Set<TransactionDetails> getTransactionDetails() {
        return transactionDetails;
    }

    public Transactions transactionDetails(Set<TransactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
        return this;
    }

    public Transactions addTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails.add(transactionDetails);
        transactionDetails.setTransactions(this);
        return this;
    }

    public Transactions removeTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails.remove(transactionDetails);
        transactionDetails.setTransactions(null);
        return this;
    }

    public void setTransactionDetails(Set<TransactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public Client getClients() {
        return clients;
    }

    public Transactions clients(Client client) {
        this.clients = client;
        return this;
    }

    public void setClients(Client client) {
        this.clients = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transactions transactions = (Transactions) o;
        if (transactions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transactions{" +
            "id=" + getId() +
            ", chosenNumber='" + getChosenNumber() + "'" +
            ", netValue='" + getNetValue() + "'" +
            "}";
    }
}
