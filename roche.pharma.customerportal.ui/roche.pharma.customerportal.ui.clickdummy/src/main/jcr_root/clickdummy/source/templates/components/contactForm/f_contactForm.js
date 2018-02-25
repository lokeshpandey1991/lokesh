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
