/*
 * header.js
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

  var _cache = {};

  snro.headerNavigationComp = {
    moduleName: 'navigationCmp',

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.searchIcon = $('.js-link-search');
      _cache.searchBarNavigation = $('.js-search-bar-navigation');
    },


    // handle stickyness of header section
    _stickyHeader: function _stickyHeader() {
      var headerObj = document.getElementsByClassName('js-header-wrapper'),
          lastscroll = 0,
          scrollPosition = void 0,
          className = 'js-scroll',
          reg = new RegExp('(\\s|^)' + className + '(\\s|$)');

      window.onscroll = function () {
        scrollPosition = window.pageYOffset;
        if (scrollPosition > lastscroll && scrollPosition > 0) {
          if (headerObj[0].className.indexOf(className) === -1) {
            headerObj[0].className += ' ' + className;
          }
        } else {
          headerObj[0].className = headerObj[0].className.replace(reg, '');
        }
        lastscroll = scrollPosition;
      };
    },

    // bind dom events
    bindEvents: function bindEvents() {
      _cache.searchIcon.on('click', this.onSearchIconClick);
    },

    //click event handler for hamburger menu
    onSearchIconClick: function onSearchIconClick() {
      if (_cache.searchBarNavigation.length > 0) {
        snro.liipboxComp._liipboxOpen(_cache.searchBarNavigation[0].innerHTML);
      }
    },


    // Module initialization
    init: function init() {
      this._stickyHeader();
      this.updateCache();
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);

/*
 * c_footer.js
 * [ This javascript code is used to  display the timestamps on the footer. ]
 *
 * @project:    SN-RO
 * @date:       2017-07-25
 * @author:     Prashant
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace footerCmp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  var _cache = {};

  snro = window.snro = snro || {};

  snro.footerCmp = {
    moduleName: 'footerCmp',

    // assignment of dom selectors to variables
    _updateCache: function _updateCache() {
      _cache.timeStamp = $('.js-timestamp');
    },


    // bind dom events
    _bindEvents: function _bindEvents() {
      _cache.timeStamp.text(this._getTimestamp());
    },
    _getTimestamp: function _getTimestamp() {
      var timestamp = new Date();
      var day = timestamp.getDate();
      var month = timestamp.getMonth() + 1;
      var year = timestamp.getFullYear();
      day = (day < 10 ? '0' : '') + day;
      month = (month < 10 ? '0' : '') + month;
      return day + '.' + month + '.' + year;
    },


    // Module initialization
    init: function init() {
      this._updateCache();
      this._bindEvents();
    }
  };
})(window, window.jQuery, window.snro);

/*
 * c_heromedia.js
 * Set methods for video playback in hero banner
 *
 * @project:    SN-RO
 * @date:       2017-07-28
 * @author:     Aftab
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */
'use strict';

var snro = window.snro || {};

/**
 * @namespace heroMedia
 * @memberof roche
 */
snro.heroMedia = function (window, $) {

  var $heroBannerCnt = $('.heroBannerContainer'),
      videoTracking = void 0;

  /**
   * @method init
   * @description Initialize for video playback functions
   * @memberof snro.heroMedia
   * @example
   * snro.heroMedia.init()
   */
  var init = function init() {
    var assetType = '';
    var videoPlayerFunction = function videoPlayerFunction(context, playerId, containerId, asseType) {
      var videoLink = $('#' + playerId).data('fileref'),
          $videoPlayBtn = $(context).find('.jp-video-play'),
          $playBtn = $(context).find('.vid-play'),
          $fullScrBtn = $(context).find('.full-scr'),
          $volCtrlBtn = $(context).find('.vol-ctrl');

      if (asseType === 'video') {
        $('#' + playerId).jPlayer({
          ready: function ready() {
            $(this).jPlayer('setMedia', {
              m4v: videoLink,
              ogv: videoLink,
              webmv: videoLink,
              wav: videoLink
            });
            $(this).find('video').css('width', 'auto').css('height', '100%').css('max-width', '100%');
          },
          preload: 'metadata',
          swfPath: 'http://jplayer.org/latest/js',
          cssSelectorAncestor: '#' + containerId,
          supplied: 'webmv, ogv, m4v',
          globalVolume: true,
          useStateClassSkin: true,
          autoBlur: false,
          smoothPlayBar: true,
          keyEnabled: true,
          size: {
            width: '100%',
            height: '100%',
            cssClass: 'jp-video-360p'
          },
          ended: function ended() {
            $(this).jPlayer('setMedia', {
              m4v: videoLink,
              ogv: videoLink,
              webmv: videoLink,
              wav: videoLink
            });
            $(this).jPlayer('playHead', 0);
            $(context).find('.jp-interface').css('visibility', 'hidden');
            $(context).find('.layer-opaque').css('visibility', 'visible');
            $(context).find('.js-video-info').css('display', 'block');
          }
        });

        $('#' + playerId).bind($.jPlayer.event.seeked, function (event) {
          var seekedPercent = parseInt(event.jPlayer.status.currentPercentAbsolute);
          if (seekedPercent === 100) {
            $.extend(window.digitalData['link'], { event: 'video-completes' });
            window._satellite.track('video-tracking');
            videoTracking['video-100%'] = 'fired';
          } else if (seekedPercent >= 75) {
            $.extend(window.digitalData['link'], { event: 'video-75%complete' });
            window._satellite.track('video-tracking');
            videoTracking['video-75%'] = 'fired';
          } else if (seekedPercent >= 50 && seekedPercent < 75) {
            $.extend(window.digitalData['link'], { event: 'video-50%complete' });
            window._satellite.track('video-tracking');
            videoTracking['video-50%'] = 'fired';
          } else if (seekedPercent >= 25 && seekedPercent < 50) {
            $.extend(window.digitalData['link'], { event: 'video-25%complete' });
            window._satellite.track('video-tracking');
            videoTracking['video-25%'] = 'fired';
          } else {
            // do nothing
          }
        });

        $('#' + playerId).bind($.jPlayer.event.timeupdate, function (event) {
          //as above, grabbing the % location and media being played
          var playerTime = event.jPlayer.status.currentPercentAbsolute;

          //There's some overlap between the seeked and stopped events. When a user clicks
          // the stop button it actually sends a "seek" to the 0 location. So if the seeked location is 0
          // then we track it as a stop, if it's greater than 0, it was an actual seek.
          if (playerTime >= 2 && playerTime < 3) {
            $.extend(window.digitalData['link'], { event: 'video-play' });
          } else if (playerTime >= 25 && playerTime < 26 && !videoTracking['video-25%']) {
            $.extend(window.digitalData['link'], { event: 'video-25%complete' });
            window._satellite.track('video-tracking');
            videoTracking['video-25%'] = 'fired';
          } else if (playerTime >= 50 && playerTime < 51 && !videoTracking['video-50%']) {
            $.extend(window.digitalData['link'], { event: 'video-50%complete' });
            window._satellite.track('video-tracking');
            videoTracking['video-50%'] = 'fired';
          } else if (playerTime >= 75 && playerTime < 76 && !videoTracking['video-75%']) {
            $.extend(window.digitalData['link'], { event: 'video-75%complete' });
            window._satellite.track('video-tracking');
            videoTracking['video-75%'] = 'fired';
          } else if (playerTime >= 99 && playerTime < 100 && !videoTracking['video-100%']) {
            $.extend(window.digitalData['link'], { event: 'video-completes' });
            window._satellite.track('video-tracking');
            videoTracking['video-100%'] = 'fired';
          }
        });

        $('#' + playerId).jPlayer('volume', 0);
        $(context).find('input[type="range"]').rangeslider({
          polyfill: false,
          onSlide: function onSlide(position, value) {
            $('#' + playerId).jPlayer('volume', value / 100);
            if (0 === value && !$volCtrlBtn.hasClass('mute')) {
              $volCtrlBtn.addClass('mute');
            } else if ($volCtrlBtn.hasClass('mute')) {
              $volCtrlBtn.removeClass('mute');
            }
          }
        });
        $(context).find('.rangeslider__handle').css('left', 0);
        $(context).find('.rangeslider__fill').css('width', '7px');
        $volCtrlBtn.addClass('mute');
        // Attach listeners
        $videoPlayBtn.on('click', function (e) {
          var $parentContainer = $(e.currentTarget).parents('.heroBannerContainer');
          $parentContainer.find('.jp-interface').css('visibility', 'visible');
          $parentContainer.find('.layer-opaque').css('visibility', 'hidden');
          $parentContainer.find('.js-video-info').css('display', 'none');
          $parentContainer.find('.vid-play').trigger('click');
        });
        // Play/Pause
        $playBtn.on('click', function (e) {
          videoTracking = {
            'video-25%': '',
            'video-50%': '',
            'video-75%': '',
            'video-100%': ''
          };
          var $jPlayer = $(e.currentTarget).parents('.heroBannerContainer').find('.jp-jplayer').attr('id'),
              $playButton = $(e.currentTarget).parents('.heroBannerContainer').find('.vid-play'),
              $jContainer = $(e.currentTarget).parents('.heroBannerContainer').find('.jp-audio').attr('id');

          if ($('#' + $jContainer).hasClass('jp-state-playing')) {
            $('#' + $jPlayer).jPlayer('pause');
            $playButton.removeClass('pause').addClass('play');
          } else {
            $('#' + $jPlayer).jPlayer('play');
            $playButton.removeClass('play').addClass('pause');
          }
        });
        // Full-screen
        $fullScrBtn.on('click', function (e) {
          var currentPlayerId = $(e.currentTarget).parents('.heroBannerContainer').find('.jp-jplayer').attr('id'),
              $currjPlayer = $('#' + currentPlayerId).get(0);
          if ($currjPlayer.requestFullscreen) {
            $currjPlayer.requestFullscreen();
          } else if ($currjPlayer.msRequestFullscreen) {
            $currjPlayer.msRequestFullscreen();
          } else if ($currjPlayer.mozRequestFullScreen) {
            $currjPlayer.mozRequestFullScreen();
          } else if ($currjPlayer.webkitRequestFullscreen) {
            $currjPlayer.webkitRequestFullscreen();
          }
        });
        // Volume control
        $volCtrlBtn.on('click', function (e) {
          var $volCtrlBtn = $(e.currentTarget).parents('.heroBannerContainer').find('.vol-ctrl'),
              $jPlayer = $(e.currentTarget).parents('.heroBannerContainer').find('.jp-jplayer').attr('id');
          if ($volCtrlBtn.hasClass('mute')) {
            $volCtrlBtn.removeClass('mute');
            $('#' + $jPlayer).jPlayer('volume', 1);
          } else {
            $volCtrlBtn.addClass('mute');
            $('#' + $jPlayer).jPlayer('volume', 0);
          }
        });
      }
    };

    $($heroBannerCnt).each(function (index) {
      if ($(this).data('assettype') === 'video') {
        assetType = $(this).data('assettype');
        index = index + 1;
        $(this).find('#jquery_jplayer_1').attr('id', 'jquery_jplayer_' + index);
        $(this).find('#jp_container_1').attr('id', 'jp_container_' + index);
        videoPlayerFunction($(this), 'jquery_jplayer_' + index, 'jp_container_' + index, assetType);
      }
    });
  };

  // Public API
  return {
    init: init
  };
}(window, jQuery);

jQuery(snro.heroMedia.init());

/*
 * liipbox.js
 * [ This javascript code is to open overlay which will act as getter for HTML content to display.
 *
 * @project:    SN-RO
 * @date:       2017-07-25
 * @author:     shubham
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace liipboxComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  var _cache = {};

  snro = window.snro = snro || {};

  /**
   *  function to close liipbox
   */
  function closeLiipbox() {
    var liipbox = $('.js-liipbox'),
        className = 'x-no-scroll',
        reg = new RegExp('(\\s|^)' + className + '(\\s|$)');

    if (liipbox.length > 0) {
      liipbox.remove();
      _cache.focusedElementBeforeModal.focus();
      document.body.className = document.body.className.replace(reg, '');
    }
  }

  /**
   * function to provide open animation to the liipbox (overlay)
   */
  function liipboxAnimation(event) {

    var toggleLiipbox = (event === 'close') > 0 ? true : false,


    // jquery selectors
    linkSelector = toggleLiipbox ? $('.js-liipbox').find('.link-row').get().reverse() : $('.js-liipbox').find('.link-row'),
        logoSelector = $('.js-liipbox').find('.js-liipbox-logo'),
        crossIcon = $('.js-liipbox').find('.js-cross-icon'),
        liipboxOverlay = $('.js-liipbox').find('.js-liipbox-content'),
        animateHeaderClass = toggleLiipbox ? 'animated fadeOut' : 'animated fadeIn',
        animateContentClass = toggleLiipbox ? 'animated fadeOutDown' : 'animated fadeInUp',
        animationEndCallback = toggleLiipbox ? closeLiipbox : '';

    // animate content
    if (linkSelector.length > 0) {
      $(linkSelector).each(function (index, element) {
        var transitionDelay = index * 0.04 + 's';
        if (window.requestAnimationFrame) {
          $(element).css('animation-delay', transitionDelay).css('-webkit-animation-delay', transitionDelay).removeClass('').addClass(animateContentClass);
        }
      }).on('animationend', animationEndCallback);
    } else {
      animationEndCallback && animationEndCallback();
    }

    // animate header
    if (toggleLiipbox) {
      $(logoSelector).removeClass('translated');
      $(crossIcon).removeClass('translated');
    } else {
      $(liipboxOverlay).addClass(animateHeaderClass);
      $(logoSelector).addClass('translated');
      $(crossIcon).addClass('translated');
    }
  }

  /**
   * function keeps track of highlighted items
   * while iterating over links, buttons &
   * input fields
   * @param e Keyboard event
   */
  function trapTabKey(e) {
    // Check for TAB key press
    if (e.keyCode === 9) {

      // SHIFT + TAB
      if (e.shiftKey) {
        if (document.activeElement === _cache.firstTabStop) {
          e.preventDefault();
          _cache.lastTabStop.focus();
        }
        // TAB
      } else if (document.activeElement === _cache.lastTabStop) {
        e.preventDefault();
        _cache.firstTabStop.focus();
      }
    }

    // ESCAPE
    if (e.keyCode === 27) {
      //closeLiipbox();
      liipboxAnimation('close');
    }
  }

  snro.liipboxComp = {

    /**
     * assignment of dom selectors to variables
     */
    _updateCache: function _updateCache() {
      _cache.documentBody = $('body');
      _cache.liipbox = $('.js-liipbox');
      _cache.focusedElementBeforeModal = '';
      _cache.firstTabStop = '';
      _cache.lastTabStop = '';
    },


    /**
     * method to show overlay
     */
    _liipboxOpen: function _liipboxOpen(data) {
      var e = document.createElement('div'),
          html = data,
          liipbox = document.getElementsByClassName('js-liipbox');

      e.className = 'js-liipbox';
      e.innerHTML = html;
      document.body.className = 'x-no-scroll';

      if (liipbox.length > 0) {
        liipbox[0].outerHTML = '';
      }

      document.body.appendChild(e);
      this._accessibleLiipbox();
      liipboxAnimation();
    },


    /**
     * binding events on liipbox
     */
    _bindEvents: function _bindEvents() {
      $('body').on('click', '.js-cross-icon', function () {
        //closeLiipbox();
        liipboxAnimation('close');
      });
    },


    /**
     *  Add keyboard accessibility to the dynamically added content
     *  in liipbox (overlay)
     *  "Tab/Shift + Tab" to iterate through links, buttons, input field
     *  "Esc" key to close the liipbox
     */
    _accessibleLiipbox: function _accessibleLiipbox() {

      // Save current focus
      _cache.focusedElementBeforeModal = document.activeElement;

      // Find the liipbox and its overlay
      var liipbox = document.querySelector('.js-liipbox'),
          liipboxOverlay = document.querySelector('.js-liipbox-overlay');

      // Listen for and trap the keyboard
      liipbox.addEventListener('keydown', trapTabKey);

      if (liipboxOverlay && $(liipboxOverlay).length > 0) {
        // Listen for indicators to close the lipbox
        liipboxOverlay.addEventListener('click', liipboxAnimation);

        // liipbox close button
        var closeButtons = liipboxOverlay.querySelector('.js-cross-icon');

        // Attach listeners to all the close modal buttons
        closeButtons.addEventListener('click', liipboxAnimation);

        // Find all focusable children
        var focusableElementsString = 'a[href], area[href], input:not([disabled]), select:not([disabled]), textarea:not([disabled]), button:not([disabled]), iframe, object, embed, [tabindex="0"], [contenteditable]',
            focusableElements = liipbox.querySelectorAll(focusableElementsString);

        if (focusableElements.length > 0) {
          // Convert NodeList to Array
          focusableElements = Array.prototype.slice.call(focusableElements);

          _cache.firstTabStop = focusableElements[0];
          _cache.lastTabStop = focusableElements[focusableElements.length - 1];

          // Focus first child
          _cache.firstTabStop.focus();
        }
      }
    },


    /**
     * Module initialization
     */
    init: function init() {
      this._updateCache();
      this._bindEvents();
    }
  };
})(window, jQuery, window.snro);

