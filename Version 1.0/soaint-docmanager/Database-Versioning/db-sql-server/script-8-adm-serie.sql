 -------------------------------------------------------------------
  --Crear ADM_SERIE
  -------------------------------------------------------------------  
  CREATE TABLE ADM_SERIE
( 
	IDE_SERIE NUMERIC(19) NOT NULL,
	ACT_ADMINISTRATIVO NVARCHAR(1020) NOT NULL,
	CAR_ADMINISTRATIVA NUMERIC(10),
	CAR_LEGAL NUMERIC(10),
	CAR_TECNICA NUMERIC(10),
	COD_SERIE NVARCHAR(1020) NOT NULL,
	EST_SERIE NUMERIC(5) NOT NULL,
	FEC_CAMBIO DATETIME2(6),
	FEC_CREACION DATETIME2(6),
	FUE_BIBLIOGRAFICA NVARCHAR(1020),
	IDE_USUARIO_CAMBIO NUMERIC(19),
	IDE_UUID NVARCHAR(1020),
	NIV_ESCRITURA NUMERIC(5),
	NIV_LECTURA NUMERIC(5),
	NOM_SERIE NVARCHAR(1020) NOT NULL,
	NOT_ALCANCE NVARCHAR(1200),
	IDE_MOT_CREACION NUMERIC(19) NOT NULL
)
ALTER TABLE ADM_SERIE ADD CONSTRAINT ADM_SERIE_PK
	PRIMARY KEY (IDE_SERIE);
	
ALTER TABLE ADM_SERIE ADD CONSTRAINT SERI_UK
	UNIQUE (COD_SERIE);
  
  Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_SERIE_SEQ','1');