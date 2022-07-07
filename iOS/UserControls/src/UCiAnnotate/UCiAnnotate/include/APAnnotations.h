
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



#import <UIKit/UIKit.h>


#ifndef AJI_APANNOTATIONFLAGS_DEFINED
#define AJI_APANNOTATIONFLAGS_DEFINED

typedef enum
{
    kAPAnnotationFlag_None            = 0x000,
    kAPAnnotationFlag_Invisible       = 0x001,
    kAPAnnotationFlag_Hidden          = 0x002, 
    kAPAnnotationFlag_Print           = 0x004, 
    kAPAnnotationFlag_NoZoom          = 0x008,
    kAPAnnotationFlag_NoRotate        = 0x010,
    kAPAnnotationFlag_NoView          = 0x020,
    kAPAnnotationFlag_ReadOnly        = 0x040,
    kAPAnnotationFlag_Locked          = 0x080,
    kAPAnnotationFlag_ToggleNoView    = 0x100,
    kAPAnnotationFlag_LockedContents  = 0x200
} APAnnotationFlags_e;
#endif

  
typedef enum {

kAPColorType_RGB,
   
kAPColorType_CMYK,
   
kAPColorType_BW,
   
} APColorType;
   
typedef enum {

kAPDestinationFitType_XYZ,
   
kAPDestinationFitType_Fit,
   
kAPDestinationFitType_FitH,
   
kAPDestinationFitType_FitV,
   
kAPDestinationFitType_FitB,
   
kAPDestinationFitType_FitBH,
   
kAPDestinationFitType_FitBV,
   
kAPDestinationFitType_FitR,
   
} APDestinationFitType;
   
typedef enum {

kAPLineEndingStyle_None,
   
kAPLineEndingStyle_Square,
   
kAPLineEndingStyle_Circle,
   
kAPLineEndingStyle_Diamond,
   
kAPLineEndingStyle_OpenArrow,
   
kAPLineEndingStyle_ClosedArrow,
   
kAPLineEndingStyle_Butt,
   
kAPLineEndingStyle_ROpenArrow,
   
kAPLineEndingStyle_RClosedArrow,
   
kAPLineEndingStyle_Slash,
   
} APLineEndingStyle;
   
typedef enum {

kAPTextMarkupType_Highlight,
   
kAPTextMarkupType_Underline,
   
kAPTextMarkupType_StrikeOut,
   
} APTextMarkupType;
   
typedef enum {

kAPSoundEncoding_Raw,
   
kAPSoundEncoding_Signed,
   
kAPSoundEncoding_MuLaw,
   
kAPSoundEncoding_ALaw,
   
} APSoundEncoding;
   
typedef enum {

kAPSoundIcon_Speaker,
   
kAPSoundIcon_Mic,
   
} APSoundIcon;
   
typedef enum {

kAPFreeTextJustification_Left,
   
kAPFreeTextJustification_Right,
   
kAPFreeTextJustification_Center,
   
} APFreeTextJustification;
   
@class APRGBColor;
    
@class APCMYKColor;
    
@class APBWColor;
    
@class APColor;
    
@class APPDFObject;
    
@class APAnnotation;
    
@class APBookmark;
    
@class APInternalDestination;
    
@class APDestination;
    
@class APLink;
    
@class APOutlineElement;
    
@class APMarkupAnnotation;
    
@class APText;
    
@class APLocation;
    
@class APPath;
    
    
@class APTextArea;
    
@class APTextMarkup;
    
@class APInk;
    
    
    
    
@class APFileAttachment;
    
@class APSoundDescription;
    
@class APSound;
    
@class APTransformableMarkupAnnotation;
    
@class APStamp;
    
@class APFreeText;
    
@class APSearchResult;
    
@class APSearchRequest;
    
@class APRGBColorPrivate;

/** Represents a display color in RGBA components. */
@interface APRGBColor : NSObject
{
@private
APRGBColorPrivate *_APRGBColorPrivate;
}

/** A value from 0.0 - 1.0 representing red component. */
@property(readonly) CGFloat red;

