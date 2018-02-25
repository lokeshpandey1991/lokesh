use(function() {
 var data = {
  "mediaList": [
  {
    "imagePath": "/content/dam/geometrixx/carouselmachine_01.png",
    "altText": "Image 1",
    "imageHeading": "Module1: Sample Supply",
    "imageText": "Samples are placed onboard the sample supply module using standard 5-position racks and supporting rack trays. Each sample is then moved automatically into the transfer module. Sample racks hold 5 primary or secondary sample tubes."
  },
  {
    "imagePath": "/content/dam/geometrixx/carouselmachine_02.png",
    "altText": "Image 2",    
    "imageHeading": "heading2",
    "imageText": "sdfsdf2"
  },
  {
    "imagePath": "/content/dam/geometrixx/carouselmachine_03.png",
    "altText": "Image 3",    
    "imageHeading": "heading3",
    "imageText": "sdfsdf3"
  },
  {
    "imagePath": "/content/dam/geometrixx/carouselmachine_04.png",
    "altText": "Image 4",       
    "imageHeading": "heading4",
    "imageText": "sdfsdf4"
  },
  {
    "imagePath": "/content/dam/geometrixx/carouselmachine_04.png",
    "altText": "Image 4",    
    "imageHeading": "heading4",
    "imageText": "sdfsdf4"
  },
  {
    "imagePath": "/content/dam/geometrixx/carouselmachine_04.png",
    "altText": "Image 4",    
    "imageHeading": "heading4",
    "imageText": "sdfsdf4"
  },{
    "imagePath": "/content/dam/geometrixx/carouselmachine_04.png",
    "altText": "Image 4",   
    "imageHeading": "heading4",
    "imageText": "sdfsdf4"
  }],
  "ctaDescription": "Have any questions?",
  "ctaText": "Contact us"
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});