//
//  GXUC_SDCharts.m
//  UCChart
//
//  Created by Pablo Musso on 10/19/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXUC_SDCharts.h"
#import "GXUCDateHelper.h"
#import "SDChartsBarView.h"
#import "SDChartsPieView.h"
#import "SDChartsTimeLineView.h"

@interface GXUC_SDCharts () <GXParseJsonOperationDelegate>

@end

@implementation GXUC_SDCharts {
	NSOperationQueue *_operationQueue;
}

@synthesize seriesAttributeCollection = _seriesAttributeCollection;
@synthesize seriesTitlesCollection = _seriesTitlesCollection;
@synthesize sdtAttributeOrVariableName = _sdtAttributeOrVariableName;
@synthesize labelThemeClass = _labelThemeClass;
@synthesize backgroundThemeClass = _backgroundThemeClass;

#pragma mark - Construcion-Destruction

- (id)initWithLayoutElement:(id<GXLayoutElement>)layoutElement
             dataDescriptor:(id<GXEntityDataDescriptor>)dataDescriptor
     businessComponentLevel:(GXBusinessComponentLevel *)businessComponentLevel
                  controlId:(NSUInteger)controlId
                     gxMode:(GXModeType)modeType
               indexerStack:(NSArray *)indexer
			  parentControl:(id<GXControlContainer>)parentControl
				  relations:(NSDictionary *)relationsByDataElementName
{
    self = [super initWithLayoutElement:layoutElement
                         dataDescriptor:dataDescriptor
                 businessComponentLevel:businessComponentLevel
                              controlId:controlId
                                 gxMode:modeType
                           indexerStack:indexer
						  parentControl:parentControl
							  relations:relationsByDataElementName
			];
    
    if (self) {
        // Initialization code
		_operationQueue = [[NSOperationQueue alloc] init];
        _seriesAttributeCollection = nil;
        _seriesTitlesCollection = nil;
        _xFieldSpecifier = nil;
        _commentsFieldSpecifier = nil;
        _seriesFieldSpecifierCollection = nil;
        _interfaceOrientationWasLoaded = [self interfaceOrientation];
    }
    return self;
}

- (void)dealloc {
	[_operationQueue cancelAllOperations];
	SDChartsMainView *chart = [self chartView];
	if (chart) {
		[chart setDataSource:nil];
		[chart setDelegate:nil];
	}
}

#pragma mark - Properties Loaders

- (NSString *)getAttributeFromDataProviderByType:(NSArray *)fieldDesc type:(GXDataType)gxType getFirst:(BOOL)getFirst  {
    // Precondicion ==> Existe al menos un atributo de tipo Date o DateTime
    
    NSString * firstAtt = @"";
    BOOL isFirst = YES;
    
	for (id <GXEntityDataFieldDescriptor> tempobject in fieldDesc) {
		GXDataType datatype = [[tempobject entityDataFieldInfo] entityDataFieldInfoDataType];
        if (datatype == gxType) {
            if (getFirst)
                return [tempobject entityDataFieldName];
            else {
                if (isFirst) {
                    firstAtt = [tempobject entityDataFieldName];
                    isFirst = NO;
                }
                else {
                    return [tempobject entityDataFieldName];
                }
            }
        }
    }
	
    if (getFirst)
        return nil; // Nunca deberia pasar de acuerdo a la precondicion
    
    if (isFirst)
        return nil;
    else
        return firstAtt;
}

- (GXUCChartType)readChartTypeFromProperties {
    NSString *chartTypeString = [[self properties] getPropertyValueString:@"@SDChartsChartType"];
    GXUCChartType chartType = GXUCChartTypeTimeLine;
    if ([[chartTypeString uppercaseString] isEqualToString:@"PIE"])
        chartType = GXUCChartTypePie;
    else if ([[chartTypeString uppercaseString] isEqualToString:@"BAR"])
        chartType = GXUCChartTypeBar;
    
    return chartType;
}

- (void)loadPropertiesTimeLineChart {
    if (_datasourceType == GXUCChartDatasourceAttributeList)
        [self loadPropertiesTimeLineChartFromAttList];
    else
        [self loadPropertiesTimeLineChartFromSDT];
}

