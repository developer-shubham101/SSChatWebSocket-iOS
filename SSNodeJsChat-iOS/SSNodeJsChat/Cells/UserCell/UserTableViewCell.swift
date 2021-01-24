//
//  UserTableViewCell.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 24/01/21.
//

import UIKit

class UserTableViewCell: UITableViewCell {

    @IBOutlet weak var profilePic: UIImageView!
    @IBOutlet weak var username: UILabel!
    @IBOutlet weak var otherData: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func configData(obj: UserModel) {
        username.text = obj.userName
//        otherData.text = obj.last_message
        
//        profilePic.sd_setImage(with: URL(string:obj.receiver_detail.profile_picture), completed: { (image, error, cache, url) in
//            
//        })
    }
    
}
