module.exports = {

    app: {
        options: {
            plugins: [
                {removeViewBox: false},
                {removeUselessStrokeAndFill: false}
            ]
        },
        files: [
            {
                expand: true,
                cwd: '<%= config.source %>/icons',
                src: ['*.svg'],
                dest: '<%= config.source %>/icons/icons-minified'
            }
        ]
    }


};
