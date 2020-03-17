package com.juego.learning;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.bson.Document;

import com.juego.learning.pojos.CustomResponse;
import com.juego.learning.pojos.MongoHelper;
import com.juego.learning.pojos.MongoUtils;
import com.juego.learning.pojos.PatternCoreData;
import com.juego.learning.pojos.PatternPOJO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Path("messaging")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

	static final Logger logger = Logger.getLogger(MessageResource.class);
	static final MongoHelper mongoHelper = MongoUtils.getDatabase("SampleDB");

	private final int maxNumberOfMessages;
	private final HashMap<String, List<Message>> map = new HashMap<>();
	private final AtomicLong counter;

	public MessageResource(int maxNumberOfMessages) {
		this.maxNumberOfMessages = maxNumberOfMessages;
		this.counter = new AtomicLong();
	}

	@GET
	@Path("/create")
	public void retrieve(@Suspended final AsyncResponse ar) {

		ExecutorService es = Executors.newSingleThreadExecutor();

		CustomResponse response = new CustomResponse();
		response.setStatus("OK");
		response.setStatusCode("200");

		es.submit(() -> {

			Instant start = Instant.now();

			try {
				MongoUtils.doMongoDbthings();
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
	@GET
	@Path("/clean")
	public void drop(@Suspended final AsyncResponse ar) {

		ExecutorService es = Executors.newSingleThreadExecutor();

		CustomResponse response = new CustomResponse();
		response.setStatus("OK");
		response.setStatusCode("200");

		es.submit(() -> {

			Instant start = Instant.now();

			try {
				
				MongoCollection<PatternPOJO> collection = mongoHelper.getDatabase().getCollection("Pattern", PatternPOJO.class);
				
				collection.drop();				
				
			} catch (Exception e) {
				logger.error(e);
			}

			Instant end = Instant.now();
			Duration between = Duration.between(start, end);

			response.setMessage("Dropped All Patterns in " + between.toMillis() + " milliseconds!");

			ar.resume(response);
			es.shutdown();
		});
	}
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public void getPatterns(@Suspended final AsyncResponse ar) {
		
		
		ExecutorService es = Executors.newSingleThreadExecutor();

		CustomResponse response = new CustomResponse();
		response.setStatus("OK");
		response.setStatusCode("200");	
		

		es.submit(() -> {
			
			MongoCollection<PatternPOJO> collection = null;

			Instant start = Instant.now();

			try {
				
				collection = mongoHelper.getDatabase().getCollection("Pattern", PatternPOJO.class);
				List<PatternPOJO> result = MongoUtils.getPatternsAsPojos(collection);
				
				Instant end = Instant.now();
				Duration between = Duration.between(start, end);

				response.setMessage("Found " + result.size() + " Patterns in " + between.toMillis() + " milliseconds!");
				
				response.setData(result);
				
				ar.resume(response);
				es.shutdown();
				
			} catch (Exception e) {
				logger.error(e);
			}

			
			
//			ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>();
//			
//			ArrayList<String> b = new ArrayList<String>();
//			b.add("b");
//			b.add("b");
//			b.add("b");
//			b.add("b");
//			b.add("b");
//			b.add("b");
//			
//			a.add(b);		
//			
//			
//			PatternPOJO p = new PatternPOJO("Hello", new PatternCoreData("0", a));
//			
//			PatternPOJO[] list = new PatternPOJO[1000];
//			
//			Arrays.fill(list, p);
			
			
		});
		
		
		
	}
	
}