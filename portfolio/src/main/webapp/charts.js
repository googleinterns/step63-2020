
google.charts.load('current', {'packages':['corechart']});
//var module = {};
module.exports = {
    sortArray: sortArray,
    finalPrep: finalPrep,
    weekMonthFormat: weekMonthFormat
}


//Draws the chart that tracks the user's mood
function drawMoodChart(arr,type) {
  if(arr.length == 0){
      noData("curve_chart");
  }
  else{
    var data = google.visualization.arrayToDataTable(arr);
    options = {
        title: 'Mood Progression',
        curveType: 'function',
        legend: { position: 'bottom' },
        vAxis: { ticks: [1,2,3,4,5] }
    };
    
    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

    chart.draw(data, options);
  }
}

//Draws the chart that tracks the user's friends&family relationships
function drawRelationChart(arr,type) {
  if(arr.length == 0){
      noData("curve_chart2");
  }
  else{
  var data = google.visualization.arrayToDataTable(arr);

  var options = {
    title: 'Relationship Progression',
    curveType: 'function',
    legend: { position: 'bottom' },
    vAxis: { ticks: [1,2,3,4,5] }
  };

  var chart = new google.visualization.LineChart(document.getElementById('curve_chart2'));

  chart.draw(data, options);
  }
}


function addChoice(value,name){
    const params = new URLSearchParams();
    params.append("value", value);
    params.append("name",name);
    fetch('/charts', {method: 'POST', body: params});
}

function fillCharts(){
    fetch('/fill-charts').then(response => response.json()).then((properties)=>{

        inputData(properties, "week");


    });
}

 function sortArray(arr){

    arr.sort(function sortByDay(a,b){
       var date1 = new Date(a[0].split('-'));
       var date2 = new Date(b[0].split('-'));

       return date1 - date2;
    })
        

    return arr;
     };
function finalPrep(arr){

    for(i =0; i<arr.length;i++){
        arr[i].pop();
    }
    for(i=0;i<arr.length;i++){
        arr[i][1] = parseInt(arr[i][1]);
    }
    arr.unshift(["Day", "Mood"]);
}

function noData(id){
    var errorMessage = document.getElementById(id);
    errorMessage.innerHTML = "No data to show";
    errorMessage.style.textAlign = 'center';
    errorMessage.style.backgroundColor= "#b68e9a";
    errorMessage.style.fontSize = "18px";
    errorMessage.style.color = "white";
}

function fillMonth(){
    fetch('/fill-charts').then(response => response.json()).then((properties)=>{
        inputData(properties, "month")
    });
}

function inputData(properties, type){
    var Q1Array = [];
    var Q2Array = [];
    if(properties.length != 0){

        for(i=0; i<properties.length; i++){
            var row = i;
            var question = properties[row][2];
            if(question == "Q1"){
                Q1Array.push(properties[row]);
            }
            else{
                Q2Array.push(properties[row]);
            }
        }


        Q1Array = sortArray(Q1Array);
        Q2Array = sortArray(Q2Array);
        finalPrep(Q1Array);
        finalPrep(Q2Array);
        
        Q1Array =weekMonthFormat(Q1Array,type);
        Q2Array =weekMonthFormat(Q2Array,type);
    }
    drawMoodChart(Q1Array,type);
    drawRelationChart(Q2Array,type);
}

function weekMonthFormat(arr,type){
      var s = arr.slice(0,1);
      if(type == "week" && arr.length > 8){

         arr = s.concat(arr.slice(arr.length-7, arr.length));
      }
      else if(type == "month" && arr.length > 31){
         arr= s.concat(arr.slice(arr.length - 30,arr.length));
      }
      return arr;
}