/** A value from 0.0 - 1.0 representing green component. */
@property(readonly) CGFloat green;

/** A value from 0.0 - 1.0 representing blue component. */
@property(readonly) CGFloat blue;

/** A value from 0.0 - 1.0 representing the transparency. */
@property(readonly) CGFloat alpha;

@end
     
@class APCMYKColorPrivate;

/** Represents a display color in CMYK components. */
@interface APCMYKColor : NSObject
{
@private
APCMYKColorPrivate *_APCMYKColorPrivate;
}

/** A value from 0.0 - 1.0 representing the cyan component. */
@property(readonly) CGFloat cyan;

/** A value from 0.0 - 1.0 representing the magenta component. */
@property(readonly) CGFloat magenta;

/** A value from 0.0 - 1.0 representing the yellow component. */
@property(readonly) CGFloat yellow;

/** A value from 0.0 - 1.0 representing the key. */
@property(readonly) CGFloat key;

@end
     
@class APBWColorPrivate;

/** Represents a grayscale color (optionally with alpha). */
@interface APBWColor : NSObject
{
@private
APBWColorPrivate *_APBWColorPrivate;
}

/** A value from 0.0 - 1.0 representing the white component. */
@property(readonly) CGFloat white;

/** A value from 0.0 - 1.0 representing the transparency. */
@property(readonly) CGFloat alpha;

@end
     
@class APColorPrivate;

/** Represents a color for a PDF object; effectively a union of the three possible color types. */
@interface APColor : NSObject
{
@private
APColorPrivate *_APColorPrivate;
}

/** Indicates the type of color being used. In most cases, this will be an APRGBColor. */
@property(readonly) APColorType colorType;

/** The RGB color object for this annotation, only valid if the colorType us kAPColorType_RGB */
@property(readonly) APRGBColor* rgbColor;

/** The CMYK color object for this annotation, only valid if the colorType us kAPColorType_CMYK */
@property(readonly) APCMYKColor* cmykColor;

/** The greyscale color object for this annotation, only valid if the colorType us kAPColorType_BW */
@property(readonly) APBWColor* bwColor;


/**
 * Used to create an APColor object from a UIColor.
 */
+(APColor *)colorWithUIColor:(UIColor *)color;

/**
 * Used to update an APColor object from a UIColor.
 */
-(void)updateWithUIColor:(UIColor *)color;

/**
 * Used to obtain the UIColor object from the APColor.
 */
-(UIColor *)UIColor;

      @end
     
@class APPDFObjectPrivate;

/** The base class for data objects (annotations and outline elements) associated with a PDF file. */
@interface APPDFObject : NSObject
{
@private
APPDFObjectPrivate *_APPDFObjectPrivate;
}

/** The color of the object; has different meanings in the context of various annotations. */
@property(retain) APColor* color;

@end
     
@class APAnnotationPrivate;

/** The base class for all annotations in a PDF. */
@interface APAnnotation : APPDFObject
{
@private
APAnnotationPrivate *_APAnnotationPrivate;
}

/** 
The 0-based PDF page upon which this annotation appears.

This attribute is required when creating new annotation objects.
         */
@property(assign) NSUInteger page;

/** 
"Page Space" coordinates of the bounding rectangle of the annotation. Page Space coordinates are in the domain 0.0 - 1.0 and represent fraction of a page's viewable area, with an origin in the top-left corner and positive y-values moving down the page.

This attribute is required when creating new annotation objects.
         */
@property(assign) CGRect rect;

/** Bitmask specifying flags for what you can do to/with this annotation (see section 12.5.3 of the PDF standard). \sa APAnnotationFlags_e  */
@property(readonly) NSUInteger flags;

/** Indication of when this annotation was last modified, in number of seconds since 1970. */
@property(readonly) NSUInteger lastModified;

@end
     
@class APBookmarkPrivate;

/** Data object for Bookmark annotations. Note that Bookmark annotations do not have a clear analog in the PDF annotation model. When persisting to the PDF, Bookmarks are added to a special section in the PDF Outline. \sa APOutlineElement */
@interface APBookmark : APAnnotation
{
@private
APBookmarkPrivate *_APBookmarkPrivate;
}

