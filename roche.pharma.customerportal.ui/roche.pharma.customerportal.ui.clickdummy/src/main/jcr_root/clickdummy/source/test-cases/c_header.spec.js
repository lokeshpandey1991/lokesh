// Dummy fail test

describe("A test suite", function() {
   beforeEach(function() { });
   afterEach(function() { });
   it('should fail', function() { expect(true).to.be.true; });


    it('should pass call init function', function (){
        var getEls = sinon.stub(document, 'getElementsByClassName');
        var fakeHeader = {
            className: 'someClass'
        };
        getEls.returns([fakeHeader]);
        document.body.style.height = '10000px';
        snro.headerNavigationComp.init();
        window.scroll(10, 1000);
        $(window).trigger("scroll");
        window.setTimeout(function () {
            expect(fakeHeader.className.indexOf('js-scroll') > 0).to.be.false;
        }, 100);

    });
});


