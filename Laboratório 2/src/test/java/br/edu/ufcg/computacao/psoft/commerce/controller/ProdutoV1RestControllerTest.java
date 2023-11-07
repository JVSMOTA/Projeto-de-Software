package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.produto.*;
import br.edu.ufcg.computacao.psoft.commerce.exception.CustomErrorType;
import br.edu.ufcg.computacao.psoft.commerce.model.Produto;
import br.edu.ufcg.computacao.psoft.commerce.repository.produto.ProdutoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DisplayName("[Produto] Testes")
@AutoConfigureMockMvc
class ProdutoV1RestControllerTest {

    final String URL_TEMPLATE = "/v1/produtos";

    @Autowired
    MockMvc driver;
    @Autowired
    ProdutoRepository produtoRepository;

    ProdutoPostDTO produtoPostDTO;
    ProdutoPutDTO produtoPutDTO;
    ProdutoPatchCodigoBarrasDTO produtoPatchCodigoBarrasDTO;
    ProdutoGetOneDTO produtoGetOneDTO;
    ProdutoGetAllDTO produtoGetAllDTO;

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
    @DisplayName("[POST] Criação de Produto")
    class ValidacaoDeEntradas {

        @Test
        @DisplayName("Quando criar produto com dados válidos Então o produto deve ser retornado")
        void quandoCriarProdutoComDadosValidos() throws Exception {
            // Arrange
            produtoPostDTO = ProdutoPostDTO.builder()
                    .nome("Smartphone Galaxy S21")
                    .codigoBarras("7891234567890")
                    .valor(2.499)
                    .fabricante("Samsung")
                    .build();

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(produtoPostDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto produtoResultado = objectMapper.readValue(responseJsonString, Produto.class);

            // Assert
            assertNotNull("A produto criada não deve ser nula", produtoResultado);
            assertTrue("O id de produto deve ser maior que 0", produtoResultado.getId() > 0);
            assertEquals("O nome deve ser 'Smartphone Galaxy S21'", "Smartphone Galaxy S21", produtoResultado.getNome());
            assertEquals("O código de barras deve ser '7891234567890'", "7891234567890", produtoResultado.getCodigoBarras());
            assertEquals("O valor deve ser 2.499", 2.499, produtoResultado.getValor());
            assertEquals("O fabricante deve ser 'Samsung'", "Samsung", produtoResultado.getFabricante());

        }

    }

    @Nested
    @DisplayName("[PUT] Atualização de Produto")
    class ValidacaoDeEntradasAtualizacao {

