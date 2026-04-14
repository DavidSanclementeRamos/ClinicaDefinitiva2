-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 14-04-2026 a las 16:29:12
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `clinica`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asiento_contable`
--

CREATE TABLE `asiento_contable` (
  `balanceado` bit(1) NOT NULL,
  `contabilizado` bit(1) NOT NULL,
  `fecha` date NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_empresa` bigint(20) NOT NULL,
  `numero_documento` varchar(50) NOT NULL,
  `descripcion` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asignacion_rol_usuario`
--

CREATE TABLE `asignacion_rol_usuario` (
  `es_principal` bit(1) NOT NULL,
  `valido_desde` date NOT NULL,
  `valido_hasta` date DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `id_rol` bigint(20) NOT NULL,
  `id_usuario_identidad` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asignacion_rol_usuario`
--

INSERT INTO `asignacion_rol_usuario` (`es_principal`, `valido_desde`, `valido_hasta`, `id`, `id_rol`, `id_usuario_identidad`) VALUES
(b'0', '2026-03-13', NULL, 2, 2, 1),
(b'0', '2026-03-23', '2026-03-23', 3, 4, 4),
(b'1', '2026-04-04', NULL, 7, 1, 24),
(b'1', '2026-04-04', NULL, 8, 1, 25),
(b'1', '2026-04-04', NULL, 9, 1, 26),
(b'1', '2026-04-04', NULL, 10, 1, 27),
(b'1', '2026-04-04', NULL, 11, 1, 28),
(b'1', '2026-04-04', NULL, 12, 1, 29),
(b'1', '2026-04-04', NULL, 13, 1, 30),
(b'1', '2026-04-04', NULL, 14, 1, 31),
(b'1', '2026-04-04', NULL, 15, 1, 32),
(b'1', '2026-04-04', NULL, 16, 1, 33),
(b'1', '2026-04-04', NULL, 17, 1, 34),
(b'1', '2026-04-04', NULL, 18, 1, 35),
(b'1', '2026-04-04', NULL, 19, 1, 36),
(b'1', '2026-04-04', NULL, 20, 1, 37),
(b'1', '2026-04-04', NULL, 21, 1, 38),
(b'1', '2026-04-04', NULL, 22, 1, 39),
(b'1', '2026-04-04', NULL, 23, 1, 40),
(b'1', '2026-04-04', NULL, 24, 1, 41),
(b'1', '2026-04-04', NULL, 25, 1, 42),
(b'1', '2026-04-04', NULL, 26, 3, 16),
(b'1', '2026-04-04', NULL, 27, 3, 17),
(b'1', '2026-04-04', NULL, 28, 3, 18),
(b'1', '2026-04-04', NULL, 29, 3, 19),
(b'1', '2026-04-04', NULL, 30, 3, 20),
(b'1', '2026-04-04', NULL, 31, 3, 21),
(b'1', '2026-04-04', NULL, 32, 3, 22),
(b'1', '2026-04-04', NULL, 33, 3, 23),
(b'1', '2026-04-04', NULL, 34, 4, 6),
(b'1', '2026-04-04', NULL, 35, 4, 7),
(b'1', '2026-04-04', NULL, 36, 4, 8),
(b'1', '2026-04-04', NULL, 37, 4, 9),
(b'1', '2026-04-04', NULL, 38, 4, 10),
(b'1', '2026-04-04', NULL, 39, 4, 11),
(b'1', '2026-04-04', NULL, 40, 4, 12),
(b'1', '2026-04-04', NULL, 41, 4, 13),
(b'1', '2026-04-04', NULL, 42, 4, 14),
(b'1', '2026-04-04', NULL, 43, 4, 15),
(b'1', '2026-04-04', NULL, 44, 4, 43),
(b'1', '2026-04-04', NULL, 45, 4, 44),
(b'1', '2026-04-04', NULL, 46, 4, 45),
(b'1', '2026-04-04', NULL, 47, 4, 46),
(b'1', '2026-04-04', NULL, 48, 4, 47),
(b'1', '2026-04-04', NULL, 49, 4, 48),
(b'1', '2026-04-04', NULL, 50, 4, 49),
(b'1', '2026-04-04', NULL, 51, 4, 50),
(b'1', '2026-04-04', NULL, 52, 4, 51),
(b'1', '2026-04-04', NULL, 53, 4, 52),
(b'1', '2026-04-04', NULL, 54, 4, 53),
(b'1', '2026-04-04', NULL, 55, 4, 54),
(b'1', '2026-04-04', NULL, 56, 4, 55),
(b'1', '2026-04-04', NULL, 57, 4, 56),
(b'1', '2026-04-04', NULL, 58, 4, 57),
(b'1', '2026-04-04', NULL, 59, 4, 58),
(b'1', '2026-04-04', NULL, 60, 4, 59),
(b'1', '2026-04-04', NULL, 61, 4, 60),
(b'1', '2026-04-04', NULL, 62, 4, 61),
(b'1', '2026-04-04', NULL, 63, 4, 62),
(b'1', '2026-04-04', NULL, 64, 4, 63),
(b'1', '2026-04-04', NULL, 65, 4, 64),
(b'1', '2026-04-04', NULL, 66, 4, 65),
(b'1', '2026-04-04', NULL, 67, 4, 66),
(b'1', '2026-04-04', NULL, 68, 4, 67),
(b'1', '2026-04-04', NULL, 69, 5, 68),
(b'1', '2026-04-04', NULL, 70, 5, 69),
(b'1', '2026-04-04', NULL, 71, 5, 70),
(b'1', '2026-04-04', NULL, 72, 5, 71),
(b'1', '2026-04-04', NULL, 73, 5, 72);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita`
--

CREATE TABLE `cita` (
  `fecha_creacion` datetime(6) NOT NULL,
  `fecha_hora_fin` datetime(6) NOT NULL,
  `fecha_hora_inicio` datetime(6) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_dentista` bigint(20) NOT NULL,
  `id_paciente` bigint(20) NOT NULL,
  `id_servicio` bigint(20) NOT NULL,
  `ultima_actualizacion` datetime(6) DEFAULT NULL,
  `estado` varchar(30) NOT NULL,
  `tipo_cita` varchar(30) NOT NULL,
  `motivo` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contrato`
--

CREATE TABLE `contrato` (
  `fecha_fin` date NOT NULL,
  `fecha_inicio` date NOT NULL,
  `tasa_cobertura` decimal(5,2) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `id_empresa` bigint(20) NOT NULL,
  `id_tercero` bigint(20) NOT NULL,
  `estado` varchar(20) NOT NULL,
  `tipo_cobertura` varchar(50) NOT NULL,
  `origen` varchar(100) DEFAULT NULL,
  `nombre` varchar(200) NOT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuenta_contable`
--

