/**
 * Created by jrodriguez on 20/09/2016.
 */

$(function () {

    // ui setup - tables ----------------------

    $("#tableSubseries").dataTable({
        info: false,
        select: true,
        "ajax": {
            url: "/Document-Manager/subseries/subseriesList",
            type: "GET"
        },
        "columnDefs": [
            {
                "visible": false,
                "searchable": false
            }
        ],
        "order": [[2, "asc"]],
        "columns": [
            {"data": "codSubserie"},
            {"data": "nomSubserie"},
            {"data": "notAlcance"},
            {"data": "fueBibliografica"},
            {"data": "estSubserie"},
            {"data": "carConcat"},
            {"data": "conConcat"}

        ]
    });

    $('#btnDownloadPlantilla').click(function () {

        window.location.href = '/Document-Manager/subseries/downloadPlantilla';
    });


    $('#exportExcel').click(function () {

       window.location.href = '/Document-Manager/subseries/generarExcel';
    });

    $('#chboxConfidencialidad').on('click', function (event) {
        event.stopPropagation();
        if (event.target.tagName == "INPUT") {
            if (event.target.previousSibling.data == "Pública") {
                if (event.target.checked) {
                    $('#conClasificada').prop("disabled", true);
                    $('#conReservada').prop("disabled", true);
                } else {
                    $('#conClasificada').prop("disabled", false);
                    $('#conReservada').prop("disabled", false);
                }
            } else if (event.target.previousSibling.data == "Clasificada") {
                if (event.target.checked) {
                    $('#conPublica').prop("disabled", true);
                    $('#conReservada').prop("disabled", true);
                } else {
                    $('#conPublica').prop("disabled", false);
                    $('#conReservada').prop("disabled", false);
                }
            } else {
                if (event.target.checked) {
                    $('#conPublica').prop("disabled", true);
                    $('#conClasificada').prop("disabled", true);
                } else {
                    $('#conPublica').prop("disabled", false);
                    $('#conClasificada').prop("disabled", false);
                }
            }
        }
    });

    // ui setup ----------------------

    $("#cbxSeries").combobox({id: 'idSerie', data: '/Document-Manager/util/seriesList', tabindex: 6});
    $("#cbxMotivos").combobox({id: 'idMotivo', data: '/Document-Manager/util/motivosCreacionList', tabindex: 6});
    $("#idMotivo").chosen();
    $("#chboxCaracteristicas").checkbox({data: '/Document-Manager/util/caracteristicasList'});
    $("#chboxConfidencialidad").checkbox({data: '/Document-Manager/util/confidencialidadList'});

    // action setup - masive load ----------------------

    $('#masiveLoadSubserie').click(function () {
        document.location.href = '/Document-Manager/subseries-ml'
    });


    $("#idSerie").chosen().change(function (e) {
        $("#formSubserie").data('bootstrapValidator').updateStatus('serie', 'NOT_VALIDATED');
    });


    // action setup - open modal to add subseries ----------------------

    $('#btmAddSubserie').click(function () {
        limpiarForm();
        $('#modalAddEditSubserieTittle').html("<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> Crear Subserie Documental");
        $('#idSerie').val("");
        $('#estSubseriecheckboxActivoFalse').attr("disabled", true);
        $("#estSubseriecheckboxActivo").prop('checked', 'checked');
        $('#nomSubserie').prop("readonly", false);
        $('#nomSubserie').prop("readonly", false);
        //Usado para inicializar la lista de series
        $("select#idSerie").trigger("liszt:updated");
        $('#idSerie').prop("disabled", false).trigger("liszt:updated");;
        $('#codSubserie').prop("readonly", false);
    });


    // action setup - save subseries ----------------------
    //funcion para validar que el nombre no este repetido en bd
    getValidatNomSubSerie = function () {
        var nomSubserie = $('#nomSubserie').val();
        var codSubserie = $('#codSubserie').val();
        return {"nomSubserie": nomSubserie, "codSubserie": codSubserie};
    };

    $("#formSubserie").bootstrapValidator({
        live: 'disabled',
        fields: {
            idSerie: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    }
                }
            },
            codSubserie: {
                validators: {
                    notEmpty: {
                        message: "El campo c&oacute;digo es requerido"
                    },
                    stringLength: {
                        min: 1,
                        max: 100,
                        message: 'El c&oacute;digo debe ser menor que 100 caracteres'
                    },
                    regexp: {
                        regexp: /^[0-9]+$/,
                        message: "El campo solo admite d&iacute;gitos"
                    }
                }
            },
            nomSubserie: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    }, remote: {
                        url: '/Document-Manager/subseries/validateNomSubSerie',
                        message: 'El nombre insertado ya existe',
                        data: getValidatNomSubSerie
                    },
                    regexp: {
                        regexp: /^[^<>]+$/,
                        message: "El campo no admite caracteres especiales"
                    }
                }
            },
            actAdministrativo: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    },
                    regexp: {
                        regexp: /^[^<>]+$/,
                        message: "El campo no admite caracteres especiales"
                    }
                }
            },
          /*  fueBibliografica: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    }
                }
            },
            notAlcance: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    }
                }
            },*/
            idMotivo: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    }
                }
            },
            chboxCaracteristicas: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    }
                }
            }
        },
        submitButtons: 'button#guardarSubserie',

        submitHandler: function (validator, form) {

            var caract = false;
            var confi = false;
            if ($("#carTecnica").prop('checked') || $("#carLegal").prop('checked') || $("#carAdministrativa").prop('checked') || $("#carContable").prop('checked') || $("#carJuridica").prop('checked')) {
                caract = true;
            } else {
                showAlert('caracteristicaSelect');
            }
            if ($("#conPublica").prop('checked') || $("#conClasificada").prop('checked') || $("#conReservada").prop('checked')) {
                confi = true;
            } else {
                showAlert('confiabilidadSelect');
            }
            if (caract && confi) {
                $('#idSerie').prop("disabled", false).trigger("liszt:updated");
                processForm('formSubserie', '/Document-Manager/subseries')
                $('#tableSubseries').dataTable().fnReloadAjax();
            }
        }
    });


    // action setup - show detail subseries ----------------------

    $('#btmDetailsSubserie').click(function () {
        var tableSubseries = $('#tableSubseries').DataTable();
        var rowselected = tableSubseries.row('.selected');
        if (rowselected.length != 0) {
            var data = tableSubseries.row('.selected').data();
            $('#idSerieDetail').val(data.codSerie + ' - ' + data.nomSerie)
            $('#codigoDetail').val(data.codSubserie);
            $('#nombreDetail').val(data.nomSubserie);
            $('#actoAdministrativoDetail').val(data.actAdministrativo);
            $('#motivoDetail').val(data.nombreMotCreacion);
            $('#notaDetail').val(data.notAlcance);
            $('#fuenteDetail').val(data.fueBibliografica);


            if ($($("#tableSubseries tr.selected input").get(0)).prop('checked')) {
                $('#estSubserieDetail').val("Activo");
            } else {
                $('#estSubserieDetail').val("Inactivo");
            }
            data.carAdministrativa ? $('#administrativaDetail').removeClass("hidden") : $('#administrativaDetail').addClass("hidden");
            data.carContable ? $('#contableDetail').removeClass("hidden") : $('#contableDetail').addClass("hidden");
            data.carJuridica ? $('#juridicaDetail').removeClass("hidden") : $('#juridicaDetail').addClass("hidden");
            data.carLegal ? $('#legalDetail').removeClass("hidden") : $('#legalDetail').addClass("hidden");
            data.carTecnica ? $('#tecnicaDetail').removeClass("hidden") : $('#tecnicaDetail').addClass("hidden");
            data.conClasificada ? $('#clasificadaDetail').removeClass("hidden") : $('#clasificadaDetail').addClass("hidden");
            data.conPublica ? $ ('#publicaDetail').removeClass("hidden") : $('#publicaDetail').addClass("hidden");
            data.conReservada ? $('#reservadaDetail').removeClass("hidden") : $('#reservadaDetail').addClass("hidden");

            $('#tableSubseries').DataTable().draw();
            $('#digDetailSubserie').modal('show');

        } else {
            showAlert('errorEdit');
        }
    });

    // action setup - edit subseries ----------------------

    $('#btmEditSubserie').click(function () {
        limpiarForm();
        $('#inputActivoFalse').attr("disabled", false);
        var tableSubseries = $('#tableSubseries').DataTable();
        var rowselected = tableSubseries.row('.selected');
        if (rowselected.length != 0) {

            //Usado para cargar el valor que tiene la lista de series
            var data = tableSubseries.row('.selected').data();
            $('#idSerie').val(rowselected.data().idSerie);
            $("select#idSerie").trigger("liszt:updated");
            $('#idSerie').prop("disabled", true).trigger("liszt:updated");
            $('#codSubserie').prop("readonly", true);
            $('#modalAddEditSubserieTittle').html("<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> Editar Subserie Documental");
            $('#estSubseriecheckboxActivoFalse').prop("disabled", false);
            $('#ideSubserie').val(data.ideSubserie);
            $('#codSubserie').val(data.codSubserie);
            $('#nomSubserie').val(data.nomSubserie);
            $('#actAdministrativo').val(data.actAdministrativo);
            $('#idMotivo').val(data.idMotivo);
            $("select#idMotivo").trigger("liszt:updated");
            $('#notAlcance').val(data.notAlcance);
            $('#fueBibliografica').val(data.fueBibliografica);
            $('#carLegal').prop('checked', data.carLegal ? true : false);
            $('#carAdministrativa').prop('checked', data.carAdministrativa ? true : false);
            $('#carTecnica').prop('checked', data.carTecnica ? true : false);
            $('#carContable').prop('checked', data.carContable ? true : false);
            $('#carJuridica').prop('checked', data.carJuridica ? true : false);
            $('#conPublica').prop('checked', data.conPublica ? true : false);
            $('#conClasificada').prop('checked', data.conClasificada ? true : false);
            $('#conReservada').prop('checked', data.conReservada ? true : false);
            $('#modalAddEditSubseries').modal('show');

            if (data.estSubserieValue === 1) {
                $("#estSubseriecheckboxActivo").prop('checked', 'checked');
            } else {
                $("#estSubseriecheckboxActivoFalse").prop('checked', 'checked');
            }

            if ($('#conPublica').prop("checked")) {
                $('#conClasificada').prop("disabled", true);
                $('#conReservada').prop("disabled", true);
            }
            if ($('#conClasificada').prop("checked")) {
                $('#conPublica').prop("disabled", true);
                $('#conReservada').prop("disabled", true);
            }
            if ($('#conReservada').prop("checked")) {
                $('#conPublica').prop("disabled", true);
                $('#conClasificada').prop("disabled", true);
            }

        } else {
            showAlert('errorEdit');
        }
    });

    // action setup - remove subseries ----------------------
    $('#btnSubserieRemove').confirmation({
        title: "Recuerde que al eliminar la serie todas las subseries y sus relaciones se eliminaran también. ¿Esta seguro de realizar esta acción?",
        singleton: true,
        popout: true,
        placement: "left",
        btnOkLabel: "Eliminar",
        btnCancelLabel: "Cancelar",
        onConfirm: function (e) {
            e.preventDefault();
            var tableSubseries = $('#tableSubseries').DataTable();
            var rowselected = tableSubseries.row('.selected');
            var data = tableSubseries.row('.selected').data();
            if (rowselected.length != 0) {
                sendDeleteRequest('/Document-Manager/subseries/removesubserie/' + data.ideSubserie);
                return false;
            } else {
                showAlert('errorEdit');
            }
            $('#tableSubseries').dataTable().fnReloadAjax();
        }
    });

    //Resetear el modal de Adicionar / Editar
    function limpiarForm() {
        $('#formSubserie').data('bootstrapValidator').resetForm(true);
        $('#idSerie').val("");
        $('#codSerie').val("");
        $('#codSubserie').val("");
        $('#nomSubserie').val("");
        $('#actAdministrativo').html("");
        $('#idMotivo').val("");
        $('#fueBibliografica').html("");
        $('#carLegal').prop('checked', false);
        $('#carTecnica').prop('checked', false);
        $('#carAdministrativa').prop('checked', false);
        $('#carContable').prop('checked', false);
        $('#carJuridica').prop('checked', false);
        $('#conPublica').prop('checked', false);
        $('#conClasificada').prop('checked', false);
        $('#conReservada').prop('checked', false);
        $('#filtroSeccion').val("");
        $('#filtroSubseccion').val("");
        $('#asignacionTable').DataTable().rows().remove().draw(false);
        $('#ideSerie').val("");
        $('#idMotivo').val(null);
        $('#ideSubserie').val("");
        $("select#idMotivo").trigger("liszt:updated");
        $("#filtrosTabla").find("option").removeAttr("disabled");
        $("#filtrosTabla").find("option").removeAttr("style");
        $("#filtrosTabla").find("option").removeClass("hidden");
        $("#filtrosTablaSub").find("option").removeAttr("disabled");
        $("#filtrosTablaSub").find("option").removeAttr("style");
        $("#filtrosTablaSub").find("option").removeClass("hidden");
        $("#usado").html("");
        $('#conPublica').prop("disabled", false);
        $('#conReservada').prop("disabled", false);
        $('#conClasificada').prop("disabled", false);
    }
    ;

});

// --- always use this name
function formSucess() {

    $('#modalAddEditSubseries').modal('hide');
    $('#tableSubseries').dataTable().fnReloadAjax();
    $("#formSubserie").data('bootstrapValidator').resetForm(true);
}

// --- always use this name
function formError() {

}