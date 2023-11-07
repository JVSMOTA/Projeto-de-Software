[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/CmgXzpBG)
[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=11521126&assignment_repo_type=AssignmentRepo)
# 🛒 PSoft-Commerce

Projeto básico da disciplina PSoft a ser evoluído em sala de aula nas atividades (assignments) do GitHub Classroom.

### 🔗 Endereços Úteis

- [SpringDoc/Swagger](http://localhost:8080/swagger-ui/index.html)
- [H2 Console](http://localhost:8080/h2-console)

## 📑 User Stories a serem implementadas

- [X] **USX:** Eu, enquanto Pessoa, quero utilizar o sistema para me cadastrar. Mais detalhadamente, deve ser possível criar, ler, editar e remover pessoas;
- Atributos esperados para pessoa: identificador único (numérico), nome, cpf (único), email (único), telefone, data de nascimento endereço e profissão.
- Cada pessoa criada no sistema pode ter mais de um endereço cadastrado, mas apenas uma profissão. Apenas o nome e cpf de uma pessoa não podem ser atualizados.
- A atualização simplificada da pessoa deve permitir a alteração do email.

- [X] **USY:** Eu, enquanto Pessoa, quero utilizar o sistema para cadastrar meu Endereço. Mais detalhadamente, deve ser possível criar, ler, editar e remover endereços;
- Atributos esperados no endereço: identificador único (numérico), tipo de logradouro, logradouro, número, complemento, bairro, cidade, estado, país e código de endereçamento postal (CEP).
- A atualização simplificada do endereço deve permitir a alteração do valor do número e complemento.

Observação: utilize coleções nos controladores para "salvar" a entidade manipulada.