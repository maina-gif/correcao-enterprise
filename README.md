# Wayne Enterprises — Sistema de Gestão Corporativa

Sistema desktop em **Java + JavaFX**, com persistência em **MySQL/MariaDB**, desenvolvido como projeto acadêmico/pessoal de aprendizado.

> Este README documenta o estado atual do projeto, o que já foi corrigido, e a jornada de depuração — deixado de propósito bem detalhado, porque boa parte do aprendizado aqui foi justamente resolver os problemas, não só o código final.

---

## Stack

- **Java 23** (com `--enable-preview`), **JavaFX** (UI via FXML + Controllers)
- **Maven** (build)
- **MySQL 8 / MariaDB** (compatível com os dois — testado em ambiente com MariaDB 10.4)
- **jBCrypt** (hash de senha)
- **iText 5**, **Apache POI**, **Jakarta Mail** (PDF, Excel, e-mail)

---

## Como rodar o projeto do zero

### 1. Pré-requisitos
- JDK 23+
- MySQL Server ou MariaDB rodando localmente
- MySQL Workbench (recomendado, evita precisar mexer no PATH do terminal)

### 2. Criar o banco de dados
Os scripts estão em `database/`. Abra cada um no MySQL Workbench (**File → Open SQL Script**) e execute na ordem:

1. `database/schema.sql` — cria o banco `wayne_db` e as 26 tabelas. **Atenção:** esse script é destrutivo por padrão (tem `DROP TABLE IF EXISTS` antes de cada tabela), então toda vez que rodar, ele reseta os dados. Já cria um usuário `admin` de teste com senha `maimai1234` (já em hash bcrypt).
2. `database/create_app_user.sql` — cria o usuário do MySQL `wayne_app` (edite a senha no arquivo antes de rodar; use a mesma senha que você vai colocar em `db.properties` depois).
3. `database/seed_data.sql` (opcional) — popula com dados de teste fictícios.

### 3. Configurar a conexão da aplicação
Copie `db.properties.example` (na raiz) para `src/main/resources/db.properties` e preencha com a senha real do `wayne_app`:
```properties
db.url=jdbc:mysql://localhost:3306/wayne_db
db.user=wayne_app
db.password=SUA_SENHA_AQUI
```
Esse arquivo **não é versionado** (está no `.gitignore`) — cada pessoa que clonar o repositório precisa criar o seu.

### 4. Rodar
No IntelliJ, roda a classe `Main` (ou `mvn javafx:run`). Login padrão: `admin` / `maimai1234`.

---

## O que já foi corrigido (partindo de um projeto legado sem documentação)

O projeto começou sem nenhum script de banco versionado, sem README, e com vários problemas de segurança e bugs. Nesta fase de correção, resolvemos:

### Segurança
- **Senha de usuário em texto puro** → agora usa hash bcrypt (`model/PasswordUtil.java`), login compara hash, nunca texto puro no SQL.
- **Credenciais de banco hardcoded** (`root`, sem senha, direto no código) → movidas para `db.properties` (fora do versionamento) + usuário dedicado `wayne_app` com privilégios mínimos.
- **Command injection no backup/restauração** — o código antigo montava um comando de shell (`cmd.exe /c` + concatenação de string) para rodar `mysqldump`/`mysql`; reescrito com `ProcessBuilder` (sem shell) e senha passada via variável de ambiente, nunca na linha de comando.

### Bugs de build/conexão
- `pom.xml` apontava `mainClass` para uma classe (`MainApp`) que **não existia** — corrigido para a classe real (`Main`).
- Conexão com banco usava uma `Connection` estática única compartilhada por todo o app, mas era fechada a cada chamada de DAO (por causa do `try-with-resources`) — causava erros intermitentes de "Connection is closed" sob concorrência. Reescrito para abrir uma conexão nova por chamada.
- `module-info.java` (o projeto usa Java Modules/JPMS) não declarava a nova dependência `jbcrypt` — toda lib nova precisa entrar tanto no `pom.xml` quanto no `module-info.java`, lição aprendida na prática.
- `DatePicker` do JavaFX lançava exceção não tratada ao salvar um formulário (bug conhecido do componente: ele tenta interpretar o texto digitado com o formato "cru" do sistema operacional) — corrigido no cadastro de funcionário com um conversor de data tolerante a erro; **o mesmo padrão existe em outras ~16 telas do sistema e ainda precisa ser replicado** (ver Pendências).

### Banco de dados
- Banco não tinha nenhum schema versionado — reconstruído por engenharia reversa lendo os `DAO`s (`database/schema.sql`, 26 tabelas).
- Documentação e diagrama ER do banco em `database/DATABASE.md`, incluindo pontos de modelagem que merecem atenção (colunas que deveriam ser chave estrangeira e são texto livre, tabelas de log duplicadas, etc.)

---

## A jornada de depuração (pra quem for aprender com isso)

Vale registrar as dificuldades reais que apareceram, porque foram tão instrutivas quanto o código em si:

