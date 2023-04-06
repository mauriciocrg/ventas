package com.ventas.crud;

import java.io.Serializable;

import com.vaadin.server.Page;
import com.ventas.VentasUI;
import com.ventas.backend.data.Producto;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManageProducto;

public class ProductoCrudLogic implements Serializable {

	private ProductoCrudView view;
	
	private ManageProducto manageProducto = new ManageProducto();
	
	public ProductoCrudLogic(ProductoCrudView productoCrudView) {
		this.view = productoCrudView;
	}
	
	public void init() {
        editProducto(null);
        // Hide and disable if not admin
        if (!VentasUI.get().getAccessControl().isUserInRole("admin")) {
            view.setNewProductoEnabled(false);
        }
    }

    public void cancelProducto() {
        setFragmentParameter("");
        view.clearSelection();
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
                "!" + ProductoCrudView.VIEW_NAME + "/" + fragmentParameter,
                false);
    }

    public void enter(String productoId) {
        if (productoId != null && !productoId.isEmpty()) {
            if (productoId.equals("new")) {
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
    }

    private Producto findProducto(int productoId) {
        return manageProducto.getProducto(productoId);
    }

    public void saveProducto(Producto producto) {
        view.showSaveNotification(producto.getNombre() + " ("
                + producto.getId() + ") updated");
        view.clearSelection();
        view.saveProducto(producto);
        setFragmentParameter("");
    }

    public void deleteProducto(Producto producto) {
        view.showSaveNotification(producto.getNombre() + " ("
                + producto.getId() + ") removed");
        view.clearSelection();
        view.removeProducto(producto);
        setFragmentParameter("");
    }

    public void editProducto(Producto producto) {
        if (producto == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(producto.getId() + "");
        }
        view.editProducto(producto);
    }

    public void newProducto() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProducto(new Producto());
    }

    public void rowSelected(Producto producto) {
        if (VentasUI.get().getAccessControl().isUserInRole("admin")) {
            view.editProducto(producto);
        }
    }
}
