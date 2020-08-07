const constructEntry = require('./journal.js').constructEntry;

var assert = require('assert');
describe('Array', function() {
  describe('#includes()', function() {
    describe("#constructEntry()",function() {
    it('indivisual sentence scores should not be printed', function() {
      var test = ["string","0.0","string","-0.0"];
      var expected = false;
      var actual = constructEntry(test).innerText.includes("0.0") && constructEntry(test).innerText.includes("-0.0");
      assert.equal(expected, actual);
    });
    });
  });
});