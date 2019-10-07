-------------------------------------------------------------------
  --Crear TVS_ORGANIGRAMA_ADMINISTRATIVO
  -------------------------------------------------------------------
  
  CREATE TABLE TVS_ORGANIGRAMA_ADMINISTRATIVO
  (
    IDE_ORGA_ADMIN       NUMBER NOT NULL ,
    COD_ORG              VARCHAR2(255 BYTE) NOT NULL ,
    NOM_ORG              VARCHAR2(100 BYTE) NOT NULL ,
    DESC_ORG             VARCHAR2(500 BYTE) NOT NULL ,
    IND_ES_ACTIVO        VARCHAR2(1 BYTE) NOT NULL ,
    IDE_DIRECCION        NUMBER ,
    IDE_PLAN_RESPONSABLE NUMBER(19, 2) ,
    IDE_ORGA_ADMIN_PADRE NUMBER ,
    COD_NIVEL            VARCHAR2(100 BYTE) ,
    FEC_CREACION         DATE ,
    IDE_USUARIO_CREO     NUMBER(10, 0) ,
    IDE_USUARIO_CAMBIO   NUMBER(10, 0) NOT NULL ,
    FEC_CAMBIO           DATE ,
    NIV_LECTURA          NUMBER(1, 0) ,
    NIV_ESCRITURA        NUMBER(1, 0) ,
    IDE_UUID             CHAR(32 BYTE) ,
    VAL_SISTEMA          VARCHAR2(2 BYTE) ,
    VAL_VERSION          VARCHAR2(10 BYTE)
  )
  LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 1 STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
  )
  NOCOMPRESS;
CREATE UNIQUE INDEX TVS_ORGA_ADMI_PK ON TVS_ORGANIGRAMA_ADMINISTRATIVO
  (
    IDE_ORGA_ADMIN ASC
  )
  LOGGING TABLESPACE USERS PCTFREE 10 INITRANS 2 STORAGE
  (
    INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS UNLIMITED BUFFER_POOL DEFAULT
  )
  NOPARALLEL;
  ALTER TABLE TVS_ORGANIGRAMA_ADMINISTRATIVO ADD CONSTRAINT TVS_ORGA_ADMI_PK PRIMARY KEY ( IDE_ORGA_ADMIN ) USING INDEX TVS_ORGA_ADMI_PK ENABLE;
  ALTER TABLE TVS_ORGANIGRAMA_ADMINISTRATIVO ADD CONSTRAINT TVS_ORGANIGRAMA_ADMINISTR_FK1 FOREIGN KEY ( IDE_ORGA_ADMIN_PADRE ) REFERENCES TVS_ORGANIGRAMA_ADMINISTRATIVO ( IDE_ORGA_ADMIN ) ENABLE;
  COMMENT ON COLUMN TVS_ORGANIGRAMA_ADMINISTRATIVO.IDE_ORGA_ADMIN IS  'Identificador Consecutivo Registro';
  COMMENT ON COLUMN TVS_ORGANIGRAMA_ADMINISTRATIVO.COD_ORG IS  'Codigo de la unidad administrativa';
  COMMENT ON COLUMN TVS_ORGANIGRAMA_ADMINISTRATIVO.NOM_ORG IS  'Nombre de la unidad administrativa';
  COMMENT ON COLUMN TVS_ORGANIGRAMA_ADMINISTRATIVO.DESC_ORG IS  'Descripción de la unidad administrativa';
  COMMENT ON COLUMN TVS_ORGANIGRAMA_ADMINISTRATIVO.IND_ES_ACTIVO IS  'Estado de la unidad administrativa';

  Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('TVS_ORGANIGRAMA_SEQ','1');