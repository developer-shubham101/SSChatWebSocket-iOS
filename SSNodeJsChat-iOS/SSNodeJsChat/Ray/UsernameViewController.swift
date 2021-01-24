import UIKit
import Starscream

final class UsernameViewController: UIViewController {
    
    // MARK: - Properties
//    var username = ""
    
    // MARK: - IBOutlets
    @IBOutlet var nextButtonItem: UIBarButtonItem!
     
    @IBOutlet weak var userNameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    
    var socket = WebSocket(url: URL(string: "ws://localhost:1337/login")!, protocols: ["chat"])
    
    
    
    // MARK: - View Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        userNameField.text = "shubham@yopmail.com"
        passwordField.text = "123456"
        
        
//        nextButtonItem.isEnabled = true
        
        
        socket.delegate = self
        socket.connect()
        
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
            "type": "login",
            "userName": userNameField.text ?? "",
            "password": passwordField.text ?? "",
            ] as [String : Any]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: messageDictionary)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
        if let message:String = jsonString as String?{
            print(message)
            socket.write(string: message) //write some Data over the socket!
        }
        
        
    }
    
    @IBAction func didTapRegister(_ sender: Any) {
        
        let messageDictionary = [
            "type": "register",
            "userName": userNameField.text ?? "",
            "password": passwordField.text ?? "",
            ] as [String : Any]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: messageDictionary)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
        if let message:String = jsonString as String?{
            print(message)
            socket.write(string: message) //write some Data over the socket!
        }
        
        
    }
    
    deinit {
        socket.disconnect(forceTimeout: 0)
        socket.delegate = nil
    }
}

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


//// MARK: - IBActions
//extension UsernameViewController {
//
//    @IBAction func usernameDidChange(textField: UITextField) {
//        guard let text = textField.text else {
//            nextButtonItem.isEnabled = false
//            username = ""
//            return
//        }
//
//        nextButtonItem.isEnabled = text.count > 0
//        username = text
//    }
//
//    @IBAction func websocketDisconnectedUnwind(unwindSegue: UIStoryboardSegue) {
//        username = ""
//        nextButtonItem.isEnabled = false
//    }
//}
