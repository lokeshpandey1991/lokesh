/*
 * productdetail.js
 * [ This javascript code is used to initialize components on pdp. ]
 *
 * @project:    SN-RO
 * @date:       2017-08-03
 * @author:     Neha
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace productdetail
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  snro = window.snro = snro || {};

  // public methods
  var _initProductInfo = function _initProductInfo() {
    var twoColumnLayout = true;
    if ($('.js-pdp-feature-benefits').children().length === 0) {
      $('.js-pdp-feature-benefits').parents('.t-pdp-info-col').hide();
      twoColumnLayout = false;
    }

    if ($('.js-pdp-intended-use').children().length === 0) {
      $('.js-pdp-intended-use').parents('.t-pdp-info-col').hide();
      twoColumnLayout = false;
    }

    if (twoColumnLayout) {
      $('.js-pdp-info').addClass('t-pdp-info-col2');
    }
  };

  /**
   * @method init
   * @description this method is used to initialize public methods.
   * @memberof snro.productdetail
   * @example
   * snro.productdetail.init()
   */
  snro.productdetail = {

    init: function init() {
      _initProductInfo();
    }
  };
})(window, jQuery, window.snro);
