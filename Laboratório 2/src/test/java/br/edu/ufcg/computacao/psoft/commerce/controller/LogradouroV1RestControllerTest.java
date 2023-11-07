package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.*;
import br.edu.ufcg.computacao.psoft.commerce.exception.CustomErrorType;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouro.LogradouroRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("[Logradouro] Testes")
@AutoConfigureMockMvc
class LogradouroV1RestControllerTest {

    final String URL_TEMPLATE = "/v1/logradouros";

    @Autowired
    MockMvc driver;
    @Autowired
    LogradouroRepository logradouroRepository;

    LogradouroPostPutDTO logradouroPostPutDTO;
    LogradouroPatchTipoDTO logradouroPatchTipoDTO;
    LogradouroPatchNomeDTO logradouroPatchNomeDTO;
    LogradouroGetOneDTO logradouroGetOneDTO;
    LogradouroGetAllDTO logradouroGetAllDTO;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    @DisplayName("[POST] Criação de Logradouro")
    class ValidacaoDeEntradas {

        @Test
        @DisplayName("Quando criar logradouro com dados válidos Então o logradouro deve ser retornado")
        void quandoCriarLogradouroComDadosValidos() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Logradouro logradouroResultado = objectMapper.readValue(responseJsonString, Logradouro.class);

