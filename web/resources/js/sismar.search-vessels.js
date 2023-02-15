/* global PF */

(function ($) {

    $.sismarSearchVessels = function (selectorInput,
            selectorTable,
            selectorButton,
            selectorDialog,
            ais,
            settings) {

        var config = {
        };

        if (settings) {
            $.extend(config, settings);
        }

        openInfoVessel = function (mmsi) {
            ais.openInfoVesselBySearch(mmsi);
            PF(selectorDialog).hide(); 
        };

        search = function (text) {
            
            if (text === undefined || text.trim() === ""){
                return;
            }
            
            $.ajax({
                type: 'GET',
                url: '/sismar/api/vessel/search/' + text,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", "Basic bGVvOjEyMw==");
                    PF('statusdialog').show();
                },
                success: function (response) {
                    table.clear().draw();
                    for (var i = 0; i < response.vessels.length; i++) {
                        var vessel = response.vessels[i];
                        table.row.add({
                            "order": (i + 1),
                            "vessel": "<div style='display: inline-flex;'>\n\
                                <img style='border-radius:8px;' width='80px' height='50px' src='" + (vessel.image === null ? "/sismar/faces/javax.faces.resource/img/sem_imagem.png" : vessel.image) + "'/>\n\
                                <span style='display: inline-block;margin-left:10px;'>\n\
                                    <b style='cursor: pointer;' onclick='openInfoVessel(" + vessel.mmsi + ");'>" + vessel.name + "</b><br>\n\
                                    IMO: " + vessel.imo +
                                    "</span>\n\
                              </div>"
                        }).draw();
                    }                   
                    PF('statusdialog').hide();                    
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    PF('statusdialog').hide();
                    
                },
                dataType: 'json'
            });

        };

        $(selectorButton).click(function () {
            search($(selectorInput).val());
        });

        var table = $(selectorTable).DataTable({
            "columnDefs": [{"targets": "no-sort", "orderable": false},
                {"width": "10px", "targets": 0, "orderable": false}],
            "columns": [
                {"data": "order"},
                {"data": "vessel"}
            ],
            "pageLength": 4,
            "dom": 'Brtip',
            "language": {"url": "/sismar/faces/javax.faces.resource/json/portuguese_brasil.json"}
        });

    };

})(jQuery);