package blessed.sector.controller;
import blessed.sector.entity.Sector;
import blessed.sector.dto.SectorRequestDTO;
import blessed.sector.dto.SectorResponseDTO;
import blessed.nonconformity.service.SectorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sectors")
public class SectorController {

    private final SectorService service;
    public SectorController(SectorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SectorResponseDTO>> getAll(){
        List<SectorResponseDTO> sectors = service.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(sectors);

    }

    @PostMapping
    public ResponseEntity<SectorResponseDTO> createSector(@Valid @RequestBody SectorRequestDTO data){
        Sector sector = service.create(data);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SectorResponseDTO(sector));
    }
}