/** The Bookmark name. */
@property(retain) NSString * name;

@end
     
@class APInternalDestinationPrivate;

/** Used to represent a destination in the PDF document. */
@interface APInternalDestination : NSObject
{
@private
APInternalDestinationPrivate *_APInternalDestinationPrivate;
}

/** See section 12.3.2.2 of the PDF standard for the meaning of fit type. */
@property(readonly) APDestinationFitType fitType;

/** The 0-based page index. */
@property(readonly) NSUInteger page;

/** A point in Page Space indicating the location of the destination. \sa APAnnotation::rect */
@property(readonly) CGPoint topLeftPt;

/** A point in Page Space indicating the extent of the destination. \sa APAnnotation::rect */
@property(readonly) CGPoint bottomRightPt;

@end
     
@class APDestinationPrivate;

/** Used to represent the destination of a link; can either be a URL or an internal destination. */
@interface APDestination : NSObject
{
@private
APDestinationPrivate *_APDestinationPrivate;
}

/** For specifying 'external' destinations.  Cannot have an internalSpot set. */
@property(readonly) NSString * url;

/** For specifying document-internal (local) destinations.  Cannot have a URL set. */
@property(readonly) APInternalDestination* internalSpot;

@end
     
@class APLinkPrivate;

/** Used to represent a link in the PDF file. */
@interface APLink : APAnnotation
{
@private
APLinkPrivate *_APLinkPrivate;
}

/** The destination of the link. */
@property(readonly) APDestination* destination;

@end
     
@class APOutlineElementPrivate;

/** Used to represent entries in the PDF Outline tree. \sa APBookmark */
@interface APOutlineElement : APPDFObject
{
@private
APOutlineElementPrivate *_APOutlineElementPrivate;
}

/** The title of the element. */
@property(readonly) NSString * title;

/** Indicates whether or not this is the (invisible) root Outline element. */
@property(readonly) BOOL isRoot;

/** The children of this Outline element. */
@property(readonly) NSArray* children;

-(NSUInteger)childrenCount;
-(APOutlineElement*)children:(NSUInteger)index;
/** The PDF destination of this element. */
@property(readonly) APDestination* destination;

@end
     
@class APMarkupAnnotationPrivate;

/** Base class for user markup annotations. */
@interface APMarkupAnnotation : APAnnotation
{
@private
APMarkupAnnotationPrivate *_APMarkupAnnotationPrivate;
}

/** 
The author of the annotation.

This attribute is required when creating new markup annotation objects.
         */
@property(retain) NSString * title;

/** Indication of when this annotation was created, in number of seconds since the reference date (see NSTimeInterval). */
@property(readonly) NSUInteger creationStamp;

/** 
Text contents of the associated pop-up note.

This attribute is required when creating new markup annotation objects (it may be empty).
         */
@property(retain) NSString * contents;

/** Indicates the "sticky" state of the annotation. */
@property(assign) BOOL popupVisible;

@end
     
@class APTextPrivate;

/** Represents a popup (note) annotation. */
@interface APText : APMarkupAnnotation
{
@private
APTextPrivate *_APTextPrivate;
}

@end
     
@class APLocationPrivate;

/** Represents a point in the path of an ink annotation (in Page Space). \sa APAnnotation::rect */
@interface APLocation : NSObject
{
@private
APLocationPrivate *_APLocationPrivate;
}

@property(assign) CGPoint location;

@end
     
@class APPathPrivate;

/** Represents a path component in an ink annotation. */
@interface APPath : NSObject
{
@private
APPathPrivate *_APPathPrivate;
}

/** The list of points making the path. */
@property(readonly) NSArray* points;

-(NSUInteger)pointsCount;
-(APLocation*)points:(NSUInteger)index;
-(void)addToPoints:(APLocation*)points;
-(void)insertInPoints:(APLocation*)points atIndex:(NSUInteger)index;
-(void)removeFromPoints:(APLocation*)points;
@end
     







     
@class APTextAreaPrivate;

