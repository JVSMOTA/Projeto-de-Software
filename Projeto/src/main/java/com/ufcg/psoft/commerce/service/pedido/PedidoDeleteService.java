package com.ufcg.psoft.commerce.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;

@Service
public class PedidoDeleteService implements PedidoDeleteServiceInterface {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public void deleteOne(Long idPedido, String codigoCliente) {
        Pedido pedidoEncontrado = this.pedidoRepository
                    .findById(idPedido)
                    .orElseThrow(OrderDoesNotExistException::new);
        if (!pedidoEncontrado.getCliente().getCodigoCliente().equals(codigoCliente)
            || pedidoEncontrado.getStatusEntrega().equals("Pedido pronto")) {
            throw new NotAuthorizedException();
        }

        this.pedidoRepository.delete(pedidoEncontrado);
    }

    @Override
    public void deleteAll() {
        this.pedidoRepository.deleteAll();
    }

    @Override
    public void estabelecimentoDeleteOne(Long idPedido, Long idEstabelecimento, String codigoEstabelecimento) {
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
        this.pedidoRepository.delete(pedidoEncontrado);
    }

    @Override
    public void estabelecimentoDeleteAll() {
        this.pedidoRepository.deleteAll();
    }
    
}
