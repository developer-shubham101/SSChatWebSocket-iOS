//
//  AppDelegate.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 24/12/20.
//

import UIKit
import Firebase
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        
        self.window = UIWindow(frame: UIScreen.main.bounds)
        //            let storyboard = UIStoryboard(name: "Main", bundle: nil)
        
        let nav1 = UINavigationController()
        //            nav1.isNavigationBarHidden = true
        
        
        let mainView = UsersListForLoginViewController()
        nav1.viewControllers = [mainView]
        self.window!.rootViewController = nav1
        self.window?.makeKeyAndVisible()
        
        
        
        
        
         
        // Use Firebase library to configure APIs
        FirebaseApp.configure()
         
            // For iOS 10 display notification (sent via APNS)
        UNUserNotificationCenter.current().delegate = self
        
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: {_, _ in })
        
        
        application.registerForRemoteNotifications()
        
        InstanceID.instanceID().instanceID { (result, error) in
            if let error = error {
                print("Error fetching remote instance ID: \(error)")
            } else if let result = result {
//                FCMToken.saveFCMToken(token: result.token)
                LoginUserModel.shared.fCMToken = result.token
                print("Remote instance ID token: \(result.token)")
            }
        }
        Messaging.messaging().delegate = self
        
        if let pushnotificationInfo = launchOptions?[UIApplication.LaunchOptionsKey.remoteNotification] {
            if let pushnotificationInfoDictionary = pushnotificationInfo as? Dictionary<AnyHashable, Any>{
                NSLog("App has been launch through push notifications. \(pushnotificationInfoDictionary)")
                UserDefaults.standard.set(pushnotificationInfoDictionary, forKey: "NotificationData")
                
                //self.handleRemoteNotification(notificationInfo: pushnotificationInfoDictionary)
            }
        }
        
         
        clearNotifications()
        
        
        return true
    }
    
    // MARK: UISceneSession Lifecycle
    
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
    
    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }
    
    
}

extension AppDelegate: MessagingDelegate {
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("DEVICE TOKEN = \(deviceToken)")
        Messaging.messaging().apnsToken = deviceToken
    }
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print(error)
    }
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        
        // Print full message.
        print("didReceiveRemoteNotification : \(userInfo)")
        
        completionHandler(UIBackgroundFetchResult.newData)
    }
    
    // The callback to handle data message received via FCM for devices running iOS 10 or above.
//    func applicationReceivedRemoteMessage(_ remoteMessage: MessagingRemoteMessage) {
//        print(remoteMessage.appData)
//    }
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("Firebase registration token: \(fcmToken)")
//        FCMToken.saveFCMToken(token: fcmToken)
        LoginUserModel.shared.fCMToken = fcmToken ?? ""
        //        let dataDict:[String: String] = ["token": fcmToken]
        //        NotificationCenter.default.post(name: Notification.Name("FCMToken"), object: nil, userInfo: dataDict)
        // TODO: If necessary send token to application server.
        // Note: This callback is fired at each app startup and whenever a new token is generated.
    }
    //    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
    //        print(userInfo)
    //    }
    
}

extension AppDelegate {
    func handleRemoteNotification(notificationInfo:Dictionary<AnyHashable, Any>, shouldInvokeUI:Bool = false) {
        
//        let receivedNotificationCategory = AppDelegate.categoryForRemoteNotification(notificationInfo: notificationInfo)
//
//        switch receivedNotificationCategory {
//        case .Chat:
//            self.handleLiveChatNotification(notificationInfo: notificationInfo, shouldInvokeUI: shouldInvokeUI)
//            break
//
//        case .Others:
//            break
//        }
        
    }
    
    
    func handleLiveChatNotification(notificationInfo:Dictionary<AnyHashable, Any>, shouldInvokeUI:Bool = false){
        //        self.playLiveChatNotificationSound()
        
        
        if let rowData = notificationInfo["raw_content"] as? [String:Any]{
            print(rowData)
            
        } else if let rowData = notificationInfo["raw_content"] as?  String {
            if let responseJSON:[String: Any] = JsonOperation.toDictionaryFrom(string: rowData) {
                let data = [
                    "room_number" : responseJSON["room_number"] as! String,
                    "username" : responseJSON["username"] as! String,
                    "profile_image" : responseJSON["profile_image"] as! String
                ]
                
                if shouldInvokeUI == true{
                    NotificationCenter.default.post(name: NSNotification.Name("ChatEventReceived"), object: nil, userInfo: data)
                }
            }
        }
        
        
    }
//    static func categoryForRemoteNotification(notificationInfo:Dictionary<AnyHashable, Any>) -> RemoteNotificationcategory{
//        var notificationCategory:RemoteNotificationcategory = .Others
//
//        var categoryType:String? = nil
//
//        if let apsData = notificationInfo["aps"] as? Dictionary<String, Any>, let notificationType = apsData[notificationTypeKey] as? String{
//            categoryType = notificationType
//        } else if let notificationType = notificationInfo[notificationTypeKey] as? String{
//            categoryType = notificationType
//        }
//
//        if let finalCategory = categoryType{
//
//            switch finalCategory {
//
//            case chatNotificationTypeKey:
//                notificationCategory = .Chat
//                break
//            default:
//                notificationCategory = .Others
//                break
//            }
//        }
//
//        return notificationCategory
//    }
    func clearNotifications(){
        UIApplication.shared.applicationIconBadgeNumber = 0
//        UIApplication.shared.cancelAllLocalNotifications()
    }
}
 
