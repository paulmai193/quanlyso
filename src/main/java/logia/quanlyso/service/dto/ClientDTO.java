package logia.quanlyso.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Client entity.
 */
public class ClientDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

    private ZonedDateTime grantAccessDate;

    private ZonedDateTime revokeAccessDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getGrantAccessDate() {
        return grantAccessDate;
    }

    public void setGrantAccessDate(ZonedDateTime grantAccessDate) {
        this.grantAccessDate = grantAccessDate;
    }

    public ZonedDateTime getRevokeAccessDate() {
        return revokeAccessDate;
    }

    public void setRevokeAccessDate(ZonedDateTime revokeAccessDate) {
        this.revokeAccessDate = revokeAccessDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if(clientDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clientDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", grantAccessDate='" + getGrantAccessDate() + "'" +
            ", revokeAccessDate='" + getRevokeAccessDate() + "'" +
            "}";
    }
}
