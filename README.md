🧪 Teste Técnico - Backend (Java 21 + Spring Boot)
==================================================

Este projeto é a parte **backend** de um teste técnico fullstack. O objetivo é construir uma API REST robusta utilizando **Java 21**, **Spring Boot**, **JPA**, **PostgreSQL**, com autenticação via **JWT**, integração com o serviço externo de CEP (ViaCEP), e suporte a usuários e múltiplos endereços.

✅ Funcionalidades Implementadas
-------------------------------

*   CRUD completo de **usuários** e **endereços**
*   Autenticação e autorização via **JWT** com **Spring Security**
*   Perfis de acesso: **Admin** e **Usuário comum**
*   Relacionamento entre usuário e múltiplos endereços
*   Integração com a API externa do **ViaCEP**
*   Paginação e ordenação nos endpoints de listagem
*   Tratamento global de exceções
*   Testes unitários e de integração com **JUnit** e **Mockito**
*   **Documentação Swagger** disponível para consulta e testes

🚀 Pré-requisitos
-----------------

Antes de rodar o projeto, você precisa ter instalado:

*   [Java 21 (JDK)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
*   [Git](https://git-scm.com)
*   [Gradle (opcional, pois usamos o Gradle Wrapper)](https://gradle.org/install/)
*   [IntelliJ IDEA](https://www.jetbrains.com/idea/) ou outra IDE com suporte a Spring Boot

📦 Instalação do Projeto
------------------------

1.  **Clone o repositório:**

    ```bash
    git clone https://github.com/Lucas-B-Teixeira/Test-Fcamara-Back-End.git
    ```

    ou

    ![image](https://github.com/user-attachments/assets/4fc7254a-04c2-4f32-99df-26c31fb98cfa)

    **e descompacte no seu computador**

2.  **Abra o terminal (cmd, PowerShell ou terminal do VS Code) dentro da pasta do projeto clonado**


3. **Execute o projeto com Gradle (Windows):**

    No terminal dentro da pasta do projeto, digite:

    ```bash
    gradlew.bat bootRun
    ```

    Ou, se estiver usando Linux ou macOS:

    ```bash
    ./gradlew bootRun
    ```

▶️ Executando o Projeto
-----------------------

Com o backend rodando, ele estará disponível em: http://localhost:8080

📄 Documentação da API - Swagger
-------------------------------

Você pode explorar todos os endpoints da API por meio da documentação interativa gerada com Swagger.

> Acesse em: http://localhost:8080/swagger-ui


🔐 Perfis de Acesso
-------------------

*   **ADMIN**: pode gerenciar todos os usuários e endereços.
*   **USUÁRIO COMUM**: só pode visualizar, editar e deletar seus próprios dados.

🧩 Integração com ViaCEP
------------------------

A API utiliza o serviço externo: https://viacep.com.br/ws/{cep}/json/

Para buscar e preencher dados de endereço a partir de um CEP válido.

🧪 Testes
---------

*   Foram implementados testes com **JUnit 5** e **Mockito**
*   Há cobertura para **serviços** e **repositórios**
*   Execute os testes com:

    No Windows:

    ```bash
    gradlew.bat test
    ```

    No Linux/macOS:

    ```bash
    ./gradlew test
    ```

  ❗ OBSERVAÇÃO IMPORTANTE
  --------------------------
Nem todos os testes automatizados foram finalizados por completo. No entanto, o projeto já possui testes unitários e de integração funcionais cobrindo os principais fluxos.

------------

*   Desenvolvedor: Lucas Barbosa
    
*   LinkedIn: https://www.linkedin.com/in/lucas-barbosa-357341191/

------------
