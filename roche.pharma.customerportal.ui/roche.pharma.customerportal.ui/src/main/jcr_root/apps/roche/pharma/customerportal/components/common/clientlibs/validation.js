(function(document, $, ns) {
    "use strict";

    $(document).on("click", ".cq-dialog-submit", function(e) {

        var $form = $(this).closest("form.foundation-form");

        var fileReference = $form.find("[name='./fileReference']").val();
        var altText = $form.find("[name='./altText']").val();
        if($form.find("[name='./view']").val()!=undefined){
            $form.find("[name='./view']").removeAttr("disabled");
            var textPsotion=$form.find("[name='./textPosition']").val();
            var variationType=$form.find("[name='./variationType']").val();
            var view=variationType+textPsotion;
            $form.find("[name='./view']").attr("value", view);
        }
        if (fileReference != undefined && altText != undefined) {
            if (($("span.is-filled").html() != undefined) && (altText == null || altText.trim() == "")) {
                                                                e.stopPropagation();
                e.preventDefault();
                  $form.find("[name='./altText']").attr("aria-invalid", "true").toggleClass("is-invalid", true);
                ns.ui.helpers.prompt({
                    title: Granite.I18n.get("Invalid Input"),
                    message: "Please enter alternate text for the image",
                    actions: [{
                        id: "CANCEL",
                        text: "CANCEL",
                       className: "coral-Button"
                    }],
                    callback: function(actionId) {
                        if (actionId === "CANCEL") {}
                    }
                });

            } 
        }
    });
})(document, Granite.$, Granite.author);
