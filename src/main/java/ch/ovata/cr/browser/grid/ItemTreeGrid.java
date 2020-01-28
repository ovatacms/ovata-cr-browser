/*
 * $Id: ItemTreeGrid.java 2898 2019-12-19 15:42:42Z dani $
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
import ch.ovata.cr.api.Session;
import ch.ovata.cr.api.Value;
import ch.ovata.cr.api.ValueFactory;
import ch.ovata.cr.browser.utils.ValueUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;

/**
 *
 * @author dani
 */
@CssImport( value = "./ovata-item-treegrid-toggle.css", themeFor = "vaadin-grid-tree-toggle")
@CssImport( value = "./ovata-item-treegrid.css", themeFor = "vaadin-grid")
public class ItemTreeGrid extends TreeGrid<Item> {

    private ValueField valueField;
    private Session session;
    
    public ItemTreeGrid( Session session) {
        this.session = session;

        this.addHierarchyColumn( Item::getName).setHeader( "Name").setEditorComponent( this::createNameControl).setSortProperty( "@Name").setFlexGrow( 4);
        this.addColumn( this::renderType).setHeader( "Type").setEditorComponent( this::createTypeControl).setSortProperty( "@Type").setFlexGrow( 1);
        this.addColumn( this::renderValue).setHeader( "Value").setEditorComponent( this::createValueControl).setFlexGrow( 2);
        this.addColumn( i -> "").setEditorComponent( this::createSaveCancelButtons).setWidth( "100px");
        this.setMultiSort( true);
        
        this.setDataProvider( new HierarchicalItemProvider( session));
        
        this.addItemDoubleClickListener( this::startEditing);
        
        this.getElement().addEventListener( "keyup", e -> this.getEditor().cancel()).setFilter( "event.key === 'Escape' || event.key === 'Esc'");
        this.getElement().addEventListener( "keyup", e -> this.getEditor().save()).setFilter( "event.key === 'Enter'");
        
        this.setClassNameGenerator( new IconClassProvider());
    }
    
    public void setSession( Session session) {
        this.session = session;
        this.setDataProvider( new HierarchicalItemProvider( session));
    }
    
    public Session getSession() {
        return this.session;
    }
    
    private void startEditing( ItemDoubleClickEvent<Item> e) {
        Editor editor = this.getEditor();
        Binder<Item> binder = new Binder();

        editor.setBinder( binder);
        editor.setBuffered( true);
        editor.editItem( e.getItem());
    }
    
    private String renderValue( Item i) {
        if( i instanceof Property) {
            return ValueUtils.toString( ((Property)i).getValue());
        }
        
        return "";
    }
    
    private String renderType( Item i) {
        if( i instanceof Node) {
            return ((Node)i).getType();
        }
        else if( i instanceof Property) {
            return ((Property)i).getValue().getType().name();
        }
        
        return "";
    }
    
    private Component createSaveCancelButtons( Item i) {
        return new DivWithPadding( new Button( VaadinIcon.CHECK.create(), this::onSave), 
                                   new Button( VaadinIcon.CLOSE.create(), l -> this.getEditor().cancel()));
    }

    private void onSave( ClickEvent<Button> event) {
        this.getEditor().save();
        this.getDataProvider().refreshAll();
    }
    
    private Component createNameControl( Item item) {
        TextField field = new TextField100();
        
        this.getEditor().getBinder().forField( field).bind( Item::getName, Item::rename);
        
        return field;
    }
    
    private Component createTypeControl( Item item) {
        if( item instanceof Node) {
            TextField field = new TextField100();

            this.getEditor().getBinder().forField( field).bind( i -> ((Node)i).getType(), (i, t) -> ((Node)i).setType( t));
            
            return field;
        }
        else {
            ValueTypeComboBox field = new ValueTypeComboBox( this::onTypeChanged);
            
            this.getEditor().getBinder().forField( field).bind( i -> ((Property)i).getValue().getType(), (i, t) -> {});
            
            return field;
        }
    }
    
    private Component createValueControl( Item item) {
        if( item instanceof Property) {
            this.valueField = new ValueField( item.getParent().getSession().getValueFactory());

            this.getEditor().getBinder().forField( valueField).bind( i -> ((Property)i).getValue(), (i, t) -> item.getParent().setProperty( i.getName(), t));
            
            return this.valueField;
        }

        return null;
    }
    
    private void onTypeChanged( ComboBox.ValueChangeEvent<Value.Type> event) {
        if( event.getValue() != this.valueField.getCurrentType()) {
            ValueFactory vf = this.session.getValueFactory();
            
            this.valueField.setValue( ValueUtils.emptyValue(vf, event.getValue()));
        }
    }
}
