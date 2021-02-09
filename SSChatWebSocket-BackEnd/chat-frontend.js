$(function () {
	"use strict";

	 
	// if user is running mozilla then use it's built-in WebSocket
	window.WebSocket = window.WebSocket || window.MozWebSocket;

 
	// open connection
	var connection = new WebSocket('ws://127.0.0.1:1337/v1');
	 


	connection.onmessage = function (message) {
		console.log("onmessage ", message.data);
	};

  

	setTimeout(() => {
		// connection.send("Shubham");
		connection.send(JSON.stringify({
			request: "login",
			type: "login",
			userName: "shubham@yopmail.com",
			password: "123456",
		}));
	}, 1000);


	setTimeout(() => {
		// connection.send("Shubham");
		connection.send(JSON.stringify({
			request: "login",
			type: "register",
			userName: "vimal@yopmail.com",
			password: "123456",
			userId: 33
		}));
	}, 2000);

	connection.onopen = function () {
		console.log("collecion onopen ", );
	};
 

});