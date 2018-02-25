/*! jCrafted - v0.1.0 - 2015-11-13
* https://github.com/craftedtm/jcrafted
* Copyright (c) 2015 Crafted.; Licensed MIT */
(function() {
  this.jCraftedData = (function() {
    function jCraftedData(options) {
      this.options = options;
      this.request = null;
    }

    jCraftedData.prototype.randomString = function(len, charSet) {
      var i, randomPoz, randomString;
      charSet = charSet || "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
      randomString = "";
      i = 0;
      while (i < len) {
        randomPoz = Math.floor(Math.random() * charSet.length);
        randomString += charSet.substring(randomPoz, randomPoz + 1);
        i++;
      }
      return randomString;
    };

    jCraftedData.prototype.loadItems = function(apiOptions, successCallback, errorCallback) {
      var _this = this;
      if (this.request) {
        this.request.abort();
      }
      return this.request = jQuery[this.options.dataType === 'jsonp' ? 'jsonp' : 'ajax']({
        callbackParameter: 'callback',
        url: this.options.url,
        dataType: this.options.dataType,
        callback: this.randomString(24),
        data: apiOptions,
        success: function(json) {
          _this.request = null;
          return successCallback(json);
        },
        error: function(e) {
          _this.request = null;
          return errorCallback(e);
        }
      });
    };

    return jCraftedData;

  })();

}).call(this);

