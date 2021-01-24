//
//  RoomListViewController.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 24/01/21.
//

import UIKit
import Starscream

class RoomListViewController: UIViewController {
    @IBOutlet weak var tableView: UITableView!
    
    fileprivate var tableItems:[ChatRoomModel] = []
//    fileprivate var viewloader:UIView?
    
    
    fileprivate var socket = WebSocket(url: URL(string: "ws://localhost:1337/room")!, protocols: ["chat"])
    
    var userName: String = ""
    
    static var userDetailsList: [UserDetailsModel] = []
    
    override func viewWillAppear(_ animated: Bool) {
//        self.navigationController?.isNavigationBarHidden = true
//        self.navigationController?.interactivePopGestureRecognizer?.isEnabled = true
//        self.navigationController?.interactivePopGestureRecognizer?.delegate = self
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initCollection()
        
//        viewloader = getActivityIndicator("Loading...")
//        view.addSubview(viewloader!)
        
        
        socket.delegate = self
        socket.connect()
        
    }
    fileprivate func initCollection() {
        tableView.delegate = self
        tableView.dataSource = self
    }
    fileprivate func getChatRoomList() {
//        NetworkManager.getChatRoomList() { (success, res) in
//            self.viewloader?.removeFromSuperview()
//            if let response:[String:Any] = res as? [String:Any]{
//                let isSuccess:Int = response["code"] as! Int
//                if(isSuccess == 200){
//                    let data = response["data"] as! [[String:Any]]
//                    self.tableItems = ChatRomModel.giveList(list: data )
//                    self.tableView.reloadData()
//
//                } else if(isSuccess == 500){
//                    self.showAlertWithMessage(message: response["message"] as! String)
//                }
//            }else if let response: String  = res as? String {
//                self.showAlertWithMessage(message: response)
//            }
//        }
    }
    @IBAction func didTapAddUser(_ sender: Any) {
        let vc = AllUserListViewController()
        vc.userName = userName
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    deinit {
        socket.disconnect(forceTimeout: 0)
        socket.delegate = nil
    }

}

extension RoomListViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableItems.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 83
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let identifier = "ChatTableViewCell"
        var cell: ChatTableViewCell! = tableView.dequeueReusableCell(withIdentifier: identifier) as? ChatTableViewCell
        if cell == nil {
            tableView.register(UINib(nibName: "ChatTableViewCell", bundle: nil), forCellReuseIdentifier: identifier)
            cell = tableView.dequeueReusableCell(withIdentifier: identifier) as? ChatTableViewCell
        }
        cell.configData(obj: tableItems[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
//        let vc:SingleChatViewController = self.storyboard?.instantiateViewController(withIdentifier: "SingleChatViewController") as! SingleChatViewController
//        vc.roomId = tableItems[indexPath.row].room_number
//
//        vc.recName = tableItems[indexPath.row].receiver_detail.username
//        vc.recImage = tableItems[indexPath.row].receiver_detail.profile_picture
//
//
//        self.navigationController?.pushViewController(vc, animated: true)
        
    }
}



// MARK: - WebSocketDelegate
extension RoomListViewController: WebSocketDelegate {
    func websocketDidConnect(socket: WebSocketClient) {
        print("Web socket connected");
        let json: [String: Any] = ["type": "allRooms", "userList": [userName]]
        if let jsonString: NSString = JsonOperation.toJsonStringFrom(dictionary: json) {
            self.socket.write(string: jsonString as String)
        }
        
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
              let jsonDict = jsonData as? [String: Any] else { return }
        
        if let data = jsonDict["data"] as? [String: Any] {
            
            RoomListViewController.userDetailsList = UserDetailsModel.giveList(list: data["userList"] as? [[String: Any]] ?? [])
            tableItems = ChatRoomModel.giveList(list: data["roomList"] as? [[String: Any]] ?? [], userId: userName)
            
            tableView.reloadData()
        }
        
        
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
