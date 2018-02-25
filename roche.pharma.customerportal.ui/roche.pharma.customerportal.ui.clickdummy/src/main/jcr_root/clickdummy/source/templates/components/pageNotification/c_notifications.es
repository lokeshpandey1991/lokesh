/*
 * c_notifications.js
 * [ This javascript code is to show page leve notifications ]
 *
 * @project:    SN-RO
 * @date:       2017-08-17
 * @author:     shubham
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace pageNotification
 * @memberof snro
 * @property {null} property - description of property
 */

(function(window, $, snro) {
  snro = window.snro = snro || {};
  let notifyTitle = $('.js-title'),
    notifyDescription = $('.js-description'),
    notifyType = $('.js-notification-type'),
    pageNotify = $('.js-page-notification'),
    notifyClose = $('.js-page-notification .js-close'),
    submitButton = $('.js-contact-form .guidebutton button');

  snro.pageNotification = {
    getPageNotification(url,dataString, timeout) {
      let options = {
        url: url,
        type: 'POST',
        dataType: 'json',
        data: dataString
      };

      if(typeof timeout !=='undefined'){
        options['timeout'] = timeout;
      }

      snro.ajaxWrapper.getXhrObj(options).done(function(data) {
        $('.loader-backdrop').hide();
        notifyTitle.text(window.Granite.I18n.get(data.notificationTitle));
        notifyDescription.text(window.Granite.I18n.get(data.notificationDescription));
        notifyType.addClass('js-notification-' + data.notificationType);
        notifyType.find('.js-link').attr('href',data.notificationCTALink).text(data.notificationCTAText);
        pageNotify.removeClass('hidden');
        $('html,body').animate({ scrollTop: 0 }, 100);
        snro.pageNotification.triggerAnalytics(data);
      }).fail(function() {
        $('.loader-backdrop').hide();
        notifyTitle.text(window.Granite.I18n.get('rdoe_ContactUs.ErrorTitle'));
        notifyDescription.text(window.Granite.I18n.get('rdoe_ContactUs.ErrorMessage'));
        notifyType.addClass('js-notification-error');
        pageNotify.removeClass('hidden');
        $('html,body').animate({ scrollTop: 0 }, 100);
      });
    },

    triggerAnalytics(data){
      let notificationType = data.notificationType;
      if(notificationType ==='confirmation'){
        window.digitalData = {
          link: {
            linkPageName: 'contact-us',
            linkCategory: 'internal',
            linkSection: 'form',
            linkHeader: snro.contactForm.formType,
            linkText: submitButton.text().trim().toLowerCase(),
            linkInteractionMethod: 'click-on-' + snro.analytics.getBrowserWidth(),
            formName: snro.contactForm.formType,
            formType: 'contact-us',
            event: 'form-complete'
          }
        };
        window._satellite.track('form-tracking');
      } 
      else {
        window.digitalData = {
          link: {
            linkPageName: 'contact-us',
            linkCategory: 'internal',
            linkSection: 'form',
            linkHeader: snro.contactForm.formType,
            linkText: submitButton.text().trim().toLowerCase(),
            linkInteractionMethod: 'click-on-' + snro.analytics.getBrowserWidth(),
            formName: snro.contactForm.formType,
            formType: 'contact-us',
            formError: data.notificationType,
            event: 'form-errors'
          }
        };
        window._satellite.track('form-tracking');
      }  
    },

   // binding event for close event 
    bindEvents(){
      notifyClose.on('click', function() {
        pageNotify.addClass('hidden');
      });
    },
  
    init() {
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);
