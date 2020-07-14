
//wk3pt6

//fetches comment array from data servlet and inserts comments into html
function populateAnalysis() {
  
  document.getElementById("history").innerText = '';
  
  const entry = document.getElementById('journal-input').value;
  fetch('/journal?journal-input='+entry).then(response => response.text()).then((comments) => {
  comments = comments.replace(/\\[|\\]|"/g,"").split(",")
  for (i = 0; i<comments.length; i++) {

    const breakElement = document.createElement("br");

    document.getElementById("history").appendChild(createListElement(comments[i]));
    document.getElementById("history").appendChild(breakElement);
    document.getElementById("history").appendChild(breakElement);


  }

  });

}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function getDate(){
    var today = new Date();
    document.getElementById('time').innerHTML=today;
}
window.onload=populateAnalysis;
window.onload=getDate;