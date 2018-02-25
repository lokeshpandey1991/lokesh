use(function() {
 var data = {
  "notificationTitle": "404 Page Not Found",
  "notificationDescription": "We are sorry we could not find the page your looking for. Lets get you back on track.",
   "links": [
    {
      "linkText": "Home",
      "linkURL" : "http://dev-roche-pharma-customerportal.com"
    },
     {
      "linkText": "Innovations",
       "linkURL" : "http://dev-roche-pharma-customerportal.com"
    },
    {
      "linkText": "Browse By solution",
       "linkURL" : "http://dev-roche-pharma-customerportal.com"
    },
    {
      "linkText": "Browse By Health Topics",
       "linkURL" : "http://dev-roche-pharma-customerportal.com"
    },
    {
      "linkText": "Search",
       "linkURL" : "http://dev-roche-pharma-customerportal.com"
    }


  ]
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});