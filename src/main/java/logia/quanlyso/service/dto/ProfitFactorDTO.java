package logia.quanlyso.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ProfitFactor entity.
 */
public class ProfitFactorDTO implements Serializable {

    private Long id;

    private Float rate;

    private Long factorsId;

    private Long stylesId;

    private Long typesId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Long getFactorsId() {
        return factorsId;
    }

    public void setFactorsId(Long factorId) {
        this.factorsId = factorId;
    }

    public Long getStylesId() {
        return stylesId;
    }

    public void setStylesId(Long styleId) {
        this.stylesId = styleId;
    }

    public Long getTypesId() {
        return typesId;
    }

    public void setTypesId(Long typesId) {
        this.typesId = typesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfitFactorDTO profitFactorDTO = (ProfitFactorDTO) o;
        if(profitFactorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profitFactorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfitFactorDTO{" +
            "id=" + getId() +
            ", rate='" + getRate() + "'" +
            "}";
    }
}
