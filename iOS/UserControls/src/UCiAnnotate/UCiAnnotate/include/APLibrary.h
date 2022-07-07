
/*
 *
 * iANNOTATE PDF SDK
 * PROPERTY OF BRANCHFIRE, INC
 * (C) COPYRIGHT 2010-13, ALL RIGHTS RESERVED
 *
 * BRANCHFIRE, INC., CONFIDENTIAL INFORMATION
 *
 * LIBRARY ACCESS AND DISTRIBUTION ARE SUBJECT TO LICENSE AGREEMENT
 * UNAUTHORIZED USE, REPRODUCTION, OR DISTRIBUTION IS PROHIBITED
 *
 * PLEASE CONTACT LIBRARY-SUPPORT@BRANCHFIRE.COM IF YOU HAVE ANY QUESTIONS REGARDING USE OR DISTRIBUTION
 *
 *
 */


#import "APPDF.h"


@class APLibraryPrivate;



/**
 * The driver class for the iAnnotate PDF SDK. This must be created
 * and initialized before using any other API calls.
 *
 * Create this object once per application session, and release it in
 * order to clean up the library and free associated resources. After
 * releasing the APLibrary object in this way, any calls to API
 * methods or use of API objects may fail.
 */
@interface APLibrary : NSObject
{
@private
    APLibraryPrivate *m_private;
}

/**
 * Initialize the iAnnotate PDF SDK. Must be called before any other
 * API calls are made. Release the library to flush any caches and
 * close any allocated resources; it is highly recommended to release
 * the library handle before terminating the application.
 *
 * \param key The license key as provided per your license
 * agreement. Please note that your license file must be included in
 * the main resource bundle of your application, under the name
 * "license.xml". For evaluation versions, pass nil.
 *
 * \param path The full path to a folder where the library should keep
 * its data/cache information. This folder must exist, otherwise the
 * default will be used. Pass <code>nil</code> to use the default,
 * which will be in the Library area.
 */
-(id)initWithLicenseKey:(NSString *)key dataFolder:(NSString *)path;

/**
 * Used to access the version of the PDF SDK.
 */
-(NSString *)versionString;


@end
