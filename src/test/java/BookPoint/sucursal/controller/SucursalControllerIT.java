package BookPoint.sucursal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import BookPoint.sucursal.model.Sucursal;
import BookPoint.sucursal.repository.SucursalRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SucursalControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SucursalRepository sucursalRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanDb() {
        sucursalRepository.deleteAll();
    }

    @Test
    void testCrearYObtenerSucursal() throws Exception {
        Sucursal sucursal = new Sucursal(null, "Calle 1", "09:00-18:00", 10L);

        mockMvc.perform(post("/api/v1/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sucursal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSucursal").exists())
                .andExpect(jsonPath("$.direccionSucursal").value("Calle 1"));

        mockMvc.perform(get("/api/v1/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].direccionSucursal").value("Calle 1"))
                .andExpect(jsonPath("$[0].horario").value("09:00-18:00"))
                .andExpect(jsonPath("$[0].idBodega").value(10L));
    }

    @Test
    void testEliminarSucursal() throws Exception {
        Sucursal sucursal = new Sucursal(null, "Calle 1", "09:00-18:00", 10L);
        Sucursal guardada = sucursalRepository.save(sucursal);

        mockMvc.perform(delete("/api/v1/sucursales/" + guardada.getIdSucursal()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/sucursales/" + guardada.getIdSucursal()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarSucursal() throws Exception {
        Sucursal sucursal = new Sucursal(null, "Calle Vieja", "09:00-18:00", 10L);
        Sucursal guardada = sucursalRepository.save(sucursal);

        Sucursal actualizada = new Sucursal(null, "Calle Nueva", "10:00-20:00", 20L);

        mockMvc.perform(put("/api/v1/sucursales/" + guardada.getIdSucursal())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.horario").value("10:00-20:00"))
                .andExpect(jsonPath("$.direccionSucursal").value("Calle Nueva"));
    }
}