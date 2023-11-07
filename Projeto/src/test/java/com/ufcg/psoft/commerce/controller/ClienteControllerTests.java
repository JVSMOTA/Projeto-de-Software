package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.clientedto.*;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Clientes")
public class ClienteControllerTests {

    final String URI_CLIENTES = "/clientes";

    @Autowired
    MockMvc driver;

    @Autowired
    ClienteRepository clienteRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Cliente cliente;

    ClienteBodyDTO clienteBodyDTO;

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        cliente = clienteRepository.save(Cliente.builder()
                .nomeCliente("Cliente Um da Silva")
                .enderecoPrincipalCliente("Rua dos Testes, 123")
                .codigoCliente("123456")
                .build()
        );
        clienteBodyDTO = ClienteBodyDTO.builder()
                .nomeCliente(cliente.getNomeCliente())
                .enderecoPrincipalCliente(cliente.getEnderecoPrincipalCliente())
                .codigoCliente(cliente.getCodigoCliente())
                .build();
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de nome")
    class ClienteVerificacaoNome {

        @Test
        @DisplayName("Quando alteramos o nome do cliente com dados válidos")
        void quandoAlteramosNomeDoClienteValido() throws Exception {
            // Arrange
            clienteBodyDTO.setNomeCliente("Cliente Um Alterado");

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cliente resultado = objectMapper.readValue(responseJsonString,
                                Cliente.ClienteBuilder.class).build();

            // Assert
            assertEquals("Cliente Um Alterado", resultado.getNomeCliente());
        }

        @Test
        @DisplayName("Quando alteramos o nome do cliente nulo")
        void quandoAlteramosNomeDoClienteNulo() throws Exception {
            // Arrange
            clienteBodyDTO.setNomeCliente(null);

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome de Cliente Obrigatorio!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o nome do cliente vazio")
        void quandoAlteramosNomeDoClienteVazio() throws Exception {
            // Arrange
            clienteBodyDTO.setNomeCliente("");

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome de Cliente Obrigatorio!", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação do endereço")
    class ClienteVerificacaoEndereco {

        @Test
        @DisplayName("Quando alteramos o endereço do cliente com dados válidos")
        void quandoAlteramosEnderecoDoClienteValido() throws Exception {
            // Arrange
            clienteBodyDTO.setEnderecoPrincipalCliente("Endereco Alterado");

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cliente resultado = objectMapper.readValue(responseJsonString, Cliente.ClienteBuilder.class).build();

            // Assert
            assertEquals("Endereco Alterado", resultado.getEnderecoPrincipalCliente());
        }

        @Test
        @DisplayName("Quando alteramos o endereço do cliente nulo")
        void quandoAlteramosEnderecoDoClienteNulo() throws Exception {
            // Arrange
            clienteBodyDTO.setEnderecoPrincipalCliente(null);

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Endereço Principal de Cliente Obrigatorio!", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o endereço do cliente vazio")
        void quandoAlteramosEnderecoDoClienteVazio() throws Exception {
            // Arrange
            clienteBodyDTO.setEnderecoPrincipalCliente("");

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Endereço Principal de Cliente Obrigatorio!", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação do código de acesso")
    class ClienteVerificacaoCodigoAcesso {

        @Test
        @DisplayName("Quando alteramos o código de acesso do cliente nulo")
        void quandoAlteramosCodigoAcessoDoClienteNulo() throws Exception {
            // Arrange
            clienteBodyDTO.setCodigoCliente(null);

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest())
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
        @DisplayName("Quando alteramos o código de acesso do cliente mais de 6 digitos")
        void quandoAlteramosCodigoAcessoDoClienteMaisDe6Digitos() throws Exception {
            // Arrange
            clienteBodyDTO.setCodigoCliente("1234567");

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest())
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
        @DisplayName("Quando alteramos o código de acesso do cliente menos de 6 digitos")
        void quandoAlteramosCodigoAcessoDoClienteMenosDe6Digitos() throws Exception {
            // Arrange
            clienteBodyDTO.setCodigoCliente("12345");

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest())
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
        @DisplayName("Quando alteramos o código de acesso do cliente caracteres não numéricos")
        void quandoAlteramosCodigoAcessoDoClienteCaracteresNaoNumericos() throws Exception {
            // Arrange
            clienteBodyDTO.setCodigoCliente("a*c4e@");

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Codigo de acesso deve ter exatamente 6 digitos numericos", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class ClienteVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando buscamos por todos clientes salvos")
        void quandoBuscamosPorTodosClienteSalvos() throws Exception {
            // Arrange
            // Vamos ter 3 clientes no banco
            Cliente cliente1 = Cliente.builder()
                    .nomeCliente("Cliente Dois Almeida")
                    .enderecoPrincipalCliente("Av. da Pits A, 100")
                    .codigoCliente("246810")
                    .build();
            Cliente cliente2 = Cliente.builder()
                    .nomeCliente("Cliente Três Lima")
                    .enderecoPrincipalCliente("Distrito dos Testadores, 200")
                    .codigoCliente("135790")
                    .build();
            clienteRepository.saveAll(Arrays.asList(cliente1, cliente2));

            // Act
            String responseJsonString = driver.perform(get(URI_CLIENTES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Cliente> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos um cliente salvo pelo id")
        void quandoBuscamosPorUmClienteSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cliente resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(cliente.getIdCliente().longValue(), resultado.getIdCliente().longValue()),
                    () -> assertEquals(cliente.getNomeCliente(), resultado.getNomeCliente())
            );
        }

        @Test
        @DisplayName("Quando buscamos um cliente inexistente")
        void quandoBuscamosPorUmClienteInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_CLIENTES + "/" + 999999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O cliente consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo cliente com dados válidos")
        void quandoCriarClienteValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_CLIENTES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cliente resultado = objectMapper.readValue(responseJsonString, Cliente.ClienteBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getIdCliente()),
                    () -> assertEquals(clienteBodyDTO.getNomeCliente(), resultado.getNomeCliente())
            );

        }

        @Test
        @DisplayName("Quando alteramos o cliente com dados válidos")
        void quandoAlteramosClienteValido() throws Exception {
            // Arrange
            Long clienteId = cliente.getIdCliente();

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Cliente resultado = objectMapper.readValue(responseJsonString, Cliente.ClienteBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getIdCliente().longValue(), clienteId),
                    () -> assertEquals(clienteBodyDTO.getNomeCliente(), resultado.getNomeCliente())
            );
        }

        @Test
        @DisplayName("Quando alteramos o cliente inexistente")
        void quandoAlteramosClienteInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + 99999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O cliente consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos o cliente passando código de acesso inválido")
        void quandoAlteramosClienteCodigoAcessoInvalido() throws Exception {
            // Arrange
            String clienteCodigo = cliente.getCodigoCliente();

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + clienteCodigo)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", "invalido")
                            .content(objectMapper.writeValueAsString(clienteBodyDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O cliente consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um cliente salvo")
        void quandoExcluimosClienteValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_CLIENTES + "/" + cliente.getIdCliente())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando excluímos um cliente inexistente")
        void quandoExcluimosClienteInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_CLIENTES + "/" + 999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O cliente consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos um cliente salvo passando código de acesso inválido")
        void quandoExcluimosClienteCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_CLIENTES + "/" + "123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", "invalido"))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O cliente consultado nao existe!", resultado.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de demonstrar interesse em sabores")
    class ClienteDemonstrarInteresseEmSabores {

