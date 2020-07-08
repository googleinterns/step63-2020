
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);
var gallery = 1;
function arrows(){
  var whichArrow = document.getElementsByTagName("A")[0].getAttribute("class");

  if(whichArrow == "backArrow"){
    gallery--;
  }
  else{
    gallery++;
  }


  if(gallery == 4){
    gallery = 1;
  }
  if(gallery == 0){
    gallery = 3;
  }
  if(gallery == 1){
    drawMoodChart();
    console.log("mood");
  }
  if (gallery == 2){
    drawRelationChart();
    console.log("relationship");
  }
  if(gallery == 3){
    drawChart();
    console.log("something");
  }
  
}

function drawMoodChart(value) {
  var input = value;
  var data = google.visualization.arrayToDataTable([
    ['Week', 'Mood'],
    ['Monday',  1],
    ['Tuesday',  2],
    ['Wednesday',  5],
    ['Thursday',  3],
    ['Friday',  input]
  ]);

  var options = {
    title: 'Mood Progression',
    curveType: 'function',
    legend: { position: 'bottom' }
  };

  var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

  chart.draw(data, options);
}

function drawRelationChart(value) {
  var input = value;
  var data = google.visualization.arrayToDataTable([
    ['Week', 'Mood'],
    ['Monday',  1],
    ['Tuesday',  2],
    ['Wednesday',  5],
    ['Thursday',  3],
    ['Friday',  input]
  ]);

  var options = {
    title: 'Relationship Progression',
    curveType: 'function',
    legend: { position: 'bottom' }
  };

  var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

  chart.draw(data, options);
}

function drawChart(value) {
  var input = value;
  var data = google.visualization.arrayToDataTable([
    ['Week', 'Mood'],
    ['Monday',  1],
    ['Tuesday',  2],
    ['Wednesday',  5],
    ['Thursday',  3],
    ['Friday',  input]
  ]);

  var options = {
    title: 'Something Progression',
    curveType: 'function',
    legend: { position: 'bottom' }
  };

  var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

  chart.draw(data, options);
}