//
//  Repository.swift
//  demo-ios
//
//  Created by Sam Da Costa on 07/11/2022.
//

import Foundation
import mockzilla

struct GetCowRequestDto: Codable {
    let aValueInTheRequest: String

    func toJson(_ encoder: JSONEncoder = JSONEncoder()) -> Data {
        try! encoder.encode(self)
    }

    static func fromJson(data: Data, _ decoder: JSONDecoder = JSONDecoder()) throws -> GetCowRequestDto {
        return try decoder.decode(GetCowRequestDto.self, from: data)
    }
}

struct CowDto: Codable {
    let name: String
    let age: Int
    let likesGrass: Bool
    let hasHorns: Bool
    let mooSample: String
    let someValueFromRequest: String

    static let empty = CowDto(
        name: "",
        age: 0,
        likesGrass: true,
        hasHorns: false,
        mooSample: "",
        someValueFromRequest: ""
    )
    
    func toJson(_ encoder: JSONEncoder = JSONEncoder()) -> String {
        let data = try! encoder.encode(self)
        return String(decoding: data, as: UTF8.self)
    }

    static func fromJson(data: Data, _ decoder: JSONDecoder = JSONDecoder()) throws -> CowDto {
        return try decoder.decode(CowDto.self, from: data)
    }
}

class Repository {
    let url: URL
    let authProvider: AuthHeaderProvider
    init(baseUrl: String, authHeaderProvider: AuthHeaderProvider) {
        url = URL(string: "\(baseUrl)/cow")!
        authProvider = authHeaderProvider
    }

    func getCow(someRequestValue: String,
                completion: @escaping (_ result: CowDto?, _ errorString: String?) -> Void)
    {
        self.authProvider.generateHeader { header, _ in
            var request = URLRequest(url: self.url)
            request.httpMethod = "POST"
            
            request.setValue(header!.value, forHTTPHeaderField: header!.key)
            request.httpBody = GetCowRequestDto(aValueInTheRequest: someRequestValue).toJson()
            
            let task = URLSession.shared.dataTask(with: request) { data, _, error in
                if let data = data {
                    do {
                        try completion(CowDto.fromJson(data: data), nil)
                    } catch {
                        completion(nil, String(describing: data))
                    }
                } else if let error = error {
                    completion(nil, error.localizedDescription)
                } else {
                    completion(nil, "Unknown error")
                }
            }
            
            task.resume()
        }
    }
}
