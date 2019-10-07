(function ($) {

    $.fn.pickList = function (options) {

        $('.js-example-basic-multiple').select2();

        var opts = $.extend({}, $.fn.pickList.defaults, options);

        this.fill = function () {
            var option = '';

            var tamano = 0;

            $.each(opts.data, function (key, val) {
                option += '<option id="opcionPick" data-id=' + val.value + '>' + val.label + '"</option>';
                 if (val.label.length > tamano) {
                     tamano = val.label.length;
                 }
            });

            console.log(tamano);

            this.find('.pickData').append(option);

            /*
            $(".pickData").sort(sort_li).appendTo('.pickData');
            function sort_li(a, b) {
                return ($(b).data('position')) < ($(a).data('position')) ? 1 : -1;
            }*/
        };

        this.controll = function () {
            var pickThis = this;

            pickThis.find(".pAdd").on('click', function () {
                var p = pickThis.find(".pickData option:selected");
                p.clone().appendTo(pickThis.find(".pickListResult"));
                p.remove();
            });

            pickThis.find(".pAddAll").on('click', function () {
                var p = pickThis.find(".pickData option");
                p.clone().appendTo(pickThis.find(".pickListResult"));
                p.remove();
            });

            pickThis.find(".pRemove").on('click', function () {
                var p = pickThis.find(".pickListResult option:selected");
                p.clone().appendTo(pickThis.find(".pickData"));
                p.remove();

                var options = $('.pickData option');
                var arr = options.map(function(_, o) { return { t: $(o).text(), v: o.value }; }).get();
                arr.sort(function(o1, o2) { return o1.t > o2.t ? 1 : o1.t < o2.t ? -1 : 0; });
                options.each(function(i, o) {
                    o.value = arr[i].v;
                    $(o).text(arr[i].t);
                });
            });

            pickThis.find(".pRemoveAll").on('click', function () {
                var p = pickThis.find(".pickListResult option");
                p.clone().appendTo(pickThis.find(".pickData"));
                p.remove();

                var options = $('.pickData option');
                var arr = options.map(function(_, o) { return { t: $(o).text(), v: o.value }; }).get();
                arr.sort(function(o1, o2) { return o1.t > o2.t ? 1 : o1.t < o2.t ? -1 : 0; });
                options.each(function(i, o) {
                    o.value = arr[i].v;
                    $(o).text(arr[i].t);
                });
            });

            pickThis.find(".pUp").on('click', function () {
                var apuntador = pickThis.find(".pickListResult option:selected").index();
                if (apuntador != 0) {
                    var nuevaPos = apuntador - 1;
                    var listaNueva = [];
                    for (var i = 0; i < $(".pickListResult").children().length; i++) {
                        var choice = $(".pickListResult").children()[i];
                        if (i == nuevaPos) {
                            i++;
                            var temp = $(".pickListResult").children()[i];
                            listaNueva.push(temp);
                        }
                        listaNueva.push(choice);
                    }
                    $(".pickListResult").empty();
                    $(".pickListResult").append(listaNueva);
                }
            });

            pickThis.find(".pUpAll").on('click', function () {
                var apuntador = pickThis.find(".pickListResult option:selected").index();
                if (apuntador != 0) {
                    var listaNueva = [];
                    var temp = pickThis.find(".pickListResult option:selected");
                    for (var i = 0; i < $(".pickListResult").children().length; i++) {
                        var choice = $(".pickListResult").children()[i];
                        if (i != apuntador) {
                            listaNueva.push(choice);
                        }
                    }
                    $(".pickListResult").empty();
                    $(".pickListResult").append(temp);
                    $(".pickListResult").append(listaNueva);
                }
            });

            pickThis.find(".pDown").on('click', function () {
                var apuntador = pickThis.find(".pickListResult option:selected").index();
                var cant = $(".pickListResult").children().length;
                if (apuntador < cant - 1) {
                    var listaNueva = [];
                    for (var i = 0; i < cant; i++) {
                        var choice = $(".pickListResult").children()[i];
                        if (i == apuntador) {
                            i++;
                            var temp = $(".pickListResult").children()[i];
                            listaNueva.push(temp);
                        }
                        listaNueva.push(choice);
                    }
                    $(".pickListResult").empty();
                    $(".pickListResult").append(listaNueva);
                }
            });

            pickThis.find(".pDownAll").on('click', function () {
                var apuntador = pickThis.find(".pickListResult option:selected").index();
                var cant = $(".pickListResult").children().length;
                if (apuntador < cant - 1) {
                    var listaNueva = [];
                    var temp = pickThis.find(".pickListResult option:selected");
                    for (var i = 0; i < cant; i++) {
                        var choice = $(".pickListResult").children()[i];
                        if (i != apuntador) {
                            listaNueva.push(choice);
                        }
                    }
                    $(".pickListResult").empty();
                    $(".pickListResult").append(listaNueva);
                    $(".pickListResult").append(temp);
                }
            });
        }


        this.init = function () {
            var pickListHtml =
                "<div class='row' id='boxPick'>" +
                "  <div class='col-sm-5' style=\"overflow-x: scroll;\">" +
                "	 <select class='js-example-basic-multiple pickData' name=\"states[]\" multiple=\"multiple\" id='Pick' style=\"min-width: 100%;border: unset;\"></select>" +
                " </div>" +
                " <div class='col-sm-1 pickListButtons' align=\"center\">" +
                "  <button id='pAddAll' class='pAddAll' type='button'>" + "<img class='flechas' src= /Document-Manager/resources/soaint-ui/img/double_arrow_right.png no-repeat center center>" + opts.addAll + "</button>" +
                "	<button id='pAdd'  class='pAdd' type='button'>" + "<img class='flechas' src= /Document-Manager/resources/soaint-ui/img/arrow_right.png no-repeat center center>" + opts.add + "</button>" +
                "	<button id='pRemove' class='pRemove' type='button'>" + "<img class='flechas' src= /Document-Manager/resources/soaint-ui/img/arrow_left.png no-repeat center center>" + opts.remove + "</button>" +
                "	<button id='pRemoveAll' class='pRemoveAll' type='button'>" + "<img class='flechas' src= /Document-Manager/resources/soaint-ui/img/double_arrow_left.png no-repeat center center>" + opts.removeAll + "</button>" +
                " </div>" +
                " <div class='col-sm-5' style=\"overflow-x: scroll;\">" +
                "    <select class='js-example-basic-multiple pickListResult' name=\"states[]\" multiple=\"multiple\" id='Pick' style=\"min-width: 100%;border: unset;\"></select>" +
                " </div>" +
                " <div class='col-sm-1 pickListButtons' align=\"center\">" +
                "  <button id='pUpAll' class='pUpAll' type='button'>" + "<img class='flechas' src= /Document-Manager/resources/soaint-ui/img/double_arrow_up.png no-repeat center center>" + opts.upAll + "</button>" +
                "	<button id='pUp' class='pUp' type='button'>" + "<img class='flechas' src= /Document-Manager/resources/soaint-ui/img/arrow_up.png no-repeat center center>" + opts.up + "</button>" +
                "	<button id='pDown' class='pDown' type='button'>" + "<img class='flechas' src= /Document-Manager/resources/soaint-ui/img/arrow_down.png no-repeat center center>" + opts.down + "</button>" +
                "	<button id='pDownAll' class='pDownAll' type='button'>" + "<img class='flechas' src= /Document-Manager/resources/soaint-ui/img/double_arrow_down.png no-repeat center center>" + opts.downAll + "</button>" +
                " </div>" +
                "</div>";

            this.append(pickListHtml);

            this.fill();
            this.controll();
        };

        this.init();
        return this;
    };

    $.fn.getValues = function () {
        var objResult = [];
        this.find(".pickListResult option").each(function () {
            objResult.push({
                id: $(this).data('id'),
                text: this.text
            });
        });
        return objResult;
    };

    $.fn.pickList.defaults = {
        add: '',
        addAll: '',
        remove: '',
        removeAll: '',
        up: '',
        upAll: '',
        down: '',
        downAll: ''
    };


}(jQuery));
