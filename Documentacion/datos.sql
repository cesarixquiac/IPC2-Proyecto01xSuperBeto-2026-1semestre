INSERT INTO cliente VALUES
('1234567890101', 'Maria Lopez', '1990-03-15', '55551234', 'mlopez@mail.com', 'Guatemalteca'),
('2345678901011', 'Juan Perez', '1985-05-20', '44445555', 'juanp@mail.com', 'Guatemalteca'),
('3132159950901', 'Cesar Enrique Ixquiac Vasquez', '2004-08-31', '58546762', 'cesarixquiac49@gmail.com', 'Guatemalteco'),
('5839201940101', 'Paco perez', '2026-04-16', '58546762', 'cesarixquiac49@gmail.com', 'Guatemalteco');

INSERT INTO destino VALUES
(1, 'Quetzaltenango', 'Guatemala', 'La cuna de la cultura, ideal para senderismo y volcanes.', NULL, NULL),
(2, 'Cancún', 'México', 'Playas de arena blanca y clima tropical.', NULL, 'https://www.everysteph.com/wp-content/uploads/2019/08/things-to-do-in-cancun-featured-2-1-1920x1000.jpg'),
(3, 'Antigua Guatemala', 'Guatemala', 'Ciudad colonial con mucha historia.', NULL, NULL),
(4, 'Peten', 'Guatemala', 'Cuna de la civilizacion Maya, hogar de Tikal', NULL, NULL),
(7, 'Machupichu', 'Peru', 'Ruinas e historia', 'Mejor epoca Otoño calido', NULL),
(8, 'Madrid', 'Europa', 'Visita las calles de madrid', 'Mejor epoca para visitar otoño', NULL);

INSERT INTO tipo_pago VALUES
(1, 'Efectivo', 1),
(2, 'Tarjeta de Crédito/Débito', 1),
(3, 'Transferencia Bancaria', 1);

INSERT INTO rol VALUES
(3, 'Administrador'),
(1, 'Atencion al Cliente'),
(2, 'Operaciones');

INSERT INTO usuario VALUES
(1, 'admin_cesar', 'admin123', 3, 'ACTIVO'),
(2, 'jperez', '123', 3, 'INACTIVO'),
(3, 'operador_xela', '123', 2, 'ACTIVO'),
(4, 'mlopez', 'Segura123', 1, 'ACTIVO'),
(5, 'admin_jefe', 'SuperPass', 3, 'ACTIVO'),
(6, 'nuevo_admin', 'secreta123', 3, 'ACTIVO'),
(7, 'agente_xela', 'viajes2026', 1, 'ACTIVO'),
(8, 'CalosLopez', 'admin123', 3, 'ACTIVO');

INSERT INTO proveedor VALUES
(1, 'TACA Airlines', 1, 'Guatemala', NULL),
(2, 'Hotel Decameron', 2, 'México', NULL),
(3, 'Transportes Xela-Bus', 4, 'Guatemala', NULL),
(4, 'Avianca', 1, 'Colombia', NULL),
(5, 'Transportes Galgos', 4, 'Guatemala', NULL),
(6, 'Hotel Barcelo', 2, 'Guatemala', 'barcelo@hotel.com');