/*
 * $Id: HierarchicalNodeProvider.java 2898 2019-12-19 15:42:42Z dani $
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
import ch.ovata.cr.api.util.PropertyComparatorIfAvailable;
import com.vaadin.flow.data.provider.hierarchy.AbstractHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 *
 * @author dani
 */
public class HierarchicalNodeProvider extends AbstractHierarchicalDataProvider<Node, Object> {
    
    private final Node virtualRoot;
    private final NodeFilter filter;
    private final Comparator<Node> comparator;
    
    public HierarchicalNodeProvider( Session session, String rootPath) {
        this( session, rootPath, new NodeFilter.All(), PropertyComparatorIfAvailable.ByOrder);
    }
    
    public HierarchicalNodeProvider( Session session, String rootPath, NodeFilter filter, Comparator<Node> comparator) {
        this.filter = filter;
        this.virtualRoot = new VirtualRootNode( session, rootPath);
        this.comparator = comparator;
    }
    
    public Node getRootNode() {
        return virtualRoot;
    }

    @Override
    public int getChildCount(HierarchicalQuery<Node, Object> query) {
        Node node = query.getParentOptional().orElse( virtualRoot);

        return (int)node.getNodes().stream().filter( this.filter).count();
    }

    @Override
    public Stream<Node> fetchChildren(HierarchicalQuery<Node, Object> query) {
        Node node = query.getParentOptional().orElse( virtualRoot);
        Comparator<Item> comp = NodeComparatorFactory.createComparator( query.getSortOrders());
        
        return node.getNodes().stream().filter( this.filter).sorted( comp).skip( query.getOffset()).limit( query.getLimit());
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public Object getId( Node node) {
        return node.getId();
    }

    @Override
    public boolean hasChildren( Node node) {
        return node.getNodes().stream().anyMatch( this.filter);
    }
}
