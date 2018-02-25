$(function () {
    'use strict';

    var $anchors = $('.js-component-list__item-anchor'),
        // we assume that touch devices == mobile
        isMobile = $('html').hasClass('touch');

    if ($anchors.length && isMobile) {
        _.each($anchors, function(anchor) {
            $(anchor).attr('href', $(anchor).attr('href').replace('/frame.html#!', ''));
        });
    }

});