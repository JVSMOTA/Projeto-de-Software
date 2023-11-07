package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.model.Sabor;
import java.util.*;

public interface SaborGetServiceInterface {

    public Sabor getOne(Long idSabor, Long idEstabelecimento, String codigoEstabelecimento);

    public Collection<Sabor> getAll();
    
}
