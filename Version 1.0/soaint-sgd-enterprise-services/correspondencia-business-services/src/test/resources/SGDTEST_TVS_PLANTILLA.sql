CREATE TABLE SGDTEST.TVS_PLANTILLA
(
    IDE_PLANTILLA NUMBER(18) PRIMARY KEY NOT NULL,
    REFERENCIA VARCHAR2(50) NOT NULL,
    COD_TIPO_DOC VARCHAR2(8),
    COD_TIPO_UBICACION VARCHAR2(8),
    UBICACION VARCHAR2(50),
    ESTADO VARCHAR2(8)
);
INSERT INTO SGDTEST.TVS_PLANTILLA (IDE_PLANTILLA, REFERENCIA, COD_TIPO_DOC, COD_TIPO_UBICACION, UBICACION, ESTADO) VALUES (1, 'test.doc', 'COD', 'FISICA', null, 'A');