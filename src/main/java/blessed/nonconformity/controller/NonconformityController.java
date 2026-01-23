package blessed.nonconformity.controller;


import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.service.NonconformityService;
import blessed.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @PostMapping()
    public ResponseEntity<NonconformityResponseDTO> create(
            @RequestBody @Valid NonconformityRequestDTO data,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        NonConformity nonconformity = service.create(data, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new NonconformityResponseDTO(nonconformity));
    }

    @GetMapping
    public List<NonconformityResponseDTO> getAll(){
        List<NonconformityResponseDTO> nonconformities = service.getAll();
        return  nonconformities;
    }

    @GetMapping(params = "id")
    public ResponseEntity<NonconformityResponseDTO> getNcById(@RequestParam Long id){
        return ResponseEntity.ok(service.getNcById(id));
    }

    @GetMapping(params = "title")
    public ResponseEntity<List<NonconformityResponseDTO>> getByTitle(@RequestParam String title){
        List<NonconformityResponseDTO> nonConformities = service.findByTitleStartingWithIgnoreCase(title);

        return ResponseEntity.ok(nonConformities);
    }


    @GetMapping("/admin/status-pending")
    public ResponseEntity<List<NonconformityResponseDTO>> getAllNcPending(){
        return ResponseEntity
                .ok(service.getAllNonConformityPending());
    }

    @PutMapping("/admin/{id}/approve")
    public ResponseEntity<Void> approvedNc(
            @PathVariable Long id,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        service.approve(id, user);

        return ResponseEntity.noContent().build();
    }


}