1. **Trocar credenciais sem reconstruir o projeto** — o Maven copia `db.properties` para `target/classes` na hora de compilar; editar o arquivo fonte sem dar um `clean` antes causava a aplicação continuar usando a versão antiga da senha, gerando erros de autenticação confusos que pareciam ser "senha errada" mas na verdade eram "senha desatualizada em cache".
2. **PowerShell vs cmd** — comandos com redirecionamento (`mysql -u root -p < arquivo.sql`) não funcionam direto no PowerShell (que é o terminal padrão do IntelliJ no Windows); é preciso `Get-Content arquivo.sql | mysql ...` ou chamar `cmd /c "..."`.
3. **Cliente `mysql` fora do PATH** — em mais de uma máquina, o executável `mysql.exe` não estava no PATH do Windows (mesmo com o MySQL Workbench funcionando, que tem cliente embutido próprio). Foi preciso localizar manualmente o executável (`Get-ChildItem "C:\Program Files\MySQL" -Recurse -Filter mysql.exe`) e adicionar ao PATH da sessão do terminal.
4. **Usuário do MySQL "sumido"** — em duas ocasiões diferentes (inclusive ao trocar de computador), o `ALTER USER` falhava com "Operation ALTER USER failed" porque o usuário `wayne_app` simplesmente não existia ainda nesse servidor — o `create_app_user.sql` não tinha sido executado. O diagnóstico definitivo foi `SELECT User, Host FROM mysql.user WHERE User = 'wayne_app';` — se vier vazio, o usuário não existe, ponto.
5. **Tabela com schema antigo "grudada"** — como o `schema.sql` original usava `CREATE TABLE IF NOT EXISTS`, uma tabela `cargos` de uma versão anterior do projeto (com colunas completamente diferentes) nunca foi substituída, e todo `INSERT` batia num erro de "coluna desconhecida". A correção foi tornar o script destrutivo (`DROP TABLE IF EXISTS` antes de cada `CREATE TABLE`), documentado claramente como destrutivo no topo do arquivo.
6. **Hash sobrescrito sem querer** — depois de gerar e aplicar o hash da senha do admin manualmente via `UPDATE`, rodar o `schema.sql` de novo (pra corrigir outra tabela) recriava a tabela `usuarios` do zero e reinseria a senha em texto puro do seed original, desfazendo o hash sem nenhum aviso. Corrigido embutindo o hash bcrypt já pronto direto no script de seed, para que ele nasça correto mesmo depois de rodado do zero.
7. **Troca de computador no meio do processo** — ao mudar de máquina, todo o ambiente precisou ser refeito (banco, usuário, PATH), já que nada disso é versionado nem portátil por natureza — reforçou a importância de ter os scripts (`database/`) documentados e reproduzíveis em vez de depender de configuração manual "de cabeça".

---

## Pendências (próximos passos)

1. Replicar a correção do `DatePicker` (feita em `CadastroController`) nas ~16 outras telas que usam esse componente.
2. Consolidar duplicações de código: `ExportadorExcel`/`ExcelExportUtil`, `ExportadorPDF`/`PdfExportUtil`/`PDFGenerator`, `FuncionarioDAO`/`FuncionarioDAOMethods`, `LogDAO`/`LogAuditoriaDAO`, `Evento`/`EventoCalendario`/`EventoCorporativo` (esse último com um DAO — `EventoCalendarioDAO` — que hoje espera colunas inexistentes na tabela `eventos`, precisa ser corrigido ou removido).
3. Introduzir uma camada de **Service** entre Controller e DAO (hoje a regra de negócio mora no Controller, junto com código de UI), começando pelo módulo de Funcionário como piloto.
4. Corrigir organização de pacotes: várias classes fisicamente na pasta `model/` declaram o pacote raiz em vez de `.model`.
5. Trocar `e.printStackTrace()` espalhado (100+ ocorrências) por um framework de log de verdade (SLF4J/Logback).
6. Escrever os primeiros testes automatizados — hoje o projeto tem zero cobertura, o que torna qualquer refatoração arriscada.
7. Revisão de modelagem do banco: colunas que deveriam ser chave estrangeira e hoje são texto livre (`funcionarios.cargo`, `equipamentos.funcionario_responsavel`), tabelas de log duplicadas, `chamados.data_abertura` como `VARCHAR` em vez de data (detalhado em `database/DATABASE.md`).

---

## Arquivos-chave

| Arquivo | O que faz |
|---|---|
| `Main.java` | Classe de entrada real da aplicação |
| `model/ConnectionFactory.java` | Conexão com o banco (lê `db.properties`) |
| `model/PasswordUtil.java` | Hash e verificação de senha (bcrypt) |
| `model/BackupRestauracao.java` | Backup/restauração do banco (seguro, sem shell) |
| `controller/LoginController.java` | Autenticação |
| `controller/CadastroController.java` | Cadastro de funcionário (já com correção do DatePicker) |
| `database/schema.sql` | Cria o banco e as 26 tabelas do zero |
| `database/create_app_user.sql` | Cria o usuário de aplicação do MySQL |
| `database/seed_data.sql` | Dados de teste |
| `database/DATABASE.md` | Diagrama ER e análise do schema |
| `GerarHashTemp.java` | Utilitário de desenvolvimento pra gerar hash bcrypt de uma senha |

---

*Projeto de estudo — sinta-se à vontade para abrir issues ou sugerir melhorias.*
