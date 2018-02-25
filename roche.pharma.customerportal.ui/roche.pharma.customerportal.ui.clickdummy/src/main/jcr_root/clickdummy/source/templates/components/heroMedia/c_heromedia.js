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
