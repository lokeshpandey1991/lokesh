/*!
* c_countrySelectorCmp.js

* This file contains utility functions for countrySelectorCmp
*
* @project   SapientNitro Roche Diagonostics
* @date      2017-08-4
* @author    Amit
* @dependencies jQuery, CSSMaps
*/

'use strict';

/**
 * @namespace countrySelectorCmp
 * @memberof snro
 * @property {null} property - description of property
 */
(function (window, $, snro) {
  snro = window.snro = snro || {};
  let
    cookie_value,
    basePagePath,
    domElements = {
      currentPagePath: $('input[name=currentPagepath]'),
      countryPickerLink: $('.js-link-country'),
      currentChannel: $('input[name=currentChannel]')
    },
    continentClassMap = {
      c1: 'af',
      c2: 'as',
      c3: 'oc',
      c4: 'eu',
      c5: 'na',
      c6: 'sa'
    },
    _showCountries = (e) => {
      domElements.countryList.hide();
      let elementId = continentClassMap[e[0].className.split(' ')[0]];
      $('#'+elementId).hide().show();
      domElements.continentSelector.removeClass('active');
      $('#js-'+elementId).addClass('active');
    },

    _toggleCountryList = () => {
      domElements.countryList.hide();
      let continentUl = $('a[data-country-code=country-'+snro.countrySelectorCmp.countryCode.toLowerCase()+']').parents('ul');
      let elementId = (continentUl.length && continentUl[0].id) ||
        ($('#na').length && 'na') ||
        (domElements.countryList.length && domElements.countryList[0].id);
      domElements.confirmBox.hide();
      domElements.globalSiteBox.show();
      $('#'+elementId).hide().show();
      domElements.continentSelector.removeClass('active');
      $('#js-'+elementId).addClass('active');
    },

    _showInitialSelection = () => {
      let country = $('a[data-country-code=country-' + snro.countrySelectorCmp.countryCode.toLowerCase() + '][data-language='+snro.countrySelectorCmp.language.toLowerCase()+']');
      if(cookie_value) {
        let currentCountry = $('a[data-country-code=country-' + cookie_value.split('|')[0] + '][data-language='+ cookie_value.split('|')[1]+']');
        
        domElements.currentCountryText.removeClass('hidden');
        domElements.currentCountryText.text(
          currentCountry && domElements.currentCountryText.data('translatedText').replace('{0}', currentCountry.data('countryName')) || '');
        
        domElements.countryLanguageSelection.removeClass('active');
        currentCountry.addClass('active');
      }

      /* TODO: Include below code back when personalization has been fully implemented for deep links
        let samePage = domElements.currentPagePath.val() && domElements.currentPagePath.val().indexOf(basePagePath.split('/').slice(0,5).join('/'))>-1
        if (cookie_value && !samePage) {
        country = $('a[data-country-code=country-'+ cookie_value.split('|')[0].toLowerCase()+'][data-language='+cookie_value.split('|')[1].toLowerCase()+']');
        domElements.countryText
        .text(
          domElements.countryText
          .data('existing-cookie-string') &&
          domElements.countryText
          .data('existing-cookie-string')
          .replace('{0}', country.data('countryName'))
          .replace('{1}', country.data('languageText'))
          .replace('{2}', window.Granite.I18n.get('rdoe_personapicker.' + snro.commonUtils.getCookie('persona-type'))));
        domElements.confirmBox.show();
        domElements.globalSiteBox.hide();
        domElements.countryList.hide().css('visibility', 'hidden');
        snro.countrySelectorCmp.countryCode = cookie_value.split('|')[0].toLowerCase();
        return;
      } else */
      if(snro.countrySelectorCmp.locationDetected && country.length) {
        domElements.countryText
        .text(
          domElements.countryText
          .data('preselection-string') &&
          domElements.countryText
          .data('preselection-string')
          .replace('{0}', country.data('countryName'))
        );
        snro.countrySelectorCmp.language = (navigator.language || navigator.browserLanguage).split('-')[0].toLowerCase();
        domElements.confirmBox.show();
        domElements.globalSiteBox.hide();
        domElements.countryList.hide().css('visibility', 'hidden');
        return;
      }
      _toggleCountryList();
    },

    proceedWithDefaultSelection = () => {
      $('.js-country-language-selection[data-country-code=country-'+snro.countrySelectorCmp.countryCode.toLowerCase()+
        '][data-language='+snro.countrySelectorCmp.language.toLowerCase()+']').click();
    },

    _bindEvents = () => {
      // eslint-disable-next-line new-cap
      domElements.mapsContainer.CSSMap({
        'size': 540,
        'tooltips': 'floating-top-center',
        'responsive': 'auto',
        'tapOnce': true,
        'onClick': _showCountries,
        'onLoad': function () {
          let continentUl = $('a[data-country-code=country-'+snro.countrySelectorCmp.countryCode.toLowerCase()+']').parents('ul'),
            elementId = continentUl.length && continentUl[0].id || $('#na').length && 'na' || domElements.countryList.length && domElements.countryList[0].id;
          if (!$('[data-target=js-' + elementId + ']').hasClass('active-region')) {
            $('[data-target=js-' + elementId + '] span')[0].click();
          }
        }
      });
      domElements.continentSelector.on('click', function (e) {
        domElements.countryList.hide();
        $('#'+e.target.id.replace('js-', '')).hide().show();
        domElements.continentSelector.removeClass('active');
        $(this).addClass('active');
        if(!$('[data-target=' + this.id + ']').hasClass('active-region')) {
          $('[data-target=' + this.id + '] span')[0].click();
        }
      });
      domElements.preselectionConfirm.on('click', proceedWithDefaultSelection);
      domElements.preselectionDeny.on('click', function () {
        domElements.countryList.css('visibility', 'visible');
        _toggleCountryList();
      });
      domElements.countryLanguageSelection.on('click', function () {
        snro.commonUtils.setCookie('location-and-language',
          $(this).data('countryCode').replace('country-', '') +'|'+ $(this).data('language'),
          Infinity);
        snro.commonUtils.setCookie('homePagePath',
          $(this).data('channel'),
          Infinity);
        if($(this).data('href') !== domElements.currentPagePath.val()) {
          snro.commonUtils.removeCookie('persona-type');
          window.location.href = $(this).data('href');
          return;
        }
        snro.countrySelectorCmp.countrySelectorPassed = true;
        snro.personaPickerComp.init();
      });
      domElements.crossIconContainer.parent().toggle(snro.countrySelectorCmp.reselectionInitiated);
    },

    _getPageMarkup = () => {
      if($('.js-link-country').length) {
        let url = domElements.countryPickerLink.data('url') &&
          snro.countrySelectorCmp.language &&
          domElements.countryPickerLink.data('url').replace('.html', '.' + snro.countrySelectorCmp.language + '.html') ||
          domElements.countryPickerLink.data('url');
        var options = {
          url: url,
          type: 'GET',
          dataType: 'html'
        };
        snro.ajaxWrapper.getXhrObj(options).done(function (data) {
          snro.liipboxComp._liipboxOpen(data);
          domElements.countryList = $('.js-country-list');
          domElements.continentSelector = $('.js-continent-selector');
          domElements.confirmBox = $('.js-confirm-box');
          domElements.globalSiteBox = $('.js-global-site-box');
          domElements.countryText = $('#js-country-text');
          domElements.mapsContainer = $('#map-continents');
          domElements.preselectionConfirm = $('.js-preselection-yes');
          domElements.preselectionDeny = $('.js-preselection-no');
          domElements.countryLanguageSelection = $('.js-country-language-selection');
          domElements.crossIconContainer = $('.js-countryselector .js-cross-icon');
          domElements.currentCountryText = $('.js-current-country-text');
          _bindEvents();
          _showInitialSelection();
        }).fail(function (err) {
          snro.commonUtils.log(err);
        });
      }
    },

    _mapsSuccessCallback = (data) => {
      if (data.status === 'OK' && data.results && data.results[0] && data.results[0].address_components) {
        snro.countrySelectorCmp.countryCode = cookie_value.split('|')[0].toLowerCase();
        $.grep(data.results[0].address_components, function (obj) {
          if (obj && obj.types && obj.types.indexOf('country') > -1) {
            snro.countrySelectorCmp.countryCode = obj.short_name || '';
          }
        });
        snro.countrySelectorCmp.locationDetected = snro.countrySelectorCmp.countryCode;
      }
      _getPageMarkup();
    },

    _mapsErrorCallback = () => {
      snro.countrySelectorCmp.countryCode = '';
      _getPageMarkup();
    };


  snro.countrySelectorCmp = {
    countrySelectorPassed: false,
    language: '',
    countryCode: '',

    initializeLanguageSelector: () => {
      cookie_value = snro.commonUtils.getCookie('location-and-language');
      basePagePath = snro.commonUtils.getCookie('homePagePath');
      let samePage = domElements.currentChannel.val() === basePagePath;
      if(!snro.countrySelectorCmp.reselectionInitiated && cookie_value && samePage) {
        snro.personaPickerComp.init();
        return;
      }
      snro.countrySelectorCmp.language = cookie_value ?
        cookie_value.split('|')[1].toLowerCase() :
        (navigator.language || navigator.browserLanguage).split('-')[0].toLowerCase();
      navigator.geolocation.getCurrentPosition(function (loc) {
        $.get('https://maps.googleapis.com/maps/api/geocode/json?latlng=' + loc.coords.latitude + ',' + loc.coords.longitude)
        .then(_mapsSuccessCallback, _mapsErrorCallback);
      }, function () {
        snro.countrySelectorCmp.locationDetected =false;
        snro.countrySelectorCmp.countryCode = '';
        _getPageMarkup();
      }, {enableHighAccuracy: false});
    },

    init: (deepLinkInitiated) => {
      let cookiePersona = snro.commonUtils.getCookie('persona-type');
      $('.blank-liipbox').hide();
      if(typeof deepLinkInitiated === 'undefined') {  
        snro.countrySelectorCmp.initializeLanguageSelector();
      }
      if(typeof cookiePersona !== 'undefined' && cookiePersona !== '') {
        snro.personaPickerComp.init();
      }
      $('body').on('click', '.js-link-country', function () {
        snro.countrySelectorCmp.reselectionInitiated = true;
        snro.countrySelectorCmp.initializeLanguageSelector();
      });
    },
  };

}(window, window.jQuery, window.snro));
