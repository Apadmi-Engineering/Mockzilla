//
//  TypeUtils.swift
//  mockzilla_ios
//
//  Created by Tom Handcock on 22/03/2024.
//

import Foundation

class DictionaryUtils {
    static func removeNils<K, V>(_ dictionary: [K?: V?]) -> [K: V] {
        return dictionary.filter { (key, value) in
            return key != nil && value != nil
        } as! [K: V]
    }
}
