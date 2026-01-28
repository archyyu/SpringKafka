package com.example.springkafka.common.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message {
    private String id;
    private String content;
    private LocalDateTime timestamp;
    private String sender;

    public Message() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public Message(String content, String sender) {
        this();
        this.content = content;
        this.sender = sender;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    @Override
    public String toString() {
        return "Message{id='" + id + "', content='" + content + "', timestamp=" + timestamp + ", sender='" + sender + "'}";
    }
}