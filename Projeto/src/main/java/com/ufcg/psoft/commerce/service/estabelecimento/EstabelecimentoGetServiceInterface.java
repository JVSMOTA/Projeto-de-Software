package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.model.Sabor;
import java.util.*;

public interface EstabelecimentoGetServiceInterface {

    public Collection<Sabor> buscarCardapioPorEstabelecimento(Long idEstabelecimento);

    public Collection<Sabor> buscarCardapioSalgados(Long idEstabelecimento);

    public Collection<Sabor> buscarCardapioDoces(Long idEstabelecimento);

}
