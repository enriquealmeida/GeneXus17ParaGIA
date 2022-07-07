//
//  GXControlStarRating.h
//  GXFlexibleClient
//
//  Created by Marcos Crispino on 01/06/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

@import GXCoreUI;
@import GXObjectsModel;

NS_ASSUME_NONNULL_BEGIN

@interface RatingThemeClass : GXThemeClassAttribute

@property(nullable, nonatomic, strong, readonly) UIColor *selectedColor;
@property(nullable, nonatomic, strong, readonly) UIColor *unselectedColor;

@end

@interface GXControlStarRating : GXControlEditableWithLabelSingleEditorViewBase {
    int _maxValue;
    int _step;
}

@property (nonatomic, readonly) int maxValue;
@property (nonatomic, readonly) int step;

@end

NS_ASSUME_NONNULL_END
