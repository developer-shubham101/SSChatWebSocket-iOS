//
//  UsersListForLoginViewController.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 25/04/21.
//

import UIKit

class UsersListForLoginViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    
    fileprivate var tableItems: [TmpUsers] = [TmpUsers(email: "anil@yopmail.com", password: "123456", userId: "1", name: "Anil"),
                                              TmpUsers(email: "amit@yopmail.com", password: "123456", userId: "2", name: "Amit"),
                                              TmpUsers(email: "shubham@yopmail.com", password: "123456", userId: "3", name: "Shubham"),
                                              TmpUsers(email: "ali@yopmail.com", password: "123456", userId: "4", name: "Ali"),
                                              TmpUsers(email: "samreen@yopmail.com", password: "123456", userId: "5", name: "Samreen")
    ]
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.title = "Select Users"
        initCollection()
        
        SocketManager.shared.connectSocket(notify: true)
        SocketManager.shared.registerToScoket(observer: self )
        
//        nextButtonItem.isEnabled = true
        
        
        
        
        navigationItem.hidesBackButton = true
    }
    
    
    func initCollection() {
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    
}

extension UsersListForLoginViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        print("rowwww count", tableItems.count)
        return tableItems.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 83
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let identifier = "SampleUserCellTableViewCell"
        var cell: SampleUserCellTableViewCell! = tableView.dequeueReusableCell(withIdentifier: identifier) as? SampleUserCellTableViewCell
        if cell == nil {
            tableView.register(UINib(nibName: identifier, bundle: nil), forCellReuseIdentifier: identifier)
            cell = tableView.dequeueReusableCell(withIdentifier: identifier) as? SampleUserCellTableViewCell
        }
        cell.configData(obj: tableItems[indexPath.row])
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let selectedUser = tableItems[indexPath.row]
        
        
        let messageDictionary = [
            "request": "login",
            "userId": selectedUser.userId,
            "type": "loginOrCreate",
            "fcm_token": LoginUserModel.shared.fCMToken,
            "userName": selectedUser.email,
            "password": selectedUser.password,
        ] as [String : Any]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: messageDictionary)
        let jsonString = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)
        if let message:String = jsonString as String?{
            SocketManager.shared.sendMessageToSocket(message: message)
            //            socket.write(string: message) //write some Data over the socket!
        }
        
    }
}



extension UsersListForLoginViewController: SocketObserver {
    func registerFor() -> [ResponseType] {
        return [.loginOrCreate]
    }
    
    func brodcastSocketMessage(to observerWithIdentifire: ResponseType, statusCode: Int, data: [String : Any], message: String) {
        print("observer ",{observerWithIdentifire})
        guard let data = data["data"] as? [String : Any] else {
            return
        }
        
        LoginUserModel.shared.login(userData: data)
        print("UsersListForLoginViewController:: \(LoginUserModel.shared.userId)")
        
        let vc = RoomListViewController()
        
        self.navigationController?.show(vc, sender: nil)
    }
    
    
    func socketConnection(status: SocketConectionStatus) {
        print(status)
    }
}
