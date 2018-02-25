var path = require('path');
module.exports = {

  options: {
    // start - global variables
    version: '<%= package.version %>',
    publish: '<%= config.publishRel %>',
    static: '<%= config.staticRel %>',
    date: '<%= config.date %>',
    // end - global variables
    //flatten: true,
    layout: 'app.hbs',
    layoutdir: '<%= config.source %>/templates/layouts',
    data: [
      '<%= config.source %>/data/*.{json,yml}',
      '<%= config.source %>/templates/components/{,*/}*.json'
    ],
    partials: [
      '<%= config.source %>/templates/components/{,*/}*.hbs',
      '<%= config.source %>/framework/templates/*.hbs',
      '<%= config.source %>/templates/pages/*.hbs'
    ],
    collections: [{
      name: 'components',
      inflection: 'component',
      sortorder: 'ascending',
      sortby: 'datetime'
    }],
    helpers: ['<%= config.source %>/helpers/*'],
    plugins: ['<%= config.source %>/framework/assemble/plugins/assemble-aem-plugin.js']
  },

  pages: {

    options: {
      aemuxlib: {
        contentRoot: '<%= config.app %>/jcr_root/content/roche-ux',
        designJcrPath: '/etc/designs/roche/customerportal',
        jcrFileRoot : '<%= config.app %>/jcr_root/',
        wrapJsonData : true
      }
    },
    files: [
      //demo components
      {
        expand: true, // Enable dynamic expansion.
        src: ['<%= config.source %>/templates/components/{,*/}*{all-components,preview}.hbs'], // Actual pattern(s) to match.
        dest: '<%= config.app %>/jcr_root/apps/roche/pharma/customerportal/components', // Destination path prefix.
        rename: function(dest, src) {
          var dirname = path.dirname(src);
          var componentName = path.basename(dirname);
          var fileName = path.basename(src, '.hbs');
          return path.join(dest,componentName,fileName);
        }
      },
      //demo pages
      {
      expand: true, // Enable dynamic expansion.
      src: ['<%= config.source %>/templates/pages/*.hbs'], // Actual pattern(s) to match.
      dest: '<%= config.app %>/jcr_root/apps/roche/pharma/customerportal/components/page/', // Destination path prefix.
      rename: function(dest, src) {
        var name = path.basename(src, '.hbs');
        return path.join(dest,name,name);
      }
    }]

  },
};
