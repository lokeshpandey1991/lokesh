module.exports = {

    app: {
        options: {
            modifyVars: {
                staticRel: '"<%= config.staticRel %>"'
            }
        },
        files: [{
          expand: true,
          src: '<%= config.source %>/assets/*.scss',
          dest: '<%= config.source %>/assets/css',
          ext: '.css',
          flatten: true
        }]
    }

};
