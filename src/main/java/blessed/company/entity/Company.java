package blessed.company.entity;


import blessed.company.dto.CompanyRequestDTO;
import blessed.company.enums.PlanType;
import blessed.company.enums.TypeDocument;
import blessed.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "companies",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "document"),
                @UniqueConstraint(columnNames = "phone")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Company {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @JdbcTypeCode(SqlTypes.CHAR)
        private UUID id;

        @Column(nullable = false)
        private String companyName;

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        private PlanType planType;

        @Column(nullable = false)
        private String document;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TypeDocument typeDocument;

        @Column(nullable = false)
        private String phone;

        @Column(nullable = false)
        @Email
        private String email;

        @Column(nullable = false)
        private String street;

        @Column(nullable = false)
        private int numberStreet;

        @Column(nullable = false)
        private String city;

        @Column(nullable = false)
        private String state;

        @Column(nullable = false)
        private boolean isActive;

        @Column(nullable = false)
        private LocalDateTime createAt;

        @Column(nullable = false)
        private LocalDateTime updateAt;


        public Company(CompanyRequestDTO data, TypeDocument typeDocument){
                this.companyName = data.companyName().toLowerCase();
                this.planType = data.planType();
                this.document = data.document();
                this.typeDocument = typeDocument;
                this.phone = data.phone();
                this.email = data.email();
                this.street = data.street();
                this.numberStreet = data.numberStreet();
                this.city = data.city();
                this.state = data.state();
                this.isActive = true;
                this.createAt = LocalDateTime.now();
                this.updateAt = LocalDateTime.now();
        }

        public void changePlan(PlanType newPlan){
                this.planType = newPlan;
                this.updateAt = LocalDateTime.now();
        }

}
