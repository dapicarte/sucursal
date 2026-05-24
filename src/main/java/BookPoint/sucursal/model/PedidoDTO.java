package BookPoint.sucursal.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PedidoDTO {
    private Long idPedido;
    private String detallePedido;
    private LocalDate fechaPedido;
    private boolean estadoPago;
}
