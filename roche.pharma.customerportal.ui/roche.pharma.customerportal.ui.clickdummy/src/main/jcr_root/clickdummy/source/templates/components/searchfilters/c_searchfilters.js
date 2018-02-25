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
