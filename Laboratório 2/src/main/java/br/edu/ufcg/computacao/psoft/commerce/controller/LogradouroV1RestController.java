package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.LogradouroPatchNomeDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.LogradouroPatchTipoDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.logradouro.LogradouroPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.service.logradouro.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/logradouros", produces = MediaType.APPLICATION_JSON_VALUE
)
public class LogradouroV1RestController {

    @Autowired
    LogradouroPostService logradouroPostService;

    @Autowired
    LogradouroPutService logradouroPutService;

    @Autowired
    LogradouroPatchTipoService logradouroPatchTipoService;

    @Autowired
    LogradouroPatchNomeService logradouroPatchNomeService;

    @Autowired
    LogradouroDeleteService logradouroDeleteService;

    @Autowired
    LogradouroGetOneService logradouroGetOneService;

    @Autowired
    LogradouroGetAllService logradouroGetAllService;

    @PostMapping
    ResponseEntity<?> logradouroPost(
            @RequestBody LogradouroPostPutDTO logradouroPostPutDTO
            ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(logradouroPostService.post(logradouroPostPutDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> logradouroPut(
            @PathVariable("id") Long id,
            @RequestBody @Valid LogradouroPostPutDTO logradouroPostPutDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logradouroPutService.put(id, logradouroPostPutDTO));
    }

    @PatchMapping("/{id}/tipoLogradouro")
    ResponseEntity<?> logradouroTipoPatch(
            @PathVariable("id") Long id,
            @RequestBody @Valid LogradouroPatchTipoDTO logradouroPatchTipoDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logradouroPatchTipoService.patchTipoLogradouro(id, logradouroPatchTipoDTO));
    }

    @PatchMapping("/{id}/nome")
    ResponseEntity<?> logradouroNomePatch(
            @PathVariable("id") Long id,
            @RequestBody @Valid LogradouroPatchNomeDTO logradouroPatchNomeDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logradouroPatchNomeService.patchNomeLogradouro(id, logradouroPatchNomeDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> logradouroDelete(
            @PathVariable("id") Long id
    ) {
        logradouroDeleteService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    ResponseEntity<?> logradouroGetOne(
            @PathVariable("id") Long id
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logradouroGetOneService.getOne(id));
    }

    @GetMapping
    ResponseEntity<?> logradouroGetAll(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logradouroGetAllService.getAll());
    }
}
