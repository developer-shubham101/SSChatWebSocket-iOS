//
//  UserModel.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 24/01/21.
//

import Foundation
@objcMembers
class UserModel: NSObject {
    
    var id:String = ""
    var userName:String = ""
   
    
    
    static func giveList(list:[[String:Any]]) -> [UserModel] {
        var couponsArray = [UserModel]()
        for cdic in list {
            couponsArray.append(giveObj(cdic: cdic))
        }
        return couponsArray
    }
    static func giveObj(cdic:[String:Any]) -> UserModel{
        let resObj = UserModel()
        
        resObj.id = cdic["_id"] as? String ?? ""
        resObj.userName = cdic["userName"] as? String ?? "" 
        
        return resObj
    }
}

 
