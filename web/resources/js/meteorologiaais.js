function MeteorologiaAis() {

    var idPanel;
    var mapModule;
    var show = false;
    var taskUpdate;

    var equipaments;

    var jpMeteorologiaAis;
    var cbVento;
    var cbCorrente;

    var lbVentoVel;
    var lbVentoDir;

    var lbCorrenteVel;
    var lbCorrenteDir;
    
    var lbCorrenteStatus;
    var lbVentoStatus;

    initialize = function () {

        $(idPanel).draggable();

        jpMeteorologiaAis = $("#meteorologiaais-main");

        lbVentoVel = $("#card-group-value-vento-vel");
        lbVentoDir = $("#card-group-value-vento-dir");

        lbCorrenteVel = $("#card-group-value-corrente-vel");
        lbCorrenteDir = $("#card-group-value-corrente-dir");

        cbVento = $("#cb-vento");
        cbCorrente = $("#cb-corrente");
        
        lbVentoStatus = $("#card-group-vento-status");
        lbCorrenteStatus = $("#card-group-corrente-status");

    };

    this.realtime = function (id, map) {

        idPanel = id;
        mapModule = map;

        initialize();
        loadEquipaments();
        $(idPanel).hide();

        addButtonToMap();

    };

    showPanelMeteorologia = function (codBercoForce) {

        if (show) {

            show = !show;
            clearInterval(taskUpdate);
            $(idPanel).hide();

        } else {

            show = !show;
            $(idPanel).show();

            var token = JSON.parse(localStorage.getItem('user_logged_in')).token;

            $.ajax({
                type: 'GET',
                url: "/sismar/api/meteorologia",
                async: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", "Bearer " + token);
                },
                success: function (response) {
                    if (!response.error) {
                        setDataMeteorologia(response);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                },
                dataType: 'json'
            });

            taskUpdate = setInterval(updateDataMeteorologia, 3000);

        }

    };

    loadEquipaments = function () {

        var token = JSON.parse(localStorage.getItem('user_logged_in')).token;

        $.ajax({
            type: 'GET',
            url: '/sismar/api/equipaments',
            async: false,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            success: function (response) {
                if (!response.error) {
                    equipaments = response.equipamentos;

                    for (var i = 0, max = equipaments.length; i < max; i++) {
                        var equipament = equipaments[i];
                        if (equipament.tipo === "vento") {
                            cbVento.append($('<option>', {
                                value: equipament.cod,
                                text: equipament.nome
                            }));
                        } else if (equipament.tipo === "corrente") {
                            cbCorrente.append($('<option>', {
                                value: equipament.cod,
                                text: equipament.nome
                            }));
                        }
                    }

                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                console.log(errorThrown);
            },
            dataType: 'json'
        });

    };

    addButtonToMap = function () {
        if (equipaments.length > 0) {
            mapModule.addButtonToMap("Meteorologia", "wind.png", showPanelMeteorologia);
        }
    };

    updateDataMeteorologia = function () {

        var token = JSON.parse(localStorage.getItem('user_logged_in')).token;

        $.ajax({
            type: 'GET',
            url: "/sismar/api/meteorologia",
            async: true,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            success: function (response) {
                if (!response.error) {
                    setDataMeteorologia(response);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            },
            dataType: 'json'
        });

    };

    setDataMeteorologia = function (data) {

        var codEquipamentVento = cbVento.children("option:selected").val();

        var codEquipamentCorrente = cbCorrente.children("option:selected").val();

        for (var i = 0, max = data.meteorologia.length; i < max; i++) {
            var meteorologia = data.meteorologia[i];
            if (meteorologia.codEquipamento == codEquipamentVento) {
                lbVentoVel.html(meteorologia.vel);
                lbVentoDir.html(meteorologia.dir);
                lbVentoStatus.html(meteorologia.status);
                lbVentoStatus.removeClass();
                lbVentoStatus.addClass("message-status-" + meteorologia.status);
                
            } else if (meteorologia.codEquipamento == codEquipamentCorrente) {
                lbCorrenteVel.html(meteorologia.vel);
                lbCorrenteDir.html(meteorologia.dir);
                lbCorrenteStatus.html(meteorologia.status);
                lbCorrenteStatus.removeClass();
                lbCorrenteStatus.addClass("message-status-" + meteorologia.status);                
                
            }
        }

    };

}
