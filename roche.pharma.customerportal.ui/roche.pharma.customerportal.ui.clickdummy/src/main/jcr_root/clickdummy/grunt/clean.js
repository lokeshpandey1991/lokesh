module.exports = {

    app: {
        options: { force: true },
        files: [{
            expand:true,
            dot: true,
            src: [
                '<%= config.app %>/*',
                //'!<%= config.app %>/.git*',
                //'!<%= config.app %>/etc'
            ]
        }]
    }

};
