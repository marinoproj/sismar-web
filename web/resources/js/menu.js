$(document).ready(function () {

    $("#menu-content-nav li:not(.active)").find('ul').slideUp();
    $("#menu-content-nav li.active").find('ul').slideDown();

    $("#menu-content-nav li:not(.active)").hover(
            function () {
                $(this).find('ul').slideDown();
            },
            function () {
                $(this).find('ul').slideUp();
    });
            
});