/*
 * header.js
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

  // public methods

  var tabsmapping = { 'tab1': 'productInfo',
    'tab2': 'productSpecs',
    'tab3': 'documents',
    'tab4': 'relatedProducts' },
      _goToTab = function _goToTab(tabName) {
    var tab = $('[data-tab-name=' + tabName + ']:visible');
    if (tab.length) {
      snro.eLabDociFrame.init();
      $(tab).trigger('click');
    }
  },
      _showTabView = function _showTabView(tabName) {
    $('.js-tab-item').removeClass('selected');
    $('[data-tab-name=' + tabName + ']:visible').addClass('selected');

    $('.js-tab-view').hide();
    $('#' + tabName).show();
    /*if($('.js-pdp-nav-tabs').length) {
      window.location.hash = tabName;
    }*/
    if (snro.commonUtils.isDesktopMode()) {
      $('body, html').animate({
        scrollTop: $('.c-tab-view-container').offset().top - $('.c-header').height() - 150 + 'px'
      }, 10);
    }
  },
      _showDefaultTab = function _showDefaultTab() {
    var index = Object.keys(tabsmapping).map(function (i) {
      return tabsmapping[i];
    }).indexOf(window.location.hash.replace('#', ''));
    if (index === -1) {
      var defaultTab = $('.js-tabs-list').data('defaultTab') || 'tab1';
      var tabName = tabsmapping[defaultTab];
      snro.eLabDociFrame.init();
      $('[data-tab-name=' + tabName + ']').trigger('click');
    }
  },
      _bindEvents = function _bindEvents() {
    $('body').on('click', '.js-tab-item', function (e) {
      e.preventDefault();
      if (!$(this).hasClass('selected')) {
        if ($('.js-pdp-nav-tabs').length) {
          $(this).scrollintoview();
        }
        var tabName = $(this).data('tabName');
        _showTabView(tabName);
      }
    });
  };

  /**
   * @method init
   * @description this method is used to initialize public methods.
   * @memberof snro.pdpNavTabs
   * @example
   * snro.pdpNavTabs.init()
   */
  snro.pdpNavTabs = {

    init: function init() {

      var visibleTabsCount = $('.js-tabs-list .js-tab-item:visible').length;
      // to hide tab component if ther is only one tab
      _bindEvents();

      // Specific to PDP page only
      if ($('.js-pdp-nav-tabs').length) {
        _showDefaultTab();
      }

      if (visibleTabsCount <= 1) {
        $('.js-tabs-container').hide();
      }

      // for directly opening the specific tab on a page
      var hash = window.location.hash;
      if (hash) {
        var tabName = hash.split('#');
        tabName = tabName[1];
        _goToTab(tabName);
      }
    }
  };
})(window, jQuery, window.snro);

/*
 * c_hamburgermenu.js
 * [ This javascript code is used to show and hide the search fields on the home page main navigation. ]
 *
 * @project:    SN-RO
 * @date:       2017-07-25
 * @author:     Prashant
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace navigationCmp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  var _cache = {};

  snro = window.snro = snro || {};

  snro.navigationCmp = {
    moduleName: 'navigationCmp',

    // assignment of dom selectors to variables
    updateCache: function updateCache() {
      _cache.hamburgerMenu = $('.js-menu');
      _cache.globalSearch = $('.js-link-search');
      _cache.countrySelector = $('.js-link-country');
      _cache.personaSelector = $('.js-link-persona');
      _cache.navigationContent = $('.js-liipbox-navigation');
    },


    // bind dom events
    bindEvents: function bindEvents() {
      _cache.hamburgerMenu.on('click', this.onHamburgerClick);
    },


    //click event handler for hamburger menu
    onHamburgerClick: function onHamburgerClick() {
      if (_cache.navigationContent.length > 0) {
        snro.liipboxComp._liipboxOpen(_cache.navigationContent[0].innerHTML);
      }
    },


    // Module initialization
    init: function init() {
      this.updateCache();
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);

/*
 * personapicker.js
 * [ This javascript code is to handle person picker on basis of cookie.
 *
 * @project:    SN-RO
 * @date:       2017-07-25
 * @author:     shubham
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace personaPickerComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro = snro || {};
  snro.personaPickerComp = {
    moduleName: 'personaPickerComp',
    cookieFlag: false,
    urlPersona: $('.js-header-persona-link').data('url'),
    redirectURL: '',
    cookieUpdate: false,
    personaTagMeta: $('meta[name="personaTag"]').attr('content'),
    metaTagAttribute: $('meta[name="personaTag"]').attr('content') ? $('meta[name="personaTag"]').attr('content').split(',')[0] : '',
    pageType: $('meta[name="page-type"]').length ? $('meta[name="page-type"]').attr('content') : 'otherPage',
    cookieValue: '',
    // check cookie if exists
    _checkCookie: function _checkCookie() {
      var cookie_value = snro.commonUtils.getCookie('persona-type');
      this._deleteCookie();
      if ($('.js-header-persona-link').length) {
        if (!cookie_value) {
          this._callPersonaPicker(this.urlPersona);
        } else if (snro.countrySelectorCmp.countrySelectorPassed) {
          $('.js-cross-icon').click();
        } else {
          this.cookieFlag = true;
          var cookie_type = snro.commonUtils.getCookie('persona-type');
          if (typeof window.Granite === 'undefined') {
            window.Granite = {
              I18n: {
                get: function get() {}
              }
            };
          }
          $('.js-header-persona-link').text(window.Granite.I18n.get('rdoe_personapicker.' + cookie_type));
        }
      }
    },


    // check if cookie value is updating

    _cookieValueCheck: function _cookieValueCheck(cookie) {
      var cookie_value = snro.commonUtils.getCookie('persona-type');
      if (cookie_value !== cookie) {
        this.cookieUpdate = true;
      } else {
        this.cookieUpdate = false;
      }
    },


    // selected persona show

    _selectPersona: function _selectPersona() {
      var cookie_value = snro.commonUtils.getCookie('persona-type');
      $('.js-persona-link').each(function () {
        var cookie = $(this).find('.js-persona-type').text().trim();
        if (cookie === cookie_value) {
          $(this).addClass('persona-selected');
        }
      });
    },


    // set cookie if persona selected.

    _setCookie: function _setCookie(cookie_type) {
      var currentPersona = snro.commonUtils.getCookie('persona-type');
      snro.commonUtils.setCookie('persona-type', cookie_type, Infinity);
      if (this.pageType.toLocaleLowerCase() === 'home' || currentPersona !== cookie_type && currentPersona !== '') {
        this._redirectPage();
      }
      if (this.metaTagAttribute === this.cookieValue) {
        snro.personaPickerComp._checkCookie();
      }
    },


    // redirecting to the home page
    _redirectPage: function _redirectPage() {
      window.location.href = this.redirectURL;
    },


    // delete persona cookie
    _deleteCookie: function _deleteCookie() {
      if (!$('.js-header-persona-link').length) {
        snro.commonUtils.removeCookie('persona-type');
      }
    },


    // AJAX Call PersonaPicker
    _callPersonaPicker: function _callPersonaPicker(url) {
      var options = {
        url: url,
        type: 'GET',
        dataType: 'html'
      };
      snro.ajaxWrapper.getXhrObj(options).done(function (data) {
        snro.liipboxComp._liipboxOpen(data);
        snro.personaPickerComp.redirectURL = $('#redirectURL').val();
        if (!snro.personaPickerComp.cookieFlag) {
          $('.js-c-persona-picker').addClass('no-cookie');
        } else {
          $('.persona-text.ellipsis').length && $('.persona-text.ellipsis').dotdotdot();
          snro.personaPickerComp._selectPersona();
        }
      }).fail(function (err) {
        snro.commonUtils.log(err);
      });
    },


    // binding click events
    _bindEvents: function _bindEvents(deepLinkInitiated) {
      var context = this,
          $modalPopUp = $('#myModalPopUp'),
          metaTagPersonaLevel = context.metaTagAttribute;
      $('body').on('click', '.js-persona-link', function () {
        var cookie_value = $(this).find('.js-persona-type').text().trim();
        context.cookieValue = cookie_value;
        if (metaTagPersonaLevel && context.personaTagMeta && context.personaTagMeta.indexOf(cookie_value) === -1 && typeof deepLinkInitiated === 'undefined') {
          snro.modalPopComp.updateTemplate(window.Granite.I18n.get('rdoe_deeplinkpopup.' + metaTagPersonaLevel), true);
          $modalPopUp.addClass('show');
          return;
        }
        snro.personaPickerComp._cookieValueCheck(cookie_value);
        $('.js-c-persona-picker').slideUp('fast', function () {
          if (snro.personaPickerComp.cookieUpdate) {
            snro.personaPickerComp._setCookie(cookie_value);
            snro.personaPickerComp._checkCookie();
          } else {
            var liipboxOb = document.getElementsByClassName('js-liipbox');
            liipboxOb[0].parentNode.removeChild(liipboxOb[0]);
          }
          $('body').removeClass('x-no-scroll');
        });
      });
      $('body').on('click', '.js-header-persona-link', function () {
        snro.personaPickerComp._callPersonaPicker(snro.personaPickerComp.urlPersona);
      });
    },


    // Module initialization
    init: function init(deepLinkInitiated) {
      this._checkCookie();
      this._bindEvents(deepLinkInitiated);
    }
  };
})(window, jQuery, window.snro);

/*
 * header.js
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

  snro.productCategoryList = {
    loadCategoryContentAnimation: function loadCategoryContentAnimation(selector) {
      selector.find('.product-category__content--cardWrapper').each(function (index, element) {
        var transitionDelay = index * 0.1 + 's';
        if (window.requestAnimationFrame) {
          $(element).css('animation-delay', transitionDelay).css('-webkit-animation-delay', transitionDelay).addClass('animated fadeInRight');
        }
      });
    },

    init: function init() {
      this.loadCategoryContentAnimation($('.product-category:visible'));
      if (snro.commonUtils.getCookie('wcmmode') !== 'edit') {
        $('.js-product-list-container').each(function (index) {
          $(this).toggle(!index);
          if (!$(this).data('productCount')) {
            $('li.js-tab-item[data-tab-name=' + this.id + ']').hide();
          }
        });
      }
      snro.commonUtils.addEllipses();
    }
  };
})(window, jQuery, window.snro);

/*
 * video.js
 * [ Behavior for video component ]
 *
 * @project:    SN-RO
 * @date:       2017-08-04
 * @author:     Aftab
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';
/**
 * @namespace video
 * @memberof roche
 *
 */

