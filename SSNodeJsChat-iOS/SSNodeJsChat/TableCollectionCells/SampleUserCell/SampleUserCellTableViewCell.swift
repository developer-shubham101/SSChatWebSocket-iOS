//
//  SampleUserCellTableViewCell.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 25/04/21.
//

import UIKit

class SampleUserCellTableViewCell: UITableViewCell {


    @IBOutlet weak var cellName: UILabel!
    @IBOutlet weak var cellEmail: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func configData(obj: TmpUsers) {
        cellName.text = "\(obj.userId): \(obj.name)"
        cellEmail.text = obj.email
    } 
}
