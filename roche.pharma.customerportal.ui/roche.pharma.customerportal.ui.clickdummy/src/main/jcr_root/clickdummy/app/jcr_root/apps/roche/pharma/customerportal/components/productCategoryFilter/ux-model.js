use(function() {
 var data = {
  "categoryFilter": [{
    "filterTitle": "Products",
    "filterTag": "product"
  }, {
    "filterTitle": "Health Topic",
    "filterTag": "health"
  }]
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});