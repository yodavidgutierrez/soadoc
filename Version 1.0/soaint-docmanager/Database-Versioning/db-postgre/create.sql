-- Database: docmanager

-- DROP DATABASE docmanager;

CREATE DATABASE docmanager
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Colombia.1252'
    LC_CTYPE = 'Spanish_Colombia.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

------------------------------------------------------------------------------------------------------------------------------
--tvs_organigrama_administrativo
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.tvs_organigrama_administrativo

-- DROP TABLE public.tvs_organigrama_administrativo;

CREATE TABLE public.tvs_organigrama_administrativo
(
    ide_orga_admin numeric NOT NULL,
    cod_org text COLLATE pg_catalog."default" NOT NULL,
    nom_org text COLLATE pg_catalog."default" NOT NULL,
    desc_org text COLLATE pg_catalog."default" NOT NULL,
    ind_es_activo text COLLATE pg_catalog."default" NOT NULL,
    ide_direccion numeric,
    ide_plan_responsable numeric,
    ide_orga_admin_padre numeric,
    cod_nivel text COLLATE pg_catalog."default",
    fec_creacion date,
    ide_usuario_creo numeric,
    ide_usuario_cambio numeric NOT NULL,
    fec_cambio date,
    niv_lectura numeric,
    niv_escritura numeric,
    ide_uuid character(32) COLLATE pg_catalog."default",
    val_sistema text COLLATE pg_catalog."default",
    val_version text COLLATE pg_catalog."default",
	abrev_org text,
    CONSTRAINT tvs_organigrama_administrativo_pkey PRIMARY KEY (ide_orga_admin),
    CONSTRAINT tvs_organigrama_administr_fk1 FOREIGN KEY (ide_orga_admin_padre)
        REFERENCES public.tvs_organigrama_administrativo (ide_orga_admin) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tvs_organigrama_administrativo
    OWNER to postgres;

COMMENT ON COLUMN public.tvs_organigrama_administrativo.ide_orga_admin
    IS 'Identificador Consecutivo Registro';

COMMENT ON COLUMN public.tvs_organigrama_administrativo.cod_org
    IS 'Codigo de la unidad administrativa';

COMMENT ON COLUMN public.tvs_organigrama_administrativo.nom_org
    IS 'Nombre de la unidad administrativa';

COMMENT ON COLUMN public.tvs_organigrama_administrativo.desc_org
    IS 'Descripcion de la unidad administrativa';

COMMENT ON COLUMN public.tvs_organigrama_administrativo.ind_es_activo
    IS 'Estado de la unidad administrativa';

------------------------------------------------------------------------------------------------------------------------------
--tvs_config_org_administrativo
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.tvs_config_org_administrativo

-- DROP TABLE public.tvs_config_org_administrativo;

CREATE TABLE public.tvs_config_org_administrativo
(
    ide_orga_admin numeric NOT NULL,
    cod_org text COLLATE pg_catalog."default" NOT NULL,
    nom_org text COLLATE pg_catalog."default" NOT NULL,
    desc_org text COLLATE pg_catalog."default" NOT NULL,
    ind_es_activo text COLLATE pg_catalog."default" NOT NULL,
    ide_direccion numeric,
    ide_plan_responsable numeric,
    ide_orga_admin_padre numeric,
    cod_nivel text COLLATE pg_catalog."default",
    fec_creacion date,
    ide_usuario_creo numeric,
    ide_usuario_cambio numeric,
    fec_cambio date,
    niv_lectura numeric,
    niv_escritura numeric,
    ide_uuid character(32) COLLATE pg_catalog."default",
    val_sistema text COLLATE pg_catalog."default",
	abrev_org text,
    CONSTRAINT tvs_config_orga_admi_pk PRIMARY KEY (ide_orga_admin),
    CONSTRAINT "ORAD_CONFIG_COD_UK" UNIQUE (cod_org),
    CONSTRAINT "TVS_CONFIG_ORG_ADMINISTRA_UK1" UNIQUE (nom_org),
    CONSTRAINT tvs_config_org_administra_fk1 FOREIGN KEY (ide_orga_admin_padre)
        REFERENCES public.tvs_config_org_administrativo (ide_orga_admin) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tvs_config_org_administrativo
    OWNER to postgres;

COMMENT ON COLUMN public.tvs_config_org_administrativo.ide_orga_admin
    IS 'Identificador Consecutivo Registro';

COMMENT ON COLUMN public.tvs_config_org_administrativo.cod_org
    IS 'Codigo de la unidad administrativa';

COMMENT ON COLUMN public.tvs_config_org_administrativo.nom_org
    IS 'Nombre de la unidad administrativa';

COMMENT ON COLUMN public.tvs_config_org_administrativo.desc_org
    IS 'Descripcion de la unidad administrativa';

COMMENT ON COLUMN public.tvs_config_org_administrativo.ind_es_activo
    IS 'Estado de la unidad administrativa';
	
------------------------------------------------------------------------------------------------------------------------------
--table_generator
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.table_generator

-- DROP TABLE public.table_generator;

CREATE TABLE public.table_generator
(
    seq_name text COLLATE pg_catalog."default" NOT NULL,
    seq_value numeric,
    CONSTRAINT table_generator_pk PRIMARY KEY (seq_name)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.table_generator
    OWNER to postgres;
	
------------------------------------------------------------------------------------------------------------------------------
--cm_carga_masiva
------------------------------------------------------------------------------------------------------------------------------
	
-- Table: public.cm_carga_masiva

-- DROP TABLE public.cm_carga_masiva;

CREATE TABLE public.cm_carga_masiva
(
    id numeric NOT NULL,
    nombre text COLLATE pg_catalog."default",
    fecha_creacion timestamp(6) without time zone,
    total_registros numeric,
    estado text COLLATE pg_catalog."default",
    total_registros_exitosos numeric,
    total_registros_error numeric,
    CONSTRAINT cm_carga_masiva_pk PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.cm_carga_masiva
    OWNER to postgres;
	
------------------------------------------------------------------------------------------------------------------------------
--cm_registro_carga_masiva
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.cm_registro_carga_masiva

-- DROP TABLE public.cm_registro_carga_masiva;

CREATE TABLE public.cm_registro_carga_masiva
(
    id numeric NOT NULL,
    carga_masiva_id numeric,
    contenido text COLLATE pg_catalog."default",
    estado text COLLATE pg_catalog."default",
    mensajes text COLLATE pg_catalog."default",
    CONSTRAINT cm_registro_carga_masiva_pk PRIMARY KEY (id),
    CONSTRAINT sys_c0011047 FOREIGN KEY (carga_masiva_id)
        REFERENCES public.cm_carga_masiva (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.cm_registro_carga_masiva
    OWNER to postgres;
	
------------------------------------------------------------------------------------------------------------------------------
--adm_version_trd
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_version_trd

-- DROP TABLE public.adm_version_trd;

CREATE TABLE public.adm_version_trd
(
    ide_version numeric NOT NULL,
    val_version text COLLATE pg_catalog."default" NOT NULL,
    fec_version date NOT NULL,
    ide_usuario numeric,
    ide_ofc_prod text COLLATE pg_catalog."default" NOT NULL,
    ide_uni_amt text COLLATE pg_catalog."default" NOT NULL,
    num_version numeric NOT NULL,
	nombre_comite text,
	num_acta text,
	fecha_acta date,
    CONSTRAINT vetrd_pk PRIMARY KEY (ide_version)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_version_trd
    OWNER to postgres;

COMMENT ON COLUMN public.adm_version_trd.ide_version
    IS 'Identificador de la version.';

COMMENT ON COLUMN public.adm_version_trd.val_version
    IS 'valor de la version.';

COMMENT ON COLUMN public.adm_version_trd.fec_version
    IS 'Fecha de creacion.';

COMMENT ON COLUMN public.adm_version_trd.ide_usuario
    IS 'Identificador del usuario.';

COMMENT ON COLUMN public.adm_version_trd.ide_ofc_prod
    IS 'Identificador de la oficina productora.';

COMMENT ON COLUMN public.adm_version_trd.ide_uni_amt
    IS 'Identificador de la unidad administrativa.';

COMMENT ON COLUMN public.adm_version_trd.num_version
    IS 'Numero de la version.';
	
------------------------------------------------------------------------------------------------------------------------------
--adm_trad_doc
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_trad_doc

-- DROP TABLE public.adm_trad_doc;

CREATE TABLE public.adm_trad_doc
(
    ide_trad_doc numeric NOT NULL,
    des_trad_doc text COLLATE pg_catalog."default" NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    nom_trad_doc text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT trdo_pk1 PRIMARY KEY (ide_trad_doc)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_trad_doc
    OWNER to postgres;
COMMENT ON TABLE public.adm_trad_doc
    IS 'Tabla que almacena el Nomenclador de Tradicion Documental';

COMMENT ON COLUMN public.adm_trad_doc.ide_trad_doc
    IS 'Identificador de la tradicion documental.';

COMMENT ON COLUMN public.adm_trad_doc.des_trad_doc
    IS 'Descripcion la tradicion documental.';

COMMENT ON COLUMN public.adm_trad_doc.fec_cambio
    IS 'Fecha de cambio.';

COMMENT ON COLUMN public.adm_trad_doc.fec_creacion
    IS 'Fecha de creacion.';

COMMENT ON COLUMN public.adm_trad_doc.ide_usuario_cambio
    IS 'Identificador usuario que cambio.';

COMMENT ON COLUMN public.adm_trad_doc.ide_uuid
    IS 'Identificacion UUID.';

COMMENT ON COLUMN public.adm_trad_doc.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_trad_doc.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_trad_doc.nom_trad_doc
    IS 'Nombre de la tradicion documental.';
	
------------------------------------------------------------------------------------------------------------------------------
--adm_soporte
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_soporte

-- DROP TABLE public.adm_soporte;

CREATE TABLE public.adm_soporte
(
    ide_soporte numeric NOT NULL,
    des_soporte text COLLATE pg_catalog."default" NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    nom_soporte text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT sopo_pk PRIMARY KEY (ide_soporte)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_soporte
    OWNER to postgres;
COMMENT ON TABLE public.adm_soporte
    IS 'Tabla Soporte, contiene los soportes de las entidades';

COMMENT ON COLUMN public.adm_soporte.ide_soporte
    IS 'Llave primaria: Id de la Tabla Soporte';

COMMENT ON COLUMN public.adm_soporte.des_soporte
    IS 'DescripciÃ³n del soporte.';

COMMENT ON COLUMN public.adm_soporte.fec_cambio
    IS 'Fecha de cambio.';

COMMENT ON COLUMN public.adm_soporte.fec_creacion
    IS 'Fecha de creaciÃ³n.';

COMMENT ON COLUMN public.adm_soporte.ide_usuario_cambio
    IS 'Identificador usuario que cambio.';

COMMENT ON COLUMN public.adm_soporte.ide_uuid
    IS 'Identificacion UUID.';

COMMENT ON COLUMN public.adm_soporte.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_soporte.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_soporte.nom_soporte
    IS 'Hace referencia al nombre del soporte';

------------------------------------------------------------------------------------------------------------------------------
--adm_serie
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_serie

-- DROP TABLE public.adm_serie;

CREATE TABLE public.adm_serie
(
    ide_serie numeric NOT NULL,
    act_administrativo text COLLATE pg_catalog."default" NOT NULL,
    car_administrativa numeric,
    car_legal numeric,
    car_tecnica numeric,
    cod_serie text COLLATE pg_catalog."default" NOT NULL,
    est_serie numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    fue_bibliografica text COLLATE pg_catalog."default",
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    nom_serie text COLLATE pg_catalog."default" NOT NULL,
    not_alcance text COLLATE pg_catalog."default",
    ide_mot_creacion numeric NOT NULL,
	car_contable numeric,
	car_juridica numeric,
	con_publica numeric,
	con_clasificada numeric,
	con_reservada numeric,
    CONSTRAINT adm_serie_pk PRIMARY KEY (ide_serie),
    CONSTRAINT seri_uk UNIQUE (cod_serie)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_serie
    OWNER to postgres;

COMMENT ON COLUMN public.adm_serie.car_legal
    IS 'Hace referencia a la CaracterÃ­stica Legal de la serie';

COMMENT ON COLUMN public.adm_serie.car_tecnica
    IS 'Hace referencia a la CaracterÃ­stica TÃ©cnica de la serie';

COMMENT ON COLUMN public.adm_serie.cod_serie
    IS 'Hace referencia a la cÃ³digo de la serie';

COMMENT ON COLUMN public.adm_serie.est_serie
    IS 'Hace referencia al Estado de la serie';

COMMENT ON COLUMN public.adm_serie.fec_cambio
    IS 'Fecha de cambio.';

COMMENT ON COLUMN public.adm_serie.fec_creacion
    IS 'Fecha de creaciÃ³n del registro';

COMMENT ON COLUMN public.adm_serie.ide_usuario_cambio
    IS 'Identificador usuario que cambio.';

COMMENT ON COLUMN public.adm_serie.ide_uuid
    IS 'Identificacion UUID.';

COMMENT ON COLUMN public.adm_serie.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_serie.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_serie.nom_serie
    IS 'Hace referencia al Nombre de la serie';

COMMENT ON COLUMN public.adm_serie.not_alcance
    IS 'Hace referencia a la Nota de Alcance de la serie';

------------------------------------------------------------------------------------------------------------------------------
--adm_mot_creacion
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_mot_creacion

-- DROP TABLE public.adm_mot_creacion;

CREATE TABLE public.adm_mot_creacion
(
    ide_mot_creacion numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    nom_mot_creacion text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT mocr_pk PRIMARY KEY (ide_mot_creacion)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_mot_creacion
    OWNER to postgres;
COMMENT ON TABLE public.adm_mot_creacion
    IS 'Tabla del Nomenclador Motivo de CreaciÃ³n';

COMMENT ON COLUMN public.adm_mot_creacion.fec_cambio
    IS 'Fecha de cambio.';

COMMENT ON COLUMN public.adm_mot_creacion.fec_creacion
    IS 'Fecha de creaciÃ³n.';

COMMENT ON COLUMN public.adm_mot_creacion.ide_usuario_cambio
    IS 'Identificador del usuario que cambia.';

COMMENT ON COLUMN public.adm_mot_creacion.ide_uuid
    IS 'IdentificaciÃ³n UUID.';

COMMENT ON COLUMN public.adm_mot_creacion.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_mot_creacion.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_mot_creacion.nom_mot_creacion
    IS 'Nombre del motivo de creacion.';
	
ALTER TABLE public.adm_mot_creacion
    ADD COLUMN est_mot_creacion text;
	
COMMENT ON COLUMN public.adm_mot_creacion.est_mot_creacion
    IS 'Estado del motivo de creacion.';

------------------------------------------------------------------------------------------------------------------------------
--adm_subserie
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_subserie

-- DROP TABLE public.adm_subserie;

CREATE TABLE public.adm_subserie
(
    ide_subserie numeric NOT NULL,
    act_administrativo text COLLATE pg_catalog."default" NOT NULL,
    car_administrativa numeric,
    car_legal numeric,
    car_tecnica numeric,
    cod_subserie text COLLATE pg_catalog."default" NOT NULL,
    est_subserie numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    fue_bibliografica text COLLATE pg_catalog."default",
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    nom_subserie text COLLATE pg_catalog."default" NOT NULL,
    not_alcance text COLLATE pg_catalog."default",
    ide_mot_creacion numeric NOT NULL,
    ide_serie numeric,
	car_juridica numeric,
	car_contable numeric,
	con_publica numeric,
	con_clasificada numeric,	
    con_reservada numeric,
    
    CONSTRAINT subs_pk PRIMARY KEY (ide_subserie),
    CONSTRAINT subs_mocr_fk FOREIGN KEY (ide_mot_creacion)
        REFERENCES public.adm_mot_creacion (ide_mot_creacion) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT subs_seri_fk FOREIGN KEY (ide_serie)
        REFERENCES public.adm_serie (ide_serie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_subserie
    OWNER to postgres;

COMMENT ON COLUMN public.adm_subserie.act_administrativo
    IS 'Hace referencia al acto administrativo';

COMMENT ON COLUMN public.adm_subserie.car_administrativa
    IS 'Caracteristica administrativa de la subserie.';

COMMENT ON COLUMN public.adm_subserie.car_legal
    IS 'Hace referencia a la Caracteristica Legal de la Subserie';

COMMENT ON COLUMN public.adm_subserie.car_tecnica
    IS 'Hace referencia a la Caracteristica Tecnica de la Subserie';

COMMENT ON COLUMN public.adm_subserie.cod_subserie
    IS 'Hace referencia al codigo de la Subserie';

COMMENT ON COLUMN public.adm_subserie.est_subserie
    IS 'Hace referencia al Estado de la Subserie';

COMMENT ON COLUMN public.adm_subserie.fec_cambio
    IS 'Fecha de cambio.';

COMMENT ON COLUMN public.adm_subserie.fec_creacion
    IS 'Fecha de creacion.';

COMMENT ON COLUMN public.adm_subserie.fue_bibliografica
    IS 'Fuente bibliogrifica.';

COMMENT ON COLUMN public.adm_subserie.ide_usuario_cambio
    IS 'Identificador usuario que cambio.';

COMMENT ON COLUMN public.adm_subserie.ide_uuid
    IS 'Identificacion UUID.';

COMMENT ON COLUMN public.adm_subserie.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_subserie.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_subserie.nom_subserie
    IS 'Hace referencia al Nombre de la Subserie';

COMMENT ON COLUMN public.adm_subserie.not_alcance
    IS 'Hace referencia a la Nota Alcance de la Subserie';

COMMENT ON COLUMN public.adm_subserie.ide_mot_creacion
    IS 'Identificador del motivo de creacion.';

COMMENT ON COLUMN public.adm_subserie.ide_serie
    IS 'llave forinea: Id de la tabla Serie asociada';

------------------------------------------------------------------------------------------------------------------------------
--adm_rel_eq_origen
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_rel_eq_origen

-- DROP TABLE public.adm_rel_eq_origen;

CREATE TABLE public.adm_rel_eq_origen
(
    ide_rel_origen numeric NOT NULL,
    ide_uni_amt text COLLATE pg_catalog."default" NOT NULL,
    ide_ofc_prod text COLLATE pg_catalog."default" NOT NULL,
    ide_serie numeric,
    ide_subserie numeric,
    fec_creacion date,
    num_version_org numeric,
    CONSTRAINT adm_rel_eq_origen_pk PRIMARY KEY (ide_rel_origen),
    CONSTRAINT adm_rel_eq_origen_fk1 FOREIGN KEY (ide_serie)
        REFERENCES public.adm_serie (ide_serie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT adm_rel_eq_origen_fk2 FOREIGN KEY (ide_subserie)
        REFERENCES public.adm_subserie (ide_subserie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_rel_eq_origen
    OWNER to postgres;
	
------------------------------------------------------------------------------------------------------------------------------
--adm_rel_eq_destino
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_rel_eq_destino

-- DROP TABLE public.adm_rel_eq_destino;

CREATE TABLE public.adm_rel_eq_destino
(
    ide_rel_destino numeric NOT NULL,
    ide_rel_origen numeric,
    ide_uni_amt text COLLATE pg_catalog."default" NOT NULL,
    ide_ofc_prod text COLLATE pg_catalog."default" NOT NULL,
    ide_serie numeric,
    ide_subserie numeric,
    fec_creacion date,
    CONSTRAINT adm_rel_eq_destino_pk PRIMARY KEY (ide_rel_destino),
    CONSTRAINT adm_rel_eq_destino_fk1 FOREIGN KEY (ide_serie)
        REFERENCES public.adm_serie (ide_serie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT adm_rel_eq_destino_fk2 FOREIGN KEY (ide_subserie)
        REFERENCES public.adm_subserie (ide_subserie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_rel_eq_destino
    OWNER to postgres;

------------------------------------------------------------------------------------------------------------------------------
--adm_tab_ret_doc
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_tab_ret_doc

-- DROP TABLE public.adm_tab_ret_doc;

CREATE TABLE public.adm_tab_ret_doc
(
    ide_tab_ret_doc numeric NOT NULL,
    ac_trd numeric NOT NULL,
    ag_trd numeric NOT NULL,
    est_tab_ret_doc numeric,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    pro_trd text COLLATE pg_catalog."default" NOT NULL,
    ide_dis_final numeric NOT NULL,
    ide_serie numeric NOT NULL,
    ide_subserie numeric,
    ide_ofc_prod text COLLATE pg_catalog."default" NOT NULL,
    ide_uni_amt text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT trdo_pk PRIMARY KEY (ide_tab_ret_doc),
    CONSTRAINT trdo_seri_fk FOREIGN KEY (ide_serie)
        REFERENCES public.adm_serie (ide_serie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT trdo_subs_fk FOREIGN KEY (ide_subserie)
        REFERENCES public.adm_subserie (ide_subserie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_tab_ret_doc
    OWNER to postgres;
COMMENT ON TABLE public.adm_tab_ret_doc
    IS 'Tabla que almacena los valores de las Tablas de RetenciÃ³n Documental';

COMMENT ON COLUMN public.adm_tab_ret_doc.ide_tab_ret_doc
    IS 'Llave primaria: Id de la TRD';

COMMENT ON COLUMN public.adm_tab_ret_doc.ac_trd
    IS 'Hace referencia a la Retencion AC asociada a la TRD';

COMMENT ON COLUMN public.adm_tab_ret_doc.ag_trd
    IS 'Hace referencia a la Retencion AG asociada a la TRD';

COMMENT ON COLUMN public.adm_tab_ret_doc.est_tab_ret_doc
    IS 'Estado de la tabla de retenciÃ³n dcumental.';

COMMENT ON COLUMN public.adm_tab_ret_doc.fec_cambio
    IS 'Fecha de cambio.';

COMMENT ON COLUMN public.adm_tab_ret_doc.fec_creacion
    IS 'Fecha de creaciÃ³n.';

COMMENT ON COLUMN public.adm_tab_ret_doc.ide_usuario_cambio
    IS 'Identificador usuario que cambio.';

COMMENT ON COLUMN public.adm_tab_ret_doc.ide_uuid
    IS 'Identificacion UUID.';

COMMENT ON COLUMN public.adm_tab_ret_doc.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_tab_ret_doc.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_tab_ret_doc.pro_trd
    IS 'Hace referencia al procedimiento asociada a la TRD';

COMMENT ON COLUMN public.adm_tab_ret_doc.ide_dis_final
    IS 'Hace referencia al ide de disposicion final asociada a la TRD';

COMMENT ON COLUMN public.adm_tab_ret_doc.ide_serie
    IS 'Llave foranea: Id de la  Serie asociada';

COMMENT ON COLUMN public.adm_tab_ret_doc.ide_subserie
    IS 'Llave foranea: Id de la  Subserie asociada';

------------------------------------------------------------------------------------------------------------------------------
--adm_tab_ret_doc_org
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_tab_ret_doc_org

-- DROP TABLE public.adm_tab_ret_doc_org;

CREATE TABLE public.adm_tab_ret_doc_org
(
    ide_ofc_prod text COLLATE pg_catalog."default" NOT NULL,
    ide_uni_amt text COLLATE pg_catalog."default" NOT NULL,
    ide_tab_ret_doc_org numeric NOT NULL,
    ide_tab_ret_doc numeric NOT NULL,
    ac_trd numeric NOT NULL,
    ag_trd numeric NOT NULL,
    pro_trd text COLLATE pg_catalog."default" NOT NULL,
    ide_dis_final numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    num_version numeric NOT NULL,
	nombre_serie text,
	nombre_subserie text,
    CONSTRAINT adm_tab_ret_doc_org_pk PRIMARY KEY (ide_tab_ret_doc_org),
    CONSTRAINT adm_tab_ret_doc_org_fk1 FOREIGN KEY (ide_tab_ret_doc)
        REFERENCES public.adm_tab_ret_doc (ide_tab_ret_doc) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_tab_ret_doc_org
    OWNER to postgres;

COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_ofc_prod
    IS 'Identificador del organigrama, oficina productora';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_uni_amt
    IS 'Identificador del organigrama, unidad administrativa';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_tab_ret_doc_org
    IS 'Identificador de la tabla';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_tab_ret_doc
    IS 'Identificador de la tabla de retencion documental';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.ac_trd
    IS 'Hace referencia a la Retencion AC asociada a la TRD';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.ag_trd
    IS 'Hace referencia a la Retencion AG asociada a la TRD';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.pro_trd
    IS 'Hace referencia al procedimiento asociada a la TRD';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_dis_final
    IS 'Hace referencia al ide de disposicion final asociada a la TRD';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.fec_cambio
    IS 'Fecha de cambio.';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.fec_creacion
    IS 'Fecha de creacion';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_usuario_cambio
    IS 'Identificador usuario que cambio.';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_uuid
    IS 'Identificacion UUID.';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_tab_ret_doc_org.num_version
    IS 'Identificador del nÃºmero de versiÃ³n';

------------------------------------------------------------------------------------------------------------------------------
--adm_tpg_doc
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_tpg_doc

-- DROP TABLE public.adm_tpg_doc;

CREATE TABLE public.adm_tpg_doc
(
    ide_tpg_doc numeric NOT NULL,
    car_administrativa numeric,
    car_legal numeric,
    car_tecnica numeric,
	car_juridico numeric,
	car_contable numeric,
    cod_tpg_doc text COLLATE pg_catalog."default",
    est_tpg_doc numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    nom_tpg_doc text COLLATE pg_catalog."default" NOT NULL,
    ide_soporte numeric,
    ide_tra_documental numeric,
    not_alcance text COLLATE pg_catalog."default",
    fue_bibliografica text COLLATE pg_catalog."default",
    CONSTRAINT tpdo_pk PRIMARY KEY (ide_tpg_doc),
    CONSTRAINT adm_tpg_doc_uk1 UNIQUE (nom_tpg_doc, ide_tpg_doc)
,
    CONSTRAINT adm_tpg_doc_fk1 FOREIGN KEY (ide_tra_documental)
        REFERENCES public.adm_trad_doc (ide_trad_doc) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT tpdo_sopo_fk FOREIGN KEY (ide_soporte)
        REFERENCES public.adm_soporte (ide_soporte) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_tpg_doc
    OWNER to postgres;
COMMENT ON TABLE public.adm_tpg_doc
    IS 'Tabla que almacena los Tipos Documentales';

COMMENT ON COLUMN public.adm_tpg_doc.ide_tpg_doc
    IS 'Llave primaria: id de la tabla Tipologia Documental';

COMMENT ON COLUMN public.adm_tpg_doc.car_administrativa
    IS 'CaracterÃ­stica adminstrativa.';

COMMENT ON COLUMN public.adm_tpg_doc.car_legal
    IS 'Hace referencia a la Caracteristica Legal de la tabla Tipologia Documental';

COMMENT ON COLUMN public.adm_tpg_doc.car_tecnica
    IS 'Hace referencia a la Caracteristica Tecnica de la tabla Tipologia Documental';

COMMENT ON COLUMN public.adm_tpg_doc.cod_tpg_doc
    IS 'Hace referencia al Codigo de la tabla Tipologia Documental';

COMMENT ON COLUMN public.adm_tpg_doc.est_tpg_doc
    IS 'Hace referencia al Estado de la tabla Tipologia Documental';

COMMENT ON COLUMN public.adm_tpg_doc.fec_cambio
    IS 'Fecha de cambio.';

COMMENT ON COLUMN public.adm_tpg_doc.fec_creacion
    IS 'Fecha de creaciÃ³n.';

COMMENT ON COLUMN public.adm_tpg_doc.ide_usuario_cambio
    IS 'Identificador usuario que cambio.';

COMMENT ON COLUMN public.adm_tpg_doc.ide_uuid
    IS 'Identificacion UUID.';

COMMENT ON COLUMN public.adm_tpg_doc.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_tpg_doc.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_tpg_doc.nom_tpg_doc
    IS 'Hace referencia al Nombre de la tabla Tipologia Documental';

COMMENT ON COLUMN public.adm_tpg_doc.ide_soporte
    IS 'Llave forÃ¡nea: Id de la tabla soporte asociada';

COMMENT ON COLUMN public.adm_tpg_doc.ide_tra_documental
    IS 'Identificador de la tradiciÃ³n documental.';

COMMENT ON COLUMN public.adm_tpg_doc.not_alcance
    IS 'Nota de alcance.';

COMMENT ON COLUMN public.adm_tpg_doc.fue_bibliografica
    IS 'Fuente bibliogrÃ¡fica.';

------------------------------------------------------------------------------------------------------------------------------
--adm_ser_subser_tpg
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_ser_subser_tpg

-- DROP TABLE public.adm_ser_subser_tpg;

CREATE TABLE public.adm_ser_subser_tpg
(
    ide_serie numeric NOT NULL,
    ide_tpg_doc numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    ide_subserie numeric,
    ide_rel_sst numeric NOT NULL,
	orden numeric,
    CONSTRAINT sstp_pk PRIMARY KEY (ide_rel_sst),
    CONSTRAINT sstp_seri_fk FOREIGN KEY (ide_serie)
        REFERENCES public.adm_serie (ide_serie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sstp_subs_fk FOREIGN KEY (ide_subserie)
        REFERENCES public.adm_subserie (ide_subserie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sstp_tpdo_fk FOREIGN KEY (ide_tpg_doc)
        REFERENCES public.adm_tpg_doc (ide_tpg_doc) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_ser_subser_tpg
    OWNER to postgres;
COMMENT ON TABLE public.adm_ser_subser_tpg
    IS 'Tabla que relaciona las Series Subseries y Tipologias Documentales';

COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_serie
    IS 'Identificador de la serie.';

COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_tpg_doc
    IS 'Identificador de la tipologÃ­a.';

COMMENT ON COLUMN public.adm_ser_subser_tpg.fec_cambio
    IS 'Fecha de cambio.';

COMMENT ON COLUMN public.adm_ser_subser_tpg.fec_creacion
    IS 'Fecha de creaciÃ³n.';

COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_usuario_cambio
    IS 'Identificador del usuario que cambia.';

COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_uuid
    IS 'IdentificaciÃ³n UUID.';

COMMENT ON COLUMN public.adm_ser_subser_tpg.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_ser_subser_tpg.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_subserie
    IS 'Identificador de la subserie.';

COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_rel_sst
    IS 'Identificador de la relaciÃ³n serie, subserie, tipologÃ­a.';

------------------------------------------------------------------------------------------------------------------------------
--adm_dis_final
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_dis_final

-- DROP TABLE public.adm_dis_final;

CREATE TABLE public.adm_dis_final
(
    ide_dis_final numeric NOT NULL,
    nom_dis_final text COLLATE pg_catalog."default" NOT NULL,
    des_dis_final text COLLATE pg_catalog."default",
    sta_dis_final text COLLATE pg_catalog."default",
    ide_usuario_cambio numeric,
    fec_cambio date,
    niv_lectura numeric,
    niv_escritura numeric,
    ide_uuid character(32) COLLATE pg_catalog."default",
    fec_creacion date,
    CONSTRAINT adm_dis_final_pk PRIMARY KEY (ide_dis_final)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_dis_final
    OWNER to postgres;

COMMENT ON COLUMN public.adm_dis_final.sta_dis_final
    IS 'Estado de la dispocicion final';

------------------------------------------------------------------------------------------------------------------------------
--adm_config_instrumento
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_config_instrumento

-- DROP TABLE public.adm_config_instrumento;

CREATE TABLE public.adm_config_instrumento
(
    ide_instrumento text COLLATE pg_catalog."default" NOT NULL,
    est_instrumento numeric DEFAULT 0,
    CONSTRAINT ines_pk PRIMARY KEY (ide_instrumento)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_config_instrumento
    OWNER to postgres;

COMMENT ON COLUMN public.adm_config_instrumento.ide_instrumento
    IS 'Identificador del estado por instrumento';

COMMENT ON COLUMN public.adm_config_instrumento.est_instrumento
    IS 'Estado, activo o inactivo.';

------------------------------------------------------------------------------------------------------------------------------
--adm_config_ccd
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_config_ccd

-- DROP TABLE public.adm_config_ccd;

CREATE TABLE public.adm_config_ccd
(
    ide_ccd numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    ide_ofc_prod text COLLATE pg_catalog."default" NOT NULL,
    ide_serie numeric NOT NULL,
    ide_subserie numeric,
    ide_uni_amt text COLLATE pg_catalog."default" NOT NULL,
    est_ccd numeric,
    CONSTRAINT config_ccds_pk PRIMARY KEY (ide_ccd),
    CONSTRAINT config_ccds_seri_fk FOREIGN KEY (ide_serie)
        REFERENCES public.adm_serie (ide_serie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_config_ccd
    OWNER to postgres;
COMMENT ON TABLE public.adm_config_ccd
    IS 'Tabla que representa el Cuadro de ClasificaciÃ³n Documental';

COMMENT ON COLUMN public.adm_config_ccd.ide_ccd
    IS 'Identificador del cuadro de clasificaciÃ³n documental.';

COMMENT ON COLUMN public.adm_config_ccd.fec_cambio
    IS 'Fecha de cambio';

COMMENT ON COLUMN public.adm_config_ccd.fec_creacion
    IS 'Fecha de creaciÃ³n.';

COMMENT ON COLUMN public.adm_config_ccd.ide_usuario_cambio
    IS 'Identificador del usuario que cambia.';

COMMENT ON COLUMN public.adm_config_ccd.ide_uuid
    IS 'Identificador universal.';

COMMENT ON COLUMN public.adm_config_ccd.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_config_ccd.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_config_ccd.ide_ofc_prod
    IS 'Identificador de la Oficina de producciÃ³n.';

COMMENT ON COLUMN public.adm_config_ccd.ide_serie
    IS 'Identificador de la serie.';

COMMENT ON COLUMN public.adm_config_ccd.ide_subserie
    IS 'Identificador de la subserie.';

COMMENT ON COLUMN public.adm_config_ccd.ide_uni_amt
    IS 'Identificador de la unidad de almacenamiento.';

COMMENT ON COLUMN public.adm_config_ccd.est_ccd
    IS 'Estado del cuadro.';

------------------------------------------------------------------------------------------------------------------------------
--adm_ccd
------------------------------------------------------------------------------------------------------------------------------

-- Table: public.adm_ccd

-- DROP TABLE public.adm_ccd;

CREATE TABLE public.adm_ccd
(
    ide_ccd numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text COLLATE pg_catalog."default",
    niv_escritura numeric,
    niv_lectura numeric,
    ide_ofc_prod text COLLATE pg_catalog."default" NOT NULL,
    ide_serie numeric NOT NULL,
    ide_subserie numeric,
    ide_uni_amt text COLLATE pg_catalog."default" NOT NULL,
    est_ccd numeric,
    val_version text COLLATE pg_catalog."default",
    val_version_org text COLLATE pg_catalog."default",
    num_version_org numeric,
	nombre_comite text,
    num_acta text,
    fecha_acta date,
    CONSTRAINT ccds_seri_fk FOREIGN KEY (ide_serie)
        REFERENCES public.adm_serie (ide_serie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT ccds_subs_fk FOREIGN KEY (ide_subserie)
        REFERENCES public.adm_subserie (ide_subserie) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.adm_ccd
    OWNER to postgres;
COMMENT ON TABLE public.adm_ccd
    IS 'Tabla que representa el Cuadro de Clasificación Documental';

COMMENT ON COLUMN public.adm_ccd.ide_ccd
    IS 'Identificador del cuadro de clasificación documental.';

COMMENT ON COLUMN public.adm_ccd.fec_cambio
    IS 'Fecha de cambio';

COMMENT ON COLUMN public.adm_ccd.fec_creacion
    IS 'Fecha de creación.';

COMMENT ON COLUMN public.adm_ccd.ide_usuario_cambio
    IS 'Identificador del usuario que cambia.';

COMMENT ON COLUMN public.adm_ccd.ide_uuid
    IS 'Identificador universal.';

COMMENT ON COLUMN public.adm_ccd.niv_escritura
    IS 'Nivel de escritura.';

COMMENT ON COLUMN public.adm_ccd.niv_lectura
    IS 'Nivel de lectura.';

COMMENT ON COLUMN public.adm_ccd.ide_ofc_prod
    IS 'Identificador de la Oficina de producción.';

COMMENT ON COLUMN public.adm_ccd.ide_serie
    IS 'Identificador de la serie.';

COMMENT ON COLUMN public.adm_ccd.ide_subserie
    IS 'Identificador de la subserie.';

COMMENT ON COLUMN public.adm_ccd.ide_uni_amt
    IS 'Identificador de la unidad de almacenamiento.';

COMMENT ON COLUMN public.adm_ccd.est_ccd
    IS 'Estado del cuadro.';

COMMENT ON COLUMN public.adm_ccd.val_version
    IS 'Contiene la version del registro';
