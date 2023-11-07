package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoa.*;
import br.edu.ufcg.computacao.psoft.commerce.service.pessoa.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/pessoas", produces = MediaType.APPLICATION_JSON_VALUE
)
public class PessoaV1RestController {

    @Autowired
    PessoaPostService pessoaPostService;
    @Autowired
    PessoaPutService pessoaPutService;
    @Autowired
    PessoaPatchEmailService pessoaPatchEmailService;
    @Autowired
    PessoaDeleteService pessoaDeleteService;
    @Autowired
    PessoaGetOneService pessoaGetOneService;
    @Autowired
    PessoaGetAllService pessoaGetAllService;

    @PostMapping
    ResponseEntity<?> pessoaPost(
            @RequestBody PessoaPostDTO pessoaPostDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pessoaPostService.post(pessoaPostDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> pessoaPut(
            @PathVariable("id") Long id,
            @RequestBody @Valid PessoaPutDTO pessoaPutDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pessoaPutService.put(id, pessoaPutDTO));
    }

    @PatchMapping("/{id}/email")
    ResponseEntity<?> pessoaEmailPatch(
            @PathVariable("id") Long id,
            @RequestBody @Valid PessoaPatchEmailDTO pessoaPatchEmailDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pessoaPatchEmailService.patchEmail(id, pessoaPatchEmailDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> pessoaDelete(
            @PathVariable("id") Long id
    ) {
        pessoaDeleteService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    ResponseEntity<?> pessoaGetOne(
            @PathVariable("id") Long id
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pessoaGetOneService.getOne(id));
    }

    @GetMapping
    ResponseEntity<?> pessoaGetAll(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pessoaGetAllService.getAll());
    }

}
