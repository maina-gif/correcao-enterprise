-- =====================================================================
-- Wayne Enterprises — Schema do banco (wayne_db)
-- Reconstruído por engenharia reversa do código-fonte (DAOs), já que o
-- projeto original não tinha nenhum script de banco versionado.
--
-- ⚠️ ATENÇÃO — VERSÃO DESTRUTIVA:
-- Este script agora tem DROP TABLE IF EXISTS antes de cada CREATE TABLE.
-- Rodá-lo APAGA e recria todas as 26 tabelas do zero, mesmo que já
-- existam (com dados diferentes, de outra versão, etc.). Isso evita o
-- problema de "tabela antiga com colunas diferentes ficar presa" — mas
-- também significa que TODOS os dados dessas tabelas são perdidos a
-- cada execução. Faça backup antes se já tiver dado real salvo.
--
-- Como usar:
--   mysql -u root -p < schema.sql
-- (ou copie e cole no MySQL Workbench / DBeaver)
--
-- Convenções adotadas:
--   - Toda tabela tem PK auto-incremento `id` (BIGINT ou INT conforme uso)
--   - FKs explícitas onde o código já usa um *_id referenciando outra tabela
--   - VARCHAR com tamanho generoso onde o código não dá pista de limite
--   - Datas: DATE quando só dia importa, DATETIME/TIMESTAMP quando tem hora
--   - utf8mb4 em tudo (evita problema de acento/emoji, comum em pt-BR)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS wayne_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE wayne_db;

SET FOREIGN_KEY_CHECKS = 0;

-- ---------------------------------------------------------------------
-- USUÁRIOS DO SISTEMA (login da aplicação)
-- Usada por: LoginController, UsuariosDAOJdbc
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario        VARCHAR(60)  NOT NULL,               -- login (username)
    senha          VARCHAR(255) NOT NULL,                -- ⚠️ guardar HASH (bcrypt), nunca texto puro
    nome_completo  VARCHAR(150) NULL,
    email          VARCHAR(150) NULL,
    online         TINYINT(1)   NOT NULL DEFAULT 0,
    last_seen      DATETIME     NULL,
    criado_em      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_usuarios_usuario (usuario),
    UNIQUE KEY uk_usuarios_email (email)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- FUNCIONÁRIOS
