# Sistema de Gerenciamento de Produtos 📦

Este projeto é uma API RESTful desenvolvida em Java com Spring Boot, projetada para gerenciar um catálogo de produtos. 
A aplicação realiza a leitura e carga inicial de dados a partir de arquivos JSON de forma paralela, armazena-os em um banco de dados e expõe endpoints para consulta paginada e inserção de novos produtos.

## ✅ Principais Funcionalidades:

- **Carga de Dados Automática:** Lê e processa múltiplos arquivos JSON com dados de produtos na inicialização da aplicação, utilizando paralelismo para otimizar o desempenho.

- **API REST:** Expõe endpoints para consultar produtos por nome ou faixa de preço e para inserir novos produtos manualmente.

- **Validação de Duplicidade:** Garante que não existam produtos com a mesma combinação de product e type.

- **Consulta Paginada:** A consulta de produtos suporta paginação para lidar com grandes volumes de dados de forma eficiente.

- **Documentação da API:** Integração com Springdoc para gerar uma documentação interativa com Swagger UI.

- **Logs Detalhados:** Gera logs para eventos importantes, como início/fim da importação de dados e erros de validação.

- **Cobertura de Testes:** Inclui testes unitários e de integração para garantir a qualidade e o funcionamento dos endpoints.

## 🛠️ Tecnologias Utilizadas

    Linguagem: Java 11

    Framework: Spring Boot 2.7.18

    Persistência: Spring Data JPA / Hibernate

    Banco de Dados: MySQL

    Build Tool: Apache Maven

    Testes: JUnit 5, Mockito, REST Assured

    Documentação: Springdoc (Swagger UI)

    Outros: Lombok, Spring Web, Spring Validation

## 🚀 Como Executar o Projeto

**Pré-requisitos**
Antes de começar, certifique-se de que você tem os seguintes softwares instalados em sua máquina:

    Java 11 (JDK), Apache Maven, Git, Docker

**Passo a passo para executar a aplicação:**
1. Clone o Repositório.

2. No diretório raiz do projeto, para subir o banco de dados MySql, execute o comando:  
   ``` docker-compose up --build -d ```

3. Depois execute o comando do maven para gerar um executável:
   ``` mvn clean package ```

4. Agora execute o sistema:
   ``` java -jar target/produtos-0.1.0.jar ```

5. Para acessar o contrato do serviço acessar:
    ``` http://localhost:8080/swagger-ui/index.html   ``` 

