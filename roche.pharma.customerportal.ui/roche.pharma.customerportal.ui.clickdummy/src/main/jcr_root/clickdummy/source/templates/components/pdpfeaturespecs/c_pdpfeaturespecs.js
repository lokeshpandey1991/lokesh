/*
 * pdpfeaturespecs.js
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

  /**
   * @method init
   * @description this method is used to initialize public methods.
   * @memberof snro.pdpfeaturespecs
   * @example
   * snro.pdpfeaturespecs.init()
   */
  snro.pdpfeaturespecs = {

    init: function init() {
      var specsCount = $('.js-system-specification-dtl .js-pdp-spec-item').length;
      if (!specsCount) {
        $('.js-specifications-container-dtl').hide();
      }
    }
  };
})(window, jQuery, window.snro);
