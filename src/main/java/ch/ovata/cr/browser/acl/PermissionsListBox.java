/*
 * $Id: PermissionsComboBox.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 31.01.2020, 12:00:00
 * 
 * Copyright (c) 2020 by Ovata GmbH,
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Ovata GmbH ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Ovata GmbH.
 */
package ch.ovata.cr.browser.acl;

import ch.ovata.cr.api.security.Permission;
import com.vaadin.flow.component.listbox.MultiSelectListBox;

/**
 *
 * @author dani
 */
public class PermissionsListBox extends MultiSelectListBox<Permission> {
    
    public PermissionsListBox() {
        this.setItems( Permission.values());
    }
}
