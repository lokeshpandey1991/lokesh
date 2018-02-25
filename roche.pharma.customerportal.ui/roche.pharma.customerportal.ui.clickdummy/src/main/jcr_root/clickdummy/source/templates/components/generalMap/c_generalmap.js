/*
 * generalmap.js
 * [ This javascript code is used to show contact location on map on contact use page ]
 *
 * @project:    SN-RO
 * @date:       2017-09-18
 * @author:     Neha
 * @licensor:   SAPIENNITRO
 * @namespaces: snro
 */

//this will cause the browser to check for errors more aggressively
'use strict';

/**
 * @namespace generalmapComp
 * @memberof roche
 * @property {null} property - description of property
 */

(function (window, $, snro) {

  snro = window.snro = snro || {};
  var _cache = {
    'map': $('#generalContactMap')
  },
      google = window.google,
      lat = parseFloat(_cache.map.data('lat')),
      lng = parseFloat(_cache.map.data('lng')),
      iconPath = _cache.map.data('marker');

  var _initMap = function _initMap() {

    var mapOptions = {
      zoom: 12,
      center: new google.maps.LatLng(lat, lng),
      styles: [{
        'featureType': 'administrative',
        'elementType': 'labels.text.fill',
        'stylers': [{
          'color': '#444444'
        }]
      }, {
        'featureType': 'landscape',
        'elementType': 'all',
        'stylers': [{
          'color': '#f2f2f2'
        }]
      }, {
        'featureType': 'poi',
        'elementType': 'all',
        'stylers': [{
          'visibility': 'off'
        }]
      }, {
        'featureType': 'road',
        'elementType': 'all',
        'stylers': [{
          'visibility': 'on'

        }, {
          'lightness': 45
        }]
      }, {
        'featureType': 'road.highway',
        'elementType': 'all',
        'stylers': [{
          'visibility': 'simplified'
        }]
      }, {
        'featureType': 'road.arterial',
        'elementType': 'labels.icon',
        'stylers': [{
          'visibility': 'off'
        }]
      }, {
        'featureType': 'transit',
        'elementType': 'all',
        'stylers': [{
          'visibility': 'off'
        }]
      }, {
        'featureType': 'water',
        'elementType': 'all',
        'stylers': [{
          'color': '#46bcec'
        }, {
          'visibility': 'on'
        }]
      }]
    },
        mapElement = _cache.map[0],
        map = new google.maps.Map(mapElement, mapOptions),
        marker = new google.maps.Marker({
      position: new google.maps.LatLng(lat, lng),
      map: map,
      icon: iconPath
    });

    //redirect to google map direction on clicking marker  
    marker.addListener('click', function () {
      var url = 'https://www.google.com/maps/dir/?api=1&destination=' + lat + ',' + lng + '';
      window.open(url);
    });
  };
  // public methods


  /**
   * @method init
   * @description this method is used to initialize public methods.
   * @memberof snro.generalMap
   * @example
   * snro.generalMap.init()
   */
  snro.generalMap = {
    init: function init() {
      _initMap();
    }
  };
})(window, jQuery, window.snro);
