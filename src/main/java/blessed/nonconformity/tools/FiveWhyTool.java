package blessed.nonconformity.tools;

import blessed.exception.BusinessException;
import blessed.nonconformity.entity.FiveWhy;
import blessed.nonconformity.entity.NonConformity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiveWhyTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "nonconformity_id", nullable = false)
    private NonConformity nonconformity;

    @OneToMany(
            mappedBy = "fiveWhyTool",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<FiveWhy> fiveWhys = new HashSet<>();

    private boolean completed;

    public void addWhy(FiveWhy why){
        if (fiveWhys.size() >= 5){
            throw new BusinessException("Não é permitido mais de 5 porquês");
        }

        if (fiveWhys.contains(why)){
            throw new BusinessException("Já existe um porquê para este nível");
        }

        fiveWhys.add(why);
    }

}
