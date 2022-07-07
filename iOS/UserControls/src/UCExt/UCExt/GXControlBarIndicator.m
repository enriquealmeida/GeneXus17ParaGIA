//
//  GXControlBarIndicator.m
//  GXFlexibleClient
//
//  Created by willy on 5/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "GXControlBarIndicator.h"
#import "GXControlBarIndicatorUIControl.h"

@implementation GXControlBarIndicator

#pragma mark - GXControlEditableWithLabelBase overrides 

- (void)setReadOnly:(BOOL)readOnly {
    [super setReadOnly:readOnly];
    if ([self isEditorViewLoaded]) {
        [self.barControl setEnabled:!readOnly && self.enabled];
    }
}

- (void)setEnabled:(BOOL)enabled {
	[super setEnabled:enabled];
	if ([self isEditorViewLoaded]) {
		[self.barControl setEnabled:enabled && !self.readOnly];
	}
}

- (void)loadEntityDataFieldValue {
    if (_skipDataChangeEvent) {
        _skipDataChangeEvent = NO;
    } else {
        NSString *value = [self entityDataFieldValue];
        if ([value isKindOfClass:[NSString class]]) {
            NSString *maxValue;
            if ([GXUtilities splitString:value ByFirstOcurrenceOfString:@"," toLeft:&value andRigth:&maxValue]) {
                self.barControl.maxValue = [maxValue integerValue];
            }
            self.barControl.value = [value integerValue];
        } else if ([value isKindOfClass:[NSNumber class]])
            self.barControl.value = [value integerValue];
    }
}

+ (BOOL)isValidForDataType:(GXDataType)dataType {
    return (dataType == GXDataTypeNumeric || dataType == GXDataTypeCharacter);
}

- (id)valueForPropertyName:(NSString *)propertyName {
	id propValue = [super valueForPropertyName:propertyName];
	if (propValue == nil) {
		if ([propertyName isEqualToString:@"barheight"]) {
			propValue = [NSNumber numberWithUnsignedInteger:[self barControl].barHeight];
		}
		else if ([propertyName isEqualToString:@"maxvalue"]) {
			propValue = [NSNumber numberWithUnsignedInteger:[self barControl].maxValue];
		}
	}
	return propValue;
}

- (BOOL)applyPropertyValue:(id)propValue toPropertyName:(NSString *)propName {
	if ([super applyPropertyValue:propValue toPropertyName:propName]) {
		return YES;
	} else if ([propName isEqualToString:@"barheight"]) {
        [self barControl].barHeight = [propValue intValue];
		return YES;
    } else if ([propName isEqualToString:@"maxvalue"]) {
        [self barControl].maxValue = [propValue intValue];
		return YES;
    } else if ([propName isEqualToString:@"barcolor"]) {
        [[self barControl] setBarColor:[GXThemeHelper colorFromValue:propValue]];
		return YES;
    }
	return NO;
}

#pragma mark - GXControlEditableWithLabelBase abstract methods

- (CGRect)editorViewFrameForEditorFrame:(CGRect)editorFrame {
    return editorFrame;
}

- (UIView *)newEditorViewWithFrame:(CGRect)frame {
    GXControlBarIndicatorUIControl *control = [[GXControlBarIndicatorUIControl alloc] initWithFrame:frame];
    id<GXPropertiesObject> props = [[self layoutElementData] layoutElementDataCustomProperties]; 
    control.maxValue = [props getPropertyValueInteger:@"@SDBarIndicatorMaxValue"];
    control.rightToLeft = [[props getPropertyValueString:@"@SDBarIndicatorDisplayStyle"] isEqualToString:@"rtl"];
    control.barHeight = [props getPropertyValueInteger:@"@SDBarIndicatorBarHeight"];
    [control setBarColor:[GXThemeHelper colorFromValue:[props getPropertyValueString:@"@SDBarIndicatorBarColor"]]];
    return control;
}

#pragma mark - Private

- (GXControlBarIndicatorUIControl *)barControl {
    return (GXControlBarIndicatorUIControl *)[self loadedEditorView];
}

@end
