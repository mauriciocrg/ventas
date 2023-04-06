package com.ventas.reports;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.ventas.backend.data.FacturacionDia;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManagePedido;

public class FacturacionDiariaLayout extends VerticalLayout {

	private DateField fromField;
	private DateField toField;
	
	private HorizontalLayout headerLayout;
	private FacturacionDiariaGrid facturacionDiariaGrid = new FacturacionDiariaGrid();
	
	public FacturacionDiariaLayout() {
		setSizeFull();
		setCaption("Facturación por día");
		setSpacing(true);
		setMargin(false);
		addComponents(getHeaderLayout(),facturacionDiariaGrid);
		setExpandRatio(facturacionDiariaGrid, 1);
	}
	
	private HorizontalLayout getHeaderLayout() {
		if(headerLayout == null) {
			headerLayout = new HorizontalLayout();
			headerLayout.setWidth("100%");
			
			headerLayout.addComponents(getFromField(),getToField());
			
		}
		return headerLayout;
	}
	
	private DateField getFromField() {
		if(fromField == null) {
			fromField = new DateField("Desde");
			fromField.setWidth("100%");
			fromField.addValueChangeListener(event -> { 
				Date desde = fromField.getValue() != null?Date.from(fromField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				Date hasta =  toField.getValue() != null?Date.from(toField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				facturacionDiariaGrid.refresh(desde, hasta);
			});
		}
		return fromField;
	}
	
	private DateField getToField() {
		if(toField == null) {
			toField = new DateField("Hasta");
			toField.setWidth("100%");
			toField.addValueChangeListener(event -> { 
				Date desde = fromField.getValue() != null?Date.from(fromField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				Date hasta =  toField.getValue() != null?Date.from(toField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
				facturacionDiariaGrid.refresh(desde, hasta);
			});
		}
		return toField;
	}
	
	private class FacturacionDiariaGrid extends Grid <FacturacionDia> {
		
		private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		private ManagePedido managePedido = new ManagePedido();
		
		public FacturacionDiariaGrid() {
			setSizeFull();

			addColumn(facturacionDia -> facturacionDia.getFecha() != null?dateFormat.format(facturacionDia.getFecha()):"").setCaption("Fecha");
	        addColumn(facturacionDia -> facturacionDia.getImporte()+" $").setCaption("Facturación").setStyleGenerator(producto -> "align-right");
	        
	        refresh(null,null);
		}
		
		public void refresh(Date desde, Date hasta) {
			setItems(managePedido.listFacturacionDiaria(desde, hasta));
		}
	}
}
