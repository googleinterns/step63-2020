// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function revealLogin() {
  console.log("fetching");
  var url;
  fetch('/login').then(response => response.json()).then((person) => {
    console.log("is user logged in? " + person.status);
    if (person.status == false){
      window.open(person.url);
      console.log("logged in");
    }
    else if(person.status == true){
      window.open(person.url);
      console.log("logged out");
    }
  });
}

/*
var currentTime = new Date().getHours();
if (currentTime <= 5 | currentTime>20) {
    if (document.body) {
        document.body.style.backgroundImage = "./images/nigh.png";
    }
}
else if (currentTime <= 12 ) {
    if (document.body) {
        document.body.style.backgroundImage = "url('./images/homemorning.png')";
    }
}
else if (currentTime <= 16 ) {
    if (document.body) {
        document.body.style.backgroundImage = "./images/homepage.png";
    }
}
else if (currentTime <= 20 ) {
    if (document.body) {
        document.body.style.backgroundImage = "./images/canyon.jpg";
    }
}

*/