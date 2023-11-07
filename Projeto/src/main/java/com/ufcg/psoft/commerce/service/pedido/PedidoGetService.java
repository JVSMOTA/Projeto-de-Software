package com.ufcg.psoft.commerce.service.pedido;

import org.springframework.stereotype.Service;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoGetService implements PedidoGetServiceInterface {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Pedido getOne(Long idPedido, Long idCliente, String codigoCliente) {
        Pedido pedidoEncontrado = this.pedidoRepository
                        .findById(idPedido)
                        .orElseThrow(OrderDoesNotExistException::new);

        Cliente clienteEncontrado = this.clienteRepository
                        .findById(idCliente)
                        .orElseThrow(ClientDoesNotExistException::new);

        Boolean pedidoDoCliente = pedidoEncontrado.getCliente().getIdCliente().equals(clienteEncontrado.getIdCliente());
        Boolean codigoClienteValido = clienteEncontrado.getCodigoCliente().equals(codigoCliente);

        if (!pedidoDoCliente || !codigoClienteValido) {
            throw new NotAuthorizedException();
        }
        return pedidoEncontrado;
    }

    @Override
    public Collection<Pedido> getAll() {
        List<Pedido> pedidosEncontrados = this.pedidoRepository.findAll();
        return pedidosEncontrados;
    }

    @Override
    public Pedido clienteVisualizarPedido(Long idPedido, Long idCliente, 
                                        Long idEstabelecimento, String codigoCliente) {
        Pedido pedidoEncontrado = this.pedidoRepository
                        .findById(idPedido)
                        .orElseThrow(OrderDoesNotExistException::new);

        Cliente clienteEncontrado = this.clienteRepository
                        .findById(idCliente)
                        .orElseThrow(ClientDoesNotExistException::new);

        Boolean pedidoDoCliente = pedidoEncontrado.getCliente().getIdCliente().equals(clienteEncontrado.getIdCliente());
        Boolean codigoClienteValido = clienteEncontrado.getCodigoCliente().equals(codigoCliente);

        if (!pedidoDoCliente || !codigoClienteValido) {
            throw new NotAuthorizedException();
        }
        if (!pedidoEncontrado.getEstabelecimento().getIdEstabelecimento().equals(idEstabelecimento)) {
            throw new CommerceDoesNotExistException();
        }
        return pedidoEncontrado;
    }

    @Override
    public Collection<Pedido> clienteHistoricoPedidos(Long idCliente, Long idEstabelecimento, String codigoCliente) {
        Cliente clienteEncontrado = this.clienteRepository
                        .findById(idCliente)
                        .orElseThrow(ClientDoesNotExistException::new);
        
        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                        .findById(idEstabelecimento)
                        .orElseThrow(CommerceDoesNotExistException::new);
        
        Boolean codigoClienteValido = clienteEncontrado.getCodigoCliente().equals(codigoCliente);

        if (!codigoClienteValido) {
            throw new NotAuthorizedException();
        }
        if (!estabelecimentoEncontrado.getIdEstabelecimento().equals(idEstabelecimento)) {
            throw new CommerceDoesNotExistException();
        }

        List<Pedido> pedidos = new ArrayList<>();
        pedidoRepository.findAll().forEach(pedido -> {
            if (pedido.getEstabelecimento().getIdEstabelecimento().equals(idEstabelecimento) && 
                (pedido.getCliente().getIdCliente().equals(idCliente))) {
                pedidos.add(pedido);
            }
        });
        return pedidos.stream()
                .filter(pedido -> (pedido.getStatusEntrega() != null))
                .sorted(Comparator.comparing((Pedido pedido) -> (pedido.getStatusEntrega() != null) ? 1 : 0)
                        .thenComparing(Pedido::getStatusEntrega))
                .map(pedido -> modelMapper.map(pedido, Pedido.class))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Pedido> clienteHistoricoPedidosFiltrado(Long idCliente, Long idEstabelecimento, String codigoCliente, String statusEntrega) {
        Cliente clienteEncontrado = this.clienteRepository
                .findById(idCliente)
                .orElseThrow(ClientDoesNotExistException::new);

        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                .findById(idEstabelecimento)
                .orElseThrow(CommerceDoesNotExistException::new);

        Boolean codigoClienteValido = clienteEncontrado.getCodigoCliente().equals(codigoCliente);

        if (!codigoClienteValido) {
            throw new NotAuthorizedException();
        }
        if (!estabelecimentoEncontrado.getIdEstabelecimento().equals(idEstabelecimento)) {
            throw new CommerceDoesNotExistException();
        }

        List<Pedido> pedidos = new ArrayList<>();
        pedidoRepository.findAll().forEach(pedido -> {
            if (pedido.getEstabelecimento().getIdEstabelecimento().equals(idEstabelecimento) &&
                    (pedido.getCliente().getIdCliente().equals(idCliente))) {
                pedidos.add(pedido);
            }
        });

        return pedidos.stream()
                .filter(pedido -> (Objects.equals(pedido.getStatusEntrega(), statusEntrega)))
                .map(pedido -> modelMapper.map(pedido, Pedido.class))
                .sorted(Comparator.comparing((Pedido pedido) -> (Objects.equals(pedido.getStatusEntrega(), statusEntrega)) ? 1 : 0)
                        .thenComparing(Pedido::getStatusEntrega))
                .collect(Collectors.toList());
                
    }

    @Override
    public Pedido estabelecimentoVisualizarPedido(Long idPedido, Long idEstabelecimento, 
                                            String codigoEstabelecimento) {

        Pedido pedidoEncontrado = this.pedidoRepository
                        .findById(idPedido)
                        .orElseThrow(OrderDoesNotExistException::new);

        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                        .findById(idEstabelecimento)
                        .orElseThrow(CommerceDoesNotExistException::new);

        Boolean pedidoDoEstabelecimento = pedidoEncontrado.getEstabelecimento().getIdEstabelecimento().equals(estabelecimentoEncontrado.getIdEstabelecimento());
        Boolean codigoEstabelecimentoValido = estabelecimentoEncontrado.getCodigoEstabelecimento().equals(codigoEstabelecimento);

        if (!pedidoDoEstabelecimento || !codigoEstabelecimentoValido) {
            throw new NotAuthorizedException();
        }
        return pedidoEncontrado;
    }

    @Override
    public Collection<Pedido> estabelecimentoListarPedidos(Long idEstabelecimento, String codigoEstabelecimento) {
        Estabelecimento estabelecimentoEncontrado = this.estabelecimentoRepository
                        .findById(idEstabelecimento)
                        .orElseThrow(CommerceDoesNotExistException::new);

        Boolean codigoEstabelecimentoValido = estabelecimentoEncontrado.getCodigoEstabelecimento().equals(codigoEstabelecimento);

        if (!codigoEstabelecimentoValido) {
            throw new NotAuthorizedException();
        }
        List<Pedido> pedidosEncontrados = this.pedidoRepository.findAll();
        return pedidosEncontrados;
    }

}
