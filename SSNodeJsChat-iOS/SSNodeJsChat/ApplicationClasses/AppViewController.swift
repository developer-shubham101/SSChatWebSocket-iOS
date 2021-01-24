//
//  AppViewController.swift
//  Reward
//
//  Created by Shubham Sharma on 24/01/20.
//  Copyright © 2020 Shubham Sharma. All rights reserved.
//

import UIKit
//import SSViews
import Toast_Swift
//import Firebase
import UserNotifications

class AppViewController: UIViewController {
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        if #available(iOS 13.0, *) {
            return .darkContent
        } else {
            return .default
        }
    }
    
    // create a new style
    var style: ToastStyle {
        get {
            var tmp = ToastStyle()
            tmp.messageColor = .white
            tmp.backgroundColor = UIColor.AppToastColor

            return tmp
        }
    }


    func showToast(message: String) {
        self.view.makeToast(message, duration: 2.0, position: .bottom, style: style)
    }
    
    func goBack() {
        navigationController?.popViewController(animated: true)
    }
    
    
    public func shareCode(code: String) {
        let items = ["Here is code \(code)"]
        let ac = UIActivityViewController(activityItems: items, applicationActivities: nil)
        self.present(ac, animated: true)
    }
    func openMap(lat: String, long: String, title:String) {
        //                if #available(iOS 10.0, *) {
        //                    UIApplication.shared.open(URL(string:"https://www.google.com/maps/@\(lat),\(long),6z")!, options: [:], completionHandler: nil)
        //                } else {
        //                    UIApplication.shared.openURL(URL(string:"https://www.google.com/maps/@\(lat),\(long),6z")!)
        //                }
        if (UIApplication.shared.canOpenURL(URL(string:"comgooglemaps://")!)) {
            let directionsURL = "comgooglemaps://?q=\(title.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)&center=\(lat),\(long)"
            guard let url = URL(string: directionsURL) else {
                return
            }
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url , options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.openURL(url)
            }
        } else {
            let directionsURL = "http://maps.apple.com/?q=\(lat),\(long)"
            guard let url = URL(string: directionsURL) else {
                return
            }
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }
}
extension UIViewController {
    func shakeView(uiView: UIView) {
        
        let midX = uiView.center.x
        let midY = uiView.center.y
        
        let animation = CABasicAnimation(keyPath: "position")
        animation.duration = 0.08
        animation.repeatCount = 2
        animation.autoreverses = true
        animation.fromValue = CGPoint(x: midX - 10, y: midY)
        animation.toValue = CGPoint(x: midX + 10, y: midY)
        uiView.layer.add(animation, forKey: "position")
    }
    
    func bouncView(uiView: UIView, bounceDown: Bool = false, repeatCount: Float = 2, duration: CFTimeInterval = 0.08) {
        
        let animation = CABasicAnimation(keyPath: "transform")
        animation.duration = duration
        animation.repeatCount = repeatCount
        animation.autoreverses = true
        //		animation.isRemovedOnCompletion = true;
        //		animation.fromValue = CGPoint(x: midX - 10, y: midY)
        animation.toValue = bounceDown ? NSValue(caTransform3D: CATransform3DMakeScale(0.8, 0.8, 1.0)) : NSValue(caTransform3D: CATransform3DMakeScale(1.2, 1.2, 1.0))
        
        uiView.layer.add(animation, forKey: "transform")
        
        
        
        //	 UIView.animate(withDuration: 0.3 / 1.5, animations: {
        //		let x = CGAffineTransform.identity.scaledBy(x: 1.1, y: 1.1)
        //		self.followBtn.transform = x
        //	 }) { finished in
        //		 UIView.animate(withDuration: 0.3 / 2, animations: {
        //			let x = CGAffineTransform.identity.scaledBy(x: 0.9, y: 0.9)
        //			 self.followBtn.transform = x
        //		 }) { finished in
        //			 UIView.animate(withDuration: 0.3 / 2, animations: {
        //				 self.followBtn.transform = CGAffineTransform.identity
        //			 })
        //		 }
        //	 }
        
    }
    
