package com.juego.learning;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

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

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.juego.learning.pojos.CustomResponse;
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
    	response.setMessage("1 Row inserted successfully");
    	
    	es.submit(() -> {
            try {
                this.doMongoDbthings();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ar.resume(response);
            es.shutdown();
        });
        
        
    }
    
    private void doMongoDbthings() {
    	
    	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		
//		MongoClient mongoClient = new MongoClient("localhost", MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).build());
		
		
		MongoCredential credentials = MongoCredential.createCredential(
                "admin",
                "SampleDB",
                new String("admin").toCharArray());
		
		MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .codecRegistry(pojoCodecRegistry)
                        .credential(credentials)
                        .applyConnectionString(new ConnectionString("mongodb://localhost:27017/"))
                        .build()
        );
		
		MongoDatabase database = mongoClient.getDatabase("SampleDB").withCodecRegistry(pojoCodecRegistry);
		
		
		MongoCollection<PatternPOJO> collection = database.getCollection("Pattern", PatternPOJO.class);
		
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		int i = 0;
		
		String[] randomColor = {"r", "g", "b", "p"};	
		
		
		while(i++ < 10) {
			
			int j = 0;
			
			ArrayList<String> a = new ArrayList<String>();
			
			while(j++ < 10) {
				
				a.add(randomColor[new Random().nextInt(4)]);
			}
				
			list.add(a);
		}
		
		
		insertDocumentToMongo(collection, list);
		
		
		mongoClient.close();
    	
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
    
}
