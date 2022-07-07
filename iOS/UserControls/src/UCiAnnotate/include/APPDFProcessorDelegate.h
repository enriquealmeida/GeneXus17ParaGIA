
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
 * Delegate protocol for providing required information to an
 * APPDFProcessor and for progress, completion, and error
 * notifications. All delegate methods are optional.
 */
@protocol APPDFProcessorDelegate


@optional


/**
 * Used when the processing engine requires a password to access or
 * modify the PDF file.
 *
 * \param processor The processing engine.
 *
 * \param passwordType The type of password required. Most often this
 * will be kAPPDFPasswordTypeUser for the user password, although in
 * some circumstances the owner password may be required.
 *
 * \param pdf The PDF document being processed.
 *
 * \return The requested password, or <code>nil</code> to abort
 * processing.
 */
-(NSString *)pdfProcessor:(APPDFProcessor *)processor requiresPasswordOfType:(APPDFPasswordType)passwordType forPDF:(APPDFDocument *)pdf;


/**
 * Notification that password validation succeeded.
 * 
 * \param processor The processing engine.
 *
 * \param password The password that was successfully validated.
 *
 * \param passwordType The type of password validated. Most often this
 * will be kAPPDFPasswordTypeUser for the user password, although in
 * some circumstances the owner password may be required.
 *
 * \param pdf The PDF document.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor validatedPassword:(NSString *)password ofType:(APPDFPasswordType)passwordType forPDF:(APPDFDocument *)pdf;


/**
 * Notification that password validation failed.
 * 
 * \param processor The processing engine.
 *
 * \param passwordType The type of password validated. Most often this
 * will be kAPPDFPasswordTypeUser for the user password, although in
 * some circumstances the owner password may be required.
 *
 * \param pdf The PDF document.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor validationFailedForPasswordOfType:(APPDFPasswordType)passwordType forPDF:(APPDFDocument *)pdf;


/**
 * Notification to indicate that the PDF processor will begin
 * processing a PDF file.
 *
 * \param processor The processing engine.
 *
 * \param pdf The PDF document being processed.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor willProcessPDF:(APPDFDocument *)pdf;


/**
 * Progress notification during the text indexing process, called for
 * each page in the PDF document (in order).
 *
 * \param processor The processing engine.
 *
 * \param page The page that has just been indexed.
 *
 * \param numPages The number of pages in the document.

 * \param pdf The PDF document being processed.
 *
 * \param [out] cancel A pointer to a boolean indicating whether the
 * processing should be cancelled; set this value to <code>YES</code>
 * to immediately abort processing, or leave it alone (set to
 * <code>NO</code>) to allow processing to continue.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor didIndexPage:(NSUInteger)page ofPage:(NSUInteger)numPages ofPDF:(APPDFDocument *)pdf shouldCancel:(BOOL *)cancel;


/**
 * Progress notification during the annotation extraction process,
 * called for each page in the PDF document (in order).
 *
 * \param processor The processing engine.
 *
 * \param page The page that has just been processed.
 *
 * \param numPages The number of pages in the document.
 *
 * \param pdf The PDF document being processed.
 *
 * \param [out] cancel A pointer to a boolean indicating whether the
 * processing should be cancelled; set this value to <code>YES</code>
 * to immediately abort processing, or leave it alone (set to
 * <code>NO</code>) to allow processing to continue.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor didProcessAnnotationsOnPage:(NSUInteger)page ofPage:(NSUInteger)numPages ofPDF:(APPDFDocument *)pdf shouldCancel:(BOOL *)cancel;


/**
 * Notification that processing has successfully finished for the
 * indicated PDF file.
 *
 * \param processor The processing engine.
 *
 * \param pdf The processed PDF document.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor didProcessPDF:(APPDFDocument *)pdf;


/**
 * Notification that an error was encountered processing the file, and
 * processing has been aborted.
 * 
 * See APProcessorError (in AjiPDFDefs.h) for the definition of error
 * codes in the kAPProcessorErrorDomain. Also, you may want to
 * implement the [APPDFProcessor
 * pdfProcessor:reportProcessingLog:forPDF:] delegate method for more
 * details on the failure, which can be useful in diagnosing the
 * problem with the PDF. (Please include this information if you need
 * to contact support about a processing error.)
 *
 * \param processor The processing engine.
 *
 * \param pdf The PDF document being processed.
 *
 * \param error An NSError object containing more detailed information
 * about the failure, including the APProcessorError error code.

 */
-(void)pdfProcessor:(APPDFProcessor *)processor failedToProcessPDF:(APPDFDocument *)pdf withError:(NSError *)error;


