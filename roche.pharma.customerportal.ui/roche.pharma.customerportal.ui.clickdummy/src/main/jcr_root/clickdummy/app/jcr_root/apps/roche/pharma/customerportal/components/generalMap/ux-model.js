use(function() {
 var data = {
  "heading": "General Contacts",
  "address": "<p>Biochemistry Building <br> 9115 hague road<br>PO BOX 50457<br>Indianapolis,IN 12345-54321 </p>",
  "timeDetailListing" : [{
  		"timeDetails": "Staurday-Sunday:10:00 a.m.-6:00 p.m."
  	},{
  		"timeDetails": "Staurday-Sunday:10:00 a.m.-6:00 p.m."
  }],
	"phoneNumber" : "1-800-428-5076",
	"email" : "event@roche.com",
	"ctaLabel": "View Website",
  "ctaLink": "http://customerportal-Customerportal-dev.roche.com/us/en/home.html",
  "latitude":"28.510563299999998",
  "longitude":"77.044"
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});