            // Assert
            assertNotNull("O logradouro criado não deve ser nulo", logradouroResultado);
            assertTrue("O id do Logradouro deve ser maior que 0", logradouroResultado.getId() > 0);
            assertEquals("O tipo de logradouro deve ser 'Rua'", "Rua", logradouroResultado.getTipoLogradouro());
            assertEquals("O nome do logradouro deve ser 'Coronel Manoel Gaudencio'", "Coronel Manoel Gaudencio", logradouroResultado.getNome());
            assertEquals("O bairro deve ser 'Centro'", "Centro", logradouroResultado.getBairro());
            assertEquals("A cidade deve ser 'Serra Branca'", "Serra Branca", logradouroResultado.getCidade());
            assertEquals("O estado deve ser 'Paraiba'", "Paraiba", logradouroResultado.getEstado());
            assertEquals("O país deve ser 'Brasil'", "Brasil", logradouroResultado.getPais());
            assertEquals("O CEP deve ser '58580-000'", "58580-000", logradouroResultado.getCep());
        }

    }

    @Nested
    @DisplayName("[PUT] Atualização de Logradouro")
    class ValidacaoDeEntradasAtualizacao {

        @Test
        @DisplayName("Quando atualizar logradouro com dados válidos Então o logradouro atualizado deve ser retornado")
        void quandoAtualizarLogradouroComDadosValidos() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

            // Act
            logradouroPostPutDTO.setTipoLogradouro("Rua");
            logradouroPostPutDTO.setNome("Rua dos Douradores");
            logradouroPostPutDTO.setBairro("Centro");
            logradouroPostPutDTO.setEstado("Distrito Federal");
            logradouroPostPutDTO.setCidade("Lisboa");
            logradouroPostPutDTO.setPais("Portugal");
            logradouroPostPutDTO.setCep("1100-207");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Logradouro logradouroResultado = objectMapper.readValue(responseJsonString, Logradouro.class);

            // Assert
            assertNotNull("O logradouro criado não deve ser nulo", logradouroResultado);
            assertTrue("O id do Logradouro deve ser maior que 0", logradouroResultado.getId() > 0);
            assertEquals("O tipo de logradouro deve ser 'Rua'", "Rua", logradouroResultado.getTipoLogradouro());
            assertEquals("O nome do logradouro deve ser 'Rua dos Douradores'", "Rua dos Douradores", logradouroResultado.getNome());
            assertEquals("O bairro deve ser 'Centro'", "Centro", logradouroResultado.getBairro());
            assertEquals("A cidade deve ser 'Lisboa'", "Lisboa", logradouroResultado.getCidade());
            assertEquals("O estado deve ser 'Distrito Federal'", "Distrito Federal", logradouroResultado.getEstado());
            assertEquals("O país deve ser 'Portugal'", "Portugal", logradouroResultado.getPais());
            assertEquals("O CEP deve ser '1100-207'", "1100-207", logradouroResultado.getCep());
        }

        @Test
        @DisplayName("Quando atualizar logradouro com tipo vazio Então um erro é retornado para o usuário")
        void quandoAtualizarLogradouroComTituloInvalido() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

            // Act
            logradouroPostPutDTO.setTipoLogradouro("");
            logradouroPostPutDTO.setNome("Rua dos Douradores");
            logradouroPostPutDTO.setBairro("Centro");
            logradouroPostPutDTO.setEstado("Distrito Federal");
            logradouroPostPutDTO.setCidade("Lisboa");
            logradouroPostPutDTO.setPais("Portugal");
            logradouroPostPutDTO.setCep("1100-207");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals(
                    "O tipo de erro deve ser 'Erros de validacao encontrados'",
                    "Erros de validacao encontrados",
                    customErrorType.getMessage());
            assertEquals(
                    "É esperado '1' erro de validação de Tipo de Logradouro",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'TIPO DE LOGRADOURO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("TIPO DE LOGRADOURO OBRIGATORIO")
                    )
            );
        }

        @Test
        @DisplayName("Quando atualizar logradouro com nome vazio Então um erro é retornado para o usuário")
        void quandoAtualizarLogradouroComNomeInvalido() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

            // Act
            logradouroPostPutDTO.setTipoLogradouro("Rua");
            logradouroPostPutDTO.setNome("");
            logradouroPostPutDTO.setBairro("Centro");
            logradouroPostPutDTO.setEstado("Distrito Federal");
            logradouroPostPutDTO.setCidade("Lisboa");
            logradouroPostPutDTO.setPais("Portugal");
            logradouroPostPutDTO.setCep("1100-207");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals(
                    "O tipo de erro deve ser 'Erros de validacao encontrados'",
                    "Erros de validacao encontrados",
                    customErrorType.getMessage());
            assertEquals(
                    "É esperado '1' erro de validação de Nome de Logradouro",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'NOME DE LOGRADOURO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("NOME DE LOGRADOURO OBRIGATORIO")
                    )
            );
        }

        @Test
        @DisplayName("Quando atualizar logradouro com bairro vazio Então um erro é retornado para o usuário")
        void quandoAtualizarLogradouroComBairroInvalido() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

            // Act
            logradouroPostPutDTO.setTipoLogradouro("Rua");
            logradouroPostPutDTO.setNome("Rua dos Douradores");
            logradouroPostPutDTO.setBairro("");
            logradouroPostPutDTO.setEstado("Distrito Federal");
            logradouroPostPutDTO.setCidade("Lisboa");
            logradouroPostPutDTO.setPais("Portugal");
            logradouroPostPutDTO.setCep("1100-207");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals(
                    "O tipo de erro deve ser 'Erros de validacao encontrados'",
                    "Erros de validacao encontrados",
                    customErrorType.getMessage());
            assertEquals(
                    "É esperado '1' erro de validação de bairro de Logradouro",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'BAIRRO DE LOGRADOURO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("BAIRRO DE LOGRADOURO OBRIGATORIO")
                    )
            );
        }

        @Test
        @DisplayName("Quando atualizar logradouro com cidade vazio Então um erro é retornado para o usuário")
        void quandoAtualizarLogradouroComCidadeInvalido() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

            // Act
            logradouroPostPutDTO.setTipoLogradouro("Rua");
            logradouroPostPutDTO.setNome("Rua dos Douradores");
            logradouroPostPutDTO.setBairro("Centro");
            logradouroPostPutDTO.setEstado("Distrito Federal");
            logradouroPostPutDTO.setCidade("");
            logradouroPostPutDTO.setPais("Portugal");
            logradouroPostPutDTO.setCep("1100-207");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals(
                    "O tipo de erro deve ser 'Erros de validacao encontrados'",
                    "Erros de validacao encontrados",
                    customErrorType.getMessage());
            assertEquals(
                    "É esperado '1' erro de validação de cidade de Logradouro",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'CIDADE DE LOGRADOURO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("CIDADE DE LOGRADOURO OBRIGATORIO")
                    )
            );
        }

        @Test
        @DisplayName("Quando atualizar logradouro com estado vazio Então um erro é retornado para o usuário")
        void quandoAtualizarLogradouroComEstadoInvalido() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

            // Act
            logradouroPostPutDTO.setTipoLogradouro("Rua");
            logradouroPostPutDTO.setNome("Rua dos Douradores");
            logradouroPostPutDTO.setBairro("Centro");
            logradouroPostPutDTO.setEstado("");
            logradouroPostPutDTO.setCidade("Lisboa");
            logradouroPostPutDTO.setPais("Portugal");
            logradouroPostPutDTO.setCep("1100-207");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals(
                    "O tipo de erro deve ser 'Erros de validacao encontrados'",
                    "Erros de validacao encontrados",
                    customErrorType.getMessage());
            assertEquals(
                    "É esperado '1' erro de validação de estado de Logradouro",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'ESTADO DE LOGRADOURO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("ESTADO DE LOGRADOURO OBRIGATORIO")
                    )
            );
        }

        @Test
        @DisplayName("Quando atualizar logradouro com país vazio Então um erro é retornado para o usuário")
        void quandoAtualizarLogradouroComPaisInvalido() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

            // Act
            logradouroPostPutDTO.setTipoLogradouro("Rua");
            logradouroPostPutDTO.setNome("Rua dos Douradores");
            logradouroPostPutDTO.setBairro("Centro");
            logradouroPostPutDTO.setEstado("Distrito Federal");
            logradouroPostPutDTO.setCidade("Lisboa");
            logradouroPostPutDTO.setPais("");
            logradouroPostPutDTO.setCep("1100-207");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals(
                    "O tipo de erro deve ser 'Erros de validacao encontrados'",
                    "Erros de validacao encontrados",
                    customErrorType.getMessage());
            assertEquals(
                    "É esperado '1' erro de validação de pais de Logradouro",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'PAIS DE LOGRADOURO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("PAIS DE LOGRADOURO OBRIGATORIO")
                    )
            );
        }

        @Test
        @DisplayName("Quando atualizar logradouro com cep vazio Então um erro é retornado para o usuário")
        void quandoAtualizarLogradouroComCepInvalido() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

            // Act
            logradouroPostPutDTO.setTipoLogradouro("Rua");
            logradouroPostPutDTO.setNome("Rua dos Douradores");
            logradouroPostPutDTO.setBairro("Centro");
            logradouroPostPutDTO.setEstado("Distrito Federal");
            logradouroPostPutDTO.setCidade("Lisboa");
            logradouroPostPutDTO.setPais("Portugal");
            logradouroPostPutDTO.setCep("");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPostPutDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals(
                    "O tipo de erro deve ser 'Erros de validacao encontrados'",
                    "Erros de validacao encontrados",
                    customErrorType.getMessage());
            assertEquals(
                    "É esperado '1' erro de validação de cep de Logradouro",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'CEP DE LOGRADOURO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("CEP DE LOGRADOURO OBRIGATORIO")
                    )
            );

        }

    }

    @Nested
    @DisplayName("[PATCH] Atualização de Logradouro")
    class ValidacaoDeEntradasTipoLogradouro {

        @Test
        @DisplayName("Quando atualizar o tipo de logradouro com dados válidos Então o logradouro atualizado deve ser retornado")
        void quandoAtualizarTipoLogradouroComDadosValidos() throws Exception {
            // Arrange
            logradouroPatchTipoDTO = LogradouroPatchTipoDTO.builder()
                    .tipoLogradouro("Rua")
                    .build();

            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPatchTipoDTO, Logradouro.class));

            // Act
            logradouroPatchTipoDTO.setTipoLogradouro("Avenida");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.patch(URL_TEMPLATE + "/" + logradouroAdicionado.getId() + "/tipoLogradouro")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPatchTipoDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Logradouro logradouroResultado = objectMapper.readValue(responseJsonString, Logradouro.class);

            // Assert
            assertNotNull("O logradouro criado não deve ser nulo", logradouroResultado);
            assertEquals("O tipo de logradouro deve ser 'Avenida'", "Avenida", logradouroResultado.getTipoLogradouro());
            assertEquals("O tipo de logradouro deve ser 'Avenida'", logradouroPatchTipoDTO.getTipoLogradouro(), logradouroResultado.getTipoLogradouro());
        }

        @Test
        @DisplayName("Quando atualizar o nome de logradouro com dados válidos Então o logradouro atualizado deve ser retornado")
        void quandoAtualizarNomeLogradouroComDadosValidos() throws Exception {
            // Arrange
            logradouroPatchNomeDTO = LogradouroPatchNomeDTO.builder()
                    .nome("Rua Brigadeiro Faria Lima")
                    .build();

            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPatchNomeDTO, Logradouro.class));

            // Act
            logradouroPatchNomeDTO.setNome("Rua Nossa Senhora de Copacabana");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.patch(URL_TEMPLATE + "/" + logradouroAdicionado.getId() + "/nome")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroPatchNomeDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Logradouro logradouroResultado = objectMapper.readValue(responseJsonString, Logradouro.class);

            // Assert
            assertNotNull("O logradouro criado não deve ser nulo", logradouroResultado);
            assertEquals("O nome de logradouro deve ser 'Rua Nossa Senhora de Copacabana'", "Rua Nossa Senhora de Copacabana", logradouroResultado.getNome());
            assertEquals("O nome de logradouro deve ser 'Rua Nossa Senhora de Copacabana'", logradouroPatchNomeDTO.getNome(), logradouroResultado.getNome());
        }

    }

    @Nested
    @DisplayName("[DELETE] Excluindo de Logradouro")
    class ValidacaoDeSaidas {

        @Test
        @DisplayName("Quando excluir o tipo de logradouro com id válido Então nada é retornado")
        void quandoExcluirTipoLogradouroComDadosValidos() throws Exception {
            // Arrange
            logradouroPostPutDTO = LogradouroPostPutDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroPostPutDTO, Logradouro.class));

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue("A resposta após ser Logradouro ser deletado dever ser Null", responseJsonString.isBlank());

        }

    }

    @Nested
    @DisplayName("[GETONE] Buscando Logradouro")
    class ValidacaoDeBuscas {

        @Test
        @DisplayName("Quando buscar o logradouro com id válido Então o logradouro deve ser retornado")
        void quandoBuscarLogradouroComDadosValidos() throws Exception {
            // Arrange
            logradouroGetOneDTO = LogradouroGetOneDTO.builder()
                    .tipoLogradouro("Rua")
                    .nome("Coronel Manoel Gaudencio")
                    .bairro("Centro")
                    .cidade("Serra Branca")
                    .estado("Paraiba")
                    .pais("Brasil")
                    .cep("58580-000")
                    .build();
            Logradouro logradouroAdicionado = logradouroRepository.add(modelMapper.map(logradouroGetOneDTO, Logradouro.class));

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/" + logradouroAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(logradouroGetOneDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Logradouro logradouroResultado = objectMapper.readValue(responseJsonString, Logradouro.class);

            // Assert
            assertNotNull("O logradouro criado não deve ser nulo", logradouroResultado);
            assertTrue("O id do Logradouro deve ser maior que 0", logradouroResultado.getId() > 0);
            assertEquals("O tipo de logradouro deve ser 'Rua'", "Rua", logradouroResultado.getTipoLogradouro());
            assertEquals("O nome do logradouro deve ser 'Coronel Manoel Gaudencio'", "Coronel Manoel Gaudencio", logradouroResultado.getNome());
            assertEquals("O bairro deve ser 'Centro'", "Centro", logradouroResultado.getBairro());
            assertEquals("A cidade deve ser 'Serra Branca'", "Serra Branca", logradouroResultado.getCidade());
            assertEquals("O estado deve ser 'Paraiba'", "Paraiba", logradouroResultado.getEstado());
            assertEquals("O país deve ser 'Brasil'", "Brasil", logradouroResultado.getPais());
            assertEquals("O CEP deve ser '58580-000'", "58580-000", logradouroResultado.getCep());
        }

    }

    @Nested
    @DisplayName("[GETALL] Listando Logradouros")
    class ValidacaoDeListagem {

        @Test
        @DisplayName("Quando listar todos os logradouros Então a lista deve ser retornada")
        void quandoListarTodosOsLogradouros() throws Exception {
            // Arrange
            logradouroGetAllDTO = LogradouroGetAllDTO.builder()
                    .tipoLogradouro("Rua 1")
                    .nome("Coronel Manoel Gaudencio 1")
                    .bairro("Centro 1")
                    .cidade("Serra Branca 1")
                    .estado("Paraiba 1")
                    .pais("Brasil 1")
                    .cep("58580-0001 ")
                    .build();
            Logradouro logradouroAdicionado1 = logradouroRepository.add(modelMapper.map(logradouroGetAllDTO, Logradouro.class));

            logradouroGetAllDTO = LogradouroGetAllDTO.builder()
                    .tipoLogradouro("Rua 2")
                    .nome("Coronel Manoel Gaudencio 2")
                    .bairro("Centro 2")
                    .cidade("Serra Branca 2")
                    .estado("Paraiba 2")
                    .pais("Brasil 2")
                    .cep("58580-000 2")
                    .build();
            Logradouro logradouroAdicionado2 = logradouroRepository.add(modelMapper.map(logradouroGetAllDTO, Logradouro.class));

            logradouroGetAllDTO = LogradouroGetAllDTO.builder()
                    .tipoLogradouro("Rua 3")
                    .nome("Coronel Manoel Gaudencio 3")
                    .bairro("Centro 3")
                    .cidade("Serra Branca 3")
                    .estado("Paraiba 3")
                    .pais("Brasil 3")
                    .cep("58580-000 3")
                    .build();
            Logradouro logradouroAdicionado3 = logradouroRepository.add(modelMapper.map(logradouroGetAllDTO, Logradouro.class));

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Logradouro> logradourosList = objectMapper.readValue(responseJsonString, new TypeReference<List<Logradouro>>() {});

            // Assert
            assertNotNull("A lista de logradouros não deve ser nula", logradourosList);
            assertFalse("A lista de logradouros não deve ser vazia", logradourosList.isEmpty());
            assertEquals("A lista deve conter 3 logradouros", 3, logradourosList.size());
        }

    }

}