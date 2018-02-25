module.exports = {

    app: {
        files: {
            '<%= config.publish %>/roche.vendor.publish/js/vendor.js': [
                '<%= config.source %>/bower_components/jquery/dist/jquery.min.js',
                '<%= config.source %>/bower_components/jPlayer/dist/jplayer/jquery.jplayer.min.js',
                '<%= config.source %>/bower_components/jQuery.dotdotdot/src/jquery.dotdotdot.min.js',
                '<%= config.source %>/bower_components/bootstrap/js/transition.js',
                '<%= config.source %>/bower_components/bootstrap/js/carousel.js',
                '<%= config.source %>/bower_components/bootstrap/js/tooltip.js',
                '<%= config.source %>/bower_components/bootstrap/js/popover.js',
                '<%= config.source %>/bower_components/bootstrap/js/collapse.js',
                '<%= config.source %>/bower_components/bootstrap/js/modal.js',
                '<%= config.source %>/bower_components/rangeslider.js/dist/rangeslider.min.js',
                '<%= config.source %>/scripts/vendor/jqueryScroll.js',
                '<%= config.source %>/scripts/vendor/jquery.lazyload.js',
                '<%= config.source %>/scripts/vendor/matchMedia-polyfill.js',
                '<%= config.source %>/scripts/vendor/slick-carousel/slick/slick.js',
                '<%= config.source %>/scripts/vendor/CSSMap.js',
                '<%= config.source %>/bower_components/handlebars/handlebars.min.js',
                '<%= config.source %>/helpers/indexOperator.js',
                '<%= config.source %>/scripts/vendor/jquery.xdomainrequest.min.js',
                '<%= config.source %>/scripts/vendor/autosize.min.js',
                '//platform-api.sharethis.com/js/sharethis.js#property=59bf8f8db63ebb00127afe76&product=inline-share-buttons'
            ],

			'<%= config.publish %>/roche.global.publish/js/global.js': [
                '<%= config.source %>/templates/components/header/*.js',
                '<%= config.source %>/templates/components/footer/*.js',
                '<%= config.source %>/templates/components/headlinebody/*.js',
                '<%= config.source %>/templates/components/heroMedia/*.js',
                '<%= config.source %>/templates/components/liipbox/*.js',
                '<%= config.source %>/templates/components/pdpnavtabs/*.js',
                '<%= config.source %>/templates/components/hamburgermenu/*.js',
                '<%= config.source %>/templates/components/personapicker/*.js',
                '<%= config.source %>/templates/components/productCategoryList/*.js',
                '<%= config.source %>/templates/components/productCategoryFilter/*.js',
                '<%= config.source %>/templates/components/video/*.js',
                '<%= config.source %>/templates/components/countryselector/*.js',
                '<%= config.source %>/templates/components/featuretable/*.js',
                '<%= config.source %>/templates/components/marketingTile/*.js',
                '<%= config.source %>/templates/components/pageNotification/*.js',
                '<%= config.source %>/templates/components/dynamedia/*.js',
                '<%= config.source %>/templates/components/listingtile/*.js',
                '<%= config.source %>/scripts/common/*.js',
                '<%= config.source %>/scripts/base/*.js',
                '<%= config.source %>/templates/components/headerSearchBar/*.js',
                '<%= config.source %>/scripts/roche.templates.js',
                '<%= config.source %>/templates/components/faq/*.js',
                '<%= config.source %>/templates/components/generalMap/*.js',
                '<%= config.source %>/templates/components/popupcomponent/*.js',
                '<%= config.source %>/templates/components/socialshare/*.js',
                '<%= config.source %>/templates/components/assayMenuTab/*.js'
            ],
              '<%= config.publish %>/roche.secondary.publish/js/secondary.js': [
              '<%= config.source %>/templates/components/productdetail/*.js',
              '<%= config.source %>/templates/components/pdpfeaturespecs/*.js',
              '<%= config.source %>/templates/components/carousel/*.js',
              '<%= config.source %>/templates/components/contactForm/*.js',
              '<%= config.source %>/templates/components/listingtile/*.js',
              '<%= config.source %>/templates/components/listingwrapper/*.js',
              '<%= config.source %>/templates/components/searchbarfilter/c_searchbarfilter.js',
              '<%= config.source %>/templates/components/searchsort/c_searchsort.js',
              '<%= config.source %>/templates/components/searchfilters/c_searchfilters.js',
              '<%= config.source %>/templates/components/socialfeed/c_socialfeed.js',
              '<%= config.source %>/templates/components/imagegallery/c_imagegallery.js',
              '<%= config.source %>/templates/components/cookiedisclaimer/*.js'
            ],
            '<%= config.publish %>/roche.pdp.ssi.publish/js/pdp.js': [
                '<%= config.source %>/scripts/pdp-view-renderer.js'
            ],
            '<%= config.publish %>/roche.vendor.publish/css/vendor.css': [
                '<%= config.source %>/assets/css/vendor.css'

            ],
            '<%= config.publish %>/roche.global.publish/css/global.css': [
                '<%= config.source %>/assets/css/global.css',
                '<%= config.source %>/assets/cssmap-continents/cssmap-themes.css',
                '<%= config.source %>/assets/cssmap-continents/cssmap-continents.css'

            ],
            '<%= config.publish %>/roche.secondary.publish/css/secondary.css': [
                '<%= config.source %>/assets/css/secondary.css'

            ]
        },
        nonull: true
    }

};
