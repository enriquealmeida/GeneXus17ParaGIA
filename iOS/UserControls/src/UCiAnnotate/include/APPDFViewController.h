
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


@class APPDFViewControllerPrivate;


/**
 * PDF View controller used for viewing, reading, and navigating PDF
 * documents.
 *
 * Note that certain features will be unavailable unless the APPDFDocument
 * provided in the initializer has been processed using the
 * APPDFProcessor API. You may call the APPDFProcessor API while the
 * file is being viewed in the view controller; in this case, once
 * processing is complete, the view controller will detect this and
 * enable all relevant functionality, updating the display if
 * necessary.
 *
 * Also note that the APPDFViewController requires that the proper
 * UIViewController view state notification methods are called,
 * including the appear/disappear notifications (eg, [UIViewController
 * viewWillAppear:]) and rotation notifications (eg, [UIViewController
 * willRotateToInterfaceOrientation:duration:]). This is done
 * automatically by the iOS frameworks and the standard
 * UIViewController subclasses (example: UINavigationController, or
 * when using [UIViewController
 * presentModalViewController:animated:]), but if you are embedding
 * the PDF view in a custom view controller -- which will be the case
 * in most applications that have additional UI controls for
 * interacting with the PDF View -- then you will need to pass along
 * these notifications at the appropriate times. These notifications
 * are required in order to ensure that the PDF view creates and
 * releases resources appropriately.
 *
 * Use the APPDFViewDelegate protocol to receive delegate
 * notifications about user actions being performed within this view
 * controller.
 */
@interface APPDFViewController : UIViewController
{
@private
    APPDFViewControllerPrivate *m_private;
}


/**
 * The delegate for user interaction notifications.
 */
@property(assign) id<APPDFViewDelegate> delegate;


/**
 * Options to control the view's behavior.
 */
@property(retain) APPDFViewOptions *viewOptions;


/**
 * The PDF for this view controller.
 */
@property(readonly) APPDFDocument *pdf;


/**
 * The visible page range; by default, all pages in the PDF will be
 * displayed. If this property is set, then only the pages in the
 * requested range will be displayed.
 */
@property(assign) NSRange visiblePageRange;


/**
 * Designated initializer, creating the view controller based on the
 * provided PDF file.
 *
 * \param pdf The PDF File to present in the view controller.
 *
 * \return The initialized view controller, or <code>nil</code> if the
 * file does not exist or is not a valid PDF file.
 */
-(id)initWithPDF:(APPDFDocument *)pdf;



/**
 * Provides programmatic page navigation.
 *
 * \param pageIndex The 0-based page index to which to navigate. The
 * view will navigate so that the upper-left corner of the page aligns
 * with the upper-left corner of the view.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)navigateToPage:(NSUInteger)pageIndex animated:(BOOL)animated;


/**
 * Programmatically navigate to the indicated annotation.
 *
 * \param annotation The APAnnotation object to which to
 * navigate. Note that this <i>must</i> be an object returned by one
 * of the APPDFInformation accessor methods.
 *
 * \param showPopup <code>YES</code> to display the annotation popup
 * view associated with this annotation; <code>NO</code> to leave the
 * popup hidden.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa APPDFInformation::allUserAnnotations
 * \sa APPDFInformation::userAnnotationsOnPage:
 */
-(void)navigateToAnnotation:(APAnnotation *)annotation showPopup:(BOOL)showPopup animated:(BOOL)animated;

/**
 * Programmatically navigate to the indicated annotation.
 *
 * \param annotation The APAnnotation object to which to
 * navigate. Note that this <i>must</i> be an object returned by one
 * of the APPDFInformation accessor methods.
 *
 * \param showPopup <code>YES</code> to display the annotation popup
 * view associated with this annotation; <code>NO</code> to leave the
 * popup hidden.
 *
 * \param showRibbon <code>YES</code> to display the ribbon used to
 * edit this annotation; <code>NO</code> to leave the ribbon hidden.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa APPDFInformation::allUserAnnotations
 * \sa APPDFInformation::userAnnotationsOnPage:
 */
