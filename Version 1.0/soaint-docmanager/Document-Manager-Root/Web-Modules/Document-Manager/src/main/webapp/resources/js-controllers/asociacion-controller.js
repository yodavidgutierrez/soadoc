/**
 * Created by malzate on 02/09/2016.
 */

$(document).ready(function() {
    $('.js-example-basic-multiple').select2();
});

$(function () {
    //Cargar las tipologias
    var tipologias = [];
    loadTipologias();

    // ui setup - tables ----------------------
    $("#tableAsociacion").dataTable({
        info: false,
        select: true,
        "ajax": {
            url: "/Document-Manager/asociacion/asociacionList",
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
            {"data":"ideSerie.codSerie"},
            {"data":"ideSerie.nomSerie"},
            {"data":"ideSubserie.codSubserie"},
            {"data": "ideSubserie.nomSubserie"},
            {"data": "ideTpgDoc",
                "render": function (ideTpgDoc, type, data) {
                    var tpg = '';
                    for (var i = 0; i < ideTpgDoc.length; i++) {
                        var choice = ideTpgDoc[i].nomTpgDoc + " </BR> ";
                        tpg += choice;
                    }
                    return tpg;
                }
            }
        ]
    });


    // ui setup ----------------------
    $("#cbxSeries").combobox({id: 'ideSerie', data: '/Document-Manager/util/seriesList', tabindex: 6});
    $("#cbxSubseries").combobox({id: 'ideSubserie', data: '/Document-Manager/util/subSeriesList', tabindex: 6});

    $('#masiveLoadAsociacion').click(function () {
        document.location.href = '/Document-Manager/asociacion-ml'
    });


    // change select option Series
    $("#ideSerie").change(function () {
        debugger;
        loadSubseriesBySerie($(this).val());

        $.ajax({
            type: 'GET',
            url: '/Document-Manager/asociacion/tipDocBySerie/' + $("#ideSerie").val(),
            success: function (response) {
                reiniciarPickList();

                for (var i = 0; i < response.length; i++) {
                    var choice = response[i];
                    $('option[data-id=' + choice.ideTpgDoc + ']').remove();
                    var option = '<option data-id=' + choice.ideTpgDoc + '>' + choice.nomTpgDoc.toUpperCase() + '</option>';
                    $('.pickListResult').append(option);
                }
            }
        });
    });

    // change select option SubSeries
    $("#ideSubserie").change(function () {
        debugger;

        $.ajax({
            type: 'GET',
            url: '/Document-Manager/asociacion/tipDocBySerieAndSubSerie/' + $("#ideSerie").val() + "/" + $(this).val(),
            success: function (response) {
                reiniciarPickList();

                for (var i = 0; i < response.length; i++) {
                    var choice = response[i];
                    $('option[data-id=' + choice.ideTpgDoc + ']').remove();
                    var option = '<option data-id=' + choice.ideTpgDoc + '>' + choice.nomTpgDoc.toUpperCase() + '</option>';
                    $('.pickListResult').append(option);
                }
            }
        });
    });

    // action setup - open modal to add series ----------------------
    $('#addAsociacion').click(function () {
        $('#modalAddEditSerieTittle').html("<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> Asociar Tipo Documental");
        $('#ideSerie').val("");
        $('#ideSerie').attr("disabled", false);
        $('#ideSubserie').val("");
        $('#ideSubserie').attr("disabled", false);
        $('#ideTpgDoc').multiselect("clearSelection");
        reiniciarPickList();
    });

    // action setup - edit series ----------------------
    $('#editAsoc').click(function () {
        debugger;
        //loadSubseriesBySerie("");
        $('#ideTpgDoc').multiselect("clearSelection");
        var tableAsociacion = $('#tableAsociacion').DataTable();
        var rowselected = tableAsociacion.row('.selected');
        if (rowselected.length !== 0) {
            var data = tableAsociacion.row('.selected').data();

            $('#modalAddEditSerieTittle').html("<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> Editar Serie Documental");
            $('#ideSerie').val(data.ideSerie.ideSerie);
            $('#ideSerie').attr("disabled", true);
            $('#ideSubserie').val(data.ideSubserie.ideSubserie);
            $('#ideSubserie').attr("disabled", true);

            if (data.ideSubserie.ideSubserie == null) {
                $.ajax({
                    type: 'GET',
                    url: '/Document-Manager/asociacion/tipDocBySerie/' + $("#ideSerie").val(),
                    success: function (response) {
                        reiniciarPickList();

                        for (var i = 0; i < response.length; i++) {
                            var choice = response[i];
                            $('option[data-id=' + choice.ideTpgDoc + ']').remove();
                            var option = '<option data-id=' + choice.ideTpgDoc + '>' + choice.nomTpgDoc.toUpperCase() + '</option>';
                            $('.pickListResult').append(option);
                        }
                    }
                });
            }else{
                $.ajax({
                    type: 'GET',
                    url: '/Document-Manager/asociacion/tipDocBySerieAndSubSerie/' + $("#ideSerie").val() + "/" + $("#ideSubserie").val(),
                    success: function (response) {
                        reiniciarPickList();

                        for (var i = 0; i < response.length; i++) {
                            var choice = response[i];
                            $('option[data-id=' + choice.ideTpgDoc + ']').remove();
                            var option = '<option data-id=' + choice.ideTpgDoc + '>' + choice.nomTpgDoc.toUpperCase() + '</option>';
                            $('.pickListResult').append(option);
                        }
                    }
                });
            }
            $('#modalAddEditAsociacion').modal('show');
        } else {
            showAlert('errorEdit');
        }
    });

    // action setup - save series ----------------------
    $("#formSerieAsoc").bootstrapValidator({
        live: 'disabled',
        fields: {
            ideSerie: {
                validators: {
                    notEmpty: {
                        message: "El campo Serie es requerido"
                    }
                }
            },

            ideTpgDoc: {
                validators: {
                    notEmpty: {
                        message: "El campo Tipologias es requerido"
                    }
                }
            }
        },
        submitButtons: 'button#guardarSerie',
        submitHandler: function (validator, form) {
            //processForm('formSerieAsoc', '/Document-Manager/asociacion')
            getJsonGuardarAsoc()
        }
    });

    $("#btReportEXCEL").click(function (){

    });

});

