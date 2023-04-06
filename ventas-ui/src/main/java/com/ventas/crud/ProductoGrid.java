package com.ventas.crud;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.ventas.backend.data.Producto;
import com.ventas.backend.data.Usuario;

public class ProductoGrid extends Grid<Producto> {
		
	public ProductoGrid() {
		setSizeFull();
		addColumn(Producto::getId, new NumberRenderer()).setCaption("Id");
        addColumn(Producto::getNombre).setCaption("Nombre");
        
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        
        addColumn(producto -> decimalFormat.format(producto.getCosto_compra()) + " $")
                        .setCaption("Costo Compra").setComparator((p1, p2) -> {
                            return p1.getCosto_compra().compareTo(p2.getCosto_compra());
                        }).setStyleGenerator(producto -> "align-right");
        
        addColumn(producto -> decimalFormat.format(producto.getCosto_venta()) + " $")
        .setCaption("Costo Compra").setComparator((p1, p2) -> {
            return p1.getCosto_venta().compareTo(p2.getCosto_venta());
        }).setStyleGenerator(producto -> "align-right");
        
        addColumn(producto -> producto.getFecha_compra() != null?dateFormat.format(producto.getFecha_compra()):"").setCaption("Fecha de Compra");
        addColumn(producto -> producto.getFecha_vencimiento() != null?dateFormat.format(producto.getFecha_vencimiento()):"").setCaption("Fecha de Vencimiento");
        
        addColumn(producto -> {
        	if(producto.getCategoria() == null)
        		return "-";
        	else 
        		return producto.getCategoria().getNombre();
        }).setCaption("Categoria");      
        
        addColumn(producto -> {
            if (producto.getStock() == 0) {
                return "-";
            }
            return Integer.toString(producto.getStock());
        }).setCaption("Stock").setComparator((p1, p2) -> {
            return Integer.compare(p1.getStock(), p2.getStock());
        }).setStyleGenerator(product -> "align-right");
       
        
        addColumn(this::htmlIsDisponible, new HtmlRenderer()).setCaption("Esta Disponible");
	}
	
	private String htmlIsDisponible(Producto producto) {
        
		if (producto.getDisponible() == 1) return "<span class=\"v-icon\" style=\"font-family: "
                + VaadinIcons.CIRCLE.getFontFamily() + "\">&#x"
                + Integer.toHexString(VaadinIcons.CHECK.getCodepoint())
                + ";</span>";
        else return "<span class=\"v-icon\" style=\"font-family: "
                + VaadinIcons.CIRCLE.getFontFamily() + "\">&#x"
                + Integer.toHexString(VaadinIcons.CLOSE.getCodepoint())
                + ";</span>";
    }
	
	public Producto getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Producto producto) {
        getDataCommunicator().refresh(producto);
    }

}
