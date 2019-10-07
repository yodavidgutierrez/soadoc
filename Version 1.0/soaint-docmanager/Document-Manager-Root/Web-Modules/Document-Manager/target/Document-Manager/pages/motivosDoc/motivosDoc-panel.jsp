<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<!-- Model add and edit serie -->

<div class="modal fade bs-modal-lg" id="modalAddEditMotivoDoc" tabindex="-1" role="dialog"
     aria-labelledby="myLargeModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-lg-pq">
        <form id="formMotivosDoc" name="formMotivosDoc">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title" id="modalAddEditMotivoDocTitle"> xxxx </h4>
                </div>
                <div class="modal-body">
                    <!--definicion well bs-component -->
                    <div class="well bs-component">
                        <div class="">
                            <input type="hidden" id="ideMotCreacion" name="ideMotCreacion"/>
                            <!-- inicio row-->
                            <div class="row" style="width:100%;">
                                <div class="col-sm-8">
                                    <ui:richInput id="nomMotCreacion" label="Nombre" tabindex="1"/>
                                </div>
                                <div class="col-sm-4">
                                    <ui:richBooleanStatus id="estado" label="Estado" tabindex="6"/>
                                </div>
                            </div>
                            <!-- final row -->
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-default" id="guardarMotivosDoc" tabindex="11">Guardar</button>
                            <button class="btn btn-default" data-dismiss="modal" tabindex="12" aria-hidden="true"
                                    id="modalAddEditMotivoDoc">Cerrar
                            </button>
                        </div>
                    </div>
                    <!-- final well bs-component -->
                </div>
            </div>
        </form>
        <!-- /.modal-dialog -->
    </div>
</div>
