package com.ventas.reports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.ventas.backend.data.FacturacionCliente;
import com.ventas.backend.data.FacturacionProducto;
import com.ventas.backend.manageData.ManagePedido;

public class MejorClienteGraficaLayout extends VerticalLayout {

	
	private HorizontalLayout headerLayout;
	private Panel chartPanel;
	
	private DateField fromField;
	private DateField toField;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	private Button refreshButton;
	
	private ManagePedido managePedido = new ManagePedido();
	
	public MejorClienteGraficaLayout() {
		setSizeFull();
		setCaption("Grafica de mejor cliente");
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
			chartPanel.setWidth(100, Sizeable.Unit.PERCENTAGE);
			chartPanel.setHeight(100, Sizeable.Unit.PERCENTAGE);
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
        chart.setWidth(100, Sizeable.Unit.PERCENTAGE);
        chart.setHeight(100, Sizeable.Unit.PERCENTAGE);
        chart.setType(Embedded.TYPE_BROWSER);
        
        StreamResource res = new StreamResource(getFacturacionDiariaChartStream(desde,hasta), "1");
        res.setMIMEType("text/html; charset=utf-8");
        
        chart.setSource(res);
        
        getChartPanel().setContent(chart);
	}
	
	private ChartStreamSource getFacturacionDiariaChartStream(Date desde, Date hasta) {
		
		String data = "";
		
		for(FacturacionCliente facturacionCliente: managePedido.listFacturacionPorCliente(desde, hasta)) {
			data = data + "['"+facturacionCliente.getNombreCliente()+"',"+facturacionCliente.getImporte()+"],";
		}
		
		data = data.length()>0?data.substring(0, data.length() -1):"";
		
		String HTML = "<html>"
				  + "<head>"
				  + "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
				  + "<script type=\"text/javascript\">"
				  + "	google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
				  + "	google.setOnLoadCallback(drawChart);"
				  + "	"
				  + "	function drawChart() {"
				  + "		var data = google.visualization.arrayToDataTable([['Producto','Importe'],"+data+"]);"
				  + "		var options = {"
				  + "					title: 'Facturación por Cliente (Fecha de generado: "+dateTimeFormat.format(new Date())+")'"
				  + "		};"
				  + "		var chart = new google.visualization.PieChart(document.getElementById('chart_div'));"
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
