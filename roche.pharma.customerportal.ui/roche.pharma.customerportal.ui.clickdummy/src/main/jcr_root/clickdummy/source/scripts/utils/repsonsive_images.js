var responsiveImgInfo = (function()
{

	var addCSSRule = function (sheet, selector, rules, index) 
	{
		if  ("insertRule" in sheet) sheet.insertRule(selector + "{" + rules + "}", index);
		else if("addRule" in sheet) sheet.addRule(selector, rules, index);
		
	},
	className = "responsive-img-info",
	infoStyle = [
		"padding: 10px 15px",
		"position: relative",
		"z-index 100000",
		"display: block",
		"background: rgba(249, 249, 251, 0.64)",
		"z-index: 99999",
		"font-size: 12px",
		"line-height: 1.4",
		"font-family: Tahoma",
		"color: black",
		"overflow: auto",
		"text-align: left"
	],
	errorStyle = [
		"background-color: rgba(204, 126, 126, 0.93)"
	],
	setInfo = function()
	{
		var $images = $("picture img[srcset]");
		var time    = 0;

		addCSSRule(document.styleSheets[0], "." + className + "__error", errorStyle.join(";"));
		addCSSRule(document.styleSheets[0], "." + className, 			  infoStyle.join(";"));

		$images.each(function()
		{
			$(this).on("load", function() 
			{
				window.setTimeout(function()
				{
					time          = 300;
					var image     = this;
					var $image    = $(this);
					var $picture  = $image.parents("picture");
					var $prev     = $picture.prev();				
					var $info     = null;
					var srcPath   = image.currentSrc.split("/");
					var curSize   = srcPath[srcPath.length -2];
					curSize       = curSize.indexOf("_") == -1 ? "full" : curSize;

					if ($prev.hasClass("responsive-img-info")) 
						  $info = $prev;
					else  $info = $("<div />" ).addClass(className).insertBefore($picture);

					$info.html([
						"Variants: "     + $picture.find("source").length,
						"Format:   "     + curSize,
						"Size:     "     + image.width        + " x " + image.height,
						"Natural Size: " + image.naturalWidth + " x " + image.naturalHeight
					].join("<br>"));

					if (image.naturalWidth < image.width || image.naturHeight < image.height) 
						  $info.addClass   (className + "__error");
					else  $info.removeClass(className + "__error");
				}.bind(this), time); // disable size caching
			}).trigger("load");
		});



	}

	return {
		"enable": function()
		{
			setInfo();
		}
	}
}());
