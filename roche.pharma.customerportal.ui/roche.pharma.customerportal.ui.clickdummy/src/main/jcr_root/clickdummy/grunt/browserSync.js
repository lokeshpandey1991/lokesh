module.exports = {

    app: {
        bsFiles: {
            src: [
                '<%= config.publish %>/scripts/*.js',
                '<%= config.publish %>/styles/*.css',
                '<%= config.app %>/components/*.html',
                '<%= config.app %>/*.html'
            ]
        },
        options: {
            watchTask: true,
            server: {
                baseDir: './app'
            }
        }
    }

};
