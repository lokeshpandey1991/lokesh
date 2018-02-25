/*
 * c_productfeaturedtile.js
 * [ This javascript code is used to implement analytics. ]
 *
 * @project:    SN-RO
 * @date:       2017-08-26
 * @author:     Prashant
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace featuredtileCmp
 * @memberof roche
 */

(function (window, $, snro) {

    //let _cache = {};

    snro = window.snro = snro || {};

    snro.featuredtileCmp = {
        moduleName: 'featuredtileCmp',

        // assignment of dom selectors to variables
        updateCache: function updateCache() {},


        // bind dom events
        bindEvents: function bindEvents() {},


        // Module initialization
        init: function init() {
            this.updateCache();
            this.bindEvents();
        }
    };
})(window, window.jQuery, window.snro);
