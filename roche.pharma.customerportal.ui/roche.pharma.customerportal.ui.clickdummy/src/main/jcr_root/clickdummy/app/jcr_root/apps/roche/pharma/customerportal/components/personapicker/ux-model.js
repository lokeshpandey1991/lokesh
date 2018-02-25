use(function() {
 var data = {
  "personaHeaderText":"We are customized for you:",
   "homepagePath" : "http://localhost:4502/content/roche-ux/header.ux-preview.html",
  "personaList":[
    {
       "personaSelectorType":"HealthCare",
       "personaSelectorName":"HEALTH CARE PROFESSIONALS",
       "personaSelectorDescription":"Lorem ipsum dolor sit amet, nullam pericula consectetuer an ius. Animal nonumes at sit. Nec an graeco expetendis.",
       "defaultPersona":"true"
    },
    {
       "personaSelectorType":"Researchers",
       "personaSelectorName":"RESEARCHERS",
       "personaSelectorDescription":"Lorem ipsum dolor sit amet, nullam pericula consectetuer an ius. Animal nonumes at sit. Nec an graeco expetendis.",
       "defaultPersona":"false"
    },
    {
       "personaSelectorType":"Patients",
       "personaSelectorName":"PATIENTS",
       "personaSelectorDescription":"Lorem ipsum dolor sit amet, nullam pericula consectetuer an ius. Animal nonumes at sit. Nec an graeco expetendis.",
       "defaultPersona":"false"
    }
   ]

}
;
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});