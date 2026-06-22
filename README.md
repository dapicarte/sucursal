# BookPoint · Microservicio de Sucursal (`ms-sucursal`)

Microservicio encargado de la gestión de sucursales dentro del sistema **BookPoint**. Expone una API REST y se comunica con los microservicios de bodega e inventario para obtener información de stock y detalles de bodega asociada.

---

## 🛠️ Tecnologías

- **Java 24**
- **Spring Boot 4.0.6**
- **Spring Web MVC**
- **Spring Data JPA**
- **MySQL** (entorno de producción/desarrollo)
- **H2** (base de datos en memoria para tests)
- **Lombok** — reducción de código boilerplate
- **Jackson** — serialización/deserialización JSON
- **Spring Boot Actuator** — monitoreo del microservicio
- **RestTemplate** — comunicación con otros microservicios
- **JUnit 5 + Mockito** — pruebas unitarias e integración

---

## 🏗️ Rol en la arquitectura

`ms-sucursal` consulta a los microservicios de bodega e inventario para enriquecer sus respuestas con datos de bodega y stock disponible.

| Microservicio | Para qué se consulta |
|---------------|----------------------|
| `ms-bodega` | Obtener nombre y capacidad de la bodega asociada |
| `ms-inventario` | Consultar stock disponible por bodega |

```
Cliente → Gateway (8080) → ms-sucursal (8092)
                                │
                                ├── RestTemplate → ms-bodega (8093)
                                └── RestTemplate → ms-inventario (8091)
```

---

## ✅ Requisitos previos

- JDK 24 o superior
- Maven 3.8+
- MySQL en ejecución (para el perfil por defecto)
- MS Bodega y MS Inventario corriendo si vas a probar el flujo completo

---

## ⚙️ Configuración

### `src/main/resources/application.properties`

```properties
spring.application.name=sucursal
server.port=8092

spring.datasource.url=jdbc:mysql://localhost:3306/sucursaldb
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### `src/test/resources/application-test.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE
```

---

## 🌐 Acceso vía API Gateway

```
GET http://localhost:8080/api/v1/sucursales
```

Rutas configuradas en el gateway:

```yaml
- id: ms-sucursal
  uri: http://localhost:8092
  predicates:
    - Path=/api/v1/sucursales,/api/v1/sucursales/**
```

---

## 📡 Endpoints

### Sucursal — Base: `/api/v1/sucursales`

| Método | Endpoint | Descripción | Código éxito |
|--------|----------|-------------|--------------|
| `GET` | `/api/v1/sucursales` | Listar todas las sucursales | `200 OK` |
| `GET` | `/api/v1/sucursales/{id}` | Obtener sucursal por ID | `200 OK` |
| `GET` | `/api/v1/sucursales/{id}/detalle` | Detalle con datos de bodega | `200 OK` |
| `GET` | `/api/v1/sucursales/{id}/stock` | Consultar stock de la sucursal | `200 OK` |
| `POST` | `/api/v1/sucursales` | Crear nueva sucursal | `200 OK` |
| `PUT` | `/api/v1/sucursales/{id}` | Actualizar sucursal | `200 OK` |
| `DELETE` | `/api/v1/sucursales/{id}` | Eliminar sucursal | `200 OK` |

### Ejemplo de respuesta detalle

```json
{
  "idSucursal": 1,
  "direccionSucursal": "Av. Libertad 123",
  "horario": "09:00 - 18:00",
  "nombreBodega": "Bodega Temuco",
  "capacidadMax": 100
}
```

### JSON de entrada para POST

```json
{
  "direccionSucursal": "Av. Libertad 123",
  "horario": "09:00 - 18:00",
  "idBodega": 1
}
```

---

## 🗂️ Modelos de datos

### `Sucursal`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `idSucursal` | `Long` | Identificador único (PK) |
| `direccionSucursal` | `String` | Dirección de la sucursal |
| `horario` | `String` | Horario de atención |
| `idBodega` | `Long` | ID de la bodega asociada |

---

- **Pruebas unitarias:** `@ExtendWith(MockitoExtension.class)` con `@Mock` / `@InjectMocks`.
- **Pruebas de integración:** `@SpringBootTest` + `@AutoConfigureMockMvc` + `@ActiveProfiles("test")`.
- Las llamadas a otros microservicios (`RestTemplate`) se mockean con Mockito.

---

## 📁 Estructura del proyecto

```
ms-sucursal/
├── src/
│   ├── main/
│   │   ├── java/BookPoint/sucursal/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── SucursalApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/BookPoint/sucursal/
│       │   ├── controller/
│       │   └── service/
│       └── resources/
│           └── application-test.properties
└── pom.xml
```

---

## 👤 Autor

Proyecto **BookPoint** — Microservicio de Sucursal.