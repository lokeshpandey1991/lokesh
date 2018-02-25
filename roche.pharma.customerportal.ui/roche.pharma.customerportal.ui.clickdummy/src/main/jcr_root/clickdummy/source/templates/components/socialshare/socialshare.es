/*
 * socialshare.es
 * [ Behavior for social share icons using sharethis ]
 *
 * @project:    SN-RO
 * @date:       2017-09-27
 * @author:     Shashank
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';
/**
 * @namespace video
 * @memberof roche
 *
 */

(function (window, $, snro) {
  snro = window.snro || {};

  // defining module
  snro.socialshare = {
    moduleName: 'socialshare',
    
    loadSTApi() {
      let apiKey = $('.js-st-container').data('shareid') || '59d313920b76a500114be43f';
      let script = document.createElement('script');
      script.type = 'text/javascript';
      script.src = '//platform-api.sharethis.com/js/sharethis.js#property='+apiKey+'&product=inline-share-buttons';
      script.async = true;
      document.getElementsByTagName('head')[0].appendChild(script);

      script.onload = script.onreadystatechange = function() {
        let socialIconArr = $('.js-social-btn-st').data('visible-icon').split(',');
        let intervalTimer = setTimeout(function() {
          $('.js-social-btn-st').find('[data-network]').each(function() {
            if(socialIconArr.indexOf($(this).data('network')) !== -1) {
              $('.js-st-container').addClass($(this).data('network'));
            }
          });
          clearTimeout(intervalTimer);
        },1000);
      };
    },

    bindAnalyticsEvents() {
      let pageName = $('[title]').text() ? $('title').text() : 'home';
      $('body').on('click','[data-network]', function() {
        let socialType = $(this).data('network');
        window.digitalData = {
          link: {
            linkPageName: pageName,
            linkCategory: 'external',
            linkSection: 'social',
            linkHeader: 'social',
            linkText: socialType,
            linkInteractionMethod: 'click-on-' + snro.analytics.getBrowserWidth(),
            social: socialType,
            event: 'social-share'
          }
        };
        window._satellite.track('generic-link-tracking');
      });
    },

    // Module initialization
    init() {
      this.loadSTApi();
      this.bindAnalyticsEvents();
      // window.sharethis.addEventListener('sharethis.ready', function() {
      //   snro.commonUtils.log('sharethis api is ready');
      // });

      // let socialIconArr = $('.js-social-btn-st').data('visible-icon').split(',');
      // $('.js-social-btn-st').find('[data-network]').each(function() {
      //   if(socialIconArr.indexOf($(this).data('network')) !== -1) {
      //     $('.js-st-container').addClass($(this).data('network'));
      //   }
      // });
    }
  };
})(window, window.jQuery, window.snro);