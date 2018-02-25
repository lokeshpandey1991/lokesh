var html = '<div class="c-listingWrapper js-listing-wrapper" data-module="listingwrapperCmp"/>\n' +
  '</div>\n';

const emptyResult = {};

const data = {
  "query": {
    "user-query": "cobas",
    "lower-results": 1,
    "upper-results": 5,
    "total-results": 5
  },
  "facets": {

    "ignore": {}
  },
  "results": {
    "data": [{
      "title": "Cobas® 6800 System",
      "url": "https://35.154.101.110/us/en/home/products/roche54353.html",
      "page-type": "Products",
      "description": "",
      "image": "/content/dam/roche/customerportal/products/product-7.png",
      "altText": "",
      "eventStartDate": "No Date",
      "eventEndDate": "No Date",
      "eventFormatDate": "",
      "venue": "",
      "featured-product": "no",
      "publishedDate": "No Date",
      "category": "Research Topics"
    }, {
      "title": "Cobas® analyzer series",
      "url": "https://35.154.101.110/us/en/home/products/roche543531.html",
      "page-type": "Products",
      "description": "",
      "image": "/content/dam/roche/customerportal/images/roche_testimages.jpg",
      "altText": "alt text",
      "eventStartDate": "No Date",
      "eventEndDate": "No Date",
      "eventFormatDate": "",
      "venue": "",
      "featured-product": "no",
      "publishedDate": "No Date",
      "category": "Research Topics"
    }, {
      "title": "Cobas 4800 BRAF V600 Mutation Test",
      "url": "https://35.154.101.110/us/en/home/products/roche543533.html",
      "page-type": "Products",
      "description": "",
      "image": "/content/dam/roche/customerportal/images/herobannerv2-2880x1404.jpg",
      "altText": "alt text",
      "eventStartDate": "No Date",
      "eventEndDate": "No Date",
      "eventFormatDate": "",
      "venue": "",
      "featured-product": "yes",
      "publishedDate": "No Date",
      "category": "Disease Areas"
    }, {
      "title": "cobas b 121 system",
      "url": "https://35.154.101.110/us/en/home/products/roche54334.html",
      "page-type": "Products",
      "description": "",
      "image": "/content/dam/roche/customerportal/products/product-7.png",
      "altText": "",
      "eventStartDate": "No Date",
      "eventEndDate": "No Date",
      "eventFormatDate": "",
      "venue": "",
      "featured-product": "no",
      "publishedDate": "No Date",
      "category": ""
    }, {
      "title": "cobas b 123 POC system",
      "url": "https://35.154.101.110/us/en/home/products/roche54335.html",
      "page-type": "Products",
      "description": "",
      "image": "/content/dam/roche/customerportal/products/product-7.png",
      "altText": "",
      "eventStartDate": "No Date",
      "eventEndDate": "No Date",
      "eventFormatDate": "",
      "venue": "",
      "featured-product": "yes",
      "publishedDate": "No Date",
      "category": ""
    }]
  }
};

describe.skip('Display results based on ajax call', function () {

  beforeEach(function () {
    $(html).appendTo(document.body);
    sinon.spy(snro.ajaxWrapper, 'getXhrObj');
  });

  afterEach(function () {
    snro.ajaxWrapper.getXhrObj.restore();
    $('.js-listing-wrapper').remove();
  });

  it('Display 5 results to user', function () {
    snro.listingwrapperCmp.renderResults(data);
    var listingWrapper = $('js-listing-wrapper');
    var resultCount = $('js-current-count');
    expect(listingWrapper.find('li')).to.have.length.equal(5);
    expect(resultCount.text()).to.contain('5');
  });

  it('Display no results found', function () {
    snro.listingwrapperCmp.renderResults(emptyResult);
    var resultCount = $('js-current-count');
    expect(resultCount.text()).to.contain('0');
  });

});
