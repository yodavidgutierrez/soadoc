/*-------------------------------------------------------------------
  --Crear DCT_ASIG_ULTIMO
------------------------------------------------------------------- */
CREATE TABLE DCT_ASIG_ULTIMO 
(
  IDE_ASIG_ULTIMO NUMBER(18, 0) NOT NULL 
, IDE_DOCUMENTO NUMBER(18, 0) NOT NULL 
, IDE_AGENTE NUMBER(18, 0) NOT NULL 
, IDE_ASIGNACION NUMBER(18, 0) NOT NULL 
, NUM_REDIRECCIONES VARCHAR2(8 BYTE) DEFAULT NULL 
, IDE_USUARIO_CREO VARCHAR2(8 BYTE) NOT NULL 
, FEC_CREO TIMESTAMP(6) NOT NULL 
, IDE_USUARIO_CAMBIO NUMBER(10, 0) NOT NULL 
, FEC_CAMBIO TIMESTAMP(6) NOT NULL 
, NIV_LECTURA NUMBER(1, 0) DEFAULT NULL 
, NIV_ESCRITURA NUMBER(1, 0) DEFAULT NULL 
, FECHA_VENCIMIENTO TIMESTAMP(6) DEFAULT NULL 
, ID_INSTANCIA VARCHAR2(50 BYTE) DEFAULT NULL 
, COD_TIP_PROCESO VARCHAR2(8 BYTE) DEFAULT NULL 
, CONSTRAINT PK_DCT_ASIG_ULTIMO PRIMARY KEY 
  (
    IDE_ASIG_ULTIMO 
  )
  ENABLE 
);

ALTER TABLE DCT_ASIG_ULTIMO
ADD CONSTRAINT DCT_ASIG_ULTIMO_COR_AGEN_FK FOREIGN KEY
(
  IDE_AGENTE 
)
REFERENCES COR_AGENTE
(
  IDE_AGENTE 
)
ENABLE;

ALTER TABLE DCT_ASIG_ULTIMO
ADD CONSTRAINT DCT_ASIG_ULTIMO_COR_CORRES_FK FOREIGN KEY
(
  IDE_DOCUMENTO 
)
REFERENCES COR_CORRESPONDENCIA
(
  IDE_DOCUMENTO 
)
ENABLE;

ALTER TABLE DCT_ASIG_ULTIMO
ADD CONSTRAINT DCT_ASIG_ULTIMO_IDE_ASIG_FK FOREIGN KEY
(
  IDE_ASIGNACION 
)
REFERENCES DCT_ASIGNACION
(
  )
ENABLE;