(function (window, $, snro) {
  // Define variables and functions for later use
  // DOM cache
  var _cache = {},
      videoTracking = void 0;
  /**
   *
   * @param  $jPlayerCnt Player's container DIV to initialize against
   * @param videoPath Path of video to initialize with
   */
  function _initjPlayer($jPlayerCnt, videoPath, imagePath) {
    $jPlayerCnt.find('.jp-jplayer').jPlayer({
      ready: function ready() {
        $(this).jPlayer('setMedia', {
          m4v: videoPath,
          ogv: videoPath,
          webmv: videoPath,
          wav: videoPath,
          poster: imagePath
        });
        $(this).find('video').css('width', '100%').css('height', 'auto');
        $(this).find('img').css('position', 'absolute').css('opacity', '1');
        if (window.Granite) {
          $(this).find('img').attr('alt', window.Granite.I18n.get('rdoe_video.altText'));
        } else {
          $(this).find('img').attr('alt', 'posterImage');
        }
      },
      ended: function ended() {
        $(this).jPlayer('setMedia', {
          m4v: videoPath,
          ogv: videoPath,
          webmv: videoPath,
          wav: videoPath,
          poster: imagePath
        });
        $(this).jPlayer('playHead', 0);
        if ($jPlayerCnt.find('.jp-audio').hasClass('jp-state-full-screen')) {
          $jPlayerCnt.find('.jp-interface').css('visibility', 'visible');
        } else {
          $jPlayerCnt.find('.jp-interface').css('visibility', 'hidden');
        }
        $jPlayerCnt.find('.layer-opaque').css('visibility', 'visible');
        $jPlayerCnt.find('.video-info').css('display', 'block');
        $jPlayerCnt.find('img').css('position', 'absolute').css('display', 'inline');
      },
      play: function play() {
        $(this).jPlayer('pauseOthers');
      },
      pause: function pause() {
        $jPlayerCnt.find('.jp-video-ctrl.vid-play').removeClass('pause').addClass('play');
      },
      cssSelectorAncestor: '#' + $jPlayerCnt.find('.jp-audio').attr('id'),
      preload: 'metadata',
      swfPath: 'http://jplayer.org/latest/dist/jplayer/jquery.jplayer.swf',
      supplied: 'm4v, ogv, webmv, wav',
      size: {
        width: '100%',
        height: 'auto',
        cssClass: 'jp-video-360p'
      },
      useStateClassSkin: true
    });

    $jPlayerCnt.bind($.jPlayer.event.seeked, function (event) {
      var seekedPercent = parseInt(event.jPlayer.status.currentPercentAbsolute);
      if (seekedPercent === 100) {
        $.extend(window.digitalData['link'], { event: 'video-completes' });
        window._satellite.track('video-tracking');
        videoTracking['video-100%'] = 'fired';
      } else if (seekedPercent >= 75) {
        $.extend(window.digitalData['link'], { event: 'video-75%complete' });
        window._satellite.track('video-tracking');
        videoTracking['video-75%'] = 'fired';
      } else if (seekedPercent >= 50 && seekedPercent < 75) {
        $.extend(window.digitalData['link'], { event: 'video-50%complete' });
        window._satellite.track('video-tracking');
        videoTracking['video-50%'] = 'fired';
      } else if (seekedPercent >= 25 && seekedPercent < 50) {
        $.extend(window.digitalData['link'], { event: 'video-25%complete' });
        window._satellite.track('video-tracking');
        videoTracking['video-25%'] = 'fired';
      } else {
        // do nothing
      }
    });

    $jPlayerCnt.find('.jp-jplayer').bind($.jPlayer.event.timeupdate, function (event) {
      //as above, grabbing the % location and media being played
      var playerTime = event.jPlayer.status.currentPercentAbsolute;

      //There's some overlap between the seeked and stopped events. When a user clicks
      // the stop button it actually sends a "seek" to the 0 location. So if the seeked location is 0
      // then we track it as a stop, if it's greater than 0, it was an actual seek.
      if (playerTime >= 2 && playerTime < 3) {
        $.extend(window.digitalData['link'], { event: 'video-play' });
      } else if (playerTime >= 25 && playerTime < 26 && !videoTracking['video-25%']) {
        $.extend(window.digitalData['link'], { event: 'video-25%complete' });
        window._satellite.track('video-tracking');
        videoTracking['video-25%'] = 'fired';
      } else if (playerTime >= 50 && playerTime < 51 && !videoTracking['video-50%']) {
        $.extend(window.digitalData['link'], { event: 'video-50%complete' });
        window._satellite.track('video-tracking');
        videoTracking['video-50%'] = 'fired';
      } else if (playerTime >= 75 && playerTime < 76 && !videoTracking['video-75%']) {
        $.extend(window.digitalData['link'], { event: 'video-75%complete' });
        window._satellite.track('video-tracking');
        videoTracking['video-75%'] = 'fired';
      } else if (playerTime >= 99 && playerTime < 100 && !videoTracking['video-100%']) {
        $.extend(window.digitalData['link'], { event: 'video-completes' });
        window._satellite.track('video-tracking');
        videoTracking['video-100%'] = 'fired';
      }
    });
  }

  /**
   *
   * @param $jPlayerCnt Player's container
   * @param $jPlayer Player instance
   * @param $volCtrlBtn Player's volume control button
   */

  function _initjPlayerVolSlider($jPlayerCnt, $jPlayer, $volCtrlBtn) {
    $jPlayerCnt.find('input[type="range"]').rangeslider({
      polyfill: false,
      onSlide: function onSlide(position, value) {
        $jPlayer.jPlayer('volume', value / 100);
        if (0 === value && !$volCtrlBtn.hasClass('mute')) {
          $volCtrlBtn.addClass('mute');
        } else if ($volCtrlBtn.hasClass('mute')) {
          $volCtrlBtn.removeClass('mute');
        }
      }
    });
  }
  /**
   *
   * @param $currjPlayer jPlayer's instance
   * @param $playBtn jPlayer's Play/Pause button
   */
  function _attachPlayPauseListener($currjPlayer, $playBtn) {
    $playBtn.on('click', function () {
      $currjPlayer.jPlayer('pauseOthers');
      if (!$currjPlayer.data().jPlayer.status.paused) {
        $currjPlayer.jPlayer('pause');
        $playBtn.removeClass('pause').addClass('play');
      } else {
        $currjPlayer.jPlayer('play');
        $playBtn.removeClass('play').addClass('pause');
      }
    });
  }
  /**
   *
   * @param $currjPlayer jPlayer's instance
   * @param $fullScrBtn jPlayer's full-screen button
   */
  function _attachFullScrListener($currjPlayer, $fullScrBtn) {
    $fullScrBtn.on('click', function () {
      $currjPlayer.parent().find('.j-cross-button').toggleClass('show');
    });
  }
  /**
   *
   * @param  $currjPlayer jPlayer's instance
   * @param  $volCtrlBtn jPlayer's full-screen button
   */
  function _attachVolListener($currjPlayer, $volCtrlBtn) {
    $volCtrlBtn.on('click', function () {
      if ($volCtrlBtn.hasClass('mute')) {
        $volCtrlBtn.removeClass('mute');
        $currjPlayer.jPlayer('volume', 1);
      } else {
        $volCtrlBtn.addClass('mute');
        $currjPlayer.jPlayer('volume', 0);
      }
    });
  }

  /**
   *
   * @param  $currjPlayer jPlayer's instance
   * @param  $volCtrlBtn jPlayer's full-screen button
   */
  function _attachClickListener($currjPlayer, $crossButton) {

    $crossButton.on('click', function (e) {
      if (document.exitFullscreen) {
        document.exitFullscreen();
      } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
      } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
      } else if (document.webkitCancelFullScreen) {
        document.webkitCancelFullScreen();
      }
      $currjPlayer.parent().removeClass('jp-state-full-screen jp-video-full');
      $currjPlayer.css('width', '100%').css('height', 'auto');
      $currjPlayer.find('video').css('width', '100%').css('height', 'auto');
      $currjPlayer.parent().addClass('jp-video-360p');
      $(e.currentTarget).removeClass('show');
    });
  }

  function onFullScreenChange() {
    var fullScreenElement = document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement;
    if (!fullScreenElement) {
      $('.j-cross-button').removeClass('show');
    }
  }
  document.onfullscreenchange = onFullScreenChange;
  document.onmsfullscreenchange = onFullScreenChange;
  document.onmozfullscreenchange = onFullScreenChange;
  document.onwebkitfullscreenchange = onFullScreenChange;

  // Register behavior against video namespace
  snro = window.snro = snro || {};
  snro.video = {
    moduleName: 'video',
    // Cache DOM refs and other dynamic data for re-use
    _cacheRefs: function _cacheRefs() {
      _cache.$heroBannerCnt = $('.c-video'); //
    },


    // Attach listeners to interesting events
    _initPlayers: function _initPlayers() {
      _cache.$heroBannerCnt.each(function (i, currjPlayerCnt) {
        var $currjPlayerCnt = $(currjPlayerCnt);
        var $currjPlayer = $currjPlayerCnt.find('.jp-jplayer');
        var $videoPlayBtn = $currjPlayerCnt.find('.jp-video-play');
        var $playBtn = $currjPlayerCnt.find('.vid-play');
        var $fullScrBtn = $currjPlayerCnt.find('.jp-full-screen');
        var $volCtrlBtn = $currjPlayerCnt.find('.vol-ctrl');
        var videoPath = $currjPlayer.data('fileref');
        var imagePath = $currjPlayer.data('posterref');
        var $crossButton = $currjPlayerCnt.find('.j-cross-button');
        // Initialize jPlayer
        _initjPlayer($currjPlayerCnt, videoPath, imagePath);
        // Initialize rangeslider for jPlayer
        _initjPlayerVolSlider($currjPlayerCnt, $currjPlayer, $volCtrlBtn);
        // Play/Pause
        _attachPlayPauseListener($currjPlayer, $playBtn);
        // Full-screen
        _attachFullScrListener($currjPlayer, $fullScrBtn);
        // Volume control
        _attachVolListener($currjPlayer, $volCtrlBtn);
        // Prepare big "play" button for first load
        $videoPlayBtn.on('click', function () {
          videoTracking = {
            'video-25%': '',
            'video-50%': '',
            'video-75%': '',
            'video-100%': ''
          };
          $currjPlayerCnt.find('.jp-interface').css('visibility', 'visible');
          $currjPlayerCnt.find('.layer-opaque').css('visibility', 'hidden');
          $currjPlayerCnt.find('.video-info').css('display', 'none');
          $playBtn.trigger('click');
        });
        _attachClickListener($currjPlayer, $crossButton);
        //_addKeyUpListner();
        $currjPlayer.jPlayer('volume', 0);
        $currjPlayerCnt.find('.rangeslider__handle').css('left', 0);
        $currjPlayerCnt.find('.rangeslider__fill').css('width', '0');
        $volCtrlBtn.addClass('mute');
      });
    },


    // Module initialization
    init: function init() {
      this._cacheRefs();
      this._initPlayers();
    }
  };
})(window, window.jQuery, window.snro);

/*!
* c_countrySelectorCmp.js

* This file contains utility functions for countrySelectorCmp
*
* @project   SapientNitro Roche Diagonostics
* @date      2017-08-4
* @author    Amit
* @dependencies jQuery, CSSMaps
*/

'use strict';

/**
 * @namespace countrySelectorCmp
 * @memberof snro
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro = snro || {};
  var cookie_value = void 0,
      basePagePath = void 0,
      domElements = {
    currentPagePath: $('input[name=currentPagepath]'),
    countryPickerLink: $('.js-link-country'),
    currentChannel: $('input[name=currentChannel]')
  },
      continentClassMap = {
    c1: 'af',
    c2: 'as',
    c3: 'oc',
    c4: 'eu',
    c5: 'na',
    c6: 'sa'
  },
      _showCountries = function _showCountries(e) {
    domElements.countryList.hide();
    var elementId = continentClassMap[e[0].className.split(' ')[0]];
    $('#' + elementId).hide().show();
    domElements.continentSelector.removeClass('active');
    $('#js-' + elementId).addClass('active');
  },
      _toggleCountryList = function _toggleCountryList() {
    domElements.countryList.hide();
    var continentUl = $('a[data-country-code=country-' + snro.countrySelectorCmp.countryCode.toLowerCase() + ']').parents('ul');
    var elementId = continentUl.length && continentUl[0].id || $('#na').length && 'na' || domElements.countryList.length && domElements.countryList[0].id;
    domElements.confirmBox.hide();
    domElements.globalSiteBox.show();
    $('#' + elementId).hide().show();
    domElements.continentSelector.removeClass('active');
    $('#js-' + elementId).addClass('active');
  },
      _showInitialSelection = function _showInitialSelection() {
    var country = $('a[data-country-code=country-' + snro.countrySelectorCmp.countryCode.toLowerCase() + '][data-language=' + snro.countrySelectorCmp.language.toLowerCase() + ']');
    if (cookie_value) {
      var currentCountry = $('a[data-country-code=country-' + cookie_value.split('|')[0] + '][data-language=' + cookie_value.split('|')[1] + ']');

      domElements.currentCountryText.removeClass('hidden');
      domElements.currentCountryText.text(currentCountry && domElements.currentCountryText.data('translatedText').replace('{0}', currentCountry.data('countryName')) || '');

      domElements.countryLanguageSelection.removeClass('active');
      currentCountry.addClass('active');
    }

    /* TODO: Include below code back when personalization has been fully implemented for deep links
      let samePage = domElements.currentPagePath.val() && domElements.currentPagePath.val().indexOf(basePagePath.split('/').slice(0,5).join('/'))>-1
      if (cookie_value && !samePage) {
      country = $('a[data-country-code=country-'+ cookie_value.split('|')[0].toLowerCase()+'][data-language='+cookie_value.split('|')[1].toLowerCase()+']');
      domElements.countryText
      .text(
        domElements.countryText
        .data('existing-cookie-string') &&
        domElements.countryText
        .data('existing-cookie-string')
        .replace('{0}', country.data('countryName'))
        .replace('{1}', country.data('languageText'))
        .replace('{2}', window.Granite.I18n.get('rdoe_personapicker.' + snro.commonUtils.getCookie('persona-type'))));
      domElements.confirmBox.show();
      domElements.globalSiteBox.hide();
      domElements.countryList.hide().css('visibility', 'hidden');
      snro.countrySelectorCmp.countryCode = cookie_value.split('|')[0].toLowerCase();
      return;
    } else */
    if (snro.countrySelectorCmp.locationDetected && country.length) {
      domElements.countryText.text(domElements.countryText.data('preselection-string') && domElements.countryText.data('preselection-string').replace('{0}', country.data('countryName')));
      snro.countrySelectorCmp.language = (navigator.language || navigator.browserLanguage).split('-')[0].toLowerCase();
      domElements.confirmBox.show();
      domElements.globalSiteBox.hide();
      domElements.countryList.hide().css('visibility', 'hidden');
      return;
    }
    _toggleCountryList();
  },
      proceedWithDefaultSelection = function proceedWithDefaultSelection() {
    $('.js-country-language-selection[data-country-code=country-' + snro.countrySelectorCmp.countryCode.toLowerCase() + '][data-language=' + snro.countrySelectorCmp.language.toLowerCase() + ']').click();
  },
      _bindEvents = function _bindEvents() {
    // eslint-disable-next-line new-cap
    domElements.mapsContainer.CSSMap({
      'size': 540,
      'tooltips': 'floating-top-center',
      'responsive': 'auto',
      'tapOnce': true,
      'onClick': _showCountries,
      'onLoad': function onLoad() {
        var continentUl = $('a[data-country-code=country-' + snro.countrySelectorCmp.countryCode.toLowerCase() + ']').parents('ul'),
            elementId = continentUl.length && continentUl[0].id || $('#na').length && 'na' || domElements.countryList.length && domElements.countryList[0].id;
        if (!$('[data-target=js-' + elementId + ']').hasClass('active-region')) {
          $('[data-target=js-' + elementId + '] span')[0].click();
        }
      }
    });
    domElements.continentSelector.on('click', function (e) {
      domElements.countryList.hide();
      $('#' + e.target.id.replace('js-', '')).hide().show();
      domElements.continentSelector.removeClass('active');
      $(this).addClass('active');
      if (!$('[data-target=' + this.id + ']').hasClass('active-region')) {
        $('[data-target=' + this.id + '] span')[0].click();
      }
    });
    domElements.preselectionConfirm.on('click', proceedWithDefaultSelection);
    domElements.preselectionDeny.on('click', function () {
      domElements.countryList.css('visibility', 'visible');
      _toggleCountryList();
    });
    domElements.countryLanguageSelection.on('click', function () {
      snro.commonUtils.setCookie('location-and-language', $(this).data('countryCode').replace('country-', '') + '|' + $(this).data('language'), Infinity);
      snro.commonUtils.setCookie('homePagePath', $(this).data('channel'), Infinity);
      if ($(this).data('href') !== domElements.currentPagePath.val()) {
        snro.commonUtils.removeCookie('persona-type');
        window.location.href = $(this).data('href');
        return;
      }
      snro.countrySelectorCmp.countrySelectorPassed = true;
      snro.personaPickerComp.init();
    });
    domElements.crossIconContainer.parent().toggle(snro.countrySelectorCmp.reselectionInitiated);
  },
      _getPageMarkup = function _getPageMarkup() {
    if ($('.js-link-country').length) {
      var url = domElements.countryPickerLink.data('url') && snro.countrySelectorCmp.language && domElements.countryPickerLink.data('url').replace('.html', '.' + snro.countrySelectorCmp.language + '.html') || domElements.countryPickerLink.data('url');
      var options = {
        url: url,
        type: 'GET',
        dataType: 'html'
      };
      snro.ajaxWrapper.getXhrObj(options).done(function (data) {
        snro.liipboxComp._liipboxOpen(data);
        domElements.countryList = $('.js-country-list');
        domElements.continentSelector = $('.js-continent-selector');
        domElements.confirmBox = $('.js-confirm-box');
        domElements.globalSiteBox = $('.js-global-site-box');
        domElements.countryText = $('#js-country-text');
        domElements.mapsContainer = $('#map-continents');
        domElements.preselectionConfirm = $('.js-preselection-yes');
        domElements.preselectionDeny = $('.js-preselection-no');
        domElements.countryLanguageSelection = $('.js-country-language-selection');
        domElements.crossIconContainer = $('.js-countryselector .js-cross-icon');
        domElements.currentCountryText = $('.js-current-country-text');
        _bindEvents();
        _showInitialSelection();
      }).fail(function (err) {
        snro.commonUtils.log(err);
      });
    }
  },
      _mapsSuccessCallback = function _mapsSuccessCallback(data) {
    if (data.status === 'OK' && data.results && data.results[0] && data.results[0].address_components) {
      snro.countrySelectorCmp.countryCode = cookie_value.split('|')[0].toLowerCase();
      $.grep(data.results[0].address_components, function (obj) {
        if (obj && obj.types && obj.types.indexOf('country') > -1) {
          snro.countrySelectorCmp.countryCode = obj.short_name || '';
        }
      });
      snro.countrySelectorCmp.locationDetected = snro.countrySelectorCmp.countryCode;
    }
    _getPageMarkup();
  },
      _mapsErrorCallback = function _mapsErrorCallback() {
    snro.countrySelectorCmp.countryCode = '';
    _getPageMarkup();
  };

  snro.countrySelectorCmp = {
    countrySelectorPassed: false,
    language: '',
    countryCode: '',

    initializeLanguageSelector: function initializeLanguageSelector() {
      cookie_value = snro.commonUtils.getCookie('location-and-language');
      basePagePath = snro.commonUtils.getCookie('homePagePath');
      var samePage = domElements.currentChannel.val() === basePagePath;
      if (!snro.countrySelectorCmp.reselectionInitiated && cookie_value && samePage) {
        snro.personaPickerComp.init();
        return;
      }
      snro.countrySelectorCmp.language = cookie_value ? cookie_value.split('|')[1].toLowerCase() : (navigator.language || navigator.browserLanguage).split('-')[0].toLowerCase();
      navigator.geolocation.getCurrentPosition(function (loc) {
        $.get('https://maps.googleapis.com/maps/api/geocode/json?latlng=' + loc.coords.latitude + ',' + loc.coords.longitude).then(_mapsSuccessCallback, _mapsErrorCallback);
      }, function () {
        snro.countrySelectorCmp.locationDetected = false;
        snro.countrySelectorCmp.countryCode = '';
        _getPageMarkup();
      }, { enableHighAccuracy: false });
    },

    init: function init(deepLinkInitiated) {
      var cookiePersona = snro.commonUtils.getCookie('persona-type');
      $('.blank-liipbox').hide();
      if (typeof deepLinkInitiated === 'undefined') {
        snro.countrySelectorCmp.initializeLanguageSelector();
      }
      if (typeof cookiePersona !== 'undefined' && cookiePersona !== '') {
        snro.personaPickerComp.init();
      }
      $('body').on('click', '.js-link-country', function () {
        snro.countrySelectorCmp.reselectionInitiated = true;
        snro.countrySelectorCmp.initializeLanguageSelector();
      });
    }
  };
})(window, window.jQuery, window.snro);

