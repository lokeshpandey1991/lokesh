## About this Project

This project is a frontend implementation of the Roche Pharma Customerportal. The project setup is partially based on Sapient upFront but instead of handlebars is using Sightly. The markup, css, js is integrated as-is into the AEM without additional transformations into the AEM backend code.

The project supports live preview (on-the-fly) of the Sightly markup in AEM using watch task. No special (maven) deployment is necessary. The markup can be developed and tested without having complete backend part (or even no back end at all). The backend is simulated using JSON example structures and AEM ux-lib pages - 'fake' AEM pages generated automatically by a grunt task.

## Prerequisites

1.  Install nodejs from [http://nodejs.org/](http://nodejs.org/) , (tested with node 6.3)
  Add environmental path where node is installed in environmental variable so that node can be accessed in any location of directory

  Run the following commnands in order:
    npm install
    npm install -g grunt-cli

2. Install bower from [http://bower.io/](http://bower.io/)
  Run the following commnands in order:
    npm install -g bower
    bower install

3. aemsync npm module installed globally `sudo npm install aemsync -g` (optional). See aemsync github project for more details
  Run the following commnand
    npm install aemsync -g
    grunt aem

4) Wait until , following execution comes
    Running "Watch" task
    Waiting..

5) Make sure  AEM 6.2 author instance running on port 4502 (for testing)


## Notes

1. If bower gives you GIT issues just copy and paste this into your CLI: ``git config --global url."https://".insteadOf git://``
2. If you get permission problems with installing bower, follow these steps:
https://docs.npmjs.com/getting-started/fixing-npm-permissions

## Run the project

Make sure you have AEM instance running.
Note: For all grunt tasks you may need to add --force option.

To start developing clickdummy having preview in the AEM:

    grunt aem

This task creates a distribution in the app folder synchronizes all changes to AEM and keeps pushing changes on the fly using grunt watch. Now you can see live preview on ([http://localhost:4502/content/roche-ux/header.ux-preview.html]

If necessary you can build and synchronize changes without watch task:

    grunt
    grunt aemsync

If you are using maven to deploy frontend changes then you will need to start watch task without aem synchronization in grunt:

    grunt watchapp

## Release

To release the changes for the AEM package integration you need to build all artifacts and commit content of the app folder into git:

  grunt dist
  git commit

**This should be always the last action after you release your changes for the AEM package integration.**

## How is a page build together

### Page

Let's use the header page http://localhost:4502/content/roche-ux/header.ux-preview.html

The specific file for this page can be found at `source/templates/header/ux-preview.hbs`

Inside of the header template you will find a header with more information

    ---
    {
      "layout"   :   "app.hbs",
      "title"      : "Main Navigation",
      ...
    }
    ---

Here you can see that the **layout** app.hbs is used for that page.


### Layout

Find the layout template at `source/templates/layouts/app.hbs`

The page content of app.hbs just goes into:

    {{> body }}

Inside of the layout there can be **includes** like header or footer.

### Component

A component can be used inside of handlebar files like

    {{ include  "header" }}

Find the component *header* at `source/templates/components/header/``

A component contains:
- the HTML as handlebars template e.g. **u.hbs**
- the CSS styling as one or multiple sass files for other breakpoints e.g. **c_header.scss**
- (optional) the logic as Javascript file e.g. **c_header.js**
- (optional) example usage to avoid duplication e.g. **ux-preview.hbs**

It is also possible to have components with attributes:

    {{> carousel
      carouselType = "multipleSmall"
      carouselId   = "carousel3"
      fillData     = carousel3
    }}


## General styles

The basic sass definitions can be found at source/styles. In this folder you can find variable definitions like colors in variables.scss
Component specific styles should go into the component folder.




## Scripts

For external libraries we use bower for dependency management. So only our files
are stored inside of the project. In the folder source/scripts there is only a
config.js and main.js which are used to initialize the component specific scripts.


## Images

Template images are stored inside of `source/images` like logos, or SVG graphics.
Normal content images goes into the `source/media` folder which is more like the
upload folder for resources.


## Handlebar Helpers

Handlebar was extended to support our component concept. Helpers like these can be
found in `source/helpers`.
