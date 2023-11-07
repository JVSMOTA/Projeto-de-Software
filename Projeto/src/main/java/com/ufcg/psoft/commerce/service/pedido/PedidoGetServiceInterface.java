package com.ufcg.psoft.commerce.service.pedido;

import java.util.*;
import com.ufcg.psoft.commerce.model.Pedido;

public interface PedidoGetServiceInterface {

    public Pedido getOne(Long idPedido, Long idCliente, String codigoCliente);

    public Collection<Pedido> getAll();

    public Pedido clienteVisualizarPedido(Long idPedido, Long idCliente, Long idEstabelecimento, String codigoCliente);

    public Collection<Pedido> clienteHistoricoPedidos(Long idCliente, Long idEstabelecimento, String codigoCliente);

    public Collection<Pedido> clienteHistoricoPedidosFiltrado(Long idCliente, Long idEstabelecimento, String codigoCliente, String statusEntrega);

    public Pedido estabelecimentoVisualizarPedido(Long idPedido, Long idEstabelecimento, String codigoEstabelecimento);

    public Collection<Pedido> estabelecimentoListarPedidos(Long idEstabelecimento, String codigoEstabelecimento);
    
}
