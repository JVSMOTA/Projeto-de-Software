package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoa.*;
import br.edu.ufcg.computacao.psoft.commerce.exception.CustomErrorType;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoa.PessoaRepository;
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
@DisplayName("[Pessoa] Testes")
@AutoConfigureMockMvc
class PessoaV1RestControllerTest {

    final String URL_TEMPLATE = "/v1/pessoas";

    @Autowired
    MockMvc driver;
    @Autowired
    PessoaRepository pessoaRepository;


    PessoaPostDTO pessoaPostDTO;
    PessoaPutDTO pessoaPutDTO;
    PessoaPatchEmailDTO pessoaPatchEmailDTO;
    PessoaGetOneDTO pessoaGetOneDTO;
    PessoaGetAllDTO pessoaGetAllDTO;

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
    @DisplayName("[POST] Criação de Pessoa")
    class ValidacaoDeEntradas {

        @Test
        @DisplayName("Quando criar pessoa com dados válidos Então a pessoa deve ser retornada")
        void quandoCriarPessoaComDadosValidos() throws Exception {
            // Arrange
            pessoaPostDTO = PessoaPostDTO.builder()
                    .nome("Joao Mota")
                    .cpf("123.456.789-00")
                    .email("joao.mota@ccc.ufcg.edu.br")
                    .dataNascimento("22-06-2001")
                    .profissao("Desenvolvedor Backend")
                    .build();

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pessoaPostDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pessoa pessoaResultado = objectMapper.readValue(responseJsonString, Pessoa.class);

            // Assert
            assertNotNull("A pessoa criada não deve ser nula", pessoaResultado);
            assertTrue("O id da Pessoa deve ser maior que 0", pessoaResultado.getId() > 0);
            assertEquals("O nome deve ser 'Joao Mota'", "Joao Mota", pessoaResultado.getNome());
            assertEquals("O CPF deve ser '123.456.789-00'", "123.456.789-00", pessoaResultado.getCpf());
            assertEquals("O email deve ser 'joao.mota@ccc.ufcg.edu.br'", "joao.mota@ccc.ufcg.edu.br", pessoaResultado.getEmail());
            assertEquals("A data de nascimento deve ser '22-06-2001'", "22-06-2001", pessoaResultado.getDataNascimento());
            assertEquals("A profissão deve ser 'Desenvolvedor Backend'", "Desenvolvedor Backend", pessoaResultado.getProfissao());

        }
    }

    @Nested
    @DisplayName("[PUT] Atualização de Pessoa")
    class ValidacaoDeEntradasAtualizacao {

        @Test
        @DisplayName("Quando atualizar pessoa com dados válidos Então a pessoa atualizada deve ser retornada")
        void quandoAtualizarPessoaComDadosValidos() throws Exception {
            // Arrange
            pessoaPutDTO = PessoaPutDTO.builder()
                    .email("joao.mota@ccc.ufcg.edu.br")
                    .dataNascimento("22-06-2001")
                    .profissao("Desenvolvedor Backend")
                    .build();
            Pessoa pessoaAdicionada = pessoaRepository.save(modelMapper.map(pessoaPutDTO, Pessoa.class));

            // Act
            pessoaPutDTO.setEmail("joao.mota@estudante.ufcg.edu.br");
            pessoaPutDTO.setDataNascimento("27-08-2023");
            pessoaPutDTO.setProfissao("Agiota");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pessoaAdicionada.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pessoaPutDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pessoa pessoaResultado = objectMapper.readValue(responseJsonString, Pessoa.class);

            // Assert
            assertNotNull("A pessoa criada não deve ser nula", pessoaResultado);
            assertTrue("O id da Pessoa deve ser maior que 0", pessoaResultado.getId() > 0);
            assertEquals("O email deve ser 'joao.mota@estudante.ufcg.edu.br'", "joao.mota@estudante.ufcg.edu.br", pessoaResultado.getEmail());
            assertEquals("A data de nascimento deve ser '27-08-2023'", "27-08-2023", pessoaResultado.getDataNascimento());
            assertEquals("A profissão deve ser 'Agiota'", "Agiota", pessoaResultado.getProfissao());


        }

