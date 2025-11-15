package com.example.ClinicaDefinitiva.infrastructure.persistence.providedService;
import jakarta.persistence.*;

@Entity
@Table(name = "provided_service_aesthetic")
public class AestheticDetailsEntity {

    @Id
    @Column(name = "provided_service_id", length = 36)
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provided_service_id")
    private ProvidedServiceEntity providedService;

    private String aestheticType;
    private String materialUsed;
    private String expectedResult;

    public AestheticDetailsEntity() {}

    public AestheticDetailsEntity(String aestheticType, String materialUsed, String expectedResult) {
        this.aestheticType = aestheticType;
        this.materialUsed = materialUsed;
        this.expectedResult = expectedResult;
    }

    public void setProvidedService(ProvidedServiceEntity providedService) {
        this.providedService = providedService;
        this.id = providedService.getId();
    }

    // getters/setters...

    public String getAestheticType() {
        return aestheticType;
    }

    public void setAestheticType(String aestheticType) {
        this.aestheticType = aestheticType;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialUsed() {
        return materialUsed;
    }

    public void setMaterialUsed(String materialUsed) {
        this.materialUsed = materialUsed;
    }

    public ProvidedServiceEntity getProvidedService() {
        return providedService;
    }
}
