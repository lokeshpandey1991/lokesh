CQ.wcm.SiteAdmin.startWorkflow = function() {
    var admin = this;
    var id = CQ.Util.createId("cq-workflowdialog");

var selections = this.getSelectedPages();
var showAdvanceDialog = false;
    var startWorkflowDialog;


    for (var i=0; i<selections.length; i++) {
        var selection = selections[i];
        var pagePath = selection.id;
		
		// Added a check for payload.
        if(pagePath.indexOf("/content/customerportal") != -1){
				showAdvanceDialog = true ;
           }
    }
    if(showAdvanceDialog){
			// Dialog for customerportal site.
        startWorkflowDialog = {
        "jcr:primaryType": "cq:Dialog",
        "title":CQ.I18n.getMessage("Start Workflow"),
        "id": id,
        "formUrl":"/etc/workflow/instances",
        "params": {
            "_charset_":"utf-8",
            "payloadType":"JCR_PATH"
        },
        "items": {
            "jcr:primaryType": "cq:Panel",
            "items": {
                "jcr:primaryType": "cq:WidgetCollection",
                "model": {
                    "xtype":"combo",
                    "name":"model",
                    "id": id + "-model",
                    "hiddenName":"model",
                    "fieldLabel":CQ.I18n.getMessage("Workflow"),
                    "displayField":"label",
                    "valueField":"wid",
                    "title":CQ.I18n.getMessage("Available Workflows"),
                    "selectOnFocus":true,
                    "triggerAction":"all",
                    "allowBlank":false,
                    "editable":false,
                    "store":new CQ.Ext.data.Store({
                        "proxy":new CQ.Ext.data.HttpProxy({
                            "url":"/libs/cq/workflow/content/console/workflows.json",
                            "method":"GET"
                        }),
                        "baseParams": { tags: 'wcm' },
                        "reader":new CQ.Ext.data.JsonReader(
                            {
                                "totalProperty":"results",
                                "root":"workflows"
                            },
                            [ {"name":"wid"}, {"name":"label"}, {"name": CQ.shared.XSS.getXSSPropertyName("label")} ]
                        )
                    }),
                    "tpl": new CQ.Ext.XTemplate(
                        '<tpl for=".">',
                        '<div class="x-combo-list-item">',
                        '{[CQ.I18n.getVarMessage(CQ.shared.XSS.getXSSTablePropertyValue(values, \"label\"))]}',
                        '</div>',
                        '</tpl>'
                    )
                },
                "comment": {
                    "jcr:primaryType": "cq:TextArea",
                    "fieldLabel":CQ.I18n.getMessage("Comment"),
                    "name":"startComment",
                    "height":200
                },
                "title": {
                    xtype: 'textfield',
                    name:'workflowTitle',
                    fieldLabel:CQ.I18n.getMessage('Workflow Title')
                },
    			"PCAP NUMBER": {
                    xtype: 'textfield',
                    name:'pcapNumber',
                   	"allowBlank":false,
                    fieldLabel:CQ.I18n.getMessage('Please enter PCAP number')
                }
            }
        },
        "okText":CQ.I18n.getMessage("Start")
    };
    }
    else{
	   // OOTB dialog.
        startWorkflowDialog = {
        "jcr:primaryType": "cq:Dialog",
        "title":CQ.I18n.getMessage("Start Workflow"),
        "id": id,
        "formUrl":"/etc/workflow/instances",
        "params": {
            "_charset_":"utf-8",
            "payloadType":"JCR_PATH"
        },
        "items": {
            "jcr:primaryType": "cq:Panel",
            "items": {
                "jcr:primaryType": "cq:WidgetCollection",
                "model": {
                    "xtype":"combo",
                    "name":"model",
                    "id": id + "-model",
                    "hiddenName":"model",
                    "fieldLabel":CQ.I18n.getMessage("Workflow"),
                    "displayField":"label",
                    "valueField":"wid",
                    "title":CQ.I18n.getMessage("Available Workflows"),
                    "selectOnFocus":true,
                    "triggerAction":"all",
                    "allowBlank":false,
                    "editable":false,
                    "store":new CQ.Ext.data.Store({
                        "proxy":new CQ.Ext.data.HttpProxy({
                            "url":"/libs/cq/workflow/content/console/workflows.json",
                            "method":"GET"
                        }),
                        "baseParams": { tags: 'wcm' },
                        "reader":new CQ.Ext.data.JsonReader(
                            {
                                "totalProperty":"results",
                                "root":"workflows"
                            },
                            [ {"name":"wid"}, {"name":"label"}, {"name": CQ.shared.XSS.getXSSPropertyName("label")} ]
                        )
                    }),
                    "tpl": new CQ.Ext.XTemplate(
                        '<tpl for=".">',
                        '<div class="x-combo-list-item">',
                        '{[CQ.I18n.getVarMessage(CQ.shared.XSS.getXSSTablePropertyValue(values, \"label\"))]}',
                        '</div>',
                        '</tpl>'
                    )
                },
                "comment": {
                    "jcr:primaryType": "cq:TextArea",
                    "fieldLabel":CQ.I18n.getMessage("Comment"),
                    "name":"startComment",
                    "height":200
                },
                "title": {
                    xtype: 'textfield',
                    name:'workflowTitle',
                    fieldLabel:CQ.I18n.getMessage('Workflow Title')
                }
            }
        },
        "okText":CQ.I18n.getMessage("Start")
    };
    }



    var dialog = CQ.WCM.getDialog(startWorkflowDialog);
    for (var i=0; i<selections.length; i++) {
        var selection = selections[i];
        var pagePath = selection.id;

        // check if page is already in workflow
        var url = CQ.HTTP.noCaching("/bin/workflow.json");
        url = CQ.HTTP.addParameter(url, "isInWorkflow", pagePath);
        url = CQ.HTTP.addParameter(url, "_charset_", "UTF-8");
        var response = CQ.HTTP.get(url);
        var isInWorkflow = false;
        if (CQ.HTTP.isOk(response)) {
            var data = CQ.Util.eval(response);
            isInWorkflow = data.status;
        }
        if (!isInWorkflow) {
            dialog.addHidden({ "payload":pagePath });
        } else {
            CQ.Ext.Msg.alert(CQ.I18n.getMessage("Info"), CQ.I18n.getMessage("Page is already subject to a workflow!"));
            return;
        }
    }
    dialog.on("beforesubmit", function(){admin.mask();});
    dialog.success = function(){admin.reloadPages();};
    dialog.failure = function(){
        admin.unmask();
        CQ.Ext.Msg.alert(CQ.I18n.getMessage("Error"),
            CQ.I18n.getMessage("Could not start workflow."));
    };
    dialog.show();
};

