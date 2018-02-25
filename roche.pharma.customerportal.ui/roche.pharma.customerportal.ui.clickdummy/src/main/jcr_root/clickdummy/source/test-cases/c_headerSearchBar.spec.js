/**
 * Created by Bindhyachal on 31/08/2017.
 */
var jsonVar ={
  "query": {
  "user-query" : "roc",
  "lower-results" : 1,
  "upper-results" : 3,
  "total-results" : 3 },
  "suggestions":[  ],
  "results":
  [  {
  "title":"title1",
  "url": "https://35.154.101.110/us/en/home/products/roche54353.html",
  "pageType": "Products"
  } ,  {
  "title":"title2",
  "url": "https://35.154.101.110/us/en/home/products/roche543531.html",
  "pageType": "Products"
  } ,  {
  "title":"Cobas Liat PCR",
  "url": "https://35.154.101.110/us/en/home/products/roche543532.html",
  "pageType": "News"
  }  
  ]
}
describe.skip('Header Search Bar', function() {
  var request;
  var stub;
  beforeEach(function() {
    request = sinon.stub(snro.ajaxWrapper, 'getXhrObj');
    //stub = sinon.stub(snro.commonUtils, 'queryParams').returns('som');
    request.returns((function() {
      var deferred;
      deferred = jQuery.Deferred();
      setTimeout(function() {
        return deferred.resolve(jsonVar);
      }, 10);
      return deferred;
    })());
    
    $('body').append('<div class="js-liipbox"><input class="roche-header-search-input" placeholder="type here" data-placeholder="type here" type="text"/> <button class="header-search-button"></button>'+
      '<div class="header-search-result-container"><div class="header-search-result"></div></div><div class="header-search-predictive">'+
      '<div class="predictive-search-results"></div></div></div>');
  });
  afterEach(function() {
    request.restore();
    //snro.ajaxWrapper.getXhrObj.restore();
  });
  it('should fail', function() {
    var $inputVal = $('.roche-header-search-input');
    $inputVal.val('');
  });
  it('should start have length 3', function() {
    console.log('here');
    snro.headerSearchComp.bindEvents();
    var $inputVar = $('.roche-header-search-input');
    var $searchButton = $('.header-search-button');
    var resultsContainer = $('.header-search-result-container');
    var $searchPredective = $('.header-search-predictive');
    var $predectiveItem = $('.js-suggestive-terms');
    var $inputID =$('#search-page-input');
    var $body = $('body');
    $inputVar.val('som');
    var e = $.Event("keyup");
    var eFocus = $.Event("focus");
    var eBlur = $.Event("blur");
    var clickEvent = $.Event("click");

    $inputVar.trigger(eFocus);
    //expect($inputVar.attr('placeholder')).to.be.empty;
    $inputVar.trigger(eBlur);
    expect($inputVar.attr('placeholder')).to.equal('type here');
    e.which = 50;
    $inputVar.trigger(e);
    expect($inputVar.val()).to.have.length.above(2);
    expect($searchButton.hasClass('active')).to.be.false;
    expect($searchPredective.css('display')).to.equal('block');
    snro.commonUtils.isDesktopMode();
    expect(snro.ajaxWrapper.getXhrObj.calledOnce);
    expect(jsonVar).to.be.an('object');
    expect(jsonVar).to.have.a.property('results');
    expect(jsonVar.results).to.have.a.property('length');
    expect(jsonVar.results).to.have.length.above(0);
    expect($('.js-liipbox .header-search-result-container')).to.have.length.above(0);
    expect($('.js-liipbox .header-search-result-container').css('display')).to.equal('block');
    $searchButton.trigger(clickEvent);
    expect($inputVar.val()).to.equal('som');
    $predectiveItem.trigger(clickEvent);
    expect($('.js-liipbox .header-search-predictive').css('display')).to.equal('block');
    
  });
});


