// Karma configuration
// Generated on fri Apr 21 2017

module.exports = function(config) {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: 'source',

        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['mocha', 'chai', 'sinon'],

        // list of files / patterns to load in the browser
        files: [
            'https://code.jquery.com/jquery-1.11.2.min.js',
            {pattern: '../source/bower_components/**/*.js', included: false},
            {pattern: '../source/framework/**/*.js', included: false},
            'scripts/utils/jqueryScroll.js',
            {pattern: '../source/helpers/!(jqueryScroll).js', included: false},
            {pattern: '../source/scripts/utils/*.js', included: false},
            {pattern: '../source/templates/components/captcha/*.js', included: false},
            {pattern: '../source/templates/components/carousel/*.js', included: false},
            {pattern: '../source/templates/components/contactForm/*.js', included: false},
            {pattern: '../source/templates/components/cookiedisclaimer/*.js', included: false},
            {pattern: '../source/templates/components/dynamedia/*.js', included: false},
            {pattern: '../source/templates/components/errorPage/*.js', included: false},
            {pattern: '../source/templates/components/faq/*.js', included: false},
            {pattern: '../source/templates/components/featuretable/*.js', included: false},
            {pattern: '../source/templates/components/footer/*.js', included: false},
            {pattern: '../source/templates/components/generalMap/*.js', included: false},
            {pattern: '../source/templates/components/headlinebody/*.js', included: false},
            {pattern: '../source/templates/components/heroMedia/*.js', included: false},
            {pattern: '../source/templates/components/listingtile/*.js', included: false},
            {pattern: '../source/templates/components/marketingTile/*.js', included: false},
            {pattern: '../source/templates/components/mediainfo/*.js', included: false},
            {pattern: '../source/templates/components/pageDetails/*.js', included: false},
            {pattern: '../source/templates/components/pageNotification/*.js', included: false},
            {pattern: '../source/templates/components/pdpfeaturespecs/*.js', included: false},
            {pattern: '../source/templates/components/preloader/*.js', included: false},
            {pattern: '../source/templates/components/productCategoryFilter/*.js', included: false},
            {pattern: '../source/templates/components/productCategoryList/*.js', included: false},
            {pattern: '../source/templates/components/productDescription/*.js', included: false},
            {pattern: '../source/templates/components/productdetail/*.js', included: false},
            {pattern: '../source/templates/components/productDetailName/*.js', included: false},
            {pattern: '../source/templates/components/productfeaturedtile/*.js', included: false},
            {pattern: '../source/templates/components/productlisting/*.js', included: false},
            {pattern: '../source/templates/components/productlistingtile/*.js', included: false},
            {pattern: '../source/templates/components/productThumbnail/*.js', included: false},
            {pattern: '../source/templates/components/registrationstatus/*.js', included: false},
            {pattern: '../source/templates/components/relatedlinks/*.js', included: false},
            {pattern: '../source/templates/components/relatedproducts/*.js', included: false},
            {pattern: '../source/templates/components/searchbarfilter/*.js', included: false},
            {pattern: '../source/templates/components/searchlisting/*.js', included: false},
            {pattern: '../source/templates/components/searchResultBar/*.js', included: false},
            {pattern: '../source/templates/components/searchsort/*.js', included: false},
            {pattern: '../source/templates/components/socialshare/*.js', included: false},
            {pattern: '../source/templates/components/texthighlightrail/*.js', included: false},
            {pattern: '../source/templates/components/title/*.js', included: false},
            {pattern: '../source/templates/components/video/*.js', included: false},
            {pattern: '../source/templates/components/assayMenuTab/*.js', included: false},
            '../source/**/*.js',
            'test-cases/*.spec.js'
        ],

        // list of files to exclude
        exclude: [
            '**/*.min.js',
            'node_modules/',
            '../source/coverage/**/*.js',
            'scripts/roche.templates.js',
            '../source/templates/components/contactForm/*.js'
        ],

        // preprocess matching files before serving them to the browser
        // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
        preprocessors: {
            '../source/templates/**/*.js' : ['coverage']
        },

        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        reporters: ['progress', 'coverage', 'dots', 'junit'],

        // web server port
        port: 9876,

        // enable / disable colors in the output (reporters and logs)
        colors: true,

        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,

//    plugins: [
//      'karma-mocha',
//      'karma-chrome-launcher',
//      'karma-sinon',
//      'karma-chai',
//      'karma-chai-sinon'
//    ],

        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['PhantomJS'],

        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: true,

        // Concurrency level
        // how many browser should be started simultaneous
        concurrency: Infinity,
        coverageReporter: {
            type: 'html',
            dir: 'coverage/'
        },

        junitReporter: {
            outputFile: 'test-results.xml'
        }

    })
}
