/*
 * $Id: ChooseNodeDialog.java 2898 2019-12-19 15:42:42Z dani $
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
package ch.ovata.cr.browser;

import ch.ovata.cr.api.Node;
import ch.ovata.cr.browser.grid.SimpleNodeGrid;
import ch.ovata.cr.browser.main.SessionMgr;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.Set;
import java.util.function.Consumer;

/**
 *
 * @author dani
 */
@CssImport( value = "./ovata-dialog.css", themeFor = "vaadin-dialog-overlay")
public class ChooseNodeDialog extends Dialog {
    
    private final SimpleNodeGrid grid = new SimpleNodeGrid( SessionMgr.getRepository().getSession( "system_users"));
    private final Consumer<Node> callback;
    
    public ChooseNodeDialog( Consumer<Node> callback) {
        this.callback = callback;
        
        this.setWidth( "800px");
    
        Button btnCancel = new Button( "Cancel", this::onCancel);
        Button btnSelect = new Button( "Select", this::onSelect);
        
        btnCancel.setThemeName( ButtonVariant.LUMO_TERTIARY.getVariantName());
        btnSelect.setThemeName( ButtonVariant.LUMO_PRIMARY.getVariantName());

        HorizontalLayout buttons = new HorizontalLayout( btnCancel, btnSelect);
        buttons.setWidth( "100%");
        buttons.setJustifyContentMode( FlexComponent.JustifyContentMode.END);
        
        WorkspacesComboBox workspaces = new WorkspacesComboBox();
        workspaces.setWidth( "100%");
        workspaces.addValueChangeListener( this::onChangeWorkspace);
        
        this.grid.setWidth( "100%");
        this.grid.setHeight( "500px");
        
        H3 title = new H3( "Choose Node");
        title.setWidth( "100%");
        
        VerticalLayout content = new VerticalLayout( title, workspaces, this.grid, buttons);
        content.setMargin( false);
        content.setSizeFull();
        
        this.add( content);
    }
    
    private void onChangeWorkspace( ValueChangeEvent<String> event) {
        this.grid.setSession( SessionMgr.getRepository().getSession( event.getValue()));
    }
    
    private void onCancel( ClickEvent<Button> event) {
        this.close();
    }
    
    private void onSelect( ClickEvent<Button> event) {
        Set<Node> nodes = this.grid.getSelectedItems();
        
        if( !nodes.isEmpty()) {
            this.callback.accept( nodes.iterator().next());
            this.close();
        }
    }
}
