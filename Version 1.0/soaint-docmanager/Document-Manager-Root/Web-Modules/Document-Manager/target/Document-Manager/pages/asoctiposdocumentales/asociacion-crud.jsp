<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:composition>

    <jsp:attribute name="header">
		<link href="/Document-Manager/resources/soaint-ui/js/vendor/bootstrap-multiselect-master/css/bootstrap-multiselect.css" type="text/css" rel="stylesheet">
        <link href="/Document-Manager/resources/css/select2.min.css" rel="stylesheet" />
		<script src="/Document-Manager/resources/soaint-ui/js/vendor/bootstrap-multiselect-master/js/bootstrap-multiselect.js"></script> 
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/asociacion-controller.js"></script>
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/comun-controller.js"></script>
        <script type="text/javascript" src="/Document-Manager/resources/js/pickList.js"></script>
        <script src="/Document-Manager/resources/js/select2.min.js"></script>
    </jsp:attribute>

    <jsp:body>

        <p class="lead">Administraci&oacute;n <i class="fa fa-angle-double-right"></i>
            <small class="text-muted">Asociaci&oacute;n de Tipos Documentales a Series/Subseries</small>
        </p>

        <form id="formExport" action="/Document-Manager/asociacion/generateExcel" target="_blank"
        method="get" name="formExport">
            <div class="table-actions btn-toolbar">
                <!--    <button id="masiveLoadAsociacion" class="btn btn-default  btn-sm">
                        <i class="fa fa-file"></i> Carga Masiva
                    </button> -->
                <button id="addAsociacion" class="btn btn-default btn-sm" type="button" data-toggle="modal" data-backdrop="static" data-keyboard="false"
                        data-target="#modalAddEditAsociacion">
                    <i class="fa fa-plus"></i> Asociar Tipos
                </button>
                <button id="editAsoc" class="btn btn-default  btn-sm" type="button" data-toggle="modal" data-backdrop="static" data-keyboard="false">
                    <i class="fa fa-pencil"></i> Editar
                </button>
                <button type="submit" id="btReportEXCEL" class="btn btn-default  btn-sm">
                    <i class="fa fa-file-pdf-o"></i> Exportar
                </button>


            </div>
        </form>

        <table id="tableAsociacion"
               class="text-center edt hover table-responsive table table table-bordered table-striped">
            <thead>
            <tr>
                <th>Código Serie</th>
                <th>Serie</th>
                <th>Código SubSerie</th>
                <th>SubSerie</th>
                <th>Tipología Documental</th>
            </tr>
            </thead>
            <tr>
                <td colspan="5">Cargando datos, por favor espere...</td>
            </tr>
        </table>

        <jsp:include page="asociacion-panel.jsp"/>

    </jsp:body>
</ui:composition>