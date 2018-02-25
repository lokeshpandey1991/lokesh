/*
 * c_popupcomponent.js
 * [ This javascript code is used to display popupcomponent component. ]
 *
 * @project:    SN-RO
 * @date:       2017-09-24
 * @author:     Bindhyachal
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace popupComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro = snro || {};
  var domElements = {
    $modalPopUp: $('#myModalPopUp'),
    $modalBackdrop: $('.js-modal-backdrop')
  };

  snro.modalPopComp = {
    moduleName: 'modalPopComp',
    personaCookieVal: snro.commonUtils.getCookie('persona-type'),
    personaMetaTagArr: $('meta[name="personaTag"]').attr('content'),
    personaMetaTag: $('meta[name="personaTag"]').attr('content') ? $('meta[name="personaTag"]').attr('content').split(',')[0] : '',
    currentChannel: $('input[name=currentChannel]').val(),
    redirectURL: $('#redirectURL').val(),
    pageType: $('meta[name="page-type"]').length ? $('meta[name="page-type"]').attr('content') : 'otherPage',
    isPersonaModalPopUp: false,
    dLinkWithNoMatchPersona: false,

    //initialize the modal
    initializeModal: function initializeModal() {
      var metaTagPersonaLevel = this.personaMetaTag,
          currentChannelI = this.currentChannel,
          modalBodyText = '',
          deepLinkPage = this.currentChannel !== snro.commonUtils.getCookie('homePagePath');
      if (this.pageType.toLocaleLowerCase() !== 'home') {
        if (!snro.commonUtils.getCookie('homePagePath') || deepLinkPage) {
          modalBodyText = deepLinkPage && snro.commonUtils.getCookie('homePagePath') ? window.Granite.I18n.get('rdoe_deeplinkpopupexists.' + currentChannelI) : window.Granite.I18n.get('rdoe_deeplinkpopup.' + currentChannelI);
          /*if(deepLinkPage && snro.commonUtils.getCookie('homePagePath')) {
            modalBodyText = window.Granite.I18n.get('rdoe_deeplinkpopupexists.' + currentChannelI);
          }*/
          this.updateTemplate(modalBodyText, false);
        } else if (metaTagPersonaLevel && this.personaMetaTagArr && this.personaMetaTagArr.indexOf(this.personaCookieVal) === -1) {
          modalBodyText = window.Granite.I18n.get('rdoe_deeplinkpopup.' + metaTagPersonaLevel);
          this.dLinkWithNoMatchPersona = true;
          this.updateTemplate(modalBodyText, false);
        }
        if (snro.commonUtils.getCookie('persona-type') !== '') {
          snro.personaPickerComp._checkCookie();
        }
        snro.countrySelectorCmp.init('deeplink');
      } else {
        snro.countrySelectorCmp.init();
      }
    },


    //update the template with data
    updateTemplate: function updateTemplate(modalBodyText, bool) {
      this.isPersonaModalPopUp = bool;
      domElements.$modalPopUp.find('.j-modal-title').text(window.Granite.I18n.get('rdoe_deeplinkpopup.title'));
      domElements.$modalPopUp.find('.j-modal-body-text').text(modalBodyText);
      domElements.$modalPopUp.find('.js-btn-secondary').text(window.Granite.I18n.get('rdoe_deeplinkpopup.agree'));
      domElements.$modalPopUp.find('.js-btn-primary').text(window.Granite.I18n.get('rdoe_deeplinkpopup.disagree'));
      domElements.$modalPopUp.addClass('show');
      domElements.$modalBackdrop.addClass('show');
    },

    // bind dom events
    bindEvents: function bindEvents() {
      var context = this,
          languageLocationCookie = context.currentChannel;
      $('body').on('click', '.j-close', function () {
        domElements.$modalPopUp.removeClass('show');
        domElements.$modalBackdrop.removeClass('show');
      });
      $('body').on('click', '.js-btn-secondary', function () {
        domElements.$modalPopUp.removeClass('show');
        domElements.$modalBackdrop.removeClass('show');
        if (context.dLinkWithNoMatchPersona) {
          snro.commonUtils.setCookie('persona-type', context.personaMetaTag, Infinity);
          snro.personaPickerComp._checkCookie();
          return;
        }
        if (!context.isPersonaModalPopUp) {
          snro.commonUtils.removeCookie('persona-type');
          snro.commonUtils.setCookie('homePagePath', context.currentChannel, Infinity);
          snro.commonUtils.setCookie('location-and-language', languageLocationCookie.replace('/', '|'), Infinity);
          snro.personaPickerComp.init();
        } else {
          domElements.$modalPopUp.removeClass('show');
          $('.js-c-persona-picker').slideUp('fast', function () {
            snro.personaPickerComp._setCookie(context.personaMetaTag);
            snro.personaPickerComp._checkCookie();
            var liipboxOb = document.getElementsByClassName('js-liipbox');
            liipboxOb[0].parentNode.removeChild(liipboxOb[0]);
            $('body').removeClass('x-no-scroll');
          });
        }
      });
      $('body').on('click', '.js-btn-primary', function () {
        if (!$('#persona-picker').length) {
          if (!snro.commonUtils.getCookie('homePagePath')) {
            snro.countrySelectorCmp.init();
          } else {
            snro.commonUtils.setCookie('homePagePath', context.currentChannel, Infinity);
            snro.commonUtils.setCookie('location-and-language', languageLocationCookie.replace('/', '|'), Infinity);
            context.redirectPage();
          }
        } else {
          snro.personaPickerComp._setCookie(snro.personaPickerComp.cookieValue);
          snro.personaPickerComp._checkCookie();
          context.redirectPage();
        }
        domElements.$modalPopUp.removeClass('show');
        domElements.$modalBackdrop.removeClass('show');
      });
    },

    // redirecting to the home page
    redirectPage: function redirectPage() {
      window.location.href = $('.c-header__link--logo').attr('href');
    },
    init: function init() {
      this.initializeModal();
      this.bindEvents();
      if (snro.commonUtils.getCookie('persona-type') !== '' && typeof snro.commonUtils.getCookie('persona-type') !== 'undefined') {
        snro.personaPickerComp._checkCookie();
      }
    }
  };
})(window, window.jQuery, window.snro);
