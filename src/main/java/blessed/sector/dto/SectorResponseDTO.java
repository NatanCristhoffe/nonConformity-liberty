package blessed.sector.dto;

import blessed.sector.entity.Sector;

import java.time.Instant;

public record SectorResponseDTO(
        Long id, String name,
        String description, boolean active,
        Instant createdAr
        ){
    public SectorResponseDTO(Sector entity){
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
