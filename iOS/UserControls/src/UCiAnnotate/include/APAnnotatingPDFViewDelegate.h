
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
#import "APPDFViewDelegate.h"




/**
 * Delegate notifications for APAnnotatingPDFViewController -- all
 * delegate methods are optional.
 */
@protocol APAnnotatingPDFViewDelegate <APPDFViewDelegate>


@optional


/**
 * Notification for when annotation mode begins.
 */
-(void)pdfController:(APAnnotatingPDFViewController *)controller didEnterAnnotationMode:(APAnnotationType)type;


/**
 * Used to provide a default color for the annotation about to be created.
 *
 * \return The color to use, or <code>nil</code> to use a default color.
 */
-(UIColor *)pdfController:(APAnnotatingPDFViewController *)controller colorForNewAnnotationOfType:(APAnnotationType)annotType;


/**
 * Used to provide a starting thickness for the ink annotation about
 * to be created.
 *
 * \return The thickness to use, which should be a value betwen 0.0
 * and 1.0 -- the default is 0.0 for a thin (one point) line, 1.0
 * yields a 24-point line.
 */
-(CGFloat)pdfController:(APAnnotatingPDFViewController *)controller thicknessForNewAnnotationOfType:(APAnnotationType)annotType;


/**
 * Used to provide a default font for the free-text annotation about to be created.
 *
 * \return The font to use, which must not be <code>nil</code>.
 */
-(UIFont *)pdfController:(APAnnotatingPDFViewController *)controller fontForNewAnnotationOfType:(APAnnotationType)annotType;

/**
 * Used to provide a default text string for the free-text annotation about to be created.
 *
 * \return The text to use.
 */
-(NSString *)pdfController:(APAnnotatingPDFViewController *)controller textForNewAnnotationOfType:(APAnnotationType)annotType;


/**
 * Used to provide a custom stamp image to use for the annotation.
 *
 * \return The absolute path to the image to use.
 */
-(NSString *)pdfController:(APAnnotatingPDFViewController *)controller customStampPathForAnnotationType:(APAnnotationType)annotType;



/**
 * Notification for when annotation mode is aborted without creating an annotation.
 */
-(void)pdfController:(APAnnotatingPDFViewController *)controller didEndAnnotationMode:(APAnnotationType)type;


/**
 * Notification for when part of a Markup (highlight, underline,
 * strikeout) or Ink annotation was updated by the user -- for
 * example, a portion of text highlighted, a highlight handlebar
 * dragged, or a stroke of the Ink added. The rect parameter indicates
 * the bounding view rectangle of the annotation.
 */
-(void)pdfController:(APAnnotatingPDFViewController *)controller didUpdateNewAnnotationOfType:(APAnnotationType)annotType inRect:(CGRect)rect;


/**
 * Notification for when a new annotation is placed -- use this to
 * configure properties of the annotation before it's displayed and
 * further edited by the user.
 */
-(void)pdfController:(APAnnotatingPDFViewController *)controller willPlaceNewAnnotation:(APAnnotation *)annotation;


/**
 * Notification for when an annotation is cplaced on screen.
 */
-(void)pdfController:(APAnnotatingPDFViewController *)controller didPlaceAnnotation:(APAnnotation *)annotation;


/**
 * Notification for when an annotation is created.
 */
-(void)pdfController:(APAnnotatingPDFViewController *)controller didCreateAnnotation:(APAnnotation *)annotation;


/**
 * Notification for when an annotation is modified.
 */
-(void)pdfController:(APAnnotatingPDFViewController *)controller didModifyAnnotation:(APAnnotation *)annotation;


/**
 * Used to control whether a confirmation dialog is displayed when a
 * user attempts to delete an anntoation. If this method is not
 * implemented, the default is YES.
 *
 * \return YES is a confirmation dialog should be displayed, NO to immediately delete the annotation.
 */
-(BOOL)pdfController:(APAnnotatingPDFViewController *)controller shouldDisplayConfirmationBeforeDeletingAnnotation:(APAnnotation *)annotation;


/**
 * Notification for when an annotation is deleted.
 */
-(void)pdfController:(APAnnotatingPDFViewController *)controller didDeleteAnnotation:(APAnnotation *)annotation;


@end
