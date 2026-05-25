# Microservicio de Sucursal

## server.port=8092

## Endpoints

### POST `/api/v1/sucursales`
Crea una nueva sucursal.

**JSON de entrada:**
```json
{
    "direccionSucursal": "Av. Libertad 123",
    "horario": "09:00 - 18:00",
    "idBodega": 1
}
```

---

### GET `/api/v1/sucursales`
Lista todas las sucursales registradas.

---

### GET `/api/v1/sucursales/{id}/detalle`
Obtiene el detalle de una sucursal con datos de su bodega asociada.

**Respuesta con MS Bodega levantado:**
```json
{
    "idSucursal": 1,
    "direccionSucursal": "Av. Libertad 123",
    "horario": "09:00 - 18:00",
    "nombreBodega": "Bodega Temuco",
    "capacidadMax": 100
}
```

---

### GET `/api/v1/sucursales/{id}/stock`
Consulta el stock disponible de la sucursal mediante su bodega asociada.

---

### PUT `/api/v1/sucursales/{id}`
Actualiza los datos de una sucursal.

**JSON de entrada:**
```json
{
    "direccionSucursal": "Av. Nueva 456",
    "horario": "10:00 - 19:00",
    "idBodega": 1
}
```

---

### DELETE `/api/v1/sucursales/{id}`
Elimina una sucursal por su id.

---

## Dependencias
| MS | Puerto | Para qué |
|---|---|---|
| MS Bodega | 8093 | Obtener datos de bodega y stock |