    private func rotateView(targetView: UIView, duration: Double = 1.0) {
        //        UIView.animate(withDuration: duration, delay: 0.0, options: .curveLinear, animations: {
        ////            targetView.transform = targetView.transform.rotated(by: CGFloat(CGFloat.pi))
        //
        //            if #available(iOS 12.0, *) {
        //
        //                let rotation = CATransform3DMakeRotation(CGFloat.pi * 90.0 / 180.0, 0, 0, 0)
        //                targetView.transform3D = CATransform3DTranslate(rotation, 20, 30, 0)
        //            } else {
        //                // Fallback on earlier versions
        //            }
        //        }) { finished in
        //            self.rotateView(targetView: targetView, duration: duration)
        //        }
        
        
        let anim = CABasicAnimation(keyPath: "transform")
        anim.fromValue = CATransform3DMakeRotation(CGFloat.pi * 90.0, 0, 1, 0)
        anim.toValue = CATransform3DMakeRotation(CGFloat.pi , 0, 1, 0)
        anim.fromValue = CATransform3DMakeRotation(CGFloat.pi * 360.0, 0, 0, 1)
        
        
        anim.duration = 1
        anim.autoreverses = true
        anim.repeatCount = .infinity
        targetView.layer.add(anim, forKey: "transform")
    }
    private func rotateViewZ(targetView: UIView, duration: Double = 1.0) {
        //        UIView.animate(withDuration: duration, delay: 0.0, options: .curveLinear, animations: {
        ////            targetView.transform = targetView.transform.rotated(by: CGFloat(CGFloat.pi))
        //
        //            if #available(iOS 12.0, *) {
        //
        //                let rotation = CATransform3DMakeRotation(CGFloat.pi * 90.0 / 180.0, 0, 0, 0)
        //                targetView.transform3D = CATransform3DTranslate(rotation, 20, 30, 0)
        //            } else {
        //                // Fallback on earlier versions
        //            }
        //        }) { finished in
        //            self.rotateView(targetView: targetView, duration: duration)
        //        }
        
        
        //            let anim = CABasicAnimation(keyPath: "transform")
        //            anim.fromValue = CATransimageform3DMakeRotation(CGFloat.pi * 90.0, 0, 1, 0)
        //                    anim.toValue = CATransform3DMakeRotation(CGFloat.pi , 0, 1, 0)
        //            anim.fromValue = CATransform3DMakeRotation(CGFloat.pi * 360.0, 0, 0, 1)
        
        let anim = CABasicAnimation(keyPath: "transform.rotation.z")
        anim.toValue = NSNumber(value: Double.pi * -2)
        anim.isCumulative = true
        anim.duration = 2
        //            anim.autoreverses = true
        anim.repeatCount = .infinity
        targetView.layer.add(anim, forKey: "transform")
    }
    
    func showAlertWithMessage(message:String) {
        let alert  = UIAlertController.init(title: Bundle.main.infoDictionary!["CFBundleDisplayName"] as? String, message:message , preferredStyle:.alert)
        let action = UIAlertAction.init(title: "OK", style: .cancel, handler: nil)
        alert.addAction(action)
        self.present(alert, animated:true, completion: nil)
    }
    func showAlertWithMessage(message:String, completion: @escaping () -> Void) {
        let alert  = UIAlertController.init(title: Bundle.main.infoDictionary!["CFBundleDisplayName"] as? String, message:message , preferredStyle:.alert)
        let action = UIAlertAction.init(title: "OK", style: .cancel) { (action) in
            completion()
        }
        alert.addAction(action)
        self.present(alert, animated:true, completion: nil)
    }
    
    func confirmDialog(title: String, message:String, confirmBtnTxt:String, cancelBtnTxt:String, completion: @escaping (ConfirmResponse) -> Void){
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .actionSheet)
        
        let btn1 = UIAlertAction(title: confirmBtnTxt, style: .default, handler: { (action) -> Void in
            completion(.confirm)
        })
        let btn2 = UIAlertAction(title: cancelBtnTxt, style: .cancel, handler: { (action) -> Void in
            completion(.cancel)
        })
        
        alertController.addAction(btn1)
        alertController.addAction(btn2)
        
