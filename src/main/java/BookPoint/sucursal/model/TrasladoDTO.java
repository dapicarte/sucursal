package BookPoint.sucursal.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TrasladoDTO {
    private Long idTraslado;
    private LocalDate fechaTraslado;
    private boolean estadoEnviado;
}
