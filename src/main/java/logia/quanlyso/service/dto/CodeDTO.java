package logia.quanlyso.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the Code entity.
 */
public class CodeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long			id;

	private String			code;

	private ZonedDateTime	openDate;

	private Long			channelsId;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ZonedDateTime getOpenDate() {
		return this.openDate;
	}

	public void setOpenDate(ZonedDateTime openDate) {
		this.openDate = openDate;
	}

	public Long getChannelsId() {
		return this.channelsId;
	}

	public void setChannelsId(Long channelId) {
		this.channelsId = channelId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		CodeDTO codeDTO = (CodeDTO) o;
		if (codeDTO.getId() == null || this.getId() == null) {
			return false;
		}
		return Objects.equals(this.getId(), codeDTO.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getId());
	}

	@Override
	public String toString() {
		return "CodeDTO{" + "id=" + this.getId() + ", code='" + this.getCode() + "'" + ", openDate='"
				+ this.getOpenDate() + "'" + "}";
	}
}
