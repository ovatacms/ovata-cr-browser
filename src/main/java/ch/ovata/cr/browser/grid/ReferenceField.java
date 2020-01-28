/*
 * $Id: ReferenceField.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 06.12.2019, 12:00:00
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
import ch.ovata.cr.api.Reference;
import ch.ovata.cr.api.Value;
import ch.ovata.cr.api.ValueFactory;
import ch.ovata.cr.browser.ChooseNodeDialog;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 *
 * @author dani
 */
public class ReferenceField extends CustomField<Value> {

    private final ValueFactory vf;
    private final Button btnClear = new Button( VaadinIcon.CLOSE_SMALL.create(), this::onClear);
    private final Button btnChoose = new Button( VaadinIcon.ELLIPSIS_DOTS_H.create(), this::onChoose);
    private final Label label = new Label();
    private Value value;
    
    public ReferenceField( ValueFactory vf) {
        this.vf = vf;
        
        this.btnClear.setThemeName( ButtonVariant.LUMO_ICON.getVariantName());
        this.btnChoose.setThemeName( ButtonVariant.LUMO_ICON.getVariantName());
        
        this.label.setWidth( "100%");
        
        this.add( new DivWithPadding( label, btnClear, btnChoose));
    }
    
    private void onClear( ClickEvent<Button> event) {
        this.value = null;
        
        this.label.setText( "");
        
        this.updateValue();
    }
    
    private void onChoose( ClickEvent<Button> event) {
        new ChooseNodeDialog( this::updateReference).open();
    }
    
    private void updateReference( Node node) {
        this.setPresentationValue( this.vf.referenceByPath( node.getSession().getWorkspace().getName(), node.getPath()));

        this.updateValue();
    }
    
    @Override
    protected Value generateModelValue() {
        return this.value;
    }

    @Override
    protected void setPresentationValue(Value newPresentationValue) {
        this.value = newPresentationValue;
        
        Reference ref = newPresentationValue.getReference();
        
        this.label.setText( ref.getWorkspace() + ":" + ref.getPath());
    }
}
