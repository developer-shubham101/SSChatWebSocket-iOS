//
//  LeftVideoTableViewCell.swift
//  World Album
//
//  Created by Shubham Sharma on 08/04/20.
//  Copyright © 2020 Shubham Sharma. All rights reserved.
//

import UIKit

class LoaderImageView: UIImageView {
	override func awakeFromNib() {
		super.awakeFromNib()
	}
	
	func startRotationAnimation() {
		let rotationAnimation = CABasicAnimation(keyPath: "transform.rotation.z")
		rotationAnimation.toValue = .pi * 2.0 * 2 * 60.0
		rotationAnimation.duration = 200.0
		rotationAnimation.isCumulative = true
		rotationAnimation.repeatCount = Float.infinity
		self.layer.add(rotationAnimation, forKey: "rotationAnimation")
	}
	func stopRotationAnimation() {
		self.layer.removeAnimation(forKey: "rotationAnimation")
	}
}


class LeftVideoTableViewCell: UITableViewCell, DownloadTableCell {
	
	@IBOutlet weak var time: UILabel!
	@IBOutlet weak var progressLabel: UILabel!
	@IBOutlet weak var chatImage: UIImageView!
	@IBOutlet weak var loadingImage: LoaderImageView!
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
