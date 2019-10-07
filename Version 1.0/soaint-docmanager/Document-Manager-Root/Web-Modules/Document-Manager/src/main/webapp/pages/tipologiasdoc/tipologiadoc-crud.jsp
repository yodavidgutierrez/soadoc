<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:composition>

    <jsp:attribute name="header">
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/tipologiadoc-controller.js"></script>
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/comun-controller.js"></script>
    </jsp:attribute>

    <jsp:body>

        <p class="lead">Administraci&oacute;n <i class="fa fa-angle-double-right"></i>
            <small class="text-muted">Parametrizaci&oacute;n de Tipos documentales</small>
        </p>

        <div class="table-actions btn-toolbar">
            <button id="masiveLoadTipologia" class="btn btn-default  btn-sm">
                <i class="fa fa-file"></i> Carga Masiva
            </button>
            <button id="btmDetailsTipologia" class="btn btn-default  btn-sm" data-toggle="modal">
                <i class="fa fa-list"></i> Detalles
            </button>
            <button id="btmAddTipologia" data-backdrop="static" data-keyboard="false" class="btn btn-default  btn-sm" data-toggle="modal" data-target="#modalAddEditTipologias">
                <i class="fa fa-plus"></i> Crear Tipologia
            </button>
            <button id="btmEditTipologia" data-backdrop="static" data-keyboard="false" class="btn btn-default  btn-sm" data-toggle="modal">
                <i class="fa fa-pencil"></i> Editar
            </button>
            <button id="btnRemoveTipologia" class="btn btn-danger  btn-sm" data-toggle="modal">
                <i class="fa fa-trash-o"></i> Eliminar
            </button>
            <button type="submit" id="exportarExcel" class="btn btn-default  btn-sm" data-toggle="modal">
            <i class="fa fa-file-excel-o"></i> Exportar
            </button>
        </div>

        <br/>

        <table id="tableTipologiasDoc" class="text-center edt hover table-responsive table table table-bordered table-striped">
            <thead>
            <tr>
                <th>Nombre</th>
                <th>Activo</th>
                <th>Tradici&oacute;n Documental</th>
                <th>Soporte</th>
                <th>Caracter&iacute;sticas</th>
            </tr>
            </thead>
            <tr>
                <td colspan="2">Cargando datos, por favor espere...</td>
            </tr>
        </table>

        <jsp:include page="tipologiadoc-panel.jsp" />

    </jsp:body>
</ui:composition>