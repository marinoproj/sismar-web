/* global Highcharts */

function Meteorology() {
}

Meteorology.gaugeSpeed = function () {

    var gaugeSpeedChart;

    this.initialize = function (id, title, subtitle, min, max, normal, attention, critical) {

        gaugeSpeedChart = Highcharts.chart(id, {

            chart: {
                type: 'gauge',
                height: 300,
                plotBackgroundColor: null,
                plotBackgroundImage: null,
                plotBorderWidth: 0,
                plotShadow: false
            },

            title: {
                text: title
            },

            subtitle: {
                text: subtitle,
                style: {
                    fontSize: '12px',
                    fontWeight: 'normal',
                    color: '#333'
                }
            },

            pane: {
                startAngle: -90,
                endAngle: 89.9,
                background: null,
                center: ['50%', '75%'],
                size: '90%'
            },

            // the value axis
            yAxis: {
                min: min,
                max: max,
                tickPixelInterval: 50,
                tickPosition: 'inside',
                tickColor: Highcharts.defaultOptions.chart.backgroundColor || '#FFFFFF',
                tickLength: 20,
                tickWidth: 2,
                minorTickInterval: null,
                labels: {
                    distance: 20,
                    style: {
                        fontSize: '14px'
                    }
                },
                lineWidth: 0,
                plotBands: [{
                        from: normal,
                        to: attention,
                        color: '#55BF3B', // green
                        thickness: 20,
                        borderRadius: '0%'
                    }, {
                        from: critical,
                        to: max,
                        color: '#DF5353', // red
                        thickness: 20,
                        borderRadius: '0%'
                    }, {
                        from: attention,
                        to: critical,
                        color: '#DDDF0D', // yellow
                        thickness: 20
                    }]
            },

            series: [{
                    name: 'Velocidade',
                    data: [15],
                    tooltip: {
                        valueSuffix: ' nós'
                    },
                    dataLabels: {
                        format: '{y} nós',
                        borderWidth: 0,
                        color: (
                                Highcharts.defaultOptions.title &&
                                Highcharts.defaultOptions.title.style &&
                                Highcharts.defaultOptions.title.style.color
                                ) || '#333333',
                        style: {
                            fontSize: '18px'
                        }
                    },
                    dial: {
                        radius: '80%',
                        backgroundColor: 'gray',
                        baseWidth: 12,
                        baseLength: '0%',
                        rearLength: '0%'
                    },
                    pivot: {
                        backgroundColor: 'gray',
                        radius: 6
                    }

                }],

            exporting: {
                enabled: false
            }

        });

    };

    this.setValue = function (value) {
        gaugeSpeedChart.series[0].points[0].update(value);
    };

};

Meteorology.gaugeDirRose = function () {

    var gaugeDirRoseChart;

    this.initialize = function (id, title, subtitle) {

        gaugeDirRoseChart = Highcharts.chart(id, {
            chart: {
                type: 'gauge',
                plotBorderWidth: 0,
                plotBackgroundColor: null,
                plotBackgroundImage: null,
                plotShadow: false
            },

            title: {
                text: title
            },

            subtitle: {
                text: subtitle,
                style: {
                    fontSize: '12px',
                    fontWeight: 'normal',
                    color: '#333'
                }
            },

            pane: {
                startAngle: 0,
                endAngle: 360,
                background: [{
                        backgroundColor: '#EEE',
                        innerRadius: '60%',
                        outerRadius: '100%',
                        shape: 'arc'
                    }]
            },

            yAxis: {
                min: 0,
                max: 360,
                tickInterval: 45,
                labels: {
                    formatter: function () {
                        const labels = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'];
                        return labels[Math.round(this.value / 45) % 8];
                    },
                    style: {
                        fontSize: '13px',
                        color: '#333'
                    }
                },
                title: {
                    text: ''
                }
            },

            plotOptions: {
                gauge: {
                    dial: {
                        radius: '100%',
                        backgroundColor: 'black',
                        baseWidth: 4,
                        topWidth: 1
                    },
                    pivot: {
                        backgroundColor: 'black'
                    }
                }
            },

            series: [{
                    name: 'Direção',
                    data: [0],
                    tooltip: {
                        valueSuffix: '°',
                        style: {
                            fontSize: '13px'
                        }
                    },
                    dataLabels: {
                        enabled: true,
                        y: 60,
                        borderWidth: 0,
                        verticalAlign: 'bottom',
                        style: {
                            fontSize: '18px',
                            color: '#333'
                        },
                        format: '{y}º'
                    }
                }],

            exporting: {
                enabled: false
            }

        });

    };

    this.setValue = function (value) {
        gaugeDirRoseChart.series[0].points[0].update(value);
    };

};

