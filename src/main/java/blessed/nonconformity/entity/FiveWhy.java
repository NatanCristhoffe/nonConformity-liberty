package blessed.nonconformity.entity;

import blessed.exception.BusinessException;
import blessed.nonconformity.dto.FiveWhyAnswerRequestDTO;
import blessed.nonconformity.dto.FiveWhyRequestDTO;
import blessed.nonconformity.tools.FiveWhyTool;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
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

    public FiveWhy(FiveWhyRequestDTO why,FiveWhyTool tool){
        this.level = why.level();
        this.question = why.question();
        this.fiveWhyTool = tool;

    }

    public void addAnswer(FiveWhyAnswerRequestDTO data, NonConformity nc){
        if (!this.fiveWhyTool.getNonconformity().getId().equals(nc.getId())) {
            throw new BusinessException("A pergunta Cinco Porquês não pertence a esta categoria de não conformidade.");
        }

        if (this.answer != null){
            throw new BusinessException("Esse Porquês já foi respondido.");
        }

        this.answer = data.answer();
    }

    public boolean areAllWhysAnswered(){
        boolean allAnswered = this.getFiveWhyTool()
                .getFiveWhys()
                .stream()
                .allMatch(w ->
                        w.getAnswer() != null &&
                                !w.getAnswer().isBlank()
                );

        return  allAnswered;
    }



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
