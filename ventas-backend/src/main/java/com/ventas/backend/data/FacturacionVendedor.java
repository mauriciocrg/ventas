package com.ventas.backend.data;

import java.util.Date;

public class FacturacionVendedor {

	private double importe;
	private String nombreVendedor;
	
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getNombreVendedor() {
		return nombreVendedor;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}
}
