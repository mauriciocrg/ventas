package com.ventas.client;

import java.util.Date;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.ventas.VentasUI;
import com.ventas.backend.data.Pedido;
import com.ventas.backend.data.PedidoItem;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManagePedido;
import com.ventas.backend.manageData.ManageProducto;
import com.ventas.backend.manageData.ManageUsuario;

public class ConfirmarPedidoLayout extends FormLayout {
	
	private ComboBox usuarioCombo;
    private Label nombreUsuario;
    private Label direccionUsuario;

    private Button confirm;
    private Button cancel;
    
    private ManageUsuario manageUsuario = new ManageUsuario();
    private ManagePedido managePedido = new ManagePedido();
    private ManageProducto manageProducto = new ManageProducto();
    
    
    private CarritoView carritoView;
    private Pedido pedido;
    
    public ConfirmarPedidoLayout(CarritoView carritoView, Pedido pedido) {
    	this.carritoView = carritoView;
    	this.pedido = pedido;
    	setWidth(100, Unit.PERCENTAGE);
    	setMargin(true);
    	setSpacing(true);
    	addComponents(getUsuarioCombo(),getNombreUsuario(),getDireccionUsuario(),getConfirmarButton(),getCancelButton());
    }
    
    private ComboBox getUsuarioCombo() {
    	if(usuarioCombo == null) {
    		usuarioCombo = new ComboBox();
    		usuarioCombo.setWidth(100, Unit.PERCENTAGE);
    		usuarioCombo.setCaption("EMail del Cliente");
    		usuarioCombo.setItems(manageUsuario.listClientes());
    		usuarioCombo.addValueChangeListener(event -> {
    			if(event.getValue() != null) {
    				Usuario usuario = (Usuario)event.getValue();
    				getNombreUsuario().setValue(usuario.getNombre());
    				getDireccionUsuario().setValue(usuario.getDireccion());
    			} else {
    				getNombreUsuario().setValue("");
    				getDireccionUsuario().setValue("");
    			}
    		});
    	}
    	return usuarioCombo;
    }
    
    private Label getNombreUsuario() {
    	if(nombreUsuario == null) {
    		nombreUsuario = new Label();
    		nombreUsuario.setWidth(100, Unit.PERCENTAGE);
    		nombreUsuario.setCaption("Nómbre del Cliente");
    	}
    	return nombreUsuario;
    }
    
    private Label getDireccionUsuario() {
    	if(direccionUsuario == null) {
    		direccionUsuario = new Label();
    		direccionUsuario.setWidth(100, Unit.PERCENTAGE);
    		direccionUsuario.setCaption("Dirección del Cliente");
    	}
    	return direccionUsuario;
    }
    
    private Button getCancelButton() {
    	if(cancel == null) {
    		cancel = new Button("Cancelar",VaadinIcons.CLOSE);
    		cancel.addClickListener(event -> {
    			carritoView.setCarrito();
    		});
    	}
    	return cancel;
    }
    
    private Button getConfirmarButton() {
    	if(confirm == null) {
    		confirm = new Button("Confirmar",VaadinIcons.CHECK);
    		confirm.addClickListener(event -> {
    			if(validate()) {
    				
    					
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
						ConfirmDialog.show(getUI(), 
										   "Confirmación de pedido.", 
										   "Esta seguro que desea realizar el pedido?", 
										   "SI", 
										   "No", new ConfirmDialog.Listener() {
		
											
							
									            public void onClose(ConfirmDialog dialog) {
									                if (dialog.isConfirmed()) {
									                	pedido.setUsuario((Usuario)usuarioCombo.getValue());
									                	pedido.setVendedor(VentasUI.get().getAccessControl().getCurrentUser());
									                	pedido.setFecha(new Date());
									                	managePedido.savePedido(pedido);
									                	
									                	for(PedidoItem pitem :pedido.getItems()) {
									                		pitem.getProducto().setStock(pitem.getProducto().getStock() - pitem.getCantidad());
									                		pitem.getProducto().setDisponible(pitem.getProducto().getStock()==0?0:1);
									                		manageProducto.updateProducto(pitem.getProducto());
									                	}

									                	carritoView.setNewCarrito();
									                } 
									            }
						});
					}
    			}
    		});
    	}
    	return confirm;
    }
    
    private boolean validate() {
    	if(usuarioCombo.getValue() != null) return true;
    	else {
    		Notification.show("Debe seleccionar un Cliente", Notification.Type.HUMANIZED_MESSAGE);
    		return false;
    	}
    }
}
