use(function() {
 var data = {
  "productThumbnailsList":[
  {
    "fileReference": "/content/dam/customerportal/cobas.png",
    "altText": "a",
    "productTitle": "Here is what you need to do  need to do ",
    "tagsId": ["Image descritpion1","Image descritpion2","Image descritpion2","Image descritpion2"],
    "productPath" : "http://dev-roche-pharma-customerportal.com"
  },
  {
    "fileReference": "/content/dam/customerportal/cobas.png",
    "altText": "b",
    "productTitle": "Here is what you need to do",
    "tagsId": ["Image descritpion3","Image descritpion4"],
    "productPath" : "http://dev-roche-pharma-customerportal.com"
  }]
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});