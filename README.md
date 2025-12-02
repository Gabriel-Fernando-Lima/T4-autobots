# API Autobots — Atividade 4

## Tecnologias Utilizadas

- Java 17

- Spring Boot

- Spring Web

- Spring Data JPA

- Spring Security

- JWT (Json Web Token)

- Spring HATEOAS

- MySQL

- Lombok

- Maven

---

## Como Executar o Projeto

### Pré-requisitos

- JDK 17 instalado

- Maven instalado

- MySQL instalado e rodando

## Configuração do Banco de Dados (MySQL)

Criar o banco manualmente:

```SQL
CREATE DATABASE base2;
```


## Configurar as credenciais no arquivo
src/main/resources/application.properties:

```
#configuracao JWT
jwt.secret = VKF.x8zKYvfnK%G(F[B/
jwt.expiration = 600000

#conexao
spring.datasource.url=jdbc:mysql://localhost:3306/base2
createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
#comandos
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql: true


```
Caso esteja usando outro nome de banco, usuário ou senha, ajuste na URL.


### Rodando o projeto
```bash
mvn spring-boot:run
```

### A API ficará disponível em:

http://localhost:8080

## Autenticação (Login + JWT)
### POST /login

Envia as credenciais e recebe um token JWT.

Request Body:
```json
{
  "login": "admin",
  "senha": "123456"
}
```

Response:
```json
{
  "token": "jwt-aqui"
}
```

Para acessar endpoints protegidos, envie o token:

Authorization: Bearer <seu-token>

## Papéis (Roles)

A API utiliza controle de acesso baseado em função.

Role	Permissões

- ADMIN	Acesso total

- GERENTE	Acesso total a empresas / serviços / mercadorias / veículos / usuários

- VENDEDOR	Pode visualizar usuários (cliente), veículos, serviços e mercadorias

- CLIENTE	Pode visualizar mercadorias e serviços

As permissões são controladas via @PreAuthorize.

---

# Endpoints da API

## A API contém seis módulos principais:

/empresas

/usuarios

/veiculos

/mercadorias

/servicos

/vendas

## Cada seção abaixo contém endpoints e JSONs de exemplo.

### 1. EMPRESA

POST /empresas/cadastrar

JSON de exemplo:

{
  "razaoSocial": "Car Service Toyota LTDA",
  "nomeFantasia": "Car Service Manutenção Veicular",
  "endereco": {
    "estado": "SP",
    "cidade": "São Paulo",
    "bairro": "Centro",
    "rua": "Av. São João",
    "numero": "00",
    "codigoPostal": "01035-000",
    "informacoesAdicionais": "Conjunto 12"
  },
  "telefones": [
    { "ddd": "11", "numero": "98888-7777" }
  ]
}


PUT /empresas/atualizar/1

Exemplo de JSON:

{
  "razaoSocial": "Car Service Toyota LTDA - Atualizada",
  "nomeFantasia": "Car Service Veicular Atualizado",
  "endereco": {
    "estado": "SP",
    "cidade": "São Paulo",
    "bairro": "Centro Novo",
    "rua": "Rua do Comércio",
    "numero": "15",
    "codigoPostal": "01010-123",
    "informacoesAdicionais": "Edifício Azul"
  },
  "telefones": [
    { "ddd": "11", "numero": "90000-1111" }
  ]
}


PUT /empresas/associar/1/usuarios/1


GET /empresas/1/usuarios


### 2. USUÁRIO

GET /usuarios/listar

GET /usuarios/1

POST /usuarios/cadastrar

{
  "nome": "Maria Silva",
  "nomeSocial": "Maria",
  "perfis": [
    "ROLE_CLIENTE"
  ],
  "credencial": {
    "nomeUsuario": "maria-silva",
    "senha": "senhaSegura123"
  },
  "telefones": [
    {
      "ddd": "12",
      "numero": "99888-1111"
    }
  ],
  "emails": [
    {
      "endereco": "maria@exemplo.com"
    }
  ],
  "endereco": {
    "estado": "SP",
    "cidade": "São José dos Campos",
    "bairro": "Jardim América",
    "rua": "Rua Dois",
    "numero": "45",
    "codigoPostal": "12200-010"
  },
  "documentos": [
    {
      "tipo": "CPF",
      "numero": "321.654.987-00",
      "dataEmissao": "2022-08-10T00:00:00Z"
    }
  ]
}

### 3. VEÍCULO

POST /veiculos/cadastrar

{
  "tipo": "HATCH",
  "modelo": "Toyota Yaris",
  "marca": "Toyota",
  "ano": 2022,
  "placa": "ABC-1234",
  "proprietarioId": 1
}

PUT /veiculos/atualizar/1

{
  "tipo": "SEDAN",
  "modelo": "Toyota Corolla",
  "marca": "Toyota",
  "ano": 2023,
  "placa": "XYZ-9876",
  "proprietarioId": 1
}

DELETE /veiculos/excluir/1

### 4. MERCADORIA

POST /mercadorias/cadastrar

{
  "nome": "Filtro de Óleo",
  "validade": "2030-01-01T00:00:00Z",
  "fabricacao": "2024-01-01T00:00:00Z",
  "quantidade": 50,
  "valor": 45.00,
  "descricao": "Filtro de óleo para motor 1.0"
}

### 5. SERVIÇO

POST /servicos/cadastrar

{
  "nome": "Limpeza de filtro",
  "valor": 200.00,
  "descricao": "Limpeza em filtro do ar condicionado"
}


### 6. VENDA

GET /vendas/listar

GET /vendas/1

POST /vendas/cadastrar

{
  "identificacao": "VENDA-001-TESTE",
  "clienteId": 4,
  "funcionarioId": 2,
  "veiculoId": 1,
  "mercadoriasIds": [1],
  "servicosIds": [1, 2]
}
