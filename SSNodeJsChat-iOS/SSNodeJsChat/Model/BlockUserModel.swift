//
//  BlockUserModel.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 31/03/21.
//

import Foundation


@objcMembers
public class BlockUserModel: NSObject {
    var blockedBy: String = ""
    var blockedTo: String = ""
 
    var isBlock: Bool = false
    
     
    static func giveList(list: [[String:Any]]) -> [BlockUserModel] {
        var couponsArray = [BlockUserModel]()
        for cdic in list {
            couponsArray.append(BlockUserModel(cdic: cdic))
        }
        return couponsArray
    }
     
    init(cdic: [String: Any]) {
        
        blockedBy = cdic["blockedBy"] as? String ?? ""
        blockedTo = cdic["blockedTo"] as? String ?? ""
        isBlock = cdic["isBlock"] as? Bool ?? false
         
    }
}