-(void)navigateToAnnotation:(APAnnotation *)annotation showPopup:(BOOL)showPopup showRibbon:(BOOL)showRibbon animated:(BOOL)animated;


/**
 * Programmatically navigate to the indicated page-space offset on the indicated page.
 *
 * \param offset The page-space offset to which to navigate.
 *
 * \param pageIndex The 0-based page index to which to navigate.
 *
 * \param centered If YES, then the offset will be centered in the view;
 * otherwise, the offset will be set to the top-left corner of the view.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)navigateToOffset:(CGPoint)offset onPage:(NSUInteger)pageIndex centered:(BOOL)centered animated:(BOOL)animated;


/**
 * Programmatically navigate to the indicated bookmark.
 *
 * \param bookmark The APBookmark object to which to navigate. Note
 * that this <i>must</i> be an object returned by one of the
 * APPDFInformation accessor methods.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa APPDFInformation::bookmarks
 */
-(void)navigateToBookmark:(APBookmark *)bookmark animated:(BOOL)animated;


/**
 * Programmatically navigate to the indicated destination.
 *
 * \param destination The APDestination to which to navigate.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)navigateToDestination:(APDestination *)destination animated:(BOOL)animated;


/**
 * Programmatically navigate to the next page.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)nextPageAnimated:(BOOL)animated;


/**
 * Used to determine if next page navigation is available.
 *
 * \return <code>YES</code> if the document is not on the last page.
 */
-(BOOL)hasNextPage;


/**
 * Programmatically navigate to the previous page.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)previousPageAnimated:(BOOL)animated;


/**
 * Used to determine if previous page navigation is available.
 *
 * \return <code>YES</code> if the document is not on the first page.
 */
-(BOOL)hasPreviousPage;


/**
 * Programmatically navigate to the first page.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)firstPageAnimated:(BOOL)animated;


/**
 * Programmatically navigate to the last page.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)lastPageAnimated:(BOOL)animated;


/**
 * Programmatically navigate "up" one screen-full (based on the size
 * of the view).
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)screenUpAnimated:(BOOL)animated;


/**
 * Programmatically navigate "down" one screen-full (based on the size
 * of the view).
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)screenDownAnimated:(BOOL)animated;


/**
 * Programmatically navigate "left" one screen-full (based on the size
 * of the view).
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)screenLeftAnimated:(BOOL)animated;


/**
 * Programmatically navigate "right" one screen-full (based on the
 * size of the view).
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)screenRightAnimated:(BOOL)animated;


/**
 * Used to display a popover controller which allows the user to enter a page number.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param rect The rectangle in the view from which to display the popover controller.
 *
 * \param view The view from which to display the popover controller.
 *
 * \param arrowDirections The permitted arrow directions for the popover controller.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa UIPopoverController
 */
-(void)showGoToPageFromRect:(CGRect)rect inView:(UIView *)view permittedArrowDirections:(UIPopoverArrowDirection)arrowDirections animated:(BOOL)animated;


/**
 * Used to display a popover controller which allows the user to enter a page number, from a UIBarButtonItem.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param item The toolbar button item from which to display the popover controller.
 *
 * \param arrowDirections The permitted arrow directions for the popover controller.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa UIPopoverController
 */
-(void)showGoToPageFromBarButtonItem:(UIBarButtonItem *)item permittedArrowDirections:(UIPopoverArrowDirection)arrowDirections animated:(BOOL)animated;


/**
 * Used to display the Bookmarks controller.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param rect The rectangle in the view from which to display the popover controller.
 *
 * \param view The view from which to display the popover controller.
 *
 * \param arrowDirections The permitted arrow directions for the popover controller.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa UIPopoverController
 */
-(void)showBookmarksFromRect:(CGRect)rect inView:(UIView *)view permittedArrowDirections:(UIPopoverArrowDirection)arrowDirections animated:(BOOL)animated;


