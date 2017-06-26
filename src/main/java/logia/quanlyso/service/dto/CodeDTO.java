package logia.quanlyso.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Code entity.
 */
public class CodeDTO implements Serializable {

    private Long id;

    private Integer code;

    private ZonedDateTime openDate;

    private Long channelsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ZonedDateTime getOpenDate() {
        return openDate;
    }

    public void setOpenDate(ZonedDateTime openDate) {
        this.openDate = openDate;
    }

    public Long getChannelsId() {
        return channelsId;
    }

    public void setChannelsId(Long channelId) {
        this.channelsId = channelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CodeDTO codeDTO = (CodeDTO) o;
        if(codeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), codeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CodeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", openDate='" + getOpenDate() + "'" +
            "}";
    }
}
