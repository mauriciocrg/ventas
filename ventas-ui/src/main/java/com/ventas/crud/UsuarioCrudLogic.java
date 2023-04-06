package com.ventas.crud;

import java.io.Serializable;

import com.vaadin.server.Page;
import com.ventas.VentasUI;
import com.ventas.backend.DataService;
import com.ventas.backend.data.Product;
import com.ventas.backend.data.Usuario;
import com.ventas.backend.manageData.ManageUsuario;

public class UsuarioCrudLogic implements Serializable {

	private UsuarioCrudView view;

	private ManageUsuario manageUsuario = new ManageUsuario();
	
    public UsuarioCrudLogic(UsuarioCrudView usuarioCrudView) {
        view = usuarioCrudView;
    }

    public void init() {
        editUsuario(null);
        // Hide and disable if not admin
        if (!VentasUI.get().getAccessControl().isUserInRole("admin")) {
            view.setNewUsuarioEnabled(false);
        }
    }

    public void cancelProduct() {
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
                "!" + UsuarioCrudView.VIEW_NAME + "/" + fragmentParameter,
                false);
    }

    public void enter(String usuarioId) {
        if (usuarioId != null && !usuarioId.isEmpty()) {
            if (usuarioId.equals("new")) {
                newUsuario();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    int pid = Integer.parseInt(usuarioId);
                    Usuario usuario = findUsuario(pid);
                    view.selectRow(usuario);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private Usuario findUsuario(int usuarioId) {
        return manageUsuario.getUsuario(usuarioId);
    }

    public void saveUsuario(Usuario usuario) {
        view.showSaveNotification(usuario.getNombre() + " ("
                + usuario.getId() + ") updated");
        view.clearSelection();
        view.updateUsuario(usuario);
        setFragmentParameter("");
    }

    public void deleteUsuario(Usuario usuario) {
        view.showSaveNotification(usuario.getNombre() + " ("
                + usuario.getId() + ") removed");
        view.clearSelection();
        view.removeUsuario(usuario);
        setFragmentParameter("");
    }

    public void editUsuario(Usuario usuario) {
        if (usuario == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(usuario.getId() + "");
        }
        view.editUsuario(usuario);
    }

    public void newUsuario() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editUsuario(new Usuario());
    }

    public void rowSelected(Usuario usuario) {
        if (VentasUI.get().getAccessControl().isUserInRole("admin")) {
            view.editUsuario(usuario);
        }
    }
}
