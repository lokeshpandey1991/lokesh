use(function() {
 var data = {
  "productName": "cobas 8100 automated workflow series",
  "productSummary": "Designed for high throughput with minimal sample handling",
  "registrationStatus": "[Canada-IVD, CE-IVD, US-IVD]",
  "defaultTab": "tab4",
  "showTab1": "true",
  "showTab2": "true",
  "showTab3": "true",
  "showTab4" : "true"
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});