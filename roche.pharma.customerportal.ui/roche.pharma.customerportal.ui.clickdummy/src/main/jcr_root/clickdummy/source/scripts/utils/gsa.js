/**
 * @global 
 * @file global function to search on gsa - only used in component: c_search_layer
 * @author Stephan Fischer <sfisc2@sapient.com>
 */

var gsa = (function()
{
	return {
		search: function(term, new_options, cb, cbFail)
		{
			var data = 
			{
				q: term,
				client: 'default_frontend',
				proxystylesheet: 'audi_json',
				filter: 0,
				site: '',
				output: 'xml_no_dtd',
				getfields: '*',
				num: 10,
				start: 0,
				ie: 'utf-8',
				oe: 'utf-8',
				gbpSearchPath:  "/gbp/search",
				gbpSuggestPath: "/gbp/"
			};

			$.extend(data, new_options);

			var searchAPI  = data.gbpSearchPath;
			var suggestAPI = data.gbpSuggestPath;

			delete data.gbpSuggestPath;
			delete data.gbpSearchPath;

			$.getJSON(searchAPI, data)
			.done(function(ret) { if(cb) 	  cb    .apply(this, [ret]); })
    		.fail(function(ret) { if(cbFail) cbFail.apply(this, [ret]); });
		}
	};

})();