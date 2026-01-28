package blessed.nonconformity.service;

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
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RootCauseService {
    private final NonConformityQuery nonConformityQuery;
    private final UserQuery userQuery;
    private final RootCauseQuery rootCauseQuery;

    public RootCauseService(
        UserQuery userQuery,
        NonConformityQuery nonConformityQuery,
        RootCauseQuery rootCauseQuery
    ){
        this.userQuery = userQuery;
        this.nonConformityQuery = nonConformityQuery;
        this.rootCauseQuery = rootCauseQuery;
    }

    @Transactional
    public RootCause create(Long ncId, RootCauseRequestDTO data, User user){

        NonConformity nc = nonConformityQuery.byId(ncId);
        User userRequest = userQuery.byId(user.getId());

        RootCause rootCause = new RootCause(data, userRequest);
        nc.addRootCause(rootCause);

        rootCauseQuery.save(rootCause);
        return rootCause;
    }
}
