//
//  Extensions.swift
//
//  Created by Shubham Sharma on 12/11/19.
//  Copyright © 2019 Shubham Sharma. All rights reserved.
//

import Foundation
import UIKit

extension UIColor {
	static let AppCerulean: UIColor = UIColor(named: "Cerulean")!
	static let AppTurquoiseBlue: UIColor = UIColor(named: "TurquoiseBlue")!
	static let AppBrownishGrey: UIColor = UIColor(named: "AppBrownishGrey")!
	static let AppRuby: UIColor = UIColor(named: "AppRuby")!
	static let AppDarkishPink: UIColor = UIColor(named: "DarkishPink")!
	static let AppDustyOrange: UIColor = UIColor(named: "AppDustyOrange")!
	static let AppToastColor: UIColor = UIColor(named: "AppToastColor")!
	static let AppBlack: UIColor = UIColor(named: "AppBlack")!
}
extension NSNotification.Name {
	static let UpdateCommentCount: NSNotification.Name = NSNotification.Name.init("UpdateCommentCount")
	static let PlotIconsNotification: NSNotification.Name = NSNotification.Name.init("PlotIconsNotification")
	static let UpdateUserProfile: NSNotification.Name = NSNotification.Name.init("UpdateUserProfile")
	static let UpdatePost: NSNotification.Name = NSNotification.Name.init("UpdatePost")
}
extension UIFont {
	static func appFont(size: CGFloat, weight: UIFont.Weight = .regular) -> UIFont {
		switch weight {
		case UIFont.Weight.bold:
			return UIFont(name: "Roboto-Bold", size: size) ?? UIFont.systemFont(ofSize: size, weight: weight)
		case UIFont.Weight.light:
			return UIFont(name: "Roboto-Light", size: size) ?? UIFont.systemFont(ofSize: size, weight: weight)
		case UIFont.Weight.regular:
			return UIFont(name: "Roboto", size: size) ?? UIFont.systemFont(ofSize: size, weight: weight)
		default:
			return UIFont(name: "Roboto", size: size) ?? UIFont.systemFont(ofSize: size, weight: weight)
		}
	}
}

