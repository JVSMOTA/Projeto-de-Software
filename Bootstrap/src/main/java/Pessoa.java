import java.util.List;

public class Pessoa {
    private String nome;
    private int idade;
    private String cpf;
    private String telefone;
    private List<String> enderecos;
    private String profissao;

    public Pessoa(String nome, int idade, String cpf, String telefone, List<String> enderecos, String profissao) {
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.telefone = telefone;
        this.enderecos = enderecos;
        this.profissao = profissao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<String> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<String> enderecos) {
        this.enderecos = enderecos;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

}