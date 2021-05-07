//
//  Messages.swift
//  SSNodeJsChat
//
//  Created by Emizen Tech Subhash on 25/02/21.
//

import Foundation
import Starscream

protocol SocketMessage {
    func connectSocket(notify: Bool)
    func registerToScoket(observer: SocketObserver)
    func unregisterToSocket(observer: SocketObserver)
    func sendMessageToSocket(message:String)
}
