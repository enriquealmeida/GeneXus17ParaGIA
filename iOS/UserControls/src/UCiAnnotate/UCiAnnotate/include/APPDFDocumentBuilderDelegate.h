
/*
 *
 * iANNOTATE PDF SDK
 * PROPERTY OF BRANCHFIRE, INC
 * (C) COPYRIGHT 2010-13, ALL RIGHTS RESERVED
 *
 * BRANCHFIRE, INC., CONFIDENTIAL INFORMATION
 *
 * LIBRARY ACCESS AND DISTRIBUTION ARE SUBJECT TO LICENSE AGREEMENT
 * UNAUTHORIZED USE, REPRODUCTION, OR DISTRIBUTION IS PROHIBITED
 *
 * PLEASE CONTACT LIBRARY-SUPPORT@BRANCHFIRE.COM IF YOU HAVE ANY QUESTIONS REGARDING USE OR DISTRIBUTION
 *
 *
 */


#import "APPDF.h"


/**
 * Delegate notifications for the APPDFDocumentBuilder. 
 * All methods are optional.
 */
@protocol APPDFDocumentBuilderDelegate<NSObject>


@optional


/**
 * Used when the builder reaches a checkpoint during an export operation to indicate
 * progress and give the delegate an opportunity to cancel the opeartion.  A cancelled
 * export will return NO and set its error field appropriately.
 *
 * \param progress A value between 0 and 1 that specified how much of the export
 * operation has completed.
 *
 * \return YES is the operation should be cancelled/aborted; NO if it should continue.
 */
-(BOOL)documentBuilder:(APPDFDocumentBuilder *)builder shouldCancelAfterProgress:(CGFloat)progress;


/**
 * Used when the document builder requires a password to access or
 * modify the PDF file.
 *
 * \param builder The document builder object.
 *
 * \param passwordType The type of password required. Most often this
 * will be kAPPDFPasswordTypeUser for the user password, although in
 * some circumstances the owner password may be required.
 *
 * \param pdfPath The path of the PDF document for which the password 
 * is required.  OR...
 * \param pdfData NSData containing the PDF document for which the password 
 * is required. 
 *
 * \return The requested password, or <code>nil</code> to abort builder.
 */
-(NSString *)documentBuilder:(APPDFDocumentBuilder *)builder requiresPasswordOfType:(APPDFPasswordType)passwordType forPDFAtPath:(NSString *)pdfPath;
-(NSString *)documentBuilder:(APPDFDocumentBuilder *)builder requiresPasswordOfType:(APPDFPasswordType)passwordType forPDFData:(NSData *)pdfData;


/**
 * Notification that password validation succeeded.
 * 
 * \param builder The document builder object.
 *
 * \param password The password that was successfully validated.
 *
 * \param passwordType The type of password validated. Most often this
 * will be kAPPDFPasswordTypeUser for the user password, although in
 * some circumstances the owner password may be required.
 *
 * \param pdfPath The path of the PDF document for which the password 
 * was validated.  OR...
 * \param pdfData NSData containing the PDF document for which the password 
 * was validated.
 */
-(void)documentBuilder:(APPDFDocumentBuilder *)builder validatedPassword:(NSString *)password ofType:(APPDFPasswordType)passwordType forPDFAtPath:(NSString *)pdfPath;
-(void)documentBuilder:(APPDFDocumentBuilder *)builder validatedPassword:(NSString *)password ofType:(APPDFPasswordType)passwordType forPDFData:(NSData *)pdfData;


/**
 * Notification that password validation failed.
 * 
 * \param builder The document builder object.
 *
 * \param passwordType The type of password validated. Most often this
 * will be kAPPDFPasswordTypeUser for the user password, although in
 * some circumstances the owner password may be required.
 *
 * \param pdfPath The path of the PDF document for which the password 
 * could not be validated.  OR...
 * \param pdfData NSData containing the PDF document for which the password 
 * could not be validated.
 */
-(void)documentBuilder:(APPDFDocumentBuilder *)builder validationFailedForPasswordOfType:(APPDFPasswordType)passwordType forPDFAtPath:(NSString *)pdfPath;
-(void)documentBuilder:(APPDFDocumentBuilder *)builder validationFailedForPasswordOfType:(APPDFPasswordType)passwordType forPDFData:(NSData *)pdfData;


@end
