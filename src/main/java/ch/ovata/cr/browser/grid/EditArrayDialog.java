/*
 * $Id: EditArrayDialog.java 2898 2019-12-19 15:42:42Z dani $
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
import ch.ovata.cr.browser.utils.ValueUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author dani
 */
@CssImport( value = "./ovata-dialog.css", themeFor = "vaadin-dialog-overlay")
public class EditArrayDialog extends Dialog {
    
    private final ValueFactory vf;
    private final Grid<ValueWrapper> grid = new Grid<>();
    private final List<ValueWrapper> array;
    private final Consumer<Value> callback;
    private ValueField valueField;

    public EditArrayDialog( ValueFactory vf, Value value, Consumer<Value> callback) {
        this.vf = vf;
        this.callback = callback;
        this.array = Arrays.stream( value.getArray()).map( v -> new ValueWrapper( v)).collect( Collectors.toList());

        this.setWidth( "900px");
        
        H3 title = new H3( "Edit Array");
        title.setWidth( "100%");
        
        this.grid.addColumn( this::getType).setHeader( "Type").setEditorComponent( this::createTypeControl).setFlexGrow( 3);
        this.grid.addColumn( v -> ValueUtils.toString( v.getValue())).setHeader( "Value").setEditorComponent( this::createValueControl).setFlexGrow( 3);
        this.grid.addColumn( i -> "").setEditorComponent( this::createSaveCancelButtons).setFlexGrow( 1);
        this.grid.setWidth( "100%");
        this.grid.setHeight( "500px");
        this.grid.setDataProvider( new ListDataProvider( array));
        this.grid.addItemDoubleClickListener( this::startEditing);
        
        Button btnCancel = new Button( "Cancel", this::onCancel);
        Button btnSave = new Button( "Save", this::onSave);

        btnCancel.setThemeName( ButtonVariant.LUMO_TERTIARY.getVariantName());
        btnSave.setThemeName( ButtonVariant.LUMO_PRIMARY.getVariantName());
        
        HorizontalLayout buttons = new HorizontalLayout( btnCancel, btnSave);
        buttons.setWidth( "100%");
        buttons.setJustifyContentMode( FlexComponent.JustifyContentMode.END);
        
        VerticalLayout vbuttons = new VerticalLayout( new Button( VaadinIcon.PLUS.create(), this::onAddElement), 
                                                      new Button( VaadinIcon.MINUS.create(), this::onRemoveElement),
                                                      new Button( VaadinIcon.ARROW_UP.create(), this::onMoveUp),
                                                      new Button( VaadinIcon.ARROW_DOWN.create(), this::onMoveDown));
        HorizontalLayout inner = new HorizontalLayout( this.grid, vbuttons);
        
        vbuttons.setHeight( "100%");
        vbuttons.setMargin( false);
        vbuttons.setPadding( false);
        vbuttons.setWidth( "50px");
        inner.setWidth( "100%");
        
        this.add( new VerticalLayout( title, inner, buttons));
    }
    
    private void startEditing( ItemDoubleClickEvent<ValueWrapper> e) {
        Editor<ValueWrapper> editor = this.grid.getEditor();
        
        Binder<ValueWrapper> binder = new Binder();

        editor.setBinder( binder);
        editor.setBuffered( true);
        editor.editItem( e.getItem());
    }

    private Component createTypeControl( ValueWrapper item) {
        ValueTypeComboBox field = new ValueTypeComboBox( this::onTypeChanged);

        this.grid.getEditor().getBinder().forField( field).bind( i -> i.getValue().getType(), (i, t) -> {});

        return field;
    }
    
    private Component createValueControl( ValueWrapper item) {
        this.valueField = new ValueField( this.vf);
        this.grid.getEditor().getBinder().forField( valueField).bind( i -> i.getValue(), (i, t) -> i.setValue( t));

        return this.valueField;
    }
    
    private Component createSaveCancelButtons( ValueWrapper i) {
        return new DivWithPadding( new Button( VaadinIcon.CHECK.create(), this::onSaveElement), 
                                   new Button( VaadinIcon.CLOSE.create(), l -> this.grid.getEditor().cancel()));
    }
    
    private void onSaveElement( ClickEvent<Button> event) {
        this.grid.getEditor().save();
        this.grid.getDataProvider().refreshAll();
    }
    
    private void onTypeChanged( ValueChangeEvent<Value.Type> event) {
        if( event.getValue() != this.valueField.getCurrentType()) {
            this.valueField.setValue( ValueUtils.emptyValue(vf, event.getValue()));
        }
    }
    
    private void onAddElement( ClickEvent<Button> event) {
        this.array.add( new ValueWrapper( this.vf.of( "Value")));
        this.grid.getDataProvider().refreshAll();
    }
    
    private void onRemoveElement( ClickEvent<Button> event) {
        this.grid.getSelectedItems().forEach( e -> this.array.remove( e));
        this.grid.getDataProvider().refreshAll();
    }
    
    private void onMoveUp( ClickEvent<Button> event) {
        Set<ValueWrapper> selected = this.grid.getSelectedItems();
        
        if( !selected.isEmpty()) {
            ValueWrapper wrapper = selected.iterator().next();
            int index = this.array.indexOf( wrapper);
            
            if( index > 0 ) {
                swapAndRefresh( index, index - 1);
            }
        }
    }
    
    private void onMoveDown( ClickEvent<Button> event) {
        Set<ValueWrapper> selected = this.grid.getSelectedItems();
        
        if( !selected.isEmpty()) {
            ValueWrapper wrapper = selected.iterator().next();
            int index = this.array.indexOf( wrapper);
            
            if( (index != -1) && (index < (this.array.size() - 1)) ) {
                swapAndRefresh( index, index + 1);
            }
        }
    }
    
    private void swapAndRefresh( int index1, int index2) {
        ValueWrapper v1 = this.array.get( index1);
        ValueWrapper v2 = this.array.get( index2);
        
        this.array.set( index1, v2);
        this.array.set( index2, v1);
        
        this.grid.getDataProvider().refreshAll();
    }
    
    private void onCancel( ClickEvent<Button> event) {
        this.close();
    }
    
    private void onSave( ClickEvent<Button> event) {
        this.callback.accept( this.vf.of( this.array.stream().map( w -> w.getValue()).collect( Collectors.toList())));
        this.close();
    }
    
    private String getType( ValueWrapper v) {
        return v.getValue().getType().name();
    }
    
    private static final class ValueWrapper {
        
        private Value value;
        
        public ValueWrapper( Value v) {
            this.value = v;
        }
        
        public void setValue( Value v) {
            this.value = v;
        }
        
        public Value getValue() {
            return this.value;
        }
    }
}