- (void)loadPropertiesTimeLineChartMinimalProp {
    NSString *xAxisPositionString = [[self properties] getPropertyValueString:@"@SDChartsXAxisPosition"];
    if ([[xAxisPositionString uppercaseString] isEqualToString:@"BOTTOM"])
        _xAxisPosition = Bottom;
    else
        _xAxisPosition = Top;
    
    NSString *yAxisPositionString = [[self properties] getPropertyValueString:@"@SDChartsYAxisPosition"];
    if ([[yAxisPositionString uppercaseString] isEqualToString:@"LEFT"])
        _yAxisPosition = Left;
    else
        _yAxisPosition = Right;
    
    // Time Period
    NSString *timePeriodCollectionString = [[self properties] getPropertyValueString:@"@SDChartsTimePeriodCollection"];
    if  (timePeriodCollectionString) {
        timePeriodCollectionString = [timePeriodCollectionString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    }
    else
    {
        timePeriodCollectionString = @"1w, 1m, 3m, 6m, 1y";
    }
    
    timePeriodCollectionString = [timePeriodCollectionString stringByReplacingOccurrencesOfString:@"," withString:@" "];
    _timePeriodCollection = [timePeriodCollectionString componentsSeparatedByString:@" "];
}

- (void)loadPropertiesTimeLineChartFromAttList {
    NSArray* fieldDesc = [self.dataDescriptor entityDataFieldDescriptors];
    
    // TimeAttribute
    NSString *kbobjRefStr = [[self properties] getPropertyValueString:@"@SDChartsTimeAttribute"];
	if (kbobjRefStr && ![kbobjRefStr isEqualToString:@"(none)"]) {
        _xAttribute = kbobjRefStr;
	}
    else {
        _xAttribute = [self getAttributeFromDataProviderByType:fieldDesc type:GXDataTypeDate getFirst:YES];
		if (_xAttribute == nil) {
            _xAttribute = [self getAttributeFromDataProviderByType:fieldDesc type:GXDataTypeDateTime getFirst:YES];
		}
    }
    
    // Series Attribute Collection
    NSString *seriesAttributeCollectionString = [[self properties] getPropertyValueString:@"@SDChartsSeriesAttributeCollection"];
    if (seriesAttributeCollectionString && ![seriesAttributeCollectionString isEqualToString:@"(none)"]) {
        seriesAttributeCollectionString = [seriesAttributeCollectionString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        _seriesAttributeCollection = [seriesAttributeCollectionString componentsSeparatedByString:@","];
    }
    else {
        _seriesAttributeCollection = [self seriesAttributesFromDataProvider:fieldDesc];
	}
	
    // Series Label Collection
	_seriesTitlesCollection = nil;
    NSString *seriesLabelCollectionString = [[self properties] getPropertyValueString:@"@SDChartsSeriesLabelCollection"];
    if (seriesLabelCollectionString)  {
        seriesLabelCollectionString = [seriesLabelCollectionString stringByTrimmingCharactersInSet:[NSCharacterSet
                                                                                                    whitespaceAndNewlineCharacterSet]];
        if ([seriesLabelCollectionString length]) {
			_seriesTitlesCollection = [seriesLabelCollectionString componentsSeparatedByString:@","];
		}
    }
    
	if (_seriesTitlesCollection == nil) {
		_seriesTitlesCollection = [_seriesAttributeCollection copy];
	}
	
    [self loadPropertiesTimeLineChartMinimalProp];
}


-(void)loadPropertiesTimeLineChartFromSDT
{
    // TimeAttribute
    _xAttribute = [[self properties] getPropertyValueString:@"@SDChartsTimeAttribute"];
    
    _xFieldSpecifier =[[self properties] getPropertyValueString:@"@SDChartsTimeField"];
    
    
    // Series Attribute Collection
    NSString *seriesAttribute = [[self properties] getPropertyValueString:@"@SDChartsSeriesAttributeCollection"];
	if (seriesAttribute) {
		_seriesAttributeCollection = [[NSArray alloc] initWithObjects:seriesAttribute, nil];
	}
    
	_seriesFieldSpecifierCollection = nil;
    NSString *seriesFieldCollectionString = [[self properties] getPropertyValueString:@"@SDChartsSeriesFieldCollection"];
    if (seriesFieldCollectionString)  {
        seriesFieldCollectionString = [seriesFieldCollectionString stringByTrimmingCharactersInSet:[NSCharacterSet
                                                                                                    whitespaceAndNewlineCharacterSet]];
        if ([seriesFieldCollectionString length]) {
			_seriesFieldSpecifierCollection = [seriesFieldCollectionString componentsSeparatedByString:@","];
		}
    }
    
    
    // Series Label Collection
	_seriesTitlesCollection = nil;
    NSString *seriesLabelCollectionString = [[self properties] getPropertyValueString:@"@SDChartsSeriesLabelCollection"];
    
    if (!seriesLabelCollectionString)
    {
        if (seriesFieldCollectionString)
            seriesLabelCollectionString = [seriesFieldCollectionString stringByReplacingOccurrencesOfString:@"item(0)." withString:@""];
        
    }
    
    
    if (seriesLabelCollectionString)  {
        seriesLabelCollectionString = [seriesLabelCollectionString stringByTrimmingCharactersInSet:[NSCharacterSet
                                                                                                    whitespaceAndNewlineCharacterSet]];
        if ([seriesLabelCollectionString length]) {
			_seriesTitlesCollection = [seriesLabelCollectionString componentsSeparatedByString:@","];
		}
    }
    
    
    [self loadPropertiesTimeLineChartMinimalProp];
    
}

- (void)loadPropertiesPieChart {
    [self loadPropertiesPieChartAttListCommonProps];
    if (_datasourceType == GXUCChartDatasourceAttributeList)
        [self loadPropertiesPieChartAttList];
    else
        [self loadPropertiesPieChartSDT];
}

- (void)loadPropertiesPieChartSDT {
    // CategoryAttribute
    _xAttribute = [[self properties] getPropertyValueString:@"@SDChartsCategoryAttribute"];

    _xFieldSpecifier =[[self properties] getPropertyValueString:@"@SDChartsCategoryField"];
    
    // ValueAttribute
    NSString *valueAttribute = [[self properties] getPropertyValueString:@"@SDChartsValueAttribute"];
	if (valueAttribute) {
		_seriesAttributeCollection = [[NSArray alloc] initWithObjects:valueAttribute, nil];
	}
    
    NSString *valueField = [[self properties] getPropertyValueString:@"@SDChartsValueField"];
	if (valueField) {
		_seriesFieldSpecifierCollection = [[NSArray alloc] initWithObjects:valueField, nil];
	}
    
    
    // Comments Attribute
    _commentsAttribute = [[self properties] getPropertyValueString:@"@SDChartsCommentsAttribute"];
    
    _commentsFieldSpecifier = [[self properties] getPropertyValueString:@"@SDChartsCommentsField"];
}


- (void)loadPropertiesPieChartAttListCommonProps {
    // Percentage
    _showInPercentage = YES;
    NSString *showInPercentageStr = [[self properties] getPropertyValueString:@"@SDChartsShowinPercentage"];
    if (showInPercentageStr) {
        _showInPercentage = [[showInPercentageStr uppercaseString] isEqualToString:@"TRUE"];
    }
}

- (NSArray *)seriesAttributesFromDataProvider:(NSArray *)fieldDesc {
    // Precondicion ==> Existe al menos un atributo de tipo Numerico
	NSMutableArray *fieldNames_ = [[NSMutableArray alloc] initWithCapacity:[fieldDesc count]];
	for (id <GXEntityDataFieldDescriptor> tempobject in fieldDesc) {
		GXDataType datatype =[[tempobject entityDataFieldInfo] entityDataFieldInfoDataType];
        if (datatype == GXDataTypeNumeric)
            [fieldNames_ addObject:[tempobject entityDataFieldName]];
	}
	NSArray *fieldNames = [fieldNames_ copy];
    return fieldNames;
}

- (void)loadPropertiesPieChartAttList {
    NSArray* fieldDesc = [self.dataDescriptor entityDataFieldDescriptors];
    
    // CategoryAttribute
    NSString *kbobjRefStrCatAtt = [[self properties] getPropertyValueString:@"@SDChartsCategoryAttribute"];
    if (![kbobjRefStrCatAtt isEqualToString:@"(none)"]) {
        _xAttribute = kbobjRefStrCatAtt;
    }
    else {
        _xAttribute = [self getAttributeFromDataProviderByType:fieldDesc type:GXDataTypeCharacter getFirst:YES];
        
        if (_xAttribute == nil)
            _xAttribute = [self getAttributeFromDataProviderByType:fieldDesc type:GXDataTypeLongVarchar getFirst:YES];
        
        if (_xAttribute == nil)
            _xAttribute = [self getAttributeFromDataProviderByType:fieldDesc type:GXDataTypeVarchar getFirst:YES];
    }
    
    // ValueAttribute
    NSString *kbobjRefStrValAtt = [[self properties] getPropertyValueString:@"@SDChartsValueAttribute"];
    
    NSString* valueAttribute = nil;
    if (![kbobjRefStrValAtt isEqualToString:@"(none)"])
        valueAttribute = kbobjRefStrValAtt;
    else
        valueAttribute = [self getAttributeFromDataProviderByType:fieldDesc type:GXDataTypeNumeric getFirst:YES];
	
	if (valueAttribute) {
		_seriesAttributeCollection = [[NSArray alloc] initWithObjects:valueAttribute, nil];
	}
    
    // Comments Attribute
    NSString *kbobjRefStrCommentsAtt = [[self properties] getPropertyValueString:@"@SDChartsCommentsAttribute"];
    if (![kbobjRefStrCommentsAtt isEqualToString:@"(none)"])
        _commentsAttribute = kbobjRefStrCommentsAtt;
    else
    {
        _commentsAttribute = [self getAttributeFromDataProviderByType:fieldDesc type:GXDataTypeCharacter getFirst:NO];
        if (_commentsAttribute == nil) {
            _commentsAttribute = [self getAttributeFromDataProviderByType:fieldDesc type:GXDataTypeLongVarchar getFirst:NO];
			if (_commentsAttribute == nil) {
				_commentsAttribute = [self getAttributeFromDataProviderByType:fieldDesc type:GXDataTypeVarchar getFirst:NO];
			}
		}
    }
}

#pragma mark - Overrides

- (UIView *)newGridViewWithFrame:(CGRect)frame {
    _datasourceType = [self calculateDataSourceType];
    
    GXUCChartType chartType = [self readChartTypeFromProperties];
	SDChartsMainView *chart = nil;
    if (chartType == GXUCChartTypePie) {
        chart = [[SDChartsPieView alloc] initWithFrame:frame];
        [(SDChartsPieView *)chart setPieDataSource:self];
        [self loadPropertiesPieChart];
        [(SDChartsPieView *)chart setPieProperties:[self calculatePieProperties]];
    }
    else if (chartType == GXUCChartTypeBar) {
        chart = [[SDChartsBarView alloc] initWithFrame:frame];
    }
    else {
        chart = [[SDChartsTimeLineView alloc] initWithFrame:frame];
        [self loadPropertiesTimeLineChart];
        [(SDChartsTimeLineView *)chart setSeriesTitlesCollection:_seriesTitlesCollection];
    }
    
	[chart setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight];
    
    [chart setDelegate:self];
    [chart setDataSource:self];
    
    [chart setBackgroundThemeClass:_backgroundThemeClass];
    [chart setLabelThemeClass:_labelThemeClass];
    
    return chart;
}

- (void)unloadView {
	SDChartsMainView *chart = [self chartView];
	if (chart) {
		[chart setDataSource:nil];
		[chart setDelegate:nil];
	}
	[super unloadView];
}

- (SDChartsMainView *) chartView {
    return (SDChartsMainView *) [self gridView];
}

- (void)layoutControls {
	[super layoutControls];
    if (!CGSizeEqualToSize(self.frame.size, CGSizeZero))  {
        [[self chartView] renderControl];
    }
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    [super willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
    [[self chartView] willRotateToInterfaceOrientation:toInterfaceOrientation interfaceOrientationWasLoaded:_interfaceOrientationWasLoaded];
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [super didRotateFromInterfaceOrientation:fromInterfaceOrientation];
    [[self chartView] didRotateFromInterfaceOrientation:fromInterfaceOrientation] ;
}

- (void)reloadData {
	[self loadData];
}

#pragma mark - Data Loaders

- (void)loadMoreIfRequired {
	if ([self.entityDataListProvider isLoading])
		return;
	
	NSInteger totalCount = [self.entityDataListProvider totalCount];
	switch (totalCount) {
		case kTotalCountUnknownLocal:
		case kTotalCountUnknownRemote:
		case kTotalCountUnknownPendingRemote:
		{
			if (self.autoLoad) {
				[self.entityDataListProvider load];
			}
			break;
		}
		default:
			break;
	}
}

- (void)loadDataFromAnnTimeLineSDTTimeLine {
	NSString *json = nil;
	if ([self.entityDataListProvider numberOfLoadedSections] && [self.entityDataListProvider numberOfLoadedEntitiesInSection:0] && _xAttribute) {
		id <GXEntityData> data = [self.entityDataListProvider entityDataAtIndex:0 section:0];
		json = [data valueForEntityDataFieldName:_xAttribute];
	}
	
	if (json) {
		[self loadPropertiesFromTimeLineSDT:json];
	}
	else {
		[self loadChartDataSDT:nil];
	}
}

- (void)loadData {
    [self loadMoreIfRequired];
    
    GXUCChartType chartType = [[self chartView] chartType];
    
    if (chartType == GXUCChartTypePie) {
        NSMutableArray *chartDataSDT = [[NSMutableArray alloc] init];
        NSUInteger sectionsCount = [self.entityDataListProvider numberOfLoadedSections];

		id <GXEntityDataFieldDescriptor> xFieldDesc = nil, commentsFieldDesc = nil, seriesFieldDesc = nil;
        for (NSUInteger sectionIndex = 0; sectionIndex < sectionsCount; sectionIndex++) {
            NSUInteger entitiesInSectionCount = [self.entityDataListProvider numberOfLoadedEntitiesInSection:sectionIndex];
            for (NSUInteger i = 0; i < entitiesInSectionCount; i++) {

                id <GXEntityDataWithOverrideValues> entityData = nil;
                NSArray *indexer = nil;

                GXPieChartPoint *p = [[GXPieChartPoint alloc] init];
                p.valueXAttribute = [super valueForEntityDataFieldName:_xAttribute
                                                        fieldSpecifier:_xFieldSpecifier
													  forEntityAtIndex:i
															   section:sectionIndex
														   enitityData:&entityData
													   fieldDescriptor:&xFieldDesc
															   indexer:&indexer
												 indexedFieldSpecifier:NULL];
                if ((_commentsAttribute != nil)) {
                    p.valueCommentsAttribute = [super valueForEntityDataFieldName:_commentsAttribute
                                                                   fieldSpecifier:_commentsFieldSpecifier
																 forEntityAtIndex:i
																		  section:sectionIndex
																	  enitityData:&entityData
																  fieldDescriptor:&commentsFieldDesc
																		  indexer:&indexer
															indexedFieldSpecifier:NULL];
                }
                else {
                    p.valueCommentsAttribute = p.valueXAttribute;
                }

                if ((_seriesFieldSpecifierCollection != nil) && ([_seriesFieldSpecifierCollection count] > 0))
                    p.valueSerieAttribute = [super valueForEntityDataFieldName:[_seriesAttributeCollection objectAtIndex:0]
                                                                fieldSpecifier:[_seriesFieldSpecifierCollection objectAtIndex:0]
															  forEntityAtIndex:i
																	   section:sectionIndex
																   enitityData:&entityData
															   fieldDescriptor:&seriesFieldDesc
																	   indexer:&indexer
														 indexedFieldSpecifier:NULL];
                else {
                    p.valueSerieAttribute = [super valueForEntityDataFieldName:[_seriesAttributeCollection objectAtIndex:0]
                                                                fieldSpecifier:nil
															  forEntityAtIndex:i
																	   section:sectionIndex
																   enitityData:&entityData
															   fieldDescriptor:&seriesFieldDesc
																	   indexer:&indexer
														 indexedFieldSpecifier:NULL];
                }
            
                [chartDataSDT addObject:p];
            }
        }
        
        [self loadChartDataSDT:chartDataSDT];
    }
    else {
        if (_datasourceType == GXUCChartDatasourceSDT) {
			if ((_xFieldSpecifier == nil) || (_seriesFieldSpecifierCollection == nil))
				[self loadDataFromAnnTimeLineSDTTimeLine];
			else
				[self loadDataFromSDTTimeLine];
        }
        else {
            [self loadDataFromAttListTimeLine];
        }
    }
}

- (void)loadDataFromSDTTimeLine {
	NSMutableArray *chartDataSDT = [[NSMutableArray alloc] init];
	NSUInteger seriesCount = [_seriesFieldSpecifierCollection count];
	
	NSString *dataKey = @"Data";
	NSString *nameKey = @"Name";
	NSString *xValueKey = @"XValue";
	NSString *yValueKey = @"YValue";
	
	for (int j = 0; j <seriesCount; j++) {
		NSString* serieName = [_seriesTitlesCollection objectAtIndex:j];
		NSMutableArray* serieData = [[NSMutableArray alloc] init];
		NSDictionary* serieSpec = [[NSDictionary alloc] initWithObjectsAndKeys:
								   serieData, dataKey,
								   serieName, nameKey,
								   nil];
		
		[chartDataSDT addObject:serieSpec];
	}
    
    NSUInteger entitiesInSectionCount = [[self entityDataListProvider] numberOfLoadedEntitiesInSection:0];
    if (entitiesInSectionCount > 0) {
		NSDateFormatter *dateFormatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:@"yyyy-MM-dd HH:mm:ss"];

		id <GXEntityDataFieldDescriptor> xFieldDesc = nil, seriesFieldDesc = nil;
        
        for (int i = 0; i < entitiesInSectionCount; i++) {
			id <GXEntityDataWithOverrideValues> entityData = nil;
			NSArray *indexer = nil;
            NSDate* dateValue;
            dateValue = [super valueForEntityDataFieldName:_xAttribute
                                            fieldSpecifier:_xFieldSpecifier
										  forEntityAtIndex:i
												   section:0
											   enitityData:&entityData
										   fieldDescriptor:&xFieldDesc
												   indexer:&indexer
									 indexedFieldSpecifier:NULL];
            
            NSString* dateValueStr = [dateFormatter stringFromDate:dateValue];
            
            for (int j = 0; j < seriesCount; j++) {
                NSDictionary* serieSpec = [chartDataSDT objectAtIndex:j];
                
                id serieValue = [super valueForEntityDataFieldName:[_seriesAttributeCollection objectAtIndex:0]
                                                    fieldSpecifier:[_seriesFieldSpecifierCollection objectAtIndex:j]
												  forEntityAtIndex:i
														   section:0
													   enitityData:&entityData
												   fieldDescriptor:&seriesFieldDesc
														   indexer:&indexer
											 indexedFieldSpecifier:NULL];
                
                NSDictionary* serieItem = [[NSDictionary alloc] initWithObjectsAndKeys:
                                           dateValueStr, xValueKey,
                                           serieValue, yValueKey,
                                           nil];
                NSMutableArray* sData = [serieSpec objectForKey:dataKey];
                [sData addObject:serieItem];
            }
        }
    }
    
	[self loadChartDataSDT:chartDataSDT];
}

- (void)loadDataFromAttListTimeLine {
	NSMutableArray *chartDataSDT = [[NSMutableArray alloc] init];
	NSMutableDictionary* auxSeriesDic = [[NSMutableDictionary alloc] init];
    NSMutableDictionary* auxSeriesDic_2 = [[NSMutableDictionary alloc] init];
	
	NSUInteger seriesCount = [_seriesTitlesCollection count];
	
	NSString *dataKey = @"Data";
	NSString *nameKey = @"Name";
	NSString *xValueKey = @"XValue";
	NSString *yValueKey = @"YValue";
	
	for (int j = 0; j <seriesCount; j++) {
		NSString* serieName = [_seriesTitlesCollection objectAtIndex:j];
		NSString* fieldName = [_seriesAttributeCollection objectAtIndex:j];
		
		NSMutableArray* serieData = [[NSMutableArray alloc] init];
		NSDictionary* serieSpec = [[NSDictionary alloc] initWithObjectsAndKeys:
								   serieData, dataKey,
								   serieName, nameKey,
								   nil];
		
		[chartDataSDT addObject:serieSpec];
		
		[auxSeriesDic setObject:serieName forKey:fieldName];
        [auxSeriesDic_2 setObject:fieldName forKey:serieName];
	}
	
	NSUInteger sectionsCount = [self.entityDataListProvider numberOfLoadedSections];
	if (sectionsCount) {
		NSDateFormatter *dateFormatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:@"yyyy-MM-dd HH:mm:ss"];
		
		for (NSUInteger sectionIndex = 0; sectionIndex < sectionsCount; sectionIndex++) {
			NSUInteger entitiesInSectionCount = [self.entityDataListProvider numberOfLoadedEntitiesInSection:sectionIndex];
			for (int i = 0; i < entitiesInSectionCount; i++) {
				id <GXEntityDataWithOverrideValues> entityData = nil;
				NSDate* dateValue;
				dateValue = [self valueForEntityDataFieldName:_xAttribute
											   fieldSpecifier:nil
											 forEntityAtIndex:i
													  section:sectionIndex
												  enitityData:&entityData
											  fieldDescriptor:NULL
													  indexer:NULL
										indexedFieldSpecifier:NULL];
				
				NSString *dateValueStr = [dateFormatter stringFromDate:dateValue];
				
				for (int j = 0; j < seriesCount; j++) {
					NSDictionary* serieSpec = [chartDataSDT objectAtIndex:j];
					
					NSString *sName = [serieSpec objectForKey:nameKey];
					NSString *fName = [auxSeriesDic objectForKey:sName];
                    
                    if (fName == nil) {
                        fName = [auxSeriesDic_2 objectForKey:sName];
                    }
                    
					id serieValue = [self valueForEntityDataFieldName:fName
													   fieldSpecifier:nil
													 forEntityAtIndex:i
															  section:sectionIndex
														  enitityData:&entityData
													  fieldDescriptor:NULL
															  indexer:NULL
												indexedFieldSpecifier:NULL];
					
					NSDictionary* serieItem = [[NSDictionary alloc] initWithObjectsAndKeys:
											   dateValueStr, xValueKey,
											   serieValue, yValueKey,
											   nil];
					NSMutableArray* sData = [serieSpec objectForKey:dataKey];
					[sData addObject:serieItem];
				}
			}
		}
	}
    
	[self loadChartDataSDT:chartDataSDT];
}

- (GXUCChartDatasourceType)calculateDataSourceType {
    GXUCChartDatasourceType dsType = GXUCChartDatasourceAttributeList;
    
    NSArray *fieldDesc = [self.dataDescriptor entityDataFieldDescriptors];
    
    NSUInteger count = [fieldDesc count];
    for (id <GXEntityDataFieldDescriptor> desc in fieldDesc) {
        if ([[desc entityDataFieldName] compare:@"gxdynprop" options:NSCaseInsensitiveSearch] == NSOrderedSame) {
            count -= 1;
        }
    }
    
    if (count == 1) {
        id <GXEntityDataFieldDescriptor> entityDataFieldDesc = [fieldDesc objectAtIndex:0];
        GXDataType dataType = [entityDataFieldDesc.entityDataFieldInfo entityDataFieldInfoDataType];
        if (dataType == GXDataTypeSDT) {
            dsType = GXUCChartDatasourceSDT;
        }
    }

    return dsType;
}

- (GXPieChartProperties *)calculatePieProperties {
    GXPieChartProperties *pieProperties = [[GXPieChartProperties alloc] init];
    
    id <GXEntityDataFieldDescriptor> ySerieDesc = [self.dataDescriptor entityDataFieldDescriptorForName:[_seriesAttributeCollection objectAtIndex:0]];
    if (ySerieDesc != nil) {
        // Precondicion el attributo asociado al FieldDescriptor es de tipo Numerico
        NSString* picture = [[ySerieDesc entityDataFieldInfo]entityDataFieldInfoInputPicture];
        
        if ([picture isEqualToString:@""]) {
            pieProperties.numberOfDecimals = [[ySerieDesc entityDataFieldInfo] entityDataFieldInfoDecimals];
        }
        
        NSRange aRange = [picture rangeOfString:@"."];
        if (aRange.location == NSNotFound) {
            pieProperties.numberOfDecimals = 0;
        }
        else {
            NSArray* pictureArr = [picture componentsSeparatedByString:@"."];
            
            if ([pictureArr count] <= 1) {
                pieProperties.numberOfDecimals = 0;
            }
            else {
                NSString* decimalStr = (NSString*) [pictureArr objectAtIndex:1];
                pieProperties.numberOfDecimals = [decimalStr length];
            }
        }
    }
    
    pieProperties.existCommentAttribute = _commentsAttribute != nil && [_xAttribute compare:_commentsAttribute options:NSCaseInsensitiveSearch] != NSOrderedSame;
    
    return pieProperties;
}

- (void)loadChartDataSDT:(NSArray *)data {
	_chartDataSDT = data ? : [[NSArray alloc] init];
	
	if ([self isViewLoaded]) {
		[[self chartView] setChartDataSDT:_chartDataSDT];
		[[self chartView] refreshChart];
	}
}


- (void)loadPropertiesFromTimeLineSDT:(NSString*) json; {
	GXParseJsonOperation *parseJsonOp = [[GXParseJsonOperation alloc] init];
	parseJsonOp.data = [json dataUsingEncoding:NSUTF8StringEncoding];
	[parseJsonOp setDelegate:self];
	
	[_operationQueue cancelAllOperations];
	[_operationQueue addOperation:parseJsonOp];
}

#pragma mark GXParseJsonOperationDelegate

- (void)parseJsonOperationDidFinishParsing:(GXParseJsonOperation *)op {
	if ([op isCancelled]) return;
	if (![NSThread isMainThread]) {
		[self performSelectorOnMainThread:_cmd
							   withObject:op
							waitUntilDone:YES];
		return;
	}
	
	[_operationQueue cancelAllOperations];
	NSArray *parsedData = nil;
	if ([op.parsedData isKindOfClass:[ NSArray class]]) {
		parsedData = op.parsedData;
	}
	[self loadChartDataSDT:parsedData];
}

- (void)parseJsonOperationDidFailParsing:(GXParseJsonOperation *)op {
	if ([op isCancelled]) return;
	if (![NSThread isMainThread]) {
		[self performSelectorOnMainThread:_cmd
							   withObject:op
							waitUntilDone:YES];
		return;
	}
	
	// Ver que error mostrar en la interfaz
	// [self drawErrorView:[op.error description]];
	[_operationQueue cancelAllOperations];
	[self loadChartDataSDT:nil];
}

#pragma mark - SDChartsViewDelegate

#pragma mark - SDChartsViewDataSource

- (void)chartsViewRefresh:(SDChartsMainView *)chartView {
    if (_datasourceType == GXUCChartDatasourceAttributeList) {
        [[self entityDataListProvider] refresh];
    }
}

- (NSString *)chartsViewTitle:(SDChartsMainView *)chartView {
    if (!_graphTitle) {
        NSString *chartTitle = [[self properties] getPropertyValueString:@"@SDChartsTitle"];
        NSString* chartTitleTranslation = [GXResources translationFor:chartTitle];
        if (chartTitleTranslation != nil) {
            NSString *chartTitleTrimmed = [chartTitleTranslation stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
            _graphTitle = chartTitleTrimmed;
        }
        else {
            _graphTitle = @"";
        }
    }
    
    return _graphTitle;
}

- (BOOL)chartsViewShowInPercentaje:(SDChartsMainView *)chartView {
    return _showInPercentage;
}

- (GXUCChartDatasourceType)chartsViewDataSourceType:(SDChartsMainView *)chartView {
    return _datasourceType;
}

- (AxisPosition)chartsViewXAxisPossition:(SDChartsMainView *)chartView {
    return _xAxisPosition;
}

- (AxisPosition)chartsViewYAxisPossition:(SDChartsMainView *)chartView {
    return _yAxisPosition;
}

- (NSArray *)chartsViewTimePeriodCollection:(SDChartsMainView *)chartView {
    return _timePeriodCollection;
}

#pragma mark - SDChartsPieViewDataSource

- (NSUInteger)pieChartNumberOfSlices:(SDChartsPieView *)pieView {
    return [_chartDataSDT count];
}

- (GXPieChartPoint *)pieChart:(SDChartsPieView *)pieView pointAtIndex:(NSUInteger)index {
    return [_chartDataSDT objectAtIndex:index];
}

- (double)pieChartTotal:(SDChartsPieView *)pieView {
    NSNumber *total = [_chartDataSDT valueForKeyPath:@"@sum.valueSerieAttribute"];
    return [total doubleValue];
}

- (NSArray *)pieChartSortedCategoryNamesByValue:(SDChartsPieView *)pieView {
    NSArray *sortedArray = [_chartDataSDT sortedArrayUsingComparator:^NSComparisonResult(GXPieChartPoint *point1, GXPieChartPoint *point2) {
        NSComparisonResult result = [point2.valueSerieAttribute compare:point1.valueSerieAttribute];
        return result;
    }];
    NSArray *catNames = [sortedArray map:^id(GXPieChartPoint *point) {
        return point.valueXAttribute;
    }];
    return catNames;
}

@end
