USE docmanager
GO

CREATE TABLE ADM_REL_EQ_DESTINO
(


   IDE_REL_DESTINO NUMERIC(20)  NOT NULL,
   IDE_REL_ORIGEN NUMERIC(20)  NULL,
   IDE_UNI_AMT NVARCHAR(20)  NOT NULL,
   IDE_OFC_PROD NVARCHAR(20)  NOT NULL,
   IDE_SERIE NUMERIC(19)  NULL,
   IDE_SUBSERIE NUMERIC(19)  NULL,
   FEC_CREACION DATETIME2(6)  NULL
)

ALTER TABLE ADM_REL_EQ_DESTINO ADD CONSTRAINT ADM_REL_EQ_DESTINO_PK1 PRIMARY KEY (IDE_REL_DESTINO)

ALTER TABLE ADM_REL_EQ_DESTINO ADD CONSTRAINT ADM_REL_EQ_DESTINO_FK1 FOREIGN KEY (IDE_SERIE) REFERENCES ADM_SERIE (IDE_SERIE)
ALTER TABLE ADM_REL_EQ_DESTINO ADD CONSTRAINT ADM_REL_EQ_DESTINO_FK2 FOREIGN KEY (IDE_SUBSERIE) REFERENCES ADM_SUBSERIE (IDE_SUBSERIE)
ALTER TABLE ADM_REL_EQ_DESTINO ADD CONSTRAINT ADM_REL_EQ_DESTINO_FK3 FOREIGN KEY (IDE_REL_ORIGEN) REFERENCES ADM_REL_EQ_ORIGEN (IDE_REL_ORIGEN)

Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_REL_EQ_DES_SEQ','0');