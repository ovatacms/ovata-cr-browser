/*
 * $Id: NodeValueProvider.java 2843 2019-11-22 09:31:33Z dani $
 * Created on 04.10.2019, 12:00:00
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
import ch.ovata.cr.api.Property;
import ch.ovata.cr.api.Value;
import com.vaadin.flow.function.ValueProvider;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 *
 * @author dani
 */
public class NodeValueProvider implements ValueProvider<Node, String> {

    private final String propertyName;

    public NodeValueProvider( String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String apply( Node source) {
        Value v = source.findProperty( this.propertyName).map( Property::getValue).orElse( null);

        if( v == null) {
            return "-";
        }

        switch( v.getType()) {
            case STRING:
                return v.getString();
            case BOOLEAN:
                return Boolean.toString( v.getBoolean());
            case LONG:
                return Long.toString( v.getLong());
            case DOUBLE:
                return Double.toString( v.getDouble());
            case DECIMAL:
                return v.getDecimal().toPlainString();
            case ZONED_DATETIME:
                return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.GERMAN).format( v.getTime());
            case LOCAL_DATE:
                return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale( Locale.GERMAN).format( v.getDate());
            default:
                return "-";
        }
    }
}