-- Usada por: FuncionarioDAO / FuncionarioDAOMethods (duplicadas, ver README)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS funcionarios;
CREATE TABLE funcionarios (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo      VARCHAR(150) NOT NULL,
    cpf                VARCHAR(14)  NOT NULL,
    cargo              VARCHAR(100) NULL,
    departamento       VARCHAR(100) NULL,
    email              VARCHAR(150) NULL,
    data_admissao      DATE NULL,
    data_nascimento    DATE NULL,
    caminho_curriculo  VARCHAR(255) NULL,
    caminho_contrato   VARCHAR(255) NULL,
    caminho_foto       VARCHAR(255) NULL,
    UNIQUE KEY uk_funcionarios_cpf (cpf),
    KEY idx_funcionarios_departamento (departamento),
    KEY idx_funcionarios_nome (nome_completo)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- FÉRIAS
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS ferias;
CREATE TABLE ferias (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    funcionario_id  INT NOT NULL,
    data_inicio     DATE NOT NULL,
    data_fim        DATE NOT NULL,
    observacao      VARCHAR(500) NULL,
    CONSTRAINT fk_ferias_funcionario FOREIGN KEY (funcionario_id)
        REFERENCES funcionarios(id) ON DELETE CASCADE,
    KEY idx_ferias_funcionario (funcionario_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- DOCUMENTOS (contratos, atestados, etc. por funcionário)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS documentos;
CREATE TABLE documentos (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    funcionario_id    INT NOT NULL,
    titulo            VARCHAR(150) NOT NULL,
    tipo              VARCHAR(60)  NULL,
    data_validade     DATE NULL,
    caminho_arquivo   VARCHAR(255) NULL,
    CONSTRAINT fk_documentos_funcionario FOREIGN KEY (funcionario_id)
        REFERENCES funcionarios(id) ON DELETE CASCADE,
    KEY idx_documentos_funcionario (funcionario_id),
    KEY idx_documentos_validade (data_validade)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- CARGOS / PLANO DE CARGOS E SALÁRIOS
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS cargos;
CREATE TABLE cargos (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    salario_base  DECIMAL(10,2) NOT NULL DEFAULT 0,
    nivel         VARCHAR(50) NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- BENEFÍCIOS
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS beneficios;
CREATE TABLE beneficios (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    tipo    VARCHAR(100) NOT NULL,
    valor   DECIMAL(10,2) NOT NULL DEFAULT 0,
    status  VARCHAR(30) NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- EQUIPAMENTOS (patrimônio/TI)
-- OBS: "funcionario_responsavel" é gravado como texto (nome), não FK —
-- assim está no código hoje. Idealmente seria funcionario_id (INT, FK).
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS equipamentos;
CREATE TABLE equipamentos (
    id                        INT AUTO_INCREMENT PRIMARY KEY,
    tipo                      VARCHAR(100) NOT NULL,
    numero_serie              VARCHAR(100) NOT NULL,
    funcionario_responsavel   VARCHAR(150) NULL,
    status                    VARCHAR(30) NULL,
    data_aquisicao            DATE NULL,
    UNIQUE KEY uk_equipamentos_numero_serie (numero_serie)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- AVISOS (mural/comunicados)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS avisos;
CREATE TABLE avisos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    titulo      VARCHAR(150) NOT NULL,
    descricao   TEXT NULL,
    data        DATE NOT NULL,
    tipo        VARCHAR(50) NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- CHAMADOS (suporte/TI)
-- OBS: "data_abertura" é lido/gravado como String no código atual
-- (Chamado.getDataAbertura() é String). Mantido VARCHAR para não quebrar
-- o código hoje — mas o ideal é migrar para DATETIME (ver README).
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS chamados;
CREATE TABLE chamados (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    titulo         VARCHAR(150) NOT NULL,
    descricao      TEXT NULL,
    status         VARCHAR(30) NOT NULL DEFAULT 'ABERTO',
    prioridade     VARCHAR(20) NULL,
    funcionario    VARCHAR(150) NULL,
    data_abertura  VARCHAR(30) NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- AVALIAÇÕES DE DESEMPENHO
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS avaliacoes;
CREATE TABLE avaliacoes (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    funcionario_id    INT NOT NULL,
    data_avaliacao    DATE NOT NULL,
    pontualidade      INT NOT NULL DEFAULT 0,
    produtividade     INT NOT NULL DEFAULT 0,
    trabalho_equipe   INT NOT NULL DEFAULT 0,
    observacoes       TEXT NULL,
    CONSTRAINT fk_avaliacoes_funcionario FOREIGN KEY (funcionario_id)
        REFERENCES funcionarios(id) ON DELETE CASCADE,
    KEY idx_avaliacoes_funcionario (funcionario_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- NOTIFICAÇÕES (sino de notificação da UI)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS notificacoes;
CREATE TABLE notificacoes (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensagem   VARCHAR(500) NOT NULL,
    data_hora  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lida       TINYINT(1) NOT NULL DEFAULT 0,
    KEY idx_notificacoes_lida (lida)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS notificacao_tipo;
CREATE TABLE notificacao_tipo (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo   VARCHAR(50) NOT NULL,
    nome     VARCHAR(100) NOT NULL,
    cor_hex  VARCHAR(7) NULL,
    ordem    INT NULL,
    ativo    TINYINT(1) NOT NULL DEFAULT 1,
    UNIQUE KEY uk_notificacao_tipo_codigo (codigo)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- LOGS — duas tabelas paralelas existem hoje no código (ver README 4.2:
-- provavelmente deveriam virar uma só). Mantidas separadas aqui só para
-- não quebrar nada, mas avalie migrar log_acoes -> log_auditoria depois.
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS log_acoes;
CREATE TABLE log_acoes (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario  VARCHAR(150) NULL,
    acao     VARCHAR(500) NOT NULL,
    momento  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_log_acoes_momento (momento)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS log_auditoria;
CREATE TABLE log_auditoria (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    usuario    VARCHAR(150) NULL,
    acao       VARCHAR(500) NOT NULL,
    modulo     VARCHAR(100) NULL,
    data_hora  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_log_auditoria_data (data_hora)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- RECRUTAMENTO: currículos, processos seletivos, candidatos
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS curriculos;
CREATE TABLE curriculos (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    nome              VARCHAR(150) NOT NULL,
    email             VARCHAR(150) NULL,
    telefone          VARCHAR(30) NULL,
    cargo_desejado    VARCHAR(100) NULL,
    skills            TEXT NULL,
    experiencia       TEXT NULL,
    escolaridade      VARCHAR(100) NULL,
    linkedin          VARCHAR(255) NULL,
    status_processo   VARCHAR(50) NULL,
    caminho_pdf       VARCHAR(255) NULL,
    data_cadastro     DATE NOT NULL DEFAULT (CURRENT_DATE)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS processos_seletivos;
CREATE TABLE processos_seletivos (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    titulo       VARCHAR(150) NOT NULL,
    descricao    TEXT NULL,
    data_inicio  DATE NULL,
    data_fim     DATE NULL
) ENGINE=InnoDB;

DROP TABLE IF EXISTS candidatos;
CREATE TABLE candidatos (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    nome               VARCHAR(150) NOT NULL,
    email              VARCHAR(150) NULL,
    cargo_pretendido   VARCHAR(100) NULL,
    link_curriculo     VARCHAR(255) NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TREINAMENTOS
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS treinamentos;
CREATE TABLE treinamentos (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    titulo  VARCHAR(150) NOT NULL,
    tipo    VARCHAR(50) NULL,
    local   VARCHAR(150) NULL,
    data    DATE NOT NULL
) ENGINE=InnoDB;

DROP TABLE IF EXISTS participacoes_treinamento;
CREATE TABLE participacoes_treinamento (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    id_funcionario      INT NOT NULL,
    id_treinamento      INT NOT NULL,
    data_participacao   DATE NOT NULL,
    CONSTRAINT fk_participacao_funcionario FOREIGN KEY (id_funcionario)
        REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_participacao_treinamento FOREIGN KEY (id_treinamento)
        REFERENCES treinamentos(id) ON DELETE CASCADE,
    KEY idx_participacao_funcionario (id_funcionario),
    KEY idx_participacao_treinamento (id_treinamento)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- EVENTOS (agenda genérica) — usada por EventoDAO
-- ⚠️ EventoCalendarioDAO também aponta para "eventos", mas espera colunas
-- diferentes (data_evento, origem) que NÃO existem aqui. Esse DAO está
-- quebrado/desalinhado — ver README seção 4. Não crie essas colunas só
-- para "consertar por fora"; o certo é corrigir o DAO (ver seção sobre
-- consolidação de Evento/EventoCalendario/EventoCorporativo).
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS eventos;
CREATE TABLE eventos (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    titulo     VARCHAR(150) NOT NULL,
    descricao  TEXT NULL,
    data       DATE NOT NULL,
    local      VARCHAR(150) NULL,
    tipo       VARCHAR(50) NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- EVENTOS CORPORATIVOS (agenda corporativa) — usada por
-- AgendaCorporativaDAO e EventoCorporativoDAO (mesma tabela, duas DAOs)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS eventos_corporativos;
CREATE TABLE eventos_corporativos (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    titulo        VARCHAR(150) NOT NULL,
    descricao     TEXT NULL,
    data_evento   DATE NOT NULL,
    tipo_evento   VARCHAR(50) NULL,
    local         VARCHAR(150) NULL,
    responsavel   VARCHAR(150) NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- INFORMAÇÕES DA EMPRESA (registro único, id sempre = 1)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS empresa_info;
CREATE TABLE empresa_info (
    id                 INT PRIMARY KEY DEFAULT 1,
    nome               VARCHAR(150) NULL,
    cnpj               VARCHAR(20) NULL,
    descricao_html     MEDIUMTEXT NULL,
    missao             TEXT NULL,
    visao              TEXT NULL,
    valores            TEXT NULL,
    endereco           VARCHAR(255) NULL,
    telefone           VARCHAR(30) NULL,
    email              VARCHAR(150) NULL,
    site               VARCHAR(150) NULL,
    redes              VARCHAR(255) NULL,
    logo_path          VARCHAR(255) NULL,
    data_atualizacao   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_empresa_info_singleton CHECK (id = 1)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- CHAT INTERNO
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS chat_conversation;
CREATE TABLE chat_conversation (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

DROP TABLE IF EXISTS chat_participant;
CREATE TABLE chat_participant (
    conversation_id  BIGINT NOT NULL,
    user_id          BIGINT NOT NULL,
    PRIMARY KEY (conversation_id, user_id),
    CONSTRAINT fk_participant_conversation FOREIGN KEY (conversation_id)
        REFERENCES chat_conversation(id) ON DELETE CASCADE,
    CONSTRAINT fk_participant_usuario FOREIGN KEY (user_id)
        REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS chat_message;
CREATE TABLE chat_message (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id  BIGINT NOT NULL,
    sender_id        BIGINT NOT NULL,
    body             TEXT NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'SENT',   -- SENT | DELIVERED | READ (enum no Java)
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_id)
        REFERENCES chat_conversation(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_sender FOREIGN KEY (sender_id)
        REFERENCES usuarios(id) ON DELETE CASCADE,
    KEY idx_message_conversation (conversation_id)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS chat_typing;
CREATE TABLE chat_typing (
    conversation_id  BIGINT NOT NULL,
    user_id          BIGINT NOT NULL,
    typing           TINYINT(1) NOT NULL DEFAULT 0,
    updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (conversation_id, user_id),
    CONSTRAINT fk_typing_conversation FOREIGN KEY (conversation_id)
        REFERENCES chat_conversation(id) ON DELETE CASCADE,
    CONSTRAINT fk_typing_usuario FOREIGN KEY (user_id)
        REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------
-- DADOS INICIAIS (seed) — ajuste antes de rodar em produção!
-- Cria 1 usuário admin de teste. A senha abaixo já é um HASH bcrypt
-- (formato $2a$) da senha "maimai1234" — ou seja, mesmo rodando este
-- script do zero várias vezes (o schema é destrutivo, ver aviso no
-- topo do arquivo), o admin sempre nasce já com hash, nunca em texto
-- puro. Para logar, use o usuário "admin" com a senha "maimai1234".
--
-- Se quiser trocar essa senha padrão: gere um novo hash com
-- GerarHashTemp.java (troque o texto lá) e substitua o valor abaixo.
-- ---------------------------------------------------------------------
INSERT INTO usuarios (usuario, senha, nome_completo, email)
VALUES ('admin', '$2a$10$AG6uk81jootCVFjBfRQGb.bFR3hIrWvfinDJIuyFpBoaPWx0Etr/C', 'Administrador', 'admin@wayne.local')
ON DUPLICATE KEY UPDATE usuario = usuario;

INSERT INTO empresa_info (id, nome, descricao_html)
VALUES (1, 'Wayne Enterprises', '<h2>Sobre a Empresa</h2><p>Texto institucional.</p>')
ON DUPLICATE KEY UPDATE id = id;
