package blessed.nonconformity.service;

import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.sector.service.query.SectorQuery;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("@ncAuth.canAccessNc(#nonconformityId, authentication)")
    public NonconformityResponseDTO getNcById(Long nonconformityId, boolean includeAll){
        NonConformity nonConformity = includeAll
                ? nonConformityQuery.byIdWithAll(nonconformityId)
                : nonConformityQuery.byId(nonconformityId);

        return new NonconformityResponseDTO(nonConformity, includeAll);
    }


    public Page<NonconformityResponseDTO> getAllOrGetByUser(
            User userRequest,
            boolean getAll,
            Pageable pageable
    ){

        User user = userQuery.byId(userRequest.getId());

        if(getAll && user.isAdmin()){
            return nonConformityQuery.getAll(pageable)
                    .map(NonconformityResponseDTO::new);
        }

        return nonConformityQuery.getAllUser(user.getId(), pageable).map(NonconformityResponseDTO::new);
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

    public Page<NonconformityResponseDTO> getMyNonconformitiesByStatus(
            NonConformityStatus status,
            User user,
            boolean includeAll,
            Pageable pageable
    ) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (includeAll && isAdmin) {
            return nonConformityQuery
                    .findAllByStatus(status, pageable)
                    .map(NonconformityResponseDTO::new);
        }

        return nonConformityQuery
                .findMyByStatus(status, user.getId(), pageable)
                .map(NonconformityResponseDTO::new);
    }

}