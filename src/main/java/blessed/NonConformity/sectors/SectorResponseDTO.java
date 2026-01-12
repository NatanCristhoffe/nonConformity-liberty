package blessed.NonConformity.sectors;

public record SectorResponseDTO(Long id, String name, String description, boolean active){
    public SectorResponseDTO(Sector entity){
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.isActive()
        );
    }
}
