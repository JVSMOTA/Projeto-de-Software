package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedidodto.*;
import com.ufcg.psoft.commerce.model.*;

public interface PedidoBodyServiceInterface {

    public Pedido post(String codigoCliente, PedidoBodyDTO pedidoBodyDTO);

    public Pedido put(Long idPedido, String codigoCliente, PedidoBodyDTO pedidoBodyDTO);

    public Pedido putStatusPedidoPronto(Long idPedido, String codigoEstabelecimento, PedidoBodyDTO pedidoBodyDTO);

    public Pedido putStatusPedidoEntregue(Long idPedido, Long idCliente, String codigoCliente, PedidoBodyDTO pedidoBodyDTO);

    public Pedido confirmarPagamento(Long idPedido, String codigoCliente, Pagamento pagamento);

    Pedido atribuirEntregador(Long idPedido, Long idEntregador);

}
