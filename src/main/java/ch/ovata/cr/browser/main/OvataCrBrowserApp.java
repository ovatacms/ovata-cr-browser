/*
 * OvataCrBrowserApp.java
 * Created on 27.09.2020, 12:00:00
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
package ch.ovata.cr.browser.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 *
 * @author dani
 */
@SpringBootApplication
@ServletComponentScan( basePackageClasses = RepositoryLifeCycleListener.class)
public class OvataCrBrowserApp {

    public static void main(String[] args) {
        SpringApplication.run( OvataCrBrowserApp.class, args);
    }
}
