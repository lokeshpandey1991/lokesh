/*
 * productdetail.js
 * [ This javascript code is used to initialize components on pdp. ]
 *
 * @project:    SN-RO
 * @date:       2017-08-03
 * @author:     Neha
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace productdetail
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  snro = window.snro = snro || {};

  // public methods
  var _initProductInfo = function _initProductInfo() {
    var twoColumnLayout = true;
    if ($('.js-pdp-feature-benefits').children().length === 0) {
      $('.js-pdp-feature-benefits').parents('.t-pdp-info-col').hide();
      twoColumnLayout = false;
    }

    if ($('.js-pdp-intended-use').children().length === 0) {
      $('.js-pdp-intended-use').parents('.t-pdp-info-col').hide();
      twoColumnLayout = false;
    }

    if (twoColumnLayout) {
      $('.js-pdp-info').addClass('t-pdp-info-col2');
    }
  };

  /**
   * @method init
   * @description this method is used to initialize public methods.
   * @memberof snro.productdetail
   * @example
   * snro.productdetail.init()
   */
  snro.productdetail = {

    init: function init() {
      _initProductInfo();
    }
  };
})(window, jQuery, window.snro);

/*
 * pdpfeaturespecs.js
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

(function (window, $, snro) {

  snro = window.snro = snro || {};

  /**
   * @method init
   * @description this method is used to initialize public methods.
   * @memberof snro.pdpfeaturespecs
   * @example
   * snro.pdpfeaturespecs.init()
   */
  snro.pdpfeaturespecs = {

    init: function init() {
      var specsCount = $('.js-system-specification-dtl .js-pdp-spec-item').length;
      if (!specsCount) {
        $('.js-specifications-container-dtl').hide();
      }
    }
  };
})(window, jQuery, window.snro);

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

