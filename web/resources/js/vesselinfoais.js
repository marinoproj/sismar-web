function VesselInfoAis() {

    var idPanel;    
    var show = false;
    var taskUpdate;

    var jpVesselInfoAis;

    initialize = function () {

        $(idPanel).draggable();

        jpVesselInfoAis = $("#vesselinfoais-main");
       
    };

    this.realtime = function (id) {

        idPanel = id;
        initialize();
        $(idPanel).hide();

    };

    this.showInfo = function (info) {

        $(idPanel).show();

        jpVesselInfoAis.empty();
        
        jpVesselInfoAis.append(info);

    };
    
    this.closed = function(){
        
        $(idPanel).hide();

        jpVesselInfoAis.empty();
        
    };

}
