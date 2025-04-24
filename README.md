# 🛡️ Motoclub Auth Service

Este microserviço gerencia a autenticação de usuários do sistema Motoclub, fornecendo endpoints públicos para login, registro de novos usuários e validação de tokens JWT.

## 📦 Stack Tecnológica

- **Java**: 21
- **Spring Boot**: Framework principal
- **Spring Security**: Gerenciamento de autenticação e autorização
- **JWT**: JSON Web Tokens para autenticação
- **H2 Database**: Banco de dados em memória para testes (intenção futura: MySQL ou OracleDB na AWS RDS)
- **Swagger/OpenAPI**: Documentação da API
- **Maven**: Gerenciamento de dependências

## 🚀 Endpoints Disponíveis

### 🔐 Autenticação

| Método | Endpoint           | Descrição                              |
|--------|--------------------|----------------------------------------|
| POST   | `/auth/register`   | Registra um novo usuário               |
| POST   | `/auth/login`      | Autentica um usuário e gera um token JWT |
| GET    | `/auth/validate`   | Valida um token JWT                    |

### 📄 Exemplos de Payloads

#### `POST /auth/register`

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "123456"
}
```

#### `POST /auth/login`

```json
{
  "email": "john@example.com",
  "password": "123456"
}
```

#### `GET /auth/validate`

**Header**:
```
Authorization: Bearer <seu_token_jwt>
```

**Resposta**:
```json
{
  "username": "john@example.com",
  "roles": []
}
```

## 🔑 Configuração do JWT

O token JWT inclui o e-mail do usuário e seus papéis (roles). A validade e a chave secreta são configuráveis no arquivo `application.yml`. A secret key deve ser definida via variável de ambiente para segurança, mas está exposta (comentada) no `application.yml` apenas para facilitar testes:

```yaml
jwt:
  secret: ${JWT_SECRET}
  # secret: eyJzZWNyZXRrZXkiOiAiVmVyeVNlY3VyZVN0cmluZ1dpdGhSZXF1aXJlZEJ5SFMzNTYifQ==
  expiration: 86400000 # 24 horas
```

## 🧪 Usuário de Teste

Um usuário administrador é criado automaticamente ao iniciar a aplicação:

- **Email**: `admin@motoclub.com`
- **Senha**: `123456`

## 🧬 Documentação da API

A documentação da API está disponível via Swagger. [Clique aqui](http://localhost:8081/swagger-ui/index.html) para acessar.

## 🧪 Teste Rápido com Postman

Importe a coleção `motoclub-service-collection.json`, localizada na raiz do projeto `motoclub-service`, para testar os endpoints facilmente.

## 📂 Executando o Projeto

### Pré-requisitos

- **Java**: 21+
- **Maven**

### Passos

```bash
git clone https://github.com/seu-usuario/motoclub-auth-service.git
cd motoclub-auth-service
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 👨‍💻 Autor

**Anderson Passos**  
[LinkedIn](https://www.linkedin.com/in/anderson-passos-dev)