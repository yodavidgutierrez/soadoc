------------------------------------------------------------------------------------------------------------------------------
--table_generator
------------------------------------------------------------------------------------------------------------------------------

Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('TVS_CONFIG_ORGANIGRAMA_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_CCD_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_CONFIG_CCD_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_DISFINAL_SEQ','6');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_MOTIVODOC_SEQ','5');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_SERIE_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_SER_SUBSER_TPG_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_SUBSERIE_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_TAB_RET_DOC_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_TPGDOC_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('CM_CARGA_MASIVA_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('CM_REGISTRO_CARGA_MASIVA_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('TVS_ORGANIGRAMA_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('VERSION_CCD_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('VERSION_ORGANIGRAMA_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_TAB_RET_DOC_ORG_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_VERSION_TRD_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_REL_EQ_OR_SEQ','0');
Insert into TABLE_GENERATOR (SEQ_NAME,SEQ_VALUE) values ('ADM_REL_EQ_DES_SEQ','0');

------------------------------------------------------------------------------------------------------------------------------
--adm_soporte
------------------------------------------------------------------------------------------------------------------------------

Insert into ADM_SOPORTE (IDE_SOPORTE,DES_SOPORTE,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_SOPORTE) 
values ('1','Soporte Físico',null,current_timestamp,null,null,null,null,'Físico');
Insert into ADM_SOPORTE (IDE_SOPORTE,DES_SOPORTE,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_SOPORTE) 
values ('2','Soporte Electrónico',null,current_timestamp,null,null,null,null,'Electrónico');
Insert into ADM_SOPORTE (IDE_SOPORTE,DES_SOPORTE,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_SOPORTE) 
values ('3','Soporte Híbrido',null,current_timestamp,null,null,null,null,'Físico y Electrónico');

------------------------------------------------------------------------------------------------------------------------------
--adm_config_instrumento
------------------------------------------------------------------------------------------------------------------------------

Insert into ADM_CONFIG_INSTRUMENTO (IDE_INSTRUMENTO,EST_INSTRUMENTO) values ('ORGANIGRAMA','2');
Insert into ADM_CONFIG_INSTRUMENTO (IDE_INSTRUMENTO,EST_INSTRUMENTO) values ('CCD','2');

------------------------------------------------------------------------------------------------------------------------------
--adm_mot_creacion
------------------------------------------------------------------------------------------------------------------------------

Insert into ADM_MOT_CREACION (IDE_MOT_CREACION,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_MOT_CREACION) 
values ('1',null,current_timestamp,null,null,null,null,'Cambio en las funciones');
Insert into ADM_MOT_CREACION (IDE_MOT_CREACION,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_MOT_CREACION) 
values ('2',null,current_timestamp,null,null,null,null,'Cambio en la estructura orgánica');
Insert into ADM_MOT_CREACION (IDE_MOT_CREACION,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_MOT_CREACION) 
values ('3',null,current_timestamp,null,null,null,null,'Cambio en marco normativo');
Insert into ADM_MOT_CREACION (IDE_MOT_CREACION,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_MOT_CREACION) 
values ('4',null,current_timestamp,null,null,null,null,'Funciones actuales');

------------------------------------------------------------------------------------------------------------------------------
--adm_dis_final
------------------------------------------------------------------------------------------------------------------------------

Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('1','CT','Conservación total','1',null,null,null,null,null,to_date('25/01/17','DD/MM/RR'));
Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('2','E','Eliminación','1',null,null,null,null,null,to_date('25/01/17','DD/MM/RR'));
Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('3','S','Selección','1',null,null,null,null,null,to_date('25/01/17','DD/MM/RR'));
Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('4','M','Microfilmación','1',null,null,null,null,null,to_date('25/01/17','DD/MM/RR'));
Insert into ADM_DIS_FINAL (IDE_DIS_FINAL,NOM_DIS_FINAL,DES_DIS_FINAL,STA_DIS_FINAL,IDE_USUARIO_CAMBIO,FEC_CAMBIO,NIV_LECTURA,NIV_ESCRITURA,IDE_UUID,FEC_CREACION) values ('5','D','Digitalización','1',null,null,null,null,null,to_date('25/01/17','DD/MM/RR'));

------------------------------------------------------------------------------------------------------------------------------
--adm_trad_doc
------------------------------------------------------------------------------------------------------------------------------

Insert into ADM_TRAD_DOC (IDE_TRAD_DOC,DES_TRAD_DOC,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_TRAD_DOC) values ('1','Original',null,current_timestamp,null,null,null,null,'Original');
Insert into ADM_TRAD_DOC (IDE_TRAD_DOC,DES_TRAD_DOC,FEC_CAMBIO,FEC_CREACION,IDE_USUARIO_CAMBIO,IDE_UUID,NIV_ESCRITURA,NIV_LECTURA,NOM_TRAD_DOC) values ('2','Copia',null,current_timestamp,null,null,null,null,'Copia');
