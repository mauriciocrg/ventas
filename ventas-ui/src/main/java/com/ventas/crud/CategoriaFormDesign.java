package com.ventas.crud;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.declarative.Design;
import com.ventas.backend.core.Cripto;

@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class CategoriaFormDesign extends CssLayout {
	protected TextField nombreCategoria;
	protected Button save;
    protected Button discard;
    protected Button cancel;
    protected Button delete;

	
	public CategoriaFormDesign() {
        Design.read(this);
        
        /*passwordUsuario.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				passwordUsuario.setValue(Cripto.Encriptar(passwordUsuario.getValue()));
			}
        });*/
    }
}
