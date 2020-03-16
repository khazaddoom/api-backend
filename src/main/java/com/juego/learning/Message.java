package com.juego.learning;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
private long id;
    
    private String recipient;
    private String sender;
    private String body;
    private long sent;
	public Message() {
	    }
    
    public Message(long id, String recipient, String sender, String body, long sent) {
        this.id = id;
        this.recipient = recipient;
        this.sender = sender;
        this.body = body;
        this.sent = sent;
    }
    @JsonProperty
    public long getId() {
        return id;
    }
    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }
    @JsonProperty
    public String getRecipient() {
        return recipient;
    }
    @JsonProperty
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    @JsonProperty
    public String getSender() {
        return sender;
    }
    @JsonProperty
    public void setSender(String sender) {
        this.sender = sender;
    }
    @JsonProperty
    public String getBody() {
        return body;
    }
    @JsonProperty
    public void setBody(String body) {
        this.body = body;
    }
    @JsonProperty
    public long getSent() {
        return sent;
    }
    @JsonProperty
    public void setSent(long sent) {
        this.sent = sent;
    }

}
