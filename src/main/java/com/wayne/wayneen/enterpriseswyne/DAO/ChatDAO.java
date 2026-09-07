package com.wayne.wayneen.enterpriseswyne.DAO;

import com.wayne.wayneen.enterpriseswyne.model.ChatMessage;
import com.wayne.wayneen.enterpriseswyne.model.ChatMessage.MessageStatus;
import com.wayne.wayneen.enterpriseswyne.model.ConnectionFactory;
import com.wayne.wayneen.enterpriseswyne.model.Conversation;


import java.sql.*;
import java.time.ZoneId;
import java.util.*;

public class ChatDAO {

    public long getOrCreateDirectConversation(long userA, long userB) throws SQLException {
        try (Connection c = ConnectionFactory.getConexao()) {
            // Tenta achar uma conversa existente com exatamente esses 2 participantes
            String sql = """
                SELECT cc.id
                FROM chat_conversation cc
                JOIN chat_participant p1 ON p1.conversation_id = cc.id AND p1.user_id = ?
                JOIN chat_participant p2 ON p2.conversation_id = cc.id AND p2.user_id = ?
                GROUP BY cc.id
                HAVING COUNT(*) = 2
            """;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setLong(1, userA);
                ps.setLong(2, userB);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getLong(1);
                }
            }

            // Cria nova conversa
            long convId;
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO chat_conversation() VALUES()", Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    convId = rs.getLong(1);
                }
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO chat_participant(conversation_id, user_id) VALUES(?,?),(?,?)")) {
                ps.setLong(1, convId);
                ps.setLong(2, userA);
                ps.setLong(3, convId);
                ps.setLong(4, userB);
                ps.executeUpdate();
            }
            return convId;
        }
    }

    public List<Conversation> listConversationsForUser(long userId) throws SQLException {
        List<Conversation> out = new ArrayList<>();
        String sql = """
            SELECT cc.id, cc.created_at
            FROM chat_conversation cc
            JOIN chat_participant p ON p.conversation_id = cc.id
            WHERE p.user_id = ?
            ORDER BY (SELECT MAX(cm.created_at) FROM chat_message cm WHERE cm.conversation_id = cc.id) DESC NULLS LAST
        """;
        try (Connection c = ConnectionFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Conversation conv = new Conversation();
                    conv.setId(rs.getLong("id"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        conv.setCreatedAt(ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    }
                    out.add(conv);
                }
            }
        }
        return out;
    }

    public List<ChatMessage> listMessages(long conversationId, int limit) throws SQLException {
        List<ChatMessage> out = new ArrayList<>();
        String sql = """
            SELECT id, conversation_id, sender_id, body, status, created_at
            FROM chat_message
            WHERE conversation_id = ?
            ORDER BY created_at DESC
            LIMIT ?
        """;
        try (Connection c = ConnectionFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, conversationId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChatMessage m = new ChatMessage();
                    m.setId(rs.getLong("id"));
                    m.setConversationId(rs.getLong("conversation_id"));
                    m.setSenderId(rs.getLong("sender_id"));
                    m.setBody(rs.getString("body"));
                    m.setStatus(MessageStatus.valueOf(rs.getString("status")));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        m.setCreatedAt(ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    }
                    out.add(m);
                }
            }
        }
        Collections.reverse(out); // ordem cronológica
        return out;
    }

    public ChatMessage sendMessage(ChatMessage msg) throws SQLException {
        String sql = """
            INSERT INTO chat_message(conversation_id, sender_id, body, status)
            VALUES(?,?,?,?)
        """;
        try (Connection c = ConnectionFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, msg.getConversationId());
            ps.setLong(2, msg.getSenderId());
            ps.setString(3, msg.getBody());
            ps.setString(4, msg.getStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) msg.setId(rs.getLong(1));
            }
        }
        return msg;
    }

    public void markDelivered(long conversationId, long currentUserId) throws SQLException {
        // Marca como ENTREGUE todas mensagens da conversa que NÃO são do usuário atual e ainda não estão entregues/lidas
        String sql = """
            UPDATE chat_message
            SET status = 'DELIVERED'
            WHERE conversation_id = ?
              AND sender_id <> ?
              AND status = 'SENT'
        """;
        try (Connection c = ConnectionFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, conversationId);
            ps.setLong(2, currentUserId);
            ps.executeUpdate();
        }
    }

    public void markRead(long conversationId, long currentUserId) throws SQLException {
        String sql = """
            UPDATE chat_message
            SET status = 'READ'
            WHERE conversation_id = ?
              AND sender_id <> ?
              AND status <> 'READ'
        """;
        try (Connection c = ConnectionFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, conversationId);
            ps.setLong(2, currentUserId);
            ps.executeUpdate();
        }
    }

    public void setTyping(long conversationId, long userId, boolean typing) throws SQLException {
        String upsert = """
            INSERT INTO chat_typing(conversation_id, user_id, typing)
            VALUES(?,?,?)
            ON DUPLICATE KEY UPDATE typing = VALUES(typing), updated_at = CURRENT_TIMESTAMP
        """;
        try (Connection c = ConnectionFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(upsert)) {
            ps.setLong(1, conversationId);
            ps.setLong(2, userId);
            ps.setBoolean(3, typing);
            ps.executeUpdate();
        }
    }

    public boolean someoneTyping(long conversationId, long exceptUserId) throws SQLException {
        String sql = """
            SELECT 1
            FROM chat_typing
            WHERE conversation_id = ? AND user_id <> ? AND typing = 1
              AND updated_at >= (CURRENT_TIMESTAMP - INTERVAL 8 SECOND)
            LIMIT 1
        """;
        try (Connection c = ConnectionFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, conversationId);
            ps.setLong(2, exceptUserId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
