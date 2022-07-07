//
//  GXControliAnnotate.m
//  GXFlexibleClient
//
//  Created by willy on 8/28/13.
//  Copyright (c) 2013 Artech. All rights reserved.
//

#import "GXControliAnnotate.h"
#import <GXFlexibleClient/GXSDTDataProtocol.h>

static APLibrary *_library;

@implementation GXControliAnnotate {
	APAnnotatingPDFViewController *_pdfView;
	APPDFInformation *_info;
	
	NSString *_pdfFileName;
	NSString *_mergedAnnotationsFileName;
	NSArray *_annotationFiles;
	NSString *_annotationsFieldName;
	NSString *_userAnnotationsFieldName;
	id<GXEntityDataFieldDescriptor> _annotationsFieldDescriptor;
	id<GXEntityDataFieldDescriptor> _userAnnotationsFieldDescriptor;
	GXHttpDownloader *_downloader;
	APPDFProcessor *_processor;
	
	UIActivityIndicatorView *_activityIndicator;
}

- (id)initWithLayoutElement:(id<GXLayoutElement>)layoutElement controlId:(NSUInteger)controlId gxMode:(GXModeType)modeType fieldDescriptor:(id<GXEntityDataFieldDescriptor>)fieldDescriptor indexerStack:(NSArray *)indexer parentControl:(id<GXControlContainer>)parentControl {
	if ((self = [super initWithLayoutElement:layoutElement
								   controlId:controlId
									  gxMode:modeType
							 fieldDescriptor:fieldDescriptor
								indexerStack:indexer
							   parentControl:parentControl])) {
		_downloader = nil;
		_annotationFiles = nil;
		_annotationsFieldName = [[self properties] getPropertyValueString:@"@SDiAnnotateAnnotationsAttribute"];
		_userAnnotationsFieldName = [[self properties] getPropertyValueString:@"@SDiAnnotateOutputAnnotationsAttribute"];
		
		id<GXEntityDataDescriptor> dataDescriptor = [[self parentControl] dataDescriptor];
		_annotationsFieldDescriptor = [dataDescriptor entityDataFieldDescriptorForName:_annotationsFieldName];
		_userAnnotationsFieldDescriptor = [dataDescriptor entityDataFieldDescriptorForName:_userAnnotationsFieldName];
		_downloader = [[GXHttpDownloader alloc] init];
		[_downloader setDelegate:self];
		_processor = [[APPDFProcessor alloc] init];
		[_processor setDelegate:self];
		_processor.processingOptions = kAPPDFProcessingOptionsAnnotationsOnly;
	}
	return self;
}

- (void)dealloc {
	[_downloader setDelegate:nil];
}

- (NSString *)storageFolder {
	NSArray *docPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    return [docPaths objectAtIndex:0];
}

- (void)loadEntityDataFieldValue {
	if ([self.entityDataFieldValue isKindOfClass:[NSString class]] && [(NSString *)self.entityDataFieldValue length]) {
		[_activityIndicator startAnimating];
		[_downloader cancel];
		_annotationFiles = nil;
		NSURL *url = [GXEntityHelper urlFromFieldValue:self.entityDataFieldValue];
		_pdfFileName = [[self storageFolder] stringByAppendingPathComponent:[@"pdf" stringByAppendingString:[[url absoluteString]lastPathComponent]]];
		
		_mergedAnnotationsFileName = [_pdfFileName stringByAppendingPathExtension:@"xml"];
		GXHttpDownloadItem *pdfItem = [[GXHttpDownloadItem alloc] initWithUrl:url destinationFileName:_pdfFileName];

		NSMutableArray *downloadItems = [NSMutableArray arrayWithCapacity:1];
		[downloadItems addObject:pdfItem];
		
		id annotationsValue = [self.entityData valueForEntityDataFieldName:_annotationsFieldName fieldSpecifier:nil fieldDescriptor:_annotationsFieldDescriptor];
		if ([annotationsValue conformsToProtocol:@protocol(GXSDTDataCollection) ]) {
			id<GXSDTDataCollection> sdt = (id<GXSDTDataCollection>)annotationsValue;
			NSMutableArray *files = [NSMutableArray arrayWithCapacity:sdt.sdtDataCollectionItemsCount];
			for (int i=0; i<sdt.sdtDataCollectionItemsCount; i++) {
				id file = [sdt valueForItemAtIndex:i];
				NSURL *url = [GXEntityHelper urlFromFieldValue:file];
				if (url) {
					NSString *annFileName = [[self storageFolder] stringByAppendingPathComponent:[@"xml" stringByAppendingString:[[url absoluteString]lastPathComponent]]];
					[files addObject:annFileName];
					GXHttpDownloadItem *annItem = [[GXHttpDownloadItem alloc] initWithUrl:url destinationFileName:annFileName];
					[downloadItems addObject:annItem];
				}
			}
			_annotationFiles = files;
		} 		
		[_downloader startWithDownloadItems:downloadItems];
	}
}

