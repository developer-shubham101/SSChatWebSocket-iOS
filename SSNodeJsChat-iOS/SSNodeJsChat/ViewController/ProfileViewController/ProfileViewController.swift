//
//  ProfileViewController.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 17/04/21.
//

import UIKit

class ProfileViewController: UIViewController {

    
    @IBOutlet weak var emailField: UITextField!
    @IBOutlet weak var nameField: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        emailField.text = LoginUserModel.shared.email
        nameField.text = LoginUserModel.shared.name
        // Do any additional setup after loading the view.
    }


    @IBAction func didTapRegister(_ sender: Any) {
        let messageDictionary = [
            "request": "login",
            "userId": LoginUserModel.shared.userId,
            "type": "updateProfile",
//            "fcm_token": "qasdfghfds",
            "firstName": nameField.text ?? "",
//            "password": passwordField.text ?? "",
            ] as [String : Any]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: messageDictionary)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
        if let message:String = jsonString as String?{
            
            SocketManager.shared.sendMessageToSocket(message: message)
//            socket.write(string: message) //write some Data over the socket!
        }
        
    }
    
}
extension ProfileViewController: SocketObserver {
    func registerFor() -> [ResponseType] {
        return [.loginOrCreate]
    }
    
    func brodcastSocketMessage(to observerWithIdentifire: ResponseType, statusCode: Int, data: [String : Any], message: String) {
        print("observer ",{observerWithIdentifire})
        guard let dat = data["data"] as? [String : Any] else {
            return
        }
        let vc = RoomListViewController()
     
        self.navigationController?.show(vc, sender: nil)
    }
    
    
    func socketConnection(status: SocketConectionStatus) {
        print(status)
    }
     
    
}
