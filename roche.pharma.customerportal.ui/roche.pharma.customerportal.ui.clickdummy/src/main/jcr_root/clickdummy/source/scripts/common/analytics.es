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
let digitalData = window.digitalData || {};
(function (w, $, snro) {
  if (!$) {return;}
  snro = w.snro = snro || {};

  // Adding few string prototype functions
  if (!String.prototype.lowerCaseFirstLetter) {
    String.prototype.lowerCaseFirstLetter = function() {
      return this.charAt(0).toLowerCase() + this.slice(1);
    };
  }

  // module definition
  snro.analytics = {
    moduleName: 'Analytics',

    getBrowserWidth:function(){
      if(snro.commonUtils.isMobile()){
        // Mobile View
        return 'mobile';
      } else if(snro.commonUtils.isTablet()){
        // Tablet View
        return 'tablet';
      } else {
        // Desktop View
        return 'desktop';
      }
    },

    loadEvent:function(){
      let pagetype, productid, productname, productcategory;
      if(document.getElementsByTagName('meta')['page-type'] === undefined){
        pagetype = 'home';
      }else{
        pagetype = document.getElementsByTagName('meta')['page-type'].content  && document.getElementsByTagName('meta')['page-type'].content.toLocaleLowerCase() || '';
      }

      let analyticsId= document.getElementById('analyticsdata'),
        siteLanglocation = snro.commonUtils.getCookie('location-and-language') && snro.commonUtils.getCookie('location-and-language').toLocaleLowerCase()|| '',
        serverName = analyticsId.getAttribute('data-servername'),
        channel = analyticsId.getAttribute('data-channel') && analyticsId.getAttribute('data-channel').toLocaleLowerCase() || '',
        siteSectionOne = analyticsId.getAttribute('data-sitesectionone') && analyticsId.getAttribute('data-sitesectionone').toLocaleLowerCase()|| '',
        siteSectionTwo = analyticsId.getAttribute('data-sitesectiontwo') && analyticsId.getAttribute('data-sitesectiontwo').toLocaleLowerCase()|| '',
        siteSectionThree = analyticsId.getAttribute('data-sitesectionthree') && analyticsId.getAttribute('data-sitesectionthree').toLocaleLowerCase()|| '',
        siteSectionFour = analyticsId.getAttribute('data-sitesectionfour') && analyticsId.getAttribute('data-sitesectionfour').toLocaleLowerCase()|| '',
        siteSectionOnePName = analyticsId.getAttribute('data-sitesectionone') && ':' + analyticsId.getAttribute('data-sitesectionone').toLocaleLowerCase()|| '',
        siteSectionTwoPName = analyticsId.getAttribute('data-sitesectiontwo') && ':' + analyticsId.getAttribute('data-sitesectiontwo').toLocaleLowerCase()|| '',
        siteSectionThreePName = analyticsId.getAttribute('data-sitesectionthree') && ':' + analyticsId.getAttribute('data-sitesectionthree').toLocaleLowerCase()|| '',
        personaType = snro.commonUtils.getCookie('persona-type') && snro.commonUtils.getCookie('persona-type').toLocaleLowerCase()|| '',
        siteCountry = siteLanglocation.split('|')[0],
        siteLanguage = siteLanglocation.split('|')[1],
        deviceView = snro.analytics.getBrowserWidth();
      window.digitalData = {
        page:{
          server:serverName,
          channel:channel,
          pageType:pagetype,
          pageName:'rd'+':'+siteCountry+':'+siteLanguage + siteSectionOnePName + siteSectionTwoPName + siteSectionThreePName,
          siteSection1:siteSectionOne,
          siteSection2:siteSectionTwo,
          siteSection3:siteSectionThree,
          siteSection4:siteSectionFour,
          pageURL:window.location.href,
          siteLanguage:siteLanguage,
          siteType:'responsive',
          persona:personaType,
          siteCountry:siteCountry
        },

        user:{
          deviceType:deviceView
        },

        link:{}
      };

      if(document.getElementsByTagName('meta')['product-id'] !== undefined && document.getElementsByTagName('meta')['product-category'] !== undefined){
        productid = document.getElementsByTagName('meta')['product-id'].content  && document.getElementsByTagName('meta')['product-id'].content.toLocaleLowerCase() || '';
        productcategory = document.getElementsByTagName('meta')['product-category'].content  && document.getElementsByTagName('meta')['product-category'].content.toLocaleLowerCase() || '';
        productname = window.document.title;
        if(productname.length > 100) {
          productname = productname.substring(0,100);
        }else if(productcategory.length > 100){
          productcategory = productcategory.substring(0,100);
        }
      }
      if(pagetype === 'products'){
        $.extend(digitalData['page'],{event:'pdp-load', productId:productid, productName:productname, productCategory:productcategory});
      }

      // for eloqua tracking
      if(pagetype === 'contact-us') {
        let eloquaFlag = 'false';
        if ($('.js-contact-form').attr('data-eloqua-enabled')) {
          eloquaFlag = 'true';
        }
        $.extend(window.digitalData['page'], { eloquaEnabled: eloquaFlag });
      }
    },

    urlType:function(target){

      if($(target).attr('href') !== undefined && $(target).attr('href').match(/\bhttps?:\/\/\S+/gi)){
        // Mobile View
        return 'external';
      } else {
        // Desktop View
        return 'internal';
      }
    },
    // handle the events
    trackEvents: function() {
      //on click track event and push in DigitalData
      $(document).on('sclick enterKeyPress', '[data-fn-click=true]', function() {
        let self = this, satelliteTrackEvent;
        window.digitalData = {
          page:{},
          user:{},
          link:{}
        };
        //update satellite.track parameter on page
        if($(this).attr('data-da-satellitetrackEvent')){
          satelliteTrackEvent = $(this).attr('data-da-satelliteTrackEvent');
        }

        $.each($(this).data(), function(attr, attrValue) {
          if (~attr.indexOf('da')) {
            let analyticsKey = attr.replace('da', '').lowerCaseFirstLetter(),
              subkey = {},
              attrArr,
              keyLevel1,
              keyLevel2;
            if (analyticsKey.indexOf('_') !== -1) {
              attrArr = analyticsKey.split('_');
              keyLevel1 = attrArr[1];
              keyLevel2 = attrArr[0];
              if(keyLevel1==='linkPageName'){
                attrValue = window.document.title || 'roche';
              }else if(keyLevel1==='linkInteractionMethod'){
                attrValue = 'click-on-' + snro.analytics.getBrowserWidth();
              }else if(keyLevel1==='linkCategory'){
                attrValue = snro.analytics.urlType(self);
              }
              else if(keyLevel1==='contentType'){
                attrValue = attrValue || window.digitalData.page.pageType || '';
              }
              if($(self).parents('.product-category').attr('id') && $(self).parents('.product-category').attr('id').indexOf('product') > -1 && keyLevel1==='event'){
                attrValue = 'product-click';
              }

              if (attrValue !== undefined && attrValue !== null && attrValue !== '') {
                attrValue = String(attrValue);
                attrValue = attrValue.replace(/\s+/g, '-').toLowerCase();
                if(attrValue.length > 100) {
                  attrValue = attrValue.substring(0,100);
                }
              }

              subkey[keyLevel1] = attrValue;
            }
            if(keyLevel2) {
              digitalData[keyLevel2] = digitalData[keyLevel2] || {};
            }
            $.extend(digitalData[keyLevel2], subkey);
          }
        });

        window._satellite.track(satelliteTrackEvent);

      });
    },
    init: function() {
      this.loadEvent();
      if (!(w.digitalData)) {
        return;
      }
      this.trackEvents();
    }
  };
})(
  window,
  window.jQuery,
  window.snro
);
