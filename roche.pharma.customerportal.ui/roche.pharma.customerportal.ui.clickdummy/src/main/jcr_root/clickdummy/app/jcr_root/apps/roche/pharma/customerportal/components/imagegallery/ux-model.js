use(function() {
 var data = {
  "fileReference1": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/image-1.png",
  "altText1": "This is sample alt text",
  "fileReference2": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/image-2.png",
  "altText2": "This is sample alt text",
  "fileReference3": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/image-3.png",
  "altText3": "This is sample alt text",
  "fileReference4": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/image-4.png",
  "altText4": "This is sample alt text",
  "fileReference5": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/image-5.png",
  "altText5": "This is sample alt text",
  "fileReference6": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/image-6.png",
  "altText6": "This is sample alt text",
  "imageHeadline": "Red Blood Cells"
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});