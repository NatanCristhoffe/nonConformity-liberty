package blessed.NonConformity.nonconformityLog;

import blessed.NonConformity.nonConformity.NonConformity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "nonconformity_logs")
@Getter
public class NonconformityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String message;
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "nonconformity_id")
    private NonConformity nonconformity;

    protected NonconformityLog(){
    }

    public NonconformityLog(NonConformity nonconformity, String message){
        this.nonconformity = nonconformity;
        this.message = message;
        this.createdAt = Instant.now();
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NonconformityLog that = (NonconformityLog) o;
        return Objects.equals(message, that.message) && Objects.equals(nonconformity, that.nonconformity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, nonconformity);
    }
}
