use(function() {
 var data = {
  "sortList": [
    {
      "searchLabel": "Relevance",
      "searchValue": "relevance",
      "searchType": "all"
    },
    {
      "searchLabel": "Name A-Z",
      "searchValue": "title",
      "searchType": "all"
    },
    {
      "searchLabel": "Name Z-A",
      "searchValue": "titledesc",
      "searchType": "all"
    },
    {
      "searchLabel": "Newest",
      "searchValue": "publishedDate",
      "searchType": "more"
    },
    {
      "searchLabel": "Oldest",
      "searchValue": "publishedDatedesc",
      "searchType": "more"
    }
  ],
  "defaultOption" : {
    "Product": "title",
    "More": "Oldest"
  }
}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});