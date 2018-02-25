module.exports = {
  options : {
    targets: ['http://admin:admin@localhost:4502']
  },

  // components : {
  //   src: ['<%= config.app %>/jcr_root/**/*.*', '!**/*.{jpg,mov,png,mp4}'],
  // },

  uxlibDemo : {
    src: ['<%= config.app %>/jcr_root/content/roche-ux/**/.content.xml'],
  },

  components : {
    src: ['<%= config.app %>/jcr_root/**/*.{html,xml,txt,js,css}'],
  },

  mediaassets : {
    src: [
      '<%= config.app %>/jcr_root/**/*.{jpg,mov,png,mp4,svg}',
    ]
  },

  fontassets : {
    src: [
      '<%= config.app %>/jcr_root/**/*.{otf,eot,ttf,woff}',
    ]
  },

  contentXML : {
    src: ['<%= config.app %>/jcr_root/**/.content.xml']
  }

};
