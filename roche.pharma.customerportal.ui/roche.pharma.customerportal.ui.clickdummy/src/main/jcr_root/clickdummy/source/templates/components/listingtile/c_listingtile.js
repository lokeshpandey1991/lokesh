/*!
 * c_listingtile.js

 * This file contians date string manuplation  functions
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-07-17
 * @author    Suneel
 * @dependencies jQuery
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace Main
 * @memberof snro
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro = snro || {};

  /**
   * This function will be use date string manuplation and wrap in html dom.
   * Accepts any valid selector or jQuery object.
   * @param {selector, HTMLElement, or jQuery object} table
   */

  var caldate = function caldate() {
    var dataDataAttr = $('.c-listingtile__date');

    dataDataAttr.each(function () {
      var self = $(this);
      if (self.data('dateformat') && self.data('dateformat').split(/\s+/).length > 2) {
        var dataValue = self.data('dateformat').split(/\s+/);
        self.html(dataValue[0] + '<span>' + dataValue[1] + '</span>' + dataValue[2]);
      }
    });
  };

  snro.listingtile = {
    init: function init() {
      caldate();
    }
  };
})(window, window.jQuery, window.snro);
