// ES Module
export default class VesselInfoAis {
  API_URL;

  constructor({ containerId, apiUrl = "/" }) {
    this.idPanel = containerId;
    this.API_URL = apiUrl;

    this.show = false;
    this.taskUpdate = null;
    $(this.idPanel).draggable();
    this.jpVesselInfoAis = $(this.idPanel);
    $(this.idPanel).hide();

    L.DomEvent.disableClickPropagation(document.getElementById("vesselinfoais-main"));
    L.DomEvent.disableScrollPropagation(document.getElementById("vesselinfoais-main"));

  }

  getContentLoading() {
    var content =
      '<div style="width: 400px;" >' +
      '<div class="col-xs-12 info-main-weather not-padding" style="font-weight: bold;">' +
      '<img src="/sismar/faces/javax.faces.resource/img/icone_loader.gif" style="height: 20px; margin-right: 10px;"> Carregando as informações, aguarde ...' +
      "</div>" +
      "</div>";
    return content;
  }

  destroyPopupLoading(popup, map) {
    if (!popup) return;
    popup.closePopup();
    popup.off?.();
    popup.unbindPopup?.();
    popup.unbindTooltip?.();
    map.removeLayer(popup);
    popup.remove?.();
  }

  getToken() {
    try {
      return JSON.parse(localStorage.getItem("user_logged_in"))?.token || "";
    } catch {
      return "";
    }
  }

  showInfo(vesselData, layer) {
    const token = this.getToken();
        
    $.ajax({
      type: "GET",
      url: this.API_URL + "/api/vessel",
      data: "mmsi=" + vesselData.mmsi,
      beforeSend: (xhr) => {
        xhr.setRequestHeader("Authorization", "Bearer " + token);
      },
      success: (response) => {
        this.setContentVesselSuccess(response);
        this.rewriteUrlParams(null, [{ key: "mmsi", value: vesselData.mmsi }], false);
      },
      error: (XMLHttpRequest, textStatus, errorThrown) => {
        var content = this.getContentError();
        layer.bindPopup(content, {className: "leaflet_popup_loading"});
        layer.openPopup();
      },
      dataType: "json",
    });
  }

  getContentError() {
    var content =
      '<div style="width: 400px;" >' +
      '<div class="col-xs-12 info-main-weather not-padding" style="font-weight: bold; color:red;">' +
      "Não foi possível buscar as informações! " +
      "</div>" +
      "</div>";
    return content;
  }

  rewriteUrlParams(urlDefault, paramsUrl, redirect) {
    var host =
      window.location.protocol +
      "//" +
      window.location.hostname +
      ":" +
      window.location.port +
      "/faces";

    var url = new URL(urlDefault ? host + urlDefault : window.location.href);

    var search = url.search;
    var params = new URLSearchParams(search);

    // deleta todos os parâmetros
    var keys = [...params.keys()];
    for (var i in keys) {
      params.delete(keys[i]);
    }

    // adiciona os novos parâmetros
    for (var i = 0; i < paramsUrl.length; i++) {
      var paramNew = paramsUrl[i];
      params.append(paramNew.key, paramNew.value);
    }

    url.search = params.toString();
    var newUrl = url.toString();

    if (redirect) {
      window.location.href = newUrl;
    } else {
      window.history.pushState("", "", newUrl);
    }
  }

  setContentVesselSuccess(response) {
    var content = "";

    if (!this.isNullValueJson(response.vessel)) {
      content = this.getContentVessel(response);
    } else {
      content = this.getContentVesselUnknown(response);
    }

    $(this.idPanel).show();
    this.jpVesselInfoAis.empty();
    this.jpVesselInfoAis.append(content);
    $("#close-vessel-info").on("click", () => this.closed());

  }

  isNullValueJson(value) {
    if (value === undefined || value === null || value === "") {
      return true;
    }
    return false;
  }

  getValueFromContent(
    value,
    unity = "",
    replaceIfNullOrEmpty = false,
    valueIfNullOrEmpty = ""
  ) {
    if (this.isNullValueJson(value)) {
      if (!replaceIfNullOrEmpty) {
        return "&nbsp;";
      } else {
        return valueIfNullOrEmpty;
      }
    }
    return value + unity;
  }

