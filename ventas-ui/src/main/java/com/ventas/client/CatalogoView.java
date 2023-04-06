package com.ventas.client;

import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.ventas.VentasUI;
import com.ventas.backend.data.Categoria;
import com.ventas.backend.data.Producto;
import com.ventas.backend.manageData.ManageCategoria;
import com.ventas.backend.manageData.ManageProducto;

public class CatalogoView extends CssLayout implements View {

	public static final String VIEW_NAME = "Catálogo";
	
	private VerticalLayout contentLayout;
	private VerticalLayout headerLayout;
	
	private Panel contentPanel;
	
	private TextField searchField;
	
	private ComboBox<Categoria> categorias; 
	
	private ManageProducto manageProducto = new ManageProducto();
	
	private ManageCategoria manageCategoria = new ManageCategoria();
	
	private CarritoView carritoView;
	
	public CatalogoView(CarritoView carritoView) {
		this.carritoView = carritoView;
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
			headerLayout.setHeight(160, Unit.PIXELS);
			
			Label label = new Label();
			label.setCaption("<h1>Catálogo</h1>");
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
			
			headerLayout.setSpacing(true);
			headerLayout.setMargin(false);
			
			headerLayout.addComponents(labelsLayout,getSearchField(),getComboBoxCategoria());
		}
		return headerLayout;
	}
	
	private Panel getContentPanel() {
		if(contentPanel == null) {
			contentPanel = new Panel();
			contentPanel.setWidth(100, Unit.PERCENTAGE);
			contentPanel.setHeight(100, Unit.PERCENTAGE);
			contentPanel.setContent(getCatalogLayout("",null));
		}
		return contentPanel;
	}
	
	private TextField getSearchField() {
		if(searchField == null) {
			searchField = new TextField();
			searchField.setStyleName("filter-textfield");
			searchField.setWidth(100, Unit.PERCENTAGE);
			searchField.setPlaceholder("Buscar producto por nombre o descripción");
			searchField.addValueChangeListener(event -> getContentPanel().setContent(getCatalogLayout(event.getValue(),getComboBoxCategoria().getValue())));
		}
		return searchField;
	}
	
	private ComboBox <Categoria> getComboBoxCategoria() {
		if(categorias == null) {
			categorias = new ComboBox<Categoria>();
			categorias.setWidth(100, Unit.PERCENTAGE);
			categorias.setItems(manageCategoria.listCategoria());
			categorias.addValueChangeListener(event -> getContentPanel().setContent(getCatalogLayout(getSearchField().getValue(),event.getValue())));
		}
		return categorias;
	}
	
	private VerticalLayout getCatalogLayout(String filter, Categoria categoria) {
		VerticalLayout catalogLayout = new VerticalLayout();
		catalogLayout.setWidth("100%");
		
		int id_categoria = categoria!=null?categoria.getId():0;
		
		for(Producto producto :manageProducto.listProducto(filter,id_categoria)) {
			catalogLayout.addComponent(new ProductLayout(producto,CatalogoView.this,this.carritoView));
		}
		
		return catalogLayout;
	}
	
	public void setDefaultFilter() {
		getContentPanel().setContent(getCatalogLayout(getSearchField().getValue(),getComboBoxCategoria().getValue()));
	}
	
	public void setPanelContent(HorizontalLayout layout) {
		
		VerticalLayout catalogLayout = new VerticalLayout();
		catalogLayout.addComponent(layout);
		getContentPanel().setContent(catalogLayout);
	}
}
