package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.estabelecimentodto.*;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de estabelecimentos")
public class EstabelecimentoControllerTests {
    @Autowired
    MockMvc driver;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    SaborRepository saborRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    ModelMapper modelMapper = new ModelMapper();

    Estabelecimento estabelecimento;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .nomeEstabelecimento("Nome Válido")
                .codigoEstabelecimento("123456")
                .build());
    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class EstabelecimentoVerificacaoFluxosBasicosApiRest {

        final String URI_ESTABELECIMENTOS = "/estabelecimentos";

        EstabelecimentoBodyDTO estabelecimentoPutRequestDTO;
        
        EstabelecimentoBodyDTO estabelecimentoPostRequestDTO;

        @BeforeEach
        void setup() {
            estabelecimentoPutRequestDTO = EstabelecimentoBodyDTO.builder()
                    .codigoEstabelecimento("123456")
                    .build();
            estabelecimentoPostRequestDTO = EstabelecimentoBodyDTO.builder()
                    .idEstabelecimento(estabelecimento.getIdEstabelecimento())
                    .nomeEstabelecimento(estabelecimento.getNomeEstabelecimento())
                    .codigoEstabelecimento("654321")
                    .build();
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados válidos")
        void quandoCriarEstabelecimentoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimentoPostRequestDTO.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EstabelecimentoBodyDTO resultado = objectMapper.readValue(responseJsonString, EstabelecimentoBodyDTO.EstabelecimentoBodyDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getIdEstabelecimento()),
                    () -> assertEquals(estabelecimentoPostRequestDTO.getCodigoEstabelecimento(), resultado.getCodigoEstabelecimento())
            );
        }

        @Test
        @DisplayName("Quando excluímos um estabelecimento salvo")
        void quandoExcluimosEstabelecimentoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_ESTABELECIMENTOS + "/" + estabelecimento.getIdEstabelecimento())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento()))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando atualizamos um estabelecimento salvo")
        void quandoAtualizamosEstabelecimentoValido() throws Exception {
            // Arrange
            estabelecimentoPutRequestDTO.setCodigoEstabelecimento("131289");
            estabelecimentoPutRequestDTO.setIdEstabelecimento(estabelecimento.getIdEstabelecimento());
            estabelecimentoPutRequestDTO.setNomeEstabelecimento("Novo Nome");

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getIdEstabelecimento())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(estabelecimentoPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            EstabelecimentoBodyDTO resultado = objectMapper.readValue(responseJsonString, EstabelecimentoBodyDTO.EstabelecimentoBodyDTOBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getIdEstabelecimento().longValue(), estabelecimento.getIdEstabelecimento().longValue()),
                    () -> assertEquals("131289", resultado.getCodigoEstabelecimento())
            );
        }


        @Test
        @DisplayName("Quando alteramos um estabelecimento com codigo de acesso inválido")
        void quandoAlterarEstabelecimentoInvalido() throws Exception {
            // Arrange
            EstabelecimentoBodyDTO estabelecimentoBodyDTO = EstabelecimentoBodyDTO.builder()
                    .idEstabelecimento(estabelecimento.getIdEstabelecimento())
                    .nomeEstabelecimento("Nome Válido")
                    .codigoEstabelecimento("13")
                    .build();

            // Act
            String responseJsonString = driver.perform(put(URI_ESTABELECIMENTOS + "/" + estabelecimento.getIdEstabelecimento())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(estabelecimentoBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando criamos um novo estabelecimento com dados inválidos")
        void quandoCriarEstabelecimentoInvalido() throws Exception {
            // Arrange
            EstabelecimentoBodyDTO estabelecimentoBodyDTO = EstabelecimentoBodyDTO.builder()
                    .idEstabelecimento(estabelecimento.getIdEstabelecimento())
                    .nomeEstabelecimento("Nome Válido")
                    .codigoEstabelecimento("13")
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_ESTABELECIMENTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimentoBodyDTO.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(estabelecimentoBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }

        @Transactional
        @Test
        @DisplayName("Quando buscamos o cardápio de um estabelecimento")
        void quandoBuscarCardapioEstabelecimento() throws Exception {
            // Arrange
            Sabor sabor1 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Calabresa")
                    .valorMedioSabor(25.0)
                    .valorGrandeSabor(35.0)
                    .tipoSabor("salgado")
                    .disponivel(true)
                    .build());

            Sabor sabor2 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Mussarela")
                    .valorMedioSabor(20.0)
                    .valorGrandeSabor(30.0)
                    .tipoSabor("salgado")
                    .disponivel(true)
                    .build());

            Sabor sabor3 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Chocolate")
                    .valorMedioSabor(25.0)
                    .valorGrandeSabor(35.0)
                    .tipoSabor("doce")
                    .disponivel(true)
                    .build());

            Sabor sabor4 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Morango")
                    .valorMedioSabor(20.0)
                    .valorGrandeSabor(30.0)
                    .tipoSabor("doce")
                    .disponivel(true)
                    .build());

            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .nomeEstabelecimento("Nome Válido 2")
                    .codigoEstabelecimento("987654")
                    .listaSabores(List.of(sabor1, sabor2, sabor3, sabor4))
                    .build();

            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getIdEstabelecimento() + "/sabores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimento1)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Sabor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(4, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento que não existe")
        void quandoBuscarCardapioEstabelecimentoInexistente() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + 9999 + "/sabores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimentoPostRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage())
            );
        }

        @Transactional
        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento por tipo (salgado)")
        void quandoBuscarCardapioEstabelecimentoPorTipo() throws Exception {
            // Arrange
            Sabor sabor1 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Calabresa")
                    .valorMedioSabor(25.0)
                    .valorGrandeSabor(35.0)
                    .tipoSabor("salgado")
                    .disponivel(true)
                    .build());

            Sabor sabor2 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Mussarela")
                    .valorMedioSabor(20.0)
                    .valorGrandeSabor(30.0)
                    .tipoSabor("salgado")
                    .disponivel(true)
                    .build());

            Sabor sabor3 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Chocolate")
                    .valorMedioSabor(25.0)
                    .valorGrandeSabor(35.0)
                    .tipoSabor("doce")
                    .disponivel(true)
                    .build());

            Sabor sabor4 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Morango")
                    .valorMedioSabor(20.0)
                    .valorGrandeSabor(30.0)
                    .tipoSabor("doce")
                    .disponivel(true)
                    .build());

            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .nomeEstabelecimento("Nome Válido 2")
                    .codigoEstabelecimento("987654")
                    .listaSabores(List.of(sabor1, sabor2, sabor3, sabor4))
                    .build();

            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getIdEstabelecimento() + "/sabores" + "/salgados")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipoSabor", "salgado")
                            .content(objectMapper.writeValueAsString(estabelecimento1)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Sabor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }

        @Transactional
        @Test
        @DisplayName("Quando buscamos o cardapio de um estabelecimento por tipo (doce)")
        void quandoBuscarCardapioEstabelecimentoPorTipoDoce() throws Exception {
            // Arrange
            Sabor sabor1 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Calabresa")
                    .valorMedioSabor(25.0)
                    .valorGrandeSabor(35.0)
                    .tipoSabor("salgado")
                    .disponivel(true)
                    .build());

            Sabor sabor2 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Mussarela")
                    .valorMedioSabor(20.0)
                    .valorGrandeSabor(30.0)
                    .tipoSabor("salgado")
                    .disponivel(true)
                    .build());

            Sabor sabor3 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Chocolate")
                    .valorMedioSabor(25.0)
                    .valorGrandeSabor(35.0)
                    .tipoSabor("doce")
                    .disponivel(true)
                    .build());

            Sabor sabor4 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Morango")
                    .valorMedioSabor(20.0)
                    .valorGrandeSabor(30.0)
                    .tipoSabor("doce")
                    .disponivel(true)
                    .build());

            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .nomeEstabelecimento("Nome Válido 2")
                    .codigoEstabelecimento("987654")
                    .listaSabores(List.of(sabor1, sabor2, sabor3, sabor4))
                    .build();

            estabelecimentoRepository.save(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getIdEstabelecimento() + "/sabores" + "/doces")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tipoSabor", "doce")
                            .content(objectMapper.writeValueAsString(estabelecimento1)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Sabor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(2, resultado.size())
            );
        }

        @Transactional
        @Test
        @DisplayName("Quando buscamos o cardápio de um estabelecimento ordenado por disponibilidade")
        void quandoBuscarCardapioEstabelecimentoOrdenadoPorDisponibilidade() throws Exception {
            // Arrange
            Sabor sabor1 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Calabresa")
                    .valorMedioSabor(25.0)
                    .valorGrandeSabor(35.0)
                    .tipoSabor("salgado")
                    .disponivel(false)
                    .build());

            Sabor sabor2 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Mussarela")
                    .valorMedioSabor(20.0)
                    .valorGrandeSabor(30.0)
                    .tipoSabor("salgado")
                    .disponivel(true)
                    .build());

            Sabor sabor3 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Chocolate")
                    .valorMedioSabor(25.0)
                    .valorGrandeSabor(35.0)
                    .tipoSabor("doce")
                    .disponivel(true)
                    .build());

            Sabor sabor4 = saborRepository.save(Sabor.builder()
                    .nomeSabor("Morango")
                    .valorMedioSabor(20.0)
                    .valorGrandeSabor(30.0)
                    .tipoSabor("doce")
                    .disponivel(true)
                    .build());

            Estabelecimento estabelecimento1 = Estabelecimento.builder()
                    .nomeEstabelecimento("Nome Válido 2")
                    .codigoEstabelecimento("987654")
                    .listaSabores(List.of(sabor1, sabor2, sabor3, sabor4))
                    .build();

            estabelecimentoRepository.save(estabelecimento1);

            System.out.println(estabelecimento1);

            // Act
            String responseJsonString = driver.perform(get(URI_ESTABELECIMENTOS + "/" + estabelecimento1.getIdEstabelecimento() + "/sabores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(estabelecimento1)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            System.out.println(responseJsonString);

            List<Sabor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(4, resultado.size()),
                    () -> assertFalse(resultado.get(3).getDisponivel())
            );
        }
    }
}
