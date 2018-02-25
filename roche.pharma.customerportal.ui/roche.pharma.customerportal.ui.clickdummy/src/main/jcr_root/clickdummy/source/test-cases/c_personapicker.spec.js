/**
 * Created by shubham on 8/1/2017.
 */

describe("Person Picker", function() {
  beforeEach(function() {
    sinon.spy(snro.liipboxComp, '_liipboxOpen');
    sinon.spy(snro.personaPickerComp,'_callPersonaPicker');
    $('body').append('<div class="js-header-persona-link"></div>');
  });

  afterEach(function() {
    snro.liipboxComp._liipboxOpen.restore();
    snro.personaPickerComp._callPersonaPicker.restore();
    $('.js-header-persona-link').remove();
  });

  it('should make a ajax call', function() {
    var ajaxStub =  sinon.stub(snro.ajaxWrapper,  'getXhrObj');
    var markup = '<div class="js-header-persona-link"></div>';
    ajaxStub.returns((function () {
      var  dfd = $.Deferred();
      return  dfd.resolve(markup);
    })());
    snro.personaPickerComp.init();
    expect(snro.ajaxWrapper.getXhrObj.calledOnce);
  });

  it('should call persona picker on header link', function() {
    snro.personaPickerComp.init();
    $('.js-header-persona-link').click();
    expect(snro.personaPickerComp._callPersonaPicker.called).to.be.true;
  });

  it('should come from country selector when cookie exists', function() {
    var getCookieStub = sinon.stub(snro.commonUtils, 'getCookie');
    getCookieStub.withArgs('persona-type').returns('HealthCare');
    snro.countrySelectorCmp.countrySelectorPassed = true;
    snro.personaPickerComp.init();
    expect(snro.countrySelectorCmp.countrySelectorPassed).to.equal.true;
    getCookieStub.restore();
  });

  it('should update header when the cookie already exists', function() {
    var getCookieStub = sinon.stub(snro.commonUtils, 'getCookie');
    getCookieStub.withArgs('persona-type').returns('HealthCare');
    snro.countrySelectorCmp.countrySelectorPassed = false;
    snro.personaPickerComp.init();
    expect(snro.countrySelectorCmp.countrySelectorPassed).to.equal.false;
    getCookieStub.restore();
  });

});

describe("Person Picker", function() {
  beforeEach(function() {
    var html = '<div class="js-header-persona-link" /> <div class="c-persona-picker x-liipbox js-c-persona-picker" id="persona-picker">\n' +
      '      <div class="x-liipbox__top-section js-liipbox-top-section">\n' +
      '         <div class="col-1 x-liipbox__left-s">\n' +
      '            <button class="x-liipbox__left-s__cross-icon js-cross-icon" itemprop="image" role="button"></button>\n' +
      '         </div>\n' +
      '\n' +
      '   \n' +
      '      </div>\n' +
      '      <div class="c-persona-picker__bottom-section clearfix">\n' +
      '       \n' +
      '         \n' +
      '\n' +
      '            <a href="javascript:void(0)" class="col-xs-12 col-sm-4 c-persona-picker__persona-link js-persona-link" tabindex="0">\n' +
      '              <div class="persona-link__type hidden js-persona-type">\n' +
      '                  HealthCare\n' +
      '               </div>\n' +
      '               <div class="persona-link__name js-persona-name">\n' +
      '                  HEALTH CARE PROFESSIONALS\n' +
      '               </div>\n' +
      '               <div class="persona-link__description">\n' +
      '                  Lorem ipsum dolor sit amet, nullam pericula consectetuer an ius. Animal nonumes at sit. Nec an graeco expetendis.\n' +
      '               </div>\n' +
      '            </a>\n' +
      '\n' +
      '         \n' +
      '\n' +
      '         \n' +
      '\n' +
      '         \n' +
      '      </div>\n' +
      '\n' +
      '   </div>\n';

    $(html).appendTo(document.body);
    sinon.spy(window, 'onbeforeunload');
    sinon.spy(snro.personaPickerComp, '_callPersonaPicker');

  });
  afterEach(function() {
    snro.personaPickerComp._callPersonaPicker.restore();
  });

  it('should set cookie', function() {
    var getCookieStub = sinon.stub(snro.commonUtils, 'getCookie');
    getCookieStub.withArgs('cookie').returns('HealthCare');
    var slideStub = sinon.stub($.prototype, 'slideUp');
    slideStub.yields();
    snro.personaPickerComp.cookieUpdate = true;
    snro.personaPickerComp.init();
    $('body').find('.js-persona-link').click();
    expect(snro.personaPickerComp._callPersonaPicker.called).to.be.true;
    getCookieStub.restore();
  });

});
