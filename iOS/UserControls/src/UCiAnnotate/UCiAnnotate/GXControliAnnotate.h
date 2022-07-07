//
//  GXControliAnnotate.h
//  GXFlexibleClient
//
//  Created by willy on 8/28/13.
//  Copyright (c) 2013 Artech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GXFlexibleClient/GXFlexibleClient.h>
#import "AjiPDFLib.h"
#import "GXHttpDownloader.h"

@interface GXControliAnnotate : GXControlEditableWithLabelSingleEditorViewBase <NSURLConnectionDataDelegate, GXHttpDownloaderDelegate, APPDFViewDelegate, APPDFProcessorDelegate>

@end
