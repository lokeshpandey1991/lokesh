(function ($) {
    'use strict';

    var $body = $('body')/*,
        $toggleDeveloper = $('.toggle-developer'),
        $toggleDebug = $('.toggle-debug')*/;

    function getHexColor() {
        var hexDigits = '0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f'.split(',');

        //Function to convert hex format to a rgb color
        function rgb2hex(rgb) {
            rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
            return '#' + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
        }

        function hex(x) {
            return isNaN(x) ? '00' : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
        }


        var color;
        $('.colors').find('li').each(function () {
            color = $(this).css('background-color');
            $(this).find('span').append('<br>' + rgb2hex(color));

        });
    }

    function getIcons(el) {

        var $list = $(el),
            source = JSON.parse($(el + '__source').html());
        _.forEach(source.icons, function (icon) {
            if (!!~(icon.indexOf('.colors'))) { // color variant
                var iconFragment = icon.split('.colors-'),
                    iconPart = iconFragment[0],
                    colors = iconFragment[1].split('-');
                buildIcon(iconPart);
                _.forEach(colors, function (color) {
                    buildIcon(iconPart, color);
                });
            } else { // no color variant
                buildIcon(icon);
            }
        });

        $(el + '__item').first().remove();

        function buildIcon(icon, color) {
            var $clone = $(el + '__item').first().clone();
            $clone.addClass('icon-' + icon + (color ? '-' + color : ''));
            $clone.find(el + '__content').text(icon + (color ? ' - ' + color : ''));
            $list.append($clone);
        }

    }

    function getFontSizes(el) {
        var $items = $(el);
        $items.each(function (index, item) {
            var $parent = $(item).parent();
            $(item).html($parent.css('font-family') + ' - ' + $parent.css('font-size') + '/' + $parent.css('line-height') + '    ' + $parent.css('font-weight'));
        });

    }

    if ($body.hasClass('all-elements')) {
        getHexColor();
        if ($('.font-size').length) {
            getFontSizes('.font-size');
        }
        if ($('.icon-list').length) {
            getIcons('.icon-list');
        }
    }

    $body.addClass('developer-menu-visible');

/*
    var developerMode = JSON.parse(localStorage.getItem('developerMode'));
    if (developerMode) {
        $body.addClass('developer');
        $toggleDeveloper.find('strong').toggleClass('fa-square-o fa-check-square-o');//.text('[ON]');
    }

    $toggleDeveloper.on('click', function () {
        localStorage.setItem('developerMode', !developerMode);
        location.reload();
    });
*/

/*
    var debugMode = JSON.parse(localStorage.getItem('debug'));
    if (debugMode) {
        $body.addClass('debug');
        $toggleDebug.find('strong').toggleClass('fa-square-o fa-check-square-o');//text('[ON]');
    }

    $toggleDebug.on('click', function () {
        localStorage.setItem('debug', !debugMode);
        location.reload();
    });
*/

})(jQuery);
