describe("Testing init function", function(){
  beforeEach(function() {

    var navTabsMarkup = "<nav class=\"c-tabs-container js-tabs-container\">\n" +
      "    <ul class=\"c-tabs-list js-tabs-list\">\n" +
      "      <li class=\"c-tab-list-item js-tab-item selected\" data-tab-name=\"productInfo\">\n" +
      "        <a>Text</a>\n" +
      "      </li>\n" +
      "      <li class=\"c-tab-list-item js-tab-item\" data-tab-name=\"some\">\n" +
      "        <a>Text</a>\n" +
      "      </li>\n" +
      "    </ul> \n" +
      "  </nav>" +
      "<div id=\"some\">\n" +
      "</div>";

    $(navTabsMarkup).appendTo(document.body);
  });
  afterEach(function() {
    $('.c-tabs-list').remove();
  });

  it('should call click function', function (){
    snro.pdpNavTabs.init();
    $('li.js-tab-item').click();
    window.setTimeout(function () {
      var isDivVisible = $('#some').is(':visible');
      expect(isDivVisible).to.be.true;
    }, 1000);

  });

  it('should call init function', function (){
    $('ul.js-tabs-list .js-tab-item:first-child').hide();
    snro.pdpNavTabs.init();
    var visibleTabsCOntainers = $('.js-tabs-container:visible').length;
    expect(visibleTabsCOntainers).to.equal(1);
  });

});