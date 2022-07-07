
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
#import "APAnnotations.h"
#import "APAnnotatingPDFViewController.h"
#import "APAnnotatingPDFViewDelegate.h"
#import "APLibrary.h"
#import "APPDFDocument.h"
#import "APPDFInformation.h"
#import "APPDFProcessor.h"
#import "APPDFProcessorDelegate.h"
#import "APPDFViewController.h"
#import "APPDFViewDelegate.h"
#import "APPDFViewOptions.h"
#import "APPDFDocumentBuilder.h"
#import "APPDFDocumentBuilderDelegate.h"


/**
 * \mainpage iAnnotate PDF SDK
 * \author Branchfire, LLC
 *
 * The iAnnotate PDF SDK, based on the iAnnotate™ iPad
 * Application, is an iOS static library used to display PDF files
 * and work with their annotations.
 *
 * To get started, we recommend glancing through the APPDFDocument,
 * APPDFInformation, APPDFProcessor, and APPDFViewController class
 * descriptions and APIs.
 *
 * The basic strategy for integrating an annotating PDF view
 * controller into your application is:
 *
 * 1) Initialize the iAnnotate PDF SDK by creating a new APLibrary
 * object
 *
 * 2) Obtain a PDF file
 *
 * 3) Create an APPDFDocument object (optionally specifying a location
 * where you'd like the library to store the "PDF Information" file.)
 *
 * 4) If you wish to enable searching, or importing existing
 * annotations, you'll need to process the APPDFDocument. For example:
 *
 * <pre>
 *    NSString *myPdfPath = ...;
 *    APPDFDocument *pdf = [[APPDFDocument alloc] initWithPath:myPdfPath];
 *    if (!pdf.information.isProcessed) {
 *        APPDFProcessor *processor = [[APPDFProcessor alloc] init];
 *        processor.delegate = self;
 *        [processor processPDF:pdf];
 *        [processor release];
 *    }
 *    else {
 *        [self useProcessedPDF:pdf];
 *    }
 * </pre>
 *
 * 5) Create an APAnnotatingPDFViewController and add it to your view
 * heirarchy
 *
 * 6) Hook up action buttons to annotation creation methods, depending
 * on your application's interface, and implement
 * APAnnotatingPDFViewDelegate callbacks to handle actions in the PDF
 * view controller
 *
 * Of course, there are many details beyond this basic process, but
 * this should help get things started. Also see the <a
 * href="../../samples">Sample Projects</a> for examples on how to use
 * various additional aspects of the iAnnotate PDF SDK APIs.
 *
 *
 * <b>NOTE</b>: when linking with this library, the following must be
 * included in "Other Linker Flags" under project settings:
 * "-lz -lsqlite3 -lstdc++ -ObjC -all_load"
 *
 * \sa <a href="projectconfig.html">Project Configuration</a> for more
 * details.
 *
 */

/**
 * \page ProjectConfig Project Configuration
 *
 * The iAnnotate PDF SDK currently supports iOS 4.2 and higher --
 * although usage of iOS 6.x is recommended as we, in keeping with iOS
 * standard practices, will likely choose to support only the most
 * recent major version of iOS in future releases.
 *
 * When setting up a project for use with the iAnnotate PDF SDK,
 * the following steps must be taken:
 *
 * 1) In the "Other Linker Flags" section of the build settings (for
 * all releases), the following must be included: "-lz -lsqlite3
 * -lstdc++ -ObjC -all_load"
 *
 * 2) All of the resources from the "resources" folder of the library
 * distribution must be added to the project. (These include image
 * files, string resources, and font data files.) When adding the
 * contents of the resources folder, make sure the "Create Folder
 * References for Any Added Folders" option is selected, so that the
 * "FontCMaps" folder is added to your project as a folder, and not a
 * group in the project tree (i.e., should show as a blue icon).
 *
 * 3) Add the header files (from the "include" directory) to the
 * project.
 *
 * 4) Add the static library (lib/libAjiPDFLib.a) to the project.
 *
 * 5) Add your license file, which must be called "license.xml", to
 * the project. Generally this should be included in the "resources"
 * folder for your application.
 *
 * 6) The following frameworks must be added to your project in order
 * to support the iAnnotate PDF SDK: Foundation.framework,
 * UIKit.framework, CoreGraphics.framework, MessageUI.framework,
 * AVFoundation.framework, Security.framework
 *
 * 7) import "AjiPDFLib.h" in any file that requires use of the API.
 *
 * Please note that the iAnnotate PDF SDK requires iOS 4.2, and has
 * built-in support for both the simulator and device.
 *
 * See the <a href="../../samples">Sample Projects</a> for examples on
 * how to properly perform these steps in an XCode project.
 *
 */

