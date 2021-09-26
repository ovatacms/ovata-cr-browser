/*
 * $Id: MainView.java 2898 2019-12-19 15:42:42Z dani $
 * Created on 05.10.2019, 12:00:00
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
package ch.ovata.cr.browser.main;

import ch.ovata.cr.browser.acl.EditAclDialog;
import ch.ovata.cr.api.CoreNodeTypes;
import ch.ovata.cr.api.Item;
import ch.ovata.cr.api.Node;
import ch.ovata.cr.api.Property;
import ch.ovata.cr.api.Session;
import ch.ovata.cr.browser.ConfirmationDialog;
import ch.ovata.cr.browser.CreateWorkspaceDialog;
import ch.ovata.cr.browser.WorkspacesComboBox;
import ch.ovata.cr.browser.grid.ItemTreeGrid;
import ch.ovata.cr.tools.NodeUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import javax.security.auth.login.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dani
 */
@Route( "")
@StyleSheet( "/font-awesome-4.7.0/css/font-awesome.css")
public class MainView extends Main implements BeforeEnterObserver, Session.Listener, PageConfigurator {
    
    private static final Logger logger = LoggerFactory.getLogger( MainView.class);
    
    private ItemTreeGrid grid;
    private WorkspacesComboBox workspaces;
    
    private final VerticalLayout buttons = new VerticalLayout();
    private final Button btnDeleteWorkspace = new Button( VaadinIcon.TRASH.create(), this::onDeleteWorkspace);
    private final Button btnCreateWorkspace = new Button( VaadinIcon.PLUS.create(), this::onCreateWorkspace);
    private final Button btnAddNode = new Button( "Add Node", this::onAddNode);
    private final Button btnAddProperty = new Button( "Add Property", this::onAddProperty);
    private final Button btnRemove = new Button( "Remove", this::onRemove);
    private final Button btnCommit = new Button( "Commit", this::onCommit);
    private final Button btnRollback = new Button( "Rollback", this::onRollback);
    private final Button btnRefresh = new Button( "Refresh", this::onRefresh);
    private final Button btnEditAcl = new Button( "Edit Acl", this::onEditAcl);
    
    private final SessionMgr sessionMgr;
    
    public MainView( SessionMgr sessionMgr) {
        this.sessionMgr = sessionMgr;
        
        this.setSizeFull();
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addFavIcon( "icon", "ovata-icon.svg", "64x64");
    }
    
    private void initView() {
        String workspaceName = SessionMgr.getRepository().listWorkspaceNames().iterator().next();
        
        this.grid = new ItemTreeGrid( SessionMgr.getRepository().getSession( workspaceName));
        this.workspaces = new WorkspacesComboBox();
        this.workspaces.setValue( workspaceName);
        
        this.grid.setSizeFull();
        this.grid.addSelectionListener( this::onSelectionChanged);
        
        this.workspaces.setWidth( "400px");
        this.workspaces.addValueChangeListener( this::onWorkspaceChanged);
        this.workspaces.setValue( "system_users");

        this.buttons.setMargin( false);
        this.buttons.setPadding( false);
        this.buttons.setSpacing( false);
        this.buttons.setWidth( "200px");
        this.buttons.setHeight( "100%");
        
        addButtons( btnAddNode, btnAddProperty, btnRemove, btnEditAcl, btnCommit, btnRollback, btnRefresh);
        
        btnRefresh.setEnabled( true);
        
        HorizontalLayout layout = new HorizontalLayout( this.grid, this.buttons);
        layout.setSizeFull();
        
        this.btnCreateWorkspace.setThemeName( ButtonVariant.LUMO_ICON.getVariantName());
        this.btnDeleteWorkspace.setThemeName( ButtonVariant.LUMO_ICON.getVariantName());
        
        HorizontalLayout middle = new HorizontalLayout( this.workspaces, this.btnDeleteWorkspace, this.btnCreateWorkspace);
        middle.setMargin( false);
        
        VerticalLayout content = new VerticalLayout( new H3( "Ovata Content Repository"), middle, layout);
        content.setHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH, grid);
        content.setSizeFull();

        this.attachSession( this.grid.getSession());
        
