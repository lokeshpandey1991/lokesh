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
(function (ns, window, document, $, Granite, undefined) {
	
	
	$.validator.register({
		selector: '.coral-Textfield',
		validate: function (el) {
			var $field,
			value,
			min,
			pattern,
			error,
			max;

			$field = el.closest(".coral-Textfield");
			var message;
			if (el.val() !== undefined) {
				value = parseInt(el.val().length, 10);
			}
			min = $field.data('min');
			max = $field.data('max');
			error = $field.data('error');
			pattern = $field.data('pattern');

			if (value < min) {
				message = Granite.I18n.get('The field must contain {0} or more items.', min);
			} else if (value > max) {
				message = Granite.I18n.get('The field must contain {0} or less items.', max);
			} else if (!(el.val()).match(pattern)) {
				message = error;
			}
			if (message) {
				$(el).attr("aria-invalid", "true").toggleClass("is-invalid", true);
				return message;
			} else {
				el.setCustomValidity(null);
				el.updateErrorUI();
			}
		}
	});

})(window.aemTouchUIValidation = window.aemTouchUIValidation || {}, window, document, Granite.$, Granite);
