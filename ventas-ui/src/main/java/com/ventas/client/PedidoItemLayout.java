package com.ventas.client;

import java.io.File;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.ventas.backend.data.PedidoItem;
import com.ventas.backend.data.Producto;
import com.ventas.ventas.samples.AttributeExtension;

public class PedidoItemLayout extends HorizontalLayout {
	
private static String baseDirectory = new File("").getAbsolutePath();
	
	private String imageFilesPath = baseDirectory+File.separator+"images"+File.separator;
	
	private FormLayout formLayout;
	
	private Image image;
	
	private TextField productName;
	private TextField productPrice;
	private TextField cantidadField;
	
	private Label productStock;
	
	private TextArea descripcionText;
	
	private Button viewButton;
	private Button quitarButton;
	private Button volverButton;
	
	private CarritoView carritoView;
	
	private PedidoItem pedidoItem;
	
	private VerticalLayout contentLayout;
	
	/*
	 * AttributeExtension stockFieldExtension = new AttributeExtension();
        stockFieldExtension.extend(stockProducto);
        stockFieldExtension.setAttribute("type", "number");
	 * */

	
	public PedidoItemLayout(PedidoItem pedidoItem, CarritoView carritoView) {
		
		this.pedidoItem = pedidoItem;
		this.carritoView = carritoView;
		setStyleName("borderLayout");
		setSpacing(true);
		setMargin(true);
		setWidth("100%");
		addComponents(getImage(),getFormLayout());
		setComponentAlignment(getImage(), Alignment.MIDDLE_LEFT);
		setComponentAlignment(getFormLayout(), Alignment.MIDDLE_RIGHT);
	}
	
	private FormLayout getFormLayout() {
		if(formLayout == null) {
			formLayout = new FormLayout();
			formLayout.setMargin(false);
			formLayout.addComponents(getProductName(),
									getProductPrice(),
									getProductStock(),
									getDescripcionText(),
									getCantidadField(),
									getViewButton(),
									getQuitarButton(),
									getVolverButton());
		}
		return formLayout;
	}
	
	private VerticalLayout getContentLayout() {
		if(contentLayout == null) {
			contentLayout = new VerticalLayout();
			contentLayout.setMargin(false);
			contentLayout.setSpacing(true);
			contentLayout.addComponents(getImage(),getFormLayout());
		}
		return contentLayout;
	}
	
	private TextField getCantidadField() {
		if(cantidadField == null) {
			cantidadField = new TextField("Cantidad");
			
			AttributeExtension cantidadFieldExtension = new AttributeExtension();
			cantidadFieldExtension.extend(cantidadField);
			cantidadFieldExtension.setAttribute("type", "number");
	        
			cantidadField.setWidth("100%");
			cantidadField.setValue(""+pedidoItem.getCantidad());
			cantidadField.addValueChangeListener(event -> {
				pedidoItem.setCantidad(new Integer(event.getValue()));
			});
		}
		return cantidadField;
	}
	
	
	private Button getViewButton() {
		if(viewButton == null) {
			viewButton = new Button("",VaadinIcons.SEARCH);
			viewButton.addClickListener(event -> {
				viewButton.setVisible(false);
				getVolverButton().setVisible(true);
				getDescripcionText().setVisible(true);
				getImage().setWidth(100,Unit.PERCENTAGE);
				//getImage().setHeight(250,Unit.PIXELS);
				
				//setHeight("100%");
				removeAllComponents();
				addComponent(getContentLayout());
				
				carritoView.setPanelContent(PedidoItemLayout.this);
			});
		}
		return viewButton;
	}
	
	private Button getVolverButton() {
		if(volverButton == null) {
			volverButton = new Button("",VaadinIcons.ARROW_FORWARD);
			volverButton.setVisible(false);
			volverButton.addClickListener(event -> {
				carritoView.setCarrito();
			});
		}
		return volverButton;
	}
	
	private Button getQuitarButton() {
		if(quitarButton == null) {
			quitarButton = new Button("",VaadinIcons.CLOSE);
			quitarButton.addClickListener(event -> {
				carritoView.quitarItem(pedidoItem);
			});
		}
		return quitarButton;
	}
	
	private TextField getProductName() {
		if(productName == null) {
			productName = new TextField("Nómbre:");
			productName.setValue(pedidoItem.getProducto().getNombre());
			productName.setWidth("100%");
			productName.setReadOnly(true);
		}
		return productName;
	}
	
	private TextField getProductPrice() {
		if(productPrice == null) {
			productPrice = new TextField("Precio:");
			productPrice.setValue(""+pedidoItem.getProducto().getCosto_venta()+" $");
			productPrice.setWidth("100%");
			productPrice.setReadOnly(true);
		}
		return productPrice;
	}
	
	private TextArea getDescripcionText() {
		if(descripcionText == null) {
			descripcionText = new TextArea("Descripción:");
			descripcionText.setValue(pedidoItem.getProducto().getDescripcion());
			descripcionText.setReadOnly(true);
			descripcionText.setVisible(false);
		}
		return descripcionText;
	}
	
	private Label getProductStock() {
		if(productStock == null) {
			productStock = new Label();
			productStock.setCaption("Stock");
			productStock.setValue(""+pedidoItem.getProducto().getStock());
		}
		return productStock;
	}
	
	private Image getImage() {
		if(image == null) {
			
			File imgFile = new File("");
			
			if(pedidoItem.getProducto().getFoto() == null || pedidoItem.getProducto().getFoto().equals("")) {
				imgFile = new File(imageFilesPath+"default.jpg");
			} else {
				imgFile = new File(imageFilesPath+pedidoItem.getProducto().getFoto());
			} 
			
			image = new Image(null,new FileResource(imgFile));
			image.setWidth(50, Sizeable.Unit.PERCENTAGE);
			//image.setHeight(100, Sizeable.Unit.PIXELS);
		}
		return image;
	}
}
