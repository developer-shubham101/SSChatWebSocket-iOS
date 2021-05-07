//
//  LoginViewController.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 25/04/21.
//

import UIKit


struct TmpUsers {
    var email = ""
    var password = ""
    var userId = ""
    var name = ""
}
class LoginViewController: UIViewController {
    
    // MARK: - Properties
    
    // MARK: - IBOutlets
    @IBOutlet weak var userNameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    
    
    
  
    
//    public static var tmpUserLogin: TmpUsers!
    
    // MARK: - View Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        SocketManager.shared.connectSocket(notify: true)
        SocketManager.shared.registerToScoket(observer: self )
        
//        nextButtonItem.isEnabled = true
        
        
        
        
        navigationItem.hidesBackButton = true
    }
    
    
//    // MARK: - Navigation
//    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
//        guard segue.identifier == "usernameSelected",
//              let viewController = segue.destination as? ViewController else {
//            return
//        }
//
//        viewController.username = username
//    }
    
    @IBAction func didTapLogin(_ sender: Any) {
        
        let messageDictionary = [
            "request": "login",
            "userId": LoginUserModel.shared.userId,
            "type": "loginOrCreate",
            "fcm_token": "qasdfghfds",
            "userName": userNameField.text ?? "",
            "password": passwordField.text ?? "",
            ] as [String : Any]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: messageDictionary)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
        if let message:String = jsonString as String?{
            SocketManager.shared.sendMessageToSocket(message: message)
//            socket.write(string: message) //write some Data over the socket!
        }
        
        
    }
    
    @IBAction func didTapRegister(_ sender: Any) {
        
//        let messageDictionary = [
//            "type": "register",
//            "userId":"12",
//            "fcm_token":"qasdfghfds",
//            "userName": userNameField.text ?? "",
//            "password": passwordField.text ?? "",
//            ] as [String : Any]
//
//        let jsonData = try! JSONSerialization.data(withJSONObject: messageDictionary)
//        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
//        if let message:String = jsonString as String?{
//            print(message)
//           // socket.write(string: message) //write some Data over the socket!
//        }
        
        
    }
    
    @IBAction func didTapOpenList(_ sender: Any) {
    }
    
}
extension LoginViewController: SocketObserver {
    func registerFor() -> [ResponseType] {
        return [.loginOrCreate]
    }
    
    func brodcastSocketMessage(to observerWithIdentifire: ResponseType, statusCode: Int, data: [String : Any], message: String) {
        print("observer ",{observerWithIdentifire})
        guard let data = data["data"] as? [String : Any] else {
            return
        }
        
        LoginUserModel.shared.login(userData: data)
        print("LoginViewController:: \(LoginUserModel.shared.userId)")
        let vc = RoomListViewController()
     
        self.navigationController?.show(vc, sender: nil)
    }
    
    
    func socketConnection(status: SocketConectionStatus) {
        print(status)
    }
}
