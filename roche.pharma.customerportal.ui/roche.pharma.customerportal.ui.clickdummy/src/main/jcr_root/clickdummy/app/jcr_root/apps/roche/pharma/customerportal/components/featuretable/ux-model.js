use(function() {
 var data = {
  "tableHeading": "cobas® HPV Test Global",
  "tableDescription": "This is product spec table",
  "tableDisclaimer": "This is product spec desclaimer",
  "table": "<table><tbody><tr><th scope='col'> Result</th><th scope='col'> cobas® HPV Test</th><th scope='col'> Test</th><th scope='col'> Test</th></tr><tr><th scope='row'>Result</th><td>test 1</td><td>test 2</td><td>test 3</td></tr><tr><th scope='row'>Result</th><td>test 1</td><td>test 2</td><td>test 3</td></tr><tr><th scope='row'>Result</th><td>test 1</td><td>test 2</td><td>test 3</td></tr></tbody></table"
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});