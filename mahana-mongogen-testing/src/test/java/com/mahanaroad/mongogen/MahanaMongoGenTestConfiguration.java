package com.mahanaroad.mongogen;


import com.mahanaroad.mongogen.persist.MongoClientFacade;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;


@Configuration
@ComponentScan(basePackages = "com.mahanaroad.mongogen")
public class MahanaMongoGenTestConfiguration {


    @Bean
    public MongoClientURI mongoClientURI() {

        return new MongoClientURI("mongodb://localhost:27017");

    }


    @Bean
    public String defaultDatabaseName() {

        return "mahana-mongogen-testing";

    }


    @Bean
    public MongoClient mongoClient() throws UnknownHostException {

        return new MongoClient(mongoClientURI());

    }


    @Bean
    public MongoClientFacade mongoClientFacade() throws UnknownHostException {

        return new MongoClientFacade(mongoClient(), defaultDatabaseName());

    }


}