(function() {
  this.jCraftedView = (function() {
    function jCraftedView($container, options) {
      this.$container = $container;
      this.options = options;
      this.$itemsBuffer = jQuery();
      this.$window = jQuery(window);
      this.frameIsSet = false;
      this.setCss(options.layout);
      this.$noItems = $('<div class="jc-no-items-card">' + this.options.i18n.noItemsCard + '</div>');
      this.reorderItemsResizeEvent = jQuery.throttle(200, jQuery.proxy(this.reorderItems, this));
      this.presentationMode = options.presentation.presentationMode;
      if (this.presentationMode) {
        this.setPresentationCss();
      }
      this.slabOptions = options.slab;
      this.ctaCount = 0;
      this.ctaShown = false;
      this.pause = false;
      this.presenting = false;
      this.loopCount = 1;
    }

    jCraftedView.prototype.buffer = function(post) {
      var $item;
      $item = jQuery(jQuery.trim(new EJS({
        url: post.template_dir
      }).render(post)));
      if (this.options.layout.bindData) {
        $item.data('post', post);
      }
      return this.$itemsBuffer.push($item.get(0));
    };

    jCraftedView.prototype.render = function(callback) {
      var $items, loadedImagesIstance, notLoadedImagesTimer, onRenderReady, that,
        _this = this;
      $items = this.$itemsBuffer;
      this.$container.append($items);
      notLoadedImagesTimer = null;
      onRenderReady = function() {
        window.clearTimeout(notLoadedImagesTimer);
        callback($items);
        return _this.updateFrame($items);
      };
      that = this;
      loadedImagesIstance = imagesLoaded(this.$container);
      notLoadedImagesTimer = window.setTimeout(function() {
        loadedImagesIstance.off('always', onRenderReady);
        onRenderReady();
        return loadedImagesIstance.on('always', function() {
          return that.reorderItems();
        });
      }, 1500);
      loadedImagesIstance.on('always', onRenderReady);
      this.$container.find('img').on('error', function() {
        $(this).parent('.jc-preview').hide();
        return that.reorderItems();
      });
      if (!this.options.presentation.presentationMode) {
        this.bindButtons($items);
      }
      this.$itemsBuffer = jQuery();
      this.pause = false;
      if (this.presentationMode && !this.presenting) {
        return that.present(this.options.presentation, $items);
      }
    };

    jCraftedView.prototype.reorderItems = function() {
      if (!this.options.layout.disableMasonry && this.msnry) {
        return this.msnry.layout();
      }
    };

    jCraftedView.prototype.setFrame = function() {
      if (!this.options.layout.disableMasonry) {
        this.createMasonry();
        if (this.options.layout.isResizeBound) {
          this.$window.on('resize', this.reorderItemsResizeEvent);
        }
        this.reorderItems();
      }
      this.frameIsSet = true;
    };

    jCraftedView.prototype.updateFrame = function($items) {
      if (!this.options.layout.disableMasonry) {
        if (!this.frameIsSet) {
          this.setFrame();
        } else {
          this.msnry.appended($items);
        }
      }
      if (!this.options.presentation.presentationMode) {
        $items.addClass('jc-visible');
      }
    };

    jCraftedView.prototype.renderEmptyCard = function() {
      return this.$container.append(this.$noItems);
    };

    jCraftedView.prototype.resetFrame = function(callback) {
      var _this = this;
      this.pause = true;
      this.loopCount = 0;
      $('.jc-cta').addClass('jc-pause');
      this.$itemsBuffer = jQuery();
      this.$container.addClass('jc-invisible');
      return window.setTimeout(function() {
        _this.$noItems.remove();
        _this.$window.off('resize', _this.reorderItemsResizeEvent);
        _this.$window.scrollTop(0);
        _this.$container.removeClass('jc-invisible').empty();
        _this.frameIsSet = false;
        callback();
      }, this.options.template.fadeOutTransitionDuration + 70);
    };

    jCraftedView.prototype.removeItem = function($item) {
      if (this.msnry) {
        this.msnry.remove($item.get(0));
        this.msnry.layout();
      }
      return $item.remove();
    };

    jCraftedView.prototype.createMasonry = function() {
      if (!this.options.layout.disableMasonry) {
        if (this.msnry) {
          this.msnry.destroy();
        }
        return this.msnry = new Masonry(this.$container.get(0), {
          transitionDuration: 150,
          hiddenStyle: {},
          visibleStyle: {},
          isOriginLeft: this.options.layout.right !== true,
          itemSelector: this.options.template.itemSelector,
          isResizeBound: false,
          gutter: this.options.layout.itemGutter || this.options.layout.gutter || this.options.layout.defaultGutter
        });
      }
    };

    jCraftedView.prototype.buildShareContent = function(dataset) {
      var data, limit;
      limit = 120 - location.href.length;
      if (dataset.image) {
        limit -= 20;
      }
      data = dataset.url;
      if (dataset.title) {
        data += "&text=" + encodeURIComponent(dataset.title) + " " + dataset.image;
      } else if (dataset.description) {
        data += "&text=" + encodeURIComponent(jQuery.truncate(dataset.description, limit) + " " + dataset.image);
      }
      return data += "&picture=" + dataset.image;
    };

    jCraftedView.prototype.bindButtons = function($items) {
      this.bindButton($items.find(".jc-twitter-link"), "http://twitter.com/share?url=", "twitter");
      this.bindButton($items.find(".jc-gplus-link"), "https://plus.google.com/share?url=", "gplus");
      return this.bindFbButton($items.find(".jc-fb-link"));
    };

    jCraftedView.prototype.bindButton = function($buttons, host, title) {
      var buildShareContent;
      buildShareContent = this.buildShareContent;
      $buttons.click(function(e) {
        var height, opts, url, width;
        e.preventDefault();
        width = 575;
        height = 400;
        url = host + buildShareContent(this.dataset);
        opts = "status=1" + ",width=" + width + ",height=" + height;
        return window.open(url, title, opts);
      });
    };

    jCraftedView.prototype.bindFbButton = function($buttons) {
      $buttons.click(function(e) {
        return FB.ui({
          method: "share",
          href: decodeURIComponent(this.dataset.url)
        }, function(response) {});
      });
    };

    jCraftedView.prototype.setCss = function(l) {
      var boxGutter, calcItemWidth, itemGutter, rules, s, stylesheet;
      this.$container.addClass('jcrafted').addClass('jc-overwrite').toggleClass('stream-boxed', l.boxed).toggleClass('stream-center', l.center).toggleClass('stream-right', l.right).toggleClass('stream-left', l.left);
      s = ".jcrafted.jc-overwrite";
      rules = [];
      itemGutter = l.itemGutter || l.gutter || l.defaultGutter;
      boxGutter = l.boxGutter || l.gutter || l.defaultGutter;
      stylesheet = document.styleSheets[0];
      rules.push(s + " {margin-top: " + itemGutter + "px; padding-left: " + itemGutter + "px; padding-right: " + itemGutter + "px}");
      rules.push(s + " .jc-item {margin-bottom: " + boxGutter + "px}");
      calcItemWidth = function(size, columns) {
        return ((size * columns) + (itemGutter * (columns - 1))) + 2;
      };
      if (l.boxed) {
        jQuery.each(l.itemWidth, function(i, size) {
          var ruleContainer, ruleItem, windowWidth;
          if (l.itemColumns[i] === 1) {
            rules.push(s + " {padding-right: 0; padding-left: 0}");
          }
          ruleContainer = s + ".stream-boxed {width: " + (calcItemWidth(size, l.itemColumns[i])) + "px}";
          ruleItem = s + " .jc-item {width: " + size + "px}";
          if (l.itemWidth[i - 1] && l.itemColumns[i - 1]) {
            windowWidth = calcItemWidth(l.itemWidth[i - 1], l.itemColumns[i - 1]) + boxGutter * 2;
            ruleContainer = "@media (max-width: " + windowWidth + "px) { " + ruleContainer + " }";
            ruleItem = "@media (max-width: " + windowWidth + "px) { " + ruleItem + " }";
          }
          rules.push(ruleContainer);
          return rules.push(ruleItem);
        });
      } else {
        rules.push(s + " .jc-item {width: " + l.itemWidth[0] + "px}");
      }
      if (stylesheet.insertRule) {
        return jQuery.each(rules, function(i, rule) {
          return stylesheet.insertRule(rule, stylesheet.cssRules.length);
        });
      } else {
        stylesheet.addRule(".jc-item", "{width: " + l.itemWidth[0] + "px}", 0);
        return stylesheet.addRule(s + ".stream-boxed", "{width: " + (calcItemWidth(l.itemWidth[0], l.itemColumns[0])) + "px}", 0);
      }
    };

    jCraftedView.prototype.setPresentationCss = function() {
      return this.$container.addClass('presentation');
    };

    jCraftedView.prototype.present = function(p, $items) {
      var $item, templateRoot;
      this.presenting = true;
      templateRoot = this.options.template.path + this.options.template.skin;
      $item = $($items[0]);
      $item.addClass('jc-visible');
      $item.find('.jc-plain').css({
        'font-size': this.calculateFontSize(p, $item) + 'px'
      });
      setTimeout((function() {
        if ($item.hasClass('jc-item-photo-full')) {
          return $item.addClass('blurred');
        }
      }), p.blurTime * 1000);
      return setInterval((function() {
        var $cta;
        if (!this.pause) {
          $items = $('.jc-item');
          $items.removeClass('jc-visible');
          $cta = $('.jc-cta');
          $cta.removeClass('jc-visible');
          $cta.removeClass('jc-pause');
          setTimeout((function() {
            return $items.removeClass('blurred');
          }), 1000);
          if (p.cta && !this.ctaShown) {
            if (this.loopCount % p.ctaEach === 0 && this.loopCount !== 0) {
              $cta = $('.jc-cta');
              $cta.addClass('jc-visible');
              $cta.css({
                'background-image': "url('" + templateRoot + "/images/cta/" + this.ctaCount + ".png')"
              });
              this.ctaShown = true;
              if (p.ctaCount - 1 > this.ctaCount) {
                this.ctaCount++;
              } else {
                this.ctaCount = 0;
              }
              return;
            }
          } else {
            this.ctaShown = false;
          }
          $item = $($items[this.loopCount]);
          $item.find('.jc-plain').css({
            'font-size': this.calculateFontSize(p, $item) + 'px'
          });
          $item.addClass('jc-visible');
          setTimeout((function() {
            if ($item.hasClass('jc-item-photo-full')) {
              return $item.addClass('blurred');
            }
          }), p.blurTime * 1000);
          if ($items.length - 1 > this.loopCount && this.loopCount < p.loopMax - 1) {
            return this.loopCount++;
          } else {
            return this.loopCount = 0;
          }
        }
      }).bind(this), p.loopTime * 1000 + p.blurTime * 1000);
    };

    jCraftedView.prototype.calculateFontSize = function(p, $item) {
      var currentSize, itemChars, newSize;
      if ($item.find('.jc-plain')) {
        itemChars = $item.find('.jc-plain').text().length;
        newSize = currentSize = parseInt($item.find('.jc-plain').css('font-size'));
        if (itemChars < p.minChars) {
          newSize = currentSize * p.minChars / itemChars;
          if (newSize > p.maxFont) {
            return p.maxFont;
          }
        } else if (itemChars > p.maxChars) {
          newSize = currentSize * p.maxChars / itemChars;
          if (newSize < p.minFont) {
            return p.minFont;
          }
        }
      }
      return newSize.toFixed(2);
    };

    jCraftedView.prototype.clear = function() {
      return this.$container.empty().removeClass('jcrafted').removeClass('jc-overwrite').removeClass('stream-boxed').removeClass('stream-center').removeClass('stream-right').removeClass('stream-left');
    };

    return jCraftedView;

  })();

}).call(this);

