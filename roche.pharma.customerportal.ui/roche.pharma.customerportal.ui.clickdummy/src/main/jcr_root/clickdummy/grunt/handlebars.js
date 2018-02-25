module.exports = {

    app: {
        options: {
            namespace: 'roche.templates',
            // TODO : (maybe) add amd support and move partials.js to app.js. needs to be evaluated if applicable with vtp integration approach
            //amd: true,
            processName: function(filePath) {
                'use strict';
                var pieces = filePath.split('/');
                return filePath.split('/')[pieces.length - 1].replace('.hbs','');
            }
        },
        files: [
            {
                '<%= config.source %>/scripts/roche.templates.js': '<%= config.source %>/templates/hbs-templates/*.hbs'
            }
        ]
    }

};
