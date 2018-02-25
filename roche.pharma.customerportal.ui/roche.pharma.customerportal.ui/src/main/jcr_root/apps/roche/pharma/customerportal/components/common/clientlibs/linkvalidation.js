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
 * Validation for Granite Touch UI multifield component.
 *
 * Additional properties for granite/ui/components/foundation/form/multifield
 * are:
 *
 * {Long}min
 * The minimum number of multifield items
 * No default
 *
 * {Long}max
 * The maximum number of multifield items
 * No default
 *
 * During usage, the multifield's add button will disable if the max is reached.
 * Upon dialog submission, the size is checked against the min and max fields
 * thereby rendering min as a 'required' field validation.
 *
 *  <myMultifield
 *       jcr:primaryType="nt:unstructured"
 *       sling:resourceType="granite/ui/components/foundation/form/multifield"
 *       fieldLabel="My multifield"
 *       min="{Long}2"
 *       max="{Long}5">
 *       <field
 *           jcr:primaryType="nt:unstructured"
 *           sling:resourceType="granite/ui/components/foundation/form/textfield"
 *           name="./myTextfield" />
 *   </myMultifield>
 */
$(document).on("dialog-ready", function () {
	$(".cq-dialog-submit").click(function () {

		var labelValue = $('.ctaLabelValidation').val();
		var linkValue = $('.ctaLinkValidation .js-coral-pathbrowser-input').val();
		var message;
        var fui = $(window).adaptTo("foundation-ui"),
		options = [{
				id: "ok",
				text: "OK",
				primary: true
			}
		];
		if (isEmpty(labelValue) && !isEmpty(linkValue)) {
			message = Granite.I18n.get('Please configure CTA label value');
            fui.prompt("Error", message, "default", options);
			$('.ctaLabelValidation').attr("aria-invalid", "true").toggleClass("is-invalid", true);
			return false;
		} else if (!isEmpty(labelValue) && isEmpty(linkValue)) {
			message = Granite.I18n.get('Please configure CTA link value');
            fui.prompt("Error", message, "default", options);
			$('.ctaLinkValidation .js-coral-pathbrowser-input').attr("aria-invalid", "true").toggleClass("is-invalid", true);
			return false;
		}

        function isEmpty(value){
  				return (value == null || value === '');
		}

	});
});

