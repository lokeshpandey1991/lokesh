/*!
 * faq.js

 * This file contians  faq functions for add ellipisis.
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
   * This faq functions for add ellipisis.
   * Accepts any valid selector or jQuery object.
   * @param {selector, HTMLElement, or jQuery object} table
   */

  var faqMaxChar = function faqMaxChar() {

    var faqDesc = $('.c-faq__description');
    if (!snro.commonUtils.isMobile()) {
      faqDesc.each(function () {
        var self = $(this);
        if (self.text().trim().length > 400) {
          snro.commonUtils.addEllipses();
        }
      });
    }
  };

  var _cache = {
    faqInput: $('.js-faq-row').find('input')
  };

  snro.faq = {
    bindEvents: function bindEvents() {
      _cache.faqInput.on('click', function () {
        _cache.faqInput.not(this).prop('checked', false);
      });
    },
    init: function init() {
      this.bindEvents();
      faqMaxChar();
    }
  };
})(window, window.jQuery, window.snro);
