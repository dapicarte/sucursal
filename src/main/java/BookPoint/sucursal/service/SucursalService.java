package BookPoint.sucursal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import BookPoint.sucursal.model.BodegaDTO;
import BookPoint.sucursal.model.PedidoDTO;
import BookPoint.sucursal.model.Sucursal;
import BookPoint.sucursal.model.TrasladoDTO;
import BookPoint.sucursal.repository.SucursalRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class SucursalService {
    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private RestTemplate restTemplate;

    public BodegaDTO obtenerBodega(Long idBodega) {
        String url = "http://localhost:8082/api/bodegas/" + idBodega;
        return restTemplate.getForObject(url, BodegaDTO.class);
    }

    // public TrasladoDTO obtenerTraslado(Long idTraslado) {
    //     String url = "http://localhost:8084/api/traslados/" + idTraslado;
    //     return restTemplate.getForObject(url, TrasladoDTO.class);
    // }

    public Sucursal crearSucursal(Sucursal sucursal) {
        BodegaDTO bodega = obtenerBodega(sucursal.getIdBodega());
        if (bodega != null) {
        }

    return sucursalRepository.save(sucursal);
    }

}
