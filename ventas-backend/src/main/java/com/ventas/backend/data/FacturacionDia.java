package com.ventas.backend.data;

import java.util.Date;

public class FacturacionDia {

	private double importe;
	private Date fecha;
	
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
