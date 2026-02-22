package blessed.company.service;


import blessed.company.dto.CompanyRequestDTO;
import blessed.company.entity.Company;
import blessed.company.enums.TypeDocument;
import blessed.company.service.query.CompanyQuery;
import blessed.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyQuery companyQuery;

    CompanyService(CompanyQuery companyQuery){
        this.companyQuery = companyQuery;
    }

    public Company create(CompanyRequestDTO data){
        existsByEmailOrPhone(data);

        Company company = new Company(data, TypeDocument.CPF);
        companyQuery.save(company);

        return company;
    }


    private void existsByEmailOrPhone(CompanyRequestDTO data){
        if (this.companyQuery.existsByEmailOrPhoneOrDocument(data.document(), data.email(), data.phone())){
            throw new BusinessException("Empresa já cadastrada com este documento, e-mail ou telefone.");
        }

    }
}
