/*
 * personapicker.js
 * [ This javascript code is to handle person picker on basis of cookie.
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
 * @namespace personaPickerComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function(window, $, snro) {
  snro = window.snro = snro || {};
  snro.personaPickerComp = {
    moduleName: 'personaPickerComp',
    cookieFlag: false,
    urlPersona: $('.js-header-persona-link').data('url'),
    redirectURL: '',
    cookieUpdate: false,
    personaTagMeta: $('meta[name="personaTag"]').attr('content'),
    metaTagAttribute: $('meta[name="personaTag"]').attr('content') ? $('meta[name="personaTag"]').attr('content').split(',')[0] : '',
    pageType: $('meta[name="page-type"]').length?$('meta[name="page-type"]').attr('content'):'otherPage',
    cookieValue:'',
    // check cookie if exists
    _checkCookie() {
      var cookie_value = snro.commonUtils.getCookie('persona-type');
      this._deleteCookie();
      if ($('.js-header-persona-link').length) {
        if (!cookie_value) {
          this._callPersonaPicker(this.urlPersona);
        } else if(snro.countrySelectorCmp.countrySelectorPassed) {
          $('.js-cross-icon').click();
        } else {
          this.cookieFlag = true;
          let cookie_type = snro.commonUtils.getCookie('persona-type');
          if (typeof window.Granite === 'undefined') {
            window.Granite = {
              I18n: {
                get() {}
              }
            };
          }
          $('.js-header-persona-link').text(window.Granite.I18n.get('rdoe_personapicker.' + cookie_type));
        }
      }
    },


    // check if cookie value is updating

    _cookieValueCheck(cookie) {
      let cookie_value = snro.commonUtils.getCookie('persona-type');
      if (cookie_value !== cookie) {
        this.cookieUpdate = true;
      } else {
        this.cookieUpdate = false;
      }
    },

    // selected persona show

    _selectPersona() {
      let cookie_value = snro.commonUtils.getCookie('persona-type');
      $('.js-persona-link').each(function() {
        let cookie = $(this).find('.js-persona-type').text().trim();
        if (cookie === cookie_value) {
          $(this).addClass('persona-selected');
        }
      });
    },

    // set cookie if persona selected.

    _setCookie(cookie_type) {
      let currentPersona = snro.commonUtils.getCookie('persona-type');
      snro.commonUtils.setCookie('persona-type', cookie_type, Infinity);
      if(this.pageType.toLocaleLowerCase()==='home' || (currentPersona !== cookie_type && currentPersona !== '')) {
        this._redirectPage();
      }
      if(this.metaTagAttribute === this.cookieValue) {
        snro.personaPickerComp._checkCookie();
      }
    },

    // redirecting to the home page
    _redirectPage() {
      window.location.href = this.redirectURL;
    },

    // delete persona cookie
    _deleteCookie() {
      if (!$('.js-header-persona-link').length) {
        snro.commonUtils.removeCookie('persona-type');
      }
    },

    // AJAX Call PersonaPicker
    _callPersonaPicker(url) {
      var options = {
        url: url,
        type: 'GET',
        dataType: 'html'
      };
      snro.ajaxWrapper.getXhrObj(options).done(function(data) {
        snro.liipboxComp._liipboxOpen(data);
        snro.personaPickerComp.redirectURL = $('#redirectURL').val();
        if (!snro.personaPickerComp.cookieFlag) {
          $('.js-c-persona-picker').addClass('no-cookie');
        } else {
          $('.persona-text.ellipsis').length && $('.persona-text.ellipsis').dotdotdot();
          snro.personaPickerComp._selectPersona();
        }
      }).fail(function(err) {
        snro.commonUtils.log(err);
      });
    },

    // binding click events
    _bindEvents(deepLinkInitiated) {
      let context = this,
        $modalPopUp = $('#myModalPopUp'),
        metaTagPersonaLevel = context.metaTagAttribute;
      $('body').on('click', '.js-persona-link', function() {
        let cookie_value = $(this).find('.js-persona-type').text().trim();
        context.cookieValue = cookie_value;
        if(metaTagPersonaLevel && context.personaTagMeta && context.personaTagMeta.indexOf(cookie_value) === -1 && typeof deepLinkInitiated === 'undefined') {
          snro.modalPopComp.updateTemplate(window.Granite.I18n.get('rdoe_deeplinkpopup.'+metaTagPersonaLevel),true);
          $modalPopUp.addClass('show');
          return;
        }
        snro.personaPickerComp._cookieValueCheck(cookie_value);
        $('.js-c-persona-picker').slideUp('fast', function() {
          if (snro.personaPickerComp.cookieUpdate) {
            snro.personaPickerComp._setCookie(cookie_value);
            snro.personaPickerComp._checkCookie();
          } else {
            var liipboxOb = document.getElementsByClassName('js-liipbox');
            liipboxOb[0].parentNode.removeChild(liipboxOb[0]);
          }
          $('body').removeClass('x-no-scroll');
        });
      });
      $('body').on('click', '.js-header-persona-link', function() {
        snro.personaPickerComp._callPersonaPicker(snro.personaPickerComp.urlPersona);
      });
    },

    // Module initialization
    init(deepLinkInitiated) {
      this._checkCookie();
      this._bindEvents(deepLinkInitiated);
    }
  };
})(window, jQuery, window.snro);
