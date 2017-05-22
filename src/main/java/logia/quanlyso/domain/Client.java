package logia.quanlyso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grant_access_date")
    private ZonedDateTime grantAccessDate;

    @Column(name = "revoke_access_date")
    private ZonedDateTime revokeAccessDate;
    
//    @OneToOne
//    @MapsId
//    private User user;

    @OneToMany(mappedBy = "clients")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transactions> transactionsses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getGrantAccessDate() {
        return grantAccessDate;
    }

    public Client grantAccessDate(ZonedDateTime grantAccessDate) {
        this.grantAccessDate = grantAccessDate;
        return this;
    }

    public void setGrantAccessDate(ZonedDateTime grantAccessDate) {
        this.grantAccessDate = grantAccessDate;
    }

    public ZonedDateTime getRevokeAccessDate() {
        return revokeAccessDate;
    }

    public Client revokeAccessDate(ZonedDateTime revokeAccessDate) {
        this.revokeAccessDate = revokeAccessDate;
        return this;
    }

    public void setRevokeAccessDate(ZonedDateTime revokeAccessDate) {
        this.revokeAccessDate = revokeAccessDate;
    }

    public Set<Transactions> getTransactionsses() {
        return transactionsses;
    }

    public Client transactionsses(Set<Transactions> transactions) {
        this.transactionsses = transactions;
        return this;
    }

    public Client addTransactionss(Transactions transactions) {
        this.transactionsses.add(transactions);
        transactions.setClients(this);
        return this;
    }

    public Client removeTransactionss(Transactions transactions) {
        this.transactionsses.remove(transactions);
        transactions.setClients(null);
        return this;
    }

    public void setTransactionsses(Set<Transactions> transactions) {
        this.transactionsses = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        if (client.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", grantAccessDate='" + getGrantAccessDate() + "'" +
            ", revokeAccessDate='" + getRevokeAccessDate() + "'" +
            "}";
    }
}
