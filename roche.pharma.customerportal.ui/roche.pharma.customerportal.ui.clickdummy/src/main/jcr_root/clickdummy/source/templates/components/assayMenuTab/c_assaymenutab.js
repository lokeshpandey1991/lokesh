/*!
 * Assa.js

 * This file contians  assaymenu functionality for product
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-10-10
 * @author    Shubham
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
  var _cache = {};

  snro = window.snro = snro || {};

  snro.assayMenuTab = {
    updateCache: function updateCache() {
      _cache.assayMenuNode = $('.js-assay-menu');
      _cache.assayMenuButton = $('.js-assay-menu__btn');
      _cache.assayMenuNodeDesktop = $('.js-assay-menu-desktop');
    },
    showTabView: function showTabView(tabName) {
      $('.js-item').removeClass('selected');
      $('[data-target=' + tabName + ']').addClass('selected');
      $('.js-tab-view').hide();
      $('.' + tabName).show();
    },
    defaultView: function defaultView() {
      if (!snro.commonUtils.isDesktop()) {
        _cache.assayMenuNode.find('.js-item').eq(1).addClass('selected');
      } else {
        _cache.assayMenuNodeDesktop.find('.js-item').eq(0).addClass('selected');
      }
      _cache.assayMenuNode.find('.js-tab-view').eq(1).show();
    },
    bindEvents: function bindEvents() {
      _cache.assayMenuNode.find('.js-view-btn').on('click', function () {
        _cache.assayMenuNode.find('.js-wrapper').show();
        _cache.assayMenuButton.hide();
      });
      _cache.assayMenuNode.find('.js-close').on('click', function () {
        _cache.assayMenuNode.find('.js-wrapper').hide();
        _cache.assayMenuButton.show();
      });
      _cache.assayMenuNode.find('.js-item').on('click', function (e) {
        e.preventDefault();
        if (!$(this).hasClass('selected')) {
          var tabName = $(this).data('target');
          snro.assayMenuTab.showTabView(tabName);
        }
      });
    },


    // Module initialization
    init: function init() {
      this.updateCache();
      this.defaultView();
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);
