package blessed.nonconformity.entity;

import blessed.nonconformity.tools.FiveWhyTool;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiveWhy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int level; //1 a 5

    @Column(nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "five_why_tool_id")
    private FiveWhyTool fiveWhyTool;

    @Column(nullable = true)
    private String answer;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FiveWhy fiveWhy = (FiveWhy) o;
        return level == fiveWhy.level && Objects.equals(question, fiveWhy.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, question);
    }
}