        self.present(alertController, animated: true, completion: nil)
    }
    
    
    func openWebsite( url: String, errorMessage: String = "Error open url" ) {
        //        var webSiteUrl = ""
        
        //        if (!(NetworkManager.PRIVACY_POLICY_URL.contains("http://") || NetworkManager.PRIVACY_POLICY_URL.contains("https://"))){
        //            webSiteUrl = "http://\(NetworkManager.PRIVACY_POLICY_URL)"
        //        }
        
        if let url = URL(string: url ) {
            UIApplication.shared.open(url)
        }else{
            showAlertWithMessage (message: errorMessage)
        }
        
    }
}
//
//extension UIViewController {
//	func getActivityIndicator(_ title: String = "Loading...") -> UIView {
//		var activityIndicator = UIActivityIndicatorView()
//		var strLabel = UILabel()
//		let effectView = UIVisualEffectView(effect: UIBlurEffect(style: .dark))
//		let mainUi = UIView()
//		
//		
//		strLabel.removeFromSuperview()
//		activityIndicator.removeFromSuperview()
//		effectView.removeFromSuperview()
//		
//		strLabel = UILabel(frame: CGRect(x: 50, y: 0, width: 114, height: 46))
//		strLabel.text = title
//		
//		strLabel.font = .systemFont(ofSize: 14, weight: .medium)
//		strLabel.textColor = UIColor(white: 0.9, alpha: 0.7)
//		
//		effectView.frame = CGRect(x: view.frame.midX - 80, y: view.frame.midY - strLabel.frame.height/2 , width: 160, height: 46)
//		effectView.layer.cornerRadius = 3
//		effectView.layer.masksToBounds = true
//		
//		activityIndicator = UIActivityIndicatorView(style: .white)
//		activityIndicator.frame = CGRect(x: 0, y: 0, width: 46, height: 46)
//		activityIndicator.startAnimating()
//		
//		effectView.contentView.addSubview(activityIndicator)
//		effectView.contentView.addSubview(strLabel)
//		
//		mainUi.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height)
//		mainUi.backgroundColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.4)
//		
//		
//		mainUi.addSubview(effectView)
//		return mainUi
//	}
//	
//	func getActivityIndicator2(_ title: String = "Loading...") -> UIView {
//		let activityIndicator = UIImageView(image: UIImage(named: "img_download.png"))
//		var strLabel = UILabel()
//		
//		let mainUi = UIView()
//		
//		
//		strLabel.removeFromSuperview()
//		activityIndicator.removeFromSuperview()
//		
//		
//		strLabel = UILabel(frame: CGRect(x: 0, y: view.frame.midY + 50 , width: view.bounds.width, height: 46))
//		strLabel.text = title
//		//		strLabel.layer.backgroundColor = UIColor.red.cgColor
//		strLabel.textAlignment = .center
//		
//		strLabel.font = .systemFont(ofSize: 22, weight: .light)
//		strLabel.textColor = UIColor.AppBrownGray//UIColor(white: 0.9, alpha: 0.7)
//		
//		
//		
//		activityIndicator.frame = CGRect(x: view.frame.midX - 100, y: view.frame.midY - 150 , width: 200, height: 200)
//		activityIndicator.startAnimating()
//		
//		
//		
//		mainUi.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height)
//		mainUi.backgroundColor = UIColor.white
//		
//		mainUi.addSubview(strLabel)
//		//		mainUi.addSubview(effectView)
//		mainUi.addSubview(activityIndicator)
//		
//		
//		return mainUi
//	}
//}
enum ConfirmResponse {
    case confirm
    case cancel
}

