module.exports = {
  uxlib : {
    options : {
      replaceTargetContentXml: true,
      useDummyContentXml: true,
      componentGroup : 'Roche'
    },
    files: [
      {
        expand: true,
        dot: true,
        cwd: '<%= config.source %>/templates/components',
        //src: ['**/*.hbs', '!**/*Sample.hbs'],
        src: ['**/*.html', '!**/*Sample.hbs'],
        dest:'<%= config.app %>/jcr_root/apps/roche/pharma/customerportal/components'
      }
    ]
  },

  //copy copiled artifacts into the maven package

  dist : {
    options : {
      replaceTargetContentXml: false,
      useDummyContentXml: false,
      componentGroup : 'Roche'
    },
    files: [
      {
        expand: true,
        dot: true,
        cwd: '<%= config.source %>/templates/components',
        //src: ['**/*.hbs', '!**/*Sample.hbs'],
        src: ['**/*.html', '!**/*Sample.hbs'],
        dest:'<%= config.app %>/jcr_root/apps/roche/pharma/customerportal/components'
      }
    ]
  },

};
