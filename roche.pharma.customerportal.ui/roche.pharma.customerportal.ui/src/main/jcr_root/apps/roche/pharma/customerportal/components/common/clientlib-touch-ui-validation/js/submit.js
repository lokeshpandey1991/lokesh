/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * AEM Touch UI Validation for submit
 */

$(document).on("dialog-ready", function () {
	$(".cq-dialog-submit").click(function () {
		var field = $(".js-coral-Multifield-add").parent();
		var max = field.attr("data-max");
		var min = field.attr("data-min");
		var totalLinkCount = $(".js-coral-Multifield-add").prev('ol').children('li').length;

		var isInvalid = $(".is-invalid").length;
		var requiredRte = $(".richtext-container").children("input").data("requiredrte");
		var message;
		var fui = $(window).adaptTo("foundation-ui"),
		options = [{
				id: "ok",
				text: "OK",
				primary: true
			}
		];
		if (totalLinkCount > max) {
			message = Granite.I18n.get('The field must contain {0} or less items.', max);
		} else if (totalLinkCount < min) {
			message = Granite.I18n.get('The field must contain {0} or more items.', min);
		}
		 else if (requiredRte) {
			$.each($(".coral-RichText-editable"), function (index, value) {
				if (_.isEmpty($(value).text())) {
					message = "RTE is required field";
				}
			});
		}

        $.each($(".coral-FileUpload"), function (index, value) {

            var requireimage  = $(value).children(".coral-FileUpload-trigger").children("input").data("requireimage");

            var label  = $(value).prev("label").text();
            if(requireimage){
                if(!$(value).children(".cq-FileUpload-thumbnail").children(".cq-FileUpload-thumbnail-img").children("img").attr("src")){
                   message = label +" is required field";
                }
            }
			});
        if (message) {
			fui.prompt("Error", message, "default", options);
			return false;

		}

	});
});
