//
//  RightImageTableViewCell.swift
//  Fahemni
//
//  Created by Shubham Sharma on 04/02/19.
//  Copyright © 2019 Shubham Sharma. All rights reserved.
//

import UIKit

class RightImageTableViewCell: UITableViewCell {

    @IBOutlet weak var time: UILabel!
    @IBOutlet weak var chatImage: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func configData(obj:ChatModel) {
        time.text = obj.message_on
        chatImage.clipsToBounds = true
		chatImage.sd_setImage(with: URL(string: (obj.message_content as! MediaModel).file_url), completed: { (image, error, cache, url) in
            
        })
    }
}
