
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



typedef enum
{
    /**
     * Document can only be read/viewed; no further permissions are granted.
     */
    kAPPDFDocumentPermissionsReadOnly              = 0,
    
    /**
     * Document allows text selection (copy/paste).
     */
    kAPPDFDocumentPermissionsAllowsTextSelection   = 1 << 0,
    
    /**
     * Document allows annotation and inserting bookmarks.
     */
    kAPPDFDocumentPermissionsAllowsAnnotation      = 1 << 1,

    /**
     * Document allows "assembly" (including inserting bookmarks).
     */
    kAPPDFDocumentPermissionsAllowsAssembly        = 1 << 2,

    /**
     * Document allows printing (both high and low quality)
     */
    kAPPDFDocumentPermissionsAllowsPrinting        = 1 << 3,
    
    /**
     * Grants all permissions on the Document.
     */
    kAPPDFDocumentPermissionsAll                   = (kAPPDFDocumentPermissionsAllowsTextSelection | kAPPDFDocumentPermissionsAllowsAnnotation | kAPPDFDocumentPermissionsAllowsAssembly | kAPPDFDocumentPermissionsAllowsPrinting),

} APPDFDocumentPermissions;


/**
 * The password type required for processing or writing annotations to a PDF Document.
 */
typedef enum {

    /**
     * The User Password allows restricted access to the PDF. Actual permissions granted depends on the document.
     */
    kAPPDFPasswordTypeAny      = 1,

    /**
     * The Owner Password allows unrestricted access to the PDF.
     */
    kAPPDFPasswordTypeOwner    = 2,

} APPDFPasswordType;


/**
 * Options controlling PDF processing behavior.
 */
typedef enum {

    /**
     * Use default options when processing the PDF.
     */
    kAPPDFProcessingOptionsDefault                       = 0,

    
    /**
     * Process the annotations in the pdf, but not the text contents.
     * This is quicker, but it is not possible to search for text until
     * the pdf has been fully processed with kAPPDFProcessingOptionsDefault.
     */
    kAPPDFProcessingOptionsAnnotationsOnly               = 1 << 1,
    
} APPDFProcessingOptions;


/**
 * Options controlling PDF generation behavior.
 */
typedef enum {

    /**
     * The default write options.  Makes a copy of the PDF,
     * including all annotations.
     */
    kAPPDFWriteOptionsCopy                               = 0,

    /**
     * Same as kAPPDFWriteOptionsCopy. Only retained for backwards 
     * compatibility, use kAPPDFWriteOptionsCopy.
     */
    kAPPDFWriteOptionsCopyOriginal                       = 0,

    /**
     * "Flattens" all annotations into the page content. Extra pages
     * may be generated and added to the document in order to
     * accomodate annotation text. Note that this will embed the
     * annotations directly into the document, so that they will not
     * be editable or removable.
     */
    kAPPDFWriteOptionsFlatten                            = 1 << 0,

    /**
     * Specifies that only annotated pages should be written.
     */
    kAPPDFWriteOptionsAnnotatedPagesOnly                 = 1 << 1,

    /**
     * Specifies that all (user-editable) annotations should be
     * stripped, leaving only the "original" PDF content.
     */
    kAPPDFWriteOptionsStripUserAnnotations               = 1 << 2,

    /**
     * Indicates the the "pageRange" parameter should be used to
     * control which pages of the PDF should be generated.
     */
    kAPPDFWriteOptionsUsePageRange                       = 1 << 3,

    /**
     * Creates a new version of a "cluttered" PDF with any unused ("residual") objects removed.
     * The resulting file should appear exactly the same in all PDF viewers.
     * If an error occurs, there may still be a partially-generated file for the client
     * to clean up.
     *
     * This operation may be time consuming, especially for large PDFs on older iPads.
     *
     * If this option is specified, only kAPPDFWriteOptionsStripUserAnnotations may also be specified.
     */
    kAPPDFWriteOptionsStripDeadObjects                   = 1 << 4,

} APPDFWriteOptions;


/**
 * Supported annotation types.
 */
typedef enum {

    /**
     * Invalid or unspecified annotation type.
     */
    kAPAnnotationTypeNone          = 0,

    /**
     * Note annotation (see APText).
     */
    kAPAnnotationTypeNote          = 1,

    /**
     * Highlight annotation (see APTextMarkup).
     */
    kAPAnnotationTypeHighlight     = 2,

    /**
     * Underline annotation (see APTextMarkup).
     */
    kAPAnnotationTypeUnderline     = 3,

    /**
     * Strikeout annotation (see APTextMarkup).
     */
    kAPAnnotationTypeStrikeout     = 4,

    /**
     * Bookmark annotation (see APBookmark).
     */
    kAPAnnotationTypeBookmark      = 5,

    /**
     * Ink annotation (see APInk).
     */
    kAPAnnotationTypeInk           = 6,

    /**
     * Straight-line ink annotation (see APInk).
     */
    kAPAnnotationTypeStraightLine  = 7,

    /**
     * Stamp annotation (see APStamp).
     */
    kAPAnnotationTypeStamp         = 8,

   /**
    * Free-text (typewriter) annotation (see APFreeText).
    */
   kAPAnnotationTypeFreeText      = 9,

   /**
    * Photo annotation (see APFileAttachment).
    */
   kAPAnnotationTypePhoto         = 11,

   /**
    * File Attachment annotation (see APFileAttachment).
    */
   kAPAnnotationTypeAttachment    = 12,

   /**
    * Sound annotation (see APSound).
    */
   kAPAnnotationTypeSound         = 13,



} APAnnotationType;


