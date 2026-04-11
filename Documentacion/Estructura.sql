CREATE DATABASE IF NOT EXISTS agencia_viajes;
USE agencia_viajes;

CREATE TABLE cliente (
  dpi VARCHAR(20) PRIMARY KEY,
  nombre_completo VARCHAR(150) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  telefono VARCHAR(20),
  email VARCHAR(100),
  nacionalidad VARCHAR(50)
);

CREATE TABLE destino (
  id_destino INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  pais VARCHAR(100) NOT NULL,
  descripcion TEXT,
  clima_epoca_ideal VARCHAR(255),
  imagen_url VARCHAR(500)
);

CREATE TABLE tipo_pago (
  id_tipo_pago INT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL,
  activo TINYINT(1) DEFAULT 1
);

CREATE TABLE proveedor (
  id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  tipo INT NOT NULL,
  pais VARCHAR(100) NOT NULL,
  contacto VARCHAR(255),
  CHECK (tipo IN (1,2,3,4,5))
);

CREATE TABLE paquete (
  id_paquete INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(150) NOT NULL UNIQUE,
  id_destino INT NOT NULL,
  duracion_dias INT NOT NULL,
  descripcion TEXT,
  precio_venta DECIMAL(12,2) NOT NULL,
  capacidad_max INT NOT NULL,
  estado TINYINT(1) DEFAULT 1,
  FOREIGN KEY (id_destino) REFERENCES destino(id_destino)
);

CREATE TABLE rol (
  id_rol INT PRIMARY KEY,
  nombre_rol VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuario (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  id_rol INT NOT NULL,
  estado VARCHAR(20) DEFAULT 'ACTIVO',
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

CREATE TABLE reservacion (
  id_reservacion INT AUTO_INCREMENT PRIMARY KEY,
  codigo_reserva VARCHAR(20) UNIQUE,
  id_paquete INT NOT NULL,
  id_usuario_agente INT NOT NULL,
  fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
  fecha_viaje DATE NOT NULL,
  estado ENUM('Pendiente','Confirmada','Cancelada','Completada') DEFAULT 'Pendiente',
  costo_total DECIMAL(12,2) NOT NULL,
  cantidad_pasajeros INT DEFAULT 1,
  fecha_cancelacion DATETIME,
  monto_reembolsado DECIMAL(12,2) DEFAULT 0.00,
  FOREIGN KEY (id_paquete) REFERENCES paquete(id_paquete),
  FOREIGN KEY (id_usuario_agente) REFERENCES usuario(id_usuario)
);

CREATE TABLE pago (
  id_pago INT AUTO_INCREMENT PRIMARY KEY,
  id_reservacion INT NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  metodo_pago INT NOT NULL,
  fecha_pago DATE NOT NULL,
  FOREIGN KEY (id_reservacion) REFERENCES reservacion(id_reservacion),
  FOREIGN KEY (metodo_pago) REFERENCES tipo_pago(id_tipo_pago),
  CHECK (metodo_pago IN (1,2,3))
);

CREATE TABLE pasajero_reservacion (
  id_reservacion INT,
  dpi_cliente VARCHAR(20),
  PRIMARY KEY (id_reservacion, dpi_cliente),
  FOREIGN KEY (id_reservacion) REFERENCES reservacion(id_reservacion),
  FOREIGN KEY (dpi_cliente) REFERENCES cliente(dpi)
);

CREATE TABLE servicio_paquete (
  id_servicio INT AUTO_INCREMENT PRIMARY KEY,
  id_paquete INT NOT NULL,
  id_proveedor INT NOT NULL,
  descripcion VARCHAR(255),
  costo_agencia DECIMAL(12,2) NOT NULL,
  FOREIGN KEY (id_paquete) REFERENCES paquete(id_paquete),
  FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor)
);

CREATE TABLE reembolso (
  id_reembolso INT AUTO_INCREMENT PRIMARY KEY,
  id_reservacion INT NOT NULL,
  monto_devuelto DECIMAL(10,2) NOT NULL,
  porcentaje_reembolso INT NOT NULL,
  fecha_reembolso DATE NOT NULL,
  observaciones VARCHAR(255),
  FOREIGN KEY (id_reservacion) REFERENCES reservacion(id_reservacion)
);