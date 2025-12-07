package com.example.ClinicaDefinitiva.infrastructure.adapters;

import com.example.ClinicaDefinitiva.domain.administration.accounting.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.ContractRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.ContractEntity;
import com.example.ClinicaDefinitiva.infrastructure.repository.ContractJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hibernate.internal.util.BytesHelper.asLong;
import static org.hibernate.internal.util.BytesHelper.fromLong;

// Adaptador que implementa ContractRepository del dominio
public class ContractRepositoryAdapter implements ContractRepository {

    private final ContractJpaRepository jpa;

    public ContractRepositoryAdapter(ContractJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Contract> findById(ContractId contractId) {
        Long id = Long.valueOf(contractId.getValue()); // o parse según tu VO
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public List<Contract> findAll() {
        return jpa.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

  //  @Override
  //  public Contract Update(ContractId id, Contract contract) {
  //      return null;
  //  }

    @Override
    public Contract update(ContractId contractId, Contract contract) {
        Long id = Long.valueOf(contractId.getValue());
        ContractEntity entity = toEntity(contract);
        entity.setId(id);
        ContractEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(ContractId contractId) {
        Long id = Long.valueOf(contractId.getValue());
        jpa.deleteById(id);
    }

// toDomain, toEntity: mapeadores entre ContractEntity y Contract
/**toDomain es un método auxiliar del adaptador que mapea una entidad de
  persistencia (ContractEntity) al agregado/objeto de dominio (Contract)
  Su contraparte es toEntity que mapea el dominio a la entidad para guardar en
  la BD. Ambos métodos pertenecen al adapter (infraestructura) y no al repositorio de dominio*/
    // ---------- Mapping helpers ----------
    private Contract toDomain(ContractEntity e) {
        // mapear ContractEntity -> Contract (dominio)
        // convierte Long -> ContractId VO
        ContractId cid = e.getId() == null ? null : ContractId.fromLong(e.getId());
        return new Contract(
                cid,                         // ahora pasa el VO al constructor del dominio
                e.getCoverageRate(),
                e.getCoverageType(),
                e.getDescription(),
                e.getEndDate(),
                e.getName(),
                e.getOrigin(),
                e.getStartDate(),
                ContractStatus.valueOf(e.getStatus()) // asume stored name() del enum
        );
    }

    private ContractEntity toEntity(Contract c) {
        ContractEntity e = new ContractEntity();
        // Si Contract contiene Long contractId expuesto, úsalo; si es VO, conviértelo
       // e.setId(c.getContractId());
        // si tu Contract expone ContractId VO
        ContractId cid = c.getContractId(); // VO en el dominio
        e.setId(cid == null ? null : cid.asLong()); // conversión VO -> Long


        e.setName(c.getName());
        e.setDescription(c.getDescription());
        e.setOrigin(c.getOrigin());
        e.setStartDate(c.getStartDate());
        e.setEndDate(c.getEndDate());
        e.setCoverageType(c.getCoverageType());
        e.setCoverageRate(c.getCoverageRate());
        e.setStatus(c.getStatus().name());
        return e;
    }
}



