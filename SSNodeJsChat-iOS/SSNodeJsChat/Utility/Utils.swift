//
//  Utils.swift
//  Fahemni
//
//  Created by Rahul on 13/02/19.
//  Copyright © 2019 arka. All rights reserved.
//

import Foundation
import CommonCrypto
import AVKit

class Utils {
    
    public static func dayDifference(from interval : TimeInterval) -> String {
        let calendar = NSCalendar.current
        let date = Date(timeIntervalSince1970: interval)
        if calendar.isDateInYesterday(date) {
            return "Yesterday"
            
        }else if calendar.isDateInToday(date) {
            
            let unixTimestamp = TimeInterval.self
            
            print(unixTimestamp)
            
            let date = Date(timeIntervalSince1970: interval)
            let dateFormatter = DateFormatter()
            //                dateFormatter.timeZone = TimeZone(abbreviation: "GMT") //Set timezone that you want
            dateFormatter.locale = NSLocale.current
            dateFormatter.dateFormat = "hh:mm a" //Specify your format that you want
            let strDate = dateFormatter.string(from: date)
            print(strDate)
            
            return strDate
            
        } else {
            let unixTimestamp = interval
            
            
            let date = Date(timeIntervalSince1970: unixTimestamp)
            let dateFormatter = DateFormatter()
            //                dateFormatter.timeZone = TimeZone(abbreviation: "GMT") //Set timezone that you want
            dateFormatter.locale = NSLocale.current
            dateFormatter.dateFormat = "dd/MM" //Specify your format that you want
            let strDate = dateFormatter.string(from: date)
            
            return strDate
            
        }
    }
    public static func dayDifference(from date : Date) -> String {
        let calendar = NSCalendar.current
        if calendar.isDateInYesterday(date) {
            return "Yesterday"
        }else if calendar.isDateInToday(date) {
            
            let unixTimestamp = TimeInterval.self
            
            print(unixTimestamp)
            
            
            let dateFormatter = DateFormatter()
            //                dateFormatter.timeZone = TimeZone(abbreviation: "GMT") //Set timezone that you want
            dateFormatter.locale = NSLocale.current
            dateFormatter.dateFormat = "hh:mm a" //Specify your format that you want
            let strDate = dateFormatter.string(from: date)
            print(strDate)
            
            return strDate
            
        } else { 
            let dateFormatter = DateFormatter()
            //                dateFormatter.timeZone = TimeZone(abbreviation: "GMT") //Set timezone that you want
            dateFormatter.locale = NSLocale.current
            dateFormatter.dateFormat = "dd/MM" //Specify your format that you want
            let strDate = dateFormatter.string(from: date)
            
            return strDate
            
        }
    }
    public static func isEmail(value:String) -> Bool{
        let regex = try! NSRegularExpression(pattern: "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
        let range = NSRange(location: 0, length: value.utf16.count)
        return regex.firstMatch(in: value, range: range) != nil
        
    }
}

extension String {
    var isEmail:Bool {
        get {
            let regex = try! NSRegularExpression(pattern: "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
            let range = NSRange(location: 0, length: self.utf16.count)
            return regex.firstMatch(in: self, range: range) != nil
        }
        
    }
    
    var getMediaUrl: URL? {
        get {
            if (self == ""){
                return nil
            }
            return URL(string: "\(NetworkManager.STORAGE_URL)\(self)")
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
}



class ThumbNail {
//    static func getThumbnailFromUrl(_ url: String, defaultImage: Bool = true, _ completion: @escaping ((_ image: UIImage?)->Void)) {
//
//        let md5Hex =  Utils.strToMD5(string: url).map { String(format: "%02hhx", $0 as CVarArg) }.joined()
//        if let oldData: Data = FileUtils.readImageFromDir(fileUrl: md5Hex) {
//            completion(UIImage(data: oldData))
//
//            //            completion(UIImage.imageByMergingImages(topImage: UIImage(named: "tmp_placeholder_video")!, bottomImage: UIImage(data: oldData)!))
//        }else{
//            guard let url = URL(string: url) else { return }
//
//            DispatchQueue.global(qos: .utility).async {
//                let asset = AVAsset(url: url)
//                //            print(asset.duration)
//                let durationSeconds = CMTimeGetSeconds(asset.duration)
//                let assetImgGenerate = AVAssetImageGenerator(asset: asset)
//                assetImgGenerate.appliesPreferredTrackTransform = true
//
//
//                let time = CMTimeMakeWithSeconds(durationSeconds/3.0, preferredTimescale: 600)
//                do {
//                    let img: CGImage = try assetImgGenerate.copyCGImage(at: time, actualTime: nil)
//                    let thumbnail = UIImage(cgImage: img)
//
//                    FileUtils.saveIntoDir(fileUrl: md5Hex, data: thumbnail.pngData()!)
//                    completion(thumbnail)
//
//                } catch (let error) {
//                    print("Error :: ", error.localizedDescription)
//                    if defaultImage {
//                        completion(UIImage(contentsOfFile: "tmp_placeholder_video"))
//                    }else{
//                        completion(nil)
//                    }
//
//
//
//                }
//            }
//        }
//    }
    static func getThumbnailFromFile(_ url: String ) -> UIImage? {
        
        guard let url = URL(string: url) else { return nil }
        
        
        let asset = AVAsset(url: url)
        //            print(asset.duration)
        let durationSeconds = CMTimeGetSeconds(asset.duration)
        let assetImgGenerate = AVAssetImageGenerator(asset: asset)
        assetImgGenerate.appliesPreferredTrackTransform = true
        
        let time = CMTimeMakeWithSeconds(durationSeconds/3.0, preferredTimescale: 600)
        do {
            let img: CGImage = try assetImgGenerate.copyCGImage(at: time, actualTime: nil)
            let thumbnail = UIImage(cgImage: img)
            
            return thumbnail
            
        } catch (let error) {
            return nil
        }
    }
    
}
class PhotoUtils: NSObject {
    
    static func thumbnail(url:CFURL) -> UIImage {
        let src = CGImageSourceCreateWithURL(url, nil)
        return thumbnailImage(src: src!)
    }
    
    static func thumbnail(data imageData:CFData) -> UIImage {
        let src = CGImageSourceCreateWithData(imageData, nil)
        return thumbnailImage(src: src!)
    }
    
    static private func thumbnailImage(src: CGImageSource) -> UIImage {
        let scale = UIScreen.main.scale
        let w = (UIScreen.main.bounds.width / 3) * scale
        let d : [NSObject:AnyObject] = [
            kCGImageSourceShouldAllowFloat : true as AnyObject,
            kCGImageSourceCreateThumbnailWithTransform : true as AnyObject,
            kCGImageSourceCreateThumbnailFromImageAlways : true as AnyObject,
            kCGImageSourceThumbnailMaxPixelSize : w as AnyObject
        ]
        let imref = CGImageSourceCreateThumbnailAtIndex(src, 0, d as CFDictionary)
        return UIImage(cgImage: imref!, scale: scale, orientation: .up)
    }
    
}