/** Represents a rectangular area (in Page Space) of a markup annotation. \sa APAnnotation::rect */
@interface APTextArea : NSObject
{
@private
APTextAreaPrivate *_APTextAreaPrivate;
}

@property(assign) CGRect area;

@end
     
@class APTextMarkupPrivate;

/** Annotation object for highlight, underline, and strike-out annotations. */
@interface APTextMarkup : APMarkupAnnotation
{
@private
APTextMarkupPrivate *_APTextMarkupPrivate;
}

/** 
Indicates the markup type of this annotation.

This attribute is required when creating new markup annotation objects.
         */
@property(assign) APTextMarkupType textType;

/** 
Rectangular areas, in page space, that are covered by the markup.

This attribute is required when creating new markup annotation objects.

\sa APAnnotation::rect
         */
@property(readonly) NSArray* areas;

-(NSUInteger)areasCount;
-(APTextArea*)areas:(NSUInteger)index;
-(void)addToAreas:(APTextArea*)areas;
-(void)insertInAreas:(APTextArea*)areas atIndex:(NSUInteger)index;
-(void)removeFromAreas:(APTextArea*)areas;
/** If available, the text (in the document) that is marked-up. */
@property(retain) NSString * markupText;

@end
     
@class APInkPrivate;

/** Annotation object for ink annotations (includes "straight-line" annotations). */
@interface APInk : APMarkupAnnotation
{
@private
APInkPrivate *_APInkPrivate;
}

/** 
The paths that compose this annotation.

This attribute is required when creating new ink annotation objects.
         */
@property(readonly) NSArray* paths;

-(NSUInteger)pathsCount;
-(APPath*)paths:(NSUInteger)index;
-(void)addToPaths:(APPath*)paths;
-(void)insertInPaths:(APPath*)paths atIndex:(NSUInteger)index;
-(void)removeFromPaths:(APPath*)paths;
/** 
The width of the line used to make this annotation.

This attribute is required when creating new ink annotation objects.
         */
@property(assign) CGFloat penWidth;


/** Converts from a thickness value (thin 0.0 to 1.0 thick) to the penWidth used for drawing */
+(CGFloat)inkPenWidthForThickness:(CGFloat)thickness;

/** Converts from the penWidth used for drawing to a thickness value (thin 0.0 to 1.0 thick) */
+(CGFloat)thicknessForInkPenWidth:(CGFloat)penWidth;
      @end
     



     



     


     
@class APFileAttachmentPrivate;

/** Images/photos will be displayed in the reader.  Other file types will be opened only if there is an app registered that can be used with "Open In" for that type. */
@interface APFileAttachment : APMarkupAnnotation
{
@private
APFileAttachmentPrivate *_APFileAttachmentPrivate;
}

/** The filename of the attached file. */
@property(retain) NSString * fileName;

/** Optional string to be used in the UI to identify the file. */
@property(retain) NSString * description;

/** Optional field that will either be a mime type or a proprietary type extension (see Annex E of the ISO PDF standard). */
@property(retain) NSString * fileType;

/** The attachment data. */
@property(retain) NSData * fileData;

@end
     
@class APSoundDescriptionPrivate;

@interface APSoundDescription : NSObject
{
@private
APSoundDescriptionPrivate *_APSoundDescriptionPrivate;
}

@property(readonly) CGFloat samplingRate;

@property(readonly) NSUInteger channels;

@property(readonly) NSUInteger bitsPerSamplePerChannel;

@property(readonly) APSoundEncoding encodingFormat;

@property(readonly) NSString * compressionFormat;

@property(readonly) NSString * compressionParameters;

@end
     
@class APSoundPrivate;

/** Represents a Sound annotation.  Note that in the PDF standard, a Sound annotation is technically not a type of Markup annotation, but for the purposes of library usage it makes more sense as a Markup annotation.  Sounds exported to PDF will still function as normal. */
@interface APSound : APMarkupAnnotation
{
@private
APSoundPrivate *_APSoundPrivate;
}

