package com.ventas.client;

import java.math.BigDecimal;
import java.util.Date;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.ventas.VentasUI;
import com.ventas.backend.data.Pedido;
import com.ventas.backend.data.PedidoItem;
import com.ventas.backend.data.Producto;
import com.ventas.backend.manageData.ManagePedido;
import com.ventas.backend.manageData.ManageProducto;

public class CarritoView extends CssLayout implements View {

	public static final String VIEW_NAME = "Carrito";
	
	private Pedido pedido = new Pedido();
	
	private VerticalLayout contentLayout;
	private VerticalLayout headerLayout;
	
	private Panel contentPanel;
	
	private TextField totalField;
	private Button confirmButton;
	
	private ManagePedido managePedido = new ManagePedido();
	private ManageProducto manateProducto = new ManageProducto();
	
	public CarritoView() {
		setWidth(100, Unit.PERCENTAGE);
		setHeight(100, Unit.PERCENTAGE);
		addComponent(getContentLayout());
	}
	
	private VerticalLayout getContentLayout() {
		if(contentLayout == null) {
			contentLayout = new VerticalLayout();
			contentLayout.setWidth(100, Unit.PERCENTAGE);
			contentLayout.setHeight(100, Unit.PERCENTAGE);
			contentLayout.setSpacing(false);
			contentLayout.setMargin(true);
			contentLayout.addComponents(getHeaderLayout(), getContentPanel());
			//contentLayout.setExpandRatio(getHeaderLayout(), 0.15f);
			contentLayout.setExpandRatio(getContentPanel(), 1);
		}
		return contentLayout;
	}
	
