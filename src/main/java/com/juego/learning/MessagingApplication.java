package com.juego.learning;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.juego.learning.MessagingConfig;

public class MessagingApplication extends Application<MessagingConfig>{
	
 	@Override
    public String getName() {
        return "Gampang Messaging";
    }
    @Override
    public void initialize(Bootstrap<MessagingConfig> bootstrap) {
        // Currently do nothing.
    }
    
    @Override
    public void run(MessagingConfig configuration, 
            Environment environment) throws Exception {
        
        final MessageResource resource = 
                new MessageResource(configuration.getMaxNumberOfMessages());
        
        environment.jersey().register(resource);
        
    }
    
    public static void main(String[] args) throws Exception {
        new MessagingApplication().run(args);
    }

}
