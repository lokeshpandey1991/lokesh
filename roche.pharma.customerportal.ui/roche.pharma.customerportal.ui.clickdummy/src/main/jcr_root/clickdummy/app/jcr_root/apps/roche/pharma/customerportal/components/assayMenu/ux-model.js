use(function() {
 var data = { 
  "title": "Assay Menu heading",
  "productId": "INS_2202",
  "productName": "Cobas123",
  "legends": [
  {
    "legendName": "New",
    "legendClass": "new"
  },
  {
    "legendName": "featured",
    "legendClass": "feature"
  }],
  "relatedAssaysMap": [
  {
    "categoryName": "Anemia",
    "assays": [
    {
      "productId": "20737941322",
      "assayTitle": "IronGen 2",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche54353",
      "legend": "feature"
    },
    {
      "productId": "11820176316",
      "assayTitle": "Tina-quant Ferttin",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/ROCHE54352",
      "legend": "new"
    }]
  },
  {
    "categoryName": "Beauty",
    "assays": [
    {
      "productId": "20737836324",
      "assayTitle": "ROCHE54351",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche543531",
      "legend": "new"
    },
    {
      "productId": "04810716190",
      "assayTitle": "ROCHE54350",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche5435311",
      "legend": "new"
    }]
  },
{
    "categoryName": "DAT",
    "assays": [
    {
      "productId": "20737836324",
      "assayTitle": "ROCHE54351",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche543531",
      "legend": "feature"
    },
    {
      "productId": "04810716190",
      "assayTitle": "ROCHE54350",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche5435311",
      "legend": "new"
    },{
      "productId": "20737836324",
      "assayTitle": "ROCHE54351",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche543531",
      "legend": "feature"
    },
    {
      "productId": "04810716190",
      "assayTitle": "ROCHE54350",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche5435311",
      "legend": "new"
    }]
  },{
    "categoryName": "Dibates",
    "assays": [
    {
      "productId": "20737836324",
      "assayTitle": "ROCHE54351",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche543531",
      "legend": "new"
    },
    {
      "productId": "04810716190",
      "assayTitle": "ROCHE54350",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche5435311",
      "legend": "new"
    }]
  },{
    "categoryName": "malaria",
    "assays": [
    {
      "productId": "20737836324",
      "assayTitle": "ROCHE54351",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche543531",
      "legend": "new"
    },
    {
      "productId": "04810716190",
      "assayTitle": "ROCHE54350",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche5435311",
      "legend": "new"
    }]
  },{
    "categoryName": "DAT",
    "assays": [
    {
      "productId": "20737836324",
      "assayTitle": "ROCHE54351",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche543531",
      "legend": "new"
    },
    {
      "productId": "04810716190",
      "assayTitle": "ROCHE54350",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche5435311",
      "legend": "new"
    }]
  },{
    "categoryName": "malDDaria",
    "assays": [
    {
      "productId": "20737836324",
      "assayTitle": "ROCHE54351",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche543531",
      "legend": "new"
    },
    {
      "productId": "04810716190",
      "assayTitle": "ROCHE54350",
      "assayUrl": "/content/customerportal/global-master-blueprint/en/home/product-category-page/roche5435311",
      "legend": "new"
    }]
  }]

}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});