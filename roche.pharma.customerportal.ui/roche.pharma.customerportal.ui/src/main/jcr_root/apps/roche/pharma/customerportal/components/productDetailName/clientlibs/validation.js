(function (document, $, ns) {
    "use strict";
 
    $(document).on("click", ".cq-dialog-submit", function (e) {

        var $form = $(this).closest("form.foundation-form"),
            productName = $form.find("[name='./productName']").val(),
               message, clazz = "coral-Button ";
		if (productName != undefined) {
            if(productName.trim() == "") {
                e.stopPropagation();
                e.preventDefault();
				 $form.find("[name='./productName']").attr("aria-invalid", "true").toggleClass("is-invalid", true);
                    ns.ui.helpers.prompt({
                    title: Granite.I18n.get("Invalid Input"),
                    message: "Please Enter a valid Product Name",
                    actions: [{
                        id: "CANCEL",
                        text: "CANCEL",
                        className: "coral-Button"
                    }],
                callback: function (actionId) {
                    if (actionId === "CANCEL") {
                    }
                }
            });
     
        	}
		}
    });
})(document, Granite.$, Granite.author);