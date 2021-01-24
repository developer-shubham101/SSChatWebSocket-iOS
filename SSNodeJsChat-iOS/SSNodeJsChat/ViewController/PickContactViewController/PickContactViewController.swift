//
//  PickContactViewController.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 24/01/21.
//

import UIKit
import SSViews

protocol ContactViewDelegate {
    func selected(contacts: [MyContact])
}
class PickContactViewController: UIViewController {
    
    @IBOutlet weak var searchField: UITextField!
    @IBOutlet weak var contactTableView: UITableView!
    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var collectionFlow: UICollectionViewFlowLayout!
 
    
     
    var delegateContact: ContactViewDelegate?
    
    fileprivate var viewloader: UIView?
    fileprivate var viewModel = ContactViewModel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        viewloader = getActivityIndicator("Loading...")
     
        
        self.searchField.addTarget(self, action: #selector(updateSearch(sender:)), for: .editingChanged)
        
        initTable()
        viewModel.requestAccess { (response) in
            if (response){
                self.viewModel.fetchContact(completion: { (isSuccess) in
                    if isSuccess  {
                        DispatchQueue.main.async {
                            self.contactTableView.reloadData()
                        }
                    }else{
                        print("unable to fetch contacts")
                    }
                })
            }else {
                DispatchQueue.main.async {
                    self.showSettingsAlert()
                }
            }
        }
        //        IQKeyboardManager.shared().disabledToolbarClasses.add(ContactViewController.self)
        //NotificationCenter.default.addObserver(self, selector: #selector( keyboardFrameChangeNotification(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        //NotificationCenter.default.addObserver(self, selector: #selector( keyboardFrameChangeNotification(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)
        //        fetchContact()
    }
    @IBAction func didTapBack(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
//        self.dismiss(animated: true)
    }
    @IBAction func didTapDone(_ sender: UIButton) {
        delegateContact?.selected(contacts: viewModel.selectedList)
        self.navigationController?.popViewController(animated: true)
    }
//    @objc func updateSearch(sender:UITextField) {
//        viewModel.updateSearch(keyWord: sender.text!)
//        self.contactTableView.reloadData()
//    }
    fileprivate func initTable() {
        contactTableView.delegate = self
        contactTableView.dataSource = self
        
        
        collectionView.dataSource = self
        collectionView.delegate = self
        
        
        // Setting the space between cells
        collectionFlow.minimumInteritemSpacing = 0
        collectionFlow.minimumLineSpacing = 0
        collectionFlow.scrollDirection = .horizontal
        
        collectionView.register(UINib(nibName: "SelectedContactCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "SelectedContactCollectionViewCell")
        
    }
    
    /*@objc func keyboardFrameChangeNotification(notification: Notification) {
        if let userInfo = notification.userInfo {
            let endFrame = userInfo[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect
            let animationDuration = userInfo[UIResponder.keyboardAnimationDurationUserInfoKey] as? Double ?? 0
            let animationCurveRawValue = (userInfo[UIResponder.keyboardAnimationCurveUserInfoKey] as? Int) ?? Int(UIView.AnimationOptions.curveEaseInOut.rawValue)
            let animationCurve = UIView.AnimationOptions(rawValue: UInt(animationCurveRawValue))
            let isKeyboardShowing = notification.name == UIResponder.keyboardWillShowNotification
            
            
            searchBoxBottomCons.constant = isKeyboardShowing ? endFrame?.height ?? 0 : 10
            
            UIView.animate(withDuration: animationDuration, delay: TimeInterval(0), options: animationCurve, animations: {
                self.view.layoutIfNeeded()
            }, completion: nil)
        }
    }*/
    private func showSettingsAlert() {
        let alert = UIAlertController(title: nil, message: "This app requires access to Contacts to proceed. Would you like to open settings and grant permission to contacts?", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Open Settings", style: .default) { action in
            
            UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!)
        })
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel) { action in
            
        })
        present(alert, animated: true)
    }
    
    
    
