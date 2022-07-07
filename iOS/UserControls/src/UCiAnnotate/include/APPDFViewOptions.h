
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


@class APPDFViewOptionsPrivate;


/**
 * View options used in the APPDFViewController class.
 *
 * Note that this conforms to the NSCoding protocol, for easy
 * persistence in, for example, user preferences.
 */
@interface APPDFViewOptions : NSObject <NSCoding>
{
@private
    APPDFViewOptionsPrivate *m_private;
}


/**
 * Controls whether or not Link areas should be displayed in the view
 * (indicated by translucent blue rectangles).
 */
@property(assign) BOOL showLinks;


/**
 * If "showLinks" is configured, allows overriding of the color used
 * to indicate link areas (default is translucent blue). Note that the
 * color must include any desired transparency (otherwise it will
 * cover completely the linked-from area).
 */
@property(copy) UIColor *linkOverlayColor;


/**
 * Controls whether or not the page locator view is displayed.
 */
@property(assign) BOOL showPageLocator;

/**
 * Some PDFs encode labels (e.g., "i", "ii", "iii", ... or "A-1", "A-2", "A-3" ...) for pages.
 * If these are available for a PDF and this property is YES, they will be used 
 * instead of numeric page indices in the page locator when shown.
 */
@property(assign) BOOL usePageLabelsIfAvailable;

/**
 * Controls whether or not the page number view is displayed.
 */
@property(assign) BOOL showPageNumber;

/**
 * Controls whether or not horizontal scrolling is allowed.
 */
@property(assign) BOOL horizontalScrollLocked;

/**
 * If horizontal scrolling is locked, this option controls whether the
 * lock is "soft"/"elastic" (allowing the user to "pull" and see
 * outside of the locked area) or not.
 */
@property(assign) BOOL softScrollLock;

/**
 * Controls whether or not annotations are displayed.
 */
@property(assign) BOOL showAnnotations;

/**
 * Controls whether or not annotation "popup" editors are
 * automatically displayed for the annotation nearest the center of
 * the PDF view.
 */
@property(assign) BOOL autoPopup;

/**
 * Set this parameter to "YES" to have the PDF View display only one
 * page at a time, instead of a continuous vertical scrolling view. In
 * order to change pages, navigation must be explicitly requested.
 *
 * \sa APPDFViewController::nextPageAnimated:, APPDFViewController::previousPageAnimated:
 */
@property(assign) BOOL singlePageMode;

/**
 * When using singlePageMode set this parameter to "YES" to have the 
 * PDF View vertically center pages that do not fill the screen.
 * (This is most prominent when viewing landscape pages while the
 * device is in a portrait orientation)
 * \sa APPDFViewController::singlePageMode:
 */
@property(assign) BOOL verticalCenterSinglePageMode;

/**
 * Only relevant in single-page mode, when "YES" and the user drags a
 * page over to the left or right, the next or previous page (as
 * appropriate) is displayed. The default is "YES".
 *
 * \sa APPDFViewOptions::singlePageMode
 */
@property(assign) BOOL slideToChangePages;

/**
 * Only relevant in single-page mode, when "YES" and the user swipes
 * left or right on a page that is zoomed out far enough that it won't
 * pan, the next or previous page (as appropriate) is displayed. The
 * default is "YES".
 */
@property(assign) BOOL enableSwipeGestures;

/**
 * Default is NO; set to YES in order to disable the menu controller
 * from displaying when the user taps on the selection or a portion of
 * a markup annotation.
 */
@property(assign) BOOL disableSelectionMenuController;

/**
 * Use to set the minimum allowed value for the page-to-view scale.
 * The default is 0.0, which uses the internally-derived minimum (very
 * small but not tiny).
 *
 * To keep the user from being able to zoom out beyond the screen
 * size, you can set the initial zoom using
 * APPDFViewController::fitToWidth, then set this value to
 * APPDFViewController::pageToViewScale.
 */
@property(assign) CGFloat minZoomScale;

/**
 * Use to set the maximum allowed value for the page-to-view scale.
 * The default is 0.0, which uses the internally-derived maximum (very
 * large but limited to avoid memory issues).
 *
 * The Quartz PDF rendering engine seems to require too much memory
 * and cause memory warnings if the zoom level is very high, so please
 * test thoroughly if adjusting this value upward.
 */
