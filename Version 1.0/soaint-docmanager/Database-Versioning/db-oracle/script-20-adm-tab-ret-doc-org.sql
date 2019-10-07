--------------------------------------------------------
-- Archivo creado  - lunes-noviembre-28-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table ADM_TAB_RET_DOC_ORG
--------------------------------------------------------

  CREATE TABLE "ADM_TAB_RET_DOC_ORG" 
   (	"IDE_OFC_PROD" VARCHAR2(20 BYTE), 
	"IDE_UNI_AMT" VARCHAR2(20 BYTE), 
	"IDE_TAB_RET_DOC_ORG" NUMBER(19,2), 
	"IDE_TAB_RET_DOC" NUMBER(19,2), 
	"AC_TRD" NUMBER(19,2), 
	"AG_TRD" NUMBER(19,2), 
	"PRO_TRD" VARCHAR2(500 CHAR), 
	"IDE_DIS_FINAL" NUMBER(19,2), 
	"FEC_CAMBIO" TIMESTAMP (6), 
	"FEC_CREACION" TIMESTAMP (6), 
	"IDE_USUARIO_CAMBIO" NUMBER(19,0), 
	"IDE_UUID" VARCHAR2(255 CHAR), 
	"NIV_ESCRITURA" NUMBER(5,0), 
	"NIV_LECTURA" NUMBER(5,0), 
	"NUM_VERSION" VARCHAR2(20 BYTE)
   );

   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."IDE_OFC_PROD" IS 'Identificador del organigrama, oficina productora';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."IDE_UNI_AMT" IS 'Identificador del organigrama, unidad administrativa';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."IDE_TAB_RET_DOC_ORG" IS 'Identificador de la tabla';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."IDE_TAB_RET_DOC" IS 'Identificador de la tabla de retencion documental';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."AC_TRD" IS 'Hace referencia a la Retencion AC asociada a la TRD';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."AG_TRD" IS 'Hace referencia a la Retencion AG asociada a la TRD';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."PRO_TRD" IS 'Hace referencia al procedimiento asociada a la TRD';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."IDE_DIS_FINAL" IS 'Hace referencia al ide de disposicion final asociada a la TRD';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."FEC_CAMBIO" IS 'Fecha de cambio.';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."FEC_CREACION" IS 'Fecha de creacion';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."IDE_USUARIO_CAMBIO" IS 'Identificador usuario que cambio.';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."IDE_UUID" IS 'Identificacion UUID.';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."NIV_ESCRITURA" IS 'Nivel de escritura.';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."NIV_LECTURA" IS 'Nivel de lectura.';
   COMMENT ON COLUMN "ADM_TAB_RET_DOC_ORG"."NUM_VERSION" IS 'Identificador del número de versión';
--------------------------------------------------------
--  DDL for Index ADM_TAB_RET_DOC_ORG_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ADM_TAB_RET_DOC_ORG_PK" ON "ADM_TAB_RET_DOC_ORG" ("IDE_TAB_RET_DOC_ORG");
--------------------------------------------------------
--  Constraints for Table ADM_TAB_RET_DOC_ORG
--------------------------------------------------------

  ALTER TABLE "ADM_TAB_RET_DOC_ORG" ADD CONSTRAINT "ADM_TAB_RET_DOC_ORG_PK" PRIMARY KEY ("IDE_TAB_RET_DOC_ORG");
  ALTER TABLE "ADM_TAB_RET_DOC_ORG" MODIFY ("NUM_VERSION" NOT NULL ENABLE);
  ALTER TABLE "ADM_TAB_RET_DOC_ORG" MODIFY ("IDE_DIS_FINAL" NOT NULL ENABLE);
  ALTER TABLE "ADM_TAB_RET_DOC_ORG" MODIFY ("PRO_TRD" NOT NULL ENABLE);
  ALTER TABLE "ADM_TAB_RET_DOC_ORG" MODIFY ("IDE_TAB_RET_DOC" NOT NULL ENABLE);
  ALTER TABLE "ADM_TAB_RET_DOC_ORG" MODIFY ("AG_TRD" NOT NULL ENABLE);
  ALTER TABLE "ADM_TAB_RET_DOC_ORG" MODIFY ("AC_TRD" NOT NULL ENABLE);
  ALTER TABLE "ADM_TAB_RET_DOC_ORG" MODIFY ("IDE_TAB_RET_DOC_ORG" NOT NULL ENABLE);
  ALTER TABLE "ADM_TAB_RET_DOC_ORG" MODIFY ("IDE_UNI_AMT" NOT NULL ENABLE);
  ALTER TABLE "ADM_TAB_RET_DOC_ORG" MODIFY ("IDE_OFC_PROD" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table ADM_TAB_RET_DOC_ORG
--------------------------------------------------------

  ALTER TABLE "ADM_TAB_RET_DOC_ORG" ADD CONSTRAINT "ADM_TAB_RET_DOC_ORG_FK1" FOREIGN KEY ("IDE_TAB_RET_DOC")
	  REFERENCES "ADM_TAB_RET_DOC" ("IDE_TAB_RET_DOC") ENABLE;
	  
--------------------------------------------------------
--  sequence for Table ADM_TAB_RET_DOC_ORG
--------------------------------------------------------

	INSERT INTO TABLE_GENERATOR (SEQ_NAME, SEQ_VALUE) VALUES ('ADM_TAB_RET_DOC_ORG_SEQ', '0');	  
	  
	  
	  
