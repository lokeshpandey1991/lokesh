use(function() {
 var data = {
  "query": {
    "user-query": "",
    "lower-results": 1,
    "upper-results": 1,
    "total-results": 1
  },
  
  "facets": [{
      "productName": [
         "Cobas sample221b"
        ]
    },
    {
      "productId": [
         "ROCH1234"
        ]
    }],

  "results": {
    "parameters": {
      "variationType": ""
    },

    "data": [{
      "title": "American Association for Clinical Chemistry",
      "subTitle": "Organization Name",
      "url": "http://35.154.101.110/content/roche/pharma/customerportal/gb/en/home/roche54353.html",
      "page-type": "event",
      "description": "Connect with global leaders in clinical chemistry, molecular customerportal, mass spectrometry and translational medicine. You’ll learn how to position your laboratory at the forefront of the field.",
      "image": "ROCH1234",
      "altText": "",
      "productName": "",
      "eventDateFormat": "Aug 24 2017",
      "eventStartDate": "Aug 24",
      "eventEndDate": " 30 2017",
      "venue": "JW Marriot, San Diego, California"
    }]
  }
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});