        this.add( content);
    }
    
    private final LoginOverlay loginOverlay = new LoginOverlay();
    private final LoginI18n loginI18N = LoginI18n.createDefault();
    
    private void initLogin() {
        loginI18N.setAdditionalInformation("To close the login form submit non-empty username and password");
        loginOverlay.setI18n( loginI18N);
        
        loginOverlay.setTitle( "Ovata CMS");
        loginOverlay.setDescription( "Repository bluesky");

        loginOverlay.addLoginListener( this::onLogin);
        loginOverlay.setForgotPasswordButtonVisible( false);
        loginOverlay.setOpened( true);
    }

    private void onLogin( AbstractLogin.LoginEvent event) {
        LoginI18n.ErrorMessage error = new LoginI18n.ErrorMessage();

        try {
            logger.info( "Login attempt for user <{}>.", event.getUsername());
            
            this.sessionMgr.login( "sirene", event.getUsername(), event.getPassword());
            
            loginOverlay.setOpened( false);
            
            this.initView();
        }
        catch( LoginException e) {
            logger.warn( "Could not login.", e);
            
            error.setTitle( "Could not login");
            error.setMessage( "Wrong username or password supplied.");

            loginI18N.setErrorMessage( error);
        }
    }
    
    private void onWorkspaceChanged( ComboBox.ValueChangeEvent<String> event) {
        if( event.getValue() != null) {
            attachSession( SessionMgr.getRepository().getSession( event.getValue()));
        }
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent bee) {
        if( !this.sessionMgr.isLoggedIn()) {
            initLogin();
        }
        else {
            initView();
        }
    }
    
    private void onSelectionChanged( SelectionEvent<Grid<Item>, Item> event) {
        event.getFirstSelectedItem().ifPresent( i -> changeButtonState( i));
    }
    
    private void onDeleteWorkspace( ClickEvent<Button> event) {
        String workspaceName = this.workspaces.getValue();
        
        new ConfirmationDialog( "Do your really want to delete workspace <" + workspaceName + ">?", "Delete", () -> this.doDeleteWorkspace( workspaceName)).open();
    }
    
    private void doDeleteWorkspace( String name) {
        SessionMgr.getRepository().dropWorkspace( name);

        this.workspaces.refresh();
    }
    
    private void onCreateWorkspace( ClickEvent<Button> event) {
        new CreateWorkspaceDialog( this::onDoCreateWorkspace).open();
    }
    
    private void onDoCreateWorkspace( String name) {
        SessionMgr.getRepository().createWorkspace( name);
        
        this.workspaces.refresh();
    }
    
    private void onAddNode( ClickEvent<Button> event) {
        getSelectedItem().ifPresent( i -> {
            NodeUtils.addNextNode( (Node)i, "node", CoreNodeTypes.UNSTRUCTURED);
            
            this.grid.getDataProvider().refreshAll();
        });
    }
    
    private void onAddProperty( ClickEvent<Button> event) {
        getSelectedItem().ifPresent( i -> {
            addNextProperty( (Node)i);
            
            this.grid.getDataProvider().refreshAll();
        });
    }
    
    private Optional<Item> getSelectedItem() {
        Set<Item> selectedItems = this.grid.getSelectedItems();
        
        if( !selectedItems.isEmpty()) {
            return Optional.of( selectedItems.iterator().next());
        }
        
        return Optional.empty();
    }
    
    private void onRemove( ClickEvent<Button> event) {
        getSelectedItem().ifPresent( i -> {
            i.remove();
            this.grid.getDataProvider().refreshAll();
        });
    }
    
    private void onCommit( ClickEvent<Button> event) {
        this.grid.getSession().commit();
    }
    
    private void onRollback( ClickEvent<Button> event) {
        this.grid.getSession().rollback();
    }
    
    private void onRefresh( ClickEvent<Button> event) {
        this.grid.getSession().refresh();
    }
    
    private void onEditAcl( ClickEvent<Button> event) {
        getSelectedItem().ifPresent( i -> {
            editAcl( (Node)i);
        });
    }
    
    private void editAcl( Node node) {
        new EditAclDialog( node).open();
    }

    @Override
    public void onStateChange(Session.Event event) {
        logger.info( "Session state changed : " + event.getClass().getName());

        enableButtons( this.grid.getSession().isDirty(), btnCommit, btnRollback);
        this.btnRefresh.setEnabled( !this.grid.getSession().isDirty());
        
        if( !this.grid.getEditor().isOpen()) {
            this.grid.getDataProvider().refreshAll();
        }
    }

    private void enableButtons( boolean flag, Button... buttons) {
        Arrays.stream( buttons).forEach( b -> b.setEnabled( flag));
    }
    
    private void disableButtons( Button... buttons) {
        enableButtons( false, buttons);
    }
    
    private void changeButtonState( Item i) {
        
        disableButtons( btnAddNode, btnAddProperty, btnRemove, btnEditAcl);
        
        if( i instanceof Node) {
            enableButtons( true, btnAddNode, btnAddProperty, btnRemove, btnEditAcl);
        }
        
        if( i instanceof Property) {
            this.btnRemove.setEnabled( true);
        }
    }
    
    private void addButtons( Button... buttons) {
        Arrays.stream( buttons).forEach( this::addButton);
    }
    
    private void addButton( Button button) {
        button.setWidth( "100%");
        button.setEnabled( false);
        
        this.buttons.add( button);
    }
    
    private void attachSession( Session session) {
        this.grid.getSession().removeListener( this);
        this.grid.setSession( session);
        this.grid.getSession().addListener( this);

        disableButtons( btnAddNode, btnAddProperty, btnRemove, btnCommit, btnRollback);
        this.btnRefresh.setEnabled( true);
    }

    private void addNextProperty( Node node) {
        int i = 0;

        do {
            i++;
        }
        while( node.hasProperty( "property" + i));

        node.setProperty( "property" + i, this.grid.getSession().getValueFactory().of( "Value"));
    }
}
