package blessed.company.service.query;

import blessed.company.dto.CompanyRequestDTO;
import blessed.company.entity.Company;
import blessed.company.repository.CompanyRepository;
import blessed.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class CompanyQuery {
    private final CompanyRepository repository;

    CompanyQuery(CompanyRepository repository){
        this.repository = repository;
    }

    public void save(Company company){
        repository.save(company);
    }


    public boolean existsByEmailOrPhoneOrDocument(
            String document,
            String email,
            String phone
    ){
        return  repository.existsByDocument(document) ||
                repository.existsByEmail(email) ||
                repository.existsByPhone(phone);
    }
}
