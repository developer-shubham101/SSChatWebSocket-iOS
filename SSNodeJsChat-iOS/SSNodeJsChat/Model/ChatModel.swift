//
//  ChatModel.swift
//  World Album
//
//  Created by Shubham Sharma on 06/04/20.
//  Copyright © 2020 Shubham Sharma. All rights reserved.
//

import Foundation
import UIKit 
 

enum DownloadStart {
	case pending
	case downloading
	case downloaded
}


//public struct UserElement {
//    public var email: String = ""
//    public var userID: String = ""
//    public var name: String = ""
//    public var profile_image: String = ""
//}

public class ChatModel {
	
	var documentId: String = ""
	var message: String = ""
	var message_type: MessageType = .text
	var message_on: String = ""
	//    var receiver_detail:CharUser = CharUser()
	var sender_detail: UserDetailsModel = UserDetailsModel()
	
	var tableCellWidth: CGFloat = 0
	var tableCellHeight: CGFloat = 0
	
	
	var message_content: Any?
	var message_content_dict: [String: Any]?
	
	var downloadStatus: DownloadStart = .pending
	
 
//	public var createdTime:String = ""
	
	//	public var height: CGFloat = 0title
	public var createdDate: Date = Date()
	
	
	required public init() {}
	
	required public init(documentId: String, data: [String: Any], senderDetail: UserDetailsModel, frame: CGRect) {
 
		self.documentId = documentId
		message = data["message"] as! String
		self.message_type = MessageType(rawValue: data["message_type"] as! String) ?? .text
		if self.message_type == .text {
			let cgsiz = CGSize(width: (frame.width - 90), height: 1000)
			let att = [NSAttributedString.Key.font: UIFont.appFont(size: 14, weight: .regular)]
			let estFram = NSString(string: message).boundingRect(with: cgsiz, options: .usesLineFragmentOrigin, attributes: att, context: nil)
			
			let height: CGFloat = estFram.height + 55
			
			tableCellWidth = estFram.width
			tableCellHeight = height
		}
		/*else if self.message_type == .replay {
			let cgsiz = CGSize(width: (frame.width - 90), height: 1000)
			let att = [NSAttributedString.Key.font: UIFont.appFont(size: 14, weight: .regular)]
			let estFram = NSString(string: message).boundingRect(with: cgsiz, options: .usesLineFragmentOrigin, attributes: att, context: nil)
			
			let height: CGFloat = estFram.height + 80
			
			tableCellWidth = estFram.width
			tableCellHeight = height
		}*/
		
		
		message_content_dict = data["message_content"] as? [String: Any]
		
		switch self.message_type {
		case .image:
			if let messageContent:[String: Any] = data["message_content"] as? [String: Any]{
				message_content = MediaModel(data: messageContent)
			}
			break
			
		case .text:
			break
		case .replay:
			if let messageContent:[String: Any] = data["message_content"] as? [String: Any]{
				message_content = ReplayModel(data: messageContent)
			}
			break
		case .document, .video:
			if let messageContent:[String: Any] = data["message_content"] as? [String: Any]{
				let doc: MediaModel = MediaModel(data: messageContent)
				message_content = doc
				if let link: URL = URL(string: doc.file_url) {
					var documentsURL: URL = try! FileManager().url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
					documentsURL.appendPathComponent(link.lastPathComponent)
					self.downloadStatus = FileManager().fileExists(atPath: documentsURL.path) ? .downloaded : .pending
				}
			}
			break
		case .location:
			if let messageContent:[String: Any] = data["message_content"] as? [String: Any]{
				message_content = LocationModel(data: messageContent)
			}
			break
		case .contact:
			if let messageContent:[String: Any] = data["message_content"] as? [String: Any]{
				message_content = MyContact.giveObj(object: messageContent)
			}
			break
//		case .video:
//			if let messageContent:[String: Any] = data["message_content"] as? [String: Any]{
//				let doc: MediaModel = MediaModel(data: messageContent)
//				message_content = doc
//
//				if let link: URL = URL(string: doc.file_url) {
//					var documentsURL: URL = try! FileManager().url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
//					documentsURL.appendPathComponent(link.lastPathComponent)
//					self.downloadStatus = FileManager().fileExists(atPath: documentsURL.path) ? .downloaded : .pending
//				}
//			}
//			break
		
		
		}
		
		
		 
		self.sender_detail = senderDetail
		
		let t = data["time"] as! String

       
        if let date = t.toDate(format: "yyyy-MM-dd'T'HH:mm:ss.SSSX"){
            createdDate = date.noon
            message_on = date.toString(format: "HH:mm:ss")//Utils.dayDifference(from: date)
        }
	}
}




public class MediaMetaModel {
	
	public static let KEY_FILE_TYPE = "file_type"
	public static let KEY_FILE_NAME = "file_name"
	public static let KEY_FILE_SIZE = "file_size"
	public static let KEY_FILE_THUMB = "thumbnail"
	
	var media_type: MediaType = .none
	var thumbnail: URL?
	var file_name: String = ""
	var file_size: Double = 0.0
	
	
	
	required public init() {}
	
	required public init(data: [String: Any] ) {
		media_type = MediaType(rawValue: data[MediaMetaModel.KEY_FILE_TYPE] as? String ?? "") ?? .none
		thumbnail = URL(string: data[MediaMetaModel.KEY_FILE_THUMB] as? String ?? "")
		file_name = data[MediaMetaModel.KEY_FILE_NAME] as? String ?? ""
		file_size = data[MediaMetaModel.KEY_FILE_SIZE] as? Double ?? 0.0
	}
}


public class ReplayModel {
	
	public static let KEY_REPLAY_DOC_ID = "documentId"
	public static let KEY_REPLAY_ORIGIN_TYPE = "originType"
	public static let KEY_REPLAY_ORIGIN_MESSAGE = "originMessage"
 
	
	 
	var documentId: String = ""
	var originType: String = ""
	var originMessage: String = ""
	
	
	
	required public init() {}
	
	required public init(data: [String: Any] ) {
		 
		documentId = data[ReplayModel.KEY_REPLAY_DOC_ID] as? String ?? ""
		originType = data[ReplayModel.KEY_REPLAY_ORIGIN_TYPE] as? String ?? ""
		originMessage = data[ReplayModel.KEY_REPLAY_ORIGIN_MESSAGE] as? String ?? ""
	}
}



public class MediaModel {
	var file_url: String = ""
	var file_meta: MediaMetaModel = MediaMetaModel()
	
	required public init() {}
	
	required public init(data: [String: Any] ) {
		file_url = data["file_url"] as? String ?? ""
		
		if let fileMeta:[String: Any] = data["file_meta"] as? [String: Any]{
			file_meta = MediaMetaModel(data: fileMeta)
		}
	
	}
}

public class LocationModel {
	var address: String = ""
	var latitude: NSString = ""
	var longitude: NSString = ""
	var name: String = ""
	
	required public init() {}
	required public init(data: [String: Any] ) {
		address = data["address"] as? String ?? ""
		latitude = data["latitude"] as? NSString ?? "0.0"
		longitude = data["longitude"] as? NSString ?? "0.0"
		name = data["name"] as? String ?? ""
	}
}


enum MediaType: String {
	case imageJPG = "jpg"
	case imagePNG = "png"
	case audioM4A = "m4a"
	case videoMP4 = "mp4"
	case none = ""
}
