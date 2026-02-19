package blessed.company.entity;


import blessed.company.dto.CompanyRequestDTO;
import blessed.company.enums.PlanType;
import blessed.company.enums.TypeDocument;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "campanies",
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
        private UUID id;

        @Column(nullable = false)
        private String companyName;

        @Column(nullable = false)
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


        Company(CompanyRequestDTO data, TypeDocument typeDocument){
                this.companyName = data.companyName();
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
}
