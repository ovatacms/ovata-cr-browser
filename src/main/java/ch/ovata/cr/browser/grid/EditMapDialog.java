/*
 * $Id: EditMapDialog.java 2898 2019-12-19 15:42:42Z dani $
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author dani
 */
@CssImport( value = "./ovata-dialog.css", themeFor = "vaadin-dialog-overlay")
public class EditMapDialog extends Dialog {
    
    private final ValueFactory vf;
    private final Grid<Entry> grid = new Grid<>();
    private final Consumer<Value> callback;
    private final List<Entry> entries;
    private ValueField valueField;

    public EditMapDialog( ValueFactory vf, Value v, Consumer<Value> callback) {
        this.vf = vf;
        this.callback = callback;
        this.entries = v.getMap().entrySet().stream().map( e -> new Entry( e.getKey(), e.getValue())).collect( Collectors.toList());

        this.setWidth( "1000px");
        
        H3 title = new H3( "Edit Map");
        title.setWidth( "100%");
        
        ListDataProvider<Entry> ldp = new ListDataProvider( this.entries);
        ldp.setSortComparator( (c1, c2) -> c1.getName().compareTo( c2.getName()));
        
        this.grid.addColumn(  e -> e.getName()).setHeader( "Name").setEditorComponent( this::createNameControl);
        this.grid.addColumn( e -> e.getValue().getType().name()).setHeader( "Type").setEditorComponent( this::createTypeControl);
        this.grid.addColumn( e -> ValueUtils.toString( e.getValue())).setHeader( "Value").setEditorComponent( this::createValueControl);
        this.grid.addColumn( i -> "").setEditorComponent( this::createSaveCancelButtons).setWidth( "100px");
        this.grid.setWidth( "100%");
        this.grid.setHeight( "500px");
        this.grid.setDataProvider( ldp);
        this.grid.addItemDoubleClickListener( this::startEditing);
        
        Button btnCancel = new Button( "Cancel", this::onCancel);
        Button btnSave = new Button( "Save", this::onSave);
    
        btnCancel.setThemeName( ButtonVariant.LUMO_TERTIARY.getVariantName());
        btnSave.setThemeName( ButtonVariant.LUMO_PRIMARY.getVariantName());
        
        HorizontalLayout buttons = new HorizontalLayout( btnCancel, btnSave);
        buttons.setWidth( "100%");
        buttons.setJustifyContentMode( FlexComponent.JustifyContentMode.END);
        buttons.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        
        VerticalLayout vbuttons = new VerticalLayout( new Button( VaadinIcon.PLUS.create(), this::onAddEntry), 
                                                      new Button( VaadinIcon.MINUS.create(), this::onRemoveEntry));
        HorizontalLayout inner = new HorizontalLayout( this.grid, vbuttons);
        
        vbuttons.setHeight( "100%");
        vbuttons.setMargin( false);
        vbuttons.setPadding( false);
        vbuttons.setWidth( "50px");
        inner.setWidth( "100%");
        
        this.add( new VerticalLayout( title, inner, buttons));
    }
    
    private void onAddEntry( ClickEvent<Button> event) {
        this.entries.add( new Entry( nextEntryName(), this.vf.of( "Value")));
        this.grid.getDataProvider().refreshAll();
    }
    
    private String nextEntryName() {
        int i = 0;
        
        while( true) {
            String name = "property" + i;

            if( !containsKey( name)) {
                return name;
            }
            
            i++;
        }
    }
    
    private boolean containsKey( String key) {
        return this.entries.stream().anyMatch( e -> e.getName().equals( key));
    }
    
    private void startEditing( ItemDoubleClickEvent<Entry> e) {
        Editor<Entry> editor = this.grid.getEditor();
        Binder<Entry> binder = new Binder();

        editor.setBinder( binder);
        editor.setBuffered( true);
        editor.editItem( e.getItem());
    }
    
    private void onRemoveEntry( ClickEvent<Button> event) {
        Set<Entry> selected = this.grid.getSelectedItems();
        
        selected.stream().forEach( e -> this.entries.remove( e));
        
        this.grid.getDataProvider().refreshAll();
    }

    private void onCancel( ClickEvent<Button> event) {
        this.close();
    }
    
    private void onSave( ClickEvent<Button> event) {
        Map<String, Value> map = new HashMap<>();
        
        this.entries.stream().forEach( e -> map.put( e.getName(), e.getValue()));
        callback.accept( this.vf.of( map));

        this.close();
    }
    private Component createNameControl( Entry entry) {
        TextField field = new TextField100();
        
        this.grid.getEditor().getBinder().forField( field).bind( Entry::getName, Entry::setName);
        
        return field;
    }
    
    private Component createTypeControl( Entry entry) {
        ValueTypeComboBox field = new ValueTypeComboBox( this::onTypeChanged);

        this.grid.getEditor().getBinder().forField( field).bind( e -> e.getValue().getType(), (i, t) -> {});

        return field;
    }
    
    private void onTypeChanged( ValueChangeEvent<Value.Type> event) {
        if( event.getValue() != this.valueField.getCurrentType()) {
            this.valueField.setValue( ValueUtils.emptyValue(vf, event.getValue()));
        }
    }
    
    private Component createValueControl( Entry entry) {
        this.valueField = new ValueField( this.vf);

        this.grid.getEditor().getBinder().forField( valueField).bind( i -> i.getValue(), (i, t) -> i.setValue( t));

        return this.valueField;
    }
    
    private Component createSaveCancelButtons( Entry i) {
        Button save = new Button( VaadinIcon.CHECK.create(), this::onSaveElement);
        Button cancel = new Button( VaadinIcon.CLOSE.create(), l -> this.grid.getEditor().cancel());
        
        return new DivWithPadding( save, cancel);
    }
    
    private void onSaveElement( ClickEvent<Button> event) {
        this.grid.getEditor().save();
        this.grid.getDataProvider().refreshAll();
    }
    
    private static class Entry {
        
        private String name;
        private Value value;
        
        public Entry( String name, Value v) {
            this.name = name;
            this.value = v;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }
    }
}
