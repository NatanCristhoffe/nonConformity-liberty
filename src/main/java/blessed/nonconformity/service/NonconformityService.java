package blessed.nonconformity.service;

import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.nonconformity.service.query.SectorQuery;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class NonconformityService {

    private final QualityToolService qualityToolService;
    private final NonConformityQuery nonConformityQuery;
    private final UserQuery userQuery;
    private final SectorQuery sectorQuery;

    public NonconformityService(
            QualityToolService qualityToolService,
            NonConformityQuery nonConformityQuery,
            UserQuery userQuery,
            SectorQuery sectorQuery
    ) {
        this.qualityToolService = qualityToolService;
        this.nonConformityQuery = nonConformityQuery;
        this.userQuery = userQuery;
        this.sectorQuery = sectorQuery;
    }


    public NonconformityResponseDTO getNcById(Long ncId, boolean includeAll){

        NonConformity nonConformity = includeAll
                ? nonConformityQuery.byIdWithAll(ncId)
                : nonConformityQuery.byId(ncId);

        return new NonconformityResponseDTO(nonConformity, includeAll);
    }

    public List<NonconformityResponseDTO> getAll(){
        return nonConformityQuery.getAll()
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();
    }

    @Transactional
    public NonConformity create(NonconformityRequestDTO data, User createdByNc){
        Sector source = sectorQuery.byId(data.sourceDepartmentId());
        Sector responsibleDepartment = sectorQuery.byId(data.responsibleDepartmentId());

        User createBy = userQuery.byId(createdByNc.getId());
        User dispositionOwner = userQuery.byId(data.dispositionOwnerId());
        User effectivenessAnalyst = userQuery.byId(data.effectivenessAnalystId());

        NonConformity nc = new NonConformity(
                data, source, responsibleDepartment, createBy,
                dispositionOwner,effectivenessAnalyst
        );
        nonConformityQuery.save(nc);
        return nc;
    }

    @Transactional
    public void approve(Long id, User user){
        NonConformity nonConformity = nonConformityQuery.byId(id);

        nonConformity.approve(user);

        if (nonConformity.getRequiresQualityTool()){
            qualityToolService.initializeTool(nonConformity);
        }
    }

    @Transactional
    public  void sendToCorrection(Long id, User user){
        NonConformity nonConformity = nonConformityQuery.byId(id);
        nonConformity.correction(user);
    }


    public List<NonconformityResponseDTO> findByTitleStartingWithIgnoreCase(String title) {
        return  nonConformityQuery.findByTitle(title)
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();
    }

    public List<NonconformityResponseDTO> getAllByStatus(NonConformityStatus status){
        return  nonConformityQuery.getTwentyByStatus(status)
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();
    }
}