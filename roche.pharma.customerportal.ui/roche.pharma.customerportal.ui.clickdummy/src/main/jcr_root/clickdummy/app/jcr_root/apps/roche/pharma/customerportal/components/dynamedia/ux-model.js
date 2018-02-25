use(function() {
 var data = {
	"smallscreensize":"https://roche-staging-h.assetsadobe2.com/is/image/content/dam/geometrixx-outdoors/roche3.jpg?scl=3",
	"mediumscreensize":"https://roche-staging-h.assetsadobe2.com/is/image/content/dam/geometrixx-outdoors/roche3.jpg?scl=2",
	"largescreensize":"https://roche-staging-h.assetsadobe2.com/is/image/content/dam/geometrixx-outdoors/roche3.jpg?scl=1"	
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});