package blessed.auth.utils;

import blessed.company.entity.Company;
import blessed.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUser {
    public User get(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return (User) authentication.getPrincipal();
    }

    public UUID getCompanyId(){
        User user = get();
        if (user.getCompany() == null){
            return null;
        }
        return user.getCompany().getId();
    }

    public UUID getId(){
        return  get().getId();
    }

    public Company getCompany(){
        return get().getCompany();
    }

    public boolean isAdmin(){
        return get().isAdmin();
    }
}
