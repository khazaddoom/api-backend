package com.juego.learning;

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
    	
    	Config c = new Config("localhost", 27017);
    	
    	
    	es.submit(() -> {
            try {
                //Simulating a long running process
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ar.resume(c);
            es.shutdown();
        });
        
        
    }
    
}
