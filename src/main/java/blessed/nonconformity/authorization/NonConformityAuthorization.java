package blessed.nonconformity.authorization;

import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.user.entity.User;
import blessed.user.service.query.UserQuery;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component("ncAuth")
public class NonConformityAuthorization {

    private final NonConformityQuery nonConformityQuery;

    public NonConformityAuthorization(NonConformityQuery nonConformityQuery) {
        this.nonConformityQuery = nonConformityQuery;
    }

    public boolean isDispositionOwnerOrAdmin(Long nonconformityId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        if (user.isAdmin()) {
            return true;
        }

        return nonConformityQuery
                .existsByIdAndDispositionOwnerId(nonconformityId, user.getId());
    }

    public boolean canAccessNc(Long ncId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        if (user.isAdmin()) {
            return true;
        }

        return nonConformityQuery.hasLink(ncId, user.getId());
    }
}

