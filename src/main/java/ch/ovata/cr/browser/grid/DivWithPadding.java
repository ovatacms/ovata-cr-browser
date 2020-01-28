/*
 * $Id: DivWithPadding.java 2898 2019-12-19 15:42:42Z dani $
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
package ch.ovata.cr.browser.grid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

/**
 *
 * @author isc-has
 */
@CssImport( value = "./ovata-div-with-padding.css")
public class DivWithPadding extends Div {

    public DivWithPadding( Component... components) {
        super( components);
        
        this.setClassName( "ovata-with-padding");
    }
}