function reiniciarPickList(){
    $('.pickData').empty();
    $('.pickListResult').empty();
    for (var i = 0; i < tipologias.length; i++) {
        var choice = tipologias[i];
        var option = '<option data-id=' + choice.value + '>' + choice.label.toUpperCase() + '</option>';
        $('.pickData').append(option);
    }
}

//Genera el Json con los datos para la asociacion
function getJsonGuardarAsoc() {
    var subSerie = $("#ideSubserie").val();
    var serie = $("#ideSerie").val();

    var selected = "";
    $('.pickListResult').children().each(function (index, tipo) {
        var extraSegment = (index == ($('.pickListResult').children().length - 1)) ? "" : ",";
        selected = selected + '{"ideTpgDoc" :' + $(this).attr("data-id") + "}" + extraSegment ;
    });

    var json = "{" + '"ideSerie":' + '"' + serie + '"' + "," + '"ideSubserie":' + '"' + subSerie + '"' + "," + '"ideTpgDoc":[' + selected + "]}";
    sendPostRequest("/Document-Manager/asociacion", json, "");
    console.log(json);

    //var table = $('#tableAsociacion').dataTable();
    //table.fnReloadAjax();
}

//Carga la lista de subseries por el id serie, si idSerie es vacio retorna todas las subSeries
function loadSubseriesBySerie(idSerie) {
    $("#ideSubserie").children().remove();
    $('#ideSubserie').append('<option value="null">--- Seleccione --</option>');

    if (idSerie === "") {
        $.ajax({
            type: 'GET',
            url: '/Document-Manager/util/subSeriesList',
            success: function (response) {
                for (var i = 0; i < response.length; i++) {
                    var choice = response[i];
                    $('#ideSubserie').append('<option value="' + choice.value + '">' + choice.label + '</option>');
                }
            }
        });
    } else {
        $.ajax({
            type: 'GET',
            url: '/Document-Manager/util/subSeriesByIdSerieList/' + idSerie,
            success: function (response) {
                for (var i = 0; i < response.length; i++) {
                    var choice = response[i];
                    $('#ideSubserie').append('<option value="' + choice.value + '">' + choice.label + '</option>');
                }
            }
        });
    }
}

//Carga la lista de tipologias
function loadTipologias(pick) {
    $("#ideSubserie").children().remove();



    $.ajax({
        type: 'GET',
        url: '/Document-Manager/util/tipologiasDocList',
        success: function (response) {
            tipologias = response;
            $("#pickList").pickList({data: response});
        }
    });

}

// --- always use this name
function formSucess() {

    $('#modalAddEditAsociacion').modal('hide');
    //tableAsociacion.fnReloadAjax();
    $('#tableAsociacion').dataTable().fnReloadAjax();
//    $("#formSerieAsoc").data('bootstrapValidator').resetForm(true);

}

// --- always use this name
function formError() {

}