(function() {
  this.jCraftedViewHelper = function(options, post) {
    var templateRoot;
    templateRoot = options.path + options.skin;
    return {
      hide_share_buttons: !!options.hideShareButtons,
      published_at_ago: jQuery.timeago(post.published_at),
      main_stats: (function() {
        switch (post.source.network) {
          case 'flickr':
            return '';
          case 'twitter':
            return post.retweets + ' ' + options.i18n.retweets;
          case 'youtube':
            return post.views + ' ' + options.i18n.views;
          default:
            return post.likes + ' ' + options.i18n.likes;
        }
      })(),
      share_title: options.i18n.share,
      social_url: post.url,
      short_description: jQuery.truncateWords(post.description, options.maxCharacters),
      slabbed_description: (function() {
        var description, i, rnd, rnd_slab, rnd_slabs, slabbed_description, slabs, _i, _len;
        if (description) {
          description = post.description;
          rnd_slabs = [];
          slabs = description.split(' ');
          slabbed_description = '';
          i = 0;
          while (i < slabs.length) {
            rnd = Math.floor(Math.random() * 6) + 4;
            rnd_slabs = rnd_slabs.concat(slabs.slice(i, i + rnd).join(' '));
            i += rnd;
          }
          for (_i = 0, _len = rnd_slabs.length; _i < _len; _i++) {
            rnd_slab = rnd_slabs[_i];
            slabbed_description += "<span class='slabtext'>" + rnd_slab + "</span>";
          }
          return slabbed_description;
        }
      })(),
      template_dir: (function() {
        var parts, templateName;
        templateName = post.source.template;
        parts = templateName.split('::');
        parts.shift();
        parts = jQuery.map(parts.slice(), function(part) {
          return part.toLowerCase();
        });
        if (parts.length === 1) {
          parts.push('plain');
        }
        return templateRoot + '/' + parts.join('/');
      })(),
      template_root: templateRoot,
      thumbnail: (function() {
        if (post.attachment.length && post.attachment[0].thumbnail) {
          if (post.source.network === 'youtube' && post.images && post.images.length > 0 && post.images[0].length > 0) {
            return post.images[0][post.images[0].length - 1].src;
          }
          return post.attachment[0].thumbnail.url;
        }
        return "";
      })(),
      main_image: (function() {
        if (post.attachment.length && post.attachment[0].content) {
          if (post.source.network === 'youtube' && post.images && post.images.length > 0 && post.images[0].length > 2) {
            return post.images[0][2].src;
          }
          return post.attachment[0].content.url;
        }
        return "";
      })(),
      author_handle: (function() {
        if (post.source.network === 'twitter') {
          return "@" + /[^/]*$/.exec(post.author.url)[0];
        } else if (post.source.network === 'instagram') {
          return "@" + /[^/]*$/.exec(post.author.url)[0];
        } else if (post.source.network === 'facebook') {
          return "Facebook";
        } else {
          return '';
        }
      })(),
      wide_image: (function() {
        var height, width;
        if (post.attachment.length && post.attachment[0].content) {
          width = post.attachment[0].content.width;
          height = post.attachment[0].content.height;
          if (width >= height) {
            return true;
          }
          return false;
        }
      })()
    };
  };

}).call(this);

