-- =====================================================================
-- Cria um usuário de aplicação dedicado para o Wayne Enterprises,
-- em vez de usar "root" (ver README seção 2.2).
--
-- Rode como root, UMA VEZ, depois de já ter criado o banco com schema.sql:
--   mysql -u root -p < create_app_user.sql
--
-- Troque 'TROQUE_ESTA_SENHA_FORTE' por uma senha forte antes de rodar.
-- =====================================================================

CREATE USER IF NOT EXISTS 'wayne_app'@'localhost' IDENTIFIED BY 'TROQUE_ESTA_SENHA_FORTE';

-- Privilégios mínimos necessários para a aplicação funcionar:
-- leitura/escrita nas tabelas do schema, sem poder de administração do MySQL.
GRANT SELECT, INSERT, UPDATE, DELETE ON wayne_db.* TO 'wayne_app'@'localhost';

FLUSH PRIVILEGES;
