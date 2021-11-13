package ch.ovata.cr.browser.main;

import ch.ovata.cr.api.RepositoryConnection;
import ch.ovata.cr.elastic.ElasticSearchProviderFactory;
import ch.ovata.cr.impl.RepositoryConnectionImpl;
import ch.ovata.cr.spi.search.SearchProviderFactory;
import ch.ovata.cr.spi.store.StoreConnection;
import ch.ovata.cr.spi.store.blob.BlobStoreFactory;
import ch.ovata.cr.store.postgresql.PostgresqlBlobStoreFactory;
import ch.ovata.cr.store.postgresql.PostgresqlConnection;
import ch.ovata.cr.store.postgresql.search.PostgresqlSearchProviderFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author dani
 */
@Configuration
public class RepositoryFactory {
    
    private final DataSource datasource;
    
    public RepositoryFactory( DataSource ds) {
        this.datasource =ds ;
    }
    
    @Bean
    public RepositoryConnection getConnection() {
        BlobStoreFactory blobStoreFactory = new PostgresqlBlobStoreFactory( this.datasource);
        StoreConnection dbconnection = new PostgresqlConnection( this.datasource, blobStoreFactory);
        SearchProviderFactory searchProviderFactory = new PostgresqlSearchProviderFactory( );
        
        return new RepositoryConnectionImpl( dbconnection, searchProviderFactory);
    }
}
