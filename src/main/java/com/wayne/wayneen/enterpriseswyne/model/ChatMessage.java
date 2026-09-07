package com.wayne.wayneen.enterpriseswyne.model;

import java.time.LocalDateTime;

public class ChatMessage {
    private long id;
    private long conversationId;
    private long senderId;
    private String body;
    private MessageStatus status;
    private LocalDateTime createdAt;

    public enum MessageStatus { SENT, DELIVERED, READ }

    public ChatMessage() {}

    public ChatMessage(long conversationId, long senderId, String body) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.body = body;
        this.status = MessageStatus.SENT;
    }

    // getters/setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getConversationId() { return conversationId; }
    public void setConversationId(long conversationId) { this.conversationId = conversationId; }

    public long getSenderId() { return senderId; }
    public void setSenderId(long senderId) { this.senderId = senderId; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public MessageStatus getStatus() { return status; }
    public void setStatus(MessageStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
