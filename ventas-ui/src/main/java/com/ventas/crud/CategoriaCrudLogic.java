package com.ventas.crud;

import java.io.Serializable;

import com.vaadin.server.Page;
import com.ventas.VentasUI;
import com.ventas.backend.DataService;
import com.ventas.backend.data.Categoria;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManageCategoria;
import com.ventas.backend.manageData.ManageUsuario;

public class CategoriaCrudLogic implements Serializable {

	private CategoriaCrudView view;

	private ManageCategoria manageCategoria = new ManageCategoria();
	
    public CategoriaCrudLogic(CategoriaCrudView categoriaCrudView) {
        view = categoriaCrudView;
    }

    public void init() {
        editCategoria(null);
        // Hide and disable if not admin
        if (!VentasUI.get().getAccessControl().isUserInRole("admin")) {
            view.setNewCategoriaEnabled(false);
        }
    }

    public void cancelProduct() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String id) {
        String fragmentParameter;
        if (id == null || id.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = id;
        }

        Page page = VentasUI.get().getPage();
        page.setUriFragment(
                "!" + UsuarioCrudView.VIEW_NAME + "/" + fragmentParameter,
                false);
    }

    public void enter(String id) {
        if (id != null && !id.isEmpty()) {
            if (id.equals("new")) {
                newCategoria();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    int pid = Integer.parseInt(id);
                    Categoria categoria = findCategoria(pid);
                    view.selectRow(categoria);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private Categoria findCategoria(int id) {
        return manageCategoria.getCategoria(id);
    }

    public void saveCategoria(Categoria categoria) {
        view.showSaveNotification(categoria.getNombre() + " ("
                + categoria.getId() + ") updated");
        view.clearSelection();
        view.updateCategoria(categoria);
        setFragmentParameter("");
    }

    public void deleteCategoria(Categoria categoria) {
        view.showSaveNotification(categoria.getNombre() + " ("
                + categoria.getId() + ") removed");
        view.clearSelection();
        view.removeCategoria(categoria);
        setFragmentParameter("");
    }

    public void editCategoria(Categoria categoria) {
        if (categoria == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(categoria.getId() + "");
        }
        view.editCategoria(categoria);
    }

    public void newCategoria() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editCategoria(new Categoria());
    }

    public void rowSelected(Categoria categoria) {
        if (VentasUI.get().getAccessControl().isUserInRole("admin")) {
            view.editCategoria(categoria);
        }
    }
}
