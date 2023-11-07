package com.ufcg.psoft.commerce.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.service.cliente.*;
import com.ufcg.psoft.commerce.dto.clientedto.*;
import org.springframework.http.*;
import jakarta.validation.*;

@RestController
@RequestMapping(
        value = "/clientes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ClienteController {

        @Autowired
        ClienteBodyServiceInterface clienteBodyService;

        @Autowired
        ClienteGetServiceInterface clienteGetService;

        @Autowired
        ClienteDeleteServiceInterface clienteDeleteService;

        @Autowired
        ClienteDemonstrarInteresseServiceInterface clienteDemonstrarInteresseService;

        @PostMapping
        ResponseEntity<?> clientePost(@RequestBody @Valid ClienteBodyDTO clienteBodyDTO) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(this.clienteBodyService.post(clienteBodyDTO));
        }

        @PutMapping("/{idCliente}")
        ResponseEntity<?> clientePut(@PathVariable(value = "idCliente") Long idCliente,
                                     @RequestParam(value = "codigoCliente", required = true) String codigoCliente,
                                     @RequestBody @Valid ClienteBodyDTO clienteBodyDTO) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(this.clienteBodyService.put(idCliente, codigoCliente, clienteBodyDTO));
        }

        @GetMapping("/{idCliente}")
        ResponseEntity<?> clienteGetOne(@PathVariable(value = "idCliente") Long idCliente) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(this.clienteGetService.getOne(idCliente));
        }

        @GetMapping
        ResponseEntity<?> clienteGetAll() {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(this.clienteGetService.getAll());
        }

        @DeleteMapping("/{idCliente}")
        ResponseEntity<?> clienteDelete(@PathVariable(value = "idCliente") Long idCliente,
                                        @RequestParam(value = "codigoCliente", required = true) String codigoCliente) {
                this.clienteDeleteService.delete(idCliente, codigoCliente);
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .build();
        }

        @PutMapping("/{idCliente}/demonstrarInteresse")
        ResponseEntity<?> demonstrarInteresseSabor(@PathVariable Long idCliente,
                                        @RequestParam(value = "codigoCliente", required = true) String codigoCliente,
                                        @RequestParam Long idSabor) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(this.clienteDemonstrarInteresseService.demonstrarInteresseSabor(
                                idCliente, codigoCliente, idSabor));
        }

}
