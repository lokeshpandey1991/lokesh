module.exports = {

    sftp: {
        files: {
            './': '<%= config.app %>/**/*'
        },
        options: {
            path: '<%= secret.path %>',
            host: '<%= secret.host %>',
            username: '<%= secret.username %>',
            password: '<%= secret.password %>',
            showProgress: true,
            srcBasePath: '<%= config.app %>/',
            createDirectories: true
        }
    }

};
