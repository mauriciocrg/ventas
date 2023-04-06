CREATE SCHEMA ventas;

USE ventas;

CREATE TABLE usuario (
	id INT NOT NULL auto_increment,
	nombre varchar(256),
	email varchar(256),
    direccion varchar(256),
    RUT varchar(11),
	password varchar(45),
	admin int(1) DEFAULT 0,
	PRIMARY KEY (id)  
);

CREATE TABLE categoria (
  id INT NOT NULL auto_increment,
  nombre VARCHAR(45) NULL,
PRIMARY KEY (id));

CREATE TABLE producto (
	id INT NOT NULL auto_increment,
	nombre varchar(45),
	stock decimal(10,0),
	foto varchar(256),
	costo_compra decimal(10,2),
	costo_venta decimal(10,2),
	fecha_compra datetime,
	fecha_vencimiento datetime,
	disponible int(1) DEFAULT 1,
    descripcion varchar(1024),
    code int(1) DEFAULT 0,
    id_categoria INT DEFAULT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id),
	PRIMARY KEY (id)
);

CREATE TABLE pedido (
	id INT NOT NULL auto_increment,
	fecha datetime,
	id_usuario int,
	id_vendedor int,
    estado INTEGER(1) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_vendedor) REFERENCES usuario(id),
    PRIMARY KEY (id)
);

/*
 
CREATE TABLE ventas.categoria (
  id INT NOT NULL auto_increment,
  nombre VARCHAR(45) NULL,
PRIMARY KEY (id));
 
ALTER TABLE ventas.producto 
ADD COLUMN id_categoria INT NULL DEFAULT NULL AFTER code;

ALTER TABLE ventas.producto 
ADD INDEX fk_categoria_idx (id_categoria ASC);
ALTER TABLE ventas.producto 
ADD CONSTRAINT fk_categoria
  FOREIGN KEY (id_categoria)
  REFERENCES ventas.categoria (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

*/

CREATE TABLE PedidoItem (
	id INT NOT NULL auto_increment,
	cantidad INT NOT NULL,
	id_producto int NOT NULL,
	id_pedido INT NOT NULL,
	disponible int(1) DEFAULT 1,
	FOREIGN KEY (id_producto) REFERENCES producto(id),
	FOREIGN KEY (id_pedido) REFERENCES pedido(id),
	PRIMARY KEY (id)
);

INSERT INTO usuario (id, nombre, direccion, email, password, admin) VALUES (122,'juan','San Bautista','juan@juan.com','gU0Q2hrFyLk=',1);