	private VerticalLayout getHeaderLayout() {
		if(headerLayout == null) {
			headerLayout = new VerticalLayout();
			headerLayout.setWidth(100, Unit.PERCENTAGE);
			headerLayout.setHeight(120, Unit.PIXELS);
			
			Label label = new Label();
			label.setCaption("<h1>Carrito</h1>");
			label.setCaptionAsHtml(true);
			
			Label usrLabel = new Label();
			usrLabel.setCaption("Bienvenido "+VentasUI.get().getAccessControl().getPrincipalName()+"!");
			
			HorizontalLayout labelsLayout = new HorizontalLayout();
			labelsLayout.setWidth(100, Unit.PERCENTAGE);
			labelsLayout.setMargin(false);
			labelsLayout.setSpacing(true);
			labelsLayout.addComponents(label,usrLabel);
			labelsLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
			labelsLayout.setComponentAlignment(usrLabel, Alignment.MIDDLE_RIGHT);
			
			FormLayout formLayout = new FormLayout();
			formLayout.setWidth(100, Unit.PERCENTAGE);
			formLayout.setMargin(false);
			formLayout.setSpacing(false);
			formLayout.addComponent(getTotalField());
			
			HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setWidth(100, Unit.PERCENTAGE);
			buttonLayout.setMargin(false);
			buttonLayout.setSpacing(true);
			buttonLayout.addComponents(formLayout,getConfirmButton());
			buttonLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_LEFT);
			buttonLayout.setComponentAlignment(getConfirmButton(), Alignment.MIDDLE_RIGHT);
			buttonLayout.setExpandRatio(formLayout, 0.5f);
			buttonLayout.setExpandRatio(getConfirmButton(), 0.5f);
			
			headerLayout.setSpacing(true);
			headerLayout.setMargin(false);
			
			headerLayout.addComponents(labelsLayout,buttonLayout);
		}
		return headerLayout;
	}
	
	private Panel getContentPanel() {
		if(contentPanel == null) {
			contentPanel = new Panel();
			contentPanel.setWidth(100, Unit.PERCENTAGE);
			contentPanel.setHeight(100, Unit.PERCENTAGE);
			contentPanel.setContent(getCarritoLayout());
		}
		return contentPanel;
	}
	
	private VerticalLayout getCarritoLayout() {
		VerticalLayout carritoLayout = new VerticalLayout();
		carritoLayout.setWidth("100%");
		
		double total = 0;
		
		for(PedidoItem pitem :pedido.getItems()) {
			carritoLayout.addComponent(new PedidoItemLayout(pitem,CarritoView.this));
			total = total + (pitem.getCantidad() * pitem.getProducto().getCosto_venta().doubleValue());
		}
		
		getTotalField().setReadOnly(false);
		getTotalField().setValue(total+" $");
		getTotalField().setReadOnly(true);
		
		return carritoLayout;
	}
	
	
	private TextField getTotalField() {
		if(totalField == null) {
			totalField = new TextField("Total:");
			totalField.setWidth("100%");
			totalField.setReadOnly(true);
		}
		return totalField;
	}
	
	private Button getConfirmButton() {
		if(confirmButton == null) {
			confirmButton = new Button("Confirmar Pedido");
			confirmButton.addClickListener(event -> {
				
				
				if(pedido.getItems().size() > 0) {
				
					//check stock
					boolean confirmar = true;
					
					for(PedidoItem pitem :pedido.getItems()) {
						if(pitem.getProducto().getStock() - pitem.getCantidad() < 0) {
							Notification.show("No hay sufuciente Stock del producto: "+pitem.getProducto().getNombre()+" por favor redusca la cantidad.",Notification.Type.HUMANIZED_MESSAGE);
							confirmar = false;
						}
	            	}
					
					//confirm dialog
					if(confirmar) {
						if(VentasUI.get().getAccessControl().getCurrentUser().getAdmin() == 0) {
							ConfirmDialog.show(getUI(), 
											   "Confirmación de pedido.", 
											   "Esta seguro que desea realizar el pedido?", 
											   "SI", 
											   "No", new ConfirmDialog.Listener() {
			
												
								
										            public void onClose(ConfirmDialog dialog) {
										                if (dialog.isConfirmed()) {
										                	pedido.setUsuario(VentasUI.get().getAccessControl().getCurrentUser());
										                	pedido.setFecha(new Date());
										                	managePedido.savePedido(pedido);
										                	
										                	for(PedidoItem pitem :pedido.getItems()) {
										                		pitem.getProducto().setStock(pitem.getProducto().getStock() - pitem.getCantidad());
										                		pitem.getProducto().setDisponible(pitem.getProducto().getStock()==0?0:1);
										                		manateProducto.updateProducto(pitem.getProducto());
										                	}
										                
										                	VaadinSession.getCurrent().getSession().invalidate();
										                    Page.getCurrent().reload();
										                }
										            }
							});
						} else {
							getContentPanel().setContent(new ConfirmarPedidoLayout(CarritoView.this,pedido));
						}
					}
				} else {
					Notification.show("No hay items en el carrito.",Notification.Type.HUMANIZED_MESSAGE);
				}
			});
		}
		return confirmButton;
	}
	
	public void addToCarrito(Producto producto, int cantidad) {
		boolean encontro = false;
		
		for(PedidoItem item : pedido.getItems()) {
			if(item.getProducto().getId() == producto.getId()) {
				item.setCantidad(cantidad);
				encontro = true;
			}
		}
		
		if(!encontro) {
			PedidoItem pedidoItem = new PedidoItem();
			pedidoItem.setCantidad(cantidad);
			pedidoItem.setProducto(producto);
			pedidoItem.setPedido(pedido);
			pedido.getItems().add(pedidoItem);
		}
		
		getContentPanel().setContent(getCarritoLayout());
	}
	
	public void quitarItem(PedidoItem pitem) {
		pedido.getItems().remove(pitem);
		getContentPanel().setContent(getCarritoLayout());
	}
	
	public void setCarrito() {
		getContentPanel().setContent(getCarritoLayout());
	}
	
	public void setNewCarrito() {
		pedido = new Pedido();
		getContentPanel().setContent(getCarritoLayout());
	}
	
	public void setPanelContent(PedidoItemLayout layout) {
		getContentPanel().setContent(layout);
	}
}
