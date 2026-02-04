package blessed.sector.controller;
import blessed.sector.entity.Sector;
import blessed.sector.dto.SectorRequestDTO;
import blessed.sector.dto.SectorResponseDTO;
import blessed.sector.service.SectorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{idSector}")
    public ResponseEntity<SectorResponseDTO> update(
            @PathVariable Long idSector,
            @RequestBody @Valid SectorRequestDTO dataUpdate){

        return ResponseEntity.ok(service.update(idSector, dataUpdate));

    }

    @DeleteMapping("/admin/delete/{idSector}")
    public ResponseEntity<Map<String, String>> deleteSectors(@PathVariable Long idSector){
        service.disable(idSector);
        return ResponseEntity.ok(Map.of("success","setor deletado com sucesso."));
    }

    @PutMapping("/admin/enable/{idSector}")
    public ResponseEntity<Map<String, String>> enableSectors(@PathVariable Long idSector){
        service.enable(idSector);
        return ResponseEntity.ok(Map.of("success","setor ativo com sucesso."));
    }
}
