package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.model.Endereco;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/enderecos")
public class EnderecoV1RestController {

    private final Map<Integer, Endereco> enderecoMap = new HashMap<>();
    private Integer nextEnderecoId = 1;

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> getEndereco(@PathVariable Integer id){
        if (enderecoMap.containsKey(id)){
            return ResponseEntity.ok(enderecoMap.get(id));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> getAllEndereco(){
        if (enderecoMap.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ArrayList<>(enderecoMap.values()));
    }

    @PostMapping
    public ResponseEntity<Void> postEndereco(@RequestBody Endereco endereco){
        endereco.setId(nextEnderecoId++);
        enderecoMap.put(endereco.getId(), endereco);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putEndereco(@PathVariable Integer id, @RequestBody Endereco endereco){
        if (enderecoMap.containsKey(id)){
            if (endereco.getNumero().equals(enderecoMap.get(id).getNumero()) && endereco.getComplemento().equals(enderecoMap.get(id).getComplemento())){
                endereco.setId(id);
                enderecoMap.put(id, endereco);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndereco(@PathVariable Integer id){
        if (enderecoMap.containsKey(id)) {
            enderecoMap.remove(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchEndereco(@PathVariable Integer id, @RequestBody Map<String, String> update){
        if (enderecoMap.containsKey(id)){
            if (update.containsKey("numero") || update.containsKey("complemento")){
                Endereco endereco = enderecoMap.get(id);
                endereco.setNumero(Integer.valueOf(update.get("numero")));
                endereco.setComplemento(update.get("complemento"));
                enderecoMap.put(id, endereco);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
