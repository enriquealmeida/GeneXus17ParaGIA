//
//  GXPieChartLegendsView.m
//  UCChart
//
//  Created by Marcos Crispino on 12/11/12.
//
//

#import "GXPieChartLegendsView.h"
@import GXFoundation;

#define kLabelSeparationOffset 5
#define kReferenceRectDimension 10
#define kReferenceRectAndLabelSeparation 5

@implementation GXPieChartLegendsView

@synthesize legends = _legends;
@synthesize colors = _colors;

#pragma mark - Public methods

- (void)setLegends:(NSArray *)legends andColors:(NSArray *)colors {
#ifdef DEBUG
	NSAssert(legends.count == colors.count, @"");
#endif
	
	if (legends.count == colors.count) {
		_legends = legends;
		_colors = colors;
		[self setNeedsLayout];
	}
}

#pragma mark - Overrides

- (void)layoutSubviews {
    NSUInteger count = [_legends count];
    NSAssert(count == [_colors count], @"Number of items do not match for labels and colors");
    
    [[self subviews] makeObjectsPerformSelector:@selector(removeFromSuperview)];
    
    UIFont *font = [UIFont boldSystemFontOfSize:13.0];
    
    CGFloat yOffset = 0;
 
    CGSize boundsSize = self.bounds.size;
    
    CGFloat labelXOffset = kReferenceRectDimension + kReferenceRectAndLabelSeparation;
    
    CGSize labelMaxSize = boundsSize;
    labelMaxSize.width -= labelXOffset;

    for (int i = 0; i < count; i++) {
        NSString *legend = [_legends objectAtIndex:i];
        UIColor *color = [_colors objectAtIndex:i];
        
        CGSize size = [legend gxSizeWithFont:font];
		size.width = MIN(labelMaxSize.width, size.width);
		size.height = MIN(labelMaxSize.height, size.height);

        CGFloat referenceRectYPos = yOffset + (size.height - kReferenceRectDimension) / 2;
        CGRect referenceFrame = CGRectMake(0, referenceRectYPos, kReferenceRectDimension, kReferenceRectDimension);

        UIView *referenceView = [[UIView alloc] initWithFrame:referenceFrame];
        [referenceView setBackgroundColor:color];
        [self addSubview:referenceView];
        
        CGRect labelFrame = CGRectMake(labelXOffset, yOffset, size.width, size.height);
        
        UILabel *labelView = [[UILabel alloc] initWithFrame:labelFrame];
        [labelView setText:legend];
        [labelView setFont:font];
        [labelView setTextAlignment:NSTextAlignmentCenter];
        [labelView setTextColor:color];
        [labelView setBackgroundColor:[UIColor clearColor]];
        [labelView setAutoresizingMask:UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin];
        [self addSubview:labelView];
        
        yOffset += size.height + kLabelSeparationOffset;
    }
    
    boundsSize.height = yOffset;
    [self setContentSize:boundsSize];
}

@end
