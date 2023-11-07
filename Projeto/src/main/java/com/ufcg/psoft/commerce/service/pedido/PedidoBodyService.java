package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedidodto.*;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class PedidoBodyService implements PedidoBodyServiceInterface {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public Pedido post(String codigoCliente, PedidoBodyDTO pedidoBodyDTO) {
        if (codigoCliente == null || codigoCliente.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Cliente clienteEncontrado = this.clienteRepository
                            .findById(pedidoBodyDTO.getCliente().getIdCliente())
                            .orElseThrow(ClientDoesNotExistException::new);

        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                            .findById(pedidoBodyDTO.getEstabelecimento().getIdEstabelecimento())
                            .orElseThrow(CommerceDoesNotExistException::new);

        if (!clienteEncontrado.getCodigoCliente().equals(codigoCliente)) {
            throw new NotAuthorizedException();
        }

        List<Pizza> pedidoPizzas = pedidoBodyDTO.getPizzas();
        String endereco = clienteEncontrado.getEnderecoPrincipalCliente();
        String enderecoOpcional = pedidoBodyDTO.getEnderecoOpcional();

        if (enderecoOpcional != null) {
            endereco = enderecoOpcional;
        }

        Pedido pedido = Pedido.builder()
                    .pizzas(pedidoPizzas)
                    .cliente(clienteEncontrado)
                    .estabelecimento(estabelecimentoEncontrado)
                    .entregador(pedidoBodyDTO.getEntregador())
                    .enderecoOpcional(endereco)
                    .valorPedido(pedidoBodyDTO.getValorPedido())
                    .pagamento(pedidoBodyDTO.getPagamento())
                    .statusEntrega("Pedido recebido")
                    .statusPagamento(pedidoBodyDTO.getStatusPagamento())
                    .build();
        
        return this.pedidoRepository.save(pedido);
    }

    @Override
    public Pedido put(Long idPedido, String codigoCliente, PedidoBodyDTO pedidoBodyDTO) {
        Pedido pedidoExistente = this.pedidoRepository
                .findById(idPedido)
                .orElseThrow(OrderDoesNotExistException::new);

        Cliente clienteEncontrado = this.clienteRepository
                .findById(pedidoBodyDTO.getCliente().getIdCliente())
                .orElseThrow(ClientDoesNotExistException::new);

        if (!clienteEncontrado.getCodigoCliente().equals(codigoCliente)) {
            throw new NotAuthorizedException();
        }

        pedidoExistente.setEnderecoOpcional(pedidoBodyDTO.getEnderecoOpcional());
        pedidoExistente.setValorPedido(pedidoBodyDTO.getValorPedido());
        pedidoExistente.setPagamento(pedidoBodyDTO.getPagamento());
        pedidoExistente.setStatusPagamento(pedidoBodyDTO.getStatusPagamento());
        pedidoExistente.setPizzas(pedidoBodyDTO.getPizzas());

        return this.pedidoRepository.save(pedidoExistente);
    }

    @Override
    public Pedido putStatusPedidoPronto(Long idPedido, String codigoEstabelecimento, PedidoBodyDTO pedidoBodyDTO) {
        Pedido pedidoExistente = this.pedidoRepository
                .findById(idPedido)
                .orElseThrow(OrderDoesNotExistException::new);

        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                .findById(pedidoBodyDTO.getEstabelecimento().getIdEstabelecimento())
                .orElseThrow(CommerceDoesNotExistException::new);

        if (!estabelecimentoEncontrado.getCodigoEstabelecimento().equals(codigoEstabelecimento)) {
            throw new NotAuthorizedException();
        }

        if (pedidoBodyDTO.getStatusEntrega().equals("Pedido pronto")) {
            if (pedidoBodyDTO.getEntregador() != null) {
                pedidoExistente.setStatusEntrega("Pedido em rota");
                System.out.println("[NOTIFICAÇÃO - ENVIADO] Pedido de ID nº" +
                        pedidoExistente.getIdPedido() +
                        " e pizza(s) " +
                        pedidoExistente.getPizzas().toString() +
                        " está sendo enviado pelo entregador " +
                        pedidoExistente.getEntregador().getNomeEntregador() +
                        "no veículo " +
                        pedidoExistente.getEntregador().getTipoVeiculo() +
                        ", placa: " + pedidoExistente.getEntregador().getPlacaVeiculo());
            }
        }

        if (pedidoBodyDTO.getStatusEntrega().equals("Pedido em preparo")) {
            pedidoExistente.setStatusEntrega("Pedido pronto");
            this.associarEntregador(pedidoExistente);
            this.pedidoRepository.save(pedidoExistente);
        }

        pedidoExistente.setEnderecoOpcional(pedidoBodyDTO.getEnderecoOpcional());
        pedidoExistente.setValorPedido(pedidoBodyDTO.getValorPedido());
        pedidoExistente.setPagamento(pedidoBodyDTO.getPagamento());
        pedidoExistente.setStatusPagamento(pedidoBodyDTO.getStatusPagamento());
        pedidoExistente.setPizzas(pedidoBodyDTO.getPizzas());

        return this.pedidoRepository.save(pedidoExistente);
    }

    private void associarEntregador(Pedido pedido){
        Estabelecimento estabelecimento = pedido.getEstabelecimento();
        if (estabelecimento.getListaEntregadoresDisponiveis() != null) {
            if(estabelecimento.getListaEntregadoresDisponiveis().isEmpty()){
                estabelecimento.getPedidosEmEspera().add(pedido.getIdPedido());
                pedido.getCliente().notificarIndisponibilidadeEntregador();
            } else {
                Entregador entregador = estabelecimento.getListaEntregadoresDisponiveis().get(0);
                estabelecimento.getListaEntregadoresDisponiveis().remove(0);
                atribuirEntregador(pedido.getIdPedido(), entregador.getIdEntregador());
            }
        }
    }

    @Override
    public Pedido putStatusPedidoEntregue(Long idPedido, Long idCliente, String codigoCliente, PedidoBodyDTO pedidoBodyDTO) {
        Pedido pedidoExistente = this.pedidoRepository
                .findById(idPedido)
                .orElseThrow(OrderDoesNotExistException::new);

        Cliente clienteEncontrado = this.clienteRepository
                .findById(pedidoBodyDTO.getCliente().getIdCliente())
                .orElseThrow(ClientDoesNotExistException::new);

        if (!clienteEncontrado.getCodigoCliente().equals(codigoCliente)) {
            throw new NotAuthorizedException();
        }

        if (pedidoBodyDTO.getStatusEntrega().equals("Pedido em rota")) {
            pedidoExistente.setStatusEntrega("Pedido entregue");
            System.out.println("[NOTIFICAÇÃO - ENTREGA] Pedido de ID nº" + 
                                pedidoExistente.getIdPedido() + 
                                " e pizza(s) " + 
                                pedidoExistente.getPizzas().toString() + 
                                " foi entregue!");
        }

        pedidoExistente.setEnderecoOpcional(pedidoBodyDTO.getEnderecoOpcional());
        pedidoExistente.setValorPedido(pedidoBodyDTO.getValorPedido());
        pedidoExistente.setPagamento(pedidoBodyDTO.getPagamento());
        pedidoExistente.setStatusPagamento(pedidoBodyDTO.getStatusPagamento());
        pedidoExistente.setPizzas(pedidoBodyDTO.getPizzas());

        return this.pedidoRepository.save(pedidoExistente);
    }

    @Override
    public Pedido confirmarPagamento(Long idPedido, String codigoCliente, Pagamento pagamento) {
        Pedido pedidoEncontrado = this.pedidoRepository
                            .findById(idPedido)
                            .orElseThrow(OrderDoesNotExistException::new);

        Cliente clienteEncontrado = this.clienteRepository
                            .findById(pedidoEncontrado.getCliente().getIdCliente())
                            .orElseThrow(ClientDoesNotExistException::new);

        if (!clienteEncontrado.getCodigoCliente().equals(codigoCliente)) {
            throw new NotAuthorizedException();
        }

        else {
            pedidoEncontrado.setStatusPagamento(true);
            pedidoEncontrado.setPagamento(pagamento);
            Double valorComDesconto = pedidoEncontrado.calculaValorTotal();
            pedidoEncontrado.setValorPedido(valorComDesconto);
            pedidoEncontrado.setStatusEntrega("Pedido em preparo");
            return this.pedidoRepository.save(pedidoEncontrado);
        }
    }

    @Override
    public Pedido atribuirEntregador(Long idPedido, Long idEntregador) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(OrderDoesNotExistException::new);
        Entregador entregador = entregadorRepository.findById(idEntregador).orElseThrow(DelivererDoesNotExistException::new);

        if (pedido.getStatusEntrega().equals("Pedido pronto")) {
            pedido.setEntregador(entregador);
            pedido.setStatusEntrega("Pedido em rota");
            pedido.getCliente().notificarPedidoEmRota(entregador);
            return pedidoRepository.save(pedido);
        }

        throw new NotAuthorizedException();
    }

}
