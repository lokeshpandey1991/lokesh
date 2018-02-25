use(function() {
 var data = {
  "categoryFilter": [{
    "filterTag": "products",
    "filterTitle": "Products"
  }, {
    "filterTag": "documentation",
    "filterTitle": "Documentation"
  }, {
    "filterTag": "more",
    "filterTitle": "More"
  }]
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});