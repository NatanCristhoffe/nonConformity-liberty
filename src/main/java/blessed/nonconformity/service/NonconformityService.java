package blessed.nonconformity.service;


import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.interfaces.QualityToolService;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class NonconformityService {
    private final NonconformityRepository nonconformityRepository;
    private final  SectorRepository sectorRepository;
    private final UserRepository userRepository;
    private final QualityToolServiceImpl qualityToolService;

    public NonconformityService(
        NonconformityRepository nonconformityRepository,
        SectorRepository sectorRepository,
        UserRepository userRepository,
        QualityToolServiceImpl qualityToolService){
        this.nonconformityRepository = nonconformityRepository;
        this.sectorRepository = sectorRepository;
        this.userRepository = userRepository;
        this.qualityToolService = qualityToolService;
    }

    public NonconformityResponseDTO getNcById(Long ncId){
        NonConformity nonConformity = nonconformityRepository.findById(ncId)
                .orElseThrow(() -> new BusinessException("Não conformidade não encontrada."));
        return new NonconformityResponseDTO(nonConformity);
    }

    public List<NonconformityResponseDTO> getAll(){
        List<NonconformityResponseDTO> nonconformities = nonconformityRepository
                .findAll()
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();

        return nonconformities;
    }

    @Transactional
    public NonConformity create(NonconformityRequestDTO data, User createdByNc){

        Sector source = sectorRepository.findById(data.sourceDepartmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Departamento de origem não encontrado"));

        User createBy = userRepository.findById(createdByNc.getId())
                .orElseThrow(() -> new BusinessException("Usuário de criação não encontrado."));


        Sector responsible = sectorRepository.findById(data.responsibleDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Departamento de origem não encontrado"));

        User dispositionOwner = userRepository.findById(data.dispositionOwnerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário responsável pela disposição não encontrado"));

        User effectivenessAnalyst = userRepository.findById(data.effectivenessAnalystId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário responsável pela análise de eficácia não encontrado"));

        NonConformity nc = new NonConformity(data);

        nc.setSourceDepartment(source);
        nc.setResponsibleDepartment(responsible);
        nc.setDispositionOwner(dispositionOwner);
        nc.setEffectivenessAnalyst(effectivenessAnalyst);

        nc.setRequiresQualityTool(data.requiresQualityTool());
        nc.setSelectedTool(data.selectedTool());
        nc.setCreatedBy(createBy);

        nc.setStatus(NonConformityStatus.PENDING);
        nonconformityRepository.save(nc);
        qualityToolService.initializeTool(nc);

        return nc;
    }

    @Transactional
    public void approve(Long id, User user){
        NonConformity nonConformity = nonconformityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não conformidade não encontrada."));

        nonConformity.approvedNonConformity(user);
    }


    public List<NonconformityResponseDTO> findByTitleStartingWithIgnoreCase(String title) {

        return nonconformityRepository
                .findTop5ByTitleStartingWithIgnoreCase(title)
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();
    }

    public List<NonconformityResponseDTO> getAllNonConformityPending(){
        List<NonconformityResponseDTO> nonConformitiesPending = nonconformityRepository.findAllByStatus(NonConformityStatus.PENDING)
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();

        return nonConformitiesPending;
    }


}
