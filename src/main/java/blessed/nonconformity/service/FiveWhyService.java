package blessed.nonconformity.service;

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

import java.util.List;
import java.util.UUID;


@Service
public class FiveWhyService {
    private final NonConformityQuery ncRepository;
    private final FiveWhyRepository fiveWhyRepository;
    private final NotificationService notificationService;

    public FiveWhyService(
            NonConformityQuery ncRepository,
            FiveWhyRepository fiveWhyRepository,
            NotificationService notificationService
            ){
        this.fiveWhyRepository = fiveWhyRepository;
        this.ncRepository = ncRepository;
        this.notificationService = notificationService;
    }

    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public void addAnswer(
            Long nonconformityId,
            Long fiveWhyId,
            FiveWhyAnswerRequestDTO answer,
            User user
    ) {
        UUID companyId = user.getCompany().getId();
        NonConformity nc = ncRepository.byId(nonconformityId, companyId);

        FiveWhy fiveWhy = fiveWhyRepository.findById(fiveWhyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Porquês não encontrado.")
                );

        fiveWhy.addAnswer(answer, nc);
        fiveWhyRepository.save(fiveWhy);

        if (fiveWhy.areAllWhysAnswered()) {
            nc.concludeFiveWhyTool(user);

            notificationService.notifyIfNotSameUser(
                nc.getCreatedBy().getId(),
                user.getId(),
                companyId,
                NotificationType.FIVE_WHY_COMPLETED,
                nc.getTitle()
            );
        }
    }

}
