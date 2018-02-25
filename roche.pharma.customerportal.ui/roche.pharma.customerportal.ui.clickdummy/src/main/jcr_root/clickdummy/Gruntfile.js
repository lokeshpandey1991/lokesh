// Generated on 2014-03-28 using generator-lessapp 0.4.9
'use strict';

// # Globbing
// for performance reasons we're only matching one level down:
// 'test/spec/{,*/}*.js'
// use this if you want to recursively match all subfolders:
// 'test/spec/**/*.js'

module.exports = function (grunt) {

    // Time how long tasks take. Can help when optimizing build times
    require('time-grunt')(grunt);

    // Load grunt tasks automatically
    require('load-grunt-config')(grunt, {
        data: {
            config: {
                app: 'app',
                source: 'source',
                publish: 'app/jcr_root/etc/designs/roche/customerportal/clientlibs',
				publishTemp: 'source/aem-demo-content/etc/designs/roche/customerportal/clientlibs',
                //publishRel: '/etc/clientlibs/roche/publish',
                static: 'app/jcr_root/etc/designs/roche/static',
                staticRel: '/etc/designs/roche/static',
                date: grunt.template.today('yyyy-mm-dd')
            },
//            secret: grunt.file.readJSON('secret.json')
        },
        jitGrunt: {
            staticMappings: {
                browserSync:   'grunt-browser-sync',
                removelogging: 'grunt-remove-logging',
                sftp: 'grunt-ssh',
                injector: 'grunt-asset-injector',
                buildcontrol: 'grunt-build-control',
                grunticon: 'grunt-grunticon',
                aemcomponentcopy: 'grunt-tasks/aem-component-copy.js',
                aemdeploy: 'grunt-tasks/aemsync-grunt.js',
                handlebars : 'grunt-handlebars.js'
            }
        }
    });

};
