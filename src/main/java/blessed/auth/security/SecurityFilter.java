package blessed.auth.security;


import blessed.auth.service.TokenService;
import blessed.user.entity.User;
import blessed.user.repository.UserRepository;
import blessed.user.service.query.UserQuery;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserQuery userQuery;

    public SecurityFilter(TokenService tokenService, UserQuery userQuery){
        this.tokenService = tokenService;
        this.userQuery = userQuery;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = recoverToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String email;
        try {
            email = tokenService.validateToken(token);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
            return;
        }

        if (email == null) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        var userOpt = userQuery.byEmail(email);

        if (userOpt.isEmpty()) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário não encontrado");
            return;
        }

        User user = userOpt.get();

        // No seu SecurityFilter.java

        if (!user.isEnabled()) {
            // 1. Limpa o contexto para garantir
            SecurityContextHolder.clearContext();

            // 2. Define o status manualmente (evita o dispatch para /error)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            // 3. Escreve a resposta
            response.getWriter().write("{\"message\": \"Usuário desativado/suspenso.\"}");

            // 4. MATA a requisição aqui. Não chama o filterChain.
            return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
