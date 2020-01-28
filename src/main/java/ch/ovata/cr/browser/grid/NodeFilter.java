/*
 * $Id: NodeFilter.java 2843 2019-11-22 09:31:33Z dani $
 * Created on 17.05.2019, 12:00:00
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 *
 * @author dani
 */
public interface NodeFilter extends Predicate<Node> {
    
    public static class All implements NodeFilter {

        @Override
        public boolean test(Node node) {
            return true;
        }
    }
    
    public static class ByType implements NodeFilter {

        private final Set<String> types;
        
        public ByType( Set<String> types) {
            this.types = new HashSet<>( types);
        }
        
        public ByType( String... types) {
            this.types = new HashSet<>( Arrays.asList( types));
        }
        
        @Override
        public boolean test(Node node) {
            return node.isRoot() || types.isEmpty() || types.contains( node.getType());
        }
    }
}
