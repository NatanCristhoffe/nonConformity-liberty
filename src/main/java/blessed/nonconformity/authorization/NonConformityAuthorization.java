package blessed.nonconformity.authorization;

import blessed.auth.utils.CurrentUser;
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
    private final CurrentUser currentUser;

    public NonConformityAuthorization(
            NonConformityQuery nonConformityQuery,
            CurrentUser currentUser
    ) {
        this.nonConformityQuery = nonConformityQuery;
        this.currentUser = currentUser;
    }

    public boolean isDispositionOwnerOrAdmin(Long nonconformityId) {
        if (currentUser.isAdmin()){
            return true;
        }

        return nonConformityQuery
                .existsByIdAndDispositionOwnerIdAndCompanyId(nonconformityId, currentUser.getId(), currentUser.getCompanyId());
    }

    public boolean isEffectivenessAnalystOrAdmin(Long nonconformityId, Authentication authentication){
        User user = (User) authentication.getPrincipal();

        if (user.isAdmin()) {
            return true;
        }

        return nonConformityQuery.existsByIdAndEffectivenessAnalystId(nonconformityId, user.getId());
    }

    public boolean canAccessNc(Long ncId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        if (user.isAdmin()) {
            return true;
        }

        return nonConformityQuery.hasLink(ncId, user.getId());
    }
}

