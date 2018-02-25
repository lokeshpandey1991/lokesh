/*!
 * Cookie Disclaimer.js

 * This file contians  Cookie Disclaimer functions for Create Cookies to check the First time user.
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-07-17
 * @author    Nimesh
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
  //moduleName: 'cookiesNot',
  /**
   * This Cookie Disclaimer functions for Create Cookies to check the First time user.
   * Accepts any valid selector or jQuery object.
   * @param {selector, HTMLElement, or jQuery object} table
   */
  var cookiesTag = $('.js-cookies-tag'),
      closeBtn = $('.js-closeBtn');
  //Hide the Cookie popup
  var cookiesPopupHide = function cookiesPopupHide() {
    cookiesTag.slideUp(500);
  };
  //Show the Cookie popup
  var cookiesPopupShow = function cookiesPopupShow() {
    cookiesTag.show().animate({ top: '0px' }, 500);
  };
  //Close cookie on button click
  var closeCookiepopup = function closeCookiepopup() {
    closeBtn.on('click', cookiesPopupHide);
  };
  var checkCookieDisclaimer = function checkCookieDisclaimer() {
    closeCookiepopup();
    var checkCookie = snro.commonUtils.getCookie('CookieDisclaimer');
    if (checkCookie !== '') {
      cookiesPopupHide();
    } else {
      checkCookie = cookiesPopupShow();
      if (checkCookie !== '' && checkCookie !== null) {
        snro.commonUtils.setCookie('CookieDisclaimer', 'Yes');
      }
    }
  };
  snro.cookiesDisclaimer = {
    init: function init() {
      checkCookieDisclaimer();
    }
  };
})(window, window.jQuery, window.snro);
