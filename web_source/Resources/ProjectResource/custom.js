$(document).ready(function(){
    //mobile menu trigger
    $("#trigger").click(function(){
        $(this).toggleClass("rotate-90");
            $("#main-menu").toggleClass("show-menu");
    });
});
if ($(window).width() > 960) {
$(function(){
    $(window).scroll(function() {
        if ($(this).scrollTop() >= 50) {
            $('div#header').addClass('stickytop');
            $('ul.main-menu li img').addClass('navbar-list-img');
        }
        else {
            $('div#header').removeClass('stickytop');
            $('ul.main-menu li img').removeClass('navbar-list-img');
        }
    });
});
}else {
    
}