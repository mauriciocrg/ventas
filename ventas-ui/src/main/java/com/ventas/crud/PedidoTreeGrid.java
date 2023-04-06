package com.ventas.crud;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.ventas.backend.data.*;
import com.ventas.backend.manageData.ManagePedido;

public class PedidoTreeGrid extends TreeGrid <PedidoItem> {
	
	private ManagePedido managePedido = new ManagePedido();
	
	public PedidoTreeGrid(PedidoForm form) {
		setSizeFull();
		
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
		
		addColumn(pedidoItem -> htmlFormatTipo(pedidoItem), new HtmlRenderer()).setCaption("Id de Pedido");
		/*
		 * addComponentColumn(pedidoItem -> {
			Label label = new Label();
			label.setWidth("100%");
			label.setValue(""+pedidoItem.getPedido().getId());
			label.setCaption("<span class=\"v-icon\" style=\"font-family: "
                + VaadinIcons.CIRCLE.getFontFamily() + ";color:gray"
                + "\">&#x"
                + Integer.toHexString(VaadinIcons.CIRCLE.getCodepoint())
                + " "+pedidoItem.getPedido().getId()
                + "</span>");
			label.setCaptionAsHtml(true);
			if(pedidoItem.getProducto()!=null) label.setStyleName("pedido-item-label");
			return label;
		}).setCaption("Id de Pedido");
		 */
		addColumn(pedidoItem -> pedidoItem.getPedido().getFecha() != null?dateFormat.format(pedidoItem.getPedido().getFecha()):"").setCaption("Fecha");
		addColumn(pedidoItem -> htmlFormatEstado(pedidoItem.getPedido()), new HtmlRenderer()).setCaption("Estado");
		addColumn(pedidoItem -> pedidoItem.getPedido().getUsuario().getId() + " - " +pedidoItem.getPedido().getUsuario().getNombre()).setCaption("Cliente");
		addColumn(pedidoItem -> { if (pedidoItem.getPedido().getVendedor() != null) {return pedidoItem.getPedido().getVendedor().getId() + " - " +pedidoItem.getPedido().getVendedor().getNombre();} else { return "";}}).setCaption("Vendedor");
		addColumn(pedidoItem -> pedidoItem.getProducto()!=null?pedidoItem.getProducto().getId():"").setCaption("Id de Producto");
		addColumn(pedidoItem -> pedidoItem.getProducto()!=null?pedidoItem.getProducto().getNombre():"").setCaption("Nombre de Producto");
		addColumn(pedidoItem -> pedidoItem.getCantidad()!=0? pedidoItem.getCantidad():"").setCaption("Cantidad").setComparator((p1, p2) -> {
            return Integer.compare(p1.getCantidad(), p2.getCantidad());
        }).setStyleGenerator(product -> "align-right");
		addColumn(pedidoItem -> pedidoItem.getProducto()!=null?decimalFormat.format(new BigDecimal(pedidoItem.getCantidad() * pedidoItem.getProducto().getCosto_venta().longValue())) + " $":getTotal(pedidoItem.getPedido())).setCaption("Total").setStyleGenerator(producto -> "align-right");
		
		addComponentColumn(pedidoItem -> {
			CheckBox checkBox = new CheckBox();
			checkBox.setValue(pedidoItem.getProducto()!=null?pedidoItem.getDisponible()==1:false);
			checkBox.setVisible(pedidoItem.getProducto()!=null);
			checkBox.addValueChangeListener(event -> {
				pedidoItem.setDisponible(event.getValue()?1:0);
				managePedido.saveOrUpdatePedidoItem(pedidoItem);
				//refresh(null,null,false);
				
				form.updateResource();
			});
			return checkBox;
		}).setCaption("Disponible");
		
		setStyleGenerator(t -> {
            if (t.getProducto() != null) {
            	//System.out.println("pedido-item");
                return "pedido-item";
            } else {
                return null;
            }
        });
		
		refresh(null,null,null, false);
	}
	
	private String htmlFormatEstado(Pedido pedido) {
        
        String text = "";

        String color = "";
        switch (pedido.getEstado()) {
        case Pedido.ESTADO_LISTO:
            color = "#2dd085";
            text = "LISTO";
            break;
        case Pedido.ESTADO_NUEVO:
            color = "#f54993";
            text = "NUEVO";
            break;
        default:
            break;
        }

        String iconCode = "<span class=\"v-icon\" style=\"font-family: "
                + VaadinIcons.CIRCLE.getFontFamily() + ";color:" + color
                + "\">&#x"
                + Integer.toHexString(VaadinIcons.CIRCLE.getCodepoint())
                + " " +text
                + "</span>";

        return iconCode;
    }
	
	private String htmlFormatTipo(PedidoItem pi) {
		
        
        String iconCode = "";
        if(pi.getProducto() != null) {       	
        	iconCode ="<span class=\"v-icon\" style=\"font-family: "
            + VaadinIcons.CIRCLE.getFontFamily() + ";color:red" 
            + "\">&#x"
            + Integer.toHexString(VaadinIcons.CIRCLE.getCodepoint())
            + " ITEM " 
            + "</span>";
        } else {
        	iconCode = ""+pi.getPedido().getId();
        }
        

        return iconCode;
	}
	
	private String getTotal(Pedido pedido) {
		BigDecimal total = new BigDecimal(0);
		for(PedidoItem pi : pedido.getItems()) {
			if(pi.getDisponible() == 1)
			total = new BigDecimal((pi.getCantidad() * pi.getProducto().getCosto_venta().longValue()) + total.longValue());
		}
		return total.toString() + " $";
	} 
	
	public void refresh(Date desde, Date hasta, Usuario vendedor, Boolean isNuevo) {
		List <Pedido> data = managePedido.listPedido();
		
		TreeDataProvider<PedidoItem> dataProvider = (TreeDataProvider<PedidoItem>) this.getDataProvider();

		TreeData<PedidoItem> dataTree = dataProvider.getTreeData();
		
		dataTree.clear();
		
		// add new items
		for(Pedido pedido : data) {
			
			//System.out.println(pedido.getUsuario().getNombre());
			boolean filtrar = false;
			
			if(desde != null && pedido.getFecha().before(desde)) filtrar = true;
				
			if(hasta != null && pedido.getFecha().after(hasta)) filtrar = true;
				
			if(isNuevo && pedido.getEstado() != Pedido.ESTADO_NUEVO) filtrar = true;
				
			if((vendedor != null && pedido.getVendedor() == null) || (vendedor != null && pedido.getVendedor() != null && pedido.getVendedor().getId() != vendedor.getId())) filtrar = true;

			if(!filtrar) {
				PedidoItem pedidoItem = new PedidoItem();
				pedidoItem.setProducto(null);
				pedidoItem.setPedido(pedido);
				
				dataTree.addItem(null, pedidoItem);
				dataTree.addItems(pedidoItem, pedido.getItems());
			}
			
			//System.out.println("Pedido = "+pedido.getId()+" items = "+pedido.getItems().size());
		}
		
		// after adding / removing data, data provider needs to be refreshed
		dataProvider.refreshAll();
	}
	
	public Pedido getSelectedRow() {
        return asSingleSelect().getValue().getPedido();
    }
}
