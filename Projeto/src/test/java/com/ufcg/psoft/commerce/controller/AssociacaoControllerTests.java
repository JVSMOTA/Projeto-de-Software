package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Associação")
class AssociacaoControllerTests {

    final String URI_ASSOCIACAO = "/associacoes";

    @Autowired
    MockMvc driver;

    @Autowired
    AssociacaoRepository associacaoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    Entregador entregador;

    Estabelecimento estabelecimento;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        entregador = entregadorRepository.save(Entregador.builder()
                .nomeEntregador("Entregador Um")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Branco")
                .tipoVeiculo("Carro")
                .codigoEntregador("123456")
                .build()
        );
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoEstabelecimento("654321")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        associacaoRepository.deleteAll();
        entregadorRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de criacao de associacao")
    class ClienteCriacaoAssociacao {

        @Test
        @DisplayName("Quando criamos uma associacao com sucesso")
        void testCriarAssociacaoComSucesso() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO + "/solicitarAssociacao/" + estabelecimento.getIdEstabelecimento() + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEntregador", entregador.getIdEntregador().toString())
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString()))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado = objectMapper.readValue(responseJsonString, Associacao.class);

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertNotNull(resultado.getIdAssociacao()),
                    () -> assertEquals(entregador.getIdEntregador(), resultado.getEntregador().getIdEntregador()),
                    () -> assertEquals(estabelecimento.getIdEstabelecimento(), resultado.getEstabelecimento().getIdEstabelecimento())
            );
        }

        @Test
        @DisplayName("Quando criamos uma associacao com entregador inexistente")
        void testCriarAssociacaoComEntregadorInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO + "/solicitarAssociacao/" + estabelecimento.getIdEstabelecimento() + "/232323")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(0, associacaoRepository.count()),
                    () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos uma associacao com estabelecimento inexistente")
        void testCriarAssociacaoComEstabelecimentoInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO + "/solicitarAssociacao/89989899" + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", entregador.getCodigoEntregador()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(0, associacaoRepository.count()),
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos uma associacao passando codigo de acesso invalido")
        void testCriarAssociacaoComCodigoDeAcessoInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_ASSOCIACAO + "/solicitarAssociacao/" + estabelecimento.getIdEstabelecimento() + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEntregador", "654321"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(0, associacaoRepository.count()),
                    () -> assertEquals("Não autorizado!", resultado.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de aprovação de associacao")
    class ClienteAprovacaoAssociacao {

        @BeforeEach
        void setUp() {
            associacaoRepository.save(Associacao.builder()
                    .entregador(entregador)
                    .estabelecimento(estabelecimento)
                    .statusAssociacao(true)
                    .build()
            );
        }

        @Test
        @DisplayName("Quando aprovamos uma associacao com sucesso")
        void quandoAprovamosAssociacaoComSucesso() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ASSOCIACAO + "/aceitarEntregador/" + estabelecimento.getIdEstabelecimento() + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Associacao resultado = objectMapper.readValue(responseJsonString, Associacao.AssociacaoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertEquals(true, resultado.getStatusAssociacao())
            );
        }

        @Test
        @DisplayName("Quando aprovamos uma associacao com entregador inexistente")
        void quandoAprovamosAssociacaoComEntregadorInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ASSOCIACAO + "/aceitarEntregador/" + estabelecimento.getIdEstabelecimento() + "/9999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertEquals("O entregador consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando aprovamos uma associacao com estabelecimento inexistente")
        void quandoAprovamosAssociacaoComEstabelecimentoInexistente() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ASSOCIACAO + "/aceitarEntregador/3535353/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando aprovamos uma associacao passando codigo de acesso invalido")
        void quandoAprovamosAssociacaoComCodigoDeAcessoInvalido() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(put(URI_ASSOCIACAO + "/aceitarEntregador/" + estabelecimento.getIdEstabelecimento() + "/" + entregador.getIdEntregador())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", "123"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals(1, associacaoRepository.count()),
                    () -> assertEquals("Não autorizado!", resultado.getMessage())
            );
        }
    }
}
