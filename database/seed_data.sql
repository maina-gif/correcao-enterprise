-- =====================================================================
-- Dados de teste (seed) — Wayne Enterprises
-- Rode depois de schema.sql. Só dados fictícios, seguro apagar/re-rodar.
--   mysql -u root -p wayne_db < seed_data.sql
-- =====================================================================

USE wayne_db;

-- Cargos
INSERT INTO cargos (nome, salario_base, nivel) VALUES
    ('Analista de TI', 5500.00, 'Pleno'),
    ('Analista Financeiro', 4800.00, 'Júnior'),
    ('Gerente de RH', 9000.00, 'Sênior'),
    ('Desenvolvedor de Software', 7200.00, 'Pleno');

-- Funcionários
INSERT INTO funcionarios (nome_completo, cpf, cargo, departamento, email, data_admissao, data_nascimento) VALUES
    ('Bruce Wayne', '111.111.111-11', 'Gerente de RH', 'Recursos Humanos', 'bruce.wayne@wayne.local', '2018-03-01', '1985-02-19'),
    ('Lucius Fox', '222.222.222-22', 'Analista de TI', 'Tecnologia', 'lucius.fox@wayne.local', '2019-07-15', '1978-11-02'),
    ('Selina Kyle', '333.333.333-33', 'Analista Financeiro', 'Financeiro', 'selina.kyle@wayne.local', '2021-01-10', '1990-06-23'),
    ('Alfred Pennyworth', '444.444.444-44', 'Desenvolvedor de Software', 'Tecnologia', 'alfred.p@wayne.local', '2020-09-05', '1970-04-14');

-- Férias
INSERT INTO ferias (funcionario_id, data_inicio, data_fim, observacao) VALUES
    (1, '2026-01-05', '2026-01-20', 'Férias de verão'),
    (2, '2026-03-10', '2026-03-25', NULL);

-- Avaliações de desempenho
INSERT INTO avaliacoes (funcionario_id, data_avaliacao, pontualidade, produtividade, trabalho_equipe, observacoes) VALUES
    (1, '2025-12-15', 9, 8, 9, 'Excelente liderança no último trimestre.'),
    (2, '2025-12-15', 7, 9, 8, 'Muito produtivo, pode melhorar pontualidade.');

-- Benefícios
INSERT INTO beneficios (tipo, valor, status) VALUES
    ('Vale Refeição', 600.00, 'Ativo'),
    ('Plano de Saúde', 350.00, 'Ativo');

-- Equipamentos
INSERT INTO equipamentos (tipo, numero_serie, funcionario_responsavel, status, data_aquisicao) VALUES
    ('Notebook', 'SN-0001', 'Lucius Fox', 'Em uso', '2023-05-10'),
    ('Monitor', 'SN-0002', 'Selina Kyle', 'Em uso', '2023-05-10');

-- Avisos
INSERT INTO avisos (titulo, descricao, data, tipo) VALUES
    ('Manutenção programada', 'Sistema ficará fora do ar das 22h às 23h.', CURRENT_DATE, 'Aviso Geral'),
    ('Bem-vindos novos colaboradores', 'Seja bem-vindo à Wayne Enterprises!', CURRENT_DATE, 'RH');

-- Chamados
INSERT INTO chamados (titulo, descricao, status, prioridade, funcionario, data_abertura) VALUES
    ('Impressora não funciona', 'Impressora do 3º andar sem tinta.', 'ABERTO', 'Baixa', 'Selina Kyle', '01/09/2026'),
    ('Acesso ao sistema negado', 'Não consigo logar desde ontem.', 'EM ANDAMENTO', 'Alta', 'Alfred Pennyworth', '02/09/2026');

-- Treinamentos + participações
INSERT INTO treinamentos (titulo, tipo, local, data) VALUES
    ('Segurança da Informação', 'Obrigatório', 'Auditório', '2026-02-10'),
    ('Liderança Ágil', 'Opcional', 'Sala de Treinamento', '2026-04-05');

INSERT INTO participacoes_treinamento (id_funcionario, id_treinamento, data_participacao) VALUES
    (1, 1, '2026-02-10'),
    (2, 1, '2026-02-10'),
    (1, 2, '2026-04-05');

-- Eventos
INSERT INTO eventos (titulo, descricao, data, local, tipo) VALUES
    ('Confraternização de fim de ano', 'Festa de encerramento anual.', '2026-12-18', 'Salão Principal', 'Social');

INSERT INTO eventos_corporativos (titulo, descricao, data_evento, tipo_evento, local, responsavel) VALUES
    ('Reunião de Diretoria', 'Alinhamento estratégico trimestral.', '2026-10-01', 'Institucional', 'Sala de Reuniões A', 'Bruce Wayne');

-- Notificações
INSERT INTO notificacoes (mensagem, data_hora, lida) VALUES
    ('Novo chamado aberto: Impressora não funciona', NOW(), 0),
    ('Férias de Bruce Wayne aprovadas', NOW(), 1);
