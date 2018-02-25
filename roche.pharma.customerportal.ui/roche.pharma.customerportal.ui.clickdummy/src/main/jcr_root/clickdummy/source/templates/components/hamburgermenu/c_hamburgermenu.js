/*
 * c_hamburgermenu.js
 * [ This javascript code is used to show and hide the search fields on the home page main navigation. ]
 *
 * @project:    SN-RO
 * @date:       2017-07-25
 * @author:     Prashant
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace navigationCmp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  var _cache = {};

  snro = window.snro = snro || {};

  snro.navigationCmp = {
    moduleName: 'navigationCmp',

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.hamburgerMenu = $('.js-menu');
      _cache.globalSearch = $('.js-link-search');
      _cache.countrySelector = $('.js-link-country');
      _cache.personaSelector = $('.js-link-persona');
      _cache.navigationContent = $('.js-liipbox-navigation');
    },


    // bind dom events
    bindEvents: function bindEvents() {
      _cache.hamburgerMenu.on('click', this.onHamburgerClick);
    },


    //click event handler for hamburger menu
    onHamburgerClick: function onHamburgerClick() {
      if (_cache.navigationContent.length > 0) {
        snro.liipboxComp._liipboxOpen(_cache.navigationContent[0].innerHTML);
      }
    },


    // Module initialization
    init: function init() {
      this.updateCache();
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);
