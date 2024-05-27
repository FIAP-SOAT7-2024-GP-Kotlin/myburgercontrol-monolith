# myburgercontrol-monolith

Este projeto é um monolito para a FIAP PosTech SOAT 2024.

## Documentação

Acesse o MIRO com o DDD da aplicação por [aqui](https://miro.com/app/board/uXjVKWJ-8T0=/?share_link_id=109211348615)

## Componentes

Esta aplicação tem basicamente 2 componentes:

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

## Subindo ambiente

O ambiente utiliza do docker compose para subir os componentes da aplicação.
Toda a configuração está no docker-compose.yml nas sessões de `envirorment`.

Usando o comando:
> docker compose up

A aplicação e o banco de dados são executados.

## Executando o projeto

Após subir a aplicação usando `docker compose` podemos acessar a UI do Swagger disponível.
Acesse:
> [http://localhost:8080/swagger-ui.html](http://localhost:8080/api/v1/swagger-ui/index.html#/)

## Liquibase

O Liquibase é um componente de gestão de versão de alteração de banco de dados.
Neste projeto estamos usando o padrão XML do Liquibase que se encontra na pasta `src/main/resources/db/changelog`.
Todos os XMLs que estão no diretório `src/main/resources/db/changelog/changes` serão executados.

### Gerando um XML do Liquibase novo

Para gerar um XML novo é recomendado subir o banco e a aplicação ao menos uma vez **ANTES** de implementar novas
entidades.
Depois disso, com o apenas o banco de dados em execução, basta rodar o comando:

> gradle diffChangelog

Esse comando vai gerar no caminho `build/tmp/` um arquivo `diff-changelog.xml`, contendo as *DIFERENÇAS* entre o banco
de
dados em execução e a implementação do JPA que consta no código fonte.

**Nota:** É fundamental olhar o arquivo gerado e validar se o mesmo está de acordo. Essa geração de arquivo do liquibase
costuma não gerar nome de PKs, por exemplo.

## Wiremock

Para acessar a interface gráfica do wiremock execute o docker-compose e acesse:
>http://localhost:9090/__admin/webapp/mappings


## Contatos
| Nome | RM |
|------|----|
| André Luis dos Santos | RM355299 |
| Fernando Florencio | RM355268 |
| Luiz Fernando Calazans | RM354794 |
| José Carlos dos Santos Rocha| RM355769 |
| Rafael José Peres Correia | RM355308 |
