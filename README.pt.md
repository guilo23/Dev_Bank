<a href="https://github.com/guilo23/Dev_Bank/actions/workflows/ci.yaml">
  <img src="https://github.com/guilo23/Dev_Bank/actions/workflows/ci.yaml/badge.svg" alt="CI - Dev_Bank"/>
</a>

<h1 align="center" style="font-size: 3em;">Dev_Bank</h1>

Para ver a versão em inglês deste documento, clique [aqui](README.md).

## 💻 Sobre o projeto

Dev_Bank é um sistema bancário completo que permite simular operações financeiras comuns, como abertura de contas, transferências, empréstimos, emissão de ordens de pagamento e geração de relatórios de movimentações.

> Este projeto foi desenvolvido exclusivamente para fins educacionais e de aprendizado.

## ⚙️ Funcionalidades

- Abertura e gerenciamento de contas bancárias
- Atualização dos dados dos clientes
- Realização de depósitos e saques
- Transferências entre contas
- Solicitação de empréstimos
- Emissão de ordens de pagamentos
- Geração de relatórios de movimentações financeiras
- Consulta de saldo e extratos bancários

## 🛠️ Tecnologias

- [Git](https://git-scm.com/) — Controle de versão do código fonte
- [Maven](https://maven.apache.org/) — Gerenciador de dependências e build
- [Java](https://www.java.com) — Linguagem de programação principal
- [Spring Boot](https://spring.io/projects/spring-boot) — Framework para desenvolvimento backend
- [Spring Security](https://spring.io/projects/spring-security) — Framework de segurança para autenticação e autorização
- [JUnit](https://junit.org/junit5/) — Framework para testes unitários em Java
- [Mockito](https://site.mockito.org/) — Framework para criação de mocks em testes Java
- [Swagger](https://swagger.io/tools/swagger-ui/) — Ferramenta para documentação interativa da API backend
- [Spotless](https://github.com/diffplug/spotless) — Plugin para formatação automática de código
- [PMD](https://pmd.github.io/) — Ferramenta para análise estática de código e detecção de bugs
- [H2 Database](https://www.h2database.com/html/main.html) — Banco de dados em memória para testes locais
- [Docker](https://www.docker.com/) — Containerização para facilitar do preview local
- [PostgreSQL](https://www.postgresql.org/) — Banco de dados relacional para produção
- [Next.js](https://nextjs.org/) — Framework React para o frontend

## 📦 Como executar o projeto(preview)

### Pré-requisitos

- [Docker](https://www.docker.com/get-started) instalado na sua máquina
- [Docker Compose](https://docs.docker.com/compose/install/) (geralmente já vem com o Docker)

### Passos para rodar

1. Clone o repositório e entre no diretório:
    ```bash
    git clone https://github.com/guilo23/Dev_Bank
    cd Dev_Bank
    ```

2. Execute o Docker Compose para subir o backend, frontend e banco de dados:
    ```bash
    docker-compose up
    ```

3. Aguarde os containers subirem. Depois, acesse no navegador:
    - Frontend: `http//localhost:3000`
    - Swagger UI (documentação da API backend): `http://localhost:8080/swagger-ui.html`

## 🧪 Testes

Este projeto inclui testes automatizados para garantir a qualidade e estabilidade do código.

### Tipos de testes incluídos

- Testes unitários com JUnit e Mockito  
- ~~Testes de integração para validar fluxos entre componentes~~  

### Como executar os testes

Para rodar todos os testes, execute o comando abaixo no diretório `backend`:

```bash
mvn test
```

## ✒️ Autores

[Guilherme Alves de Amorim](https://github.com/guilo23)

[Reinan Vieira](https://github.com/reinanmat)
