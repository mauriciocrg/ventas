package com.ventas.backend.data;

import java.io.Serializable;

public class PedidoItem implements Serializable {
	
	private int id;
	private int cantidad;
	private Producto producto;
	private Pedido pedido;
	private int disponible = 1;

	public PedidoItem() {}
	
	public PedidoItem(int cantidad,
					  Producto producto,
					  Pedido pedido) {
		this.cantidad=cantidad;
		this.producto=producto;
		this.pedido=pedido;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Pedido getPedido() {
		return pedido;
	}
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	public int getDisponible() {
		return disponible;
	}
	public void setDisponible(int disponible) {
		this.disponible = disponible;
	}
}
