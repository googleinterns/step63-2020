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

// function revealLogin() {
//     console.log("fetching login status");
//     //fetches login staus
//     // document.getElementById("username").style.visibility = "hidden";

//     fetch('/login').then(response => response.json()).then((person) => {
//       console.log("is user logged in? " + person.status);
//       if(person.status == true){
//         var a = document.createElement('a');  
//         var link = document.createTextNode("Log out Here"); 
//         a.appendChild(link);  
//         a.title = "Log out Here";  
//         a.href = person.url;  
//         document.body.appendChild(a);
  
//         //after they logout, they are directed to the home page
//         // window.open("/_ah/logout?continue\u003d%2Flogin");


//       }
//       else if (person.status == false){
//         // var a = document.createElement('a');  
//         // var link = document.createTextNode("Log in Here"); 
//         // a.appendChild(link);  
//         // a.title = "Log in Here";  
//         // a.href = person.url;  
//         // document.body.appendChild(a);
        
//         window.open("/_ah/login?continue\u003d%2Flogin");

//         window.open("/nickname.html");

        
//         //after they login, they create their username
//         // document.getElementById("username").style.visibility = "visible";
//       }
//     });
//   }
  
function setNickname(){
  fetch('/nickname').then(response => response.json()).then((status) => {
    if(person.status == true){
      document.getElementById("name").value = status.name;

      window.open("/index.html");
    }
    else{
      var a = document.createElement('a');  
      var link = document.createTextNode("Log in Here"); 
      a.appendChild(link);  
      a.title = "Log in Here";  
      a.href = person.url;  
      document.body.appendChild(a);
      window.open("/login.html");
    }
  });
}

function revealLogin() {
  fetch('/login').then(response => response.json()).then((person) => {
    console.log("is user logged in? " + person.status);
    if(person.status == true){
      var a = document.createElement('a');  
      var link = document.createTextNode("Log out Here"); 
      a.appendChild(link);  
      a.title = "Log out Here";  
      a.href = person.url;  
      document.body.appendChild(a);
    }
    else if (person.status == false){
      var a = document.createElement('a');  
      var link = document.createTextNode("Log in Here"); 
      a.appendChild(link);  
      a.title = "Log in Here";  
      a.href = person.url;  
      document.body.appendChild(a);
    }
  });
}

function backHome(){
  var a = document.createElement('a');  
  var link = document.createTextNode("Home"); 
  a.appendChild(link);  
  a.title = "Home";  
  a.href = index.html;  
  // a.onclick = window.open("/index.html");
  document.body.appendChild(a);
}
