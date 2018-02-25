/*
 * liipbox.js
 * [ This javascript code is to open overlay which will act as getter for HTML content to display.
 *
 * @project:    SN-RO
 * @date:       2017-07-25
 * @author:     shubham
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace liipboxComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  var _cache = {};

  snro = window.snro = snro || {};

  /**
   *  function to close liipbox
   */
  function closeLiipbox() {
    var liipbox = $('.js-liipbox'),
        className = 'x-no-scroll',
        reg = new RegExp('(\\s|^)' + className + '(\\s|$)');

    if (liipbox.length > 0) {
      liipbox.remove();
      _cache.focusedElementBeforeModal.focus();
      document.body.className = document.body.className.replace(reg, '');
    }
  }

  /**
   * function to provide open animation to the liipbox (overlay)
   */
  function liipboxAnimation(event) {

    var toggleLiipbox = (event === 'close') > 0 ? true : false,


    // jquery selectors
    linkSelector = toggleLiipbox ? $('.js-liipbox').find('.link-row').get().reverse() : $('.js-liipbox').find('.link-row'),
        logoSelector = $('.js-liipbox').find('.js-liipbox-logo'),
        crossIcon = $('.js-liipbox').find('.js-cross-icon'),
        liipboxOverlay = $('.js-liipbox').find('.js-liipbox-content'),
        animateHeaderClass = toggleLiipbox ? 'animated fadeOut' : 'animated fadeIn',
        animateContentClass = toggleLiipbox ? 'animated fadeOutDown' : 'animated fadeInUp',
        animationEndCallback = toggleLiipbox ? closeLiipbox : '';

    // animate content
    if (linkSelector.length > 0) {
      $(linkSelector).each(function (index, element) {
        var transitionDelay = index * 0.04 + 's';
        if (window.requestAnimationFrame) {
          $(element).css('animation-delay', transitionDelay).css('-webkit-animation-delay', transitionDelay).removeClass('').addClass(animateContentClass);
        }
      }).on('animationend', animationEndCallback);
    } else {
      animationEndCallback && animationEndCallback();
    }

    // animate header
    if (toggleLiipbox) {
      $(logoSelector).removeClass('translated');
      $(crossIcon).removeClass('translated');
    } else {
      $(liipboxOverlay).addClass(animateHeaderClass);
      $(logoSelector).addClass('translated');
      $(crossIcon).addClass('translated');
    }
  }

  /**
   * function keeps track of highlighted items
   * while iterating over links, buttons &
   * input fields
   * @param e Keyboard event
   */
  function trapTabKey(e) {
    // Check for TAB key press
    if (e.keyCode === 9) {

      // SHIFT + TAB
      if (e.shiftKey) {
        if (document.activeElement === _cache.firstTabStop) {
          e.preventDefault();
          _cache.lastTabStop.focus();
        }
        // TAB
      } else if (document.activeElement === _cache.lastTabStop) {
        e.preventDefault();
        _cache.firstTabStop.focus();
      }
    }

    // ESCAPE
    if (e.keyCode === 27) {
      //closeLiipbox();
      liipboxAnimation('close');
    }
  }

  snro.liipboxComp = {

    /**
     * assignment of dom selectors to variables
     */
    _updateCache: function _updateCache() {
      _cache.documentBody = $('body');
      _cache.liipbox = $('.js-liipbox');
      _cache.focusedElementBeforeModal = '';
      _cache.firstTabStop = '';
      _cache.lastTabStop = '';
    },


    /**
     * method to show overlay
     */
    _liipboxOpen: function _liipboxOpen(data) {
      var e = document.createElement('div'),
          html = data,
          liipbox = document.getElementsByClassName('js-liipbox');

      e.className = 'js-liipbox';
      e.innerHTML = html;
      document.body.className = 'x-no-scroll';

      if (liipbox.length > 0) {
        liipbox[0].outerHTML = '';
      }

      document.body.appendChild(e);
      this._accessibleLiipbox();
      liipboxAnimation();
    },


    /**
     * binding events on liipbox
     */
    _bindEvents: function _bindEvents() {
      $('body').on('click', '.js-cross-icon', function () {
        //closeLiipbox();
        liipboxAnimation('close');
      });
    },


    /**
     *  Add keyboard accessibility to the dynamically added content
     *  in liipbox (overlay)
     *  "Tab/Shift + Tab" to iterate through links, buttons, input field
     *  "Esc" key to close the liipbox
     */
    _accessibleLiipbox: function _accessibleLiipbox() {

      // Save current focus
      _cache.focusedElementBeforeModal = document.activeElement;

      // Find the liipbox and its overlay
      var liipbox = document.querySelector('.js-liipbox'),
          liipboxOverlay = document.querySelector('.js-liipbox-overlay');

      // Listen for and trap the keyboard
      liipbox.addEventListener('keydown', trapTabKey);

      if (liipboxOverlay && $(liipboxOverlay).length > 0) {
        // Listen for indicators to close the lipbox
        liipboxOverlay.addEventListener('click', liipboxAnimation);

        // liipbox close button
        var closeButtons = liipboxOverlay.querySelector('.js-cross-icon');

        // Attach listeners to all the close modal buttons
        closeButtons.addEventListener('click', liipboxAnimation);

        // Find all focusable children
        var focusableElementsString = 'a[href], area[href], input:not([disabled]), select:not([disabled]), textarea:not([disabled]), button:not([disabled]), iframe, object, embed, [tabindex="0"], [contenteditable]',
            focusableElements = liipbox.querySelectorAll(focusableElementsString);

        if (focusableElements.length > 0) {
          // Convert NodeList to Array
          focusableElements = Array.prototype.slice.call(focusableElements);

          _cache.firstTabStop = focusableElements[0];
          _cache.lastTabStop = focusableElements[focusableElements.length - 1];

          // Focus first child
          _cache.firstTabStop.focus();
        }
      }
    },


    /**
     * Module initialization
     */
    init: function init() {
      this._updateCache();
      this._bindEvents();
    }
  };
})(window, jQuery, window.snro);
