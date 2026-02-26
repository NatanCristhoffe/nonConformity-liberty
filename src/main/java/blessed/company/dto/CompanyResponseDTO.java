package blessed.company.dto;

import blessed.company.entity.Company;
import blessed.company.enums.PlanType;

public record CompanyResponseDTO(
    String companyName,
    PlanType planType
) {

    public CompanyResponseDTO(Company data){
        this(
                data.getCompanyName(),
                data.getPlanType()
        );
    }
}
