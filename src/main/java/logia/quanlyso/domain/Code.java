package logia.quanlyso.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Code.
 */
@Entity
@Table(name = "code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Code implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private Integer code;

    @Column(name = "open_date")
    private ZonedDateTime openDate;

    @ManyToOne
    private Channel channels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public Code code(Integer code) {
        this.code = code;
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ZonedDateTime getOpenDate() {
        return openDate;
    }

    public Code openDate(ZonedDateTime openDate) {
        this.openDate = openDate;
        return this;
    }

    public void setOpenDate(ZonedDateTime openDate) {
        this.openDate = openDate;
    }

    public Channel getChannels() {
        return channels;
    }

    public Code channels(Channel channel) {
        this.channels = channel;
        return this;
    }

    public void setChannels(Channel channel) {
        this.channels = channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Code code = (Code) o;
        if (code.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), code.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Code{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", openDate='" + getOpenDate() + "'" +
            "}";
    }
}
