/**
 * Created by shubham on 8/1/2017.
 */

describe("Country Selector", function() {
  var getStub, ajaxStub, result = {
      "results" : [
        {
          "address_components" : [
            {
              "long_name" : "Confrence1",
              "short_name" : "Confrence1",
              "types" : [ "establishment", "point_of_interest", "premise" ]
            },
            {
              "long_name" : "Sector 21",
              "short_name" : "Sector 21",
              "types" : [ "political", "sublocality", "sublocality_level_1" ]
            },
            {
              "long_name" : "Gurugram",
              "short_name" : "Gurugram",
              "types" : [ "locality", "political" ]
            },
            {
              "long_name" : "Gurgaon",
              "short_name" : "Gurgaon",
              "types" : [ "administrative_area_level_2", "political" ]
            },
            {
              "long_name" : "Haryana",
              "short_name" : "HR",
              "types" : [ "administrative_area_level_1", "political" ]
            },
            {
              "long_name" : "India",
              "short_name" : "IN",
              "types" : [ "country", "political" ]
            },
            {
              "long_name" : "122016",
              "short_name" : "122016",
              "types" : [ "postal_code" ]
            }
          ],
          "formatted_address" : "Confrence1, Sector 21, Gurugram, Haryana 122016, India",
          "geometry" : {
            "location" : {
              "lat" : 28.5103758,
              "lng" : 77.07248389999999
            },
            "location_type" : "ROOFTOP",
            "viewport" : {
              "northeast" : {
                "lat" : 28.5117247802915,
                "lng" : 77.07383288029149
              },
              "southwest" : {
                "lat" : 28.5090268197085,
                "lng" : 77.07113491970848
              }
            }
          },
          "place_id" : "ChIJG_mIFWQZDTkRu6Gy0L6NR3o",
          "types" : [ "establishment", "point_of_interest", "premise" ]
        }
      ],
      "status" : "OK"
    },
    markup = '<div class="c-countryselector x-liipbox  js-countryselector" data-module="countrySelectorCmp">\n' +
      '    <div class="x-liipbox__top-section js-liipbox-top-section">\n' +
      '      <div class="col-1 x-liipbox__left-s">\n' +
      '        <button class="navbar-toggle js-cross-icon translated" data-fn-click="true" data-da-satellitetrackevent="generic-link-tracking" data-da-link_link-page-name="" data-da-link_link-category="" data-da-link_link-section="countryselector" data-da-link_link-interaction-method="">\n' +
      '          <span class="icon-bar"></span>\n' +
      '          <span class="icon-bar"></span>\n' +
      '          <span class="icon-bar"></span>\n' +
      '          <span class="sr-only">open navigation</span>\n' +
      '        </button>\n' +
      '      </div>\n' +
      '        <div class="x-liipbox__text ellipsis">Location Selector</div>\n' +
      '      <div class="c-countryselector__country-text x-liipbox__text ellipsis js-current-country-text" data-translated-text="You are currently on the site">You are currently on the site</div>\n' +
      '      <div class="col-2 x-liipbox__right-s">\n' +
      '        <div class="x-liipbox__right-s__logo js-liipbox-logo translated">\n' +
      '          <img src="" itemprop="logo">\n' +
      '        </div>\n' +
      '      </div>\n' +
      '    </div>\n' +
      '    <div class="links-section">\n' +
      '      <div class="container-fluid">\n' +
      '        <div class="col-xs-12 col-sm-3 row-1">\n' +
      '          <div class="left-sm ">\n' +
      '            <div class="sign-in link-row  animated fadeInUp" style="animation-delay: 0s;">\n' +
      '              <div id="map-continents" class="cssmap-container cssmap-320" style="height: auto;">\n' +
      '                <ul class="continents cssmap">\n' +
      '                  \n' +
      '                    \n' +
      '                    \n' +
      '                    \n' +
      '                    \n' +
      '                    <li class="c5" data-target="js-na">' +
      '<span class="m"><span class="s1"></span></span><a href="#north-america">North America</a><span class="bg"></span></li>\n' +
      '                    \n' +
      '                  \n' +
      '                </ul>\n' +
      '              <span class="cssmap-loader" style="left: 160px; margin-left: -47px; margin-top: -10px; top: 88px; display: none;">Loading ...</span></div>\n' +
      '              <h2>\n' +
      '                <ul class="c-countryselector__continent-list">\n' +
      '                  \n' +
      '                    <li><a href="javascript:void(0);" class="js-continent-selector active" id="js-na">North America </a></li>\n' +
      '                  \n' +
      '                </ul>\n' +
      '              </h2>\n' +
      '            </div>\n' +
      '          </div>\n' +
      '        </div>\n' +
      '        <div class="col-xs-12 col-sm-9 row-2">\n' +
      '          <div class="right-sm">\n' +
      '            <div class="confirm-box js-confirm-box" style="display: none;">\n' +
      '              <p class="confirm-box__content">\n' +
      '                <span data-preselection-string="We detected your location to be {0}. Is this correct?" data-existing-cookie-string="We detected your location to be {0}. Is this correct?" id="js-country-text"></span>\n' +
      '              </p>\n' +
      '              <div class="confirm-box__btngroup">\n' +
      '                <a class="confirm-box__btngroup__btn js-preselection-yes" href="#">Yes</a>\n' +
      '                <a class="confirm-box__btngroup__btn js-preselection-no" href="#">No</a>\n' +
      '              </div>\n' +
      '            </div>\n' +
      '            \n' +
      '              <ul class="right-sm__col country-list js-country-list" id="na" style="display: block;">\n' +
      '                \n' +
      '                  \n' +
      '                    <li class="link-row animated fadeInUp" style="animation-delay: 0.04s;">\n' +
      '                      <a href="#" itemprop="url" data-href="/content/customerportal/us/en/home.html" data-country-code="country-us" data-country-name="United States" data-language="en" data-language-text="rdoe_countrySelector.language.en" class="link js-country-language-selection active">\n' +
      '                        <span class="content">United States | English</span>\n' +
      '                      </a>\n' +
      '                    </li>\n' +
      '                  \n' +
      '                    <li class="link-row animated fadeInUp" style="animation-delay: 0.08s;">\n' +
      '                      <a href="#" itemprop="url" data-href="/content/customerportal/us/fr/home.html" data-country-code="country-us" data-country-name="United States" data-language="fr" data-language-text="rdoe_countrySelector.language.fr" class="link js-country-language-selection">\n' +
      '                        <span class="content">United States | French</span>\n' +
      '                      </a>\n' +
      '                    </li>\n' +
      '                  \n' +
      '              </ul>\n' +
      '            \n' +
      '            <div class="global-site-box js-global-site-box" style="display: block;">\n' +
      '              <span class="global-site-text"> If the country and language you are looking for is not listed, use our global site:</span>\n' +
      '              <a data-href="/content/customerportal/us/en/home.html" href="#" data-country-code="country-us" data-language="en" class="global-site-link global-link x-sub-hover js-country-language-selection active">Global Site\n' +
      '                <span class="u-right-arrow x-text-hidden">Next</span>\n' +
      '              </a>\n' +
      '            </div>\n' +
      '          </div>\n' +
      '        </div>\n' +
      '      </div>\n' +
      '    </div>\n' +
      '  </div>';
  beforeEach(function() {
    $('body').append('<a href="#" class="js-link-country"></a>');
    $('body').append('<input name="currentPagepath" />');
    getStub = sinon.stub(jQuery, 'get').callsFake(function () {
      var dfd = $.Deferred();
      return dfd.resolve(result)
    });
    ajaxStub = sinon.stub(snro.ajaxWrapper, 'getXhrObj');
    ajaxStub.returns((function () {
      var dfd = $.Deferred();
      return dfd.resolve(markup);
    })());
  });

  afterEach(function() {
    $('.js-link-country').remove();
    getStub.restore();
    ajaxStub.restore();
  });

  it('should make a ajax call', function() {
    navigator.geolocation = function() {};
    navigator.geolocation.getCurrentPosition = function(callBack) {
      callBack({coords: {latitude: '72', longitude: '71'}});
    };
    snro.countrySelectorCmp.init();
    expect(snro.ajaxWrapper.getXhrObj.called).to.be.true;
  });

  it('should bind events and hide cross icon', function() {
    navigator.geolocation = function() {};
    navigator.geolocation.getCurrentPosition = function(callBack) {
      callBack({coords: {latitude: '72', longitude: '71'}});
    };
    snro.countrySelectorCmp.init();
    expect($('.js-countryselector .js-cross-icon').parent().css('display')).to.equal('none');
  });

  it('should location detected notice if location is found', function() {
    navigator.geolocation = function() {};
    navigator.geolocation.getCurrentPosition = function(callBack) {
      callBack({coords: {latitude: '72', longitude: '71'}});
    };
    $('body').append('<a href="#" data-href="#" data-country-code="country-in" data-country-name="India" data-language="en" class="js-country-language-selection"> </a>');
    snro.countrySelectorCmp.init();
    setTimeout(function () {
      expect($('.js-confirm-box').css('display')).to.equal('block');
    }, 10);
  });

  it('should work if location is not detected', function() {
    navigator.geolocation = function() {};
    navigator.geolocation.getCurrentPosition = function(callBack, errorCalBack) {
      errorCalBack();
    };
    snro.countrySelectorCmp.init();
    expect(snro.countrySelectorCmp.locationDetected).to.be.false;
  });

  it('should trigger country selector on click of location selector', function() {
    navigator.geolocation = function() {};
    navigator.geolocation.getCurrentPosition = function(callBack, errorCalBack) {
      errorCalBack();
    };
    snro.countrySelectorCmp.init();
    $('.js-link-country').click();
    expect(snro.countrySelectorCmp.reselectionInitiated).to.be.true;
  });

  it('should show list of countries', function() {
    navigator.geolocation = function() {};
    navigator.geolocation.getCurrentPosition = function(callBack) {
      callBack({coords: {latitude: '72', longitude: '71'}});
    };
    sinon.spy(snro.commonUtils ,'setCookie');
    snro.countrySelectorCmp.init();
    $('.js-preselection-yes').click();
    setTimeout(function () {
      expect(snro.commonUtils.setCookie.called).to.be.true;
    }, 10)
    snro.commonUtils.setCookie.restore();
  });

  it('should show list of countries', function() {
    navigator.geolocation = function() {};
    navigator.geolocation.getCurrentPosition = function(callBack) {
      callBack({coords: {latitude: '72', longitude: '71'}});
    };
    $('body').append('<li class="js-continent-selector" data-target="js-us" id="js-us">\' +\n' +
      '      \'<span class="m"><span class="s1 "></span></span><a href="#north-america">North America</a><span class="bg"></span></li>' +
      '<div id="us"/>\\n\' ');
    snro.countrySelectorCmp.init();
    $('#js-us').click();
    expect($('#us').css('display')).to.equal('block');

  });

  /*it('should show list of countries', function() {
    navigator.geolocation = function() {};
    navigator.geolocation.getCurrentPosition = function(callBack) {
      callBack({coords: {latitude: '72', longitude: '71'}});
    };
    sinon.spy(snro.commonUtils ,'setCookie');
    $('.js-country-language-selection').click()
    snro.countrySelectorCmp.init();
    expect(snro.commonUtils.setCookie.called).to.be.true
    snro.commonUtils.setCookie.restore();

  });*/

});
