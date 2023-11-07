package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.model.Endereco;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/pessoas")
public class PessoaV1RestController {

    private final Map<Integer, Pessoa> pessoaMap = new HashMap<>();
    private Integer nextPessoaId = 1;

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> getPessoa(@PathVariable Integer id){
        if (pessoaMap.containsKey(id)){
            return ResponseEntity.ok(pessoaMap.get(id));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> getAllPessoa(){
        if (pessoaMap.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ArrayList<>(pessoaMap.values()));
    }

    @PostMapping
    public ResponseEntity<Void> postPessoa(@RequestBody Pessoa pessoa){
        pessoa.setId(nextPessoaId++);
        pessoaMap.put(pessoa.getId(), pessoa);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putPessoa(@PathVariable Integer id, @RequestBody Pessoa pessoa) {
        if (pessoaMap.containsKey(id)){
            if (pessoa.getNome().equals(pessoaMap.get(id).getNome()) && pessoa.getCpf().equals(pessoaMap.get(id).getCpf()) ) {
                pessoa.setId(id);
                pessoaMap.put(id, pessoa);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePessoa(@PathVariable Integer id){
        if (pessoaMap.containsKey(id)){
            pessoaMap.remove(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchPessoa(@PathVariable Integer id, @RequestBody Map<String, String> update){
        if (pessoaMap.containsKey(id)){
            if (update.containsKey("email")){
                Pessoa pessoa = pessoaMap.get(id);
                pessoa.setEmail(update.get("email"));
                pessoaMap.put(id, pessoa);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/enderecos")
    public ResponseEntity<Pessoa> adicionarEndereco(@PathVariable Integer id, @RequestBody Endereco endereco){
        if (pessoaMap.containsKey(id)){
            Pessoa pessoa = pessoaMap.get(id);
            pessoa.adicionarEndereco(endereco);
            pessoaMap.put(id, pessoa);
            return ResponseEntity.ok(pessoa);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/enderecos/{enderecoId}")
    public ResponseEntity<Pessoa> removerEndereco(@PathVariable Integer id, @PathVariable Integer enderecoId){
        if (pessoaMap.containsKey(id)){
            Pessoa pessoa = pessoaMap.get(id);
            Endereco endereco = pessoa.getEnderecos().stream()
                    .filter(e -> e.getId().equals(enderecoId))
                    .findFirst()
                    .orElse(null);
            if (endereco != null) {
                pessoa.removerEndereco(endereco);
                pessoaMap.put(id, pessoa);
                return ResponseEntity.ok(pessoa);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

}
