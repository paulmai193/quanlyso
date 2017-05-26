package logia.quanlyso.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Style entity.
 *
 * @author Dai Mai
 */
public class StyleDTO implements Serializable {

    /** The id. */
    private Long id;

    /** The name. */
    private String name;

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
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
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

        StyleDTO styleDTO = (StyleDTO) o;
        if(styleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), styleDTO.getId());
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
        return "StyleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
