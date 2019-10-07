--------------------------------------------------------
-- Archivo creado  - miércoles-noviembre-30-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table ADM_VERSION_TRD
--------------------------------------------------------

  CREATE TABLE "ADM_VERSION_TRD" 
   (	"IDE_VERSION" NUMBER(18,1), 
	"VAL_VERSION" VARCHAR2(6 BYTE), 
	"FEC_VERSION" DATE DEFAULT sysdate, 
	"IDE_USUARIO" NUMBER(18,1), 
	"IDE_OFC_PROD" VARCHAR2(20 BYTE), 
	"IDE_UNI_AMT" VARCHAR2(20 BYTE), 
	"NUM_VERSION" NUMBER(18,0)
   );

   COMMENT ON COLUMN "ADM_VERSION_TRD"."IDE_VERSION" IS 'Identificador de la versión.';
   COMMENT ON COLUMN "ADM_VERSION_TRD"."VAL_VERSION" IS 'valor de la versión.';
   COMMENT ON COLUMN "ADM_VERSION_TRD"."FEC_VERSION" IS 'Fecha de creación.';
   COMMENT ON COLUMN "ADM_VERSION_TRD"."IDE_USUARIO" IS 'Identificador del usuario.';
   COMMENT ON COLUMN "ADM_VERSION_TRD"."IDE_OFC_PROD" IS 'Identificador de la oficina productora.';
   COMMENT ON COLUMN "ADM_VERSION_TRD"."IDE_UNI_AMT" IS 'Identificador de la unidad administrativa.';
   COMMENT ON COLUMN "ADM_VERSION_TRD"."NUM_VERSION" IS 'Número de la versión.';
--------------------------------------------------------
--  DDL for Index VETRD_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "VETRD_PK" ON "ADM_VERSION_TRD" ("IDE_VERSION");
--------------------------------------------------------
--  Constraints for Table ADM_VERSION_TRD
--------------------------------------------------------

  ALTER TABLE "ADM_VERSION_TRD" MODIFY ("NUM_VERSION" NOT NULL ENABLE);
  ALTER TABLE "ADM_VERSION_TRD" ADD CONSTRAINT "VETRD_PK" PRIMARY KEY ("IDE_VERSION");
  ALTER TABLE "ADM_VERSION_TRD" MODIFY ("IDE_UNI_AMT" NOT NULL ENABLE);
  ALTER TABLE "ADM_VERSION_TRD" MODIFY ("IDE_OFC_PROD" NOT NULL ENABLE);
  ALTER TABLE "ADM_VERSION_TRD" MODIFY ("FEC_VERSION" NOT NULL ENABLE);
  ALTER TABLE "ADM_VERSION_TRD" MODIFY ("VAL_VERSION" NOT NULL ENABLE);
  ALTER TABLE "ADM_VERSION_TRD" MODIFY ("IDE_VERSION" NOT NULL ENABLE);
  
  

INSERT INTO TABLE_GENERATOR (SEQ_NAME, SEQ_VALUE) VALUES ('ADM_VERSION_TRD_SEQ', '0');

