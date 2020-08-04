google = {'charts': {'load': function() {}}};
var charts = require('./charts.js');
var assert = require('assert');

var unsorted = [["2020-07-04"], ["1997-04-04"], ["2001-08-09"], ["1999-11-30"], ["1950-02-11"], ["2020-05-18"]];
var sorted = [["1950-02-11"],["1997-04-04"], ["1999-11-30"], ["2001-08-09"], ["2020-05-18"], ["2020-07-04"]];

var monthEntries = [["Day", "Mood"], ["2020-07-01", "1", "Q1"],["2020-07-02", "1", "Q1"], ["2020-07-03", "1", "Q1"],
                    ["2020-07-04", "1", "Q1"], ["2020-07-05", "1", "Q1"], ["2020-07-06", "1", "Q1"], ["2020-07-07", "1", "Q1"],
                    ["2020-07-08", "1", "Q1"], ["2020-07-09", "1", "Q1"], ["2020-07-10", "1", "Q1"], ["2020-07-11", "1", "Q1"],
                    ["2020-07-12", "1", "Q1"], ["2020-07-13", "1", "Q1"], ["2020-07-14", "1", "Q1"], ["2020-07-15", "1", "Q1"],
                    ["2020-07-16", "1", "Q1"], ["2020-07-17", "1", "Q1"], ["2020-07-18", "1", "Q1"], ["2020-07-19", "1", "Q1"],
                    ["2020-07-20", "1", "Q1"], ["2020-07-21", "1", "Q1"], ["2020-07-22", "1", "Q1"], ["2020-07-23", "1", "Q1"],
                    ["2020-07-24", "1", "Q1"], ["2020-07-25", "1", "Q1"], ["2020-07-26", "1", "Q1"], ["2020-07-27", "1", "Q1"],
                    ["2020-07-28", "1", "Q1"], ["2020-07-29", "1", "Q1"], ["2020-07-30", "1", "Q1"], ["2020-07-31", "1", "Q1"] ];
var weekChart = [["Day", "Mood"], ["2020-07-25", "1", "Q1"], ["2020-07-26", "1", "Q1"], ["2020-07-27", "1", "Q1"],
                 ["2020-07-28", "1", "Q1"], ["2020-07-29", "1", "Q1"], ["2020-07-30", "1", "Q1"], ["2020-07-31", "1", "Q1"]  ];
var monthChart = [ ["Day", "Mood"], ["2020-07-02", "1", "Q1"], ["2020-07-03", "1", "Q1"],
                    ["2020-07-04", "1", "Q1"], ["2020-07-05", "1", "Q1"], ["2020-07-06", "1", "Q1"], ["2020-07-07", "1", "Q1"],
                    ["2020-07-08", "1", "Q1"], ["2020-07-09", "1", "Q1"], ["2020-07-10", "1", "Q1"], ["2020-07-11", "1", "Q1"],
                    ["2020-07-12", "1", "Q1"], ["2020-07-13", "1", "Q1"], ["2020-07-14", "1", "Q1"], ["2020-07-15", "1", "Q1"],
                    ["2020-07-16", "1", "Q1"], ["2020-07-17", "1", "Q1"], ["2020-07-18", "1", "Q1"], ["2020-07-19", "1", "Q1"],
                    ["2020-07-20", "1", "Q1"], ["2020-07-21", "1", "Q1"], ["2020-07-22", "1", "Q1"], ["2020-07-23", "1", "Q1"],
                    ["2020-07-24", "1", "Q1"], ["2020-07-25", "1", "Q1"], ["2020-07-26", "1", "Q1"], ["2020-07-27", "1", "Q1"],
                    ["2020-07-28", "1", "Q1"], ["2020-07-29", "1", "Q1"], ["2020-07-30", "1", "Q1"], ["2020-07-31", "1", "Q1"] ];

var oneEntry = [["2020-07-24", "1", "Q2"]];


/*
describe('Array', function() {
  describe('#indexOf()', function() {
    it('should return -1 when the value is not present', function(){
      assert.equal(-1, [1,2,3].indexOf(4));
    });
  });
});

describe('Math', function() {
  describe('math1', function() {
    it('should equal 9', function(){
      assert.equal(9, 3*3);
    });
    it('should equal -8', function(){
      assert.equal(-8, (3 - 4)*8);
    });
  });
});
*/

describe('Sort', function() {
  describe('day sort', function() {
    it('should return a sorted array of dates', function(){
      assert.deepEqual(sorted, charts.sortArray(unsorted));
    });
  });
});

describe('Edit by week or month', function() {
  describe('Edit by week', function() {
    it('should return an array of 7 entries', function(){
      assert.deepEqual(weekChart, charts.weekMonthFormat(monthEntries, "week"));
    });
    it('should return the same array with one entry', function(){
      assert.deepEqual(oneEntry, charts.weekMonthFormat(oneEntry, "week"));
    });

  });
  describe('Edit by month', function() {
    it('should return an array of 30 entries', function(){
      assert.deepEqual(monthChart, charts.weekMonthFormat(monthEntries, "month"));
    });
    it('should return the same array of 7 entries', function(){
      assert.deepEqual(weekChart, charts.weekMonthFormat(weekChart, "month"));
    });
  });
});