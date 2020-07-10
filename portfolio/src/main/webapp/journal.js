
function populateAnalysis() {
  
  const input = document.getElementById('jornal-input').value;
  fetch('/journal?jornal-input='+input);

  }


