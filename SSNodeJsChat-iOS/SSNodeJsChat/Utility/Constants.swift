//
//  Constants.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 24/03/21.
//

import Foundation


class Constants {
    public class Request {
        public static var REQUEST_TYPE_KEY = "request"
        public static var REQUEST_TYPE_LOGIN = "login"
        public static var REQUEST_TYPE_CREATE_CONNECTION = "create_connection"
        public static var REQUEST_TYPE_ROOM = "room"
        public static var REQUEST_TYPE_USERS = "users"
        public static var REQUEST_TYPE_MESSAGE = "message"
        public static var REQUEST_TYPE_BLOCK_USER = "block_user"
    }
}

enum ResponseType: String {
    case checkRoom = "checkRoom"
    case createRoom = "createRoom"
    case allUsers = "allUsers"
    case roomsModified = "roomsModified"
    case allRooms = "allRooms"
    case message = "message"
    case login = "login"
    case loginOrCreate = "loginOrCreate"
    case userModified = "userModified"
    case roomsDetails = "roomsDetails"
    case blockUserModified = "blockUser"
    case allBlockUser = "allBlockUser"
}
