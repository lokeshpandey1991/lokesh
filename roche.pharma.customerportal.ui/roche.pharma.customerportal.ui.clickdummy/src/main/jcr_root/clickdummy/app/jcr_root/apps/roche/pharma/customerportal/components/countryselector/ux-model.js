use(function() {
 var data = {
  "currentText": "Current {0} Text",
  "globalHomePagePath": "/content/ww/en/home-page.html",
  "locationText": "Location Selector",
  "bottomMessage": "Please go to this sites",
  "countrySelectorList": [
    {
      "continentCode": "na",
      "continentName": "North America",
      "thumbnail": "/etc/design/na/thumbnail.jpg",
      "countryBean": [
        {
          "countryCode": "IN",
          "countryName": "United States 1",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "CA",
          "countryName": "Canada2",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            },
            {
              "languageCode": "fr",
              "languageName": "French",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "DE",
          "countryName": "3 Germany",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "FR",
          "countryName": "4 Franch",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            },
            {
              "languageCode": "fr",
              "languageName": "French",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "US",
          "countryName": "5 United States",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "CA",
          "countryName": "Canada",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            },
            {
              "languageCode": "fr",
              "languageName": "French",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "DE",
          "countryName": "Germany",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "FR",
          "countryName": "Franch",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            },
            {
              "languageCode": "fr",
              "languageName": "French",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "US",
          "countryName": "United States",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "CA",
          "countryName": "Canada",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            },
            {
              "languageCode": "fr",
              "languageName": "French",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        }
      ]
    },
    {
      "continentCode": "eu",
      "continentName": "Europe",
      "thumbnail": "/etc/design/eu/thumbnail.jpg",
      "countryBean": [
        {
          "countryCode": "DE",
          "countryName": "Germany",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        },
        {
          "countryCode": "FR",
          "countryName": "Franch",
          "languageBean": [
            {
              "languageCode": "en",
              "languageName": "English",
              "redirectPath": "http://www.roche.com/"
            },
            {
              "languageCode": "fr",
              "languageName": "French",
              "redirectPath": "http://www.roche.com/"
            }
          ]
        }
      ]
    }
  ]
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});