package blessed.nonconformity.service;

import blessed.exception.BusinessException;
import blessed.nonconformity.entity.FiveWhy;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.entity.RootCause;

public class RootCauseService {

    public void addFiveWhy(
            RootCause rootCause,
            FiveWhy newFiveWhy,
            NonConformity nc
            ){
        if (rootCause.getFiveWhys().size() >= 5){
            throw new BusinessException("Uma causa raiz não pode ter mais de 5 porquês.");
        }
        if (newFiveWhy.getLevel() < 1 || newFiveWhy.getLevel() > 5) {
            throw new BusinessException("O nível do porquê deve estar entre 1 e 5.");
        }

        newFiveWhy.setRootCause(rootCause);

        if (rootCause.getFiveWhys().contains(newFiveWhy)) {
            throw new BusinessException("Já existe um porquê para esse nível.");
        }

        rootCause.getFiveWhys().add(newFiveWhy);
    }

}
