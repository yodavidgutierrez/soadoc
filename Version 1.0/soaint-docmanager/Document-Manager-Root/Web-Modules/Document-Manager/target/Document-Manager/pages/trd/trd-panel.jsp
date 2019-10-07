<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<!-- Model add and edit serie -->

<div class="modal fade bs-modal-lg" id="modalPublicar" tabindex="-1" role="dialog"
     aria-labelledby="myLargeModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <form id="formPublicar" name="formPublicar">

            <div class="modal-content">

                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Publicar Versi&#243;n</h4>
                </div>

                <div class="modal-body">
                    <div class="well bs-component">
                        <div>
                            <div class="row">
                                <div class="col-md-12 py-1">
                                    <ui:richInput id="nomComite" label="Nombre Comit&#233;"
                                                  tabindex="1"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 py-1">
                                    <ui:richInput id="numActa" label="N&#250;mero Acta" tabindex="2"/>
                                </div>

                                <div class="col-md-6 py-1">
                                    <ui:richDatePicker id="fechaActa" label="Fecha"
                                                       tabindex="3"/>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div class="modal-footer">
                        <button class="btn btn-default" id="publicarTRD" tabindex="11">Publicar</button>
                        <button class="btn btn-default" data-dismiss="modal" tabindex="12" aria-hidden="true"
                                id="cerrarPublicarCCD">Cerrar
                        </button>
                    </div>
                </div>
            </div>
            <!-- /.modal-content -->
        </form>
    </div>

    <!-- /.modal-dialog -->
</div>



