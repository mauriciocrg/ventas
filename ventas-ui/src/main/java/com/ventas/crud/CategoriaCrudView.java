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
import com.ventas.backend.data.Categoria;
import com.ventas.backend.data.Usuario;
import com.ventas.ventas.samples.ResetButtonForTextField;

public class CategoriaCrudView extends CssLayout implements View {

	public static final String VIEW_NAME = "Categoria";
	
	private CategoriaGrid grid;
	private CategoriaForm form;
	private TextField filter;
	 
	private CategoriaCrudLogic viewLogic = new CategoriaCrudLogic(this);
	 
	private CategoriaDataProvider dataProvider = new CategoriaDataProvider();
	
	private Button newCategoria;
	 
	public CategoriaCrudView() {
		setSizeFull();
        addStyleName("crud-view");
        
        //setCaption("<h1>Usuarios</h1>");
		//setCaptionAsHtml(true);
		
        HorizontalLayout topLayout = createTopBar();
     
	 
        grid = new CategoriaGrid();
        grid.setDataProvider(dataProvider);
        grid.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));

        form = new CategoriaForm(viewLogic);

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
        filter.setPlaceholder("Filtrar id o nombre");
        ResetButtonForTextField.extend(filter);
        // Apply the filter to grid's data provider. TextField value is never null
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));

        newCategoria = new Button("Nueva Categoria");
        newCategoria.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newCategoria.setIcon(VaadinIcons.PLUS_CIRCLE);
        newCategoria.addClickListener(click -> viewLogic.newCategoria());

        HorizontalLayout topLayoutx = new HorizontalLayout();
        topLayoutx.setWidth("100%");
        topLayoutx.addComponent(filter);
        topLayoutx.addComponent(newCategoria);
        topLayoutx.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topLayoutx.setExpandRatio(filter, 1);
        
        
        Label label = new Label();
        label.setCaption("<h1>Categorías</h1>");
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

    public void setNewCategoriaEnabled(boolean enabled) {
        newCategoria.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Categoria row) {
        grid.getSelectionModel().select(row);
    }

    public Categoria getSelectedRow() {
        return grid.getSelectedRow();
    }

    public void updateCategoria(Categoria categoria) {
        dataProvider.save(categoria);
        // FIXME: Grid used to scroll to the updated item
    }

    public void removeCategoria(Categoria categoria) {
        dataProvider.delete(categoria);
    }

    public void editCategoria(Categoria categoria) {
        if (categoria != null) {
            form.addStyleName("visible");
            form.setEnabled(true);
        } else {
            form.removeStyleName("visible");
            form.setEnabled(false);
        }
        form.editCategoria(categoria);
    }
}
