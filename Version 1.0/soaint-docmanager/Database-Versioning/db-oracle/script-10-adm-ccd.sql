-------------------------------------------------------------------
  --Crear ADM_CCD
  -------------------------------------------------------------------  
  
  CREATE TABLE ADM_CCD
  (
    IDE_CCD            NUMBER(19, 2) NOT NULL ,
    FEC_CAMBIO         TIMESTAMP(6) ,
    FEC_CREACION       TIMESTAMP(6) ,
    IDE_USUARIO_CAMBIO NUMBER(19, 0) ,
    IDE_UUID           VARCHAR2(255 CHAR) ,
    NIV_ESCRITURA      NUMBER(5, 0) ,
    NIV_LECTURA        NUMBER(5, 0) ,
    IDE_OFC_PROD       VARCHAR2(20 BYTE) NOT NULL ,
    IDE_SERIE          NUMBER(19, 2) NOT NULL ,
    IDE_SUBSERIE       NUMBER(19, 2) ,
    IDE_UNI_AMT        VARCHAR2(20 BYTE) NOT NULL ,
    EST_CCD            NUMBER(5, 0) ,
    VAL_VERSION        VARCHAR2(6 BYTE),
	VAL_VERSION_ORG    VARCHAR2(6 BYTE) DEFAULT null, 
	NUM_VERSION_ORG    NUMBER(16,0),
  )
  LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 1 STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
  )
  NOCOMPRESS;
CREATE UNIQUE INDEX CCDS_PK ON ADM_CCD
  (
    IDE_CCD ASC
  )
  LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
  )
  NOPARALLEL;
  CREATE INDEX X_CCDS_ORGA ON ADM_CCD
    (
      IDE_OFC_PROD ASC
    )
    LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
    (
      INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
    )
    NOPARALLEL;
  CREATE INDEX X_CCDS_ORGA1 ON ADM_CCD
    (
      IDE_UNI_AMT ASC
    )
    LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
    (
      INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
    )
    NOPARALLEL;
  CREATE INDEX X_CCDS_SERI ON ADM_CCD
    (
      IDE_SERIE ASC
    )
    LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
    (
      INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
    )
    NOPARALLEL;
  CREATE INDEX X_CCDS_SUBS ON ADM_CCD
    (
      IDE_SUBSERIE ASC
    )
    LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
    (
      INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
    )
    NOPARALLEL;
  ALTER TABLE ADM_CCD ADD CONSTRAINT CCDS_PK PRIMARY KEY ( IDE_CCD ) USING INDEX CCDS_PK ENABLE;
  ALTER TABLE ADM_CCD ADD CONSTRAINT CCDS_SERI_FK FOREIGN KEY ( IDE_SERIE ) REFERENCES ADM_SERIE ( IDE_SERIE ) DISABLE;
  ALTER TABLE ADM_CCD ADD CONSTRAINT CCDS_SUBS_FK FOREIGN KEY ( IDE_SUBSERIE ) REFERENCES ADM_SUBSERIE ( IDE_SUBSERIE ) DISABLE;
 
  COMMENT ON TABLE ADM_CCD IS  'Tabla que representa el Cuadro de Clasificación Documental';
  COMMENT ON COLUMN ADM_CCD.IDE_CCD IS  'Identificador del cuadro de clasificación documental.';
  COMMENT ON COLUMN ADM_CCD.FEC_CAMBIO IS  'Fecha de cambio';
  COMMENT ON COLUMN ADM_CCD.FEC_CREACION IS  'Fecha de creación.';
  COMMENT ON COLUMN ADM_CCD.IDE_USUARIO_CAMBIO IS  'Identificador del usuario que cambia.';
  COMMENT ON COLUMN ADM_CCD.IDE_UUID IS  'Identificador universal.';
  COMMENT ON COLUMN ADM_CCD.NIV_ESCRITURA IS  'Nivel de escritura.';
  COMMENT ON COLUMN ADM_CCD.NIV_LECTURA IS  'Nivel de lectura.';
  COMMENT ON COLUMN ADM_CCD.IDE_OFC_PROD IS  'Identificador de la Oficina de producción.';
  COMMENT ON COLUMN ADM_CCD.IDE_SERIE IS  'Identificador de la serie.';
  COMMENT ON COLUMN ADM_CCD.IDE_SUBSERIE IS  'Identificador de la subserie.';
  COMMENT ON COLUMN ADM_CCD.IDE_UNI_AMT IS  'Identificador de la unidad de almacenamiento.';
  COMMENT ON COLUMN ADM_CCD.EST_CCD IS  'Estado del cuadro.';
  COMMENT ON COLUMN ADM_CCD.VAL_VERSION IS  'Contiene la version del registro';
  
  Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_CCD_SEQ','0');