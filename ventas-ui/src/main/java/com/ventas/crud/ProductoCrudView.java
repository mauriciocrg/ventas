package com.ventas.crud;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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
import com.ventas.backend.data.Producto;
import com.ventas.ventas.samples.ResetButtonForTextField;

public class ProductoCrudView extends CssLayout implements View  {

	
	public static final String VIEW_NAME = "Productos";
    private ProductoGrid grid;
    private ProductoForm form;
    private TextField filter;

    private ProductoCrudLogic viewLogic = new ProductoCrudLogic(this);
    private Button newProduct;

    private ProductoDataProvider dataProvider = new ProductoDataProvider();

    public ProductoCrudView() {
        setSizeFull();
        addStyleName("crud-view");
        
        //setCaption("<h1>Productos</h1>");
		//setCaptionAsHtml(true);
        
        HorizontalLayout topLayout = createTopBar();

        grid = new ProductoGrid();
        grid.setDataProvider(dataProvider);
        grid.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));

        form = new ProductoForm(viewLogic);

        VerticalLayout barAndGridLayout = new VerticalLayout();
        
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
        filter.setPlaceholder("Filtrar id, nombre, o descripcion");
        ResetButtonForTextField.extend(filter);
        // Apply the filter to grid's data provider. TextField value is never null
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));

        newProduct = new Button("Nuevo Producto");
        newProduct.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newProduct.setIcon(VaadinIcons.PLUS_CIRCLE);
        newProduct.addClickListener(click -> viewLogic.newProducto());

        Label label = new Label();
        label.setCaption("<h1>Productos</h1>");
        label.setCaptionAsHtml(true);
        
       
        
        HorizontalLayout topLayoutx = new HorizontalLayout();
        topLayoutx.setWidth("100%");
        
        topLayoutx.addComponent(filter);
        topLayoutx.addComponent(newProduct);
        topLayoutx.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topLayoutx.setExpandRatio(filter, 1);
        
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setWidth("100%");
        vlayout.setMargin(false);
        vlayout.addComponents(label,topLayoutx);
        
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.setStyleName("top-bar");
        topLayout.addComponent(vlayout);
        
        return topLayout;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        viewLogic.enter(event.getParameters());
    }

    public void showError(String msg) {
        Notification.show(msg, Type.ERROR_MESSAGE);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg, Type.TRAY_NOTIFICATION);
    }

    public void setNewProductoEnabled(boolean enabled) {
        newProduct.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Producto row) {
        grid.getSelectionModel().select(row);
    }

    public Producto getSelectedRow() {
        return grid.getSelectedRow();
    }

    public void saveProducto(Producto producto) {
        dataProvider.save(producto);
        // FIXME: Grid used to scroll to the updated item
    }
    
    public void updateProducto(Producto producto) {
        dataProvider.update(producto);
        // FIXME: Grid used to scroll to the updated item
    }

    public void removeProducto(Producto product) {
        dataProvider.delete(product);
    }

    public void editProducto(Producto producto) {
        if (producto != null) {
            form.addStyleName("visible");
            form.setEnabled(true);
        } else {
            form.removeStyleName("visible");
            form.setEnabled(false);
        }
        form.editProducto(producto);
    }
}
