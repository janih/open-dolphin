<%--
  A simple page that sends a value change to the server.
  It does not depend on OpenDolphin but is an example of whatever small (IoT) device that
  can merely send an HTTP GET.
  The point of the demo is to show that even such a basic device can perfectly integrate
  with any OpenDolphin application no matter whether it uses web or desktop clients.
  author: dierk koenig
--%>

<%
  velocityUrl = createLink(controller: 'setVelocity', action: 'set')
  range       = "range"
  label       = "valueLabel"
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta name="layout" content="demo">
  <title>Dolphin.js Setting the velocity Page</title>
</head>

<body>

<div class="container">
  <h3>A no-dolphin event provider</h3>
<p>Use the slider or <br>
  tilt your device if it supports device orientation.
</p>

<input id="${range}" type="range" value="0" min="0" max="100">
<div id="${label}"></div>

<hr>
<a href="https://github.com/canoo/open-dolphin/blob/master/dolphin-grails/grails-app/views/dolphinjs/velocity.gsp">source code</a>
</div>
<script >

var request = new XMLHttpRequest();
var url = "${velocityUrl}";

// update the label and send slider changes to the server
var rangeInput = document.getElementById("${range}");
rangeInput.addEventListener("input", function () {
  document.getElementById("${label}").innerHTML = "value: "+rangeInput.value;
  request.open('GET', url + "?value=" + rangeInput.value, true);
  request.send();
});

// send tilt changes to the server
if (window.DeviceOrientationEvent) {
  var lastOrientation = 0;
  console.log("DeviceOrientation is supported");
  window.addEventListener('deviceorientation', function (eventData) {
    // gamma is the left-to-right tilt in degrees, where right is positive
    var newOrientation = (Math.floor(Number(eventData.gamma))) % 100;
    if (newOrientation !== lastOrientation) {
      lastOrientation = newOrientation;
      request.open('GET', url + "?value=" + newOrientation, true);
      document.getElementById("${label}").innerHTML = "value: "+newOrientation;
      request.send();
    }
  }, true);
}

</script>

</body>
</html>