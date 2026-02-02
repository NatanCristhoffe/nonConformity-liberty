package blessed.nonconformity.controller;


import blessed.exception.BusinessException;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.service.NonconformityService;
import blessed.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<NonconformityResponseDTO> getNcById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean includeAll
        ){
        return ResponseEntity.ok(service.getNcById(id, includeAll));
    }

    @GetMapping(params = "title")
    public ResponseEntity<List<NonconformityResponseDTO>> getByTitle(@RequestParam String title){
        List<NonconformityResponseDTO> nonConformities = service.findByTitleStartingWithIgnoreCase(title);

        return ResponseEntity.ok(nonConformities);
    }

    @GetMapping("/by-status")
    public ResponseEntity<Page<NonconformityResponseDTO>> getByStatus(
            @RequestParam NonConformityStatus status,
            @RequestParam(defaultValue = "false") boolean includeAll,
            @PageableDefault(size = 20) Pageable pageable,
            Authentication authentication
    ) {
        User userRequest = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                service.getMyNonconformitiesByStatus(
                        status,
                        userRequest,
                        includeAll,
                        pageable
                )
        );
    }


    //Routes Admin

    @PutMapping("/admin/{id}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable Long id,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        service.approve(id, user);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/{id}/correction")
    public ResponseEntity<Void> correction(
            @PathVariable Long id,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        service.sendToCorrection(id, user);

        return ResponseEntity.noContent().build();
    }



}
