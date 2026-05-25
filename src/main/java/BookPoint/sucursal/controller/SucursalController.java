package BookPoint.sucursal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BookPoint.sucursal.model.Sucursal;
import BookPoint.sucursal.model.SucursalDTO;
import BookPoint.sucursal.service.SucursalService;

@RestController
@RequestMapping("api/v1/sucursales")
public class SucursalController {
    @Autowired
    private SucursalService sucursalService;

    @PostMapping
    public ResponseEntity<?> crearSucursal(@RequestBody Sucursal sucursal) {
        try {
            return new ResponseEntity<>(sucursalService.crearSucursal(sucursal), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear la sucursal", HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<?> listarSucursales() {
        List<Sucursal> sucursales = sucursalService.listarSucursales();
        if (sucursales.isEmpty()) {
            return new ResponseEntity<>("No existen sucursales registradas", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sucursales, HttpStatus.OK);
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> obtenerDetalle(@PathVariable Long id) {
        SucursalDTO dto = sucursalService.obtenerSucursalDTO(id);
        if (dto == null) {
            return new ResponseEntity<>("Sucursal con id " + id + " no existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<?> consultarStock(@PathVariable Long id) {
        String stock = sucursalService.consultarStock(id);
        if (stock == null) {
            return new ResponseEntity<>("Sucursal con id " + id + " no existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerSucursal(@PathVariable Long id) {
        Sucursal buscado = sucursalService.findById(id).orElse(null);
        if (buscado == null) {
            return new ResponseEntity<>("Sucursal con id " + id + " no existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(buscado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSucursal(@PathVariable Long id) {
        if (sucursalService.eliminarSucursal(id)) {
            return new ResponseEntity<>("Sucursal con id " + id + " eliminada correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Sucursal con id " + id + " no existe", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarSucursal(@PathVariable Long id, @RequestBody Sucursal sucursal) {
        Sucursal actualizado = sucursalService.actualizarSucursal(id, sucursal);
        if (actualizado == null) {
            return new ResponseEntity<>("Sucursal con id " + id + " no existe", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }
}
