-------------------------------------------------------------------
  --Crear ADM_TPG_DOC
  -------------------------------------------------------------------
  CREATE TABLE ADM_TPG_DOC
  (
    IDE_TPG_DOC        NUMBER(19, 2) NOT NULL ,
    CAR_ADMINISTRATIVA NUMBER(10, 0) ,
    CAR_LEGAL          NUMBER(10, 0) ,
    CAR_TECNICA        NUMBER(10, 0) ,
    COD_TPG_DOC        VARCHAR2(255 CHAR) ,
    EST_TPG_DOC        NUMBER(19, 2) NOT NULL ,
    FEC_CAMBIO         TIMESTAMP(6) ,
    FEC_CREACION       TIMESTAMP(6) ,
    IDE_USUARIO_CAMBIO NUMBER(19, 0) ,
    IDE_UUID           VARCHAR2(255 CHAR) ,
    NIV_ESCRITURA      NUMBER(5, 0) ,
    NIV_LECTURA        NUMBER(5, 0) ,
    NOM_TPG_DOC        VARCHAR2(500 CHAR) NOT NULL ,
    IDE_SOPORTE        NUMBER(19, 0) ,
    IDE_TRA_DOCUMENTAL NUMBER(19, 2) ,
    NOT_ALCANCE        VARCHAR2(255 CHAR) ,
    FUE_BIBLIOGRAFICA  VARCHAR2(255 CHAR)
  )
  LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 1 STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
  )
  NOCOMPRESS;
CREATE UNIQUE INDEX ADM_TPG_DOC_UK1 ON ADM_TPG_DOC
  (
    NOM_TPG_DOC ASC, IDE_TPG_DOC ASC
  )
  LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
  )
  NOPARALLEL;
CREATE UNIQUE INDEX TIDO_UK ON ADM_TPG_DOC
  (
    COD_TPG_DOC ASC
  )
  LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
  )
  NOPARALLEL;
CREATE UNIQUE INDEX TPDO_PK ON ADM_TPG_DOC
  (
    IDE_TPG_DOC ASC
  )
  LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
  )
  NOPARALLEL;
  CREATE INDEX X_TPDO_SOPO ON ADM_TPG_DOC
    (
      IDE_SOPORTE ASC
    )
    LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
    (
      INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
    )
    NOPARALLEL;
  CREATE INDEX X_TPDO_TRDO ON ADM_TPG_DOC
    (
      IDE_TRA_DOCUMENTAL ASC
    )
    LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
    (
      INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
    )
    NOPARALLEL;
  ALTER TABLE ADM_TPG_DOC ADD CONSTRAINT TPDO_PK PRIMARY KEY ( IDE_TPG_DOC ) USING INDEX TPDO_PK ENABLE;
  ALTER TABLE ADM_TPG_DOC ADD CONSTRAINT ADM_TPG_DOC_UK1 UNIQUE ( NOM_TPG_DOC , IDE_TPG_DOC ) USING INDEX ADM_TPG_DOC_UK1 ENABLE;
  ALTER TABLE ADM_TPG_DOC ADD CONSTRAINT ADM_TPG_DOC_FK1 FOREIGN KEY ( IDE_TRA_DOCUMENTAL ) REFERENCES ADM_TRAD_DOC ( IDE_TRAD_DOC ) ENABLE;
  ALTER TABLE ADM_TPG_DOC ADD CONSTRAINT TPDO_SOPO_FK FOREIGN KEY ( IDE_SOPORTE ) REFERENCES ADM_SOPORTE ( IDE_SOPORTE ) ENABLE;
  COMMENT ON TABLE ADM_TPG_DOC IS  'Tabla que almacena los Tipos Documentales';
  COMMENT ON COLUMN ADM_TPG_DOC.IDE_TPG_DOC IS  'Llave primaria: id de la tabla Tipologia Documental';
  COMMENT ON COLUMN ADM_TPG_DOC.CAR_ADMINISTRATIVA IS  'Característica adminstrativa.';
  COMMENT ON COLUMN ADM_TPG_DOC.CAR_LEGAL IS  'Hace referencia a la Caracteristica Legal de la tabla Tipologia Documental';
  COMMENT ON COLUMN ADM_TPG_DOC.CAR_TECNICA IS  'Hace referencia a la Caracteristica Tecnica de la tabla Tipologia Documental';
  COMMENT ON COLUMN ADM_TPG_DOC.COD_TPG_DOC IS  'Hace referencia al Codigo de la tabla Tipologia Documental';
  COMMENT ON COLUMN ADM_TPG_DOC.EST_TPG_DOC IS  'Hace referencia al Estado de la tabla Tipologia Documental';
  COMMENT ON COLUMN ADM_TPG_DOC.FEC_CAMBIO IS  'Fecha de cambio.';
  COMMENT ON COLUMN ADM_TPG_DOC.FEC_CREACION IS  'Fecha de creación.';
  COMMENT ON COLUMN ADM_TPG_DOC.IDE_USUARIO_CAMBIO IS  'Identificador usuario que cambio.';
  COMMENT ON COLUMN ADM_TPG_DOC.IDE_UUID IS  'Identificacion UUID.';
  COMMENT ON COLUMN ADM_TPG_DOC.NIV_ESCRITURA IS  'Nivel de escritura.';
  COMMENT ON COLUMN ADM_TPG_DOC.NIV_LECTURA IS  'Nivel de lectura.';
  COMMENT ON COLUMN ADM_TPG_DOC.NOM_TPG_DOC IS  'Hace referencia al Nombre de la tabla Tipologia Documental';
  COMMENT ON COLUMN ADM_TPG_DOC.IDE_SOPORTE IS  'Llave foránea: Id de la tabla soporte asociada';
  COMMENT ON COLUMN ADM_TPG_DOC.IDE_TRA_DOCUMENTAL IS  'Identificador de la tradición documental.';
  COMMENT ON COLUMN ADM_TPG_DOC.NOT_ALCANCE IS  'Nota de alcance.';
  COMMENT ON COLUMN ADM_TPG_DOC.FUE_BIBLIOGRAFICA IS  'Fuente bibliográfica.';
  
  Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_TPGDOC_SEQ','1');