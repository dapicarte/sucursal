package BookPoint.sucursal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import BookPoint.sucursal.model.Sucursal;
import BookPoint.sucursal.model.SucursalDTO;
import BookPoint.sucursal.service.SucursalService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SucursalController.class)
@ActiveProfiles("test")
public class SucursalControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @SuppressWarnings("removal")
        @MockitoBean
        private SucursalService sucursalService;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Test
        void testListarSucursales() throws Exception {
                Sucursal s1 = new Sucursal(1L, "Calle 1", "09:00-18:00", 10L);
                Sucursal s2 = new Sucursal(2L, "Calle 2", "10:00-19:00", 20L);

                Mockito.when(sucursalService.listarSucursales()).thenReturn(Arrays.asList(s1, s2));

                mockMvc.perform(get("/api/v1/sucursales"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].direccionSucursal", is("Calle 1")))
                                .andExpect(jsonPath("$[1].horario", is("10:00-19:00")));
        }

        @Test
        void testListarSucursalesVacio() throws Exception {
                Mockito.when(sucursalService.listarSucursales()).thenReturn(Collections.emptyList());

                mockMvc.perform(get("/api/v1/sucursales"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testCrearSucursal() throws Exception {
                Sucursal nueva = new Sucursal(null, "Calle 1", "09:00-18:00", 10L);
                Sucursal guardada = new Sucursal(1L, "Calle 1", "09:00-18:00", 10L);

                Mockito.when(sucursalService.crearSucursal(any(Sucursal.class))).thenReturn(guardada);

                mockMvc.perform(post("/api/v1/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nueva)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.idSucursal").value(1L))
                                .andExpect(jsonPath("$.direccionSucursal").value("Calle 1"))
                                .andExpect(jsonPath("$.horario").value("09:00-18:00"))
                                .andExpect(jsonPath("$.idBodega").value(10L));
        }

        @Test
        void testCrearSucursalError() throws Exception {
                Sucursal nueva = new Sucursal(null, "Calle 1", "09:00-18:00", 10L);

                Mockito.when(sucursalService.crearSucursal(any(Sucursal.class)))
                                .thenThrow(new RuntimeException("Error en BD"));

                mockMvc.perform(post("/api/v1/sucursales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nueva)))
                                .andExpect(status().isConflict());
        }

        @Test
        void testObtenerSucursalExistente() throws Exception {
                Sucursal buscado = new Sucursal(1L, "Calle 1", "09:00-18:00", 10L);

                Mockito.when(sucursalService.findById(1L)).thenReturn(Optional.of(buscado));

                mockMvc.perform(get("/api/v1/sucursales/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.idSucursal").value(1L))
                                .andExpect(jsonPath("$.direccionSucursal").value("Calle 1"));
        }

        @Test
        void testObtenerSucursalNoExistente() throws Exception {
                Mockito.when(sucursalService.findById(99L)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/v1/sucursales/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testObtenerDetalleExistente() throws Exception {
                SucursalDTO dto = new SucursalDTO();
                dto.setIdSucursal(1L);
                dto.setDireccionSucursal("Calle 1");
                dto.setHorario("09:00-18:00");
                dto.setNombreBodega("Bodega Central");
                dto.setCapacidadMax(500);

                Mockito.when(sucursalService.obtenerSucursalDTO(1L)).thenReturn(dto);

                mockMvc.perform(get("/api/v1/sucursales/1/detalle"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.idSucursal").value(1L))
                                .andExpect(jsonPath("$.nombreBodega").value("Bodega Central"))
                                .andExpect(jsonPath("$.capacidadMax").value(500));
        }

        @Test
        void testObtenerDetalleNoExistente() throws Exception {
                Mockito.when(sucursalService.obtenerSucursalDTO(99L)).thenReturn(null);

                mockMvc.perform(get("/api/v1/sucursales/99/detalle"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testConsultarStockExistente() throws Exception {
                Mockito.when(sucursalService.consultarStock(1L)).thenReturn("Stock: 120 unidades");

                mockMvc.perform(get("/api/v1/sucursales/1/stock"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Stock: 120 unidades"));
        }

        @Test
        void testConsultarStockNoExistente() throws Exception {
                Mockito.when(sucursalService.consultarStock(99L)).thenReturn(null);

                mockMvc.perform(get("/api/v1/sucursales/99/stock"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testActualizarSucursal() throws Exception {
                Sucursal datosNuevos = new Sucursal(null, "Calle Nueva", "10:00-20:00", 20L);
                Sucursal actualizada = new Sucursal(1L, "Calle Nueva", "10:00-20:00", 20L);

                Mockito.when(sucursalService.actualizarSucursal(eq(1L), any(Sucursal.class)))
                                .thenReturn(actualizada);

                mockMvc.perform(put("/api/v1/sucursales/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(datosNuevos)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.idSucursal").value(1L))
                                .andExpect(jsonPath("$.direccionSucursal").value("Calle Nueva"))
                                .andExpect(jsonPath("$.horario").value("10:00-20:00"))
                                .andExpect(jsonPath("$.idBodega").value(20L));
        }

        @Test
        void testActualizarSucursalNoExistente() throws Exception {
                Sucursal datosNuevos = new Sucursal(null, "Calle Nueva", "10:00-20:00", 20L);

                Mockito.when(sucursalService.actualizarSucursal(eq(99L), any(Sucursal.class)))
                                .thenReturn(null);

                mockMvc.perform(put("/api/v1/sucursales/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(datosNuevos)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testEliminarSucursal() throws Exception {
                Mockito.when(sucursalService.eliminarSucursal(1L)).thenReturn(true);

                mockMvc.perform(delete("/api/v1/sucursales/1"))
                                .andExpect(status().isOk());
        }

        @Test
        void testEliminarSucursalNoExistente() throws Exception {
                Mockito.when(sucursalService.eliminarSucursal(99L)).thenReturn(false);

                mockMvc.perform(delete("/api/v1/sucursales/99"))
                                .andExpect(status().isNotFound());
        }
}