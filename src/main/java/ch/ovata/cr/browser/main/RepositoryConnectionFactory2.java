/*
 * $Id: RepositoryConnectionFactory.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 05.10.2019, 12:00:00
 * 
 * Copyright (c) 2019 by Ovata GmbH,
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Ovata GmbH ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Ovata GmbH.
 */
package ch.ovata.cr.browser.main;

import ch.ovata.cr.api.RepositoryConnection;
import ch.ovata.cr.impl.RepositoryConnectionImpl;
import ch.ovata.cr.impl.concurrency.LocalConcurrencyControlFactory;
import ch.ovata.cr.store.aws.S3BlobStoreFactory;
import ch.ovata.cr.store.mongodb.MongoDbConnection;
import ch.ovata.cr.store.mongodb.search.MongoDbSearchProviderFactory;
import com.amazonaws.regions.Regions;
import com.mongodb.client.MongoClients;

/**
 *
 * @author dani
 */
public class RepositoryConnectionFactory2 {
    
    private static RepositoryConnectionFactory2 instance = new RepositoryConnectionFactory2();
    
    public static RepositoryConnectionFactory2 instance() {
        return instance;
    }
    
    private final RepositoryConnection connection;
    
    private RepositoryConnectionFactory2() {
        var client = MongoClients.create( "mongodb+srv://ovatacms:33Ta3TYDZInudadU@ovatacms-cluster-test-dffel.mongodb.net/test?retryWrites=true&minPoolSize=1&maxPoolSize=50&maxIdleTimeMS=120000");
        var blobStoreFactory = new S3BlobStoreFactory( Regions.EU_WEST_1, "ovata-cr-bucket-test");
        var ccontrol = new LocalConcurrencyControlFactory();
        var dbconnection = new MongoDbConnection( client, blobStoreFactory, ccontrol);
        var searchProviderFactory = new MongoDbSearchProviderFactory( dbconnection);
        
        this.connection = new RepositoryConnectionImpl( dbconnection, searchProviderFactory);
    }
    
    public RepositoryConnection getConnection() {
        return this.connection;
    }
    
    public void shutdown() {
        this.connection.shutdown();
    }
}
