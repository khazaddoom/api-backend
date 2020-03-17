package com.juego.learning.pojos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoHelper {
	
	MongoDatabase database;
	MongoClient client;
	
	public MongoHelper(MongoDatabase db, MongoClient cl) {
		this.database = db;
		this.client = cl;
	}

	public MongoDatabase getDatabase() {
		return database;
	}

	public void setDatabase(MongoDatabase database) {
		this.database = database;
	}

	public MongoClient getClient() {
		return client;
	}

	public void setClient(MongoClient client) {
		this.client = client;
	}

}
