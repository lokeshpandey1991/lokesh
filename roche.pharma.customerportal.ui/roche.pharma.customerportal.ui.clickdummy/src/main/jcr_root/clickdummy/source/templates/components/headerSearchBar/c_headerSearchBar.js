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
