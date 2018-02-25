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
