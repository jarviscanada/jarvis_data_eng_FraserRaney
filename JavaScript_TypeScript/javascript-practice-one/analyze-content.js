const {JSDOM} = require("jsdom");

function analyzeContent(stringContent) {

  let contentType = checkContentType(stringContent);

  switch (contentType) {
    case 'TEXT':
      return {contentType: contentType, lineNumber: countLines(stringContent)};
    case 'HTML':
      return {contentType: contentType, tags: countUniqueTags(stringContent)};
    case 'CSS':
      return {
        contentType: contentType,
        cssTargets: countUniqueCssSelectors(stringContent)
      };
    default:
      return {};
  }

  return {};
}

function checkContentType(str) {
  if (isHTML(str)) {
    return 'HTML';
  } else if (isCSS(str)) {
    return 'CSS';
  } else {
    return 'TEXT';
  }
}

function isHTML(str) {
  const dom = new JSDOM(str);
  return dom.window.document.body.children.length > 0;
}

function isCSS(str) {
  const cssRegex = /^\s*([.#]?[a-zA-Z0-9_-]+)\s*\{[\s\S]*\}$/im;
  return cssRegex.test(str.trim());
}

function countLines(str) {
  const lines = str.split(/\r\n|\r|\n/);
  return lines.length;
}

function countUniqueTags(htmlString) {
  const dom = new JSDOM(htmlString);
  const elements = dom.window.document.getElementsByTagName('*');
  const tagCounts = {};
  const raw = htmlString.toLowerCase();

  for (const el of elements) {
    const tag = el.tagName.toLowerCase();

    // Only count if this tag was actually present in the input
    if (!raw.includes(`<${tag}`)) {
      continue;
    }

    tagCounts[tag] = (tagCounts[tag] || 0) + 1;
  }
  return tagCounts;
}

function countUniqueCssSelectors(cssString) {
  const selectorRegex = /([#\.a-zA-Z0-9_\-]+(?:[.#][#\.a-zA-Z0-9_\-]+)*)(?:[,\s]*\s*{)/g;
  let matches;
  const selectorCounts = {};

  while ((matches = selectorRegex.exec(cssString)) !== null) {
    // Split by comma, trim whitespace, and add to map
    const selectors = matches[1].split(',').map(s => s.trim()).filter(s => s);
    selectors.forEach(selector => {
      selectorCounts[selector] = (selectorCounts[selector] || 0) + 1;
    });
  }

  return selectorCounts
}

module.exports = analyzeContent;