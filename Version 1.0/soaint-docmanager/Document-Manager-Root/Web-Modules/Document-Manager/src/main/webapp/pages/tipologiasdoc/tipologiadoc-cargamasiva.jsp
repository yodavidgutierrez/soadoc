<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:composition>

    <jsp:attribute name="header">
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/tipologiadoc-controller.js"></script>
    </jsp:attribute>

    <jsp:body>

        <p class="lead">Administraci&oacute;n <i class="fa fa-angle-double-right"></i>
            <small class="text-muted">Carga Masiva de Tipos Documentales</small>
        </p>
        <div class="table-actions btn-toolbar">
            <button id="btnDescargarTipologia" class="btn btn-default  btn-sm" data-toggle="modal">
                <i class="fa fa-file-excel-o"></i> Descargar Plantilla
            </button>
            <button id="adminTpgLoad" class="btn btn-default  btn-sm">
                <i class="glyphicon glyphicon-share-alt"></i> Regresar
            </button>
        </div>

        <br>
        <br>

        <ui:massiveLoader uploadUrl="/Document-Manager/tipologiasdoc-ml/uploadFile"/>

    </jsp:body>
</ui:composition>