        @Autowired
        EstabelecimentoRepository estabelecimentoRepository;

        @Autowired
        SaborRepository saborRepository;

        Estabelecimento estabelecimento;

        Sabor sabor;

        @BeforeEach
        void setUp() {
            estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoEstabelecimento("654321")
                    .build()
            );
            sabor = saborRepository.save(Sabor.builder()
                    .nomeSabor("Sabor Um")
                    .tipoSabor("salgado")
                    .valorMedioSabor(10.0)
                    .valorGrandeSabor(20.0)
                    .disponivel(false)
                    .build());
        }

        @AfterEach
        void tearDown() {
            estabelecimentoRepository.deleteAll();
            saborRepository.deleteAll();
        }

        @Test
        @DisplayName("Quando demonstramos interesse em um sabor válido")
        void quandoDemonstramosInteresseEmSaborValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente() + "/demonstrarInteresse")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .param("idSabor", sabor.getIdSabor().toString()))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Sabor resultado = objectMapper.readValue(responseJsonString, Sabor.SaborBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertFalse(resultado.getDisponivel()),
                    () -> assertEquals(1, resultado.getClientesInteressados().size())
            );
        }

        @Test
        @DisplayName("Quando demonstramos interesse em um sabor com código de acesso inválido")
        void quandoDemonstramosInteresseEmSaborCodigoAcessoInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente() + "/demonstrarInteresse")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", "invalido")
                            .param("idSabor", sabor.getIdSabor().toString()))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Não autorizado!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando demonstramos interesse em um sabor inexistente")
        void quandoDemonstramosInteresseEmSaborInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente() + "/demonstrarInteresse")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .param("idSabor", "999999"))
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
        @DisplayName("Quando um cliente inexistente demonstra interesse em um sabor")
        void quandoDemonstramosInteresseEmSaborClienteInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + 999999 + "/demonstrarInteresse")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .param("idSabor", sabor.getIdSabor().toString()))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O cliente consultado nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um cliente demonstra interesse em um sabor que já está disponível")
        void quandoDemonstramosInteresseEmSaborJaDisponivel() throws Exception {
            // Arrange
            sabor.setDisponivel(true);
            saborRepository.save(sabor);

            // Act
            String responseJsonString = driver.perform(put(URI_CLIENTES + "/" + cliente.getIdCliente() + "/demonstrarInteresse")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .param("idSabor", sabor.getIdSabor().toString()))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O sabor consultado ja esta disponivel!", resultado.getMessage())
            );
        }
    }
}
