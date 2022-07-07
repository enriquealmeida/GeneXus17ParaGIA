
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
#import "APPDFViewController.h"


/**
 * Extension of APPDFViewController that permits creation and editing
 * of annotations.
 *
 * <b>NOTE:</b> This class is not supported on the iPhone or iPod
 * Touch. Use APPDFViewController instead.
 */
@interface APAnnotatingPDFViewController : APPDFViewController
{
}


/**
 * The delegate for callback notifications. Note that any methods
 * from the APAnnotatingPDFViewDelegate protocol that are defined
 * on the delegate will be called as well.
 */
@property(assign) id<APPDFViewDelegate> delegate;


/**
 * Controls whether non-contiguous markup annotations (highlight,
 * underline, strikeout) are allowed. Default is NO, but set this
 * property to YES to disable.
 */
@property(assign) BOOL disableNoncontiguousMarkup;


/**
 * The 'User' to use when creating annotations. If unspecified, a
 * generic 'Unknown' author is used.
 *
 * \sa APMarkupAnnotation::title
 */
@property(retain) NSString *annotationAuthor;


/**
 * Used to enter "annotation mode" for the indicated annotation
 * type. Subsequent touch interaction will be interpreted according to
 * the type of annotation being created.
 *
 * \param type The type of annotation to be created.
 */
-(void)addAnnotationOfType:(APAnnotationType)type;


/**
 * For location-based annotations (such as notes), this will immediately create an annotation of the indicated type at the specified location.
 *
 * \param type The type of annotation to be created.
 *
 * \param location The location at which the annotation should be created.
 */
-(void)addAnnotationOfType:(APAnnotationType)type atViewLocation:(CGPoint)location;


/**
 * Used to obtain the text that is selected in an in-progress markup
 * (highlight, underline, strikeout) annotation.
 *
 * \return The text for the current markup annotation, or
 * <code>nil</code> if no markup annotation is in progress or text is
 * not yet selected.
 */
-(NSString *)selectedAnnotationText;


/**
 * Used to end "annotation mode" for multi-gesture annotations, such as highlights or ink annotations.
 */
-(void)finishCurrentAnnotation;


/**
 * Used to cancel "anotation mode" for any type of annotation. No annotation will be created.
 */
-(void)cancelAddAnnotation;


/**
 * Used to permanently remove all user annotations from the PDF view
 * (and underlying information object).
 */
-(void)removeAllAnnotations;


/**
 * Used to permanently remove all user annotations from the indicated
 * page, in the PDF view and the underlying information object.
 *
 * \param pageIndex The 0-based page index of the page from which all
 * annotations are to be removed.
 */
-(void)removeAnnotationsOnPage:(NSUInteger)pageIndex;


/**
 * Applies only when the PDF View is in Ink creation mode; sets the
 * finger input mode to drawing mode, i.e., touches will be
 * interpreted as adding new strokes to the Ink annotation.
 */
-(void)setInkDrawingMode;


/**
 * Applies only when the PDF View is in Ink creation mode; sets the
 * finger input mode to eraser mode, i.e., touches will be interpreted
 * as erasing existing strokes from the Ink annotation.
 */
-(void)setInkEraserMode;


/**
 * Applies only when the PDF View is in Ink creation mode; used to
 * determine if the view is current in drawing mode.
 *
 * \return <code>YES</code> if the view is in Ink creation mode and
 * drawing mode; <code>NO</code> if the view is not in Ink creation
 * mode, or if it is in eraser mode.
 */
-(BOOL)isInInkDrawingMode;


/**
 * Applies only when the PDF View is in Ink creation mode; used to
 * undo the last Ink operation (whether stroke or eraser).
 */
-(void)undoInkOperation;


/**
 * Applies only when the PDF View is in Ink creation mode; used to
 * determine if there are any Ink operations to undo.
 *
 * \return <code>YES</code> if the view is in Ink creation mode and
 * there are any operations on the undo stack.
 */
-(BOOL)canUndoInkOperation;


/**
 * Applies only when the PDF View is in Ink creation mode; used to
 * redo the last undone Ink operation (whether stroke or eraser).
 */
-(void)redoInkOperation;


/**
 * Applies only when the PDF View is in Ink creation mode; used to
 * determine if there are any Ink operations to redo.
 *
 * \return <code>YES</code> if the view is in Ink creation mode and
 * there are any operations on the redo stack.
 */
-(BOOL)canRedoInkOperation;


/**
 * Returns the currently active annotation.  This will be a newly
 * created annotation or an existing annotation that was tapped
 * by the user (generally indicating by pulsating in and out).
 *
 * \sa APPDFViewDelegate::pdfController:didTapOnAnnotation:inRect:
 * \sa APPDFViewOptions::pulsateActiveAnnotation
 */
-(APAnnotation *)activeAnnotation;

/**
 * Updates the view of the currently active annotation.
 * Used to programmatically change a brand new annotation (or a
 * recently tapped annotation) by calling 
 * APAnnotatingPDFViewController::activeAnnotation then changing
 * some properties, then calling this method.
 * The activeAnnotation passed to this method MSUT be the same
 * object returned by APAnnotatingPDFViewController::activeAnnotation.
 * The changes are not persisted to the database until the user
 * taps done or finishCurrentAnnotation is called.
 * \sa APAnnotatingPDFViewController::activeAnnotation
 */
-(BOOL)updateActiveAnnotation:(APAnnotation *)activeAnnotation;

/**
 * Clears the active annotation (if any).
 */
-(void)clearActiveAnnotation;


/**
 * Begins (or ends) editing mode on the currently active
 * annotation. This is normally only useful if not using the default
 * ribbon UI.
 *
 * \sa APPDFViewDelegate::pdfController:shouldShowRibbonForAnnotation:
 */
-(void)editActiveAnnotation;


/**
 * Begins (or ends) annotation transformation (rotate/resize) mode on
 * the currently active annotation. This is normally only useful if
 * not using the default ribbon UI. Note that the currently active
 * annotation must support transformation (eg, must be of type APInk,
 * APFreeText, or APStamp).
 *
 * \sa APPDFViewDelegate::pdfController:shouldShowRibbonForAnnotation:
 */
-(void)transformActiveAnnotation;





@end
