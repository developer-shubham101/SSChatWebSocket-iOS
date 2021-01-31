// http://ejohn.org/blog/ecmascript-5-strict-mode-json-and-more/
"use strict";

// Optional. You will see this name in eg. 'ps' or 'top' command
process.title = 'node-chat';

// Port where we'll run the websocket server
var webSocketsServerPort = 1337;

// websocket and http servers
var webSocketServer = require('websocket').server;
var http = require('http');
var mongoose = require('mongoose');





var dbUrl = 'mongodb://127.0.0.1:27017/ReactChat';


var UsersModel = mongoose.model('Users', {
	// name: String,
	// id: String,
	userName: String,
	password: String
});

const messageSchema = mongoose.Schema({
	roomId: String,
	media: String,
	message: String,
	receverId: String,
	time: String,
	type: String,
	senderId: String,
	messageContent: Object
});

var MessageSchema = {
	roomId: String,

	message: String,
	message_type: String,

	media: String,

	receverId: String,
	time: String,

	senderId: String,
	message_content: Object
};

var MessageModel = mongoose.model('Message', MessageSchema);



var RoomModel = mongoose.model('Room', {
	users: Object,
});


/**
 * Global variables
 */
// latest 100 messages
var history = [];
// list of currently connected clients (users)
var clients = [];

/**
 * Helper function for escaping input strings
 */