extension UIViewController {
    func getActivityIndicator(_ title: String = "Loading...") -> UIView {
        let screenSize: CGRect = UIScreen.main.bounds
        
        
        var activityIndicator = UIActivityIndicatorView()
        var strLabel = UILabel()
        let effectView = UIVisualEffectView(effect: UIBlurEffect(style: .dark))
        let mainUi = UIView()
        
        
        strLabel.removeFromSuperview()
        activityIndicator.removeFromSuperview()
        effectView.removeFromSuperview()
        
        strLabel = UILabel(frame: CGRect(x: 50, y: 0, width: 114, height: 46))
        strLabel.text = title
        
        strLabel.font = .systemFont(ofSize: 14, weight: .medium)
        strLabel.textColor = UIColor(white: 0.9, alpha: 0.7)
        
        effectView.frame = CGRect(x: screenSize.midX - 80, y: screenSize.midY - strLabel.frame.height/2 , width: 160, height: 46)
        effectView.layer.cornerRadius = 3
        effectView.layer.masksToBounds = true
        
        activityIndicator = UIActivityIndicatorView(style: .white)
        activityIndicator.frame = CGRect(x: 0, y: 0, width: 46, height: 46)
        activityIndicator.startAnimating()
        
        effectView.contentView.addSubview(activityIndicator)
        effectView.contentView.addSubview(strLabel)
        
        mainUi.frame = CGRect(x: 0, y: 0, width: screenSize.width, height: screenSize.height)
        mainUi.backgroundColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.4)
        
        
        mainUi.addSubview(effectView)
        return mainUi
    }
    
    func getActivityIndicatorX(_ title: String = "Loading...") -> UIView {
        let activityIndicator = UIImageView(image: UIImage(named: "ic_loading.png"))
        let activityIndicatorWrapper = UIView()
//        activityIndicatorWrapper.radius = true
//        activityIndicatorWrapper.radiusAmt = 100
        activityIndicatorWrapper.backgroundColor = .white
        //        var strLabel = UILabel()
        
        let mainUi = UIView()
        
        
        //        strLabel.removeFromSuperview()
        //        activityIndicator.removeFromSuperview()
        //
        //
        //        strLabel = UILabel(frame: CGRect(x: 0, y: view.frame.midY + 50 , width: view.bounds.width, height: 46))
        //        strLabel.text = title
        //        //        strLabel.layer.backgroundColor = UIColor.red.cgColor
        //        strLabel.textAlignment = .center
        //
        //        strLabel.font = .systemFont(ofSize: 22, weight: .light)
        //        strLabel.textColor = UIColor.red//UIColor(white: 0.9, alpha: 0.7)
        
        
        
        activityIndicatorWrapper.frame = CGRect(x: view.frame.midX - 100, y: view.frame.midY - 150 , width: 200, height: 200)
        
        activityIndicator.frame = CGRect(x: 0, y: 0, width: 200, height: 200)
        
        
        //        activityIndicator.startAnimating()
        
        
        
        mainUi.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height)
        //        mainUi.backgroundColor = UIColor.white
        mainUi.backgroundColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.4)
        
        
        activityIndicatorWrapper.addSubview(activityIndicator)
        //        mainUi.addSubview(strLabel)
        //        mainUi.addSubview(effectView)
        mainUi.addSubview(activityIndicatorWrapper)
        
        rotateView(targetView: activityIndicator)
        //        bouncView(uiView: activityIndicator, bounceDown: false, repeatCount: Float.infinity, duration: 0.3)
        return mainUi
    }
    
    
    func getActivityIndicatorddd(_ title: String = "Loading...") -> UIView {
        let activityIndicator = UIImageView(image: UIImage(named: "ic_loading.png"))
        let mainUi = UIView()
        activityIndicator.frame = CGRect(x: view.frame.midX - 100, y: view.frame.midY - 100 , width: 200, height: 200)
        
        
        //        activityIndicator.startAnimating()
        
        
        
        mainUi.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height)
        //        mainUi.backgroundColor = UIColor.white
        mainUi.backgroundColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.4)
        
        mainUi.addSubview(activityIndicator)
        
        rotateView(targetView: activityIndicator)
        //        bouncView(uiView: activityIndicator, bounceDown: false, repeatCount: Float.infinity, duration: 0.3)
        return mainUi
    }
}

extension UIColor{
    func HexToColor(hexString: String, alpha:CGFloat? = 1.0) -> UIColor {
        // Convert hex string to an integer
        let hexint = Int(self.intFromHexString(hexStr: hexString))
        let red = CGFloat((hexint & 0xff0000) >> 16) / 255.0
        let green = CGFloat((hexint & 0xff00) >> 8) / 255.0
        let blue = CGFloat((hexint & 0xff) >> 0) / 255.0
        let alpha = alpha!
        // Create color object, specifying alpha as well
        let color = UIColor(red: red, green: green, blue: blue, alpha: alpha)
        return color
    }
    
    
    
    
    func intFromHexString(hexStr: String) -> UInt32 {
        var hexInt: UInt32 = 0
        // Create scanner
        let scanner: Scanner = Scanner(string: hexStr)
        // Tell scanner to skip the # character
        scanner.charactersToBeSkipped = NSCharacterSet(charactersIn: "#") as CharacterSet
        // Scan hex value
        scanner.scanHexInt32(&hexInt)
        return hexInt
    }
    
    
    
}
enum AppStoryboard : String {
    case Main = "Main"
    case PlaceDetails = "PlaceDetails"
    case TravelerTab = "TravelerTab"
    case TravelerDetails = "TravelerDetails"
    case Chat = "Chat"
    
    var instance : UIStoryboard {
        return UIStoryboard(name: self.rawValue, bundle: Bundle.main)
    }
    
    func viewController<T: UIViewController>(viewControllerClass: T.Type) -> T {
        let storyBoardId = (viewControllerClass as UIViewController.Type).storyboardID
        return instance.instantiateViewController(withIdentifier: storyBoardId) as! T
    }
    
}
extension UIViewController {
    class var storyboardID : String {
        return "\(self)"
    }
}