/*
 * f_contactForm.js
 * [ This javascript code is used for Contact form. ]
 *
 * @project:    Roche Customerportal
 * @date:       2017-09-19
 * @author:     Vikash, Shubham
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace contactForm
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  var _cache = {};

  snro = window.snro || {};

  snro.contactForm = {
    moduleName: 'contactForm',
    interactedFieldList: [],
    formType: '',
    startCounter: 1,
    pageReferrer: document.referrer,

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.questionIcons = $('.guideHelpQuestionMark');
      _cache.checkboxPath = $('.c-contact-form .guideCheckBoxItem');
      _cache.checkboxLabel = $('.c-contact-form .guideCheckBoxItem label');
      _cache.submitButton = $('.js-contact-form .guidebutton button');
      _cache.checkboxLabel = $('.js-contact-form .guideCheckBoxItem label');
      _cache.submitButton = $('.js-contact-form .guidebutton button');
      _cache.actionPath = $('.js-contact-form .action input').val();
      _cache.currentURL = $('[name="currentPagepath"]').val();
      _cache.input = $('.js-contact-form input');
      _cache.textarea = $('.js-contact-form textarea');
      _cache.select = $('.js-contact-form .guideDropDownList select');
      _cache.labelText = $('.js-contact-form .guidedropdownlist label');
      _cache.formTitle = $('.js-form-title .title');
      _cache.errornode = $('.guideFieldNode  .guideFieldError');
      window.autosize(_cache.textarea);
      _cache.input.val(''); // IE9 fix
      _cache.textarea.val(''); // IE 9 fix
    },
    formStartAnalytics: function formStartAnalytics(fieldName) {
      if (this.startCounter < 2) {
        window.digitalData = {
          link: {
            linkPageName: 'contact-us',
            linkCategory: 'internal',
            linkSection: 'form',
            linkHeader: snro.contactForm.formType,
            linkText: fieldName,
            linkInteractionMethod: 'click-on-' + snro.analytics.getBrowserWidth(),
            formName: snro.contactForm.formType,
            formType: 'contact-us',
            event: 'form-start'
          }
        };
        window._satellite.track('form-tracking');
      }
      this.startCounter++;
    },
    getFormType: function getFormType() {
      if (document.getElementsByTagName('meta')['form-type'] === undefined) {
        this.formType = 'contact-us';
      } else {
        this.formType = document.getElementsByTagName('meta')['form-type'].content && document.getElementsByTagName('meta')['form-type'].content.toLocaleLowerCase() || '';
      }
    },


    // submitting form fields by AJAX
    submitForm: function submitForm() {
      var dataXML = '',
          captchaResponse = '';
      if (typeof window.grecaptcha !== 'undefined') {
        captchaResponse = $('#g-recaptcha-response').val();
      }
      window.guideBridge.getDataXML({
        success: function success(guideResultObject) {
          dataXML = guideResultObject.data;
        }
      });
      var date = new Date(),
          timeout = 30000,
          dataString = 'dataXML=' + dataXML + '&date=' + date + '&currentPagepath=' + _cache.currentURL + '&recaptcha=' + captchaResponse + '&referrer=' + this.pageReferrer;
      snro.pageNotification.getPageNotification(_cache.actionPath, dataString, timeout);
    },


    // getting errors from the form
    findErrorList: function findErrorList() {
      var errorList = '';
      _cache.errornode.each(function () {
        var _this = $(this).text();
        _this = _this.replace(/\s+/g, '-').toLowerCase();
        if (_this) {
          errorList += _this + '|';
        }
      });
      var str = errorList.slice(0, -1);
      return str;
    },


    // bind dom events
    bindEvents: function bindEvents() {
      _cache.checkboxPath.on('click', function () {
        var fieldName = $(this).find('input').attr('aria-label');
        fieldName = fieldName.replace(/\s+/g, '-').toLowerCase();
        snro.contactForm.interactedFieldList.push(fieldName);
        snro.contactForm.formStartAnalytics(fieldName);
        if ($(this).find('input').is(':checked')) {
          _cache.checkboxLabel.addClass('js-checked');
        } else {
          _cache.checkboxLabel.removeClass('js-checked');
        }
      });
      _cache.input.on('focus', function () {
        var fieldName = $(this).attr('aria-label');
        fieldName = fieldName.replace(/\s+/g, '-').toLowerCase();
        snro.contactForm.interactedFieldList.push(fieldName);
        snro.contactForm.formStartAnalytics(fieldName);
        $(this).closest('.guideFieldNode').addClass('js-active');
      });
      _cache.textarea.on('focus', function () {
        var fieldName = $(this).attr('aria-label');
        fieldName = fieldName.replace(/\s+/g, '-').toLowerCase();
        snro.contactForm.interactedFieldList.push(fieldName);
        snro.contactForm.formStartAnalytics(fieldName);
        $(this).closest('.guideFieldNode').addClass('js-active');
      });
      _cache.submitButton.on('click', function () {
        var captchaResp = '',
            captchaErrorHTML = '<div class="guideFieldError captcha-error">' + window.Granite.I18n.get('rdoe_ContactUs.CaptchaError') + '</div>';
        if (typeof window.grecaptcha !== 'undefined') {
          captchaResp = window.grecaptcha.getResponse();
          if (captchaResp === '' && !$('.captcha-error').length) {
            $('.g-recaptcha').after(captchaErrorHTML);
          } else if (captchaResp !== '') {
            $('.captcha-error').hide();
          }
        } else {
          captchaResp = 'empty';
        }
        if (window.guideBridge.validate() && captchaResp !== '') {
          snro.commonUtils.loader($('.c-contact-form'), true, true);
          snro.contactForm.submitForm();
          if (typeof window.grecaptcha !== 'undefined') {
            window.grecaptcha.reset();
          }
        } else {
          var errorList = snro.contactForm.findErrorList();
          $('html, body').animate({
            scrollTop: $('.guideFieldError').offset().top - 300
          }, 100);
          window.digitalData = {
            link: {
              linkPageName: 'contact-us',
              linkCategory: 'internal',
              linkSection: 'form',
              linkHeader: snro.contactForm.formType,
              linkText: _cache.submitButton.text().trim().toLowerCase(),
              linkInteractionMethod: 'click-on-' + snro.analytics.getBrowserWidth(),
              formName: snro.contactForm.formType,
              formType: 'contact-us',
              formError: errorList,
              event: 'form-errors'
            }
          };
          window._satellite.track('form-tracking');
        }
      });
      _cache.select.on('click', function () {
        var fieldName = $(this).attr('aria-label');
        fieldName = fieldName.replace(/\s+/g, '-').toLowerCase();
        snro.contactForm.interactedFieldList.push(fieldName);
        snro.contactForm.formStartAnalytics(fieldName);
      });
      window.addEventListener('beforeunload', function () {
        if ($('.js-contact-form').length) {
          var lastInteractedField = snro.contactForm.interactedFieldList.pop();
          window.digitalData = {
            link: {
              linkPageName: 'contact-us',
              linkCategory: 'internal',
              linkSection: 'form',
              linkHeader: snro.contactForm.formType,
              linkText: 'form-abandon',
              linkInteractionMethod: 'click-on-' + snro.analytics.getBrowserWidth(),
              formName: snro.contactForm.formType,
              formType: 'contact-us',
              lastAccessFormField: lastInteractedField,
              event: 'form-abandon'
            }
          };
          window._satellite.track('form-tracking');
        }
      });
      this.initializeTooltips();
    },


    /**
     * Initialize tooltips
     */
    initializeTooltips: function initializeTooltips() {

      // adding anchor tag to be used as questionIcon for accessbility purpose
      _cache.questionIcons.after('<a href="javascript:void(0)" class="x-hint-btn x-sub-hover"></a>');
      var hintBtns = $('.x-hint-btn');

      function hideHint(context) {
        context = context ? context : this;
        $(context).popover('hide').removeClass('active');
        $('.hint-text').css('margin-left', 0);
      }

      function showHint(context) {
        context = context ? context : this;
        $(context).popover('show').addClass('active');
        window.setTimeout(function () {
          $('.hint-text').css('margin-left', -1);
        });

        window.setTimeout(function () {
          $(window.document).one('click', function () {
            hideHint(context);
          });
        });
      }

      // preventing default behaviour of anchors used as hint button
      hintBtns.click(function () {
        if ($(this).hasClass('active')) {
          hideHint(this);
        } else {
          showHint(this);
        }
      });

      if (snro.commonUtils.isDesktop()) {
        hintBtns.hover(function () {
          showHint(this);
        }, function () {
          hideHint(this);
        }).on('focusout', function () {
          $(window.document).click();
        });
      }

      // asigning related descriptions to question icons required for bootstrap popover
      hintBtns.each(function () {
        var hintBtn = $(this);
        var hintText = hintBtn.siblings('.guideFieldDescription.long').text();
        hintBtn.attr('data-content', hintText);
      });

      hintBtns.popover({
        toggle: 'popover',
        placement: 'top',
        trigger: 'manual',
        animation: true
      }).map(function () {
        $(this).data('bs.popover').tip().addClass('hint-text');
      });
    },


    // Module initialization
    init: function init() {
      this.updateCache();
      this.bindEvents();
      this.getFormType();
    }
  };
  if ($('.js-contact-form').length) {
    snro.contactForm.init();
  }
})(window, window.jQuery, window.snro);

/*!
 * c_listingtile.js

 * This file contians date string manuplation  functions
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-07-17
 * @author    Suneel
 * @dependencies jQuery
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace Main
 * @memberof snro
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro = snro || {};

  /**
   * This function will be use date string manuplation and wrap in html dom.
   * Accepts any valid selector or jQuery object.
   * @param {selector, HTMLElement, or jQuery object} table
   */

  var caldate = function caldate() {
    var dataDataAttr = $('.c-listingtile__date');

    dataDataAttr.each(function () {
      var self = $(this);
      if (self.data('dateformat') && self.data('dateformat').split(/\s+/).length > 2) {
        var dataValue = self.data('dateformat').split(/\s+/);
        self.html(dataValue[0] + '<span>' + dataValue[1] + '</span>' + dataValue[2]);
      }
    });
  };

  snro.listingtile = {
    init: function init() {
      caldate();
    }
  };
})(window, window.jQuery, window.snro);

/*
 * c_listingwrapper.js
 * [ This javascript code is used to render the appropriate handlebars template based on data. ]
 *
 * @project:    SN-RO
 * @date:       2017-08-25
 * @author:     Prashant
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace listingwrapperCmp
 * @memberof roche
 */

