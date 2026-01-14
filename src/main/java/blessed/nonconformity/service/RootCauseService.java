package blessed.nonconformity.service;

import blessed.exception.BusinessException;
import blessed.exception.ResourceNotFoundException;
import blessed.nonconformity.dto.RootCauseRequestDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.entity.RootCause;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.repository.NonconformityRepository;
import blessed.nonconformity.repository.RootCauseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RootCauseService {
    private final RootCauseRepository rcRepository;
    private final NonconformityRepository ncRepository;

    public RootCauseService(
        RootCauseRepository rcRepository,
        NonconformityRepository ncRepository
    ){
        this.rcRepository = rcRepository;
        this.ncRepository = ncRepository;
    }

    @Transactional
    public RootCause create(Long ncId, RootCauseRequestDTO data){
        NonConformity nc = ncRepository.findById(ncId)
                .orElseThrow(() -> new ResourceNotFoundException("Não conformidade não encontrada para o ID informado."));

        if(nc.getStatus() != NonConformityStatus.WAITING_ROOT_CAUSE){
            throw new BusinessException(
                    "A não conformidade não está em um status que permita o cadastro de causa raiz."
            );
        }

        RootCause rootCause = new RootCause(data);
        rootCause.setNonconformity(nc);

        rcRepository.save(rootCause);
        return rootCause;
    }
}
