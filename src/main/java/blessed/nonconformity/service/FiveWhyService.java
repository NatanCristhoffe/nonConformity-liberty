package blessed.nonconformity.service;

import blessed.auth.utils.CurrentUser;
import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.FiveWhyAnswerRequestDTO;
import blessed.nonconformity.dto.FiveWhyRequestDTO;
import blessed.nonconformity.entity.FiveWhy;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.FiveWhyRepository;
import blessed.nonconformity.repository.FiveWhyToolRepository;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.service.query.FiveWhyQuery;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.nonconformity.tools.FiveWhyTool;
import blessed.notification.enums.NotificationType;
import blessed.notification.service.NotificationService;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import blessed.utils.DataTimeUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
public class FiveWhyService {
    private final NonConformityQuery ncRepository;
    private final FiveWhyQuery fiveWhyQuery;
    private final UserQuery userQuery;
    private final NotificationService notificationService;
    private final CurrentUser currentUser;

    public FiveWhyService(
            NonConformityQuery ncRepository,
            FiveWhyQuery fiveWhyQuery,
            UserQuery userQuery,
            NotificationService notificationService,
            CurrentUser currentUser
            ){
        this.fiveWhyQuery = fiveWhyQuery;
        this.ncRepository = ncRepository;
        this.userQuery = userQuery;
        this.notificationService = notificationService;
        this.currentUser = currentUser;
    }

    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId)")
    @Transactional
    public void addAnswer(
            Long nonconformityId,
            Long fiveWhyId,
            FiveWhyAnswerRequestDTO answer
    ) {
        UUID companyId = currentUser.getCompanyId();
        NonConformity nc = ncRepository.byId(nonconformityId, companyId);
        User user = userQuery.byId(companyId, currentUser.getId());

        FiveWhy fiveWhy = fiveWhyQuery.byId(fiveWhyId);

        fiveWhy.addAnswer(answer, nc);

        if (fiveWhy.areAllWhysAnswered()) {
            nc.concludeFiveWhyTool(user);

            Set<UUID> usersId = new HashSet<UUID>();

            usersId.add(nc.getCreatedBy().getId());
            usersId.add(nc.getEffectivenessAnalyst().getId());

            notificationService.notifyIfNotSameUser(
                usersId,
                currentUser.getId(),
                companyId,
                NotificationType.QUALITY_TOOL_COMPLETED,
                nc.getTitle()
            );
        }
    }

}
