import java.util.List;

public interface PessoaDAO {
    void create(Pessoa pessoa);
    Pessoa read(String cpf);
    void update(Pessoa pessoa);
    void delete(String cpf);
    List<Pessoa> getAll();
}