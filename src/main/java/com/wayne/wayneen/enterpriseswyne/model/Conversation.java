package com.wayne.wayneen.enterpriseswyne.model;

import java.time.LocalDateTime;
import java.util.List;

public class Conversation {
    private long id;
    private List<Long> participantIds;
    private LocalDateTime createdAt;

    public Conversation() {}

    public Conversation(long id) { this.id = id; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public List<Long> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<Long> participantIds) { this.participantIds = participantIds; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
