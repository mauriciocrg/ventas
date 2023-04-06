package com.ventas.backend.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Pedido implements Serializable {
	
	public static final int ESTADO_NUEVO = 0;
	public static final int ESTADO_LISTO = 1;
	
	
	private int id;
	private Date fecha;
	private Usuario usuario;
	private Usuario vendedor;
	private int estado = ESTADO_NUEVO;

	private Set<PedidoItem> items = new HashSet<PedidoItem>();


	public Pedido() {}
	
	public Pedido(Date fecha,
				  int estado,
				  Usuario usuario,
				  Usuario vendedor,
				  Set<PedidoItem> items) {
		this.fecha=fecha;
		this.usuario=usuario;
		this.vendedor=vendedor;
		this.estado=estado;
		this.items=items;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }
	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}
	public Usuario getVendedor() { return vendedor;}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public Set<PedidoItem> getItems() {
		return items;
	}
	public void setItems(Set<PedidoItem> items) {
		this.items = items;
	}
}