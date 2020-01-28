/*
 * $Id: NodeComparatorFactory.java 2843 2019-11-22 09:31:33Z dani $
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
import ch.ovata.cr.api.Value;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author dani
 */
public final class NodeComparatorFactory {
    
    private NodeComparatorFactory() {
    }
    
    public static Comparator<Item> createComparator( List<QuerySortOrder> sortOrders) {
        Comparator<Item> comparator = new ItemTypeComparator();
        
        for( QuerySortOrder sortOrder : sortOrders) {
            if( sortOrder == null) {
                continue;
            }
            
            switch (sortOrder.getSorted()) {
                case "@Name":
                    comparator = new NameComparator( sortOrder.getDirection(), comparator);
                    break;
                case "@Type":
                    comparator = new TypeComparator( sortOrder.getDirection(), comparator);
                    break;
                default:
                    comparator = new PropertyComparator( sortOrder.getSorted(), sortOrder.getDirection(), comparator);
                    break;
            }
        }
        
        return comparator;
    }
    
    public static class PropertyComparator extends AbstractChainedComparator {
        
        private final String name;
        private final SortDirection direction;
        
        public PropertyComparator( String name, SortDirection direction, Comparator<Item> predecessor) {
            super( predecessor);
            
            this.name = name;
            this.direction = direction;
        }
        
        @Override
        protected int compareInternal( Item i1, Item i2) {
            if( i1 instanceof Node && i2 instanceof Node) {
                Optional<Value> v1 = ((Node)i1).findProperty( this.name).map( p -> p.getValue());
                Optional<Value> v2 = ((Node)i2).findProperty( this.name).map( p -> p.getValue());
                
                if( v1.isPresent() && v2.isPresent()) {
                    int c = v1.get().compareTo( v2.get());
                    
                    return (this.direction == SortDirection.ASCENDING) ? c : -c;
                }
            }                

            return 0;
        }
    }
    
    public static class TypeComparator extends AbstractChainedComparator {

        private final SortDirection direction;
        
        public TypeComparator( SortDirection direction, Comparator<Item> predecessor) {
            super( predecessor);
            
            this.direction = direction;
        }
        
        @Override
        protected int compareInternal(Item i1, Item i2) {
            if( i1 instanceof Node && i2 instanceof Node) {
                int c = ((Node)i1).getType().compareTo( ((Node)i2).getType());
                
                return (this.direction == SortDirection.ASCENDING) ? c : -c;
            }
            else {
                return 0;
            }
        }
    }
    
    public static class NameComparator extends AbstractChainedComparator {

        private final SortDirection direction;
        
        public NameComparator( SortDirection direction, Comparator<Item> predecessor) {
            super( predecessor);
            
            this.direction = direction;
        }
        
        @Override
        protected int compareInternal(Item i1, Item i2) {
            int c = i1.getName().compareTo( i2.getName());
            
            return (this.direction == SortDirection.ASCENDING) ? c : -c;
        }
    }
    
    public static class ItemTypeComparator implements Comparator<Item> {

        @Override
        public int compare(Item o1, Item o2) {
            if( o1 instanceof Property && o2 instanceof Node) {
                return -1;
            }
            else if( o1 instanceof Node && o2 instanceof Property) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
    
    public static abstract class AbstractChainedComparator implements Comparator<Item> {
        
        private final Comparator<Item> predecessor;
        
        protected AbstractChainedComparator( Comparator<Item> predecessor) {
            this.predecessor = predecessor;
        }
        
        @Override
        public int compare( Item i1, Item i2) {
            int r = this.predecessor.compare( i1, i2);
            
            if( r == 0) {
                return compareInternal( i1, i2);
            }
            
            return 0;
        }
        
        protected abstract int compareInternal( Item i1, Item i2);
    }
}
