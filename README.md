## Para Executar a Aplicação
1. Certifique-se de ter Docker e Docker Compose instalados, Maven e Java11.

2. No diretório raiz do projeto, para subir o banco de dados MySql, execute o comando:  
   ``` docker-compose up --build -d ```

3. Depois execute o comando do maven para gerar um executável:
   ``` mvn clean package ```

4. Agora execute o sistema:
   ``` java -jar target/produtos-0.1.0.jar ```

5. Para acessar o contrato do serviço acessar:
    ``` http://localhost:8080/swagger-ui   ``` 

