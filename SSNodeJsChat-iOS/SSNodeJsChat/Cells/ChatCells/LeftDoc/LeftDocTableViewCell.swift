//
//  LeftDocTableViewCell.swift
//  Fahemni
//
//  Created by Shubham Sharma on 07/02/19.
//  Copyright © 2019 Shubham Sharma. All rights reserved.
//

import UIKit

class LeftDocTableViewCell: UITableViewCell, DownloadTableCell  {
	@IBOutlet weak var docName: UILabel!
	@IBOutlet weak var docExt: UILabel!
	@IBOutlet weak var time: UILabel!
	@IBOutlet weak var progressLabel: UILabel!
	@IBOutlet weak var loadingImage: LoaderImageView!
	@IBOutlet weak var fileImage: UIImageView!
	override func awakeFromNib() {
		super.awakeFromNib()
		// Initialization code
	}
	
	override func setSelected(_ selected: Bool, animated: Bool) {
		super.setSelected(selected, animated: animated)
		
		// Configure the view for the selected state
	}
	func configData(obj: ChatModel){
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
			loadingImage.image = UIImage(named: "ic_open_file")
			progressLabel.text = FileUtils.convertFileSize(byteSize: message_content.file_meta.file_size)
			break
		}
		
		
		
		time.text = obj.message_on
		if let link:URL = URL(string: message_content.file_url) {
			docName.text = message_content.file_meta.file_name
			docExt.text =  link.pathExtension
			fileImage.image = LeftDocTableViewCell.getFileIcon(fileExtension: link.pathExtension)
		}
		
	}
	
	func download(progress: Double) {
		progressLabel.text = "\(Int(progress * 100))%"
	}
	
	static func getFileIcon(fileExtension: String) -> UIImage? {
		switch fileExtension {
		case "m4a":
			return UIImage(named: "ic_mic")
		default:
			return UIImage(named: "ic_paper_clip")
		}
	}
}
