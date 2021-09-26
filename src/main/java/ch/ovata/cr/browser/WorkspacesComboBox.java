/*
 * $Id: WorkspacesComboBox.java 2860 2019-12-04 10:39:17Z dani $
 * Created on 04.10.2019, 12:00:00
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
package ch.ovata.cr.browser;

import ch.ovata.cr.browser.main.SessionMgr;
import com.vaadin.flow.component.combobox.ComboBox;
import java.util.Collection;

/**
 *
 * @author dani
 */
public class WorkspacesComboBox extends ComboBox<String> {
    
    public WorkspacesComboBox() {
        super( null, SessionMgr.getRepository().listWorkspaceNames());
        
        this.setAllowCustomValue( false);
        this.setRequired( true);
        
        this.setValue( SessionMgr.getRepository().listWorkspaceNames().iterator().next());
    }
    
    public void refresh() {
        Collection<String> items = SessionMgr.getRepository().listWorkspaceNames();
        
        this.setItems( items);
        this.setValue( items.iterator().next());
    }
}
