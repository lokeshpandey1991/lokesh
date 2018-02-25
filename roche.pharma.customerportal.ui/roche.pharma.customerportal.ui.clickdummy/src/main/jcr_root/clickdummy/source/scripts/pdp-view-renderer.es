/** to render pdp components with data */
$(function(){

	let productSSI = document.getElementsByClassName('productSSI'),
	pdpData, pdpComponents;

	let decodeHtml = (html) => {
		var txt = document.createElement("textarea");
	    txt.innerHTML = html;
	    return txt.value;
	},

	formatSpecificationData = (data) => {
		let dataArr = [];
		dataArr = $.map(data, function(value, key) {
			if($.trim(value)){
				return {'key': key, 'value':value};
			}
		});
		if(dataArr.length <= 10){
			$('.js-pdp-specs-see-all').hide();
		}else{
			dataArr = dataArr.slice(0, 10);
		}
		return dataArr;
	};

	if(productSSI.length){
		try{
			pdpData =  JSON.parse(productSSI[0].innerHTML);
			pdpData = pdpData.results[0];
		}catch(e){
			console.log(e);
		}
		
		if(typeof pdpData !== 'undefined'){
			pdpComponents = {
			'systemSpecification' : {
				'templateName':'systemSpecification',
				'templateSelector':'js-system-specification-dtl'
				},
				'featureAndBenefitsText' : {
				'templateSelector':'js-pdp-feature-benefits'
				},
				'intendedUseText' : {
				'templateSelector':'js-pdp-intended-use'
				} 
			};

			for (let k in pdpComponents) {
				if(pdpData[k]){
				   	let data = pdpData[k], html,
					templateSelector = pdpComponents[k].templateSelector;

					if(k === 'systemSpecification'){
						data = formatSpecificationData(data);
					}

					if(pdpComponents[k].templateName){
						let templateName = pdpComponents[k].templateName,
						componentTemplate = roche.templates[templateName];
						html = componentTemplate(data);
					}else{
						html = decodeHtml(data);
					}

					document.getElementsByClassName(templateSelector)[0].innerHTML = html;							
				}
			}
		}
	}
});
  
