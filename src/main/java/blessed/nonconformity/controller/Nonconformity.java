package blessed.nonconformity.controller;


import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.service.NonconformityService;
import jakarta.validation.Valid;
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
    public ResponseEntity<NonconformityResponseDTO> createdNonconformity(@Valid @RequestBody NonconformityRequestDTO data){
        NonConformity nonconformityData = service.create(data);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new NonconformityResponseDTO(nonconformityData));
    }
}
