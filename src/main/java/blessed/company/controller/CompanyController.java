package blessed.company.controller;


import blessed.application.dto.CompanyWithAdminRequestDTO;
import blessed.application.service.CreateCompanyWithAdminService;
import blessed.company.dto.CompanyRequestDTO;
import blessed.company.service.CompanyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/company")
public class CompanyController {
    private final CreateCompanyWithAdminService service;

    CompanyController(
            CreateCompanyWithAdminService service
    ){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> companyCreate(@RequestBody CompanyWithAdminRequestDTO data){
        service.createCompanyWithAdmin(data);

        return ResponseEntity.ok(Map.of("success", "Empresa cadastrada com sucesso!"));
    }
}
