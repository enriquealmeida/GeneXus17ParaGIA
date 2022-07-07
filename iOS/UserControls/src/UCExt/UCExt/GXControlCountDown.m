//
//  GXControlCountDown.m
//  GXFlexibleClient
//
//  Created by willy on 4/30/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "GXControlCountDown.h"

@implementation GXControlCountDown

#pragma mark - GXControlEditableWithLabelBase overrides 

- (void)setReadOnly:(BOOL)readOnly {
    [super setReadOnly:readOnly];
    if ([self isEditorViewLoaded]) {
        [self.countDownControl setEnabled:!readOnly && self.enabled];
    }
}

- (void)setEnabled:(BOOL)enabled {
	[super setEnabled:enabled];
	if ([self isEditorViewLoaded]) {
		[self.countDownControl setEnabled:enabled && !self.readOnly];
	}
}

- (void)viewWillDisappear:(BOOL)animated {
    [self.countDownControl suspend];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.countDownControl resume];
}

- (void)loadEntityDataFieldValue {
    if (_skipDataChangeEvent) {
        _skipDataChangeEvent = NO;
    } else
        self.countDownControl.value = [[self entityDataFieldValue] intValue];
}

+ (BOOL)isValidForDataType:(GXDataType)dataType {
    return (dataType == GXDataTypeNumeric);
}

#pragma mark GXControlCountDownControlDelegateProtocol

- (void)gXControlCountDownUIControlTick {
    _skipDataChangeEvent = YES;
    [self updateEntityDataResolvedFieldWithValue:[NSNumber numberWithInt: self.countDownControl.value]];
}

- (void)gXControlCountDownUIControlCounterEnded {
	GXControlCountDownUIControl *countDownControl = [self countDownControl];
	GXUserInterfaceContext *uiContext = [GXUserInterfaceContext new];
	[uiContext addUserInterfaceContextWithView:countDownControl];
	[self fireControlEvent:@"end" sender:countDownControl userInterfaceContext:uiContext];
}

#pragma mark - GXControlEditableWithLabelBase abstract methods

- (CGRect)editorViewFrameForEditorFrame:(CGRect)editorFrame {
    return editorFrame;
}

- (UIView *)newEditorViewWithFrame:(CGRect)frame {
    GXControlCountDownUIControl *control = [[GXControlCountDownUIControl alloc] initWithFrame:frame];
    [control setDelegate:self];
    return control;
}

- (void)executeMethod:(NSString *)methodName withParameters:(NSArray *)parameters {
    if ([methodName isEqualToString:@"Stop"]) {
        [self.countDownControl suspend];
    }
}

#pragma mark - Private

- (GXControlCountDownUIControl *)countDownControl {
    return (GXControlCountDownUIControl *)[self loadedEditorView];
}

@end
