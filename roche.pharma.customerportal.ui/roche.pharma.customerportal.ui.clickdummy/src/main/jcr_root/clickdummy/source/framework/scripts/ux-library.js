(function ($, document, window) {
    'use strict';

    // methods
    var init,
        _setVPWidth,
        _setInitialVPWidth,
        _toggleComponentInformation,

    // properties
        vpSetterSel = '.vp-width-setter',
        viewerSel = '.ux-library-viewer',
        componentInfoSel = '.quick-navigation-page-information',
        componentNameSel = '.quick-navigation-component-name',
        viewportMapping,
        $vpSetter,
        $viewer,
        $componentInfo,
        $componentName;

    init = function () {
        $vpSetter = $(vpSetterSel);
        $viewer = $(viewerSel);

        viewportMapping = {
            'S': 500,
            'M': 768,
            'L': 1024,
            'Reset': '100%'
        };

        $vpSetter.off('click').on('click', function (e) {
            e.stopPropagation();
            var $self = $(this);
            if ($self.hasClass('active')) {
                _setVPWidth($self, true);
            } else {

                _setVPWidth($self);
            }
            $vpSetter.each(function (index, el) {
                if ($(el)[0] != $self[0]) {
                    $(el).removeClass('active');
                } else {
                    $self.toggleClass('active');
                }
            })

        });

        _setInitialVPWidth();
    };

    _setVPWidth = function (link, resetValue) {
        var widthValue,
            selectedViewport;
        selectedViewport = resetValue ? 'Reset' : link.data('viewport');
        widthValue = resetValue ? window.innerWidth : viewportMapping[selectedViewport];

        $viewer.animate({
            width: widthValue
        }, 800);


        localStorage.setItem('currentViewport', selectedViewport);
    };

    _setInitialVPWidth = function () {
        var initialWidth,
            initialViewport;

        initialViewport = localStorage.getItem('currentViewport');
        initialWidth = viewportMapping[initialViewport];

        if (initialWidth) {
            $viewer.width(initialWidth);
            $vpSetter.each(function (index, el) {
                if ($(el).data('viewport') == initialViewport) {
                    $(el).addClass('active');
                }
            })
        }
    };

    _toggleComponentInformation = function (componentData, showInfo) {
        $componentInfo = $(componentInfoSel);
        $componentName = $(componentNameSel);

        if (showInfo) {
            $componentInfo.show();
            $componentName.html('<i class="fa fa-' + componentData.category + '"></i> &nbsp;' + componentData.title);
        } else {
            $componentInfo.hide();
            $componentName.html('');
        }
    };

    $.receiveMessage(function (e) {
        var data = JSON.parse(e.data);

        if (data.category === 'components' || data.category === 'sample-pages') {
            _toggleComponentInformation(data, true);
            init();
        } else {
            _toggleComponentInformation(data, false);
        }

    }, 'http://' + window.location.host);


})(jQuery, document, window);