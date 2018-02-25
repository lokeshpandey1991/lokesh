(function ($, $document) {

	var CORAL_RTE = ".coral-RichText",

	fieldErrorEl = $("<span class='coral-Form-fielderror coral-Icon coral-Icon--alert coral-Icon--sizeS'" +
			"data-init='quicktip' data-quicktip-type='error' />");

	foundationReg = $(window).adaptTo("foundation-registry");
	$(document).on("foundation-contentloaded", function (e) {

		$(CORAL_RTE).after("<input type=text style='display:none'/>");

		$(CORAL_RTE).on("input", function () {
			var $invisibleText = $(this).nextAll("input:text").val($(this).text().trim());

			$invisibleText.checkValidity();

		})
	});

	foundationReg.register("foundation.validation.validator", {
		selector: ".richtext-container > input:text",
		validate: function (el) {

			var $form = $(el).closest("form.foundation-form");
			var $hidden = $form.find("input[type=hidden].coral-Textfield");

			isRequired = $hidden.attr("required") === true ||
				$hidden.attr("aria-required") === "true";

			var mltifileldRte = $hidden.attr("data-requiredrte");

			var $check = $hidden.closest("coral-Form-field coral-Textfield");
			if (isRequired && _.isEmpty($(".coral-RichText").children("p").text())) {
				return $(el).message("validation.required") || "Please fill out this field";
			} else if (mltifileldRte && _.isEmpty($(".coral-RichText-editable").text())) {
				return $(el).message("validation.required") || "Please fill out this field";
			} else {
				var $hid = $(el).removeAttr("aria-invalid").removeClass("is-invalid").nextAll(".coral-Form-fielderror");
				$hid.tooltip("hide").remove();
				return null;
			}
		},
		show: function (el, message, ctx) {

			this.clear(el);
			fieldErrorEl.clone()
			.attr("data-quicktip-content", message)
			.insertAfter($(el));
			$(el).attr("aria-invalid", "true").toggleClass("is-invalid", true);
		},
		clear: function (el) {
			var $hid = $(el).removeAttr("aria-invalid").removeClass("is-invalid").nextAll(".coral-Form-fielderror");
			$hid.tooltip("hide").remove();

		}

	});
}
	(jQuery));
