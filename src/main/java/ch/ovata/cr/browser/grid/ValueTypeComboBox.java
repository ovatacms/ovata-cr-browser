/*
 * $Id: ValueTypeComboBox.java 2898 2019-12-19 15:42:42Z dani $
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

import ch.ovata.cr.api.Value;
import com.vaadin.flow.component.combobox.ComboBox;

/**
 *
 * @author dani
 */
public class ValueTypeComboBox extends ComboBox<Value.Type> {
    
    public ValueTypeComboBox( ValueChangeListener<ValueChangeEvent<Value.Type>> listener) {
        super( null, Value.Type.values());
        
        this.setWidth( "100%");
        this.addValueChangeListener( listener);
    }
}
