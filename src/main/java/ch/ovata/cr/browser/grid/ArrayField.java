/*
 * $Id: ArrayField.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 09.12.2019, 12:00:00
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
import ch.ovata.cr.api.ValueFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 *
 * @author dani
 */
public class ArrayField extends CustomField<Value> {

    private final ValueFactory vf;
    private final Button btnEdit = new Button( VaadinIcon.ELLIPSIS_DOTS_H.create(), this::onEdit);
    private Value value;
    
    public ArrayField( ValueFactory vf) {
        this.vf = vf;
        this.btnEdit.setThemeName( ButtonVariant.LUMO_ICON.getVariantName());
        
        this.add( btnEdit);
    }
    
    private void onEdit( ClickEvent<Button> event) {
        new EditArrayDialog( this.vf, this.value, this::onSave).open();
    }
    
    private void onSave( Value v) {
        this.value = v;
        
        updateValue();
    }
    
    @Override
    protected Value generateModelValue() {
        return this.value;
    }

    @Override
    protected void setPresentationValue(Value newPresentationValue) {
        this.value = newPresentationValue;
    }
}
