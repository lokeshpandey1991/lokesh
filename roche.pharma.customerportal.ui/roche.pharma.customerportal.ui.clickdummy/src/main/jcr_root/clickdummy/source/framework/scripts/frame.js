$(function () {

    var $frame = $('.js-ux-frame');
    var hash = window.location.hash.replace('#!','');

    $frame.attr('src', hash);

});