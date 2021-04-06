//
//  ContactViewModel.swift
//  Reward
//
//  Created by Shubham Sharma on 22/11/19.
//  Copyright © 2019 Shubham Sharma. All rights reserved.
//

import Foundation
import UIKit
import Contacts
import Alamofire
import SSViews

enum UserValidationState {
	case valid
	case invalid(String)
}


struct MyContact {
	var firstName = ""
	var middleName = ""
	var lastName = ""
	var mobile = ""
	 
	var is_selected = false
	
	static func giveList(list:[[String:Any]]) -> [MyContact] {
		var objArray = [MyContact]()
		for item in list {
			objArray.append(giveObj(object: item))
		}
		return objArray
	}
	
	static func giveObj(object:[String:Any]) -> MyContact {
		var obj = MyContact()
		obj.firstName = object["first_name"] as? String ?? ""
		obj.middleName = object["middle_name"] as? String ?? ""
		obj.lastName = object["last_name"] as? String ?? ""
		obj.mobile = object["mobile"] as? String ?? ""
		 
		return obj
	}
	
	public func toDictionary() -> [String:Any] {
		return [
			"first_name" : firstName,
			"middle_name" : middleName,
			"last_name" : lastName,
			"mobile" : mobile
		]
		 
		
	}
}
class ContactViewModel {
	
	// Saving the newly created contact
	fileprivate let contactStore = CNContactStore()
	
	fileprivate var allContactList:[MyContact] = []
	var selectedList:[MyContact] = []
	var contactList:[MyContact] = []
 
	
	func requestAccess(completionHandler: @escaping (_ accessGranted: Bool) -> Void) {
		switch CNContactStore.authorizationStatus(for: .contacts) {
		case .authorized:
			completionHandler(true)
			break
		case .denied:
			completionHandler(false)
			break
//			showSettingsAlert(completionHandler)
		case .restricted, .notDetermined:
			contactStore.requestAccess(for: .contacts) { granted, error in
				if granted {
					completionHandler(true)
				} else {
					completionHandler(false)
					
				}
			}
			break
        @unknown default:
            completionHandler(true)
        }
	}
	
