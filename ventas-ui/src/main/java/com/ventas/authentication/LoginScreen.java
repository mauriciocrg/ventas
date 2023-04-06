package com.ventas.authentication;

import java.io.Serializable;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.ventas.backend.core.Cripto;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManageUsuario;

/**
 * UI content when the user is not logged in yet.
 */
public class LoginScreen extends CssLayout {

    private TextField email;
    private TextField nombreUsuario;
    private TextField direccionUsuario;
    private TextField rutUsuario;
    private PasswordField password;
    private PasswordField passwordConfirm;
    private Button login;
    private Button loginLink;
    private Button registrar;
    private Button registrarLink;
    private LoginListener loginListener;
    private AccessControl accessControl;
    
    private ManageUsuario manageUsuario = new ManageUsuario();

    public LoginScreen(AccessControl accessControl, LoginListener loginListener) {
        this.loginListener = loginListener;
        this.accessControl = accessControl;
        buildUI();
        email.focus();
    }

    private void buildUI() {
        addStyleName("login-screen");

        // login form, centered in the available part of the screen
        Component loginForm = buildLoginForm();

        // layout to center login form when there is sufficient screen space
        // - see the theme for how this is made responsive for various screen
        // sizes
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setMargin(false);
        centeringLayout.setSpacing(false);
        centeringLayout.setStyleName("centering-layout");
        centeringLayout.addComponent(loginForm);
        centeringLayout.setComponentAlignment(loginForm,
                Alignment.MIDDLE_CENTER);

        // information text about logging in
        CssLayout loginInformation = buildLoginInformation();

        addComponent(centeringLayout);
        addComponent(loginInformation);
    }

    private Component buildLoginForm() {
        FormLayout loginForm = new FormLayout();

        loginForm.addStyleName("login-form");
        loginForm.setSizeUndefined();
        loginForm.setMargin(false);

        loginForm.addComponent(email = new TextField("E-Mail"));
        email.setRequiredIndicatorVisible(true);
        email.setWidth(15, Unit.EM);
        
        nombreUsuario = new TextField("Nombre de Usuario");
        nombreUsuario.setRequiredIndicatorVisible(true);
        nombreUsuario.setVisible(false);
        nombreUsuario.setWidth(15, Unit.EM);
        loginForm.addComponent(nombreUsuario);
        
        direccionUsuario = new TextField("Dirección");
        direccionUsuario.setRequiredIndicatorVisible(true);
        direccionUsuario.setVisible(false);
        direccionUsuario.setWidth(15, Unit.EM);
        loginForm.addComponent(direccionUsuario);

        rutUsuario = new TextField("RUT");
        rutUsuario.setRequiredIndicatorVisible(false);
        rutUsuario.setVisible(false);
        rutUsuario.setWidth(15, Unit.EM);
        loginForm.addComponent(rutUsuario);

        loginForm.addComponent(password = new PasswordField("Password"));
        password.setRequiredIndicatorVisible(true);
        password.setWidth(15, Unit.EM);
        
        passwordConfirm = new PasswordField("Confirmar Password");
        passwordConfirm.setRequiredIndicatorVisible(true);
        passwordConfirm.setVisible(false);
        passwordConfirm.setWidth(15, Unit.EM);
        loginForm.addComponent(passwordConfirm);
        
        
        CssLayout buttons = new CssLayout();
        buttons.setStyleName("buttons");
        loginForm.addComponent(buttons);

        buttons.addComponent(login = new Button("Login"));
        login.setDisableOnClick(true);
        login.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    login();
                } finally {
                    login.setEnabled(true);
                }
            }
        });
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        
        registrar = new Button("Registrarse");
        //registrar.setDisableOnClick(true);
        registrar.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        registrar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        registrar.setVisible(false);
        registrar.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
            	register();
            }
        });
        
        buttons.addComponent(registrar);
        
        buttons.addComponent(registrarLink = new Button("Registrese."));
        registrarLink.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
            	nombreUsuario.setVisible(true);
            	direccionUsuario.setVisible(true);
            	rutUsuario.setVisible(true);
            	passwordConfirm.setVisible(true);
            	registrar.setVisible(true);
            	loginLink.setVisible(true);
            	login.setVisible(false);
            	registrarLink.setVisible(false);
            }
        });
        registrarLink.addStyleName(ValoTheme.BUTTON_LINK);
        
        loginLink = new Button("Login.");
        loginLink.setVisible(false);
        buttons.addComponent(loginLink);
        loginLink.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
            	nombreUsuario.setVisible(false);
            	direccionUsuario.setVisible(false);
            	rutUsuario.setVisible(false);
            	passwordConfirm.setVisible(false);
            	registrar.setVisible(false);
            	loginLink.setVisible(false);
            	login.setVisible(true);
            	registrarLink.setVisible(true);
            }
        });
        loginLink.addStyleName(ValoTheme.BUTTON_LINK);
        
        return loginForm;
    }

    private CssLayout buildLoginInformation() {
        CssLayout loginInformation = new CssLayout();
        loginInformation.setStyleName("login-information");
        Label loginInfoText = new Label(
                "<h1>Sistema de Ventas 1.0</h1>"
                        + "Si no cuenta con un usuario puede registrarse, y así poder acceder al catalogo de productos para realizar compras.",
                ContentMode.HTML);
        loginInfoText.setSizeFull();
        loginInformation.addComponent(loginInfoText);
        return loginInformation;
    }

    private void login() {
        if (accessControl.signIn(email.getValue(), password.getValue())) {
            loginListener.loginSuccessful();
        } else {
            showNotification(new Notification("Fallo al logearse",
                    "Pro favor chquee su E-Mail o su Password.",
                    Notification.Type.HUMANIZED_MESSAGE));
            email.focus();
        }
    }
    
    private boolean validate() {
    	if(email.getValue().isEmpty()) {
    		showNotification(new Notification("Advertencia","El E-Mail no puede ser vacio.",Notification.Type.HUMANIZED_MESSAGE));
    		return false;
    	}
    	if(nombreUsuario.getValue().isEmpty()) {
    		showNotification(new Notification("Advertencia","El Nombre del Usuario no puede ser vacio.",Notification.Type.HUMANIZED_MESSAGE));
    		return false;
    	}
    	if(direccionUsuario.getValue().isEmpty()) {
    		showNotification(new Notification("Advertencia","La Dirección del Usuario no puede ser vacio.",Notification.Type.HUMANIZED_MESSAGE));
    		return false;
    	}
    	if(password.getValue().isEmpty()) {
    		showNotification(new Notification("Advertencia","El Password del Usuario no puede ser vacio.",Notification.Type.HUMANIZED_MESSAGE));
    		return false;
    	}
    	if(!password.getValue().equals(passwordConfirm.getValue())) {
    		showNotification(new Notification("Advertencia","El password y su confirmación deben coincidir.",Notification.Type.HUMANIZED_MESSAGE));
    		return false;
    	}
    	if(manageUsuario.getUsuario(email.getValue()) != null) {
    		showNotification(new Notification("Advertencia","Ya existe un usuario registrado con el mismo E-Mail.",Notification.Type.HUMANIZED_MESSAGE));
    		return false;
    	}
    	return true;
    }
    
    
    private void register() {
    	if(validate()) {
    		manageUsuario.saveUsuario(new Usuario(nombreUsuario.getValue(),email.getValue(),direccionUsuario.getValue(),Cripto.Encriptar(password.getValue()),0,rutUsuario.getValue()));
    		login();
    	}
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }

    public interface LoginListener extends Serializable {
        void loginSuccessful();
    }
}
