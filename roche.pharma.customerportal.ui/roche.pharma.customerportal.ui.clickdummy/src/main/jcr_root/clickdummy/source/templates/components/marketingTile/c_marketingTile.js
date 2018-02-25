/*!
 * common.utils.js

 * This file contians some most common utility functions
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-07-17
 * @author    Shashank
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
   * Generic function will be use if description length will exceed from 150 char more link will be displayed.
   * Accepts any valid selector or jQuery object.
   * @param {selector, HTMLElement, or jQuery object} table
   */

  var seeMorelink = function seeMorelink() {

    var viewLink = $('.c-marketingtile__description'),
        seeMore = '.js-seemore',
        ctaEle = '.c-marketingtile__cta';

    viewLink.each(function () {
      var self = $(this);
      if (self.text().trim().length > 150) {
        self.parent('div').find(ctaEle).hide();
        self.parent('div').find(seeMore).show();
        snro.commonUtils.addEllipses();
      } else {
        self.parent('div').find(ctaEle).show();
        self.parent('div').find(seeMore).hide();
      }
    });
  };

  snro.marketingTile = {
    init: function init() {
      seeMorelink();
    }
  };
})(window, window.jQuery, window.snro);
