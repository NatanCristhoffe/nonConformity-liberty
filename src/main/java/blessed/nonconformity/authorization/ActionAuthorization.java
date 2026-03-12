package blessed.nonconformity.authorization;

import blessed.auth.utils.CurrentUser;
import blessed.nonconformity.service.query.ActionQuery;
import blessed.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("actionAuth")
public class ActionAuthorization {
    private final ActionQuery actionQuery;
    private final CurrentUser currentUser;

    public ActionAuthorization(
            ActionQuery actionQuery, CurrentUser currentUser
    ){
        this.actionQuery = actionQuery;
        this.currentUser = currentUser;
    }

    public boolean isResponsibleOrAdmin(Long actionId){
        if (currentUser.isAdmin()){
            return true;
        }
        return actionQuery.existsByIdAndResponsibleUserId(actionId, currentUser.getId());
    }

}
