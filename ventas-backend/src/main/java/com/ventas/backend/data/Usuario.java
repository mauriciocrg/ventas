package com.ventas.backend.data;

import java.io.Serializable;

public class Usuario implements Serializable {
	
	private static int IS_ADMIN = 1;
	private static int IS_SELLER = 2;
	
	private int id = 0;
	private String nombre = "";
	private String email = "";
	private String direccion = "";
	private String password = "";
	private int admin = 0; //
	private String rut = "";
	public Usuario() {}
	
	public Usuario(String nombre,
				   String email,
				   String direccion,
				   String password,
				   int admin,
					String rut) {
		this.nombre = nombre;
		this.email = email;
		this.direccion = direccion;
		this.password = password;
		this.admin = admin;
		this.rut=rut;
	}


	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAdmin() {
		return admin;
	}
	public void setAdmin(int admin) {
		this.admin = admin;
	}

	public String toString() {
		return this.getEmail();
	}
}
