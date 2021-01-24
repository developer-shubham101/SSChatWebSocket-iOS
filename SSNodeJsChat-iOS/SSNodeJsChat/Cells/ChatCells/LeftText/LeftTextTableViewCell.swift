//
//  LeftTextTableViewCell.swift
//  Fahemni
//
//  Created by Shubham Sharma on 04/02/19.
//  Copyright © 2019 Shubham Sharma. All rights reserved.
//

import UIKit
import SSViews

class LeftTextTableViewCell: UITableViewCell {

    @IBOutlet weak var chatMessage: UILabel!
	@IBOutlet weak var quotMessage: UILabel!
	@IBOutlet weak var time: UILabel!
	@IBOutlet weak var quotWrapperView: UIView!
	@IBOutlet weak var wrapperView: UIView!
	
	
	override func awakeFromNib() {
		super.awakeFromNib()
		// Initialization code
	}
	
	override func setSelected(_ selected: Bool, animated: Bool) {
		super.setSelected(selected, animated: animated)
		
		// Configure the view for the selected state
	}
	func configData(obj: ChatModel){
		time.text = obj.message_on
		chatMessage.text = obj.message
		if obj.message_type == .replay {
			if let locationModel: ReplayModel = obj.message_content as? ReplayModel {
				quotMessage.text = locationModel.originMessage
			}
		}
		
		
	}
}
