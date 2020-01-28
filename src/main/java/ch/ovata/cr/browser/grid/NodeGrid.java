/*
 * $Id: NodeGrid.java 2843 2019-11-22 09:31:33Z dani $
 * Created on 05.04.2019, 12:00:00
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

import ch.ovata.cr.api.Node;
import ch.ovata.cr.api.util.PropertyComparator;
import com.vaadin.flow.component.grid.Grid;

/**
 *
 * @author dani
 */
public class NodeGrid extends Grid<Node> {
    
    private static final String OPTION_WIDTH = "width";
    private static final String OPTION_TITLE = "title";
    private static final String OPTION_COLUMNS = "columns";
    private static final String OPTION_DEFAULT_ACTION = "defaultAction";
    
    private final Node desc;
    
    public NodeGrid( Node desc, NodeProvider provider) {
        this.desc = desc;
        
        this.setDataProvider( provider);
        
        this.desc.getNode( OPTION_COLUMNS).getNodes().stream().sorted( PropertyComparator.ByOrder).forEach( this::createColumn);
    }
    
    private void createColumn( Node column) {
        String name = column.getName();
        String title = column.findProperty( OPTION_TITLE).map( p -> p.getValue().getString()).orElse( "-");
        double width = column.findProperty( OPTION_WIDTH).map( p -> p.getValue().getDouble()).orElse( 1.0d);
        
        if( width < 1) {
            width = 10 * width;
        }
        
        switch( name) {
            case "@NodeName":
                this.addColumn( n -> n.getName()).setHeader( title);
                break;
            case "@NodeType":
                this.addColumn( n -> n.getType()).setHeader( title);
                break;
            default:
                this.addColumn( new NodeValueProvider( column.getName())).setHeader( title).setSortProperty( name).setFlexGrow( (int)width);
                break;
        }
    }
}
