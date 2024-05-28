# My Burguer Control - 7SOAT

___

## `myburgercontrol-monolith`

Este projeto é um monolito para o curso de Software Architeture da turma 7SOAT na instituição FIAP no ano de 2024.
___

## DDD

Acesse [aqui](https://miro.com/app/board/uXjVKWJ-8T0=/?share_link_id=109211348615) o miro com:

- levantamento de requisitos
- dicionário de linguagem ubíqua
- event storming

## Componentes

Esta aplicação tem basicamente 3 componentes:

- Banco de dados PostgresSQL versão 16
- Aplicação `myburgercontrol-monolith`
- Wiremock

## Frameworks

Este projeto utiliza as seguintes tecnologias e linguagens:

- Kotlin JVM
- JVM OpenJDK Temurin 21 LTS
- Springboot 3
- JaCoCo
- Liquibase

## Inicialização da aplicação

O ambiente utiliza Docker Compose para iniciar os componentes da aplicação. Todas as configurações das variáveis de
ambiente estão especificadas na seção `environment` do arquivo `docker-compose.yml`.

### Configuração de ambiente

Após clonar o repositório, execute o seguinte comando :

```sh
docker compose up
```

### Acessando a aplicação

Neste momento a interface do Swagger estará disponível no endereço abaixo:

> [http://localhost:8080/swagger-ui.html](http://localhost:8080/api/v1/swagger-ui/index.html#/)

### Jornadas

Para facilitar o uso da aplicação, utilizamos as jornadas com o conjunto de endereços restritos as funcionalidades
desejada na ordem de execução.

#### Jornada de autenticação

- **[POST]** | */users* | Utilize esta rota para criar um novo usuário.
- **[GET]** | */users* Utilize esta rota para encontrar um usuário utilizando o cpf.
- **[POST]** | */auth* | Utilize esta rota para autenticar um usuário já criado.
- **[POST]** | */users/{id}* | Utilize esta rota para encontrar um usuário utilizando o identificador na base de dados.

#### Jornada de Cliente

- **[POST]** | */customers* | Utilize esta rota para criar um novo cliente.
- **[GET]** | */customers* | Utilize esta rota para encontrar um cliente pelo CPF.

#### Jornada de Pedido

- **[GET]** | */products* | Utilize esta rota para buscar todos os produtos cadastrados.
- **[GET]** | */products/type* | Utilize esta rota para buscar todos os produtos cadastrados por categoria.
- **[POST]** | */orders* | Utilize esta rota para criar um pedido.
- **[GET]** | */orders/queue* | Utilize esta rota para encontrar a lista do(s) novo(s) pedido(s).
- **[GET]** | */orders* | Utilize esta rota para encontrar o(s) pedido(s) por cpf de cliente.
- **[POST]** | */orders/received* | Utilize esta rota para alterar o estado pedido para RECEBIDO.
- **[POST]** | */orders/in-progress* | Utilize esta rota para alterar o estado pedido para EM PREPARAÇÃO.
- **[POST]** | */orders/ready* | Utilize esta rota para alterar o estado pedido para PRONTO.
- **[POST]** | */orders/finished* | Utilize esta rota para alterar o estado pedido para FINALIZADO.

#### Adminstrativo

- **[POST]** | */products* | Utilize esta rota para cadastrar um novo produto.
- **[GET]** | */products/{id}* | Utilize esta rota para encontrar um produto utilizando o identificador na base de
  dados.
- **[GET]** | */customer/{id}* | Utilize esta rota para encontrar um cliente utilizando o identificador na base de
  dados.

### Liquibase

O Liquibase é um componente de gerenciamento de versões de alterações de banco de dados. Neste projeto, estamos
utilizando o padrão XML do Liquibase, localizado na pasta `src/main/resources/db/changelog`. Todos os arquivos XML no
diretório `src/main/resources/db/changelog/changes` serão executados.

#### Gerando um XML do Liquibase novo

Para gerar um XML novo é recomendado subir o banco e a aplicação ao menos uma vez **ANTES** de implementar novas
entidades.
Depois disso, com o apenas o banco de dados em execução, basta rodar o comando:

```sh
gradle diffChangelog
```

Esse comando vai gerar no caminho `build/tmp/` um arquivo `diff-changelog.xml`, contendo as *DIFERENÇAS* entre o banco
de
dados em execução e a implementação do JPA que consta no código fonte.

> **Nota:** É fundamental olhar o arquivo gerado e validar se o mesmo está de acordo. Essa geração de arquivo do
> liquibase
> costuma não gerar nome de PKs, por exemplo.

### Wiremock

Para acessar a interface gráfica do wiremock basta acessar o endereço abaixo:
> http://localhost:9090/__admin/webapp/mappings

## Contatos

### Grupo composto pelos os alunos

| Nome                         | RM       |
|------------------------------|----------|
| André Luis dos Santos        | RM355299 |
| Fernando Florencio           | RM355268 |
| Luiz Fernando Calazans       | RM354794 |
| José Carlos dos Santos Rocha | RM355769 |
| Rafael José Peres Correia    | RM355308 |
