package com.ventas.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.CheckBox;
import com.ventas.backend.data.Pedido;
import com.ventas.backend.data.PedidoItem;

public class GeneratePDFTicket {

	private Font boldFont = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
	private Font boldFont14 = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
	private Font normalFont = new Font(Font.TIMES_ROMAN, 14, Font.ITALIC);
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	
	private File file;
	
	public GeneratePDFTicket(Pedido pedido) {
		
		try {
			
			File dir = new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()+File.separator+"tmp");
			dir.mkdirs();
			
			file = new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()+File.separator+"tmp"+File.separator+"Ticket_"+new Date().getTime()+".pdf");
			
			
			
    		Document document = new Document();
    		document.setMargins(0, 0, 0, 0);
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
	        if(pedido != null) 
	        	document.add(createTable(pedido));
	        else 
	           document.add(new Paragraph("Ticket vacio..."));
	        document.close();
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addHeader(PdfPTable table) {
		PdfPCell cellProducto;
		cellProducto = new PdfPCell(new Phrase("Producto",boldFont14));
		cellProducto.setBorder(Rectangle.BOTTOM);
		
		table.addCell(cellProducto);

		PdfPCell cellCantidad;
		cellCantidad = new PdfPCell(new Phrase("Cantidad",boldFont14));
		cellCantidad.setBorder(Rectangle.BOTTOM);

		table.addCell(cellCantidad);

		PdfPCell cellPrecioUnitario;
		cellPrecioUnitario = new PdfPCell(new Phrase("P.Unit",boldFont14));
		cellPrecioUnitario.setBorder(Rectangle.BOTTOM);

		table.addCell(cellPrecioUnitario);
		
		PdfPCell cellPrecio;
		cellPrecio = new PdfPCell(new Phrase("Precio",boldFont14));
		cellPrecio.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cellPrecio.setBorder(Rectangle.BOTTOM);
		
		table.addCell(cellPrecio);
	}
	
	
	private void addFooter(PdfPTable table, Double total) {

		PdfPCell cellVacia;
		cellVacia = new PdfPCell(new Phrase( "",normalFont));
		cellVacia.setBorder(Rectangle.NO_BORDER);
		table.addCell(cellVacia);

		PdfPCell cellProducto;
		cellProducto = new PdfPCell(new Phrase("TOTAL:",boldFont));
		cellProducto.setBorder(Rectangle.TOP);
		cellProducto.setColspan(2);
		table.addCell(cellProducto);
		
		PdfPCell cellPrecio;
		cellPrecio = new PdfPCell(new Phrase(total.toString(),boldFont));
		cellPrecio.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cellPrecio.setBorder(Rectangle.TOP);
		
		table.addCell(cellPrecio);
	}
	
	
	
	private PdfPTable createTable(Pedido pedido) {

		PdfPTable table = new PdfPTable(4);
		
		Double total = new Double(0);
		
		try {
			
			table.setWidthPercentage(90);
			table.setWidths(new float[]{ 65, 15, 15, 15});

			PdfPCell cellTitulo;
			cellTitulo = new PdfPCell(new Phrase("Nota de pedido",boldFont));
			cellTitulo.setBorder(Rectangle.NO_BORDER);
			cellTitulo.setColspan(4);

			table.addCell(cellTitulo);

			PdfPCell cellTicket;
			cellTicket = new PdfPCell(new Phrase("Ticket: "+pedido.getId(),normalFont));
			cellTicket.setBorder(Rectangle.NO_BORDER);

			table.addCell(cellTicket);

			PdfPCell cellNombreCliente;
			cellNombreCliente = new PdfPCell(new Phrase( "Cliente: " + pedido.getUsuario().getNombre(),normalFont));
			cellNombreCliente.setBorder(Rectangle.NO_BORDER);
			cellNombreCliente.setColspan(3);

			table.addCell(cellNombreCliente);

			PdfPCell cellFecha;
			cellFecha = new PdfPCell(new Phrase("Fecha: "+dateFormat.format(pedido.getFecha()),normalFont));
			cellFecha.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cellFecha);

			PdfPCell cellDireccion;
			cellDireccion = new PdfPCell(new Phrase( "Dirección: " + pedido.getUsuario().getDireccion(),normalFont));
			cellDireccion.setBorder(Rectangle.NO_BORDER);
			cellDireccion.setColspan(3);

			table.addCell(cellDireccion);

			PdfPCell cellVacia;
			cellVacia = new PdfPCell(new Phrase( "",normalFont));
			cellVacia.setBorder(Rectangle.NO_BORDER);

			table.addCell(cellVacia);

			PdfPCell cellRut;
			cellRut = new PdfPCell(new Phrase( "Rut: " + pedido.getUsuario().getRut(),normalFont));
			cellRut.setBorder(Rectangle.NO_BORDER);
			cellRut.setColspan(3);

			table.addCell(cellRut);

			PdfPCell cellSpan;
			cellSpan = new PdfPCell(new Phrase("",boldFont));
			cellSpan.setColspan(6);
			cellSpan.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cellSpan);
			
			
			addHeader(table);
			
			for(PedidoItem pedidoItem: pedido.getItems()) {
				
				if(pedidoItem.getDisponible() == 1) {
					String producto = pedidoItem.getProducto().getNombre();
					Integer cantidad = pedidoItem.getCantidad();
					Double precio = pedidoItem.getProducto().getCosto_venta().doubleValue();

					PdfPCell cellProducto;
					cellProducto = new PdfPCell(new Phrase(producto,normalFont));
					cellProducto.setBorder(Rectangle.NO_BORDER);
					
					table.addCell(cellProducto);

					PdfPCell cellCantidad;
					cellCantidad = new PdfPCell(new Phrase(new Double(cantidad).toString(),normalFont));
					cellCantidad.setBorder(Rectangle.NO_BORDER);

					table.addCell(cellCantidad);

					PdfPCell cellPrecioUnitario;
					cellPrecioUnitario = new PdfPCell(new Phrase(new Double(precio).toString(),normalFont));
					cellPrecioUnitario.setBorder(Rectangle.NO_BORDER);

					table.addCell(cellPrecioUnitario);

					PdfPCell cellPrecio;
					cellPrecio = new PdfPCell(new Phrase(new Double(precio*cantidad).toString(),normalFont));
					cellPrecio.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					cellPrecio.setBorder(Rectangle.NO_BORDER);
					
					table.addCell(cellPrecio);
					
					total = total + new Double(precio*cantidad);
				}
			}
			
			addFooter(table, total);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}
	
	public File getFile() {
		return this.file;
	}
}
