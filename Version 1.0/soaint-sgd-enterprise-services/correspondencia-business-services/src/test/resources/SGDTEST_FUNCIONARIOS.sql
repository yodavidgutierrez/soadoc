CREATE TABLE SGDTEST.FUNCIONARIOS
(
    IDE_FUNCI NUMBER(18) PRIMARY KEY NOT NULL,
    COD_TIP_DOC_IDENT VARCHAR2(8) NOT NULL,
    NRO_IDENTIFICACION VARCHAR2(38) NOT NULL,
    NOM_FUNCIONARIO VARCHAR2(100) NOT NULL,
    VAL_APELLIDO1 VARCHAR2(50) NOT NULL,
    VAL_APELLIDO2 VARCHAR2(50) DEFAULT NULL 
,
    CORR_ELECTRONICO VARCHAR2(100) DEFAULT NULL 
,
    LOGIN_NAME VARCHAR2(150) NOT NULL,
    ESTADO VARCHAR2(2 char) DEFAULT NULL 
,
    FEC_CAMBIO TIMESTAMP(6),
    FEC_CREACION TIMESTAMP(6),
    COD_USUARIO_CREA VARCHAR2(20),
    CREDENCIALES VARCHAR2(50)
);
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (2, 'CTDI', '83031630106', 'Alberto', 'Arce', null, 'alber6@gmail.com', 'arce', 'A', TO_TIMESTAMP('2018-04-19 11:01:39.705000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, null, 'YXJjZTpudWxs');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (16, 'CTDI', '11111111111', 'Eric', 'Rodriguez', 'Nadal', 'ericrnadal@gmail.com', 'eric.rodriguez', 'A', null, null, null, 'ZXJpYy5yb2RyaWd1ZXo6ZGVzY2FyZ2E=');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (3, 'CTDI', '11111111111', 'Daniel', 'Barrios', null, 'dbarrios1907@gmail.com', 'daniel.barrios', 'A', TO_TIMESTAMP('2018-01-11 15:48:28.385000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, null, 'ZGFuaWVsLmJhcnJpb3M6bnVsbA==');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (4, 'CTDI', '11111111111', 'Leinier', 'Alvarez', null, 'leinieralvarez@gmail.com', 'leinier.alvarez', 'A', TO_TIMESTAMP('2018-01-12 11:24:40.449000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, null, 'bGVpbmllci5hbHZhcmV6Om51bGw=');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (5, 'CTDI', '11111111111', 'Dasiel', 'Otero', null, 'dasiel@gmail.com', 'dasiel.otero', 'A', null, null, null, 'ZGFzaWVsLm90ZXJvOmRlc2Nhcmdh');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (1, 'CTDI', '78120228106', 'Ernesto', 'Sanchez', 'Aliaga', 'esanchez7802@gmail.com', 'ernesto.sanchez', 'A', TO_TIMESTAMP('2018-01-11 11:51:54.358000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, 'eric.rodriguez', 'ZXJuZXN0by5zYW5jaGV6Om51bGw=');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (6, 'CTDI', '11111111111', 'Jorge', 'Infante', null, 'jorge.softdevelop@gmail.com', 'jorge.infante', 'A', null, null, null, 'am9yZ2UuaW5mYW50ZTpkZXNjYXJnYQ==');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (15, 'CTDI', '11111111111', 'Harvet', 'Gonzalez', 'Rojas', 'harvet2504@gmail.com', 'harvet.gonzalez', 'A', null, null, null, 'aGFydmV0LmdvbnphbGV6OmRlc2Nhcmdh');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (17, 'CTDI', '78120228106', 'JC', 'Prieto', 'Alvarez', 'jcprieto@gmail.com', 'julio.c.prueba', 'A', null, null, null, 'anVsaW8uYy5wcnVlYmE6bnVsbA==');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (13, 'DNI', '11111111111', 'Macuto', 'Hernandez', 'Gutierrez', 'macuto@gmail.com', 'macuto', 'A', null, null, null, 'bWFjdXRvOm1hY3V0bw==');
INSERT INTO SGDTEST.FUNCIONARIOS (IDE_FUNCI, COD_TIP_DOC_IDENT, NRO_IDENTIFICACION, NOM_FUNCIONARIO, VAL_APELLIDO1, VAL_APELLIDO2, CORR_ELECTRONICO, LOGIN_NAME, ESTADO, FEC_CAMBIO, FEC_CREACION, COD_USUARIO_CREA, CREDENCIALES) VALUES (14, 'CTDI', '11111111111', 'Ingrid', 'Toscano', 'Useche', 'itoscano@soaint.com', 'ingrid', 'A', null, null, null, 'aW5ncmlkOmRlc2Nhcmdh');