/*!
 * common.utils.js

 * This file contians some most common utility functions
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-07-17
 * @author    Shashank
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
   * Generic table will be shown and hidden.
   * Accepts any valid selector or jQuery object.
   * @param {selector, HTMLElement, or jQuery object} table
   */

  var showHideTbl = function showHideTbl() {
    // define variable for table

    var viewLink = $('.view-btn'),
        viewTarget = $('.js-table'),
        viewTargetChild = $('.js-table tr'),
        linkLocation = void 0,
        closeBtn = 'js-close',
        setTableHeight = $('.js-table-height'),
        clientHeight = $(window).height();

    //this function is using for show the table when click on view full link btn
    viewLink.on('click', function (event) {
      var data = $(event.target).attr('data-action');
      linkLocation = $(event.target).offset().top;
      $('.' + data).show();
    });

    //this function is using for hide table when click on close
    viewTarget.on('click', function (event) {
      if ($(event.target).hasClass(closeBtn)) {
        $(this).hide();
        $(window).scrollTop(linkLocation);
      }
    });

    //this function is using for row animation
    viewTargetChild.hover(function () {
      $(this).find('th').addClass('animaterow');
    }, function () {
      $(this).find('th').removeClass('animaterow');
    });

    // Check for mobile
    if (snro.commonUtils.isMobile()) {
      setTableHeight.height(clientHeight - 150 + 'px');
    }
  };

  snro.featureTable = {
    init: function init() {
      showHideTbl();
    }
  };
})(window, window.jQuery, window.snro);

/*!
 * common.utils.js

 * This file contians some most common utility functions
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-07-17
 * @author    Shashank
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
   * Generic function will be use if description length will exceed from 150 char more link will be displayed.
   * Accepts any valid selector or jQuery object.
   * @param {selector, HTMLElement, or jQuery object} table
   */

  var seeMorelink = function seeMorelink() {

    var viewLink = $('.c-marketingtile__description'),
        seeMore = '.js-seemore',
        ctaEle = '.c-marketingtile__cta';

    viewLink.each(function () {
      var self = $(this);
      if (self.text().trim().length > 150) {
        self.parent('div').find(ctaEle).hide();
        self.parent('div').find(seeMore).show();
        snro.commonUtils.addEllipses();
      } else {
        self.parent('div').find(ctaEle).show();
        self.parent('div').find(seeMore).hide();
      }
    });
  };

  snro.marketingTile = {
    init: function init() {
      seeMorelink();
    }
  };
})(window, window.jQuery, window.snro);

/*
 * c_notifications.js
 * [ This javascript code is to show page leve notifications ]
 *
 * @project:    SN-RO
 * @date:       2017-08-17
 * @author:     shubham
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace pageNotification
 * @memberof snro
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro = snro || {};
  var notifyTitle = $('.js-title'),
      notifyDescription = $('.js-description'),
      notifyType = $('.js-notification-type'),
      pageNotify = $('.js-page-notification'),
      notifyClose = $('.js-page-notification .js-close'),
      submitButton = $('.js-contact-form .guidebutton button');

  snro.pageNotification = {
    getPageNotification: function getPageNotification(url, dataString, timeout) {
      var options = {
        url: url,
        type: 'POST',
        dataType: 'json',
        data: dataString
      };

      if (typeof timeout !== 'undefined') {
        options['timeout'] = timeout;
      }

      snro.ajaxWrapper.getXhrObj(options).done(function (data) {
        $('.loader-backdrop').hide();
        notifyTitle.text(window.Granite.I18n.get(data.notificationTitle));
        notifyDescription.text(window.Granite.I18n.get(data.notificationDescription));
        notifyType.addClass('js-notification-' + data.notificationType);
        notifyType.find('.js-link').attr('href', data.notificationCTALink).text(data.notificationCTAText);
        pageNotify.removeClass('hidden');
        $('html,body').animate({ scrollTop: 0 }, 100);
        snro.pageNotification.triggerAnalytics(data);
      }).fail(function () {
        $('.loader-backdrop').hide();
        notifyTitle.text(window.Granite.I18n.get('rdoe_ContactUs.ErrorTitle'));
        notifyDescription.text(window.Granite.I18n.get('rdoe_ContactUs.ErrorMessage'));
        notifyType.addClass('js-notification-error');
        pageNotify.removeClass('hidden');
        $('html,body').animate({ scrollTop: 0 }, 100);
      });
    },
    triggerAnalytics: function triggerAnalytics(data) {
      var notificationType = data.notificationType;
      if (notificationType === 'confirmation') {
        window.digitalData = {
          link: {
            linkPageName: 'contact-us',
            linkCategory: 'internal',
            linkSection: 'form',
            linkHeader: snro.contactForm.formType,
            linkText: submitButton.text().trim().toLowerCase(),
            linkInteractionMethod: 'click-on-' + snro.analytics.getBrowserWidth(),
            formName: snro.contactForm.formType,
            formType: 'contact-us',
            event: 'form-complete'
          }
        };
        window._satellite.track('form-tracking');
      } else {
        window.digitalData = {
          link: {
            linkPageName: 'contact-us',
            linkCategory: 'internal',
            linkSection: 'form',
            linkHeader: snro.contactForm.formType,
            linkText: submitButton.text().trim().toLowerCase(),
            linkInteractionMethod: 'click-on-' + snro.analytics.getBrowserWidth(),
            formName: snro.contactForm.formType,
            formType: 'contact-us',
            formError: data.notificationType,
            event: 'form-errors'
          }
        };
        window._satellite.track('form-tracking');
      }
    },


    // binding event for close event 
    bindEvents: function bindEvents() {
      notifyClose.on('click', function () {
        pageNotify.addClass('hidden');
      });
    },
    init: function init() {
      this.bindEvents();
    }
  };
})(window, window.jQuery, window.snro);

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

/*!
 * ajax.wrapper.js

 * This file contians ajax wrapper method that handles all ajax calls
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-07-17
 * @author    Shashank
 * @dependencies jQuery
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace ajaxWrapper
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro = snro || {};
  snro.ajaxWrapper = {
    moduleName: 'ajaxWrapper', // Added for debug logs
    xhrPool: {
      name: 'xhrPool'
    },
    getXhrObj: function getXhrObj(options, callback, complete) {
      var self = this,
          ajaxOptions = void 0,
          defaultOptions = {
        type: 'POST',
        async: true,
        cache: false,
        url: '',
        data: {},
        dataType: 'json',
        loaderRef: null,
        beforeSend: function beforeSend(jqXHR) {
          if (ajaxOptions.cancellable) {
            if (self.xhrPool[ajaxOptions.url]) {
              self.xhrPool[ajaxOptions.url].abort();
              self.xhrPool[ajaxOptions.url] = jqXHR;
            } else {
              self.xhrPool[ajaxOptions.url] = jqXHR;
            }
          }
          if (ajaxOptions.loader && ajaxOptions.loader.length) {
            // Check if target type is a button or link
            if (ajaxOptions.loader.hasClass('loader-btn')) {
              self.loaderRef = snro.commonUtils.loader().insertAfter(ajaxOptions.loader);
            } else {
              self.loaderRef = snro.commonUtils.loader().target(ajaxOptions.loader);
            }
          }
        },
        cancellable: false, // By default allow multiple request on one URL
        loader: null // Specify a target element where loader needs to be shown
      };
      ajaxOptions = $.extend({}, defaultOptions, options);
      return $.ajax(ajaxOptions).done(function (data, status, jqXHR) {
        $.each(self.xhrPool, function (url, xhrObj) {
          if (xhrObj === jqXHR) {
            delete self.xhrPool[url];
            return false;
          }
        });
        if (!callback) {
          return;
        }
        callback.apply(this, arguments);
      }).fail(function (jqXHR) {
        $.each(self.xhrPool, function (url, xhrObj) {
          if (xhrObj === jqXHR) {
            delete self.xhrPool[url];
            return false;
          }
        });
        if (!callback) {
          return;
        }
        callback.apply(this, arguments);
      }).always(function () {
        if (self.loaderRef && self.loaderRef.length) {
          self.loaderRef.remove();
        }
        if (!complete) {
          return;
        }
        complete.apply(this, arguments);
      });
    },
    init: function init() {
      // Since init is mandatory
      if (!window.ajaxWrapper) {
        window.ajaxWrapper = this;
      }
    }
  };
})(window, window.jQuery, window.snro);

/*
 * Analytics Implementation
 * [ This javascript code is to handle images from Adobe server
 *
 * @project:    SN-RO
 * @date:       2015-07-27
 * @author:     Shashank
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';
// check if digitalData is already available or not

var digitalData = window.digitalData || {};
(function (w, $, snro) {
  if (!$) {
    return;
  }
  snro = w.snro = snro || {};

  // Adding few string prototype functions
  if (!String.prototype.lowerCaseFirstLetter) {
    String.prototype.lowerCaseFirstLetter = function () {
      return this.charAt(0).toLowerCase() + this.slice(1);
    };
  }

  // module definition
  snro.analytics = {
    moduleName: 'Analytics',

    getBrowserWidth: function getBrowserWidth() {
      if (snro.commonUtils.isMobile()) {
        // Mobile View
        return 'mobile';
      } else if (snro.commonUtils.isTablet()) {
        // Tablet View
        return 'tablet';
      } else {
        // Desktop View
        return 'desktop';
      }
    },

    loadEvent: function loadEvent() {
      var pagetype = void 0,
          productid = void 0,
          productname = void 0,
          productcategory = void 0;
      if (document.getElementsByTagName('meta')['page-type'] === undefined) {
        pagetype = 'home';
      } else {
        pagetype = document.getElementsByTagName('meta')['page-type'].content && document.getElementsByTagName('meta')['page-type'].content.toLocaleLowerCase() || '';
      }

      var analyticsId = document.getElementById('analyticsdata'),
          siteLanglocation = snro.commonUtils.getCookie('location-and-language') && snro.commonUtils.getCookie('location-and-language').toLocaleLowerCase() || '',
          serverName = analyticsId.getAttribute('data-servername'),
          channel = analyticsId.getAttribute('data-channel') && analyticsId.getAttribute('data-channel').toLocaleLowerCase() || '',
          siteSectionOne = analyticsId.getAttribute('data-sitesectionone') && analyticsId.getAttribute('data-sitesectionone').toLocaleLowerCase() || '',
          siteSectionTwo = analyticsId.getAttribute('data-sitesectiontwo') && analyticsId.getAttribute('data-sitesectiontwo').toLocaleLowerCase() || '',
          siteSectionThree = analyticsId.getAttribute('data-sitesectionthree') && analyticsId.getAttribute('data-sitesectionthree').toLocaleLowerCase() || '',
          siteSectionFour = analyticsId.getAttribute('data-sitesectionfour') && analyticsId.getAttribute('data-sitesectionfour').toLocaleLowerCase() || '',
          siteSectionOnePName = analyticsId.getAttribute('data-sitesectionone') && ':' + analyticsId.getAttribute('data-sitesectionone').toLocaleLowerCase() || '',
          siteSectionTwoPName = analyticsId.getAttribute('data-sitesectiontwo') && ':' + analyticsId.getAttribute('data-sitesectiontwo').toLocaleLowerCase() || '',
          siteSectionThreePName = analyticsId.getAttribute('data-sitesectionthree') && ':' + analyticsId.getAttribute('data-sitesectionthree').toLocaleLowerCase() || '',
          personaType = snro.commonUtils.getCookie('persona-type') && snro.commonUtils.getCookie('persona-type').toLocaleLowerCase() || '',
          siteCountry = siteLanglocation.split('|')[0],
          siteLanguage = siteLanglocation.split('|')[1],
          deviceView = snro.analytics.getBrowserWidth();
      window.digitalData = {
        page: {
          server: serverName,
          channel: channel,
          pageType: pagetype,
          pageName: 'rd' + ':' + siteCountry + ':' + siteLanguage + siteSectionOnePName + siteSectionTwoPName + siteSectionThreePName,
          siteSection1: siteSectionOne,
          siteSection2: siteSectionTwo,
          siteSection3: siteSectionThree,
          siteSection4: siteSectionFour,
          pageURL: window.location.href,
          siteLanguage: siteLanguage,
          siteType: 'responsive',
          persona: personaType,
          siteCountry: siteCountry
        },

        user: {
          deviceType: deviceView
        },

        link: {}
      };

      if (document.getElementsByTagName('meta')['product-id'] !== undefined && document.getElementsByTagName('meta')['product-category'] !== undefined) {
        productid = document.getElementsByTagName('meta')['product-id'].content && document.getElementsByTagName('meta')['product-id'].content.toLocaleLowerCase() || '';
        productcategory = document.getElementsByTagName('meta')['product-category'].content && document.getElementsByTagName('meta')['product-category'].content.toLocaleLowerCase() || '';
        productname = window.document.title;
        if (productname.length > 100) {
          productname = productname.substring(0, 100);
        } else if (productcategory.length > 100) {
          productcategory = productcategory.substring(0, 100);
        }
      }
      if (pagetype === 'products') {
        $.extend(digitalData['page'], { event: 'pdp-load', productId: productid, productName: productname, productCategory: productcategory });
      }

      // for eloqua tracking
      if (pagetype === 'contact-us') {
        var eloquaFlag = 'false';
        if ($('.js-contact-form').attr('data-eloqua-enabled')) {
          eloquaFlag = 'true';
        }
        $.extend(window.digitalData['page'], { eloquaEnabled: eloquaFlag });
      }
    },

    urlType: function urlType(target) {

      if ($(target).attr('href') !== undefined && $(target).attr('href').match(/\bhttps?:\/\/\S+/gi)) {
        // Mobile View
        return 'external';
      } else {
        // Desktop View
        return 'internal';
      }
    },
    // handle the events
    trackEvents: function trackEvents() {
      //on click track event and push in DigitalData
      $(document).on('sclick enterKeyPress', '[data-fn-click=true]', function () {
        var self = this,
            satelliteTrackEvent = void 0;
        window.digitalData = {
          page: {},
          user: {},
          link: {}
        };
        //update satellite.track parameter on page
        if ($(this).attr('data-da-satellitetrackEvent')) {
          satelliteTrackEvent = $(this).attr('data-da-satelliteTrackEvent');
        }

        $.each($(this).data(), function (attr, attrValue) {
          if (~attr.indexOf('da')) {
            var analyticsKey = attr.replace('da', '').lowerCaseFirstLetter(),
                subkey = {},
                attrArr = void 0,
                keyLevel1 = void 0,
                keyLevel2 = void 0;
            if (analyticsKey.indexOf('_') !== -1) {
              attrArr = analyticsKey.split('_');
              keyLevel1 = attrArr[1];
              keyLevel2 = attrArr[0];
              if (keyLevel1 === 'linkPageName') {
                attrValue = window.document.title || 'roche';
              } else if (keyLevel1 === 'linkInteractionMethod') {
                attrValue = 'click-on-' + snro.analytics.getBrowserWidth();
              } else if (keyLevel1 === 'linkCategory') {
                attrValue = snro.analytics.urlType(self);
              } else if (keyLevel1 === 'contentType') {
                attrValue = attrValue || window.digitalData.page.pageType || '';
              }
              if ($(self).parents('.product-category').attr('id') && $(self).parents('.product-category').attr('id').indexOf('product') > -1 && keyLevel1 === 'event') {
                attrValue = 'product-click';
              }

              if (attrValue !== undefined && attrValue !== null && attrValue !== '') {
                attrValue = String(attrValue);
                attrValue = attrValue.replace(/\s+/g, '-').toLowerCase();
                if (attrValue.length > 100) {
                  attrValue = attrValue.substring(0, 100);
                }
              }

              subkey[keyLevel1] = attrValue;
            }
            if (keyLevel2) {
              digitalData[keyLevel2] = digitalData[keyLevel2] || {};
            }
            $.extend(digitalData[keyLevel2], subkey);
          }
        });

        window._satellite.track(satelliteTrackEvent);
      });
    },
    init: function init() {
      this.loadEvent();
      if (!w.digitalData) {
        return;
      }
      this.trackEvents();
    }
  };
})(window, window.jQuery, window.snro);

/*!
 * common.utils.js

 * This file contians some most common utility functions
 *
 * @project   SapientNitro Roche Diagonostics
 * @date      2017-07-17
 * @author    Shashank
 * @dependencies jQuery
 */