(function() {
  this.jCraftedController = (function() {
    function jCraftedController($container, pluginOptions) {
      var hashOptions;
      this.$container = $container;
      this.options = jQuery.extend(true, {
        api: {
          filterViaLocationHash: true,
          dataType: 'jsonp',
          params: {
            page: 1,
            limit: 16
          }
        },
        template: {
          path: '/jcrafted/templates/',
          skin: 'default',
          fadeOutTransitionDuration: 200,
          maxCharacters: 200,
          itemSelector: '.jc-item-object',
          hideShareButtons: false
        },
        layout: {
          gutter: undefined,
          itemGutter: undefined,
          boxGutter: undefined,
          defaultGutter: 20,
          itemColumns: [3, 3, 2, 1],
          itemWidth: [384, 304, 284, 224],
          boxed: true,
          center: true,
          right: false,
          left: false,
          disableMasonry: false,
          isResizeBound: true,
          bindData: false
        },
        presentation: {
          presentationMode: false,
          loopMax: 20,
          loopTime: 10,
          reloadTime: 600,
          blurTime: 5,
          cta: false,
          ctaCount: 0,
          ctaEach: 5,
          minChars: 75,
          maxChars: 100,
          minFont: 60,
          maxFont: 110
        },
        slab: {
          maxFontSize: 100
        },
        i18n: {
          noItemsCard: 'No post were found',
          share: 'Share',
          retweets: 'retweets',
          views: 'views',
          likes: 'likes',
          time: {
            minute: 'a minute',
            hour: 'an hour',
            hours: '%d hours',
            month: 'a month',
            year: 'a year'
          }
        }
      }, pluginOptions);
      this.apiParams = this.options.api.params;
      jQuery.each(this.options.i18n.time, function(key, value) {
        return jQuery.timeago.settings.strings[key] = value;
      });
      this.$window = jQuery(window);
      this.view = new jCraftedView(this.$container, {
        template: this.options.template,
        layout: this.options.layout,
        presentation: this.options.presentation,
        slab: this.options.slab,
        i18n: this.options.i18n
      });
      this.data = new jCraftedData({
        url: this.options.api.url,
        dataType: this.options.api.dataType
      });
      this.checkForLoadAtBottomThrottled = jQuery.throttle(500, jQuery.proxy(this.checkForLoadAtBottom, this));
      this.hashChangedProxy = jQuery.proxy(this.hashChanged, this);
      if (this.options.api.filterViaLocationHash) {
        hashOptions = this.getFilters(location.hash);
        if (hashOptions) {
          this.setApiOptions(hashOptions);
        }
        this.$window.on('hashchange', this.hashChangedProxy);
      }
    }

    jCraftedController.prototype.setScrollEvents = function() {
      return this.$window.on('scroll', this.checkForLoadAtBottomThrottled).on('mousewheel', this.checkForLoadAtBottomThrottled);
    };

    jCraftedController.prototype.removeScrollEvents = function() {
      return this.$window.off('scroll', this.checkForLoadAtBottomThrottled).off('mousewheel', this.checkForLoadAtBottomThrottled);
    };

    jCraftedController.prototype.getFilters = function(url) {
      var a, i, pair, params;
      if (!url.match('#filter')) {
        return;
      }
      pair = void 0;
      params = {};
      url = url.substring(url.indexOf('?') + 1).split('&');
      i = url.length - 1;
      while (i >= 0) {
        pair = url[i].split("=");
        a = pair[1];
        params[decodeURIComponent(pair[0])] = a ? decodeURIComponent(a) : void 0;
        i--;
      }
      return params;
    };

    jCraftedController.prototype.setApiOptions = function(apiOptions) {
      var _this = this;
      if (apiOptions) {
        jQuery.extend(this.apiParams, apiOptions);
      }
      return jQuery.each(this.apiParams, function(key, value) {
        if (!(value || value === false)) {
          return delete _this.apiParams[key];
        }
      });
    };

    jCraftedController.prototype.addToViewBuffer = function(i, post) {
      var viewData, viewHelper;
      viewHelper = jCraftedViewHelper(jQuery.extend({}, this.options.template, {
        i18n: this.options.i18n
      }), post);
      viewData = jQuery.extend({}, post, viewHelper);
      return this.view.buffer(viewData);
    };

    jCraftedController.prototype.loadAndDisplayItems = function(showEmptyCard) {
      var _this = this;
      if (showEmptyCard == null) {
        showEmptyCard = true;
      }
      this.removeScrollEvents();
      this.setApiOptions();
      this.$container.trigger('startLoadItems');
      return this.data.loadItems(this.apiParams, (function(itemsData) {
        jQuery.each(itemsData, jQuery.proxy(_this.addToViewBuffer, _this));
        _this.view.render(function($items) {
          var hashOptions, presentationOptions;
          presentationOptions = _this.options.presentation;
          if (presentationOptions.presentationMode) {
            hashOptions = _this.getFilters(location.hash);
            setTimeout((function() {
              return this.reset(true, hashOptions);
            }).bind(_this), presentationOptions.reloadTime * 1000);
          } else {
            _this.setScrollEvents();
          }
          return _this.$container.trigger('itemsAdded', {
            items: $items
          });
        });
        return _this.$container.trigger('endLoadItems');
      }), (function(e) {
        if (showEmptyCard) {
          _this.view.renderEmptyCard();
        }
        return _this.$container.trigger('endLoadItems');
      }));
    };

    jCraftedController.prototype.checkForLoadAtBottom = function() {
      var containerBottom, windowBottom;
      windowBottom = window.innerHeight + window.scrollY;
      containerBottom = this.$container.height() + this.$container.position().top - 250;
      if (windowBottom >= containerBottom) {
        this.loadMoreItems();
      }
    };

    jCraftedController.prototype.reset = function(showNewItems, apiOptions) {
      var _this = this;
      if (showNewItems == null) {
        showNewItems = false;
      }
      if (apiOptions == null) {
        apiOptions = {};
      }
      return this.view.resetFrame(function() {
        _this.apiParams.page = 1;
        _this.setApiOptions(apiOptions);
        if (showNewItems) {
          return _this.loadAndDisplayItems();
        }
      });
    };

    jCraftedController.prototype.hashChanged = function() {
      var hashOptions;
      hashOptions = this.getFilters(location.hash);
      if (hashOptions) {
        return this.reset(true, hashOptions);
      }
    };

    jCraftedController.prototype.clear = function() {
      this.removeScrollEvents();
      this.$window.off('hashchange', this.hashChangedProxy);
      return this.view.clear();
    };

    jCraftedController.prototype.destroyItems = function() {
      return this.view.resetFrame(function() {
        return this.clear();
      });
    };

    jCraftedController.prototype.removeItem = function($item) {
      return this.view.removeItem($item);
    };

    jCraftedController.prototype.loadMoreItems = function(pageNr) {
      if (pageNr) {
        this.apiParams.page = pageNr;
      } else {
        this.apiParams.page += 1;
      }
      return this.loadAndDisplayItems(false);
    };

    return jCraftedController;

  })();

}).call(this);

