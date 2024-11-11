function Atracacao() {

    var idPanel;
    var mapModule;
    var show = false;
    var taskUpdate;
    var codAtracacao;
    var requestRealtimeGetData;

    var codBercoSelecionado;
    var bercos;

    // campos
    var jpAtracacao;
    var cbBerco;
    var lbInfoSemAtracacao;
    var jpAtracacaoContent;
    var lbAngulo;
    var lbDtAtualizacaoEsq;
    var lbDtAtualizacaoDir;
    var lbDistDir;
    var lbDistEsq;
    var lbVelDir;
    var lbVelEsq;
    var lbEmbarcacaoNome;
    var lbEmbarcacaoImo;
    var jpEmbarcacao;
    var embarcacaoImage;
    var lbStatus;

    // playback
    var embarcacao;
    var coordenadasBerco;

    initialize = function () {

        $(idPanel).draggable();

        jpAtracacao = $("#atracacao-laser");
        cbBerco = $("#cb-berco");
        lbInfoSemAtracacao = $("#info-sem-atracacao");
        jpAtracacaoContent = $("#atracacao-content");
        lbAngulo = $("#angulo");
        lbDtAtualizacaoEsq = $("#dt-atualizacao-esq");
        lbDtAtualizacaoDir = $("#dt-atualizacao-dir");
        lbDistDir = $("#dist-dir");
        lbDistEsq = $("#dist-esq");
        lbVelDir = $("#vel-dir");
        lbVelEsq = $("#vel-esq");
        lbEmbarcacaoNome = $("#vessel-name");
        lbEmbarcacaoImo = $("#imo");
        jpEmbarcacao = $("#embarcacao");
        embarcacaoImage = $("#imagem-embarcacao");
        lbStatus = $("#status");

        setSemManobra();

    };

    this.realtime = function (id, map) {

        idPanel = id;
        mapModule = map;

        initialize();
        loadBercos();
        $(idPanel).hide();

        addButtonToMap();

        var url = new URL(window.location.href);
        codAtracacao = url.searchParams.get("codAtracacao");

        if (codAtracacao) {

            var token = JSON.parse(localStorage.getItem('user_logged_in')).token;

            $.ajax({
                type: 'GET',
                url: "/sismar/api/mooring/" + codAtracacao,
                async: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", "Bearer " + token);
                },
                success: function (response) {
                    if (!response.error) {
                        var berco = response.berco;
                        showPanelAtracacao(berco.cod);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                },
                dataType: 'json'
            });

        }

    };

    this.playback = function (id) {

        idPanel = id;

        initialize();

        var url = new URL(window.location.href);
        codAtracacao = url.searchParams.get("codAtracacao");

        if (!codAtracacao) {
            return;
        }

        var token = JSON.parse(localStorage.getItem('user_logged_in')).token;

        $.ajax({
            type: 'GET',
            url: "/sismar/api/mooring/" + codAtracacao,
            async: false,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            success: function (response) {
                if (!response.error) {

                    var berco = response.berco;
                    cbBerco.append($('<option>', {
                        value: berco.cod,
                        text: berco.nome
                    }));

                    if (berco.latitude && berco.longitude) {
                        coordenadasBerco = [berco.latitude, berco.longitude];
                    }

                    embarcacao = response.embarcacao;

                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            },
            dataType: 'json'
        });

        $(idPanel).show();

    };

    showPanelAtracacao = function (codBercoForce) {

        if (show) {
            show = !show;
            codAtracacao = null;
            clearInterval(taskUpdate);
            $(idPanel).hide();

        } else {

            if (codBercoForce) {
                cbBerco.val(codBercoForce);
                taskUpdate = setInterval(updateDataAtracacao, 1000);
                show = !show;
                return;
            }

            show = !show;

            if (coordenadasBerco) {
                mapModule.setLatLngAndZoom(coordenadasBerco[0], coordenadasBerco[1], 15);
            }

            var token = JSON.parse(localStorage.getItem('user_logged_in')).token;

            $.ajax({
                type: 'GET',
                url: '/sismar/api/bercos/operando',
                async: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", "Bearer " + token);
                },
                success: function (response) {
                    if (!response.error) {
                        var bercosOperando = response.bercos;
                        if (bercosOperando.length > 0) {
                            cbBerco.val(bercosOperando[0].cod);
                        }
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                },
                dataType: 'json'
            });

            taskUpdate = setInterval(updateDataAtracacao, 1000);

        }

    };

    loadBercos = function () {

        var token = JSON.parse(localStorage.getItem('user_logged_in')).token;

        $.ajax({
            type: 'GET',
            url: '/sismar/api/bercos',
            async: false,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            success: function (response) {
                if (!response.error) {
                    bercos = response.bercos;
                    for (var i = 0, max = bercos.length; i < max; i++) {
                        var berco = bercos[i];
                        cbBerco.append($('<option>', {
                            value: berco.cod,
                            text: berco.nome
                        }));
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
        if (bercos.length > 0) {
            mapModule.addButtonToMap("Atracacoes", "ship.png", showPanelAtracacao);
        }
    };

    updateDataAtracacao = function () {

        var codBerco = cbBerco.children("option:selected").val();
        var token = JSON.parse(localStorage.getItem('user_logged_in')).token;

        if (codBercoSelecionado !== codBerco) {
            for (var i = 0; i < bercos.length; i++) {
                var berco = bercos[i];
                if (berco.cod == codBerco && berco.latitude && berco.longitude) {
                    coordenadasBerco = [berco.latitude, berco.longitude];
                    console.log("change berço");
                    console.log(berco);
                    mapModule.setLatLngAndZoom(berco.latitude, berco.longitude, 15);
                    break;
                }
            }
        }

        codBercoSelecionado = codBerco;
        
        if (requestRealtimeGetData){
            requestRealtimeGetData.abort();
            requestRealtimeGetData = null;
        }

        requestRealtimeGetData = $.ajax({
            type: 'GET',
            url: "/sismar/api/mooring/realtime?codBerco=" + codBerco,
            async: true,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            success: function (response) {
                if (!response.error) {
                    setDataAtracacao(response);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            },
            dataType: 'json'
        });

    };

    this.getCoordenadasBerco = function () {
        return coordenadasBerco;
    };

    this.updateDataAtracacao = function (dataHoraTime) {

        if (!codAtracacao) {
            return;
        }

        var token = JSON.parse(localStorage.getItem('user_logged_in')).token;

        $.ajax({
            type: 'GET',
            url: "/sismar/api/mooring/playback?codAtracacao=" + codAtracacao + "&dataHoraTime=" + dataHoraTime,
            async: true,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            success: function (response) {
                if (!response.error) {
                    setDataAtracacao(response);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            },
            dataType: 'json'
        });

    };

    setDataAtracacao = function (data) {

        if (show || codAtracacao) {
            $(idPanel).show();
        }

        if (data.operando) {
            setOperando(data);
        } else {
            setSemManobra();
        }

    };

    setSemManobra = function (data) {

        jpAtracacaoContent.hide();
        jpAtracacao.removeClass();
        jpAtracacao.addClass("sem-atracacao");
        lbInfoSemAtracacao.show();

    };

    setImagemEmbarcacao = function (imagem) {

        embarcacaoImage.attr("src", imagem ? imagem : "/sismar/faces/javax.faces.resource/img/sem_imagem.png");

    };

    setEmbarcacao = function (data) {

        jpAtracacao.removeClass();

        var emb = data.embarcacao ? data.embarcacao : embarcacao;

        if (emb) {

            lbEmbarcacaoNome.text(emb.nome ? emb.nome : "");
            lbEmbarcacaoImo.text(emb.imo ? emb.imo : "");
            setImagemEmbarcacao(emb.imagem);
            jpEmbarcacao.show();

            jpAtracacao.addClass("com-atracacao-com-embarcacao");

        } else {

            jpEmbarcacao.hide();
            jpAtracacao.addClass("com-atracacao-sem-embarcacao");

        }

    };

    setLadoDireito = function (direito) {

        if (direito) {

            lbDtAtualizacaoDir.html(direito.dataHora ? direito.dataHora : "&nbsp;");
            lbDistDir.html(direito.distancia ? direito.distancia : "&nbsp;");
            lbVelDir.html(direito.velocidade ? direito.velocidade : "&nbsp;");

        } else {

            lbDtAtualizacaoDir.html("&nbsp;");
            lbDistDir.html("&nbsp;");
            lbVelDir.html("&nbsp;");

        }

    };

    setLadoEsquerdo = function (esquerdo) {

        if (esquerdo) {
            
            lbDtAtualizacaoEsq.html(esquerdo.dataHora ? esquerdo.dataHora : "&nbsp;");
            lbDistEsq.html(esquerdo.distancia ? esquerdo.distancia : "&nbsp;");
            lbVelEsq.html(esquerdo.velocidade ? esquerdo.velocidade : "&nbsp;");
            
        } else {
            
            lbDtAtualizacaoEsq.html("&nbsp;");
            lbDistEsq.html("&nbsp;");
            lbVelEsq.html("&nbsp;");
            
        }

    };

    setStatus = function (status, statusClass) {

        lbStatus.text(status);
        lbStatus.removeClass();
        lbStatus.addClass(statusClass);

    };

    setOperando = function (data) {

        setEmbarcacao(data);
        setLadoDireito(data.direito);
        setLadoEsquerdo(data.esquerdo);
        setStatus(data.status, data.statusClass);
        lbAngulo.html(data.angulo ? data.angulo : "&nbsp;");

        jpAtracacaoContent.show();
        lbInfoSemAtracacao.hide();

    };

}
