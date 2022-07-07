//
//  UIGradientView.m
//  GXFlexibleClient
//
//  Created by willy on 5/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "GXGradientView.h"

@implementation GXGradientView

-(void)updateGradient {
	CGColorSpaceRef rgb = CGColorSpaceCreateDeviceRGB();
	CGFloat colors[8];
	
	if(self.backgroundColor)
	{
		CGColorRef col = self.backgroundColor.CGColor;
		CGColorSpaceRef colSpace = CGColorGetColorSpace(col);
		const CGFloat *bgCols = CGColorGetComponents(col);
		if(CGColorSpaceGetNumberOfComponents(colSpace) == 1) // White Color Space 
		{
			colors[0] = bgCols[0];
			colors[1] = bgCols[0];
			colors[2] = bgCols[0];
			colors[3] = bgCols[1];
		}
		else
		{
			colors[0] = bgCols[0];
			colors[1] = bgCols[1];
			colors[2] = bgCols[2];
			colors[3] = bgCols[3];
		}
	}
	if(self.gradientColor)
	{
		CGColorRef col = self.gradientColor.CGColor;
		CGColorSpaceRef colSpace = CGColorGetColorSpace(col);
		const CGFloat *bgCols = CGColorGetComponents(col);
		if(CGColorSpaceGetNumberOfComponents(colSpace) == 1) // White Color Space 
		{
			colors[4] = bgCols[0];
			colors[5] = bgCols[0];
			colors[6] = bgCols[0];
			colors[7] = bgCols[1];
		}
		else
		{
			colors[4] = bgCols[0];
			colors[5] = bgCols[1];
			colors[6] = bgCols[2];
			colors[7] = bgCols[3];
		}
	}
	
	if (_gradient) {
		CGGradientRelease(_gradient);
		_gradient = NULL;
	}
	
	if(self.backgroundColor && self.gradientColor)
	{
		_gradient = CGGradientCreateWithColorComponents(rgb, colors, NULL, sizeof(colors)/(sizeof(colors[0])*4));

	}
	
	CGColorSpaceRelease(rgb);
}

- (id)initWithCoder:(NSCoder *)coder {
    if (self = [super initWithCoder:coder]) {
		[self updateGradient];
    }
    return self;
}

-(void)setBackgroundColor: (UIColor *) color {
	BOOL updateGradient = color != self.backgroundColor;
	super.backgroundColor = color;
	if (updateGradient) {
		[self updateGradient];
	}
}

@synthesize gradientColor = _gradientColor;

-(void)setGradientColor: (UIColor *) color {
	if (_gradientColor != color) {
		_gradientColor = color;
		[self updateGradient];
		[self setNeedsDisplay];
	}
}

- (void)setFrame:(CGRect)frame {
	BOOL needsDisplay = !CGSizeEqualToSize(frame.size, self.frame.size);
    [super setFrame:frame];
	if (needsDisplay) {
		[self setNeedsDisplay];
	}
}

- (void)drawRect:(CGRect)rect {	
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	CGPoint start = CGPointMake(rect.origin.x, rect.origin.y);
	CGPoint end   = CGPointMake(rect.origin.x, rect.origin.y + rect.size.height);
	
	CGContextClipToRect(context, rect);
	if (_gradient) {
		CGContextDrawLinearGradient(context, _gradient, start, end, 0);
	}
	else if (self.backgroundColor && !self.gradientColor) {
		[[self backgroundColor] setFill];
		CGContextFillPath(context);
	} 
	else if(!self.backgroundColor && self.gradientColor)
	{
        // Not sure what to do here?
	}
	else 
	{
		assert(0);
	}
}

- (void)dealloc {
	if (_gradient) {
		CGGradientRelease(_gradient);
	}
}

@end