//    fileprivate func syncContact(list:[[String: Any]]) {
//
//        view.addSubview(viewloader!)
//
//        viewModel.syncContact(isInvited: .askFriend, list: list) { (error) in
//            self.viewloader?.removeFromSuperview()
//            if let errorMessage = error {
//                self.showAlertWithMessage(message: errorMessage)
//            }else{
//                self.contactTableView.reloadData()
//            }
//        }
//    }
}

//MARK:- Table view delegats
extension PickContactViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return viewModel.contactList.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 90
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let identifier = "ContactTableViewCell"
        var cell: ContactTableViewCell! = tableView.dequeueReusableCell(withIdentifier: identifier) as? ContactTableViewCell
        if cell == nil {
            tableView.register(UINib(nibName: identifier, bundle: nil), forCellReuseIdentifier: identifier)
            cell = tableView.dequeueReusableCell(withIdentifier: identifier) as? ContactTableViewCell
        }
        cell.selectionStyle = .none
        cell.configData(data: viewModel.contactList[indexPath.row])
//        if (viewModel.contactList[indexPath.row].isRegistered){
//            cell.actionButton.isHidden = false
//            cell.actionButton.tag = indexPath.row
//            if (self.__root == .syncCode){
//                cell.actionButton.setTitle("Invite", for: UIControl.State.normal)
//                cell.actionButton.addTarget(self, action: #selector(invite), for: UIControl.Event.touchUpInside)
//            }else{
//                cell.actionButton.setTitle("Request", for: UIControl.State.normal)
//                cell.actionButton.addTarget(self, action: #selector(request), for: UIControl.Event.touchUpInside)
//            }
//        }else {
//            cell.actionButton.isHidden = true
//        }
        
        return cell
        
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        viewModel.select(indexPath: indexPath)
        collectionView.reloadData()

        tableView.reloadData()
    }
}


extension PickContactViewController: UITextFieldDelegate {
    @objc func updateSearch(sender: UITextField) {
        viewModel.updateSearch(keyWord: sender.text!)
        self.contactTableView.reloadData()
    }
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField == searchField {
            //any task to perform
            textField.resignFirstResponder() //if you want to dismiss your keyboard
            
//            tableList = self.usersList.filter({ (element) -> Bool in
//                return element.name.lowercased().contains(textField.text!.lowercased())
//            })
//            tableView.reloadData()
        }
        return true
    }
}
extension PickContactViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 5
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return viewModel.selectedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "SelectedContactCollectionViewCell", for: indexPath) as! SelectedContactCollectionViewCell
        cell.configData(data: viewModel.selectedList[indexPath.row] )
        return cell
    }
    
    //    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
    //        return CGSize(width: (collectionView.frame.size.width / 2) - 10, height: (collectionView.frame.size.width / 2) - 10) ;
    //    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 100, height: 122 );
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
    }
}
//MARK:- Web service
extension PickContactViewController {
//    fileprivate func sendRequest(id: Int) {
//
//        view.addSubview(viewloader!)
//
//        let params:Parameters = ["user_id": id]
//        NetworkManager.sendRequest( parameters: params  ) { (resStatus) in
//            self.viewloader?.removeFromSuperview()
//            switch resStatus {
//            case .failed(let errorMessage ):
//                self.showAlertWithMessage(message: errorMessage)
//            case .success(let response ):
//                let isSuccess:Int = response["status_code"] as! Int
//                if(isSuccess == 200){
//                    self.showAlertWithMessage(message: response["message"] as! String, completion: {
//                        self.dismiss(animated: true)
//                        self.__delegateContact?.submitted(requested: true)
//                    })
//                    //                    let data = response["data"] as! [[String: Any]]
//                    //                    self.contactList = MyContact.giveList(list: data)
//                    //                    self.contactTableView.reloadData()
//
//                } else if(isSuccess == 500) {
//                    self.showAlertWithMessage(message: response["message"] as! String)
//                }
//
//            }
//
//        }
//    }
}
