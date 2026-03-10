package blessed.nonconformity.service;

import blessed.company.entity.Company;
import blessed.company.service.query.CompanyQuery;
import blessed.exception.BusinessException;
import blessed.infra.enums.FileType;
import blessed.infra.storage.S3FileStorageService;
import blessed.nonconformity.dto.ActionResponseDTO;
import blessed.nonconformity.dto.NonconformityUpdateDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.notification.entity.Notification;
import blessed.notification.enums.NotificationType;
import blessed.notification.service.NotificationService;
import blessed.sector.service.query.SectorQuery;
import blessed.sector.entity.Sector;
import blessed.user.entity.User;
import blessed.user.service.UserService;
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
    private final UserService userService;
    private final SectorQuery sectorQuery;
    private final S3FileStorageService s3Service;
    private final CompanyQuery companyQuery;
    private final NotificationService notificationService;

    public NonconformityService(
            QualityToolService qualityToolService,
            NonConformityQuery nonConformityQuery,
            UserService userService,
            SectorQuery sectorQuery,
            S3FileStorageService s3Service,
            CompanyQuery companyQuery,
            NotificationService notificationService
    ) {
        this.qualityToolService = qualityToolService;
        this.nonConformityQuery = nonConformityQuery;
        this.userService = userService;
        this.sectorQuery = sectorQuery;
        this.s3Service = s3Service;
        this.companyQuery = companyQuery;
        this.notificationService = notificationService;
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

        User user = userService.getById(userRequest.getCompany().getId(),userRequest.getId());

        if(getAll && user.isAdmin()){
            return nonConformityQuery.getAll(user.getCompany().getId(),pageable)
                    .map(NonconformityResponseDTO::new);
        }

        return nonConformityQuery.getAllNonconformitiesByUser(user.getId(), user.getCompany().getId(),pageable).map(NonconformityResponseDTO::new);
    }

    @Transactional
    public NonconformityResponseDTO create(
            NonconformityRequestDTO data, User user,
            UUID companyId,MultipartFile file
    ){
        Sector source = sectorQuery.byId(data.sourceDepartmentId(), companyId);
        Sector responsibleDepartment = sectorQuery.byId(data.responsibleDepartmentId(), companyId);
        Company company = companyQuery.byId(companyId);

        User createBy = userService.getById(companyId, user.getId());

        User dispositionOwner = userService.getById(companyId, data.dispositionOwnerId());
        User effectivenessAnalyst = userService.getById(companyId, data.effectivenessAnalystId());

        if (!effectivenessAnalyst.isAdmin()){
            throw new BusinessException("Usuário não possui permissão para realizar a análise de eficácia.");
        }

        String urlEvidence = null;

        if (file != null && !file.isEmpty()) {
            urlEvidence = s3Service.uploadFile(file, "evidencias", FileType.EVIDENCE);
        }

        NonConformity nc = new NonConformity(
                data, source, responsibleDepartment, createBy,
                dispositionOwner,effectivenessAnalyst, urlEvidence,
                company
        );
        nonConformityQuery.save(nc);

        notificationService.notifyByUser(
                dispositionOwner.getId(),
                companyId,
                NotificationType.DISPOSITION_OWNER_ASSIGNED,
                nc.getTitle()
        );
        notificationService.notifyByUser(
                effectivenessAnalyst.getId(),
                companyId,
                NotificationType.EFFECTIVENESS_ANALYST_ASSIGNED,
                nc.getTitle()
        );
        return new NonconformityResponseDTO(nc);
    }

    @Transactional
    public void approve(Long id, User user){
        NonConformity nonConformity = nonConformityQuery.byId(id, user.getCompany().getId());
        UUID companyId = user.getCompany().getId();

        nonConformity.approve(user);

        if (nonConformity.getRequiresQualityTool()){
            qualityToolService.initializeTool(nonConformity);

            notificationService.notifyIfNotSameUser(
                    nonConformity.getDispositionOwner().getId(),
                    user.getId(),
                    companyId,
                    NotificationType.QUALITY_TOOL_REQUIRED,
                    nonConformity.getTitle()
            );

        } else {
            notificationService.notifyIfNotSameUser(
                    nonConformity.getDispositionOwner().getId(),
                    user.getId(),
                    companyId,
                    NotificationType.ROOT_CAUSE_REQUIRED,
                    nonConformity.getTitle()
            );
        }

        notificationService.notifyIfNotSameUser(
                nonConformity.getCreatedBy().getId(),
                user.getId(),
                companyId,
                NotificationType.NON_CONFORMITY_APPROVED,
                nonConformity.getTitle()
        );
    }

    @Transactional
    public  void sendToCorrection(Long id, User user){
        NonConformity nonConformity = nonConformityQuery.byId(id, user.getCompany().getId());
        nonConformity.correction(user);

        notificationService.notifyIfNotSameUser(
                nonConformity.getCreatedBy().getId(),
                user.getId(),
                user.getCompany().getId(),
                NotificationType.NON_CONFORMITY_RETURNED_FOR_CORRECTION,
                nonConformity.getTitle()
        );
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

        User dispositionUser = userService.getById(userRequest.getCompany().getId(),data.dispositionOwnerId());
        User effectivenessUser = userService.getById(userRequest.getCompany().getId(), data.effectivenessAnalystId());

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