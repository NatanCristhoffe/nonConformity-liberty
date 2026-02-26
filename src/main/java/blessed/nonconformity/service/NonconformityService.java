package blessed.nonconformity.service;

import blessed.company.entity.Company;
import blessed.company.service.query.CompanyQuery;
import blessed.infra.enums.FileType;
import blessed.infra.storage.S3FileStorageService;
import blessed.nonconformity.dto.ActionResponseDTO;
import blessed.nonconformity.dto.NonconformityUpdateDTO;
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
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class NonconformityService {

    private final QualityToolService qualityToolService;
    private final NonConformityQuery nonConformityQuery;
    private final UserQuery userQuery;
    private final SectorQuery sectorQuery;
    private final S3FileStorageService s3Service;
    private final CompanyQuery companyQuery;

    public NonconformityService(
            QualityToolService qualityToolService,
            NonConformityQuery nonConformityQuery,
            UserQuery userQuery,
            SectorQuery sectorQuery,
            S3FileStorageService s3Service,
            CompanyQuery companyQuery
    ) {
        this.qualityToolService = qualityToolService;
        this.nonConformityQuery = nonConformityQuery;
        this.userQuery = userQuery;
        this.sectorQuery = sectorQuery;
        this.s3Service = s3Service;
        this.companyQuery = companyQuery;
    }

    @PreAuthorize("@ncAuth.canAccessNc(#nonconformityId, authentication)")
    public NonconformityResponseDTO getNcById(
            Long nonconformityId, boolean includeAll, UUID companyId){

        NonConformity nonConformity = includeAll
                ? nonConformityQuery.byIdWithAll(nonconformityId, companyId)
                : nonConformityQuery.byId(nonconformityId, companyId);

        String presignedUrl = s3Service.generatePresignedUrl(nonConformity.getUrlEvidence());

        Set<ActionResponseDTO> actionsDto = null;

        if (includeAll && nonConformity.getActions() != null) {
            actionsDto = nonConformity.getActions().stream()
                    .map(action -> new ActionResponseDTO(
                            action,
                            s3Service.generatePresignedUrl(action.getEvidenceUrl())
                    ))
                    .collect(Collectors.toSet());
        }

        return new NonconformityResponseDTO(nonConformity, includeAll, presignedUrl, actionsDto);
    }


    public Page<NonconformityResponseDTO> getAllOrGetByUser(
            User userRequest,
            boolean getAll,
            Pageable pageable
    ){

        User user = userQuery.byId(userRequest.getCompany().getId(),userRequest.getId());

        if(getAll && user.isAdmin()){
            return nonConformityQuery.getAll(user.getCompany().getId(),pageable)
                    .map(NonconformityResponseDTO::new);
        }

        return nonConformityQuery.getAllNonconformitiesByUser(user.getId(), user.getCompany().getId(),pageable).map(NonconformityResponseDTO::new);
    }

    @Transactional
    public NonConformity create(
            NonconformityRequestDTO data, User createdByNc,
            UUID companyId,MultipartFile file
    ){
        Sector source = sectorQuery.byId(data.sourceDepartmentId(), companyId);
        Sector responsibleDepartment = sectorQuery.byId(data.responsibleDepartmentId(), companyId);
        Company company = companyQuery.byId(companyId);

        User createBy = userQuery.byId(createdByNc.getCompany().getId(), createdByNc.getId());
        User dispositionOwner = userQuery.byId(createdByNc.getCompany().getId(), data.dispositionOwnerId());
        User effectivenessAnalyst = userQuery.byId(createdByNc.getCompany().getId(), data.effectivenessAnalystId());

        String urlEvidence = null; // Começa nulo

        if (file != null && !file.isEmpty()) {
            urlEvidence = s3Service.uploadFile(file, "evidencias", FileType.EVIDENCE);
        }

        NonConformity nc = new NonConformity(
                data, source, responsibleDepartment, createBy,
                dispositionOwner,effectivenessAnalyst, urlEvidence,
                company
        );
        nonConformityQuery.save(nc);
        return nc;
    }

    @Transactional
    public void approve(Long id, User user){
        NonConformity nonConformity = nonConformityQuery.byId(id, user.getCompany().getId());

        nonConformity.approve(user);

        if (nonConformity.getRequiresQualityTool()){
            qualityToolService.initializeTool(nonConformity);
        }
    }

    @Transactional
    public  void sendToCorrection(Long id, User user){
        NonConformity nonConformity = nonConformityQuery.byId(id, user.getCompany().getId());
        nonConformity.correction(user);
    }


    public List<NonconformityResponseDTO> findByTitleStartingWithIgnoreCase(String title, UUID companyId) {
        return  nonConformityQuery.findByTitle(title, companyId)
                .stream()
                .map(NonconformityResponseDTO::new)
                .toList();
    }

    public Page<NonconformityResponseDTO> getMyNonconformitiesByStatus(
            NonConformityStatus status,
            User user,
            UUID companyId,
            boolean includeAll,
            Pageable pageable
    ) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (includeAll && isAdmin) {
            return nonConformityQuery
                    .findAllByStatus(status, companyId,pageable)
                    .map(NonconformityResponseDTO::new);
        }

        return nonConformityQuery
                .findMyByStatus(status, user.getId(), companyId,pageable)
                .map(NonconformityResponseDTO::new);
    }

    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public void update(Long id, NonconformityUpdateDTO data, User userRequest, MultipartFile file){
        NonConformity nonConformity = nonConformityQuery.byId(id, userRequest.getCompany().getId());

        String oldEvidence = nonConformity.getUrlEvidence();

        String newUrlEvidence;

        if (file != null && !file.isEmpty()) {
            newUrlEvidence = s3Service.uploadFile(file, "evidencias", FileType.EVIDENCE);
        } else {
            newUrlEvidence = null;
        }

        Sector sourceDepartment = sectorQuery.byId(data.sourceDepartmentId(), userRequest.getCompany().getId());
        Sector responsibleDepartment = sectorQuery.byId(data.responsibleDepartmentId(), userRequest.getCompany().getId());

        User dispositionUser = userQuery.byId(userRequest.getCompany().getId(),data.dispositionOwnerId());
        User effectivenessUser = userQuery.byId(userRequest.getCompany().getId(), data.effectivenessAnalystId());

        nonConformity.update(
                data,sourceDepartment, responsibleDepartment,
                dispositionUser, effectivenessUser, userRequest, newUrlEvidence
        );

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit(){
                        if (oldEvidence != null && newUrlEvidence != null){
                            s3Service.deleteFile(oldEvidence);
                        }
                    }
                }
        );

    }
}