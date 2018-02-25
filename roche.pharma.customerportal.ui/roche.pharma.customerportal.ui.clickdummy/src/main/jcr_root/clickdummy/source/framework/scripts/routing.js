(function ($, document, window) {
    'use strict';

    var developerMode = (localStorage.getItem('developerMode') == "true");
    if (developerMode) {
        $('.quick-navigation').find('li').find('a').each(function (index, el) {
            var url = $(el).attr('href');
            $(el).attr('href', url.replace(/^#!/, ''));
        })
    }

    $(window).on('hashchange', function (e) {
        var hash = window.location.hash;
        console.log('hashchange:', hash, e.originalEvent, e.originalEvent['newURL'], e.originalEvent['oldURL']);
        if (hashIsRoute()) {
            restoreState();
            if ($('.quick-navigation').hasClass('expanded')) {
                $('.nav-expander').click();
            }
        }
        if (!hash) {
            window.location.reload();
        }
    });

    if (hashIsRoute()) {
        restoreState()
    }

    function restoreState() {
        var url = window.location.hash.replace(/^#!/, '');
        console.log("Changing iframe url :", url);
        var frame = $('.ux-library-viewer')[0];

        frame.contentWindow.location.replace(url);

    }

    function hashIsRoute(hash) {
        var currentHash = hash || window.location.hash;
        return /^#!/.test(currentHash);

    }

})(jQuery, document, window);