/**
 * Unknown APPDFInformation version
 */
#define kAPPDFInformationVersionUnknown    0x00000000



/**
 * APPDFInformation Version 5
 */
#define kAPPDFInformationVersion5          0x00050000

/**
 * APPDFInformation Version 6
 */
#define kAPPDFInformationVersion6          0x00060000

/**
 * APPDFInformation Version 7
 */
#define kAPPDFInformationVersion7          0x00070000

/**
 * APPDFInformation Version 8
 */
#define kAPPDFInformationVersion8          0x00080000

/**
 * The current version of APPDFInformation files.
 */
#define kAPPDFInformationCurrentVersion    kAPPDFInformationVersion8


/**
 * APPDFProcessor error codes.
 *
 * Please note that you may want to implement the [APPDFProcessor
 * pdfProcessor:reportProcessingLog:forPDF:] delegate method for more
 * details if you are getting one of these error codes. (Please
 * include this information if you need to contact support about an
 * error.)
 */
typedef enum {

    /**
     * kAPProcessorErrorDomain Error Code 1.
     *
     * Invalid processing options were provided to the APPDFProcessor.
     */
    kAPProcessorErrorInvalidProcessingOptions             = 1,

    /**
     * kAPProcessorErrorDomain Error Code 2.
     *
     * Invalid write options were provided to the APPDFProcessor.
     */
    kAPProcessorErrorInvalidWriteOptions                  = 2,

    /**
     * kAPProcessorErrorDomain Error Code 3.
     *
     * Some or all elements of the PDF outline could not be loaded
     * from the PDF.
     */
    kAPProcessorErrorSomeOutlineElementsFailed            = 3,

    /**
     * kAPProcessorErrorDomain Error Code 4.
     *
     * Some or all of the Aji Bookmarks could not be loaded from or
     * written to the PDF outline.
     */
    kAPProcessorErrorSomeBookmarksFailed                  = 4,

    /**
     * kAPProcessorErrorDomain Error Code 5.
     *
     * Some or all of the PDF annotations could not be loaded from the
     * PDF file.
     */
    kAPProcessorErrorSomeAnnotationsFailed                = 5,

    /**
     * kAPProcessorErrorDomain Error Code 6.
     *
     * The PDF file appears to be invalid/corrupt.
     */
    kAPProcessorErrorInvalidPDF                           = 6,

    /**
     * kAPProcessorErrorDomain Error Code 7.
     *
     * There was an error parsing the PDF when attempting to extract
     * information about the text.
     */
    kAPProcessorErrorProcessingPDFText                    = 7,

    /**
     * kAPProcessorErrorDomain Error Code 8.
     *
     * There was an error writing the annotations back to the PDF
     * document.
     */
    kAPProcessorErrorWritingAnnotations                   = 8,

    /**
     * kAPProcessorErrorDomain Error Code 9.
     *
     * There was an error updating existing annotations in the PDF
     * file.
     */
    kAPProcessorErrorUpdatingWrittenAnnotations           = 9,

    /**
     * kAPProcessorErrorDomain Error Code 10.
     *
     * The PDF has already been processed.
     */
    kAPProcessorErrorAlreadyProcessed                     = 10,

    /**
     * kAPProcessorErrorDomain Error Code 11.
     *
     * An internal logic error occurred.
     */
    kAPProcessorErrorInternal                             = 11,

    /**
     * kAPProcessorErrorDomain Error Code 12.
     *
     * The PDF password provided does not give sufficient permissions
     * for the requested operation.
     */
    kAPProcessorErrorPermissions                          = 12,

    /**
     * kAPProcessorErrorDomain Error Code 13.
     *
     * An incorrect PDF password was provided.
     */
    kAPProcessorErrorInvalidDocumentPassword              = 13,

    /**
     * kAPProcessorErrorDomain Error Code 14.
     *
     * The operation failed due to a cancellation request.
     */
    kAPProcessorErrorCancelled                            = 14,

} APProcessorError;

#define kAPProcessorErrorDomain         @"AjiPDFLib_APProcessor"


/**
 * APPDFDocumentBuilder error codes.
 */
typedef enum {

    /**
     * kAPPDFDocumentBuilderErrorDomain Error Code 1.
     *
     * A miscellaneous or unknown failure occurred.
     */
    kAPPDFDocumentBuilderErrorFailure                    = 1,

    /**
     * kAPPDFDocumentBuilderErrorDomain Error Code 2.
     *
     * Built-in PDF permissions do not allow assembly operations.
     */
    kAPPDFDocumentBuilderErrorPermissions                 = 2,

    /**
     * kAPPDFDocumentBuilderErrorDomain Error Code 3.
     *
     * Problem with a specified filesystem path.
     */
    kAPPDFDocumentBuilderErrorPathIssue                   = 3,

    /**
     * kAPPDFDocumentBuilderErrorDomain Error Code 13.
     *
     * The PDF was password-protected and a valid password was not supplied.
     */
    kAPPDFDocumentBuilderErrorPasswordFailure             = 13,

    /**
     * kAPPDFDocumentBuilderErrorDomain Error Code 14.
     *
     * The operation failed due to a cancellation request.
     */
    kAPPDFDocumentBuilderErrorCancelled                   = 14,


} APPDFDocumentBuilderError;


#define kAPPDFDocumentBuilderErrorDomain   @"AjiPDFLib_APPDFDocumentBuilder"
