/**
 * Created by adube7 on 7/13/2017.
 */

describe("Hamburger Menu", function () {

  before(function () {
    $('<div class="x-hamburger-icon js-menu"></div>').appendTo(document.body);
  });

  after(function () {
    $('.js-liipbox-navigation').remove();
  });

  it('should bind function to button click', function () {
    snro.navigationCmp.init();
    sinon.spy(snro.navigationCmp, 'onHamburgerClick');
    $('.js-menu').click();
    window.setTimeout(function () {
      expect(snro.navigationCmp.onHamburgerClick.called).to.be.true;
    }, 1000);
  });

});

