/*
 * header.js
 * [ This javascript code is used to show and hide the search fields on the home page main navigation. ]
 *
 * @project:    SN-RO
 * @date:       2015-04-17
 * @author:     Shubham
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace headerNavigationComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  snro = window.snro = snro || {};

  var _cache = {};

  snro.headerNavigationComp = {
    moduleName: 'navigationCmp',

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.searchIcon = $('.js-link-search');
      _cache.searchBarNavigation = $('.js-search-bar-navigation');
    },


    // handle stickyness of header section
    _stickyHeader: function _stickyHeader() {
      var headerObj = document.getElementsByClassName('js-header-wrapper'),
          lastscroll = 0,
          scrollPosition = void 0,
          className = 'js-scroll',
          reg = new RegExp('(\\s|^)' + className + '(\\s|$)');

      window.onscroll = function () {
        scrollPosition = window.pageYOffset;
        if (scrollPosition > lastscroll && scrollPosition > 0) {
          if (headerObj[0].className.indexOf(className) === -1) {
            headerObj[0].className += ' ' + className;
          }
        } else {
          headerObj[0].className = headerObj[0].className.replace(reg, '');
        }
        lastscroll = scrollPosition;
      };
    },

    // bind dom events
    bindEvents: function bindEvents() {
      _cache.searchIcon.on('click', this.onSearchIconClick);
    },

    //click event handler for hamburger menu
    onSearchIconClick: function onSearchIconClick() {
      if (_cache.searchBarNavigation.length > 0) {
        snro.liipboxComp._liipboxOpen(_cache.searchBarNavigation[0].innerHTML);
      }
    },


    // Module initialization
    init: function init() {
      this._stickyHeader();
      this.updateCache();
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);
