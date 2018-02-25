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

  let showHideTbl = () => {
  // define variable for table

    let viewLink = $('.view-btn'),
      viewTarget = $('.js-table'),
      viewTargetChild = $('.js-table tr'),
      linkLocation,
      closeBtn = 'js-close',
      setTableHeight=$('.js-table-height'),
      clientHeight = $( window ).height();

    //this function is using for show the table when click on view full link btn
    viewLink.on('click', (event) => {
      let data = $(event.target).attr('data-action');
      linkLocation = $(event.target).offset().top;
      $('.'+ data).show();
    });

    
    //this function is using for hide table when click on close
    viewTarget.on('click', function(event){
      if($(event.target).hasClass(closeBtn)){
        $(this).hide();
        $(window).scrollTop(linkLocation);
      }
    });
    
    
  //this function is using for row animation
    viewTargetChild.hover(
      function(){
        $(this).find('th').addClass('animaterow');
      },
      function(){
        $(this).find('th').removeClass('animaterow');
      }
    );

    // Check for mobile
    if(snro.commonUtils.isMobile()){
      setTableHeight.height(clientHeight-150+'px');
    }
  };

  snro.featureTable = {
    init: () => {
      showHideTbl();
    }
  };

}(window, window.jQuery, window.snro));
