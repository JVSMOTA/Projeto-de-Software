import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAOImpl implements PessoaDAO {
    private Map<String, Pessoa> pessoaMap = new HashMap<>();

    @Override
    public void create(Pessoa pessoa) {
        pessoaMap.put(pessoa.getCpf(), pessoa);
    }

    @Override
    public Pessoa read(String cpf) {
        return pessoaMap.get(cpf);
    }

    @Override
    public void update(Pessoa pessoa) {
        if (pessoaMap.containsKey(pessoa.getCpf())) {
            pessoaMap.put(pessoa.getCpf(), pessoa);
        }
        return;
    }

    @Override
    public void delete(String cpf) {
        pessoaMap.remove(cpf);
    }

    @Override
    public List<Pessoa> getAll() {
        return new ArrayList<>(pessoaMap.values());
    }
}