- (void)initViewer {
	[[_pdfView view] removeFromSuperview];
	[_pdfView setDelegate:nil];
	
	_info = [[APPDFInformation alloc] initAsMemoryDatabase];
	
	for (NSString *annFile in _annotationFiles) {
		[_info importAnnotationXMLFromStream:[[NSInputStream alloc] initWithFileAtPath:annFile]];
	}
	
	APPDFDocument *document = [[APPDFDocument alloc] initWithPath:_pdfFileName information:_info];
	
	_pdfView = [[APAnnotatingPDFViewController alloc] initWithPDF:document];
	_pdfView.delegate = self;
    _pdfView.view.frame = self.loadedEditorView.bounds;
    [_pdfView fitToWidth];
    _pdfView.view.autoresizingMask = (UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight);
    [self.loadedEditorView addSubview:_pdfView.view];
	
	if (!_pdfView.pdf.information.isProcessed)
    {
        [self performSelectorInBackground:@selector(doProcessing) withObject:nil];
    }
	[_activityIndicator stopAnimating];
}

-(void)doProcessing
{
    [_processor processPDF:_pdfView.pdf];
	[self annotationsToData];
}

- (void)annotationsToData {
	if ([_pdfView.pdf.information exportAnnotationXMLToStream:[[NSOutputStream alloc] initToFileAtPath:_mergedAnnotationsFileName append:NO] includeBookmarks:YES]) {
		[GXEntityHelper updateEntityData:self.entityData
					  forFieldDescriptor:_userAnnotationsFieldDescriptor
				  indexedFfieldSpecifier:nil
					   resolvedFieldInfo:[_userAnnotationsFieldDescriptor entityDataFieldInfo]
							   withValue:[[NSURL fileURLWithPath:_mergedAnnotationsFileName] absoluteString] ];
	}
}

- (void)executeMethod:(NSString *)methodName withParameters:(NSArray *)parameters {
	if ([methodName isEqualToString:@"addannotationoftype"]) {
		[_pdfView addAnnotationOfType:[[parameters objectAtIndex:0] intValue]];
	} else if ([methodName isEqualToString:@"writeannotationstopdf"]) {
		[_processor writePDFWithAnnotations:_pdfView.pdf toPath:_pdfFileName options:nil];
	} else if ([methodName isEqualToString:@"removeannotations"]) {
		[_pdfView removeAllAnnotations];
	}else if ([methodName isEqualToString:@"finishcurrentannotation"]) {
		[_pdfView finishCurrentAnnotation];
		[self annotationsToData];
	} else {
		[super executeMethod:methodName withParameters:parameters];
	}
}


- (void)setReadOnly:(BOOL)readOnly {
}

- (UIView *)newEditorViewWithFrame:(CGRect)frame {
	if (!_library) {
		_library = [[APLibrary alloc] initWithLicenseKey:[[self properties] getPropertyValueString:@"@SDiAnnotateLicenseKey"] dataFolder:nil];
	}
	UIView *editorView = [[UIView alloc] initWithFrame:frame];
	editorView.backgroundColor = [UIColor scrollViewTexturedBackgroundColor];
	[editorView setAutoresizesSubviews:YES];
	
	_activityIndicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
	[_activityIndicator setFrame:frame];
	[_activityIndicator setAutoresizingMask:UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth];
	[_activityIndicator setHidesWhenStopped:YES];
	[editorView addSubview:_activityIndicator];
	return editorView;
}

#pragma mark - APPDFProcessorDelegate
	
-(void)pdfProcessor:(APPDFProcessor *)processor didWritePDFWithAnnotations:(APPDFDocument *)pdf toPath:(NSString *)path; {
	[self updateEntityDataResolvedFieldWithValue:path];
}

#pragma mark - APPDFViewDelegate	

-(void)pdfController:(APAnnotatingPDFViewController *)controller didEndAnnotationMode:(APAnnotationType)type {
	[self annotationsToData];
	GXUserInterfaceContext *uiContext = [GXUserInterfaceContext new];
	[uiContext addUserInterfaceContextWithLayoutControl:self];
	[self fireControlEvent:@"AnnotationAdded" userInterfaceContext:uiContext];
}

#pragma mark - GXHttpDownloader delegate

- (void)gxHttpDownloader:(GXHttpDownloader *)downloader didFinishDownloadAllItemsWithError:(NSError *)error {
	if (!error) {
		[self initViewer];
	} else {
		NSLog(@"%@",error);
	}
}

@end
