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
import blessed.nonconformity.tools.FiveWhyTool;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import blessed.utils.DataTimeUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FiveWhyService {
    private final UserQuery userQuery;
    private final NonconformityRepository ncRepository;
    private final FiveWhyRepository fiveWhyRepository;
    private final FiveWhyToolRepository fiveWhyToolRepository;
    private final AuthenticationManager authenticationManager;

    public FiveWhyService(
            NonconformityRepository ncRepository,
            FiveWhyToolRepository fiveWhyToolRepository,
            FiveWhyRepository fiveWhyRepository,
            UserQuery userQuery,
            AuthenticationManager authenticationManager){
        this.fiveWhyToolRepository = fiveWhyToolRepository;
        this.fiveWhyRepository = fiveWhyRepository;
        this.ncRepository = ncRepository;
        this.userQuery = userQuery;
        this.authenticationManager = authenticationManager;
    }

    @PreAuthorize("@ncAuth.isDispositionOwnerOrAdmin(#nonconformityId, authentication)")
    @Transactional
    public void addAnswer(
            Long nonconformityId,
            Long fiveWhyId,
            FiveWhyAnswerRequestDTO answer,
            User user
    ) {
        NonConformity nc = ncRepository.findById(nonconformityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Não conformidade não encontrada.")
                );

        FiveWhy fiveWhy = fiveWhyRepository.findById(fiveWhyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Porquês não encontrado.")
                );

        fiveWhy.addAnswer(answer, nc);
        fiveWhyRepository.save(fiveWhy);

        if (fiveWhy.areAllWhysAnswered()) {
            nc.concludeFiveWhyTool(user);
        }
    }

}
