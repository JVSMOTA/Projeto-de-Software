package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.service.associacao.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/associacoes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AssociacaoController {

    @Autowired
    AssociacaoServiceInterface associacaoService;

    @PostMapping("/solicitarAssociacao/{idEstabelecimento}/{idEntregador}")
    public ResponseEntity<?> solicitarAssociacao(@RequestParam(value = "codigoEntregador", required = true) String codigoEntregador,
                                                @PathVariable(value = "idEstabelecimento") Long idEstabelecimento,
                                                @PathVariable(value = "idEntregador") Long idEntregador){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(associacaoService.associarEntregadorEstabelecimento(codigoEntregador, idEstabelecimento, idEntregador));
    }

    @PutMapping("/aceitarEntregador/{idEstabelecimento}/{idEntregador}")
    public ResponseEntity<?> associarEntregador(@RequestParam(value = "codigoEstabelecimento", required = true) String codigoEstabelecimento,
                                                @PathVariable(value = "idEstabelecimento") Long idEstabelecimento,
                                                @PathVariable(value = "idEntregador") Long idEntregador){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(associacaoService.aceitarAssociacao(codigoEstabelecimento, idEstabelecimento, idEntregador));
    }
    
}
