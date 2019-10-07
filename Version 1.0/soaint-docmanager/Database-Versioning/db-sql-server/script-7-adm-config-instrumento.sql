  -------------------------------------------------------------------
  --Crear ADM_CONFIG_INSTRUMENTO
  -------------------------------------------------------------------  
  CREATE TABLE ADM_CONFIG_INSTRUMENTO
( 
	IDE_INSTRUMENTO NVARCHAR(75) NOT NULL,
	EST_INSTRUMENTO NUMERIC DEFAULT 0
)

ALTER TABLE ADM_CONFIG_INSTRUMENTO ADD CONSTRAINT INES_PK
	PRIMARY KEY (IDE_INSTRUMENTO)

