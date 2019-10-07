<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:composition>

    <jsp:attribute name="header">
        <script type="text/javascript"
                src="/Document-Manager/resources/js-controllers/versionTrd-controller.js"></script>
		<script type="text/javascript" src="/Document-Manager/resources/js-controllers/comun-controller.js"></script>
    </jsp:attribute>

    <jsp:body>

        <p class="lead">Visualización <i class="fa fa-angle-double-right"></i>
            <small class="text-muted">Tabla de Retenci&oacute;n Documental</small>
        </p>

        <hr/>

        <form id="formVersionTRD" action="/Document-Manager/versionTrd/generateVersionTrdPDF" target="_blank"
              method="get" name="formVersionTRD">
            <div class="form-horizontal">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">

                            <label for="codVersionOrg" class="col-sm-4 control-label" style="padding-top: 0;">Version Organigrama </label>

                            <div id="cbxCodVersionOrg" class=" col-sm-8">
                                <select id="codVersionOrg" name="codVersionOrg" class="form-control"
                                        onchange="recargarCombosUniAmt(this)" data-required="true">
                                    <option value="0"> --- Seleccione ---</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <hr/>
            <div class="form-horizontal">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">

                            <label for="codUniAmt" class="col-sm-4 control-label" style="padding-top: 0;">Dependencia Jerárquica</label>

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
                                        onchange="recargarComboVersionesTrdOfcProd(this)" data-required="true">
                                    <option value="0"> --- Seleccione ---</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <hr/>
            <div class="form-horizontal">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-4" for="idVersionOfcProd">Consultar versiones</label>

                                <div id="cbxIdVersionOfcProduc" class="col-sm-8">
                                    <select id="idVersionOfcProd" name="idVersionOfcProd" class="form-control"
                                            onchange="definirTRD(this)" data-required="true">
                                        <option value="0"> --- Seleccione ---</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Datos de la version -->
                    <div class="col-sm-5 text-right">
                        <div class="version-fecha"><label><strong>Fecha de Creaci&oacute;n: </strong><span
                                id="currentVersionDate">-</span></label></div>
                        <div class="version-numero">
                            <input type="hidden" name="currentShowVersion" id="currentShowVersion"
                                   value=""/>

                            <div id="datosTRDVersion"><label><strong>Versi&oacute;n: </strong><span
                                    id="currentVersionNumber">-</span></label></div>
                        </div>
                        <div class="col-sm-2 col-sm-offset-8">
                            <button type="submit" id="btReportPDF" class="btn btn-default  btn-sm">
                                <i class="fa fa-file-pdf-o"></i> Ver PDF
                            </button>
                        </div>
                        <div class="col-sm-2">
                            <button type="submit" id="btReportExcel" class="btn btn-default  btn-sm">
                                <i class="fa fa-file-excel-o"></i> Ver EXCEL
                            </button>
                        </div>
                        <input type="hidden" name="reportType" id="reportType" value=""/>
                    </div>
                </div>
            </div>
            <hr/>
        </form>

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
                    <th class="cabecera_overflow" rowspan="2" style="width: 5%">CONFID</th>
                    <th class="cabecera_overflow" colspan="2" style="text-align: center; width: 15%;">RETENCIÓN</th>
                    <th class="cabecera_overflow" colspan="5" style="text-align: center; width: 20%;">DISPOSICIÓN FINAL</th>
                    <th class="cabecera_overflow" rowspan="2" style="width: 15%;text-align: center">PROCEDIMIENTOS</th>
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
                <label class="control-label col-md-6"
                       for="codOfcProd">CONFIDENCIALIDAD:
                    <b>P:</b><span> Pública, </span><b>C:</b><span> Clasificada, </span><b>R:</b><span> Reservada</span></label>

                <div class="form-horizontal col-md-6">
                    <div class="row">
                        <label class="control-label col-md-3"
                               for="codUniAmt">NOMBRE COMIT&#201;:</label>&nbsp;

                        <div class="col-md-8">
                            <label class="control-label">
                                <small id="nombreComite" class="text-muted"></small>
                            </label>
                        </div>
                    </div>
                    <div class="row">
                        <label class="control-label col-md-3"
                               for="codUniAmt">N&#218;MERO ACTA:</label>&nbsp;

                        <div class="col-md-8">
                            <label class="control-label">
                                <small id="numeroActa" class="text-muted"></small>
                            </label>
                        </div>
                    </div>
                    <div class="row">
                        <label class="control-label col-md-3"
                               for="codUniAmt">FECHA:</label>&nbsp;

                        <div class="col-md-8">
                            <label class="control-label">
                                <small id="fechaActa" class="text-muted"></small>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </jsp:body>
</ui:composition>
