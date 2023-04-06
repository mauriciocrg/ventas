package com.ventas.crud;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.ventas.backend.data.Availability;
import com.ventas.backend.data.Categoria;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Usuario;

public class CategoriaGrid extends Grid<Categoria> {

	public CategoriaGrid() {
		setSizeFull();
		addColumn(Categoria::getId, new NumberRenderer()).setCaption("Id");
        addColumn(Categoria::getNombre).setCaption("Nombre");
	}
	
	public Categoria getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Categoria categoria) {
        getDataCommunicator().refresh(categoria);
    }
}
