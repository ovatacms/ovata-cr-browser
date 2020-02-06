/*
 * $Id: EditAclDialog.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 30.01.2020, 12:00:00
 * 
 * Copyright (c) 2020 by Ovata GmbH,
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Ovata GmbH ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Ovata GmbH.
 */
package ch.ovata.cr.browser.acl;

import ch.ovata.cr.api.Node;
import ch.ovata.cr.impl.security.ModifiableAcl;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author dani
 */
@CssImport( value = "./ovata-dialog.css", themeFor = "vaadin-dialog-overlay")
public class EditAclDialog extends Dialog {
    
    private final AclGrid grid;
    private final Node node;
    private final List<ModifiableAcl> acls;
    
    private final Button btnCancel = new Button( "Cancel", this::onCancel);
    private final Button btnConfirm = new Button( "Save", this::onSave);
    private final Button addAcl = new Button( VaadinIcon.PLUS.create(), this::onAddAcl);
    private final Button removeAcl = new Button( VaadinIcon.MINUS.create(), this::onRemoveAcl);
    private final Button cancelEdit = new Button( VaadinIcon.CLOSE_SMALL.create(), this::onCancelEdit);
    private final Button saveEdit = new Button( VaadinIcon.CHECK.create(), this::onSaveEdit);
    
    public EditAclDialog( Node node) {
        this.node = node;
        this.acls = getModifiableCopyOfAcls();
        
        this.setWidth( "800px");
        this.setCloseOnEsc( false);
        this.setCloseOnOutsideClick( false);
        
        this.grid = new AclGrid( node.getSession());
        this.grid.addItemDoubleClickListener( this::onStartEditing);
        this.grid.setItems( acls);
        this.grid.setWidth( "100%");
        
        this.btnCancel.setThemeName( ButtonVariant.LUMO_TERTIARY.getVariantName());
        this.btnConfirm.setThemeName( ButtonVariant.LUMO_PRIMARY.getVariantName());
        
        HorizontalLayout buttons = new HorizontalLayout( this.btnCancel, this.btnConfirm);
        buttons.setWidth( "100%");
        buttons.setJustifyContentMode( FlexComponent.JustifyContentMode.END);

        VerticalLayout abuttons = new VerticalLayout( addAcl, removeAcl, saveEdit, cancelEdit);
        abuttons.setMargin( false);
        abuttons.setPadding( false);
        
        saveEdit.setVisible( false);
        cancelEdit.setVisible( false);
        
        HorizontalLayout mc = new HorizontalLayout( this.grid, abuttons);
        
        mc.setSizeFull();
        mc.setMargin( false);
        mc.setPadding( false);
        abuttons.setWidth( "50px");
        abuttons.setSpacing( false);
       
        VerticalLayout content = new VerticalLayout( new H3( "Edit ACL"), mc, buttons);
        content.setWidth( "100%");

        this.add( content);
    }
    
    private void onAddAcl( ClickEvent<Button> event) {
        this.acls.add( new ModifiableAcl());
        this.grid.getDataProvider().refreshAll();
    }
    
    private void onRemoveAcl( ClickEvent<Button> event) {
        this.acls.removeAll( this.grid.getSelectedItems());
        this.grid.getDataProvider().refreshAll();
    }
    
    private void onCancelEdit( ClickEvent<Button> event) {
        if( this.grid.getEditor().isOpen()) {
            this.grid.getEditor().cancel();
            leaveEditState();
        }
    }
    
    private void onSaveEdit( ClickEvent<Button> event) {
        if( this.grid.getEditor().isOpen()) {
            this.grid.getEditor().save();
            leaveEditState();
        }
    }
            
    private void onStartEditing( ItemDoubleClickEvent<ModifiableAcl> e) {
        enterEditState();
        this.grid.getEditor().editItem( e.getItem());
    }
    
    private void onCancel( ClickEvent<Button> event) {
        this.close();
    }
    
    private void onSave( ClickEvent<Button> event) {
        this.node.getPolicy().updateAcls( (List)this.acls);
        this.close();
    }
    
    private void enterEditState() {
        this.saveEdit.setVisible( true);
        this.cancelEdit.setVisible( true);
        this.addAcl.setVisible( false);
        this.removeAcl.setVisible( false);
        
        this.btnCancel.setEnabled( false);
        this.btnConfirm.setEnabled( false);
    }
    
    private void leaveEditState() {
        this.saveEdit.setVisible( false);
        this.cancelEdit.setVisible( false);
        this.addAcl.setVisible( true);
        this.removeAcl.setVisible( true);
        
        this.btnCancel.setEnabled( true);
        this.btnConfirm.setEnabled( true);
    }
    
    private List<ModifiableAcl> getModifiableCopyOfAcls() {
        return new ArrayList( this.node.getPolicy().getAcls().stream().map( a -> new ModifiableAcl( a)).collect( Collectors.toList()));
    }
}
