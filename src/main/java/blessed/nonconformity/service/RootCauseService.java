package blessed.nonconformity.service;

import blessed.auth.utils.CurrentUser;
import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.RootCauseRequestDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.entity.RootCause;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.repository.RootCauseRepository;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.nonconformity.service.query.RootCauseQuery;
import blessed.notification.enums.NotificationType;
import blessed.notification.service.NotificationService;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class RootCauseService {
    private final NonConformityQuery nonConformityQuery;
    private final UserQuery userQuery;
    private final RootCauseQuery rootCauseQuery;
    private final NotificationService notificationService;
    private final  CurrentUser currentUser;


    public RootCauseService(
        UserQuery userQuery,
        NonConformityQuery nonConformityQuery,
        RootCauseQuery rootCauseQuery,
        NotificationService notificationService,
        CurrentUser currentUser
    ){
        this.userQuery = userQuery;
        this.nonConformityQuery = nonConformityQuery;
        this.rootCauseQuery = rootCauseQuery;
        this.notificationService = notificationService;
        this.currentUser = currentUser;
    }

    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId)")
    @Transactional
    public RootCause create(Long nonconformityId, RootCauseRequestDTO data){

        UUID companyId = currentUser.getCompanyId();
        User user = userQuery.byId(companyId, currentUser.getId());
        NonConformity nc = nonConformityQuery.byId(nonconformityId, companyId);

        RootCause rootCause = new RootCause(nc, data, user);
        rootCauseQuery.save(rootCause);

        nc.addRootCause(rootCause, user);

        Set<UUID> usersToNotify = new HashSet<>();

        usersToNotify.add(nc.getCreatedBy().getId());
        usersToNotify.add(nc.getEffectivenessAnalyst().getId());

        notificationService.notifyIfNotSameUser(
                usersToNotify,
                currentUser.getId(),
                companyId,
                NotificationType.ROOT_CAUSE_COMPLETED,
                nc.getTitle()
        );


        return rootCause;
    }
}
