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
package ch.ovata.cr.browser.main;

import ch.ovata.cr.api.Repository;
import ch.ovata.cr.api.Session;
import com.vaadin.flow.server.VaadinSession;
import javax.security.auth.login.LoginException;
import org.springframework.stereotype.Service;

/**
 *
 * @author dani
 */
@Service
public class SessionMgr {
    
    private final RepositoryFactory repositoryFactory;
    
    public SessionMgr( RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }
    
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
    
    public void login( String repositoryName, String username, String password) throws LoginException {
        VaadinSession session = VaadinSession.getCurrent();
        Repository repository = this.repositoryFactory.getConnection().login( repositoryName, username, password.toCharArray());

        session.setAttribute( Repository.class, repository);
    }
    
    public  boolean isLoggedIn() {
        return (VaadinSession.getCurrent() != null) && (getRepository() != null);
    }
    
    public Session getEditorSession() {
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
}
