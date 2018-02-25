/*
Handlebars.registerHelper('dynamicMedia', function (src, altText, template) {

  const $config = $('.js-media-config');
  const baseURL = $config.data('media-url');
  let templateClass = '.' + template;

  // extract data attributes for different rendition

  let configAttr = $config.find(templateClass).data();

  for (let attr in configAttr) {
    if (configAttr.hasOwnProperty(attr)) {
      let dataAttrValue = $config.find(templateClass).data(attr);
      let wid = dataAttrValue && dataAttrValue.length > 0 ? dataAttrValue.split(',')[0] : '';
      let hei = dataAttrValue && dataAttrValue.length > 0 ? dataAttrValue.split(',')[1] : '';

      let dataAttr = 'data-src_' + attr + '=' + src + '?';

      if (wid) {
        dataAttr = dataAttr + 'wid=' + wid;
      }

      if (wid && hei) {
        dataAttr = dataAttr + '&';
      }

      if (hei) {
        dataAttr = dataAttr + 'hei=' + hei;
      }

      console.log(dataAttr);

    }
  }

  const desktop = $config.find(templateClass).data('desktop');
  let desktopWid = desktop && desktop.length > 0 ? desktop.split(',')[0] : '';
  let desktopHei = desktop && desktop.length > 0 ? desktop.split(',')[1] : '';

  return new Handlebars.SafeString(
    "<img data-url='" + url + "'>" + text + "</img>"
  );
});
*/