/**
 * Used to display the Bookmarks controller, from a UIBarButtonItem.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param item The toolbar button item from which to display the popover controller.
 *
 * \param arrowDirections The permitted arrow directions for the popover controller.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa UIPopoverController
 */
-(void)showBookmarksFromBarButtonItem:(UIBarButtonItem *)item permittedArrowDirections:(UIPopoverArrowDirection)arrowDirections animated:(BOOL)animated;


/**
 * Used to display the PDF Outline controller.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param rect The rectangle in the view from which to display the popover controller.
 *
 * \param view The view from which to display the popover controller.
 *
 * \param arrowDirections The permitted arrow directions for the popover controller.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa UIPopoverController
 */
-(void)showPDFOutlineFromRect:(CGRect)rect inView:(UIView *)view permittedArrowDirections:(UIPopoverArrowDirection)arrowDirections animated:(BOOL)animated;


/**
 * Used to display the PDF Outline controller, from a UIBarButtonItem.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param item The toolbar button item from which to display the popover controller.
 *
 * \param arrowDirections The permitted arrow directions for the popover controller.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa UIPopoverController
 */
-(void)showPDFOutlineFromBarButtonItem:(UIBarButtonItem *)item permittedArrowDirections:(UIPopoverArrowDirection)arrowDirections animated:(BOOL)animated;


/**
 * Used to display the Annotation List controller, which allows navigation to the annotations in the document.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param rect The rectangle in the view from which to display the popover controller.
 *
 * \param view The view from which to display the popover controller.
 *
 * \param arrowDirections The permitted arrow directions for the popover controller.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa UIPopoverController
 */
-(void)showAnnotationListFromRect:(CGRect)rect inView:(UIView *)view permittedArrowDirections:(UIPopoverArrowDirection)arrowDirections animated:(BOOL)animated;


/**
 * Used to display the Annotation List controller, from a UIBarButtonItem.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param item The toolbar button item from which to display the popover controller.
 *
 * \param arrowDirections The permitted arrow directions for the popover controller.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa UIPopoverController
 */
-(void)showAnnotationListFromBarButtonItem:(UIBarButtonItem *)item permittedArrowDirections:(UIPopoverArrowDirection)arrowDirections animated:(BOOL)animated;


/**
 * Used to dismiss an active navigation popover.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 */
-(void)dismissActivePopoverAnimated:(BOOL)animated;


/**
 * Used to display the document search bar.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)showSearchAnimated:(BOOL)animated;


/**
 * Used to display the document search bar, initialized with the
 * indicated term. The first occurrence of the indicated term, if
 * present, will be highlighted.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param term The initial text to search for.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)showSearchForTerm:(NSString *)term animated:(BOOL)animated;


/**
 * Used to hide the document search bar.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)hideSearchAnimated:(BOOL)animated;

/**
 * Hides the rectangle highlighting a search result.
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)hideSearchHighlightAnimated:(BOOL)animated;

/**
 * Used to determine in the document search bar is currently visible.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \return <code>YES</code> if the search bar is currently visible.
 */
-(BOOL)isSearchVisible;

/**
 * Used to navigate to the indicated APSearchResult and highlight it.
 *
 * \param result The APSearchResult obtained from peforming a search
 * request using APPDFInformation::performSearch:
 *
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 *
 * \sa APSearchResult
 * \sa APSearch
 * \sa APPDFInformation::performSearch:
 */
-(void)navigateToSearchResult:(APSearchResult *)result animated:(BOOL)animated;

/**
 * Used to clear any visible text selection
 */
-(void)clearSelection;


/**
 * Used to enter text selection mode. Only available if the PDF has
 * been processed and the permissions allows text selection. When in
 * selection mode, any user touches that drag over text areas will
 * update the current selection.
 */
-(void)enterSelectionMode;


/**
 * Ends selection mode.
 */
-(void)endSelectionMode;


/**
 * Used to determine whether selection mode is currently enabled.
 *
 * \return YES if the view controller is currently in selection mode.
 */
