/*
 * header.js
 * [ This javascript code is used to show and hide the search fields on the home page main navigation. ]
 *
 * @project:    SN-RO
 * @date:       2015-04-17
 * @author:     Shubham
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace headerNavigationComp
 * @memberof roche
 * @property {null} property - description of property
 */


(function(window, $, snro) {

  snro = window.snro = snro || {};

  // public methods

  let tabsmapping = {'tab1': 'productInfo',
      'tab2': 'productSpecs',
      'tab3' : 'documents',
      'tab4': 'relatedProducts'},

    _goToTab = (tabName) => {
      let tab = $('[data-tab-name='+tabName+']:visible');
      if(tab.length){
        snro.eLabDociFrame.init();
        $(tab).trigger('click');
      }
    },

    _showTabView = (tabName) => {
      $('.js-tab-item').removeClass('selected');
      $('[data-tab-name='+tabName+']:visible').addClass('selected');

      $('.js-tab-view').hide();
      $('#'+tabName).show();
      /*if($('.js-pdp-nav-tabs').length) {
        window.location.hash = tabName;
      }*/
      if(snro.commonUtils.isDesktopMode()){
        $('body, html').animate({
          scrollTop: $('.c-tab-view-container').offset().top - $('.c-header').height() -150 + 'px'
        }, 10);
      }
    },

    _showDefaultTab = () => {
      let index = Object
        .keys(tabsmapping)
        .map(function (i) {
          return tabsmapping[i];
        })
        .indexOf(window.location.hash.replace('#', ''));
      if (index === -1) {
        let defaultTab = $('.js-tabs-list').data('defaultTab') || 'tab1';
        let tabName = tabsmapping[defaultTab];
        snro.eLabDociFrame.init();
        $('[data-tab-name=' + tabName + ']').trigger('click');
      }
    },

    _bindEvents = () => {
      $('body').on('click','.js-tab-item', function(e){
        e.preventDefault();
        if(!$(this).hasClass('selected')){
          if($('.js-pdp-nav-tabs').length) {
            $(this).scrollintoview();
          }
          let tabName = $(this).data('tabName');
          _showTabView(tabName);
        }
      });
    };

  /**
   * @method init
   * @description this method is used to initialize public methods.
   * @memberof snro.pdpNavTabs
   * @example
   * snro.pdpNavTabs.init()
   */
  snro.pdpNavTabs = {

    init : () => {

      let visibleTabsCount = $('.js-tabs-list .js-tab-item:visible').length;
      // to hide tab component if ther is only one tab
      _bindEvents();


      // Specific to PDP page only
      if($('.js-pdp-nav-tabs').length){
        _showDefaultTab();
      }
      
      if(visibleTabsCount <= 1){
        $('.js-tabs-container').hide();
      }

      // for directly opening the specific tab on a page
      let hash = window.location.hash;
      if(hash) {
        let tabName = hash.split('#');
        tabName = tabName[1];
        _goToTab(tabName);
      }
    }
  };

}(window, jQuery, window.snro));

