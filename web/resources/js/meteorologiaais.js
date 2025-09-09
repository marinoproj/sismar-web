// ES Module
export default class MeteorologiaAis {
  
  API_URL;
  
  idPanel = null;
  mapModule = null;
  show = false;
  taskUpdate = null;
  equipaments = [];

  jpMeteorologiaAis = null;
  cbVento = null;
  cbCorrente = null;

  lbVentoVel = null;
  lbVentoDir = null;
  lbCorrenteVel = null;
  lbCorrenteDir = null;

  lbCorrenteStatus = null;
  lbVentoStatus = null;

  constructor({
    apiUrl = "/"
  }) {
    this.API_URL = apiUrl;
  }

  init(id, map) {
    this.idPanel = id;
    this.mapModule = map;

    this.initializeDom();
    this.loadEquipaments();
    $(this.idPanel).hide();
    this.addButtonToMap();
  }

  stop() {
    if (this.taskUpdate) {
      clearInterval(this.taskUpdate);
      this.taskUpdate = null;
    }
  }

  togglePanel() {
    this.showPanelMeteorologia();
  }

  initializeDom() {
    $(this.idPanel).draggable();

    this.jpMeteorologiaAis = $("#meteorologiaais-main");

    this.lbVentoVel = $("#card-group-value-vento-vel");
    this.lbVentoDir = $("#card-group-value-vento-dir");

    this.lbCorrenteVel = $("#card-group-value-corrente-vel");
    this.lbCorrenteDir = $("#card-group-value-corrente-dir");

    this.cbVento = $("#cb-vento");
    this.cbCorrente = $("#cb-corrente");

    this.lbVentoStatus = $("#card-group-vento-status");
    this.lbCorrenteStatus = $("#card-group-corrente-status");

    this.cbVento.on("change", () => {
      this.updateDataMeteorologia();
    });

    this.cbCorrente.on("change", () => {
      this.updateDataMeteorologia();
    });

    L.DomEvent.disableClickPropagation(document.getElementById("meteorologiaais-main"));
    L.DomEvent.disableScrollPropagation(document.getElementById("meteorologiaais-main"));
  }

  getToken() {
    try {
      return JSON.parse(localStorage.getItem("user_logged_in"))?.token || "";
    } catch {
      return "";
    }
  }

  showPanelMeteorologia() {
    if (this.show) {
      this.show = false;
      this.stop();
      $(this.idPanel).hide();
      return;
    }

    this.show = true;
    $(this.idPanel).show();

    const token = this.getToken();

    $.ajax({
      type: "GET",
      url: this.API_URL + "/api/meteorologia",
      async: false,
      beforeSend: (xhr) => {
        xhr.setRequestHeader("Authorization", "Bearer " + token);
      },
      success: (response) => {
        if (!response?.error) {
          this.setDataMeteorologia(response);
        }
      },
      error: () => {},
      dataType: "json",
    });

    this.taskUpdate = setInterval(() => this.updateDataMeteorologia(), 30000);
  }

  loadEquipaments() {
    const token = this.getToken();

    $.ajax({
      type: "GET",
      url: this.API_URL + "/api/equipaments",
      async: false,
      beforeSend: (xhr) => {
        xhr.setRequestHeader("Authorization", "Bearer " + token);
      },
      success: (response) => {
        if (!response?.error) {
          this.equipaments = response.equipamentos || [];

          // limpa selects antes de preencher
          this.cbVento.empty();
          this.cbCorrente.empty();

          for (let equip of this.equipaments) {
            const opt = $("<option>", { value: equip.cod, text: equip.nome });

            if (equip.tipo === "vento") {
              this.cbVento.append(opt);
            } else if (equip.tipo === "corrente") {
              this.cbCorrente.append(opt.clone());
            }
          }

          // seleciona primeira opção por padrão (se houver)
          if (this.cbVento.children().length && !this.cbVento.val()) {
            this.cbVento.prop("selectedIndex", 0);
          }
          if (this.cbCorrente.children().length && !this.cbCorrente.val()) {
            this.cbCorrente.prop("selectedIndex", 0);
          }
        }
      },
      error: (xhr, textStatus, errorThrown) => {
        console.log(errorThrown);
      },
      dataType: "json",
    });
  }

  addButtonToMap() {
    if (this.equipaments.length > 0 && this.mapModule?.addButtonToMap) {
      this.mapModule.addButtonToMap("Meteorologia", "wind.png", () =>
        this.showPanelMeteorologia()
      );
    }
  }

  updateDataMeteorologia() {
    const token = this.getToken();

    $.ajax({
      type: "GET",
      url: this.API_URL + "/api/meteorologia",
      async: true,
      beforeSend: (xhr) => {
        xhr.setRequestHeader("Authorization", "Bearer " + token);
      },
      success: (response) => {
        if (!response?.error) {
          this.setDataMeteorologia(response);
        }
      },
      error: () => {},
      dataType: "json",
    });
  }

  setDataMeteorologia(data) {
    const codEquipVento =
      this.cbVento.children("option:selected").val() ||
      this.cbVento.children().first().val();
    const codEquipCorr =
      this.cbCorrente.children("option:selected").val() ||
      this.cbCorrente.children().first().val();

    const lista = data?.meteorologia || [];
    for (let m of lista) {
      if (String(m.codEquipamento) === String(codEquipVento)) {
        this.lbVentoVel.html(m.vel);
        this.lbVentoDir.html(m.dir);
        this.lbVentoStatus.html(m.status);
        this.lbVentoStatus.removeClass().addClass("message-status-" + m.status);
      } else if (String(m.codEquipamento) === String(codEquipCorr)) {
        this.lbCorrenteVel.html(m.vel);
        this.lbCorrenteDir.html(m.dir);
        this.lbCorrenteStatus.html(m.status);
        this.lbCorrenteStatus
          .removeClass()
          .addClass("message-status-" + m.status);
      }
    }
  }
}
