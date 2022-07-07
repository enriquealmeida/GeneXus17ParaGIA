//
//  GXFirebaseRemoteConfigurationProvider.swift
//

import GXFoundation
import GXCoreModule_SD_RemoteConfig
import FirebaseRemoteConfig

public class GXFirebaseRemoteConfigurationProvider: NSObject {
	internal class func register() {
		let providerName = "FIREBASE_REMOTE_CONFIGURATION_PROVIDER"
		GXRemoteConfigurationProviders.register(shared, forName: providerName) { settings in
			let rc = shared.remoteConfig
			if rc.configSettings.minimumFetchInterval != settings.minInterval {
				let rcSettings = RemoteConfigSettings()
				rcSettings.minimumFetchInterval = settings.minInterval
				rc.configSettings = rcSettings
			}
			rc.setDefaults(settings.defaultValues as [String : NSObject])
		}
	}
	
	// MARK: Singleton

	private static let shared = GXFirebaseRemoteConfigurationProvider()
	
	private override init() {
		super.init()
	}
	
	// MARK: Private Helpers
	
	private var remoteConfig: RemoteConfig {
		return RemoteConfig.remoteConfig()
	}
	
	private func remoteConfigNonStaticValue(forKey key: String) -> RemoteConfigValue? {
		let value = remoteConfig.configValue(forKey: key)
		return value.source == .static ? nil : value
	}
}

extension GXFirebaseRemoteConfigurationProvider: GXRemoteConfigurationProvider {
	public var lastFetchStatus: FetchStatus {
		switch remoteConfig.lastFetchStatus {
		case .noFetchYet:
			return .none
		case .success:
			return .success
		case .failure, .throttled:
			return .failure
		@unknown default:
			fatalError()
		}
	}
	
	public var lastSuccessfulFetch: Date? {
		return remoteConfig.lastFetchTime
	}
	
	public func hasValue(for key: Key) -> Bool {
		return remoteConfigNonStaticValue(forKey: key) != nil
	}
	
	public func stringValue(for key: Key) -> String? {
		return remoteConfigNonStaticValue(forKey: key)?.stringValue
	}
	
	public func fetch(completion: ((Error?) -> Void)?) {
		guard let completion = completion else {
			remoteConfig.fetch()
			return
		}
		remoteConfig.fetch { (status, error) in
			let completionError: Error? = {
				if error != nil || status == .success {
					return error
				}
				else if status == .throttled {
					return NSError.defaultGXError(withDeveloperDescription: "Throttled: fetching to frequently.")
				}
				else {
					return NSError.defaultGXError()
				}
			}()
			completion(completionError)
		}
	}
	
	public func apply(completion: ((Error?) -> Void)?) {
		guard let completion = completion else {
			remoteConfig.activate()
			return
		}
		remoteConfig.activate { (_, error) in
			completion(error)
		}
	}
}

extension GXFirebaseRemoteConfigurationProvider: GXRemoteConfigurationTypedProvider {
	public func booleanValue(for key: Key) -> Bool? {
		return remoteConfigNonStaticValue(forKey: key)?.boolValue
	}

	public func numberValue(for key: Key) -> NSNumber? {
		return remoteConfigNonStaticValue(forKey: key)?.numberValue
	}
}
