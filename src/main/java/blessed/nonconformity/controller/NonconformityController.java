package blessed.nonconformity.controller;


import blessed.exception.BusinessException;
import blessed.nonconformity.dto.NonconformityUpdateDTO;
import blessed.nonconformity.dto.PageResponseDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.service.NonconformityService;
import blessed.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin()
@RestController
@RequestMapping("/nonconformity")
public class NonconformityController {

    private final NonconformityService service;
    public NonconformityController(NonconformityService service){
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NonconformityResponseDTO> create(
            @RequestPart("data") @Valid NonconformityRequestDTO data,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        NonConformity nonconformity = service.create(data, user, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new NonconformityResponseDTO(nonconformity));
    }

    @GetMapping
    public ResponseEntity<Page<NonconformityResponseDTO>> getAllOrGetByUser(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "false") boolean getAll,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getAllOrGetByUser(user, getAll, pageable));
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
    public ResponseEntity<PageResponseDTO<NonconformityResponseDTO>> getByStatus(
            @RequestParam NonConformityStatus status,
            @RequestParam(defaultValue = "false") boolean includeAll,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        Page<NonconformityResponseDTO> page =
                service.getMyNonconformitiesByStatus(
                        status,
                        user,
                        includeAll,
                        pageable
                );

        return ResponseEntity.ok(PageResponseDTO.from(page));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateNonconformity(
            @PathVariable Long id,
            @Valid @RequestPart("data") NonconformityUpdateDTO data,
            @RequestPart(value = "file", required = false)  MultipartFile file,
            @AuthenticationPrincipal User userRequest
            ){
        service.update(id, data, userRequest, file);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("Success", "Nc atualizada com sucesso."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable Long id,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        service.approve(id, user);

        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/correction")
    public ResponseEntity<Void> correction(
            @PathVariable Long id,
            Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        service.sendToCorrection(id, user);

        return ResponseEntity.noContent().build();
    }

}
