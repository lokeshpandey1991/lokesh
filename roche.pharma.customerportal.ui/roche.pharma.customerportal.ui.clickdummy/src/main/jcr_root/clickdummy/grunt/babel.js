module.exports = {
    options: {
        sourceMap: false,
        presets: ['es2015']
    },
    dist: {
        files: [{
            expand: true,
            cwd: '<%= config.source %>/templates/components',
            src: ['**/*.es'],
            dest: '<%= config.source %>/templates/components',
            ext: '.js'
        },{
            expand: true,
            cwd: '<%= config.source %>/scripts/base',
            src: ['**/*.es'],
            dest: '<%= config.source %>/scripts/base',
            ext: '.js'
        },{
            expand: true,
            cwd: '<%= config.source %>/scripts/common',
            src: ['**/*.es'],
            dest: '<%= config.source %>/scripts/common',
            ext: '.js'
        },{
            expand: true,
            cwd: '<%= config.source %>/scripts/utils',
            src: ['**/*.es'],
            dest: '<%= config.source %>/scripts/utils',
            ext: '.js'
        },{
            expand: true,
            cwd: '<%= config.source %>/scripts',
            src: ['**/*.es'],
            dest: '<%= config.source %>/scripts',
            ext: '.js'
        }]
    }
}