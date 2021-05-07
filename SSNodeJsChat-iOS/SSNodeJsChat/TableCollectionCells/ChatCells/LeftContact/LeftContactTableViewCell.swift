//
//  LeftContactTableViewCell.swift
//  World Album
//
//  Created by Shubham Sharma on 07/04/20.
//  Copyright © 2020 Shubham Sharma. All rights reserved.
//

import UIKit

class LeftContactTableViewCell: UITableViewCell {
	@IBOutlet weak var contactName: UILabel!
	@IBOutlet weak var time: UILabel!
	@IBOutlet weak var number: UILabel!
	override func awakeFromNib() {
		super.awakeFromNib()
		// Initialization code
	}
	
	override func setSelected(_ selected: Bool, animated: Bool) {
		super.setSelected(selected, animated: animated)
		
		// Configure the view for the selected state
	}
	func configData(obj:ChatModel){
		time.text = obj.message_on
		let contact = (obj.message_content as! MyContact)
		contactName.text = "\(contact.firstName) \(contact.middleName) \(contact.lastName)"
		number.text = "\(contact.mobile)"
		//        guard let responseJSON:[String: Any] = SingleChatViewController.convertToDictionary(text: obj.message ) else {
		//            print("invalid json recieved : \( obj.message )")
		//            return
		//        }
		//        locationName.text = responseJSON["name"] as? String
		//        location.text = responseJSON["address"] as? String
	}
	
}
