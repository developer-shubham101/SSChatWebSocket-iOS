//
//  StringExtensions.swift
//  DragonFit
//
//  Created by Ajit Jain on 25/03/21.
//  Copyright © 2021 Devpoint. All rights reserved.
//

import Foundation
import CommonCrypto

extension String {
    
    var capitalizingFirstLetter: String  {
        get {
             return prefix(1).uppercased() + self.lowercased().dropFirst()
        }
    }
   
    
    mutating func capitalizeFirstLetter() {
        self = self.capitalizingFirstLetter
    }
    
    
    var isEmail:Bool {
        get {
            let regex = try! NSRegularExpression(pattern: "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
            let range = NSRange(location: 0, length: self.utf16.count)
            return regex.firstMatch(in: self, range: range) != nil
        }
        
    }
    
    var isPhone:Bool {
        get {
            let regex = try! NSRegularExpression(pattern: "^[0-9]{8,15}$")
            let range = NSRange(location: 0, length: self.utf16.count)
//            return self.count > 8 && self.count < 15 //regex.firstMatch(in: self, range: range) != nil
            return regex.firstMatch(in: self, range: range) != nil
        }
        
    }
     
//    var getMediaUrl: URL? {
//        get {
//            if (self == ""){
//                return nil
//            }
//            return URL(string: "\(NetworkManager.STORAGE_URL)\(self)")
//        }
//    }
    
    
    
    var toURL: URL? {
        get {
            if (self == ""){
                return nil
            }
            return URL(string: self)
        }
    }
    
    func toMD5() -> String {
        let messageData = self.data(using:.utf8)!
        var digestData = Data(count: Int(CC_MD5_DIGEST_LENGTH))
        
        _ = digestData.withUnsafeMutableBytes {digestBytes in
            messageData.withUnsafeBytes {messageBytes in
                CC_MD5(messageBytes, CC_LONG(messageData.count), digestBytes)
            }
        }
        
        return digestData.map { String(format: "%02hhx", $0 as CVarArg) }.joined()
    }
    
    
    public func toDate(format:String = "yyyy-MM-dd HH:mm:ss") -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        return dateFormatter.date(from: self)
    }
    
    
}


extension StringProtocol {
    subscript(offset: Int) -> Character { self[index(startIndex, offsetBy: offset)] }
    subscript(range: Range<Int>) -> SubSequence {
        let startIndex = index(self.startIndex, offsetBy: range.lowerBound)
        return self[startIndex..<index(startIndex, offsetBy: range.count)]
    }
    subscript(range: ClosedRange<Int>) -> SubSequence {
        let startIndex = index(self.startIndex, offsetBy: range.lowerBound)
        return self[startIndex..<index(startIndex, offsetBy: range.count)]
    }
    subscript(range: PartialRangeFrom<Int>) -> SubSequence { self[index(startIndex, offsetBy: range.lowerBound)...] }
    subscript(range: PartialRangeThrough<Int>) -> SubSequence { self[...index(startIndex, offsetBy: range.upperBound)] }
    subscript(range: PartialRangeUpTo<Int>) -> SubSequence { self[..<index(startIndex, offsetBy: range.upperBound)] }
}
