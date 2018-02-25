module.exports = {

    app: {
        options: {
            config: '.eslintrc'
        },
        src: [
            '<%= config.source %>/scripts/components/**/*.js',
            '<%= config.source %>/templates/components/**/*.es',
            '<%= config.source %>/scripts/base/*.es',
            '<%= config.source %>/scripts/common/*.es',
                // Recommended to only check developer created source
                // files to avoid overcrowding console output
                // Kept for demonstration, or when you can't do
                // away without needing it
        ]
    }

};
