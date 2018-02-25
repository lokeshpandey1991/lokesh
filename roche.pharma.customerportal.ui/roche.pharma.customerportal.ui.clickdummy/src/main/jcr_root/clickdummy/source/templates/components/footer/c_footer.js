/*
 * c_footer.js
 * [ This javascript code is used to  display the timestamps on the footer. ]
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
 * @namespace footerCmp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  var _cache = {};

  snro = window.snro = snro || {};

  snro.footerCmp = {
    moduleName: 'footerCmp',

    // assignment of dom selectors to variables
    _updateCache: function _updateCache() {
      _cache.timeStamp = $('.js-timestamp');
    },


    // bind dom events
    _bindEvents: function _bindEvents() {
      _cache.timeStamp.text(this._getTimestamp());
    },
    _getTimestamp: function _getTimestamp() {
      var timestamp = new Date();
      var day = timestamp.getDate();
      var month = timestamp.getMonth() + 1;
      var year = timestamp.getFullYear();
      day = (day < 10 ? '0' : '') + day;
      month = (month < 10 ? '0' : '') + month;
      return day + '.' + month + '.' + year;
    },


    // Module initialization
    init: function init() {
      this._updateCache();
      this._bindEvents();
    }
  };
})(window, window.jQuery, window.snro);