(function (window, $, snro) {

  var _cache = { 'firstLoad': true },
      _populateAnalytics = function _populateAnalytics(data) {
    if (_cache.firstLoad) {
      var digitalData = window.digitalData || {},
          searchDA = {
        internalSearchTerm: snro.listingwrapperCmp.queryObject['q'],
        numberOfInternalSearchResults: data.query['total-results'],
        autoSuggestions: false
      },
          page = {
        filterCategory: snro.listingwrapperCmp.queryObject['categoryType'],
        filterOption: '',
        filterSorting: snro.listingwrapperCmp.queryObject['sortBy'] || 'relevance'
      };
      $.extend(digitalData.page, page);
      if (!_cache.plpPageProperties.length) {
        digitalData.search = digitalData.search || {};
        $.extend(digitalData.search, searchDA);
      }
      _cache.firstLoad = false;
      var _satellite = window._satellite || {
        track: function track() {
          snro.commonUtils.log('Satellite is not defined');
        }
      };
      _satellite.track('generic-link-tracking');
    }
  };

  snro = window.snro = snro || {};

  function _createQueryString(queryObject) {
    var updatedString = '?';
    $.each(queryObject, function (index, value) {
      if (value) {
        updatedString = updatedString + index + '=' + value + '&';
      }
    });
    return updatedString;
  }

  snro.listingwrapperCmp = {
    moduleName: 'listingwrapperCmp',
    upperResult: 0,
    offset: 0,
    total: 0,
    queryObject: {
      'q': '',
      'locale': '',
      'offset': '',
      'limit': ''
    },
    refreshList: true,
    reloadFilters: true,

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.listingWrapper = $('.js-listing-wrapper');
      _cache.searchURL = $('.js-link-search').data('searchurl');
      _cache.searchLimit = $('.js-link-search').data('searchlimit');
      _cache.plpPageProperties = $('.js-page-props');
      _cache.locale = $('meta[name=collection]').attr('content');
      _cache.persona = snro.commonUtils.getCookie('persona-type');
      _cache.currentCountElement = $('.js-current-count');
      _cache.currentCountText = _cache.currentCountElement.data('text');
      _cache.footerHeight = $('.js-footer').outerHeight(true) || 0;
      _cache.listing = _cache.plpPageProperties.data('listing');
      _cache.productCategoryDesc = _cache.plpPageProperties.data('ctd');
      _cache.productCategoryVal = _cache.plpPageProperties.data('ctv');
      _cache.productCategoryDescLvl_01 = _cache.plpPageProperties.data('ctd1');
      _cache.productCategoryValLvl_01 = _cache.plpPageProperties.data('ctv1');
      _cache.enableTemplate = $('.js-listing-container').data('enable-template');
      _cache.categoryType = '';
      _cache.moreTab = $('[data-tab-name=more]');
      _cache.poductTab = $('[data-tab-name=products]');
      _cache.scrollReady = true;
    },


    /**
     * function to extract url parameters for searching
     * @param url
     */
    getUrlParameter: function getUrlParameter() {
      var vars = {},
          hash = void 0;
      if (window.location.search) {
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for (var i = 0; i < hashes.length; i++) {
          hash = hashes[i].split('=');
          if (hash[1] && hash[1] !== 'undefined') {
            vars[hash[0]] = hash[1];
          }
        }
      }
      return vars;
    },


    updateQueryObject: function updateQueryObject(queryObject) {
      var updatedString = _createQueryString(queryObject);
      window.history.replaceState('', document.title, window.location.pathname + updatedString);
    },

    updateCount: function updateCount(upperResult, total) {
      var currentCountString = _cache.currentCountText && _cache.currentCountText.replace('{0}', upperResult || 0).replace('{1}', total || 0);
      _cache.currentCountElement && _cache.currentCountElement.html(currentCountString);
    },

    /**
     * function to select required template based on results
     * @param data - json data for the template
     */
    selectTemplate: function selectTemplate() {
      if (_cache.plpPageProperties.length) {
        return this.queryObject.categoryType === 'featured' && 'productFeaturedTile' || 'productListingTile';
      }
      var selectedTabId = $('.js-tab-item.selected').data('tab-name'),
          selectedWrapper = selectedTabId ? $('#' + selectedTabId).find('.js-listing-container') : $('.js-listing-container'),
          isTemplateEnabled = $(selectedWrapper).data('enable-template'),
          templateName = $(selectedWrapper).data('listing-template'),
          defaultTemplate = 'products';

      templateName = isTemplateEnabled ? templateName : defaultTemplate;

      switch (templateName) {
        case 'events':case 'news':
          templateName = 'newsEventsListingTile';
          break;
        case 'featured':
          templateName = 'productFeaturedTile';
          break;
        default:
          templateName = 'productListingTile';
      }
      return templateName;
    },


    /**
     * @param templateName - name of the hbs template to be rendered
     * @param data - json data for the hbs template
     */
    renderTemplate: function renderTemplate(templateName, data) {
      if (templateName) {
        var resultTemplate = window.roche.templates[templateName];
        return resultTemplate(data);
      }
    },


    /**
     * function display the results for search in the listing wrapper
     * based on the refresh parameter
     * @param data - json data to be rendered
     */
    renderResults: function renderResults(data) {
      var listingWrapper = this.getListingWrapper();
      _populateAnalytics(data);
      if (data.results && data.results.data && data.results.data.length > 0) {
        var results = data.results.data;
        this.total = data.query['total-results'];
        this.upperResult = data.query['upper-results'];
        var resultsHtml = this.renderTemplate(this.selectTemplate(), results);
        this.updateCount(this.upperResult, this.total);
        $('.js-loading-icon').remove();
        if (this.refreshList === true) {
          $(listingWrapper).html(resultsHtml);
        } else {
          $(listingWrapper).append(resultsHtml);
        }

        if (this.reloadFilters && this.queryObject.categoryType !== 'featured') {
          snro.searchFiltersCmp.init();
          snro.searchFiltersCmp.renderFilters(data.facets);
        }
        return;
      }

      if ($('.js-filter-facet').length) {
        this.updateCount();
        $(listingWrapper).html('<p class="no-result">' + window.Granite.I18n.get('rdoe_searchResultBar.searchNoResulText') + '</p>');
        return;
      }
      if (_cache.plpPageProperties.length) {
        $('.js-filter-container').html('');
        $('.js-filter-facets-list').html('');
      }
      if (!_cache.plpPageProperties.length && this.queryObject.categoryType !== 'more' && snro.commonUtils.getCookie('wcmmode') !== 'edit') {
        _cache.moreTab.click();
        _cache.poductTab.addClass('x-disable-click');
        return;
      }
      this.updateCount();
      $(listingWrapper).html('<p class="no-result">' + window.Granite.I18n.get('rdoe_searchResultBar.searchNoResulText') + '</p>');
    },


    /**
     * return result wrapper in case page has multiple wrappers/tabs
     */
    getListingWrapper: function getListingWrapper() {
      var selectedTabId = $('.js-tab-item.selected').data('tab-name'),
          selectedWrapper = selectedTabId ? $('#' + selectedTabId).find('.js-listing-wrapper') : $('.js-listing-wrapper');
      return selectedWrapper;
    },


    /**
     * call init method for other components
     */
    initDynamicComps: function initDynamicComps() {
      snro.listingtile.init();
    },


    /**
     * function get search results via xhr call based on url
     */
    getSearchResults: function getSearchResults() {
      var that = this,
          requestURL = _cache.searchURL + _createQueryString(this.queryObject),
          requestedUrl = '//' + decodeURI(requestURL).split('://')[1],
          listingWrapper = this.getListingWrapper();
      snro.commonUtils.loader(listingWrapper, true, false);
      // check if ie9
      if (navigator.appVersion.indexOf('MSIE 9') !== -1) {
        $.getJSON(requestedUrl).done(function (data) {
          that.renderResults(data);
          that.initDynamicComps();
          snro.dynamicMedia.processDynamicImages();
        }).always(function () {
          _cache.scrollReady = true;
        });
      } else {
        var options = {
          url: requestedUrl,
          type: 'GET',
          dataType: 'json'
        };
        snro.ajaxWrapper.getXhrObj(options).done(function (data) {
          that.renderResults(data);
          that.initDynamicComps();
          snro.dynamicMedia.processDynamicImages();
        }).fail(function (err) {
          snro.commonUtils.log(err);
        }).always(function () {
          _cache.scrollReady = true;
        });
      }
    },


    // bind dom events
    bindEvents: function bindEvents() {
      var queryObject = this.getUrlParameter(),
          that = this,
          timeout = void 0,
          selectedOption = snro.searhSortCmp.getDefaultSortOption(),
          selectedTabId = $('.js-tab-item.selected').data('tab-name'),
          selectedWrapper = selectedTabId ? $('#' + selectedTabId).find('.js-listing-container') : $('.js-listing-container'),
          isTemplateEnabled = $(selectedWrapper).data('enable-template'),
          templateName = $(selectedWrapper).data('listing-template');

      _cache.categoryType = isTemplateEnabled ? templateName : '';

      this.queryObject['q'] = queryObject['q'] || '';
      this.queryObject['locale'] = _cache.locale;
      this.queryObject['ps'] = _cache.persona;
      this.queryObject['offset'] = queryObject['offset'] || 0;
      this.queryObject['limit'] = _cache.searchLimit || 20;
      this.queryObject['categoryType'] = queryObject['categoryType'] || _cache.categoryType;
      this.queryObject['sortBy'] = selectedOption && selectedOption.value || '';
      this.queryObject['listing'] = _cache.listing ? _cache.listing : '';
      this.queryObject['autoSuggest'] = queryObject['autoSuggest'] || false;
      this.queryObject['ctd'] = _cache.productCategoryDesc || '';
      this.queryObject['ctv'] = _cache.productCategoryVal || '';
      this.queryObject['ctd1'] = _cache.productCategoryDescLvl_01 || '';
      this.queryObject['ctv1'] = _cache.productCategoryValLvl_01 || '';

      $(window).scroll(function () {
        if (_cache.scrollReady && $(window).scrollTop() >= $(document).height() - $(window).height() - _cache.footerHeight) {
          if (that.upperResult >= that.total) {
            return;
          }
          clearTimeout(timeout);
          timeout = setTimeout(function () {
            _cache.scrollReady = false;
            that.offset = that.upperResult / parseInt(that.queryObject['limit']) + 1;
            that.queryObject['offset'] = that.offset;
            that.updateQueryObject(that.queryObject);
            that.getSearchResults();
            that.refreshList = false;
          }, 200);
        }
      });

      //select default categoryType on page load
      if (queryObject['categoryType'] && !_cache.plpPageProperties.length) {
        $('[data-tab-name=' + queryObject['categoryType'] + ']').length ? $('[data-tab-name=' + queryObject['categoryType'] + ']').click() : this.getSearchResults();
        return;
      }

      this.getSearchResults();
    },


    enablePLPFilters: function enablePLPFilters() {
      var that = this;
      snro.searchFiltersCmp.init();
      snro.searchFiltersCmp.bindFilterFunctions();
      _cache.plpSSIProperties = $('.js-ssi-category-facet') && $('.js-ssi-category-facet').html() || '';
      try {
        _cache.plpSSIProperties = _cache.plpSSIProperties && JSON.parse(_cache.plpSSIProperties);
        if (_cache.plpSSIProperties) {
          var ssiProperties = _cache.plpSSIProperties.facets[0].values,
              filterMarkup = '';
          for (var i = 0; i < ssiProperties.length; i++) {
            filterMarkup += '<a href="javascript:void(0)" class="c-plp-filters js-plp-filters x-sub-hover" ' + 'data-value="' + ssiProperties[i].value + '">' + ssiProperties[i].displayKey + ' </a>';
          }
          if (snro.commonUtils.isMobileMode()) {
            $('.js-plp-filter-mobile').data('category', _cache.plpSSIProperties.facets[0].label).append(filterMarkup);
          } else {
            $('.js-prod-filter-container').data('category', _cache.plpSSIProperties.facets[0].label).append(filterMarkup);
          }

          $('.js-plp-filters').bind('click', function () {
            that.queryObject['categoryType'] = $(this).data('value') === 'featured' ? 'featured' : '';
            snro.searchFiltersCmp.init();
            snro.searchFiltersCmp.resetFilters(that.queryObject);
            if (that.queryObject['categoryType'] === 'featured') {
              delete that.queryObject['q1'];
              delete that.queryObject['x1'];
              $('.js-filter-container').html('');
              $('.js-filter-facets-list').html('');
            } else {
              that.queryObject['q1'] = $(this).data('value');
              that.queryObject['x1'] = $(this).parent().data('category');
            }
            that.refreshList = true;
            that.reloadFilters = true;
            that.queryObject['offset'] = '';
            that.getSearchResults();
          });
        }
      } catch (e) {
        snro.commonUtils.log(e);
      }
    },

    // Module initialization
    init: function init() {
      this.updateCache();
      if (_cache.plpPageProperties.length) {
        this.enablePLPFilters();
      }
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);

/*
 * c_searchbarfilter.js
 * [ This javascript code is used to show and hide the search tabs on the Search page. ]
 *
 * @project:    SN-RO
 * @date:       2017-08-31
 * @author:     Amit
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace searchBarFilterCmp
 * @memberof roche
 */

(function (window, $, snro) {

  var domElements = {};

  snro = window.snro = snro || {};

  snro.searchBarFilterCmp = {

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      domElements.searchTabs = $('.js-search-nav .js-tab-item');
    },


    // bind dom events
    bindEvents: function bindEvents() {
      domElements.searchTabs.on('click', function (e) {
        snro.listingwrapperCmp.queryObject['categoryType'] = $(this).data('tabName');
        snro.listingwrapperCmp.updateQueryObject(snro.listingwrapperCmp.queryObject);
        snro.searhSortCmp.onTabChange(e);
        if ($(this).data('tabName') !== 'documentation') {
          snro.listingwrapperCmp.offset = 0;
          snro.listingwrapperCmp.upperResult = 0;
          snro.listingwrapperCmp.total = 0;
          snro.listingwrapperCmp.refreshList = true;
          snro.listingwrapperCmp.reloadFilters = true;
          snro.listingwrapperCmp.getSearchResults();
        }
      });
    },


    // Module initialization
    init: function init() {
      this.updateCache();
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);

/*
 * c_searchsort.js
 * [ This javascript code is used to handle sorting events for search sort component. ]
 *
 * @project:    SN-RO
 * @date:       2017-09-05
 * @author:     Prashant
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace searhSortCmp
 * @memberof roche
 */

(function (window, $, snro) {

  var _cache = {};

  snro = window.snro = snro || {};

  snro.searhSortCmp = {
    moduleName: 'searhSortCmp',

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.sortBar = $('.j-sort-Bar');
      _cache.upArrow = _cache.sortBar.find('.j-sort-down-arrow');
      _cache.dropDownContainer = _cache.sortBar.find('.j-dropdown-container');
      _cache.listElement = _cache.dropDownContainer.find('li');
      _cache.sortInput = _cache.sortBar.find('.j-sort-input');
      _cache.mobileListElement = $('.js-content-link');
      _cache.currentDropdown = $('.j-sort-Bar .j-dropdown-container');
    },


    // bind dom events
    bindEvents: function bindEvents() {
      //Select default option
      this.selectDefaultOption();

      //Event handler to open/close sorting dropdown
      _cache.sortBar.on('click', function (e) {
        if (!snro.commonUtils.isMobileMode()) {
          e.stopPropagation();
          var target = e.currentTarget,
              targetDropdown = $(target).find('.j-dropdown-container');

          $(target).toggleClass('j-open');
          $(target).find('.j-sort-down-arrow').toggleClass('c-sort-up-arrow');

          if (targetDropdown.hasClass('show')) {
            targetDropdown.addClass('hide').removeClass('show');
          } else {
            targetDropdown.addClass('show').removeClass('hide');
          }
        } else {
          e.preventDefault();
          $('.sort-list-xs').fadeIn().removeClass('hidden');
          $('body').addClass('x-no-scroll');
        }
      });

      /**
       * Close open sorting dropdown if clicked anywhere outside the dropdown
       */
      if (!snro.commonUtils.isMobileMode()) {
        $('body').on('click', function () {
          if ($('.j-sort-Bar.j-open').length > 0) {

            var target = $('.j-sort-Bar.j-open'),
                targetDropdown = $(target).find('.j-dropdown-container');

            $(target).removeClass('j-open');
            $(target).find('.j-sort-down-arrow').removeClass('c-sort-up-arrow');
            targetDropdown.addClass('hide').removeClass('show');
          }
        });
      }

      _cache.sortBar.on('keydown', this.keyboardNavForSort);

      //Event handler for selecting option on mouse click
      _cache.listElement.on('click', this.selectSortOption.bind(this));

      //Event handler for selecting option on mobile
      _cache.mobileListElement.on('click', this.selectSortOption.bind(this));

      //Event handler for closing sorting overlay on mobile
      $('body').on('click', '.js-sort-dropdown-mobile .js-cross-icon', function (e) {
        e.preventDefault();
        $('.sort-list-xs').fadeOut().addClass('hidden');
        $('body').removeClass('x-no-scroll');
      });
    },


    /**
     * enable keyboard navigation for custom dropdown
     * @param e - DOM target for key event
     */
    keyboardNavForSort: function keyboardNavForSort(e) {
      var sortBar = $(e.currentTarget).closest('.j-sort-Bar');
      var dropdown = sortBar.find('.j-dropdown-container');

      if (e.keyCode === 13) {
        // enter

        e.preventDefault();

        if (dropdown.is(':visible')) {
          dropdown.find('li.active').trigger('click');
        } else {
          sortBar.trigger('click');
        }
      }
      if (e.keyCode === 38) {
        // up

        e.preventDefault();

        var selected = sortBar.find('li.active');
        sortBar.find('li').removeClass('active');

        if (selected.prev().length === 0) {
          selected.siblings().last().addClass('active');
        } else {
          selected.prev().addClass('active');
        }
      }
      if (e.keyCode === 40) {
        // down

        e.preventDefault();

        var _selected = sortBar.find('li.active');
        sortBar.find('li').removeClass('active');

        if (_selected.next().length === 0) {
          _selected.siblings().first().addClass('active');
        } else {
          _selected.next().addClass('active');
        }
      }
    },


    /**
     * select default option in dropdown
     */
    selectDefaultOption: function selectDefaultOption() {
      //Desktop
      var selectedOption = $('.js-sort-item.selected');

      $(selectedOption).each(function (index, option) {

        var selectedOptionValue = $(option).data('value');
        var selectedOptionText = $(option).text();

        var inputPlaceholder = $(option).closest('.j-sort-Bar').find('.j-sort-input');

        $(inputPlaceholder).val(selectedOptionText);
        $(inputPlaceholder).attr('placeholder', selectedOptionText);
        $(inputPlaceholder).attr('aria-label', selectedOptionText);
        $(inputPlaceholder).data('selectedOption', selectedOptionValue);
      });

      //Mobile
      var selectedMobileOption = $('.js-content-link.selected');

      $(selectedMobileOption).each(function (index, mobileOption) {
        $(mobileOption).find('input').prop('checked', true);
      });
    },
    selectSortOption: function selectSortOption(e) {
      var sortOptionText = $(e.currentTarget).text();
      var sortOptionValue = $(e.currentTarget).data('value') || '';
      var selectedTab = $(e.currentTarget).parents('.js-tab-view');
      var sortInput = selectedTab.length > 0 ? $(selectedTab).find(_cache.sortInput) : $(_cache.sortInput);
      var sortInputPreviousOption = $(sortInput).data('selectedOption');
      var listItemParent = $(e.currentTarget).parent();

      //set 'selected' & 'active' classes based on selected option
      if (snro.commonUtils.isMobileMode()) {
        $(listItemParent).find('.js-content-link').each(function (index, option) {
          $(option).removeClass('selected');
        });

        $(e.currentTarget).addClass('selected');
      } else {
        $(listItemParent).find('.js-sort-item').each(function (index, option) {
          $(option).removeClass('selected active');
        });

        $(e.currentTarget).addClass('selected active');
      }

      if ($(e.currentTarget).find('input').length) {
        $(e.currentTarget).find('input').prop('checked', true);
      }

      if (sortInputPreviousOption === sortOptionValue) {
        return;
      } else {
        $(sortInput).val(sortOptionText);
        $(sortInput).data('selectedOption', sortOptionValue);
        $(sortInput).attr('aria-label', sortOptionText);
      }

      //Update sort parameter
      if (sortOptionValue) {
        snro.listingwrapperCmp.queryObject['sortBy'] = sortOptionValue;
      } else {
        this.resetSort();
      }

      //close liipbox
      if ($('.js-sort-dropdown-mobile').is(':visible')) {
        this.closeLiipbox();
      }
      snro.listingwrapperCmp.queryObject['offset'] = '';
      snro.listingwrapperCmp.updateQueryObject(snro.listingwrapperCmp.queryObject);
      snro.listingwrapperCmp.refreshList = true;
      snro.listingwrapperCmp.getSearchResults();
    },
    closeLiipbox: function closeLiipbox() {
      $('.sort-list-xs').fadeOut().addClass('hidden');
      $('body').removeClass('x-no-scroll');
    },
    onTabChange: function onTabChange(e) {
      var tabView = $(e.currentTarget).data('tab-name'),
          selectedTab = $('#' + tabView),
          sortValue = void 0;

      if (snro.commonUtils.isMobileMode()) {
        var mobileSortList = selectedTab.find('.js-sort-list');
        var selectedMobileOption = mobileSortList.find('li.selected');
        sortValue = selectedMobileOption.data('value');
      } else {
        var inputPlaceholder = selectedTab.find('.j-sort-input'),
            selectedOption = selectedTab.find('.j-sort-Bar li.selected'),
            selectedOptionText = selectedOption.text();
        _cache.currentDropdown.addClass('hide').removeClass('show');

        $(inputPlaceholder).val(selectedOptionText);
        $(inputPlaceholder).attr('placeholder', selectedOptionText);
        $(inputPlaceholder).attr('aria-label', selectedOptionText);
        $(inputPlaceholder).data('selectedOption', selectedOption.data('value'));
        sortValue = selectedOption.data('value');
      }

      snro.searchFiltersCmp.init();
      snro.searchFiltersCmp.resetFilters(snro.listingwrapperCmp.queryObject);
      snro.listingwrapperCmp.queryObject['offset'] = '';
      snro.listingwrapperCmp.queryObject['sortBy'] = sortValue;
      snro.listingwrapperCmp.updateQueryObject(snro.listingwrapperCmp.queryObject);
    },
    getDefaultSortOption: function getDefaultSortOption() {
      var sortOption = {};
      var selectedTab = $('.js-tab-view:visible');
      var selectedOption = selectedTab.length > 0 ? selectedTab.find('.j-sort-Bar li.selected') : _cache.dropDownContainer ? _cache.dropDownContainer.find('li.selected') : '';
      if (selectedOption) {
        sortOption.text = $(selectedOption).text();
        sortOption.value = $(selectedOption).data('value');
      }
      return sortOption;
    },
    resetSort: function resetSort() {
      delete snro.listingwrapperCmp.queryObject['sortBy'];
      snro.listingwrapperCmp.updateQueryObject(snro.listingwrapperCmp.queryObject);
    },


    // Module initialization
    init: function init() {
      this.updateCache();
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);

/*
 * c_productlistingtile.js
 * [ This javascript code is used to show and hide the search fields on the home page main navigation. ]
 *
 * @project:    SN-RO
 * @date:       2017-08-25
 * @author:     Prashant
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace productListingTileCmp
 * @memberof roche
 */

(function (window, $, snro) {

  var _cache = {},
      _self = void 0,
      _listingComp = void 0,
      link = { filterOption: '' },
      _updateCache = function _updateCache() {
    _cache.facetContainer = $('.js-facets-container');
    _cache.facetControls = $('.js-filter-radio input[type=radio]');
    _cache.searchListing = $('.js-search-listing').length;
    _cache.filterContainer = $('.js-filter-container');
    _cache.filterContainerMobile = $('.js-filter-list');
    _cache.filterContainerMobileOverlay = $('.js-filter-list-overlay');
    _cache.filterContainerMobileList = $('.js-filter-facets-list');
    _cache.filterContainerMobileClose = $('.js-filter-list-overlay .js-cross-icon');
  };

  snro = window.snro = snro || {};

  snro.searchFiltersCmp = {

    queryFilterObject: {},

    updateSearchResult: function updateSearchResult() {
      _listingComp.getSearchResults();
    },

    populateFacets: function populateFacets(filters) {

      var facetTemplate = '<div class="c-filter__facet js-filter-facet">\n' + '    <span class="c-filter__cross js-filter-cross" data-facet="{0}" data-facet-category="{1}" data-facet-value="{2}"> \n' + '      <span class="c-filter__cross-wrapper">\n' + '        <span class="c-filter__cross--icon-bar"></span>\n' + '        <span class="c-filter__cross--icon-bar"></span>\n' + '      </span >\n' + '    </span >\n' + '    <span class="c-filter__facet-text">{3}</span></div>',
          facetMarkup = '',
          selectedTabId = $('.js-tab-item.selected').data('tab-name'),
          facetContainer = selectedTabId ? $('#' + selectedTabId).find('.js-facets-container') : $('.js-facets-container');

      $.each(filters, function (index, val) {
        var facetId = Object.keys(val)[0],
            //x1,x2 etc
        filterId = Object.keys(val)[1],
            //q1,q2 etc
        facetValue = val[Object.keys(val)[0]],
            //value of x1
        filterText = val[Object.keys(val)[2]]; //text corresponding to x1

        if (facetId === 'eventTiming') {
          filterText = facetValue;
        }

        facetMarkup += facetTemplate.replace('{0}', facetId).replace('{1}', filterId).replace('{2}', facetValue).replace('{3}', filterText);
      });
      $(facetContainer).html(facetMarkup);

      //bind functionality to cross icon
      $('.js-filter-cross').on('click', function () {
        $('input[value="' + $(this).data('facetValue') + '"]').attr('checked', false);
        if ($(this).data('facet') === 'eventTiming') {
          delete _listingComp.queryObject['eventTiming'];
          delete _listingComp.queryObject['currd'];
          delete _listingComp.queryObject['currm'];
          delete _listingComp.queryObject['curry'];
        }
        delete _listingComp.queryObject[$(this).data('facet')];
        delete _listingComp.queryObject[$(this).data('facetCategory')];
        $(this).parent().remove();
        _listingComp.queryObject['offset'] = '';
        _listingComp.updateQueryObject(_listingComp.queryObject);
        _listingComp.refreshList = true;
        _listingComp.reloadFilters = false;
        _listingComp.getSearchResults();
      });
    },

    bindFilterFunctions: function bindFilterFunctions() {
      var indexIncrement = $('.js-page-props').length ? 2 : 1;
      _cache.filterContainerMobile.unbind('click').bind('click', function () {
        _cache.filterContainerMobileOverlay.fadeIn().removeClass('hidden');
        $('body').addClass('x-no-scroll');
        _self.bindShowAllFilters();
      });

      _cache.facetControls.unbind('click').bind('click', function () {
        var filterObject = [],
            facetContainer = snro.commonUtils.isMobileMode() ? $('.js-filter-content-section input[type=radio]:checked') : $('.js-filter-container input[type=radio]:checked');

        link.filterOption = '';
        $(facetContainer).each(function (i, e) {
          var temp = {},
              index = i + indexIncrement;
          if (e.name && e.name.toLowerCase() !== 'eventtiming') {
            temp['q' + index] = e.value;
            temp['x' + index] = e.name;
            temp['qx' + index] = $(e).data('text');
            _self.queryFilterObject['q' + index] = e.value;
            _self.queryFilterObject['x' + index] = e.name;
          } else {
            temp['eventTiming'] = e.value;
            _self.queryFilterObject['eventTiming'] = e.value;
            _self.queryFilterObject['currd'] = _self.getTimeStamp('D');
            _self.queryFilterObject['currm'] = _self.getTimeStamp('M');
            _self.queryFilterObject['curry'] = _self.getTimeStamp('Y');
          }
          link.filterOption = link.filterOption + e.value + '|';
          filterObject.push(temp);
        });
        _self.populateFacets(filterObject);
        _listingComp.queryObject['offset'] = '';
        _listingComp.updateQueryObject(_listingComp.queryObject);
        _listingComp.queryObject = $.extend({}, _listingComp.queryObject, _self.queryFilterObject);
        _listingComp.refreshList = true;
        _listingComp.reloadFilters = false;
        var _satellite = window._satellite || {
          track: function track() {
            snro.commonUtils.log('Satellite is not defined');
          }
        },
            digitalData = window.digitalData || {};
        $.extend(digitalData.link, link);
        _satellite.track('filter-tracking');
        _listingComp.getSearchResults();
      });

      _cache.filterContainerMobileClose.unbind('click').bind('click', function (e) {
        e.preventDefault();
        _cache.filterContainerMobileOverlay.fadeOut().addClass('hidden');
        $('body').removeClass('x-no-scroll');
      });
    },

    bindShowAllFilters: function bindShowAllFilters() {
      $('.js-display-hidden').on('click', function () {
        $(this).nextAll('li.hidden').removeClass('hidden');
        $(this).remove();
      });
    },

    renderFilters: function renderFilters(facets) {
      var mdFacets = _self.removeLastNode(facets);
      var resultsFilters = window.roche.templates['searchFilters'](mdFacets);

      _cache.filterContainer.html(resultsFilters);
      _self.bindShowAllFilters();

      if (snro.commonUtils.isMobileMode()) {
        _cache.filterContainerMobileList.html($('.js-filter-facet-list').html());
        _cache.filterContainer.html('');
      }
      _updateCache();
      _self.bindFilterFunctions();
    },

    getTimeStamp: function getTimeStamp(dateComp) {
      var timestamp = new Date();
      if (dateComp === 'D') {
        var day = timestamp.getDate();
        day = (day < 10 ? '0' : '') + day;
        return day;
      } else if (dateComp === 'M') {
        var month = timestamp.getMonth() + 1;
        month = (month < 10 ? '0' : '') + month;
        return month;
      } else if (dateComp === 'Y') {
        var year = timestamp.getFullYear();
        return year;
      }
    },

    /*
     * This function accepts an array of json object &
     * 1. Removes aray value having key 'last'
     * 2. Adds i18Key value
     */
    removeLastNode: function removeLastNode(facets) {
      for (var i = 0; i < facets.length; i++) {
        for (var key in facets[i]) {
          if (key === 'last') {
            delete facets[i];
          } else if (Object.prototype.hasOwnProperty.call(facets[i], 'values')) {
            _self.removeLastNode(facets[i]['values']);
          }
        }
      }
      return facets;
    },

    /**
     * function to reset query object
     * Empty the div content from previously added tags
     */
    resetFilters: function resetFilters(queryObject) {
      //Remove facet tags
      _cache.facetContainer.each(function (index, container) {
        $(container).empty();
      });

      //Reset query object
      if (_self.queryFilterObject) {
        Object.keys(queryObject).forEach(function (itm) {
          Object.keys(_self.queryFilterObject).forEach(function (itm1) {
            if (itm === itm1) {
              delete queryObject[itm];
            }
          });
        });
      }
      return queryObject;
    },

    // Module initialization
    init: function init() {
      _updateCache();
      _self = this;
      _listingComp = snro.listingwrapperCmp;
    }
  };
})(window, window.jQuery, window.snro);

/*
 * c_socialfeed.js
 * [ This javascript code is used to display socialfeed component. ]
 *
 * @project:    SN-RO
 * @date:       2017-09-23
 * @author:     Vikash
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace socialfeedComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  var _cache = {};
  snro = window.snro = snro || {};
  snro.socialfeedComp = {
    moduleName: 'socialfeedComp',

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.feedsContent = $('.js-socialfeed-content');
    },


    /**
     * Initialize socialfeed
     */
    initializeSocialFeed: function initializeSocialFeed() {
      var _this = this;

      // Loading the widgets.js file asynchronously
      window.twttr = function (d, s, id) {
        var js = void 0,
            fjs = d.getElementsByTagName(s)[0],
            t = window.twttr || {};
        if (d.getElementById(id)) {
          return;
        }
        js = d.createElement(s);
        js.id = id;
        js.src = 'https://platform.twitter.com/widgets.js';
        fjs.parentNode.insertBefore(js, fjs);
        t._e = [];
        t.ready = function (f) {
          t._e.push(f);
        };
        return t;
      }(document, 'script', 'twitter-wjs');

      window.twttr.ready(function (twttr) {
        _this.createTimeline(twttr);

        // customize after rendering
        twttr.events.bind('rendered', function () {
          _cache.feedsContent.find('iframe:not(:last-child)').remove();
          _cache.feedsContent.height('auto');
        });

        // refressing timeline on interval of 3 minutes (180000 miliseconds)
        window.setInterval(function () {
          // fixing height of feed container untill updated timeline is rendered
          _cache.feedsContent.height(_cache.feedsContent.height());

          _this.createTimeline(twttr);
        }, 180000);
      });
    },
    createTimeline: function createTimeline(twttr) {
      var userName = _cache.feedsContent.data('userName');
      var limit = parseInt(_cache.feedsContent.data('limit'));
      twttr.widgets.createTimeline({
        sourceType: 'profile',
        screenName: userName
      }, _cache.feedsContent.get(0), {
        chrome: 'nofooter noheader transparent',
        linkColor: '#292F33',
        borderColor: '#fff',
        tweetLimit: limit
      });
    },


    // Module initialization
    init: function init() {
      this.updateCache();
      this.initializeSocialFeed();
    }
  };
})(window, window.jQuery, window.snro);

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

  var _cache = {};

  $.fn.isInViewport = function () {
    var elementTop = $(this).offset().top,
        elementBottom = elementTop + $(this).outerHeight(),
        viewportTop = $(window).scrollTop(),
        viewportBottom = viewportTop + $(window).height();

    return elementBottom > viewportTop && elementTop < viewportBottom;
  };

  snro.imagegalleryCmp = {
    moduleName: 'imagegalleryCmp',

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.visibleOnScroll = $('.js-visible-onscroll');
    },


    // bind dom events
    bindEvents: function bindEvents() {
      var that = this;

      that.bindRowsAnimation();

      $(window).on('resize scroll', function () {
        that.bindRowsAnimation();
      });
    },
    bindRowsAnimation: function bindRowsAnimation() {
      var that = this;

      _cache.visibleOnScroll.each(function (index, instance) {
        that.animateRowImages(instance);
      });
    },
    animateRowImages: function animateRowImages(ref) {
      if ($(ref).isInViewport()) {
        var target = ref,
            row = $(target).data('select-row'),
            animateContentClass = 'start_animation bottom-to-top';

        $('[data-row=' + row + ']').each(function (index, element) {
          var transitionDelay = index * 0.4 + 's';
          if (requestAnimationFrame) {
            $(element).css('animation-delay', transitionDelay).css('-webkit-animation-delay', transitionDelay).addClass(animateContentClass);
          }
        });
      }
    },


    // Module initialization
    init: function init() {
      this.updateCache();
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);

/*!
 * Cookie Disclaimer.js

 * This file contians  Cookie Disclaimer functions for Create Cookies to check the First time user.
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-07-17
 * @author    Nimesh
 * @dependencies jQuery
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace Main
 * @memberof snro
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro = snro || {};
  //moduleName: 'cookiesNot',
  /**
   * This Cookie Disclaimer functions for Create Cookies to check the First time user.
   * Accepts any valid selector or jQuery object.
   * @param {selector, HTMLElement, or jQuery object} table
   */
  var cookiesTag = $('.js-cookies-tag'),
      closeBtn = $('.js-closeBtn');
  //Hide the Cookie popup
  var cookiesPopupHide = function cookiesPopupHide() {
    cookiesTag.slideUp(500);
  };
  //Show the Cookie popup
  var cookiesPopupShow = function cookiesPopupShow() {
    cookiesTag.show().animate({ top: '0px' }, 500);
  };
  //Close cookie on button click
  var closeCookiepopup = function closeCookiepopup() {
    closeBtn.on('click', cookiesPopupHide);
  };
  var checkCookieDisclaimer = function checkCookieDisclaimer() {
    closeCookiepopup();
    var checkCookie = snro.commonUtils.getCookie('CookieDisclaimer');
    if (checkCookie !== '') {
      cookiesPopupHide();
    } else {
      checkCookie = cookiesPopupShow();
      if (checkCookie !== '' && checkCookie !== null) {
        snro.commonUtils.setCookie('CookieDisclaimer', 'Yes');
      }
    }
  };
  snro.cookiesDisclaimer = {
    init: function init() {
      checkCookieDisclaimer();
    }
  };
})(window, window.jQuery, window.snro);
