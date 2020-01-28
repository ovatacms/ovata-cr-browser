/*
 * $Id: ValueUtils.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 19.12.2019, 12:00:00
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
package ch.ovata.cr.browser.utils;

import ch.ovata.cr.api.Value;
import ch.ovata.cr.api.ValueFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author isc-has
 */
public final class ValueUtils {
    
    private ValueUtils() {
    }
    
    public static String toString( Value value) {
        switch( value.getType()) {
            case STRING:
                return value.getString();
            case LONG:
                return Long.toString( value.getLong());
            case DOUBLE:
                return Double.toString( value.getDouble());
            case DECIMAL:
                return value.getDecimal().toPlainString();
            case BOOLEAN:
                return Boolean.toString( value.getBoolean());
            case BINARY:
                return value.getBinary().getFilename() + " / " + value.getBinary().getContentType();
            case ZONED_DATETIME:
                return value.getTime().format( DateTimeFormatter.ISO_DATE_TIME);
            case LOCAL_DATE:
                return value.getDate().format( DateTimeFormatter.ISO_DATE);
            case REFERENCE:
                return value.getReference().getWorkspace() + ":" + value.getReference().getPath();
            case MAP:
                return "<Map>";
            case ARRAY:
                return "<Array>";
        }
        
        return "";
    }
    
    public static Value emptyValue( ValueFactory vf, Value.Type type) {
        switch( type) {
            case STRING:
                return vf.of( "");
            case LONG:
                return vf.of( 0l);
            case DOUBLE:
                return vf.of( 0.0d);
            case BOOLEAN:
                return vf.of( false);
            case LOCAL_DATE:
                return vf.of( LocalDate.now());
            case ZONED_DATETIME:
                return vf.of( ZonedDateTime.now());
            case DECIMAL:
                return vf.of( new BigDecimal( "0.0"));
            case BINARY:
                return vf.binary( new byte[0], "noname.txt", "text/plain");
            case REFERENCE:
                return vf.referenceByPath( vf.getSession().getWorkspace().getName(), "/");
            case MAP:
                return vf.of( new HashMap<>());
            case ARRAY:
                return vf.of( new ArrayList<>());
            default:
                return vf.of( "");
        }    
    }
}