CREATE TABLE `cuenta_contable` (
  `activo` bit(1) NOT NULL,
  `requiere_documento` bit(1) NOT NULL,
  `requiere_tercero` bit(1) NOT NULL,
  `codigo` varchar(8) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_empresa` bigint(20) NOT NULL,
  `naturaleza` varchar(20) NOT NULL,
  `nombre` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dentista`
--

CREATE TABLE `dentista` (
  `fecha_nacimiento` date DEFAULT NULL,
  `tipo_sangre` varchar(5) DEFAULT NULL,
  `fin_incapacidad` datetime(6) DEFAULT NULL,
  `fin_vacaciones` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `id_turno` bigint(20) DEFAULT NULL,
  `id_usuario_identidad` bigint(20) NOT NULL,
  `inicio_incapacidad` datetime(6) DEFAULT NULL,
  `inicio_vacaciones` datetime(6) DEFAULT NULL,
  `ultima_actualizacion` datetime(6) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `tipo_documento` varchar(20) NOT NULL,
  `estado_disponibilidad` varchar(30) NOT NULL,
  `numero_documento` varchar(30) NOT NULL,
  `documento_eps` varchar(50) DEFAULT NULL,
  `especialidades` varchar(200) DEFAULT NULL,
  `nombre_completo` varchar(200) NOT NULL,
  `direccion` text DEFAULT NULL,
  `horas_trabajo_json` text DEFAULT NULL,
  `nota_incapacidad` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `dentista`
--

INSERT INTO `dentista` (`fecha_nacimiento`, `tipo_sangre`, `fin_incapacidad`, `fin_vacaciones`, `id`, `id_turno`, `id_usuario_identidad`, `inicio_incapacidad`, `inicio_vacaciones`, `ultima_actualizacion`, `telefono`, `tipo_documento`, `estado_disponibilidad`, `numero_documento`, `documento_eps`, `especialidades`, `nombre_completo`, `direccion`, `horas_trabajo_json`, `nota_incapacidad`) VALUES
('1989-03-24', 'A-', NULL, NULL, 3, NULL, 1, NULL, NULL, '2026-04-06 15:55:10.000000', '5555678', '87654321', 'AVAILABLE', '87654321', 'EPS456', 'ENDODONTICS', 'Luis Fernández', 'Nueva 456|YTRF|Antioquia|GDD|050001', '{\"start\":\"09:00\",\"end\":\"18:00\",\"dayOfWeek\":\"TUESDAY\",\"declaredHoursPerWeek\":40}', NULL),
('1950-10-27', 'B+', NULL, NULL, 4, NULL, 16, NULL, NULL, '2026-04-06 15:09:27.000000', '5551001', '11111111', 'AVAILABLE', '11111111', 'EPS_SONIDO', 'ORAL_SURGERY', 'Orochimaru Sanni', 'Laboratorio Sonido, Oto|Aldea Oculta del Sonido|País del Sonido|Naruto World|SON100', '{\"start\":\"08:00\",\"end\":\"17:00\",\"dayOfWeek\":\"MONDAY\",\"declaredHoursPerWeek\":40}', NULL),
('1980-02-29', 'AB+', NULL, NULL, 5, NULL, 17, NULL, NULL, '2026-04-06 15:10:08.000000', '5551002', '22222222', 'AVAILABLE', '22222222', 'EPS_SONIDO', 'ENDODONTICS', 'Kabuto Yakushi', 'Cueva de experimentos|Aldea Oculta del Sonido|País del Sonido|Naruto World|SON101', '{\"start\":\"08:00\",\"end\":\"17:00\",\"dayOfWeek\":\"TUESDAY\",\"declaredHoursPerWeek\":40}', NULL),
('1900-01-01', 'A+', NULL, NULL, 7, NULL, 18, NULL, NULL, '2026-04-06 15:18:36.000000', '5551004', '44444444', 'AVAILABLE', '44444444', 'EPS_DRUM', 'GENERAL_DENTISTRY', 'Dr. Kureha', 'Reino de Drum, nieve|Drum|Isla Drum|One Piece World|ONE101', '{\"start\":\"08:00\",\"end\":\"17:00\",\"dayOfWeek\":\"THURSDAY\",\"declaredHoursPerWeek\":40}', NULL),
('1900-05-15', 'AB-', NULL, NULL, 8, NULL, 19, NULL, NULL, '2026-04-06 15:22:33.000000', '5551005', '55555555', 'AVAILABLE', '55555555', 'EPS_CABO', 'PROSTHODONTICS', 'Crocus Ballena', 'Cabo Gemelos, Laboon|Cabo Gemelos|Mar|One Piece World|ONE102', '{\"start\":\"08:00\",\"end\":\"17:00\",\"dayOfWeek\":\"FRIDAY\",\"declaredHoursPerWeek\":40}', NULL),
('1970-06-10', 'B-', NULL, NULL, 9, NULL, 20, NULL, NULL, '2026-04-06 15:23:12.000000', '5551006', '66666666', 'AVAILABLE', '66666666', 'EPS_LIOR', 'ORTHODONTICS', 'Shou Tucker', 'Laboratorio de Lior|Lior|Amestris|FMA World|FMA100', '{\"start\":\"08:00\",\"end\":\"17:00\",\"dayOfWeek\":\"SATURDAY\",\"declaredHoursPerWeek\":40}', NULL),
('1985-03-15', 'O+', NULL, NULL, 10, NULL, 21, NULL, NULL, '2026-04-06 15:25:07.000000', '5551007', '77777777', 'AVAILABLE', '77777777', 'EPS_ISHVAL', 'GENERAL_DENTISTRY', 'Tim Marcoh', 'Ishbal, clínica rural|Ishbal|Amestris|FMA World|FMA101', '{\"start\":\"08:00\",\"end\":\"17:00\",\"dayOfWeek\":\"SUNDAY\",\"declaredHoursPerWeek\":40}', NULL),
('1980-04-20', 'AB+', NULL, NULL, 11, NULL, 22, NULL, NULL, '2026-04-06 15:25:55.000000', '5551008', '88888888', 'AVAILABLE', '88888888', 'EPS_ACADEMY', 'ORAL_SURGERY', 'Franken Stein', 'Shibusen, laboratorio|Shibusen|Death City|Soul Eater World|SE100', '{\"start\":\"08:00\",\"end\":\"17:00\",\"dayOfWeek\":\"MONDAY\",\"declaredHoursPerWeek\":40}', NULL),
('1985-08-15', 'A-', NULL, NULL, 12, NULL, 23, NULL, NULL, '2026-04-06 15:28:08.000000', '5551009', '99999999', 'AVAILABLE', '99999999', 'EPS_VECTOR', 'ENDODONTICS', 'Medusa Gorgon', 'Laboratorio subterráneo|Shibusen|Death City|Soul Eater World|SE100', '{\"start\":\"08:00\",\"end\":\"17:00\",\"dayOfWeek\":\"TUESDAY\",\"declaredHoursPerWeek\":40}', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresa`
--

CREATE TABLE `empresa` (
  `fecha_constitucion` date NOT NULL,
  `id` bigint(20) NOT NULL,
  `nit` varchar(20) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `tipo_persona` varchar(20) NOT NULL,
  `estado` varchar(30) NOT NULL,
  `regimen_tributario` varchar(30) NOT NULL,
  `representante_legal` varchar(150) DEFAULT NULL,
  `nombre` varchar(200) NOT NULL,
  `correo_electronico` varchar(255) DEFAULT NULL,
  `direccion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empresa`
--

INSERT INTO `empresa` (`fecha_constitucion`, `id`, `nit`, `telefono`, `tipo_persona`, `estado`, `regimen_tributario`, `representante_legal`, `nombre`, `correo_electronico`, `direccion`) VALUES
('2010-05-20', 1, '900123456-7', '6018765432', 'JURIDICAL', 'ACTIVE', 'GRAN_CONTRIBUYENTE', 'María Gómez', 'Clínica Dental Ejemplo SAS (Actualizada)', 'nuevo.contacto@clinicadental.com', 'Carrera 15 # 45-67|Medellín|Antioquia|Colombia|050001');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `factura`
--

CREATE TABLE `factura` (
  `impuesto` decimal(19,4) NOT NULL,
  `moneda` varchar(3) NOT NULL,
  `subtotal` decimal(19,4) NOT NULL,
  `total` decimal(19,4) NOT NULL,
  `total_pagado` decimal(19,4) NOT NULL,
  `actualizado_en` datetime(6) DEFAULT NULL,
  `fecha_vencimiento` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `id_contrato` bigint(20) DEFAULT NULL,
  `id_dentista` bigint(20) NOT NULL,
  `id_paciente` bigint(20) DEFAULT NULL,
  `id_proveedor` bigint(20) DEFAULT NULL,
  `estado` varchar(20) NOT NULL,
  `numero_factura` varchar(30) DEFAULT NULL,
  `notas` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fase_tratamiento`
--

CREATE TABLE `fase_tratamiento` (
  `fecha_completada` date DEFAULT NULL,
  `fecha_planificada` date DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `id_tratamiento` bigint(20) NOT NULL,
  `estado` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item_factura`
--

CREATE TABLE `item_factura` (
  `cantidad` int(11) NOT NULL,
  `moneda` varchar(3) NOT NULL,
  `precio_unitario` decimal(19,4) NOT NULL,
  `fecha_realizacion` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `id_factura` bigint(20) NOT NULL,
  `id_servicio` bigint(20) NOT NULL,
  `id_tarifa` bigint(20) DEFAULT NULL,
  `codigo_servicio` varchar(20) NOT NULL,
  `descripcion_servicio` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `linea_asiento_contable`
--

CREATE TABLE `linea_asiento_contable` (
  `es_debito` bit(1) NOT NULL,
  `moneda` varchar(3) NOT NULL,
  `monto` decimal(19,4) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_asiento_contable` bigint(20) NOT NULL,
  `id_cuenta_contable` bigint(20) NOT NULL,
  `id_tercero` bigint(20) DEFAULT NULL,
  `referencia_documento` varchar(100) DEFAULT NULL,
  `descripcion` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paciente`
--

CREATE TABLE `paciente` (
  `fecha_nacimiento` date DEFAULT NULL,
  `tipo_sangre` varchar(5) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `id_contrato` bigint(20) DEFAULT NULL,
  `id_responsable` bigint(20) DEFAULT NULL,
  `id_usuario_identidad` bigint(20) NOT NULL,
  `ultima_actualizacion` datetime(6) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `tipo_documento` varchar(20) NOT NULL,
  `numero_documento` varchar(30) NOT NULL,
  `documento_eps` varchar(50) DEFAULT NULL,
  `nombre_completo` varchar(200) NOT NULL,
  `direccion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `paciente`
--

INSERT INTO `paciente` (`fecha_nacimiento`, `tipo_sangre`, `id`, `id_contrato`, `id_responsable`, `id_usuario_identidad`, `ultima_actualizacion`, `telefono`, `tipo_documento`, `numero_documento`, `documento_eps`, `nombre_completo`, `direccion`) VALUES
('1900-01-01', 'AB-', 2, NULL, NULL, 51, '2026-04-06 20:19:03.000000', '5554009', '99999999', '99999999', NULL, 'Muzan Kibutsuji', 'Castillo infinito|Tokio|Japón|Demon Slayer World|DS001'),
('1985-08-15', 'B+', 3, NULL, NULL, 52, '2026-04-06 20:26:01.000000', '5554010', '10101010', '10101010', NULL, 'Satoko Hojo', 'Hinamizawa|Hinamizawa|Gifu|Higurashi World|HG001'),
('1982-08-21', 'A+', 4, NULL, NULL, 53, '2026-04-06 20:26:59.000000', '5554011', '11111111', '11111111', NULL, 'Rika Furude', 'Hinamizawa|Hinamizawa|Gifu|Higurashi World|HG002'),
('2010-02-10', 'O+', 5, NULL, 22, 54, '2026-04-06 20:37:26.000000', '5554012', '12121212', '12121212', NULL, 'Kanna Kamui', 'Casa de Kobayashi|Tokio|Japón|Dragon Maid World|DM001'),
('1998-04-15', 'AB+', 6, NULL, NULL, 55, '2026-04-06 20:38:53.000000', '5554013', '13131313', '13131313', NULL, 'Tohru Kobayashi', 'Casa de Kobayashi|Tokio|Japón|Dragon Maid World|DM002'),
('2001-12-04', 'A-', 7, NULL, NULL, 56, '2026-04-06 20:39:41.000000', '5554014', '14141414', '14141414', NULL, 'Asuka Langley', 'Tokio-3|Tokio-3|Kanto|Evangelion World|EV001'),
('1986-11-30', 'B+', 8, NULL, NULL, 57, '2026-04-06 21:05:11.000000', '5554015', '15151515', '15151515', NULL, 'Misato Katsuragi', 'Tokio-3|Tokio-3|Kanto|Evangelion World|EV002'),
('1990-05-19', 'O-', 9, NULL, NULL, 58, '2026-04-06 21:06:14.000000', '5554016', '16161616', '16161616', NULL, 'Fuko Ibuki', 'Escuela|Ciudad de la colina|Nagoya|Clannad World|CL001'),
('1989-12-24', 'A+', 10, NULL, NULL, 59, '2026-04-06 21:07:11.000000', '5554017', '17171717', '17171717', NULL, 'Nagisa Furukawa', 'Ciudad de la colina|Ciudad de la colina|Nagoya|Clannad World|CL002'),
('2010-01-01', 'AB-', 11, NULL, 21, 60, '2026-04-06 21:08:34.000000', '5554018', '18181818', '18181818', NULL, 'Ushio Okazaki', 'Ciudad de la colina|Ciudad de la colina|Nagoya|Clannad World|CL003'),
('2013-03-15', 'O+', 12, NULL, 15, 43, '2026-04-06 21:24:16.000000', '5554001', '11111111', '11111111', NULL, 'Nina Tucker', 'Laboratorio de Lior|Lior|Amestris|FMA World|LIOR001'),
('2009-06-20', 'A+', 13, NULL, 16, 44, '2026-04-06 21:27:05.000000', '5554002', '22222222', '22222222', NULL, 'Alexander Amestris', 'Laboratorio de Lior|Lior|Amestris|FMA World|LIOR002'),
('2001-06-06', 'A-', 14, NULL, 17, 46, '2026-04-06 21:28:14.000000', '5554004', '44444444', '44444444', NULL, 'Shinji Ikari', 'Tokio-3|Tokio-3|Kanto|Evangelion World|EV001'),
('2012-12-31', 'AB-', 15, NULL, 15, 45, '2026-04-06 21:30:09.000000', '5554003', '33333333', '33333333', NULL, 'Prushka XD', 'Estación de Ida|Orth|Abismo|Made in Abyss World|ABY001');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pago`
--

CREATE TABLE `pago` (
  `moneda` varchar(3) NOT NULL,
  `monto` decimal(19,4) NOT NULL,
  `monto_reembolsado` decimal(19,4) NOT NULL,
  `actualizado_en` datetime(6) DEFAULT NULL,
  `creado_en` datetime(6) NOT NULL,
  `fecha_pago` datetime(6) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_factura` bigint(20) NOT NULL,
  `id_referencia_pagador` varchar(255) DEFAULT NULL,
  `estado` varchar(20) NOT NULL,
  `metodo_pago` varchar(30) NOT NULL,
  `tipo_pagador` varchar(30) NOT NULL,
  `id_pago_gateway` varchar(100) DEFAULT NULL,
  `referencia_transaccion` varchar(100) DEFAULT NULL,
  `mensaje_error` text DEFAULT NULL,
  `nombre_pagador` varchar(255) DEFAULT NULL,
  `razon_rembolso` varchar(30) NOT NULL,
  `razon_reembolso` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recepcionista`
--

CREATE TABLE `recepcionista` (
  `fecha_nacimiento` date DEFAULT NULL,
  `tipo_sangre` varchar(5) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `id_usuario_identidad` bigint(20) NOT NULL,
  `ultima_actualizacion` datetime(6) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `tipo_documento` varchar(20) NOT NULL,
  `numero_documento` varchar(30) NOT NULL,
  `documento_eps` varchar(50) DEFAULT NULL,
  `sector` varchar(50) DEFAULT NULL,
  `nombre_completo` varchar(200) NOT NULL,
  `direccion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `recepcionista`
--

INSERT INTO `recepcionista` (`fecha_nacimiento`, `tipo_sangre`, `id`, `id_usuario_identidad`, `ultima_actualizacion`, `telefono`, `tipo_documento`, `numero_documento`, `documento_eps`, `sector`, `nombre_completo`, `direccion`) VALUES
('1990-09-02', 'A-', 5, 40, '2026-04-06 13:25:35.000000', '320548735', '17171717', '17171717', 'EPS456', 'HUMAN_RESOURCES', 'Boa Hancock D Monkey', 'Amazon Lily|Amazon Lily|Calm Belt|One Piece World|ONE005'),
('1995-03-28', 'O+', 8, 24, '2026-04-06 11:29:22.000000', '555872001', '11111111', '11111111', 'EPS_NARUTO', 'RECEPTION', 'Sakura Haruno', 'Aldea Oculta 1|Konoha|País del Fuego|Naruto World|NAR001'),
('1995-12-27', 'A+', 9, 25, '2026-04-06 11:40:01.000000', '555952002', '22222222', '22222222', 'EPS_NARUTO', 'RECEPTION', 'Hinata Hyuga', 'Aldea Oculta 2|Konoha|País del Fuego|Naruto World|NAR002'),
('1980-08-02', 'B+', 10, 26, '2026-04-06 11:44:42.000000', '5556322003', '33333333', '33333333', 'EPS_NARUTO', 'ADMINISTRATION', 'Tsunade Senju', 'Torre Hokage|Konoha|País del Fuego|Naruto World|NAR003'),
('1985-01-28', 'AB+', 11, 27, '2026-04-06 11:47:44.000000', '555872004', '44444444', '44444444', 'EPS_NARUTO', 'ADMINISTRATION', 'Shizune Cerdo', 'Torre Hokage 2|Konoha|País del Fuego|Naruto World|NAR004'),
('1995-07-03', 'O-', 12, 28, '2026-04-06 11:49:02.000000', '5554322005', '55555555', '55555555', 'EPS_ONE', 'BILLING', 'Nami Naranja', 'Thousand Sunny|Mar|Grand Line|One Piece World|ONE001'),
('1993-03-15', 'B-', 13, 30, '2026-04-06 11:57:09.000000', '5552007', '77777777', '77777777', 'EPS_DB', 'CUSTOMER_SERVICE', 'Videl Satan', 'Ciudad Satán|Ciudad Satán|West City|Dragon Ball World|DB001'),
('1899-08-08', 'AB-', 14, 8, '2026-04-06 11:58:00.000000', '5552008', '88888888', '88888888', 'EPS_FMA', 'CUSTOMER_SERVICE', 'Winry Rockbell', 'Resembool|Resembool|Amestris|FMA World|FMA002'),
('1996-02-06', 'O+', 15, 32, '2026-04-06 11:59:29.000000', '5552009', '99999999', '99999999', 'EPS_ONE', 'MEDICAL_RECORDS', 'Nico Robin', 'Thousand Sunny|Mar|Grand Line|One Piece World|ONE002'),
('1995-08-23', 'A+', 16, 33, '2026-04-06 12:00:10.000000', '5552010', '10101010', '10101010', 'EPS_NARUTO', 'MEDICAL_RECORDS', 'Temari Arena', 'Aldea Oculta 3|Konoha|País del Viento|Naruto World|NAR005'),
('1989-02-20', 'AB+', 17, 34, '2026-04-06 12:01:01.000000', '5552011', '11111111', '11111111', 'EPS_NARUTO', 'CALL_CENTER', 'Konan Angel', 'Aldea Oculta 4|Amegakure|País de la Lluvia|Naruto World|NAR006'),
('1995-09-23', 'B+', 18, 35, '2026-04-06 12:01:47.000000', '5552012', '12121212', '12121212', 'EPS_NARUTO', 'CALL_CENTER', 'Ino Yamanaka', 'Aldea Oculta 5|Konoha|País del Fuego|Naruto World|NAR007'),
('1986-10-24', 'O-', 19, 36, '2026-04-06 12:09:14.000000', '5552013', '13131313', '13131313', 'EPS_NARUTO', 'INVENTORY', 'Anko Mitarashi', 'Aldea Oculta 6|Konoha|País del Fuego|Naruto|NAR007'),
('1996-03-09', 'A-', 20, 37, '2026-04-06 12:10:05.000000', '5552014', '14141414', '14141414', 'EPS_NARUTO', 'INVENTORY', 'Tenten Armas', 'Aldea Oculta 7|Konoha|País del Fuego|Naruto World|NAR008'),
('1994-03-09', 'B-', 22, 38, '2026-04-06 12:18:21.000000', '5552015', '15151515', '15151515', 'EPS_ONE', 'DENTAL_TECHNICIAN_SUPPORT', 'Franky Sunny', 'Thousand Sunny|Mar|Grand Line|One Piece World|ONE003'),
('1996-04-01', 'AB-', 23, 39, '2026-04-06 12:19:18.000000', '5552016', '16161616', '16161616', 'EPS_ONE', 'DENTAL_TECHNICIAN_SUPPORT', 'Usopp Naris', 'Thousand Sunny|Mar|Grand Line|One Piece World|ONE004');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reporte_adjunto`
--

CREATE TABLE `reporte_adjunto` (
  `id` bigint(20) NOT NULL,
  `id_reporte` bigint(20) NOT NULL,
  `tipo_archivo` varchar(50) DEFAULT NULL,
  `nombre_archivo` varchar(255) NOT NULL,
  `url_archivo` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reporte_administrativo`
--

CREATE TABLE `reporte_administrativo` (
  `periodo_fin` date NOT NULL,
  `periodo_inicio` date NOT NULL,
  `creado_en` datetime(6) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_aprobado_por` bigint(20) DEFAULT NULL,
  `id_creado_por` bigint(20) NOT NULL,
  `ultima_actualizacion` datetime(6) DEFAULT NULL,
  `estado` varchar(30) NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `notas` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reporte_indicador`
--

CREATE TABLE `reporte_indicador` (
  `id` bigint(20) NOT NULL,
  `id_reporte` bigint(20) NOT NULL,
  `unidad` varchar(30) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `valor` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reporte_referencia_asiento`
--

CREATE TABLE `reporte_referencia_asiento` (
  `id_asiento_contable` bigint(20) NOT NULL,
  `id_reporte` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `responsable`
--

CREATE TABLE `responsable` (
  `fecha_nacimiento` date DEFAULT NULL,
  `tipo_sangre` varchar(5) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `id_usuario_identidad` bigint(20) NOT NULL,
  `ultima_actualizacion` datetime(6) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `tipo_documento` varchar(20) NOT NULL,
  `numero_documento` varchar(30) NOT NULL,
  `tipo_responsable` varchar(30) NOT NULL,
  `documento_eps` varchar(50) DEFAULT NULL,
  `nombre_completo` varchar(200) NOT NULL,
  `direccion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `responsable`
--

INSERT INTO `responsable` (`fecha_nacimiento`, `tipo_sangre`, `id`, `id_usuario_identidad`, `ultima_actualizacion`, `telefono`, `tipo_documento`, `numero_documento`, `tipo_responsable`, `documento_eps`, `nombre_completo`, `direccion`) VALUES
('1970-06-10', 'B-', 15, 68, '2026-04-05 12:04:10.000000', '5553001', '11111111', '11111111', 'PAPA', 'EPS_LIOR', 'Shou Tucker', 'Laboratorio de Lior|Lior|Amestris|Japón|000-0001'),
('1967-01-31', 'B+', 16, 69, '2026-04-05 12:13:26.000000', '5559873002', '11111111', '11111111', 'TUTOR_LEGAL', 'EPS_SONIDO', 'Orochimaru Sanni', 'Laboratorio Sonido|Aldea Oculta del Sonido|País del Sonido|Naruto World|SON001'),
('1967-04-29', 'A+', 17, 70, '2026-04-05 12:16:40.000000', '5559873003', '12345678', '12345678', 'PAPA', 'EPS_NERV', 'Gendo Ikari', 'Sede NERV, Tokio-3|Tokio-3|Kanto|Japón|100000'),
('1975-03-15', 'O-', 20, 72, '2026-04-05 12:21:36.000000', '5553005', '11223344', '11223344', 'MAMA', 'EPS_REVOCS', 'Ragyo Kiryuin', 'Academia Honnouji|Honnouji|Kansai|Japón|REV001'),
('1967-01-31', 'AB+', 21, 71, '2026-04-05 12:24:01.000000', '5553004', '87654321', '87654321', 'TUTOR_LEGAL', 'EPS_ABYSS', 'Bondrewd Pepa', 'Estación de Ida, Abismo|Abismo|Orth|Made in Abyss World|ABY001'),
('1967-01-31', 'AB+', 22, 68, '2026-04-05 19:32:37.000000', '555876789', '33333333', '33333333', 'PAPA', 'EPS_SOUL', 'Byakuya López Rodríguez', 'Carrera 15 # 45-67|Medellín|Antioquia|Colombia|050001');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resultado_cita`
--

CREATE TABLE `resultado_cita` (
  `duracion_real_minutos` int(11) DEFAULT NULL,
  `id_cita` bigint(20) NOT NULL,
  `diagnostico` text DEFAULT NULL,
  `notas_clinicas` text DEFAULT NULL,
  `proximos_pasos` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `es_editable` bit(1) NOT NULL,
  `es_eliminable` bit(1) NOT NULL,
  `es_predeterminado` bit(1) NOT NULL,
  `id` bigint(20) NOT NULL,
  `estado` varchar(30) NOT NULL,
  `tipo_rol` varchar(50) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `razon_estado` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`es_editable`, `es_eliminable`, `es_predeterminado`, `id`, `estado`, `tipo_rol`, `descripcion`, `razon_estado`) VALUES
(b'0', b'0', b'1', 1, 'ACTIVE', 'RECEPTIONIST', 'Recepcionista por defecto', NULL),
(b'0', b'0', b'1', 2, 'ACTIVE', 'ADMINISTRATOR', 'Administrador por defecto', NULL),
(b'0', b'0', b'1', 3, 'SUSPENDED', 'DENTIST', 'Odontólogo', 'Suspensión por pruebas'),
(b'0', b'0', b'1', 4, 'ACTIVE', 'PATIENT', 'Paciente', 'Activación manual para pruebas'),
(b'0', b'0', b'1', 5, 'ACTIVE', 'GUARDIAN', 'Responsable / Tutor', NULL),
(b'1', b'1', b'0', 8, 'ACTIVE', 'ADMINISTRATOR', 'Rol clonado  el original', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol_permiso`
--

CREATE TABLE `rol_permiso` (
  `id_rol` bigint(20) NOT NULL,
  `permiso` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol_permiso`
--

INSERT INTO `rol_permiso` (`id_rol`, `permiso`) VALUES
(8, 'READ_USER_IDENTITY'),
(8, 'UPDATE_USER_IDENTITY');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `saldo_inicial`
--

CREATE TABLE `saldo_inicial` (
  `fecha` date NOT NULL,
  `moneda` varchar(3) NOT NULL,
  `monto` decimal(19,4) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_cuenta_contable` bigint(20) NOT NULL,
  `id_empresa` bigint(20) NOT NULL,
  `id_tercero` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicio_detalle_cirugia`
--

CREATE TABLE `servicio_detalle_cirugia` (
  `requiere_anestesia` bit(1) NOT NULL,
  `requiere_quirofano` bit(1) NOT NULL,
  `id_servicio` bigint(20) NOT NULL,
  `nivel_complejidad` varchar(10) DEFAULT NULL,
  `tipo_cirugia` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `servicio_detalle_cirugia`
--

INSERT INTO `servicio_detalle_cirugia` (`requiere_anestesia`, `requiere_quirofano`, `id_servicio`, `nivel_complejidad`, `tipo_cirugia`) VALUES
(b'1', b'1', 3, 'MEDIUM', 'EXTRACTION');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicio_detalle_estetico`
--

CREATE TABLE `servicio_detalle_estetico` (
  `id_servicio` bigint(20) NOT NULL,
  `tipo_estetico` varchar(30) NOT NULL,
  `material_utilizado` varchar(100) DEFAULT NULL,
  `resultado_esperado` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `servicio_detalle_estetico`
--

INSERT INTO `servicio_detalle_estetico` (`id_servicio`, `tipo_estetico`, `material_utilizado`, `resultado_esperado`) VALUES
(4, 'WHITENING', 'Peróxido de hidrógeno', 'Dientes más blancos en 3 tonos');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicio_detalle_implantologia`
--

CREATE TABLE `servicio_detalle_implantologia` (
  `meses_cicatrizacion` int(11) DEFAULT NULL,
  `requiere_injerto_oseo` bit(1) NOT NULL,
  `id_servicio` bigint(20) NOT NULL,
  `sitio_colocacion` varchar(100) DEFAULT NULL,
  `tipo_implante` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `servicio_detalle_implantologia`
--

INSERT INTO `servicio_detalle_implantologia` (`meses_cicatrizacion`, `requiere_injerto_oseo`, `id_servicio`, `sitio_colocacion`, `tipo_implante`) VALUES
(6, b'0', 5, 'Maxilar superior', 'TITANIO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicio_detalle_ortodoncia`
--

CREATE TABLE `servicio_detalle_ortodoncia` (
  `duracion_meses` int(11) DEFAULT NULL,
  `requiere_seguimiento` bit(1) NOT NULL,
  `id_servicio` bigint(20) NOT NULL,
  `tipo_aparato` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `servicio_detalle_ortodoncia`
--

INSERT INTO `servicio_detalle_ortodoncia` (`duracion_meses`, `requiere_seguimiento`, `id_servicio`, `tipo_aparato`) VALUES
(24, b'1', 2, 'METAL_BRACKETS');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicio_detalle_pediatria`
--

CREATE TABLE `servicio_detalle_pediatria` (
  `rango_edad_max` int(11) DEFAULT NULL,
  `rango_edad_min` int(11) DEFAULT NULL,
  `id_servicio` bigint(20) NOT NULL,
  `manejo_comportamiento` varchar(200) DEFAULT NULL,
  `materiales_pediatricos` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicio_detalle_protesis`
--

CREATE TABLE `servicio_detalle_protesis` (
  `unidades` int(11) NOT NULL,
  `id_servicio` bigint(20) NOT NULL,
  `fija_o_removible` varchar(10) NOT NULL,
  `material` varchar(100) DEFAULT NULL,
  `tipo_protesis` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicio_odontologico`
--

CREATE TABLE `servicio_odontologico` (
  `duracion_minutos` int(11) NOT NULL,
  `moneda_tarifa_base` varchar(3) NOT NULL,
  `requiere_autorizacion` bit(1) NOT NULL,
  `tarifa_base` decimal(19,4) NOT NULL,
  `id` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL,
  `estado` varchar(20) NOT NULL,
  `tipo_servicio` varchar(30) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `servicio_odontologico`
--

INSERT INTO `servicio_odontologico` (`duracion_minutos`, `moneda_tarifa_base`, `requiere_autorizacion`, `tarifa_base`, `id`, `codigo`, `estado`, `tipo_servicio`, `categoria`, `nombre`, `descripcion`) VALUES
(60, 'COP', b'1', 90.0000, 1, 'GENERAL', 'ACTIVE', 'GENERAL', 'PREVENTIVO', 'CLEANING', 'Incluye aplicación de flúor y sellantes'),
(60, 'COP', b'1', 1500000.0000, 2, 'ORTHODONTIC', 'ACTIVE', 'ORTHODONTIC', 'ORTHODONTIC', 'Ortodoncia con Brackets Metálicos', 'Tratamiento de ortodoncia con brackets metálicos tradicionales'),
(90, 'COP', b'1', 350000.0000, 3, 'SURGERY', 'ACTIVE', 'SURGERY', 'SURGERY', 'Extracción de Muela del Juicio', 'Extracción quirúrgica de terceros molares.'),
(60, 'COP', b'0', 400000.0000, 4, 'AESTHETICS', 'ACTIVE', 'AESTHETICS', 'AESTHETICS', 'Blanqueamiento Dental', 'Blanqueamiento dental con peróxido de hidrógeno.'),
(120, 'COP', b'1', 2800000.0000, 5, 'IMPLANTOLOGY', 'ACTIVE', 'IMPLANTOLOGY', 'IMPLANTOLOGY', 'Implante Dental Unitario', 'Colocación de implante dental de titanio.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tarifa`
--

CREATE TABLE `tarifa` (
  `moneda` varchar(3) NOT NULL,
  `monto` decimal(19,4) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_contrato` bigint(20) DEFAULT NULL,
  `id_servicio` bigint(20) NOT NULL,
  `vigente_desde` datetime(6) NOT NULL,
  `vigente_hasta` datetime(6) DEFAULT NULL,
  `estado` varchar(20) NOT NULL,
  `tipo_pagador` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tercero`
--

CREATE TABLE `tercero` (
  `activo` bit(1) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_empresa` bigint(20) NOT NULL,
  `numero_documento` varchar(20) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `tipo_documento` varchar(20) NOT NULL,
  `tipo_tercero` varchar(30) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `correo_electronico` varchar(255) DEFAULT NULL,
  `direccion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tratamiento`
--

CREATE TABLE `tratamiento` (
  `fecha_fin_esperada` date DEFAULT NULL,
  `fecha_fin_real` date DEFAULT NULL,
  `fecha_inicio` date NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_dentista` bigint(20) NOT NULL,
  `id_paciente` bigint(20) NOT NULL,
  `id_servicio` bigint(20) NOT NULL,
  `id_tarifa` bigint(20) DEFAULT NULL,
  `estado` varchar(20) NOT NULL,
  `notas` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turno`
--

CREATE TABLE `turno` (
  `fecha` date NOT NULL,
  `hora_fin` time(6) NOT NULL,
  `hora_inicio` time(6) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_dentista` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `estado` varchar(30) NOT NULL,
  `tipo` varchar(30) NOT NULL,
  `motivo_cancelacion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `turno`
--

INSERT INTO `turno` (`fecha`, `hora_fin`, `hora_inicio`, `id`, `id_dentista`, `version`, `estado`, `tipo`, `motivo_cancelacion`) VALUES
('2026-04-07', '18:00:00.000000', '10:00:00.000000', 1, 3, 0, 'ACTIVE', 'CLINICAL', NULL),
('2026-04-14', '18:00:00.000000', '10:00:00.000000', 2, 3, 0, 'ACTIVE', 'CLINICAL', NULL),
('2026-04-21', '18:00:00.000000', '10:00:00.000000', 3, 3, 1, 'COMPLETED', 'CLINICAL', NULL),
('2026-04-28', '18:00:00.000000', '10:00:00.000000', 4, 3, 0, 'ACTIVE', 'CLINICAL', NULL),
('2026-05-05', '18:00:00.000000', '10:00:00.000000', 5, 3, 1, 'CANCELLED', 'CLINICAL', 'Personal'),
('2026-05-12', '18:00:00.000000', '10:00:00.000000', 6, 3, 0, 'ACTIVE', 'CLINICAL', NULL),
('2026-04-07', '17:00:00.000000', '10:00:00.000000', 7, 5, 0, 'ACTIVE', 'CLINICAL', NULL),
('2026-04-07', '17:00:00.000000', '10:00:00.000000', 8, 12, 0, 'ACTIVE', 'CLINICAL', NULL),
('2026-04-14', '17:00:00.000000', '10:00:00.000000', 9, 12, 0, 'ACTIVE', 'CLINICAL', NULL),
('2026-04-14', '17:00:00.000000', '10:00:00.000000', 10, 5, 0, 'ACTIVE', 'CLINICAL', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turno_bloque_excluido`
--

CREATE TABLE `turno_bloque_excluido` (
  `hora_fin_bloque` time(6) NOT NULL,
  `hora_inicio_bloque` time(6) NOT NULL,
  `id` bigint(20) NOT NULL,
  `id_turno` bigint(20) NOT NULL,
  `motivo` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_identidad`
--

CREATE TABLE `usuario_identidad` (
  `intentos_fallidos` int(11) NOT NULL,
  `verificado` bit(1) NOT NULL,
  `bloqueado_hasta` datetime(6) DEFAULT NULL,
  `creado_en` datetime(6) NOT NULL,
  `id` bigint(20) NOT NULL,
  `ultimo_acceso` datetime(6) DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `estado` varchar(30) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `contrasena_hash` varchar(255) NOT NULL,
  `correo_electronico` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario_identidad`
--

INSERT INTO `usuario_identidad` (`intentos_fallidos`, `verificado`, `bloqueado_hasta`, `creado_en`, `id`, `ultimo_acceso`, `version`, `estado`, `nombre`, `contrasena_hash`, `correo_electronico`) VALUES
(0, b'1', NULL, '2026-03-13 14:10:29.000000', 1, NULL, 0, 'ACTIVE', 'Admin', '$2a$10$9PWmU/5U93P4FznFbMoWvuz55kpL9mj2QYD9ImWwNhjFFeiOcIui2', 'admin@test.com'),
(0, b'1', NULL, '2026-03-15 23:48:06.000000', 2, NULL, 7, 'ACTIVE', 'MARICO', '$2a$10$Afa2WG6HzueTVzU.dft71OY200TljOcXuxF5ESUGHLHFlyzH3JgHC', 'YUTA@gmail.com'),
(0, b'0', NULL, '2026-03-16 02:25:21.000000', 3, NULL, 0, 'ACTIVE', 'David User 1742073890123', '$2a$10$382r00voAA.hehO0ubnZae/0A/DZbF6RWyAqbnV/Qrs378uLJwb16', 'DAVID1742073890123@gmail.com'),
(0, b'0', NULL, '2026-03-18 01:18:39.000000', 4, NULL, 1, 'SUSPENDED', 'PEPE User 1742073890123', '$2a$10$Nry/DFwzXPT4ph/gmZ.oiOfPBeBh7FjZR8i2i4TLvRti98oDV47yS', 'PEPE1742073890123@gmail.com'),
(0, b'0', NULL, '2026-03-23 17:25:54.000000', 5, NULL, 1, 'SUSPENDED', 'MANCO', '$2a$10$KMh3n2rtcWoitnQBvaSgz.m8SApyjoTdqumt357ZbNUKn0hHOYRvO', 'JHOSY@gmail.com'),
(0, b'1', NULL, '2026-04-04 22:34:23.000000', 6, NULL, 0, 'ACTIVE', 'Goku', '$2a$10$eOg8aeeD9QVO5sfRKontB.sxV/xVPhUzA7n07JhqHNt8W4mUXcS42', 'goku@anime.com'),
(0, b'0', NULL, '2026-04-04 22:35:15.000000', 7, NULL, 0, 'ACTIVE', 'Vegeta', '$2a$10$dLRTHERopj3svxv3npn4iuZTtQyzdPAMItxhZ65w22hTq850RTh2W', 'vegeta@anime.com'),
(0, b'0', NULL, '2026-04-04 22:35:51.000000', 8, NULL, 0, 'ACTIVE', 'Naruto', '$2a$10$TeZPBuF9qSAL3CBysD/SpetXrLKs6DV.5EcE/RZ.PR906ijuKWigy', 'naruto@anime.com'),
(0, b'0', NULL, '2026-04-04 22:36:28.000000', 9, NULL, 0, 'ACTIVE', 'Sasuke', '$2a$10$OQm9VtzysPSsel5s6aZF8e7ClyPE2/H4lxJKy/yTmAJ9bTK/8dhzK', 'sasuke@anime.com'),
(0, b'0', NULL, '2026-04-04 22:37:05.000000', 10, NULL, 0, 'ACTIVE', 'Luffy', '$2a$10$NCgSgt8eD1rMXwOagFEXPeMAOmZ3gFpOnfw8ScE293w7exk43p8rO', 'luffy@anime.com'),
(0, b'0', NULL, '2026-04-04 22:37:45.000000', 11, NULL, 0, 'ACTIVE', 'Zoro', '$2a$10$WsToRziTGW30iF4JVHrze.xG0RMsOtjyIvMCzY/22iFquNq0oT5vK', 'zoro@anime.com'),
(0, b'0', NULL, '2026-04-04 22:38:16.000000', 12, NULL, 0, 'ACTIVE', 'Piccolo', '$2a$10$/heRNeV5MsFy94sznFsG7uE19pID4QcRyn71mYXIKhCO7EPwDziNu', 'piccolo@anime.com'),
(0, b'0', NULL, '2026-04-04 22:38:33.000000', 13, NULL, 0, 'ACTIVE', 'Kakashi', '$2a$10$jBqosUfewLoPT7lz9sQ4/eHZh7FB4Ik4LdXghc.L5W3VhM599PySa', 'kakashi@anime.com'),
(0, b'0', NULL, '2026-04-04 22:39:02.000000', 14, NULL, 0, 'ACTIVE', 'Sanji', '$2a$10$RD1xoFo5a.gO7KdP7tKqKeKHAKnLBnn6b2UM0jlq0E6NuXgQlK92S', 'sanji@anime.com'),
(0, b'0', NULL, '2026-04-04 22:39:23.000000', 15, NULL, 0, 'ACTIVE', 'Rock Lee', '$2a$10$T5OTUGk4iKRd9.YNuA65xOftMd1EmWy5APue5lCKnS8X6NuHMJ9ym', 'rocklee@anime.com'),
(0, b'0', NULL, '2026-04-04 23:17:57.000000', 16, NULL, 0, 'ACTIVE', 'Orochimaru', '$2a$10$i9EQbaPM/cgsAvR47fEGGeX3zYpSxG/m7uW.G89pTn7W2.apXjn26', 'orochimaru@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:22:18.000000', 17, NULL, 0, 'ACTIVE', 'Kabuto Yakushi', '$2a$10$pmjp3u4bbXgDLCtNioy6gO1ytStK0enMv0G3HvuqPU6GPOAX4swma', 'kabuto@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:25:19.000000', 18, NULL, 0, 'ACTIVE', 'Dr. Kureha', '$2a$10$iIu1WasWqLDVrXu0SGbnvOPcSMEQryxO.EYt9nohE.KJIMEfEXpX2', 'kureha@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:26:16.000000', 19, NULL, 0, 'ACTIVE', 'Crocus', '$2a$10$IXEZwQdmH4l6V/KQ1vY/pu5E6KVOmcu.JoPj3uxt5LR9/JQss8yf.', 'crocus@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:26:32.000000', 20, NULL, 0, 'ACTIVE', 'Shou Tucker', '$2a$10$6wli2IA2tKmhbNcnSbkL4eePPDzlghVbEA4hVT2INcoymCj1MVpOy', 'tucker@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:27:04.000000', 21, NULL, 0, 'ACTIVE', 'Tim Marcoh', '$2a$10$cwSC2qdsAEoNsRms5vM27u4YSnFK62rzSWzs/K/wdyc.Woay.u6yy', 'marcoh@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:27:20.000000', 22, NULL, 0, 'ACTIVE', 'Franken Stein', '$2a$10$.3UBDX78/Ge.HWtRpj5i/.OYFma98NGKgevyZ1lIYoLlj/PBlpmHS', 'stein@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:27:43.000000', 23, NULL, 0, 'ACTIVE', 'Medusa Gorgon', '$2a$10$uuHrgusXC5W09o2.H/nJteIKCtgEwt2vaIrIyB.MlaymUcXxJPCei', 'medusa@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:32:11.000000', 24, NULL, 0, 'ACTIVE', 'Sakura Haruno', '$2a$10$Qg4d2WK2xt0H.S/3Cbec8e8c/jN4uxI2HMLygoh90tcqLkGX2l/UG', 'sakura@recep.com'),
(0, b'0', NULL, '2026-04-04 23:32:28.000000', 25, NULL, 0, 'ACTIVE', 'Hinata Hyuga', '$2a$10$AUXngMtkZF8alhbdcnebDuw9dfg7PenNxFMoHJY2W2fZXtTqdU1i6', 'hinata@recep.com'),
(0, b'0', NULL, '2026-04-04 23:32:48.000000', 26, NULL, 0, 'ACTIVE', 'Tsunade Senju', '$2a$10$1ZIPrPawpi8duhBpVV37zeo43AmS.ycOOLB1syYJYRrTA8IBkYsqC', 'tsunade@admin.com'),
(0, b'0', NULL, '2026-04-04 23:33:03.000000', 27, NULL, 0, 'ACTIVE', 'Shizune', '$2a$10$Z5eNcH5iajsKyMo.DL8Sx.LolhNmRpXHJBWGTmttoL.bfLEDxVHIe', 'shizune@admin.com'),
(0, b'0', NULL, '2026-04-04 23:33:17.000000', 28, NULL, 0, 'ACTIVE', 'Nami', '$2a$10$y29o7Qmq/0iKsDDXS5npfOURzpIvdTwiAyu6wV7VBwnO3jSXp6s.6', 'nami@billing.com'),
(0, b'0', NULL, '2026-04-04 23:33:38.000000', 29, NULL, 0, 'ACTIVE', 'Riza Hawkeye', '$2a$10$J9SuTx.ouWUnjQCDrFvck.6w2idbA9VipWpzgmww6.OdibaYlj.nS', 'riza@billing.com'),
(0, b'0', NULL, '2026-04-04 23:33:53.000000', 30, NULL, 0, 'ACTIVE', 'Videl Satan', '$2a$10$ZwKISGz7woSC.ROOPrMCtOtk.lEmNASaM3kd7sEt6W.poKt1FecGK', 'videl@cs.com'),
(0, b'0', NULL, '2026-04-04 23:34:09.000000', 31, NULL, 0, 'ACTIVE', 'Winry Rockbell', '$2a$10$nuV8SR334UxX2JB9ltSiTexXDmdDsPaeqNOKfHNapsxVH6AYRhg46', 'winry@cs.com'),
(0, b'0', NULL, '2026-04-04 23:34:25.000000', 32, NULL, 0, 'ACTIVE', 'Nico Robin', '$2a$10$3KQsPDc6k8IOmM7IteVLj.bXC6qVeZu75IAWYEjieFLf9B86pPWzC', 'robin@records.com'),
(0, b'0', NULL, '2026-04-04 23:34:39.000000', 33, NULL, 0, 'ACTIVE', 'Temari', '$2a$10$gpwX3fj8j3/Q8sKCw8jtv.FEbS4eS3Eq4dLTfRsOPkXMYxSFfZBdW', 'temari@records.com'),
(0, b'0', NULL, '2026-04-04 23:34:53.000000', 34, NULL, 0, 'ACTIVE', 'Konan', '$2a$10$Zy8/7A9I27BCJoWiSotobOhSDp5V99C4SKAstDBjeVafDgQnYoDby', 'konan@call.com'),
(0, b'0', NULL, '2026-04-04 23:35:06.000000', 35, NULL, 0, 'ACTIVE', 'Ino Yamanaka', '$2a$10$PkIZaNjP8os9F0gmRsxxz.fa3n1M/riWPFEpoho38si6m6tyLz1g6', 'ino@call.com'),
(0, b'0', NULL, '2026-04-04 23:35:20.000000', 36, NULL, 0, 'ACTIVE', 'Anko Mitarashi', '$2a$10$5qlhsQXeQ4ipt/jgDkiX0O.OUN6hCNVmbivvrAHCx5hnGxx2914oS', 'anko@inventory.com'),
(0, b'0', NULL, '2026-04-04 23:35:40.000000', 37, NULL, 0, 'ACTIVE', 'Tenten', '$2a$10$FNH5PheDrK.SJjdQUT18JuZ/GFfzB0pN.yoxhMjQb0pKSdnxfzbp2', 'tenten@inventory.com'),
(0, b'0', NULL, '2026-04-04 23:35:54.000000', 38, NULL, 0, 'ACTIVE', 'Franky', '$2a$10$sw6jtRx/BCxMlh0FOWisJOrj/OMcB7bjvJbBSd2C6YvcCmqb4Y5da', 'franky@tech.com'),
(0, b'0', NULL, '2026-04-04 23:36:14.000000', 39, NULL, 0, 'ACTIVE', 'Usopp', '$2a$10$7Si/.uWUkTInYZFxUEZ/luJSLHHwarCK0TZJFy/w3w4CE1bgf.hBO', 'usopp@tech.com'),
(0, b'1', NULL, '2026-04-04 23:36:30.000000', 40, NULL, 0, 'ACTIVE', 'Boa Hancock', '$2a$10$LxyFTjkN/q8bHyPV.Tf0wu9cXFcCy7cCuVlpsifVeY00TFQUYj2W2', 'hancock@hr.com'),
(0, b'0', NULL, '2026-04-04 23:36:46.000000', 41, NULL, 0, 'ACTIVE', 'Lust', '$2a$10$KNedCrac.W4q/hg9rmT2F.d8WxlM8O1tTGHnAIBivsa6UgK2jJIqC', 'lust@hr.com'),
(0, b'0', NULL, '2026-04-04 23:37:23.000000', 42, NULL, 0, 'ACTIVE', 'Karin', '$2a$10$rGSRvAMPVroqOL63xP00Pem/jKZEeciBx732zwMNvYIlY9EPqkM.S', 'karin@assist.com'),
(0, b'0', NULL, '2026-04-04 23:40:49.000000', 43, NULL, 0, 'ACTIVE', 'Nina Tucker', '$2a$10$XkrZXIl6Q5Wy/hmtBxXVYeoLoBaNwdeFpgG/zKq.Yn3ZayGRcu9MC', 'nina@victimas.com'),
(0, b'0', NULL, '2026-04-04 23:41:08.000000', 44, NULL, 0, 'ACTIVE', 'Alexander', '$2a$10$UM3hsrB/UsXpTrc2P.2/Y.OWybjGV5IgigqTuQwWQdV40M0DU6I9G', 'alexander@victimas.com'),
(0, b'0', NULL, '2026-04-04 23:41:33.000000', 45, NULL, 0, 'ACTIVE', 'Prushka', '$2a$10$JWhEgz6J3QC1kW0kkzxEFOJ23RlNTFbh77tFnNzn2/3p5MrYJoNTi', 'prushka@abyss.com'),
(0, b'0', NULL, '2026-04-04 23:41:50.000000', 46, NULL, 0, 'ACTIVE', 'Shinji Ikari', '$2a$10$4iFmC/3ppjaCJoD0WFbDFum4abf64JiJTTDZNm5dnqiMq3mNW9/dC', 'shinji@nerv.com'),
(0, b'0', NULL, '2026-04-04 23:42:06.000000', 47, NULL, 0, 'ACTIVE', 'Rei Ayanami', '$2a$10$SWLVM1T8YVm44vNnp8YlXOxQamN80w149ATMOh/OIRhck/c6CQKTC', 'rei@nerv.com'),
(0, b'0', NULL, '2026-04-04 23:42:22.000000', 48, NULL, 0, 'ACTIVE', 'Eri', '$2a$10$UKmhdhLy4cMwuWqzwkjuV.jiMoltqoPm3UiIhF4dXkfxQJAkKc2Uy', 'eri@hero.com'),
(0, b'0', NULL, '2026-04-04 23:42:47.000000', 49, NULL, 0, 'ACTIVE', 'Killua Zoldyck', '$2a$10$E1W6g.q2zOuDTtkvhBElLeHYhae6r5C/hsGL5NYZEMfNwFdg9YEW.', 'killua@hunter.com'),
(0, b'0', NULL, '2026-04-04 23:43:02.000000', 50, NULL, 0, 'ACTIVE', 'Lucy', '$2a$10$mKTFzSnlLz7nYjSvfQgj3er0NCxfTLj7C2vkegEMimrAlt2TNBt9y', 'lucy@diclonius.com'),
(0, b'0', NULL, '2026-04-04 23:43:15.000000', 51, NULL, 0, 'ACTIVE', 'Muzan Kibutsuji', '$2a$10$nI/BpZ4MSe2WMOBEj0bUR.HSpwezjg8YdBxdqNykznqO1MJxTchli', 'muzan@demonslayer.com'),
(0, b'0', NULL, '2026-04-04 23:43:29.000000', 52, NULL, 0, 'ACTIVE', 'Satoko Hojo', '$2a$10$7DcEAYtgjBe0LdpJJM6brewpeMrN74Fi6kZ0A5YOhxrDRSfuKPxl2', 'satoko@higurashi.com'),
(0, b'0', NULL, '2026-04-04 23:43:44.000000', 53, NULL, 0, 'ACTIVE', 'Rika Furude', '$2a$10$ayppdx/O/nmJffq59lu.f.8AIsaNTDUbrxFr5QybsHufZ5aSga85S', 'rika@higurashi.com'),
(0, b'0', NULL, '2026-04-04 23:43:59.000000', 54, NULL, 0, 'ACTIVE', 'Kanna Kamui', '$2a$10$r/gWJoMI4MAF4bu3tDZEDeqKN.zJiOkCVNYEWYZysGtqRxpIvLn9m', 'kanna@dragon.com'),
(0, b'0', NULL, '2026-04-04 23:44:14.000000', 55, NULL, 0, 'ACTIVE', 'Tohru', '$2a$10$nLUL6QbfjC6CwxHK3FhrrepXQ1E4I9DHWfOXLd7Znrt.ydiDmDyuu', 'tohru@dragon.com'),
(0, b'0', NULL, '2026-04-04 23:44:28.000000', 56, NULL, 0, 'ACTIVE', 'Asuka Langley', '$2a$10$VR.GDtvNFIEpkEsLhEwt9.MTpzUQOMNr17xnDuhEwjBhkxr4BxX0i', 'asuka@nerv.com'),
(0, b'0', NULL, '2026-04-04 23:45:16.000000', 57, NULL, 0, 'ACTIVE', 'MisatoKatsuragi', '$2a$10$OVsg2cvzW56AQOUS1KeXYe9DdjksDw539D52gT0QZ3Uco829VS.8K', 'misato@nerv.com'),
(0, b'0', NULL, '2026-04-04 23:46:07.000000', 58, NULL, 0, 'ACTIVE', 'Fuko Ibuki', '$2a$10$catxI.uxQr2Cmc3sqYj40emCvpP/18A.fd.SOCq/a/KV4QBx50nSu', 'fuko@clannad.com'),
(0, b'0', NULL, '2026-04-04 23:46:25.000000', 59, NULL, 0, 'ACTIVE', 'Nagisa Furukawa', '$2a$10$4/MGmy0ZW6lnKJUpov7EGOb2.ywpmMHCEOTBZmBHYlDJfDsSlagru', 'nagisa@clannad.com'),
(0, b'0', NULL, '2026-04-04 23:46:38.000000', 60, NULL, 0, 'ACTIVE', 'Ushio Okazaki', '$2a$10$MraforSITDvLpMTzodMAuuEM9za7j7WLSktZEwsrcI3wOJMxIKIGS', 'ushio@clannad.com'),
(0, b'0', NULL, '2026-04-04 23:46:54.000000', 61, NULL, 0, 'ACTIVE', 'Mima', '$2a$10$mir586THA9WDbOZBNKzhcugOXjwGrUGFzeFuOKy.uM0LREROPqM1W', 'mima@perfectblue.com'),
(0, b'0', NULL, '2026-04-04 23:47:12.000000', 62, NULL, 0, 'ACTIVE', 'Lain Iwakura', '$2a$10$nrS1lp7NNH5fokBcKWQyhe01FM6HILw.J9LFZ2IOeb1I3n.CDG9FS', 'lain@wired.com'),
(0, b'0', NULL, '2026-04-04 23:47:28.000000', 63, NULL, 0, 'ACTIVE', 'Madoka Kaname', '$2a$10$4pRF1Wx9USTbrzxdTovwA./bRHBbIB9FZtM08jirl/998OqUBkH02', 'madoka@madoka.com'),
(0, b'0', NULL, '2026-04-04 23:47:46.000000', 64, NULL, 0, 'ACTIVE', 'Homura Akemi', '$2a$10$R14YLzgZ.MJuOX2o9cj1d.hqAy3T0WSSJrlekBj6MqV/5HYZL/Jmu', 'homura@madoka.com'),
(0, b'0', NULL, '2026-04-04 23:48:04.000000', 65, NULL, 0, 'ACTIVE', 'Mami Tomoe', '$2a$10$9AYHTq6imdGiIA6050rbA.NtT1RnhQ0Npe/ZYZAw10mxB7INuG8.W', 'mami@madoka.com'),
(0, b'0', NULL, '2026-04-04 23:48:18.000000', 66, NULL, 0, 'ACTIVE', 'Kyubey', '$2a$10$v9Zxp4/x1lsAYWj1i22AXOgxnDlWAcHUgVdvRq1BQQzvwW6ufXVom', 'kyubey@madoka.com'),
(0, b'0', NULL, '2026-04-04 23:48:31.000000', 67, NULL, 0, 'ACTIVE', 'Makima', '$2a$10$dS48YYoOWLm9chA3aY39yuz0H5cD5neIyGNmuKocFwWvmsljSVvHq', 'makima@chainsaw.com'),
(0, b'0', NULL, '2026-04-04 23:50:20.000000', 68, NULL, 0, 'ACTIVE', 'Shou Tucker', '$2a$10$d4pyzT95R/hdgOsfcOFPpOxf/oESji7TReRcecUKRqvvgVWiLWbtW', 'tucker.padre@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:50:51.000000', 69, NULL, 0, 'ACTIVE', 'Orochimaru', '$2a$10$F6wApR89buTnFV4I5IMGUOhzSdgTjmsTQgTtPAT3A/6i8nRYoZl6q', 'orochimaru.custodio@villanos.com'),
(0, b'0', NULL, '2026-04-04 23:51:11.000000', 70, NULL, 0, 'ACTIVE', 'Gendo Ikari', '$2a$10$EJ2.DxQMoSAHhiTgsOqjFOLj1fAgeuB67eRQt7EYaZhP0FXiy1bv6', 'gendo@nerv.com'),
(0, b'0', NULL, '2026-04-04 23:51:24.000000', 71, NULL, 0, 'ACTIVE', 'Bondrewd', '$2a$10$beQpZaVDngU6mOCSMFC63u03Opu9RHN0An11JmH9IFl6fcyBbv4t.', 'bondrewd@abyss.com'),
(0, b'0', NULL, '2026-04-04 23:51:40.000000', 72, NULL, 0, 'ACTIVE', 'Ragyo Kiryuin', '$2a$10$m5JWH6VpXvMb6.4/9vgH8OGDvfiiPxHkNOp/T.y8xKQtuH6Znkaga', 'ragyo@revocs.com');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asiento_contable`
--
ALTER TABLE `asiento_contable`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKllvtygsr2xnmyh07t5k764kli` (`id_empresa`);

--
-- Indices de la tabla `asignacion_rol_usuario`
--
ALTER TABLE `asignacion_rol_usuario`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6ryud7lv2sg939297s3dd1mtk` (`id_rol`),
  ADD KEY `FKj7tds1b3eqdokqetu90l1xo1y` (`id_usuario_identidad`);

--
-- Indices de la tabla `cita`
--
ALTER TABLE `cita`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKa7avajh4ned5r3gppf52rv267` (`id_servicio`),
  ADD KEY `FK7e54fqk7f9sgp3e2qxaxfhm40` (`id_dentista`),
  ADD KEY `FK7fljkhue1c7r80b4li70f6fh3` (`id_paciente`);

--
-- Indices de la tabla `contrato`
--
ALTER TABLE `contrato`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK72ui1925h72ll9k0gndyo2qej` (`id_empresa`),
  ADD KEY `FKkmycnpwcity39mkjiosasbekp` (`id_tercero`);

--
-- Indices de la tabla `cuenta_contable`
--
ALTER TABLE `cuenta_contable`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKoonbs1ixlgxupwbce8ph1huu2` (`id_empresa`);

--
-- Indices de la tabla `dentista`
--
ALTER TABLE `dentista`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKa6l8ig7m3t7vc6aidlmxxe4k5` (`id_turno`),
  ADD KEY `FKnaupnspprbx1c0cw90eb0re63` (`id_usuario_identidad`);

--
-- Indices de la tabla `empresa`
--
ALTER TABLE `empresa`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKa344uhvvn5iuti0u46a2e8no9` (`nit`);

--
-- Indices de la tabla `factura`
--
ALTER TABLE `factura`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKq4rschxwntd1d2yt1heie7l5j` (`numero_factura`),
  ADD KEY `FK378bm0f4e3vs7qudirgujo7gj` (`id_contrato`),
  ADD KEY `FK1jixe8usxn3pnhnhv7m9hqufm` (`id_dentista`),
  ADD KEY `FKnrgnnmsxe86o0o3csosc7iphr` (`id_paciente`);

--
-- Indices de la tabla `fase_tratamiento`
--
ALTER TABLE `fase_tratamiento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK21u5qqq370wynopeq9k37a6h2` (`id_tratamiento`);

--
-- Indices de la tabla `item_factura`
--
ALTER TABLE `item_factura`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK7rhof1rg2ggq6b4dk3wjgc7e5` (`id_servicio`),
  ADD KEY `FKme1a7hhgoobstbeb5p3m97xj5` (`id_factura`),
  ADD KEY `FKpl4bgyig8uh47j522qlkg7f0c` (`id_tarifa`);

--
-- Indices de la tabla `linea_asiento_contable`
--
ALTER TABLE `linea_asiento_contable`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmja44xutchfnuia6y76nust9x` (`id_cuenta_contable`),
  ADD KEY `FKa29bdh422vcvne14pu60t563f` (`id_asiento_contable`),
  ADD KEY `FKl49s7x3h4wclbal45klls3qsr` (`id_tercero`);

--
-- Indices de la tabla `paciente`
--
ALTER TABLE `paciente`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKbg8k9yhratpme3pkhnyjjumrq` (`id_contrato`),
  ADD KEY `FKq63017asfrcyp1h1dp8e4gps2` (`id_responsable`),
  ADD KEY `FKmep3pjye4ou8fy17e98ck2fj4` (`id_usuario_identidad`);

--
-- Indices de la tabla `pago`
--
ALTER TABLE `pago`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3d0wkpdsru2erd716s3q98j69` (`id_factura`);

--
-- Indices de la tabla `recepcionista`
--
ALTER TABLE `recepcionista`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKa1anq0oqqmit2qyhcluyghxod` (`id_usuario_identidad`);

--
-- Indices de la tabla `reporte_adjunto`
--
ALTER TABLE `reporte_adjunto`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK8ybxn70voksga8g432qkoxub4` (`id_reporte`);

--
-- Indices de la tabla `reporte_administrativo`
--
ALTER TABLE `reporte_administrativo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9sfefqrf9fwt1j684hffdb7hs` (`id_aprobado_por`),
  ADD KEY `FK3jcugx51vu3dk3tspoqe57046` (`id_creado_por`);

--
-- Indices de la tabla `reporte_indicador`
--
ALTER TABLE `reporte_indicador`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKs6g95vbugx50561mno2jms672` (`id_reporte`);

--
-- Indices de la tabla `reporte_referencia_asiento`
--
ALTER TABLE `reporte_referencia_asiento`
  ADD PRIMARY KEY (`id_asiento_contable`,`id_reporte`),
  ADD KEY `FKa1m4ax2bq7a922s5upkpnvlqd` (`id_reporte`);

--
-- Indices de la tabla `responsable`
--
ALTER TABLE `responsable`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhs22qma6nw0pk94tpu4eyh7tl` (`id_usuario_identidad`);

--
-- Indices de la tabla `resultado_cita`
--
ALTER TABLE `resultado_cita`
  ADD PRIMARY KEY (`id_cita`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `rol_permiso`
--
ALTER TABLE `rol_permiso`
  ADD PRIMARY KEY (`id_rol`,`permiso`);

--
-- Indices de la tabla `saldo_inicial`
--
ALTER TABLE `saldo_inicial`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKlgxs1gk57ix7emmggpspitjux` (`id_cuenta_contable`),
  ADD KEY `FKrreq7w3q9vfefi4b0logcgnd0` (`id_empresa`),
  ADD KEY `FK62u2y761gs6bwucv5hggcnd8l` (`id_tercero`);

--
-- Indices de la tabla `servicio_detalle_cirugia`
--
ALTER TABLE `servicio_detalle_cirugia`
  ADD PRIMARY KEY (`id_servicio`);

--
-- Indices de la tabla `servicio_detalle_estetico`
--
ALTER TABLE `servicio_detalle_estetico`
  ADD PRIMARY KEY (`id_servicio`);

--
-- Indices de la tabla `servicio_detalle_implantologia`
--
ALTER TABLE `servicio_detalle_implantologia`
  ADD PRIMARY KEY (`id_servicio`);

--
-- Indices de la tabla `servicio_detalle_ortodoncia`
--
ALTER TABLE `servicio_detalle_ortodoncia`
  ADD PRIMARY KEY (`id_servicio`);

--
-- Indices de la tabla `servicio_detalle_pediatria`
--
ALTER TABLE `servicio_detalle_pediatria`
  ADD PRIMARY KEY (`id_servicio`);

--
-- Indices de la tabla `servicio_detalle_protesis`
--
ALTER TABLE `servicio_detalle_protesis`
  ADD PRIMARY KEY (`id_servicio`);

--
-- Indices de la tabla `servicio_odontologico`
--
ALTER TABLE `servicio_odontologico`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKqdkeyimtqnm8xe1u3rchsrmys` (`codigo`);

--
-- Indices de la tabla `tarifa`
--
ALTER TABLE `tarifa`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKattr64w7ex51df9k5yxfrmg3m` (`id_contrato`),
  ADD KEY `FKcj76qclw1xfypf6vg0okmpkdj` (`id_servicio`);

--
-- Indices de la tabla `tercero`
--
ALTER TABLE `tercero`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK1b9ikti9mke551yjps55d0hjf` (`id_empresa`);

--
-- Indices de la tabla `tratamiento`
--
ALTER TABLE `tratamiento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKal4r7i1jo4r8omkye4rykcx0m` (`id_servicio`),
  ADD KEY `FKl1gr4e5fo0igy3rfh1lulupt3` (`id_dentista`),
  ADD KEY `FKj5q6975hjpmvw1hhcsrb8amed` (`id_paciente`),
  ADD KEY `FKapamgp57jd1jnkqv22p8knsk6` (`id_tarifa`);

--
-- Indices de la tabla `turno`
--
ALTER TABLE `turno`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKf7qshv991bqn8b7l649adg3el` (`id_dentista`);

--
-- Indices de la tabla `turno_bloque_excluido`
--
ALTER TABLE `turno_bloque_excluido`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK55to9is5rfc4wem899wunxpd6` (`id_turno`);

--
-- Indices de la tabla `usuario_identidad`
--
ALTER TABLE `usuario_identidad`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKrcy4g1rlrqa9gbswflfj85nhp` (`correo_electronico`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `asiento_contable`
--
ALTER TABLE `asiento_contable`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `asignacion_rol_usuario`
--
ALTER TABLE `asignacion_rol_usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=74;

--
-- AUTO_INCREMENT de la tabla `cita`
--
ALTER TABLE `cita`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `contrato`
--
ALTER TABLE `contrato`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cuenta_contable`
--
ALTER TABLE `cuenta_contable`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `dentista`
--
ALTER TABLE `dentista`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `empresa`
--
ALTER TABLE `empresa`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `factura`
--
ALTER TABLE `factura`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `fase_tratamiento`
--
ALTER TABLE `fase_tratamiento`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `item_factura`
--
ALTER TABLE `item_factura`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `linea_asiento_contable`
--
ALTER TABLE `linea_asiento_contable`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `paciente`
--
ALTER TABLE `paciente`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `pago`
--
ALTER TABLE `pago`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `recepcionista`
--
ALTER TABLE `recepcionista`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `reporte_adjunto`
--
ALTER TABLE `reporte_adjunto`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `reporte_administrativo`
--
ALTER TABLE `reporte_administrativo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `reporte_indicador`
--
ALTER TABLE `reporte_indicador`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `responsable`
--
ALTER TABLE `responsable`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `saldo_inicial`
--
ALTER TABLE `saldo_inicial`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `servicio_odontologico`
--
ALTER TABLE `servicio_odontologico`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `tarifa`
--
ALTER TABLE `tarifa`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tercero`
--
ALTER TABLE `tercero`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tratamiento`
--
ALTER TABLE `tratamiento`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `turno`
--
ALTER TABLE `turno`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `turno_bloque_excluido`
--
ALTER TABLE `turno_bloque_excluido`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuario_identidad`
--
ALTER TABLE `usuario_identidad`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asiento_contable`
--
ALTER TABLE `asiento_contable`
  ADD CONSTRAINT `FKllvtygsr2xnmyh07t5k764kli` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);

--
-- Filtros para la tabla `asignacion_rol_usuario`
--
ALTER TABLE `asignacion_rol_usuario`
  ADD CONSTRAINT `FK6ryud7lv2sg939297s3dd1mtk` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id`),
  ADD CONSTRAINT `FKj7tds1b3eqdokqetu90l1xo1y` FOREIGN KEY (`id_usuario_identidad`) REFERENCES `usuario_identidad` (`id`);

--
-- Filtros para la tabla `cita`
--
ALTER TABLE `cita`
  ADD CONSTRAINT `FK7e54fqk7f9sgp3e2qxaxfhm40` FOREIGN KEY (`id_dentista`) REFERENCES `dentista` (`id`),
  ADD CONSTRAINT `FK7fljkhue1c7r80b4li70f6fh3` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`),
  ADD CONSTRAINT `FKa7avajh4ned5r3gppf52rv267` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`);

--
-- Filtros para la tabla `contrato`
--
ALTER TABLE `contrato`
  ADD CONSTRAINT `FK72ui1925h72ll9k0gndyo2qej` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`),
  ADD CONSTRAINT `FKkmycnpwcity39mkjiosasbekp` FOREIGN KEY (`id_tercero`) REFERENCES `tercero` (`id`);

--
-- Filtros para la tabla `cuenta_contable`
--
ALTER TABLE `cuenta_contable`
  ADD CONSTRAINT `FKoonbs1ixlgxupwbce8ph1huu2` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);

--
-- Filtros para la tabla `dentista`
--
ALTER TABLE `dentista`
  ADD CONSTRAINT `FKa6l8ig7m3t7vc6aidlmxxe4k5` FOREIGN KEY (`id_turno`) REFERENCES `turno` (`id`),
  ADD CONSTRAINT `FKnaupnspprbx1c0cw90eb0re63` FOREIGN KEY (`id_usuario_identidad`) REFERENCES `usuario_identidad` (`id`);

--
-- Filtros para la tabla `factura`
--
ALTER TABLE `factura`
  ADD CONSTRAINT `FK1jixe8usxn3pnhnhv7m9hqufm` FOREIGN KEY (`id_dentista`) REFERENCES `dentista` (`id`),
  ADD CONSTRAINT `FK378bm0f4e3vs7qudirgujo7gj` FOREIGN KEY (`id_contrato`) REFERENCES `contrato` (`id`),
  ADD CONSTRAINT `FKnrgnnmsxe86o0o3csosc7iphr` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`);

--
-- Filtros para la tabla `fase_tratamiento`
--
ALTER TABLE `fase_tratamiento`
  ADD CONSTRAINT `FK21u5qqq370wynopeq9k37a6h2` FOREIGN KEY (`id_tratamiento`) REFERENCES `tratamiento` (`id`);

--
-- Filtros para la tabla `item_factura`
--
ALTER TABLE `item_factura`
  ADD CONSTRAINT `FK7rhof1rg2ggq6b4dk3wjgc7e5` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`),
  ADD CONSTRAINT `FKme1a7hhgoobstbeb5p3m97xj5` FOREIGN KEY (`id_factura`) REFERENCES `factura` (`id`),
  ADD CONSTRAINT `FKpl4bgyig8uh47j522qlkg7f0c` FOREIGN KEY (`id_tarifa`) REFERENCES `tarifa` (`id`);

--
-- Filtros para la tabla `linea_asiento_contable`
--
ALTER TABLE `linea_asiento_contable`
  ADD CONSTRAINT `FKa29bdh422vcvne14pu60t563f` FOREIGN KEY (`id_asiento_contable`) REFERENCES `asiento_contable` (`id`),
  ADD CONSTRAINT `FKl49s7x3h4wclbal45klls3qsr` FOREIGN KEY (`id_tercero`) REFERENCES `tercero` (`id`),
  ADD CONSTRAINT `FKmja44xutchfnuia6y76nust9x` FOREIGN KEY (`id_cuenta_contable`) REFERENCES `cuenta_contable` (`id`);

--
-- Filtros para la tabla `paciente`
--
ALTER TABLE `paciente`
  ADD CONSTRAINT `FKbg8k9yhratpme3pkhnyjjumrq` FOREIGN KEY (`id_contrato`) REFERENCES `contrato` (`id`),
  ADD CONSTRAINT `FKmep3pjye4ou8fy17e98ck2fj4` FOREIGN KEY (`id_usuario_identidad`) REFERENCES `usuario_identidad` (`id`),
  ADD CONSTRAINT `FKq63017asfrcyp1h1dp8e4gps2` FOREIGN KEY (`id_responsable`) REFERENCES `responsable` (`id`);

--
-- Filtros para la tabla `pago`
--
ALTER TABLE `pago`
  ADD CONSTRAINT `FK3d0wkpdsru2erd716s3q98j69` FOREIGN KEY (`id_factura`) REFERENCES `factura` (`id`);

--
-- Filtros para la tabla `recepcionista`
--
ALTER TABLE `recepcionista`
  ADD CONSTRAINT `FKa1anq0oqqmit2qyhcluyghxod` FOREIGN KEY (`id_usuario_identidad`) REFERENCES `usuario_identidad` (`id`);

--
-- Filtros para la tabla `reporte_adjunto`
--
ALTER TABLE `reporte_adjunto`
  ADD CONSTRAINT `FK8ybxn70voksga8g432qkoxub4` FOREIGN KEY (`id_reporte`) REFERENCES `reporte_administrativo` (`id`);

--
-- Filtros para la tabla `reporte_administrativo`
--
ALTER TABLE `reporte_administrativo`
  ADD CONSTRAINT `FK3jcugx51vu3dk3tspoqe57046` FOREIGN KEY (`id_creado_por`) REFERENCES `usuario_identidad` (`id`),
  ADD CONSTRAINT `FK9sfefqrf9fwt1j684hffdb7hs` FOREIGN KEY (`id_aprobado_por`) REFERENCES `usuario_identidad` (`id`);

--
-- Filtros para la tabla `reporte_indicador`
--
ALTER TABLE `reporte_indicador`
  ADD CONSTRAINT `FKs6g95vbugx50561mno2jms672` FOREIGN KEY (`id_reporte`) REFERENCES `reporte_administrativo` (`id`);

--
-- Filtros para la tabla `reporte_referencia_asiento`
--
ALTER TABLE `reporte_referencia_asiento`
  ADD CONSTRAINT `FK4crpe2y8r1pks10vfpve8aow5` FOREIGN KEY (`id_asiento_contable`) REFERENCES `asiento_contable` (`id`),
  ADD CONSTRAINT `FKa1m4ax2bq7a922s5upkpnvlqd` FOREIGN KEY (`id_reporte`) REFERENCES `reporte_administrativo` (`id`);

--
-- Filtros para la tabla `responsable`
--
ALTER TABLE `responsable`
  ADD CONSTRAINT `FKhs22qma6nw0pk94tpu4eyh7tl` FOREIGN KEY (`id_usuario_identidad`) REFERENCES `usuario_identidad` (`id`);

--
-- Filtros para la tabla `resultado_cita`
--
ALTER TABLE `resultado_cita`
  ADD CONSTRAINT `FKla4oh5r5bruk3yd82d9fcbohd` FOREIGN KEY (`id_cita`) REFERENCES `cita` (`id`);

--
-- Filtros para la tabla `rol_permiso`
--
ALTER TABLE `rol_permiso`
  ADD CONSTRAINT `FKsxc3d8lmtj7em6n8j0wl4jwco` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id`);

--
-- Filtros para la tabla `saldo_inicial`
--
ALTER TABLE `saldo_inicial`
  ADD CONSTRAINT `FK62u2y761gs6bwucv5hggcnd8l` FOREIGN KEY (`id_tercero`) REFERENCES `tercero` (`id`),
  ADD CONSTRAINT `FKlgxs1gk57ix7emmggpspitjux` FOREIGN KEY (`id_cuenta_contable`) REFERENCES `cuenta_contable` (`id`),
  ADD CONSTRAINT `FKrreq7w3q9vfefi4b0logcgnd0` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);

--
-- Filtros para la tabla `servicio_detalle_cirugia`
--
ALTER TABLE `servicio_detalle_cirugia`
  ADD CONSTRAINT `FKe8uerl9qim69te2u4a7x6vud1` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`);

--
-- Filtros para la tabla `servicio_detalle_estetico`
--
ALTER TABLE `servicio_detalle_estetico`
  ADD CONSTRAINT `FKky37udyawxkf65wwhbd7om22g` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`);

--
-- Filtros para la tabla `servicio_detalle_implantologia`
--
ALTER TABLE `servicio_detalle_implantologia`
  ADD CONSTRAINT `FKsqqugjudskdevnkpyi6yym6m7` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`);

--
-- Filtros para la tabla `servicio_detalle_ortodoncia`
--
ALTER TABLE `servicio_detalle_ortodoncia`
  ADD CONSTRAINT `FKg2e2kw8editbs98fgt799exwj` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`);

--
-- Filtros para la tabla `servicio_detalle_pediatria`
--
ALTER TABLE `servicio_detalle_pediatria`
  ADD CONSTRAINT `FKj3fa0q2l1hf10lj1buqgl026a` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`);

--
-- Filtros para la tabla `servicio_detalle_protesis`
--
ALTER TABLE `servicio_detalle_protesis`
  ADD CONSTRAINT `FK9qldya9viggvysjxen5oyiv86` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`);

--
-- Filtros para la tabla `tarifa`
--
ALTER TABLE `tarifa`
  ADD CONSTRAINT `FKattr64w7ex51df9k5yxfrmg3m` FOREIGN KEY (`id_contrato`) REFERENCES `contrato` (`id`),
  ADD CONSTRAINT `FKcj76qclw1xfypf6vg0okmpkdj` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`);

--
-- Filtros para la tabla `tercero`
--
ALTER TABLE `tercero`
  ADD CONSTRAINT `FK1b9ikti9mke551yjps55d0hjf` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`id`);

--
-- Filtros para la tabla `tratamiento`
--
ALTER TABLE `tratamiento`
  ADD CONSTRAINT `FKal4r7i1jo4r8omkye4rykcx0m` FOREIGN KEY (`id_servicio`) REFERENCES `servicio_odontologico` (`id`),
  ADD CONSTRAINT `FKapamgp57jd1jnkqv22p8knsk6` FOREIGN KEY (`id_tarifa`) REFERENCES `tarifa` (`id`),
  ADD CONSTRAINT `FKj5q6975hjpmvw1hhcsrb8amed` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`),
  ADD CONSTRAINT `FKl1gr4e5fo0igy3rfh1lulupt3` FOREIGN KEY (`id_dentista`) REFERENCES `dentista` (`id`);

--
-- Filtros para la tabla `turno`
--
ALTER TABLE `turno`
  ADD CONSTRAINT `FKf7qshv991bqn8b7l649adg3el` FOREIGN KEY (`id_dentista`) REFERENCES `dentista` (`id`);

--
-- Filtros para la tabla `turno_bloque_excluido`
--
ALTER TABLE `turno_bloque_excluido`
  ADD CONSTRAINT `FK55to9is5rfc4wem899wunxpd6` FOREIGN KEY (`id_turno`) REFERENCES `turno` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
