package blessed.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DataTimeUtils {

    private static final ZoneId BRAZIL_ZONE = ZoneId
            .of("America/Sao_Paulo");

    private static final DateTimeFormatter BRAZIL_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .withZone(BRAZIL_ZONE);

    public DataTimeUtils() {
    }

    public  static String formatNow(){
        return BRAZIL_FORMATTER.format(Instant.now());
    }
}
