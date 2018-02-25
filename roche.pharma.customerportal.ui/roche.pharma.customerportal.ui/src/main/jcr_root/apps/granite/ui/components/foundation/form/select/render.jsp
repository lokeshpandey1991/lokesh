<%--
  ADOBE CONFIDENTIAL

  Copyright 2013 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and may be covered by U.S. and Foreign Patents,
  patents in process, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@page session="false"
          import="java.text.Collator,
                  java.util.Collections,
                  java.util.Comparator,
                  java.util.Iterator,
                  java.util.List,
                  javax.servlet.jsp.JspWriter,
                  org.apache.commons.collections.IteratorUtils,
                  org.apache.commons.lang.StringUtils,
                  com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.ComponentHelper,
                  com.adobe.granite.ui.components.Tag" %><%--###
Select
======

.. granite:servercomponent:: /libs/granite/ui/components/foundation/form/select
   :supertype: /libs/granite/ui/components/foundation/form/field
   
   Select is a component to represent a concept of selection of some options.

   It extends :granite:servercomponent:`Field </libs/granite/ui/components/foundation/form/field>` component.

   It has the following content structure:

   .. gnd:gnd::

      [granite:FormSelect] > granite:FormField
      
      /**
       * The id attribute.
       */
      - id (String)

      /**
       * The class attribute. This is used to indicate the semantic relationship of the component similar to ``rel`` attribute.
       */
      - rel (String)

      /**
       * The class attribute.
       */
      - class (String)

      /**
       * The title attribute.
       */
      - title (String) i18n
      
      /**
       * The name that identifies the field when submitting the form.
       */
      - name (String)
      
      /**
       * The initial text to display when nothing is selected.
       */
      - emptyText (String) i18n
      
      /**
       * Indicates if the field is in disabled state.
       */
      - disabled (Boolean)
      
      /**
       * Indicates if the field is mandatory to be filled.
       */
      - required (Boolean)
      
      /**
       * The name of the validator to be applied. E.g. ``foundation.jcr.name``.
       * See :doc:`validation </jcr_root/libs/granite/ui/components/foundation/clientlibs/foundation/js/validation/index>` in Granite UI.
       */
      - validation (String) multiple
      
      /**
       * Indicates if the user is able to select multiple selections.
       */
      - multiple (Boolean)
      
      /**
       * ``true`` to translate the options, ``false`` otherwise.
       */
      - translateOptions (Boolean) = true
      
      /**
       * ``true`` to sort the options based on the text, ``false`` otherwise.
       *
       * It is assumed that the options don't contain option group.
       */
      - ordered (Boolean) = false
      
      /** 
       * ``true`` to also add an empty option; ``false`` otherwise.
       *
       * Empty option is an option having both value and text equal to empty string.
       */
      - emptyOption (Boolean) = false
      
      /**
       * ``true`` to generate the `SlingPostServlet @Delete <http://sling.apache.org/documentation/bundles/manipulating-content-the-slingpostservlet-servlets-post.html#delete>`_ hidden input based on the field name.
       */
      - deleteHint (Boolean) = true

   The options of the select are specified using :ref:`ItemDataSource <ItemDataSource>`.      
      
   Each option has the following structure:
   
   .. gnd:gnd::

      [granite:FormSelectItem]
      
      /**
       * The id attribute.
       */
      - id (String)

      /**
       * The class attribute. This is used to indicate the semantic relationship of the component similar to ``rel`` attribute.
       */
      - rel (String)

      /**
       * The class attribute.
       */
      - class (String)
      
      /**
       * The title attribute.
       */
      - title (String) i18n
      
      /**
       * The value of the option.
       */
      - value (StringEL) mandatory
      
      /**
       * Indicates if the option is in disabled state.
       */
      - disabled (Boolean)
      
      /**
       * ``true`` to pre-select this option, ``false`` otherwise.
       */
      - selected (Boolean)
      
      /**
       * The text of the option.
       */
      - text (String) i18n
      
   Example::
   
      + myselect
        - sling:resourceType = "granite/ui/components/foundation/form/select"
        - emptyText = "Select"
        - name = "myselect"
        + items
          + option1
            - text = "op1"
          + option2
            - text = "op2"
