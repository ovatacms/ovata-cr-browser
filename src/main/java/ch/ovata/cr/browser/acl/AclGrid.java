/*
 * $Id: AclGrid.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 06.02.2020, 12:00:00
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

import ch.ovata.cr.api.Session;
import ch.ovata.cr.api.security.Acl;
import ch.ovata.cr.impl.security.ModifiableAcl;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.Binder;

/**
 *
 * @author dani
 */
public class AclGrid extends Grid<ModifiableAcl> {
    
    private final Session session;
    
    public AclGrid( Session session) {
        this.session = session;
        
        PrincipalComboBox principalCb = new PrincipalComboBox( session);
        Checkbox isNegativeCheckBox = new Checkbox();
        PermissionsListBox permissionsLb = new PermissionsListBox();
        
        Binder<ModifiableAcl> binder = new Binder();

        binder.forField( principalCb).bind( ModifiableAcl::getPrincipal, ModifiableAcl::setPrincipal);
        binder.forField( isNegativeCheckBox).bind( ModifiableAcl::isNegative, ModifiableAcl::setNegative);
        binder.forField( permissionsLb).bind( ModifiableAcl::getPermissions, ModifiableAcl::setPermissions);
        
        principalCb.setWidth( "100%");
        permissionsLb.setWidth( "100%");
        
        this.addColumn( acl -> (acl.getPrincipal() != null) ? acl.getPrincipal().getName() : "")
                    .setHeader( "Principal")
                    .setFlexGrow( 4)
                    .setEditorComponent( principalCb);
        this.addColumn( Acl::isNegative).setHeader( "Negative?")
                    .setFlexGrow( 1)
                    .setEditorComponent( isNegativeCheckBox);
        this.addColumn( Acl::getPermissions)
                    .setHeader( "Permissions").setFlexGrow( 6)
                    .setEditorComponent( permissionsLb);
        
        this.getEditor().setBinder( binder);
        this.getEditor().setBuffered( true);
    }
}
