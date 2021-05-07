//
//  RightVideoTableViewCell.swift
//  World Album
//
//  Created by Shubham Sharma on 08/04/20.
//  Copyright © 2020 Shubham Sharma. All rights reserved.
//

import UIKit

class RightVideoTableViewCell: UITableViewCell, DownloadTableCell {
	
	@IBOutlet weak var time: UILabel!
	@IBOutlet weak var progressLabel: UILabel!
	@IBOutlet weak var loadingImage: LoaderImageView!
	@IBOutlet weak var chatImage: UIImageView!
	
	override func awakeFromNib() {
		super.awakeFromNib()
		// Initialization code
	}
	
	override func setSelected(_ selected: Bool, animated: Bool) {
		super.setSelected(selected, animated: animated)
		
		// Configure the view for the selected state
	}
	func configData(obj: ChatModel) {
		let message_content = (obj.message_content as! MediaModel)
		
		switch obj.downloadStatus {
		case .pending:
			loadingImage.stopRotationAnimation()
			loadingImage.image = UIImage(named: "ic_download")//UIImage.gifImageWithName("loading")
			progressLabel.text = FileUtils.convertFileSize(byteSize: message_content.file_meta.file_size)
			break
		case .downloading:
			loadingImage.startRotationAnimation()
			loadingImage.image = UIImage(named: "ic_load")
			break
		case .downloaded:
			loadingImage.stopRotationAnimation()
			loadingImage.image = UIImage(named: "ic_play")
			progressLabel.text = FileUtils.convertFileSize(byteSize: message_content.file_meta.file_size)
			break
		}
		
		
		
		time.text = obj.message_on
		chatImage.clipsToBounds = true
		chatImage.sd_setImage(with: message_content.file_meta.thumbnail, completed: { (image, error, cache, url) in
			
		})
	} 
	func download(progress: Double) {
		progressLabel.text = "\(Int(progress * 100))%"
	}
}
