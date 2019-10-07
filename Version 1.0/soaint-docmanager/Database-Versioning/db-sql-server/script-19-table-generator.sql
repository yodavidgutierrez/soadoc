  -------------------------------------------------------------------
  --Crear TABLE_GENERATOR
  -------------------------------------------------------------------
  CREATE TABLE TABLE_GENERATOR
( 
	SEQ_NAME NVARCHAR(40) NOT NULL,
	SEQ_VALUE NUMERIC(15)
);
ALTER TABLE TABLE_GENERATOR ADD CONSTRAINT TABLE_GENERATOR_PK
	PRIMARY KEY (SEQ_NAME);