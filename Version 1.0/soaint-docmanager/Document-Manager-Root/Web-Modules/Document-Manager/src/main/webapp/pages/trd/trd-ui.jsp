<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:composition>

    <jsp:attribute name="header">
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/trd-controller.js"></script>
		<script type="text/javascript" src="/Document-Manager/resources/js-controllers/comun-controller.js"></script>
    </jsp:attribute>

    <jsp:body>

        <p class="lead">Administraci&oacute;n <i class="fa fa-angle-double-right"></i>
            <small class="text-muted">Generar TRD por Dependencia Productora</small>
        </p>

        <hr/>
        <form id="formAddTRD" name="formAddTRD">
            <div class="form-horizontal">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">

                            <label for="codUniAmt" class="col-sm-4 control-label" style="padding-top: 0;">Dependencia
                                Jer&aacute;rquica</label>
                            <div id="cbxCodUniAmt" class=" col-sm-8">
                                <select id="codUniAmt" name="codUniAmt" class="form-control"
                                        onchange="recargarCombosOfcProduc(this)" data-required="true">
                                    <option value="0"> --- Seleccione ---</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label for="codOfcProd" class="col-sm-4 control-label" style="padding-top: 0;">Dependencia
                                Productora</label>
                            <div id="cbxOfcProduc" class=" col-sm-8">
                                <select id="codOfcProd" name="codOfcProd" class="form-control"
                                        onchange="definirTRD(this)" data-required="true">
                                    <option value="0"> --- Seleccione ---</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>

        <hr/>

        <div class="row">
            <div class="col-sm-12">
                <div class="pull-right">
                    <button id="publicarTRD" class="btn btn-default btn-sm require-confirmation hidden"
                            data-toggle="modal" data-target="#modalPublicar">
                        <i class="glyphicon glyphicon-ok-sign"></i> Publicar Versi&oacute;n
                    </button>
                </div>
            </div>
        </div>
        <hr id="data"/>

        <div class="panel-group hidden" id="filtroseccionsubseccion">
            <div class="row">
                <div class="form-horizontal col-lg-12 col-md-12 col-sm-12">
                    <div>
                        <label class="control-label col-md-3"
                               for="codUniAmt">DEPENDENCIA JER&Aacute;RQUICA:</label>&nbsp;

                        <div class="col-md-8">
                            <label class="control-label">
                                <small id="seccionDetgroup" class="text-muted"></small>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-horizontal col-lg-12 col-md-12 col-sm-12">
                    <div>
                        <label class="control-label col-md-3 text-left"
                               for="codOfcProd">DEPENDENCIA PRODUCTORA:</label>

                        <div class="col-md-8">
                            <label class="control-label">
                                <small id="subseccionDetgroup" class="text-muted"></small>
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <table id="trdAdicionarTable" class="text-left edt hover table table table-bordered table-striped" style="table-layout: fixed; width: 100%;">
                <thead>
                <tr>
                    <th rowspan="2" style="width: 7% !important;">C&Oacute;DIGO</th>
                    <th rowspan="2" style="width: 29%">SERIE, SUBSERIE Y TIPO DOCUMENTAL</th>
                    <th rowspan="2" style="width: 9%">SOPORTE</th>
                    <th rowspan="2" class="cabecera_overflow" style="width: 5%">CONFID</th>
                    <th colspan="2" class="cabecera_overflow" style="text-align: center; width: 15%;">RETENCI&Oacute;N</th>
                    <th colspan="5" class="cabecera_overflow" style="text-align: center; width: 20%;">DISPOSICI&Oacute;N FINAL</th>
                    <th rowspan="2" class="cabecera_overflow" style="width: 15%;text-align: center">PROCEDIMIENTOS</th>
                </tr>
                <tr>
                    <th class="cabecera_overflow">ARCHIVO GESTIÓN</th>
                    <th class="cabecera_overflow">ARCHIVO CENTRAL</th>
                    <th class="cabecera_overflow">CT</th>
                    <th class="cabecera_overflow">E</th>
                    <th class="cabecera_overflow">M</th>
                    <th class="cabecera_overflow">S</th>
                    <th class="cabecera_overflow">D</th>
                </thead>
            </table>

            <div class="row">
                <label class="control-label col-md-12"
                       for="codOfcProd">CONFIDENCIALIDAD:
                    <b>P:</b><span> Pública, </span><b>C:</b><span> Clasificada, </span><b>R:</b><span> Reservada</span></label>
            </div>
        </div>

        <jsp:include page="trd-panel.jsp"/>
    </jsp:body>
</ui:composition>
