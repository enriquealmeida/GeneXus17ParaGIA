//
//  KTThumbView.m
//  KTPhotoBrowser
//
//  Created by Kirby Turner on 2/3/10.
//  Copyright 2010 White Peak Software Inc. All rights reserved.
//

#import "KTThumbView.h"
#import "KTThumbsViewController.h"
@import QuartzCore;

@implementation KTThumbView

+ (UIImage*)imageWithImage:(UIImage*)image scaledToSize:(CGSize)newSize {
	CGSize imageSize = [image size];
	UIImage* newImage;
	if (CGSizeEqualToSize(imageSize, newSize)) {
		newImage = image;
	}
	else {
		UIGraphicsBeginImageContext( newSize );
		[image drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];
		newImage = UIGraphicsGetImageFromCurrentImageContext();
		UIGraphicsEndImageContext();
		
	}
	return newImage;
}

@synthesize touchDelegate = _touchDelegate;


- (id)initWithFrame:(CGRect)frame {
	if ((self = [super initWithFrame:frame])) {

		[self addTarget:self
				 action:@selector(didTouch:)
	   forControlEvents:UIControlEventTouchUpInside];

		[self setClipsToBounds:YES];

		// If the thumbnail needs to be scaled, it should mantain its aspect ratio.
		[[self imageView] setContentMode:UIViewContentModeScaleAspectFill];
	}
	return self;
}

- (void)didTouch:(id)sender {
	if (_touchDelegate) {
		[_touchDelegate didSelectThumbAtIndex:[self tag]];
	}
}

- (void)setThumbImage:(UIImage *)newImage  {
    UIImage *resizedImage = [[self class] imageWithImage:newImage scaledToSize:[self bounds].size];
	[self setImage:resizedImage forState:UIControlStateNormal];
	[self setShowsTouchWhenHighlighted:YES];
}

- (void)setHasBorder:(BOOL)hasBorder {
	if (hasBorder) {
		self.layer.borderColor = [UIColor colorWithWhite:0.85 alpha:1.0].CGColor;
		self.layer.borderWidth = 1;
	} else {
		self.layer.borderColor = nil;
	}
}

- (void)prepareForReuse {
	[self setImage:nil forState:UIControlStateNormal];
	[self setTouchDelegate:nil];
	[self setActivityIndicatorVisible:NO];
	[_overlayView removeFromSuperview];
	_overlayView = nil;
}

- (CGPoint)centerRelative {
	CGSize size = self.bounds.size;
	return CGPointMake(size.width/2, size.height/2);
}

- (void)setActivityIndicatorVisible:(BOOL)visible {
	if (visible) {
		if (!_activityIndicator) {
			_activityIndicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
		}
		
		[_activityIndicator setCenter:[self centerRelative]];
		[_activityIndicator startAnimating];
		[_activityIndicator setHidden:NO];
		[self addSubview:_activityIndicator];
	}
	else {
		[_activityIndicator stopAnimating];
		[_activityIndicator setHidden:YES];
		[_activityIndicator removeFromSuperview];
	}
}

- (void)layoutSubviews {
	[super layoutSubviews];
	if (_activityIndicator) {
		[_activityIndicator setCenter:[self centerRelative]];
	}
}

- (void)setSelected:(BOOL)selected {
	[super setSelected:selected];
	
	if (selected) {
		_overlayView = [[UIView alloc] initWithFrame:self.bounds];
		[_overlayView setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight];
		[_overlayView setBackgroundColor:[UIColor colorWithWhite:1 alpha:0.5]];
		[_overlayView setUserInteractionEnabled:NO];
		[self addSubview:_overlayView];
		[self bringSubviewToFront:_overlayView];
	}
	else {
		[_overlayView removeFromSuperview];
		_overlayView = nil;
	}
}

@end
