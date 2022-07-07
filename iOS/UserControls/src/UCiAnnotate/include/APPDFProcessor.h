
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


@class APPDFProcessorPrivate;
@class APPDFProcessorWriteOptions;
@class APPDFProcessorWriteOptionsPrivate;


/**
 * Driver class for processing PDF files and writing back annotations
 * to PDF files.
 *
 * In order to support certain reading and annotation features, the
 * PDF must first be <i>processed</i> to determine the locations of
 * all text on the document pages, as well as other information such
 * as links, existing annotations, and security information.
 *
 * This extraction process generally takes a "long" time, so this
 * information is cached on disk in the form of an APPDFInformation
 * object. That way, each document only needs to be processed once,
 * and then subsequent reads can access the information instantly.
 *
 * This PDF Information file is then also used to store and manage
 * annotations as the user creates them in real-time. When an
 * annotated PDF file is required, this driver class can be used to
 * generate it. (Generating an annotated PDF file can, again, be a
 * lengthy process, depending on the number, order, and location of
 * annotations; hence, it is only done on-demand using this driver
 * class.)
 *
 * It is highly recommend that this driver class is only used on a
 * background thread, as these operations can take significant amounts
 * of time (up to minutes for very large documents) depending on the
 * size and complexity of the document being processed. The delegate
 * callbacks in the APPDFProcessorDelegate protocol will be used to
 * report progress. Note that a delegate is required for encrypted PDF
 * documents, in order to provide the document password.
 *
 * Use the delegate to receive notifications of processing progress,
 * completion, and errors. Note that delegate callbacks will be
 * performed on a non-main thread, so your delegate may have to
 * perform selectors on the main thread to do any corresponding
 * interface updates.
 *
 * Also note that calls to the methods in this class will be
 * automatically serialized by the library to avoid over-utilization
 * of resources.
 *
 * Implementations may cache information about PDF documents across
 * processing and writeback requests, to improve performance for some
 * common scenarios. You may therefore wish to keep one instance of
 * the processor for the duration of your application, to take
 * advantage of this potential performance boost. On the other hand,
 * cache information may require additional memory resources; so at
 * the very least you should release any idle processor object in the
 * case of a memory warning. It is always safe to release the
 * processor object after using it.
 */
@interface APPDFProcessor : NSObject
{
@private
    APPDFProcessorPrivate *m_private;
}


/**
 * The delegate used for notification and information callbacks.
 */
@property(assign) id<APPDFProcessorDelegate> delegate;


/**
 * The options to use when processing PDF files. Defaults to
 * kAPPDFProcessingOptionsDefault.
 */
@property(assign) APPDFProcessingOptions processingOptions;


/**
 * Used to process the PDF Information from a PDF file. Use the
 * APPDFProcessorDelegate callbacks to obtain result or error
 * information, or notification when processing is complete.
 *
 * \param pdf The APPDFDocument to process. Upon success, the
 * APPDFDocument::information property of the file will be updated with
 * the information extracted from the document.
 */
-(void)processPDF:(APPDFDocument *)pdf;


/**
 * Used to sync the PDF file with the associated annotations in the
 * APPDFInformation file. Use the APPDFProcessorDelegate callbacks to
 * obtain result or error information, or notification when the
 * write-back is complete.
 *
 * If using a disk-based PDF, this method updates the PDF file
 * in-place, which is done via an atomic file replace operation if the
 * operation is successful. If using a data-based PDF, the
 * APPDFProcessorDelegate::pdfProcessor:didSyncAnnotationsToPDF:
 * delegate callback will be used to report the updated data.
 *
 * \param pdf The APPDFDocument which has annotations in its
 * APPDFDocument::information property.
 *
 * \sa APPDFInformation::isModified
 */
-(void)syncAnnotationsToPDF:(APPDFDocument *)pdf;


/**
 * Used to write the PDF content and annotations to another output PDF
 * file. Use the options parameter to control the method used to write
 * the output file. Use the APPDFProcessorDelegate callbacks to obtain
 * result or error information, or notification when the output is
 * complete.
 *
 * This method writes the output PDF file at the specified path. Any
 * existing file is removed and replaced. If the operation fails, the
 * status of the file at destPath is undefined.
 *
 * NSData backed PDFs must use the writePDFWithAnnotations:toData 
 * method instead of this one.
 *
 * \param pdf The APPDFDocument indicating the PDF and associated
 * annotations.
 *
 * \param destPath The full path of the output file.
 *
 * \param options Options used to control the output PDF.
 */
-(void)writePDFWithAnnotations:(APPDFDocument *)pdf toPath:(NSString *)destPath options:(APPDFProcessorWriteOptions *)options;


/**
 * Used to write the PDF content and annotations to an NSMutableData
 * object. Use the options parameter to control the method 
 * used to write the output data. Use the APPDFProcessorDelegate 
 * callbacks to obtain result or error information, or notification when
 * the output is complete.
 * 
 * File backed PDFs must use the writePDFWithAnnotations:toFile 
 * method instead of this one.
 *
 * \param pdf The APPDFDocument indicating the PDF and associated
 * annotations.
 *
 * \param destData The NSData object to hold the output PDF document.
 *
 * \param options Options used to control the output PDF.
 */
-(void)writePDFWithAnnotations:(APPDFDocument *)pdf toData:(NSMutableData *)destData options:(APPDFProcessorWriteOptions *)options;


@end


/**
 * Container class used to hold options when writing out PDF using
 * APPDFProcessor::writePDFWithAnnotations:toPath:options:
 */
@interface APPDFProcessorWriteOptions : NSObject
{
    APPDFProcessorWriteOptionsPrivate *m_private;
}


/**
 * Flags used to control the output PDF. The default is
 * kAPPDFWriteAnnotationOptionsDefault.
 */
@property(assign) APPDFWriteOptions flags;

/**
 * If the kAPPDFWriteOptionsUsePageRange option is specified, then
 * this range is used to control the range of pages from the base PDF
 * to output.
 */
@property(assign) NSRange pageRange;


/**
 * Convenience initializer
 */
-(id)initWithFlags:(APPDFWriteOptions)flags pageRange:(NSRange)pageRange;


/**
 * Convenience initializer
 */
-(id)initWithFlags:(APPDFWriteOptions)flags;


/**
 * Convenience initializer
 */
+(APPDFProcessorWriteOptions *)optionsWithFlags:(APPDFWriteOptions)flags pageRange:(NSRange)pageRange;


/**
 * Convenience initializer
 */
+(APPDFProcessorWriteOptions *)optionsWithFlags:(APPDFWriteOptions)flags;


@end
