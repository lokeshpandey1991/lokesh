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