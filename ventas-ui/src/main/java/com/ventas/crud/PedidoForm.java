package com.ventas.crud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.ventas.backend.data.Pedido;
import com.ventas.backend.data.Producto;
import com.ventas.backend.data.Usuario;
import com.ventas.crud.UsuarioForm.PersonalEmailValidator;
import com.ventas.pdf.GeneratePDFTicket;

public class PedidoForm extends PedidoFormDesign {
	
	private Binder<Pedido> pedidoBinder;
	private Binder<Usuario> usuarioBinder;
	
	private Pedido currentPedido;
	
	private BrowserWindowOpener opener;
	
	private PedidoCrudLogic viewLogic;
	
	private static class IntegerIdConverter extends StringToIntegerConverter {

        public IntegerIdConverter() {
            super("No se puede convertir el valor a " + Integer.class.getName());
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            // do not use a thousands separator, as HTML5 input type
            // number expects a fixed wire/DOM number format regardless
            // of how the browser presents it to the user (which could
            // depend on the browser locale)
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }

        @Override
        public Result<Integer> convertToModel(String value,
                ValueContext context) {
            Result<Integer> result = super.convertToModel(value, context);
            return result.map(id -> id == null ? 0 : id);
        }

    }
    
	
	public PedidoForm(PedidoCrudLogic viewLogic) {
		super();
        addStyleName("product-form");
        this.viewLogic = viewLogic;
        
        pedidoBinder = new BeanValidationBinder<>(Pedido.class);
        usuarioBinder = new BeanValidationBinder<>(Usuario.class);
        
        Converter<Boolean, Integer> converter = new Converter<Boolean, Integer>() {
			@Override
			public Result<Integer> convertToModel(Boolean value, ValueContext context) {
				// TODO Auto-generated method stub
				Result<Integer> result =  Result.ok(value ? 1 : 0);
				
				return result;
			}

			@Override
			public Boolean convertToPresentation(Integer value, ValueContext context) {
				// TODO Auto-generated method stub
				return value != 0;
			}
        };
        
        id.setReadOnly(true);
        fecha.setReadOnly(true);
        nombreUsuario.setReadOnly(true);
        emailUsuario.setReadOnly(true);
        direccionUsuario.setReadOnly(true);
        pedidoBinder.forField(id).withConverter(new IntegerIdConverter()).bind("id");
        pedidoBinder.forField(fecha).withConverter(new LocalDateToDateConverter()).bind("fecha");
        pedidoBinder.forField(estado).withConverter(converter).bind("estado");
        
        
        
        usuarioBinder.forField(nombreUsuario).bind("nombre");
        usuarioBinder.forField(emailUsuario).bind("email");
        usuarioBinder.forField(direccionUsuario).bind("direccion");
        
        pedidoBinder.bindInstanceFields(this);
        
        pedidoBinder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = pedidoBinder.hasChanges();
            
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });
        
        
        File file = new GeneratePDFTicket(currentPedido).getFile();
    	
    	StreamSource source = new StreamSource() {
    		public InputStream getStream() {
    			try {
					return  new FileInputStream(file.getAbsoluteFile());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
    		}	
    	};
    	      
        StreamResource resource = new StreamResource(source, file.getName());

        resource.setMIMEType("application/pdf");
        resource.getStream().setParameter(
                "Content-Disposition",
                "attachment; filename="+file.getName());

        
        opener = new BrowserWindowOpener(resource);
        	
        opener.extend(print);
        
        save.addClickListener(event -> {
            if (currentPedido != null
                    && pedidoBinder.writeBeanIfValid(currentPedido)) {
                viewLogic.savePedido(currentPedido);
            }
        });

        discard.addClickListener(
                event -> viewLogic.editPedido(currentPedido));

        cancel.addClickListener(event -> viewLogic.cancelPedido());
	}
	
	public void updateResource() {
		File file = new GeneratePDFTicket(currentPedido).getFile();
    	
    	StreamSource source = new StreamSource() {
    		public InputStream getStream() {
    			try {
					return  new FileInputStream(file.getAbsoluteFile());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
    		}	
    	};
    	
        StreamResource resource = new StreamResource(source, file.getName());

        resource.setMIMEType("application/pdf");
        resource.getStream().setParameter(
                "Content-Disposition",
                "attachment; filename="+file.getName());
      
        opener.setResource(resource);
	}
	
	
	public void editPedido(Pedido pedido) {
		currentPedido = pedido;
		
		id.setReadOnly(false);
        fecha.setReadOnly(false);
        nombreUsuario.setReadOnly(false);
        emailUsuario.setReadOnly(false);
        direccionUsuario.setReadOnly(false);
        
        pedidoBinder.readBean(pedido);
        usuarioBinder.readBean(pedido.getUsuario());
        
        id.setReadOnly(true);
        fecha.setReadOnly(true);
        nombreUsuario.setReadOnly(true);
        emailUsuario.setReadOnly(true);
        direccionUsuario.setReadOnly(true);
        
        updateResource();
        
        String scrollScript = "window.document.getElementById('" + getId()
        + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
	}
}
