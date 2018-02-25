/*
 * c_imagegallery.js
 * [ This javascript code is used to display animated image gallery when visible in user view. ]
 *
 * @project:    SN-RO
 * @date:       2015-04-17
 * @author:     Prashant
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace imagegalleryCmp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  snro = window.snro = snro || {};

  let _cache = {};

  $.fn.isInViewport = function () {
    let elementTop = $(this).offset().top,
      elementBottom = elementTop + $(this).outerHeight(),
      viewportTop = $(window).scrollTop(),
      viewportBottom = viewportTop + $(window).height();

    return elementBottom > viewportTop && elementTop < viewportBottom;
  };

  snro.imagegalleryCmp = {
    moduleName: 'imagegalleryCmp',

    // assignment of dom selectors to variables
    updateCache() {
      _cache.visibleOnScroll = $('.js-visible-onscroll');
    },

    // bind dom events
    bindEvents() {
      let that = this;

      that.bindRowsAnimation();

      $(window).on('resize scroll', function () {
        that.bindRowsAnimation();
      });
    },

    bindRowsAnimation() {
      let that = this;

      _cache.visibleOnScroll.each(function (index, instance) {
        that.animateRowImages(instance);
      });
    },

    animateRowImages(ref) {
      if ($(ref).isInViewport()) {
        let target = ref,
          row = $(target).data('select-row'),
          animateContentClass = 'start_animation bottom-to-top';

        $('[data-row=' + row + ']').each(function (index, element) {
          let transitionDelay = (index * 0.4) + 's';
          if (requestAnimationFrame) {
            $(element).css('animation-delay', transitionDelay)
              .css('-webkit-animation-delay', transitionDelay)
              .addClass(animateContentClass);
          }
        });
      }
    },

    // Module initialization
    init() {
      this.updateCache();
      this.bindEvents();
    }
  };

}(window, window.jQuery, window.snro));
