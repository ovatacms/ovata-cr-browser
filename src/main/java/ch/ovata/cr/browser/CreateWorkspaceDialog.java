/*
 * $Id: CreateWorkspaceDialog.java 2898 2019-12-19 15:42:42Z dani $
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import java.util.function.Consumer;

/**
 *
 * @author dani
 */
public class CreateWorkspaceDialog extends Dialog {

    private final TextField name = new TextField();
    private final Consumer<String> callback;
    
    public CreateWorkspaceDialog( Consumer<String> callback) {
        this.callback = callback;
        
        this.setWidth( "500px");
        this.setCloseOnEsc( false);
        this.setCloseOnOutsideClick( false);

        this.name.setWidth( "100%");
        this.name.setLabel( "Workspace name");
        this.name.focus();
        
        Button btnCancel = new Button( "Cancel", this::onCancel);
        Button btnCreate = new Button( "Create", this::onCreate);
        btnCreate.setWidth( "100px");
        btnCreate.setThemeName( ButtonVariant.LUMO_PRIMARY.getVariantName());
        btnCancel.setWidth( "100px");
        btnCancel.setThemeName( ButtonVariant.LUMO_TERTIARY.getVariantName());
        
        HorizontalLayout buttons = new HorizontalLayout( btnCancel, btnCreate);
        buttons.setWidth( "100%");
        buttons.setJustifyContentMode( FlexComponent.JustifyContentMode.END);
        
        VerticalLayout content = new VerticalLayout( this.name, buttons);
        content.setMargin( false);
        
        this.add( content);
    }
    
    private void onCancel( ClickEvent<Button> event) {
        this.close();
    }
    
    private void onCreate( ClickEvent<Button> event) {
        String value = this.name.getValue().trim();
        
        if( value.isEmpty()) {
            this.name.setErrorMessage( "Name must not be blank.");
        }
        else {
            callback.accept( this.name.getValue());
            this.close();
        }
    }
}