-(BOOL)isSelectionMode;


/**
 * Used to obtain the currently selected text.
 *
 * \return The currently selected text, or <code>nil</code> if no text
 * is selected.
 */
-(NSString *)selectedText;


/**
 * Used to determine the current page.
 *
 * \return The 0-based page index of the current page at the middle of the view.
 */
-(NSUInteger)pageIndexAtMidscreen;




/**
 * Used to determine the current page.
 *
 * \return The 0-based page index of the current page at the
 * upper-left corner of the view.
 */
-(NSUInteger)pageIndexAtContentOffset;


/**
 * Used to determine the current scroll location of the PDF view.
 *
 * \return The coordinates of the location in the upper-left corner of
 * the view, in page space.
 */
-(CGPoint)pageSpaceContentOffset;


/**
 * Used to determine the current scroll location of the PDF view.
 *
 * \return The current view offset (in view coordinates).
 */
-(CGPoint)viewSpaceContentOffset;


/**
 * Used to programatically navigate to a particular view location.
 *
 * \param offset The new offset, in view coordinates.
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)setViewSpaceContentOffset:(CGPoint)offset animated:(BOOL)animated;


/**
 * Used to determine the currently visible frame in view-space
 * coordinates.
 */
-(CGRect)currentViewRect;


/**
 * Used to convert a rectangle in page space coordinates, on the
 * indicated page, to view coordinates. Note that view coordinates are
 * dependent upon the current zoom scale and scroll offset of the PDF
 * view.
 *
 * \param rect The rectangle in page space coordinates.
 * \param pageIndex The 0-based page index of rectangle.
 * \return The corresponding rectangle in view space coordinates.
 */
-(CGRect)viewRectForPageSpaceRect:(CGRect)rect onPage:(NSUInteger)pageIndex;


/**
 * Used to convert a rectangle in view coordinates to page space
 * coordinates on the indicated page. Note that view coordinates are
 * dependent upon the current zoom scale and scroll offset of the PDF
 * view.
 *
 * \param pageIndex The 0-based page index of the page space to
 * convert to.
 * \param rect The rectangle in view space coordinates.
 * \return The corresponding rectangle in page space coordinates of
 * the indicated page.
 */
-(CGRect)pageSpaceRectOnPage:(NSUInteger)pageIndex forViewRect:(CGRect)rect;


/**
 * Used to convert a point in page space coordinates, on the indicated
 * page, to view coordinates. Note that view coordinates are dependent
 * upon the current zoom scale and scroll offset of the PDF view.
 *
 * \param point The point in page space coordinates.
 * \param pageIndex The 0-based page index of point.
 * \return The corresponding point in view space coordinates.
 */
-(CGPoint)viewPointForPageSpacePoint:(CGPoint)point onPage:(NSUInteger)pageIndex;


/**
 * Used to convert a point in view coordinates to page space
 * coordinates on the indicated page. Note that view coordinates are
 * dependent upon the current zoom scale and scroll offset of the PDF
 * view.
 *
 * \param pageIndex The 0-based page index of the page space to
 * convert to.
 * \param point The point in view space coordinates.
 * \return The corresponding point in page space coordinates of the
 * indicated page.
 */
-(CGPoint)pageSpacePointOnPage:(NSUInteger)pageIndex forViewPoint:(CGPoint)point;


/**
 * Used to obtain the current scaling factor, from page space
 * coordinates to view coordinates.
 *
 * \return The current scaling factor.
 */
-(CGFloat)pageToViewScale;


/**
 * Used to programatically set the current scaling factor.
 *
 * \param scale The new scaling factor
 */
-(void)setPageToViewScale:(CGFloat)scale;


/**
 * Scales the PDF content so that the width of the PDF page matches
 * the width of the view size.
 */
-(void)fitToWidth;


/**
 * Scales the PDF content so that the height of the PDF page matches
 * the height of the view size.
 */
-(void)fitToHeight;


