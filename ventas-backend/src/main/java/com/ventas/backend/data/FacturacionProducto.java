package com.ventas.backend.data;

import java.util.Date;

public class FacturacionProducto {

	private double importe;
	private Date fecha;
	private String nombreMenuItem;
	
	public String getNombreMenuItem() {
		return nombreMenuItem;
	}
	public void setNombreMenuItem(String nombreMenuItem) {
		this.nombreMenuItem = nombreMenuItem;
	}
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}
