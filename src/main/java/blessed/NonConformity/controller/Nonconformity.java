package blessed.NonConformity.controller;


import blessed.NonConformity.nonConformity.NonConformity;
import blessed.NonConformity.nonConformity.NonconformityRepository;
import blessed.NonConformity.nonConformity.NonconformityRequestDTO;
import blessed.NonConformity.nonConformity.NonconformityResponseDTO;
import blessed.NonConformity.service.NonconformityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nonconformity")
public class Nonconformity {

    private final NonconformityService service;
    public Nonconformity(NonconformityService service){
        this.service = service;
    }

    @GetMapping
    public List<NonconformityResponseDTO> getAll(){
        List<NonconformityResponseDTO> nonconformities = service.getAll();
        return  nonconformities;
    }

    @PostMapping
    public ResponseEntity<NonconformityResponseDTO> createdNonconformity(@RequestBody NonconformityRequestDTO data){
        NonConformity nonconformityData = service.create(data);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new NonconformityResponseDTO(nonconformityData));
    }
}
