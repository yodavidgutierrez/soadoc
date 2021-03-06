/*-------------------------------------------------------------------
  --Crear TVS_PAIS
------------------------------------------------------------------- */

CREATE TABLE TVS_PAIS 
(
  IDE_PAIS NUMBER(18, 0) NOT NULL 
, NOMBRE_PAIS VARCHAR2(64 BYTE) DEFAULT NULL 
, COD_PAIS VARCHAR2(7 BYTE) NOT NULL 
, ESTADO CHAR(2 BYTE) DEFAULT NULL 
, FEC_CAMBIO TIMESTAMP(6) DEFAULT NULL 
, FEC_CREACION TIMESTAMP(6) DEFAULT NULL 
, COD_USUARIO_CREA VARCHAR2(20 BYTE) DEFAULT NULL 
, CONSTRAINT TVS_PAIS_PK PRIMARY KEY 
  (
    IDE_PAIS 
  )
  ENABLE 
);