	-------------------------------------------------------------------
	--Crear ADM_DIS_FINAL
	-------------------------------------------------------------------
	
	
CREATE TABLE ADM_DIS_FINAL
  (
    IDE_DIS_FINAL      NUMBER NOT NULL ,
    NOM_DIS_FINAL      VARCHAR2(100 BYTE) NOT NULL ,
    DES_DIS_FINAL      VARCHAR2(100 BYTE) ,
    STA_DIS_FINAL      VARCHAR2(2 BYTE) ,
    IDE_USUARIO_CAMBIO NUMBER(10, 0) ,
    FEC_CAMBIO         DATE,
    NIV_LECTURA        NUMBER(1, 0) ,
    NIV_ESCRITURA      NUMBER(1, 0) ,
    IDE_UUID           CHAR(32 BYTE) ,
    FEC_CREACION       DATE
  );

	CREATE UNIQUE INDEX ADM_DIS_FINAL_PK ON ADM_DIS_FINAL
  (
    IDE_DIS_FINAL ASC
  );
  
  ALTER TABLE ADM_DIS_FINAL ADD CONSTRAINT ADM_DIS_FINAL_PK PRIMARY KEY ( IDE_DIS_FINAL ) USING INDEX ADM_DIS_FINAL_PK ENABLE;
  COMMENT ON COLUMN ADM_DIS_FINAL.STA_DIS_FINAL IS 'Estado de la dispocicion final';
  
  Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_DISFINAL_SEQ','1');