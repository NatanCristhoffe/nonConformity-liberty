package blessed.NonConformity.service;


import blessed.NonConformity.exception.ResourceNotFoundException;
import blessed.NonConformity.nonConformity.NonConformity;
import blessed.NonConformity.nonConformity.NonconformityRepository;
import blessed.NonConformity.nonConformity.NonconformityRequestDTO;
import blessed.NonConformity.nonConformity.NonconformityResponseDTO;
import blessed.NonConformity.sectors.Sector;
import blessed.NonConformity.sectors.SectorRepository;
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
