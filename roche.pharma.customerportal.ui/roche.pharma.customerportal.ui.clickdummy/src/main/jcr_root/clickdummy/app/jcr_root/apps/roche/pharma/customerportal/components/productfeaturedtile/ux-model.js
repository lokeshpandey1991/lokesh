use(function() {
 var data = {
  "query": {
    "user-query": "",
    "lower-results": 1,
    "upper-results": 1,
    "total-results": 1
  },
  "facets": [{
       "productName": ["Cobas sample221b"]
     },
     {
       "productId": ["ROCH1234"]
     },
     {
  }],
  "results": {
    "products" : [{
       "title": "Cobas® 6800/8800 Systems - Molecular",
       "url": "/content/roche/pharma/customerportal/us",
       "category": "Molecular",
       "image": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/product-4_bg.png",
       "altText": "Cobas 6800/8800",
       "productType" : "",
       "page-type":"",
       "description": "",
       "eventStartDate":"",
       "eventEndDate": "",
       "eventFormatDate":"",
       "venue": ""
      },
      {
       "title": "Cobas®8800 Modular Analyser Series",
       "url": "/content/roche/pharma/customerportal/us",
       "category": "Molecular",
       "image": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/product-6_bg.png",
       "altText": "Cobas 8800",
       "productType" : "",
       "page-type":"",
       "description": "",
       "eventStartDate":"",
       "eventEndDate": "",
       "eventFormatDate":"",
       "venue": ""
      },
      {
       "title": "Cobas®Liat PCR System - Molecular",
       "url": "/content/roche/pharma/customerportal/us",
       "category": "Molecular",
       "image": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/product-5_bg.png",
       "altText": "Cobas 8800",
       "productType" : "",
       "page-type":"",
       "description": "",
       "eventStartDate":"",
       "eventEndDate": "",
       "eventFormatDate":"",
       "venue": ""
      }
    ]
    }
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});