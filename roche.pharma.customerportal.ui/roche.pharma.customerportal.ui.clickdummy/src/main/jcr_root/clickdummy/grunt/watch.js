module.exports = {

    options: {
        interval: 250
    },
    assemble: {
        files: [
            '<%= config.source %>/{data,templates}/**/*.{hbs,json}',
            '<%= config.source %>/helpers/*.js'
        ],
        tasks: ['assemble', 'aemdeploy:components'],
        options: {
            spawn: false
        }
    },
    handlebars: {
        files: [
            '<%= config.source %>/templates/components/{,*/}*__tpl.hbs'
        ],
        tasks: ['handlebars', 'concat']
    },
    js: {
        files: [
            '<%= config.source %>/scripts/**/*.js',
            '<%= config.source %>/framework/scripts/*.js',
            '<%= config.source %>/templates/**/*.es'
        ],
        tasks: ['eslint','babel','concat','aemdeploy:components']
    },
    grunticon: {
        files: ['<%= config.source %>/icons/*.svg'],
        tasks: ['svgmin', 'grunticon', 'less','aemdeploy:components']
    },
    sass: {
        files: [
            '<%= config.source %>/styles/vendor/*.scss',
            '<%= config.source %>/styles/vendor/*/*.scss',
            '<%= config.source %>/templates/components/{,*/}*.scss',
            '<%= config.source %>/assets/styles/*.scss',
            '<%= config.source %>/assets/*.scss'
        ],
        tasks: ['sass','autoprefixer','concat','aemdeploy:components']
    },
    // aemdeploy : {
    //   files: [
    //       '<%= config.app %>/jcr_root/{,*/}*.*'
    //   ],
    //   tasks: ['aemdeploy:components']
    // },
    aemcomponentcopy : {
      files: [
          '<%= config.source %>/templates/components/{,*/}*.html'
      ],
      tasks: ['aemcomponentcopy:uxlib','aemdeploy:components'],
    }



};
