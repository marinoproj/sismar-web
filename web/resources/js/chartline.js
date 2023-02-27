/* global Highcharts */

function Chart() {
}

Chart.failedCreateChartLine = function (id, status, message) {

    $("#" + id).empty();
    var child = "<span class=\"col-xs-12 chart-data-info\" style=\"text-align: center\">";

    switch (status) {
        case 2: // selecione o período
            child += "<i class=\"glyphicon glyphicon-exclamation-sign\"></i> ";
            child += message;
            break;
        case 3: // gráfico sem dados
            child += "<i class=\"glyphicon glyphicon-exclamation-sign\"></i> ";
            child += message;
            break;
        case 4: // erro na consulta de dados
            child += "<i class=\"glyphicon glyphicon-warning-sign\"></i> ";
            child += message;
            break;
        default:
            break;
    }

    child += "</span>";
    $("#" + id).append(child);

};

Chart.createChartLineArea = function (id, chartLine) {

    function createChart(id, chartLine) {

        Highcharts.chart(id, {
            chart: {
                type: 'area'
            },
            accessibility: {
                description: ''
            },
            exporting: false,
            title: {
                text: ''
            },
            credits: {
                enabled: false
            },
            subtitle: {
                text: ''
            },
            legend: {
                enabled: false
            },
            xAxis: {
                visible: false,
                type: 'datetime',
                labels: {
                    formatter: function () {
                        return this.value;
                    }
                },
                gridLineWidth: 0

            },
            yAxis: {
                visible: false,
                title: {
                    text: ''
                },
                labels: {
                    formatter: function () {
                        return this.value;
                    }
                },
                gridLineWidth: 0
            },
            tooltip: {
                formatter: function () {
                    var time = Highcharts.dateFormat('%d/%m/%Y %H:%M', this.x) + '<br/>';                    
                    var hours = this.y / 60;
                    var minutes = this.y % 60;
                    var h = hours < 10 ? ("0" + hours) : (hours + "");
                    var m = minutes < 10 ? ("0" + minutes) : (minutes + "");
                    var dur = "Duração: " + h + "h" + m + "m";
                    return time + '<br/>' + dur;
                }
            },
            plotOptions: {
                series: {
                    fillOpacity: 0.3
                },
                area: {
                    marker: {
                        enabled: false,
                        symbol: 'circle',
                        radius: 2,
                        states: {
                            hover: {
                                enabled: true
                            }
                        }
                    }
                }
            },
            series: getSeries(chartLine)

        });

    }

    function getSeries(chartLine) {
        var series = [];
        $.each(chartLine.series, function (index, serie) {

            var objSerieData = [];

            $.each(serie.data, function (index, data) {

                var date = new Date(data.date);

                var year = date.getFullYear();
                var month = date.getMonth();
                var day = date.getDate();

                var hours = date.getHours();
                var minutes = date.getMinutes();
                var seconds = date.getSeconds();

                objSerieData.push([Date.UTC(year, month, day, hours, minutes, seconds), data.value]);

            });

            var objSerie = {name: serie.nameSerie,
                color: serie.colorLineSerie,
                data: objSerieData};

            series.push(objSerie);

        });
        return series;
    }

    createChart(id, jQuery.parseJSON(chartLine));

};

Chart.createCartLine = function (id, chartLine) {

    function getAxis(chartLine) {
        var axis = [];
        $.each(chartLine.series, function (index, serie) {
            var y = {title: {text: (serie.nameSerie + " " + serie.typeValue)},
                min: (serie.range === undefined ? null : serie.range.min),
                max: (serie.range === undefined ? null : serie.range.max),
                endOnTick: false,
                opposite: serie.opposite
            };
            axis.push(y);
        });
        return axis;
    }

    function getSeries(chartLine) {
        var series = [];
        $.each(chartLine.series, function (index, serie) {

            var objSerieData = [];

            $.each(serie.data, function (index, data) {

                var date = new Date(data.date);

                var year = date.getFullYear();
                var month = date.getMonth();
                var day = date.getDate();

                var hours = date.getHours();
                var minutes = date.getMinutes();
                var seconds = date.getSeconds();

                objSerieData.push([Date.UTC(year, month, day, hours, minutes, seconds), data.value]);

            });

            var objSerie = {name: serie.nameSerie,
                yAxis: index,
                typeValue: serie.typeValue,
                color: serie.colorLineSerie,
                data: objSerieData};

            series.push(objSerie);

        });
        return series;
    }

    function createChart(id, chartLine) {

        Highcharts.chart(id, {

            chart: {
                type: 'spline',
                zoomType: 'x'
            },
            title: {
                text: chartLine.title
            },
            subtitle: {
                text: chartLine.subtitle
            },
            xAxis: {
                type: 'datetime',
                labels: {
                    format: '{value:%d/%m/%Y %H:%M}'
                },
                title: {
                    text: chartLine.titleXAxis
                }
            },
            yAxis: getAxis(chartLine),

            tooltip: {
                formatter: function () {
                    var text = '<b>' + this.series.name + '</b><br/>';
                    text += Highcharts.dateFormat('%d/%m/%Y %H:%M', this.x) + '<br/>';
                    text += Highcharts.numberFormat(this.y, 1);
                    return text;
                }
            },

            plotOptions: {
                spline: {
                    marker: {
                        enabled: false,
                        radius: 0
                    },
                    lineWidth: 1,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    }

                }
            },

            series: getSeries(chartLine)
        });

    }

    createChart(id, jQuery.parseJSON(chartLine));

};