package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.entregadordto.*;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Entregadores")
public class EntregadorControllerTests {

    final String URI_ENTREGADORES = "/entregadores";

    @Autowired
    MockMvc driver;

    @Autowired
    EntregadorRepository entregadorRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Entregador entregador;

    EntregadorBodyDTO entregadorBodyDTO;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        entregador = entregadorRepository.save(Entregador.builder()
                .nomeEntregador("Lana Del Rey")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("moto")
                .codigoEntregador("123456")
                .build()
        );
        entregadorBodyDTO = EntregadorBodyDTO.builder()
                .nomeEntregador(entregador.getNomeEntregador())
                .codigoEntregador(entregador.getCodigoEntregador())
                .placaVeiculo(entregador.getPlacaVeiculo())
                .corVeiculo(entregador.getCorVeiculo())
                .tipoVeiculo(entregador.getTipoVeiculo())
                .build();
    }

    @AfterEach
    void tearDown() {
        entregadorRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de fluxos básicos API Rest")
    class EntregadorVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando buscamos por todos entregadores salvos")
        void quandoBuscamosTodosEntregadores() throws Exception {
            // Arrange
            // Vamos ter 3 entregadores no banco
            Entregador entregador1 = Entregador.builder()
                    .nomeEntregador("Jose")
                    .placaVeiculo("GHF-1212")
                    .corVeiculo("Prata")
                    .tipoVeiculo("carro")
                    .codigoEntregador("654321")
                    .build();
            Entregador entregador2 = Entregador.builder()
                    .nomeEntregador("Halloran")
                    .placaVeiculo("MRD-0217")
                    .corVeiculo("Preto")
                    .tipoVeiculo("carro")
                    .codigoEntregador("217217")
                    .build();
            entregadorRepository.saveAll(Arrays.asList(entregador1, entregador2));

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Entregador> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(3, resultado.size());
        }

        @Test
        @DisplayName("Quando buscamos por um entregador salvo pelo id")
        void quandoBuscamosEntregadorPorId() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(entregador.getIdEntregador(), resultado.getIdEntregador()),
                    () -> assertEquals(entregador.getNomeEntregador(), resultado.getNomeEntregador()),
                    () -> assertEquals(entregador.getPlacaVeiculo(), resultado.getPlacaVeiculo()),
                    () -> assertEquals(entregador.getCorVeiculo(), resultado.getCorVeiculo()),
                    () -> assertEquals(entregador.getTipoVeiculo(), resultado.getTipoVeiculo())
            );
        }

        @Test
        @DisplayName("Quando buscamos um entregador inexistente")
        void quandoBuscamosEntregadorInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ENTREGADORES + "/999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando criamos um entregador com dados válidos")
        void quandoCriamosEntregadorValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_ENTREGADORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(entregadorBodyDTO.getNomeEntregador(), resultado.getNomeEntregador()),
                    () -> assertEquals(entregadorBodyDTO.getPlacaVeiculo(), resultado.getPlacaVeiculo()),
                    () -> assertEquals(entregadorBodyDTO.getCorVeiculo(), resultado.getCorVeiculo()),
                    () -> assertEquals(entregadorBodyDTO.getTipoVeiculo(), resultado.getTipoVeiculo())
            );
        }

        @Test
        @DisplayName("Quando alteramos o entregador com dados válidos")
        void quandoAlteramosEntregadorValido() throws Exception {
            // Arrange
            Long entregadorIdEntregador = entregador.getIdEntregador();

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(entregadorIdEntregador, resultado.getIdEntregador().longValue()),
                    () -> assertEquals(entregadorBodyDTO.getNomeEntregador(), resultado.getNomeEntregador()),
                    () -> assertEquals(entregadorBodyDTO.getPlacaVeiculo(), resultado.getPlacaVeiculo()),
                    () -> assertEquals(entregadorBodyDTO.getCorVeiculo(), resultado.getCorVeiculo()),
                    () -> assertEquals(entregadorBodyDTO.getTipoVeiculo(), resultado.getTipoVeiculo())
            );
        }

        @Test
        @DisplayName("Quando alteramos um entregador inexistente")
        void quandoAlteramosEntregadorInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos um entregador passando um código de acesso inválido")
        void quandoAlteramosEntregadorComCodigoEntregadorInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", "999999")
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando excluímos um entregador salvo")
        void quandoExcluimosEntregadorSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um entregador inexistente")
        void quandoExcluimosEntregadorInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ENTREGADORES + "/999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando excluímos um entregador passando um código de acesso inválido")
        void quandoExcluimosEntregadorComCodigoEntregadorInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ENTREGADORES + "/" + entregador.getIdEntregador() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", "999999"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }
    }


    @Nested
    @DisplayName("Conjunto de casos de verificação de nomeEntregador")
    class EntregadorVerificacaoNome {

        @Test
        @DisplayName("Quando alteramos o nomeEntregador do entregador com dados válidos")
        void quandoAlteramosNomeDoEntregadorValido() throws Exception {
            // Arrange
            entregadorBodyDTO.setNomeEntregador("Lana Del Rey Alterada");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertEquals("Lana Del Rey Alterada", resultado.getNomeEntregador());
        }

        @Test
        @DisplayName("Quando alteramos o entregador com nomeEntregador vazio")
        void quandoAlteramosEntregadorComNomeVazio() throws Exception {
            // Arrange
            entregadorBodyDTO.setNomeEntregador("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome de Entregador Obrigatorio!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o entregador passando codigo de acesso inválido")
        void quandoAlteramosEntregadorComCodigoEntregadorInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", "999999")
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de placa")
    class EntregadorVerificacaoPlaca {

        @Test
        @DisplayName("Quando alteramos o entregador com placa válida")
        void quandoAlteramosEntregadorComPlacaValida() throws Exception {
            // Arrange
            entregadorBodyDTO.setPlacaVeiculo("DEF-3456");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertEquals("DEF-3456", resultado.getPlacaVeiculo());
        }

        @Test
        @DisplayName("Quando alteramos o entregador com placa vazia")
        void quandoAlteramosEntregadorComPlacaVazia() throws Exception {
            // Arrange
            entregadorBodyDTO.setPlacaVeiculo("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Placa de Veiculo Obrigatoria!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o entregador passando codigo de acesso inválido")
        void quandoAlteramosEntregadorComCodigoEntregadorInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", "999999")
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de tipo")
    class EntregadorVerificacaoTipo {

        @Test
        @DisplayName("Quando alteramos o entregador com tipo de veiculo válido")
        void quandoAlteramosEntregadorComTipoVeiculoValido() throws Exception {
            // Arrange
            entregadorBodyDTO.setTipoVeiculo("carro");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertEquals("carro", resultado.getTipoVeiculo());
        }

        @Test
        @DisplayName("Quando alteramos o entregador com tipo de veiculo nulo")
        void quandoAlteramosEntregadorComTipoVeiculoVazio() throws Exception {
            // Arrange
            entregadorBodyDTO.setTipoVeiculo(null);

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tipo de Veiculo Obrigatorio!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o entregador com tipo de veiculo inválido")
        void quandoAlteramosEntregadorComTipoVeiculoInvalido() throws Exception {
            // Arrange
            entregadorBodyDTO.setTipoVeiculo("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Tipo de Veiculo Obrigatorio!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o tipo passando codigo de acesso inválido")
        void quandoAlteramosEntregadorComCodigoEntregadorInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", "999999")
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de cor do veiculo")
    class EntregadorVerificacaoCorVeiculo {

        @Test
        @DisplayName("Quando alteramos o entregador com cor do veiculo válida")
        void quandoAlteramosEntregadorComCorVeiculoValida() throws Exception {
            // Arrange
            entregadorBodyDTO.setCorVeiculo("preto");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertEquals("preto", resultado.getCorVeiculo());
        }

        @Test
        @DisplayName("Quando alteramos o entregador com cor do veiculo vazia")
        void quandoAlteramosEntregadorComCorVeiculoVazia() throws Exception {
            // Arrange
            entregadorBodyDTO.setCorVeiculo("");

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Cor de Veiculo Obrigatoria!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos a cor do veiculo passando codigo de acesso inválido")
        void quandoAlteramosEntregadorComCodigoEntregadorInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador() + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", "999999")
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de alteração de disponibilidade do entregador")
    class EntregadorDefinirDisponibilidade {

        @Test
        @DisplayName("Quando alteramos a disponibilidade do entregador para disponível")
        void quandoAlteramosDisponibilidadeParaDisponivel() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador() + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .param("disponibilidade", "true")
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertTrue(resultado.getDisponibilidade());
        }
 
        @Test
        @DisplayName("Quando alteramos a disponibilidade do entregador para indisponível")
        void quandoAlteramosDisponibilidadeParaIndisponivel() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador() + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .param("disponibilidade", "false")
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Entregador resultado = objectMapper.readValue(responseJsonString, Entregador.EntregadorBuilder.class).build();

            // Assert
            assertFalse(resultado.getDisponibilidade());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um entregador inexistente")
        void quandoAlteramosDisponibilidadeDeEntregadorInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/999999/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .param("disponibilidade", "true")
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O entregador consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos a disponibilidade de um entregador passando codigo de acesso inválido")
        void quandoAlteramosDisponibilidadeDeEntregadorComCodigoEntregadorInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ENTREGADORES + "/" + entregador.getIdEntregador() + "/disponibilidade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", "999999")
                            .param("disponibilidade", "true")
                            .content(objectMapper.writeValueAsString(entregadorBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Não autorizado!", resultado.getMessage());
        }
       
    }
    
}
