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
/* eslint-disable new-cap */

/**
 * @namespace Main
 * @memberof snro
 * @property {null} property - description of property
 */

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

(function (window, $, snro) {
  snro = window.snro = snro || {};

  /**
   * Generic loader constructor to customize where to place the loader. Accepts any valid selector or jQuery object
   * @param {selector, HTMLElement, or jQuery object} loader
   */
  function _Loader(loader) {
    if (!(loader instanceof jQuery)) {
      loader = $(loader);
    }
    this.length = loader.length;
    if (!this.length) {
      return this;
    }
    this.htmlText = loader[0].outerHTML;
    this.loader = loader;
    this[0] = loader[0];
  }

  /**
   * Generic loader prototype methods
   */
  _Loader.prototype = {
    constructor: _Loader,
    target: function target(domEle) {
      if (this.length) {
        $(domEle).html(this.loader);
      }
      return this;
    },
    appendTo: function appendTo(domEle) {
      if (this.length) {
        $(domEle).append(this.loader);
      }
      return this;
    },
    insertAfter: function insertAfter(domEle) {
      if (this.length) {
        $(domEle).after(this.loader);
      }
      return this;
    },
    insertBefore: function insertBefore(domEle) {
      if (this.length) {
        $(domEle).before(this.loader);
      }
      return this;
    },
    remove: function remove() {
      return this.loader.remove();
    }
  };

  window.globalCache = snro.globalCache = {
    regex: {
      mobile: /android|webos|iphone|blackberry|iemobile|opera mini/i,
      tablet: /ipad|android 3.0|xoom|sch-i800|playbook|tablet|kindle/i,
      breakpoint: /:(?=([\s\d]+)px)/i
    },
    UA: navigator.userAgent.toLowerCase(),
    isKeyboardFocus: false
  };

  // check device type
  function _testUAPortableDevices() {
    return snro.globalCache.regex.mobile.test(snro.globalCache.UA) || snro.globalCache.regex.tablet.test(snro.globalCache.UA);
  }

  // Fire event when breakpoint is changed
  function _triggerBreakpointChange() {
    var breakpointInfo = $(document.body).pseudoContent(':after').match(snro.globalCache.regex.breakpoint);
    if (+breakpointInfo[1]) {
      breakpointInfo[1] = +breakpointInfo[1];
      // If previous breakpoint does not match current breakpoint
      if (snro.globalCache.breakpoint !== breakpointInfo[1]) {
        snro.commonUtils.log('Desktop Mode: Breakpoint change');
        snro.globalCache.breakpoint = breakpointInfo[1];
        $(window).trigger('responsive', [breakpointInfo[1]]);
      }
    }
  }

  // define custom events to handle multiple events
  function _defineCustomEvents() {
    // New event for enter key
    $(document.body).on('keydown', function (e) {
      var keyPressEvent = null;
      if (e.keyCode === 13) {
        // Enter key
        keyPressEvent = $.Event('enterKeyPress', $.extend(e, { type: 'enterKeyPress' }));
        $(e.target).trigger(keyPressEvent);
      }
      if (e.keyCode === 27) {
        // Escape key
        keyPressEvent = $.Event('escapeKey', $.extend(e, { type: 'escapeKey' }));
        $(e.target).trigger(keyPressEvent);
      }
      if (e.keyCode === 9) {
        //  Tab key
        $('.focussed').removeClass('focussed');
        snro.globalCache.isKeyboardFocus = true;
      }
    });
    $(document.body).on('mousedown', function () {
      snro.globalCache.isKeyboardFocus = false;
    });
    // New event for simulated click, useful for google analytics events
    $(document.body).on('mouseup', function (e) {
      if (e.which === 1) {
        // left click
        var simulatedClickEvent = $.Event('sclick', $.extend(e, { type: 'sclick' }));
        $(e.target).trigger(simulatedClickEvent);
      }
    });

    // Focus events
    $(document.body).on('focusin', snro.keyboardAccess);

    $(document).on('mouseenter focusin', '.x-overlay-link', function () {
      var parentList = $(this).parents('.x-overlay-link-container');
      parentList.find('.x-overlay-link').not(this).stop(true).fadeTo(300, 0.5);
    });

    $(document).on('mouseleave focusout', '.x-overlay-link', function () {
      var parentList = $(this).parents('.x-overlay-link-container');
      parentList.find('.x-overlay-link').stop(true).fadeTo(300, 1);
    });

    // Events for breakpoint change
    if (_testUAPortableDevices()) {
      $(window).on('orientationchange', function () {
        snro.commonUtils.log('Mobile mode: Orientation change');
        $(window).trigger('responsive', $(window).outerWidth());
      });
    } else {
      var breakpointInfo = $(document.body).pseudoContent(':after').match(snro.globalCache.regex.breakpoint);
      if (+breakpointInfo[1]) {
        snro.globalCache.breakpoint = +breakpointInfo[1];
        $(window).on('resize', _triggerBreakpointChange);
      }
    }
  }

  var _initializeRedirectPopup = function _initializeRedirectPopup() {
    var popupData = {
      'title': window.Granite.I18n.get('rdoe_redirectpopup.title'),
      'subTitle': window.Granite.I18n.get('rdoe_redirectpopup.subtitle'),
      'bodyText': window.Granite.I18n.get('rdoe_redirectpopup.desc'),
      'ctaLabel': window.Granite.I18n.get('rdoe_redirectpopup.catlabel')
    },
        redirectPopupTemplate = window.roche.templates['redirectPopup'],
        redirectPopupHTML = redirectPopupTemplate(popupData);

    $('body').append(redirectPopupHTML);

    $('body').on('click', '.js-redirect-ok', function () {
      var targetLink = $('.js-redirect-popup').data('target');
      $('.js-redirect-popup').modal('hide');
      window.location.href = targetLink;
    });
  },
      _handleExternalLinks = function _handleExternalLinks() {
    $('body').on('click', 'a', function (e) {
      e.preventDefault();
      var givenTarget = $(this).attr('href').trim();
      if (givenTarget) {
        if (givenTarget.indexOf(location.hostname) === -1 && givenTarget.substr(0, 1) !== '/' && givenTarget.substr(0, 1) !== '#' && givenTarget.substr(0, 10) !== 'javascript') {

          $('.js-redirect-popup').modal({
            'keyboard': false,
            backdrop: 'static'
          });

          $('.js-redirect-popup').data('target', givenTarget);
        } else {
          window.location.href = givenTarget;
        }
      }
    });
  };

  snro.commonUtils = {
    moduleName: 'commonUtils', // Added for debug logs
    // check if mobile
    isMobile: function isMobile() {
      return snro.globalCache.regex.mobile.test(snro.globalCache.UA);
    },
    // check if viewport is equal to mobile (resized desktop browser)
    isMobileMode: function isMobileMode() {
      return $(window).outerWidth() < 768;
    },
    // check if tablet
    isTablet: function isTablet() {
      return snro.globalCache.regex.tablet.test(snro.globalCache.UA);
    },
    // check if viewport is equal to tablet (resized desktop browser)
    isTabletMode: function isTabletMode() {
      return $(window).outerWidth() >= 768 && $(window).outerWidth() < 992;
    },
    // check if desktop
    isDesktop: function isDesktop() {
      return !this.isMobileOrTablet();
    },
    // check if desktop or tablet viewport width
    isTabletOrDesktopMode: function isTabletOrDesktopMode() {
      return this.isTabletMode() || this.isDesktopMode();
    },
    // check if viewport width is qualified desktop
    isDesktopMode: function isDesktopMode() {
      return $(window).outerWidth() >= 992;
    },
    // check if mobile or tablet
    isMobileOrTablet: function isMobileOrTablet() {
      return this.isMobile() || this.isTablet();
    },
    // check if iframe is resized
    resizeIframe: function resizeIframe(obj) {
      if (obj instanceof window.Node && obj.nodeType === 1) {
        // Is DOM element other than document
        obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
      }
    },
    // set local cookie
    setCookie: function setCookie(key, value, exp, path, domain) {
      if (!(typeof key === 'string' && key.length)) {
        return; // Key is mandatory
      }
      if (typeof value !== 'string') {
        value = '';
      } //If value is invalid by default empty string will be set
      var dt = new Date();
      if (typeof exp === 'number') {
        if (exp === Infinity) {
          dt = new Date('Thu, 31 Dec 2037 00:00:00 GMT');
        } else {
          dt.setTime(dt.getTime() + exp * 24 * 60 * 60 * 1000);
        }
      }
      var expires = exp ? '; expires=' + dt.toUTCString() : '',
          cookiePath = '; path=' + (typeof path === 'string' ? path.trim() : '/'),
          defaultDomain = window.location.hostname,
          cookieDomain = '';
      if (defaultDomain === 'localhost') {
        // IE does not allow localhost domain
        if (typeof domain === 'string') {
          cookieDomain = '; domain=' + domain.trim();
        }
      } else {
        cookieDomain = '; domain=' + (typeof domain === 'string' ? domain.trim() : defaultDomain);
      }

      var secureCookieFlag = '';
      if (location.protocol === 'https:') {
        secureCookieFlag = '; secure';
      }
      document.cookie = key + '=' + value + expires + cookieDomain + cookiePath + secureCookieFlag;
    },
    // get cookie
    getCookie: function getCookie(key) {
      if (!(typeof key === 'string' && key.length)) {
        return '';
      }
      var cookieString = decodeURIComponent(document.cookie),
          index = 0,
          allCookies = [],
          c = '';
      key += '=';
      if ((allCookies = cookieString.split(';')).length) {
        for (; index < allCookies.length; index++) {
          if (~(c = allCookies[index].trim()).indexOf(key)) {
            return c.substring(key.length, c.length).trim();
          }
        }
      }
      return '';
    },
    // remove cookie
    removeCookie: function removeCookie(key, path, domain) {
      if (!(typeof key === 'string' && key.length)) {
        return false;
      }
      var cookiePath = typeof path === 'string' ? path : '/',
          defaultDomain = window.location.hostname,
          cookieDomain = '',
          deletedCookieString = '';
      if (defaultDomain === 'localhost') {
        // IE does not allow localhost domain
        if (typeof domain === 'string') {
          cookieDomain = '; domain=' + domain.trim();
        }
      } else {
        cookieDomain = '; domain=' + (typeof domain === 'string' ? domain.trim() : defaultDomain);
      }
      deletedCookieString = key + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC' + cookieDomain + '; path=' + cookiePath;
      document.cookie = deletedCookieString;
      return !this.getCookie(key).length; // Ensure if cookie has been deleted
    },
    // reset cookie
    resetCookie: function resetCookie(key, value, exp, path, domain) {
      this.removeCookie(key, path, domain);
      this.setCookie(key, value, exp, path, domain);
    },
    // set in storage (session / local / cookie) based on fallback
    storage: {
      available: typeof window.Storage === 'function', // True only if webstorage is available
      // Method to store key values in any available storages
      set: function set(key, value, isSession) {
        if (!(typeof key === 'string' && key.length)) {
          return;
        }
        isSession = typeof isSession === 'boolean' ? isSession : false; // By default localStorage will be used
        var vl = (typeof value === 'undefined' ? 'undefined' : _typeof(value)) === 'object' && value !== null ? JSON.stringify(value) : value;
        // Check if storage is defined
        if (this.available) {
          try {
            if (isSession) {
              window.sessionStorage.setItem(key, vl);
            } else {
              window.localStorage.setItem(key, vl);
            }
            return;
          } catch (e) {
            // catch error here
          }
        }
        // If control has reached here, it means storage operation was unsuccessful and we need to set a cookie instead
        if (isSession) {
          // Set a session cookie
          snro.commonUtils.setCookie(key, vl);
        } else {
          snro.commonUtils.setCookie(key, vl, Infinity);
        }
        return;
      },
      // Method to remove key from all available storages
      remove: function remove(key) {
        if (!(typeof key === 'string' && key.length)) {
          return false;
        }
        if (this.available) {
          try {
            window.localStorage.removeItem(key);
            window.sessionStorage.removeItem(key);
            return !window.localStorage.key(key) || !window.sessionStorage.key(key) || snro.commonUtils.removeCookie(key);
          } catch (e) {
            //catch error here
          }
        }
        return snro.commonUtils.removeCookie(key);
      },
      // Get stored values from all available storages
      getAll: function getAll(key, isSession) {
        var returnValue = [],
            cookieValue = null;
        isSession = typeof isSession === 'boolean' ? isSession : false;
        if (this.available) {
          try {
            if (Object.prototype.hasOwnProperty.call(window.sessionStorage, key) && !isSession) {
              returnValue.push({ value: window.sessionStorage.getItem(key), storage: 'sessionStorage' });
            }
            if (Object.prototype.hasOwnProperty.call(window.localStorage, key)) {
              returnValue.push({ value: window.localStorage.getItem(key), storage: 'localStorage' });
            }
          } catch (e) {
            // catch error here
          }
        }
        if ((cookieValue === snro.commonUtils.getCookie(key)).length) {
          returnValue.push({ value: cookieValue, storage: 'cookie' });
        }
        return returnValue.map(function (data) {
          try {
            data.value = JSON.parse(data.value);
            return data;
          } catch (e) {
            return data;
          }
        });
      },
      // Get stored value from first match
      get: function get(key, isSession) {
        var storedValue = null;
        isSession = typeof isSession === 'boolean' ? isSession : false;
        if (!isSession) {
          // Check session storage first. Session storage should always have priority over local storage
          storedValue = this.getFromSessionStorage(key);
          if (!storedValue) {
            storedValue = this.getFromLocalStorage(key);
          }
        } else {
          // If isSession is true, then session storage is forced. In means we cannot get value from local storage
          storedValue = this.getFromSessionStorage(key);
        }
        // If neither of the storages have value. It means value could be in cookies
        if (!storedValue && !(storedValue = this.getFromCookies(key))) {
          return; // Return undefined
        }
        // Return the value part if value object has been successfully received
        return storedValue.value;
      },
      // update the value in storage
      update: function update(key, callbackOrValue, isSession) {
        var value = this.get(key);
        if (typeof callbackOrValue === 'function') {
          this.set(key, callbackOrValue(value, key), isSession);
        } else {
          this.set(key, callbackOrValue, isSession);
        }
      },
      // Get stored value from local storage only
      getFromLocalStorage: function getFromLocalStorage(key) {
        return this.getAll(key, true).filter(function (valueOb) {
          return valueOb.storage === 'localStorage';
        })[0];
      },
      // Get stored value from session storage only
      getFromSessionStorage: function getFromSessionStorage(key) {
        return this.getAll(key).filter(function (valueOb) {
          return valueOb.storage === 'sessionStorage';
        })[0];
      },
      // Get stored value from cookies only
      getFromCookies: function getFromCookies(key) {
        return this.getAll(key).filter(function (valueOb) {
          return valueOb.storage === 'cookie';
        })[0];
      }
    },
    // add ellipsis to any element container with text
    addEllipses: function addEllipses() {
      $('.ellipses').dotdotdot({ watch: true });
    },

    // get fullname of country
    getCountryName: function getCountryName(countryCode) {
      if (typeof countryCode === 'string') {
        switch (countryCode.toLowerCase()) {
          case 'us':
            return 'United States';
          case 'uk':
            return 'United Kingdom';
          case 'fr':
            return 'France';
          case 'jp':
            return 'Japan';
          case 'cn':
            return 'China';
          case 'hk':
            return 'Hong Kong';
          case 'sg':
            return 'Singapore';
          default:
            return countryCode;
        }
      }
    },
    // check if user is logged in
    isLoggedIn: function isLoggedIn() {
      return !!this.getCookie('loggedIn');
    },
    // add loader to any target
    loader: function loader(target, appendMode) {
      var overlay = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;

      target = target || 'body';
      var loader = void 0,
          loadercol = $('<div>').addClass('loading-icon js-loading-icon').prepend('<span></span><span></span>');
      if (!overlay) {
        loader = loadercol;
      } else {
        loader = $('<div>').addClass('loader-backdrop').wrapInner(loadercol);
      }
      if (target) {
        if (appendMode) {
          $(target).append(loader);
        } else {
          $(target).html(loader);
        }
      } else {
        return new _Loader(loader);
      }
    },

    // log to console if debug mode
    log: function log() {
      try {
        // Enable logs if development environment is true or debugClientLibs param is provided
        if (snro.commonUtils.queryParams('debugClientLibs')) {
          console.log(arguments[0]);
        }
      } catch (e) {
        // catch error here
      }
    },
    // get value of any url query parameter
    queryParams: function queryParams(name, url) {
      if (!url) {
        url = location.href;
      }
      name = name.replace(/[\[]/, '\\\[').replace(/[\]]/, '\\\]');
      var regexS = '[\\?&]' + name + '=([^&#]*)',
          regex = new RegExp(regexS),
          results = regex.exec(url);
      return results === null ? null : results[1];
    },

    // initialize lazyloading of images
    lazyloadImages: function lazyloadImages() {
      $('[data-original]').lazyload({
        threshold: 999
      });
    },


    // Loader constructor
    Loader: _Loader,
    // init method
    init: function init() {
      // Added since init is mandatory for all modules
      this.addEllipses();
      _initializeRedirectPopup();
      _handleExternalLinks();
      _defineCustomEvents();
      // Adding commonUtils in global space
      if (!window.commonUtils) {
        // Unless there is another commonUtils js library available globally, add commonUtils to global namespace
        window.commonUtils = this;
      }
    }
  };
})(window, window.jQuery, window.snro);
