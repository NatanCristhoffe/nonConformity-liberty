package blessed.nonconformity.service;


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

    public List<NonconformityResponseDTO> getAll(){
        List<NonconformityResponseDTO> nonconformities = nonconformityRepository
                .findAll()
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();

        return nonconformities;
    }

    @Transactional
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

        NonConformity nc = new NonConformity(data);

        nc.setSourceDepartment(source);
        nc.setResponsibleDepartment(responsible);
        nc.setDispositionOwner(dispositionOwner);
        nc.setEffectivenessAnalyst(effectivenessAnalyst);
        nc.setCreatedBy(createdBy);

        nc.setRequiresQualityTool(data.requiresQualityTool());
        nc.setSelectedTool(data.selectedTool());

        if (nc.getRequiresQualityTool() == true){
            nc.setStatus(NonConformityStatus.WAITING_QUALITY_TOOL);
        } else {
            nc.setStatus(NonConformityStatus.WAITING_ROOT_CAUSE);
        }
        nonconformityRepository.save(nc);
        qualityToolService.initializeTool(nc);

        return nc;
    }


    public List<NonconformityResponseDTO> findByTitleStartingWithIgnoreCase(String title) {

        List<NonConformity> nonConformities = nonconformityRepository
                .findTop5ByTitleStartingWithIgnoreCase(title);

        List<NonconformityResponseDTO> responseNonConformities = nonConformities
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();

        return responseNonConformities;
    }


}
