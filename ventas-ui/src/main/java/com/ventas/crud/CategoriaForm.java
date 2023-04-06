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
import com.ventas.backend.data.Categoria;
import com.ventas.backend.data.Category;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManageCategoria;
import com.ventas.backend.manageData.ManageUsuario;
import com.ventas.ventas.samples.AttributeExtension;


public class CategoriaForm extends CategoriaFormDesign {
	
	private CategoriaCrudLogic viewLogic;
	private Binder<Categoria> binder;
    private Categoria currentCategoria;
   
    private ManageCategoria manageCategoria = new ManageCategoria();
    
    public CategoriaForm(CategoriaCrudLogic categoriaCrudLogic) {
        super();
        addStyleName("product-form");
        viewLogic = categoriaCrudLogic;

        // Mark the stock count field as numeric.
        // This affects the virtual keyboard shown on mobile devices.

        
        binder = new BeanValidationBinder<>(Categoria.class);
        nombreCategoria.setRequiredIndicatorVisible(true);
        
        binder.forField(nombreCategoria).bind("nombre");
            
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors() && 
            		!nombreCategoria.getValue().isEmpty();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save.addClickListener(event -> {
            if (currentCategoria != null && binder.writeBeanIfValid(currentCategoria)) {
                viewLogic.saveCategoria(currentCategoria);
            }
        });

        discard.addClickListener(
                event -> viewLogic.editCategoria(currentCategoria));

        delete.addClickListener(event -> {
            if (currentCategoria != null) {
                viewLogic.deleteCategoria(currentCategoria);
            }
        });
    }


    public void editCategoria(Categoria categoria) {
        if (categoria == null) {
            categoria = new Categoria();
        }
        currentCategoria = categoria;
        binder.readBean(categoria);

        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId()
                + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }

}
