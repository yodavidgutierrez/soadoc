/**
 * Created by jrodriguez on 02/09/2016.
 */

$(document).ready(function () {
    // ui setup - tables ----------------------


    var tableSeries = $("#tableSeries").dataTable({


        info: false,
        select: true,
        "ajax": {
            url: "/Document-Manager/series/seriesList",
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
            {"data": "codSerie"},
            {"data": "nomSerie"},
            {"data": "actAdministrativo"},
            {"data": "nombreMotCreacion"},
            {"data": "estSerie"},
            {"data": "carConcat"},
            {"data": "conConcat"}

        ]
    })

    $('#exportarExcel').click(function () {
        window.location.href = '/Document-Manager/series/generateExcel';
    });

    $('#downloadPlantilla').click(function () {
        window.location.href = '/Document-Manager/series/downloadExcel';
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
    $("#cbxMotivos").combobox({id: 'idMotivo', data: '/Document-Manager/util/motivosCreacionList', tabindex: 6});
    $("#idMotivo").chosen();
    $("#chboxCaracteristicas").checkbox({data: '/Document-Manager/util/caracteristicasList'});
    $("#chboxConfidencialidad").checkbox({data: '/Document-Manager/util/confidencialidadList'});

    // action setup - masive load ----------------------

    $('#masiveLoadSerie').click(function () {
        document.location.href = '/Document-Manager/series-ml'
    });

    // action setup - open modal to add series ----------------------

    $('#addSerie').click(function () {
        limpiarForm();
        $('#modalAddEditSerieTittle').html("<span class='glyphicon glyphicon-pencil ' aria-hidden='true'></span> Crear Serie Documental");
        $('#ideSerie').val("");
        $('#estSeriecheckboxActivoFalse').attr("disabled", true);
        $("#estSeriecheckboxActivo").prop('checked', 'checked');
        $('#codSerie').prop("readonly", false);
        $('#nomSerie').prop("readonly", false);
        $('#indUnidadCorcheckboxNo').prop('checked', 'checked');
    });

    // action setup - show detail series ----------------------

    $('#detailsSerie').click(function () {
        var tableSeries = $('#tableSeries').DataTable();
        var rowselected = tableSeries.row('.selected');
        if (rowselected.length != 0) {
            var data = tableSeries.row('.selected').data();
            $('#codigoDetail').val(data.codSerie);
            $('#nombreDetail').val(data.nomSerie);
            $('#actoAdministrativoDetail').val(data.actAdministrativo);
            $('#motivoDetail').val(data.nombreMotCreacion);
            $('#notaDetail').val(data.notAlcance);
            $('#fuenteDetail').val(data.fueBibliografica);

            if (data.indUnidadCor === '1') {
                $("#indUnidadCorcheckboxSi").prop('checked', 'checked');
            } else {
                $("#indUnidadCorcheckboxNo").prop('checked', 'checked');
            }

            if ($($("#tableSeries tr.selected input").get(0)).prop('checked')) {
                $('#estSerieDetail').val("Activo");
            } else {
                $('#estSerieDetail').val("Inactivo");
            }

            data.carAdministrativa ? $('#administrativaDetail').removeClass("hidden") : $('#administrativaDetail').addClass("hidden");
            data.carContable ? $('#contableDetail').removeClass("hidden") : $('#contableDetail').addClass("hidden");
            data.carJuridica ? $('#juridicaDetail').removeClass("hidden") : $('#juridicaDetail').addClass("hidden");
            data.carLegal ? $('#legalDetail').removeClass("hidden") : $('#legalDetail').addClass("hidden");
            data.carTecnica ? $('#tecnicaDetail').removeClass("hidden") : $('#tecnicaDetail').addClass("hidden");
            data.conClasificada ? $('#clasificadaDetail').removeClass("hidden") : $('#clasificadaDetail').addClass("hidden");
            data.conPublica ? $ ('#publicaDetail').removeClass("hidden") : $('#publicaDetail').addClass("hidden");
            data.conReservada ? $('#reservadaDetail').removeClass("hidden") : $('#reservadaDetail').addClass("hidden");


            $('#tableSeries').DataTable().draw();
            $('#digDetailSerie').modal('show');
        }
        else {

            showAlert('errorEdit');
        }
    });

    // action setup - edit series ----------------------

    $('#editSerie').click(function () {
        limpiarForm();

        var tableSeries = $('#tableSeries').DataTable();
        var rowselected = tableSeries.row('.selected');

        if (rowselected.length != 0) {
            var data = tableSeries.row('.selected').data();

            $('#modalAddEditSerieTittle').html("<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> Editar Serie Documental");
            $('#estSeriecheckboxActivoFalse').prop("disabled", false);
            $('#ideSerie').val(data.ideSerie);
            $('#codSerie').val(data.codSerie);
            $('#codSerie').prop("readonly", true);
            $('#nomSerie').val(data.nomSerie);
            $('#nomSerie').prop("readonly", false);
            $('#notAlcance').val(data.notAlcance);
            $('#fueBibliografica').val(data.fueBibliografica);
            $('#actAdministrativo').val(data.actAdministrativo);
            $('#idMotivo').val(data.idMotivo);
            $("select#idMotivo").trigger("liszt:updated");

            if (data.indUnidadCor === '1') {
                $("#indUnidadCorcheckboxSi").prop('checked', 'checked');
            } else {
                $("#indUnidadCorcheckboxNo").prop('checked', 'checked');
            }

            if (data.estSerieValue === 1) {
                $("#estSeriecheckboxActivo").prop('checked', 'checked');
            } else {
                $("#estSeriecheckboxActivoFalse").prop('checked', 'checked');
            }
            $('#carLegal').prop('checked', data.carLegal ? true : false);
            $('#carAdministrativa').prop('checked', data.carAdministrativa ? true : false);
            $('#carTecnica').prop('checked', data.carTecnica ? true : false);
            $('#carContable').prop('checked', data.carContable ? true : false);
            $('#carJuridica').prop('checked', data.carJuridica ? true : false);
            $('#conPublica').prop('checked', data.conPublica ? true : false);
            $('#conClasificada').prop('checked', data.conClasificada ? true : false);
            $('#conReservada').prop('checked', data.conReservada ? true : false);
            $('#modalAddEditSerieDocumental').modal('show');

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

    // action setup - remove series ----------------------

    $('#btnremove').confirmation({
        title: "Recuerde que al eliminar la serie todas las subseries y sus relaciones se eliminaran también. ¿Esta seguro de realizar esta acción?",
        singleton: true,
        popout: true,
        placement: "left",
        btnOkLabel: "Eliminar",
        btnCancelLabel: "Cancelar",
        onConfirm: function (e) {
            e.preventDefault();
            var tableserie = $('#tableSeries').DataTable();
            var data = tableserie.row('.selected').data();
            var rowselected = tableserie.row('.selected');

            if (rowselected.length != 0) {
                sendDeleteRequest('/Document-Manager/series/removeSerie/' + data.ideSerie);
                $('#tableSeries').dataTable().fnReloadAjax();
                return false;
            } else {
                showAlert('errorEdit');
            }
            $('#tableSeries').dataTable().fnReloadAjax();
        }


    });

    //Función para  enviar mas de un campo al validar el codigo unico en el formulario
    getValidateCod = function () {
        var codSerie = $('#codSerie').val();
        var ideSerie = $('#ideSerie').val();
        return {"codSerie": codSerie, "ideSerie": ideSerie};
    };

    //Función para validar que el nombre no este repetido en bd
    getValidateNomSerie = function () {
        var nomSerie = $('#nomSerie').val();
        var ideSerie = $('#ideSerie').val();
        return {"nomSerie": nomSerie, "ideSerie": ideSerie};
    };


    // action setup - save series ----------------------

    $("#formSerieAsoc").bootstrapValidator({


        live: 'disabled',
        fields: {
            codSerie: {
                validators: {
                    notEmpty: {
                        message: "El campo c&oacute;digo es requerido"
                    },
                    remote: {
                        url: '/Document-Manager/series/validateCodSerie',
                        message: 'El c&oacute;digo ingresado ya existe',
                        data: getValidateCod
                    },
                    regexp: {
                        regexp: /^[0-9]+$/,
                        message: "El campo solo admite d&iacute;gitos"
                    },
                    stringLength: {
                        min: 1,
                        max: 100,
                        message: 'El c&oacute;digo debe ser menor que 100 caracteres'
                    }
                }
            },
            nomSerie: {
                validators: {
                    notEmpty: {
                        message: "El campo nombre es requerido"
                    }, remote: {
                        url: '/Document-Manager/series/validateNomSerie',
                        message: 'El nombre ingresado ya existe',
                        data: getValidateNomSerie
                    },
                    regexp: {
                        regexp: /^[^<>]+$/,
                        message: "El campo no admite caracteres especiales"
                    }
                }
            },

            /* fueBibliografica: {
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
            idMotivo: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    }
                }
            }
        },
        submitButtons: 'button#guardarSerie',
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
                processForm('formSerieAsoc', '/Document-Manager/series');
            }
        }
    });


    //Resetear el modal de Adicionar / Editar
    function limpiarForm() {
        $('#formSerieAsoc').data('bootstrapValidator').resetForm(true);
        $('#codSerie').val("");
        $('#nomSerie').val("");
        $('#actAdministrativo').html("");
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
        $('#idMotivo').val(null);
        $("select#idMotivo").trigger("liszt:updated");
        $('#filtroSubseccion').val("");
        $('#asignacionTable').DataTable().rows().remove().draw(false);
        $('#ideSerie').val("");
        $("#filtrosTabla").find("option").removeAttr("disabled");
        $("#filtrosTabla").find("option").removeAttr("style");
        $("#filtrosTabla").find("option").removeClass("hidden");
        $("#filtrosTablaSub").find("option").removeAttr("disabled");
        $("#filtrosTablaSub").find("option").removeAttr("style");
        $("#filtrosTablaSub").find("option").removeClass("hidden");
        $('#indUnidadCorcheckboxNo').prop('checked', 'checked');
        $("#usado").html("");
        $('#conPublica').prop("disabled", false);
        $('#conReservada').prop("disabled", false);
        $('#conClasificada').prop("disabled", false);
    }
});

// --- always use this name
function formSucess() {

    $('#modalAddEditSerieDocumental').modal('hide');
    $('#tableSeries').dataTable().fnReloadAjax();

}

// --- always use this name
function formError() {

}