/**
 * \page Security iAnnotate PDF SDK Security
 *
 * The iAnnotate PDF SDK has been implemented using best security
 * practices, and provides support for security-conscious applications
 * including:
 *
 * <ul>
 *  <li>Full support of iOS Data Protection (see <a href="http://support.apple.com/kb/HT4175">http://support.apple.com/kb/HT4175</a>).
 *  This includes using the NSFileProtectionComplete attribute on all
 *  persistent and temporary files created by the SDK. Please note
 *  that incoming PDF files are *not* modified: so if your application
 *  wishes to take advantage of iOS Data Protection, it must set the
 *  appropriate file attributes for PDF files and any other files used
 *  outside of the scope of the iAnnotate PDF SDK.</li>
 * <li>The iAnnotate PDF SDK includes full support for encrypted
 *  PDFs. Encrypted PDFs cannot be accessed without the appropriate
 *  password, and all permissions requested by the PDF author are 
 *  respected by the iAnnotate PDF SDK. (So, for example, it is not
 *  possible to add annotations to an encrypted PDF file that is
 *  protected against adding annotations, using this SDK.) When
 *  annotations are added to an encrypted PDF, they are also encrypted
 *  with the same level of security as the original.</li>
 * <li>PDF passwords are never persisted by the SDK. (If passwords
 * should be saved for the user, the client application should use the
 *  iOS Keychain to accomplish this.)</li>
 * <li>For high-security environments, you can provide the PDF data in
 * an NSData object (see APPDFDocument::initWithPDFData:), in which
 * case the SDK will never write any of the PDF information to disk,
 * whether or not annotations are added to the PDF.</li>
 * <li>The iAnnotate PDF SDK does not initiate any kind of network
 *  connection, so will never transmit any data off of the
 *  device. Likewise, it will never use iOS inter-app communication
 *  functionality to launch another app or otherwise transmit data to
 *  any other app or service on the device. If requested (through user
 *  interaction), it may place textual data on the pasteboard, and
 *  receive data from the pasteboard using standard text-based UI
 *  controls (for example, when selecting text or editing a note).</li>
 * </ul>
 *
 * Some notes about potential points of exposure for
 * security-conscious environments:
 * <ul>
 *  <li>If relying on the iOS Data Protection APIs to secure user data,
 * please be sure to research and familiarize yourself with the
 * requirements and limitiations of this technique, in particular what
 * is required of the end user's configuration in order to support
 * encryption.</li>
 * <li>The limitations of the iOS Data Protection APIs means that the
 * SDK is forced to have some potential exposure points:
 * <ul>
 *  <li>The APPDFInformation files used by the SDK are sqlite (http://www.sqlite.org)
 *  database files. When working with the files, temporary journal
 *  files can be used by the SDK. Due to the ephemeral nature of
 *  these files, it is not possible for the SDK to apply the
 *  protected file attributes when they are created. Therefore, it is
 *  potentially possible (though highly unlikely) that an application
 *  crash (force-quit) could be triggered by an attacker during a
 *  processing operation, which could potentially leave an unencrypted
 *  journal file on disk, which could potentially have PDF textual
 *  content or annotation content stored within it.</li>
 *  <li>Though no other specific instances are known at this time,
 *  it's possible that other system libraries (or iOS itself) that are
 *  used by the iAnnotate PDF SDK could make similar use of temp files.
 *  However, all temporary files explicitly managed by the SDK are
 *  properly protected (or not used when using the in-memory APIs).</li>
 * </ul>
 * <li>In order to perform the required functionaltiy, the PDF SDK
 * does keep textual PDF contents, the contents of PDF annotations,
 * and PDF passwords unencrypted in device memory (RAM) at certain
 * times.</li>
 * <li>Due to the nature of iOS, it is not possible to prevent the
 * user from taking a screen shot of a running application with PDF
 * contents displayed in an APPDFViewController.</li>
 * </ul>
 */

