use(function() {
 var data = {
  "notificationTitle": "Big Page level Error -",
  "notificationDescription": "Here is what you need to do",
  "notificationCTAText": "Act now",
  "notificationCTALink": "http://dev-roche-pharma-customerportal.com",
  "notificationType" : "alert"
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});