//this will cause the browser to check for errors more aggressively
'use strict';
/* eslint-disable new-cap */

/**
 * @namespace Main
 * @memberof snro
 * @property {null} property - description of property
 */

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

(function (window, $, snro) {
  snro = window.snro = snro || {};

  /**
   * Generic loader constructor to customize where to place the loader. Accepts any valid selector or jQuery object
   * @param {selector, HTMLElement, or jQuery object} loader
   */
  function _Loader(loader) {
    if (!(loader instanceof jQuery)) {
      loader = $(loader);
    }
    this.length = loader.length;
    if (!this.length) {
      return this;
    }
    this.htmlText = loader[0].outerHTML;
    this.loader = loader;
    this[0] = loader[0];
  }

  /**
   * Generic loader prototype methods
   */
  _Loader.prototype = {
    constructor: _Loader,
    target: function target(domEle) {
      if (this.length) {
        $(domEle).html(this.loader);
      }
      return this;
    },
    appendTo: function appendTo(domEle) {
      if (this.length) {
        $(domEle).append(this.loader);
      }
      return this;
    },
    insertAfter: function insertAfter(domEle) {
      if (this.length) {
        $(domEle).after(this.loader);
      }
      return this;
    },
    insertBefore: function insertBefore(domEle) {
      if (this.length) {
        $(domEle).before(this.loader);
      }
      return this;
    },
    remove: function remove() {
      return this.loader.remove();
    }
  };

  window.globalCache = snro.globalCache = {
    regex: {
      mobile: /android|webos|iphone|blackberry|iemobile|opera mini/i,
      tablet: /ipad|android 3.0|xoom|sch-i800|playbook|tablet|kindle/i,
      breakpoint: /:(?=([\s\d]+)px)/i
    },
    UA: navigator.userAgent.toLowerCase(),
    isKeyboardFocus: false
  };

  // check device type
  function _testUAPortableDevices() {
    return snro.globalCache.regex.mobile.test(snro.globalCache.UA) || snro.globalCache.regex.tablet.test(snro.globalCache.UA);
  }

  // Fire event when breakpoint is changed
  function _triggerBreakpointChange() {
    var breakpointInfo = $(document.body).pseudoContent(':after').match(snro.globalCache.regex.breakpoint);
    if (+breakpointInfo[1]) {
      breakpointInfo[1] = +breakpointInfo[1];
      // If previous breakpoint does not match current breakpoint
      if (snro.globalCache.breakpoint !== breakpointInfo[1]) {
        snro.commonUtils.log('Desktop Mode: Breakpoint change');
        snro.globalCache.breakpoint = breakpointInfo[1];
        $(window).trigger('responsive', [breakpointInfo[1]]);
      }
    }
  }

  // define custom events to handle multiple events
  function _defineCustomEvents() {
    // New event for enter key
    $(document.body).on('keydown', function (e) {
      var keyPressEvent = null;
      if (e.keyCode === 13) {
        // Enter key
        keyPressEvent = $.Event('enterKeyPress', $.extend(e, { type: 'enterKeyPress' }));
        $(e.target).trigger(keyPressEvent);
      }
      if (e.keyCode === 27) {
        // Escape key
        keyPressEvent = $.Event('escapeKey', $.extend(e, { type: 'escapeKey' }));
        $(e.target).trigger(keyPressEvent);
      }
      if (e.keyCode === 9) {
        //  Tab key
        $('.focussed').removeClass('focussed');
        snro.globalCache.isKeyboardFocus = true;
      }
    });
    $(document.body).on('mousedown', function () {
      snro.globalCache.isKeyboardFocus = false;
    });
    // New event for simulated click, useful for google analytics events
    $(document.body).on('mouseup', function (e) {
      if (e.which === 1) {
        // left click
        var simulatedClickEvent = $.Event('sclick', $.extend(e, { type: 'sclick' }));
        $(e.target).trigger(simulatedClickEvent);
      }
    });

    // Focus events
    $(document.body).on('focusin', snro.keyboardAccess);

    $(document).on('mouseenter focusin', '.x-overlay-link', function () {
      var parentList = $(this).parents('.x-overlay-link-container');
      parentList.find('.x-overlay-link').not(this).stop(true).fadeTo(300, 0.5);
    });

    $(document).on('mouseleave focusout', '.x-overlay-link', function () {
      var parentList = $(this).parents('.x-overlay-link-container');
      parentList.find('.x-overlay-link').stop(true).fadeTo(300, 1);
    });

    // Events for breakpoint change
    if (_testUAPortableDevices()) {
      $(window).on('orientationchange', function () {
        snro.commonUtils.log('Mobile mode: Orientation change');
        $(window).trigger('responsive', $(window).outerWidth());
      });
    } else {
      var breakpointInfo = $(document.body).pseudoContent(':after').match(snro.globalCache.regex.breakpoint);
      if (+breakpointInfo[1]) {
        snro.globalCache.breakpoint = +breakpointInfo[1];
        $(window).on('resize', _triggerBreakpointChange);
      }
    }
  }

  var _initializeRedirectPopup = function _initializeRedirectPopup() {
    var popupData = {
      'title': window.Granite.I18n.get('rdoe_redirectpopup.title'),
      'subTitle': window.Granite.I18n.get('rdoe_redirectpopup.subtitle'),
      'bodyText': window.Granite.I18n.get('rdoe_redirectpopup.desc'),
      'ctaLabel': window.Granite.I18n.get('rdoe_redirectpopup.catlabel')
    },
        redirectPopupTemplate = window.roche.templates['redirectPopup'],
        redirectPopupHTML = redirectPopupTemplate(popupData);

    $('body').append(redirectPopupHTML);

    $('body').on('click', '.js-redirect-ok', function () {
      var targetLink = $('.js-redirect-popup').data('target');
      $('.js-redirect-popup').modal('hide');
      window.location.href = targetLink;
    });
  },
      _handleExternalLinks = function _handleExternalLinks() {
    $('body').on('click', 'a', function (e) {
      e.preventDefault();
      var givenTarget = $(this).attr('href').trim();
      if (givenTarget) {
        if (givenTarget.indexOf(location.hostname) === -1 && givenTarget.substr(0, 1) !== '/' && givenTarget.substr(0, 1) !== '#' && givenTarget.substr(0, 10) !== 'javascript') {

          $('.js-redirect-popup').modal({
            'keyboard': false,
            backdrop: 'static'
          });

          $('.js-redirect-popup').data('target', givenTarget);
        } else {
          window.location.href = givenTarget;
        }
      }
    });
  };

  snro.commonUtils = {
    moduleName: 'commonUtils', // Added for debug logs
    // check if mobile
    isMobile: function isMobile() {
      return snro.globalCache.regex.mobile.test(snro.globalCache.UA);
    },
    // check if viewport is equal to mobile (resized desktop browser)
    isMobileMode: function isMobileMode() {
      return $(window).outerWidth() < 768;
    },
    // check if tablet
    isTablet: function isTablet() {
      return snro.globalCache.regex.tablet.test(snro.globalCache.UA);
    },
    // check if viewport is equal to tablet (resized desktop browser)
    isTabletMode: function isTabletMode() {
      return $(window).outerWidth() >= 768 && $(window).outerWidth() < 992;
    },
    // check if desktop
    isDesktop: function isDesktop() {
      return !this.isMobileOrTablet();
    },
    // check if desktop or tablet viewport width
    isTabletOrDesktopMode: function isTabletOrDesktopMode() {
      return this.isTabletMode() || this.isDesktopMode();
    },
    // check if viewport width is qualified desktop
    isDesktopMode: function isDesktopMode() {
      return $(window).outerWidth() >= 992;
    },
    // check if mobile or tablet
    isMobileOrTablet: function isMobileOrTablet() {
      return this.isMobile() || this.isTablet();
    },
    // check if iframe is resized
    resizeIframe: function resizeIframe(obj) {
      if (obj instanceof window.Node && obj.nodeType === 1) {
        // Is DOM element other than document
        obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
      }
    },
    // set local cookie
    setCookie: function setCookie(key, value, exp, path, domain) {
      if (!(typeof key === 'string' && key.length)) {
        return; // Key is mandatory
      }
      if (typeof value !== 'string') {
        value = '';
      } //If value is invalid by default empty string will be set
      var dt = new Date();
      if (typeof exp === 'number') {
        if (exp === Infinity) {
          dt = new Date('Thu, 31 Dec 2037 00:00:00 GMT');
        } else {
          dt.setTime(dt.getTime() + exp * 24 * 60 * 60 * 1000);
        }
      }
      var expires = exp ? '; expires=' + dt.toUTCString() : '',
          cookiePath = '; path=' + (typeof path === 'string' ? path.trim() : '/'),
          defaultDomain = window.location.hostname,
          cookieDomain = '';
      if (defaultDomain === 'localhost') {
        // IE does not allow localhost domain
        if (typeof domain === 'string') {
          cookieDomain = '; domain=' + domain.trim();
        }
      } else {
        cookieDomain = '; domain=' + (typeof domain === 'string' ? domain.trim() : defaultDomain);
      }

      var secureCookieFlag = '';
      if (location.protocol === 'https:') {
        secureCookieFlag = '; secure';
      }
      document.cookie = key + '=' + value + expires + cookieDomain + cookiePath + secureCookieFlag;
    },
    // get cookie
    getCookie: function getCookie(key) {
      if (!(typeof key === 'string' && key.length)) {
        return '';
      }
      var cookieString = decodeURIComponent(document.cookie),
          index = 0,
          allCookies = [],
          c = '';
      key += '=';
      if ((allCookies = cookieString.split(';')).length) {
        for (; index < allCookies.length; index++) {
          if (~(c = allCookies[index].trim()).indexOf(key)) {
            return c.substring(key.length, c.length).trim();
          }
        }
      }
      return '';
    },
    // remove cookie
    removeCookie: function removeCookie(key, path, domain) {
      if (!(typeof key === 'string' && key.length)) {
        return false;
      }
      var cookiePath = typeof path === 'string' ? path : '/',
          defaultDomain = window.location.hostname,
          cookieDomain = '',
          deletedCookieString = '';
      if (defaultDomain === 'localhost') {
        // IE does not allow localhost domain
        if (typeof domain === 'string') {
          cookieDomain = '; domain=' + domain.trim();
        }
      } else {
        cookieDomain = '; domain=' + (typeof domain === 'string' ? domain.trim() : defaultDomain);
      }
      deletedCookieString = key + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC' + cookieDomain + '; path=' + cookiePath;
      document.cookie = deletedCookieString;
      return !this.getCookie(key).length; // Ensure if cookie has been deleted
    },
    // reset cookie
    resetCookie: function resetCookie(key, value, exp, path, domain) {
      this.removeCookie(key, path, domain);
      this.setCookie(key, value, exp, path, domain);
    },
    // set in storage (session / local / cookie) based on fallback
    storage: {
      available: typeof window.Storage === 'function', // True only if webstorage is available
      // Method to store key values in any available storages
      set: function set(key, value, isSession) {
        if (!(typeof key === 'string' && key.length)) {
          return;
        }
        isSession = typeof isSession === 'boolean' ? isSession : false; // By default localStorage will be used
        var vl = (typeof value === 'undefined' ? 'undefined' : _typeof(value)) === 'object' && value !== null ? JSON.stringify(value) : value;
        // Check if storage is defined
        if (this.available) {
          try {
            if (isSession) {
              window.sessionStorage.setItem(key, vl);
            } else {
              window.localStorage.setItem(key, vl);
            }
            return;
          } catch (e) {
            // catch error here
          }
        }
        // If control has reached here, it means storage operation was unsuccessful and we need to set a cookie instead
        if (isSession) {
          // Set a session cookie
          snro.commonUtils.setCookie(key, vl);
        } else {
          snro.commonUtils.setCookie(key, vl, Infinity);
        }
        return;
      },
      // Method to remove key from all available storages
      remove: function remove(key) {
        if (!(typeof key === 'string' && key.length)) {
          return false;
        }
        if (this.available) {
          try {
            window.localStorage.removeItem(key);
            window.sessionStorage.removeItem(key);
            return !window.localStorage.key(key) || !window.sessionStorage.key(key) || snro.commonUtils.removeCookie(key);
          } catch (e) {
            //catch error here
          }
        }
        return snro.commonUtils.removeCookie(key);
      },
      // Get stored values from all available storages
      getAll: function getAll(key, isSession) {
        var returnValue = [],
            cookieValue = null;
        isSession = typeof isSession === 'boolean' ? isSession : false;
        if (this.available) {
          try {
            if (Object.prototype.hasOwnProperty.call(window.sessionStorage, key) && !isSession) {
              returnValue.push({ value: window.sessionStorage.getItem(key), storage: 'sessionStorage' });
            }
            if (Object.prototype.hasOwnProperty.call(window.localStorage, key)) {
              returnValue.push({ value: window.localStorage.getItem(key), storage: 'localStorage' });
            }
          } catch (e) {
            // catch error here
          }
        }
        if ((cookieValue === snro.commonUtils.getCookie(key)).length) {
          returnValue.push({ value: cookieValue, storage: 'cookie' });
        }
        return returnValue.map(function (data) {
          try {
            data.value = JSON.parse(data.value);
            return data;
          } catch (e) {
            return data;
          }
        });
      },
      // Get stored value from first match
      get: function get(key, isSession) {
        var storedValue = null;
        isSession = typeof isSession === 'boolean' ? isSession : false;
        if (!isSession) {
          // Check session storage first. Session storage should always have priority over local storage
          storedValue = this.getFromSessionStorage(key);
          if (!storedValue) {
            storedValue = this.getFromLocalStorage(key);
          }
        } else {
          // If isSession is true, then session storage is forced. In means we cannot get value from local storage
          storedValue = this.getFromSessionStorage(key);
        }
        // If neither of the storages have value. It means value could be in cookies
        if (!storedValue && !(storedValue = this.getFromCookies(key))) {
          return; // Return undefined
        }
        // Return the value part if value object has been successfully received
        return storedValue.value;
      },
      // update the value in storage
      update: function update(key, callbackOrValue, isSession) {
        var value = this.get(key);
        if (typeof callbackOrValue === 'function') {
          this.set(key, callbackOrValue(value, key), isSession);
        } else {
          this.set(key, callbackOrValue, isSession);
        }
      },
      // Get stored value from local storage only
      getFromLocalStorage: function getFromLocalStorage(key) {
        return this.getAll(key, true).filter(function (valueOb) {
          return valueOb.storage === 'localStorage';
        })[0];
      },
      // Get stored value from session storage only
      getFromSessionStorage: function getFromSessionStorage(key) {
        return this.getAll(key).filter(function (valueOb) {
          return valueOb.storage === 'sessionStorage';
        })[0];
      },
      // Get stored value from cookies only
      getFromCookies: function getFromCookies(key) {
        return this.getAll(key).filter(function (valueOb) {
          return valueOb.storage === 'cookie';
        })[0];
      }
    },
    // add ellipsis to any element container with text
    addEllipses: function addEllipses() {
      $('.ellipses').dotdotdot({ watch: true });
    },

    // get fullname of country
    getCountryName: function getCountryName(countryCode) {
      if (typeof countryCode === 'string') {
        switch (countryCode.toLowerCase()) {
          case 'us':
            return 'United States';
          case 'uk':
            return 'United Kingdom';
          case 'fr':
            return 'France';
          case 'jp':
            return 'Japan';
          case 'cn':
            return 'China';
          case 'hk':
            return 'Hong Kong';
          case 'sg':
            return 'Singapore';
          default:
            return countryCode;
        }
      }
    },
    // check if user is logged in
    isLoggedIn: function isLoggedIn() {
      return !!this.getCookie('loggedIn');
    },
    // add loader to any target
    loader: function loader(target, appendMode) {
      var overlay = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;

      target = target || 'body';
      var loader = void 0,
          loadercol = $('<div>').addClass('loading-icon js-loading-icon').prepend('<span></span><span></span>');
      if (!overlay) {
        loader = loadercol;
      } else {
        loader = $('<div>').addClass('loader-backdrop').wrapInner(loadercol);
      }
      if (target) {
        if (appendMode) {
          $(target).append(loader);
        } else {
          $(target).html(loader);
        }
      } else {
        return new _Loader(loader);
      }
    },

    // log to console if debug mode
    log: function log() {
      try {
        // Enable logs if development environment is true or debugClientLibs param is provided
        if (snro.commonUtils.queryParams('debugClientLibs')) {
          console.log(arguments[0]);
        }
      } catch (e) {
        // catch error here
      }
    },
    // get value of any url query parameter
    queryParams: function queryParams(name, url) {
      if (!url) {
        url = location.href;
      }
      name = name.replace(/[\[]/, '\\\[').replace(/[\]]/, '\\\]');
      var regexS = '[\\?&]' + name + '=([^&#]*)',
          regex = new RegExp(regexS),
          results = regex.exec(url);
      return results === null ? null : results[1];
    },

    // initialize lazyloading of images
    lazyloadImages: function lazyloadImages() {
      $('[data-original]').lazyload({
        threshold: 999
      });
    },


    // Loader constructor
    Loader: _Loader,
    // init method
    init: function init() {
      // Added since init is mandatory for all modules
      this.addEllipses();
      _initializeRedirectPopup();
      _handleExternalLinks();
      _defineCustomEvents();
      // Adding commonUtils in global space
      if (!window.commonUtils) {
        // Unless there is another commonUtils js library available globally, add commonUtils to global namespace
        window.commonUtils = this;
      }
    }
  };
})(window, window.jQuery, window.snro);

