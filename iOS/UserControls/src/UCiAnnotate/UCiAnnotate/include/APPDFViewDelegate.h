
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
 * Delegate notifications for the APPDFViewController. All methods are
 * optional.
 */
@protocol APPDFViewDelegate


@optional


/**
 * Notification that the PDF controller has loaded an encrypted
 * document, and will prompt the user for the password required to
 * view it.
 */
-(void)pdfControllerWillAskUserForDocumentPassword:(APPDFViewController *)controller;


/**
 * Notification that the PDF controller has unlocked the document. The
 * password is provided in case the application wishes to cache it for
 * future use -- note that the password is internally cached in the
 * APPDFDocument object for the lifetime of the object.
 */
-(void)pdfController:(APPDFViewController *)controller didUnlockDocumentWithPassword:(NSString *)password;


/**
 * Notification that the main content view has been loaded; generally,
 * this will be immediately after the view itself loads, unless the
 * document is locked and the password entry view is being displayed.
 */
-(void)pdfControllerDidLoadContentView:(APPDFViewController *)controller;


/**
 * External URL Link notification.
 */
-(void)pdfController:(APPDFViewController *)controller didTapOnLinkToExternalURL:(NSURL *)url;


/**
 * Notification that the user tapped a link annotation that points to an internal location in the pdf.
 * Return true to allow the view to navigate to the destination.
 */
-(BOOL)pdfController:(APPDFViewController *)controller shouldNavigateToInternalDestination:(APInternalDestination *)dest;


/**
 * Notification that the PDF view will show the search bar.
 */
-(void)pdfController:(APPDFViewController *)controller willShowSearchAnimated:(BOOL)animated;


/**
 * Notification that the PDF view will hide the search bar.
 */
-(void)pdfController:(APPDFViewController *)controller willHideSearchAnimated:(BOOL)animated;


/**
 * Use this to control whether the search UI and yellow box used to highlight 
 * a search result get hidden after the box has just been tapped.
 * If this is not implemented, the default behavior is to hide these when 
 * a search result box has been tapped.
 *
 * \return YES to hide the box and UI, or NO to keep it on the screen.
 */
-(BOOL)shouldHideSearchAfterResultTapInPdfController:(APPDFViewController *)controller;


/**
 * Notification that the PDF view has entered selection mode.
 */
-(void)pdfControllerDidEnterSelectionMode:(APPDFViewController *)controller;


/**
 * Notification that the PDF view has ended selection mode.
 */
-(void)pdfControllerDidEndSelectionMode:(APPDFViewController *)controller;


/**
 * Notification that the page of the PDF view has changed (e.g., due
 * to user scrolling or a navigation request).  This is called at least
 * every time the page changes, but may also be called more than that
 * under certain circumstances.
 */
-(void)pdfControllerDidChangePage:(APPDFViewController *)controller;


/**
 * Notification that the PDF view layout may have changed, generally
 * due to view resizing or configuration of the view options.
 */
-(void)pdfControllerDidChangeLayout:(APPDFViewController *)controller;


/**
 * Notification that the PDF view was scrolled.
 */
-(void)pdfController:(APPDFViewController *)controller didScrollToContentOffset:(CGPoint)contentOffset;


/**
 * Notification for when user zooms
 */
-(void)pdfController:(APPDFViewController *)controller didZoomToScale:(CGFloat)scale;


/**
 * Notification that an annotation view was tapped by the user.
 */
-(void)pdfController:(APPDFViewController *)controller didTapOnAnnotation:(APAnnotation *)annotation inRect:(CGRect)rect;


/**
 * Notification that the selected text was tapped by the user.
 */
-(void)pdfController:(APPDFViewController *)controller didTapOnSelectionInRect:(CGRect)rect;


/**
 * Notification that the user has updated the selection by dragging in
 * selection mode or adjusting the handlebars in selection mode.
 */
-(void)pdfController:(APPDFViewController *)controller didUpdateSelectionInRect:(CGRect)rect;


/**
 * Notification that a long tap was detected at the indicated location in the view.
 */
-(void)pdfController:(APPDFViewController *)controller didLongPressAtViewLocation:(CGPoint)location;


/**
 * Use this to control whether the popup annotation text interface is
 * displayed for the indicated annotation. If this method is not
 * implemented, the default return is YES.
 *
 * \return YES to display the interface, or NO to keep it from being
 * displayed.
 */
-(BOOL)pdfController:(APPDFViewController *)controller shouldShowPopupForAnnotation:(APAnnotation *)annotation;


/**
 * Notification that a popup view was shown for a particular
 * annotation.
 */
-(void)pdfController:(APPDFViewController *)controller didShowPopupForAnnotation:(APAnnotation *)annotation;


/**
 * Notification that a popup view was hidden for a particular
 * annotation.
 */
-(void)pdfController:(APPDFViewController *)controller didHidePopupForAnnotation:(APAnnotation *)annotation;


/**
 * Use this to control whether the annotation ribbon editing interface
 * is displayed for the indicated annotation. If this method is not
 * implemented, the default return is YES.
 *
 * \return YES to display the interface, or NO to keep it from being
 * displayed.
 */
-(BOOL)pdfController:(APPDFViewController *)controller shouldShowRibbonForAnnotation:(APAnnotation *)annotation;


/**
 * Notification that the annotation ribbon editing interface was
 * shown.
 */
-(void)pdfControllerDidShowRibbon:(APPDFViewController *)controller;


/**
 * Notification that the annotation ribbon editing interface was
 * hidden.
 */
-(void)pdfControllerDidHideRibbon:(APPDFViewController *)controller;


/**
 * Return YES if the PDF controller should not display a
 * UIMenuController for the current selection.
 */
-(BOOL)pdfControllerShouldSuppressSelectionMenu:(APPDFViewController *)controller;


/**
 * Used to obtain the "master" view in which this PDF view is
 * embedded, which should be a top-level full-screen application
 * view. This is used, for example, when displaying an image
 * annotation in full-screen mode.
 *
 * If this method is not implemented or 'nil' is returned, then the
 * UIWindow's rootViewController property is used to obtain the master
 * view. So if your application has set the rootViewController, there
 * is no need to implement this method.
 */
-(UIView *)masterViewForPDFController:(APPDFViewController *)controller;


@end
