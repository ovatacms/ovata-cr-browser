/*
 * $Id: SimpleNodeGrid.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 23.01.2019, 12:00:00
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
package ch.ovata.cr.browser.grid;

import ch.ovata.cr.api.Item;
import ch.ovata.cr.api.Node;
import ch.ovata.cr.api.Property;
import ch.ovata.cr.api.Session;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.treegrid.TreeGrid;

/**
 *
 * @author dani
 */
@CssImport( value = "./ovata-item-treegrid-toggle.css", themeFor = "vaadin-grid-tree-toggle")
@CssImport( value = "./ovata-item-treegrid.css", themeFor = "vaadin-grid")
public class SimpleNodeGrid extends TreeGrid<Node> {

    private Session session;
    
    public SimpleNodeGrid( Session session) {
        this.session = session;

        this.addHierarchyColumn( Item::getName).setHeader( "Name").setSortProperty( "@Name").setFlexGrow( 2);
        this.addColumn( this::renderType).setHeader( "Type").setSortProperty( "@Type").setFlexGrow( 1);
        this.setMultiSort( true);
        this.setDataProvider( new HierarchicalNodeProvider( session, "/"));
        this.setClassNameGenerator( new IconClassProvider());
    }
    
    public void setSession( Session session) {
        this.session = session;
        this.setDataProvider( new HierarchicalNodeProvider( session, "/"));
    }
    
    public Session getSession() {
        return this.session;
    }
    
    private String renderType( Item i) {
        if( i instanceof Node) {
            return ((Node)i).getType();
        }
        else if( i instanceof Property) {
            return ((Property)i).getValue().getType().name();
        }
        
        return "";
    }
}