/** The sound data. */
@property(readonly) NSData * soundData;

/** Detailed information about the sound format and encoding. */
@property(readonly) APSoundDescription* soundDescription;


/**
 * Creates and returns an NSData of the sound in WAV format.
 */
-(NSData *)makeWavData;
      @end
     
@class APTransformableMarkupAnnotationPrivate;

/** Base class for transformable user markup annotations. */
@interface APTransformableMarkupAnnotation : APMarkupAnnotation
{
@private
APTransformableMarkupAnnotationPrivate *_APTransformableMarkupAnnotationPrivate;
}

/** The affine transform that was applied to the original annotation. */
@property(assign) CGAffineTransform transform;

/** Holds the Rect of the annotation prior to the application of the transform. */
@property(assign) CGRect originalRect;

@end
     
@class APStampPrivate;

/** Represents images that are pasted into the PDF as if they had been rubber-stamped onto the page. */
@interface APStamp : APTransformableMarkupAnnotation
{
@private
APStampPrivate *_APStampPrivate;
}


@end
     
@class APFreeTextPrivate;

/** A free-text (typewriter) annotation, with text that is embedded directly onto the PDF page content. */
@interface APFreeText : APTransformableMarkupAnnotation
{
@private
APFreeTextPrivate *_APFreeTextPrivate;
}

/** Text justification within the rectangle. */
@property(assign) APFreeTextJustification justification;

/** Font name to be used. */
@property(retain) NSString * fontName;

/** Point size text. */
@property(assign) CGFloat fontSize;


      @end
     
@class APSearchResultPrivate;

/** Data object describing a search result from a query based on an APSearchRequest. */
@interface APSearchResult : NSObject
{
@private
APSearchResultPrivate *_APSearchResultPrivate;
}

/** The actual text from the document that matched the query (e.g., could be different from the query text due to case insensitivity or if a regular expression search was performed). */
@property(readonly) NSString * matchedText;

/** The page number upon which this match was found. */
@property(readonly) NSUInteger pageIndex;

/** Additional textual context, if available, found before and after the matched text. */
@property(readonly) NSString * context;

/** The 0-based index of the matchedText attribute within the context attribuate. */
@property(readonly) NSUInteger indexOfMatchInContext;

/** One or more rectangles in page space where the characters matching the search result were found. */
@property(readonly) NSArray* areas;

-(NSUInteger)areasCount;
-(APTextArea*)areas:(NSUInteger)index;
/** The bounding rectangle of the matched areas. */
@property(readonly) CGRect bounds;

/** If this result was found in an annotation, the annotation object in which the match was found; otherwise nil. */
@property(readonly) APAnnotation* annotation;

@end
     
@class APSearchRequestPrivate;

/** Used to describe a search query, which can be passed to APPDFInformation::performSearch: to do a full-text search of an APPDFDocument */
@interface APSearchRequest : NSObject
{
@private
APSearchRequestPrivate *_APSearchRequestPrivate;
}

/** The text to use for the search query. */
@property(retain) NSString * query;

/** The page upon which to start the search.  Must fall within range defined by pageRangeStart and pageRangeLength. */
@property(assign) NSUInteger basePage;

/** The first page in the range of pages in which to search.  basePage must fall within this range. */
@property(assign) NSUInteger pageRangeStart;

/** The number of pages starting at searchRangeStartPage to search.  Use 0 to search all.  basePage must fall within this range. */
@property(assign) NSUInteger pageRangeLength;

/** The location on the base page from which to start the search. */
@property(assign) CGPoint baseLocation;

/** Indicates whether the query attribute is a plain text search, or a regular expression (see NSRegularExpression). */
@property(assign) BOOL queryIsRegularExpression;

/** Indicates whether annotations should also be searched, or just the plain text of the document. */
@property(assign) BOOL searchAnnotations;

@property(assign) BOOL cancel;

@property(readonly) BOOL searchResultsCapped;

@property(readonly) BOOL inProgress;


-(NSUInteger)numResults;
      @end
     