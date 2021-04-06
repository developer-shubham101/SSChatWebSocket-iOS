//
//  ChatTableViewCell.swift
//  Fahemni
//
//  Created by Rahul on 27/12/18.
//  Copyright © 2018 arka. All rights reserved.
//

import UIKit
import SDWebImage

class ChatTableViewCell: UITableViewCell {
    
    @IBOutlet weak var profilePic: UIImageView!
    @IBOutlet weak var username: UILabel!
    @IBOutlet weak var lastMessage: UILabel!
    @IBOutlet weak var messageTime: UILabel!
    @IBOutlet weak var rootVIew: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func configData(obj: ChatRoomModel) {
        if let individualDetail: UserDetailsModel = RoomListViewController.userDetailsList[obj.individualUserId] {
            username.text = "\(individualDetail.firstName) \(individualDetail.userName)"
            rootVIew.backgroundColor = individualDetail.is_online ? .systemGreen : .systemRed
        }
        
        lastMessage.text = obj.last_message
        messageTime.text = obj.last_message_time
//
//        profilePic.sd_setImage(with: URL(string:obj.receiver_detail.profile_picture), completed: { (image, error, cache, url) in
//
//        })
    }
}
