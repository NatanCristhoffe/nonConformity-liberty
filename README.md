Blessed
Enterprise Non-Conformity Management System (SaaS – Multi-Tenant)
Blessed é uma API REST SaaS desenvolvida com Java + Spring Boot, projetada para gestão corporativa de não conformidades com arquitetura multi-tenant isolada por empresa, autenticação segura via JWT e infraestrutura hospedada na AWS.

O sistema foi projetado com foco em:

Segurança

Escalabilidade

Isolamento de dados

Governança corporativa

Auditoria e rastreabilidade

🏢 Arquitetura Geral

Blessed opera como um sistema multi-tenant SaaS, onde múltiplas empresas utilizam a mesma aplicação com isolamento completo de dados.

🔐 Estratégia de Multi-Tenancy

O isolamento é feito via:

companyId embutido no JWT

Validação contextual no Service Layer

Regras explícitas de autorização por role

Cada requisição autenticada:

Extrai o token JWT

Recupera:

userId

companyId

role

Valida:

Se o usuário pertence à empresa correta

Se possui permissão para executar a ação

Isso impede qualquer acesso cross-tenant.

🔒 Segurança

Spring Security

JWT Stateless Authentication

Expiração de Token com retorno automático de 401

Autorização baseada em Roles (USER / ADMIN)

Validações de acesso no Service Layer

Controle de permissões por empresa

Exception Handler Global

Soft Delete para integridade histórica

Quando o token expira:

A API retorna HTTP 401

O front-end pode invalidar sessão automaticamente

☁️ Infraestrutura Cloud (AWS)

O sistema foi projetado para ambiente produtivo em nuvem.

🖥 Aplicação

Hospedada em instância Amazon EC2

🗄 Banco de Dados

Amazon RDS

MySQL

Banco isolado por ambiente

📦 Armazenamento de Arquivos

Amazon S3

Armazenamento centralizado para:

Evidências de abertura de não conformidades

Evidências de ações corretivas

Documentações anexadas

(Futuro) Fotos de perfil de usuários

Arquitetura preparada para expansão de storage sem impacto estrutural.

📦 Tecnologias Utilizadas

Java 17

Spring Boot

Spring Security

JWT

JPA / Hibernate

MySQL

Lombok

Maven

AWS EC2

AWS RDS

AWS S3

📌 Módulos do Sistema
👥 Gestão de Empresas
POST /company
Permite criação de nova empresa dentro do ambiente multi-tenant.

👤 Gestão de Usuários

Criar usuário

Atualizar dados

Atualizar role (PATCH)

Desativar usuário (soft disable)

Validação de tenant antes de qualquer operação

Regras:

Apenas administradores podem gerenciar usuários

Administrador só pode gerenciar usuários da própria empresa

📋 Gestão de Não Conformidades

O núcleo do sistema.

➕ Criar Não Conformidade
POST /non-conformity

Consome:

multipart/form-data
Estrutura da requisição:

data → JSON com informações estruturadas

file → Arquivo de evidência (imagem ou documento)

Exemplo de uso:

Incidente estrutural

Falha de processo

Evento de segurança

Desvio operacional

🔍 Consultas Disponíveis

GET todas (por usuário autenticado)

GET por ID

GET por título (autocomplete)

GET por status

GET por status com includeAll (ADMIN only)

Paginação via:

page

size (default configurado)

Usuários comuns:

Visualizam apenas suas não conformidades

Administradores:

Visualizam todas da empresa

🔁 Atualizações

PUT → Atualização completa

PATCH → Atualizações parciais

Cancelamento ao invés de DELETE

Nenhuma não conformidade é removida permanentemente, garantindo rastreabilidade histórica.

🧩 Fluxo de Ações Corretivas

Cada não conformidade pode conter:

Causa Raiz

Plano de Ação

Execução de Ação

Evidência da Ação (upload S3)

Análise de Eficácia

Aprovação Administrativa

Isso cria um ciclo completo de gestão corretiva.

📊 Auditoria e Logs

O sistema mantém registro de:

Atualizações relevantes

Mudanças de status

Ações executadas

Aprovações

Correções

Projetado para atender ambientes corporativos que exigem rastreabilidade.

🧠 Decisões Técnicas

Multi-tenancy implementado via contexto JWT

Regras críticas protegidas no Service Layer

Soft deletion para preservar histórico

Upload desacoplado via S3

Arquitetura organizada em Controller / Service / Repository

DTOs para isolamento da camada de domínio

▶️ Como Executar Localmente
git clone https://github.com/seu-usuario/blessed
cd blessed
mvn spring-boot:run

Configurar application.yml com:

MySQL

Secret JWT

Configurações AWS (S3)

🚀 Roadmap Futuro

Refresh Token

Dockerização completa

Testes automatizados (JUnit + Mockito)

Observabilidade (CloudWatch)

Rate Limiting

API Documentation com Swagger/OpenAPI

Sistema de permissões granular

💼 Diferenciais do Projeto

✔ Arquitetura SaaS real
✔ Multi-tenancy com isolamento forte
✔ Segurança JWT robusta
✔ Infraestrutura AWS profissional
✔ Upload seguro em S3
✔ Controle de acesso por empresa e role
✔ Ciclo completo de gestão de não conformidade
