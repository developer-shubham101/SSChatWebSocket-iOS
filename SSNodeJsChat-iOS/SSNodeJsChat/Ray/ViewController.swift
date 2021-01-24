import UIKit
import Starscream

final class ViewController: UIViewController {
    
    // MARK: - Properties
    var username = ""
    var socket = WebSocket(url: URL(string: "ws://127.0.0.1:1337/")!, protocols: ["chat"])
    var socket2 = WebSocket(url: URL(string: "ws://127.0.0.1:1337/?path=data")!, protocols: ["chat"])
    
    // MARK: - IBOutlets
    @IBOutlet var emojiLabel: UILabel!
    @IBOutlet var usernameLabel: UILabel!
    
    // MARK: - View Life Cycle
    override func viewDidLoad() {
        super.viewDidLoad()
        
        socket.delegate = self
        socket.connect()
        
        socket2.delegate = self
        socket2.connect()
        
        
        navigationItem.hidesBackButton = true
    }
    
    deinit {
        socket.disconnect(forceTimeout: 0)
        socket.delegate = nil
    }
}

// MARK: - IBActions
extension ViewController {
    
    @IBAction func selectedEmojiUnwind(unwindSegue: UIStoryboardSegue) {
        guard let viewController = unwindSegue.source as? CollectionViewController,
              let emoji = viewController.selectedEmoji() else {
            return
        }
        
        sendMessage(emoji)
    }
}

// MARK: - FilePrivate
extension ViewController {
    
    fileprivate func sendMessage(_ message: String) {
        socket.write(string: "this is message \(message)")
    }
    
    fileprivate func messageReceived(_ message: String, senderName: String) {
        emojiLabel.text = message
        usernameLabel.text = senderName
    }
}

// MARK: - WebSocketDelegate
extension ViewController : WebSocketDelegate {
    func websocketDidConnect(socket: WebSocketClient) {
        socket.write(string: username)
    }
    
    func websocketDidDisconnect(socket: WebSocketClient, error: Error?) {
        performSegue(withIdentifier: "websocketDisconnected", sender: self)
    }
    
    func websocketDidReceiveMessage(socket: WebSocketClient, text: String) {
        /* Message format:
         * {"type":"message","data":{"time":1472513071731,"text":"😍","author":"iPhone Simulator","color":"orange"}}
         */
        print(text)
        guard let data = text.data(using: .utf16),
              let jsonData = try? JSONSerialization.jsonObject(with: data),
              let jsonDict = jsonData as? [String: Any],
              let messageType = jsonDict["type"] as? String else { return }
        
        if messageType == "message",
           let messageData = jsonDict["data"] as? [String: Any],
           let messageAuthor = messageData["author"] as? String,
           let messageText = messageData["text"] as? String {
            
            messageReceived(messageText, senderName: messageAuthor)
        }
    }
    
    func websocketDidReceiveData(socket: WebSocketClient, data: Data) {
        print(data)
    }
}
