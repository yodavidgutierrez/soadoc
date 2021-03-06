PGDMP         )                w            docmanager_test    10.5    10.5    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                       false            �           1262    18578    docmanager_test    DATABASE     �   CREATE DATABASE docmanager_test WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Spanish_Colombia.1252' LC_CTYPE = 'Spanish_Colombia.1252';
    DROP DATABASE docmanager_test;
             postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
             postgres    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                  postgres    false    3                        3079    12924    plpgsql 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    DROP EXTENSION plpgsql;
                  false            �           0    0    EXTENSION plpgsql    COMMENT     @   COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
                       false    1            �            1259    18579    adm_ccd    TABLE        CREATE TABLE public.adm_ccd (
    ide_ccd numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    ide_ofc_prod text NOT NULL,
    ide_serie numeric NOT NULL,
    ide_subserie numeric,
    ide_uni_amt text NOT NULL,
    est_ccd numeric,
    val_version text,
    val_version_org text,
    num_version_org numeric,
    nombre_comite text,
    num_acta text,
    fecha_acta date
);
    DROP TABLE public.adm_ccd;
       public         postgres    false    3            �           0    0    TABLE adm_ccd    COMMENT     b   COMMENT ON TABLE public.adm_ccd IS 'Tabla que representa el Cuadro de Clasificación Documental';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.ide_ccd    COMMENT     f   COMMENT ON COLUMN public.adm_ccd.ide_ccd IS 'Identificador del cuadro de clasificación documental.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.fec_cambio    COMMENT     B   COMMENT ON COLUMN public.adm_ccd.fec_cambio IS 'Fecha de cambio';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.fec_creacion    COMMENT     H   COMMENT ON COLUMN public.adm_ccd.fec_creacion IS 'Fecha de creación.';
            public       postgres    false    196            �           0    0 !   COLUMN adm_ccd.ide_usuario_cambio    COMMENT     `   COMMENT ON COLUMN public.adm_ccd.ide_usuario_cambio IS 'Identificador del usuario que cambia.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.ide_uuid    COMMENT     I   COMMENT ON COLUMN public.adm_ccd.ide_uuid IS 'Identificador universal.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.niv_escritura    COMMENT     I   COMMENT ON COLUMN public.adm_ccd.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.niv_lectura    COMMENT     E   COMMENT ON COLUMN public.adm_ccd.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.ide_ofc_prod    COMMENT     `   COMMENT ON COLUMN public.adm_ccd.ide_ofc_prod IS 'Identificador de la Oficina de producción.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.ide_serie    COMMENT     L   COMMENT ON COLUMN public.adm_ccd.ide_serie IS 'Identificador de la serie.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.ide_subserie    COMMENT     R   COMMENT ON COLUMN public.adm_ccd.ide_subserie IS 'Identificador de la subserie.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.ide_uni_amt    COMMENT     a   COMMENT ON COLUMN public.adm_ccd.ide_uni_amt IS 'Identificador de la unidad de almacenamiento.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.est_ccd    COMMENT     B   COMMENT ON COLUMN public.adm_ccd.est_ccd IS 'Estado del cuadro.';
            public       postgres    false    196            �           0    0    COLUMN adm_ccd.val_version    COMMENT     T   COMMENT ON COLUMN public.adm_ccd.val_version IS 'Contiene la version del registro';
            public       postgres    false    196            �            1259    18585    adm_config_ccd    TABLE     �  CREATE TABLE public.adm_config_ccd (
    ide_ccd numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    ide_ofc_prod text NOT NULL,
    ide_serie numeric NOT NULL,
    ide_subserie numeric,
    ide_uni_amt text NOT NULL,
    est_ccd numeric
);
 "   DROP TABLE public.adm_config_ccd;
       public         postgres    false    3            �           0    0    TABLE adm_config_ccd    COMMENT     k   COMMENT ON TABLE public.adm_config_ccd IS 'Tabla que representa el Cuadro de ClasificaciÃ³n Documental';
            public       postgres    false    197            �           0    0    COLUMN adm_config_ccd.ide_ccd    COMMENT     o   COMMENT ON COLUMN public.adm_config_ccd.ide_ccd IS 'Identificador del cuadro de clasificaciÃ³n documental.';
            public       postgres    false    197            �           0    0     COLUMN adm_config_ccd.fec_cambio    COMMENT     I   COMMENT ON COLUMN public.adm_config_ccd.fec_cambio IS 'Fecha de cambio';
            public       postgres    false    197            �           0    0 "   COLUMN adm_config_ccd.fec_creacion    COMMENT     Q   COMMENT ON COLUMN public.adm_config_ccd.fec_creacion IS 'Fecha de creaciÃ³n.';
            public       postgres    false    197            �           0    0 (   COLUMN adm_config_ccd.ide_usuario_cambio    COMMENT     g   COMMENT ON COLUMN public.adm_config_ccd.ide_usuario_cambio IS 'Identificador del usuario que cambia.';
            public       postgres    false    197            �           0    0    COLUMN adm_config_ccd.ide_uuid    COMMENT     P   COMMENT ON COLUMN public.adm_config_ccd.ide_uuid IS 'Identificador universal.';
            public       postgres    false    197            �           0    0 #   COLUMN adm_config_ccd.niv_escritura    COMMENT     P   COMMENT ON COLUMN public.adm_config_ccd.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    197            �           0    0 !   COLUMN adm_config_ccd.niv_lectura    COMMENT     L   COMMENT ON COLUMN public.adm_config_ccd.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    197            �           0    0 "   COLUMN adm_config_ccd.ide_ofc_prod    COMMENT     i   COMMENT ON COLUMN public.adm_config_ccd.ide_ofc_prod IS 'Identificador de la Oficina de producciÃ³n.';
            public       postgres    false    197            �           0    0    COLUMN adm_config_ccd.ide_serie    COMMENT     S   COMMENT ON COLUMN public.adm_config_ccd.ide_serie IS 'Identificador de la serie.';
            public       postgres    false    197            �           0    0 "   COLUMN adm_config_ccd.ide_subserie    COMMENT     Y   COMMENT ON COLUMN public.adm_config_ccd.ide_subserie IS 'Identificador de la subserie.';
            public       postgres    false    197            �           0    0 !   COLUMN adm_config_ccd.ide_uni_amt    COMMENT     h   COMMENT ON COLUMN public.adm_config_ccd.ide_uni_amt IS 'Identificador de la unidad de almacenamiento.';
            public       postgres    false    197            �           0    0    COLUMN adm_config_ccd.est_ccd    COMMENT     I   COMMENT ON COLUMN public.adm_config_ccd.est_ccd IS 'Estado del cuadro.';
            public       postgres    false    197            �            1259    18591    adm_config_instrumento    TABLE     y   CREATE TABLE public.adm_config_instrumento (
    ide_instrumento text NOT NULL,
    est_instrumento numeric DEFAULT 0
);
 *   DROP TABLE public.adm_config_instrumento;
       public         postgres    false    3            �           0    0 -   COLUMN adm_config_instrumento.ide_instrumento    COMMENT     o   COMMENT ON COLUMN public.adm_config_instrumento.ide_instrumento IS 'Identificador del estado por instrumento';
            public       postgres    false    198            �           0    0 -   COLUMN adm_config_instrumento.est_instrumento    COMMENT     a   COMMENT ON COLUMN public.adm_config_instrumento.est_instrumento IS 'Estado, activo o inactivo.';
            public       postgres    false    198            �            1259    18598    adm_dis_final    TABLE     7  CREATE TABLE public.adm_dis_final (
    ide_dis_final numeric NOT NULL,
    nom_dis_final text NOT NULL,
    des_dis_final text,
    sta_dis_final text,
    ide_usuario_cambio numeric,
    fec_cambio date,
    niv_lectura numeric,
    niv_escritura numeric,
    ide_uuid character(32),
    fec_creacion date
);
 !   DROP TABLE public.adm_dis_final;
       public         postgres    false    3            �           0    0 "   COLUMN adm_dis_final.sta_dis_final    COMMENT     Z   COMMENT ON COLUMN public.adm_dis_final.sta_dis_final IS 'Estado de la dispocicion final';
            public       postgres    false    199            �            1259    18604    adm_mot_creacion    TABLE     V  CREATE TABLE public.adm_mot_creacion (
    ide_mot_creacion numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    nom_mot_creacion text NOT NULL,
    est_mot_creacion text
);
 $   DROP TABLE public.adm_mot_creacion;
       public         postgres    false    3            �           0    0    TABLE adm_mot_creacion    COMMENT     [   COMMENT ON TABLE public.adm_mot_creacion IS 'Tabla del Nomenclador Motivo de CreaciÃ³n';
            public       postgres    false    200            �           0    0 "   COLUMN adm_mot_creacion.fec_cambio    COMMENT     L   COMMENT ON COLUMN public.adm_mot_creacion.fec_cambio IS 'Fecha de cambio.';
            public       postgres    false    200            �           0    0 $   COLUMN adm_mot_creacion.fec_creacion    COMMENT     S   COMMENT ON COLUMN public.adm_mot_creacion.fec_creacion IS 'Fecha de creaciÃ³n.';
            public       postgres    false    200            �           0    0 *   COLUMN adm_mot_creacion.ide_usuario_cambio    COMMENT     i   COMMENT ON COLUMN public.adm_mot_creacion.ide_usuario_cambio IS 'Identificador del usuario que cambia.';
            public       postgres    false    200            �           0    0     COLUMN adm_mot_creacion.ide_uuid    COMMENT     Q   COMMENT ON COLUMN public.adm_mot_creacion.ide_uuid IS 'IdentificaciÃ³n UUID.';
            public       postgres    false    200            �           0    0 %   COLUMN adm_mot_creacion.niv_escritura    COMMENT     R   COMMENT ON COLUMN public.adm_mot_creacion.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    200            �           0    0 #   COLUMN adm_mot_creacion.niv_lectura    COMMENT     N   COMMENT ON COLUMN public.adm_mot_creacion.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    200            �           0    0 (   COLUMN adm_mot_creacion.nom_mot_creacion    COMMENT     `   COMMENT ON COLUMN public.adm_mot_creacion.nom_mot_creacion IS 'Nombre del motivo de creacion.';
            public       postgres    false    200            �            1259    18610    adm_rel_eq_destino    TABLE     �   CREATE TABLE public.adm_rel_eq_destino (
    ide_rel_destino numeric NOT NULL,
    ide_rel_origen numeric,
    ide_uni_amt text NOT NULL,
    ide_ofc_prod text NOT NULL,
    ide_serie numeric,
    ide_subserie numeric,
    fec_creacion date
);
 &   DROP TABLE public.adm_rel_eq_destino;
       public         postgres    false    3            �            1259    18616    adm_rel_eq_origen    TABLE     �   CREATE TABLE public.adm_rel_eq_origen (
    ide_rel_origen numeric NOT NULL,
    ide_uni_amt text NOT NULL,
    ide_ofc_prod text NOT NULL,
    ide_serie numeric,
    ide_subserie numeric,
    fec_creacion date,
    num_version_org numeric
);
 %   DROP TABLE public.adm_rel_eq_origen;
       public         postgres    false    3            �            1259    18622    adm_ser_subser_tpg    TABLE     �  CREATE TABLE public.adm_ser_subser_tpg (
    ide_serie numeric NOT NULL,
    ide_tpg_doc numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    ide_subserie numeric,
    ide_rel_sst numeric NOT NULL,
    orden numeric
);
 &   DROP TABLE public.adm_ser_subser_tpg;
       public         postgres    false    3            �           0    0    TABLE adm_ser_subser_tpg    COMMENT     t   COMMENT ON TABLE public.adm_ser_subser_tpg IS 'Tabla que relaciona las Series Subseries y Tipologias Documentales';
            public       postgres    false    203            �           0    0 #   COLUMN adm_ser_subser_tpg.ide_serie    COMMENT     W   COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_serie IS 'Identificador de la serie.';
            public       postgres    false    203            �           0    0 %   COLUMN adm_ser_subser_tpg.ide_tpg_doc    COMMENT     `   COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_tpg_doc IS 'Identificador de la tipologÃ­a.';
            public       postgres    false    203            �           0    0 $   COLUMN adm_ser_subser_tpg.fec_cambio    COMMENT     N   COMMENT ON COLUMN public.adm_ser_subser_tpg.fec_cambio IS 'Fecha de cambio.';
            public       postgres    false    203            �           0    0 &   COLUMN adm_ser_subser_tpg.fec_creacion    COMMENT     U   COMMENT ON COLUMN public.adm_ser_subser_tpg.fec_creacion IS 'Fecha de creaciÃ³n.';
            public       postgres    false    203            �           0    0 ,   COLUMN adm_ser_subser_tpg.ide_usuario_cambio    COMMENT     k   COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_usuario_cambio IS 'Identificador del usuario que cambia.';
            public       postgres    false    203            �           0    0 "   COLUMN adm_ser_subser_tpg.ide_uuid    COMMENT     S   COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_uuid IS 'IdentificaciÃ³n UUID.';
            public       postgres    false    203            �           0    0 '   COLUMN adm_ser_subser_tpg.niv_escritura    COMMENT     T   COMMENT ON COLUMN public.adm_ser_subser_tpg.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    203            �           0    0 %   COLUMN adm_ser_subser_tpg.niv_lectura    COMMENT     P   COMMENT ON COLUMN public.adm_ser_subser_tpg.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    203            �           0    0 &   COLUMN adm_ser_subser_tpg.ide_subserie    COMMENT     ]   COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_subserie IS 'Identificador de la subserie.';
            public       postgres    false    203            �           0    0 %   COLUMN adm_ser_subser_tpg.ide_rel_sst    COMMENT     }   COMMENT ON COLUMN public.adm_ser_subser_tpg.ide_rel_sst IS 'Identificador de la relaciÃ³n serie, subserie, tipologÃ­a.';
            public       postgres    false    203            �            1259    18628 	   adm_serie    TABLE     �  CREATE TABLE public.adm_serie (
    ide_serie numeric NOT NULL,
    act_administrativo text NOT NULL,
    car_administrativa numeric,
    car_legal numeric,
    car_tecnica numeric,
    cod_serie text NOT NULL,
    est_serie numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    fue_bibliografica text,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    nom_serie text NOT NULL,
    not_alcance text,
    ide_mot_creacion numeric NOT NULL,
    car_contable numeric,
    car_juridica numeric,
    con_publica numeric,
    con_clasificada numeric,
    con_reservada numeric
);
    DROP TABLE public.adm_serie;
       public         postgres    false    3            �           0    0    COLUMN adm_serie.car_legal    COMMENT     l   COMMENT ON COLUMN public.adm_serie.car_legal IS 'Hace referencia a la CaracterÃ­stica Legal de la serie';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.car_tecnica    COMMENT     s   COMMENT ON COLUMN public.adm_serie.car_tecnica IS 'Hace referencia a la CaracterÃ­stica TÃ©cnica de la serie';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.cod_serie    COMMENT     ^   COMMENT ON COLUMN public.adm_serie.cod_serie IS 'Hace referencia a la cÃ³digo de la serie';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.est_serie    COMMENT     Y   COMMENT ON COLUMN public.adm_serie.est_serie IS 'Hace referencia al Estado de la serie';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.fec_cambio    COMMENT     E   COMMENT ON COLUMN public.adm_serie.fec_cambio IS 'Fecha de cambio.';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.fec_creacion    COMMENT     X   COMMENT ON COLUMN public.adm_serie.fec_creacion IS 'Fecha de creaciÃ³n del registro';
            public       postgres    false    204            �           0    0 #   COLUMN adm_serie.ide_usuario_cambio    COMMENT     ^   COMMENT ON COLUMN public.adm_serie.ide_usuario_cambio IS 'Identificador usuario que cambio.';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.ide_uuid    COMMENT     G   COMMENT ON COLUMN public.adm_serie.ide_uuid IS 'Identificacion UUID.';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.niv_escritura    COMMENT     K   COMMENT ON COLUMN public.adm_serie.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.niv_lectura    COMMENT     G   COMMENT ON COLUMN public.adm_serie.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.nom_serie    COMMENT     Y   COMMENT ON COLUMN public.adm_serie.nom_serie IS 'Hace referencia al Nombre de la serie';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.not_alcance    COMMENT     f   COMMENT ON COLUMN public.adm_serie.not_alcance IS 'Hace referencia a la Nota de Alcance de la serie';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.car_contable    COMMENT     o   COMMENT ON COLUMN public.adm_serie.car_contable IS 'Hace referencia a la Caracteristica contable de la serie';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.car_juridica    COMMENT     o   COMMENT ON COLUMN public.adm_serie.car_juridica IS 'Hace referencia a la caracteristica juridica de la serie';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.con_publica    COMMENT     o   COMMENT ON COLUMN public.adm_serie.con_publica IS 'Hace referencia a la confidencialidad publica de la serie';
            public       postgres    false    204            �           0    0     COLUMN adm_serie.con_clasificada    COMMENT     w   COMMENT ON COLUMN public.adm_serie.con_clasificada IS 'Hace referencia a la confidencialidad clasificada de la serie';
            public       postgres    false    204            �           0    0    COLUMN adm_serie.con_reservada    COMMENT     s   COMMENT ON COLUMN public.adm_serie.con_reservada IS 'Hace referencia a la confidencialidad reservada de la serie';
            public       postgres    false    204            �            1259    18634    adm_soporte    TABLE     K  CREATE TABLE public.adm_soporte (
    ide_soporte numeric NOT NULL,
    des_soporte text NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    nom_soporte text NOT NULL
);
    DROP TABLE public.adm_soporte;
       public         postgres    false    3            �           0    0    TABLE adm_soporte    COMMENT     `   COMMENT ON TABLE public.adm_soporte IS 'Tabla Soporte, contiene los soportes de las entidades';
            public       postgres    false    205            �           0    0    COLUMN adm_soporte.ide_soporte    COMMENT     ^   COMMENT ON COLUMN public.adm_soporte.ide_soporte IS 'Llave primaria: Id de la Tabla Soporte';
            public       postgres    false    205            �           0    0    COLUMN adm_soporte.des_soporte    COMMENT     S   COMMENT ON COLUMN public.adm_soporte.des_soporte IS 'DescripciÃ³n del soporte.';
            public       postgres    false    205            �           0    0    COLUMN adm_soporte.fec_cambio    COMMENT     G   COMMENT ON COLUMN public.adm_soporte.fec_cambio IS 'Fecha de cambio.';
            public       postgres    false    205            �           0    0    COLUMN adm_soporte.fec_creacion    COMMENT     N   COMMENT ON COLUMN public.adm_soporte.fec_creacion IS 'Fecha de creaciÃ³n.';
            public       postgres    false    205            �           0    0 %   COLUMN adm_soporte.ide_usuario_cambio    COMMENT     `   COMMENT ON COLUMN public.adm_soporte.ide_usuario_cambio IS 'Identificador usuario que cambio.';
            public       postgres    false    205            �           0    0    COLUMN adm_soporte.ide_uuid    COMMENT     I   COMMENT ON COLUMN public.adm_soporte.ide_uuid IS 'Identificacion UUID.';
            public       postgres    false    205            �           0    0     COLUMN adm_soporte.niv_escritura    COMMENT     M   COMMENT ON COLUMN public.adm_soporte.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    205            �           0    0    COLUMN adm_soporte.niv_lectura    COMMENT     I   COMMENT ON COLUMN public.adm_soporte.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    205            �           0    0    COLUMN adm_soporte.nom_soporte    COMMENT     ]   COMMENT ON COLUMN public.adm_soporte.nom_soporte IS 'Hace referencia al nombre del soporte';
            public       postgres    false    205            �            1259    18640    adm_subserie    TABLE     �  CREATE TABLE public.adm_subserie (
    ide_subserie numeric NOT NULL,
    act_administrativo text NOT NULL,
    car_administrativa numeric,
    car_legal numeric,
    car_tecnica numeric,
    cod_subserie text NOT NULL,
    est_subserie numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    fue_bibliografica text,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    nom_subserie text NOT NULL,
    not_alcance text,
    ide_mot_creacion numeric NOT NULL,
    ide_serie numeric,
    car_juridica numeric,
    car_contable numeric,
    con_publica numeric,
    con_clasificada numeric,
    con_reservada numeric
);
     DROP TABLE public.adm_subserie;
       public         postgres    false    3            �           0    0 &   COLUMN adm_subserie.act_administrativo    COMMENT     f   COMMENT ON COLUMN public.adm_subserie.act_administrativo IS 'Hace referencia al acto administrativo';
            public       postgres    false    206            �           0    0 &   COLUMN adm_subserie.car_administrativa    COMMENT     m   COMMENT ON COLUMN public.adm_subserie.car_administrativa IS 'Caracteristica administrativa de la subserie.';
            public       postgres    false    206            �           0    0    COLUMN adm_subserie.car_legal    COMMENT     o   COMMENT ON COLUMN public.adm_subserie.car_legal IS 'Hace referencia a la Caracteristica Legal de la Subserie';
            public       postgres    false    206            �           0    0    COLUMN adm_subserie.car_tecnica    COMMENT     s   COMMENT ON COLUMN public.adm_subserie.car_tecnica IS 'Hace referencia a la Caracteristica Tecnica de la Subserie';
            public       postgres    false    206            �           0    0     COLUMN adm_subserie.cod_subserie    COMMENT     b   COMMENT ON COLUMN public.adm_subserie.cod_subserie IS 'Hace referencia al codigo de la Subserie';
            public       postgres    false    206            �           0    0     COLUMN adm_subserie.est_subserie    COMMENT     b   COMMENT ON COLUMN public.adm_subserie.est_subserie IS 'Hace referencia al Estado de la Subserie';
            public       postgres    false    206            �           0    0    COLUMN adm_subserie.fec_cambio    COMMENT     H   COMMENT ON COLUMN public.adm_subserie.fec_cambio IS 'Fecha de cambio.';
            public       postgres    false    206            �           0    0     COLUMN adm_subserie.fec_creacion    COMMENT     L   COMMENT ON COLUMN public.adm_subserie.fec_creacion IS 'Fecha de creacion.';
            public       postgres    false    206                        0    0 %   COLUMN adm_subserie.fue_bibliografica    COMMENT     T   COMMENT ON COLUMN public.adm_subserie.fue_bibliografica IS 'Fuente bibliogrifica.';
            public       postgres    false    206                       0    0 &   COLUMN adm_subserie.ide_usuario_cambio    COMMENT     a   COMMENT ON COLUMN public.adm_subserie.ide_usuario_cambio IS 'Identificador usuario que cambio.';
            public       postgres    false    206                       0    0    COLUMN adm_subserie.ide_uuid    COMMENT     J   COMMENT ON COLUMN public.adm_subserie.ide_uuid IS 'Identificacion UUID.';
            public       postgres    false    206                       0    0 !   COLUMN adm_subserie.niv_escritura    COMMENT     N   COMMENT ON COLUMN public.adm_subserie.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    206                       0    0    COLUMN adm_subserie.niv_lectura    COMMENT     J   COMMENT ON COLUMN public.adm_subserie.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    206                       0    0     COLUMN adm_subserie.nom_subserie    COMMENT     b   COMMENT ON COLUMN public.adm_subserie.nom_subserie IS 'Hace referencia al Nombre de la Subserie';
            public       postgres    false    206                       0    0    COLUMN adm_subserie.not_alcance    COMMENT     i   COMMENT ON COLUMN public.adm_subserie.not_alcance IS 'Hace referencia a la Nota Alcance de la Subserie';
            public       postgres    false    206                       0    0 $   COLUMN adm_subserie.ide_mot_creacion    COMMENT     c   COMMENT ON COLUMN public.adm_subserie.ide_mot_creacion IS 'Identificador del motivo de creacion.';
            public       postgres    false    206                       0    0    COLUMN adm_subserie.ide_serie    COMMENT     c   COMMENT ON COLUMN public.adm_subserie.ide_serie IS 'llave forinea: Id de la tabla Serie asociada';
            public       postgres    false    206            	           0    0     COLUMN adm_subserie.car_juridica    COMMENT     u   COMMENT ON COLUMN public.adm_subserie.car_juridica IS 'Hace referencia a la caracteristica juridica de la subserie';
            public       postgres    false    206            
           0    0     COLUMN adm_subserie.car_contable    COMMENT     u   COMMENT ON COLUMN public.adm_subserie.car_contable IS 'hace referencia a la caracteristica contable de la subserie';
            public       postgres    false    206                       0    0    COLUMN adm_subserie.con_publica    COMMENT     u   COMMENT ON COLUMN public.adm_subserie.con_publica IS 'hace referencia a la confidencialidad publica de la subserie';
            public       postgres    false    206                       0    0 #   COLUMN adm_subserie.con_clasificada    COMMENT     z   COMMENT ON COLUMN public.adm_subserie.con_clasificada IS 'hace referencia a la confiabilidad clasificada de la subserie';
            public       postgres    false    206                       0    0 !   COLUMN adm_subserie.con_reservada    COMMENT     v   COMMENT ON COLUMN public.adm_subserie.con_reservada IS 'hace referencia a la confiabilidad reservada de la subserie';
            public       postgres    false    206            �            1259    18646    adm_tab_ret_doc    TABLE     $  CREATE TABLE public.adm_tab_ret_doc (
    ide_tab_ret_doc numeric NOT NULL,
    ac_trd numeric NOT NULL,
    ag_trd numeric NOT NULL,
    est_tab_ret_doc numeric,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    pro_trd text NOT NULL,
    ide_dis_final numeric NOT NULL,
    ide_serie numeric NOT NULL,
    ide_subserie numeric,
    ide_ofc_prod text NOT NULL,
    ide_uni_amt text NOT NULL
);
 #   DROP TABLE public.adm_tab_ret_doc;
       public         postgres    false    3                       0    0    TABLE adm_tab_ret_doc    COMMENT     v   COMMENT ON TABLE public.adm_tab_ret_doc IS 'Tabla que almacena los valores de las Tablas de RetenciÃ³n Documental';
            public       postgres    false    207                       0    0 &   COLUMN adm_tab_ret_doc.ide_tab_ret_doc    COMMENT     \   COMMENT ON COLUMN public.adm_tab_ret_doc.ide_tab_ret_doc IS 'Llave primaria: Id de la TRD';
            public       postgres    false    207                       0    0    COLUMN adm_tab_ret_doc.ac_trd    COMMENT     j   COMMENT ON COLUMN public.adm_tab_ret_doc.ac_trd IS 'Hace referencia a la Retencion AC asociada a la TRD';
            public       postgres    false    207                       0    0    COLUMN adm_tab_ret_doc.ag_trd    COMMENT     j   COMMENT ON COLUMN public.adm_tab_ret_doc.ag_trd IS 'Hace referencia a la Retencion AG asociada a la TRD';
            public       postgres    false    207                       0    0 &   COLUMN adm_tab_ret_doc.est_tab_ret_doc    COMMENT     m   COMMENT ON COLUMN public.adm_tab_ret_doc.est_tab_ret_doc IS 'Estado de la tabla de retenciÃ³n dcumental.';
            public       postgres    false    207                       0    0 !   COLUMN adm_tab_ret_doc.fec_cambio    COMMENT     K   COMMENT ON COLUMN public.adm_tab_ret_doc.fec_cambio IS 'Fecha de cambio.';
            public       postgres    false    207                       0    0 #   COLUMN adm_tab_ret_doc.fec_creacion    COMMENT     R   COMMENT ON COLUMN public.adm_tab_ret_doc.fec_creacion IS 'Fecha de creaciÃ³n.';
            public       postgres    false    207                       0    0 )   COLUMN adm_tab_ret_doc.ide_usuario_cambio    COMMENT     d   COMMENT ON COLUMN public.adm_tab_ret_doc.ide_usuario_cambio IS 'Identificador usuario que cambio.';
            public       postgres    false    207                       0    0    COLUMN adm_tab_ret_doc.ide_uuid    COMMENT     M   COMMENT ON COLUMN public.adm_tab_ret_doc.ide_uuid IS 'Identificacion UUID.';
            public       postgres    false    207                       0    0 $   COLUMN adm_tab_ret_doc.niv_escritura    COMMENT     Q   COMMENT ON COLUMN public.adm_tab_ret_doc.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    207                       0    0 "   COLUMN adm_tab_ret_doc.niv_lectura    COMMENT     M   COMMENT ON COLUMN public.adm_tab_ret_doc.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    207                       0    0    COLUMN adm_tab_ret_doc.pro_trd    COMMENT     j   COMMENT ON COLUMN public.adm_tab_ret_doc.pro_trd IS 'Hace referencia al procedimiento asociada a la TRD';
            public       postgres    false    207                       0    0 $   COLUMN adm_tab_ret_doc.ide_dis_final    COMMENT     {   COMMENT ON COLUMN public.adm_tab_ret_doc.ide_dis_final IS 'Hace referencia al ide de disposicion final asociada a la TRD';
            public       postgres    false    207                       0    0     COLUMN adm_tab_ret_doc.ide_serie    COMMENT     a   COMMENT ON COLUMN public.adm_tab_ret_doc.ide_serie IS 'Llave foranea: Id de la  Serie asociada';
            public       postgres    false    207                       0    0 #   COLUMN adm_tab_ret_doc.ide_subserie    COMMENT     g   COMMENT ON COLUMN public.adm_tab_ret_doc.ide_subserie IS 'Llave foranea: Id de la  Subserie asociada';
            public       postgres    false    207            �            1259    18652    adm_tab_ret_doc_org    TABLE     N  CREATE TABLE public.adm_tab_ret_doc_org (
    ide_ofc_prod text NOT NULL,
    ide_uni_amt text NOT NULL,
    ide_tab_ret_doc_org numeric NOT NULL,
    ide_tab_ret_doc numeric NOT NULL,
    ac_trd numeric NOT NULL,
    ag_trd numeric NOT NULL,
    pro_trd text NOT NULL,
    ide_dis_final numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    num_version numeric NOT NULL,
    nombre_serie text,
    nombre_subserie text
);
 '   DROP TABLE public.adm_tab_ret_doc_org;
       public         postgres    false    3                       0    0 '   COLUMN adm_tab_ret_doc_org.ide_ofc_prod    COMMENT     r   COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_ofc_prod IS 'Identificador del organigrama, oficina productora';
            public       postgres    false    208                       0    0 &   COLUMN adm_tab_ret_doc_org.ide_uni_amt    COMMENT     t   COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_uni_amt IS 'Identificador del organigrama, unidad administrativa';
            public       postgres    false    208                       0    0 .   COLUMN adm_tab_ret_doc_org.ide_tab_ret_doc_org    COMMENT     a   COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_tab_ret_doc_org IS 'Identificador de la tabla';
            public       postgres    false    208                        0    0 *   COLUMN adm_tab_ret_doc_org.ide_tab_ret_doc    COMMENT     u   COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_tab_ret_doc IS 'Identificador de la tabla de retencion documental';
            public       postgres    false    208            !           0    0 !   COLUMN adm_tab_ret_doc_org.ac_trd    COMMENT     n   COMMENT ON COLUMN public.adm_tab_ret_doc_org.ac_trd IS 'Hace referencia a la Retencion AC asociada a la TRD';
            public       postgres    false    208            "           0    0 !   COLUMN adm_tab_ret_doc_org.ag_trd    COMMENT     n   COMMENT ON COLUMN public.adm_tab_ret_doc_org.ag_trd IS 'Hace referencia a la Retencion AG asociada a la TRD';
            public       postgres    false    208            #           0    0 "   COLUMN adm_tab_ret_doc_org.pro_trd    COMMENT     n   COMMENT ON COLUMN public.adm_tab_ret_doc_org.pro_trd IS 'Hace referencia al procedimiento asociada a la TRD';
            public       postgres    false    208            $           0    0 (   COLUMN adm_tab_ret_doc_org.ide_dis_final    COMMENT        COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_dis_final IS 'Hace referencia al ide de disposicion final asociada a la TRD';
            public       postgres    false    208            %           0    0 %   COLUMN adm_tab_ret_doc_org.fec_cambio    COMMENT     O   COMMENT ON COLUMN public.adm_tab_ret_doc_org.fec_cambio IS 'Fecha de cambio.';
            public       postgres    false    208            &           0    0 '   COLUMN adm_tab_ret_doc_org.fec_creacion    COMMENT     R   COMMENT ON COLUMN public.adm_tab_ret_doc_org.fec_creacion IS 'Fecha de creacion';
            public       postgres    false    208            '           0    0 -   COLUMN adm_tab_ret_doc_org.ide_usuario_cambio    COMMENT     h   COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_usuario_cambio IS 'Identificador usuario que cambio.';
            public       postgres    false    208            (           0    0 #   COLUMN adm_tab_ret_doc_org.ide_uuid    COMMENT     Q   COMMENT ON COLUMN public.adm_tab_ret_doc_org.ide_uuid IS 'Identificacion UUID.';
            public       postgres    false    208            )           0    0 (   COLUMN adm_tab_ret_doc_org.niv_escritura    COMMENT     U   COMMENT ON COLUMN public.adm_tab_ret_doc_org.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    208            *           0    0 &   COLUMN adm_tab_ret_doc_org.niv_lectura    COMMENT     Q   COMMENT ON COLUMN public.adm_tab_ret_doc_org.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    208            +           0    0 &   COLUMN adm_tab_ret_doc_org.num_version    COMMENT     i   COMMENT ON COLUMN public.adm_tab_ret_doc_org.num_version IS 'Identificador del nÃºmero de versiÃ³n';
            public       postgres    false    208            ,           0    0 '   COLUMN adm_tab_ret_doc_org.nombre_serie    COMMENT     f   COMMENT ON COLUMN public.adm_tab_ret_doc_org.nombre_serie IS 'hace referencia al nombre de la serie';
            public       postgres    false    208            -           0    0 *   COLUMN adm_tab_ret_doc_org.nombre_subserie    COMMENT     l   COMMENT ON COLUMN public.adm_tab_ret_doc_org.nombre_subserie IS 'hace referencia al nombre de la subserie';
            public       postgres    false    208            �            1259    18658    adm_tpg_doc    TABLE     S  CREATE TABLE public.adm_tpg_doc (
    ide_tpg_doc numeric NOT NULL,
    car_administrativa numeric,
    car_legal numeric,
    car_tecnica numeric,
    cod_tpg_doc text,
    est_tpg_doc numeric NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    nom_tpg_doc text NOT NULL,
    ide_soporte numeric,
    ide_tra_documental numeric,
    not_alcance text,
    fue_bibliografica text,
    car_juridico numeric,
    car_contable numeric
);
    DROP TABLE public.adm_tpg_doc;
       public         postgres    false    3            .           0    0    TABLE adm_tpg_doc    COMMENT     T   COMMENT ON TABLE public.adm_tpg_doc IS 'Tabla que almacena los Tipos Documentales';
            public       postgres    false    209            /           0    0    COLUMN adm_tpg_doc.ide_tpg_doc    COMMENT     k   COMMENT ON COLUMN public.adm_tpg_doc.ide_tpg_doc IS 'Llave primaria: id de la tabla Tipologia Documental';
            public       postgres    false    209            0           0    0 %   COLUMN adm_tpg_doc.car_administrativa    COMMENT     _   COMMENT ON COLUMN public.adm_tpg_doc.car_administrativa IS 'CaracterÃ­stica adminstrativa.';
            public       postgres    false    209            1           0    0    COLUMN adm_tpg_doc.car_legal    COMMENT     �   COMMENT ON COLUMN public.adm_tpg_doc.car_legal IS 'Hace referencia a la Caracteristica Legal de la tabla Tipologia Documental';
            public       postgres    false    209            2           0    0    COLUMN adm_tpg_doc.car_tecnica    COMMENT     �   COMMENT ON COLUMN public.adm_tpg_doc.car_tecnica IS 'Hace referencia a la Caracteristica Tecnica de la tabla Tipologia Documental';
            public       postgres    false    209            3           0    0    COLUMN adm_tpg_doc.cod_tpg_doc    COMMENT     r   COMMENT ON COLUMN public.adm_tpg_doc.cod_tpg_doc IS 'Hace referencia al Codigo de la tabla Tipologia Documental';
            public       postgres    false    209            4           0    0    COLUMN adm_tpg_doc.est_tpg_doc    COMMENT     r   COMMENT ON COLUMN public.adm_tpg_doc.est_tpg_doc IS 'Hace referencia al Estado de la tabla Tipologia Documental';
            public       postgres    false    209            5           0    0    COLUMN adm_tpg_doc.fec_cambio    COMMENT     G   COMMENT ON COLUMN public.adm_tpg_doc.fec_cambio IS 'Fecha de cambio.';
            public       postgres    false    209            6           0    0    COLUMN adm_tpg_doc.fec_creacion    COMMENT     N   COMMENT ON COLUMN public.adm_tpg_doc.fec_creacion IS 'Fecha de creaciÃ³n.';
            public       postgres    false    209            7           0    0 %   COLUMN adm_tpg_doc.ide_usuario_cambio    COMMENT     `   COMMENT ON COLUMN public.adm_tpg_doc.ide_usuario_cambio IS 'Identificador usuario que cambio.';
            public       postgres    false    209            8           0    0    COLUMN adm_tpg_doc.ide_uuid    COMMENT     I   COMMENT ON COLUMN public.adm_tpg_doc.ide_uuid IS 'Identificacion UUID.';
            public       postgres    false    209            9           0    0     COLUMN adm_tpg_doc.niv_escritura    COMMENT     M   COMMENT ON COLUMN public.adm_tpg_doc.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    209            :           0    0    COLUMN adm_tpg_doc.niv_lectura    COMMENT     I   COMMENT ON COLUMN public.adm_tpg_doc.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    209            ;           0    0    COLUMN adm_tpg_doc.nom_tpg_doc    COMMENT     r   COMMENT ON COLUMN public.adm_tpg_doc.nom_tpg_doc IS 'Hace referencia al Nombre de la tabla Tipologia Documental';
            public       postgres    false    209            <           0    0    COLUMN adm_tpg_doc.ide_soporte    COMMENT     i   COMMENT ON COLUMN public.adm_tpg_doc.ide_soporte IS 'Llave forÃ¡nea: Id de la tabla soporte asociada';
            public       postgres    false    209            =           0    0 %   COLUMN adm_tpg_doc.ide_tra_documental    COMMENT     k   COMMENT ON COLUMN public.adm_tpg_doc.ide_tra_documental IS 'Identificador de la tradiciÃ³n documental.';
            public       postgres    false    209            >           0    0    COLUMN adm_tpg_doc.not_alcance    COMMENT     H   COMMENT ON COLUMN public.adm_tpg_doc.not_alcance IS 'Nota de alcance.';
            public       postgres    false    209            ?           0    0 $   COLUMN adm_tpg_doc.fue_bibliografica    COMMENT     V   COMMENT ON COLUMN public.adm_tpg_doc.fue_bibliografica IS 'Fuente bibliogrÃ¡fica.';
            public       postgres    false    209            �            1259    18664    adm_trad_doc    TABLE     O  CREATE TABLE public.adm_trad_doc (
    ide_trad_doc numeric NOT NULL,
    des_trad_doc text NOT NULL,
    fec_cambio timestamp(6) without time zone,
    fec_creacion timestamp(6) without time zone,
    ide_usuario_cambio numeric,
    ide_uuid text,
    niv_escritura numeric,
    niv_lectura numeric,
    nom_trad_doc text NOT NULL
);
     DROP TABLE public.adm_trad_doc;
       public         postgres    false    3            @           0    0    TABLE adm_trad_doc    COMMENT     e   COMMENT ON TABLE public.adm_trad_doc IS 'Tabla que almacena el Nomenclador de Tradicion Documental';
            public       postgres    false    210            A           0    0     COLUMN adm_trad_doc.ide_trad_doc    COMMENT     c   COMMENT ON COLUMN public.adm_trad_doc.ide_trad_doc IS 'Identificador de la tradicion documental.';
            public       postgres    false    210            B           0    0     COLUMN adm_trad_doc.des_trad_doc    COMMENT     ^   COMMENT ON COLUMN public.adm_trad_doc.des_trad_doc IS 'Descripcion la tradicion documental.';
            public       postgres    false    210            C           0    0    COLUMN adm_trad_doc.fec_cambio    COMMENT     H   COMMENT ON COLUMN public.adm_trad_doc.fec_cambio IS 'Fecha de cambio.';
            public       postgres    false    210            D           0    0     COLUMN adm_trad_doc.fec_creacion    COMMENT     L   COMMENT ON COLUMN public.adm_trad_doc.fec_creacion IS 'Fecha de creacion.';
            public       postgres    false    210            E           0    0 &   COLUMN adm_trad_doc.ide_usuario_cambio    COMMENT     a   COMMENT ON COLUMN public.adm_trad_doc.ide_usuario_cambio IS 'Identificador usuario que cambio.';
            public       postgres    false    210            F           0    0    COLUMN adm_trad_doc.ide_uuid    COMMENT     J   COMMENT ON COLUMN public.adm_trad_doc.ide_uuid IS 'Identificacion UUID.';
            public       postgres    false    210            G           0    0 !   COLUMN adm_trad_doc.niv_escritura    COMMENT     N   COMMENT ON COLUMN public.adm_trad_doc.niv_escritura IS 'Nivel de escritura.';
            public       postgres    false    210            H           0    0    COLUMN adm_trad_doc.niv_lectura    COMMENT     J   COMMENT ON COLUMN public.adm_trad_doc.niv_lectura IS 'Nivel de lectura.';
            public       postgres    false    210            I           0    0     COLUMN adm_trad_doc.nom_trad_doc    COMMENT     \   COMMENT ON COLUMN public.adm_trad_doc.nom_trad_doc IS 'Nombre de la tradicion documental.';
            public       postgres    false    210            �            1259    18670    adm_version_trd    TABLE     B  CREATE TABLE public.adm_version_trd (
    ide_version numeric NOT NULL,
    val_version text NOT NULL,
    fec_version date NOT NULL,
    ide_usuario numeric,
    ide_ofc_prod text NOT NULL,
    ide_uni_amt text NOT NULL,
    num_version numeric NOT NULL,
    nombre_comite text,
    num_acta text,
    fecha_acta date
);
 #   DROP TABLE public.adm_version_trd;
       public         postgres    false    3            J           0    0 "   COLUMN adm_version_trd.ide_version    COMMENT     X   COMMENT ON COLUMN public.adm_version_trd.ide_version IS 'Identificador de la version.';
            public       postgres    false    211            K           0    0 "   COLUMN adm_version_trd.val_version    COMMENT     P   COMMENT ON COLUMN public.adm_version_trd.val_version IS 'valor de la version.';
            public       postgres    false    211            L           0    0 "   COLUMN adm_version_trd.fec_version    COMMENT     N   COMMENT ON COLUMN public.adm_version_trd.fec_version IS 'Fecha de creacion.';
            public       postgres    false    211            M           0    0 "   COLUMN adm_version_trd.ide_usuario    COMMENT     V   COMMENT ON COLUMN public.adm_version_trd.ide_usuario IS 'Identificador del usuario.';
            public       postgres    false    211            N           0    0 #   COLUMN adm_version_trd.ide_ofc_prod    COMMENT     d   COMMENT ON COLUMN public.adm_version_trd.ide_ofc_prod IS 'Identificador de la oficina productora.';
            public       postgres    false    211            O           0    0 "   COLUMN adm_version_trd.ide_uni_amt    COMMENT     f   COMMENT ON COLUMN public.adm_version_trd.ide_uni_amt IS 'Identificador de la unidad administrativa.';
            public       postgres    false    211            P           0    0 "   COLUMN adm_version_trd.num_version    COMMENT     Q   COMMENT ON COLUMN public.adm_version_trd.num_version IS 'Numero de la version.';
            public       postgres    false    211            �            1259    18676    cm_carga_masiva    TABLE     �   CREATE TABLE public.cm_carga_masiva (
    id numeric NOT NULL,
    nombre text,
    fecha_creacion timestamp(6) without time zone,
    total_registros numeric,
    estado text,
    total_registros_exitosos numeric,
    total_registros_error numeric
);
 #   DROP TABLE public.cm_carga_masiva;
       public         postgres    false    3            �            1259    18682    cm_registro_carga_masiva    TABLE     �   CREATE TABLE public.cm_registro_carga_masiva (
    id numeric NOT NULL,
    carga_masiva_id numeric,
    contenido text,
    estado text,
    mensajes text
);
 ,   DROP TABLE public.cm_registro_carga_masiva;
       public         postgres    false    3            �            1259    18688    table_generator    TABLE     [   CREATE TABLE public.table_generator (
    seq_name text NOT NULL,
    seq_value numeric
);
 #   DROP TABLE public.table_generator;
       public         postgres    false    3            �            1259    18694    tvs_config_org_administrativo    TABLE       CREATE TABLE public.tvs_config_org_administrativo (
    ide_orga_admin numeric NOT NULL,
    cod_org text NOT NULL,
    nom_org text NOT NULL,
    desc_org text NOT NULL,
    ind_es_activo text NOT NULL,
    ide_direccion numeric,
    ide_plan_responsable numeric,
    ide_orga_admin_padre numeric,
    cod_nivel text,
    fec_creacion date,
    ide_usuario_creo numeric,
    ide_usuario_cambio numeric,
    fec_cambio date,
    niv_lectura numeric,
    niv_escritura numeric,
    ide_uuid text,
    val_sistema text,
    abrev_org text
);
 1   DROP TABLE public.tvs_config_org_administrativo;
       public         postgres    false    3            Q           0    0 3   COLUMN tvs_config_org_administrativo.ide_orga_admin    COMMENT     o   COMMENT ON COLUMN public.tvs_config_org_administrativo.ide_orga_admin IS 'Identificador Consecutivo Registro';
            public       postgres    false    215            R           0    0 ,   COLUMN tvs_config_org_administrativo.cod_org    COMMENT     h   COMMENT ON COLUMN public.tvs_config_org_administrativo.cod_org IS 'Codigo de la unidad administrativa';
            public       postgres    false    215            S           0    0 ,   COLUMN tvs_config_org_administrativo.nom_org    COMMENT     h   COMMENT ON COLUMN public.tvs_config_org_administrativo.nom_org IS 'Nombre de la unidad administrativa';
            public       postgres    false    215            T           0    0 -   COLUMN tvs_config_org_administrativo.desc_org    COMMENT     n   COMMENT ON COLUMN public.tvs_config_org_administrativo.desc_org IS 'Descripcion de la unidad administrativa';
            public       postgres    false    215            U           0    0 2   COLUMN tvs_config_org_administrativo.ind_es_activo    COMMENT     n   COMMENT ON COLUMN public.tvs_config_org_administrativo.ind_es_activo IS 'Estado de la unidad administrativa';
            public       postgres    false    215            �            1259    18700    tvs_organigrama_administrativo    TABLE     V  CREATE TABLE public.tvs_organigrama_administrativo (
    ide_orga_admin numeric NOT NULL,
    cod_org text NOT NULL,
    nom_org text NOT NULL,
    desc_org text NOT NULL,
    ind_es_activo text NOT NULL,
    ide_direccion numeric,
    ide_plan_responsable numeric,
    ide_orga_admin_padre numeric,
    cod_nivel text,
    fec_creacion timestamp(4) without time zone,
    ide_usuario_creo numeric,
    ide_usuario_cambio numeric NOT NULL,
    fec_cambio date,
    niv_lectura numeric,
    niv_escritura numeric,
    ide_uuid text,
    val_sistema text,
    val_version text,
    abrev_org text
);
 2   DROP TABLE public.tvs_organigrama_administrativo;
       public         postgres    false    3            V           0    0 4   COLUMN tvs_organigrama_administrativo.ide_orga_admin    COMMENT     p   COMMENT ON COLUMN public.tvs_organigrama_administrativo.ide_orga_admin IS 'Identificador Consecutivo Registro';
            public       postgres    false    216            W           0    0 -   COLUMN tvs_organigrama_administrativo.cod_org    COMMENT     i   COMMENT ON COLUMN public.tvs_organigrama_administrativo.cod_org IS 'Codigo de la unidad administrativa';
            public       postgres    false    216            X           0    0 -   COLUMN tvs_organigrama_administrativo.nom_org    COMMENT     i   COMMENT ON COLUMN public.tvs_organigrama_administrativo.nom_org IS 'Nombre de la unidad administrativa';
            public       postgres    false    216            Y           0    0 .   COLUMN tvs_organigrama_administrativo.desc_org    COMMENT     o   COMMENT ON COLUMN public.tvs_organigrama_administrativo.desc_org IS 'Descripcion de la unidad administrativa';
            public       postgres    false    216            Z           0    0 3   COLUMN tvs_organigrama_administrativo.ind_es_activo    COMMENT     o   COMMENT ON COLUMN public.tvs_organigrama_administrativo.ind_es_activo IS 'Estado de la unidad administrativa';
            public       postgres    false    216            �          0    18579    adm_ccd 
   TABLE DATA                 COPY public.adm_ccd (ide_ccd, fec_cambio, fec_creacion, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, ide_ofc_prod, ide_serie, ide_subserie, ide_uni_amt, est_ccd, val_version, val_version_org, num_version_org, nombre_comite, num_acta, fecha_acta) FROM stdin;
    public       postgres    false    196   m;      �          0    18585    adm_config_ccd 
   TABLE DATA               �   COPY public.adm_config_ccd (ide_ccd, fec_cambio, fec_creacion, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, ide_ofc_prod, ide_serie, ide_subserie, ide_uni_amt, est_ccd) FROM stdin;
    public       postgres    false    197   �;      �          0    18591    adm_config_instrumento 
   TABLE DATA               R   COPY public.adm_config_instrumento (ide_instrumento, est_instrumento) FROM stdin;
    public       postgres    false    198   �;      �          0    18598    adm_dis_final 
   TABLE DATA               �   COPY public.adm_dis_final (ide_dis_final, nom_dis_final, des_dis_final, sta_dis_final, ide_usuario_cambio, fec_cambio, niv_lectura, niv_escritura, ide_uuid, fec_creacion) FROM stdin;
    public       postgres    false    199   �;      �          0    18604    adm_mot_creacion 
   TABLE DATA               �   COPY public.adm_mot_creacion (ide_mot_creacion, fec_cambio, fec_creacion, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, nom_mot_creacion, est_mot_creacion) FROM stdin;
    public       postgres    false    200   �<      �          0    18610    adm_rel_eq_destino 
   TABLE DATA               �   COPY public.adm_rel_eq_destino (ide_rel_destino, ide_rel_origen, ide_uni_amt, ide_ofc_prod, ide_serie, ide_subserie, fec_creacion) FROM stdin;
    public       postgres    false    201   Q=      �          0    18616    adm_rel_eq_origen 
   TABLE DATA               �   COPY public.adm_rel_eq_origen (ide_rel_origen, ide_uni_amt, ide_ofc_prod, ide_serie, ide_subserie, fec_creacion, num_version_org) FROM stdin;
    public       postgres    false    202   n=      �          0    18622    adm_ser_subser_tpg 
   TABLE DATA               �   COPY public.adm_ser_subser_tpg (ide_serie, ide_tpg_doc, fec_cambio, fec_creacion, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, ide_subserie, ide_rel_sst, orden) FROM stdin;
    public       postgres    false    203   �=      �          0    18628 	   adm_serie 
   TABLE DATA               ^  COPY public.adm_serie (ide_serie, act_administrativo, car_administrativa, car_legal, car_tecnica, cod_serie, est_serie, fec_cambio, fec_creacion, fue_bibliografica, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, nom_serie, not_alcance, ide_mot_creacion, car_contable, car_juridica, con_publica, con_clasificada, con_reservada) FROM stdin;
    public       postgres    false    204   �=      �          0    18634    adm_soporte 
   TABLE DATA               �   COPY public.adm_soporte (ide_soporte, des_soporte, fec_cambio, fec_creacion, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, nom_soporte) FROM stdin;
    public       postgres    false    205   �=      �          0    18640    adm_subserie 
   TABLE DATA               x  COPY public.adm_subserie (ide_subserie, act_administrativo, car_administrativa, car_legal, car_tecnica, cod_subserie, est_subserie, fec_cambio, fec_creacion, fue_bibliografica, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, nom_subserie, not_alcance, ide_mot_creacion, ide_serie, car_juridica, car_contable, con_publica, con_clasificada, con_reservada) FROM stdin;
    public       postgres    false    206   <>      �          0    18646    adm_tab_ret_doc 
   TABLE DATA               �   COPY public.adm_tab_ret_doc (ide_tab_ret_doc, ac_trd, ag_trd, est_tab_ret_doc, fec_cambio, fec_creacion, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, pro_trd, ide_dis_final, ide_serie, ide_subserie, ide_ofc_prod, ide_uni_amt) FROM stdin;
    public       postgres    false    207   Y>      �          0    18652    adm_tab_ret_doc_org 
   TABLE DATA                 COPY public.adm_tab_ret_doc_org (ide_ofc_prod, ide_uni_amt, ide_tab_ret_doc_org, ide_tab_ret_doc, ac_trd, ag_trd, pro_trd, ide_dis_final, fec_cambio, fec_creacion, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, num_version, nombre_serie, nombre_subserie) FROM stdin;
    public       postgres    false    208   v>      �          0    18658    adm_tpg_doc 
   TABLE DATA               6  COPY public.adm_tpg_doc (ide_tpg_doc, car_administrativa, car_legal, car_tecnica, cod_tpg_doc, est_tpg_doc, fec_cambio, fec_creacion, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, nom_tpg_doc, ide_soporte, ide_tra_documental, not_alcance, fue_bibliografica, car_juridico, car_contable) FROM stdin;
    public       postgres    false    209   �>      �          0    18664    adm_trad_doc 
   TABLE DATA               �   COPY public.adm_trad_doc (ide_trad_doc, des_trad_doc, fec_cambio, fec_creacion, ide_usuario_cambio, ide_uuid, niv_escritura, niv_lectura, nom_trad_doc) FROM stdin;
    public       postgres    false    210   �>      �          0    18670    adm_version_trd 
   TABLE DATA               �   COPY public.adm_version_trd (ide_version, val_version, fec_version, ide_usuario, ide_ofc_prod, ide_uni_amt, num_version, nombre_comite, num_acta, fecha_acta) FROM stdin;
    public       postgres    false    211   ?      �          0    18676    cm_carga_masiva 
   TABLE DATA               �   COPY public.cm_carga_masiva (id, nombre, fecha_creacion, total_registros, estado, total_registros_exitosos, total_registros_error) FROM stdin;
    public       postgres    false    212   $?      �          0    18682    cm_registro_carga_masiva 
   TABLE DATA               d   COPY public.cm_registro_carga_masiva (id, carga_masiva_id, contenido, estado, mensajes) FROM stdin;
    public       postgres    false    213   A?      �          0    18688    table_generator 
   TABLE DATA               >   COPY public.table_generator (seq_name, seq_value) FROM stdin;
    public       postgres    false    214   ^?      �          0    18694    tvs_config_org_administrativo 
   TABLE DATA               6  COPY public.tvs_config_org_administrativo (ide_orga_admin, cod_org, nom_org, desc_org, ind_es_activo, ide_direccion, ide_plan_responsable, ide_orga_admin_padre, cod_nivel, fec_creacion, ide_usuario_creo, ide_usuario_cambio, fec_cambio, niv_lectura, niv_escritura, ide_uuid, val_sistema, abrev_org) FROM stdin;
    public       postgres    false    215   @      �          0    18700    tvs_organigrama_administrativo 
   TABLE DATA               D  COPY public.tvs_organigrama_administrativo (ide_orga_admin, cod_org, nom_org, desc_org, ind_es_activo, ide_direccion, ide_plan_responsable, ide_orga_admin_padre, cod_nivel, fec_creacion, ide_usuario_creo, ide_usuario_cambio, fec_cambio, niv_lectura, niv_escritura, ide_uuid, val_sistema, val_version, abrev_org) FROM stdin;
    public       postgres    false    216   -@      �
           2606    18707 0   tvs_config_org_administrativo ORAD_CONFIG_COD_UK 
   CONSTRAINT     p   ALTER TABLE ONLY public.tvs_config_org_administrativo
    ADD CONSTRAINT "ORAD_CONFIG_COD_UK" UNIQUE (cod_org);
 \   ALTER TABLE ONLY public.tvs_config_org_administrativo DROP CONSTRAINT "ORAD_CONFIG_COD_UK";
       public         postgres    false    215            �
           2606    18709 ;   tvs_config_org_administrativo TVS_CONFIG_ORG_ADMINISTRA_UK1 
   CONSTRAINT     {   ALTER TABLE ONLY public.tvs_config_org_administrativo
    ADD CONSTRAINT "TVS_CONFIG_ORG_ADMINISTRA_UK1" UNIQUE (nom_org);
 g   ALTER TABLE ONLY public.tvs_config_org_administrativo DROP CONSTRAINT "TVS_CONFIG_ORG_ADMINISTRA_UK1";
       public         postgres    false    215            �
           2606    18711    adm_dis_final adm_dis_final_pk 
   CONSTRAINT     g   ALTER TABLE ONLY public.adm_dis_final
    ADD CONSTRAINT adm_dis_final_pk PRIMARY KEY (ide_dis_final);
 H   ALTER TABLE ONLY public.adm_dis_final DROP CONSTRAINT adm_dis_final_pk;
       public         postgres    false    199            �
           2606    18713 (   adm_rel_eq_destino adm_rel_eq_destino_pk 
   CONSTRAINT     s   ALTER TABLE ONLY public.adm_rel_eq_destino
    ADD CONSTRAINT adm_rel_eq_destino_pk PRIMARY KEY (ide_rel_destino);
 R   ALTER TABLE ONLY public.adm_rel_eq_destino DROP CONSTRAINT adm_rel_eq_destino_pk;
       public         postgres    false    201            �
           2606    18715 &   adm_rel_eq_origen adm_rel_eq_origen_pk 
   CONSTRAINT     p   ALTER TABLE ONLY public.adm_rel_eq_origen
    ADD CONSTRAINT adm_rel_eq_origen_pk PRIMARY KEY (ide_rel_origen);
 P   ALTER TABLE ONLY public.adm_rel_eq_origen DROP CONSTRAINT adm_rel_eq_origen_pk;
       public         postgres    false    202            �
           2606    18717    adm_serie adm_serie_pk 
   CONSTRAINT     [   ALTER TABLE ONLY public.adm_serie
    ADD CONSTRAINT adm_serie_pk PRIMARY KEY (ide_serie);
 @   ALTER TABLE ONLY public.adm_serie DROP CONSTRAINT adm_serie_pk;
       public         postgres    false    204            �
           2606    18719 *   adm_tab_ret_doc_org adm_tab_ret_doc_org_pk 
   CONSTRAINT     y   ALTER TABLE ONLY public.adm_tab_ret_doc_org
    ADD CONSTRAINT adm_tab_ret_doc_org_pk PRIMARY KEY (ide_tab_ret_doc_org);
 T   ALTER TABLE ONLY public.adm_tab_ret_doc_org DROP CONSTRAINT adm_tab_ret_doc_org_pk;
       public         postgres    false    208            �
           2606    18721    adm_tpg_doc adm_tpg_doc_uk1 
   CONSTRAINT     j   ALTER TABLE ONLY public.adm_tpg_doc
    ADD CONSTRAINT adm_tpg_doc_uk1 UNIQUE (nom_tpg_doc, ide_tpg_doc);
 E   ALTER TABLE ONLY public.adm_tpg_doc DROP CONSTRAINT adm_tpg_doc_uk1;
       public         postgres    false    209    209            �
           2606    18723 "   cm_carga_masiva cm_carga_masiva_pk 
   CONSTRAINT     `   ALTER TABLE ONLY public.cm_carga_masiva
    ADD CONSTRAINT cm_carga_masiva_pk PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.cm_carga_masiva DROP CONSTRAINT cm_carga_masiva_pk;
       public         postgres    false    212            �
           2606    18725 4   cm_registro_carga_masiva cm_registro_carga_masiva_pk 
   CONSTRAINT     r   ALTER TABLE ONLY public.cm_registro_carga_masiva
    ADD CONSTRAINT cm_registro_carga_masiva_pk PRIMARY KEY (id);
 ^   ALTER TABLE ONLY public.cm_registro_carga_masiva DROP CONSTRAINT cm_registro_carga_masiva_pk;
       public         postgres    false    213            �
           2606    18727    adm_config_ccd config_ccds_pk 
   CONSTRAINT     `   ALTER TABLE ONLY public.adm_config_ccd
    ADD CONSTRAINT config_ccds_pk PRIMARY KEY (ide_ccd);
 G   ALTER TABLE ONLY public.adm_config_ccd DROP CONSTRAINT config_ccds_pk;
       public         postgres    false    197            �
           2606    18729    adm_config_instrumento ines_pk 
   CONSTRAINT     i   ALTER TABLE ONLY public.adm_config_instrumento
    ADD CONSTRAINT ines_pk PRIMARY KEY (ide_instrumento);
 H   ALTER TABLE ONLY public.adm_config_instrumento DROP CONSTRAINT ines_pk;
       public         postgres    false    198            �
           2606    18731    adm_mot_creacion mocr_pk 
   CONSTRAINT     d   ALTER TABLE ONLY public.adm_mot_creacion
    ADD CONSTRAINT mocr_pk PRIMARY KEY (ide_mot_creacion);
 B   ALTER TABLE ONLY public.adm_mot_creacion DROP CONSTRAINT mocr_pk;
       public         postgres    false    200            �
           2606    18733    adm_serie seri_uk 
   CONSTRAINT     Q   ALTER TABLE ONLY public.adm_serie
    ADD CONSTRAINT seri_uk UNIQUE (cod_serie);
 ;   ALTER TABLE ONLY public.adm_serie DROP CONSTRAINT seri_uk;
       public         postgres    false    204            �
           2606    18735    adm_soporte sopo_pk 
   CONSTRAINT     Z   ALTER TABLE ONLY public.adm_soporte
    ADD CONSTRAINT sopo_pk PRIMARY KEY (ide_soporte);
 =   ALTER TABLE ONLY public.adm_soporte DROP CONSTRAINT sopo_pk;
       public         postgres    false    205            �
           2606    18737    adm_ser_subser_tpg sstp_pk 
   CONSTRAINT     a   ALTER TABLE ONLY public.adm_ser_subser_tpg
    ADD CONSTRAINT sstp_pk PRIMARY KEY (ide_rel_sst);
 D   ALTER TABLE ONLY public.adm_ser_subser_tpg DROP CONSTRAINT sstp_pk;
       public         postgres    false    203            �
           2606    18739    adm_subserie subs_pk 
   CONSTRAINT     \   ALTER TABLE ONLY public.adm_subserie
    ADD CONSTRAINT subs_pk PRIMARY KEY (ide_subserie);
 >   ALTER TABLE ONLY public.adm_subserie DROP CONSTRAINT subs_pk;
       public         postgres    false    206            �
           2606    18741 "   table_generator table_generator_pk 
   CONSTRAINT     f   ALTER TABLE ONLY public.table_generator
    ADD CONSTRAINT table_generator_pk PRIMARY KEY (seq_name);
 L   ALTER TABLE ONLY public.table_generator DROP CONSTRAINT table_generator_pk;
       public         postgres    false    214            �
           2606    18743    adm_tpg_doc tpdo_pk 
   CONSTRAINT     Z   ALTER TABLE ONLY public.adm_tpg_doc
    ADD CONSTRAINT tpdo_pk PRIMARY KEY (ide_tpg_doc);
 =   ALTER TABLE ONLY public.adm_tpg_doc DROP CONSTRAINT tpdo_pk;
       public         postgres    false    209            �
           2606    18745    adm_tab_ret_doc trdo_pk 
   CONSTRAINT     b   ALTER TABLE ONLY public.adm_tab_ret_doc
    ADD CONSTRAINT trdo_pk PRIMARY KEY (ide_tab_ret_doc);
 A   ALTER TABLE ONLY public.adm_tab_ret_doc DROP CONSTRAINT trdo_pk;
       public         postgres    false    207            �
           2606    18747    adm_trad_doc trdo_pk1 
   CONSTRAINT     ]   ALTER TABLE ONLY public.adm_trad_doc
    ADD CONSTRAINT trdo_pk1 PRIMARY KEY (ide_trad_doc);
 ?   ALTER TABLE ONLY public.adm_trad_doc DROP CONSTRAINT trdo_pk1;
       public         postgres    false    210            �
           2606    18749 5   tvs_config_org_administrativo tvs_config_orga_admi_pk 
   CONSTRAINT        ALTER TABLE ONLY public.tvs_config_org_administrativo
    ADD CONSTRAINT tvs_config_orga_admi_pk PRIMARY KEY (ide_orga_admin);
 _   ALTER TABLE ONLY public.tvs_config_org_administrativo DROP CONSTRAINT tvs_config_orga_admi_pk;
       public         postgres    false    215                       2606    18751 B   tvs_organigrama_administrativo tvs_organigrama_administrativo_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.tvs_organigrama_administrativo
    ADD CONSTRAINT tvs_organigrama_administrativo_pkey PRIMARY KEY (ide_orga_admin);
 l   ALTER TABLE ONLY public.tvs_organigrama_administrativo DROP CONSTRAINT tvs_organigrama_administrativo_pkey;
       public         postgres    false    216            �
           2606    18753    adm_version_trd vetrd_pk 
   CONSTRAINT     _   ALTER TABLE ONLY public.adm_version_trd
    ADD CONSTRAINT vetrd_pk PRIMARY KEY (ide_version);
 B   ALTER TABLE ONLY public.adm_version_trd DROP CONSTRAINT vetrd_pk;
       public         postgres    false    211                       2606    18754 )   adm_rel_eq_destino adm_rel_eq_destino_fk1    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_rel_eq_destino
    ADD CONSTRAINT adm_rel_eq_destino_fk1 FOREIGN KEY (ide_serie) REFERENCES public.adm_serie(ide_serie);
 S   ALTER TABLE ONLY public.adm_rel_eq_destino DROP CONSTRAINT adm_rel_eq_destino_fk1;
       public       postgres    false    204    201    2785                       2606    18759 )   adm_rel_eq_destino adm_rel_eq_destino_fk2    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_rel_eq_destino
    ADD CONSTRAINT adm_rel_eq_destino_fk2 FOREIGN KEY (ide_subserie) REFERENCES public.adm_subserie(ide_subserie);
 S   ALTER TABLE ONLY public.adm_rel_eq_destino DROP CONSTRAINT adm_rel_eq_destino_fk2;
       public       postgres    false    201    206    2791                       2606    18764 '   adm_rel_eq_origen adm_rel_eq_origen_fk1    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_rel_eq_origen
    ADD CONSTRAINT adm_rel_eq_origen_fk1 FOREIGN KEY (ide_serie) REFERENCES public.adm_serie(ide_serie);
 Q   ALTER TABLE ONLY public.adm_rel_eq_origen DROP CONSTRAINT adm_rel_eq_origen_fk1;
       public       postgres    false    202    204    2785                       2606    18769 '   adm_rel_eq_origen adm_rel_eq_origen_fk2    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_rel_eq_origen
    ADD CONSTRAINT adm_rel_eq_origen_fk2 FOREIGN KEY (ide_subserie) REFERENCES public.adm_subserie(ide_subserie);
 Q   ALTER TABLE ONLY public.adm_rel_eq_origen DROP CONSTRAINT adm_rel_eq_origen_fk2;
       public       postgres    false    202    206    2791                       2606    18774 +   adm_tab_ret_doc_org adm_tab_ret_doc_org_fk1    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_tab_ret_doc_org
    ADD CONSTRAINT adm_tab_ret_doc_org_fk1 FOREIGN KEY (ide_tab_ret_doc) REFERENCES public.adm_tab_ret_doc(ide_tab_ret_doc);
 U   ALTER TABLE ONLY public.adm_tab_ret_doc_org DROP CONSTRAINT adm_tab_ret_doc_org_fk1;
       public       postgres    false    208    207    2793                       2606    18779    adm_tpg_doc adm_tpg_doc_fk1    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_tpg_doc
    ADD CONSTRAINT adm_tpg_doc_fk1 FOREIGN KEY (ide_tra_documental) REFERENCES public.adm_trad_doc(ide_trad_doc);
 E   ALTER TABLE ONLY public.adm_tpg_doc DROP CONSTRAINT adm_tpg_doc_fk1;
       public       postgres    false    209    210    2801                       2606    18784    adm_ccd ccds_seri_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_ccd
    ADD CONSTRAINT ccds_seri_fk FOREIGN KEY (ide_serie) REFERENCES public.adm_serie(ide_serie);
 >   ALTER TABLE ONLY public.adm_ccd DROP CONSTRAINT ccds_seri_fk;
       public       postgres    false    196    2785    204                       2606    18789    adm_ccd ccds_subs_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_ccd
    ADD CONSTRAINT ccds_subs_fk FOREIGN KEY (ide_subserie) REFERENCES public.adm_subserie(ide_subserie);
 >   ALTER TABLE ONLY public.adm_ccd DROP CONSTRAINT ccds_subs_fk;
       public       postgres    false    2791    196    206                       2606    18794 "   adm_config_ccd config_ccds_seri_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_config_ccd
    ADD CONSTRAINT config_ccds_seri_fk FOREIGN KEY (ide_serie) REFERENCES public.adm_serie(ide_serie);
 L   ALTER TABLE ONLY public.adm_config_ccd DROP CONSTRAINT config_ccds_seri_fk;
       public       postgres    false    2785    204    197            	           2606    18799    adm_ser_subser_tpg sstp_seri_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_ser_subser_tpg
    ADD CONSTRAINT sstp_seri_fk FOREIGN KEY (ide_serie) REFERENCES public.adm_serie(ide_serie);
 I   ALTER TABLE ONLY public.adm_ser_subser_tpg DROP CONSTRAINT sstp_seri_fk;
       public       postgres    false    2785    204    203            
           2606    18804    adm_ser_subser_tpg sstp_subs_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_ser_subser_tpg
    ADD CONSTRAINT sstp_subs_fk FOREIGN KEY (ide_subserie) REFERENCES public.adm_subserie(ide_subserie);
 I   ALTER TABLE ONLY public.adm_ser_subser_tpg DROP CONSTRAINT sstp_subs_fk;
       public       postgres    false    2791    203    206                       2606    18809    adm_ser_subser_tpg sstp_tpdo_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_ser_subser_tpg
    ADD CONSTRAINT sstp_tpdo_fk FOREIGN KEY (ide_tpg_doc) REFERENCES public.adm_tpg_doc(ide_tpg_doc);
 I   ALTER TABLE ONLY public.adm_ser_subser_tpg DROP CONSTRAINT sstp_tpdo_fk;
       public       postgres    false    209    203    2799                       2606    18814    adm_subserie subs_mocr_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_subserie
    ADD CONSTRAINT subs_mocr_fk FOREIGN KEY (ide_mot_creacion) REFERENCES public.adm_mot_creacion(ide_mot_creacion);
 C   ALTER TABLE ONLY public.adm_subserie DROP CONSTRAINT subs_mocr_fk;
       public       postgres    false    206    200    2777                       2606    18819    adm_subserie subs_seri_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_subserie
    ADD CONSTRAINT subs_seri_fk FOREIGN KEY (ide_serie) REFERENCES public.adm_serie(ide_serie);
 C   ALTER TABLE ONLY public.adm_subserie DROP CONSTRAINT subs_seri_fk;
       public       postgres    false    206    204    2785                       2606    18824 %   cm_registro_carga_masiva sys_c0011047    FK CONSTRAINT     �   ALTER TABLE ONLY public.cm_registro_carga_masiva
    ADD CONSTRAINT sys_c0011047 FOREIGN KEY (carga_masiva_id) REFERENCES public.cm_carga_masiva(id);
 O   ALTER TABLE ONLY public.cm_registro_carga_masiva DROP CONSTRAINT sys_c0011047;
       public       postgres    false    213    212    2805                       2606    18829    adm_tpg_doc tpdo_sopo_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_tpg_doc
    ADD CONSTRAINT tpdo_sopo_fk FOREIGN KEY (ide_soporte) REFERENCES public.adm_soporte(ide_soporte);
 B   ALTER TABLE ONLY public.adm_tpg_doc DROP CONSTRAINT tpdo_sopo_fk;
       public       postgres    false    209    2789    205                       2606    18834    adm_tab_ret_doc trdo_seri_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_tab_ret_doc
    ADD CONSTRAINT trdo_seri_fk FOREIGN KEY (ide_serie) REFERENCES public.adm_serie(ide_serie);
 F   ALTER TABLE ONLY public.adm_tab_ret_doc DROP CONSTRAINT trdo_seri_fk;
       public       postgres    false    2785    207    204                       2606    18839    adm_tab_ret_doc trdo_subs_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.adm_tab_ret_doc
    ADD CONSTRAINT trdo_subs_fk FOREIGN KEY (ide_subserie) REFERENCES public.adm_subserie(ide_subserie);
 F   ALTER TABLE ONLY public.adm_tab_ret_doc DROP CONSTRAINT trdo_subs_fk;
       public       postgres    false    2791    206    207                       2606    18844 ;   tvs_config_org_administrativo tvs_config_org_administra_fk1    FK CONSTRAINT     �   ALTER TABLE ONLY public.tvs_config_org_administrativo
    ADD CONSTRAINT tvs_config_org_administra_fk1 FOREIGN KEY (ide_orga_admin_padre) REFERENCES public.tvs_config_org_administrativo(ide_orga_admin);
 e   ALTER TABLE ONLY public.tvs_config_org_administrativo DROP CONSTRAINT tvs_config_org_administra_fk1;
       public       postgres    false    215    215    2815                       2606    18849 <   tvs_organigrama_administrativo tvs_organigrama_administr_fk1    FK CONSTRAINT     �   ALTER TABLE ONLY public.tvs_organigrama_administrativo
    ADD CONSTRAINT tvs_organigrama_administr_fk1 FOREIGN KEY (ide_orga_admin_padre) REFERENCES public.tvs_organigrama_administrativo(ide_orga_admin);
 f   ALTER TABLE ONLY public.tvs_organigrama_administrativo DROP CONSTRAINT tvs_organigrama_administr_fk1;
       public       postgres    false    2817    216    216            �      x������ � �      �      x������ � �      �      x�svv�4��rw��tr�u�b���� I�%      �   �   x���1
�@����)��ag�먥a��6F6a`]AM�\+G��"�^��3
9r�-����ǃP]V�RR8M����#
��ݝ]�/�P���6��F��<֎���+����E0vS?wJR���O�����V
r%㕌ah��)�h�Ѧ�B��e^      �   �   x�u�A
�0D��)�򓦶�
.�t��li�}<�3*V�}of����5��`c��)��W�4�>�p�Ns�>7Ȫ��Ơ0Jr���݋��L�'���9�����%!�4��1K�gA���h�U�v���	p��$�t�߂��e�������!ͮ'�B�z�(#�m����t�1� �C^�      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �   g   x�3��/�/*IUp;��839�3Ə����B��R��\��������P������$GP\Fp3\sR�K�o�#� dm\�p�<�M*�L!�I
�(N����� �'C�      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �   G   x�3��/�L��K����420��5��52W04�25�24�34�01���L��s~Af"	Z��b���� -#�      �      x������ � �      �      x������ � �      �      x������ � �      �   �   x�u�Q� @��a��T�!����k	���lӮ6ݾx ��g�Mq�=, ,vP�̃D�J�ڊ��ي�����9��[K�(��	F�"�˸ۼ�mW�^w�,;m��
OQ�B����#e�:$8�m̕�*
�96}�����m��e-�Z�w��wqA      �      x������ � �      �      x������ � �     