use(function() {
 var data = {
  "homePagePath": "",
  "logoPath": "/content/dam/roche/roche-logo.svg",
  "logoAltText": "Roche Logo Alt Text",
  "countryCode": "Country Code",
  "countrySelectorPath": "/content/dam/roche/pharma",
  "isPersonaDisabled": "false",
  "persona": "Persona ",
  "personaSelectorPath": "/content/dam/roche/pharma",
  "searchUrl":"http://sp10015dbf.guided.ss-omtrdc.net",
  "searchResultPath":"",
  "searchLimit":"20",
  "headerLinksList": [{
      "pageName": "Innovations",
      "pagePath": "",
      "pageSuffix": ""
    },
    {
      "pageName": "Products",
      "pagePath": "",
      "pageSuffix": ""
    }
  ]
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});