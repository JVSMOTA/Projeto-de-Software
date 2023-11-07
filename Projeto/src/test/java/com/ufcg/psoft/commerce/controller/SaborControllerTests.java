package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import com.ufcg.psoft.commerce.dto.sabordto.*;
import com.ufcg.psoft.commerce.exception.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Sabores")
public class SaborControllerTests {

    final String URI_SABORES = "/sabores";

    @Autowired
    MockMvc driver;

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Sabor sabor;

    Estabelecimento estabelecimento;

    SaborBodyDTO saborBodyDTO;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        sabor = saborRepository.save(Sabor.builder()
                .nomeSabor("Calabresa")
                .tipoSabor("salgado")
                .valorMedioSabor(10.0)
                .valorGrandeSabor(15.0)
                .disponivel(true)
                .build());
        saborBodyDTO = saborBodyDTO.builder()
                .idSabor(sabor.getIdSabor())
                .nomeSabor(sabor.getNomeSabor())
                .tipoSabor(sabor.getTipoSabor())
                .valorMedioSabor(sabor.getValorMedioSabor())
                .valorGrandeSabor(sabor.getValorGrandeSabor())
                .disponivel(sabor.getDisponivel())
                .build();
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoEstabelecimento("654321")
                .build());
    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class SaborVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando buscamos por todos sabores salvos")
        void quandoBuscamosPorTodosSaboresSalvos() throws Exception {
            // Arrange
            // Vamos ter 3 sabores no banco
            Sabor sabor1 = Sabor.builder()
                    .nomeSabor("Chocolate")
                    .tipoSabor("doce")
                    .valorMedioSabor(10.0)
                    .valorGrandeSabor(15.0)
                    .disponivel(true)
                    .build();
            Sabor sabor2 = Sabor.builder()
                    .nomeSabor("Frango")
                    .tipoSabor("salgado")
                    .valorMedioSabor(10.0)
                    .valorGrandeSabor(15.0)
                    .disponivel(true)
                    .build();
            saborRepository.saveAll(Arrays.asList(sabor1, sabor2));

            // Act
            String responseJsonString = driver.perform(get(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Sabor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos um sabor salvo pelo id")
        void quandoBuscamosPorUmSaborSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Sabor resultado = objectMapper.readValue(responseJsonString, Sabor.SaborBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(sabor.getIdSabor().longValue(), resultado.getIdSabor().longValue()),
                    () -> assertEquals(sabor.getNomeSabor(), resultado.getNomeSabor()),
                    () -> assertEquals(sabor.getTipoSabor(), resultado.getTipoSabor()),
                    () -> assertEquals(sabor.getValorMedioSabor(), resultado.getValorMedioSabor()),
                    () -> assertEquals(sabor.getValorGrandeSabor(), resultado.getValorGrandeSabor()),
                    () -> assertEquals(sabor.getDisponivel(), resultado.getDisponivel())
            );
        }

        @Test
        @DisplayName("Quando buscamos um sabor inexistente")
        void quandoBuscamosPorUmSaborInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_SABORES + "/999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos um sabor com código de acesso inválido")
        void quandoBuscamosPorUmSaborComCodigoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_SABORES + "/" + sabor.getIdSabor() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", "999999")
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo sabor com dados válidos")
        void quandoCriarSaborValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_SABORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborBodyDTO resultado = objectMapper.readValue(responseJsonString, SaborBodyDTO.SaborBodyDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(saborBodyDTO.getNomeSabor(), resultado.getNomeSabor()),
                    () -> assertEquals(saborBodyDTO.getTipoSabor(), resultado.getTipoSabor()),
                    () -> assertEquals(saborBodyDTO.getValorMedioSabor(), resultado.getValorMedioSabor()),
                    () -> assertEquals(saborBodyDTO.getValorGrandeSabor(), resultado.getValorGrandeSabor()),
                    () -> assertEquals(saborBodyDTO.getDisponivel(), resultado.getDisponivel())
            );
        }

        @Test
        @DisplayName("Quando alteramos o sabor com dados válidos")
        void quandoAlteramosSaborValido() throws Exception {
            // Arrange
            Long saborId = sabor.getIdSabor();

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Sabor resultado = objectMapper.readValue(responseJsonString, Sabor.SaborBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getIdSabor().longValue(), saborId),
                    () -> assertEquals(saborBodyDTO.getNomeSabor(), resultado.getNomeSabor()),
                    () -> assertEquals(saborBodyDTO.getTipoSabor(), resultado.getTipoSabor()),
                    () -> assertEquals(saborBodyDTO.getValorMedioSabor(), resultado.getValorMedioSabor()),
                    () -> assertEquals(saborBodyDTO.getValorGrandeSabor(), resultado.getValorGrandeSabor()),
                    () -> assertEquals(saborBodyDTO.getDisponivel(), resultado.getDisponivel())
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor inexistente")
        void quandoAlteramosSaborInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + "999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor passando código de acesso inválido")
        void quandoAlteramosSaborCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", "999999")
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um sabor salvo")
        void quandoExcluimosSaborValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento()))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um sabor inexistente")
        void quandoExcluimosSaborInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_SABORES + "/" + "999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento()))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um sabor passando código de acesso inválido")
        void quandoExcluimosSaborCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_SABORES + "/" + sabor.getIdSabor() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", "999999"))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado nao existe!", resultado.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de nome")
    class SaborVerificacaoNome {

        @Test
        @DisplayName("Quando alteramos um sabor com nome válido")
        void quandoAlteramosSaborNomeValido() throws Exception {
            // Arrange
            saborBodyDTO.setNomeSabor("Portuguesa");

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSabor", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborBodyDTO resultado = objectMapper.readValue(responseJsonString, SaborBodyDTO.SaborBodyDTOBuilder.class).build();

            // Assert
            assertEquals("Portuguesa", resultado.getNomeSabor());
        }

        @Test
        @DisplayName("Quando alteramos um sabor com nome vazio")
        void quandoAlteramosSaborNomeVazio() throws Exception {
            // Arrange
            saborBodyDTO.setNomeSabor("");

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome de Sabor Obrigatorio!", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de tipo")
    class SaborVerificacaoTipo {

        @Test
        @DisplayName("Quando alteramos um sabor com tipo válido")
        void quandoAlteramosSaborTipoValido() throws Exception {
            // Arrange
            saborBodyDTO.setTipoSabor("salgado");

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSabor", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborBodyDTO resultado = objectMapper.readValue(responseJsonString, SaborBodyDTO.SaborBodyDTOBuilder.class).build();

            // Assert
            assertEquals("salgado", resultado.getTipoSabor());
        }

        @Test
        @DisplayName("Quando alteramos um sabor com tipo nulo")
        void quandoAlteramosSaborTipoNulo() throws Exception {
            // Arrange
            saborBodyDTO.setTipoSabor(null);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSabor", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tipo de Sabor Obrigatorio!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com tipo inválido")
        void quandoAlteramosSaborTipoInvalido() throws Exception {
            // Arrange
            saborBodyDTO.setTipoSabor("tipo invalido");

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tipo deve ser salgado ou doce", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de preco")
    class SaborVerificacaoPreco {

        @Test
        @DisplayName("Quando alteramos um sabor com precos válidos")
        void quandoAlteramosSaborPrecosValidos() throws Exception {
            // Arrange
            saborBodyDTO.setValorMedioSabor(40.0);
            saborBodyDTO.setValorGrandeSabor(60.0);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSabor", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborBodyDTO resultado = objectMapper.readValue(responseJsonString, SaborBodyDTO.SaborBodyDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(40.0, resultado.getValorMedioSabor()),
                    () -> assertEquals(60.0, resultado.getValorGrandeSabor())
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com precos nulos")
        void quandoAlteramosSaborPrecosVazios() throws Exception {
            // Arrange
            saborBodyDTO.setValorMedioSabor(null);
            saborBodyDTO.setValorGrandeSabor(null);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSabor", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertTrue(resultado.getErrors().contains("Valor Medio Obrigatorio!")),
                    () -> assertTrue(resultado.getErrors().contains("Valor Grande Obrigatorio!"))
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com precos inválidos")
        void quandoAlteramosSaborPrecosInvalidos() throws Exception {
            // Arrange
            saborBodyDTO.setValorMedioSabor(-10.0);
            saborBodyDTO.setValorGrandeSabor(-250.0);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("saborId", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertTrue(resultado.getErrors().contains("Valor medio deve ser maior que zero")),
                    () -> assertTrue(resultado.getErrors().contains("Valor grande deve ser maior que zero"))
            );
        }

        @Test
        @DisplayName("Quando alteramos um sabor com precos válidos e inválidos")
        void quandoAlteramosSaborPrecosValidosEInvalidos() throws Exception {
            // Arrange
            saborBodyDTO.setValorMedioSabor(40.0);
            saborBodyDTO.setValorGrandeSabor(-250.0);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSabor", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Valor grande deve ser maior que zero", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de disponibilidade")
    class SaborVerificacaoDisponibilidade {

        @Test
        @DisplayName("Quando alteramos um sabor com disponibilidade válida")
        void quandoAlteramosSaborDisponibilidadeValida() throws Exception {
            // Arrange
            saborBodyDTO.setDisponivel(false);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idSabor", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborBodyDTO resultado = objectMapper.readValue(responseJsonString, SaborBodyDTO.SaborBodyDTOBuilder.class).build();

            // Assert
            assertFalse(resultado.getDisponivel());
        }

        @Test
        @DisplayName("Quando alteramos um sabor com disponibilidade nula")
        void quandoAlteramosSaborDisponibilidadeNula() throws Exception {
            // Arrange
            saborBodyDTO.setDisponivel(null);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("saborId", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Disponibilidade Obrigatoria!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um sabor para false")
        void quandoAlteramosDisponibilidadeSaborFalse() throws Exception {
            // Arrange
            saborBodyDTO.setDisponivel(false);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("saborId", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborBodyDTO resultado = objectMapper.readValue(responseJsonString, SaborBodyDTO.SaborBodyDTOBuilder.class).build();

            // Assert
            assertFalse(resultado.getDisponivel());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um sabor para true")
        void quandoAlteramosDisponibilidadeSaborTrue() throws Exception {
            // Arrange
            saborBodyDTO.setDisponivel(true);
            saborBodyDTO.setDisponivel(false);

            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("saborId", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            SaborBodyDTO resultado = objectMapper.readValue(responseJsonString, SaborBodyDTO.SaborBodyDTOBuilder.class).build();

            // Assert
            assertFalse(resultado.getDisponivel());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um sabor com o código de acesso errado")
        void quandoAlteramosDisponibilidadeSaborCodigoErrado() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(put(URI_SABORES + "/" + sabor.getIdSabor() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("saborId", sabor.getIdSabor().toString())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", "aaaaaa")
                            .param("disponibilidade", String.valueOf(false))
                            .content(objectMapper.writeValueAsString(saborBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O sabor consultado nao existe!", resultado.getMessage());
        }
    }
}
