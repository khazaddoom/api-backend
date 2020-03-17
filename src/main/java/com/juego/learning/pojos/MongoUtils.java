package com.juego.learning.pojos;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class MongoUtils {
	
	static final Logger logger = Logger.getLogger(MongoUtils.class);
	

	public static void doMongoDbthings() {

		logger.info("********************************=> async stuff starting");

		MongoHelper mongoHelper = getDatabase("SampleDB");

		MongoCollection<PatternPOJO> collection = mongoHelper.getDatabase().getCollection("Pattern", PatternPOJO.class);

		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		int i = 0;

		String[] randomColor = { "r", "g", "b", "p" };

		while (i++ < 10) {

			int j = 0;

			ArrayList<String> a = new ArrayList<String>();

			while (j++ < 10) {

				a.add(randomColor[new Random().nextInt(4)]);
			}

			list.add(a);
		}

		for (int index = 0; index < 1000; index++) {
			insertDocumentToMongo(collection, list);
		}

		mongoHelper.getClient().close();

	}

	public static int insertDocumentToMongo(MongoCollection collection, ArrayList<ArrayList<String>> data) {

		int insertedRowCount = 0;

		try {

			collection.insertOne(new PatternPOJO("asdasd", new PatternCoreData("123123", data)));

			return 1;

		} catch (Exception e) {
			return insertedRowCount;
		}
	}

	public static MongoHelper getDatabase(String dbName) {

		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

//		MongoClient mongoClient = new MongoClient("localhost", MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).build());

		MongoCredential credentials = MongoCredential.createCredential("admin", dbName,
				new String("admin").toCharArray());

		MongoClient mongoClient = MongoClients
				.create(MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).credential(credentials)
						.applyConnectionString(new ConnectionString("mongodb://localhost:27017/")).build());

		MongoDatabase database = mongoClient.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);

		return new MongoHelper(database, mongoClient);

	}
	
	public static List<PatternPOJO> getPatternsAsPojos(MongoCollection collectionInput) {
		
//		METHOD 1
//		collection.find(exists("patternData")).;
		
//		ArrayList<PatternPOJO> result = new ArrayList<PatternPOJO>();
//		
//		Iterator<PatternPOJO> iter = collection.find().iterator();
//		
//		while (iter.hasNext()) {
//			PatternPOJO e = (PatternPOJO) iter.next();
//			result.add(e);
//		}		
//		
//		return result;
		
//		METHOD 2: Auto wrapping from mongo document to POJO		
		MongoCollection<Document> collection = collectionInput;
		Document query = new Document();
		List<PatternPOJO> products = collection.find(query, PatternPOJO.class).into(new ArrayList<>());
		
		return products;
		
	}



}