        @Test
        @DisplayName("Quando atualizar produto com dados válidos Então o produto atualizado deve ser retornado")
        void quandoAtualizarProdutoComDadosValidos() throws Exception {
            // Arrange
            produtoPutDTO = ProdutoPutDTO.builder()
                    .nome("Smartphone Galaxy S21")
                    .codigoBarras("7891234567890")
                    .valor(2.499)
                    .fabricante("Samsung")
                    .build();

            Produto produtoAdicionado = produtoRepository.add(modelMapper.map(produtoPutDTO, Produto.class));

            // Act
            produtoPutDTO.setNome("Smartphone Galaxy Roubado");
            produtoPutDTO.setCodigoBarras("7890000000000");
            produtoPutDTO.setValor(200.00);
            produtoPutDTO.setFabricante("Juninho da Boca de Fumo");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + produtoAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(produtoPutDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto produtoResultado = objectMapper.readValue(responseJsonString, Produto.class);

            // Assert
            assertNotNull("A produto criada não deve ser nula", produtoResultado);
            assertTrue("O id de produto deve ser maior que 0", produtoResultado.getId() > 0);
            assertEquals("O nome deve ser 'Smartphone Galaxy Roubado'", "Smartphone Galaxy Roubado", produtoResultado.getNome());
            assertEquals("O código de barras deve ser '7890000000000'", "7890000000000", produtoResultado.getCodigoBarras());
            assertEquals("O valor deve ser 200.00", 200.00, produtoResultado.getValor());
            assertEquals("O fabricante deve ser 'Juninho da Boca de Fumo'", "Juninho da Boca de Fumo", produtoResultado.getFabricante());

        }

        @Test
        @DisplayName("Quando atualizar produto com nome vazio Então um erro é retornado para o usuário")
        void quandoAtualizarProdutoComNomeInvalido() throws Exception {
            // Arrange
            produtoPutDTO = ProdutoPutDTO.builder()
                    .nome("Smartphone Galaxy S21")
                    .codigoBarras("7891234567890")
                    .valor(2.499)
                    .fabricante("Samsung")
                    .build();
            Produto produtoAdicionado = produtoRepository.add(modelMapper.map(produtoPutDTO, Produto.class));

            // Act
            produtoPutDTO.setNome("");
            produtoPutDTO.setCodigoBarras("7891234567890");
            produtoPutDTO.setValor(200.00);
            produtoPutDTO.setFabricante("Juninho da Boca de Fumo");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + produtoAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(produtoPutDTO)))
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
                    "É esperado '1' erro de validação de Nome de Produto",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'NOME DE PRODUTO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("NOME DE PRODUTO OBRIGATORIO")
                    )
            );

        }

        @Test
        @DisplayName("Quando atualizar produto com codigo de barras vazio Então um erro é retornado para o usuário")
        void quandoAtualizarProdutoComCodigoBarrasInvalido() throws Exception {
            // Arrange
            produtoPutDTO = ProdutoPutDTO.builder()
                    .nome("Smartphone Galaxy S21")
                    .codigoBarras("7891234567890")
                    .valor(2.499)
                    .fabricante("Samsung")
                    .build();
            Produto produtoAdicionado = produtoRepository.add(modelMapper.map(produtoPutDTO, Produto.class));

            // Act
            produtoPutDTO.setNome("Smartphone Galaxy Roubado");
            produtoPutDTO.setCodigoBarras("");
            produtoPutDTO.setValor(200.00);
            produtoPutDTO.setFabricante("Juninho da Boca de Fumo");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + produtoAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(produtoPutDTO)))
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
                    "É esperado '1' erro de validação de Codigo de Barras de Produto",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'CODIGO DE BARRAS DE PRODUTO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("CODIGO DE BARRAS DE PRODUTO OBRIGATORIO")
                    )
            );

        }

        @Test
        @DisplayName("Quando atualizar produto com valor invalido Então um erro é retornado para o usuário")
        void quandoAtualizarProdutoComValorInvalido() throws Exception {
            // Arrange
            produtoPutDTO = ProdutoPutDTO.builder()
                    .nome("Smartphone Galaxy S21")
                    .codigoBarras("7891234567890")
                    .valor(2.499)
                    .fabricante("Samsung")
                    .build();
            Produto produtoAdicionado = produtoRepository.add(modelMapper.map(produtoPutDTO, Produto.class));

            // Act
            produtoPutDTO.setNome("Smartphone Galaxy Roubado");
            produtoPutDTO.setCodigoBarras("7891234567890");
            produtoPutDTO.setValor(0.0);
            produtoPutDTO.setFabricante("Juninho da Boca de Fumo");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + produtoAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(produtoPutDTO)))
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
                    "É esperado '1' erro de validação de Valor de Produto",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'O VALOR DO PRODUTO DEVE SER MAIOR QUE ZERO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("O VALOR DO PRODUTO DEVE SER MAIOR QUE ZERO")
                    )
            );

        }

        @Test
        @DisplayName("Quando atualizar produto com fabricante vazio Então um erro é retornado para o usuário")
        void quandoAtualizarProdutoComFabricanteInvalido() throws Exception {
            // Arrange
            produtoPutDTO = ProdutoPutDTO.builder()
                    .nome("Smartphone Galaxy S21")
                    .codigoBarras("7891234567890")
                    .valor(2.499)
                    .fabricante("Samsung")
                    .build();
            Produto produtoAdicionado = produtoRepository.add(modelMapper.map(produtoPutDTO, Produto.class));

            // Act
            produtoPutDTO.setNome("Smartphone Galaxy Roubado");
            produtoPutDTO.setCodigoBarras("7891234567890");
            produtoPutDTO.setValor(200.00);
            produtoPutDTO.setFabricante("");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + produtoAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(produtoPutDTO)))
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
                    "É esperado '1' erro de validação de Fabricante de Produto",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'FABRICANTE DE PRODUTO OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("FABRICANTE DE PRODUTO OBRIGATORIO")
                    )
            );

        }

    }

    @Nested
    @DisplayName("[PATCH] Atualização de Produto")
    class ValidacaoDeEntradasCodigoBarra {

        @Test
        @DisplayName("Quando atualizar o codigo de barras de produto com dados válidos Então o produto atualizado deve ser retornado")
        void quandoAtualizarCodigoBarrasComDadosValidos() throws Exception {
            // Arrange
            produtoPatchCodigoBarrasDTO = ProdutoPatchCodigoBarrasDTO.builder()
                    .codigoBarras("7891234567890")
                    .build();

            Produto produtoAdicionado = produtoRepository.add(modelMapper.map(produtoPatchCodigoBarrasDTO, Produto.class));

            // Act
            produtoPatchCodigoBarrasDTO.setCodigoBarras("7890000000000");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.patch(URL_TEMPLATE + "/" + produtoAdicionado.getId() + "/codigoBarras")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(produtoPatchCodigoBarrasDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto produtoResultado = objectMapper.readValue(responseJsonString, Produto.class);

            // Assert
            assertNotNull("O produto criado não deve ser nulo", produtoResultado);
            assertEquals("O codigo de barras de produto deve ser '7890000000000'", "7890000000000", produtoResultado.getCodigoBarras());
        }

    }

    @Nested
    @DisplayName("[DELETE] Excluindo Produto")
    class ValidacaoDeSaidas {

        @Test
        @DisplayName("Quando excluir produto com id válido Então nada é retornado")
        void quandoExcluirProdutoComDadosValidos() throws Exception {
            // Arrange
            produtoPostDTO = ProdutoPostDTO.builder()
                    .nome("Smartphone Galaxy S21")
                    .codigoBarras("7891234567890")
                    .valor(2.499)
                    .fabricante("Samsung")
                    .build();
            Produto produtoAdicionado = produtoRepository.add(modelMapper.map(produtoPostDTO, Produto.class));

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/" + produtoAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue("A resposta após Produto ser deletado dever ser Null", responseJsonString.isBlank());

        }

    }

    @Nested
    @DisplayName("[GETONE] Buscando Produto")
    class ValidacaoDeBuscas {

        @Test
        @DisplayName("Quando buscar o produto com id válido Então o produto deve ser retornado")
        void quandoBuscarProdutoComDadosValidos() throws Exception {
            // Arrange
            produtoGetOneDTO = ProdutoGetOneDTO.builder()
                    .nome("Smartphone Galaxy S21")
                    .codigoBarras("7891234567890")
                    .valor(2.499)
                    .fabricante("Samsung")
                    .build();
            Produto produtoAdicionado = produtoRepository.add(modelMapper.map(produtoGetOneDTO, Produto.class));

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/" + produtoAdicionado.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(produtoGetOneDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto produtoResultado = objectMapper.readValue(responseJsonString, Produto.class);

            // Assert
            assertNotNull("O produto criado não deve ser nulo", produtoResultado);
            assertTrue("O id do produto deve ser maior que 0", produtoResultado.getId() > 0);

        }

    }

    @Nested
    @DisplayName("[GETALL] Listando Produtos")
    class ValidacaoDeListagem {

        @Test
        @DisplayName("Quando listar todos os produtos Então a lista deve ser retornada")
        void quandoListarTodosOsLogradouros() throws Exception {
            // Arrange
            produtoGetAllDTO = ProdutoGetAllDTO.builder()
                    .nome("Smartphone Galaxy S21 1")
                    .codigoBarras("7891234567890 1")
                    .valor(2.500)
                    .fabricante("Samsung 1")
                    .build();
            Produto produtoAdicionado1 = produtoRepository.add(modelMapper.map(produtoGetAllDTO, Produto.class));

            produtoGetAllDTO = ProdutoGetAllDTO.builder()
                    .nome("Smartphone Galaxy S21 2")
                    .codigoBarras("7891234567890 2")
                    .valor(2.501)
                    .fabricante("Samsung 2")
                    .build();
            Produto produtoAdicionado2 = produtoRepository.add(modelMapper.map(produtoGetAllDTO, Produto.class));

            produtoGetAllDTO = ProdutoGetAllDTO.builder()
                    .nome("Smartphone Galaxy S21 3")
                    .codigoBarras("7891234567890 3")
                    .valor(2.502)
                    .fabricante("Samsung 3")
                    .build();
            Produto produtoAdicionado3 = produtoRepository.add(modelMapper.map(produtoGetAllDTO, Produto.class));

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Produto> produtoList = objectMapper.readValue(responseJsonString, new TypeReference<List<Produto>>() {});

            // Assert
            assertNotNull("A lista de produtos não deve ser nula", produtoList);
            assertFalse("A lista de produtos não deve ser vazia", produtoList.isEmpty());
            assertEquals("A lista deve conter 3 produtos", 3, produtoList.size());
        }

    }

}