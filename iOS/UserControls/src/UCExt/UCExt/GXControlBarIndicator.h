//
//  GXControlBarIndicator.h
//  GXFlexibleClient
//
//  Created by willy on 5/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GXFlexibleClient/GXFlexibleClient.h>

@interface GXControlBarIndicator : GXControlEditableWithLabelSingleEditorViewBase {
    @private
    BOOL _skipDataChangeEvent;
}

@end
