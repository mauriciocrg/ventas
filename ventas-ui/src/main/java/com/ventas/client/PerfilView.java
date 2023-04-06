package com.ventas.client;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.ventas.VentasUI;
import com.ventas.backend.core.Cripto;
import com.ventas.backend.manageData.ManageUsuario;

public class PerfilView extends CssLayout implements View {

	public static final String VIEW_NAME = "Perfil";
	
	private ManageUsuario manageUsuario = new ManageUsuario();
	
	private FormLayout formLayout;
	private VerticalLayout contentLayout;
	private VerticalLayout headerLayout;
	
	private TextField email;
    private TextField nombreUsuario;
    private TextField direccionUsuario;
	private TextField rutUsuario;
    private PasswordField passwordActual;
    private PasswordField password;
    private PasswordField passwordConfirm;
    
    private Button save;
	
	public PerfilView() {
		setWidth(100, Unit.PERCENTAGE);
		setHeight(100, Unit.PERCENTAGE);
		addComponent(getContentLayout());
	}
	
	private VerticalLayout getContentLayout() {
		if(contentLayout == null) {
			contentLayout = new VerticalLayout();
			contentLayout.setWidth(100, Unit.PERCENTAGE);
			contentLayout.setHeight(100, Unit.PERCENTAGE);
			contentLayout.setSpacing(false);
			contentLayout.setMargin(true);
			contentLayout.addComponents(getHeaderLayout(), getFormLayout());
			//contentLayout.setExpandRatio(getHeaderLayout(), 0.15f);
			contentLayout.setExpandRatio(getFormLayout(), 1);
		}
		return contentLayout;
	}
	
	private VerticalLayout getHeaderLayout() {
		if(headerLayout == null) {
			headerLayout = new VerticalLayout();
			headerLayout.setWidth(100, Unit.PERCENTAGE);
			headerLayout.setHeight(120, Unit.PIXELS);
			
			Label label = new Label();
			label.setCaption("<h1>Perfil</h1>");
			label.setCaptionAsHtml(true);
			
			Label usrLabel = new Label();
			usrLabel.setCaption("Bienvenido "+VentasUI.get().getAccessControl().getPrincipalName()+"!");
			
			HorizontalLayout labelsLayout = new HorizontalLayout();
			labelsLayout.setWidth(100, Unit.PERCENTAGE);
			labelsLayout.setMargin(false);
			labelsLayout.setSpacing(true);
			labelsLayout.addComponents(label,usrLabel);
			labelsLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
			labelsLayout.setComponentAlignment(usrLabel, Alignment.MIDDLE_RIGHT);
			
			headerLayout.setSpacing(true);
			headerLayout.setMargin(false);
			
			headerLayout.addComponents(labelsLayout);
		}
		return headerLayout;
	}
	
	private FormLayout getFormLayout() {
		if(formLayout == null) {
			formLayout = new FormLayout();
			formLayout.setWidth(100, Unit.PERCENTAGE);
			formLayout.setHeight(100, Unit.PERCENTAGE);
			
			formLayout.addStyleName("login-form");
			//formLayout.setSizeUndefined();
			formLayout.setMargin(false);

			formLayout.addComponent(email = new TextField("E-Mail"));
	        email.setRequiredIndicatorVisible(true);
	        email.setValue(VentasUI.get().getAccessControl().getCurrentUser().getEmail());
	        email.setWidth(100, Unit.PERCENTAGE);
	        
	        nombreUsuario = new TextField("Nombre de Usuario");
	        nombreUsuario.setRequiredIndicatorVisible(true);
	        nombreUsuario.setValue(VentasUI.get().getAccessControl().getCurrentUser().getNombre());
	        nombreUsuario.setWidth(100, Unit.PERCENTAGE);
	        formLayout.addComponent(nombreUsuario);
	        
	        direccionUsuario = new TextField("Dirección");
	        direccionUsuario.setRequiredIndicatorVisible(true);
	        direccionUsuario.setValue(VentasUI.get().getAccessControl().getCurrentUser().getDireccion());
	        direccionUsuario.setWidth(100, Unit.PERCENTAGE);
	        formLayout.addComponent(direccionUsuario);

			rutUsuario = new TextField("RUT");
			rutUsuario.setRequiredIndicatorVisible(false);
			rutUsuario.setValue(VentasUI.get().getAccessControl().getCurrentUser().getRut());
			rutUsuario.setWidth(100, Unit.PERCENTAGE);
			formLayout.addComponent(rutUsuario);
	        
	        formLayout.addComponent(passwordActual = new PasswordField("Password Actual"));
	        passwordActual.setRequiredIndicatorVisible(true);
	        passwordActual.setWidth(100, Unit.PERCENTAGE);
	        
	        formLayout.addComponent(password = new PasswordField("Password"));
	        password.setRequiredIndicatorVisible(true);
	        password.setWidth(100, Unit.PERCENTAGE);
	        
	        passwordConfirm = new PasswordField("Confirmar Password");
	        passwordConfirm.setRequiredIndicatorVisible(true);

	        passwordConfirm.setWidth(100, Unit.PERCENTAGE);
	        formLayout.addComponent(passwordConfirm);
	        
	        
	        CssLayout buttons = new CssLayout();
	        buttons.setStyleName("buttons");
	        formLayout.addComponent(buttons);

	        buttons.addComponent(save = new Button("Guardar"));
	        //save.setDisableOnClick(true);
	        save.addClickListener(new Button.ClickListener() {
	            public void buttonClick(Button.ClickEvent event) {
	            	if(validate()) {
	            		VentasUI.get().getAccessControl().getCurrentUser().setDireccion(direccionUsuario.getValue());
	            		VentasUI.get().getAccessControl().getCurrentUser().setEmail(email.getValue());
	            		VentasUI.get().getAccessControl().getCurrentUser().setPassword(Cripto.getInstance().Encriptar(password.getValue()));
	            		VentasUI.get().getAccessControl().getCurrentUser().setNombre(nombreUsuario.getValue());
	            		manageUsuario.updateUsuario(VentasUI.get().getAccessControl().getCurrentUser());
	            		
	            		showNotification(new Notification("Su perfil ha sido actualizado.",Notification.Type.HUMANIZED_MESSAGE));
	            	}
	            }
	        });

		}
		return formLayout;
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
    	if(!passwordActual.getValue().equals(Cripto.getInstance().Desencriptar(VentasUI.get().getAccessControl().getCurrentUser().getPassword()))) {
    		if(password.getValue().isEmpty()) {
        		showNotification(new Notification("Advertencia","El Password Actual es incorrecto.",Notification.Type.HUMANIZED_MESSAGE));
        		return false;
        	}
    	}
    	if(password.getValue().isEmpty()) {
    		showNotification(new Notification("Advertencia","El Password del Usuario no puede ser vacio.",Notification.Type.HUMANIZED_MESSAGE));
    		return false;
    	}
    	if(!password.getValue().equals(passwordConfirm.getValue())) {
    		showNotification(new Notification("Advertencia","El password y su confirmación deben coincidir.",Notification.Type.HUMANIZED_MESSAGE));
    		return false;
    	}
    	if(manageUsuario.getUsuario(email.getValue()).getId() != VentasUI.get().getAccessControl().getCurrentUser().getId()) {
    		showNotification(new Notification("Advertencia","Ya existe otro usuario registrado con el mismo E-Mail.",Notification.Type.HUMANIZED_MESSAGE));
    		return false;
    	}
    	return true;
    }
	
	private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }

}
