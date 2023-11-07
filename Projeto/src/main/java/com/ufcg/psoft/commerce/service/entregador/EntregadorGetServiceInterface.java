package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.model.Entregador;
import java.util.*;

public interface EntregadorGetServiceInterface {

    public Entregador getOne(Long idEntregador);

    public Collection<Entregador> getAll();

}
