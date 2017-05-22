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
 * A Channel.
 */
@Entity
@Table(name = "channel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "sunday")
    private Boolean sunday;

    @Column(name = "monday")
    private Boolean monday;

    @Column(name = "tuesday")
    private Boolean tuesday;

    @Column(name = "wednesday")
    private Boolean wednesday;

    @Column(name = "thursday")
    private Boolean thursday;

    @Column(name = "friday")
    private Boolean friday;

    @Column(name = "saturday")
    private Boolean saturday;

    @OneToMany(mappedBy = "channels")
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

    public Channel name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public Channel code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isSunday() {
        return sunday;
    }

    public Channel sunday(Boolean sunday) {
        this.sunday = sunday;
        return this;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public Boolean isMonday() {
        return monday;
    }

    public Channel monday(Boolean monday) {
        this.monday = monday;
        return this;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean isTuesday() {
        return tuesday;
    }

    public Channel tuesday(Boolean tuesday) {
        this.tuesday = tuesday;
        return this;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean isWednesday() {
        return wednesday;
    }

    public Channel wednesday(Boolean wednesday) {
        this.wednesday = wednesday;
        return this;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean isThursday() {
        return thursday;
    }

    public Channel thursday(Boolean thursday) {
        this.thursday = thursday;
        return this;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean isFriday() {
        return friday;
    }

    public Channel friday(Boolean friday) {
        this.friday = friday;
        return this;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean isSaturday() {
        return saturday;
    }

    public Channel saturday(Boolean saturday) {
        this.saturday = saturday;
        return this;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Set<TransactionDetails> getTransactionDetails() {
        return transactionDetails;
    }

    public Channel transactionDetails(Set<TransactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
        return this;
    }

    public Channel addTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails.add(transactionDetails);
        transactionDetails.setChannels(this);
        return this;
    }

    public Channel removeTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails.remove(transactionDetails);
        transactionDetails.setChannels(null);
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
        Channel channel = (Channel) o;
        if (channel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), channel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Channel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", sunday='" + isSunday() + "'" +
            ", monday='" + isMonday() + "'" +
            ", tuesday='" + isTuesday() + "'" +
            ", wednesday='" + isWednesday() + "'" +
            ", thursday='" + isThursday() + "'" +
            ", friday='" + isFriday() + "'" +
            ", saturday='" + isSaturday() + "'" +
            "}";
    }
}