/**
 * Force a layout of the window -- should be called after a resize of
 * the view.
 */
-(void)layout;


/**
 * Used to programatically navigate to a specific page and location in
 * page space.
 *
 * \param page The 0-based page to which to navigate
 * \param offset The page-space coordinates to which to navigate (will
 * be the location of the upper-left corner of the view, or as near as
 * possible).
 * \param animated <code>YES</code> to animate the transition,
 * <code>NO</code> to make the transition immediate.
 */
-(void)setPage:(NSUInteger)page contentOffsetInPageSpace:(CGPoint)offset animated:(BOOL)animated;


/**
 * Returns the view that should be used if additional gesture
 * recognizers are required on the PDF view. Gesture recognizers are
 * enabled whenever the PDF view is in "normal" user interaction mode;
 * that is, when the user is scrolling or viewing annotations. When
 * the PDF view is in "annotation creation" mode -- that is, after a
 * call to APAnnotatingPDFViewController::addAnnotationOfType:,
 * between the APAnnotatingPDFViewDelegate::didEnterAnnotationMode:
 * and APAnnotatingPDFViewDelegate::didEnterAnnotationMode: delegate
 * callbacks -- these gestures are disabled, because the PDF view must
 * perform annotation-specific gesture handling.
 *
 * If you wish to handle taps/gestures on the background (outside of
 * the PDF page), you may want to add gesture recognizers to the
 * backgroundGestureView as well.
 *
 * Note that this may return nil if the content view is not loaded.
 *
 * \sa APPDFViewController::backgroundGestureView
 * \sa APAnnotatingPDFViewController::addAnnotationOfType:
 * \sa APAnnotatingPDFViewDelegate::didEnterAnnotationMode:
 * \sa APAnnotatingPDFViewDelegate::didEndAnnotationMode:
 * \sa APPDFViewDelegate::pdfControllerDidLoadContentView:
 *
 * \return a UIView object suitable for adding new gesture recognizers.
 */
-(UIView *)gestureView;


/**
 * Similar to APPDFViewController::gestureView, but allows detection
 * of user interaction in background (outside the PDF page).
 *
 * \sa APPDFViewController::gestureView
 *
 * \return a UIView object suitable for adding new gesture recognizers.
 */
-(UIView *)backgroundGestureView;


/**
 * Returns the UIScrollView used for panning and zooming. This can be
 * used to configure behavior such as whether or not the scroll bars
 * are visible.
 *
 * The APPDFViewController makes heavy use of the scroll view, so
 * please use caution when changing scroll view properties; in
 * particular, the delegate must not be overwritten, and changes that
 * might affect touch interaction must be avoided.
 *
 * do not attempt to modify the view hierarchy or move the scroll view
 * to another view.
 *
 *  \return the base UIScrollView object in the view
 */
-(UIScrollView *)scrollView;


/**
 * Used to determine if the view has loaded a valid PDF file. This
 * could return NO, for example, if an attempt was made to load a
 * corrupt PDF, or a password-protected has been loaded but the
 * password has not yet been supplied by the user.
 *
 * \sa APPDFViewDelegate::pdfControllerDidLoadContentView:
 *
 * \return YES if a PDF document is loaded and displayed
 */
-(BOOL)hasValidDocument;


/**
 * UIViewController override. Be sure to call this appropriately when
 * adding the PDF view to your application's view heirarchy.
 */
-(void)viewWillAppear:(BOOL)animated;


/**
 * UIViewController override. Be sure to call this appropriately when
 * adding the PDF view to your application's view heirarchy.
 */
-(void)viewDidAppear:(BOOL)animated;


/**
 * UIViewController override. Be sure to call this appropriately when
 * removing the PDF view to your application's view heirarchy.
 */
-(void)viewWillDisappear:(BOOL)animated;


/**
 * UIViewController override. Be sure to call this appropriately when
 * removing the PDF view to your application's view heirarchy.
 */
-(void)viewDidDisappear:(BOOL)animated;


