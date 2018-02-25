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
