/*
 * $Id: PrincipalComboBox.java 2898 2019-12-19 15:42:42Z dani $
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

import ch.ovata.cr.api.Node;
import ch.ovata.cr.api.Session;
import ch.ovata.cr.api.Workspace;
import ch.ovata.cr.api.security.RolePrincipal;
import com.vaadin.flow.component.combobox.ComboBox;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author dani
 */
public class PrincipalComboBox extends ComboBox<Principal> {
    public PrincipalComboBox( Session session) {
        super( null, getPrincipals( session));

        this.setItemLabelGenerator( Principal::getName);
    }

    private static List<Principal> getPrincipals( Session session) {
        Session rolesSession = session.getRepository().getSession( Workspace.WORKSPACE_USERS);
        Collection<? extends Node> roleNodes = rolesSession.getNodeByPath( "/roles").getNodes();

        return roleNodes.stream().map( n -> new RolePrincipal( n.getName())).collect( Collectors.toList());
    }
}
