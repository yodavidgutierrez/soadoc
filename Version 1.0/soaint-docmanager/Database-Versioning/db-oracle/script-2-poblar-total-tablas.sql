Insert into ADM_TRAD_DOC (IDE_TRAD_DOC,DES_TRAD_DOC,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_TRAD_DOC) values ('1','Original',null,to_timestamp('25/01/16 12:00:00,000000000 AM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,'Original');
Insert into ADM_TRAD_DOC (IDE_TRAD_DOC,DES_TRAD_DOC,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_TRAD_DOC) values ('2','Copia',null,to_timestamp('25/01/16 12:00:00,000000000 AM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,'Copia');

Insert into ADM_MOT_CREACION (IDE_MOT_CREACION,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_MOT_CREACION) values ('1',to_timestamp('26/11/14 02:40:55,657000000 PM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,null,'Nuevas Funciones');
Insert into ADM_MOT_CREACION (IDE_MOT_CREACION,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_MOT_CREACION) values ('2',to_timestamp('26/11/14 02:41:20,991000000 PM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,null,'Reestructuracion');
Insert into ADM_MOT_CREACION (IDE_MOT_CREACION,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_MOT_CREACION) values ('3',to_timestamp('26/11/14 02:41:37,699000000 PM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,null,'Fusion de Dependencias');
Insert into ADM_MOT_CREACION (IDE_MOT_CREACION,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_MOT_CREACION) values ('4',to_timestamp('30/06/15 12:04:02,436000000 PM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,null,'Cambio de Funciones entre Dependencias');
Insert into ADM_MOT_CREACION (IDE_MOT_CREACION,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_MOT_CREACION) values ('5',to_timestamp('26/11/14 02:42:11,972000000 PM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,null,'Normatividad');

Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('1','MICROFILMACIÓN',null,null,null,null,null,null,null,to_date('28/11/16','DD/MM/RR'));
Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('2','CONSERVACIÓN TOTAL',null,null,null,null,null,null,null,to_date('28/11/16','DD/MM/RR'));
Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('3','SELECCIÓN',null,null,null,null,null,null,null,to_date('28/11/16','DD/MM/RR'));
Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('4','ELIMINACIÓN',null,null,null,null,null,null,null,to_date('28/11/16','DD/MM/RR'));
Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('5','DIGITALIZACIÓN',null,null,null,null,null,null,null,to_date('28/11/16','DD/MM/RR'));


Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_CCD_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_CONFIG_CCD_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_DISFINAL_SEQ','6');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_MOTIVODOC_SEQ','6');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_SERIE_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_SER_SUBSER_TPG_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_SUBSERIE_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_TAB_RET_DOC_SEQ', '0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_TPGDOC_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('CM_CARGA_MASIVA_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('CM_REGISTRO_CARGA_MASIVA_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('TVS_CONFIG_ORGANIGRAMA_SEQ','56');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('TVS_ORGANIGRAMA_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('VERSION_CCD_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('VERSION_ORGANIGRAMA_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_TAB_RET_DOC_ORG_SEQ', '0');
INSERT INTO TABLE_GENERATOR (SEQ_NAME, SEQ_VALUE) values ('ADM_VERSION_TRD_SEQ', '0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_REL_EQ_OR_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_REL_EQ_DES_SEQ','0');

Insert into ADM_SOPORTE (IDE_SOPORTE,DES_SOPORTE,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_SOPORTE) values ('1','Soporte Físico',null,to_timestamp('25/01/16 12:00:00,000000000 AM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,'Físico');
Insert into ADM_SOPORTE (IDE_SOPORTE,DES_SOPORTE,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_SOPORTE) values ('2','Soporte Electrónico',null,to_timestamp('25/01/16 12:00:00,000000000 AM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,'Electrónico');
Insert into ADM_SOPORTE (IDE_SOPORTE,DES_SOPORTE,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_SOPORTE) values ('3','Soporte Híbrido',null,to_timestamp('25/01/16 12:00:00,000000000 AM','DD/MM/RR HH12:MI:SSXFF AM'),null,null,null,null,'Físico y Electrónico');



Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('1','10000000000','COLPENSIONES','COLPENSIONES','1',null,null,null,'0',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('2','1000000000','BOGOTÁ','BOGOTÁ','1',null,null,'1','1',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('3','1020100000','PRESIDENCIA','PRESIDENCIA','1',null,null,'2','2',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('4','1040000000','OFICINA NACIONAL DE CONTROL INTERNO','OFICINA NACIONAL DE CONTROL INTERNO','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('5','1050000000','OFICINA NACIONAL DE CONTROL DISCIPLINARIO INTERNO','OFICINA NACIONAL DE CONTROL DISCIPLINARIO INTERNO','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('6','1060000000','OFICINA NACIONAL DE INGENIERÍA PROCESOS','OFICINA NACIONAL DE INGENIERÍA PROCESOS','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('7','1140000000','VICEPRESIDENCIA ADMINISTRATIVA','VICEPRESIDENCIA ADMINISTRATIVA','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('8','1148100000','GERENCIA NACIONAL ECONÓMICA','GERENCIA NACIONAL ECONÓMICA','1',null,null,'7','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('9','1148200000','GERENCIA NACIONAL DE BIENES Y SERVICIOS','GERENCIA NACIONAL DE BIENES Y SERVICIOS','1',null,null,'7','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('10','1148300000','GERENCIA NACIONAL DE GESTIÓN CONTRACTUAL','GERENCIA NACIONAL DE GESTIÓN CONTRACTUAL','1',null,null,'7','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('11','1148400000','GERENCIA NACIONAL DE GESTIÓN DOCUMENTAL','GERENCIA NACIONAL DE GESTIÓN DOCUMENTAL','1',null,null,'7','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('12','1070000000','VICEPRESIDENCIA DE FINANCIAMIENTO E INVERSIONES','VICEPRESIDENCIA DE FINANCIAMIENTO E INVERSIONES','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('13','1070100000','GERENCIA NACIONAL DE INGRESOS Y EGRESOS','GERENCIA NACIONAL DE INGRESOS Y EGRESOS','1',null,null,'12','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('14','1070200000','GERENCIA NACIONAL DE APORTES Y RECAUDO','GERENCIA NACIONAL DE APORTES Y RECAUDO','1',null,null,'12','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('15','1070300000','GERENCIA NACIONAL DE COBRO','GERENCIA NACIONAL DE COBRO','1',null,null,'12','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('16','1070400000','GERENCIA NACIONAL DE TESORERÍA E INVERSIONES','GERENCIA NACIONAL DE TESORERÍA E INVERSIONES','1',null,null,'12','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('17','1080000000','VICEPRESIDENCIA DE SERVICIO CIUDADANO','VICEPRESIDENCIA DE SERVICIO CIUDADANO','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('18','1081100000','GERENCIA NACIONAL DE ATENCIÓN AL AFILIADO','GERENCIA NACIONAL DE ATENCIÓN AL AFILIADO','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('19','1081200000','GERENCIA NACIONAL DE RED COLPENSIONES Y CANALES ALTERNOS RPM','GERENCIA NACIONAL DE RED COLPENSIONES Y CANALES ALTERNOS RPM','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('20','1081300000','GERENCIA NACIONAL DE SERVICIO AL CIUDADANO Y SAC','GERENCIA NACIONAL DE SERVICIO AL CIUDADANO Y SAC','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('21','1081400000','GERENCIA REGIONAL BOGOTÁ','GERENCIA REGIONAL BOGOTÁ','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('22','1081500000','GERENCIA REGIONAL CENTRO','GERENCIA REGIONAL CENTRO','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('23','1081600000','GERENCIA REGIONAL ANTIOQUIA','GERENCIA REGIONAL ANTIOQUIA','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('24','1081700000','GERENCIA REGIONAL OCCIDENTE','GERENCIA REGIONAL OCCIDENTE','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('25','1081800000','GERENCIA REGIONAL CARIBE','GERENCIA REGIONAL CARIBE','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('26','1081900000','GERENCIA REGIONAL EJE CAFETERO','GERENCIA REGIONAL EJE CAFETERO','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('27','1082000000','GERENCIA REGIONAL SUR','GERENCIA REGIONAL SUR','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('28','1082100000','GERENCIA REGIONAL SANTANDER','GERENCIA REGIONAL SANTANDER','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('29','5080100000','GERENCIA NACIONAL DE PETICIONES, QUEJAS, RECLAMOS Y SUGERENCIAS','GERENCIA NACIONAL DE PETICIONES, QUEJAS, RECLAMOS Y SUGERENCIAS','1',null,null,'17','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('30','1090000000','VICEPRESIDENCIA COMERCIAL','VICEPRESIDENCIA COMERCIAL','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('31','1093100000','GERENCIA NACIONAL DE INVESTIGACIÓN Y DESARROLLO','GERENCIA NACIONAL DE INVESTIGACIÓN Y DESARROLLO','1',null,null,'30','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('32','1093300000','GERENCIA NACIONAL DE GESTIÓN COMERCIAL','GERENCIA NACIONAL DE GESTIÓN COMERCIAL','1',null,null,'30','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('33','1093200000','GERENCIA NACIONAL DE MERCADEO','GERENCIA NACIONAL DE MERCADEO','1',null,null,'30','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('34','1104000000','VICEPRESIDENCIA DE BENEFICIOS Y PRESTACIONES','VICEPRESIDENCIA DE BENEFICIOS Y PRESTACIONES','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('35','1104100000','GERENCIA NACIONAL DE RECONOCIMIENTO','GERENCIA NACIONAL DE RECONOCIMIENTO','1',null,null,'34','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('36','1104200000','GERENCIA NACIONAL DE NÓMINA','GERENCIA NACIONAL DE NÓMINA','1',null,null,'34','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('37','1115000000','VICEPRESIDENCIA DE OPERACIONES Y TECNOLOGÍA','VICEPRESIDENCIA DE OPERACIONES Y TECNOLOGÍA','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('38','1115100000','GERENCIA NACIONAL DE OPERACIONES RPM','GERENCIA NACIONAL DE OPERACIONES RPM','1',null,null,'37','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('39','1115200000','GERENCIA NACIONAL DE GESTIÓN DE SISTEMAS DE INFORMACIÓN','GERENCIA NACIONAL DE GESTIÓN DE SISTEMAS DE INFORMACIÓN','1',null,null,'37','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('40','1115300000','GERENCIA NACIONAL DE INFRAESTRUCTURA TECNOLÓGICA','GERENCIA NACIONAL DE INFRAESTRUCTURA TECNOLÓGICA','1',null,null,'37','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('41','1115400000','GERENCIA NACIONAL PROYECTO NUEVO SISTEMA','GERENCIA NACIONAL PROYECTO NUEVO SISTEMA','1',null,null,'37','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('42','5115100000','GERENCIA NACIONAL DE OPERACIONES BEPS','GERENCIA NACIONAL DE OPERACIONES BEPS','1',null,null,'37','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('43','1120000000','VICEPRESIDENCIA DE PLANEACIÓN Y RIESGOS','VICEPRESIDENCIA DE PLANEACIÓN Y RIESGOS','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('44','1126100000','GERENCIA NACIONAL DE ESTUDIOS DE SISTEMAS PENSIONALES Y BEPS','GERENCIA NACIONAL DE ESTUDIOS DE SISTEMAS PENSIONALES Y BEPS','1',null,null,'43','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('45','1126200000','GERENCIA NACIONAL DE GESTIÓN ACTUARIAL','GERENCIA NACIONAL DE GESTIÓN ACTUARIAL','1',null,null,'43','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('46','1126300000','GERENCIA NACIONAL DE GESTIÓN DE RIESGOS','GERENCIA NACIONAL DE GESTIÓN DE RIESGOS','1',null,null,'43','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('47','1130000000','VICEPRESIDENCIA DE TALENTO HUMANO','VICEPRESIDENCIA DE TALENTO HUMANO','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('48','1137200000','GERENCIA NACIONAL DE DESARROLLO TALENTO HUMANO','GERENCIA NACIONAL DE DESARROLLO TALENTO HUMANO','1',null,null,'47','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('49','1137100000','GERENCIA NACIONAL DE GESTIÓN DEL TALENTO HUMANO','GERENCIA NACIONAL DE GESTIÓN DEL TALENTO HUMANO','1',null,null,'47','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('50','1150000000','VICEPRESIDENCIA JURÍDICA Y SECRETARIA GENERAL','VICEPRESIDENCIA JURÍDICA Y SECRETARIA GENERAL','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('51','1159100000','GERENCIA NACIONAL DE DEFENSA JUDICIAL','GERENCIA NACIONAL DE DEFENSA JUDICIAL','1',null,null,'50','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('52','1159200000','GERENCIA NACIONAL DE DOCTRINA','GERENCIA NACIONAL DE DOCTRINA','1',null,null,'50','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('53','5160000000','VICEPRESIDENCIA DE BENEFICIOS ECONÓMICOS PERIÓDICOS','VICEPRESIDENCIA DE BENEFICIOS ECONÓMICOS PERIÓDICOS','1',null,null,'3','3',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('54','5160100000','GERENCIA NACIONAL DE ADMINISTRACIÓN DE INCENTIVOS','GERENCIA NACIONAL DE ADMINISTRACIÓN DE INCENTIVOS','1',null,null,'53','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
Insert into TVS_CONFIG_ORG_ADMINISTRATIVO (IDE_ORGA_ADMIN,COD_ORG,NOM_ORG,DESC_ORG,IND_ES_ACTIVO,IDE_DIRECCION,IDE_PLAN_RESPONSABLE,IDE_ORGA_ADMIN_PADRE,COD_NIVEL,FEC_CREACION,IDE_USUARIO_CREO,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,VAL_SISTEMA) values ('55','5160200000','GERENCIA NACIONAL DE GESTIÓN DE LA RED BEPS','GERENCIA NACIONAL DE GESTIÓN DE LA RED BEPS','1',null,null,'53','4',to_date('24/11/16','DD/MM/RR'),null,'0',null,'0','0',null,null);