/*
 * core.js
 * [ To handle core functionalities on app level]
 *
 * @project:    SN-RO
 * @date:       2015-07-17
 * @author:     Shashank
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

var snro = window.snro || {};

// self executing function to perform core tasks

$(function () {
  var moduleArray = ['ajaxWrapper', 'commonUtils', 'modalPopComp', 'liipboxComp', 'dynamicMedia', 'analytics'];
  $('[data-module]').each(function () {
    var currentModule = $(this).attr('data-module');
    // check if no double entry of module
    if (moduleArray.indexOf(currentModule) === -1) {
      moduleArray.push(currentModule);
    }
  });

  // check of all available selectors to initialize corresponsing modules
  $.each(moduleArray, function (index, value) {
    try {
      // initialize the current module
      snro[value].init();
    } catch (e) {
      // catch error, if any, while initialing module
      snro.commonUtils.log(value + ' doesn\'t have init method.');
    }
  });

  //Check for targeted modules and call their init functions
  if (window.ContextHub) {
    window.ContextHub.eventing.on(window.ContextHub.SegmentEngine.PageInteraction.Teaser.prototype.info.loadEvent, function () {
      //TODO: create a list of all personalized modules and iterate through them here.
      if ($('[data-module="productCategoryList"]').length) {
        window.snro.productCategoryList.init();
      }
    });
  }
});

/*
 * c_headerSearchBar.js
 * [ This javascript code is used to display carousel component. ]
 *
 * @project:    SN-RO
 * @date:       2017-08-17
 * @author:     Bindhyachal
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

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

(function (window, $, snro) {
  snro = window.snro = snro || {};
  var _autoSuggest = false;

  snro.headerSearchComp = {
    moduleName: 'headerSearchComp',
    // bind dom events
    bindEvents: function bindEvents() {
      var liElement = void 0,
          liSelected = void 0,
          nextElement = void 0,
          metaTagAttribute = $('meta[name="collection"]').attr('content'),
          metaTagPersona = snro.commonUtils.getCookie('persona-type'),
          defaultFontSizHeaderBar = $('#header-search-input').css('font-size'),
          defaultFontSizeSearchResult = $('#search-page-input').css('font-size');
      //click on body elements
      $('body').on('click', function (e) {
        if (e.target.className !== 'header-search-result-container') {
          $('.js-header-search-overlay .header-search-result-container').hide();
        } else {
          $('.js-header-search-overlay .header-search-result-container').show();
        }
      });
      //focus event for placeholder text
      $('body').on('focus', '.roche-header-search-input', function (e) {
        $(e.currentTarget).attr('placeholder', '');
      });
      //blur event for placeholder text
      $('body').on('blur', '.roche-header-search-input', function (e) {
        $(e.currentTarget).attr('placeholder', $(e.currentTarget).attr('data-placeholder'));
      });
      //function to shrink input box size
      var measureText = function measureText(txt, font) {
        var id = 'text-width-tester',
            $tag = $('#' + id);
        if (!$tag.length) {
          $tag = $('<span id="' + id + '" style="display:none;font:' + font + ';">' + txt + '</span>');
          $('body').append($tag);
        } else {
          $tag.css({ font: font }).text(txt);
        }
        return {
          width: $tag.width(),
          height: $tag.height()
        };
      };
      var shrinkToFill = function shrinkToFill(input, fontSize, fontWeight, fontFamily) {
        var $input = $(input),
            txt = $input.val().replace(/\s/g, 'x'),
            maxWidth = $input.width(),
            // add some padding
        font = fontWeight + ' ' + fontSize + 'px ' + fontFamily;
        // see how big the text is at the default size
        var textWidth = measureText(txt, font).width;
        if (textWidth > maxWidth) {
          // if it's too big, calculate a new font size
          // the extra .9 here makes up for some over-measures
          fontSize = fontSize * maxWidth / textWidth * .9;
          font = fontWeight + ' ' + fontSize + 'px ' + fontFamily;
          // and set the style on the input
        }
        $input.css({ font: font });
      };
      this.bindEvents.shrinkSearchResult = function (input, fontSize, fontWeight, fontFamily) {
        shrinkToFill(input, fontSize, fontWeight, fontFamily);
      };
      //key up event in input search box
      $('body').on('keyup', '.roche-header-search-input', function (e) {
        var specialKeyCodes = [37, 38, 39, 40, 65, 17];
        if (specialKeyCodes.indexOf(e.which) === -1) {
          var targetInput = e.currentTarget,
              start = targetInput.selectionStart,
              end = 0,
              newVal = $(targetInput).val(),
              charLength = newVal.length,
              charLengthNew = 0;

          newVal = newVal.replace(/[*?]/g, '').replace(/</g, '&lt;').replace(/>/g, '&gt;');
          charLengthNew = newVal.length;
          end = start + (charLengthNew - charLength);
          $(targetInput).val(newVal);
          targetInput.selectionEnd = end;
        }

        var inputStringValue = $(e.currentTarget).val(),
            parametersToSend = {},
            $headerSearchResult = $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-result'),
            $headerContainer = $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-result-container'),
            $predictiveContainer = $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-predictive'),
            $predictiveItem = $(e.currentTarget).parents('.js-header-search-overlay').find('.header-predictive-item'),
            requestedUrl = $('.js-link-search').data('searchurl') + '/?ac=' + inputStringValue + '&type=autocomplete&locale=' + metaTagAttribute + '&limit=5' + '&ps=' + metaTagPersona;
        if (snro.commonUtils.isDesktopMode()) {
          if (!$(e.currentTarget).parents('.c-search-results-bar').length) {
            shrinkToFill($(e.currentTarget), parseInt(defaultFontSizHeaderBar), '', $(e.currentTarget).css('font-family'));
          } else {
            shrinkToFill($(e.currentTarget), parseInt(defaultFontSizeSearchResult), '', $(e.currentTarget).css('font-family'));
          }
        }
        if (e.which === 40 || e.which === 38) {
          e.preventDefault();return;
        }
        if (!(e.which === 40 || e.which === 38) && inputStringValue.length > 2) {
          $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-button').addClass('header-search-button--active');
          $('.js-header-search-overlay .header-search-predictive').hide();
          liSelected = '';

          //predictive Search Item
          var checkPredictiveSearchItem = function checkPredictiveSearchItem(items) {
            if (items.length) {
              $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-predictive').show();
              for (var i = 0; i < items.length; i++) {
                if (i + 1 === items.length) {
                  $(e.currentTarget).parents('.js-header-search-overlay').find('.header-predictive-item').append('<span class="js-suggestive-terms header-predictive-item--val">' + items[i].suggestTerm + '</span>');
                } else {
                  $(e.currentTarget).parents('.js-header-search-overlay').find('.header-predictive-item').append('<span class="js-suggestive-terms header-predictive-item--val">' + items[i].suggestTerm + '<span class="suggestive-helper--text js-suggestive-helper--text">' + ' ' + window.Granite.I18n.get('rdoe_search.suggestion_ortext') + ' ' + '</span></span>');
                }
              }
            } else {
              $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-predictive').hide();
            }
          };
          //success ajax callback
          var populateSuggestions = function populateSuggestions(data) {
            if (inputStringValue.length === 0) {
              $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-result').fadeOut();
            } else {
              // Show the result List box
              var $ul = $('<ul>'),
                  jsonData = '';
              try {
                jsonData = JSON.parse(data);
              } catch (e) {
                jsonData = data;
              }
              if ((typeof jsonData === 'undefined' ? 'undefined' : _typeof(jsonData)) === 'object' && jsonData !== null) {
                if (jsonData.results.length > 0) {
                  _autoSuggest = true;
                  $(jsonData.results).each(function (i, match) {
                    var trimmedText = '',
                        originalText = '',
                        startWith = new RegExp('^' + inputStringValue),
                        emptyString = ' ',
                        endsWithEmpty = new RegExp(emptyString + '$'),
                        endsCharacters = '';
                    if (match.title.toLocaleLowerCase().indexOf(inputStringValue.toLocaleLowerCase()) !== -1) {
                      trimmedText = match.title.substring(inputStringValue.length, match.title.length);
                      originalText = match.title.substring(0, inputStringValue.length);
                    }
                    if (match.title.toLocaleLowerCase().match(startWith)) {
                      // do this if begins with Hello
                      $ul.append('<li class="drop-text"><a class="x-sub-hover" href="' + match.url + '" title="' + match.url + '"><span class="color-white">' + originalText + '</span><span class="color-blue">' + trimmedText + '</span><span class="color-blue search-helper-text">' + window.Granite.I18n.get('rdoe_search.suggestion_intext') + '</span><span class="color-white">' + match.pageType + '</span></a></li>');
                    } else {
                      if (inputStringValue.match(endsWithEmpty)) {
                        inputStringValue = inputStringValue.trim();
                      }

                      if (match.title.toLocaleLowerCase().indexOf(inputStringValue.toLocaleLowerCase()) === -1) {
                        trimmedText = match.title;
                      } else {
                        trimmedText = match.title.substr(0, match.title.toLocaleLowerCase().indexOf(inputStringValue.toLocaleLowerCase()));
                        originalText = match.title.substr(match.title.toLocaleLowerCase().indexOf(inputStringValue.toLocaleLowerCase()), inputStringValue.length);
                        endsCharacters = match.title.substr(match.title.toLocaleLowerCase().indexOf(inputStringValue.toLocaleLowerCase()) + inputStringValue.length, match.title.length);
                      }
                      $ul.append('<li class="drop-text"><a class="x-sub-hover" href="' + match.url + '" title="' + match.url + '"><span class="color-blue">' + trimmedText + '</span><span class="color-white">' + originalText + '</span><span class="color-blue">' + endsCharacters + '</span><span class="color-blue search-helper-text">' + window.Granite.I18n.get('rdoe_search.suggestion_intext') + '</span><span class="color-white">' + match.pageType + '</span></a></li>');
                    }
                  });
                  $headerSearchResult.empty().append($ul).fadeIn();
                  $headerContainer.show();
                  $predictiveContainer.fadeOut();
                } else {
                  $headerSearchResult.fadeOut();
                  $predictiveItem.empty();
                  //condition for predictive search to go here
                  checkPredictiveSearchItem(jsonData.suggestions);
                }
              } else {
                $('.js-header-search-overlay .header-search-result').fadeOut();
              }
            }
          };
          if (navigator.appVersion.indexOf('MSIE 9') !== -1) {
            $.getJSON(requestedUrl).done(function (data) {
              populateSuggestions(data);
            });
          } else {
            parametersToSend = {
              url: requestedUrl,
              type: 'GET',
              dataType: 'json'
            };
            snro.ajaxWrapper.getXhrObj(parametersToSend).done(function (data) {
              populateSuggestions(data);
            }).fail(function (err) {
              console.log(err);
            });
          }
        } else {
          $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-button').removeClass('header-search-button--active');
          $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-result').fadeOut();
          $predictiveContainer.fadeOut();
          $headerContainer.fadeOut();
          var intervalTimer = setTimeout(function () {
            if (e.which === 8 && inputStringValue.length <= 2) {
              $predictiveContainer.fadeOut();
            }
            clearTimeout(intervalTimer);
          }, 1000);
        }
      });

      //function for arrow key function accessibility
      $('body').on('keydown', '.roche-header-search-input', function (e) {
        liElement = $('.js-header-search-overlay .header-search-result ul li');
        //let key = e.keyCode;
        if (e.which === 40) {
          //down arrow
          if (liSelected) {

            liSelected.removeClass('selected');
            nextElement = liSelected.next();
            if (nextElement.length > 0) {
              liSelected = nextElement.addClass('selected');
            } else {
              liSelected = liElement.eq(0).addClass('selected');
            }
          } else {
            liSelected = liElement.eq(0).addClass('selected');
          }
          $(liSelected).focus();
        } else if (e.which === 38) {
          //up arror
          if (liSelected) {

            liSelected.removeClass('selected');
            nextElement = liSelected.prev();
            if (nextElement.length > 0) {
              liSelected = nextElement.addClass('selected');
            } else {
              liSelected = liElement.last().addClass('selected');
            }
          } else {
            liSelected = liElement.last().addClass('selected');
          }
          $(liSelected).focus();
        } else if (e.which === 13) {
          // the enter key code
          if (liSelected) {
            location.href = $(liSelected).find('a').attr('href');
          } else {
            $('.header-search-button').trigger('click');
          }
          $('.js-header-search-overlay .header-search-result').hide();
        }
      });
      //search button click
      $('body').on('click', '.header-search-button', function (e) {
        var searchResult = $(e.currentTarget).parents('.roche-header-search-box').find('.roche-header-search-input').val();
        if (searchResult.length > 2) {
          location.href = $('.js-link-search').data('searchresult') + '.html?q=' + searchResult + '&autoSuggest=' + _autoSuggest;
        }
      });
      $('body').on('click', '.js-suggestive-terms', function (e) {
        if (!$(e.target).hasClass('js-suggestive-helper--text')) {
          if ($(e.currentTarget).find('.js-suggestive-helper--text').text()) {
            $(e.currentTarget).parents('.js-header-search-overlay').find('.roche-header-search-input').val($(e.currentTarget).text().split($(e.currentTarget).find('.js-suggestive-helper--text').text())[0]);
          } else {
            $(e.currentTarget).parents('.js-header-search-overlay').find('.roche-header-search-input').val($(e.currentTarget).text());
          }
          $(e.currentTarget).parents('.js-header-search-overlay').find('.header-search-predictive').hide();
          $('.roche-header-search-input').trigger('keyup');
          $(e.currentTarget).parents('.js-header-search-overlay').find('.roche-header-search-input').focus();
        }
      });
    },
    prefillSearchBox: function prefillSearchBox() {
      var searchParam = snro.commonUtils.queryParams('q').trim();
      if (searchParam !== '') {
        $('#search-page-input').val(decodeURI(searchParam));
        snro.headerSearchComp.bindEvents.shrinkSearchResult($('#search-page-input'), parseInt($('#search-page-input').css('font-size')), '', $('#search-page-input').css('font-family'));
        if (searchParam.length > 2) {
          $('#search-page-input').parents('.header-search-overlay').find('.header-search-button').addClass('header-search-button--active');
        }
      }
    },
    init: function init() {
      this.bindEvents();
      this.prefillSearchBox();
    }
  };
})(window, window.jQuery, window.snro);

this["roche"] = this["roche"] || {};
this["roche"]["templates"] = this["roche"]["templates"] || {};

this["roche"]["templates"]["newsEventsListingTile"] = Handlebars.template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return " <section class=\"c-listingtile\">\r\n    <div class=\"c-listingtile__row\">\r\n      <a href=\""
    + alias4(((helper = (helper = helpers.url || (depth0 != null ? depth0.url : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"url","hash":{},"data":data}) : helper)))
    + "\" itemprop=\"url\" class=\"c-listingtile__link\"\r\n         data-fn-click=\"true\"\r\n         data-da-satelliteTrackEvent=\"search-click-tracking\"\r\n         data-da-link_link-Page-Name=\"\"\r\n         data-da-link_link-Category=\"\"\r\n         data-da-link_link-Section=\"news\"\r\n         data-da-link_link-Text=\"news\"\r\n         data-da-link_link-Interaction-Method=\"\"\r\n         data-da-link_content-type=\"news\"\r\n         data-da-link_content-title=\""
    + alias4(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\"\r\n         data-da-link_content-id=\"\"\r\n         data-da-link_content-AdditionalInformation=\"\"\r\n         data-da-link_link-header=\"news\"\r\n         data-da-link_event=\"content-clicked\"\r\n         data-da-search_selected-position=\""
    + alias4(((helper = (helper = helpers.searchIndex || (depth0 != null ? depth0.searchIndex : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"searchIndex","hash":{},"data":data}) : helper)))
    + "\">\r\n        <div class=\"c-listingtile__col\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.eventDateFormat : depth0),{"name":"if","hash":{},"fn":container.program(2, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.title : depth0),{"name":"if","hash":{},"fn":container.program(4, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.subTitle : depth0),{"name":"if","hash":{},"fn":container.program(6, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.eventDateRange : depth0),{"name":"if","hash":{},"fn":container.program(8, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.description : depth0),{"name":"if","hash":{},"fn":container.program(13, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        </div>\r\n      </a>\r\n    </div>\r\n  </section>\r\n";
},"2":function(container,depth0,helpers,partials,data) {
    var helper;

  return "          <div class=\"c-listingtile__date\" data-dateformat=\""
    + container.escapeExpression(((helper = (helper = helpers.eventDateFormat || (depth0 != null ? depth0.eventDateFormat : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"eventDateFormat","hash":{},"data":data}) : helper)))
    + "\"></div>\r\n";
},"4":function(container,depth0,helpers,partials,data) {
    var helper;

  return "            <p class=\"c-listingtile__title\">"
    + container.escapeExpression(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"title","hash":{},"data":data}) : helper)))
    + "</p>\r\n";
},"6":function(container,depth0,helpers,partials,data) {
    var helper;

  return "            <p class=\"c-listingtile__subTitle\">"
    + container.escapeExpression(((helper = (helper = helpers.subTitle || (depth0 != null ? depth0.subTitle : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"subTitle","hash":{},"data":data}) : helper)))
    + "</p>\r\n";
},"8":function(container,depth0,helpers,partials,data) {
    var stack1, alias1=depth0 != null ? depth0 : (container.nullContext || {});

  return "          <ul class=\"c-listingtile__event\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.eventDateFormat : depth0),{"name":"if","hash":{},"fn":container.program(9, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.venue : depth0),{"name":"if","hash":{},"fn":container.program(11, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "          </ul>\r\n";
},"9":function(container,depth0,helpers,partials,data) {
    var helper;

  return "              <li class=\"c-listingtile__eventdata\">"
    + container.escapeExpression(((helper = (helper = helpers.eventDateRange || (depth0 != null ? depth0.eventDateRange : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"eventDateRange","hash":{},"data":data}) : helper)))
    + "</li>\r\n";
},"11":function(container,depth0,helpers,partials,data) {
    var helper;

  return "              <li class=\"c-listingtile__eventdata\">"
    + container.escapeExpression(((helper = (helper = helpers.venue || (depth0 != null ? depth0.venue : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"venue","hash":{},"data":data}) : helper)))
    + "</li>\r\n";
},"13":function(container,depth0,helpers,partials,data) {
    var helper;

  return "            <div class=\"c-listingtile__description\">"
    + container.escapeExpression(((helper = (helper = helpers.description || (depth0 != null ? depth0.description : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"description","hash":{},"data":data}) : helper)))
    + "</div>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "");
},"useData":true});

this["roche"]["templates"]["productFeaturedTile"] = Handlebars.template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "<div class=\"c-featuredTile__list row\">\r\n  <a href=\""
    + alias4(((helper = (helper = helpers.url || (depth0 != null ? depth0.url : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"url","hash":{},"data":data}) : helper)))
    + "\" class=\"c-featuredTile__link x-sub-hover\"\r\n     data-fn-click=\"true\"\r\n     data-da-satelliteTrackEvent=\"product-click-tracking\"\r\n     data-da-link_link-Page-Name=\"\"\r\n     data-da-link_link-Category=\"\"\r\n     data-da-link_link-Section=\"product-tile\"\r\n     data-da-link_link-Text=\""
    + alias4(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\"\r\n     data-da-link_link-Interaction-Method=\"\"\r\n     data-da-link_product-name=\""
    + alias4(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\"\r\n     data-da-link_product-category=\""
    + alias4(((helper = (helper = helpers.category || (depth0 != null ? depth0.category : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"category","hash":{},"data":data}) : helper)))
    + "\"\r\n     data-da-link_product-id=\""
    + alias4(((helper = (helper = helpers.id || (depth0 != null ? depth0.id : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"id","hash":{},"data":data}) : helper)))
    + "\"\r\n     data-da-link_link-Header=\"product\"\r\n     data-da-search_selected-position=\""
    + alias4(((helper = (helper = helpers.searchIndex || (depth0 != null ? depth0.searchIndex : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"searchIndex","hash":{},"data":data}) : helper)))
    + "\"\r\n     data-da-link_event=\"product-click\">\r\n    <div class=\"c-featuredTile__productWrapper col-xs-12\">\r\n      <div class=\"c-featuredTile__productAsset\">\r\n        <img src=\"/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/blank.png\" data-src=\""
    + alias4(((helper = (helper = helpers.image || (depth0 != null ? depth0.image : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"image","hash":{},"data":data}) : helper)))
    + "\"\r\n          alt=\""
    + alias4(((helper = (helper = helpers.altText || (depth0 != null ? depth0.altText : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"altText","hash":{},"data":data}) : helper)))
    + "\" class=\"js-dynamic-image\" data-template=\"featuredTile\" data-processed=\"false\"\r\n        />\r\n      </div>\r\n      <div class=\"c-featuredTile__productContent\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.title : depth0),{"name":"if","hash":{},"fn":container.program(2, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.category : depth0),{"name":"if","hash":{},"fn":container.program(4, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "      </div>\r\n    </div>\r\n  </a>\r\n</div>\r\n";
},"2":function(container,depth0,helpers,partials,data) {
    var helper;

  return "        <span class=\"c-featuredTile__productTitle\">"
    + container.escapeExpression(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"title","hash":{},"data":data}) : helper)))
    + "</span>\r\n";
},"4":function(container,depth0,helpers,partials,data) {
    var helper;

  return "        <span class=\"c-featuredTile__productCategory\">"
    + container.escapeExpression(((helper = (helper = helpers.category || (depth0 != null ? depth0.category : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"category","hash":{},"data":data}) : helper)))
    + "</span>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "");
},"useData":true});

this["roche"]["templates"]["productListingTile"] = Handlebars.template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "<div class=\"c-listingTile__list\">\r\n  <a href=\""
    + alias4(((helper = (helper = helpers.url || (depth0 != null ? depth0.url : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"url","hash":{},"data":data}) : helper)))
    + "\" class=\"c-listingTile__link x-sub-hover\"\r\n     data-fn-click=\"true\"\r\n     data-da-satelliteTrackEvent=\"search-click-tracking\"\r\n     data-da-link_link-Page-Name=\"\"\r\n     data-da-link_link-Category=\"\"\r\n     data-da-link_link-Section=\"product-tile\"\r\n     data-da-link_link-Text=\""
    + alias4(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\"\r\n     data-da-link_link-Interaction-Method=\"\"\r\n     data-da-link_product-name=\""
    + alias4(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\"\r\n     data-da-link_product-category=\""
    + alias4(((helper = (helper = helpers.category || (depth0 != null ? depth0.category : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"category","hash":{},"data":data}) : helper)))
    + "\"\r\n     data-da-link_product-id=\""
    + alias4(((helper = (helper = helpers.id || (depth0 != null ? depth0.id : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"id","hash":{},"data":data}) : helper)))
    + "\"\r\n     data-da-link_event=\"product-click\"\r\n     data-da-link_link-Header=\"product\"\r\n     data-da-search_selected-position=\""
    + alias4(((helper = (helper = helpers.searchIndex || (depth0 != null ? depth0.searchIndex : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"searchIndex","hash":{},"data":data}) : helper)))
    + "\">\r\n    <div class=\"c-listingTile__productWrapper\">\r\n      <div class=\"c-listingTile__productContent col-xs-8\">\r\n        <div class=\"c-listingTile__productContentWrapper\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.title : depth0),{"name":"if","hash":{},"fn":container.program(2, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.category : depth0),{"name":"if","hash":{},"fn":container.program(4, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        </div>\r\n      </div>\r\n      <div class=\"c-listingTile__productAsset\">\r\n        <img src=\"/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/blank.png\" data-src=\""
    + alias4(((helper = (helper = helpers.image || (depth0 != null ? depth0.image : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"image","hash":{},"data":data}) : helper)))
    + "\"\r\n          alt=\""
    + alias4(((helper = (helper = helpers.altText || (depth0 != null ? depth0.altText : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"altText","hash":{},"data":data}) : helper)))
    + "\" class=\"js-dynamic-image\" data-template=\"productTile\" data-processed=\"false\"\r\n        />\r\n      </div>\r\n    </div>\r\n  </a>\r\n</div>\r\n";
},"2":function(container,depth0,helpers,partials,data) {
    var helper;

  return "          <span class=\"c-listingTile__productTitle\">"
    + container.escapeExpression(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"title","hash":{},"data":data}) : helper)))
    + "</span>\r\n";
},"4":function(container,depth0,helpers,partials,data) {
    var helper;

  return "          <span class=\"c-listingTile__productCategory\">"
    + container.escapeExpression(((helper = (helper = helpers.category || (depth0 != null ? depth0.category : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"category","hash":{},"data":data}) : helper)))
    + "</span>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "");
},"useData":true});

this["roche"]["templates"]["redirectPopup"] = Handlebars.template({"1":function(container,depth0,helpers,partials,data) {
    var helper;

  return "          <h3 class=\"modal-subtitle\">"
    + container.escapeExpression(((helper = (helper = helpers.subTitle || (depth0 != null ? depth0.subTitle : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"subTitle","hash":{},"data":data}) : helper)))
    + "</h3>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "<!-- Modal -->\r\n<div class=\"modal x-modal fade js-redirect-popup\" role=\"dialog\" data-target=\"\" aria-labelledby=\""
    + alias4(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\">\r\n  <div class=\"modal-dialog\" role=\"document\">\r\n    <!-- Modal content-->\r\n    <div class=\"modal-content\">\r\n      <div class=\"modal-header\">\r\n        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\r\n            Close\r\n        </button>\r\n        <h2 class=\"modal-title\">"
    + alias4(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "</h4>\r\n      </div>\r\n      <div class=\"modal-body\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.subTitle : depth0),{"name":"if","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        <p class=\"x-text modal-desc\">"
    + alias4(((helper = (helper = helpers.bodyText || (depth0 != null ? depth0.bodyText : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"bodyText","hash":{},"data":data}) : helper)))
    + "</p>\r\n        <button type=\"button\" class=\"x-submit-button js-redirect-ok\" data-da-satelliteTrackEvent=\"generic-link-tracking\"\r\n               data-da-link_link-page-name=\"\" \r\n               data-da-link_link-category=\"\" \r\n               data-da-link_link-section=\"global-top-navigation\"\r\n               data-da-link_link-header=\"hamburger-menu-overlay\"\r\n               data-da-link_link-text=\"${'rdoe_redirctpopup.agree' @ i18n}\">"
    + alias4(((helper = (helper = helpers.ctaLabel || (depth0 != null ? depth0.ctaLabel : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"ctaLabel","hash":{},"data":data}) : helper)))
    + "</button>\r\n      </div>\r\n    </div>\r\n  </div>\r\n</div>";
},"useData":true});

this["roche"]["templates"]["searchFilters"] = Handlebars.template({"1":function(container,depth0,helpers,partials,data,blockParams,depths) {
    var stack1, helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "            <div class=\"panel-heading c-filter__heading\">\r\n                <h4 class=\"panel-title\">\r\n                    <a data-toggle=\"collapse\" data-target=\"#collapse"
    + alias4(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"index","hash":{},"data":data}) : helper)))
    + "\" class=\"c-filter__heading-link x-sub-hover\">\r\n                        <span class=\"c-filter__collapse-cta\"></span>"
    + alias4((helpers.i18keyToValue || (depth0 && depth0.i18keyToValue) || alias2).call(alias1,(depth0 != null ? depth0.labelDisplayKey : depth0),{"name":"i18keyToValue","hash":{},"data":data}))
    + "\r\n                    </a>\r\n                </h4>\r\n            </div>\r\n            <div id=\"collapse"
    + alias4(((helper = (helper = helpers.index || (data && data.index)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"index","hash":{},"data":data}) : helper)))
    + "\" class=\"panel-collapse collapse in\" >\r\n                <ul class=\"list-group c-filter__list\">\r\n"
    + ((stack1 = helpers.each.call(alias1,(depth0 != null ? depth0.values : depth0),{"name":"each","hash":{},"fn":container.program(2, data, 0, blockParams, depths),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "                </ul>\r\n            </div>\r\n";
},"2":function(container,depth0,helpers,partials,data,blockParams,depths) {
    var stack1, helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3=container.escapeExpression;

  return "\r\n                    "
    + ((stack1 = (helpers.equalToFive || (depth0 && depth0.equalToFive) || alias2).call(alias1,(data && data.index),{"name":"equalToFive","hash":{},"data":data})) != null ? stack1 : "")
    + "\r\n\r\n                    <li class=\"list-group-item "
    + alias3((helpers.greaterThanFive || (depth0 && depth0.greaterThanFive) || alias2).call(alias1,(data && data.index),{"name":"greaterThanFive","hash":{},"data":data}))
    + "\">\r\n                        <div class=\"radio\">\r\n                            <label class=\"c-filter__label js-filter-radio\">\r\n                                <input type=\"radio\" name=\""
    + alias3(container.lambda((depths[1] != null ? depths[1].label : depths[1]), depth0))
    + "\" value=\""
    + alias3(((helper = (helper = helpers.value || (depth0 != null ? depth0.value : depth0)) != null ? helper : alias2),(typeof helper === "function" ? helper.call(alias1,{"name":"value","hash":{},"data":data}) : helper)))
    + "\" data-text=\""
    + alias3((helpers.i18keyToValue || (depth0 && depth0.i18keyToValue) || alias2).call(alias1,(depth0 != null ? depth0.displayKey : depth0),{"name":"i18keyToValue","hash":{},"data":data}))
    + "\">\r\n                                <span class=\"customRadio\"></span>\r\n                                    "
    + alias3((helpers.i18keyToValue || (depth0 && depth0.i18keyToValue) || alias2).call(alias1,(depth0 != null ? depth0.displayKey : depth0),{"name":"i18keyToValue","hash":{},"data":data}))
    + "\r\n                            </label>\r\n                        </div>\r\n                    </li>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data,blockParams,depths) {
    var stack1, alias1=depth0 != null ? depth0 : (container.nullContext || {});

  return "<div class=\"c-filter x-left-margins\">\r\n    <div class=\"hidden-xs col-sm-12 x-no-padding\">\r\n        <h3 class=\"c-filter__title\">"
    + container.escapeExpression((helpers.i18keyToValue || (depth0 && depth0.i18keyToValue) || helpers.helperMissing).call(alias1,"rdoe_search.searchFilter",{"name":"i18keyToValue","hash":{},"data":data}))
    + "</h3>\r\n        <div class=\"js-filter-facet-list\">\r\n"
    + ((stack1 = helpers.each.call(alias1,depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0, blockParams, depths),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        </div>\r\n    </div>\r\n</div>\r\n";
},"useData":true,"useDepths":true});

this["roche"]["templates"]["systemSpecification"] = Handlebars.template({"1":function(container,depth0,helpers,partials,data) {
    var helper, alias1=container.escapeExpression;

  return "<li class=\"clearfix c-pdp-spec-item js-pdp-spec-item\">\r\n	<p class=\"c-pdp-spec\">\r\n		<span class=\"c-pdp-spec-label\">"
    + alias1(((helper = (helper = helpers.key || (depth0 != null ? depth0.key : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"key","hash":{},"data":data}) : helper)))
    + "</span>\r\n		<span class=\"c-pdp-spec-line\"></span></p>\r\n	<p class=\"c-pdp-spec-desc\">\r\n	  <span>"
    + alias1(container.lambda((depth0 != null ? depth0.value : depth0), depth0))
    + "</span>\r\n	</p>\r\n</li>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "");
},"useData":true});
/*!
 * faq.js

 * This file contians  faq functions for add ellipisis.
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
   * This faq functions for add ellipisis.
   * Accepts any valid selector or jQuery object.
   * @param {selector, HTMLElement, or jQuery object} table
   */

  var faqMaxChar = function faqMaxChar() {

    var faqDesc = $('.c-faq__description');
    if (!snro.commonUtils.isMobile()) {
      faqDesc.each(function () {
        var self = $(this);
        if (self.text().trim().length > 400) {
          snro.commonUtils.addEllipses();
        }
      });
    }
  };

  var _cache = {
    faqInput: $('.js-faq-row').find('input')
  };

  snro.faq = {
    bindEvents: function bindEvents() {
      _cache.faqInput.on('click', function () {
        _cache.faqInput.not(this).prop('checked', false);
      });
    },
    init: function init() {
      this.bindEvents();
      faqMaxChar();
    }
  };
})(window, window.jQuery, window.snro);

