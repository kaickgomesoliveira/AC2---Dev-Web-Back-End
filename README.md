# Sistema de Controle de Projetos

Aplicação desenvolvida com Spring Boot para cadastrar setores, funcionários e projetos. A tela web permite cadastrar projetos e consultar os que já foram registrados.

## Tecnologias

- Java 17 e Spring Boot
- Spring Web e Spring Data JPA
- Banco de dados H2
- HTML, CSS e JavaScript

## Como executar

Na pasta do projeto, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Depois, acesse a tela em [http://localhost:8080](http://localhost:8080). A aplicação usa um banco H2 em memória.

## Prints da aplicação

### Banco de dados

![Estrutura das tabelas no H2](fotos/Captura%20de%20tela%202026-10-21%20195327.png)

O console do H2 mostra as tabelas `SETORES`, `FUNCIONARIOS` e `PROJETOS`. A tabela `PROJETO_FUNCIONARIO` registra os vínculos entre projetos e funcionários.

### Cadastro e consulta de setor

![Cadastro de setor pela API](fotos/Captura%20de%20tela%202026-10-21%20195714.png)

Requisição `POST /api/setores` com o nome do setor. A resposta `201` indica que o cadastro foi realizado.

![Consulta SQL dos setores no H2](fotos/Captura%20de%20tela%202026-10-21%20195840.png)

Consulta `SELECT * FROM SETORES` no H2, exibindo o setor cadastrado e seu ID.

![Consulta do setor e seus funcionários - print 1](fotos/Captura%20de%20tela%202026-10-21%20200045.png)

Requisição `GET /api/setores/1`. A API retorna os dados do setor e a lista de funcionários vinculados.

![Consulta do setor e seus funcionários - print 2](fotos/Captura%20de%20tela%202026-10-21%20200050.png)

Outro print da consulta `GET /api/setores/1`, mostrando a resposta JSON com o setor e seus funcionários.

### Cadastro e consulta de funcionário

![Cadastro de funcionário pela API](fotos/Captura%20de%20tela%202026-10-21%20195936.png)

Requisição `POST /api/funcionarios` com o nome do funcionário e o `setorId`. A resposta `201` confirma o cadastro.

### Cadastro e listagem de projetos

![Cadastro de projeto pela API](fotos/Captura%20de%20tela%202026-10-21%20200148.png)

Requisição `POST /api/projetos` com descrição, data de início e data de término. A resposta `201` confirma a criação do projeto.

![Listagem de projetos pela API](fotos/Captura%20de%20tela%202026-10-21%20200240.png)

Requisição `GET /api/projetos`. A resposta lista os projetos cadastrados, suas datas e os funcionários vinculados.

![Tela web de projetos](fotos/interface.png)

Tela do frontend para cadastrar um projeto e listar os projetos existentes. O print mostra uma mensagem de sucesso após o cadastro e o projeto exibido na tabela.
