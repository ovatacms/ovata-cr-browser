/*
 * $Id: BinaryField.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 04.12.2019, 12:00:00
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

import ch.ovata.cr.api.Binary;
import ch.ovata.cr.api.Value;
import ch.ovata.cr.api.ValueFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.FinishedEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;

/**
 *
 * @author dani
 */
public class BinaryField extends CustomField<Value> {

    private final ValueFactory vf;
    private Value binaryValue;

    private final MemoryBuffer uploadBuffer = new MemoryBuffer();
    private final Upload upload = new Upload();
    private final Div downloadWrapper = new Div();
    private final Label filename = new Label();
    private final Label mimeType = new Label();
    private final Label filesize = new Label();

    public BinaryField(ValueFactory vf) {
        this.vf = vf;

        upload.setUploadButton( new Button( VaadinIcon.UPLOAD.create()));
        upload.setAutoUpload( true);
        upload.setReceiver( uploadBuffer);
        upload.setDropAllowed( false);
        upload.addFinishedListener(this::onFinishedUpload);

        HorizontalLayout content = new HorizontalLayout( upload, downloadWrapper, filename, mimeType, filesize);

        content.setMargin( false);
        content.setPadding( false);

        this.add( content);
    }
    
    @Override
    protected Value generateModelValue() {
        return this.binaryValue;
    }

    @Override
    protected void setPresentationValue(Value value) {
        this.binaryValue = value;

        updateDownloadLink( binaryValue.getBinary());
    }

    private StreamResource getStreamResource(Binary binary) {
        StreamResource sr = new StreamResource(binary.getFilename(), () -> binary.getInputStream());
        
        sr.setContentType( binary.getContentType());
        
        return sr;
    }

    private void onFinishedUpload(FinishedEvent event) {
        binaryValue = this.vf.binary( uploadBuffer.getInputStream(), event.getFileName(), event.getMIMEType());

        updateDownloadLink( binaryValue.getBinary());
        
        this.updateValue();
    }
    
    private void updateDownloadLink( Binary binary) {
        this.filename.setText( binary.getFilename());
        this.mimeType.setText( binary.getContentType());
        this.filesize.setText( Long.toString( binary.getLength()));
        
        downloadWrapper.removeAll();

        Button button = new Button( VaadinIcon.DOWNLOAD.create());
        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper( getStreamResource( binary));
        buttonWrapper.wrapComponent(button);
        
        downloadWrapper.add( buttonWrapper);
    }
}
