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
    
    
    
    static var userDetailsList: [String: UserDetailsModel] = [:]
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initCollection()
        
        let editImage    = UIImage(named: "ic_edit")
//        let searchImage  = UIImage(named: "search")
        
        let editButton   = UIBarButtonItem(image: editImage, style: .plain, target: self, action: #selector(didTapEditButton(sender:)))
//        let searchButton = UIBarButtonItem(image: searchImage, style: .plain, target: self, action: #selector(didTapSearchButton(sender:)))
        
        
        navigationItem.rightBarButtonItems = [editButton]
        
        title = "Room"
        
    }
    @objc func didTapEditButton(sender: AnyObject){
        let vc: ProfileViewController = ProfileViewController()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @objc func didTapSearchButton(sender: AnyObject){
        
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        SocketManager.shared.registerToScoket(observer: self )
        let json: [String: Any] = ["request": "room",
                                   "type": "allRooms",
//                                   "userList": [LoginUserModel.shared.userId]
                                   "userList": []
        ]
        if let jsonString: NSString = JsonOperation.toJsonStringFrom(dictionary: json) {
            SocketManager.shared.sendMessageToSocket(message: jsonString as String)
        }
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
        
        self.navigationController?.pushViewController(vc, animated: true)
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        SocketManager.shared.unregisterToSocket(observer: self)
    }
    
    deinit {
        print("RoomListViewController::: deinit")
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
        
        let vc: ChatViewController = ChatViewController()
        vc.roomId = "\(tableItems[indexPath.row].id)"
        
        if let individualDetail: UserDetailsModel = RoomListViewController.userDetailsList[tableItems[indexPath.row].individualUserId] {
            vc.individualDetail = individualDetail
        }
        
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
}

// MARK: - WebSocketDelegate
extension RoomListViewController: SocketObserver {
    func registerFor() -> [ResponseType] {
        return [.allRooms, .roomsModified,. userModified, .createRoom]
    }
    
    func brodcastSocketMessage(to observerWithIdentifire: ResponseType, statusCode: Int, data: [String : Any], message: String) {
        
        print(data)
        if (observerWithIdentifire == .allRooms){
            if let data = data["data"] as? [String: Any] {
                let tmpUserList = UserDetailsModel.giveList(list: data["userList"] as? [[String: Any]] ?? [])
                
                tmpUserList.forEach { (element) in
                    RoomListViewController.userDetailsList[element.userId] = element
                }
                
                
                tableItems = ChatRoomModel.giveList(list: data["roomList"] as? [[String: Any]] ?? [] )
            }
            
            tableView.reloadData()
        } else if (observerWithIdentifire == .roomsModified){
            if let data = data["data"] as? [String: Any] {
                //                let tmpUserList = UserDetailsModel.giveList(list: data["userList"] as? [[String: Any]] ?? [])
                //
                //                tmpUserList.forEach { (element) in
                //                    RoomListViewController.userDetailsList[element.userId] = element
                //                }
                //
                //
                
                let updatedRoom = ChatRoomModel(disc: data)
                
                tableItems = tableItems.map { (element) -> ChatRoomModel in
                    var elementX = element
                    if (element.id == updatedRoom.id){
                        elementX = updatedRoom
                    }
                    
                    return elementX
                    
                }
                
                 
                tableView.reloadData()
                
                
            }
        } else if (observerWithIdentifire == .createRoom){
            if let data = data["data"] as? [String: Any] {
                let tmpUserList = UserDetailsModel.giveList(list: data["userList"] as? [[String: Any]] ?? [])

                tmpUserList.forEach { (element) in
                    RoomListViewController.userDetailsList[element.userId] = element
                }
                if let newRoom = data["newRoom"] as? [String: Any] {
                    let updatedRoom = ChatRoomModel(disc: newRoom)
                    tableItems.append(updatedRoom)
                }
    
                
              
//                tableItems = tableItems.map { (element) -> ChatRoomModel in
//                    var elementX = element
//                    if (element.id == updatedRoom.id){
//                        elementX = updatedRoom
//                    }
//
//                    return elementX
//
//                }
                
                 
                tableView.reloadData()
                
                
            }
        } else if observerWithIdentifire == .userModified {
            print("ChatViewController", observerWithIdentifire, message)
            if (statusCode == 200){
                if let data: [String : Any] = data["data"] as? [String: Any] {
                    let updatedUserInfo = UserDetailsModel.giveObj(cdic: data)
                    RoomListViewController.userDetailsList[updatedUserInfo.userId] = updatedUserInfo
                    tableView.reloadData()
                }
            }
        }
    }
    
    func socketConnection(status: SocketConectionStatus) {
        print("websocket connected!!")
    }
    
}
