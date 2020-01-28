/*
 * $Id: ZonedDateTimeField.java 2898 2019-12-19 15:42:42Z dani $
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

import ch.ovata.cr.api.Value;
import ch.ovata.cr.api.ValueFactory;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.timepicker.TimePicker;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * @author dani
 */
public class ZonedDateTimeField extends CustomField<Value> {

    private final ValueFactory vf;
    private final DatePicker datePicker = new DatePicker();
    private final TimePicker timePicker = new TimePicker();
    
    public ZonedDateTimeField( ValueFactory vf) {
        this.vf = vf;
        
        datePicker.addValueChangeListener( this::onValueChanged);
        timePicker.addValueChangeListener( this::onValueChanged);
        
        datePicker.setWidth( "49%");
        timePicker.setWidth( "49%");
        
        this.add( new DivWithPadding( this.datePicker, this.timePicker));
    }
    
    private void onValueChanged( ValueChangeEvent event) {
        this.updateValue();
    }
    
    @Override
    protected Value generateModelValue() {
        LocalDate date = this.datePicker.getValue();
        LocalTime time = this.timePicker.getValue();
        
        if( (date != null) && (time != null)) {
            ZonedDateTime zdt = ZonedDateTime.of(date, time, ZoneId.systemDefault());

            return vf.of( zdt);
        }
        else {
            return vf.of( ZonedDateTime.now());
        }
    }

    @Override
    protected void setPresentationValue(Value newPresentationValue) {
        ZonedDateTime zdt = newPresentationValue.getTime();
        
        this.datePicker.setValue( zdt.toLocalDate());
        this.timePicker.setValue( zdt.toLocalTime());
    }
}
