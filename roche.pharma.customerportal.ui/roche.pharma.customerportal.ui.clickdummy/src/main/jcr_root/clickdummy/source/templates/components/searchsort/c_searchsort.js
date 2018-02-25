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
