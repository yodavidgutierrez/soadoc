---------------------------------------------------------------------------------
-- AJUSTES SOLICITADOS -  CONSTANTES
---------------------------------------------------------------------------------

-- INSERTAR CONSTANTES

INSERT INTO TVS_CONSTANTES (IDE_CONST, CODIGO, NOMBRE, COD_PADRE, ESTADO) VALUES (204, 'TP-MNT', 'Motivo de No Tramitacion UD', null, 'A ');
INSERT INTO TVS_CONSTANTES (IDE_CONST, CODIGO, NOMBRE, COD_PADRE, ESTADO) VALUES (205, 'TP-MNTN', 'Ya existe', 'TP-MNT', 'A ');



DROP TABLE TVS_SOLICITUD_UD;

CREATE TABLE TVS_SOLICITUD_UD
(
  ID NUMBER(18) PRIMARY KEY NOT NULL,
  ID_UM VARCHAR2(8) DEFAULT NULL
  ,
  ID_CONSTANTE VARCHAR2(8) DEFAULT NULL
  ,
  FEC_HORA TIMESTAMP(6) DEFAULT SYSTIMESTAMP
  ,
  NOMBRE_UNIDAD_DOCUMENTAL VARCHAR2(50) DEFAULT NULL
  ,
  DESCRIPTOR1 VARCHAR2(50) DEFAULT NULL
  ,
  DESCRIPTOR2 VARCHAR2(50) DEFAULT NULL
  ,
  NRO VARCHAR2(50) DEFAULT NULL
  ,
  CODIGO_SERIE VARCHAR2(50) DEFAULT NULL
  ,
  CODIGO_SUBSERIE VARCHAR2(50) DEFAULT NULL
  ,
  CODIGO_SEDE VARCHAR2(50) DEFAULT NULL
  ,
  CODIGO_DEPENDENCIA VARCHAR2(50) DEFAULT NULL
  ,
  ID_SOLICITANTE VARCHAR2(8) DEFAULT NULL
  ,
  ESTADO VARCHAR2(250) DEFAULT NULL
  ,
  ACCION VARCHAR2(100) DEFAULT NULL
  ,
  OBSERVACIONES VARCHAR2(250) DEFAULT NULL

);

INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (1, '4949449', null, TO_TIMESTAMP('2018-05-06 19:23:25.474000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor2', 'e4c22850-8d12-4f92-9421-ee87490b2345', '30000', '564', '1040', '10401040', '2', null, null, 'Observaciones');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (2, '4949450', null, TO_TIMESTAMP('2018-05-06 19:23:25.474000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor2', 'e4c22850-8d12-4f92-9421-ee87490b2345', '30000', '564', '1040', '10401040', '2', null, null, 'Observaciones');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (3, '4949450', null, TO_TIMESTAMP('2018-05-06 19:23:39.930000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor2', 'e4c22850-8d12-4f92-9421-ee87490b2345', '30000', '564', '1040', '10401040', '2', null, null, 'Observaciones');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (4, '4949450', null, TO_TIMESTAMP('2018-05-05 20:08:09.600000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor2', 'e4c22850-8d12-4f92-9421-ee87490b2345', '30000', '564', '1040', '10401040', '2', null, null, 'Observaciones');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (9, '4949450', null, TO_TIMESTAMP('2018-05-05 20:38:15.195000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor2', 'e4c22850-8d12-4f92-9421-ee87490b2345', '30000', '564', '1040', '10401040', '2', null, null, 'Probando fecha temporal Date.');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (10, '4949450', null, TO_TIMESTAMP('2018-05-05 00:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor2', 'e4c22850-8d12-4f92-9421-ee87490b2345', '30000', '564', '1040', '10401040', '2', null, null, 'Probando fecha temporal Date.');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (14, '4189448', null, TO_TIMESTAMP('2018-05-07 10:59:58.393000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor 2', 'b243c296-7d04-4bf1-9735-1341446f38f7', '30000', '564', '1040', '10401040', '2', null, null, 'Observaciones');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (15, null, null, TO_TIMESTAMP('2018-05-07 11:00:42.431000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'UniDocuCreadaporJorge', 'Descriptor1', 'Descriptor2', null, '0231', '02311', '1000', '10001040', '2', null, null, null);
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (16, '4494950', 'idconst', TO_TIMESTAMP('2018-05-07 11:04:07.622000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor1 ', 'Descriptor2 ', '7251bc06-37e1-4afd-8fea-811340b98b75', '30000', '564', '1040', '10401040', '2', '1', 'actualizado', 'Observaciones...');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (17, '4949450', null, TO_TIMESTAMP('2018-05-07 11:26:31.223000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor2', 'e4c22850-8d12-4f92-9421-ee87490b2345', '30000', '564', '1040', '10401040', '2', null, null, 'Probando fecha temporal Date.');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (23, '54999', null, TO_TIMESTAMP('2018-05-08 17:55:34.912000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor 2', '3bbaac7a-47dc-47c4-98b3-aebc54e9009f', '30000', '564', '1040', '10401040', '2', null, null, 'Observaciones');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (18, '444994', null, TO_TIMESTAMP('2018-05-07 20:57:41.505000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor 2', '91f2ce06-9481-4c00-86f8-d6c345f9b68d', '30000', '564', '1040', '10401040', '2', null, null, 'Probando esto');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (19, '965844', null, TO_TIMESTAMP('2018-05-07 21:14:03.527000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor 2', '6ae70a71-741c-4c7f-bdb5-327063166091', '30000', '564', '1040', '10401040', '2', null, null, 'Probando');
INSERT INTO TVS_SOLICITUD_UD (ID, ID_UM, ID_CONSTANTE, FEC_HORA, NOMBRE_UNIDAD_DOCUMENTAL, DESCRIPTOR1, DESCRIPTOR2, NRO, CODIGO_SERIE, CODIGO_SUBSERIE, CODIGO_SEDE, CODIGO_DEPENDENCIA, ID_SOLICITANTE, ESTADO, ACCION, OBSERVACIONES) VALUES (24, '4949450', null, TO_TIMESTAMP('2018-05-09 10:10:04.938000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'Probando', 'Descriptor 1', 'Descriptor2', 'e4c22850-8d12-4f92-9421-ee87490b2345', '30000', '564', '1040', '10401040', '2', null, null, 'Probando crear.');