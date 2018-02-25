module.exports = {

    app: {
        files: [{
            expand: true,
            cwd: '<%= config.source %>/icons/icons-minified',
            src: ['*.svg'],
            dest: '<%= config.source %>/styles/icons'
        }],
        options: {
            datasvgcss: 'icons-svg.less',
            datapngcss: 'icons-png.less',
            urlpngcss: 'icons-url.less',
            cssprefix: '.icon-',
            colors: {
                red: '#cc0033',
                darkred: '#aa142d',
                graynormal: '#b0b6b8',
                graybright: '#d7dadb',
                black: '#000000',
                white: '#ffffff'
            }
        }
    }

};
