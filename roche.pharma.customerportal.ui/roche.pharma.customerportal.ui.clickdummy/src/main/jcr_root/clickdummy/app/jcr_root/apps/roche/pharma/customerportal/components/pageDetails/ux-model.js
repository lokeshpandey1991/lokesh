use(function() {
 var data = {
  "titleOne": "rver (or inferred from the filename, or any other usual method) MUST be the only consideration, and the 'mediaType' property of the link MUST NOT be",
  "pageDescriptionOne": "<p>When choosing how to interpret data, the type information provided by the server (or inferred from the filename, or any other usual method) MUST be the only consideration, and the 'mediaType' property of the link MUST NOT be used. User agents MAY use this information to determine how they represent the link or where to display it (for example hover-text, opening in a new tab). If user agents decide to pass the link to an external program, they SHOULD first verify that the data is of a type that would normally be passed to that external program. <br></p> ",
  "twoColumnView": true,
  "titleTwo": "Sub heading",
  "pageDescriptionTwo": "<p>If present, this property indicates the media type format the client should use for the request payload described by <a>'submissionSchema'</a> submissionSchema.<br></p>",
  "label": "Label",
  "target": "_self",
  "eventPage": false,
  "url": "http://google.com",
  "titleThree" : "Roche at the NSH Symposium Roche at the NSH Symposium",
  "location": "Author Name",
  "publishDate": "January 23, 2017 to January 23, 2017",
  "viewType":"event",
  "isTwoColLayout":true,
    "fileReference": "/content/dam/roche/diagnostic/products/11/04/58/110458.png",
    "altText": "cobas® HPV Test Global"

}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});