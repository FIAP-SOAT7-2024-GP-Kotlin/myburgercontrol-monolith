# myburgercontrol-monolith

Este projeto é um monolito para a FIAP PosTech SOAT 2024.

## Componentes

Esta aplicação tem basicamente 2 componentes:

- Banco de dados PostgresSQL versão 16
- Aplicação `myburgercontrol-monolith`

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

## Acessando a documentação Swagger

Após subir a aplicação usando `docker compose` podemos acessar a UI do Swagger disponível.
Acesse:
> http://localhost:8080/swagger-ui.html

## Verificando a cobertura de Testes

Com o comando:
> gradle jacocoTestReport

Podemos ver o relatorio de cobertura de teste da aplicação. Algo mais ou menos assim:

```
Test Coverage:
- Class Coverage: 75%
- Method Coverage: 81.1%
- Branch Coverage: 35.7%
- Line Coverage: 92%
- Instruction Coverage: 76.6%
- Complexity Coverage: 68.2%
```

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
