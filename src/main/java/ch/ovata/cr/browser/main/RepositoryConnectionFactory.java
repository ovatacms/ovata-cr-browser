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
import ch.ovata.cr.elastic.ElasticSearchProviderFactory;
import ch.ovata.cr.impl.RepositoryConnectionImpl;
import ch.ovata.cr.spi.search.SearchProviderFactory;
import ch.ovata.cr.spi.store.ConcurrencyControlFactory;
import ch.ovata.cr.spi.store.StoreConnection;
import ch.ovata.cr.spi.store.blob.BlobStoreFactory;
import ch.ovata.cr.store.postgresql.PostgresqlBlobStoreFactory;
import ch.ovata.cr.store.postgresql.PostgresqlConnection;
import ch.ovata.cr.store.postgresql.concurrency.PostgresqlConcurrencyControlFactory;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author dani
 */
public class RepositoryConnectionFactory {
    
    private final RepositoryConnection connection;
    
    private static RepositoryConnectionFactory instance = new RepositoryConnectionFactory();
    
    public static RepositoryConnectionFactory instance() {
        return instance;
    }
    
    private RepositoryConnectionFactory() {
        BasicDataSource ds = new BasicDataSource();

        ds.setDriverClassName( "org.postgresql.Driver");
        ds.setUrl( "jdbc:postgresql://localhost:5432/dani");
        ds.setUsername( "postgres");
        ds.setPassword( "");
        ds.setAutoCommitOnReturn( true);
        ds.setDefaultAutoCommit( false);
        ds.setInitialSize( 1);

        BlobStoreFactory blobStoreFactory = new PostgresqlBlobStoreFactory( ds);
        ConcurrencyControlFactory ccontrol = new PostgresqlConcurrencyControlFactory( ds);
        StoreConnection dbconnection = new PostgresqlConnection( ds, blobStoreFactory, ccontrol);
        SearchProviderFactory searchProviderFactory = new ElasticSearchProviderFactory( blobStoreFactory, "localhost", "");
        
        this.connection = new RepositoryConnectionImpl( dbconnection, searchProviderFactory);
    }
    
    public RepositoryConnection getConnection() {
        return this.connection;
    }
    
    public void shutdown() {
        this.connection.shutdown();
    }
}
