package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EstabelecimentoGetService implements EstabelecimentoGetServiceInterface {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public List<Sabor> buscarCardapioPorEstabelecimento(Long idEstabelecimento) {
        Estabelecimento estabelecimento = this.estabelecimentoRepository
                                        .findById(idEstabelecimento)
                                        .orElseThrow(CommerceDoesNotExistException::new);

        List<Sabor> cardapio = new ArrayList<>();
        List<Sabor> indisponiveis = new ArrayList<>();

        for (Sabor s: estabelecimento.getListaSabores()){
            if (s.getDisponivel()){
                cardapio.add(s);
            } else {
                indisponiveis.add(s);
            }
        }

        cardapio.addAll(indisponiveis);

        return cardapio;
    }

    @Override
    public List<Sabor> buscarCardapioSalgados(Long idEstabelecimento){
        List<Sabor> cardapio = buscarCardapioPorEstabelecimento(idEstabelecimento);
        List<Sabor> salgados = new ArrayList<>();
        for (Sabor s: cardapio){
            if (s.getTipoSabor().toLowerCase().contains("salgado")){
                salgados.add(s);
            }
        }
        return salgados;
    }

    @Override
    public List<Sabor> buscarCardapioDoces(Long idEstabelecimento){
        List<Sabor> cardapio = buscarCardapioPorEstabelecimento(idEstabelecimento);
        List<Sabor> doces = new ArrayList<>();
        for (Sabor s: cardapio){
            if (s.getTipoSabor().toLowerCase().contains("doce")){
                doces.add(s);
            }
        }
        return doces;
    }

}