###--%><%

    Config cfg = cmp.getConfig();

	String name = cfg.get("name", String.class);

    Iterator<Resource> itemIterator = cmp.getItemDataSource().iterator();
    
    if (cfg.get("ordered", false)) {
        List<Resource> items = (List<Resource>) IteratorUtils.toList(itemIterator);
        final Collator langCollator = Collator.getInstance(request.getLocale());
        
        Collections.sort(items, new Comparator<Resource>() {
            public int compare(Resource o1, Resource o2) {
                return langCollator.compare(getOptionText(o1, cmp), getOptionText(o2, cmp));
            }
        });
        
        itemIterator = items.iterator();
    }

    Tag tag = cmp.consumeTag();
    AttrBuilder attrs = tag.getAttrs();

    attrs.add("id", cfg.get("id", String.class));
    attrs.addClass(cfg.get("class", String.class));
    attrs.addRel(cfg.get("rel", String.class));
    attrs.add("title", i18n.getVar(cfg.get("title", String.class)));
    
    attrs.addClass("coral-Select");
    attrs.add("data-init", "select");
    attrs.add("data-collision", "none");
    
    if (cfg.get("disabled", false)) {
        attrs.add("data-disabled", true);
    }
    
    attrs.addOthers(cfg.getProperties(), "id", "class", "rel", "title", "name", "multiple", "disabled", "required", "validation", "renderReadOnly", "fieldLabel", "fieldDescription", "emptyText", "ignoreData", "translateOptions", "ordered");
    

    AttrBuilder buttonAttrs = new AttrBuilder(request, xssAPI);
    buttonAttrs.add("type", "button");
    buttonAttrs.addClass("coral-Select-button coral-MinimalButton");
    

    AttrBuilder selectAttrs = new AttrBuilder(request, xssAPI);
    selectAttrs.add("name", cfg.get("name", String.class));
    selectAttrs.addMultiple(cfg.get("multiple", false));
    selectAttrs.addClass("coral-Select-select");
    
    if (cfg.get("required", false)) {
        selectAttrs.add("aria-required", true);
    }
    
    String validation = StringUtils.join(cfg.get("validation", new String[0]), " ");
    selectAttrs.add("data-validation", validation);

    AttrBuilder selectListAttrs = new AttrBuilder(request, xssAPI);
    selectListAttrs.add("data-collision-adjustment", cfg.get("collisionAdjustment", String.class));

%><span <%= attrs.build() %>>
    <button <%= buttonAttrs.build() %>>
        <span class="coral-Select-button-text"><%= outVar(xssAPI, i18n, cfg.get("emptyText", "")) %></span>
    </button>
    <select <%= selectAttrs.build() %>><%
        if (cfg.get("emptyOption", false)) {
            String value = "";
    
            AttrBuilder opAttrs = new AttrBuilder(null, xssAPI);
            opAttrs.add("value", value);
            opAttrs.addSelected(cmp.getValue().isSelected(value, false));
            
            out.println("<option " + opAttrs.build() + "></option>");
        }
    
        for (Iterator<Resource> items = itemIterator; items.hasNext();) {
            printOption(out, items.next(), cmp);
        }
    %></select>
    <%-- the select list must be removed from the markup once GRANITE-5713 is fixed --%>
    <ul class="coral-SelectList" <%= selectListAttrs.build() %>></ul><%
    
    if (!StringUtils.isBlank(name) && cfg.get("deleteHint", true)) {
        AttrBuilder deleteAttrs = new AttrBuilder(request, xssAPI);
        deleteAttrs.add("type", "hidden");
        deleteAttrs.add("name", name + "@Delete");

        %><input <%= deleteAttrs %>><%
    }
%></span><%!

    private void printOption(JspWriter out, Resource option, ComponentHelper cmp) throws Exception {
        I18n i18n = cmp.getI18n();
        XSSAPI xss = cmp.getXss();
    
        Config optionCfg = new Config(option);
        String value = cmp.getExpressionHelper().getString(optionCfg.get("value", String.class));

        AttrBuilder opAttrs = new AttrBuilder(null, cmp.getXss());

        opAttrs.add("id", optionCfg.get("id", String.class));
        opAttrs.addClass(optionCfg.get("class", String.class));
        opAttrs.addRel(optionCfg.get("rel", String.class));
        opAttrs.add("title", i18n.getVar(optionCfg.get("title", String.class)));
        opAttrs.add("value", value);
        opAttrs.addDisabled(optionCfg.get("disabled", false));
        opAttrs.addOthers(optionCfg.getProperties(), "id", "class", "rel", "title", "value", "text", "disabled", "selected", "group");

        // if the item is an optgroup, render the <optgroup> and all its containing items
        if (optionCfg.get("group", false)) {
            opAttrs.add("label", i18n.getVar(optionCfg.get("text", String.class)));

            out.println("<optgroup " + opAttrs.build() + ">");
            for (Iterator<Resource> options = option.listChildren(); options.hasNext();) {
                printOption(out, options.next(), cmp);
            }
            out.println("</optgroup>");
        } else {
            // otherwise, render the <option>
            opAttrs.addSelected(cmp.getValue().isSelected(value, optionCfg.get("selected", false)));
            out.println("<option " + opAttrs.build() + ">" + xss.encodeForHTML(getOptionText(option, cmp)) + "</option>");
        }
    }

    private String getOptionText(Resource option, ComponentHelper cmp) {
        Config optionCfg = new Config(option);
        String text = optionCfg.get("text", "");
        
        if (cmp.getConfig().get("translateOptions", true)) {
            text = cmp.getI18n().getVar(text);
        }
        
        return text;
    }
%>