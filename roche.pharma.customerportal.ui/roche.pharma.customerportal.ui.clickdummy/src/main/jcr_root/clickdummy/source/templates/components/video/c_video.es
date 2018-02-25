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
  let _cache = {},
    videoTracking;
  /**
   *
   * @param  $jPlayerCnt Player's container DIV to initialize against
   * @param videoPath Path of video to initialize with
   */
  function _initjPlayer($jPlayerCnt, videoPath,imagePath){
    $jPlayerCnt.find('.jp-jplayer').jPlayer({
      ready: function() {
        $(this).jPlayer('setMedia', {
          m4v:    videoPath,
          ogv:    videoPath,
          webmv:  videoPath,
          wav:    videoPath,
          poster: imagePath
        });
        $(this).find('video')
               .css('width', '100%')
               .css('height', 'auto');
        $(this).find('img')
                .css('position', 'absolute')
                .css('opacity','1');
        if(window.Granite) {
          $(this).find('img').attr('alt',window.Granite.I18n.get('rdoe_video.altText'));
        }
        else{
          $(this).find('img').attr('alt','posterImage');
        }
      },
      ended: function() {
        $(this).jPlayer('setMedia', {
          m4v:   videoPath,
          ogv:    videoPath,
          webmv:  videoPath,
          wav:    videoPath,
          poster: imagePath
        });
        $(this).jPlayer('playHead', 0);
        if($jPlayerCnt.find('.jp-audio').hasClass('jp-state-full-screen')){
          $jPlayerCnt.find('.jp-interface').css('visibility', 'visible');
        }
        else {
          $jPlayerCnt.find('.jp-interface').css('visibility', 'hidden');
        }
        $jPlayerCnt.find('.layer-opaque').css('visibility', 'visible');
        $jPlayerCnt.find('.video-info').css('display', 'block');
        $jPlayerCnt.find('img')
                .css('position', 'absolute')
                .css('display', 'inline');
      },
      play: function(){
        $(this).jPlayer('pauseOthers');
      },
      pause: function(){
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

    $jPlayerCnt.bind($.jPlayer.event.seeked, function(event) {
      let seekedPercent = parseInt(event.jPlayer.status.currentPercentAbsolute);
      if(seekedPercent === 100) {
        $.extend(window.digitalData['link'],{event:'video-completes'});
        window._satellite.track('video-tracking');
        videoTracking['video-100%'] = 'fired';
      } else if(seekedPercent >= 75) {
        $.extend(window.digitalData['link'],{event:'video-75%complete'});
        window._satellite.track('video-tracking');
        videoTracking['video-75%'] = 'fired';
      } else if(seekedPercent >=50 && seekedPercent < 75) {
        $.extend(window.digitalData['link'],{event:'video-50%complete'});
        window._satellite.track('video-tracking');
        videoTracking['video-50%'] = 'fired';
      } else if(seekedPercent >=25 && seekedPercent < 50) {
        $.extend(window.digitalData['link'],{event:'video-25%complete'});
        window._satellite.track('video-tracking');
        videoTracking['video-25%'] = 'fired';
      } else {
        // do nothing
      }
    });

    $jPlayerCnt.find('.jp-jplayer').bind($.jPlayer.event.timeupdate, function(event) {
     //as above, grabbing the % location and media being played
      let playerTime = event.jPlayer.status.currentPercentAbsolute;
       
     //There's some overlap between the seeked and stopped events. When a user clicks
     // the stop button it actually sends a "seek" to the 0 location. So if the seeked location is 0
     // then we track it as a stop, if it's greater than 0, it was an actual seek.
      if(playerTime >= 2 && playerTime < 3){
        $.extend(window.digitalData['link'],{event:'video-play'});
      }else if(playerTime >= 25 && playerTime < 26 && !(videoTracking['video-25%']) ){
        $.extend(window.digitalData['link'],{event:'video-25%complete'});
        window._satellite.track('video-tracking');
        videoTracking['video-25%'] = 'fired';
      }else if(playerTime >= 50 && playerTime < 51 && !(videoTracking['video-50%']) ){
        $.extend(window.digitalData['link'],{event:'video-50%complete'});
        window._satellite.track('video-tracking');
        videoTracking['video-50%'] = 'fired';
      }else if(playerTime >= 75 && playerTime < 76 && !(videoTracking['video-75%'])){
        $.extend(window.digitalData['link'],{event:'video-75%complete'});
        window._satellite.track('video-tracking');
        videoTracking['video-75%'] = 'fired';
      }else if(playerTime >= 99 && playerTime < 100 && !(videoTracking['video-100%'])){
        $.extend(window.digitalData['link'],{event:'video-completes'});
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

  function _initjPlayerVolSlider($jPlayerCnt, $jPlayer, $volCtrlBtn){
    $jPlayerCnt.find('input[type="range"]').rangeslider({
      polyfill: false,
      onSlide: function(position, value) {
        $jPlayer.jPlayer('volume', value / 100 );
        if(0 === value && !$volCtrlBtn.hasClass('mute')){
          $volCtrlBtn.addClass('mute');
        }else if($volCtrlBtn.hasClass('mute')){
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
  function _attachPlayPauseListener($currjPlayer, $playBtn){
    $playBtn.on('click', () => {
      $currjPlayer.jPlayer('pauseOthers');
      if(!$currjPlayer.data().jPlayer.status.paused){
        $currjPlayer.jPlayer('pause');
        $playBtn.removeClass('pause').addClass('play');
      }else{
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
  function _attachFullScrListener($currjPlayer, $fullScrBtn){
    $fullScrBtn.on('click', () => {
      $currjPlayer.parent().find('.j-cross-button').toggleClass('show');
    });
  }
  /**
   *
   * @param  $currjPlayer jPlayer's instance
   * @param  $volCtrlBtn jPlayer's full-screen button
   */
  function _attachVolListener($currjPlayer, $volCtrlBtn){
    $volCtrlBtn.on('click', () => {
      if($volCtrlBtn.hasClass('mute')) {
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
  function _attachClickListener($currjPlayer, $crossButton){

    $crossButton.on('click', (e) => {
      if (document.exitFullscreen) {
        document.exitFullscreen();
      }
      else if (document.msExitFullscreen) {
        document.msExitFullscreen();
      }
      else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
      }
      else if (document.webkitCancelFullScreen) {
        document.webkitCancelFullScreen();
      }
      $currjPlayer.parent().removeClass('jp-state-full-screen jp-video-full');
      $currjPlayer.css('width', '100%').css('height', 'auto');
      $currjPlayer.find('video')
               .css('width', '100%')
               .css('height', 'auto');
      $currjPlayer.parent().addClass('jp-video-360p');
      $(e.currentTarget).removeClass('show');
    });
  }
 
  
  function onFullScreenChange () {
    let fullScreenElement =
      document.fullscreenElement ||
      document.msFullscreenElement ||
      document.mozFullScreenElement ||
      document.webkitFullscreenElement;
    if(!fullScreenElement) {
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
    _cacheRefs() {
      _cache.$heroBannerCnt  = $('.c-video'); //
    },

     // Attach listeners to interesting events
    _initPlayers() {
      _cache.$heroBannerCnt.each((i, currjPlayerCnt) => {
        let $currjPlayerCnt = $(currjPlayerCnt);
        let $currjPlayer    = $currjPlayerCnt.find('.jp-jplayer');
        let $videoPlayBtn   = $currjPlayerCnt.find('.jp-video-play');
        let $playBtn        = $currjPlayerCnt.find('.vid-play');
        let $fullScrBtn     = $currjPlayerCnt.find('.jp-full-screen');
        let $volCtrlBtn     = $currjPlayerCnt.find('.vol-ctrl');
        let videoPath       = $currjPlayer.data('fileref');
        let imagePath       = $currjPlayer.data('posterref');
        let $crossButton    = $currjPlayerCnt.find('.j-cross-button');
        // Initialize jPlayer
        _initjPlayer($currjPlayerCnt, videoPath,imagePath);
        // Initialize rangeslider for jPlayer
        _initjPlayerVolSlider($currjPlayerCnt, $currjPlayer, $volCtrlBtn);
        // Play/Pause
        _attachPlayPauseListener($currjPlayer, $playBtn);
        // Full-screen
        _attachFullScrListener($currjPlayer, $fullScrBtn);
        // Volume control
        _attachVolListener($currjPlayer, $volCtrlBtn);
        // Prepare big "play" button for first load
        $videoPlayBtn.on('click', () => {
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
        $currjPlayerCnt.find('.rangeslider__handle').css('left',0);
        $currjPlayerCnt.find('.rangeslider__fill').css('width','0');
        $volCtrlBtn.addClass('mute');
      });
    },

    // Module initialization
    init() {
      this._cacheRefs();
      this._initPlayers();
    }
  };
})(window, window.jQuery, window.snro);

