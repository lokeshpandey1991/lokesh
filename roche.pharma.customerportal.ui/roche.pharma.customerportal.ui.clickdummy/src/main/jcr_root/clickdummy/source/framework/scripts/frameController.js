// TODO : include highlighting of active control
// TODO : include scale to full-screen when clicking on current active control
$(function () {
    'use strict';

    var $frame = $('.js-fw-flexible-frame__iframe'),
        hash = location.hash;

    if ($frame.length) {
        var breakpoints = {
            mobile: 321,
            tablet: 750,
            desktop: 1025
        };

        $('.fw-page-header__control--mobile').on('click', function (e) {
            e.preventDefault();
            setFrameWidth(breakpoints.mobile);
        });

        $('.fw-page-header__control--tablet').on('click', function (e) {
            e.preventDefault();
            setFrameWidth(breakpoints.tablet);
        });

        $('.fw-page-header__control--desktop').on('click', function (e) {
            e.preventDefault();
            setFrameWidth(breakpoints.desktop);
        });

        var setFrameWidth = function (width) {
            $frame.animate({
                width: width
            }, 800);
        };

        if (hash) {
            $frame.attr('src', hash.replace('#!', ''));
        }

    }

});