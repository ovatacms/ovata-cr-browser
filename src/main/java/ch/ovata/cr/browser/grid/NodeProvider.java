/*
 * $Id: NodeProvider.java 2843 2019-11-22 09:31:33Z dani $
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
package ch.ovata.cr.browser.grid;

import ch.ovata.cr.api.Item;
import ch.ovata.cr.api.Node;
import ch.ovata.cr.api.Session;
import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.provider.Query;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 *
 * @author dani
 */
public class NodeProvider extends AbstractDataProvider<Node, Object> {

    private final Session session;
    private final String rootPath;
    
    public NodeProvider( Session session, String rootPath) {
        this.session = session;
        this.rootPath = rootPath;
    }
    
    @Override
    public int size(Query<Node, Object> query) {
        Node root = session.getNodeByPath( this.rootPath);
        
        return (int)root.getNodes().stream().skip( query.getOffset()).limit( query.getLimit()).count();
    }

    @Override
    public Stream<Node> fetch(Query<Node, Object> query) {
        Node root = session.getNodeByPath( this.rootPath);
        Comparator<Item> comparator = NodeComparatorFactory.createComparator( query.getSortOrders());
        
        return root.getNodes().stream().sorted( comparator).skip( query.getOffset()).limit( query.getLimit());
    }

    @Override
    public boolean isInMemory() {
        return false;
    }
}