/*
 * generalmap.js
 * [ This javascript code is used to show contact location on map on contact use page ]
 *
 * @project:    SN-RO
 * @date:       2017-09-18
 * @author:     Neha
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace generalmapComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  snro = window.snro = snro || {};
  var _cache = {
    'map': $('#generalContactMap')
  },
      google = window.google,
      lat = parseFloat(_cache.map.data('lat')),
      lng = parseFloat(_cache.map.data('lng')),
      iconPath = _cache.map.data('marker');

  var _initMap = function _initMap() {

    var mapOptions = {
      zoom: 12,
      center: new google.maps.LatLng(lat, lng),
      styles: [{
        'featureType': 'administrative',
        'elementType': 'labels.text.fill',
        'stylers': [{
          'color': '#444444'
        }]
      }, {
        'featureType': 'landscape',
        'elementType': 'all',
        'stylers': [{
          'color': '#f2f2f2'
        }]
      }, {
        'featureType': 'poi',
        'elementType': 'all',
        'stylers': [{
          'visibility': 'off'
        }]
      }, {
        'featureType': 'road',
        'elementType': 'all',
        'stylers': [{
          'visibility': 'on'

        }, {
          'lightness': 45
        }]
      }, {
        'featureType': 'road.highway',
        'elementType': 'all',
        'stylers': [{
          'visibility': 'simplified'
        }]
      }, {
        'featureType': 'road.arterial',
        'elementType': 'labels.icon',
        'stylers': [{
          'visibility': 'off'
        }]
      }, {
        'featureType': 'transit',
        'elementType': 'all',
        'stylers': [{
          'visibility': 'off'
        }]
      }, {
        'featureType': 'water',
        'elementType': 'all',
        'stylers': [{
          'color': '#46bcec'
        }, {
          'visibility': 'on'
        }]
      }]
    },
        mapElement = _cache.map[0],
        map = new google.maps.Map(mapElement, mapOptions),
        marker = new google.maps.Marker({
      position: new google.maps.LatLng(lat, lng),
      map: map,
      icon: iconPath
    });

    //redirect to google map direction on clicking marker  
    marker.addListener('click', function () {
      var url = 'https://www.google.com/maps/dir/?api=1&destination=' + lat + ',' + lng + '';
      window.open(url);
    });
  };
  // public methods


  /**
   * @method init
   * @description this method is used to initialize public methods.
   * @memberof snro.generalMap
   * @example
   * snro.generalMap.init()
   */
  snro.generalMap = {
    init: function init() {
      _initMap();
    }
  };
})(window, jQuery, window.snro);

