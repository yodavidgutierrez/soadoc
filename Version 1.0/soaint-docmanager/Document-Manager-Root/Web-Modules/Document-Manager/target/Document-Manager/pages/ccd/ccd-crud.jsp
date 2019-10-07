<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:composition>

    <jsp:attribute name="header">
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/ccd-controller.js"></script>
        <script type="text/javascript" src="/Document-Manager/resources/js-controllers/comun-controller.js"></script>
    </jsp:attribute>

    <jsp:body>
        <p class="lead">Administraci&oacute;n <i class="fa fa-angle-double-right"></i>
            <small class="text-muted">Parametrizaci&oacute;n del Cuadro de Clasificaci&oacute;n Documental</small>
        </p>
        <div class="row">
            <div class="col-sm-12">
                <div class="pull-left">
                    <b>Estado :</b> <input type="hidden" style="border: none;" name="statusInstrument" id="statusInstrument" data-instrument="CCD" />
                    <span id="statusInstrumentLabel"></span>
                </div>
                <div class="pull-right">
                    <button id="publicVersionCcd" class="btn btn-default btn-sm require-confirmation"
                            data-toggle="modal" data-target="#modalPublicar" disabled>
                        <i class="glyphicon glyphicon-ok-sign"></i> Publicar Versi&oacute;n
                    </button>
                    <button id="setForConfiguration" data-instrument="CCD" class="btn btn-default btn-sm"><i
                            class="glyphicon glyphicon-cog"></i> Configurar
                    </button>
                    <button id="addItemOrg" class="btn btn-default  btn-sm" data-toggle="modal" disabled data-backdrop="static" data-keyboard="false">
                        <i class="fa fa-plus"></i> Crear CCD
                    </button>
                    <button id="editItemOrg" class="btn btn-default  btn-sm" data-toggle="modal" disabled data-backdrop="static" data-keyboard="false">
                        <i class="fa fa-pencil"></i> Editar
                    </button>
                    <button id="cancelConfiguration" data-instrument="CCD" class="btn btn-danger btn-sm"><i
                            class="glyphicon glyphicon-ban-circle"></i> Cancelar
                    </button>
                </div>
            </div>
        </div>
        

        <br/>

        <table id="tableCCD" class="text-center edt hover table-responsive table table table-bordered table-striped">
            <thead>
                <tr>
                    <th>Dependencia Jerárquica</th>
                    <th>Código/Nombre Dependencia</th>
                    <th>Código/Nombre Serie</th>
                    <th>Código/Nombre SubSerie</th>
                    <th>Activo</th>
                </tr>
            </thead>
            <tr>
                <td colspan="5">Cargando datos, por favor espere...</td>
            </tr>
        </table>

        <jsp:include page="ccd-panel.jsp"/>
        <jsp:include page="ccd-panel-publicar.jsp"/>

    </jsp:body>
</ui:composition>