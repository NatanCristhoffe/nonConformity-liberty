package blessed.sector.controller;
import blessed.sector.entity.Sector;
import blessed.sector.dto.SectorRequestDTO;
import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.service.SectorService;
import blessed.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/sectors")
public class SectorController {

    private final SectorService service;
    public SectorController(SectorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SectorResponseDTO>> getAll(
            @AuthenticationPrincipal User user
    ){
        List<SectorResponseDTO> sectors = service.getAll(user.getCompany().getId());
        return ResponseEntity.status(HttpStatus.OK).body(sectors);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<SectorResponseDTO> createSector(
            @Valid @RequestBody SectorRequestDTO data,
            @AuthenticationPrincipal User user
    ){
        Sector sector = service.create(data, user.getCompany().getId());
       return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SectorResponseDTO(sector));
    }

    @PutMapping("/{idSector}")
    public ResponseEntity<SectorResponseDTO> update(
            @PathVariable Long idSector,
            @RequestBody @Valid SectorRequestDTO dataUpdate,
            @AuthenticationPrincipal User user
            ){

        return ResponseEntity.ok(service.update(idSector, dataUpdate, user.getCompany().getId()));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{idSector}")
    public ResponseEntity<Map<String, String>> deleteSectors(@PathVariable Long idSector){
        service.disable(idSector);
        return ResponseEntity.ok(Map.of("success","setor deletado com sucesso."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/enable/{idSector}")
    public ResponseEntity<Map<String, String>> enableSectors(@PathVariable Long idSector){
        service.enable(idSector);
        return ResponseEntity.ok(Map.of("success","setor ativo com sucesso."));
    }

    @GetMapping("/get-by")
    public ResponseEntity<List<SectorResponseDTO>> getByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "false") boolean getNotActive,
            @AuthenticationPrincipal User userRequest){
        return ResponseEntity.ok(service.getByName(name, getNotActive, userRequest));
    }
}
