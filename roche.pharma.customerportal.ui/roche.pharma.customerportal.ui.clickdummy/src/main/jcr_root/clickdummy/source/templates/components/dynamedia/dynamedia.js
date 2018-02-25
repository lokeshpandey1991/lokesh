/*
 * Dynamic Media
 * [ This javascript code is to handle dynamic media images from Adobe server
 *
 * @project:    SN-RO
 * @date:       2015-07-27
 * @author:     Shashank
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro || {};

  // defining module
  snro.dynamicMedia = {
    moduleName: 'dynamicMedia',
    // process data attributes and apply best suited image as per viewport
    processImageAttributes: function processImageAttributes() {
      $('.js-dynamic-media').each(function () {
        var desktopSrc = $(this).attr('data-src_desktop'),
            tabletLandSrc = $(this).attr('data-src_tabletl'),
            tabletPortSrc = $(this).attr('data-src_tabletp'),
            mobileLandSrc = $(this).attr('data-src_mobilel'),
            mobilePortSrc = $(this).attr('data-src_mobilep');

        if (typeof desktopSrc !== 'undefined') {
          if (typeof tabletLandSrc === 'undefined') {
            tabletLandSrc = desktopSrc;
          }

          if (typeof tabletPortSrc === 'undefined') {
            tabletPortSrc = tabletLandSrc;
          }

          if (typeof mobileLandSrc === 'undefined') {
            mobileLandSrc = tabletPortSrc;
          }

          if (typeof mobilePortSrc === 'undefined') {
            mobilePortSrc = mobileLandSrc;
          }
        }

        if (window.matchMedia('(max-width: 414px)').matches || window.matchMedia('(max-width: 414px) and (-webkit-min-device-pixel-ratio: 2), (max-width: 414px) and (min-resolution: 192dpi), (max-width: 414px) and (min-resolution: 2dppx)').matches) {
          // mobile portrait
          $(this).attr('data-original', mobilePortSrc);
        } else if (window.matchMedia('(min-width: 415px) and (max-width: 767px)').matches || window.matchMedia('(min-width: 415px) and (max-width: 767px) and (-webkit-min-device-pixel-ratio: 2), (min-width: 415px) and (max-width: 767px) and (min-resolution: 192dpi), (min-width: 415px) and (max-width: 767px) and (min-resolution: 2dppx)').matches) {
          // mobile landscape
          $(this).attr('data-original', mobileLandSrc);
        } else if (window.matchMedia('(min-width: 768px) and (max-width: 991px)').matches || window.matchMedia('(min-width: 768px) and (max-width: 991px) and (-webkit-min-device-pixel-ratio: 2), (min-width: 768px) and (max-width: 991px) and (min-resolution: 192dpi), (min-width: 768px) and (max-width: 991px) and (min-resolution: 2dppx)').matches) {
          // tablet portrait
          $(this).attr('data-original', tabletPortSrc);
        } else if (window.matchMedia('(min-width: 992px) and (max-width: 1024px)').matches || window.matchMedia('(min-width: 992px) and (max-width: 1024px) and (-webkit-min-device-pixel-ratio: 2), (min-width: 992px) and (max-width: 1024px) and (min-resolution: 192dpi), (min-width: 992px) and (max-width: 1024px) and (min-resolution: 2dppx)').matches) {
          // tablet landscape
          $(this).attr('data-original', tabletLandSrc);
        } else {
          //desktop
          $(this).attr('data-original', desktopSrc);
        }
      });
      snro.commonUtils.lazyloadImages();
    },


    /**
     * function is responsible for creating image tag
     * as per dynamic media specification
     *
     * 1. It works by reading dynamic media configuration from
     * the page for the respective handlebars template used
     *
     * 2. Iterate over the images added dynamically using
     * pre-defined class
     *
     * 3. Add data attributes with dynamic media url along with
     * height, width paramters as per configuration
     *
     * 4. Call the global dynamic media functionality for
     * correctly replacing the image source
     */
    processDynamicImages: function processDynamicImages() {
      //
      var self = this;

      // select the media configuration available on page load
      var $config = $('.js-media-config');

      // static dynamic media url
      var baseURL = $config.data('media-url');

      // template used for generation of search results
      var template = $('.js-dynamic-image').data('template');

      // class used for selecting correct dynamic media configuration
      var templateClass = '.' + template;

      // get all data attributes from configuration
      var configAttr = $config.find(templateClass).data();

      // array used for storing all attributes (e.g. desktop, mobileL) & its properties(e.g. wid, hei)
      var attrArray = [];

      /**
       * Iterate over configured attributes and create attributes array
       * Example: [
       *   {
       *     'attribute': 'data-src_desktop',
       *     'variation': {
       *       'wid': '200',
       *       'height': '50'
       *     }
       *   }
       * ]
       */
      for (var attr in configAttr) {
        if (Object.prototype.hasOwnProperty.call(configAttr, attr)) {
          var dataAttrValue = $config.find(templateClass).data(attr);
          var wid = dataAttrValue && dataAttrValue.length > 0 ? dataAttrValue.split(',')[0] : '';
          var hei = dataAttrValue && dataAttrValue.length > 0 ? dataAttrValue.split(',')[1] : '';

          var dataAttr = {
            'attribute': '',
            'variation': {}
          };

          //  data source attribute
          dataAttr.attribute = 'data-src_' + attr;

          // width configuration
          if (wid) {
            dataAttr.variation.wid = wid;
          }

          // height configuration
          if (hei) {
            dataAttr.variation.hei = hei;
          }

          attrArray.push(dataAttr);
        }
      }

      /**
       * 1. Iterate over all images added through handlebars
       * 2. Add dynamic attributes
       * 3. Add dynamic attributes value by combining
       *   a. static url from configuration
       *   b. image src provided from json
       *   c. height, width parameters from Attribute array
       */
      $('.js-dynamic-image[data-processed=false]').each(function () {

        // save reference
        var that = this;

        // image source from json
        var imgSrc = $(that).data('src');

        for (var i = 0; i < attrArray.length; i++) {

          // dynamic attribute as read from configuration
          var dataSrcAttribute = attrArray[i].attribute,
              dataSrcValue = baseURL + imgSrc + '?',
              mediaParams = '';

          for (var _attr in attrArray[i].variation) {

            // add & symbol if one attribute already added
            if (mediaParams) {
              mediaParams = mediaParams + '&';
            }
            mediaParams = _attr + '=' + attrArray[i].variation[_attr];
          }

          // adding static url with dynamic media configuartion
          dataSrcValue = dataSrcValue + mediaParams;

          // adding data attribute
          $(that).attr(dataSrcAttribute, dataSrcValue);
        }

        /**
         * 1. set processed flag to true to prevent being processed for
         * dynamic added images
         * 2. add 'js-dynamic-media' class to be processed by global
         * dynamic media functionality
         */
        $(that).attr('data-processed', true).addClass('js-dynamic-media');
      });

      // call globall dynamic media functionality to process images
      self.processImageAttributes();
    },
    bindEvents: function bindEvents() {
      var self = this;
      $(window).on('resize orientationchange', function () {
        self.processImageAttributes();
      });
    },
    init: function init() {
      snro.commonUtils.log('dynamicMedia init');
      this.bindEvents();
      this.processImageAttributes();
    }
  };
})(window, window.jQuery, window.snro);
