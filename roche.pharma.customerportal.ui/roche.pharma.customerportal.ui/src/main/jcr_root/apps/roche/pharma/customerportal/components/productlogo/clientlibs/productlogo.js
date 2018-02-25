updateProductLogo=function(object){
	var logoName=object.dataset.logoname;
    var logoDescription=object.dataset.logodescription;
    var logoReference=object.dataset.logoreference;
    var logoStatus=object.dataset.logostatus;
     var url = "/bin/Customerportal/productlogoupdateservlet.html?logoName="+logoName+"&logoDescription="+logoDescription+"&logoReference="+logoReference+"&logoStatus="+logoStatus+"&action=update";
      var options = {
        url: url,
        type: 'GET',
        dataType: 'html'
      };

    snro.ajaxWrapper.getXhrObj(options).done(function (data) {
       alert(data);

      }).fail(function (err) {
        alert(err);
        snro.commonUtils.log(err);
      });

}


getPages=function(object){

    var logoName=object.dataset.logoname;
	var url = "/bin/Customerportal/productlogoupdateservlet.html?logoName="+logoName+"&action=page";
     var options = {
        url: url,
        type: 'GET',
        dataType: 'html'
      };

    snro.ajaxWrapper.getXhrObj(options).done(function (data) {
         object.parentElement.parentElement.lastElementChild.innerHTML=data;

      }).fail(function (err) {
        alert(err);
        snro.commonUtils.log(err);
      });

}


