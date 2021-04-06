import UIKit


struct TmpUsers {
    var email = ""
    var password = ""
    var userId = ""
    var name = ""
}

final class UsernameViewController: UIViewController {
    
    // MARK: - Properties
//    var username = ""
    
    // MARK: - IBOutlets
    @IBOutlet var nextButtonItem: UIBarButtonItem!
     
    @IBOutlet weak var userNameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    
    
    
    fileprivate let tmpUserList: [TmpUsers] = [TmpUsers(email: "anil@yopmail.com", password: "123456", userId: "1", name: "Anil"),
                                TmpUsers(email: "amit@yopmail.com", password: "123456", userId: "2", name: "Amit"),
                                TmpUsers(email: "shubham@yopmail.com", password: "123456", userId: "3", name: "Shubham"),
                                TmpUsers(email: "ali@yopmail.com", password: "123456", userId: "4", name: "Ali"),
                                TmpUsers(email: "samreen@yopmail.com", password: "123456", userId: "5", name: "Samreen")
    ]
    
    public static var tmpUserLogin: TmpUsers!
    
    // MARK: - View Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()

        UsernameViewController.tmpUserLogin = tmpUserList[3]
        
        userNameField.text = UsernameViewController.tmpUserLogin.email
        passwordField.text = UsernameViewController.tmpUserLogin.password
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
    
    @IBAction func didTapNext(_ sender: Any) {
        
        let messageDictionary = [
            "request": "login",
            "userId": UsernameViewController.tmpUserLogin.userId,
            "type": "loginOrCreate",
            "fcm_token": "qasdfghfds",
            "userName": userNameField.text ?? "",
            "password": passwordField.text ?? "",
            ] as [String : Any]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: messageDictionary)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
        if let message:String = jsonString as String?{
            print(message)
            SocketManager.shared.sendMessageToSocket(message: message, observer: self)
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
    
    
}
extension UsernameViewController: SocketObserver {
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


/*
// MARK: - WebSocketDelegate
extension UsernameViewController : WebSocketDelegate {
    func websocketDidConnect(socket: WebSocketClient) {
         print("Web socket connected");
    }
    
    func websocketDidDisconnect(socket: WebSocketClient, error: Error?) {
//        performSegue(withIdentifier: "websocketDisconnected", sender: self)
    }
    
    func websocketDidReceiveMessage(socket: WebSocketClient, text: String) {
        /* Message format:
         * {"type":"message","data":{"time":1472513071731,"text":"😍","author":"iPhone Simulator","color":"orange"}}
         */
        print("Message Receved")
        print(text)
        guard let data = text.data(using: .utf16),
              let jsonData = try? JSONSerialization.jsonObject(with: data),
              let jsonDict = jsonData as? [String: Any],
              let userName = jsonDict["_id"] as? String else { return }
        
        let vc = RoomListViewController()
        vc.userName = userName
        self.navigationController?.pushViewController(vc, animated: true)
        
//        if messageType == "message",
//           let messageData = jsonDict["data"] as? [String: Any],
//           let messageAuthor = messageData["author"] as? String,
//           let messageText = messageData["text"] as? String {
//
//
//        }
    }
    
    func websocketDidReceiveData(socket: WebSocketClient, data: Data) {
        print(data)
    }
}

*/
