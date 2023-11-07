package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.estabelecimentodto.*;
import com.ufcg.psoft.commerce.service.estabelecimento.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/estabelecimentos",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class EstabelecimentoController {

    @Autowired
    EstabelecimentoBodyServiceInterface estabelecimentoBodyService;

    @Autowired
    EstabelecimentoDeleteServiceInterface estabelecimentoDeleteService;

    @Autowired
    EstabelecimentoGetServiceInterface estabelecimentoGetService;

    @PostMapping
    ResponseEntity<?> estabelecimentoPost(@RequestBody @Valid EstabelecimentoBodyDTO estabelecimentoBodyDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estabelecimentoBodyService.post(estabelecimentoBodyDTO));
    }

    @PutMapping("/{idEstabelecimento}")
    ResponseEntity<?> estabelecimentoPut(@PathVariable("idEstabelecimento") Long idEstabelecimento,
                                @RequestBody @Valid EstabelecimentoBodyDTO estabelecimentoBodyDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoBodyService.put(idEstabelecimento, estabelecimentoBodyDTO));
    }

    @DeleteMapping("/{idEstabelecimento}")
    ResponseEntity<?> estabelecimentoDelete(@PathVariable("idEstabelecimento") Long idEstabelecimento) {
        estabelecimentoDeleteService.delete(idEstabelecimento);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{idEstabelecimento}/sabores")
    public ResponseEntity<?> buscarCardapioDoEstabelecimento(@PathVariable Long idEstabelecimento) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoGetService.buscarCardapioPorEstabelecimento(idEstabelecimento));
    }

    @GetMapping("/{idEstabelecimento}/sabores/salgados")
    public ResponseEntity<?> buscarCardapioSalgados(@PathVariable Long idEstabelecimento){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoGetService.buscarCardapioSalgados(idEstabelecimento));
    }

    @GetMapping("/{idEstabelecimento}/sabores/doces")
    public ResponseEntity<?> buscarCardapioDoces(@PathVariable Long idEstabelecimento){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoGetService.buscarCardapioDoces(idEstabelecimento));
    }

}