        @Test
        @DisplayName("Quando atualizar pessoa com email vazio Então um erro é retornado para o usuário")
        void quandoAtualizarPessoaComEmailInvalido() throws Exception {
            // Arrange
            pessoaPutDTO = PessoaPutDTO.builder()
                    .email("joao.mota@ccc.ufcg.edu.br")
                    .dataNascimento("22-06-2001")
                    .profissao("Desenvolvedor Backend")
                    .build();
            Pessoa pessoaAdicionada = pessoaRepository.save(modelMapper.map(pessoaPutDTO, Pessoa.class));

            // Act
            pessoaPutDTO.setEmail("");
            pessoaPutDTO.setDataNascimento("27-08-2023");
            pessoaPutDTO.setProfissao("Agiota");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pessoaAdicionada.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pessoaPutDTO)))
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
                    "É esperado '1' erro de validação de Email de Pessoa",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'EMAIL DE PESSOA OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("EMAIL DE PESSOA OBRIGATORIO")
                    )
            );

        }

        @Test
        @DisplayName("Quando atualizar pessoa com data de nascimento vazio Então um erro é retornado para o usuário")
        void quandoAtualizarPessoaComDataNascimentoInvalido() throws Exception {
            // Arrange
            pessoaPutDTO = PessoaPutDTO.builder()
                    .email("joao.mota@ccc.ufcg.edu.br")
                    .dataNascimento("22-06-2001")
                    .profissao("Desenvolvedor Backend")
                    .build();
            Pessoa pessoaAdicionada = pessoaRepository.save(modelMapper.map(pessoaPutDTO, Pessoa.class));

            // Act
            pessoaPutDTO.setEmail("joao.mota@codexjr.com.br");
            pessoaPutDTO.setDataNascimento("");
            pessoaPutDTO.setProfissao("Agiota");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pessoaAdicionada.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pessoaPutDTO)))
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
                    "É esperado '1' erro de validação de Data de Nascimento de Pessoa",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'DATA DE NASCIMENTO DE PESSOA OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("DATA DE NASCIMENTO DE PESSOA OBRIGATORIO")
                    )
            );

        }

        @Test
        @DisplayName("Quando atualizar pessoa com profissao vazio Então um erro é retornado para o usuário")
        void quandoAtualizarPessoaComProfissaoInvalido() throws Exception {
            // Arrange
            pessoaPutDTO = PessoaPutDTO.builder()
                    .email("joao.mota@ccc.ufcg.edu.br")
                    .dataNascimento("22-06-2001")
                    .profissao("Desenvolvedor Backend")
                    .build();
            Pessoa pessoaAdicionada = pessoaRepository.save(modelMapper.map(pessoaPutDTO, Pessoa.class));

            // Act
            pessoaPutDTO.setEmail("joao.mota@codexjr.com.br");
            pessoaPutDTO.setDataNascimento("23-08-2023");
            pessoaPutDTO.setProfissao("");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.put(URL_TEMPLATE + "/" + pessoaAdicionada.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pessoaPutDTO)))
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
                    "É esperado '1' erro de validação de Profissao de Pessoa",
                    1,
                    customErrorType.getErrors().size()
            );
            assertTrue(
                    "É esperado uma mensagem de erro de 'PROFISSAO DE PESSOA OBRIGATORIO'",
                    customErrorType.getErrors().stream().anyMatch(
                            (msg) -> msg.toUpperCase().contains("PROFISSAO DE PESSOA OBRIGATORIO")
                    )
            );

        }

    }

    @Nested
    @DisplayName("[PATCH] Atualização de Pessoa")
    class ValidacaoDeEntradasEmail {

        @Test
        @DisplayName("Quando atualizar o email de pessoa com dados válidos Então a pessoa atualizado deve ser retornado")
        void quandoAtualizarEmailPessoaComDadosValidos() throws Exception {
            // Arrange
            pessoaPatchEmailDTO = PessoaPatchEmailDTO.builder()
                    .email("joao.mota@ccc.ufcg.edu.br")
                    .build();

            Pessoa pessoaAdicionada = pessoaRepository.save(modelMapper.map(pessoaPatchEmailDTO, Pessoa.class));

            // Act
            pessoaPatchEmailDTO.setEmail("joao.mota@codexjr.com.br");
            String responseJsonString = driver.perform(MockMvcRequestBuilders.patch(URL_TEMPLATE + "/" + pessoaAdicionada.getId() + "/email")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pessoaPatchEmailDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pessoa pessoaResultado = objectMapper.readValue(responseJsonString, Pessoa.class);

            // Assert
            assertNotNull("A pessoa criada não deve ser nulo", pessoaResultado);
            assertEquals("O email de pessoa deve ser 'joao.mota@codexjr.com.br'", "joao.mota@codexjr.com.br", pessoaResultado.getEmail());
        }

    }

    @Nested
    @DisplayName("[DELETE] Excluindo de Pessoa")
    class ValidacaoDeSaidas {

        @Test
        @DisplayName("Quando excluir pessoa com id válido Então nada é retornado")
        void quandoExcluirPessoaComDadosValidos() throws Exception {
            // Arrange
            pessoaPostDTO = PessoaPostDTO.builder()
                    .nome("Joao Mota")
                    .cpf("123.456.789-00")
                    .email("joao.mota@ccc.ufcg.edu.br")
                    .dataNascimento("22-06-2001")
                    .profissao("Desenvolvedor Backend")
                    .build();
            Pessoa pessoaAdicionada = pessoaRepository.save(modelMapper.map(pessoaPostDTO, Pessoa.class));

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/" + pessoaAdicionada.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue("A resposta após Pessoa ser deletado dever ser Null", responseJsonString.isBlank());

        }

    }

    @Nested
    @DisplayName("[GETONE] Buscando Pessoa")
    class ValidacaoDeBuscas {

        @Test
        @DisplayName("Quando buscar o pessoa com id válido Então a pessoa deve ser retornada")
        void quandoBuscarPessoaComDadosValidos() throws Exception {
            // Arrange
            pessoaGetOneDTO = PessoaGetOneDTO.builder()
                    .nome("Joao Mota")
                    .cpf("123.456.789-00")
                    .email("joao.mota@ccc.ufcg.edu.br")
                    .dataNascimento("22-06-2001")
                    .profissao("Desenvolvedor Backend")
                    .build();
            Pessoa pessoaAdicionada = pessoaRepository.save(modelMapper.map(pessoaGetOneDTO, Pessoa.class));

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/" + pessoaAdicionada.getId())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(pessoaGetOneDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pessoa pessoaResultado = objectMapper.readValue(responseJsonString, Pessoa.class);

            // Assert
            assertNotNull("O pessoa criada não deve ser nulo", pessoaResultado);
            assertTrue("O id do Pessoa deve ser maior que 0", pessoaResultado.getId() > 0);

        }

    }

    @Nested
    @DisplayName("[GETALL] Listando Pessoas")
    class ValidacaoDeListagem {

        @Test
        @DisplayName("Quando listar todas as pessoas Então a lista deve ser retornada")
        void quandoListarTodosAsPessoas() throws Exception {
            // Arrange
            pessoaGetAllDTO = PessoaGetAllDTO.builder()
                    .nome("Joao Mota 1")
                    .cpf("123.456.789-00 1")
                    .email("joao.mota@ccc.ufcg.edu.br 1")
                    .dataNascimento("22-06-2001 1")
                    .profissao("Desenvolvedor Backend 1")
                    .build();
            Pessoa pessoaAdicionada1 = pessoaRepository.save(modelMapper.map(pessoaGetAllDTO, Pessoa.class));

            pessoaGetAllDTO = PessoaGetAllDTO.builder()
                    .nome("Joao Mota 2")
                    .cpf("123.456.789-00 2")
                    .email("joao.mota@ccc.ufcg.edu.br 2")
                    .dataNascimento("22-06-2001 2")
                    .profissao("Desenvolvedor Backend 2")
                    .build();
            Pessoa pessoaAdicionada2 = pessoaRepository.save(modelMapper.map(pessoaGetAllDTO, Pessoa.class));

            pessoaGetAllDTO = PessoaGetAllDTO.builder()
                    .nome("Joao Mota 3")
                    .cpf("123.456.789-00 3")
                    .email("joao.mota@ccc.ufcg.edu.br 3")
                    .dataNascimento("22-06-2001 3")
                    .profissao("Desenvolvedor Backend 3")
                    .build();
            Pessoa pessoaAdicionada3 = pessoaRepository.save(modelMapper.map(pessoaGetAllDTO, Pessoa.class));

            // Act
            String responseJsonString = driver.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pessoa> pessoasList = objectMapper.readValue(responseJsonString, new TypeReference<List<Pessoa>>() {});

            // Assert
            assertNotNull("A lista de pessoas não deve ser nula", pessoasList);
            assertFalse("A lista de pessoas não deve ser vazia", pessoasList.isEmpty());
            assertEquals("A lista deve conter 3 pessoas", 3, pessoasList.size());
        }

    }

}