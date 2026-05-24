package BookPoint.sucursal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BookPoint.sucursal.service.SucursalService;

@RestController
@RequestMapping("api/sucursales")
public class SucursalController {
    @Autowired
    private SucursalService sucursalService;
}
