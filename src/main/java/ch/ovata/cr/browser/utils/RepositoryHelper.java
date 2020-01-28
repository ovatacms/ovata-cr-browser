/*
 * $Id: RepositoryHelper.java 2843 2019-11-22 09:31:33Z dani $
 * Created on 27.09.2019, 12:00:00
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
package ch.ovata.cr.browser.utils;

import ch.ovata.cr.api.RepositoryConnection;

/**
 *
 * @author dani
 */
public class RepositoryHelper {
    
    private static RepositoryConnection connection;
    
    public static RepositoryConnection getConnection() {
        return connection;
    }
    
    public static void setConnection( RepositoryConnection rcon) {
        RepositoryHelper.connection = rcon;
    }
    
    public static void clear() {
        RepositoryHelper.connection = null;
    }
}
