/**
 * @global 
 * @file global object, to determine mobile browsers, handhelds, touch devices and click events.
 * @author Stephan Fischer <sfisc2@sapient.com>
 */

var browser = (function()
{

  var on = {};

  on.agent       = navigator.userAgent;
  on.ios         = /iPhone|iPad|iPod/i.test(on.agent);
  on.android     = /Android/i.test(on.agent);
  on.iemobile    = /IEMobile/i.test(on.agent);
  on.blackberry  = /BlackBerry/i.test(on.agent);
  on.tablet      = /Tablet|iPad/i.test(on.agent);
  on.mobile      = (on.ios || on.android || on.iemobile || on.blackberry);
  on.touch       =  Modernizr.touch;
  on.handheld    =  on.touch && !on.tablet;
  on.click       =  on.touch ? 'touchend'  : 'click';
  on.fastClick   =  on.touch ? 'touchstart' : 'click';

  $(function()
  {
      if (on.mobile)      $('html').addClass('mobile');
      if (on.tablet)      $('html').addClass('tablet');
      if (on.handheld)    $('html').addClass('handheld');
      if (on.ios)         $('html').addClass('browser-ios');
      if (on.android)     $('html').addClass('browser-android');
      if (on.iemobile)    $('html').addClass('browser-iemobile');
      if (on.blackberry)  $('html').addClass('browser-blackberry');

  });

  return on;
})();
