use(function() {
 var data = {
  "title": "Assay Menu heading",
  "compareTitle": "Assay Compare Menu heading",
  "products": [
  {
    "productId": "INS_2202",
    "productName": "Cobas1",
    "productImage": "<imagePath>",
    "altText": "altText",
    "productPath": "</content/"
  },
  {
    "productId": "INS_2201",
    "productName": "Cobas2",
    "productImage": "<imagePath>",
    "altText": "altText",
    "productPath": "</content/"
  },
  {
    "productId": "INS_22023",
    "productName": "Cobas1",
    "productImage": "<imagePath>",
    "altText": "altText",
    "productPath": "</content/"
  },
  {
    "productId": "INS_2204",
    "productName": "Cobas2",
    "productImage": "<imagePath>",
    "altText": "altText",
    "productPath": "</content/"
  }]
}

;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});