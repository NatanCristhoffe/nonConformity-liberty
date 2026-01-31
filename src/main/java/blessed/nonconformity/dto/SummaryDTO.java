package blessed.nonconformity.dto;


import blessed.nonconformity.enums.NonConformityStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SummaryDTO {

    private Map<NonConformityStatus, Long> byStatus;
    private Long total;

}