	func fetchContact(completion: @escaping (_ isSuccess: Bool) -> Void) {
		
		//		var contacts = [CNContact]()
		var myContacts = [MyContact]()
        var _:[String: [String: Any]] = [:]
		
		let keys = [
			CNContactFormatter.descriptorForRequiredKeys(for: .fullName),
			CNContactEmailAddressesKey as CNKeyDescriptor,
			CNContactPhoneNumbersKey as CNKeyDescriptor,
			CNContactImageDataAvailableKey as CNKeyDescriptor,
			CNContactThumbnailImageDataKey as CNKeyDescriptor]
		let request = CNContactFetchRequest(keysToFetch: keys)
		
		
		do {
			try self.contactStore.enumerateContacts(with: request) {
				(contact, stop) in
				// Array containing all unified contacts from everywhere
				for phoneNumber in contact.phoneNumbers {
					let filteredMobileNo = phoneNumber.value.stringValue
//						.replacingOccurrences(of: "+91", with: "")
//						.replacingOccurrences(of: "-", with: "")
//						.replacingOccurrences(of: "(", with: "")
//						.replacingOccurrences(of: ")", with: "")
//						.replacingOccurrences(of: " ", with: "")
					
//					if (filteredMobileNo.count > 10 && filteredMobileNo.hasPrefix("0") ){
//						filteredMobileNo = String(filteredMobileNo.dropFirst())
//					}
					
					
					//					myContacts.append(MyContact(fullName: contact.givenName + " " + contact.middleName + " " + contact.familyName , mobileNo: phoneNumber.value.stringValue.replacingOccurrences(of: "+91", with: "")))
					myContacts.append(MyContact(firstName: contact.givenName,
												middleName: contact.middleName,
												lastName: contact.familyName,
												mobile: filteredMobileNo
												)
					)
				}
				
			}
			contactList = myContacts
			allContactList = myContacts
			
//			for (index, element) in myContacts.enumerated() {
////				mobileContacts.append(element.mobile: [
////					"first_name": element.firstName,
////										"last_name": element.lastName,
////										"mobile": element.mobile,
////										"is_register": false
////				])
//
//
//				mobileContacts[element.mobile] = [
//					"first_name": element.firstName,
//					"last_name": element.lastName,
//					"mobile": element.mobile,
//					"is_register": false
//				]
//			}
//
//			var contacts:[[String: Any]] = []
//			for (_, element) in mobileContacts {
//				contacts.append(element)
//			}
		 
			completion(true)
			//			syncContact(list: mobileContacts)
			//			contactTableView.reloadData()
		}
		catch {
			completion(false)
//			print("unable to fetch contacts")
		}
	}
}
enum SyncType {
	case isInvited
	case askFriend
	case contactManage
	
}
// MARK: Public Methods
extension ContactViewModel {
//
//	func syncContact(isInvited:SyncType, list:[[String: Any]], completion: @escaping (_ errorString: String?) -> Void) {
//
//		let params:Parameters = ["contact_number": list ]
//
////		print(JsonOperation.toJsonStringFrom(dictionary: params))
//		var url = "sync"// isInvited ? "sync" : "ask-friend"
//		switch isInvited {
//		case .isInvited:
//			  url = "sync"
//			break
//		case .askFriend:
//			 url = "ask-friend"
//			break
//		case .contactManage:
//			url = "hide-sync"
//			break
//		}
//		NetworkManager.syncContact(url: url, parameters: params  ) { (resStatus) in
//
////			self.viewloader?.removeFromSuperview()
//			switch resStatus {
//			case .failed(let errorMessage ):
//				completion(errorMessage)
////				self.showAlertWithMessage(message: errorMessage)
//			case .success(let response ):
//				let isSuccess:Int = response["status_code"] as! Int
//				if(isSuccess == 200){
//					let data = response["data"] as! [[String: Any]]
//					self.allContactList = MyContact.giveList(list: data).filter({ (element) -> Bool in
//						return element.mobile != LoginUserModel.shared.mobile
//					})
//
//					if self.showUnregistered {
//						let registered = self.allContactList.filter({ (element) -> Bool in
//							return element.isRegistered
//						})
//						var notRegistered = self.allContactList.filter({ (element) -> Bool in
//							return !element.isRegistered
//						})
//						 notRegistered.sort { (element, element2) -> Bool in
//							return element.firstName.lowercased() < element2.firstName.lowercased()
//						}
//						self.contactList = registered
//						self.contactList.append(contentsOf: notRegistered)
//
//
//					}else{
//						let registered = self.allContactList.filter({ (element) -> Bool in
//							return element.isRegistered
//						})
//						self.contactList = registered
//					}
//					completion(nil)
////					self.contactTableView.reloadData()
//
//				} else if(isSuccess == 500) {
//					completion(response["message"] as! String)
////					self.showAlertWithMessage(message: response["message"] as! String)
//				}
//
//			}
//
//		}
//	}
 
	 
	func updateSearch(keyWord:String) {

		if keyWord == "" {
			self.allContactList.sort { (element, element2) -> Bool in
				return element.firstName.lowercased() < element2.firstName.lowercased()
			}
			self.contactList = self.allContactList
		} else if keyWord.count > 1 {
			let lowerCaseKeyword = (keyWord).lowercased()
		 
			let tmp: [MyContact] = allContactList.filter { (element) -> Bool in
				if (element.firstName + " " + element.lastName).lowercased().range(of: lowerCaseKeyword) != nil ||
					(element.firstName).range(of: lowerCaseKeyword) != nil ||
					(element.lastName).range(of: lowerCaseKeyword) != nil ||
					(element.firstName).lowercased().range(of: lowerCaseKeyword) != nil ||
					(element.lastName).lowercased().range(of: lowerCaseKeyword) != nil ||
					(element.mobile).lowercased().range(of: lowerCaseKeyword) != nil {
					return true
				}
				return false
			}
			self.contactList = tmp
		}
	}
	
	func select( indexPath: IndexPath) {
		contactList[indexPath.row].is_selected = !contactList[indexPath.row].is_selected

		if (contactList[indexPath.row].is_selected){
			selectedList.append(contactList[indexPath.row])
		} else {
			let filterList = selectedList.filter { (element) -> Bool in
				return element.mobile != contactList[indexPath.row].mobile
			}
			selectedList = filterList
		}
	 
	}
}
