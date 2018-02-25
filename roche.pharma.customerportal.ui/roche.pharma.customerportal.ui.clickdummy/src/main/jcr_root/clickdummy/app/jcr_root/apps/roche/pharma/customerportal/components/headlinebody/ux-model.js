use(function() {
 var data = {
  "headlineType": "x-h3",
  "headlinePosition": "center",
  "headlineText": "The power of knowing",
  "authorName": "Author Name",
  "publishDate": "Jan 23, 2017",
  "socialMedia": "true",
  "searchUrl": "/content/roche/pharma/customerportal/us",
  "localTagTitles": "{summer, winter}",
  "bodyText": "<p><a href='https://52.19.118.133:5433/content/roche/pharma/customerportal/global/en/home.html'>Roche</a> plays a pioneering role in healthcare. As an innovator of products and services for the early detection, prevention, diagnosis and treatment of diseases, we contribute on a broad range of fronts to improving people's health and quality of life.<ul><li>One</li><li>Two</li></ul> Roche is providing the first products that are tailored to the needs of specific patient groups. <ol><li>One</li><li>Two</li></ol>Our mission today and tomorrow is to create added value in healthcare by focusing on our expertise in customerportal and pharmaceuticals.</p><p>Performed on blood, tissue or other patient samples, in vitro customerportal are a critical source of objective information for improved disease management and patient care. In modern healthcare, in vitro customerportal go far beyond simply telling a doctor whether a patient has a certain disease or not.</p>"
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});