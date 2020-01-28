/*
 * $Id: IconClassProvider.java 2843 2019-11-22 09:31:33Z dani $
 * Created on 15.05.2019, 12:00:00
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

import ch.ovata.cr.api.CoreNodeTypes;
import ch.ovata.cr.api.Item;
import ch.ovata.cr.api.Node;
import ch.ovata.cr.api.Property;
import com.vaadin.flow.function.SerializableFunction;

/**
 *
 * @author dani
 * @param <T> type of item to inspect
 */
public class IconClassProvider<T extends Item> implements SerializableFunction<T, String> {

    @Override
    public String apply(Item item) {
        if( item instanceof Property) {
            return "ovata-toggle-property";
        }
        else {
            Node node = (Node)item;
            
            switch( node.getType()) {
                case CoreNodeTypes.FOLDER:
                    return "ovata-toggle-folder";
                case CoreNodeTypes.RESOURCE:
                    return "ovata-toggle-resource";
                case CoreNodeTypes.APPLICATION:
                    return "ovata-toggle-application";
                case CoreNodeTypes.ROLE:
                    return "ovata-toggle-role";
                case CoreNodeTypes.USER:
                    return "ovata-toggle-user";
                case CoreNodeTypes.FORM:
                    return "ovata-toggle-form";
            }
        }
        
        return "ovata-toggle-unstructured";
    }
}