@property(assign) CGFloat maxZoomScale;

/**
 * Default is <code>NO</code>; set to <code>YES</code> in order to
 * disable the "sharing" options menu on image/sound/file annotations,
 * which by default present the user with options to attach the
 * corresponding file to an email, and/or open the file in another
 * application.
 */
@property(assign) BOOL sharingDisabled;

/**
 * Default is <code>NO</code>; set to <code>YES</code> in order to
 * disable the live processing used to enable character-level markup
 * annotations. If set to <code>YES</code>, processing will be
 * required in order for markup annotation tools and text selection to
 * work, and they will only work on a word-based level. Turning this
 * option on may incur a minor speed/memory benefit in some
 * situations, but in general it is recommended to leave this to the
 * default value of <code>NO</code>.
 *
 * \sa wordBasedTextSelection
 * \sa wordBasedTextHandlebars
 */
@property(assign) BOOL liveProcessingDisabled;

/**
 * Default is <code>NO</code>; set to <code>YES</code> in order to
 * force the of use word-based (instead of character-based) selection
 * when the user is first dragging to select text or create markup
 * annotations.
 *
 * \sa liveProcessingDisabled
 * \sa forceWordBasedTextHandlebars
 */
@property(assign) BOOL forceWordBasedTextSelection;

/**
 * Default is <code>NO</code>; set to <code>YES</code> in order force
 * the use of word-based (instead of character-based) selection when
 * the user is dragging the handlebars to adjust text selection or
 * markup annotations.
 *
 * \sa liveProcessingDisabled
 * \sa forceWordBasedTextSelection
 */
@property(assign) BOOL forceWordBasedTextHandlebars;

/** 
 * Default is NO; set to YES in order to show the magnification
 * loupe when the user is first dragging to create markup annotations.
 */
@property(assign) BOOL showLoupeOnMarkupDrag;

/** 
 * Default is NO; set to YES in order to auto-fade the page locator
 * view after a few moments of being visible. It will be re-displayed
 * whenever the user navigates in the PDF view.
 */
@property(assign) BOOL autofadePageLocator;

/**
 * Default is NO; set to YES in order to disable autocorrection
 * when the user inputs into text fields. Disabling autocorrection
 * also prevents the user's inputs from being persisted in iOS's 
 * keyboard cache, which may be desirable in high security contexts.
 */
@property(assign) BOOL autocorrectionDisabled;

/**
 * Default is <code>NO</code>; set to <code>YES</code> in order to make
 * all annotation objects read-only in the interface, so that the user is
 * able to view, but unable to edit/delete any annotations.
 *
 * Note that this does not prevent the creation of new annotations; it
 * is up to the client application to do this by not invoking the
 * APAnnotatingPDFViewController::addAnnotationOfType: method. Alling
 * the creation of new annotations in read-only mode is not
 * recommended, as there may be unexpected behavior in trying to edit
 * the properties of new annotations.
 */
@property(assign) BOOL readOnlyAnnotations;

/**
 * Default is NO; set to YES to force rendering at low-res resolution
 * even if a retina display is detected. This can reduce CPU and
 * memory usage, at the cost of quality of rendered content.
 */
@property(assign) BOOL disableRetinaDisplayRendering;

/**
 * Default is NO; set to YES to suppress the hint text in the ribbon
 * which is displayed when creating new annotations.
 */
@property(assign) BOOL disableRibbonHint;

/**
 * Default is NO; set to YES to disable the selection of the nearest
 * word when long-tapping on the PDF view near text.
 */
@property(assign) BOOL disableLongTapSelection;

/**
 * Default is YES; set to NO to prevent the actively selected
 * annotation from pulsating.
 */
@property(assign) BOOL pulsateActiveAnnotation;

/**
 * Default is centered, use to override text alignemnt in annotation
 * popup views.
 */
@property(assign) UITextAlignment popupTextAlignment;

/**
 * Default is NO; set to YES to prevent text or information from being copied out of the PDF view.
 */
@property(assign) BOOL lockdownMode;

/**
 * Maximum length of a sound annotation, in seconds.
 */
@property(assign) CGFloat maxSoundAnnotationLength;


@end
