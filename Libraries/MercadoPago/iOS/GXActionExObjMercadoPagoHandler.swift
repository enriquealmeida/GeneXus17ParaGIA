//
//  GXActionExObjMercadoPagoHandler.swift
//
//  Copyright © 2018 GeneXus. All rights reserved.
//

import Foundation
import GXCoreBL
import GXFoundation
import MercadoPagoServicesV4

@objc public class GXActionExObjMercadoPagoHandler: GXActionExternalObjectHandler {

	// MARK: - Overrides

	public override static func handleActionExecutionUsingMethodHandlerSelectorNamePrefix() -> Bool {
		return true
	}

	// MARK: - Action Handlers

	@objc public func gxActionExObjMethodHandler_StartCheckoutFlow() {
		onFinishedExecutingWithError(notImplementedError())
	}
	
	@objc public func gxActionExObjMethodHandler_GetCardToken(_ params: [Any]) {
		let methodName = "GetCardToken"
		
		guard let paymentMethod = readStringParameter(from: params, at: 0) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "PaymentMethod")
			return
		}
		guard let cardNumber = readStringParameter(from: params, at: 1) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "CardNumber")
			return
		}
		guard let securityCode = readIntParameter(from: params, at: 2) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "SecurityCode")
			return
		}
		guard let cardHolderName = readStringParameter(from: params, at: 3) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "CardHolderName")
			return
		}
		guard let identificationNumber = readStringParameter(from: params, at: 4) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "IdentificationNumber")
			return
		}
		guard let identificationType = readStringParameter(from: params, at: 5) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "IdentificationType")
			return
		}
		guard let expirationMonth = readIntParameter(from: params, at: 6) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "ExpiryMonth")
			return
		}
		guard let expirationYear = readIntParameter(from: params, at: 7) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "ExpiryYear")
			return
		}
		
		guard let publicKey = getPublickKey() else {
			onFinishedExecutingWithMissingPublicKeyError()
			return
		}

		let mpServices = MercadoPagoServices(merchantPublicKey: publicKey)
		mpServices.getPaymentMethods(callback: { (paymentMethods) in
			if let pm = paymentMethods.filter({ $0.id?.caseInsensitiveCompare(paymentMethod) == .orderedSame }).first {
				
				let shouldCheckIdType = (identificationType != "" && identificationType != "null")
				
				let idType = shouldCheckIdType ? identificationType   : "null"
				let idNum  = shouldCheckIdType ? identificationNumber : "null"
				
				let createToken: () -> Void = {
					let cardToken = PXCardToken(cardNumber: cardNumber,
												expMonth: expirationMonth,
												expYear: expirationYear,
												securityCode: "\(securityCode)",
												holderName: cardHolderName,
												idNumber: idNum,
												idType: idType)

					let (valid, error_, message_) = self.validateCardToken(cardToken, pm)
					if valid {
						mpServices.createToken(cardToken: cardToken,
											   callback: { self.onFinished(pxToken: $0) },
											   failure: { self.onFinished(pxError: $0) })
					}
					else {
						self.onFinished(error: error_, message: message_)
					}
				}
				
				if shouldCheckIdType {
					mpServices.getIdentificationTypes(callback: { (idTypes) in
						guard let _ = idTypes.filter({ $0.id?.caseInsensitiveCompare(identificationType) == .orderedSame }).first else {
							self.onFinishedExecutingWithError(NSError.defaultGXError(withDeveloperDescription: "Could not find identification type"))
							return
						}
						
						createToken()
					}, failure: { self.onFinished(pxError: $0) })
				}
				else {
					createToken()
				}
			}
			else {
				self.onFinished(error: "PaymentMethod", message: "Could no find payment method")
			}

		}, failure: { self.onFinished(pxError: $0) })
	}

	@objc public func gxActionExObjMethodHandler_GetSavedCardToken(_ params: [Any]) {
		let methodName = "GetSavedCardToken"
		guard let cardId = readStringParameter(from: params, at: 0) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "CardId")
			return
		}
		guard let securityCode = readIntParameter(from: params, at: 1) else {
			onFinishedExecutingWithWrongParameterError(methodName, paramName: "SecurityCode")
			return
		}
		
		guard let publicKey = getPublickKey() else {
			onFinishedExecutingWithMissingPublicKeyError()
			return
		}

		let mpServices = MercadoPagoServices(merchantPublicKey: publicKey)

		let savedCardToken = PXSavedCardToken()
		savedCardToken.cardId = cardId
		savedCardToken.securityCode = "\(securityCode)"
		
		mpServices.createToken(savedCardToken: savedCardToken,
							   callback: { self.onFinished(pxToken: $0) },
							   failure: { self.onFinished(pxError: $0) })
	}

	// MARK: - Private
	
	private func onFinished(pxToken: PXToken) {
		let result = successResult(tokenId: pxToken.id, lastFourDigits: pxToken.lastFourDigits)
		setReturnValue(result)
		onFinishedExecutingWithSuccess()
	}
	
	private func onFinished(pxError: PXError) {
		let apiException = pxError.apiException
		let result = errorResult(error: apiException?.error)
		setReturnValue(result)
		onFinishedExecutingWithSuccess()
	}
	
	private func onFinished(error: String?, message: String?) {
		let result = errorResult(error: error)
		setReturnValue(result)
		onFinishedExecutingWithSuccess()
	}
	
	private func successResult(tokenId: String, lastFourDigits: String?) -> Dictionary<String, String> {
		var result = GetCardTokenSDT()
		result.id = tokenId
		result.last_four_digits = lastFourDigits ?? ""
		return result.toDictionary()
	}
	
	private func errorResult(error: String?) -> Dictionary<String, String> {
		var result = GetCardTokenSDT()
		result.error = mercadoPagoErrorCodeEnum(fromError: error)
		return result.toDictionary()
	}
	
	private enum MercadoPagoErrorCode: Int {
		case ok = 0
		case cardNumber = 1
		case securityCode = 2
		case expirationDate = 3
		case cardholder = 4
		case idNumber = 5
		case idType = 6
		case paymentMethod = 7
		case other = 8
	}

	private func mercadoPagoErrorCodeEnum(fromError err: String?) -> MercadoPagoErrorCode {
		guard let err = err else { return .ok }
		
		switch err {
		case "CardNumber":
			return .cardNumber
		case "SecurityCode":
			return .securityCode
		case "ExpiryDate":
			return .expirationDate
		case "Cardholder":
			return .cardholder
		case "IdentificationNumber":
			return .idNumber
		case "IdentificationType":
			return .idType
		case "PaymentMethod":
			return .paymentMethod
		default:
			return .other
		}
	}
	
	private struct GetCardTokenSDT {
		var id: String = ""
		var last_four_digits: String = ""
		var error: MercadoPagoErrorCode = .ok
		
		public func toDictionary() -> Dictionary<String, String> {
			return [
				"id": id,
				"last_four_digits": last_four_digits,
				"Error": error.rawValue.stringValue
			]
		}
	}
	
	private func readStringParameter(from params: [Any], at index: Int) -> String? {
		if let descriptors = self.actionDescParametersArray, descriptors.count > index, params.count > index {
			return self.stringParameter(descriptors[index], fromValue: params[index])
		}
		return nil
	}

	private func readIntParameter(from params: [Any], at index: Int) -> Int? {
		if let descriptors = self.actionDescParametersArray, descriptors.count > index, params.count > index {
			return self.integerParameter(descriptors[index], fromValue: params[index])
		}
		return nil
	}
	
	static var staticPublicKey: String? = {
		guard
			let mainaName = GXModel.current()?.appModel.appEntryPoint?.appEntryPointName,
			let file = Bundle.main.url(forResource: "\((mainaName as NSString).lowercased).properties", withExtension: "json"),
			let data = try? Data(contentsOf: file),
			let jsonDocument = try? YAJLDocument(data: data, parserOptions: .none),
			let documentRoot = jsonDocument.root as? Dictionary<String, Dictionary<String, String>>,
			let value = documentRoot["properties"]?["MPPublicKey"]
		else {
			return nil
		}
		return value
	}()
	private func getPublickKey() -> String? {
		return type(of: self).staticPublicKey
	}

	// MARK: - Error handling

	func notImplementedError() -> Error {
		return NSError.defaultGXError(withDeveloperDescription: "not implemented")
	}

	func wrongParameterError(_ method: String, paramName: String) -> Error {
		return NSError.defaultGXError(withDeveloperDescription: "\(method):: \(paramName) parameter not found or has wrong type")
	}

	func onFinishedExecutingWithWrongParameterError(_ methodName: String, paramName: String) {
		self.onFinishedExecutingWithError(wrongParameterError(methodName, paramName: paramName))
	}
	
	func onFinishedExecutingWithMissingPublicKeyError() {
		let error = NSError.defaultGXError(withDeveloperDescription: "Public Key is missing")
		self.onFinishedExecutingWithError(error)
	}

	// MARK: - Validations

	typealias ValidationResult = (valid: Bool, error: String?, message: String?)

	func validateCardToken(_ cardToken: PXCardToken, _ paymentMethod: PXPaymentMethod) -> ValidationResult {
		if !cardToken.validateCardNumber(paymentMethod) {
			return (false, "CardNumber", "El número de la tarjeta es incorrecto.")
		}
		if !cardToken.validateSecurityCode(paymentMethod) {
			return (false, "SecurityCode", "El CVV de la tarjeta es incorrecto.")
		}
		if !cardToken.validateExpiryDate(cardToken.expirationMonth ?? 0,
										 year: cardToken.expirationYear ?? 0) {
			return (false, "ExpiryDate", "La fecha de vencimiento de la tarjeta es incorrecto.")
		}
		if !cardToken.validateCardholderName() {
			return (false, "CardHolderName", "El nombre de la tarjeta es incorrecto.")
		}
		if cardToken.validateIdentificationNumber(nil) {
			return (false, "IdentificationNumber", "La CI de la persona es incorrecta.")
		}
		return (true, nil, nil)
	}
}

extension PXCardToken {
	convenience internal init(cardNumber: String?, expMonth: Int?, expYear: Int?, securityCode: String?, holderName:String?, idNumber: String?, idType: String?) {
		
		self.init()
		
		self.cardNumber = cardNumber
		self.expirationMonth = expMonth
		self.expirationYear = expYear
		self.securityCode = securityCode
		
		let holderIdent = PXIdentification(number: idNumber, type: idType)
		self.cardholder = PXCardHolder(name: holderName, identification: holderIdent)
	}
}
