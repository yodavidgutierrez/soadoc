--------------------------------------------------------
--  DDL for Table ADM_REL_EQ_ORIGEN
--------------------------------------------------------

  CREATE TABLE ADM_REL_EQ_ORIGEN
   (	IDE_REL_ORIGEN NUMBER, 
	IDE_UNI_AMT VARCHAR2(20 BYTE), 
	IDE_OFC_PROD VARCHAR2(20 BYTE), 
	IDE_SERIE NUMBER(19,0), 
	IDE_SUBSERIE NUMBER(19,0), 
	FEC_CREACION DATE
   );
--------------------------------------------------------
--  DDL for Index ADM_REL_EQ_ORIGEN_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX ADM_REL_EQ_ORIGEN_PK ON ADM_REL_EQ_ORIGEN (IDE_REL_ORIGEN);
--------------------------------------------------------
--  Constraints for Table ADM_REL_EQ_ORIGEN
--------------------------------------------------------

  ALTER TABLE ADM_REL_EQ_ORIGEN ADD CONSTRAINT ADM_REL_EQ_ORIGEN_PK PRIMARY KEY (IDE_REL_ORIGEN);
  ALTER TABLE ADM_REL_EQ_ORIGEN MODIFY (IDE_OFC_PROD NOT NULL ENABLE);
  ALTER TABLE ADM_REL_EQ_ORIGEN MODIFY (IDE_UNI_AMT NOT NULL ENABLE);
  ALTER TABLE ADM_REL_EQ_ORIGEN MODIFY (IDE_REL_ORIGEN NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table ADM_REL_EQ_ORIGEN
--------------------------------------------------------

  ALTER TABLE ADM_REL_EQ_ORIGEN ADD CONSTRAINT ADM_REL_EQ_ORIGEN_FK1 FOREIGN KEY (IDE_SERIE)
	  REFERENCES ADM_SERIE (IDE_SERIE) ENABLE;
  ALTER TABLE ADM_REL_EQ_ORIGEN ADD CONSTRAINT ADM_REL_EQ_ORIGEN_FK2 FOREIGN KEY (IDE_SUBSERIE)
	  REFERENCES ADM_SUBSERIE (IDE_SUBSERIE) DISABLE;

	  
	  
	  Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_REL_EQ_OR_SEQ','0');