Meteorology.chartLineWindSpeedAndGust = function () {

    var chartWindSpeedAndGust;

    //const SEVEN_DAYS = 7 * 24 * 60 * 60 * 1000;

    this.initialize = function (id, title) {

        chartWindSpeedAndGust = Highcharts.chart(id, {
            chart: {
                type: 'line'
            },
            title: {
                text: title
            },
            xAxis: {
                type: 'datetime',
                title: {
                    text: 'Data/Hora'
                },
                labels: {
                    format: '{value:%d/%m %H:%M}',
                    style: {
                        fontSize: '12px',
                        fontWeight: 'normal',
                        color: '#333'
                    }
                }
            },
            yAxis: {
                title: {
                    text: 'Velocidade (nós)',
                    style: {
                        fontSize: '13px',
                        fontWeight: 'bold',
                        color: '#333'
                    }
                },
                min: 0,
                labels: {
                    style: {
                        fontSize: '12px',
                        color: '#333'
                    }
                }
            },
            tooltip: {
                shared: true,
                xDateFormat: '%d/%m/%Y %H:%M:%S',
                valueSuffix: ' nós',
                style: {
                    fontSize: '13px'
                }
            },
            series: [{
                    name: 'Velocidade do Vento',
                    data: [],
                    color: '#007bff',
                    marker: {
                        enabled: false
                    }
                }, {
                    name: 'Rajada de Vento',
                    data: [],
                    color: '#df5353',
                    marker: {
                        enabled: false
                    }
                }],
            legend: {
                align: 'center',
                verticalAlign: 'bottom',
                itemStyle: {
                    fontSize: '13px',
                    fontWeight: 'bold',
                    color: '#333'
                }
            },
            exporting: {
                enabled: false
            }
        });

    };

    this.addValue = function (timestamp, windSpeed, gustSpeed) {

        chartWindSpeedAndGust.series[0].addPoint([timestamp, windSpeed], false);
        chartWindSpeedAndGust.series[1].addPoint([timestamp, gustSpeed], false);

        // Limpar dados mais antigos que 7 dias
        /*const now = Date.now();
        chartWindSpeedAndGust.series.forEach(series => {
            while (series.data.length && (now - series.data[0].x) > SEVEN_DAYS) {
                series.data[0].remove(false);
            }
        });*/

        chartWindSpeedAndGust.redraw();

    };

    this.addValues = function (dataArray) {

        dataArray.forEach(item => {
            chartWindSpeedAndGust.series[0].addPoint([item.timestamp, item.windSpeed], false);
            chartWindSpeedAndGust.series[1].addPoint([item.timestamp, item.gustSpeed], false);
        });

        // Limpar dados mais antigos que 7 dias
        /*const now = Date.now();
        chartWindSpeedAndGust.series.forEach(series => {
            while (series.data.length && (now - series.data[0].x) > SEVEN_DAYS) {
                series.data[0].remove(false);
            }
        });*/

        chartWindSpeedAndGust.redraw();
    };

    this.clear = function () {
        chartWindSpeedAndGust.series.forEach(series => {
            while (series.data.length) {
                series.data[0].remove(false);
            }
        });
    };

};