/**
 * \page ReleaseNotes Release Notes
 *
 * <h2>Version 2.5.2</h2>
 * General Updates:
 * <ul>
 *  <li>You can now set sound annotation length up to 180 seconds, using the
 *   maxSoundAnnotationLength API in APPDFViewOptions.</li>
 *  <li>We have deprecated the reloadPDF:persistLayout: API.</li>
 *  <li>Now you can dismiss the yellow search result rectangles programmatically
 *   with hideSearchHighlightAnimated:.</li>
 *  <li>Some improvements to CJK font handling.</li>
 * </ul>
 * <h2>Version 2.5.1</h2>
 * General Updates
 * <ul>
 *  <li>Fixed an issue that could prevent the keyboard from appearing in iPhone
 *   implementations.</li>
 *  <li>Two-finger scroll to pan while placing an ink annotation</li>
 *  <li>enableSwipeGestures view option now defaults to YES.</li>
 *  <li>General bugfixes and performance enhancements.</li>
 * <ul>
 * <h2>Version 2.5</h2>
 * General Updates:
 * <ul>
 *  <li>Exposed document "assembly" operations (insert/delete/move/rotate pages) 
 *   via new APPDFDocumentBuilder class. Use this class to make these changes to 
 *   your PDF, then write out a new copy reflecting those changes. We also have 
 *   a new API that lets you swap your view controller's current PDF with another, 
 *   so you can immediately show the updated document. See APPDFDocumentBuilder.h 
 *   and APPDFDocumentBuilderDelegate.h for more documentation.</li>
 *  <li>It is now possible to update the state of an annotation while in annotation 
 *   mode, so you can change color in response to user interactsion (if you're not 
 *   using our built-in ribbon interface).</li>
 *  <li>You can now choose to center PDFs in the y-axis when in single-page mode, 
 *   though there will be a small jump on page change.</li>
 *  <li>The sample application has been updated to better demonstrate annotating on 
 *   the iPhone.</li>
 * </ul>
 * New/Updated APIs:
 * <ul>
 *  <li>APPDFDocumentBuilder</li>
 *  <li>APPDFDocumentBuilderDelegate</li>
 *  <li>APPDFViewController::reloadWithPDF:preserveLayout:</li>
 *  <li>APAnnotatingPDFViewController::updateActiveAnnotation:</li>
 *  <li>APPDFViewOptions::verticalCenterSinglePageMode:</li>
 * </ul>
 * <h2>Version 2.4</h2>
 * General Updates:
 * <ul>
 *  <li>Combined APPDFInformation::hasText and APPDFViewController::hasText 
 *   into a single method (APPDFInformation::hasText).  Check the new 
 *   APPDFInformation::isSearchable if you need to determine whether document 
 *   search is currently possible.
 *   (Similarly for hasTextOnPage.)</li>
 *  <li> Renamed the APPDFWriteAnnotationOptions enum to be APPDFWriteOptions 
 *   and added the kAPPDFWriteOptionStripDeadObjects value.</li>
 * </ul>
 * New/Updated APIs:
 * <ul>
 *  <li>APPDFDocument::hasPageLabels</li>
 *  <li>APPDFDocument::pageLabelForPageIndex:</li>
 *  <li>APPDFInformation::isSearchable</li>
 *  <li>APPDFInformation::hasText</li>
 *  <li>APPDFViewController::hasText</li>
 *  <li>APPDFInformation::hasTextOnPage:</li>
 *  <li>APPDFViewController::hasTextOnPage:</li>
 * </ul>
 *
 * <h2>Version 2.3.1</h2>
 *  New/Updated APIs:
 * <ul>
 *  <li>[APAnnotatingPDFViewDelegate
 *  pdfController:willPlaceNewAnnotation:], useful for
 *  auto-configuring to-be-placed annotations.</li>
 *  <li>APPDFDocument::initWithPDFData:information:</li>
 *  <li>APPDFInformation:exportAnnotationXMLToStream:includeBookmarks:
 *  and APPDFInformation::importAnnotationXMLFromStream: now support
 *  sound, image, and file attachment annotations.</li>
 *  <li>APPDFViewController::updateAnnotationViews and
 *  APPDFViewController::reloadAnnotationViews have been moved from
 *  APAnnotatingPDFViewController to APPDFViewController</li>
 *  <li>APPDFDocument::corrupt</li>
 *  <li>APPDFDocument::isPDFPortfolio</li>
 * </ul>
 * </ul>
 *
 * <h2>Version 2.3</h2>
 * <b>Important!</b> API behavior changes:
 * <ul>
 *  <li>There have been modifications to the behavior of the
 *  [APAnnotatingPDFViewDelegate didEnterAnnotationMode] and
 *  [APAnnotatingPDFViewDelegate didCreateAnnotation] callbacks to
 *  provide more consistent behavior across all annotation types. Now,
 *  for annotation types link Note and Typewriter, the
 *  "didCreateAnnotation" delegate callback will only be called back
 *  after the initial text editing is complete -- previously, it would
 *  be called immediately after placement of the annotation but before
 *  any text had been added. If you are relying on these callbacks to
 *  trigger interface updates, please test and verify that things
 *  still work as expected with this new behavior.</li>
 *  <li>When returning NO from [APPDFViewDelegate
 *  shouldShowRibbonForAnnotation:], the annotation will now still be
 *  made active and, by default, pulsate. Previously, the behavior was
 *  to disable pulsation. If you wish to disable pulsation, use the
 *  APPDFViewOptions::pulsateActiveAnnotation property.</li>
 * </ul>
 * General Updates:
 * <ul>
 *  <li>Updated for iOS 6 API changes related to device rotation.  Now support 
 *      -(BOOL)shouldAutorotate  and  -(NSUInteger)supportedInterfaceOrientations, 
 *      while -(BOOL)shouldAutorotateToInterfaceOrientation: has been deprecated.</li>
 *  <li>Now support PDFs encrypted using AES-256 (R5), as specified in 
 *    Adobe's Level 3 extensions to the PDF standard</li>
 *  <li>Some PDFs encode labels (e.g., "i", "ii", "iii", ... or "A-1", 
 *    "A-2", "A-3" ...) for pages.  If these are available for a PDF, 
 *    they can now be used instead of numeric page indices in the page 
 *    locator when shown via the new usePageLabelsIfAvailable view 
 *    option.</li>
 *  <li>Added the ability to convert between page-space and view-space
 *  coordinates, for working with annotations programmatically.</li>
 *  <li>Added the option to include Bookmarks in exported XML as if 
 *    they were annotations.</li>
 *  <li>Exposed APAStamp annotations type and added delegate to get custom 
 *    images for stamp annotations.</li>
 *  <li>New links property on APPDFInformation.</li>
 *  <li>Various bugfixes and minor improvements.</li>
 * </ul>
 * New/Updated APIs:
 * <ul>
 *  <li>APPDFInformation::exportAnnotationXMLToStream:includeBookmarks:</li>
 *  <li>APPDFInformation::hasLinks</li>
 *  <li>APPDFInformation::links</li>
 *  <li>APPDFViewController::currentViewRect</li>
 *  <li>APPDFViewController::viewRectForPageSpaceRect:onPage:</li>
 *  <li>APPDFViewController::pageSpaceRectOnPage:forViewRect:</li>
 *  <li>APPDFViewController::viewPointForPageSpacePoint:onPage:</li>
 *  <li>APPDFViewController::pageSpacePointOnPage:forViewPoint:</li>
 *  <li>APPDFViewController::backgroundGestureView</li>
 *  <li>APPDFViewController::editActiveAnnotation</li>
 *  <li>APPDFViewController::transformActiveAnnotation</li>
 *  <li>APPDFViewController::shouldAutorotate</li>
 *  <li>APPDFViewController::supportedInterfaceOrientations</li>
 *  <li>APPDFViewDelegate::didScrollToContentOffset:</li>
 *  <li>APPDFViewDelegate::shouldHideSearchAfterResultTapInPdfController:</li>
 *  <li>APPDFViewOptions::usePageLabelsIfAvailable</li>
 *  <li>APPDFViewOptions::pulsateActiveAnnotation</li>
 *  <li>APPDFViewOptions::popupTextAlignment</li>
 *  <li>APPDFAnnotatingPDFViewDelegate::pdfController:customStampPathForAnnotationType:</li>
 * </ul>
 * 
 * <h2>Version 2.2.1</h2>
 * <ul>
 *  <li>Added view option to enable swipe gestures (in addition to
 *    slide) for single-page mode. Off by default, so that if you've
 *    implemented these previously using the gestureView, they won't
 *    conflict.</li>
 *  <li>Exposed the underlying UIScrollView to allow configuraiton of
 *    some of the view options.</li>
 *  <li>Various minor bugfixes.</li>
 * </ul>
 * New/Updated APIs:
 * <ul>
 *  <li>APPDFViewOptions::enableSwipeGestures</li>
 *  <li>APPDFViewController::scrollView</li>
 * </ul>
 *
 * <h2>Version 2.2</h2>
 * <ul>
 *  <li>Improved rendering performance and behavior.</li>
 *  <li>Writing file-to-NSMutableData and NSMutableData-to-file is now fully supported for all options</li>
 *  <li>New processor option kAPPDFProcessingOptionsAnnotationsOnly</li>
 *  <li>Library will now compile in universal applications</li>
 *  <li>Miscellaneous minor updates, bugfixes, and error reporting improvements.</li>
 * </ul>
 * New/Updated APIs:
 * <ul>
 *  <li>APInk::inkPenWidthForThickness:</li>
 *  <li>APInk::thicknessForInkPenWidth:</li>
 *  <li>APPDFViewDelegate::pdfController:shouldNavigateToInternalDestination:</li>
 *  <li>APPDFViewController::hidePopups</li>
 *  <li>APPDFViewController::hidePopupForAnnotation:</li>
 *  <li>APText::setPopupVisible:</li>
 * </ul>
 *
 * <h2>Version 2.1.1</h2>
 * <ul>
 *  <li>Retina display support added: on supported devices (iPad 3),
 *  the PDF view will render at retina-display
 *  resolution. High-resolution graphics assets have been added --
 *  please note that all of the "@2x" PNG graphics in the resources
 *  area must be added to your project file to support retina display
 *  devices.</li>
 *  <li>Minor bugfixes and error reporting improvements.</li>
 * </ul>
 * New/Updated APIs:
 * <ul>
 *  <li>APPDFViewDelegate::pdfControllerDidShowRibbon:</li>
 *  <li>APPDFViewDelegate::pdfControllerDidHideRibbon:</li>
 *  <li>APPDFViewOptions::disableRetinaDisplayRendering</li>
 * </ul>
 *
 * <h2>Version 2.1</h2>
 * <ul>
 *  <li>The annotation interface now uses the "ribbon" editing UI
 *  introduced in iAnnotate 2.0, rather than the popup editing
 *  UI. Note that this may require implementing the new
 *  APPDFViewDelegate::pdfController:shouldShowRibbonForAnnotation:
 *  method if your application uses a custom UI and you do <i>not</i>
 *  want the ribbon interface to be used. Note also that popups are
 *  still present, but are only used for the text contents of
 *  annotations</li>
 *  <li>APPDFViewController now includes the ability to "live process"
 *  PDF documents. This means that markup annotations (highlight,
 *  underline, strikeout) and text selection can be done without first
 *  having to process the document (cf. APPDFProcessor). Also, this
 *  enables character-level selection. Note that processing is still
 *  required to support searching of PDF documents and loading of any
 *  pre-existing annotations.
 *   <ul>
 *    <li>In particular, see the new method
 *    APPDFViewController::hasText and the discussion there -- there
 *    are caes where this will be more appropriate to use than
 *    APPDFInformation::hasText</li>
 *   </ul>
 *  </li>
 *  <li>Highlighters now use "blend mode" to place the highlighting
 *  color "behind" the text, instead of overlaying highlight on top of
 *  the PDF using alpha transparency. And an optional magnifier is now
 *  available for text selection and markup creation.</li>
 *  <li>The ability to export annotation data to XML and import
 *  annotation XML data have been added: see
 *  APPDFInformation::exportAnnotationXMLToStream: and
 *  APPDFInformation::importAnnotationXMLFromStream:</li>
 *  <li>In security related updates, the SDK now supports both
 *  flattening of NSData-backed PDF and flattening to NSData; persmissions
 *  of a PDF are now available before processing the PDF (note that you
 *  may still need to decrypt the document before you could get the
 *  accurate persmissions); and there is now a new view option for 
 *  disabling autocorrection of text inputs for annotations thus 
 *  preventing such inputs from being saved in the iOS keyboard cache.</li>
 * </ul>
 * New/Updated APIs:
 * <ul>
 *  <li>New view options:
 *    <ul>
 *      <li>APPDFViewOptions::liveProcessingDisabled</li>
 *      <li>APPDFViewOptions::charBasedTextSelection</li>
 *      <li>APPDFViewOptions::charBasedTextHandlebars</li>
 *      <li>APPDFViewOptions::showLoupeOnMarkupDrag</li>
 *      <li>APPDFViewOptions::autofadePageLocator</li>
 *      <li>APPDFViewOptions::readOnlyAnnotations</li>
 *      <li>APPDFViewOptions::autocorrectionDisabled</li>
 *    </ul>
 *  </li>
 *  <li>APPDFViewDelegate::pdfController:didLongPressAtViewLocation:
 *  -- if your 1.x application used a UILongPressGestureRecognizer in
 *  the gestureView, use this instead for better slow scrolling
 *  behavior.</li>
 *  <li>APPDFViewDelegate::pdfController:didUpdateSelectionInRect:</li>
 *  <li>APPDFViewController::navigateToSearchResult:animated: for
 *  programmatically navigating to results from programmatic
 *  search.</li>
 *  <li>APPDFViewController::hasText</li>
 *  <li>APPDFViewController::hasTextOnPage:</li>
 *  <li>APPDFInformation::exportAnnotationXMLToStream:</li>
 *  <li>APPDFInformation::importAnnotationXMLFromStream:</li>
 *  <li>Programmatic control of Ink annotation mode:
 *    <ul>
 *      <li>APAnnotatingPDFViewController::setInkDrawingMode</li>
 *      <li>APAnnotatingPDFViewController::setInkEraserMode</li>
 *      <li>APAnnotatingPDFViewController::isInInkDrawingMode</li>
 *      <li>APAnnotatingPDFViewController::undoInkOperation</li>
 *      <li>APAnnotatingPDFViewController::canUndoInkOperation</li>
 *      <li>APAnnotatingPDFViewController::redoInkOperation</li>
 *      <li>APAnnotatingPDFViewController::canRedoInkOperation</li>
 *    </ul>
 *  </li>
 *  <li>Programmatic control of FreeText creation:
 *    <ul>
 *      <li>APAnnotatingPDFViewDelegate::pdfController:fontForNewAnnotationOfType:</li>
 *      <li>APAnnotatingPDFViewDelegate::pdfController:textForNewAnnotationOfType:</li>
 *    </ul>
 *  </li>
 *  <li>APPDFViewController::selectWordAtViewLocation: for selecting the word 
 *  at the specified view location.</li>
 *  <li>APPDFDocument::thumbnailImageForPage:ofSize: for generating
 *  thumbnail images of PDF pages.</li>
 *  <li>APPDFDocument::permissions has been moved from APPDFInformation to
 *  APPDFDocument and is available before processing PDF.</li>
 *  <li>APColor::updateWithUIColor: for updating color values.</li>
 *  <li>APPDFProcessorDelegate::pdfProcessorRequiresNameForBookmarksRoot:
 *  and APPDFProcessorDelegate::pdfProcessor:isBookmarksRootName: have
 *  been added to allow programmatic control of the outline element
 *  name used when reading/writing user bookmarks from/to PDF files
 *  (default is 'Branchfire Bookmarks')</li>
 * </ul>
 *
 *
 * <h2>Version 1.5.3</h2>
 *
 * New/Updated APIs:
 * <ul>
 *  <li>APPDFInformation::numberOfUserAnnotations</li>
 *  <li>APPDFInformation::identifierForAnnotation:</li>
 *  <li>APPDFInformation::annotationForIdentifier:</li>
 *  <li>Added explicit method signatures to APPDFViewController to
 *      emphasize the UIViewController overrides that must be
 *      forwarded and respected (eg,
 *      APPDFViewController::viewWillAppear:).</li>
 *  <li>Added APPDFViewDelegate::masterViewForPDFController: delegate
 *      method, used for the fullscreen preview of photo
 *      annotations.</li>
 *  <li>Flatten operations now function properly on NSData-backed PDF</li>
 *  <li>Added APSound::makeWavData for accessing usable sound data for
 *      sound annotations (replaces old APSound::soundData, which was in 
 *      a PDF-specific encoding).</li>
 * </ul>
 *
 * 
 * <h2>Version 1.5</h2>
 *
 * New/Updated APIs:
 * <ul>
 *  <li>NSData-based API allows viewing and annotation of in-memory PDF:</li>
 *  <ul>
 *   <li>APPDFDocument::initWithPDFData:</li>
 *   <li>APPDFDocument::data</li>
 *  </ul>
 *  <li>Programmatic search:
 *   <ul>
 *    <li>APPDFInformation::performSearch:</li>
 *    <li>APSearch</li>
 *    <li>APSearchRequest</li>
 *    <li>APSearchResult</li>
 *    <li>APPDFSearchDelegate</li>
 *   </ul>
 *  </li>
 *  <li>Ability to flatten annotations into the PDF using APPDFProcessor::writePDFWithAnnotations:toPath:options:</li>
 *  <li>New annotation types: APFreeText (typewriter), APFileAttachment (including photo), APSound</li>
 *  <li>Mirroring view (for VGA presentation): see APPDFViewController::mirroringController</li>
 *  <li>APAnnotatingPDFViewController::reloadAnnotationViews</li>
 *  <li>APAnnotatingPDFViewController::removeAllAnnotations</li>
 *  <li>APAnnotatingPDFViewController::removeAnnotationsOnPage:</li>
 *  <li>Added a <a href="Security.html">Security</a> page with detailed information about application security practices and assumptions.</li>
 * </ul>
 *
 *
 * <h2>Version 1.0.3</h2>
 *
 * New/Updated APIs:
 * <ul>
 *  <li>APPDFViewController::dismissActivePopoverAnimated:</li>
 *  <li>For encrypted PDF that do not allow selection or writeback of annotations, the 
 *  library will no longer display a message to this effect -- it is left to the application 
 *  client to check the permissions and decide whether or not (and how) to warn/notify 
 *  the user.</li>
 * </ul>
 *
 * <h2>Version 1.0.2</h2>
 *
 * New/Updated APIs:
 * <ul>
 *  <li>APPDFViewOptions no longer supports the showBookmarks parameter</li>
 *  <li>APPDFViewOptions::disableSelectionMenuController</li>
 *  <li>Changed name of APAnnotatingPDFViewDelegate::pdfController:didAbortAnnotationMode:
 *  to APAnnotatingPDFViewDelegate::pdfController:didEndAnnotationMode:, 
 *  and this method will always be called regardless of how annotation 
 *  mode was ended.</li>
 * </ul>
 *
 * <hr>
 * <h2>Version 1.0.1</h2>
 *
 * New/Updated APIs:
 * <ul>
 *  <li>APPDFViewOptions::minZoomScale</li>
 *  <li>APPDFViewOptions::maxZoomScale</li>
 * </ul>
 *
 * <hr>
 * <h2>Version 1.0</h2>
 *
 * <ul>
 *  <li>A release build is now distributed (libAjiPDFLib.a), instead
 *  of the debug build used in previous pre-release versions
 *  (libAjiPDFLibD.a), so project files may need to be updated
 *  appropriately.</li>
 *  <li>The APPDFViewController::selectedText method now only returns
 *  a value if the PDF view is in selection mode and the user has
 *  selected some text -- it will no longer return a value if a markup
 *  annotation is in progress. Instead, use the
 *  APAnnotatingPDFViewController::selectedAnnotationText to retrieve
 *  the currently selected text for an in-progress markup
 *  annotation.</li>
 *  <li>Programmatic updates to annotation objects can propogated to
 *  the interface using the following procedure:
 *   <ol>
 *    <li>Update the APAnnotation object appropriately</li>
 *    <li>Use APPDFInformation::updateUserAnnotation: to persist the
 *    changes to the APPDFInformation file</li>
 *    <li>Call APAnnotatingPDFViewController::updateAnnotationViews to
 *    force the annotations to update in the view</li>
 *   </ol></li>
 *  <li>The new APPDFViewOptions::singlePageMode property can be used
 *  to allow the display of only one page of the PDF at a time.</li>
 *  <li>New delegate notifications in APPDFViewDelegate allow you to
 *  control whether or not the popup editor for annotations is
 *  displayed, and also provide information for custom interface
 *  handling when an annotation is tapped.</li>
 *  <li>The APLibrary initializer has changed to accomodate license
 *  keys for release libraries.</li>
 * </ul>
 *
 * New/Updated APIs:
 * <ul>
 *  <li>APAnnotatingPDFViewController::disableNoncontiguousMarkup</li>
 *  <li>APAnnotatingPDFViewController::selectedAnnotationText</li>
 *  <li>APAnnotatingPDFViewController::updateAnnotationViews</li>
 *  <li>APLibrary::initWithLicenseKey:dataFolder:</li>
 *  <li>APPDFViewDelegate::pdfController:shouldShowPopupForAnnotation:</li>
 *  <li>APPDFViewDelegate::pdfController:didTapOnAnnotation:inRect:</li>
 *  <li>APPDFViewOptions::singlePageMode</li>
 *  <li>APPDFViewOptions::slideToChangePages</li>
 * </ul>
 *
 * <hr>
 *
 * <h2>Version 0.9.3</h2>
 *
 * New/Updated APIs:
 * <ul>
 *  <li>APAnnotatingPDFViewController::annotationAuthor</li>
 *  <li>APAnnotatingPDFViewDelegate::pdfController:didUpdateNewAnnotationOfType:inRect:</li>
 *  <li>APPDFInformation::versionOfInformationAtPath:</li>
 *  <li>APPDFInformation::userAnnotationsOnPage:</li>
 *  <li>APPDFInformation::addUserAnnotation:</li>
 *  <li>APPDFInformation::updateUserAnnotation:</li>
 *  <li>APPDFInformation::removeAnnotation:</li>
 *  <li>APPDFInformation::removeAllAnnotations:</li>
 *  <li>APPDFInformation::hasUserAnnotations</li>
 *  <li>APPDFProcessor::syncAnnotationsToPDF:</li>
 *  <li>APPDFViewController::visiblePageRange</li>
 *  <li>APPDFViewController::isSelectionMode</li>
 *  <li>APPDFViewController::pageIndexAtMidscreen</li>
 *  <li>APPDFViewController::pageIndexAtContentOffset</li>
 *  <li>APPDFViewController::pageSpaceContentOffset</li>
 *  <li>APPDFViewController::layout</li>
 *  <li>APPDFViewController::gestureView</li>
 *  <li>APPDFViewController::hasValidDocument</li>
 *  <li>APPDFViewDelegate::pdfControllerDidLoadContentView:</li>
 *  <li>APPDFViewDelegate::pdfController:willShowSearchAnimated:</li>
 *  <li>APPDFViewDelegate::pdfController:willHideSearchAnimated:</li>
 *  <li>APPDFViewDelegate::pdfControllerDidEnterSelectionMode:</li>
 *  <li>APPDFViewDelegate::pdfControllerDidEnterSelectionMode:</li>
 *  <li>APPDFViewDelegate::pdfControllerDidChangePage:</li>
 *  <li>APPDFViewDelegate::pdfControllerDidChangeLayout:</li>
 *  <li>APPDFViewDelegate::pdfController:didShowPopupForAnnotation:</li>
 *  <li>APPDFViewDelegate::pdfController:didHidePopupForAnnotation:</li>
 * </ul>
 *
 *
 */
