package blessed.nonconformity.authorization;

import blessed.nonconformity.service.query.ActionQuery;
import blessed.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("actionAuth")
public class ActionAuthorization {
    private final ActionQuery actionQuery;

    public ActionAuthorization(ActionQuery actionQuery){
        this.actionQuery = actionQuery;
    }

    public boolean isResponsibleOrAdmin(Long actionId, Authentication authentication){
        User user = (User) authentication.getPrincipal();

        if (user.isAdmin()){
            return true;
        }

        return actionQuery.existsByIdAndResponsibleUserId(actionId, user.getId());
    }

}
