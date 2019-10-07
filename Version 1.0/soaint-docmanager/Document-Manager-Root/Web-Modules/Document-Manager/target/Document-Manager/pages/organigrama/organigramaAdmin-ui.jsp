<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:composition>

    <jsp:attribute name="header">

        <script type="text/javascript" src="/Document-Manager/resources/soaint-ui/js/vendor/jstree.min.js"></script>
        <link href="${pageContext.request.contextPath}/resources/soaint-ui/css/jtree/style.min.css" rel="stylesheet">
        <script type="text/javascript"
                src="/Document-Manager/resources/js-controllers/organigama-controller.js"></script>
		<script type="text/javascript" src="/Document-Manager/resources/js-controllers/comun-controller.js"></script>
    </jsp:attribute>


    <jsp:body>
        <div class="container my-2 px-5">
            <div class="row">
                <div class="col-md-12">
                    <p class="lead">Administraci&oacute;n <i class="fa fa-angle-double-right"></i>
                        <small class="text-muted">Organigrama Administrativo</small>
                    </p>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="pull-left">
                        <b>Estado:</b> <input type="hidden" style="border: none;" name="statusInstrument"
                                              id="statusInstrument" data-instrument="ORGANIGRAMA"/>
                        <span id="statusInstrumentLabel"></span>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="pull-right">
                                <button id="publicarOrganigrama" class="btn btn-default btn-sm require-confirmation"><i
                                        class="glyphicon glyphicon-ok-sign" disabled></i> Publicar Versi&oacute;n
                                </button>
                                <button id="setForConfiguration" data-instrument="ORGANIGRAMA"
                                        class="btn btn-default btn-sm"><i
                                        class="glyphicon glyphicon-cog"></i> Configurar
                                </button>
                                <button id="addItemOrg" class="btn btn-default btn-sm" data-toggle="modal"
                                        data-target="#modalAddEditOrganigrama" disabled data-backdrop="static" data-keyboard="false">
                                        <i class="fa fa-file"></i> Crear Nodo
                                </button>
                                <button id="editItemOrg" class="btn btn-default btn-sm" disabled data-backdrop="static" data-keyboard="false">
                                        <i class="fa fa-pencil"></i> Editar Nodo
                                </button>
                                <button id="cancelConfiguration" data-instrument="ORGANIGRAMA"
                                        class="btn btn-danger btn-sm"><i
                                        class="glyphicon glyphicon-ban-circle"></i> Cancelar
                                </button>

                            </div>
                        </div>



                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <div class="input-group input-group-lg my-2">
                        <span class="input-group-addon addon-blue" id="busqueda"><i class="fa fa-search"></i></span>
                        <input type="text" class="form-control" aria-label="Large" aria-describedby="busqueda"
                               name="search-input" id="search-input" placeholder="Buscar...">
                    </div>
                </div>
            </div>

            <div class="col-lg-6" style="font-size:smaller; overflow-x: auto; overflow-y: auto" id="organigrama"></div>

            <div class="row" style="min-height: 300px;">
                <div class="col-lg-6" style="font-size:smaller; overflow-x: auto; overflow-y: auto"
                     id="organigrama"></div>
                <div class="col-lg-6" id="detailsOrganigrama" style="padding-left: 50px;">
                    <div class="form-horizontal">
                        <div class="form-group" id="placeholder-wrapper">
                            <div class="col-sm-12">Seleccione un elemento para ver los detalles</div>
                        </div>
                        <div style="min-height: 200px;">
                            <div class="form-group" id="detailsParent-wrapper" style="display: none;">
                                <label for="detailsParent" class="col-sm-4 control-label">Dependencia Jer&aacute;rquica
                                    (Secci&oacute;n)</label>

                                <div class="col-sm-8">
                                    <div id="detailsParent" class="form-control-static">[UNIDAD_ADMINISTRATIVA]</div>
                                </div>
                            </div>
                            <div class="form-group" id="detailsParentCode-wrapper" style="display: none;">
                                <label for="detailsParentCode" class="col-sm-4 control-label">Código</label>

                                <div class="col-sm-8">
                                    <div id="detailsParentCode" class="form-control-static">[UNIDAD_ADMINISTRATIVA_CODE]
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" id="detailsChild-wrapper" style="display: none;">
                                <label for="detailsChild" class="col-sm-4 control-label">Dependencia Productora
                                    (Secci&oacute;n)</label>

                                <div class="col-sm-8">
                                    <div id="detailsChild" class="form-control-static">[OFICINA_PRODUCTORA]</div>
                                </div>
                            </div>
                            <div class="form-group" id="detailsChildCode-wrapper" style="display: none;">
                                <label for="detailsParentCode" class="col-sm-4 control-label">Código</label>

                                <div class="col-sm-8">
                                    <div id="detailsChildCode" class="form-control-static">[OFICINA_PRODUCTORA_CODE]
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" id="detailsChild-status-wrapper" style="display: none;">
                                <label for="detailsChild-status" class="col-sm-4 control-label">Estado</label>

                                <div class="col-sm-8">
                                    <div id="detailsChild-status" class="form-control-static">[Activado]</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="organigramaAdmin-crud.jsp"/>
    </jsp:body>
</ui:composition>