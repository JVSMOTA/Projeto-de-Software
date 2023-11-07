package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.produto.ProdutoPatchCodigoBarrasDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.produto.ProdutoPostDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.produto.ProdutoPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.service.produto.*;
import br.edu.ufcg.computacao.psoft.commerce.service.produto.ProdutoPatchCodigoBarrasService;
import br.edu.ufcg.computacao.psoft.commerce.service.produto.ProdutoPostService;
import br.edu.ufcg.computacao.psoft.commerce.service.produto.ProdutoPutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/produtos", produces = MediaType.APPLICATION_JSON_VALUE
)
public class ProdutoV1RestController {

    @Autowired
    ProdutoPostService produtoPostService;
    @Autowired
    ProdutoPutService produtoPutService;
    @Autowired
    ProdutoPatchCodigoBarrasService produtoPatchCodigoBarrasService;
    @Autowired
    ProdutoDeleteService produtoDeleteService;
    @Autowired
    ProdutoGetOneService produtoGetOneService;
    @Autowired
    ProdutoGetAllService produtoGetAllService;


    @PostMapping
    ResponseEntity<?> produtoPost(
            @RequestBody ProdutoPostDTO produtoPostDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoPostService.post(produtoPostDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> produtoPut(
            @PathVariable("id") Long id,
            @RequestBody @Valid ProdutoPutDTO produtoPutDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoPutService.put(id, produtoPutDTO));
    }

    @PatchMapping("/{id}/codigoBarras")
    ResponseEntity<?> produtoCodigoBarrasPatch(
            @PathVariable("id") Long id,
            @RequestBody @Valid ProdutoPatchCodigoBarrasDTO produtoPatchCodigoBarrasDTO
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoPatchCodigoBarrasService.patchCodigoBarras(id, produtoPatchCodigoBarrasDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> produtoDelete(
            @PathVariable("id") Long id
    ) {
        produtoDeleteService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/{id}")
    ResponseEntity<?> produtoGetOne(
            @PathVariable("id") Long id
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoGetOneService.getOne(id));
    }

    @GetMapping
    ResponseEntity<?> produtoGetAll(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoGetAllService.getAll());
    }
}
