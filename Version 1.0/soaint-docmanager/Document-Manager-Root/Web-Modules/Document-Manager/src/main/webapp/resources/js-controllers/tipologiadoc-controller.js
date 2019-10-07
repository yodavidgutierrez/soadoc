/**
 * Created by jrodriguez on 22/09/2016.
 */

$(function () {

    // ui setup - tables ----------------------
    $("#tableTipologiasDoc").dataTable({
        info: false,
        select: true,
        "ajax": {
            url: "/Document-Manager/tipologiasdoc/tipologiadocumentalList",
            type: "GET"
        },
        "columnDefs": [
            {
                "visible": false,
                "searchable": false
            }
        ],
        "order": [[0, "asc"]],
        "columns": [
            {"data": "nomTpgDoc"},
            {"data": "estTpgDoc"},
            {"data": "nomTraDocumental"},
            {"data": "nomSoport"},
            {"data": "caracteristicas"}
        ]
    });

    //Download plantilla excel -----------
    $('#btnDescargarTipologia').click(function () {
        window.location.href = '/Document-Manager/tipologiasdoc/downloadExcel';
    });

    //Download report excel
    $('#exportarExcel').click(function () {
        window.location.href= '/Document-Manager/tipologiasdoc/generateExcel';
    });

    // ui setup ----------------------
    $("#cbxMotivos").combobox({id: 'idMotivo', data: '/Document-Manager/util/motivosCreacionList', tabindex: 6});
    $("#idMotivo").chosen();
    $("#cbxSoportes").combobox({id: 'idSoporte', data: '/Document-Manager/util/soportesList', tabindex: 6});
    $("#idSoporte").chosen();
    $("#cbxTradiciones").combobox({id: 'idTraDocumental', data: '/Document-Manager/util/tradicionDocList', tabindex: 6});
    $("#idTraDocumental").chosen();
    $("#chboxCaracteristicas").checkbox({data: '/Document-Manager/util/caracteristicasListTipo'});

    // action setup - masive load ----------------------
    $('#masiveLoadTipologia').click(function () {
        document.location.href = '/Document-Manager/tipologiasdoc-ml'
    });



    // action setup - open modal to add tipologias ----------------------

    $('#btmAddTipologia').click(function () {
        limpiarForm();
        $('#modalAddEditTipologiasTittle').html("<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> Crear Tipos Documentales");
        $('#ideTpgDoc').val("");
        $('#estTpgDoccheckboxActivoFalse').attr("disabled", true);
        $("#estTpgDoccheckboxActivo").prop('checked', 'checked');
        $('#nomTpgDoc').prop("readonly", false);


    });

    //funcion para validar que el nombre no este repetido en bd
    getValidatNomTpcDoc = function () {
        var nomTpgDoc = $('#nomTpgDoc').val();
        var ideTpgDoc = $('#ideTpgDoc').val();
        return {"nomTpgDoc": nomTpgDoc, "ideTpgDoc": ideTpgDoc};
    };
    // action setup - save tipologias ----------------------
    $("#formTipologias").bootstrapValidator({
        live: 'disabled',
        fields: {
            nomTpgDoc: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    }, remote: {
                        url: '/Document-Manager/tipologiasdoc/validateNomTpcDoc',
                        message: 'El nombre insertado ya existe',
                        data: getValidatNomTpcDoc
                    },
                    stringLength: {
                        min: 1,
                        max: 255,
                        message: 'El c&oacute;digo debe ser menor que 255 caracteres'
                    }
                }
            },
            /* notAlcance: {
                 validators: {
                     notEmpty: {
                         message: "El campo es requerido"
                     }
                 }
             },
             fueBibliografica: {
                 validators: {
                     notEmpty: {
                         message: "El campo es requerido"
                     }
                 }
             },*/
            idSoporte: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    }
                }
            }
        },
        submitButtons: 'button#guardarTipologias',
        submitHandler: function (validator, form) {

                var caract = false;

                if ($("#carAdministrativa").prop('checked') ||  $("#carContable").prop('checked') || $("#carJuridico").prop('checked')
                    || $("#carLegal").prop('checked') || $("#carTecnica").prop('checked')   ) {
                    caract = true;
                } else {
                    showAlert('caracteristicaSelect');
                }

                if (caract) {
                    processForm('formTipologias', '/Document-Manager/tipologiasdoc');
                    var x = $('#tableSeries').dataTable();
                    x.fnReloadAjax();
                }



/*
            if ($("#carTecnica").is(':checked') || $("#carLegal").is(':checked') || $("#carAdministrativa").is(':checked') || $("#carJuridico").is(':checked') || $("#carContable").is(':checked')) {
                // Code in the case checkbox is checked.
                processForm('formTipologias', '/Document-Manager/tipologiasdoc');
                var x = $('#tableSeries').dataTable();
                x.fnReloadAjax();
            } else {
                showAlert('caracteristicaSelect');
            }
*/
        }
    });



    // action setup - edit tipologias ----------------------
    $('#btmEditTipologia').click(function () {
        limpiarForm();
        $('#inputActivoFalse').attr("disabled", false);
        var tableTipologiasDoc = $('#tableTipologiasDoc').DataTable();
        var rowselected = tableTipologiasDoc.row('.selected');
        if (rowselected.length != 0) {
            var data = tableTipologiasDoc.row('.selected').data();
            $('#modalAddEditTipologiasTittle').html("<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> Editar Tipos Documentales");
            $('#estTpgDoccheckboxActivoFalse').prop("disabled", false);
            $('#ideTpgDoc').val(data.ideTpgDoc);
            $('#nomTpgDoc').val(data.nomTpgDoc);
            $('#nomTpgDoc').prop("readonly", true);
            $('#idSoporte').val(data.idSoporte);
            $("select#idSoporte").trigger("liszt:updated");
            $('#idTraDocumental').val(data.idTraDocumental);
            $("select#idTraDocumental").trigger("liszt:updated");
            $('#notAlcance').val(data.notAlcance);
            $('#fueBibliografica').val(data.fueBibliografica);

            if (data.estadoTpgDocValue === 1) {
                $("#estTpgDoccheckboxActivo").prop('checked', 'checked');
            } else {
                $("#estTpgDoccheckboxActivoFalse").prop('checked', 'checked');
            }
            $('#carAdministrativa').prop('checked', data.carAdministrativa ? true : false);
            $('#carContable').prop('checked', data.carContable ? true : false);
            $('#carJuridico').prop('checked', data.carJuridico ? true : false);
            $('#carLegal').prop('checked', data.carLegal ? true : false);
            $('#carTecnica').prop('checked', data.carTecnica ? true : false);
            $('#tableSeries').DataTable().draw();
            $('#modalAddEditTipologias').modal('show');

        } else {
            showAlert('errorEdit');
        }
    });


    // action setup - show detail tipologia ----------------------

    $('#btmDetailsTipologia').click(function () {
        var tableTipologiasDoc = $('#tableTipologiasDoc').DataTable();
        var rowselected = tableTipologiasDoc.row('.selected');
        if (rowselected.length !== 0) {
            var data = tableTipologiasDoc.row('.selected').data();

            $('#nombreDetail').val(data.nomTpgDoc);
            $('#soporteDetail').val(data.nomSoport);
            $('#tradicionDocDetail').val(data.nomTraDocumental);
            $('#notaDetail').val(data.notAlcance);
            $('#fuenteDetail').val(data.fueBibliografica);

            if ($($("#tableTipologiasDoc tr.selected input").get(0)).prop('checked')) {
                $('#estTpgDocDetail').val("Activo");
            } else {
                $('#estTpgDocDetail').val("Inactivo");
            }
            data.carAdministrativa ? $('#administrativaDetail').removeClass("hidden") : $('#administrativaDetail').addClass("hidden");
            data.carContable ? $('#contableDetail').removeClass("hidden") : $('#contableDetail').addClass("hidden");
            data.carJuridico ? $('#juridicaDetail').removeClass("hidden") : $('#juridicaDetail').addClass("hidden");
            console.log(data.carJuridico)
            data.carLegal ? $('#legalDetail').removeClass("hidden") : $('#legalDetail').addClass("hidden");
            data.carTecnica ? $('#tecnicaDetail').removeClass("hidden") : $('#tecnicaDetail').addClass("hidden");
            $('#tableTipologiasDoc').DataTable().draw();
            $('#digDetailTipologia').modal('show');
            console.log($('#carJuridico'));
        } else {
            showAlert('errorEdit');
        }
    });




    // action setup - remove subseries ----------------------
    $('#btnRemoveTipologia').confirmation({
        title: "¿Está seguro de realizar esta acción?",
        singleton: true,
        popout: true,
        placement: "left",
        btnOkLabel: "Eliminar",
        btnCancelLabel: "Cancelar",
        onConfirm: function (e) {
            e.preventDefault();
            var tableTipologiasDoc = $('#tableTipologiasDoc').DataTable();
            var data = tableTipologiasDoc.row('.selected').data();
            var rowselected = tableTipologiasDoc.row('.selected');
            if (rowselected.length != 0) {
                sendDeleteRequest('/Document-Manager/tipologiasdoc/removetpgdoc/' + data.ideTpgDoc);
                return false;
            } else {
                showAlert('errorEdit');
            }
        }
    });
    //Resetear el modal de Adicionar / Editar
    function limpiarForm() {
        $('#formTipologias').data('bootstrapValidator').resetForm(true);
        $('#ideTpgDoc').val("");
        $('#nomTpgDoc').val("");
        $('#idSoporte').val("");
        $('#idTraDocumental').val("");
        $('#notAlcance').val("");
        $('#idSoporte').val(null);
        $("select#idSoporte").trigger("liszt:updated");
        $('#idTraDocumental').val(null);
        $("select#idTraDocumental").trigger("liszt:updated");
        $('#fueBibliografica').val("");
        $('#carLegal').prop('checked', false);
        $('#carTecnica').prop('checked', false);
        $('#carAdministrativa').prop('checked', false);
        $('#carJuridico').prop('checked', false);
        $('#carContable').prop('checked', false);
        $('#filtroSeccion').val("");
        $('#filtroSubseccion').val("");
        $('#asignacionTable').DataTable().rows().remove().draw(false);
        $("#usado").html("");
    }
    ;
});


// --- always use this name
function formSucess() {

    $('#modalAddEditTipologias').modal('hide');
    $('#tableTipologiasDoc').dataTable().fnReloadAjax();
    $("#formTipologias").data('bootstrapValidator').resetForm(true);
}

// --- always use this name
function formError() {
}
