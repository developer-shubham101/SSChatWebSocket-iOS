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
            username.text = "\(individualDetail.firstName)\n\(individualDetail.userName)"
            rootVIew.backgroundColor = individualDetail.is_online ? .systemGreen : .systemRed
        }
        
        //2021-04-25T18:05:05.080Z
        print("ChatTableViewCell:: \(obj.last_message_time)")
        
        lastMessage.text = obj.last_message
        
        if let date = obj.last_message_time.toDate(format: "yyyy-MM-dd'T'HH:mm:ss.SSSX") {
            messageTime.text = date.elapsedInterval
        }
//        profilePic.sd_setImage(with: URL(string:obj.receiver_detail.profile_picture), completed: { (image, error, cache, url) in
//
//        })
    }
}
