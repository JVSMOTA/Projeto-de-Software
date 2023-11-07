import java.util.List;

public class PessoaController {
    private PessoaDAO pessoaDAO;

    public PessoaController(PessoaDAO pessoaDAO) {
        this.pessoaDAO = pessoaDAO;
    }

    public void criarPessoa(Pessoa pessoa) {
        pessoaDAO.create(pessoa);
    }

    public Pessoa lerPessoa(String cpf) {
        return pessoaDAO.read(cpf);
    }

    public void atualizarPessoa(Pessoa pessoa) {
        pessoaDAO.update(pessoa);
    }

    public void excluirPessoa(String cpf) {
        pessoaDAO.delete(cpf);
    }

    public List<Pessoa> listarPessoas() {
        return pessoaDAO.getAll();
    }
}
