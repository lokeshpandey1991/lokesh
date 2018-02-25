/*
 * c_carousel.js
 * [ This javascript code is used to display carousel component. ]
 *
 * @project:    SN-RO
 * @date:       2015-08-02
 * @author:     Prashant
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace carouselComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  var _cache = {};

  snro = window.snro = snro || {};

  /**
   * Sync clone images with original images
   */
  function syncCloneImages() {
    var $carouselItemArray = $('.js-event-item');
    $carouselItemArray.each(function (index, item) {
      var origImgSrc = $(item).find('.js-event-asset').attr('src');
      $(item).find('.js-event-asset-cloned').attr('src', origImgSrc);
    });
  }

  snro.carouselComp = {
    moduleName: 'carouselComp',
    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.carouselContainer = $('.js-carousel-content');
      _cache.validChange = true; // variable to Validate carousel change event
    },


    // bind dom events
    bindEvents: function bindEvents() {
      this.initializeCarousel();
    },


    /**
     * Initialize carousal
     */
    initializeCarousel: function initializeCarousel() {
      _cache.carouselContainer.slick({
        variableWidth: true,
        arrows: false,
        adaptiveHeight: true,
        centerMode: false,
        slidesToShow: 4,
        draggable: false,
        dots: true,
        speed: 0,
        focusOnSelect: true,
        useTransform: false,
        responsive: [{
          breakpoint: 768,
          settings: {
            slidesToShow: 1,
            infinite: true,
            draggable: true,
            speed: 300,
            dots: true,
            accessibility: false
          }
        }]
      });

      /**
       * Function called just before the slide change
       */
      _cache.carouselContainer.on('beforeChange', function (event, slick, currentSlide, nextSlide) {

        //Prevent click event if target slide is currently active slide
        if (currentSlide === nextSlide) {
          _cache.validChange = false;
          return false;
        }

        _cache.validChange = true;

        var targetSlide = $(slick.$slides.get(currentSlide))[0];

        //if current slide has video - stop video before change
        var $jpPlayer = $(targetSlide).find('.jp-jplayer');
        $jpPlayer = $jpPlayer.length ? $($jpPlayer[0]) : null;
        if ($jpPlayer) {
          $jpPlayer.bind($.jPlayer.event.canplay + '.inactiveSlideVideo', function () {
            $jpPlayer.jPlayer('pause', 0);
            $jpPlayer.unbind('.inactiveSlideVideo');
          });
          $jpPlayer.bind($.jPlayer.event.play + '.inactiveSlideVideo', function () {
            $jpPlayer.unbind('.inactiveSlideVideo');
          });
          $jpPlayer.jPlayer('pause');
          $(targetSlide).find('.video-info').css('visibility', '');
          $(targetSlide).find('.jp-interface').css('visibility', 'hidden');
        }

        $('.slick-slide .c-carousel__event-asset').removeClass('slideInRight slideOutLeft');

        var $activeSlides = $('.slick-slide.slick-active'),
            activeSlidesImagesArray = [];
        //nextActiveSlidesIndexsArray = [];

        //Get total number of slides
        var slideCount = slick.slideCount;

        //Get Total number of active slides
        var activeSlides = $activeSlides.length;

        //Get all active slides image sources as array
        $activeSlides.each(function (index, slide) {
          activeSlidesImagesArray.push($(slide).find('.js-event-asset-cloned').attr('src'));
        });

        //Get all indexes of active slides after change as array
        for (var i = 0; i <= activeSlides; i++) {
          var newIndex = nextSlide + i < slideCount ? nextSlide + i : nextSlide + i - slideCount,
              $slideFragment = $(slick.$slides.get(newIndex));

          if (i === 0) {
            //$slideFragment.find('.js-asset-wrapper').addClass('slideInRight');
            $slideFragment.find('.js-asset-wrapper-cloned').removeClass('hidden').find('.js-event-asset-cloned').attr('src', activeSlidesImagesArray[i]);
          }
        }
      });

      /**
       * Function called after the slide change event is completed
       */
      _cache.carouselContainer.on('afterChange', function (event, slick) {
        //verify if user clicked on same slide
        if (!_cache.validChange) {
          return false;
        }

        //scroll slick dots to position inside screen view
        slick.$dots.find('.slick-active').scrollintoview({
          direction: 'horizontal'
        });

        //Adding classes for animation
        $('.slick-slide.slick-current .js-asset-wrapper').addClass('slideInRight').on('animationend webkitAnimationEnd oAnimationEnd', function () {
          //Removing animation classes after slide transition is complete
          if (window.requestAnimationFrame) {
            $('.js-asset-wrapper-cloned').addClass('hidden').removeClass('animated slideOutLeft');
            $('.slick-slide .c-carousel__event-content').addClass('fadeIn');
            $('.slick-slide .c-carousel__event-asset').removeClass('slideLeft slideInRight');
            syncCloneImages();
          }
        });
      });

      $('.slick-dots li').on('click', function () {
        $(this).parents('.slick-slider').find('.slick-slide.slick-current').trigger('sclick');
      });
    },


    // Module initialization
    init: function init() {
      this.updateCache();
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);
