<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:composition>

    <jsp:attribute name="header">
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/subseries-controller.js"></script>
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/comun-controller.js"></script>
    </jsp:attribute>

    <jsp:body>

        <p class="lead">Administraci&oacute;n <i class="fa fa-angle-double-right"></i>
            <small class="text-muted">Parametrizaci&oacute;n de Subseries Documentales</small>
        </p>

        <div class="table-actions btn-toolbar">
            <button id="masiveLoadSubserie" class="btn btn-default  btn-sm">
                <i class="fa fa-file"></i> Carga Masiva
            </button>
            <button id="btmDetailsSubserie" class="btn btn-default  btn-sm" data-toggle="modal" data-backdrop="static" data-keyboard="false">
                <i class="fa fa-list"></i> Detalles
            </button>
            <button id="btmAddSubserie" class="btn btn-default  btn-sm" data-toggle="modal" data-backdrop="static" data-keyboard="false"
                    data-target="#modalAddEditSubseries">
                <i class="fa fa-plus"></i> Crear Subserie
            </button>
            <button id="btmEditSubserie" class="btn btn-default  btn-sm" data-toggle="modal" data-backdrop="static" data-keyboard="false">
                <i class="fa fa-pencil"></i> Editar
            </button>
            <button id="btnSubserieRemove" class="btn btn-danger  btn-sm" data-toggle="modal">
                <i class="fa fa-trash-o"></i> Eliminar
            </button>
            <button  id="exportExcel" class="btn btn-default  btn-sm" data-toggle="modal">
                <i class="fa fa-file-excel-o"></i> Exportar
            </button>
        </div>

        <br/>

        <table id="tableSubseries" class="text-center edt hover table-responsive table table table-bordered table-striped">
            <thead>
            <tr>
                <th>C&oacute;digo</th>
                <th>Nombre</th>
                <th>Descripción</th>
                <th>Fuente Bibliografica</th>
                <th>Activo</th>
                <th>Características</th>
                <th>Confidencialidad</th>
            </tr>
            </thead>
            <tr>
                <td colspan="5">Cargando datos, por favor espere...</td>
            </tr>
        </table>

        <jsp:include page="subseries-panel.jsp" />

    </jsp:body>
</ui:composition>