ContextHub.console.log(ContextHub.Shared.timestamp(), '[loading] contexthub.store.contexthub.persona - Store.PersistedStore');

(function($) {
    'use strict';







    /**
     * Persona Store implementation.
     *
     * @extends ContextHub.Store.SessionStore
     * @param {String} name - store name
     * @param {Object} config - config
     * @constructor
     */
    var PersonaStore = function(name, config) {
        this.init(name, config);
        this.getCookieCheck();
    };

    /* inherit from ContextHub.Store.PersistedStore */
    ContextHub.Utils.inheritance.inherit(PersonaStore, ContextHub.Store.PersistedStore);

    /**
     * Returns name.
     *
     * @return {String}
     */
    PersonaStore.prototype.getName = function() {
        return this.getItem('name') || '[unknown]';
    };

    PersonaStore.prototype.getCookieCheck=function(){
        var result = {};


  	var personaSelected=ContextHub.Utils.Cookie.getItem("persona-type")||'';       
      this.setItem('name',personaSelected,result);
    };

    /* register store candidate */
    ContextHub.Utils.storeCandidates.registerStoreCandidate(PersonaStore, 'contexthub.persona', 0);

}(ContextHubJQ));
