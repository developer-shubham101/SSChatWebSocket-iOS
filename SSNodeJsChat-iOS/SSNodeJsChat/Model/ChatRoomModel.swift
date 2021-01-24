//
//  ChatRoomModel.swift
//  Fahemni
//
//  Created by Rahul on 05/02/19.
//  Copyright © 2019 arka. All rights reserved.
//

import Foundation
@objcMembers
class ChatRoomModel: NSObject {
    
    var id: String = ""
    var isGroup: Bool = false
    var groupDetail: GroupModel?
    var individualDetail: IndividualModel?
    
    static func giveList(list: [[String:Any]], userId: String) -> [ChatRoomModel] {
        var couponsArray = [ChatRoomModel]()
        for cdic in list {
            couponsArray.append(ChatRoomModel(disc: cdic, userId: userId))
        }
        return couponsArray
    }
    init(disc: [String: Any], userId: String) {
        id = disc["_id"] as? String ?? ""
        
        let tmpUsers = disc["users"] as? [String: Bool] ?? [:]
        
        let users: [String] = Array(tmpUsers.keys)
        
        individualDetail = IndividualModel(disc: [:])
        let user: [String] = users.filter({ (element) -> Bool in
            return element != userId
        })
        
        let name = RoomListViewController.userDetailsList.first { (element) -> Bool in
            return user[0] == element.id
        }
        individualDetail?.userData = name
    }
    
    
}
@objcMembers
class GroupModel: NSObject {
    
    var name: String = "Sample"
    var users: [String] = []
    
    init(disc: [String:Any]) {
        
    }
}

@objcMembers
class IndividualModel: NSObject {
    
    var userData: UserDetailsModel?
    
    init(disc: [String:Any]) {
        
    }
}

@objcMembers
class ReceiverDetail: NSObject {
    var gender:String = ""
    var id:NSNumber = 0
    var location:String = ""
    var profile_picture:String = ""
    var role:String = ""
    var title:String = ""
    var username:String = "asdasd"
    
    static func giveList(list:[[String:Any]]) -> [ReceiverDetail] {
        var couponsArray = [ReceiverDetail]()
        for cdic in list {
            couponsArray.append(giveObj(cdic: cdic))
        }
        return couponsArray
    }
    static func giveObj(cdic:[String:Any]) -> ReceiverDetail{
        let resObj = ReceiverDetail()
        resObj.id = cdic["id"]! as! NSNumber
        
        resObj.profile_picture = cdic["profile_picture"]! as! String
        resObj.title = cdic["title"]! as! String
        resObj.username = cdic["username"]! as! String
        resObj.location = cdic["location"]! as! String
        resObj.role = cdic["role"]! as! String
        resObj.gender = cdic["gender"]! as! String
        return resObj
    }
}





@objcMembers
class UserDetailsModel: NSObject {
    var userName: String = ""
    var id: String = ""
     
    static func giveList(list: [[String:Any]]) -> [UserDetailsModel] {
        var couponsArray = [UserDetailsModel]()
        for cdic in list {
            couponsArray.append(giveObj(cdic: cdic))
        }
        return couponsArray
    }
    static func giveObj(cdic:[String:Any]) -> UserDetailsModel {
        let resObj = UserDetailsModel()
        
        resObj.id = cdic["_id"] as? String ?? ""
        
        resObj.userName = cdic["userName"] as? String ?? ""
        
        return resObj
    }
}
