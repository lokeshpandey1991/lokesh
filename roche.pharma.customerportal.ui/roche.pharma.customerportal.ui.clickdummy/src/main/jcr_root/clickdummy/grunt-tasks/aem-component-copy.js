module.exports = function(grunt) {
  var _ = require('lodash');
  var async = require('async');
  var xmlBuilder = require('xmlbuilder');
  var fs = require('fs'),
    path = require('path');


  grunt.registerMultiTask('aemcomponentcopy', 'Aem component copy', function() {
    var done = this.async();

    var options = this.options({
      replaceTargetContentXml: false,
      useDummyContentXml: true,
      componentGroup: 'Roche'
    });

    grunt.log.writeln("Replace target .content.xml = ", options.replaceTargetContentXml);
    this.files.forEach(function(file) {

      var sourceFile = Array.isArray(file.src) ? file.src[0] : file.src;
      var destFile = Array.isArray(file.dest) ? file.dest[0] : file.dest;

      grunt.file.copy(sourceFile , destFile);
      grunt.verbose.writeln("Copy component ", sourceFile);
      var srcContentXml = path.join(path.dirname(sourceFile), '.content.xml');
      var destContentXml = path.join(path.dirname(destFile), '.content.xml');
      if (options.replaceTargetContentXml) {
        if (grunt.file.exists(srcContentXml)) {
          grunt.file.copy(srcContentXml, destContentXml);
        } else {
          if (options.useDummyContentXml) {
            grunt.verbose.writeln("Generateing dummy .content.xml for " + sourceFile);
            var dummyComponentContentXml = buildContentDescriptor(path.basename(path.dirname(sourceFile)) + " component", options.componentGroup);
            grunt.file.write(destContentXml, dummyComponentContentXml);
          }
        }
      }
    });

    grunt.log.ok(this.files.length, " files processed");

    done();
  });


  var buildContentDescriptor = function(component, group) {
    //     <?xml version="1.0" encoding="UTF-8"?>
    // <jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
    //     jcr:primaryType="cq:Component"
    //     jcr:title="List - Sightly"
    //     sling:resourceSuperType="foundation/components/parbase"
    //     allowedParents="[*/parsys]"
    //     componentGroup="General"/>
    var content = {
      'jcr:root': {
        '@xmlns:cq': "http://www.day.com/jcr/cq/1.0",
        '@xmlns:jcr': "http://www.jcp.org/jcr/1.0",
        '@jcr:primaryType': "cq:Component",
        '@jcr:title': "Roche "+component,
        '@allowedParents': "[*/parsys]",
        '@componentGroup': group
      }
    };

    var xml = xmlBuilder.create(content, { encoding: 'UTF-8' });
    return xml.end({
      pretty: true,
      indent: '  ',
      newline: '\n'
    });
  }

}
