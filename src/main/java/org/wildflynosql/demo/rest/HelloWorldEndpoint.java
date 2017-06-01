package org.wildflynosql.demo.rest;

import java.util.HashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.mongodb.client.MongoDatabase;

@ApplicationScoped
@Path("/hello")
public class HelloWorldEndpoint {

    @Inject @Named("mongodbtestprofile")
    MongoDatabase database;

    @GET
    @Produces("text/plain")
    public Response doGet() {
        return Response.ok("Hello from WildFly Swarm! database = " + database).build();
    }
}
