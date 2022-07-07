//
//  GXControlStarRating.m
//  GXFlexibleClient
//
//  Created by Marcos Crispino on 01/06/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GXControlStarRating.h"
#import "DLStarRatingControl.h"
#import "DLStarViewRender.h"

#define kSelectedColorPropName @"RatingSelectedColor"
#define kUnselectedColorPropName @"RatingUnselectedColor"

@implementation RatingThemeClass
+ (NSMutableDictionary<NSString *, id> *)loadPropertiesValuesByName:(NSMutableDictionary<NSString *, id> *)propertyValuesByName
													   fromMetadata:(nullable NSDictionary<NSString *, id> *)metadata {
	
	propertyValuesByName = [[self superclass] loadPropertiesValuesByName:propertyValuesByName
															fromMetadata:metadata];
	propertyValuesByName = [GXThemeClassWithBackgroundHelper loadPropertiesValuesByName:propertyValuesByName
																		   fromMetadata:metadata];
	propertyValuesByName = [GXThemeClassWithBorderHelper loadPropertiesValuesByName:propertyValuesByName
																	   fromMetadata:metadata];
	propertyValuesByName = [GXThemeClassWithLabelHelper loadPropertiesValuesByName:propertyValuesByName
																	  fromMetadata:metadata];
	propertyValuesByName = [GXThemeClassWithMarginHelper loadPropertiesValuesByName:propertyValuesByName
																	   fromMetadata:metadata];
	propertyValuesByName = [GXThemeClassWithPaddingHelper loadPropertiesValuesByName:propertyValuesByName
																		fromMetadata:metadata];
	UIColor *selectedColor = [UIColor colorFromValue:[metadata objectForKey:kSelectedColorPropName]];
	if (selectedColor) {
		propertyValuesByName[kSelectedColorPropName] = selectedColor;
	}
	UIColor *unselectedColor = [UIColor colorFromValue:[metadata objectForKey:kUnselectedColorPropName]];
	if (unselectedColor) {
		propertyValuesByName[kUnselectedColorPropName] = unselectedColor;
	}
	return propertyValuesByName;
}

- (nullable UIColor *)selectedColor {
	return self.themePropertiesValuesByName[kSelectedColorPropName];
}

- (nullable UIColor *)unselectedColor {
	return self.themePropertiesValuesByName[kUnselectedColorPropName];
}

@end

static int CalculateNumberOfStars(int value, int step) {
	if (step == 0) return 0;
	return (value / step) + (value % step == 0 ? 0 : 1);
}

#pragma mark -

@implementation GXControlStarRating

@synthesize maxValue = _maxValue, step = _step;

#pragma mark - GXControlEditableWithLabelBase abstract methods

- (CGRect)editorViewFrameForEditorFrame:(CGRect)editorFrame {
	int cantStars = CalculateNumberOfStars(_maxValue, _step);
	if (cantStars > 0) {
		CGFloat boundSize = MIN(editorFrame.size.width / cantStars, editorFrame.size.height);
		CGRect starFrame = CGRectMake(editorFrame.origin.x, editorFrame.origin.y, boundSize * cantStars, boundSize);
        switch ([self horizontalAlign]) {
            case GXHorizontalAlignTypeCenter:
            case GXHorizontalAlignTypeDefault:
                starFrame.origin.x += (editorFrame.size.width - starFrame.size.width) / 2;
                break;
            case GXHorizontalAlignTypeLeft:
                break;
            case GXHorizontalAlignTypeRight:
                starFrame.origin.x += (editorFrame.size.width - starFrame.size.width);
                break;
        }
		
		switch ([self verticalAlign]) {
			case GXVerticalAlignTypeBottom:
				starFrame.origin.y += editorFrame.size.height - starFrame.size.height;
				break;
			case GXVerticalAlignTypeCenter:
			case GXVerticalAlignTypeDefault:
				starFrame.origin.y += (editorFrame.size.height - starFrame.size.height) / 2;
				break;
			default:
				break;
		}
		return GXPixelAlignedFrameWithMainScreenScale(starFrame);
	}
	else {
		editorFrame.size = CGSizeZero;
		return GXPixelAlignedFrameWithMainScreenScale(editorFrame);
	}
}

- (UIView *)newEditorViewWithFrame:(CGRect)frame {
    if ([self properties]) {
        _maxValue = (int)[[self properties] getPropertyValueInteger:@"@RatingMaxValue"];
        _step =     (int)[[self properties] getPropertyValueInteger:@"@RatingStep"];
    }
    else {
        _maxValue = 5;
        _step = 1;
    }
    
    int cantStars = CalculateNumberOfStars(_maxValue, _step);    
    DLStarRatingControl *control = [[DLStarRatingControl alloc] initWithFrame:frame andStars:cantStars];
    [control setContentVerticalAlignment:UIControlContentVerticalAlignmentCenter];
    [control addTarget:self action:@selector(valueChangedForControl:) forControlEvents:UIControlEventValueChanged];
    [control setEnabled:self.enabled && !self.readOnly];
    return control;
}

- (void)valueChangedForControl:(id)sender {
    if (sender == self.starRatingControl && !self.readOnly) {
		
		id objectOldValue = [self entityDataFieldValue];
		int oldValue = 0; 
		if (objectOldValue && [objectOldValue respondsToSelector:@selector(intValue)]) {
			oldValue = [objectOldValue intValue];
		}
		
        int val = self.starRatingControl.rating * self.step;
        if (val > self.maxValue) {
            val = self.maxValue;
		}
		
		if (val == oldValue && oldValue == 1) {
			// tapped the only selected star, wants to clear the value (see issue 19930)
			val = 0;
			[[self starRatingControl] setRating:0];
		}
        
		[self updateEntityDataResolvedFieldWithValue:[NSNumber numberWithInt:val]];
	}
}

- (nullable RatingThemeClass *)ratingThemeClass {
	GXThemeClass *themeClass = self.themeClass;
	return [themeClass isKindOfClass:[RatingThemeClass class]] ? (RatingThemeClass *)themeClass : nil;
}

- (void)applyThemeClass {
	[super applyThemeClass];
	if ([self isViewLoaded]) {
		RatingThemeClass *ratingThemeClass = self.ratingThemeClass;
		DLStarRatingControl *starRatingControl = [self starRatingControl];
		starRatingControl.selectedColor = ratingThemeClass.selectedColor ? : DLStarViewRender.defaultSelectedColor;
		starRatingControl.unselectedColor = ratingThemeClass.unselectedColor ? : DLStarViewRender.defaultUnselectedColor;
		[starRatingControl invalidate];
	}
}

#pragma mark - GXControlEditableWithLabelBase overrides 

- (void)onReadOnlyChanged {
	[super onReadOnlyChanged];
    if ([self isEditorViewLoaded]) {
        [self.starRatingControl setEnabled:!self.readOnly && self.enabled];
    }
}

- (void)onEnabledChanged {
	[super onEnabledChanged];
	if ([self isEditorViewLoaded]) {
		[self.starRatingControl setEnabled:self.enabled && !self.readOnly];
	}
}

- (void)loadEntityDataFieldValue {
	int value = [self.entityDataFieldValue respondsToSelector:@selector(intValue)] ? MAX(0, [self.entityDataFieldValue intValue]) : 0;
	[self.starRatingControl setRating:CalculateNumberOfStars(value, self.step)];
}

+ (BOOL)isValidForDataType:(GXDataType)dataType {
    return (dataType == GXDataTypeNumeric);
}

#pragma mark - Private

- (DLStarRatingControl *)starRatingControl {
    return (DLStarRatingControl *)[self loadedEditorView];
}

@end


