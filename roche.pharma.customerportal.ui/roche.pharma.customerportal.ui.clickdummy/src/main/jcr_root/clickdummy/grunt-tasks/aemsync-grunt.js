module.exports = function(grunt) {
  var _ = require('lodash');
  var async = require('async');
  var chalk = require('chalk');
  var chalk = require('chalk');
  var Pusher = require('aemsync').Pusher
  var fs = require('fs'),
    path = require('path');


  grunt.registerMultiTask('aemdeploy', 'Deploy content to AEM using aemsync', function() {
    var done = this.async();

    var options = this.options({
      targets: ['http://admin:admin@localhost:4502']
    });

    grunt.log.writeln("Aemsync targets: ", options.targets.join(','));
    var pusher = new Pusher(options.targets, 0, function() { done(); });

    this.filesSrc.forEach(function(file) {
      pusher.addItem(path.resolve(file));

    });

    pusher.processQueue();
    grunt.log.ok("Files processed: " + this.filesSrc.length);

    //done();
  });


}
