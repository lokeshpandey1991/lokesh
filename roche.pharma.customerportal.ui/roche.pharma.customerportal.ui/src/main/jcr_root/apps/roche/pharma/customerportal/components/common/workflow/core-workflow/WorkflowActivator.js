/*
 * ADOBE CONFIDENTIAL
 *
 * Copyright 2014 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */
/*
  This script handles the activation when starting a workflow and seeing an active workflow's information.
  It also handles any resulting modals and their behavior.
 */
(function($, ns, channel, window, undefined) {

    var ui = $(window).adaptTo("foundation-ui");
    var startWorkflowRel = '.cq-common-createworkflowdialog-form';

    var workflowModelsPayload = '.coral3-ColumnView-column-content .is-selected';
    var workflowStartButtonRel = ' .cq-siteadmin-admin-actions-create-activator';
     var workflowModelsRel = ' .cq-custom-toolbar-actions-workflow-select .coral3-Select-button';
    var startSubmitButtonRel = ' .cq-common-createworkflowdialog-submit';
    var startSubmitButtonRelPcapNumber = startWorkflowRel + ' .coral-Form-field-pcapnumber';

          if ($(workflowModelsPayload).length > 0) {
              $(startSubmitButtonRel).prop('disabled', true);
             $(startSubmitButtonRelPcapNumber).prop('type', "hidden");

        }


    // Enable or disable the 'Start Workflow' button based on workflow and pcap number is not entered on model selected.
    $(document).on("foundation-contentloaded", function () {
         $(workflowModelsRel).on('click'+ startWorkflowRel, function (e) {
              if($(workflowModelsPayload).data("foundation-collection-item-id").indexOf("/content/customerportal") != -1){
				 $(startSubmitButtonRelPcapNumber).prop('type', "text");
                $(startSubmitButtonRel).prop('disabled', true);
            }else{

				 $(startSubmitButtonRelPcapNumber).prop('type', "hidden");
                $(startSubmitButtonRel).prop('disabled', false);
            }
        });

        $(startSubmitButtonRelPcapNumber).on('change', function (e) {
            if($(startWorkflowRel).find('input[name=pcapNumber]') == undefined){
                  $(startSubmitButtonRel).prop('disabled', $(startWorkflowRel).find('input[name=model]').val() == "");
            }else{
                if($(startWorkflowRel).find('input[name=pcapNumber]').val() == ""){
					$(startSubmitButtonRel).prop('disabled', true);
                }else{
						$(startSubmitButtonRel).prop('disabled', $(startWorkflowRel).find('input[name=model]').val() == "");
                }

            }
        });
    });

    var startWorkflowModal = new CUI.Modal({ element:startWorkflowRel, visible: false });

}(jQuery, Granite.author, jQuery(document), this));
