package com.ufcg.psoft.commerce.service.sabor;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaborNotificarPadraoService implements SaborNotificarService{
    @Autowired
    SaborRepository saborRepository;

    @Override
    public List<String> notificar(Long idSabor) {
        Sabor sabor = saborRepository
                        .findById(idSabor)
                        .orElseThrow(RuntimeException::new);

        List<Cliente> interessados = sabor.getClientesInteressados();
        List<String> saida = new ArrayList<String>();

        if (!interessados.isEmpty()) {
            for (Cliente interessado : interessados) {
                String notificacao = "Notificando cliente de ID " + interessado + " sobre disponibilidade de sabor";
                System.out.println(notificacao);
                saida.add(notificacao);
            }

            sabor.setClientesInteressados(new ArrayList<Cliente>());
            return saida;
        }

        return saida;
    }
}
