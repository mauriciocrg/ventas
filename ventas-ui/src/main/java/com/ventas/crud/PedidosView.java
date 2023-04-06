package com.ventas.crud;

import java.time.ZoneId;
import java.util.Date;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.ventas.backend.data.Pedido;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManagePedido;
import com.ventas.backend.manageData.ManageUsuario;

public class PedidosView extends CssLayout implements View {

	public static final String VIEW_NAME = "Pedidos";

	private ManageUsuario manageUsuario = new ManageUsuario();

	private DateField fromField;
	private DateField toField;
	private ComboBox vendedorCombo;
	private CheckBox estadoNuevoCheckBox;
	
	private PedidoForm form;
	private PedidoTreeGrid grid;
	
	private PedidoCrudLogic pedidoCrudLogic = new PedidoCrudLogic(this);
	
	private ManagePedido managePedido = new ManagePedido();
	
	public PedidosView() {
		setSizeFull();
        addStyleName("crud-view");
        
        /*setCaption("<h1>Pedidos</h1>");
		setCaptionAsHtml(true);
        */
        
        form = new PedidoForm(pedidoCrudLogic);
        
        grid = new PedidoTreeGrid(form);
        grid.asSingleSelect().addValueChangeListener(event -> {
        	if(event.getValue()!=null) pedidoCrudLogic.rowSelected(event.getValue().getPedido());
        	else pedidoCrudLogic.rowSelected(null);
        });
        
        
        
        HorizontalLayout topLayout = createTopBar();
        
        VerticalLayout barAndGridLayout = new VerticalLayout();
        
        
        
        barAndGridLayout.addComponent(topLayout);
        barAndGridLayout.addComponent(grid);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.setExpandRatio(grid, 1);
        barAndGridLayout.setStyleName("crud-main-layout");
        
        addComponent(barAndGridLayout);
        addComponent(form);
	}


	public HorizontalLayout createTopBar() {
		HorizontalLayout topBar = new HorizontalLayout();
		topBar.setWidth("100%");
		topBar.setMargin(false);
		
		fromField = new DateField("Desde");
		fromField.setWidth("100%");
		fromField.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date desde = fromField.getValue() != null?Date.from(fromField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				Date hasta =  toField.getValue() != null?Date.from(toField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				boolean value = estadoNuevoCheckBox.getValue();
				Usuario usuario = vendedorCombo.getValue() != null?(Usuario)vendedorCombo.getValue():null;

				grid.refresh(desde,hasta,usuario,value);
			}
		});
		
		toField = new DateField("Hasta");
		toField.setWidth("100%");
		toField.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date desde = fromField.getValue() != null?Date.from(fromField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				Date hasta =  toField.getValue() != null?Date.from(toField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				boolean value = estadoNuevoCheckBox.getValue();
				Usuario usuario = vendedorCombo.getValue() != null?(Usuario)vendedorCombo.getValue():null;

				grid.refresh(desde,hasta,usuario,value);
			}
		});
		
		estadoNuevoCheckBox = new CheckBox("Pedidos no Atendidos");
		estadoNuevoCheckBox.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date desde = fromField.getValue() != null?Date.from(fromField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				Date hasta =  toField.getValue() != null?Date.from(toField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				boolean value = estadoNuevoCheckBox.getValue();
				Usuario usuario = vendedorCombo.getValue() != null?(Usuario)vendedorCombo.getValue():null;

				grid.refresh(desde,hasta,usuario,value);
			}
		});
		
		
		Button refreshButton = new Button("Actualizar");
		refreshButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		refreshButton.setIcon(VaadinIcons.REFRESH);
		refreshButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Date desde = fromField.getValue() != null?Date.from(fromField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				Date hasta =  toField.getValue() != null?Date.from(toField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				boolean value = estadoNuevoCheckBox.getValue();

				Usuario usuario = vendedorCombo.getValue() != null?(Usuario)vendedorCombo.getValue():null;

				grid.refresh(desde,hasta,usuario,value);
			}
		});

		vendedorCombo = new ComboBox();
		vendedorCombo.setWidth(100, Unit.PERCENTAGE);
		vendedorCombo.setCaption("EMail del Vendedor");
		vendedorCombo.setItems(manageUsuario.listNonClientes());
		vendedorCombo.addValueChangeListener(event -> {
			if(event.getValue() != null) {
				Date desde = fromField.getValue() != null?Date.from(fromField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				Date hasta =  toField.getValue() != null?Date.from(toField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				boolean value = estadoNuevoCheckBox.getValue();
				Usuario usuario = event.getValue() != null?(Usuario)event.getValue():null;

				grid.refresh(desde,hasta,usuario,value);
			}
		});
		
		HorizontalLayout filterDateLayout = new HorizontalLayout();
		filterDateLayout.setWidth("100%");
		filterDateLayout.addComponents(fromField,toField);
		
		VerticalLayout filterLayout = new VerticalLayout();
		filterLayout.addComponents(filterDateLayout,vendedorCombo,estadoNuevoCheckBox);
		filterLayout.setMargin(false);
		
		Label label = new Label();
        label.setCaption("<h1>Pedidos</h1>");
        label.setCaptionAsHtml(true);
        
		
		VerticalLayout filterLayoutContent = new VerticalLayout();
		filterLayoutContent.addComponents(label,filterLayout,refreshButton);
		filterLayoutContent.setMargin(false);
		filterLayoutContent.setWidth("100%");
		filterLayoutContent.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);
		filterLayoutContent.setComponentAlignment(filterLayout, Alignment.MIDDLE_LEFT);
		//filterLayoutContent.setExpandRatio(filterLayout, 1);
		
		topBar.addComponent(filterLayoutContent);
		
		
		topBar.setStyleName("top-bar");
		
		return topBar;
	}

    public void showError(String msg) {
        Notification.show(msg, Type.ERROR_MESSAGE);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg, Type.TRAY_NOTIFICATION);
    }

   
    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Pedido row) {
        grid.getSelectionModel().select(row.getItems().iterator().next());
    }

    public Pedido getSelectedRow() {
        return grid.getSelectedRow();
    }

    public void updatePedido(Pedido pedido) {
    	managePedido.updatePedido(pedido);
        // FIXME: Grid used to scroll to the updated item
    }

    
    public void editPedido(Pedido pedido) {
        if (pedido != null) {
            form.addStyleName("visible");
            form.setEnabled(true);
            form.editPedido(pedido);
        } else {
            form.removeStyleName("visible");
            form.setEnabled(false);
        }
    }
}
