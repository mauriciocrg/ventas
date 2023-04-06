package com.ventas.crud;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.ventas.backend.DataService;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Usuario;
import com.ventas.ventas.samples.ResetButtonForTextField;

public class UsuarioCrudView extends CssLayout implements View {
	
	public static final String VIEW_NAME = "Usuarios";
	
	private UsuarioGrid grid;
	private UsuarioForm form;
	private TextField filter;
	 
	private UsuarioCrudLogic viewLogic = new UsuarioCrudLogic(this);
	 
	private UsuarioDataProvider dataProvider = new UsuarioDataProvider();
	
	private Button newUsuario;
	 
	public UsuarioCrudView() {
		setSizeFull();
        addStyleName("crud-view");
        
        //setCaption("<h1>Usuarios</h1>");
		//setCaptionAsHtml(true);
		
        HorizontalLayout topLayout = createTopBar();
     
	 
        grid = new UsuarioGrid();
        grid.setDataProvider(dataProvider);
        grid.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));

        form = new UsuarioForm(viewLogic);
        form.setUserTypes();
        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.setHeight("100%");
        
        
        
        barAndGridLayout.addComponent(topLayout);
        barAndGridLayout.addComponent(grid);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.setExpandRatio(grid, 1);
        barAndGridLayout.setStyleName("crud-main-layout");

        addComponent(barAndGridLayout);
        addComponent(form);

        viewLogic.init();
	 }

	 
	 public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setStyleName("filter-textfield");
        filter.setPlaceholder("Filtrar id, nombre, direccion o email");
        ResetButtonForTextField.extend(filter);
        // Apply the filter to grid's data provider. TextField value is never null
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));

        newUsuario = new Button("Nuevo Usuario");
        newUsuario.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newUsuario.setIcon(VaadinIcons.PLUS_CIRCLE);
        newUsuario.addClickListener(click -> viewLogic.newUsuario());

        HorizontalLayout topLayoutx = new HorizontalLayout();
        topLayoutx.setWidth("100%");
        topLayoutx.addComponent(filter);
        topLayoutx.addComponent(newUsuario);
        topLayoutx.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topLayoutx.setExpandRatio(filter, 1);
        
        
        Label label = new Label();
        label.setCaption("<h1>Usuarios</h1>");
        label.setCaptionAsHtml(true);
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setWidth("100%");
        vlayout.setMargin(false);
        vlayout.addComponents(label,topLayoutx);
        
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.addComponent(vlayout);
        topLayout.setStyleName("top-bar");
        
        return topLayout;
    }
	 
	 
	public void showError(String msg) {
        Notification.show(msg, Type.ERROR_MESSAGE);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg, Type.TRAY_NOTIFICATION);
    }

    public void setNewUsuarioEnabled(boolean enabled) {
        newUsuario.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Usuario row) {
        grid.getSelectionModel().select(row);
    }

    public Usuario getSelectedRow() {
        return grid.getSelectedRow();
    }

    public void updateUsuario(Usuario usuario) {
        dataProvider.save(usuario);
        // FIXME: Grid used to scroll to the updated item
    }

    public void removeUsuario(Usuario usuario) {
        dataProvider.delete(usuario);
    }

    public void editUsuario(Usuario usuario) {
        if (usuario != null) {
            form.addStyleName("visible");
            form.setEnabled(true);
        } else {
            form.removeStyleName("visible");
            form.setEnabled(false);
        }
        form.editUsuario(usuario);
    }
}
