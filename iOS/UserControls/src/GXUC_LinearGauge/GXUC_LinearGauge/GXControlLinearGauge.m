//
//  GXControlLinearGauge.m
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/04/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GXControlLinearGauge.h"
#import "GXLinearGaugeView.h"

@implementation GXControlLinearGauge

#pragma mark - GXControlWithLabelBase abstract methods

- (void)setCaption:(NSString *)caption {
	[super setCaption:caption];
	if (caption) {
		[self.linearGaugeView setNeedsLayout];
	}
}

- (UIView *)newEditorViewWithFrame:(CGRect)frame {
    return [[GXLinearGaugeView alloc] initWithFrame:frame specification:[self newSpeficicationFromFieldValue]];
}

- (void)loadEntityDataFieldValue {
	self.linearGaugeView.specification = [self newSpeficicationFromFieldValue];
}

- (void)applyThemeClass {
	[super applyThemeClass];
	GXLinearGaugeView *view = self.linearGaugeView;
	if (view != nil) {
		GXThemeClass *themeClass = self.themeClass;
		if (themeClass != view.themeClass) {
			view.themeClass = themeClass;
		}
		else {
			[view applyThemeClass];
		}
	}
}

#pragma mark - Private Methods

- (nullable GXLinearGaugeView *)linearGaugeView {
    return (GXLinearGaugeView *)[super loadedEditorView];
}

- (nonnull GXLinearGaugeSpecification *)newSpeficicationFromFieldValue {
	NSString *stringValue = [GXUtilities nonEmptyStringFromObject:self.entityDataFieldValue];
	return [[GXLinearGaugeSpecification alloc] initFromFromValue:stringValue];
}

@end
