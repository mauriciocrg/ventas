package com.ventas.reports;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.*;
import com.ventas.backend.data.FacturacionDia;
import com.ventas.backend.data.FacturacionVendedor;
import com.ventas.backend.manageData.ManagePedido;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FacturacionPorVendedorGraficaLayout extends VerticalLayout {


	private HorizontalLayout headerLayout;
	private Panel chartPanel;

	private DateField fromField;
	private DateField toField;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private Button refreshButton;

	private ManagePedido managePedido = new ManagePedido();

	public FacturacionPorVendedorGraficaLayout() {
		setSizeFull();
		setCaption("Grafica de facturación por Vendedor");
		setSpacing(true);
		setMargin(false);
		
		refreshButton = new Button("Actualizar");
		refreshButton.addClickListener(event -> {
			setData();
		});
		
		addComponents(getHeaderLayout(),refreshButton,getChartPanel());
		setExpandRatio(getChartPanel(), 1);
		setData();
	}
	
	private HorizontalLayout getHeaderLayout() {
		if(headerLayout == null) {
			headerLayout = new HorizontalLayout();
			headerLayout.setWidth("100%");
			
			headerLayout.addComponents(getFromField(),getToField());
			
		}
		return headerLayout;
	}
	
	private Panel getChartPanel() {
		if(chartPanel == null) {
			chartPanel = new Panel("Grafica");
			chartPanel.setWidth(100, Unit.PERCENTAGE);
			chartPanel.setHeight(100, Unit.PERCENTAGE);
		}
		return chartPanel;
	}
	
	private DateField getFromField() {
		if(fromField == null) {
			fromField = new DateField("Desde");
			fromField.setWidth("100%");
			fromField.addValueChangeListener(event -> { 
				setData();
			});
		}
		return fromField;
	}
	
	private DateField getToField() {
		if(toField == null) {
			toField = new DateField("Hasta");
			toField.setWidth("100%");
			toField.addValueChangeListener(event -> { 
				setData();
			});
		}
		return toField;
	}
	
	private void setData() {
		Date desde = fromField.getValue() != null?Date.from(fromField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
		Date hasta =  toField.getValue() != null?Date.from(toField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()):null;
		
		Embedded chart = new Embedded();
        chart.setWidth(100, Unit.PERCENTAGE);
        chart.setHeight(100, Unit.PERCENTAGE);
        chart.setType(Embedded.TYPE_BROWSER);
        
        StreamResource res = new StreamResource(getFacturacionPorVendedorChartStream(desde,hasta), "1");
        res.setMIMEType("text/html; charset=utf-8");
        
        chart.setSource(res);
        
        getChartPanel().setContent(chart);
	}
	
	private ChartStreamSource getFacturacionPorVendedorChartStream(Date desde, Date hasta) {
		
		String data = "";
		
		List <FacturacionVendedor> listFacturacionVendedor = managePedido.listFacturacionPorVendedor(desde, hasta);
		
		Collections.reverse(listFacturacionVendedor);
		
		for(FacturacionVendedor facVend: listFacturacionVendedor) {
			data = data + "['"+facVend.getNombreVendedor()+"',"+facVend.getImporte()+"],";
		}
		
		
		data = data.length()>0?data.substring(0, data.length() -1):"";
		
		//System.out.println(data);
		
		String HTML = "<html>"
					  + "<head>"
					  + "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
					  + "<script type=\"text/javascript\">"
					  + "	google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
					  + "	google.setOnLoadCallback(drawChart);"
					  + "	"
					  + "	function drawChart() {"
					  + "		var data = google.visualization.arrayToDataTable([['Vendedor','Importe'],"+data+"]);"
					  + "		var options = {"
					  + "					title: 'Facturación por Vendedor (Fecha de generado: "+dateTimeFormat.format(new Date())+")',"
					  + "					vAxis: {title: 'Importe'},"
					  + "					hAxis: {title: 'Vendedor'},"
					  + "					seriesType: 'bars',"
					  + "					orientation: 'horizontal'"
					  + "		};"
					  + "		var chart = new google.visualization.BarChart(document.getElementById('chart_div'));"
					  + ""
					  + "	chart.draw(data, options);}"
					  + "</script>"
					  + "</head>"
					  + "<body>"
					  + "	<div id=\"chart_div\" style=\"width: 100%; height: 100%;\">"
					  + "	</div>"
					  + "</body>"
					  + "</html>";

		//System.out.println(HTML);
		
		return new ChartStreamSource(HTML);
	}
	
	private static class ChartStreamSource implements StreamSource {
        
        private String HTML;
        public ChartStreamSource(String HTML) {
        	this.HTML = HTML;
        }
        
        public InputStream getStream() {
            return new ByteArrayInputStream(HTML.getBytes());
        }
        
    }
}
