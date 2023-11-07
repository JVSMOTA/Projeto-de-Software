package com.ufcg.psoft.commerce.service.pedido;

public interface PedidoDeleteServiceInterface {

    public void deleteOne(Long idPedido, String codigoCliente);

    public void deleteAll();

    public void estabelecimentoDeleteOne(Long idPedido, Long idEstabelecimento, String codigoEstabelecimento);

    public void estabelecimentoDeleteAll();
    
}
