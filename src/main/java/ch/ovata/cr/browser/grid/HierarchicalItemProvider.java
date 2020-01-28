/*
 * $Id: HierarchicalItemProvider.java 2843 2019-11-22 09:31:33Z dani $
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
import ch.ovata.cr.api.Session;
import com.vaadin.flow.data.provider.hierarchy.AbstractHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 *
 * @author dani
 */
public class HierarchicalItemProvider extends AbstractHierarchicalDataProvider<Item, Object> {
    
    private final Node virtualRoot;
    
    public HierarchicalItemProvider( Session session) {
        this.virtualRoot = new VirtualRootNode( session, "/");
    }
    
    public Node getRootNode() {
        return virtualRoot;
    }

    @Override
    public int getChildCount(HierarchicalQuery<Item, Object> query) {
        Object item = query.getParentOptional().orElse( virtualRoot);

        if (item instanceof Node) {
            Node node = (Node)item;

            return node.getNodes().size() + node.getProperties().size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Stream<Item> fetchChildren(HierarchicalQuery<Item, Object> query) {
        Item item = query.getParentOptional().orElse( virtualRoot);

        if (item instanceof Node) {
            Node node = (Node)item;
            Comparator<Item> comparator = NodeComparatorFactory.createComparator( query.getSortOrders());
            return Stream.concat( node.getProperties().stream(), node.getNodes().stream()).sorted( comparator).skip( query.getOffset()).limit( query.getLimit());
        }
        else {
            return Collections.EMPTY_LIST.stream();
        }
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public Object getId(Item item) {
        if (item instanceof Node) {
            return ((Node) item).getId();
        }
        else {
            return item.getParent().getId() + "#" + item;
        }
    }

    @Override
    public boolean hasChildren( Item item) {
        return (item instanceof Node) && (!((Node) item).getNodes().isEmpty() || !((Node) item).getProperties().isEmpty());
    }
}