/**
 * Notification to indicate that the PDF processor will begin the
 * process of synchronizing the annotations to the PDF file.
 *
 * \param processor The processing engine.
 *
 * \param pdf The PDF document whose annotations will be written.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor willSyncAnnotationsToPDF:(APPDFDocument *)pdf;


/**
 * Notification that PDF file has been successfully updated.
 *
 * \param processor The processing engine.
 *
 * \param pdf The PDF document with annotations updated.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor didSyncAnnotationsToPDF:(APPDFDocument *)pdf;


/**
 * Notification that an error was encountered while attempting to
 * synchronize the annotations to the file; the PDF is unchanged.
 * 
 * See APProcessorError (in AjiPDFDefs.h) for the definition of error
 * codes in the kAPProcessorErrorDomain. Also, you may want to
 * implement the [APPDFProcessor
 * pdfProcessor:reportProcessingLog:forPDF:] delegate method for more
 * details on the failure, which can be useful in diagnosing the
 * problem with the PDF. (Please include this information if you need
 * to contact support about a writeback error.)
 *
 * \param processor The processing engine.
 *
 * \param pdf The PDF document which is being written.
 *
 * \param error An NSError object containing more detailed information
 * about the failure.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor failedToSyncAnnotationsToPDF:(APPDFDocument *)pdf withError:(NSError *)error;


/**
 * Notification to indicate that the PDF processor will begin the
 * process of creating an output PDF file.
 *
 * \param processor The processing engine.
 *
 * \param pdf The base PDF document and annotations.
 *
 * \param path The path to the output PDF file.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor willWritePDFWithAnnotations:(APPDFDocument *)pdf toPath:(NSString *)path;


/**
 * Notification to indicate that the PDF processor will begin the
 * process of creating an NSData-backed PDF document.
 *
 * \param processor The processing engine.
 *
 * \param pdf The base PDF document and annotations.
 *
 * \param data The NSData object to hold the output PDF document.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor willWritePDFWithAnnotations:(APPDFDocument *)pdf toData:(NSMutableData *)data;


/**
 * Notification that the output PDF file was successfully created.
 *
 * \param processor The processing engine.
 *
 * \param pdf The base PDF document and annotations.
 *
 * \param path The path to the output PDF file.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor didWritePDFWithAnnotations:(APPDFDocument *)pdf toPath:(NSString *)path;


/**
 * Notification that the NSData-backed output PDF document was successfully created.
 *
 * \param processor The processing engine.
 *
 * \param pdf The base PDF document and annotations.
 *
 * \param data The NSData object to hold the output PDF document.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor didWritePDFWithAnnotations:(APPDFDocument *)pdf toData:(NSMutableData *)data;


/**
 * Notification that an error was encountered while attempting to
 * create the output PDF file.
 * 
 * See APProcessorError (in AjiPDFDefs.h) for the definition of error
 * codes in the kAPProcessorErrorDomain. Also, you may want to
 * implement the [APPDFProcessor
 * pdfProcessor:reportProcessingLog:forPDF:] delegate method for more
 * details on the failure, which can be useful in diagnosing the
 * problem. (Please include this information if you need to contact
 * support about a writeback error.)
 *
 * \param processor The processing engine.
 *
 * \param pdf The base PDF document and annotations.
 *
 * \param path The path to the output PDF file.
 *
 * \param error An NSError object containing more detailed information
 * about the failure.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor failedToWritePDFWithAnnotations:(APPDFDocument *)pdf toPath:(NSString *)path withError:(NSError *)error;


/**
 * Notification that an error was encountered while attempting to
 * create the NSData-backed output PDF document.
 * 
 * See APProcessorError (in AjiPDFDefs.h) for the definition of error
 * codes in the kAPProcessorErrorDomain. Also, you may want to
 * implement the [APPDFProcessor
 * pdfProcessor:reportProcessingLog:forPDF:] delegate method for more
 * details on the failure, which can be useful in diagnosing the
 * problem. (Please include this information if you need to contact
 * support about a writeback error.)
 *
 * \param processor The processing engine.
 *
 * \param pdf The base PDF document and annotations.
 *
 * \param data The NSData object to hold the output PDF document.
 *
 * \param error An NSError object containing more detailed information
 * about the failure.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor failedToWritePDFWithAnnotationsToData:(APPDFDocument *)pdf withError:(NSError *)error;


/**
 * Notification that an error or warning was encountered while
 * performing an operation; but the error/warning is not fatal, so the
 * operation will continue.
 *
 * See APProcessorError (in AjiPDFDefs.h) for the definition of error
 * codes in the kAPProcessorErrorDomain. Also, you may want to
 * implement the [APPDFProcessor
 * pdfProcessor:reportProcessingLog:forPDF:] delegate method for more
 * details on the warning. (Please include this information if you
 * need to contact support about an error.)
 *
 * \param processor The processing engine.
 *
 * \param pdf The PDF document.
 *
 * \param error An NSError object containing more detailed information
 * about the warning or error.
 */
-(void)pdfProcessor:(APPDFProcessor *)processor encounteredNonFatalError:(NSError *)error whileProcessingPDF:(APPDFDocument *)pdf;


/**
 * Notification that a report log is available for the process/write
 * operation. If a process/write operation fails, this will generally
 * be reported (not in certain error cases when a log is not
 * applicable). This also may be reported even when a process/write
 * operation succeeds, to give information about warnings that may
 * have occurred (generally, these can be ignored, and only offer
 * additional diagnostic info: for example, corrupt or nonstandard
 * PDFs may trigger warnings). The log will have diagnostic
 * information about why the operation failed or any issues
 * encountered while processing the PDF. In cases where the library is
 * mishandling a PDF file, this report will often be useful in
 * determining the cause of the problem.
 *
 * This notification is for diagnostic purposes only and can safely be
 * ignored. All error reporting will be performed through the other
 * delegate notification callbacks, so that is the correct place to
 * perform any required error handling.
 *
 * \param processor The processing engine.
 *
 * \param processingLog The detailed log file.
 *
 * \param pdf The PDF document which is being written.
 *
 */
-(void)pdfProcessor:(APPDFProcessor *)processor reportProcessingLog:(NSString *)processingLog forPDF:(APPDFDocument *)pdf;


/**
 * Used to provide the name to be used for user bookmark annotations
 * in the PDF outline (if not provided, the default is "Branchfire
 * Bookmarks").
 */
-(NSString *)pdfProcessorRequiresNameForBookmarksRoot:(APPDFProcessor *)pdfProcessor;


/**
 * Used to determine if the indicated name matches the bookmarks root
 * name. The default implementation compares against
 * pdfProcessorRequiresNameForBookmarksRoot: and is usually sufficient.
 */
-(BOOL)pdfProcessor:(APPDFProcessor *)pdfProcessor isBookmarksRootName:(NSString *)name;


@end
