function mixin(target) {
    var arg, key,
        len = arguments.length,
        i = 1;

    for (; i < len; i++) {
        arg = arguments[i];

        if (!arg) {
            continue;
        }

        for (key in arg) {
            /* istanbul ignore else */
            if (arg.hasOwnProperty(key)) {
                target[key] = arg[key];
            }
        }
    }

    return target;
}

function noop() {
    return '';
}

function getStack(context) {
    return context._layoutStack || (context._layoutStack = []);
}

function initActions(context) {
    var stack = getStack(context),
        actions = {};

    context._layoutActions = actions;

    while (stack.length) {
        stack.pop()(context);
    }

    return actions;
}

function getActions(context) {
    return context._layoutActions || initActions(context);
}

function getActionsByName(context, name) {
    var actions = getActions(context);

    return actions[name] || (actions[name] = []);
}

function applyAction(val, action) {
    /* jshint validthis:true */

    switch (action.mode) {
        case 'append':
        {
            return val + action.fn(this);
        }

        case 'prepend':
        {
            return action.fn(this) + val;
        }

        case 'replace':
        {
            return action.fn(this);
        }

        default:
        {
            return val;
        }
    }
}


module.exports.register = function (Handlebars) {
    'use strict';

    // console.log(require('util').inspect(options, {depth:null}));
    Handlebars.registerHelper('component-include', function (name, options) {

        options = options || {};

        var fn = options.fn || noop,
            context = Object.create(this || {}),
            template = Handlebars.partials[name],
            renderedTemplate = "",
            richTextContent = fn && fn(context) || null;

        // Mix attributes into context
        mixin(context, options.hash, {'richTextContent': richTextContent});

        // Partial template required
        if (template == null) {
            throw new Error('Missing partial: \'' + name + '\'');
        }

        // Compile partial, if needed
        if (typeof template !== 'function') {
            template = Handlebars.compile(template);
            renderedTemplate = template(context);
        }

        // Add overrides to stack
        getStack(context).push(fn);

        // console.log(require('util').inspect(this.blockquote, {depth:null}));
        // Render partial
        return new Handlebars.SafeString(renderedTemplate);

    });


    Handlebars.registerHelper('component-block', function (name, options) {

        options = options || {};

        var fn = options.fn || noop,
            context = this || {};

        return getActionsByName(context, name).reduce(
            applyAction.bind(context),
            fn(context)
        );

    });

    Handlebars.registerHelper('component-block-content', function (name, options) {

        options = options || {};

        var fn = options.fn || noop,
            hash = options.hash || {},
            mode = hash.mode || 'replace',
            context = this || {};

        getActionsByName(context, name).push({
            mode: mode.toLowerCase(),
            fn: fn
        });

        return '';

    });

};
