/*
 * $Id: RepositoryLifeCycleListener.java 2856 2019-11-27 14:17:14Z dani $
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

import ch.ovata.cr.browser.utils.RepositoryHelper;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dani
 */
@WebListener
public class RepositoryLifeCycleListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger( RepositoryLifeCycleListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info( "Initializing servlet context.");
        
//        RepositoryHelper.setConnection( RepositoryConnectionFactory2.instance().getConnection());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info( "Shutting down servlet context.");
        
        RepositoryHelper.clear();
//        RepositoryConnectionFactory2.instance().shutdown();
    }
}
