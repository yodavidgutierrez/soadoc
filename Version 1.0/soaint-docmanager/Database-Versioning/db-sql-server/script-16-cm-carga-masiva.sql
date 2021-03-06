-------------------------------------------------------------------
  --Crear CM_CARGA_MASIVA
  -------------------------------------------------------------------
  
  CREATE TABLE CM_CARGA_MASIVA
( 
	ID NUMERIC NOT NULL,
	NOMBRE NVARCHAR(100),
	FECHA_CREACION DATETIME2(6),
	TOTAL_REGISTROS NUMERIC,
	ESTADO NVARCHAR(30),
	TOTAL_REGISTROS_EXITOSOS NUMERIC(4),
	TOTAL_REGISTROS_ERROR NUMERIC(4)
);
ALTER TABLE CM_CARGA_MASIVA ADD CONSTRAINT CM_CARGA_MASIVA_PK
	PRIMARY KEY (ID);

  
  Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('CM_CARGA_MASIVA_SEQ','1');
  