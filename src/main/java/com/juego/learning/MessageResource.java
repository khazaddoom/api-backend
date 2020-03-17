package com.juego.learning;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.juego.learning.pojos.CustomResponse;
import com.juego.learning.pojos.MongoHelper;
import com.juego.learning.pojos.PatternCoreData;
import com.juego.learning.pojos.PatternPOJO;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Path("messaging")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

	static final Logger logger = Logger.getLogger(MessageResource.class);

	private final int maxNumberOfMessages;
	private final HashMap<String, List<Message>> map = new HashMap<>();
	private final AtomicLong counter;

	public MessageResource(int maxNumberOfMessages) {
		this.maxNumberOfMessages = maxNumberOfMessages;
		this.counter = new AtomicLong();
	}

	@GET
	public void retrieve(@Suspended final AsyncResponse ar) {

		ExecutorService es = Executors.newSingleThreadExecutor();

		CustomResponse response = new CustomResponse();
		response.setStatus("OK");
		response.setStatusCode("200");

		es.submit(() -> {

			Instant start = Instant.now();

			try {
				this.doMongoDbthings();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Instant end = Instant.now();
			Duration between = Duration.between(start, end);

			response.setMessage("1000 Rows inserted successfully in " + between.toMillis() + " milliseconds!");

			ar.resume(response);
			es.shutdown();
		});

	}

	private void doMongoDbthings() {

		logger.info("********************************=> async stuff starting");

		MongoHelper mongoHelper = this.getDatabase("SampleDB");

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

	private static int insertDocumentToMongo(MongoCollection collection, ArrayList<ArrayList<String>> data) {

		int insertedRowCount = 0;

		try {

			collection.insertOne(new PatternPOJO("asdasd", new PatternCoreData("123123", data)));

			return 1;

		} catch (Exception e) {
			return insertedRowCount;
		}
	}

	private MongoHelper getDatabase(String dbName) {

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

	@GET
	@Path("/clean")
	public void drop(@Suspended final AsyncResponse ar) {

		ExecutorService es = Executors.newSingleThreadExecutor();

		CustomResponse response = new CustomResponse();
		response.setStatus("OK");
		response.setStatusCode("200");

		es.submit(() -> {

			Instant start = Instant.now();
			
			MongoHelper mongoHelper = null;

			try {
				
				mongoHelper = this.getDatabase("SampleDB");
				
				MongoCollection<PatternPOJO> collection = mongoHelper.getDatabase().getCollection("Pattern", PatternPOJO.class);
				
				collection.drop();				
				
			} catch (Exception e) {
				logger.error(e);
			} finally {
				if (mongoHelper != null) {
					mongoHelper.getClient().close();
				}				
			}

			Instant end = Instant.now();
			Duration between = Duration.between(start, end);

			response.setMessage("Dropped All Patterns in " + between.toMillis() + " milliseconds!");

			ar.resume(response);
			es.shutdown();
		});
	}

}
