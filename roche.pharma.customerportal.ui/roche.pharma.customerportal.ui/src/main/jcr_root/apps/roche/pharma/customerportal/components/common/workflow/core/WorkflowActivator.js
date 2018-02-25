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

    var startWorkflowRel = '.cq-workflow-start-modal';
    var workflowModelsRel = startWorkflowRel + ' .cq-common-admin-timeline-toolbar-actions-workflow-select';
    var startSubmitButtonRel = startWorkflowRel + ' .start-workflow-activator-start';
    var startSubmitButtonRelPcapNumber = startWorkflowRel + ' .coral-Form-field-pcapnumber';

    // Listen for the tap on the 'start workflow' activator and show the modal.
    channel.on("click", ".start-workflow-activator", function(event) {
        // Reset the dialog values
        if ($(workflowModelsRel).length > 0) {
            $(workflowModelsRel).data('select').setValue('');
            $(startWorkflowRel).find('input[name=workflowTitle]').val('');
        }
        $(startSubmitButtonRel).prop('disabled', true);

        startWorkflowModal.show();
    });


    // Enable or disable the 'Start Workflow' button based on workflow and pcap number is not entered on model selected.
    $(document).on("foundation-contentloaded", function () {
        $(workflowModelsRel).on('selected' + startWorkflowRel, function (e) {
            if($(startWorkflowRel).find('input[name=pcapNumber]') == undefined){
            $(startSubmitButtonRel).prop('disabled', (e.selected.length == 0));
            }else{
                if($(startWorkflowRel).find('input[name=pcapNumber]').val() == ""){
					$(startSubmitButtonRel).prop('disabled', true);
                }else{
						$(startSubmitButtonRel).prop('disabled', e.selected.length == 0);
                }

            }
        });
        $(startSubmitButtonRelPcapNumber).on('change' + startWorkflowRel, function (e) {
            if($(startWorkflowRel).find('input[name=pcapNumber]') == undefined){
                  $(startSubmitButtonRel).prop('disabled', $('#workflowmodel :selected').val() == "");
            }else{
                if($(startWorkflowRel).find('input[name=pcapNumber]').val() == ""){
					$(startSubmitButtonRel).prop('disabled', true);
                }else{
						$(startSubmitButtonRel).prop('disabled', $('#workflowmodel :selected').val() == "");
                }

            }
        });
    });

    var startWorkflowModal = new CUI.Modal({ element:startWorkflowRel, visible: false });

}(jQuery, Granite.author, jQuery(document), this));
