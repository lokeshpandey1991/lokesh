/*
  jquery.mrfischer.mobile.js
  Created by Stephan Fischer <stephan@mrfischer.de> on 24.09.2013
  Copyright 2013 Stephan Fischer - MrFischer Web-Development. All rights reserved.
*/

$.extend({ 
    MobileSwipe: { 
        active  : false,
        shutter : false
    }
}); 

$.fn.MobileSwipe = function(options) 
{
  
  var canTouch = "ontouchstart" in window,
  touchstart   = canTouch ? 'touchstart' : 'mousedown',
  touchmove    = canTouch ? 'touchmove'  : 'mousemove',
  touchend     = canTouch ? 'touchend'   : 'mouseup';

  
  var realEventType = function (event)
  {
    return (canTouch) ? event.touches[0] : event;
  };

  var inArray = function(item,arr) 
  {
    for(p=0;p<arr.length;p++) 
      if (item == arr[p]) return true;
  
    return false;
  };
  
  var defaults = 
  {
      threshold: {x: 50, y: 20},
      swipeDelay: 250,
      swipeLeft:  function() { alert('swiped left');  },
      swipeRight: function() { alert('swiped right'); },
      disElementClasses : [],
      preventDefaultEvents: false
  };

  var options = $.extend(defaults, options);

  if (!this) return false;

  return this.each(function() 
  {

    var me = $(this);
  // Private variables for each element
    var orCoord      = { x: 0, y: 0 };
    var finalCoord   = { x: 0, y: 0 };
    var distance     = { x: 0, y: 0 };
    var isSwipe      = false;
 
    // Screen touched, store the original coordinate
    var swipeStart = function (event) 
    {
    
        if ($.MobileSwipe.shutter)  return;
        if ($.MobileSwipe.active)   return;
        if (multipleTouches(event)) return;
    
    
        var strClassName = event.target.className;
    
        // maybe user swpied on a disabled element then quit
        if (inArray(strClassName, options.disElementClasses)) {          
            return false;
        }
    
        var touch  = realEventType(event);
        orCoord.x  = touch.pageX;
        orCoord.y  = touch.pageY;       
        finalCoord = { x: 0, y: 0 };
        distance   = { x: 0, y: 0 };
    
        $.MobileSwipe.active = true;
        isSwipe = true;
    };

      
    var swipeMove = function(event)
    {
        
      /* before Swiping meachnism to start always */

      if (defaults.preventDefaultEvents) event.preventDefault();

      if ($.MobileSwipe.shutter) {
        event.preventDefault();
      }

      
      if ($.MobileSwipe.shutter)  return;
      if (!$.MobileSwipe.active)  return;
      if (multipleTouches(event)) return;
     
      
      var touch = realEventType(event);
        
      finalCoord.x  = touch.pageX;
      finalCoord.y  = touch.pageY;

      distance.x    = finalCoord.x  - orCoord.x ;
      distance.y    = finalCoord.y  - orCoord.y ;

      if (Math.abs(distance.y) <= options.threshold.y) {
          if (distance.x       <  -options.threshold.x)  { 
            $.MobileSwipe.shutter  = true;   
            $.MobileSwipe.active   = false;
            defaults.swipeRight();
          }  else if (distance.x  >   options.threshold.x)  { 
            $.MobileSwipe.shutter  = true;   
            $.MobileSwipe.active   = false; 
            defaults.swipeLeft();
          }
      } 
            
    };
      
    var swipeEnd = function(event) 
    {
        if ($.MobileSwipe.active) {
            $.MobileSwipe.active  = false;
        }
    };
      
    var multipleTouches = function(event) 
    {
      if(event.targetTouches != undefined) 
      {
        if (event.targetTouches.length != 1) 
        {
          return true;

        }
      }
    };

    // Add gestures to all swipable areas
    this.addEventListener(touchstart,    swipeStart, false);
    this.addEventListener(touchmove,     swipeMove,  false);
    this.addEventListener(touchend,      swipeEnd,   false);
    this.addEventListener('touchcancel', swipeEnd,   false);
  });
};