package blessed.nonconformity.entity;

import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.RootCauseRequestDTO;
import blessed.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RootCause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 10, max = 2000)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_created_roote_cause_id", nullable = false)
    private User userCreated;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nonconformity_id")
    private NonConformity nonconformity;

    public RootCause(RootCauseRequestDTO data, User user){
        this.description = data.description();
        this.userCreated = user;
    }

}
