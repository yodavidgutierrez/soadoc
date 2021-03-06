 -------------------------------------------------------------------
  --Crear ADM_TAB_RET_DOC
  -------------------------------------------------------------------
CREATE TABLE ADM_TAB_RET_DOC
( 
	IDE_TAB_RET_DOC NUMERIC(19) NOT NULL,
	AC_TRD NUMERIC(19) NOT NULL,
	AG_TRD NUMERIC(19) NOT NULL,
	EST_TAB_RET_DOC NUMERIC(5),
	FEC_CAMBIO DATETIME2(6),
	FEC_CREACION DATETIME2(6),
	IDE_USUARIO_CAMBIO NUMERIC(19),
	IDE_UUID NVARCHAR(1020),
	NIV_ESCRITURA NUMERIC(5),
	NIV_LECTURA NUMERIC(5),
	PRO_TRD NVARCHAR(2000) NOT NULL,
	IDE_DIS_FINAL NUMERIC(19) NOT NULL,
	IDE_SERIE NUMERIC(19) NOT NULL,
	IDE_SUBSERIE NUMERIC(19),
	IDE_OFC_PROD NVARCHAR(20) NOT NULL,
	IDE_UNI_AMT NVARCHAR(20) NOT NULL
);
ALTER TABLE ADM_TAB_RET_DOC ADD CONSTRAINT TRDO_PK
	PRIMARY KEY (IDE_TAB_RET_DOC);
ALTER TABLE ADM_TAB_RET_DOC ADD CONSTRAINT TRDO_SERI_FK
	FOREIGN KEY (IDE_SERIE)
		REFERENCES ADM_SERIE (IDE_SERIE);
ALTER TABLE ADM_TAB_RET_DOC ADD CONSTRAINT TRDO_SUBS_FK
	FOREIGN KEY (IDE_SUBSERIE)
		REFERENCES ADM_SUBSERIE (IDE_SUBSERIE);
  
  
  Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_TAB_RET_DOC_SEQ', '1');