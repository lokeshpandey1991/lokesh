(function () {
    'use strict';
    function get_query() {
        var url = location.href;
        var qs = url.substring(url.indexOf('?') + 1).split('&');
        for (var i = 0, result = {}; i < qs.length; i++) {
            qs[i] = qs[i].split('=');
            result[qs[i][0]] = decodeURIComponent(qs[i][1]);
        }
        return result;
    }

    if (/Paparazzi/.test(navigator.userAgent) || /PhantomJS/.test(navigator.userAgent)) {
        var query = get_query();
        var executeList = query.do && query.do.split(",");

        if (executeList) {
            executeList.forEach(function (el) {
                eval(el + '()');
            });
        }
    }


})(jQuery, document, window);
