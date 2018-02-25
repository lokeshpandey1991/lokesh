var markup = '<div class="c-gallery x-outer-margins" data-module="imagegalleryCmp">\n' +
  '  <div class="c-gallery__wrapper">\n' +
  '    <div class="c-gallery__top-wrapper col-sm-9 x-no-padding js-visible-onscroll" data-select-row="one">\n' +
  '      <div class="c-gallery__wrapper--three js-row-one col-sm-7 almost_visible x-no-padding" data-row="one">\n' +
  '        <sly data-sly-use.lib="/apps/roche/pharma/customerportal/components/dynamicimage/dynamicimage.html"\n' +
  '             data-sly-call="${lib.dynamicimage_template @imagePath=data.fileReference3, imageView="image3",\n' +
  '             altText=data.altText3, className="c-gallery__image"}" />\n' +
  '        <div class="c-gallery__img-overlay">\n' +
  '          <span class="c-gallery__img-title">${data.imageHeadline}</span>\n' +
  '        </div>\n' +
  '      </div>\n' +
  '    </div>\n' +
  '  </div>\n' +
  '</div>';

describe('Image Gallery', function () {
  beforeEach(function () {
    $(markup).appendTo(document.body);
  });

  afterEach(function () {
    $('.c-gallery').remove();
  });

  it('Bind animation event on scroll', function () {
    var spy = sinon.spy();
    var stubAnimateRowImages = sinon.stub(snro.imagegalleryCmp, 'animateRowImages');
    snro.imagegalleryCmp.init();
    window.scrollBy(0, 10);
    window.setTimeout(function () {
      expect(stubAnimateRowImages.calledOnce).to.be.true;
      stubAnimateRowImages.restore();
    }, 2000);
  });
});
