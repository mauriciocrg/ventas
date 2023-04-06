package com.ventas.reports;

import com.vaadin.navigator.View;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class ReportesView extends CssLayout implements View {
	
	public static final String VIEW_NAME = "Reportes";

	
	private TabSheet tabSheet;
	private VerticalLayout contentLayout;
	
	public ReportesView() {
		setWidth("100%");
		setHeight("100%");
		//setCaption("<h1>Reportes</h1>");
		//setCaptionAsHtml(true);
		addComponent(getContentLayout());
	}
	
	private VerticalLayout getContentLayout() {
		if(contentLayout == null) {
			contentLayout = new VerticalLayout();
			contentLayout.setWidth("100%");
			contentLayout.setHeight("100%");
			contentLayout.setMargin(true);
			contentLayout.setSpacing(true);
			
			Label label = new Label("Reportes");
	        //label.setCaption("<h1>Reportes</h1>");
	        //label.setCaptionAsHtml(true);
	        
	        contentLayout.addComponents(label,getTabSheet());
	        contentLayout.setExpandRatio(getTabSheet(), 1);
		}
		return contentLayout;
	}
	
	private TabSheet getTabSheet() {
		if(tabSheet == null) {
			tabSheet = new TabSheet();
			tabSheet.setWidth("100%");
			tabSheet.setHeight("100%");
			
			FacturacionDiariaLayout tab1 = new FacturacionDiariaLayout();
			
			FacturacionDiariaGraficaLayout tab2 = new FacturacionDiariaGraficaLayout();

			ProductoMasVendidoGraficaLayout tab3 = new ProductoMasVendidoGraficaLayout();
			
			MejorClienteGraficaLayout tab4 = new MejorClienteGraficaLayout();

			FacturacionPorVendedorGraficaLayout tab5 = new FacturacionPorVendedorGraficaLayout();

			tabSheet.addTab(tab1);
			tabSheet.addTab(tab2);
			tabSheet.addTab(tab3);
			tabSheet.addTab(tab4);
			tabSheet.addTab(tab5);
		}
		return tabSheet;
	}
}