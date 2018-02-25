use(function() {
 var data = {
"headline": "Training Resources",
"relatedList": [{
"pageName": "InnovationsInnovations InnovationsInnovations InnovationsInnovations InnovationsInnovations InnovationsInnovationsInnovationsInnovations InnovationsInnovations InnovationsInnovations",
"pagePath": "https://www.google.com",
"pageSuffix": "",
"linkBehaviour": "_self",
"contentType":"sample"
},
{
"pageName": "Products",
"pagePath": "https://www.google.com",
"pageSuffix": "",
"linkBehaviour": "_blank",
"contentType":"sample"
}
]
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});