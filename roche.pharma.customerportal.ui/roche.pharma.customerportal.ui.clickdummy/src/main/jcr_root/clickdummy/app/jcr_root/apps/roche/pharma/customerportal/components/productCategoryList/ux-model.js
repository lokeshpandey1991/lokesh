use(function() {
 var data = {
  "filterTag":"healthTopic",
  "categoryListing": [{
    "categoryTitle": "Core Labs",
    "categoryDescription": "Lorem ipsum dolor sit amet, nullam pericula consectetuer. Animal nonumes at sit. Nec an graeco expetendis Nec an graeco expetendis Nec an graeco expetendis SampleText Nec an graeco expetendis SampleText graeco expetendis SampleText",
    "categoryURL": "http://www.roche.com/"

  }, {
    "categoryTitle": "Molecular customerportal",
    "categoryDescription": "Lorem ipsum dolor sit amet, nullam pericula consectetuer. Animal nonumes at sit. Nec an",
    "categoryURL": "http://www.roche.com/"
  },
    {
      "categoryTitle": "Point of Care",
      "categoryDescription": "Lorem ipsum dolor sit amet, ",
      "categoryURL": "http://www.roche.com/"
    }, {
      "categoryTitle": "Core Labs",
      "categoryDescription": "Lorem ipsum dolor sit amet, nullam pericula consectetuer. Animal nonumes at sit. Nec an graeco expetendis Nec an graeco expetendis SampleText Nec an graeco expetendis SampleText",
      "categoryURL": "http://www.roche.com/"

    }, {
      "categoryTitle": "Molecular customerportal",
      "categoryDescription": "Lorem ipsum dolor sit amet, nullam pericula consectetuer. Animal nonumes at sit. Nec an graeco expetendis Nec an graeco expetendis SampleText Nec an graeco expetendis SampleText",
      "categoryURL": ""
    }]
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});