Meteorology.gaugeWindDirectionFrequency = function () {

    var gaugeWindDirectionFrequencyChart;

    const freq = {
        'N': 0, 'NE': 0, 'E': 0, 'SE': 0, 'S': 0, 'SW': 0, 'W': 0, 'NW': 0
    };

    this.initialize = function (id, title) {

        gaugeWindDirectionFrequencyChart = Highcharts.chart(id, {
            chart: {
                polar: true,
                type: 'column'
            },

            title: {
                text: title
            },

            pane: {
                size: '80%'
            },

            xAxis: {
                categories: ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'],
                tickmarkPlacement: 'on',
                lineWidth: 0,
                labels: {
                    style: {
                        fontSize: '13px',
                        color: '#333'
                    }
                }
            },

            yAxis: {
                min: 0,
                endOnTick: false,
                showLastLabel: true,
                gridLineInterpolation: 'polygon',
                title: {
                    text: 'Frequência',
                    style: {
                        fontSize: '13px',
                        color: '#333'
                    }
                }
            },

            legend: {
                itemStyle: {
                    fontSize: '13px'
                }
            },

            tooltip: {
                shared: true,
                pointFormat: '<b>{point.y} ocorrências</b>',
                style: {
                    fontSize: '13px'
                }
            },

            series: [{
                    name: 'Ocorrências',
                    data: Object.values(freq),
                    pointPlacement: 'on',
                    color: '#004080'
                }],

            exporting: {
                enabled: false
            }
        });

    };

    this.addValues = function (dataArray) {
        for (const direcao in dataArray) {
            if (freq.hasOwnProperty(direcao)) {
                freq[direcao] = dataArray[direcao];
            }
        }
        gaugeWindDirectionFrequencyChart.series[0].setData(Object.values(freq));
    };

    this.clear = function () {
        gaugeWindDirectionFrequencyChart.series.forEach(series => {
            while (series.data.length) {
                series.data[0].remove(false);
            }
        });
    };

};

Meteorology.chartLineSeacurrentSpeed = function () {

    var chartSeacurrentSpeed;

    //const SEVEN_DAYS = 7 * 24 * 60 * 60 * 1000;

    this.initialize = function (id, title) {

        chartSeacurrentSpeed = Highcharts.chart(id, {
            chart: {
                type: 'line'
            },
            title: {
                text: title
            },
            xAxis: {
                type: 'datetime',
                title: {
                    text: 'Data/Hora'
                },
                labels: {
                    format: '{value:%d/%m %H:%M}',
                    style: {
                        fontSize: '12px',
                        fontWeight: 'normal',
                        color: '#333'
                    }
                }
            },
            yAxis: {
                title: {
                    text: 'Velocidade (nós)',
                    style: {
                        fontSize: '13px',
                        fontWeight: 'bold',
                        color: '#333'
                    }
                },
                min: 0,
                labels: {
                    style: {
                        fontSize: '12px',
                        color: '#333'
                    }
                }
            },
            tooltip: {
                shared: true,
                xDateFormat: '%d/%m/%Y %H:%M:%S',
                valueSuffix: ' nós',
                style: {
                    fontSize: '13px'
                }
            },
            series: [{
                    name: 'Velocidade',
                    data: [],
                    color: '#007bff',
                    marker: {
                        enabled: false
                    }
                }],
            legend: {
                align: 'center',
                verticalAlign: 'bottom',
                itemStyle: {
                    fontSize: '13px',
                    fontWeight: 'bold',
                    color: '#333'
                }
            },
            exporting: {
                enabled: false
            }
        });

    };

    this.addValue = function (timestamp, speed) {
        chartSeacurrentSpeed.series[0].addPoint([timestamp, speed], false);
        chartSeacurrentSpeed.redraw();
    };

    this.addValues = function (dataArray) {

        dataArray.forEach(item => {
            chartSeacurrentSpeed.series[0].addPoint([item.timestamp, item.speed], false);
        });

        chartSeacurrentSpeed.redraw();
    };

    this.clear = function () {
        chartSeacurrentSpeed.series.forEach(series => {
            while (series.data.length) {
                series.data[0].remove(false);
            }
        });
    };

};