function htmlEntities(str) {
	return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;')
		.replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

// Array with some colors
var colors = ['red', 'green', 'blue', 'magenta', 'purple', 'plum', 'orange'];
// ... in random order
colors.sort(function (a, b) { return Math.random() > 0.5; });

/**
 * HTTP server
 */
var server = http.createServer(function (request, response) {
	// Not important for us. We're writing WebSocket server, not HTTP server
});
server.listen(webSocketsServerPort, function () {
	console.log((new Date()) + " Server is listening on port " + webSocketsServerPort);
});

/**
 * WebSocket server
 */
var wsServer = new webSocketServer({
	// WebSocket server is tied to a HTTP server. WebSocket request is just
	// an enhanced HTTP request. For more info http://tools.ietf.org/html/rfc6455#page-6
	httpServer: server
});



var responseError = (code, message, toString) => {
	let data = {
		errorCode: code,
		message: message,
		data: {}
	};
	if (toString) {
		return JSON.stringify(data);
	} else {
		return data;
	}

}

var responseSuccess = (code, dataObject, message, toString) => {
	let data = {
		errorCode: code,
		message: message,
		data: dataObject
	};
	if (toString) {
		return JSON.stringify(data);
	} else {
		return data;
	}

}

var MsgModel = undefined;
function messageRequest(request) {

	// accept connection - you should check 'request.origin' to make sure that
	// client is connecting from your website
	// (http://en.wikipedia.org/wiki/Same_origin_policy)
	var connection = request.accept(null, request.origin);
	// we need to know client index to remove them on 'close' event


	console.log((new Date()) + ' Connection accepted message.');


	// user sent some message
	connection.on('message', function (message) {
		console.log("On Message on login" + message.utf8Data);
		// console.log("On Message on login" + );
		// UsersModel.remove({}, (err, messages) => {
		// 	//res.send(messages);
		// 	console.log(`On connect Error:::${err} users:::`, messages);
		// });


		var key = request.resourceURL.query.key
		console.log('key-----', key);
		// console.log('req body ', request.body);


		let requestData = JSON.parse(message.utf8Data);

		// let MsgModel = mongoose.model(`message_${key}`, MessageSchema);


		console.log("MsgModel:::", MsgModel);
		if (MsgModel == undefined) {
			MsgModel = mongoose.model(`message_${key}`, MessageSchema);
		}


		if (requestData.type == 'allMessage') {

			let findObject = {};

			MsgModel.find(findObject, (err, messages) => {
				//res.send(messages);
				console.log(`On connect Error:::${err} data:::`, messages);
				// connection.sendUTF(`user login successfully ${messages}`);

				if (messages && messages.length > 0) {
					console.log(`Room Data Found....`);
					connection.sendUTF(JSON.stringify(messages));
				} else {
					connection.sendUTF(responseError(404, "Data not found.", true));
				}
			});

		} else if (requestData.type == 'addMessage') {

			let messageData = requestData;
			console.log("Message Data" + messageData);


/* 
roomId: String,

message: String,
message_type: String,

media: String,

receverId: String,
time: String,

senderId: String,
message_content: Object
*/

			var user = new MsgModel({
				roomId: messageData.roomId,

				message: messageData.message,
				message_type: "TXT",

				media: "",
				
				receverId: 12,
				time: "asdasd",
				
				senderId: 33,
				message_content: { "contactName": "Shubham Sharma", "contactNo": "7877462405" }
			});
			// try {
			user.save().then((savedMessage) => {
				console.log(`Message Saved.`, savedMessage);
			}).catch((ex) => {
				console.error(`Message Failed to Saved.`, ex);
			});
			// }catch(ex){
			// 	console.log("Save message error:: ",ex);
			// }

		}



	});

	// user disconnected
	connection.on('close', function (connection) {
		console.log((new Date()) + 'connection closed');
	});
}

function roomRequest(request) {

	// accept connection - you should check 'request.origin' to make sure that
	// client is connecting from your website
	// (http://en.wikipedia.org/wiki/Same_origin_policy)
	var connection = request.accept(null, request.origin);
	// we need to know client index to remove them on 'close' event


	console.log((new Date()) + ' Connection accepted room.');


	// user sent some message
	connection.on('message', function (message) {
		console.log("On Message on login" + message.utf8Data);

		let requestData = JSON.parse(message.utf8Data);

		// conditions

		if (requestData.type == 'allRooms') {
			if (!requestData.userList) {
				connection.sendUTF(JSON.stringify({ error: true, message: "Please enter user id" }));
			}
			let userList = requestData.userList;

			let findObject = {};
			userList.forEach((element) => {
				findObject[`users.${element}`] = true;
			});

			console.log(findObject);
			RoomModel.find(findObject, (err, messages) => {
				//res.send(messages);
				console.log(`On connect Error:::${err} data:::`, messages);
				// connection.sendUTF(`user login successfully ${messages}`);

				if (messages && messages.length > 0) {
					console.log(`Room Data Found....`);
					let usersList = [];
					messages.forEach((element) => {
						usersList = usersList.concat(Object.keys(element.users));
					});

					usersList = [...new Set(usersList)];

					let fondData = { _id: { $in: usersList } };
					// { "userName": requestData.userName, "password": requestData.password };
					UsersModel.find(fondData, (err, userList) => {

						let responseData = { roomList: messages, userList: userList };
						console.log(`responseData::: `, responseData);
						connection.sendUTF(responseSuccess(200, responseData, "Data Found", true));
						// userList
					});

					// console.log("usersList:::::::", usersList);

				} else {
					connection.sendUTF(responseError(404, "Not Found", true));
				}
			});
		} else if (requestData.type == 'createRoom') {

			var userList = requestData.users;
			console.log("userList", userList);
			// var roomType = requestData.roomType;
			let findObject = {};
			userList.forEach((element) => {
				findObject[`users.${element}`] = true;
			});

			var room = new RoomModel(findObject);

			room.save().then((savedMessage) => {
				console.log(`Room Saved.`, savedMessage);

			}).catch((ex) => {
				console.error(`Room Failed to Saved.`, ex);
			});
			connection.sendUTF(JSON.stringify({ message: "no user found" }));
		}




	});

	// user disconnected
	connection.on('close', function (connection) {
		console.log((new Date()) + 'connection closed');
	});
}

function loginRequest(request) {

	// accept connection - you should check 'request.origin' to make sure that
	// client is connecting from your website
	// (http://en.wikipedia.org/wiki/Same_origin_policy)
	var connection = request.accept(null, request.origin);
	// we need to know client index to remove them on 'close' event


	console.log((new Date()) + ' Connection accepted login.');


	// user sent some message
	connection.on('message', function (message) {
		console.log("On Message on login" + message.utf8Data);

		console.log(typeof message.utf8Data);

		let requestData = JSON.parse(message.utf8Data);

		if (requestData.type == 'login') {
			let fondData = { "userName": requestData.userName, "password": requestData.password };
			UsersModel.find(fondData, (err, messages) => {
				//res.send(messages);
				console.log(`On connect Error:::${err} users:::`, messages);
				if (messages && messages.length > 0) {
					console.log(`user login successfully`);
					connection.sendUTF(JSON.stringify(messages[0]));
				} else {
					connection.sendUTF(responseError(401, "Unauthorized", true));

				}
			});
		} else if (requestData.type == 'register') {

			let fondData = { "userName": requestData.userName, "password": requestData.password };
			var user = new UsersModel(fondData);
			user.save().then((savedMessage) => {
				console.log(`User Saved.`, savedMessage);
				connection.sendUTF(JSON.stringify(savedMessage));
			}).catch((ex) => {
				console.error(`User Failed to Saved.`, ex);

				connection.sendUTF(JSON.stringify({ "error": true }));
			});
		} else {
			connection.sendUTF(responseError(404, "Action/Path not found.", true));
		}


	});

	// user disconnected
	connection.on('close', function (connection) {
		console.log((new Date()) + 'connection closed');
	});
}


function registerRequest(request) {

	var connection = request.accept(null, request.origin);
	let requestData = JSON.parse(message.utf8Data);
	var user = new UsersModel(requestData);

	user.save().then((savedMessage) => {
		console.log(`User Saved.`, savedMessage);
		connection.sendUTF(JSON.stringify(savedMessage));
	}).catch((ex) => {
		console.error(`User Failed to Saved.`, ex);

		connection.sendUTF(JSON.stringify({ "error": true }));
	});
}

function allUser(request) {

	var connection = request.accept(null, request.origin);
	//let requestData = JSON.parse(message.utf8Data);

	// user sent some message
	connection.on('message', function (message) {

		UsersModel.find({}, (err, messages) => {
			//res.send(messages);
			console.log(`On connect Error:::${err} users:::`, messages);
			if (messages && messages.length > 0) {
				console.log(`user login successfully`);
				connection.sendUTF(JSON.stringify(messages));
			} else {
				connection.sendUTF(responseError(404, "Not Found", true));

			}
		});
	});

	// user disconnected
	connection.on('close', function (connection) {
		console.log((new Date()) + 'connection closed');
	});
}

function chatRequest(request) {

	// accept connection - you should check 'request.origin' to make sure that
	// client is connecting from your website
	// (http://en.wikipedia.org/wiki/Same_origin_policy)
	var connection = request.accept(null, request.origin);
	// we need to know client index to remove them on 'close' event
	var index = clients.push(connection) - 1;
	var userName = false;
	var userColor = false;

	console.log((new Date()) + ' Connection accepted.');

	// send back chat history
	if (history.length > 0) {
		connection.sendUTF(JSON.stringify({ type: 'history', data: history }));
	}

	// user sent some message
	connection.on('message', function (message) {
		console.log("On Message on chat" + JSON.stringify(message));

		// UsersModel.find({}, (err, messages) => {
		// 	//res.send(messages);
		// 	console.log(`On connect Error:::${err} users:::`, messages);
		// });


		if (message.type === 'utf8') { // accept only text
			if (userName === false) { // first message sent by user is their name
				// remember user name
				userName = htmlEntities(message.utf8Data);
				// get random color and send it back to the user
				userColor = colors.shift();
				connection.sendUTF(JSON.stringify({ type: 'color', data: userColor }));
				console.log((new Date()) + ' User is known as: ' + userName
					+ ' with ' + userColor + ' color.');

			} else { // log and broadcast the message
				console.log((new Date()) + ' Received Message from '
					+ userName + ': ' + message.utf8Data);

				// we want to keep history of all sent messages
				var obj = {
					time: (new Date()).getTime(),
					text: htmlEntities(message.utf8Data),
					author: userName,
					color: userColor
				};
				history.push(obj);
				history = history.slice(-100);

				// broadcast message to all connected clients
				var json = JSON.stringify({ type: 'message', data: obj });
				for (var i = 0; i < clients.length; i++) {
					console.log("Message Data Sent To All Users");
					clients[i].sendUTF(json);
				}
			}
		}
	});

	// user disconnected
	connection.on('close', function (connection) {
		if (userName !== false && userColor !== false) {
			console.log((new Date()) + " Peer "
				+ connection.remoteAddress + " disconnected.");
			// remove user from the list of connected clients
			clients.splice(index, 1);
			// push back user's color to be reused by another user
			colors.push(userColor);
		}
	});
}
function originIsAllowed(request) {
	let requestPath = request.resourceURL.pathname.replace(/\/$/, "");
	// put logic here to detect whether the specified origin is allowed.
	console.log(`origin:::   ${requestPath}`);
	if ("/chat" === requestPath) {
		chatRequest(request);
		return true;
	} else if ("/login" === requestPath) {
		loginRequest(request);
		return true;
	} else if ("/room" === requestPath) {
		roomRequest(request);
		return true;
	} else if ("/message" === requestPath) {
		console.log('request........', request.resourceURL.query.key);
		messageRequest(request);
		return true;
	} else if ('/register' === requestPath) {
		registerRequest(request);
		return true;
	} else if ('/users' === requestPath) {
		allUser(request);
		return true;
	}

	return false

}
// This callback function is called every time someone
// tries to connect to the WebSocket server
wsServer.on('request', function (request) {
	console.log((new Date()) + ' Connection from origin ' + request.origin + '.');
	// console.log((new Date()) + ' Connection from origin ' + JSON.stringify(request) + '.');

	if (!originIsAllowed(request)) {
		// Make sure we only accept requests from an allowed origin
		request.reject();
		console.log((new Date()) + ' Connection from origin ' + request.origin + ' rejected.');
		return;
	}
	// chatRequest(request);
});


mongoose.connect(dbUrl,
	{
		useNewUrlParser: true,
		useUnifiedTopology: true,

	}, (err) => {
		console.log('mongodb connected', err);
	})
