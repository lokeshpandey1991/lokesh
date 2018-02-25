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

  snro.productCategoryList = {
    loadCategoryContentAnimation: (selector) => {
      selector.find('.product-category__content--cardWrapper').each(function (index, element) {
        let transitionDelay = (index * 0.1) + 's';
        if (window.requestAnimationFrame) {
          $(element).css('animation-delay', transitionDelay)
          .css('-webkit-animation-delay', transitionDelay)
          .addClass('animated fadeInRight');
        }
      });
    },

    init: function() {
      this.loadCategoryContentAnimation($('.product-category:visible'));
      if (snro.commonUtils.getCookie('wcmmode') !== 'edit') {
        $('.js-product-list-container').each(function (index) {
          $(this).toggle(!index);
          if (!$(this).data('productCount')) {
            $('li.js-tab-item[data-tab-name=' + this.id + ']').hide();
          }
        });
      }
      snro.commonUtils.addEllipses();
    }
  };

}(window, jQuery, window.snro));