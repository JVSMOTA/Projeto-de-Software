package com.ufcg.psoft.commerce.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.dto.sabordto.*;
import com.ufcg.psoft.commerce.service.sabor.*;
import jakarta.validation.*;

@RestController
@RequestMapping(
        value = "/sabores",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class SaborController {

        @Autowired
        SaborBodyServiceInterface saborBodyService;

        @Autowired
        SaborGetServiceInterface saborGetService;

        @Autowired
        SaborDeleteServiceInterface saborDeleteService;

        @PostMapping
        ResponseEntity<?> saborPost(@RequestBody @Valid SaborBodyDTO saborBodyDTO,
                                    @RequestParam("idEstabelecimento") Long idEstabelecimento,
                                    @RequestParam(value = "codigoEstabelecimento", required = true) String codigoEstabelecimento) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(this.saborBodyService.post(idEstabelecimento, codigoEstabelecimento, saborBodyDTO));
        }

        @PutMapping("/{idSabor}")
        ResponseEntity<?> saborPut(@PathVariable(value = "idSabor") Long idSabor,
                                   @RequestBody @Valid SaborBodyDTO saborBodyDTO,
                                   @RequestParam("idEstabelecimento") Long idEstabelecimento,
                                   @RequestParam(value = "codigoEstabelecimento", required = true) String codigoEstabelecimento) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(this.saborBodyService.put(idEstabelecimento, codigoEstabelecimento, idSabor, saborBodyDTO));
        }

        @GetMapping("/{idSabor}")
        ResponseEntity<?> saborGetOne(@PathVariable(value = "idSabor") Long idSabor,
                                      @RequestParam("idEstabelecimento") Long idEstabelecimento,
                                      @RequestParam(value = "codigoEstabelecimento", required = true) String codigoEstabelecimento) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(this.saborGetService.getOne(idSabor, idEstabelecimento, codigoEstabelecimento));
        }

        @GetMapping
        ResponseEntity<?> saborGetAll(@RequestParam("idEstabelecimento") Long idEstabelecimento,
                                      @RequestParam(value = "codigoEstabelecimento", required = true) String codigoEstabelecimento) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(this.saborGetService.getAll());
        }

        @DeleteMapping("/{idSabor}")
        ResponseEntity<?> saborDelete(@PathVariable(value = "idSabor") Long idSabor,
                                @RequestParam("idEstabelecimento") Long idEstabelecimento,
                                @RequestParam(value = "codigoEstabelecimento", required = true) String codigoEstabelecimento) {
                this.saborDeleteService.delete(idSabor, idEstabelecimento, codigoEstabelecimento);
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .build();
        }

}
