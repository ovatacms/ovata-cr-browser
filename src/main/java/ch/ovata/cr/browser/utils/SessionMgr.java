/*
 * $Id: SessionMgr.java 2843 2019-11-22 09:31:33Z dani $
 * Created on 04.04.2019, 08:31:00
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

import ch.ovata.cr.api.Repository;
import ch.ovata.cr.api.RepositoryConnection;
import ch.ovata.cr.api.RepositoryException;
import ch.ovata.cr.api.Session;
import com.vaadin.flow.server.VaadinSession;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;

/**
 *
 * @author dani
 */
public class SessionMgr {
    
    public static Repository getRepository() {
        return VaadinSession.getCurrent().getAttribute( Repository.class);
    }
    
    public static Repository getRepositoryLocked() {
        VaadinSession session = VaadinSession.getCurrent();
        
        try {
            session.lock();
            
            return session.getAttribute( Repository.class);
        }
        finally {
            session.unlock();
        }
    }
    
    public static void login( String repositoryName, String username, String password) throws LoginException {
        try {
            VaadinSession session = VaadinSession.getCurrent();
            Repository repository = getConnection().login( repositoryName, username, password.toCharArray());

            session.setAttribute( Repository.class, repository);
        }
        catch( NamingException e) {
            throw new RepositoryException( "Could not login to repository.", e);
        }
    }
    
    public static boolean isLoggedIn() {
        return (VaadinSession.getCurrent() != null) && (getRepository() != null);
    }
    
    public static Session getEditorSession() {
        VaadinSession vs = VaadinSession.getCurrent();
        
        try {
            vs.lock();
            
            return (Session)vs.getAttribute( "ch.ovata.cms.editor.Session");
        }
        finally {
            vs.unlock();
        }
    }
    
    public static void setEditorSession( Session session) {
        VaadinSession.getCurrent().setAttribute( "ch.ovata.cms.editor.Session", session);
    }
    
    private static RepositoryConnection getConnection() throws NamingException {
        return RepositoryHelper.getConnection();
    }
}
