/**
 * @fileoverview Basic test to make sure Mocha works.
 * First example in
 * https://codeburst.io/how-to-test-javascript-with-mocha-the-basics-80132324752e
 */
var assert = require('assert');
describe('Array', function() {
  describe('#indexOf()', function() {
    it('should return -1 when the value is not present', function() {
      assert.equal(-1, [1,2,3].indexOf(4));
    });
  });
});
