package blessed.nonconformity.service;


import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NonconformityService {
    private final NonconformityRepository nonconformityRepository;
    private final  SectorRepository sectorRepository;
    private final UserRepository userRepository;

    public NonconformityService(
        NonconformityRepository nonconformityRepository,
        SectorRepository sectorRepository,
        UserRepository userRepository){
        this.nonconformityRepository = nonconformityRepository;
        this.sectorRepository = sectorRepository;
        this.userRepository = userRepository;
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

        User dispositionOwner = userRepository.findById(data.dispositionOwnerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário responsável pela disposição não encontrado"));

        User effectivenessAnalyst = userRepository.findById(data.effectivenessAnalystId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário responsável pela análise de eficácia não encontrado"));

        User createdBy = userRepository.findById(data.createdById())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário responsável pela criação não encontrado"));

        NonConformity nonconformity = new NonConformity(data);

        nonconformity.setSourceDepartment(source);
        nonconformity.setResponsibleDepartment(responsible);
        nonconformity.setDispositionOwner(dispositionOwner);
        nonconformity.setEffectivenessAnalyst(effectivenessAnalyst);
        nonconformity.setCreatedBy(createdBy);
        nonconformityRepository.save(nonconformity);

        return nonconformity;
    }
}
