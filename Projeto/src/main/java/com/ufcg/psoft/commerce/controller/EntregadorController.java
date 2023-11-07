package com.ufcg.psoft.commerce.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.service.entregador.*;
import com.ufcg.psoft.commerce.dto.entregadordto.*;
import jakarta.validation.*;

@RestController
@RequestMapping(
        value = "/entregadores",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class EntregadorController {

    @Autowired
    EntregadorBodyServiceInterface entregadorBodyService;

    @Autowired
    EntregadorGetServiceInterface entregadorGetService;

    @Autowired
    EntregadorDeleteServiceInterface entregadorDeleteService;

    @PostMapping
    ResponseEntity<?> entregadorPost(@RequestBody @Valid EntregadorBodyDTO entregadorBodyDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.entregadorBodyService.post(entregadorBodyDTO));
    }

    @PutMapping("/{idEntregador}")
    ResponseEntity<?> entregadorPut(@PathVariable(value = "idEntregador") Long idEntregador,
                                @RequestParam(value = "codigoEntregador", required = true) String codigoEntregador,
                                @RequestBody @Valid EntregadorBodyDTO entregadorBodyDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.entregadorBodyService.put(idEntregador, codigoEntregador, entregadorBodyDTO));
    }

    @GetMapping("/{idEntregador}")
    ResponseEntity<?> entregadorGetOne(@PathVariable(value = "idEntregador") Long idEntregador) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.entregadorGetService.getOne(idEntregador));
    }

    @GetMapping
    ResponseEntity<?> entregadorGetAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.entregadorGetService.getAll());
    }

    @DeleteMapping("/{idEntregador}")
    ResponseEntity<?> entregadorDelete(@PathVariable(value = "idEntregador") Long idEntregador,
                                    @RequestParam(value = "codigoEntregador", required = true) String codigoEntregador) {
        this.entregadorDeleteService.delete(idEntregador, codigoEntregador);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{idEntregador}/disponibilidade")
    ResponseEntity<?> entregadorDisponibilidade(@PathVariable(value = "idEntregador") Long idEntregador,
                                @RequestParam(value = "codigoEntregador", required = true) String codigoEntregador,
                                @RequestParam(value = "disponibilidade") Boolean disponibilidade) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.entregadorBodyService.alteraDisponibilidade(idEntregador, codigoEntregador, disponibilidade));
    }

}
