# Blessed NonConformity

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Security-JWT-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL-orange)
![AWS](https://img.shields.io/badge/Cloud-AWS-yellow)
![Architecture](https://img.shields.io/badge/Architecture-Multi--Tenant-purple)
![Status](https://img.shields.io/badge/Status-Production%20Ready-success)

> Enterprise Non-Conformity Management System (SaaS – Multi-Tenant)

Blessed é uma API REST SaaS desenvolvida com **Java + Spring Boot**, projetada para gestão corporativa de não conformidades com arquitetura **multi-tenant isolada por empresa**, autenticação segura via JWT e infraestrutura hospedada na AWS.

---

## 🚀 Visão Geral

O sistema permite que múltiplas empresas utilizem a mesma aplicação com isolamento completo de dados.

### Objetivos principais

- 🔐 Segurança robusta
- 🏢 Isolamento por empresa (Multi-Tenancy)
- ☁️ Infraestrutura Cloud na AWS
- 📊 Auditoria e rastreabilidade
- 📁 Upload seguro de evidências

---

## 🏗 Arquitetura Multi-Tenant

O isolamento é realizado utilizando o `companyId` embutido no JWT.

### 🔑 Fluxo de Autenticação

1. Usuário realiza login via `/auth/login`
2. Se credenciais válidas:
   - Geração de JWT contendo:
     - `userId`
     - `companyId`
     - `role`
3. Toda requisição autenticada valida:
   - Se o usuário pertence à empresa correta
   - Se possui permissão adequada

Isso impede qualquer acesso cross-tenant.

---

## 🔒 Segurança

- Spring Security
- JWT Stateless Authentication
- Expiração automática com retorno HTTP 401
- Autorização baseada em Roles (USER / ADMIN)
- Validações de autorização no Service Layer
- Exception Handler Global
- Soft Delete para preservação histórica

Quando o token expira:
- A API retorna HTTP 401
- O front-end pode invalidar a sessão automaticamente

---

## ☁️ Infraestrutura AWS

A aplicação foi projetada para ambiente produtivo em nuvem.

### 🖥 Aplicação
- Amazon EC2

### 🗄 Banco de Dados
- Amazon RDS
- MySQL

### 📦 Armazenamento de Arquivos
- Amazon S3

Utilizado para:

- Evidências de abertura de não conformidades
- Evidências de ações corretivas
- Documentações anexadas
- (Futuro) Foto de perfil de usuários

Arquitetura preparada para expansão de storage sem impacto estrutural.

---

## 🧰 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security
- JWT
- JPA / Hibernate
- MySQL
- Lombok
- Maven
- AWS EC2
- AWS RDS
- AWS S3

---

## 📌 Principais Funcionalidades

### 👥 Gestão de Empresas

```http
POST /company
```

Permite criação de novas empresas no ambiente multi-tenant.

---

### 👤 Gestão de Usuários

- Criar usuário
- Atualizar dados
- Atualizar role (PATCH)
- Desativar usuário (soft disable)
- Validação de tenant antes de qualquer operação

Regras:

- Apenas administradores podem gerenciar usuários
- Administradores só podem gerenciar usuários da própria empresa

---

### 📋 Gestão de Não Conformidades

#### ➕ Criar Não Conformidade

```http
POST /non-conformity
Content-Type: multipart/form-data
```

Permite envio de:

- `data` → JSON estruturado
- `file` → Arquivo de evidência (imagem, PDF, etc.)

Exemplo de uso:

- Incidente estrutural
- Falha de processo
- Evento de segurança
- Desvio operacional

---

### 🔍 Consultas Disponíveis

- GET todas (por usuário autenticado)
- GET por ID
- GET por título (autocomplete)
- GET por status
- Paginação via `page` e `size`

Usuários comuns:
- Visualizam apenas suas não conformidades

Administradores:
- Visualizam todas da empresa

---

### 🔁 Atualizações

- PUT → Atualização completa
- PATCH → Atualização parcial
- Cancelamento ao invés de DELETE

Nenhuma não conformidade é removida permanentemente, garantindo rastreabilidade histórica.

---

## 🧩 Fluxo de Ações Corretivas

Cada não conformidade pode conter:

- Causa Raiz
- Plano de Ação
- Execução da Ação
- Evidência da Ação (upload S3)
- Análise de Eficácia
- Aprovação Administrativa

Isso cria um ciclo completo de gestão corretiva.

---

## 📊 Auditoria e Logs

O sistema mantém registro de:

- Mudanças de status
- Execução de ações
- Aprovações
- Correções
- Atualizações críticas

Projetado para ambientes corporativos que exigem rastreabilidade.

---

## ▶️ Como Executar Localmente

```bash
git clone https://github.com/seu-usuario/blessed
cd blessed
mvn spring-boot:run
```

Configurar `application.yml` com:

- Banco MySQL
- Secret JWT
- Credenciais AWS (S3)

---

## 🧠 Decisões Técnicas

- Multi-tenancy baseado em contexto JWT
- Regras críticas protegidas no Service Layer
- Upload desacoplado via S3
- DTOs para isolamento da camada de domínio
- Arquitetura organizada em Controller / Service / Repository

---

## 💼 Diferenciais do Projeto

✔ Arquitetura SaaS real  
✔ Multi-tenancy robusto  
✔ Segurança JWT  
✔ Infraestrutura AWS  
✔ Upload seguro via S3  
✔ Controle de acesso por empresa e role  

---

## 👨‍💻 Autor

Desenvolvido por **Natan Carvalho**
