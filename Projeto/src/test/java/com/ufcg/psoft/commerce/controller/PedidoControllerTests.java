package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.pedidodto.*;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de pedidos")
public class PedidoControllerTests {

    final String URI_PEDIDOS = "/pedidos";

    @Autowired
    MockMvc driver;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    AssociacaoRepository associacaoRepository;

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Cliente cliente;

    Associacao associacao;

    Entregador entregador;

    Sabor sabor1;

    Sabor sabor2;

    Pizza pizzaM;

    Pizza pizzaG;

    List<Pizza> pizzas;

    Estabelecimento estabelecimento;

    Pedido pedido;

    Pedido pedido1;

    PedidoBodyDTO pedidoBodyDTO;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());

        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoEstabelecimento("654321")
                .build());

        sabor1 = saborRepository.save(Sabor.builder()
                .nomeSabor("Sabor Um")
                .tipoSabor("salgado")
                .valorMedioSabor(10.0)
                .valorGrandeSabor(20.0)
                .disponivel(true)
                .build());

        sabor2 = saborRepository.save(Sabor.builder()
                .nomeSabor("Sabor Dois")
                .tipoSabor("doce")
                .valorMedioSabor(15.0)
                .valorGrandeSabor(30.0)
                .disponivel(true)
                .build());

        cliente = clienteRepository.save(Cliente.builder()
                .nomeCliente("Anton Ego")
                .enderecoPrincipalCliente("Paris")
                .codigoCliente("123456")
                .build());

        entregador = entregadorRepository.save(Entregador.builder()
                .nomeEntregador("Joãozinho")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoEntregador("101010")
                .build());

        pizzaM = Pizza.builder()
                .quantidade(1)
                .sabor1(sabor1)
                .tamanho("medio")
                .build();

        pizzaG = Pizza.builder()
                .quantidade(1)
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build();

        pizzas = new ArrayList<>();
        pizzas.add(pizzaM);
        pizzas.add(pizzaG);

        pedido = pedidoRepository.save(Pedido.builder()
                .valorPedido(10.0)
                .enderecoOpcional("Casa 237")
                .cliente(cliente)
                .estabelecimento(estabelecimento)
                .entregador(entregador)
                .pagamento(Pagamento.CREDITO)
                .statusPagamento(false)
                .statusEntrega("Pedido recebido")
                .pizzas(pizzas)
                .build());

        pedido1 = pedidoRepository.save(Pedido.builder()
                .valorPedido(10.0)
                .enderecoOpcional("Casa 237")
                .cliente(cliente)
                .estabelecimento(estabelecimento)
                .entregador(entregador)
                .pizzas(pizzas)
                .build());

        pedidoBodyDTO = PedidoBodyDTO.builder()
                .idPedido(pedido.getIdPedido())
                .cliente(pedido.getCliente())
                .entregador(pedido.getEntregador())
                .estabelecimento(pedido.getEstabelecimento())
                .enderecoOpcional(pedido.getEnderecoOpcional())
                .pizzas(pedido.getPizzas())
                .valorPedido(pedido.calculaValorTotal())
                .pagamento(pedido.getPagamento())
                .statusPagamento(pedido.getStatusPagamento())
                .build();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class PedidoVerificacaoFluxosBasicosApiRest {

        @Test
        @DisplayName("Quando criamos um novo pedido com dados válidos")
        void quandoCriamosUmNovoPedidoComDadosValidos() throws Exception {
            // Arrange

            // Act
            String responseJsonString = driver.perform(post(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idCliente", cliente.getIdCliente().toString())
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .param("idEstabelecimento", estabelecimento.getIdEstabelecimento().toString())
                            .content(objectMapper.writeValueAsString(pedidoBodyDTO)))
                    .andExpect(status().isCreated())
                    .andDo(print())// Codigo 201
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getIdPedido()),
                    () -> assertEquals(pedidoBodyDTO.getEnderecoOpcional(), resultado.getEnderecoOpcional()),
                    () -> assertEquals(pedidoBodyDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getCliente().getIdCliente(), resultado.getCliente().getIdCliente()),
                    () -> assertEquals(pedido.getEstabelecimento().getIdEstabelecimento(), resultado.getEstabelecimento().getIdEstabelecimento()),
                    () -> assertEquals(pedido.getValorPedido(), resultado.getValorPedido())
            );
        }

        @Test
        @DisplayName("Quando alteramos um novo pedido com dados válidos")
        void quandoAlteramosPedidoValido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Long idPedido = pedido.getIdPedido();

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPedido", pedido.getIdPedido().toString())
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(pedidoBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(idPedido, resultado.getIdPedido().longValue()),
                    () -> assertEquals(pedidoBodyDTO.getEnderecoOpcional(), resultado.getEnderecoOpcional()),
                    () -> assertEquals(pedidoBodyDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getCliente().getIdCliente(), resultado.getCliente().getIdCliente()),
                    () -> assertEquals(pedido.getEstabelecimento().getIdEstabelecimento(), resultado.getEstabelecimento().getIdEstabelecimento()),
                    () -> assertEquals(pedido.getValorPedido(), resultado.getValorPedido())
            );
        }

        @Test
        @DisplayName("Quando alteramos um pedido inexistente")
        void quandoAlteramosPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + "999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPedido", "999999")
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(pedidoBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos um pedido passando codigo de acesso invalido")
        void quandoAlteramosPedidoPassandoCodigoAcessoInvalido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idPedido", pedido.getIdPedido().toString())
                            .param("codigoCliente", "999999")
                            .content(objectMapper.writeValueAsString(pedidoBodyDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Não autorizado!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca por todos seus pedidos salvos")
        void quandoClienteBuscaTodosPedidos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(pedido1);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS)
                            .param("idCliente", cliente.getIdCliente().toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pedidoBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(2, resultado.size());
        }


        @Test
        @DisplayName("Quando um cliente busca por um pedido seu salvo pelo id primeiro")
        void quandoClienteBuscaPedidoPorId() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/" + cliente.getIdCliente())
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getIdPedido()),
                    () -> assertEquals(pedidoBodyDTO.getEnderecoOpcional(), resultado.getEnderecoOpcional()),
                    () -> assertEquals(pedidoBodyDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getCliente().getIdCliente(), resultado.getCliente().getIdCliente()),
                    () -> assertEquals(pedido.getEstabelecimento().getIdEstabelecimento(), resultado.getEstabelecimento().getIdEstabelecimento()),
                    () -> assertEquals(pedido.getValorPedido(), resultado.getValorPedido())
            );
        }

        @Test
        @DisplayName("Quando um cliente busca por um pedido seu salvo por id inexistente")
        void quandoClienteBuscaPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "999999" + "/" + cliente.getIdCliente())
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca por um pedido feito por outro cliente")
        void quandoClienteBuscaPedidoDeOutroCliente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Cliente cliente1 = clienteRepository.save(Cliente.builder()
                    .nomeCliente("Catarina")
                    .enderecoPrincipalCliente("Casinha")
                    .codigoCliente("121212")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/" + cliente1.getIdCliente())
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Não autorizado!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelecimento busca todos os pedidos feitos nele")
        void quandoEstabelecimentoBuscaTodosPedidos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(pedido1);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/listarPedidos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id primeiro")
        void quandoEstabelecimentoBuscaPedidoPorId() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/visualizarPedido")
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getIdPedido()),
                    () -> assertEquals(pedidoBodyDTO.getEnderecoOpcional(), resultado.getEnderecoOpcional()),
                    () -> assertEquals(pedidoBodyDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                    () -> assertEquals(pedido.getCliente().getIdCliente(), resultado.getCliente().getIdCliente()),
                    () -> assertEquals(pedido.getEstabelecimento().getCodigoEstabelecimento(), resultado.getEstabelecimento().getCodigoEstabelecimento()),
                    () -> assertEquals(pedido.getValorPedido(), resultado.getValorPedido())
            );
        }

        @Test
        @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id inexistente")
        void quandoEstabelecimentoBuscaPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/9999999" + "/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/visualizarPedido")
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelecimento busca por um pedido feito em outro estabelecimento")
        void quandoEstabelecimentoBuscaPedidoDeOutroEstabelecimento() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoEstabelecimento("121212")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/estabelecimento/" + estabelecimento1.getIdEstabelecimento() + "/visualizarPedido")
                            .param("codigoEstabelecimento", estabelecimento1.getCodigoEstabelecimento())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Não autorizado!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente excluí um pedido feito por ele salvo")
        void quandoClienteExcluiPedidoSalvo() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getIdPedido())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um cliente excluí um pedido inexistente")
        void quandoClienteExcluiPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + "999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente excluí todos seus pedidos feitos por ele salvos")
        void quandoClienteExcluiTodosPedidosSalvos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(Pedido.builder()
                    .valorPedido(10.0)
                    .enderecoOpcional("Casa 237")
                    .cliente(cliente)
                    .estabelecimento(estabelecimento)
                    .pizzas(List.of(pizzaM, pizzaG))
                    .build());

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí um pedido feito nele salvo")
        void quandoEstabelecimentoExcluiPedidoSalvo() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/estabelecimentoDeleteOne")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí um pedido inexistente")
        void quandoEstabelecimentoExcluiPedidoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/999999/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/estabelecimentoDeleteOne")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí um pedido feito em outro estabelecimento")
        void quandoEstabelecimentoExcluiPedidoDeOutroEstabelecimento() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
                    .codigoEstabelecimento("121212")
                    .build());

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/estabelecimento/" + estabelecimento1.getIdEstabelecimento() + "/estabelecimentoDeleteOne")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoEstabelecimento", estabelecimento1.getCodigoEstabelecimento()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Não autorizado!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um estabelencimento excluí todos os pedidos feitos nele salvos")
        void quandoEstabelecimentoExcluiTodosPedidosSalvos() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);
            pedidoRepository.save(Pedido.builder()
                    .valorPedido(10.0)
                    .enderecoOpcional("Casa 237")
                    .cliente(cliente)
                    .estabelecimento(estabelecimento)
                    .pizzas(List.of(pizzaM, pizzaG))
                    .build());

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/estabelecimentoDeleteAll")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            // Assert
            assertTrue(responseJsonString.isBlank());
        }


        @Test
        @DisplayName("Quando um cliente cancela um pedido")
        void quandoClienteCancelaPedido() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/cancelar-pedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimento() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/cliente/" + cliente.getIdCliente() + "/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/visualizarPedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.PedidoBuilder.class).build();

            // Assert
            assertEquals(pedido.getIdPedido(), resultado.getIdPedido());
            assertEquals(pedido.getCliente().getIdCliente(), resultado.getCliente().getIdCliente());
            assertEquals(pedido.getEstabelecimento().getIdEstabelecimento(), resultado.getEstabelecimento().getIdEstabelecimento());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento inexistente")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoInexistente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/cliente/" + cliente.getIdCliente() + "/estabelecimento/999999/visualizarPedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com pedido inexistente")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComPedidoInexistente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/999999/cliente/" + cliente.getIdCliente() + "/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/visualizarPedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com cliente inexistente")
        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComClienteInexistente() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/cliente/999999/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/visualizarPedido")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("O cliente consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com idPedido null")
        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComidPedidoNull() throws Exception {
            // Arrange
            pedidoRepository.save(pedido);

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + cliente.getIdCliente() + "/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/historicoPedidos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<PedidoBodyDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(1, resultado.size());
            assertEquals(pedido.getIdPedido(), resultado.get(0).getIdPedido());
            assertEquals(pedido.getCliente().getIdCliente(), resultado.get(0).getCliente().getIdCliente());
            assertEquals(pedido.getEstabelecimento().getIdEstabelecimento(), resultado.get(0).getEstabelecimento().getIdEstabelecimento());
        }

        @Test
        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com status")
        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComStatus() throws Exception {
            // Arrange
            Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                    .valorPedido(30.0)
                    .enderecoOpcional("Casa 237")
                    .cliente(cliente)
                    .estabelecimento(estabelecimento)
                    .pizzas(List.of(pizzaM))
                    .statusEntrega("Pedido em preparo")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + cliente.getIdCliente() + "/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/historicoPedidos/" + pedido3.getStatusEntrega())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<PedidoBodyDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(1, resultado.size());
            assertEquals(pedido3.getIdPedido(), resultado.get(0).getIdPedido());
            assertEquals(pedido3.getCliente().getIdCliente(), resultado.get(0).getCliente().getIdCliente());
            assertEquals(pedido3.getEstabelecimento().getIdEstabelecimento(), resultado.get(0).getEstabelecimento().getIdEstabelecimento());
        }

        @Test
        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento filtrados por entrega")
        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComPedidosFiltradosPorEntrega() throws Exception {
            // Arrange
            Pedido pedido3 = pedidoRepository.save(Pedido.builder()
                    .valorPedido(30.0)
                    .enderecoOpcional("Casa 237")
                    .cliente(cliente)
                    .estabelecimento(estabelecimento)
                    .pizzas(List.of(pizzaM))
                    .statusEntrega("Pedido entregue")
                    .build());
            Pedido pedido4 = pedidoRepository.save(Pedido.builder()
                    .valorPedido(30.0)
                    .enderecoOpcional("Casa 237")
                    .cliente(cliente)
                    .estabelecimento(estabelecimento)
                    .pizzas(List.of(pizzaM))
                    .statusEntrega("Pedido em preparo")
                    .build());

            // Act
            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + cliente.getIdCliente() + "/estabelecimento/" + estabelecimento.getIdEstabelecimento() + "/historicoPedidos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<PedidoBodyDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertEquals(3, resultado.size());
            assertEquals(pedido4.getIdPedido(), resultado.get(0).getIdPedido());
            assertEquals(pedido4.getCliente().getIdCliente(), resultado.get(0).getCliente().getIdCliente());
            assertEquals(pedido4.getEstabelecimento().getIdEstabelecimento(), resultado.get(0).getEstabelecimento().getIdEstabelecimento());
            assertEquals(pedido3.getIdPedido(), resultado.get(1).getIdPedido());
            assertEquals(pedido3.getCliente().getIdCliente(), resultado.get(1).getCliente().getIdCliente());
            assertEquals(pedido3.getEstabelecimento().getIdEstabelecimento(), resultado.get(1).getEstabelecimento().getIdEstabelecimento());

        }
    }

    @Nested
    @DisplayName("Alteração de estado de pedido")
    public class AlteracaoEstadoPedidoTest {
        Pedido pedido1;

        @BeforeEach
        void setUp() {
            pedido1 = pedidoRepository.save(Pedido.builder()
                    .estabelecimento(estabelecimento)
                    .cliente(cliente)
                    .enderecoOpcional("Rua 1")
                    .pizzas(List.of(pizzaG))
                    .valorPedido(10.0)
                    .build()
            );
        }

        @Test
        @DisplayName("Quando o estabelecimento indica o termino do preparo")
        void quandoEstabelecimentoIndicaTerminoPreparo() throws Exception {
            // Arrange

            pedidoBodyDTO.setStatusEntrega("Pedido em preparo");

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/pedido-pronto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("estabelecimentoId", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(pedidoBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            PedidoBodyDTO resultado = objectMapper.readValue(responseJsonString, PedidoBodyDTO.class);

            // Assert
            assertEquals("Pedido pronto", resultado.getStatusEntrega());
            assertEquals(entregador.getIdEntregador(), resultado.getEntregador().getIdEntregador());
        }

        @Test
        @DisplayName("Quando o estabelecimento associa um pedido a um entregador")
        void quandoEstabelecimentoAssociaPedidoEntregador() throws Exception {
            // Arrange
            pedidoBodyDTO.setStatusEntrega("Pedido pronto");
            entregador.setStatusAprovacao(true);
            List<Entregador> entregadores = new LinkedList<>();
            entregadores.add(entregador);
            estabelecimento.setListaEntregadoresDisponiveis(entregadores);
            pedidoBodyDTO.setEntregador(entregador);
            entregador.setDisponibilidade(true);

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/associar-pedido-entregador")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("estabelecimentoId", estabelecimento.getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", estabelecimento.getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(pedidoBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            PedidoBodyDTO resultado = objectMapper.readValue(responseJsonString, PedidoBodyDTO.class);

            // Assert
            assertEquals("Pedido em rota", resultado.getStatusEntrega());
            assertEquals(entregador.getIdEntregador(), resultado.getEntregador().getIdEntregador());
        }

        @Test
        @DisplayName("Quando o cliente confirma a entrega de um pedido")
        void quandoClienteConfirmaEntregaPedido() throws Exception {
            // Arrange
            pedidoBodyDTO.setStatusEntrega("Pedido em rota");

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/" + cliente.getIdCliente() + "/cliente-confirmar-entrega")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .content(objectMapper.writeValueAsString(pedidoBodyDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            PedidoBodyDTO resultado = objectMapper.readValue(responseJsonString, PedidoBodyDTO.class);

            // Assert
            assertEquals(resultado.getStatusEntrega(), "Pedido entregue");
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de teste da confirmação de pagamento de um pedido")
    public class PedidoConfirmarPagamentoTests {

        Pedido pedido1;

        @BeforeEach
        void setUp() {
            pedido1 = pedidoRepository.save(Pedido.builder()
                    .estabelecimento(estabelecimento)
                    .cliente(cliente)
                    .enderecoOpcional("Rua 1")
                    .pizzas(List.of(pizzaG))
                    .valorPedido(25.0)
                    .build()
            );
        }

        @Test
        @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de crédito")
        void confirmaPagamentoCartaoCredito() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/confirmarPagamento")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .param("idPedido", pedido1.getIdPedido().toString())
                            .param("pagamento", "CREDITO"))
                    .andExpect(status().isOk()) // Codigo 200
                    .andReturn().getResponse().getContentAsString();
            // Assert
            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
            assertAll(
                    () -> assertTrue(resultado.getStatusPagamento()),
                    () -> assertEquals(25, resultado.getValorPedido())
            );
        }

        @Test
        @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de débito")
        void confirmaPagamentoCartaoDebito() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/confirmarPagamento")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .param("idPedido", pedido1.getIdPedido().toString())
                            .param("pagamento", "DEBITO"))
                    .andExpect(status().isOk()) // Codigo 200
                    .andReturn().getResponse().getContentAsString();
            // Assert
            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
            assertAll(
                    () -> assertTrue(resultado.getStatusPagamento()),
                    () -> assertEquals(24.375, resultado.getValorPedido())
            );
        }

        @Test
        @DisplayName("Quando confirmamos o pagamento de um pedido por PIX")
        void confirmaPagamentoPIX() throws Exception {
            // Arrange
            // Act
            String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/confirmarPagamento")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("codigoCliente", cliente.getCodigoCliente())
                            .param("idPedido", pedido1.getIdPedido().toString())
                            .param("pagamento", "PIX"))
                    .andExpect(status().isOk()) // Codigo 200
                    .andReturn().getResponse().getContentAsString();
            // Assert
            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
            assertAll(
                    () -> assertTrue(resultado.getStatusPagamento()),
                    () -> assertEquals(23.75, resultado.getValorPedido())
            );
        }
    }

    @Nested
    @DisplayName("Testes para sistema de automação de atribuição de pedidos a entregadores")
    public class PedidoAutomacaoAtribuicaoEntregadorControllerTests {

        @BeforeEach
        public void setUp() {
            entregador = entregadorRepository.save(Entregador.builder()
                    .nomeEntregador("Jose da Silva")
                    .corVeiculo("Branco")
                    .placaVeiculo("123456")
                    .tipoVeiculo("MOTO")
                    .disponibilidade(false)
                    .codigoEntregador("12345678").build());

            estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                    .nomeEstabelecimento("Jipao")
                    .codigoEstabelecimento("123456")
                    .listaEntregadoresDisponiveis(new ArrayList<>())
                    .build());

            associacao = associacaoRepository.save(Associacao.builder()
                    .entregador(entregador)
                    .estabelecimento(estabelecimento)
                    .statusAssociacao(true)
                    .build());

            cliente = clienteRepository.save(Cliente.builder()
                    .nomeCliente("Joao")
                    .enderecoPrincipalCliente("Rua 1")
                    .codigoCliente("123456")
                    .build());

            Sabor sabor = saborRepository.save(Sabor.builder()
                    .nomeSabor("Frango")
                    .tipoSabor("salgado")
                    .estabelecimento(estabelecimento)
                    .valorGrandeSabor(59.90)
                    .valorMedioSabor(39.90)
                    .build());

            List<Sabor> sabores = new ArrayList<Sabor>();
            sabores.add(sabor);

            Pizza pizza1 = Pizza.builder()
                    .valorPizza(sabor.getValorGrandeSabor())
                    .sabor1(sabor)
                    .sabor2(null)
                    .tamanho("grande")
                    .quantidade(1)
                    .build();

            pizzas = new ArrayList<>();
            pizzas.add(pizza1);

            pedido = pedidoRepository.save(Pedido.builder()
                    .cliente(cliente)
                    .pizzas(pizzas)
                    .estabelecimento(estabelecimento)
                    .enderecoOpcional("abc")
                    .build());
        }

        @Test
        @DisplayName("Teste de atribuição automática quando não há entregador disponível")
        void atribuicaoAutomaticaSemEntregadorDisponivelTest() throws Exception {
            // Arrange
            pedido.setEntregador(null);
            pedido.setStatusEntrega("Pedido em preparo");

            // Act
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/pedido-pronto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", pedido.getEstabelecimento().getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", pedido.getEstabelecimento().getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(pedido)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

            // Assert
            assertEquals("Pedido pronto", resultado.getStatusEntrega());
            assertNull(resultado.getEntregador());
        }

        @Test
        @DisplayName("Teste de atribuição automática com entregador disponível")
        void atribuicaoAutomaticaComEntregadorDisponivelTest() throws Exception {
            pedido.setStatusEntrega("Pedido em preparo");
            pedidoRepository.save(pedido);

            driver.perform(put("/entregadores/" + entregador.getIdEntregador() + "/disponibilidade")
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .param("disponibilidade", "true")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            estabelecimento.getListaEntregadoresDisponiveis().add(entregador);

            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/pedido-pronto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", pedido.getEstabelecimento().getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", pedido.getEstabelecimento().getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(pedido)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoEmRota = objectMapper.readValue(responseJsonString, Pedido.class);

            assertEquals("Pedido em rota", pedidoEmRota.getStatusEntrega());
            assertEquals(estabelecimento.getListaEntregadoresDisponiveis().size(), 0);
        }

        @Test
        @DisplayName("Teste de atribuição automática com espera de entregador disponível")
        void atribuicaoAutomaticaEsperandoEntregadorDisponivelTest() throws Exception {
            pedido.setStatusEntrega("Pedido em preparo");
            pedidoRepository.save(pedido);
            estabelecimento.getListaEntregadoresDisponiveis().add(entregador);

            driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/pedido-pronto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", pedido.getEstabelecimento().getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", pedido.getEstabelecimento().getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(pedido)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            driver.perform(put("/entregadores/" + entregador.getIdEntregador() + "/disponibilidade")
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .param("disponibilidade", "true")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            estabelecimento.getListaEntregadoresDisponiveis().add(entregador);
            Pedido pedidoEmRota = pedidoRepository.findById(pedido.getIdPedido()).get();

            assertEquals(pedidoEmRota.getStatusEntrega(), "Pedido em rota");
        }

        @Test
        @DisplayName("Teste priorizar entregador aguardando por mais tempo")
        void priorizaEntregadorAguardandoMaisTempo() throws Exception {
            pedido.setStatusEntrega("Pedido em preparo");
            pedidoRepository.save(pedido);

            Entregador entregador2 = entregadorRepository.save(Entregador.builder()
                    .nomeEntregador("Joao da Moto")
                    .corVeiculo("Vermelho")
                    .placaVeiculo("12345q")
                    .tipoVeiculo("MOTO")
                    .codigoEntregador("12345678").build());

            Associacao associacao2 = associacaoRepository.save(Associacao.builder()
                    .entregador(entregador2)
                    .estabelecimento(estabelecimento)
                    .statusAssociacao(true)
                    .build());

            driver.perform(put("/entregadores/" + entregador2.getIdEntregador() + "/disponibilidade")
                            .param("codigoEntregador", entregador2.getCodigoEntregador())
                            .param("disponibilidade", "true")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            driver.perform(put("/entregadores/" + entregador.getIdEntregador() + "/disponibilidade")
                            .param("codigoEntregador", entregador.getCodigoEntregador())
                            .param("disponibilidade", "true")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            estabelecimento.getListaEntregadoresDisponiveis().add(entregador2);
            estabelecimento.getListaEntregadoresDisponiveis().add(entregador);

            String respostaJson = driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/pedido-pronto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", pedido.getEstabelecimento().getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", pedido.getEstabelecimento().getCodigoEstabelecimento())
                            .content(objectMapper.writeValueAsString(pedido)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Pedido pedidoEmRota = objectMapper.readValue(respostaJson, Pedido.class);

            assertEquals(pedidoEmRota.getStatusEntrega(), "Pedido em rota");
            assertEquals(estabelecimento.getListaEntregadoresDisponiveis().size(), 1);
            assertEquals(pedidoEmRota.getEntregador().getNomeEntregador(), entregador2.getNomeEntregador());
            assertEquals(entregadorRepository.findById(estabelecimento.getListaEntregadoresDisponiveis().get(0).getIdEntregador()).get().getNomeEntregador(),
                    entregador.getNomeEntregador());

        }

        @Test
        @DisplayName("Teste tentar colocar pedido pronto com código de acesso errado")
        void codigoDeAcessoIncorrecoTest() throws Exception {
            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getIdPedido() + "/pedido-pronto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idEstabelecimento", pedido.getEstabelecimento().getIdEstabelecimento().toString())
                            .param("codigoEstabelecimento", "CodigoIncorreto")
                            .content(objectMapper.writeValueAsString(pedido)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType erro =  objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertEquals("Não autorizado!", erro.getMessage());
        }

    }

}
