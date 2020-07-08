/* 
function populateAnalysis() {
  
  const input = document.getElementById('jornal-input').value;
  fetch('/journal?jornal-input='+input).then(response => response.text()).then((comments) => {
  comments = comments.replace("[","").replace("]","").split(",");
  for (i = 0; i<comments.length; i++) {

    const breakElement = document.createElement("br");

    document.getElementById("history").appendChild(createListElement(comments[i]));
    document.getElementById("history").appendChild(breakElement);
    document.getElementById("history").appendChild(breakElement);


  }

  });

}
*/
