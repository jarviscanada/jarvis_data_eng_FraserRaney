const analyzeContent = require("./analyze-content.ts");

test('analyzes text content correctly', () => {
  expect(analyzeContent("this is a test\nSeems to work"))
  .toEqual({ contentType: "TEXT", lineNumber: 2 });
});

test('analyzes CSS content correctly', () => {
  expect(analyzeContent("body{blabla} a{color:#fff} a{ padding:0}"))
  .toEqual({contentType: "CSS", cssTargets: {body: 1, a: 2}});
});

test('analyzes HTML content correctly', () => {
  expect(analyzeContent("<html><div></div><div></div></html>"))
  .toEqual({contentType: "HTML", tags: {html: 1, div: 2}});
});