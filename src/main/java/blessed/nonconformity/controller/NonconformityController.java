package blessed.nonconformity.controller;


import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.service.NonconformityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/nonconformity")
public class NonconformityController {

    private final NonconformityService service;
    public NonconformityController(NonconformityService service){
        this.service = service;
    }

    @GetMapping
    public List<NonconformityResponseDTO> getAll(){
        List<NonconformityResponseDTO> nonconformities = service.getAll();
        return  nonconformities;
    }

    @GetMapping(params = "title")
    public ResponseEntity<List<NonconformityResponseDTO>> getByTitle(@RequestParam String title){
        List<NonconformityResponseDTO> nonConformities = service.findByTitleStartingWithIgnoreCase(title);

        return ResponseEntity.ok(nonConformities);
    }


    @PostMapping()
    public ResponseEntity<NonconformityResponseDTO> create(
            @RequestBody @Valid NonconformityRequestDTO data
//            @RequestPart(value = "evidences", required = false) List<MultipartFile> evidences
    ) {
        NonConformity nonconformity = service.create(data);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new NonconformityResponseDTO(nonconformity));
    }

}