/*
 * c_popupcomponent.js
 * [ This javascript code is used to display popupcomponent component. ]
 *
 * @project:    SN-RO
 * @date:       2017-09-24
 * @author:     Bindhyachal
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace popupComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {
  snro = window.snro = snro || {};
  var domElements = {
    $modalPopUp: $('#myModalPopUp'),
    $modalBackdrop: $('.js-modal-backdrop')
  };

  snro.modalPopComp = {
    moduleName: 'modalPopComp',
    personaCookieVal: snro.commonUtils.getCookie('persona-type'),
    personaMetaTagArr: $('meta[name="personaTag"]').attr('content'),
    personaMetaTag: $('meta[name="personaTag"]').attr('content') ? $('meta[name="personaTag"]').attr('content').split(',')[0] : '',
    currentChannel: $('input[name=currentChannel]').val(),
    redirectURL: $('#redirectURL').val(),
    pageType: $('meta[name="page-type"]').length ? $('meta[name="page-type"]').attr('content') : 'otherPage',
    isPersonaModalPopUp: false,
    dLinkWithNoMatchPersona: false,

    //initialize the modal
    initializeModal: function initializeModal() {
      var metaTagPersonaLevel = this.personaMetaTag,
          currentChannelI = this.currentChannel,
          modalBodyText = '',
          deepLinkPage = this.currentChannel !== snro.commonUtils.getCookie('homePagePath');
      if (this.pageType.toLocaleLowerCase() !== 'home') {
        if (!snro.commonUtils.getCookie('homePagePath') || deepLinkPage) {
          modalBodyText = deepLinkPage && snro.commonUtils.getCookie('homePagePath') ? window.Granite.I18n.get('rdoe_deeplinkpopupexists.' + currentChannelI) : window.Granite.I18n.get('rdoe_deeplinkpopup.' + currentChannelI);
          /*if(deepLinkPage && snro.commonUtils.getCookie('homePagePath')) {
            modalBodyText = window.Granite.I18n.get('rdoe_deeplinkpopupexists.' + currentChannelI);
          }*/
          this.updateTemplate(modalBodyText, false);
        } else if (metaTagPersonaLevel && this.personaMetaTagArr && this.personaMetaTagArr.indexOf(this.personaCookieVal) === -1) {
          modalBodyText = window.Granite.I18n.get('rdoe_deeplinkpopup.' + metaTagPersonaLevel);
          this.dLinkWithNoMatchPersona = true;
          this.updateTemplate(modalBodyText, false);
        }
        if (snro.commonUtils.getCookie('persona-type') !== '') {
          snro.personaPickerComp._checkCookie();
        }
        snro.countrySelectorCmp.init('deeplink');
      } else {
        snro.countrySelectorCmp.init();
      }
    },


    //update the template with data
    updateTemplate: function updateTemplate(modalBodyText, bool) {
      this.isPersonaModalPopUp = bool;
      domElements.$modalPopUp.find('.j-modal-title').text(window.Granite.I18n.get('rdoe_deeplinkpopup.title'));
      domElements.$modalPopUp.find('.j-modal-body-text').text(modalBodyText);
      domElements.$modalPopUp.find('.js-btn-secondary').text(window.Granite.I18n.get('rdoe_deeplinkpopup.agree'));
      domElements.$modalPopUp.find('.js-btn-primary').text(window.Granite.I18n.get('rdoe_deeplinkpopup.disagree'));
      domElements.$modalPopUp.addClass('show');
      domElements.$modalBackdrop.addClass('show');
    },

    // bind dom events
    bindEvents: function bindEvents() {
      var context = this,
          languageLocationCookie = context.currentChannel;
      $('body').on('click', '.j-close', function () {
        domElements.$modalPopUp.removeClass('show');
        domElements.$modalBackdrop.removeClass('show');
      });
      $('body').on('click', '.js-btn-secondary', function () {
        domElements.$modalPopUp.removeClass('show');
        domElements.$modalBackdrop.removeClass('show');
        if (context.dLinkWithNoMatchPersona) {
          snro.commonUtils.setCookie('persona-type', context.personaMetaTag, Infinity);
          snro.personaPickerComp._checkCookie();
          return;
        }
        if (!context.isPersonaModalPopUp) {
          snro.commonUtils.removeCookie('persona-type');
          snro.commonUtils.setCookie('homePagePath', context.currentChannel, Infinity);
          snro.commonUtils.setCookie('location-and-language', languageLocationCookie.replace('/', '|'), Infinity);
          snro.personaPickerComp.init();
        } else {
          domElements.$modalPopUp.removeClass('show');
          $('.js-c-persona-picker').slideUp('fast', function () {
            snro.personaPickerComp._setCookie(context.personaMetaTag);
            snro.personaPickerComp._checkCookie();
            var liipboxOb = document.getElementsByClassName('js-liipbox');
            liipboxOb[0].parentNode.removeChild(liipboxOb[0]);
            $('body').removeClass('x-no-scroll');
          });
        }
      });
      $('body').on('click', '.js-btn-primary', function () {
        if (!$('#persona-picker').length) {
          if (!snro.commonUtils.getCookie('homePagePath')) {
            snro.countrySelectorCmp.init();
          } else {
            snro.commonUtils.setCookie('homePagePath', context.currentChannel, Infinity);
            snro.commonUtils.setCookie('location-and-language', languageLocationCookie.replace('/', '|'), Infinity);
            context.redirectPage();
          }
        } else {
          snro.personaPickerComp._setCookie(snro.personaPickerComp.cookieValue);
          snro.personaPickerComp._checkCookie();
          context.redirectPage();
        }
        domElements.$modalPopUp.removeClass('show');
        domElements.$modalBackdrop.removeClass('show');
      });
    },

    // redirecting to the home page
    redirectPage: function redirectPage() {
      window.location.href = $('.c-header__link--logo').attr('href');
    },
    init: function init() {
      this.initializeModal();
      this.bindEvents();
      if (snro.commonUtils.getCookie('persona-type') !== '' && typeof snro.commonUtils.getCookie('persona-type') !== 'undefined') {
        snro.personaPickerComp._checkCookie();
      }
    }
  };
})(window, window.jQuery, window.snro);

/*
 * socialshare.es
 * [ Behavior for social share icons using sharethis ]
 *
 * @project:    SN-RO
 * @date:       2017-09-27
 * @author:     Shashank
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';
/**
 * @namespace video
 * @memberof roche
 *
 */

(function (window, $, snro) {
  snro = window.snro || {};

  // defining module
  snro.socialshare = {
    moduleName: 'socialshare',

    loadSTApi: function loadSTApi() {
      var apiKey = $('.js-st-container').data('shareid') || '59d313920b76a500114be43f';
      var script = document.createElement('script');
      script.type = 'text/javascript';
      script.src = '//platform-api.sharethis.com/js/sharethis.js#property=' + apiKey + '&product=inline-share-buttons';
      script.async = true;
      document.getElementsByTagName('head')[0].appendChild(script);

      script.onload = script.onreadystatechange = function () {
        var socialIconArr = $('.js-social-btn-st').data('visible-icon').split(',');
        var intervalTimer = setTimeout(function () {
          $('.js-social-btn-st').find('[data-network]').each(function () {
            if (socialIconArr.indexOf($(this).data('network')) !== -1) {
              $('.js-st-container').addClass($(this).data('network'));
            }
          });
          clearTimeout(intervalTimer);
        }, 1000);
      };
    },
    bindAnalyticsEvents: function bindAnalyticsEvents() {
      var pageName = $('[title]').text() ? $('title').text() : 'home';
      $('body').on('click', '[data-network]', function () {
        var socialType = $(this).data('network');
        window.digitalData = {
          link: {
            linkPageName: pageName,
            linkCategory: 'external',
            linkSection: 'social',
            linkHeader: 'social',
            linkText: socialType,
            linkInteractionMethod: 'click-on-' + snro.analytics.getBrowserWidth(),
            social: socialType,
            event: 'social-share'
          }
        };
        window._satellite.track('generic-link-tracking');
      });
    },


    // Module initialization
    init: function init() {
      this.loadSTApi();
      this.bindAnalyticsEvents();
      // window.sharethis.addEventListener('sharethis.ready', function() {
      //   snro.commonUtils.log('sharethis api is ready');
      // });

      // let socialIconArr = $('.js-social-btn-st').data('visible-icon').split(',');
      // $('.js-social-btn-st').find('[data-network]').each(function() {
      //   if(socialIconArr.indexOf($(this).data('network')) !== -1) {
      //     $('.js-st-container').addClass($(this).data('network'));
      //   }
      // });
    }
  };
})(window, window.jQuery, window.snro);

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
