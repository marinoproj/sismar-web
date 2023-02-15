function Alert() {
}
;

Alert.success = function (opt) {
    Alert.modal(opt, "success");
};

Alert.error = function (opt) {
    Alert.modal(opt, "error");
};

Alert.info = function (opt) {
    Alert.modal(opt, "info");
};

Alert.modal = function (opt, type) {

    var s = "<div id=\"mod_alert\">";
    s += "<div id=\"mod_modal\">";
    s += "<div id=\"mod_title\" class=\"" + type + "\">";
    s += "<span>" + opt.title + "</span>";
    s += "<div id=\"mod_close\">X</div>";
    s += "</div>";

    s += "<div id=\"mod_body\">" + opt.message + "</div>";
    s += "</div>";
    s += "</div>";

    $("#mod_alert").remove();

    $("body:last-child").append(s);

    var largura = $("#mod_modal").width() / 2;
    var altura = $("#mod_modal").height() / 2;

    $("#mod_modal").css("margin-left", largura * -1);
    $("#mod_modal").css("margin-top", altura * -1);

    $('#mod_close').unbind();
    $('#mod_alert').unbind();
    $(document).unbind("keyup");

    $('#mod_close').click(function (event) {
        Alert.close(opt.onClose);
    });

    $('#mod_alert').click(function (event) {
        Alert.close(opt.onClose);
    });

    if (opt.timeout !== undefined && opt.timeout !== null) {
        setTimeout(function () {
            Alert.close(opt.onClose);
        }, opt.timeout);
    }

    $(document).keyup(function (e) {
        if (e.keyCode === 27) {
            Alert.close(opt.onClose);
        }
    });

};


Alert.close = function (acao) {
    if ($("#mod_alert").length !== 0) {
        $("#mod_alert").remove();
        if (acao !== undefined && acao !== null) {
            acao();
        }
        $('#mod_close').unbind();
        $('#mod_alert').unbind();
        $(document).unbind("keyup");
    }
};





/*




(function () {

    function setup($) {

        $.alertSuccess = function (opt) {
            $.alertModal(opt, "success");
        };

        $.alertError = function (opt) {
            $.alertModal(opt, "error");
        };

        $.alertInfo = function (opt) {
            $.alertModal(opt, "info");
        };

        $.alertModal = function (opt, type) {

            var s = "<div id=\"mod_alert\">";
            s += "<div id=\"mod_modal\">";
            s += "<div id=\"mod_title\" class=\"" + type + "\">";
            s += "<span>" + opt.title + "</span>";
            s += "<div id=\"mod_close\">X</div>";
            s += "</div>";

            s += "<div id=\"mod_body\">" + opt.message + "</div>";
            s += "</div>";
            s += "</div>";

            $("#mod_alert").remove();

            $("body:last-child").append(s);

            var largura = $("#mod_modal").width() / 2;
            var altura = $("#mod_modal").height() / 2;

            $("#mod_modal").css("margin-left", largura * -1);
            $("#mod_modal").css("margin-top", altura * -1);

            $('#mod_close').unbind();
            $('#mod_alert').unbind();
            $(document).unbind("keyup");

            $('#mod_close').click(function (event) {
                $.close(opt.onClose);
            });

            $('#mod_alert').click(function (event) {
                $.close(opt.onClose);
            });

            if (opt.timeout !== undefined && opt.timeout !== null) {
                setTimeout(function () {
                    $.close(opt.onClose);
                }, opt.timeout);
            }

            $(document).keyup(function (e) {
                if (e.keyCode === 27) {
                    $.close(opt.onClose);
                }
            });

        };


        $.close = function (acao) {
            if ($("#mod_alert").length !== 0) {
                $("#mod_alert").remove();
                if (acao !== undefined && acao !== null) {
                    acao();
                }
                $('#mod_close').unbind();
                $('#mod_alert').unbind();
                $(document).unbind("keyup");
            }
        };

    }

    setup(jQuery);

})();*/