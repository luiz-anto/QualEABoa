# 📍 Qual é a Boa?

VIDEO PITCH:https://www.youtube.com/watch?v=5dJ8nsIaAOU

Plataforma de descoberta e criação de eventos locais. Os usuários podem criar eventos públicos ou privados, participar, receber ingressos e visualizar tudo em um mapa interativo.

---

## 🛠️ Tecnologias

| Camada | Tecnologia |
|---|---|
| Frontend | HTML, CSS, JavaScript (vanilla) |
| Backend | Java 17 + Spring Boot 4 |
| Banco de dados | PostgreSQL 17 |
| Mapa | MapLibre GL + OpenStreetMap / OSRM |
| Build | Maven (via Maven Wrapper) |

---

## ✅ Pré-requisitos

Antes de rodar o projeto, instale:

- **Java 17** — [adoptium.net](https://adoptium.net/temurin/releases/) (baixe o Temurin 17, versão `.msi` para Windows)
- **PostgreSQL** — [postgresql.org/download](https://www.postgresql.org/download/) (marque "PostgreSQL Server" e "Command Line Tools" na instalação)

> Não é necessário instalar o Maven separadamente. O projeto já inclui o Maven Wrapper (`mvnw.cmd`).

---

## ⚙️ Configuração do banco de dados

Após instalar o PostgreSQL, crie o banco de dados com o nome exato abaixo.

**Via pgAdmin** (interface visual):
1. Abra o pgAdmin
2. Clique com botão direito em **Databases** → **Create** → **Database**
3. Nome: `Qual_ea_Boa`
4. Clique em **Save**

**Via terminal:**
```bash
psql -U postgres
CREATE DATABASE "Qual_ea_Boa";
\q
```

---

## ▶️ Como rodar localmente

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/qual-a-boa.git
cd qual-a-boa
```

### 2. Configure as variáveis de ambiente

Abra um terminal e defina as variáveis com os dados do seu PostgreSQL:

**Windows (CMD):**
```cmd
set DB_host=localhost:5432
set DB_User=postgres
set Password=SUA_SENHA_AQUI
```

**Mac/Linux:**
```bash
export DB_host=localhost:5432
export DB_User=postgres
export Password=SUA_SENHA_AQUI
```

> ⚠️ Essas variáveis valem apenas para a sessão atual do terminal. Não feche o terminal antes de rodar o projeto.

### 3. Suba o backend

No mesmo terminal, navegue até a pasta do projeto Spring Boot e execute:

```cmd
cd semestral-main\Semestral
mvnw.cmd spring-boot:run
```

Na primeira execução, o Maven vai baixar todas as dependências automaticamente (pode levar 2–5 minutos). O Hibernate criará todas as tabelas no banco automaticamente.

Quando aparecer a mensagem abaixo, o backend está no ar:

```
Started SemestralApplication in X.XXX seconds
```

A API estará disponível em: **http://localhost:8080**

### 4. Abra o frontend

Com o backend rodando, abra o arquivo `index.html` diretamente no navegador (Chrome ou Firefox recomendados):

```
qual-a-boa/
└── index.html   ← abra este arquivo
```

> ⚠️ Não feche o terminal enquanto estiver usando o sistema. Fechar o terminal encerra o backend e o site para de funcionar.

---

## 📁 Estrutura do projeto

```
qual-a-boa/
├── index.html                          # Frontend completo (HTML + CSS + JS)
├── README.md
└── semestral-main/
    └── Semestral/
        ├── pom.xml                     # Dependências Maven
        ├── mvnw.cmd                    # Maven Wrapper (Windows)
        ├── mvnw                        # Maven Wrapper (Mac/Linux)
        └── src/main/java/br/com/fecaf/Semestral/
            ├── SemestralApplication.java
            ├── config/
            │   └── CorsConfig.java
            ├── controler/
            │   ├── EventoController.java
            │   ├── IngressoController.java
            │   ├── MensagemController.java
            │   ├── NotificacaoController.java
            │   └── UsuarioController.java
            ├── dto/
            │   ├── CancelarEventoRequest.java
            │   ├── CriarEventoRequest.java
            │   └── LoginRequest.java
            ├── model/
            │   ├── Endereco.java
            │   ├── Evento.java
            │   ├── Ingresso.java
            │   ├── Mensagem.java
            │   ├── Notificacao.java
            │   └── Usuario.java
            ├── repository/
            │   ├── EventoRepository.java
            │   ├── IngressoRepository.java
            │   ├── MensagemRepository.java
            │   ├── NotificacaoRepository.java
            │   └── UsuarioRepository.java
            └── service/
                ├── EventoService.java
                ├── IngressoService.java
                ├── MensagemService.java
                ├── NotificacaoService.java
                └── UsuarioService.java
```

---

## 🔌 Endpoints da API

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/usuario` | Cadastrar usuário |
| `POST` | `/api/usuario/login` | Login |
| `GET` | `/api/usuario/{id}` | Buscar usuário por ID |
| `GET` | `/api/eventos` | Listar eventos (filtros: `status`, `usuarioId`) |
| `POST` | `/api/eventos` | Criar evento |
| `PATCH` | `/api/eventos/{id}/cancelar` | Cancelar evento |
| `GET` | `/api/ingressos/{usuarioId}` | Listar ingressos do usuário |
| `GET` | `/api/mensagens/{usuarioId}` | Listar mensagens do usuário |
| `PATCH` | `/api/mensagens/{id}/lida` | Marcar mensagem como lida |
| `GET` | `/api/notificacoes/{usuarioId}` | Listar notificações |
| `PATCH` | `/api/notificacoes/{id}/lida` | Marcar notificação como lida |

---

## ❗ Problemas comuns

**Porta 8080 já em uso:**
```cmd
netstat -ano | findstr :8080
taskkill /PID <numero_do_pid> /F
```

**Erro de conexão com o banco:**
Verifique se o PostgreSQL está rodando e se as variáveis `DB_host`, `DB_User` e `Password` estão definidas corretamente no terminal atual.

**O site abre mas nada funciona:**
O backend não está rodando. Volte ao terminal e execute o `mvnw.cmd spring-boot:run` novamente.

---
