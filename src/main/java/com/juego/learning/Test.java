package com.juego.learning;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class Test extends Configuration{
	
	@NotEmpty
    private int maxNumberOfMessages = 10;
    @JsonProperty
    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }
    @JsonProperty
    public void setMaxNumberOfMessages(int maxNumberOfMessages) {
        this.maxNumberOfMessages = maxNumberOfMessages;
    }
}
