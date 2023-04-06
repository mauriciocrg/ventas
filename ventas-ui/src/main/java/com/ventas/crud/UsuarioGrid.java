package com.ventas.crud;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.ventas.backend.data.Availability;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Usuario;

public class UsuarioGrid extends Grid<Usuario> {

	public UsuarioGrid() {
		setSizeFull();
		addColumn(Usuario::getId, new NumberRenderer()).setCaption("Id");
        addColumn(Usuario::getNombre).setCaption("Nombre");
        addColumn(Usuario::getEmail).setCaption("E-Mail");
        addColumn(Usuario::getDireccion).setCaption("Dirección");
        addColumn(Usuario::getRut).setCaption("RUT");
        addColumn(usuario -> {
          if(usuario.getAdmin() == 1) {
              return "Administrador";
          } else if(usuario.getAdmin() == 2) {
              return "Vendedor";
          } else {
              return "Cliente";
          }
        }).setCaption("Tipo de Usuario");
	}
	
	private String htmlIsAdmin(Usuario usuario) {
        
		if (usuario.getAdmin() == 1) return "<span class=\"v-icon\" style=\"font-family: "
                + VaadinIcons.CIRCLE.getFontFamily() + "\">&#x"
                + Integer.toHexString(VaadinIcons.CHECK.getCodepoint())
                + ";</span>";
        else return "<span class=\"v-icon\" style=\"font-family: "
                + VaadinIcons.CIRCLE.getFontFamily() + "\">&#x"
                + Integer.toHexString(VaadinIcons.CLOSE.getCodepoint())
                + ";</span>";
    }
	
	public Usuario getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Usuario usuario) {
        getDataCommunicator().refresh(usuario);
    }
}
