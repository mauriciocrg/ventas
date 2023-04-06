package com.ventas.crud;

import java.util.Collection;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.Page;
import com.ventas.backend.core.Cripto;
import com.ventas.backend.data.Availability;
import com.ventas.backend.data.Category;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManageUsuario;
import com.ventas.ventas.samples.AttributeExtension;


public class UsuarioForm extends UsuarioFormDesign {
	
	private UsuarioCrudLogic viewLogic;
	private Binder<Usuario> binder;
    private Usuario currentUsuario;
   
    private ManageUsuario manageUsuario = new ManageUsuario();
    
    public class PersonalEmailValidator extends EmailValidator{

		public PersonalEmailValidator(String errorMessage) {
			super(errorMessage);
			// TODO Auto-generated constructor stub
		}
    	
		@Override
        public ValidationResult apply(String value, ValueContext context) {
            if(!super.apply(value, context).isError()) {
            	Usuario usuario = manageUsuario.getUsuario(value);
            	
            	if(usuario != null && currentUsuario.getId() != usuario.getId())
            		return ValidationResult.error("Ya existe usuario con igual E-Mail.");
            	else
            		return ValidationResult.ok();
            } else {
                return super.apply(value, context);
            }
        }
    }
    
    public UsuarioForm(UsuarioCrudLogic usuairoCrudLogic) {
        super();
        addStyleName("product-form");
        viewLogic = usuairoCrudLogic;

        // Mark the stock count field as numeric.
        // This affects the virtual keyboard shown on mobile devices.
        
        Converter<String, Integer> converter = new Converter<String, Integer>() {
			@Override
			public Result<Integer> convertToModel(String value, ValueContext context) {
				// TODO Auto-generated method stub

                if ("Administrador".equals(value)) return Result.ok(1);
                else if ("Vendedor".equals(value)) return Result.ok(2) ;
                else return Result.ok(0);

			}

            @Override
            public String convertToPresentation(Integer integer, ValueContext valueContext) {
                if(integer == 1) return "Administrador";
                else if(integer == 2) return "Vendedor";
                else return "Cliente";
            }


        };
        
        Converter<String, String> passConverter = new Converter<String, String>() {
		
			@Override
			public Result<String> convertToModel(String value, ValueContext context) {
				// TODO Auto-generated method stub
				Result<String> result = Result.ok(Cripto.Encriptar(value));
				
				return result;
			}

			@Override
			public String convertToPresentation(String value, ValueContext context) {
				// TODO Auto-generated method stub
				return value;
			}
        };

        
        binder = new BeanValidationBinder<>(Usuario.class);
        nombreUsuario.setRequiredIndicatorVisible(true);
        emailUsuario.setRequiredIndicatorVisible(true);
        direccionUsuario.setRequiredIndicatorVisible(true);
        passwordUsuario.setRequiredIndicatorVisible(true);
        binder.forField(nombreUsuario).bind("nombre");
        binder.forField(emailUsuario).withValidator(new PersonalEmailValidator(emailUsuario.getValue())).bind("email");
        binder.forField(direccionUsuario).bind("direccion");
        binder.forField(rutUsuario).bind("rut");
        binder.forField(passwordUsuario).withConverter(passConverter).bind("password");
        binder.forField(tipoUsuario).withConverter(converter).bind("admin");
       
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors() && 
            		!nombreUsuario.getValue().isEmpty() && 
            		!emailUsuario.getValue().isEmpty() &&
            		!direccionUsuario.getValue().isEmpty() &&
            		!passwordUsuario.getValue().isEmpty();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save.addClickListener(event -> {
            if (currentUsuario != null && binder.writeBeanIfValid(currentUsuario)) {
                viewLogic.saveUsuario(currentUsuario);
            }
        });

        discard.addClickListener(
                event -> viewLogic.editUsuario(currentUsuario));

        delete.addClickListener(event -> {
            if (currentUsuario != null) {
                viewLogic.deleteUsuario(currentUsuario);
            }
        });
    }

    public void setUserTypes() {

        tipoUsuario.setItems("Cliente", "Administrador", "Vendedor");

    }
    public void editUsuario(Usuario usuario) {
        if (usuario == null) {
            usuario = new Usuario();
        }
        currentUsuario = usuario;
        binder.readBean(usuario);

        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId()
                + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }

}
