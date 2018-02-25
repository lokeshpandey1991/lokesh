module.exports = {

    // app: {
    //     expand: true,
    //     dot: true,
    //     cwd: '<%= config.source %>',
    //     dest: '<%= config.app %>',
    //     src: ['*.{ico,txt,md}']
    // },

  uxlibdemo: {
    files: [
			{
				expand: true,
				dot: true,
				cwd: '<%= config.source %>/aem-demo-content',
				src: ['etc/designs/**'],
				dest:'<%= config.app %>/jcr_root/'
			}
		]
  }

};
