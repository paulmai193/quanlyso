package logia.quanlyso.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Types entity.
 *
 * @author Dai Mai
 */
public class TypesDTO implements Serializable {

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

        TypesDTO typesDTO = (TypesDTO) o;
        if(typesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typesDTO.getId());
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
        return "TypesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
