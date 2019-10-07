  -------------------------------------------------------------------
  --Añadir campo VAL_VERSION_ORG -  Tabla ADM_CCD
  -------------------------------------------------------------------  
ALTER TABLE "dbo"."ADM_CCD"
	ADD "VAL_VERSION_ORG" NVARCHAR(6) NULL DEFAULT NULL;
	
  -------------------------------------------------------------------
  --Añadir campo NUM_VERSION_ORG -  Tabla ADM_CCD
  -------------------------------------------------------------------  	
	
	ALTER TABLE "dbo"."ADM_CCD"
	ADD "NUM_VERSION_ORG" NUMERIC(16,0) NULL DEFAULT NULL;


