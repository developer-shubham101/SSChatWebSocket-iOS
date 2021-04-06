//
//  SocketObserver.swift
//  SSNodeJsChat
//
//  Created by Emizen Tech Subhash on 25/02/21.
//

import UIKit

protocol SocketObserver: class {
    
    func socketConnection(status: SocketConectionStatus)
    func brodcastSocketMessage(to observerWithIdentifire: ResponseType, statusCode: Int, data: [String:Any], message: String)
    
    func registerFor() -> [ResponseType] 
}
