/*
 * $Id: ValueField.java 2881 2019-12-09 12:07:46Z dani $
 * Created on 17.05.2019, 12:00:00
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
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

/**
 *
 * @author dani
 */
@CssImport( value = "./ovata-value-field.css", themeFor = "vaadin-custom-field")
public class ValueField extends CustomField<Value> {

    private final ValueFactory vf;
    private HasValue field = new TextField();
    private Value.Type currentType;
        
    public ValueField( ValueFactory vf) {
        this.vf = vf;
        this.setWidth( "100%");
    }    

    @Override
    protected Value generateModelValue() {
        switch( currentType) {
            case STRING:
                return vf.of( ((TextField)this.field).getValue());
            case LONG:
                return vf.of( ((NumberField)this.field).getValue().longValue());
            case DOUBLE:
                return vf.of( ((NumberField)this.field).getValue());
            case BOOLEAN:
                return vf.of( ((Checkbox)this.field).getValue());
            case LOCAL_DATE:
                return vf.of( ((DatePicker)this.field).getValue());
            case ZONED_DATETIME:
                return ((ZonedDateTimeField)this.field).getValue();
            case DECIMAL:
                return vf.of( ((BigDecimalField)this.field).getValue());
            case BINARY:
                return ((BinaryField)this.field).getValue();
            case REFERENCE:
                return ((ReferenceField)this.field).getValue();
            case ARRAY:
                return ((ArrayField)this.field).getValue();
            case MAP:
                return ((MapField)this.field).getValue();
        }
        
        return null;
    }
    
    @Override
    protected void setPresentationValue(Value value) {
        if( value != null) {
            HasValue newfield = null;
            currentType = value.getType();
            
            switch( this.currentType) {
                case STRING:
                    newfield = new TextField();
                    newfield.setValue( value.getString());
                    break;
                case LONG:
                    newfield = new IntegerField();
                    newfield.setValue( value.getLong().intValue());
                    break;
                case DOUBLE:
                    newfield = new NumberField();
                    newfield.setValue( value.getDouble());
                    break;
                case DECIMAL:
                    newfield = new BigDecimalField();
                    newfield.setValue( value.getDecimal());
                    break;
                case BOOLEAN:
                    newfield = new Checkbox();
                    newfield.setValue( value.getBoolean());
                    break;
                case LOCAL_DATE:
                    newfield = new DatePicker();
                    newfield.setValue( value.getDate());
                    break;
                case ZONED_DATETIME:
                    newfield = new ZonedDateTimeField( this.vf);
                    newfield.setValue( value);
                    
                    newfield.addValueChangeListener( this::onValueChanged);
                    break;
                case BINARY:
                    newfield = new BinaryField( this.vf);
                    newfield.setValue( value);
                    
                    newfield.addValueChangeListener( this::onValueChanged);
                    break;
                case REFERENCE:
                    newfield = new ReferenceField( this.vf);
                    newfield.setValue( value);
                    
                    newfield.addValueChangeListener( this::onValueChanged);
                    break;
                case ARRAY:
                    newfield = new ArrayField( this.vf);
                    newfield.setValue( value);
                    
                    newfield.addValueChangeListener( this::onValueChanged);
                    break;
                case MAP:
                    newfield = new MapField( this.vf);
                    newfield.setValue( value);
                    
                    newfield.addValueChangeListener( this::onValueChanged);
                    break;
            }
            
            if( newfield instanceof HasSize) {
                ((HasSize)newfield).setWidth( "100%");
            }
            
            if( newfield != null) {
                swapField( (Component)newfield);
            }
        }
    }
    
    private void onValueChanged( ValueChangeEvent<Value> event) {
        this.updateValue();
    }
    
    public Value.Type getCurrentType() {
        return this.currentType;
    }
    
    private void swapField( Component field) {
        removeField();
        addField( field);
    }

    private void removeField() {
        if( this.field != null) {
            this.remove( (Component)this.field);
        }
    }
    
    private void addField( Component field) {
        this.field = (HasValue)field;
        
        this.add( field);
    }
}
