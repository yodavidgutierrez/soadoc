/**
 * Created by jrodriguez on 02/09/2016.
 */

$(document).ready(function() {

    // ui setup - tables ----------------------
    $("#tableDisposiciones").dataTable({
        info: false,
        select: true,
        "ajax": {
            url: "/Document-Manager/disposicionFinal/disposicionFinalList",
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
            {"data": "nomDisFinal"},
            {"data": "desDisFinal"},
            {"data": "ideUuid"}
        ]
    });

    function limpiarForm() {
        $("#formDisposiciones").data('bootstrapValidator').resetForm(true);
        $('#desDisFinal').val("");
        $('#ideDisFinal').val("");
        $('#nomDisFinal').val("");
        $('#staDisFinalcheckboxActivo').prop('checked', 'checked');
    };

    $('#btmAgrDisposiciones').click(function () {
        limpiarForm();
        $('#modalAddEditDisposicionesTitle').html("<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> Crear Disposici&oacute;nes Finales");

    });

    $('#btmEditDisposiciones').click(function () {
        var tableDisposiciones = $('#tableDisposiciones').DataTable();
        var rowselected = tableDisposiciones.row('.selected');
        if (rowselected.length != 0) {
            var data = tableDisposiciones.row('.selected').data();
            $('#ideDisFinal').val(data.ideDisFinal);
            $('#modalAddEditDisposicionesTitle').html("<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span> Editar Disposici&oacute;nes Finales");
            $('#desDisFinal').val(data.desDisFinal);
            $('#nomDisFinal').val(data.nomDisFinal);
            if (data.staDisFinal === '1') {
                $("#staDisFinalcheckboxActivo").prop('checked', 'checked');
            } else {
                $("#staDisFinalcheckboxActivoFalse").prop('checked', 'checked');
            }
            $('#modalAddEditDisposiciones').modal('show');
            /*  }
             }
             });*/
        } else {
            showAlert('errorEdit');
        }
    });

    // action setup - save tipologias ----------------------
    $("#formDisposiciones").bootstrapValidator({
        live: 'disabled',
        fields: {
            desDisFinal: {
                validators: {
                    stringLength: {
                        min: 1,
                        max: 100,
                        message: 'El nombre debe ser menor que 100 caracteres'
                    },
                    notEmpty: {
                        message: "El campo es requerido"
                    },
                    remote: {
                        url: '/Document-Manager/disposicionFinal/validateDesDisDisposiciones',
                        message: 'El nombre insertado ya existe.',
                        data: getValidateDesSerie
                    }

                }
            },

            nomDisFinal: {
                validators: {
                    notEmpty: {
                        message: "El campo es requerido"
                    },

                    remote: {
                        url: '/Document-Manager/disposicionFinal/validateNomDisDisposiciones',
                        message: 'La sigla insertada ya existe.',
                        data: getValidateNomSerie
                    }
                }
            },
        },
        submitButtons: 'button#guardarDisposiciones',
        submitHandler: function (validator, form) {
            processForm('formDisposiciones', '/Document-Manager/disposicionFinal')
            $('#tableDisposiciones').dataTable().fnReloadAjax();
        }
    });
});


//funcion para validar que el nombre no este repetido en bd
getValidateNomSerie = function () {
    var ideDisFinal =  $('#ideDisFinal').val();
    var nomDisFinal =  $('#nomDisFinal').val();
    return {"nomDisFinal": nomDisFinal, "ideDisFinal": ideDisFinal};
};

getValidateDesSerie = function () {
    var desDisFinal = $('#desDisFinal').val();
    var ideDisFinal =  $('#ideDisFinal').val();
    return {"ideDisFinal": ideDisFinal, "desDisFinal": desDisFinal};
};
    
function formSucess() {
    $('#modalAddEditDisposiciones').modal('hide');
    $('#tableDisposiciones').dataTable().fnReloadAjax();
    $("#formDisposiciones").data('bootstrapValidator').resetForm(true);
    
}

// --- always use this name
function formError() {
}
