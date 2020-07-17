
//wk3pt6

//fetches comment array from data servlet and inserts comments into html
function populateAnalysis() {
  
  document.getElementById("history").innerText = '';
  
  const entry = document.getElementById('journal-input').value;
  fetch('/journal?journal-input='+entry).then(response => response.text()).then((comments) => {
  comments = comments.replace(/\\[|\\]|"/g,"").split(",")
  for (i = 0; i<comments.length-2; i++) {

    const breakElement = document.createElement("br");

    if (comments[i].slice(0,2) != "0." && comments[i].slice(0,3) != "-0." && comments[i] != "null" && comments[i] != "\\r"){

    
    if (i+1 == comments.length-2) {
        const listElement = document.createElement('li');
        listElement.innerText = comments[i];
    } else if (i<comments.length-2) {
    document.getElementById("history").appendChild(createListElement(comments[i],comments[i+1]));
    }

    document.getElementById("history").appendChild(breakElement);
    document.getElementById("history").appendChild(breakElement);

    } else {
        const prompt = document.getElementById("prompt");
        prompt.innerText = comments[comments.length-2]+" "+comments[comments.length-1];
    }
    if (comments[i] = "NEW ENTRY"){
        document.getElementById("history").appendChild(breakElement);
        document.getElementById("history").appendChild(breakElement);
        document.getElementById("history").appendChild(breakElement);
        document.getElementById("history").appendChild(breakElement);
    }


  }

  });

}

/** Creates an <li> element containing text. */
function createListElement(text , nextElement) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  if (nextElement.slice(0,2) == "0." | nextElement.slice(0,3) == "-0." ){
      var score = parseFloat(nextElement);
      if (score<-0.7) {
          liElement.id = "dark-red";
      } else if (score <-0.25) {
          liElement.id = "red";
      } else if (score <0.0) {
          liElement.id = "yellow";
      } else if (score == 0.0) {
          liElement.id = "grey";
      } else if (score < 0.25) {
          liElement.id = "light-blue";
      } else if (score < 0.7) {
          liElement.id = "light-green";
      } else if (score > 0.7) {
          liElement.id = "green";
      }
  }
  return liElement;
}

function getDate(){
    var today = new Date();
    document.getElementById('time').innerHTML=today;
}
window.onload=populateAnalysis;
window.onload=getDate;