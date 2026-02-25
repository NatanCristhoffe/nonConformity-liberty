package blessed.user.entity;

import blessed.application.dto.AdminOnboardingRequestDTO;
import blessed.auth.dto.AuthenticationDTO;
import blessed.auth.dto.RegisterDTO;
import blessed.company.entity.Company;
import blessed.exception.BusinessException;
import blessed.user.dto.UpdateUserDTO;
import blessed.user.enums.UserRole;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import blessed.sector.entity.Sector;
import blessed.user.dto.UserRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class User implements UserDetails{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    public User(
            AdminOnboardingRequestDTO data, String encryptedPassword,
            Sector sector, Company company
    ){
        if (encryptedPassword == null || encryptedPassword.isBlank()) {
            throw new BusinessException("Senha inválida.");
        }

        if (data.email() == null || !data.email().contains("@")) {
            throw new BusinessException("E-mail inválido.");
        }

        this.email = data.email().toLowerCase();
        this.password = encryptedPassword;
        this.firstName = data.firstName().toLowerCase();
        this.lastName = data.lastName().toLowerCase();
        this.phone = data.phone();
        this.sector = sector;
        this.role=UserRole.ADMIN;
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.company = company;
    }

    public User(
            RegisterDTO data, String encryptedPassword,
            Sector sector, Company company
    ){
        if (encryptedPassword == null || encryptedPassword.isBlank()) {
            throw new BusinessException("Senha inválida.");
        }

        if (data.email() == null || !data.email().contains("@")) {
            throw new BusinessException("E-mail inválido.");
        }

        this.email = data.email().toLowerCase();
        this.password = encryptedPassword;
        this.firstName = data.firstName().toLowerCase();
        this.lastName = data.lastName().toLowerCase();
        this.phone = data.phone();
        this.sector = sector;
        this.role=data.role();
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.company = company;
    }

    public void update(UpdateUserDTO newData, Sector newSector, String newPassword){
        this.firstName = newData.firstName().toLowerCase();
        this.lastName = newData.lastName().toLowerCase();
        this.phone = newData.phone();
        this.sector =  newSector;

        if (newPassword != null){
            this.password = newPassword;
        }


    }

    public void enable(){
        this.enabled = true;
        this.updateAt = LocalDateTime.now();
    }

    public void disable(){
        this.enabled = false;
        this.updateAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN){
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        } else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isAdmin(){
        return this.role == UserRole.ADMIN;
    }
}
