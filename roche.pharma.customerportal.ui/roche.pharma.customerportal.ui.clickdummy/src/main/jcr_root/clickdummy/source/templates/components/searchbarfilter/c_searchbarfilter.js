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
