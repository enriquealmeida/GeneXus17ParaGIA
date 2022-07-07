
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


@class APPDFDocumentBuilderPrivate;



/**
 * Driver class for performing "assembly operations" on PDFs, 
 * including adding/removing/moving/rotating pages, in order to
 * "build" a new PDF from an original.
 * 
 * Operations are queued by this class and applied when one of 
 * the export methods are called to create the new PDF.
 */
@interface APPDFDocumentBuilder : NSObject
{
@private
    APPDFDocumentBuilderPrivate *m_private;
}



/**
 * The delegate used for progress and password callbacks.
 */
@property(assign) id<APPDFDocumentBuilderDelegate> delegate;



/**
 * Initializes a new Document Builder starting with an empty PDF 
 * (having no pages -- pages must be added).
 */
-(id)init;


/**
 * Creates a Document Builder starting with a pre-existing PDF file.
 *
 * \param pdfPath The full path to a PDF file.  This file will not be changed.
 *
 * \return An initialized APPDFDocumentBuilder object, or <code>nil</code> if
 * a PDF file could not be found at the specified path.
 */
-(id)initWithPDFAtPath:(NSString *)pdfPath;


/**
 * Creates a Document Builder starting with a pre-existing PDF in data.
 *
 * \param pdfData An object containing a PDF.
 *
 * \return An initialized APPDFDocumentBuilder object, or <code>nil</code> if
 * the data did not contain a valid PDF.
 */
-(id)initWithPDFData:(NSData *)pdfData;


/**
 * Use this method to find out how many pages are currently in the
 * document being constructed (i.e. if you were to export it now)
 * based on the queued operations.
 *
 * \return the current number of pages in the document being constructed 
 */
-(NSUInteger)currentPageCount;


/**
 * Convenience method to find out the current rotation angle in degrees of a
 * page relative to its original orientation in the document being constructed 
 * based on the queued operations.  Positive angles are clockwise rotations,
 * negative angles are counter-clockwise rotations.
 *
 * \param pageIndex The 0-based page index of the page to get the angle of
 *
 * \return the current angle in degrees of the specified page
 */
-(NSInteger)angleOfPageAtIndex:(NSUInteger)pageIndex;


/**
 * Convenience method to find out the the index that a page would be at
 * in an exported PDF given the currently-queued operations based on its
 * index in the original PDF.
 *
 * \param pageIndex The 0-based page index of the page in the original PDF
 *
 * \return the 0-based page index the page will have in the constructed PDF
 */
-(NSInteger)currentPageIndexForPageOriginallyAtIndex:(NSUInteger)pageIndex;


/**
 * Remove all pages in a specified range from the PDF under construction.
 *
 * \param pageRange A range of pages to remove.  Use a range of (0, -1) to specify the whole document.
 *
 * \return YES if the operation was successfully queued; NO if the operation was incorrectly specified (see the NSLog for details).
 */
-(BOOL)deletePageRange:(NSRange)pageRange;


/**
 * Insert blank pages into the PDF under construction.
 *
 * \param pageSize The size of in PDF coordinates (e.g., 612 x 792) of the page to add.
 *
 * \param pageIndex The 0-based page index of the spot to insert the page.  
 * 0 specifies that it will be the first page; N specifies it will be the last (when there are N pages).
 *
 * \return YES if the operation was successfully queued; NO if the operation was incorrectly specified (see the NSLog for details).
 */
-(BOOL)insertBlankPageOfSize:(CGSize)pageSize atIndex:(NSUInteger)pageIndex;


                          

/**
 * Move a range of pages to a different location in the PDF under construction. 
 *
 * \param pageRange A range of pages to move. 
 *
 * \param pageIndex The 0-based page index of the spot where the pages will be inserted.
 * 0 specifies that they will be before the first page; N specifies that they will be after the last (when there are N pages).
 *
 * \return YES if the operation was successfully queued; NO if the operation was incorrectly specified (see the NSLog for details).
 */
-(BOOL)movePageRange:(NSRange)pageRange toIndex:(NSUInteger)pageIndex;


/**
 * Rotate all pages in a specified range in the PDF under construction.
 *
 * \param pageRange A range of pages to rotate.  Use a range of (0, -1) to specify the whole document.
 *
 * \param angle The angle in degrees by which to rotate the pages.  Positive values are clockwise,
 * negative values are counter-clockwise.  Must be a multiple of 90 degrees.
 *
 * \return YES if the operation was successfully queued; NO if the operation was incorrectly specified (see the NSLog for details).
 */
-(BOOL)rotatePageRange:(NSRange)pageRange byAngle:(NSInteger)angle;


/**
 * Apply all queued operations in order to the original PDF to create a new PDF.
 * Note: this operation will take a long time; it is recommended tha it be performed on a background thread.
 * The delegate's shouldCancelAfterProgress: method will be used for UI feedback during this time.
 *
 * Note that this is completely independent of any currently-open documents in any APPDFViewControllers.
 * If you want to reload a PDF view after constructing and exporting a new document via an APPDFDocumentBuilder,
 * you will need to create a new APPDFViewController using the new, exported PDF file.
 *
 * \param pdfPath A path where the newly-constructed PDF will be written.  There must not be a file there already
 * (including the original).
 *
 * \param error If non-NULL, will contain an error object denoting what went wrong if NO is returned.
 *
 * \return YES if the operations were successfully applied; NO if something went wrong (in which case "error" be set).
 */
-(BOOL)exportToPath:(NSString *)pdfPath withError:(NSError **)error;


/**
 * Apply all queued operations in order to the original PDF to create a new PDF.
 * Note: this operation will take a long time; it is recommended tha it be performed on a background thread.
 * The delegate's shouldCancelAfterProgress: method will be used for UI feedback during this time.
 *
 * Note that this is completely independent of any currently-open documents in any APPDFViewControllers.
 * If you want to reload a PDF view after constructing and exporting a new document via an APPDFDocumentBuilder,
 * you will need to create a new APPDFViewController using the new, exported PDF file.
 *
 * \param error If non-NULL, will contain an error object denoting what went wrong if NO is returned.
 *
 * \return A NSMutableData object containing the new PDF if the operations were successfully applied; 
 * nil if something went wrong (in which case "error" be set).
 */
-(NSMutableData *)exportToDataWithError:(NSError **)error;



@end
