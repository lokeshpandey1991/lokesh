module.exports = {

    app: {
        options: {
            jshintrc: '.jshintrc',
            reporter: require('jshint-stylish')
        },
        all: [
            '<%= config.source %>/scripts/components/*.js',
            '<%= config.source %>/scripts/utils/*.js'
        ]
    }

};
