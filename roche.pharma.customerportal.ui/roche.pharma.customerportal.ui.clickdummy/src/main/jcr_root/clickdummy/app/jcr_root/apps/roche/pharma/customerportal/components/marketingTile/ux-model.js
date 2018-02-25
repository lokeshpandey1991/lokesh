use(function() {
 var data = {
 "headline": "Marketing Tile",
 "articleTitle":"Marketing Subtile",
 "articleSummary":"<p>Vestibulum enim adipiscing massa morbi pretium malesuada id in magnis vestibulum mus nisl adipiscing a sociosqu vestibulum mus parturient auctor a id ac porta ultrices platea quam. </p>",
 "ctaLabel": "Call to Action",
 "ctaLink": "#",
 "fileReference": "/etc/designs/roche/customerportal/clientlibs/roche.global.publish/images/marketing-tiles.png",
 "altText":"abc", 
 "variationType": "",
 "imagePosition" : ""
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});