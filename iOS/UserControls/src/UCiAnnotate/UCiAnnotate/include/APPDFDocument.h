
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


@class APPDFDocumentPrivate;

/**
 * Encapsulates a PDF file, including its "Information" file.
 *
 * All PDF documents used with the API must be wrapped in an APPDFDocument
 * object before use. This class includes a reference to the full path
 * of the PDF file, and also a reference to its "Information", an
 * instance of the APPDFInformation class.
 */
@interface APPDFDocument : NSObject
{
@private
    APPDFDocumentPrivate *m_private;
}


/**
 * The full path to the PDF file (nil if this is an in-memory PDF)
 */
@property(readonly) NSString *path;


/**
 * The in-memory bytes for this PDF file (nil if this is a disk-based PDF).
 */
@property(readonly) NSData *data;


/**
 * The PDF Information file for this PDF file.
 */
@property(readonly) APPDFInformation *information;


/**
 * Document permissions. For most documents, this is
 * kAPPDFDocumentPermissionsAll, unless the document is encrypted and
 * a password is provided.
 * \sa APPDFProcessorDelegate
 * \sa APPDFPasswordType
 */
@property(readonly) APPDFDocumentPermissions permissions;


/**
 * Designated initializer for path-based PDF.
 *
 * \param path The full path to the PDF file.
 *
 * \param information The APPDFInformation for this PDF file.
 *
 * \return The initialized APPDFDocument object, or <code>nil</code> if
 * the PDF file could not be found at the specified path.
 */
-(id)initWithPath:(NSString *)path information:(APPDFInformation *)information;


/**
 * Convenience intializer which looks for the information file at the
 * same location as the PDF file, but with appending ".metadata" to
 * the filename. If the file is not found, it is created
 * uninitialized. Note that the document will have to be processed
 * before certain functionality will be enabled -- see APPDFProcessor
 * for more information.
 *
 * \param path The full path to the PDF file.
 *
 * \return <code>nil</code> if the PDF file could not be found at the specified path.
 */
-(id)initWithPath:(NSString *)path;


/**
 * Initializer for creating NSData-backed PDF.
 *
 * Note that the corresponding information object will be created
 * in-memory as well, so that no data is ever written or cached to
 * disk. In this case, the information object cannot be cached; so the
 * newly created APPDFDocument will have to be processed before
 * text-based features will be available.
 *
 * Note that the information object will be destroyed when this
 * document is destroyed. So, if you wish to persist the annotations,
 * you'll need to use APPDFProcessor to do the writeback (which will
 * update the NSMutableData object) before releasing this object.
 *
 * Please use care when using in-memory PDF, as the entire PDF, index
 * (if processed), and all annotation objects will be loaded in memory
 * -- the size of PDF you will be able to use will be limited by the
 * available RAM on the device.
 *
 * Once initialized, the passed-in data object becomes owned by 
 * the APPDFDocument object and should not be changed on any other
 * threads until the APPDFDocument object has been released.
 *
 * \param data The PDF data. 
 *
 * \return <code>nil</code> if the PDF file could not be created from the specified data object.
 */
-(id)initWithPDFData:(NSMutableData *)data;

/**
 * Initializer for creating NSData-backed PDF, using a file-based
 * information file.
 *
 * Use of this initializer is <b>NOT RECOMMENDED</b>: the information
 * file will be written in plaintext on disk, which means that all
 * annotations (and, if processed for search, the full-text index) in
 * the document will be stored unencrypted on the disk.
 *
 * Please be certain that you understand the security implications and
 * have a very specific use case before using this initializer.
 *
 * \sa APPDFDocument::initWithPDFData:
 */
-(id)initWithPDFData:(NSMutableData *)data information:(APPDFInformation *)information;


/**
 * Used to determine if the document has encryption. Note that this is
 * a static property of the PDF file. Also note that it is possible
 * for a PDF document to be encrypted without a password; in this
 * case, this method will return YES and APPDFDocument::isDecrypted
 * will always return YES (and there is no need to explicitly decrypt
 * the document).
 *
 * \return YES if the PDF document is encrypted.
 */
-(BOOL)isEncrypted;


/**
 * Used to determine if an encrypted PDF document has been decrypted
 * (not applicable if the PDF is not encrypted). If the PDF is
 * encrypted, this returns YES if the password is empty, or if the
 * document has been successfully decrypted using
 * APPDFDocument::decryptWithPassword:.
 *
 * \return YES if the PDF document is encrypted and either has an
 * empty password or has been successfully decrypted.
 *
 * \sa APPDFDocument::isEncrypted
 * \sa APPDFDocument::decryptWithPassword:
 */
-(BOOL)isDecrypted;


/**
 * Use this method to decrypt an ecrypted PDF document with the
 * indicated password.
 *
 * \return YES if the password was succesfully used to decrypt the
 * document.
 */
-(BOOL)decryptWithPassword:(NSString *)password;


/**
 * \return The number of pages in the PDF. Note that this method will
 * return 0 if the PDF is corrupt or if it is an encrypted PDF which
 * has not yet been decrypted.
 */
-(NSUInteger)pageCount;


/**
 * \return The Crop Box rectangle for the PDF page (in page space
 * coordinates).
 */
-(CGRect)displayBoxForPage:(NSUInteger)page;


/**
 * Some PDFs encode labels (e.g., "i", "ii", "iii", ... or "A-1", "A-2", "A-3" ...) for pages;
 * use this to get these.
 *
 * \param pageIndex The 0-based page index of the page to retrieve the label for
 *
 * \return If the PDF specifies labels, this will return the appropriate label for the specified page,
 *         otherwise it will return a string representation of the pageIndex in a 1-based scheme.
 */
-(NSString *)pageLabelForPageIndex:(NSUInteger)pageIndex;


/**
 * Some PDFs encode labels (e.g., "i", "ii", "iii", ... or "A-1", "A-2", "A-3" ...) for pages;
 * use this to determine if any page labels are specified in this PDF.
 * Note:  It is possible for a PDF to encode labels for some pages but not others.
 *
 * \return YES if any page labels are specified for any pages in the PDF
 */
-(BOOL)hasPageLabels;


/**
 * Used to update the path of the PDF file after it is renamed
 * or moved.
 *
 * \param newPath The full new path to the PDF file.
 */
-(void)updatePath:(NSString *)newPath;


/**
 * Used to generate thumbnail images for pages in this PDF. Note that
 * thumbnail images do not include annotations. If this PDF is encrypted,
 * it must be first decrypted for this operation to work. This operation 
 * could be time consuming. Please consider running it in the background. 
 *
 * \param pageIndex the 0-based page index of the page to draw an image of
 *
 * \param thumbSize the size of the generated image
 *
 * \return A UIImage object of the PDF page.
 */
-(UIImage *)thumbnailImageForPage:(NSUInteger)pageIndex ofSize:(CGSize)thumbSize;


/**
 * Used to determine if a PDF file cannot be parsed or displayed.
 *
 * \return YES if the document is corrupt.
 */
-(BOOL)corrupt;


/**
 * Used to determine if the PDF file is actually a PDF Portfolio,
 * which cannot be handled by the SDK (and will be reported as
 * corrupt). This may be useful in handling differently when a PDF is
 * corrupt vs. when it's a PDF Portfolio.
 *
 * \return YES if the document is a PDF portfolio.
 */
-(BOOL)isPDFPortfolio;



@end
