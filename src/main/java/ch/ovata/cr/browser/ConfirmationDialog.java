/*
 * $Id: ConfirmationDialog.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 04.12.2019, 12:00:00
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
package ch.ovata.cr.browser;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 *
 * @author dani
 */
public class ConfirmationDialog extends Dialog {
    
    public static interface Action {
        void execute();
    }
    
    private final Action action;
    
    public ConfirmationDialog( String text, Action action) {
        this( text, null, action);
    }
    
    public ConfirmationDialog( String text, String confirmationLabel, Action action) {
        this.action = action;
        
        this.setWidth( "400px");
        this.setCloseOnEsc( false);
        this.setCloseOnOutsideClick( false);
        
        Button btnCancel = new Button( "Cancel", this::onCancel);
        Button btnConfirm = new Button( "Confirm", this::onConfirm);

        if( confirmationLabel != null) {
            btnConfirm.setText( confirmationLabel);
        }
        
        btnCancel.setThemeName( ButtonVariant.LUMO_TERTIARY.getVariantName());
        btnConfirm.setThemeName( ButtonVariant.LUMO_PRIMARY.getVariantName());
        
        HorizontalLayout buttons = new HorizontalLayout( btnCancel, btnConfirm);
        buttons.setWidth( "100%");
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        
        VerticalLayout content = new VerticalLayout( new Label( text), buttons);
        content.setWidth( "100%");
        
        this.add( content);
    }
    
    private void onCancel( ClickEvent<Button> event) {
        this.close();
    }
    
    private void onConfirm( ClickEvent<Button> event) {
        action.execute();
        
        this.close();
    }
}
