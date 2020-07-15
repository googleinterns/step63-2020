// E0707 16:49:28.866267 2373771 loas_util.cc:489] NOT_FOUND: No user creds available. isu: , user: /run/credentials-cache/loas-erysd/cookies/erysd.loas2credentials: No such file.
// E0707 16:49:28.874190 2373819 loas_util.cc:489] NOT_FOUND: No user creds available. isu: , user: /run/credentials-cache/loas-erysd/cookies/erysd.loas2credentials: No such file.
// E0707 16:49:28.912895 2373771 loas_util.cc:472] NOT_FOUND: No user creds available. isu: , user: /run/credentials-cache/loas-erysd/cookies/erysd.loas2credentials: No such file.
// E0707 16:49:28.913787 2373830 loas_util.cc:489] NOT_FOUND: No user creds available. isu: , user: /run/credentials-cache/loas-erysd/cookies/erysd.loas2credentials: No such file.
// E0707 16:49:28.930017 2373849 loas_util.cc:489] NOT_FOUND: No user creds available. isu: , user: /run/credentials-cache/loas-erysd/cookies/erysd.loas2credentials: No such file.
// E0707 16:49:28.980258 2373863 loas_util.cc:489] NOT_FOUND: No user creds available. isu: , user: /run/credentials-cache/loas-erysd/cookies/erysd.loas2credentials: No such file.
// E0707 16:49:28.981930 2373863 loas_util.cc:489] NOT_FOUND: No user creds available. isu: , user: /run/credentials-cache/loas-erysd/cookies/erysd.loas2credentials: No such file.
// E0707 16:49:28.990588 2373863 internals-authprotocolparser.cc:1165] Could not pre-initialize server: AUTH_FAIL - No user creds available. isu: , user: /run/credentials-cache/loas-erysd/cookies/erysd.loas2credentials: No such file.
// E0707 16:49:28.992697 2373863 loas_util.cc:489] NOT_FOUND: No user creds available. isu: , user: /run/credentials-cache/loas-erysd/cookies/erysd.loas2credentials: No such file.
/**
 * @fileoverview Description of this file.
 */
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

// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">

// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">

var map;
var service;
var infowindow;

// var mykey = config.API_KEY;


function initMap() {
  var pyrmont = new google.maps.LatLng(-33.8665433,151.1956316);
  var loc;
  var geocoder = new google.maps.Geocoder();

  map = new google.maps.Map(document.getElementById('map'), {
      center: pyrmont,
      zoom: 15
    });

  document.getElementById("submit").addEventListener("click", function() {
    var address = document.getElementById("address").value;
    geocoder.geocode({ address: address }, function(results, status) {
      if (status === "OK") {
        loc = new google.maps.LatLng(results[0].geometry.location.lat(), results[0].geometry.location.lng());
        map.setCenter(results[0].geometry.location);
      } else {
        alert("Geocode was not successful for the following reason: " + status);
      }
      var request = {
        location: loc,
        radius: '500',
        query: 'therapist',
        fields: ["name", "geometry", "formatted_address"]
      };

      service = new google.maps.places.PlacesService(map);
      service.textSearch(request, callback);
    });
  });

}

function callback(results, status) {
  if (status == google.maps.places.PlacesServiceStatus.OK) {
    for (var i = 0; i < results.length; i++) {
      createMarker(results[i]);
    }
  }
}

function createMarker(place) {
    var marker = new google.maps.Marker({
      map: map,
      position: place.geometry.location,
      title: place.name
    });
    console.log("phone: " + place.formatted_phone_number);

    var conStr = "<p><b>" + place.name + "</b></p>" + "<p><b>Address: </b>" + place.formatted_address + "</p>";
    var infowindow = new google.maps.InfoWindow({
      content: conStr
    });
  
    google.maps.event.addListener(marker, "click", function() {
      infowindow.open(map, marker);
    });
  }
  