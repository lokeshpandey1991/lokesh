use(function() {
 var data = {
  "registrationstatus": [
    "Canada-IVD", "CE-IVD", "US-IVD"
  ]
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});