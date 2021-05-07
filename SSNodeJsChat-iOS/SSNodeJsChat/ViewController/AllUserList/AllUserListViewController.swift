//
//  AllUserListViewController.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 24/01/21.
//

import UIKit
import Starscream

class AllUserListViewController: UIViewController{
    
    
    @IBOutlet weak var tableView: UITableView!
    
    fileprivate var tableItems: [UserDetailsModel] = []
    
    override func viewWillAppear(_ animated: Bool) {
        initCollection()
        let json: [String: Any] = ["request":"users","type": "allUsers"]
        if let jsonString: NSString = JsonOperation.toJsonStringFrom(dictionary: json) {
            SocketManager.shared.sendMessageToSocket(message: jsonString as String)
        }
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        SocketManager.shared.registerToScoket(observer: self )
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        SocketManager.shared.unregisterToSocket(observer: self)
    }
    
    func initCollection() {
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    deinit {
        print("deinit Called:: AllUserListViewController ")
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
    @IBAction func notification(_ sender: Any) {
        //        let vc = self.storyboard?.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
        //        self.navigationController?.pushViewController(vc, animated: true)
    }
    
}
extension AllUserListViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        print("rowwww count", tableItems.count)
        return tableItems.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 83
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let identifier = "UserTableViewCell"
        var cell: UserTableViewCell! = tableView.dequeueReusableCell(withIdentifier: identifier) as? UserTableViewCell
        if cell == nil {
            tableView.register(UINib(nibName: identifier, bundle: nil), forCellReuseIdentifier: identifier)
            cell = tableView.dequeueReusableCell(withIdentifier: identifier) as? UserTableViewCell
        }
        cell.configData(obj: tableItems[indexPath.row])
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let json: [String: Any] = ["type": "createRoom",
                                   "userList": [LoginUserModel.shared.userId, tableItems[indexPath.row].userId],
                                   "createBy": LoginUserModel.shared.userId,
                                   "request":"room"]
        if let jsonString: NSString = JsonOperation.toJsonStringFrom(dictionary: json) {
            SocketManager.shared.sendMessageToSocket(message: jsonString as String)
        }
        
        //
        //      let vc:SingleChatViewController = self.storyboard?.instantiateViewController(withIdentifier: "SingleChatViewController") as! SingleChatViewController
        //
        //      vc.roomId = tableItems[indexPath.row].room_number
        //
        //      vc.recName = tableItems[indexPath.row].receiver_detail.username
        //      vc.recImage = tableItems[indexPath.row].receiver_detail.profile_picture
        //
        //
        //    self.navigationController?.pushViewController(vc, animated: true)
        //
        
    }
}



// MARK: - WebSocketDelegate
extension AllUserListViewController:SocketObserver {
    func registerFor() -> [ResponseType] {
        return [.allUsers]
    }
    
    func brodcastSocketMessage(to observerWithIdentifire: ResponseType, statusCode: Int, data: [String : Any], message: String) {
        print(data)
        
        if observerWithIdentifire == .allUsers {
            if let data = data["data"] as? [[String: Any]] {
                
                tableItems = UserDetailsModel.giveList(list: data)
                ///Exclude Login user
                tableItems = tableItems.filter({ (element) -> Bool in
                    return element.userId != LoginUserModel.shared.userId
                })
                self.tableView.reloadData()
            }
        }
        
    }
    
    func socketConnection(status: SocketConectionStatus) {
        
    }
    
}
