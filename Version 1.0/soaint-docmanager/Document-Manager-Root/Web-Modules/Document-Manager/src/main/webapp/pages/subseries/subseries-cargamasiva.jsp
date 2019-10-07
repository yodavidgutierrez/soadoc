<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:composition>

    <jsp:attribute name="header">
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/subseries-controller.js"></script>
    </jsp:attribute>

    <jsp:body>

        <p class="lead">Administraci&oacute;n <i class="fa fa-angle-double-right"></i>
            <small class="text-muted">Carga Masiva de Subseries Documentales</small>
        </p>
        <div class="table-actions btn-toolbar">
            <button id="btnDownloadPlantilla" class="btn btn-default  btn-sm" data-toggle="modal">
                <i class="fa fa-file-excel-o"></i> Descargar Plantilla
            </button>
            <button id="adminSubSerieLoad" class="btn btn-default  btn-sm">
                <i class="glyphicon glyphicon-share-alt"></i> Regresar
            </button>
        </div>

        <br>
        <br>

        <ui:massiveLoader uploadUrl="/Document-Manager/subseries-ml/uploadFile"/>

    </jsp:body>
</ui:composition>