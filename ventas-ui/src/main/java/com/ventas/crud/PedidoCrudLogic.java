package com.ventas.crud;

import java.io.Serializable;

import com.vaadin.server.Page;
import com.ventas.VentasUI;
import com.ventas.backend.data.Pedido;
import com.ventas.backend.data.Producto;
import com.ventas.backend.manageData.ManagePedido;
import com.ventas.backend.manageData.ManageProducto;

public class PedidoCrudLogic implements Serializable {

	private PedidosView pedidosView;
	
	private ManagePedido managePedido = new ManagePedido();
	
	public PedidoCrudLogic(PedidosView pedidosView) {
		this.pedidosView = pedidosView;
	}
	
	public void init() {
        editPedido(null);
        // Hide and disable if not admin
        /*if (!VentasUI.get().getAccessControl().isUserInRole("admin")) {
        	pedidosView.setNewProductoEnabled(false);
        }*/
    }

    public void cancelPedido() {
        setFragmentParameter("");
        pedidosView.clearSelection();
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String usuarioId) {
        String fragmentParameter;
        if (usuarioId == null || usuarioId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = usuarioId;
        }

        Page page = VentasUI.get().getPage();
        page.setUriFragment(
                "!" + PedidosView.VIEW_NAME + "/" + fragmentParameter,
                false);
    }

    /*public void enter(String pedidoId) {
        if (pedidoId != null && !pedidoId.isEmpty()) {
            if (pedidoId.equals("new")) {
                newProducto();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    int pid = Integer.parseInt(productoId);
                    Producto producto = findProducto(pid);
                    view.selectRow(producto);
                } catch (NumberFormatException e) {
                }
            }
        }
    }*/

    private Pedido findPedido(int pedidoId) {
        return managePedido.getPedido(pedidoId);
    }

    public void savePedido(Pedido pedido) {
    	pedidosView.showSaveNotification("Pedido ("
                + pedido.getId() + ") updated");
    	pedidosView.clearSelection();
    	pedidosView.updatePedido(pedido);
        setFragmentParameter("");
    }

    public void editPedido(Pedido pedido) {
        if (pedido == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(pedido.getId() + "");
        }
        pedidosView.editPedido(pedido);
    }

    public void rowSelected(Pedido pedido) {
        if (VentasUI.get().getAccessControl().isUserInRole("admin")) {
        	pedidosView.editPedido(pedido);
        }
    }
}
