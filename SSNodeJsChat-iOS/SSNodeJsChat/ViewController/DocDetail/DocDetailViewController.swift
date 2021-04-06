//
//  DocDetailViewController.swift
//  SSNodeJsChat
//
//  Created by Ajit Jain on 24/01/21.
//


import UIKit
import WebKit

class DocDetailViewController: UIViewController, WKNavigationDelegate, WKScriptMessageHandler {
     
    @IBOutlet weak var webView: WKWebView!
    @IBOutlet var activity: UIActivityIndicatorView!
    
    private var webViewContentIsLoaded = false
    
    var docUrlDetail:URL!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        activity.startAnimating()
        webView.configuration.userContentController.add(self, name: "paymentResponse")
        
        webView.scrollView.bounces = false
        webView.navigationDelegate = self
        
        if !webViewContentIsLoaded {
             
                let request = URLRequest(url: docUrlDetail)
                webView.load(request)
                webViewContentIsLoaded = true
             
            
        }
        
        
    }
    @IBAction func didTapGoBack(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    private func evaluateJavascript(_ javascript: String, sourceURL: String? = nil, completion: ((_ error: String?) -> Void)? = nil) {
        var javascript = javascript
        
        // Adding a sourceURL comment makes the javascript source visible when debugging the simulator via Safari in Mac OS
        if let sourceURL = sourceURL {
            javascript = "//# sourceURL=\(sourceURL).js\n" + javascript
        }
        
        webView.evaluateJavaScript(javascript) { _, error in
            completion?(error?.localizedDescription)
        }
    }
    
    // MARK: - WKNavigationDelegate
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        activity.stopAnimating()
        // This must be valid javascript!  Critically don't forget to terminate statements with either a newline or semicolon!
        _ =
            "var outerHTML = document.documentElement.outerHTML.toString()\n" +
                "var message = {\"type\": \"outerHTML\", \"outerHTML\": outerHTML }\n" +
        "window.webkit.messageHandlers.paymentResponse.postMessage(message)\n"
        
        //        evaluateJavascript(javascript, sourceURL: nil)
    }
    
 
    
    // MARK: - WKScriptMessageHandler
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        print(message.body)
        
//        guard let body = message.body as? [String: Any] else {
//            print("could not convert message body to dictionary: \(message.body)")
//            backAlert(message: "Payment Declined", isSuccess: false)
//            return
//        }
//
//        guard let status = body["Status"] as? String else {
//            print("could not convert body[\"type\"] to string: \(body)")
//            backAlert(message: "Payment Declined", isSuccess: false)
//            return
//        }
//
//        switch status {
//        case "FAILED":
//            backAlert(message: "Payment Declined", isSuccess: false)
//            print("Transaction Failed")
//            break
//
//        case "SUCCESS":
//            //            guard let transactionId = body["TransactionId"] as? String else {
//            //                print("could not convert body[\"outerHTML\"] to string: \(body)")
//            //                return
//            //            }
//            //            print("outerHTML is \(transactionId)")
//            backAlert(message: "Payment Success", isSuccess: true)
//            break
//        default:
//            print("unknown message type \(status)")
//            return
//        }
    }
    
}

//extension PaymentViewController: WKScriptMessageHandler {
//
//}