  getContentVessel(response) {
    var functionalities = JSON.parse(
      localStorage.getItem("user_logged_in")
    ).functionalities;

    var content =
      '<div class="container-popup-vessel">' +
      '<div class="row" style="padding: 0px !important;">' +
      '<div class="container-popup-title col-xs-12 col-md-12">' +
      '<div class="dropdown" style="float: left;">' +
      '<button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
      '<i class="glyphicon glyphicon-menu-hamburger"></i></button>' +
      '<ul class="dropdown-menu">';

    if (functionalities.indexOf("ais_track") > -1) {
      content +=
        "<li>" +
        '<a class="button-open-period-track" href="#" class="ui-commandlink ui-widget" onclick="$(\'#form-select-period-track input[name=mmsi]\').val(' +
        response.last_ais_record.mmsi +
        "); PF('dialog-select-period-track').show(); return false;\">" +
        '<i class="glyphicon glyphicon-search"></i>' +
        "Track (Rastro)" +
        "</a>" +
        "</li>";
    }

    content +=
      "</ul>" +
      "</div>" +
      '<div class="dropdown" style="float: right;">' +
      '<button id="close-vessel-info" class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
      '<i class="glyphicon glyphicon-remove-circle"></i></button>' +
      "</div>" +
      '<p class="container-popup-name">' +
      this.getValueFromContent(response.vessel.name, "", true, "DESCONHECIDO") +
      "</p>" +
      '<p class="container-popup-type" style="padding-left: 30px;">' +
      "<b>IMO:</b> " +
      this.getValueFromContent(response.vessel.imo) +
      " " +
      "<b>Categoria:</b> " +
      this.getValueFromContent(response.vessel.type) +
      "</p>" +
      "</div>" +
      "</div>" +
      '<div class="row" style="padding: 0px !important;">' +
      '<div class="container-popup-img col-xs-12 col-md-12">' +
      (this.isNullValueJson(response.vessel.image)
        ? '<img id="image-vessel-popup" src="/sismar/faces/javax.faces.resource/img/sem_imagem.png" width="100%" height="200px"></img>'
        : '<img id="image-vessel-popup" src="' +
          response.vessel.image +
          '" width="100%" height="200px"></img>') +
      "</div>" +
      "</div>" +
      '<div class="row" style="padding: 0px !important;">' +
      '<div class="container-popup-info col-xs-12 col-md-12" style="padding: 0px !important;">' +
      '<div class="row">' +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Dimensão:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.vessel.dimension) +
      "</p>" +
      "</div>" +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Velocidade:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.velocity, " kn") +
      "</p>" +
      "</div>" +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Curso:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.direction, "º") +
      "</p>" +
      "</div>" +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Calado:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.draught, "m") +
      "</p>" +
      "</div>" +
      "</div>" +
      '<div class="row">' +
      '<div class="container-popup-info-card col-xs-5 col-md-5">' +
      '<p class="container-popup-info-card-title">Destino:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.destination) +
      "</p>" +
      "</div>" +
      '<div class="container-popup-info-card col-xs-4 col-md-4">' +
      '<p class="container-popup-info-card-title">Estado:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.state) +
      "</p>" +
      "</div>" +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Call Sign:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.vessel.call_sign) +
      "</p>" +
      "</div>" +
      "</div>" +
      "</div>" +
      "</div>" +
      '<div class="row" style="padding: 0px !important;">' +
      '<div class="container-popup-footer col-xs-12 col-md-12">' +
      "<p><b>Recebido:</b> " +
      this.getValueFromContent(response.last_ais_record.message) +
      "</p>" +
      '<p style="display: flex; padding-top: 10px; padding-bottom: 5px;"><b>Latitude:</b><span style="line-height: 1.5;margin-top: -8px;padding-left: 5px;padding-right: 10px;"> ' +
      this.getValueFromContent(response.last_ais_record.lat_graus) +
      " <br>" +
      this.getValueFromContent(response.last_ais_record.lat) +
      '</span><b>Longitude:</b><span style="line-height: 1.5;margin-top: -8px;padding-left: 5px;padding-right: 10px;"> ' +
      this.getValueFromContent(response.last_ais_record.lng_graus) +
      "<br>" +
      this.getValueFromContent(response.last_ais_record.lng) +
      "</span></p>" +
      "<p><b>MMSI:</b> " +
      this.getValueFromContent(response.last_ais_record.mmsi) +
      "</p>" +
      "</div>" +
      "</div>" +
      "</div>";

    return content;
  }

  getContentVesselUnknown(response) {
    var functionalities = JSON.parse(
      localStorage.getItem("user_logged_in")
    ).functionalities;

    var content =
      '<div class="container-popup-vessel">' +
      '<div class="row">' +
      '<div class="container-popup-title col-xs-12 col-md-12">' +
      '<div class="dropdown" style="float: left;">' +
      '<button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
      '<i class="glyphicon glyphicon-menu-hamburger"></i></button>' +
      '<ul class="dropdown-menu">';

    if (functionalities.indexOf("ais_track") > -1) {
      content +=
        "<li>" +
        '<a class="button-open-period-track" href="#" class="ui-commandlink ui-widget" onclick="$(\'#form-select-period-track input[name=mmsi]\').val(' +
        response.last_ais_record.mmsi +
        "); PF('dialog-select-period-track').show(); return false;\">" +
        '<i class="glyphicon glyphicon-search"></i>' +
        "Track (Rastro)" +
        "</a>" +
        "</li>";
    }

    content +=
      "</ul>" +
      "</div>" +
      '<div class="dropdown" style="float: right;">' +
      '<button id="close-vessel-info" class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" style="color: white; padding-left: 0px !important; padding-top: 9px;">' +
      '<i class="glyphicon glyphicon-remove-circle"></i></button>' +
      "</div>" +
      '<p class="container-popup-name">DESCONHECIDO</p>' +
      '<p class="container-popup-type" style="padding-left: 30px;">' +
      "<b>IMO:</b> DESCONHECIDO " +
      "<b>Categoria:</b> DESCONHECIDO" +
      "</p>" +
      "</div>" +
      "</div>" +
      '<div class="row">' +
      '<div class="container-popup-img col-xs-12 col-md-12">' +
      '<img src="/sismar/faces/javax.faces.resource/img/sem_imagem.png" width="100%" height="200px"></img>' +
      "</div>" +
      "</div>" +
      '<div class="row">' +
      '<div class="container-popup-info col-xs-12 col-md-12">' +
      '<div class="row">' +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Dimensão:</p>' +
      '<p class="container-popup-info-card-value">&nbsp;</p>' +
      "</div>" +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Velocidade:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.velocity, " kn") +
      "</p>" +
      "</div>" +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Curso:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.direction, "º") +
      "</p>" +
      "</div>" +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Calado:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.draught, "m") +
      "</p>" +
      "</div>" +
      "</div>" +
      '<div class="row">' +
      '<div class="container-popup-info-card col-xs-5 col-md-5">' +
      '<p class="container-popup-info-card-title">Destino:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.destination) +
      "</p>" +
      "</div>" +
      '<div class="container-popup-info-card col-xs-4 col-md-4">' +
      '<p class="container-popup-info-card-title">Estado:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.last_ais_record.state) +
      "</p>" +
      "</div>" +
      '<div class="container-popup-info-card col-xs-3 col-md-3">' +
      '<p class="container-popup-info-card-title">Call Sign:</p>' +
      '<p class="container-popup-info-card-value">' +
      this.getValueFromContent(response.vessel.call_sign) +
      "</p>" +
      "</div>" +
      "</div>" +
      "</div>" +
      "</div>" +
      '<div class="row">' +
      '<div class="container-popup-footer col-xs-12 col-md-12">' +
      "<p><b>Recebido:</b> " +
      this.getValueFromContent(response.last_ais_record.message) +
      "</p>" +
      '<p style="display: flex;"><b>Latitude:</b><span style="line-height: 1.5;margin-top: -8px;padding-left: 5px;padding-right: 10px;"> ' +
      this.getValueFromContent(response.last_ais_record.lat_graus) +
      " <br>" +
      this.getValueFromContent(response.last_ais_record.lat) +
      '</span><b>Longitude:</b><span style="line-height: 1.5;margin-top: -8px;padding-left: 5px;padding-right: 10px;"> ' +
      this.getValueFromContent(response.last_ais_record.lng_graus) +
      "<br>" +
      this.getValueFromContent(response.last_ais_record.lng) +
      "</span></p>" +
      "<p><b>MMSI:</b> " +
      this.getValueFromContent(response.last_ais_record.mmsi) +
      "</p>" +
      "</div>" +
      "</div>" +
      "</div>";

    return content;
  }

  closed() {
    $(this.idPanel).hide();
    this.jpVesselInfoAis.empty();
    this.rewriteUrlParams(null, [], false);
  }
}
