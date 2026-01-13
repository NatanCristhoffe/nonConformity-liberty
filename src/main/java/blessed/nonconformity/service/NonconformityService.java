package blessed.nonconformity.service;


import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.nonConformity.NonConformity;
import blessed.nonconformity.nonConformity.NonconformityRepository;
import blessed.nonconformity.nonConformity.NonconformityRequestDTO;
import blessed.nonconformity.nonConformity.NonconformityResponseDTO;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NonconformityService {
    private final NonconformityRepository nonconformityRepository;
    private final  SectorRepository sectorRepository;

    public NonconformityService(
        NonconformityRepository nonconformityRepository,
        SectorRepository sectorRepository
    ){
        this.nonconformityRepository = nonconformityRepository;
        this.sectorRepository = sectorRepository;

    }

    public List<NonconformityResponseDTO> getAll(){
        List<NonconformityResponseDTO> nonconformities = nonconformityRepository
                .findAll()
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();

        return nonconformities;
    }

    public NonConformity create(NonconformityRequestDTO data){

        Sector source = sectorRepository.findById(data.sourceDepartmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Departamento de origem não encontrado"));

        Sector responsible = sectorRepository.findById(data.responsibleDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Departamento de origem não encontrado"));

        NonConformity nonconformity = new NonConformity(data);
        nonconformity.setSourceDepartment(source);
        nonconformity.setResponsibleDepartment(responsible);
        nonconformityRepository.save(nonconformity);
        return nonconformity;
    }
}
