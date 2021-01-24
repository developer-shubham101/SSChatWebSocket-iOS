//
//  JsonOperation.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 26/12/20.
//

import Foundation
class JsonOperation {
    public static func toJsonStringFrom(dictionary: [String:Any]) -> NSString? {
        let jsonData = try! JSONSerialization.data(withJSONObject: dictionary)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
        return jsonString
    }
    public static func toJsonStringFrom(array: [String]) -> NSString? {
        let jsonData = try! JSONSerialization.data(withJSONObject: array)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
        return jsonString
    }
    static func toDictionaryFrom(string: String) -> [String: Any]? {
        if let data = string.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
}


extension Dictionary {
    public static func toJsonStringFrom() -> NSString? {
        let jsonData = try! JSONSerialization.data(withJSONObject: self)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
        return jsonString
    }
}
