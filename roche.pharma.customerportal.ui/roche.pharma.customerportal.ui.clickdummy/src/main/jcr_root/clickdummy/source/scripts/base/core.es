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

$(function() {
  let moduleArray = ['ajaxWrapper', 'commonUtils', 'modalPopComp', 'liipboxComp', 'dynamicMedia', 'analytics'];
  $('[data-module]').each(function() {
    let currentModule = $(this).attr('data-module');
  // check if no double entry of module
    if(moduleArray.indexOf(currentModule) === -1) {
      moduleArray.push(currentModule);
    }
  });

  // check of all available selectors to initialize corresponsing modules
  $.each(moduleArray, function(index, value) {
    try {
      // initialize the current module
      snro[value].init();
    } catch(e) {
      // catch error, if any, while initialing module
      snro.commonUtils.log(`${value} doesn't have init method.` );
    }
  });

  //Check for targeted modules and call their init functions
  if(window.ContextHub) {
    window.ContextHub.eventing.on(window.ContextHub.SegmentEngine.PageInteraction.Teaser.prototype.info.loadEvent, function () {
      //TODO: create a list of all personalized modules and iterate through them here.
      if($('[data-module="productCategoryList"]').length) {
        window.snro.productCategoryList.init();
      }
    });
  }
});