(function() {
  (function($) {
    $.toUpperFirst = function(string) {
      if (!string) {
        return '';
      }
      return string.charAt(0).toUpperCase() + string.slice(1);
    };
    $.truncate = function(string, length) {
      var trimmedString;
      if (!(string || length)) {
        return '';
      }
      trimmedString = string.substr(0, length);
      return trimmedString.substr(0, Math.min(trimmedString.length, trimmedString.lastIndexOf(" ")));
    };
    $.truncateWords = function(string, length) {
      var trimmedString;
      if (length == null) {
        length = 200;
      }
      if (!string) {
        return '';
      }
      if (!(length < string.length)) {
        return string;
      }
      trimmedString = string.substr(0, length);
      return trimmedString.substr(0, Math.min(trimmedString.length, trimmedString.lastIndexOf(" "))) + '…';
    };
    $.parseLinks = function(string) {
      if (!string) {
        return '';
      }
      return string.replace(/[-a-zA-Z0-9@:%_\+.~#?&//=]{2,256}\.[a-z]{2,4}\b(\/[-a-zA-Z0-9@:%_\+.~#?&//=]*)?/gi, "<a href='$&'>$&</a>").replace(/[#]+[A-Za-zäöüÄÖÜéàèÉÈ0-9-_ß]+/g, "<span class='jc-hashtag'>$&</span>").replace(/[@]+[A-Za-zäöüÄÖÜéàèÉÈ0-9-_ß]+/g, "<span class='jc-username'>$&</span>");
    };
    return $.fn.extend({
      jCrafted: function(pluginOptions) {
        if (this.length !== 1) {
          throw new Error('jCrafted Error: Provide only one element');
        }
        return this.each(function() {
          var $container, controller;
          $container = $(this);
          controller = new jCraftedController($container, pluginOptions);
          $container.data('jCrafted', controller);
          if (pluginOptions.initialItemLoad !== false) {
            controller.loadAndDisplayItems();
          }
        });
      }
    });
  })(jQuery);

}).call(this);