/**
 * UIViewController override. Be sure to call this appropriately when
 * handling device auto-rotation. If your application does not support
 * rotation, then it is always safe to leave the view unrotated; but
 * rotating and resizing the view may have adverse effects in
 * annotation mode, so this method should be called to verify that the
 * PDF view is in an appropriate state before allowing device
 * auto-rotation.
 *
 * NOTE:  This method is deprecated.  Only use in pre-iOS6 implementations.
 * For iOS6 and beyond, use shouldAutorotate and supportedInterfaceOrientations instead.
 */
-(BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)orientation;


/**
 * UIViewController override. Be sure to call this appropriately when
 * handling device auto-rotation. If your application does not support
 * rotation, then it is always safe to leave the view unrotated; but
 * rotating and resizing the view may have adverse effects in
 * annotation mode, so this method should be called to verify that the
 * PDF view is in an appropriate state before allowing device
 * auto-rotation.
 */
-(BOOL)shouldAutorotate;


/**
 * UIViewController override. Be sure to call this appropriately when
 * handling device auto-rotation. If your application does not support
 * rotation, then it is always safe to leave the view unrotated; but
 * rotating and resizing the view may have adverse effects in
 * annotation mode, so this method should be called to verify that the
 * PDF view is in an appropriate state before allowing device
 * auto-rotation.
 */
-(NSUInteger)supportedInterfaceOrientations;


/**
 * UIViewController override. Be sure to call this appropriately when
 * the device is rotating.
 */
-(void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration;


/**
 * UIViewController override. Be sure to call this appropriately when
 * the device is rotating.
 */
-(void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation;


/**
 * UIViewController override. Declared just to emphasize that it's
 * available; since PDF rendering and cached bitmaps can take up
 * significant amounts of memory, it is recommended that this is
 * called whenever the host application receives a memory warning.
 */
-(void)didReceiveMemoryWarning;


/**
 * Used to select the word at the specified view location; detects the
 * nearest (vector) text at the specified location and, if any exists,
 * highlights the appropriate word.
 *
 * \param viewLocation The point, in the coordinates of the PDF view,
 * at which to select text.
 *
 * \return <code>YES</code> if any text has been selected.
 */
-(BOOL)selectWordAtViewLocation:(CGPoint)viewLocation;


/**
 * Used to programmatically hide the ribbon, if it's currently visible
 * and being used to show information about a selected
 * annotation. Note that if the PDF view is currently in annotation
 * mode (creating a new annotation), this will have no effect; in this
 * case use finishCurrentAnnotation.
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \sa APAnnotatingPDFViewController::finishCurrentAnnotation
 */
-(void)hideRibbon;

/**
 * Show the ribbon used to edit the given annotation.
 * If the annotation is not visible you should use navigateToAnnotation showRibbon:YES
 *
 * <b>NOTE:</b> This method is not supported on the iPhone or iPod Touch.
 *
 * \sa APAnnotatingPDFViewController::navigateToAnnotation
 */
-(void)showRibbonForAnnotation:(APAnnotation *)annotation;

/**
 * Hides all visible popups assocaited with all annotations.
 * (Such as the bubble containing the text for a 'Note' annotation)
 */
-(void)hidePopups:(BOOL)animated;

/**
 * Hides the popup associated with the given annotation, if it is visible.
 * (Such as the bubble containing the text for a 'Note' annotation)
 */
-(void)hidePopupForAnnotation:(APAnnotation *)annotation animated:(BOOL)animated;


/**
 * Used to force all current annotation views to update; call this
 * after any programmatic changes to existing annotations to have them
 * reflected in the PDF view.
 *
 * \sa reloadAnnotationViews
 */
-(void)updateAnnotationViews;


/**
 * Used to force a programmatic refresh of all annotation views. Use
 * this method after programmatically adding or removing annotations
 * from the APPDFInformation associated with this view.
 *
 * \sa updateAnnotationViews
 */
-(void)reloadAnnotationViews;
    

@end
