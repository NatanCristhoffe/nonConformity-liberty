package blessed.nonconformity.service.query;

import blessed.exception.BusinessException;
import blessed.sector.entity.Sector;
import blessed.sector.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectorQuery {
    @Autowired
    SectorRepository sectorRepository;

    public Sector byId(Long id){
        return sectorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Setor não encontrado"));
    }


}
