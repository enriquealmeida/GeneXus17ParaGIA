//
//  GXControlCountDown.h
//  GXFlexibleClient
//
//  Created by willy on 4/30/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <GXFlexibleClient/GXFlexibleClient.h>
#import "GXControlCountDownUIControl.h"

@interface GXControlCountDown : GXControlEditableWithLabelSingleEditorViewBase <GXControlCountDownUIControlDelegateProtocol> {
    @private
    BOOL _skipDataChangeEvent;
}

@end
