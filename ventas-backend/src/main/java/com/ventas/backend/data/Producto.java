package com.ventas.backend.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Producto implements Serializable {
	
	private int id = 0;
	private String nombre = "";
	private int stock = 0;
	private String foto = "";
	private BigDecimal costo_compra = new BigDecimal(0);
	private BigDecimal costo_venta = new BigDecimal(0);
	private Date fecha_compra = new Date();
	private Date fecha_vencimiento = new Date();
	private int disponible = 1;
	private String descripcion;
	private long code = 0;
	private Categoria categoria;
	
	public Producto() {}
	
	public Producto(String nombre,
					int stock,
					String foto,
					BigDecimal costo_compra,
					BigDecimal costo_venta,
					Date fecha_compra,
					Date fecha_vencimiento,
					int disponible,
					String descripcion,
					int code,
					Categoria categoria) {
		this.nombre = nombre;
		this.stock = stock;
		this.foto = foto;
		this.costo_compra = costo_compra;
		this.costo_venta = costo_venta;
		this.fecha_compra = fecha_compra;
		this.fecha_vencimiento = fecha_vencimiento;
		this.disponible = disponible;
		this.descripcion = descripcion;
		this.code = code;
		this.categoria = categoria;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public BigDecimal getCosto_compra() {
		return costo_compra;
	}
	public void setCosto_compra(BigDecimal costo_compra) {
		this.costo_compra = costo_compra;
	}
	public BigDecimal getCosto_venta() {
		return costo_venta;
	}
	public void setCosto_venta(BigDecimal costo_venta) {
		this.costo_venta = costo_venta;
	}
	public Date getFecha_compra() {
		return fecha_compra;
	}
	public void setFecha_compra(Date fecha_compra) {
		this.fecha_compra = fecha_compra;
	}
	public Date getFecha_vencimiento() {
		return fecha_vencimiento;
	}
	public void setFecha_vencimiento(Date fecha_vencimiento) {
		this.fecha_vencimiento = fecha_vencimiento;
	}
	public int getDisponible() {
		return disponible;
	}
	public void setDisponible(int disponible) {
		this.disponible = disponible;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}
