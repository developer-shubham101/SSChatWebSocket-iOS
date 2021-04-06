//
//  DateExtensions.swift
//  DragonFit
//
//  Created by Ajit Jain on 25/03/21.
//  Copyright © 2021 Devpoint. All rights reserved.
//

import Foundation

extension Date {
    static var yesterday: Date { return Date().dayBefore }
    static var tomorrow:  Date { return Date().dayAfter }
    static var lastWeak:  Date { return Date().dayAfter }
    static var lastMonth:  Date { return Date().monthBefore }
    
    var monthBefore: Date {
        return Calendar.current.date(byAdding: .month, value: -1, to: noon)!
    }
    var dayBefore: Date {
        return Calendar.current.date(byAdding: .day, value: -1, to: noon)!
    }
    var weakBefore: Date {
        return Calendar.current.date(byAdding: .weekOfYear, value: -1, to: noon)!
    }
    var dayAfter: Date {
        return Calendar.current.date(byAdding: .day, value: 1, to: noon)!
    }
    var noon: Date {
        return Calendar.current.date(bySettingHour: 12, minute: 0, second: 0, of: self)!
    }
    var month: Int {
        return Calendar.current.component(.month,  from: self)
    }
    var isLastDayOfMonth: Bool {
        return dayAfter.month != month
    }
    
    static func getLastMonth() -> (Date, Date) {
        
        let calendar = NSCalendar.current
        
        let components = calendar.dateComponents([.year, .month], from: Date.lastMonth)
        let startOfMonth = calendar.date(from: components)!
        
        
        var comps2 = DateComponents()
        comps2.month = 1
        comps2.day = -1
        let endOfMonth = Calendar.current.date(byAdding: comps2, to: startOfMonth)!
        
        
        return (startOfMonth, endOfMonth)
    }
    public func getTimeWith(format:String = "yyyy-MM-dd'T'HH:mm:ss") -> String {
        let dateFormatter : DateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        
        let dateString = dateFormatter.string(from: self)
        //  let interval = date.timeIntervalSince1970
        return dateString
    }
    public func toString(format:String = "yyyy-MM-dd'T'HH:mm:ss") -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        return dateFormatter.string(from: self)
    }
    
    public func getMonth(format:String = "MMM") -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        return dateFormatter.string(from: self)
    }
    
    public func getDay(format:String = "E") -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        return dateFormatter.string(from: self)
    }
    
    
    public static func getCurrentTimeBy(timeZone: String) -> String {
        // create dateFormatter with UTC time format
        let dateFormatter = DateFormatter()
        //        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        //        dateFormatter.timeZone = NSTimeZone(name: "Africa/Abidjan") as TimeZone?
        let date = Date() //dateFormatter.date(from: "2015-04-01T11:42:00")// create   date from string
        
        // change to a readable time format and change to local time zone
        dateFormatter.dateFormat = "h-m-s"
        dateFormatter.timeZone = NSTimeZone(name: timeZone) as TimeZone?
        return dateFormatter.string(from: date)
    }
    
}

