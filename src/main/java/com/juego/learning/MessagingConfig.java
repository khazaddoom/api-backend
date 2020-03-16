package com.juego.learning;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class MessagingConfig extends Configuration {

	@NotNull
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
