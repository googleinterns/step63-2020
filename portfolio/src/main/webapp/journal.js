////                
//wk3pt6

//fetches comment array from data servlet and inserts comments into html
function populateAnalysis() {

    document.getElementById("history").innerText = '';

    const entry = document.getElementById('journal-input').value;
    fetch('/journal?journal-input=' + entry).then(response => response.text()).then((comments) => {
        comments = comments.replace(/\\[|\\]|"/g, "").split(",")
        var i = 0;
        var commlen = comments.length-1;
        while (i < comments.length-1) {
            console.log("General while loop");
            console.log("The current length is "+i);
            console.log("The max length is "+commlen);
            const breakElement = document.createElement("br");
                //console.log(comments[i]);
                //console.log(comments[i].includes("NEW ENTRY"));
                //console.log(i);
                if ( comments[i].includes("NEW ENTRY")) {
                document.getElementById("history").appendChild(breakElement);
                document.getElementById("history").appendChild(breakElement);
                document.getElementById("history").appendChild(breakElement);

                var entryList = [];

                //var skip = 0;
                i++;
                while ((comments[i].includes("NEW ENTRY") == false)&& i<comments.length-1) {
                    console.log("Includes while loop");
                    console.log("The current length is "+i);
                    i ++;
                    entryList.push(comments[i]);
                }


                document.getElementById("history").appendChild(constructEntry(entryList));

                //document.getElementById("history").appendChild(entryList);
                //i += skip;
                    
                
                /** 
                if (i + 1 == comments.length) {
                    const listElement = document.createElement('li');
                    listElement.innerText = comments[i];
                } else if (i < comments.length) {
                    document.getElementById("history").appendChild(createListElement(comments[i], comments[i + 1]));
                }

                document.getElementById("history").appendChild(breakElement);
                document.getElementById("history").appendChild(breakElement);
                **/

            }else {
                i++;
            }
            


        } 
    });

}





function constructEntry(inputList) {
    const pElement = document.createElement('p');
    //const testElem = document.createElement('li');
    //testElem.innerText = "testing....";
    //document.getElementById("history").appendChild(testElem);
    inputList.reverse();
    inputList.push("END OF ENTRY");
    console.log(inputList);
    for (i=0; i<inputList.length-1; i++){
        if (inputList[i+1].slice(0, 2) == "0."  | inputList[i+1].slice(0, 3) == "-0.") {
            var score = "";
            try{
            score = parseFloat(inputList[i+1]);
            } finally {
            score = parseFloat(inputList[i+1].slice(0,inputList[i+1].length-1));
            }
            const divElement = document.createElement('span');
             if (score < -0.7) {
                divElement.id = "dark-red";
            } else if (score < -0.25) {
                divElement.id = "red";
            } else if (score < 0.0) {
                divElement.id = "yellow";
            } else if (score == 0.0) {
                divElement.id = "grey";
            } else if (score < 0.25) {
                divElement.id = "light-blue";
            } else if (score < 0.7) {
                divElement.id = "light-green";
            } else if (score > 0.7) {
                divElement.id = "green";
            }
            if (inputList[i] == ("\\"+"r")){
            pElement.appendChild(document.createElement('br'));
            } else if (inputList[i].length >= 12 &&(inputList[i].slice(0,12)== "The weighted" | inputList[i].slice(0,11) == "The average")){
                   pElement.appendChild(document.createElement('br'));
                   divElement.innerText = inputList[i+1];
                   var scoreId = divElement.id;
                   var newId = scoreId+"-score";
                   divElement.id = newId;
                   if (inputList[i].slice(0,12)== "The weighted"){
                       var weightedHeader = document.createElement('div');
                       weightedHeader.innerText = "WT. AVG.";
                       pElement.appendChild(weightedHeader);
                   }else{
                       var averageHeader = document.createElement('div');
                       averageHeader.innerText = "AVG.";
                       pElement.appendChild(averageHeader);
                   }
                   
                   pElement.appendChild(divElement);
    

            }
            else {
            console.log(inputList[i]);
            console.log(inputList[i].slice(0,12)== "The weighted");
            console.log(inputList[i].slice(0,11) == "The average");
            divElement.innerText = inputList[i];
            pElement.appendChild(divElement);
            }
        
        }
        else {
            
           if (inputList[i] != "Care to talk about that?" && inputList[i].slice(0, 2) != "0."  && inputList[i].slice(0, 3) != "-0." && inputList[i] != "NEW ENTRY") {
           pElement.appendChild(document.createElement('br'));
           const dividerElement = document.createElement('span');
           dividerElement.innerText = inputList[i]; 
           pElement.appendChild(dividerElement);
           }
        }
    }
    console.log("done");
    pElement.id = "entry-text";
    return pElement;
}


/** Creates an <li> element containing text. */
function createListElement(text, nextElement) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    if (nextElement.slice(0, 2) == "0." | nextElement.slice(0, 3) == "-0.") {
        var score = parseFloat(nextElement);
        if (score < -0.7) {
            liElement.id = "dark-red";
        } else if (score < -0.25) {
            liElement.id = "red";
        } else if (score < 0.0) {
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

function getDate() {
    var today = String(new Date());
    document.getElementById('time').innerHTML = "•  "+today.slice(0, 10)+"  •";
}
window.onload = populateAnalysis;
window.onload = getDate;