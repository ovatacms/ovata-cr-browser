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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
        DataSource ds = getDataSource();
        BlobStoreFactory blobStoreFactory = new PostgresqlBlobStoreFactory( ds);
        ConcurrencyControlFactory ccontrol = new PostgresqlConcurrencyControlFactory( ds);
        StoreConnection dbconnection = new PostgresqlConnection( ds, blobStoreFactory, ccontrol);
        SearchProviderFactory searchProviderFactory = new ElasticSearchProviderFactory( blobStoreFactory, "localhost", "");
        
        this.connection = new RepositoryConnectionImpl( dbconnection, searchProviderFactory);
    }
    
    private DataSource getDataSource() {
        try {
            InitialContext ctx = new InitialContext();

            try {
                return (DataSource)ctx.lookup( "java:/comp/env/jdbc/database");
            }
            finally {
                ctx.close();
            }
        }
        catch( NamingException e) {
            throw new IllegalStateException( "Could not initialize content repository.", e);
        }
    }
    
    public RepositoryConnection getConnection() {
        return this.connection;
    }
    
    public void shutdown() {
        this.connection.shutdown();
    }
}
