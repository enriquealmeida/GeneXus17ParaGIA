/*
               File: reorg
        Description: Table Manager
             Author: GeneXus Java Generator version 15_0_4-112600
       Generated on: March 30, 2017 18:53:2.98
       Program type: Callable routine
          Main DBMS: oracle7
*/
package artech.security.api ;
import artech.security.api.*;
import java.sql.*;
import com.genexus.db.*;
import com.genexus.*;
import com.genexus.util.*;

public final  class reorg extends GXProcedure
{
   public reorg( int remoteHandle )
   {
      super( remoteHandle , new ModelContext( reorg.class ), "" );
   }

   public reorg( int remoteHandle ,
                 ModelContext context )
   {
      super( remoteHandle , context, "" );
   }

   public void execute( )
   {
      execute_int();
   }

   private void execute_int( )
   {
      initialize();
      if ( previousCheck() )
      {
         executeReorganization( ) ;
      }
   }

   private void FirstActions( )
   {
      Status = (byte)(0) ;
      GXv_int1[0] = Status ;
      new artech.security.api.gxrtctls(remoteHandle, context).execute( GXv_int1) ;
      reorg.this.Status = GXv_int1[0] ;
      if ( Status != 0 )
      {
         System.out.println("ERROR");
         System.exit( 6 );
      }
      /* Load data into tables. */
   }

   public void ReorganizeSession( ) throws SQLException
   {
      String cmdBufferCreate = "" ;
      String cmdBuffer = "" ;
      /* Indices for table Session */
      cmdBuffer = " ALTER TABLE \"Session\" ADD (SesOauthScope  NVARCHAR2(1024) , SesOauthTokenExpires ";
      cmdBuffer += "   NUMERIC(6) , SesOauthTokenMaxRenew  NUMERIC(4) , SesAutTypeName  NCHAR(60) , SesExtToken2 ";
      cmdBuffer += "   NVARCHAR2(1024) ) ";
      ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
   }

   public void ReorganizeSessionHistory( ) throws SQLException
   {
      String cmdBufferCreate = "" ;
      String cmdBuffer = "" ;
      /* Indices for table SessionHistory */
      cmdBuffer = " ALTER TABLE SessionHistory ADD (SesHisOauthScope  NVARCHAR2(1024) , SesHisOauthTokenExpires ";
      cmdBuffer += "   NUMERIC(6) , SesHisOauthTokenMaxRenew  NUMERIC(4) , SesHisAppTokenExp  DATE , SesHisDeviceId ";
      cmdBuffer += "   NCHAR(40) , SesHisRefreshToken  NCHAR(40) , SesHisAppId  NUMERIC(12) , SesHisExtToken2 ";
      cmdBuffer += "   NVARCHAR2(1024) ) ";
      ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
   }

   public void ReorganizeApplication( ) throws SQLException
   {
      String cmdBufferCreate = "" ;
      String cmdBuffer = "" ;
      /* Indices for table Application */
      cmdBuffer = " ALTER TABLE Application ADD (AppCliLocalLoginURL  NVARCHAR2(1024) , AppCliAllowGetUserAdditionalDa ";
      cmdBuffer += "   NUMERIC(1) , AppCliAllowGetUserRoles  NUMERIC(1) , AppCliAllowRemoteAuth  NUMERIC(1) ";
      cmdBuffer += "  ) ";
      ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      cmdBuffer = " INSERT INTO Repository (RepId, RepVer, RepUpg, RepDefAutTypeName, RepUserActType, ";
      cmdBuffer += "  RepUserActTimeOut, RepDefSecPolId, RepGenAud, RepUserRemMeTimeOut, RepGenSesSta, ";
      cmdBuffer += "  RepUserChrMinLog, RepGUID, RepCanRegUser, RepUserRemMeType, RepGiveAnoSes, RepAnoUserGUID, ";
      cmdBuffer += "  RepDefRoleId, RepCreDate, RepCreUser, RepUpdDate, RepUpdUser, RepNameSpace, RepUserRecPwdKeyTimeOut, ";
      cmdBuffer += "  RepUserReqPwd, RepUserReqEmail, RepUserReqFirstName, RepUserReqLastName, RepUserReqBir, ";
      cmdBuffer += "  RepUserReqGen, RepUserReqPhone, RepUserReqAddress, RepUserReqCity, RepUserReqState, ";
      cmdBuffer += "  RepUserReqCountry, RepUserReqPostCode, RepUserReqLng, RepUserReqTimeZone, RepUserReqPhoto, ";
      cmdBuffer += "  RepUserAttLogLockUser, RepUserAttLogLockSession, RepDsc, RepUserEmailSecAdm, RepUserTimeToUnblock, ";
      cmdBuffer += "  RepName, RepUserEmailUnique, RepAllowOauthAccess, RepUserIdentification, RepKeepSesOnlyOriIP, ";
      cmdBuffer += "  RepUserSessionCacheTimeout, RepAnoUserSDRoleId) SELECT DISTINCT RepId, N' ', N' ', ";
      cmdBuffer += "  NULL, N'A', 36, NULL, N'None', 0, N'None', 3, N' ', 0, N' ', 0, NULL, NULL, to_date( ";
      cmdBuffer += "  '1-1-1 0:0:0', 'YYYY-MM-DD HH24:MI:SS'), N' ', TO_DATE('0001-01-01', 'YYYY-MM-DD'), ";
      cmdBuffer += "  N' ', N' ', 120, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 10, NULL, NULL, ";
      cmdBuffer += "  30, N' ', 0, 1, N'name', 0, 60, NULL FROM Application T WHERE NOT EXISTS (SELECT ";
      cmdBuffer += "  1 FROM Repository WHERE RepId = T.RepId) ";
      ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      cmdBuffer = " INSERT INTO Application (RepId, AppId, AppName, AppGUID, AppVer, AppComName, AppTypeId, ";
      cmdBuffer += "  AppParAppId, AppDsc, AppCopyright, AppOrder, AppRetMnuOptWithoutPrm, AppCurEnvId, ";
      cmdBuffer += "  AppBaseAppId, AppCreDate, AppCreUser, AppUpdDate, AppUpdUser, AppIsBaseApplication, ";
      cmdBuffer += "  AppCliId, AppCliSecret, AppCliUserGUID, AppCliImageURL, AppCliSiteURL, AppCliSiteDomain, ";
      cmdBuffer += "  AppCliRevoked, AppType, AppCliEncKey, AppCliDefPrmGUID, AppAccessReqPrm, AppCliAccessUniqueByUser, ";
      cmdBuffer += "  AppCliAutoRegAnomymousUser, AppCliAllowRemoteAuth, AppCliAllowGetUserRoles, AppCliAllowGetUserAdditionalDa, ";
      cmdBuffer += "  AppCliLocalLoginURL) SELECT DISTINCT RepId, AppId, AppName, AppGUID, AppVer, AppComName, ";
      cmdBuffer += "  AppTypeId, AppParAppId, AppDsc, AppCopyright, AppOrder, AppRetMnuOptWithoutPrm, ";
      cmdBuffer += "  AppCurEnvId, AppBaseAppId, AppCreDate, AppCreUser, AppUpdDate, AppUpdUser, AppIsBaseApplication, ";
      cmdBuffer += "  AppCliId, AppCliSecret, AppCliUserGUID, AppCliImageURL, AppCliSiteURL, AppCliSiteDomain, ";
      cmdBuffer += "  AppCliRevoked, AppType, AppCliEncKey, AppCliDefPrmGUID, AppAccessReqPrm, AppCliAccessUniqueByUser, ";
      cmdBuffer += "  AppCliAutoRegAnomymousUser, NULL, NULL, NULL, NULL FROM Application T WHERE NOT ";
      cmdBuffer += "  EXISTS (SELECT 1 FROM Application WHERE RepId = T.RepId AND AppId = T.AppId) ";
      ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      cmdBuffer = " INSERT INTO AppEnvironment (RepId, AppId, AppEnvId, AppEnvOrder, AppEnvGUID, AppEnvSrvHost, ";
      cmdBuffer += "  AppEnvSrvPort, AppEnvSrvVirDir, AppEnvSrvHttps, AppEnvSrvPgmPac, AppEnvSrvPgmExt, ";
      cmdBuffer += "  AppEnvName, AppEnvCreDate, AppEnvCreUser, AppEnvUpdDate, AppEnvUpdUser) SELECT DISTINCT ";
      cmdBuffer += "  RepId, AppId, AppCurEnvId, 0, ' ', ' ', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, ";
      cmdBuffer += "  TO_DATE('0001-01-01', 'YYYY-MM-DD'), NULL FROM Application T WHERE NOT EXISTS (SELECT ";
      cmdBuffer += "  1 FROM AppEnvironment WHERE RepId = T.RepId AND AppId = T.AppId AND AppEnvId = T.AppCurEnvId) ";
      cmdBuffer += "  AND (NOT AppCurEnvId IS NULL )  ";
      ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      cmdBuffer = " ALTER TABLE Application MODIFY AppCliSecret  NCHAR(120)   ";
      ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      try
      {
         cmdBuffer = " CREATE INDEX IAPPLICATIONENV ON Application (RepId ,AppId ,AppCurEnvId ) ";
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      }
      catch(SQLException ex)
      {
		try{
			cmdBuffer = " DROP INDEX IAPPLICATIONENV ";
	         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;

			 try{
				cmdBuffer = " DROP INDEX IAPPLICATION1 ";
				ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
 			}catch(SQLException aex1) {}

 		 }catch(SQLException aex1) {
		 
			 try{
				cmdBuffer = " DROP INDEX IAPPLICATION1 ";
				ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
 			}catch(SQLException aex2) {}
		 }

         cmdBuffer = " CREATE INDEX IAPPLICATIONENV ON Application (RepId ,AppId ,AppCurEnvId ) ";
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      }
   }

   public void CreateUserMemRoleProp( ) throws SQLException
   {
      String cmdBufferCreate = "" ;
      String cmdBuffer = "" ;
      /* Indices for table UserMemRoleProp */
      try
      {
         cmdBufferCreate = " CREATE TABLE UserMemRoleProp (UserGUID  NCHAR(40) NOT NULL , RepId  NUMERIC(9) NOT ";
         cmdBufferCreate += "  NULL , UserMemRoleRoleId  NUMERIC(12) NOT NULL , UserMemRolePropId  NCHAR(60) NOT ";
         cmdBufferCreate += "  NULL , UserMemRolePropToken  NCHAR(40) NOT NULL , UserMemRolePropValue  NVARCHAR2(400) ";
         cmdBufferCreate += "  NOT NULL , PRIMARY KEY(UserGUID, RepId, UserMemRoleRoleId, UserMemRolePropId, UserMemRolePropToken)) ";
         cmdBufferCreate += "   ";
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBufferCreate) ;
      }
      catch(SQLException ex)
      {
         try
         {
            cmdBuffer = " DROP TABLE UserMemRoleProp ";
            ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
         }
         catch(SQLException sqlex1)
         {
            try
            {
               cmdBuffer = " DROP VIEW UserMemRoleProp ";
               ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
            }
            catch(SQLException sqlex2)
            {
               try
               {
                  cmdBuffer = " DROP FUNCTION UserMemRoleProp ";
                  ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
               }
               catch(SQLException sqlex3)
               {
               }
            }
         }
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBufferCreate) ;
      }
   }

   public void CreateUserMemAppRoleProp( ) throws SQLException
   {
      String cmdBufferCreate = "" ;
      String cmdBuffer = "" ;
      /* Indices for table UserMemAppRoleProp */
      try
      {
         cmdBufferCreate = " CREATE TABLE UserMemAppRoleProp (UserGUID  NCHAR(40) NOT NULL , RepId  NUMERIC(9) ";
         cmdBufferCreate += "  NOT NULL , UserMemAppRoleAppId  NUMERIC(12) NOT NULL , UserMemAppRoleRoleId  NUMERIC(12) ";
         cmdBufferCreate += "  NOT NULL , UserMemAppRolePropId  NCHAR(60) NOT NULL , UserMemAppRolePropToken  NCHAR(40) ";
         cmdBufferCreate += "  NOT NULL , UserMemAppRolePropValue  NVARCHAR2(400) NOT NULL , PRIMARY KEY(UserGUID, ";
         cmdBufferCreate += "  RepId, UserMemAppRoleAppId, UserMemAppRoleRoleId, UserMemAppRolePropId, UserMemAppRolePropToken)) ";
         cmdBufferCreate += "   ";
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBufferCreate) ;
      }
      catch(SQLException ex)
      {
         try
         {
            cmdBuffer = " DROP TABLE UserMemAppRoleProp ";
            ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
         }
         catch(SQLException sqlex1)
         {
            try
            {
               cmdBuffer = " DROP VIEW UserMemAppRoleProp ";
               ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
            }
            catch(SQLException sqlex2)
            {
               try
               {
                  cmdBuffer = " DROP FUNCTION UserMemAppRoleProp ";
                  ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
               }
               catch(SQLException sqlex3)
               {
               }
            }
         }
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBufferCreate) ;
      }
   }

   public void CreateSecurityPolicyProp( ) throws SQLException
   {
      String cmdBufferCreate = "" ;
      String cmdBuffer = "" ;
      /* Indices for table SecurityPolicyProp */
      try
      {
         cmdBufferCreate = " CREATE TABLE SecurityPolicyProp (RepId  NUMERIC(9) NOT NULL , SecPolId  NUMERIC(9) ";
         cmdBufferCreate += "  NOT NULL , SecPolPropId  NCHAR(60) NOT NULL , SecPolPropToken  NCHAR(40) NOT NULL ";
         cmdBufferCreate += "  , SecPolPropValue  NVARCHAR2(400) NOT NULL , PRIMARY KEY(RepId, SecPolId, SecPolPropId, ";
         cmdBufferCreate += "  SecPolPropToken))  ";
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBufferCreate) ;
      }
      catch(SQLException ex)
      {
         try
         {
            cmdBuffer = " DROP TABLE SecurityPolicyProp ";
            ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
         }
         catch(SQLException sqlex1)
         {
            try
            {
               cmdBuffer = " DROP VIEW SecurityPolicyProp ";
               ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
            }
            catch(SQLException sqlex2)
            {
               try
               {
                  cmdBuffer = " DROP FUNCTION SecurityPolicyProp ";
                  ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
               }
               catch(SQLException sqlex3)
               {
               }
            }
         }
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBufferCreate) ;
      }
   }

   public void ReorganizeUserMemAppRole( ) throws SQLException
   {
      String cmdBufferCreate = "" ;
      String cmdBuffer = "" ;
      /* Indices for table UserMemAppRole */
	  try{
		cmdBuffer = " DROP INDEX IUSERMEMAPPROLE1 ";
		 ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
	  }catch(SQLException aex2) {}

      try
      {
         cmdBuffer = " CREATE INDEX IUSERMEMAPPROLEAPP ON UserMemAppRole (RepId ,UserMemAppRoleAppId ) ";
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      }
      catch(SQLException ex)
      {
		try{
			 cmdBuffer = " DROP INDEX IUSERMEMAPPROLEAPP ";
			 ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
		}catch(SQLException aex3) {}

         cmdBuffer = " CREATE INDEX IUSERMEMAPPROLEAPP ON UserMemAppRole (RepId ,UserMemAppRoleAppId ) ";
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      }
      try
      {
         cmdBuffer = " CREATE INDEX IUSERMEMAPPROLEROLE ON UserMemAppRole (RepId ,UserMemAppRoleRoleId ) ";
         cmdBuffer += "  ";
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      }
      catch(SQLException ex)
      {
	      try
		  {
			 cmdBuffer = " DROP INDEX IUSERMEMAPPROLEROLE ";
			 ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;

			  try
			  {
				 cmdBuffer = " DROP INDEX IUSERMEMAPPROLE2 ";
				 ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
			  }catch(SQLException aex4) {  }

	      }catch(SQLException aex5) {
		  
				 cmdBuffer = " DROP INDEX IUSERMEMAPPROLE2 ";
				 ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
		  }

         cmdBuffer = " CREATE INDEX IUSERMEMAPPROLEROLE ON UserMemAppRole (RepId ,UserMemAppRoleRoleId ) ";
         cmdBuffer += "  ";
         ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
      }
   }

   public void ReorganizeSessionHistoryLogAtt( ) throws SQLException
   {
      String cmdBufferCreate = "" ;
      String cmdBuffer = "" ;
      /* Indices for table SessionHistoryLogAtt */
      try
      {
         try
         {
            cmdBuffer = " DROP TABLE SessionHistoryLogAtt ";
            ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
         }
         catch(SQLException sqlex1)
         {
            try
            {
               cmdBuffer = " DROP VIEW SessionHistoryLogAtt ";
               ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
            }
            catch(SQLException sqlex2)
            {
               try
               {
                  cmdBuffer = " DROP FUNCTION SessionHistoryLogAtt ";
                  ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
               }
               catch(SQLException sqlex3)
               {
               }
            }
         }
      }
      catch ( Exception ex )
      {
      }
      cmdBuffer = " ALTER TABLE SessionHistoryHisLogAtt RENAME TO SessionHistoryLogAtt ";
      ExecuteDirectSQL.executeWithThrow(context, remoteHandle, "DEFAULT", cmdBuffer) ;
   }

   private void tablesCount( )
   {
      if ( ! GXReorganization.isResumeMode( ) )
      {
         /* Using cursor P00012 */
         pr_default.execute(0);
         SessionCount = P00012_ASessionCount[0] ;
         pr_default.close(0);
         GXReorganization.printRecordCount ( "Session" ,  SessionCount );
         /* Using cursor P00023 */
         pr_default.execute(1);
         SessionHistoryCount = P00023_ASessionHistoryCount[0] ;
         pr_default.close(1);
         GXReorganization.printRecordCount ( "SessionHistory" ,  SessionHistoryCount );
         /* Using cursor P00034 */
         pr_default.execute(2);
         ApplicationCount = P00034_AApplicationCount[0] ;
         pr_default.close(2);
         GXReorganization.printRecordCount ( "Application" ,  ApplicationCount );
         /* Using cursor P00045 */
         pr_default.execute(3);
         UserMemAppRoleCount = P00045_AUserMemAppRoleCount[0] ;
         pr_default.close(3);
         GXReorganization.printRecordCount ( "UserMemAppRole" ,  UserMemAppRoleCount );
         /* Using cursor P00056 */
         pr_default.execute(4);
         SessionHistoryLogAttCount = P00056_ASessionHistoryLogAttCount[0] ;
         pr_default.close(4);
         GXReorganization.printRecordCount ( "SessionHistoryLogAtt" ,  SessionHistoryLogAttCount );
      }
   }

   private boolean previousCheck( )
   {
      if ( ! GXReorganization.isResumeMode( ) )
      {
         if ( GXutil.dbmsVersion( context, remoteHandle, "DEFAULT") < 9 )
         {
            GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_bad_DBMS_version", new Object[] {"9"}) ) ;
            return false ;
         }
      }
      if ( ! GXReorganization.mustRunCheck( ) )
      {
         return true ;
      }
      sSchemaVar = context.getUserId( "Server", remoteHandle, pr_default) ;
      if ( ColumnExist("Session",sSchemaVar,"SesOauthScope") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesOauthScope","Session"}) ) ;
         return false ;
      }
      if ( ColumnExist("Session",sSchemaVar,"SesOauthTokenExpires") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesOauthTokenExpires","Session"}) ) ;
         return false ;
      }
      if ( ColumnExist("Session",sSchemaVar,"SesOauthTokenMaxRenew") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesOauthTokenMaxRenew","Session"}) ) ;
         return false ;
      }
      if ( ColumnExist("Session",sSchemaVar,"SesAutTypeName") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesAutTypeName","Session"}) ) ;
         return false ;
      }
      if ( ColumnExist("Session",sSchemaVar,"SesExtToken2") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesExtToken2","Session"}) ) ;
         return false ;
      }
      if ( ColumnExist("SessionHistory",sSchemaVar,"SesHisOauthScope") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesHisOauthScope","SessionHistory"}) ) ;
         return false ;
      }
      if ( ColumnExist("SessionHistory",sSchemaVar,"SesHisOauthTokenExpires") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesHisOauthTokenExpires","SessionHistory"}) ) ;
         return false ;
      }
      if ( ColumnExist("SessionHistory",sSchemaVar,"SesHisOauthTokenMaxRenew") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesHisOauthTokenMaxRenew","SessionHistory"}) ) ;
         return false ;
      }
      if ( ColumnExist("SessionHistory",sSchemaVar,"SesHisAppTokenExp") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesHisAppTokenExp","SessionHistory"}) ) ;
         return false ;
      }
      if ( ColumnExist("SessionHistory",sSchemaVar,"SesHisDeviceId") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesHisDeviceId","SessionHistory"}) ) ;
         return false ;
      }
      if ( ColumnExist("SessionHistory",sSchemaVar,"SesHisRefreshToken") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesHisRefreshToken","SessionHistory"}) ) ;
         return false ;
      }
      if ( ColumnExist("SessionHistory",sSchemaVar,"SesHisAppId") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesHisAppId","SessionHistory"}) ) ;
         return false ;
      }
      if ( ColumnExist("SessionHistory",sSchemaVar,"SesHisExtToken2") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"SesHisExtToken2","SessionHistory"}) ) ;
         return false ;
      }
      if ( ColumnExist("Application",sSchemaVar,"AppCliLocalLoginURL") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"AppCliLocalLoginURL","Application"}) ) ;
         return false ;
      }
      if ( ColumnExist("Application",sSchemaVar,"AppCliAllowGetUserAdditionalDa") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"AppCliAllowGetUserAdditionalDa","Application"}) ) ;
         return false ;
      }
      if ( ColumnExist("Application",sSchemaVar,"AppCliAllowGetUserRoles") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"AppCliAllowGetUserRoles","Application"}) ) ;
         return false ;
      }
      if ( ColumnExist("Application",sSchemaVar,"AppCliAllowRemoteAuth") )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_column_exist", new Object[] {"AppCliAllowRemoteAuth","Application"}) ) ;
         return false ;
      }
      if ( tableexist("UserMemRoleProp",sSchemaVar) )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_table_exist", new Object[] {"UserMemRoleProp"}) ) ;
         return false ;
      }
      if ( tableexist("UserMemAppRoleProp",sSchemaVar) )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_table_exist", new Object[] {"UserMemAppRoleProp"}) ) ;
         return false ;
      }
      if ( tableexist("SecurityPolicyProp",sSchemaVar) )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_table_exist", new Object[] {"SecurityPolicyProp"}) ) ;
         return false ;
      }
      if ( tableexist("SessionHistoryLogAtt",sSchemaVar) )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_table_exist", new Object[] {"SessionHistoryLogAtt"}) ) ;
         return false ;
      }
      if ( ! tableexist("SessionHistoryHisLogAtt",sSchemaVar) )
      {
         GXReorganization.setCheckError ( localUtil.getMessages().getMessage("GXM_table_not_exist", new Object[] {"SessionHistoryHisLogAtt"}) ) ;
         return false ;
      }
      return true ;
   }

   private boolean tableexist( String sTableName ,
                               String sMySchemaName )
   {
      boolean result ;
      result = false ;
      /* Using cursor P00067 */
      pr_default.execute(5, new Object[] {sTableName, sMySchemaName});
      while ( (pr_default.getStatus(5) != 101) )
      {
         tablename = P00067_Atablename[0] ;
         ntablename = P00067_ntablename[0] ;
         schemaname = P00067_Aschemaname[0] ;
         nschemaname = P00067_nschemaname[0] ;
         result = true ;
         pr_default.readNext(5);
      }
      pr_default.close(5);
      /* Using cursor P00078 */
      pr_default.execute(6, new Object[] {sTableName, sMySchemaName});
      while ( (pr_default.getStatus(6) != 101) )
      {
         tablename = P00078_Atablename[0] ;
         ntablename = P00078_ntablename[0] ;
         schemaname = P00078_Aschemaname[0] ;
         nschemaname = P00078_nschemaname[0] ;
         result = true ;
         pr_default.readNext(6);
      }
      pr_default.close(6);
      return result ;
   }

   private boolean ColumnExist( String sTableName ,
                                String sMySchemaName ,
                                String sMyColumnName )
   {
      boolean result ;
      result = false ;
      /* Using cursor P00089 */
      pr_default.execute(7, new Object[] {sTableName, sMySchemaName, sMyColumnName});
      while ( (pr_default.getStatus(7) != 101) )
      {
         tablename = P00089_Atablename[0] ;
         ntablename = P00089_ntablename[0] ;
         schemaname = P00089_Aschemaname[0] ;
         nschemaname = P00089_nschemaname[0] ;
         columnname = P00089_Acolumnname[0] ;
         ncolumnname = P00089_ncolumnname[0] ;
         result = true ;
         pr_default.readNext(7);
      }
      pr_default.close(7);
      return result ;
   }

   private void executeOnlyTablesReorganization( )
   {
      callSubmit( "ReorganizeSession" ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"Session",""}) ,  1 , new Object[]{ });
      callSubmit( "ReorganizeSessionHistory" ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"SessionHistory",""}) ,  2 , new Object[]{ });
      callSubmit( "ReorganizeApplication" ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"Application",""}) ,  3 , new Object[]{ });
      callSubmit( "CreateUserMemRoleProp" ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"UserMemRoleProp",""}) ,  4 , new Object[]{ });
      callSubmit( "CreateUserMemAppRoleProp" ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"UserMemAppRoleProp",""}) ,  5 , new Object[]{ });
      callSubmit( "CreateSecurityPolicyProp" ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"SecurityPolicyProp",""}) ,  6 , new Object[]{ });
      callSubmit( "ReorganizeUserMemAppRole" ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"UserMemAppRole",""}) ,  7 , new Object[]{ });
      callSubmit( "ReorganizeSessionHistoryLogAtt" ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"SessionHistoryLogAtt",""}) ,  8 , new Object[]{ });
   }

   private void executeOnlyRisReorganization( )
   {
   }

   private void executeTablesReorganization( )
   {
      executeOnlyTablesReorganization( ) ;
      executeOnlyRisReorganization( ) ;
      ReorgSubmitThreadPool.startProcess();
   }

   private void setPrecedence( )
   {
      setPrecedencetables( ) ;
      setPrecedenceris( ) ;
   }

   private void setPrecedencetables( )
   {
      GXReorganization.addMsg( 1 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"Session",""}) );
      ReorgSubmitThreadPool.addBlock( "ReorganizeSession" );
      GXReorganization.addMsg( 2 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"SessionHistory",""}) );
      ReorgSubmitThreadPool.addBlock( "ReorganizeSessionHistory" );
      GXReorganization.addMsg( 3 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"Application",""}) );
      ReorgSubmitThreadPool.addBlock( "ReorganizeApplication" );
      GXReorganization.addMsg( 4 ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"UserMemRoleProp",""}) );
      ReorgSubmitThreadPool.addBlock( "CreateUserMemRoleProp" );
      GXReorganization.addMsg( 5 ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"UserMemAppRoleProp",""}) );
      ReorgSubmitThreadPool.addBlock( "CreateUserMemAppRoleProp" );
      GXReorganization.addMsg( 6 ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"SecurityPolicyProp",""}) );
      ReorgSubmitThreadPool.addBlock( "CreateSecurityPolicyProp" );
      GXReorganization.addMsg( 7 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"UserMemAppRole",""}) );
      ReorgSubmitThreadPool.addBlock( "ReorganizeUserMemAppRole" );
      GXReorganization.addMsg( 8 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"SessionHistoryLogAtt",""}) );
      ReorgSubmitThreadPool.addBlock( "ReorganizeSessionHistoryLogAtt" );
   }

   private void setPrecedenceris( )
   {
   }

   private void executeReorganization( )
   {
      if ( ErrCode == 0 )
      {
         tablesCount( ) ;
         if ( ! GXReorganization.getRecordCount( ) )
         {
            FirstActions( ) ;
            setPrecedence( ) ;
            executeTablesReorganization( ) ;
         }
      }
   }

   public void UtilsCleanup( )
   {
      cleanup();
   }

   protected void cleanup( )
   {
      CloseOpenCursors();
   }

   protected void CloseOpenCursors( )
   {
   }

   /* Aggregate/select formulas */
   public void submitReorg( int submitId ,
                            Object [] submitParms ) throws SQLException
   {
      UserInformation submitUI = Application.getConnectionManager().createUserInformation(Namespace.getNamespace(context.getNAME_SPACE()));
      switch ( submitId )
      {
            case 1 :
               GXReorganization.replaceMsg( 1 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"Session",""})+" STARTED" );
               ReorganizeSession( ) ;
               GXReorganization.replaceMsg( 1 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"Session",""})+" ENDED" );
               try { submitUI.disconnect(); } catch(Exception submitExc) { ; }
               break;
            case 2 :
               GXReorganization.replaceMsg( 2 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"SessionHistory",""})+" STARTED" );
               ReorganizeSessionHistory( ) ;
               GXReorganization.replaceMsg( 2 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"SessionHistory",""})+" ENDED" );
               try { submitUI.disconnect(); } catch(Exception submitExc) { ; }
               break;
            case 3 :
               GXReorganization.replaceMsg( 3 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"Application",""})+" STARTED" );
               ReorganizeApplication( ) ;
               GXReorganization.replaceMsg( 3 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"Application",""})+" ENDED" );
               try { submitUI.disconnect(); } catch(Exception submitExc) { ; }
               break;
            case 4 :
               GXReorganization.replaceMsg( 4 ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"UserMemRoleProp",""})+" STARTED" );
               CreateUserMemRoleProp( ) ;
               GXReorganization.replaceMsg( 4 ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"UserMemRoleProp",""})+" ENDED" );
               try { submitUI.disconnect(); } catch(Exception submitExc) { ; }
               break;
            case 5 :
               GXReorganization.replaceMsg( 5 ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"UserMemAppRoleProp",""})+" STARTED" );
               CreateUserMemAppRoleProp( ) ;
               GXReorganization.replaceMsg( 5 ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"UserMemAppRoleProp",""})+" ENDED" );
               try { submitUI.disconnect(); } catch(Exception submitExc) { ; }
               break;
            case 6 :
               GXReorganization.replaceMsg( 6 ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"SecurityPolicyProp",""})+" STARTED" );
               CreateSecurityPolicyProp( ) ;
               GXReorganization.replaceMsg( 6 ,  localUtil.getMessages().getMessage("GXM_filecrea", new Object[] {"SecurityPolicyProp",""})+" ENDED" );
               try { submitUI.disconnect(); } catch(Exception submitExc) { ; }
               break;
            case 7 :
               GXReorganization.replaceMsg( 7 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"UserMemAppRole",""})+" STARTED" );
               ReorganizeUserMemAppRole( ) ;
               GXReorganization.replaceMsg( 7 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"UserMemAppRole",""})+" ENDED" );
               try { submitUI.disconnect(); } catch(Exception submitExc) { ; }
               break;
            case 8 :
               GXReorganization.replaceMsg( 8 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"SessionHistoryLogAtt",""})+" STARTED" );
               ReorganizeSessionHistoryLogAtt( ) ;
               GXReorganization.replaceMsg( 8 ,  localUtil.getMessages().getMessage("GXM_fileupdate", new Object[] {"SessionHistoryLogAtt",""})+" ENDED" );
               try { submitUI.disconnect(); } catch(Exception submitExc) { ; }
               break;
      }
   }

   public void initialize( )
   {
      GXv_int1 = new byte [1] ;
      scmdbuf = "" ;
      P00012_ASessionCount = new int[1] ;
      P00023_ASessionHistoryCount = new int[1] ;
      P00034_AApplicationCount = new int[1] ;
      P00045_AUserMemAppRoleCount = new int[1] ;
      P00056_ASessionHistoryLogAttCount = new int[1] ;
      sSchemaVar = "" ;
      sTableName = "" ;
      sMySchemaName = "" ;
      tablename = "" ;
      ntablename = false ;
      schemaname = "" ;
      nschemaname = false ;
      P00067_Atablename = new String[] {""} ;
      P00067_ntablename = new boolean[] {false} ;
      P00067_Aschemaname = new String[] {""} ;
      P00067_nschemaname = new boolean[] {false} ;
      P00078_Atablename = new String[] {""} ;
      P00078_ntablename = new boolean[] {false} ;
      P00078_Aschemaname = new String[] {""} ;
      P00078_nschemaname = new boolean[] {false} ;
      sMyColumnName = "" ;
      columnname = "" ;
      ncolumnname = false ;
      P00089_Atablename = new String[] {""} ;
      P00089_ntablename = new boolean[] {false} ;
      P00089_Aschemaname = new String[] {""} ;
      P00089_nschemaname = new boolean[] {false} ;
      P00089_Acolumnname = new String[] {""} ;
      P00089_ncolumnname = new boolean[] {false} ;
      pr_default = new DataStoreProvider(context, remoteHandle, new artech.security.api.reorg__default(),
         new Object[] {
             new Object[] {
            P00012_ASessionCount
            }
            , new Object[] {
            P00023_ASessionHistoryCount
            }
            , new Object[] {
            P00034_AApplicationCount
            }
            , new Object[] {
            P00045_AUserMemAppRoleCount
            }
            , new Object[] {
            P00056_ASessionHistoryLogAttCount
            }
            , new Object[] {
            P00067_Atablename, P00067_Aschemaname
            }
            , new Object[] {
            P00078_Atablename, P00078_Aschemaname
            }
            , new Object[] {
            P00089_Atablename, P00089_Aschemaname, P00089_Acolumnname
            }
         }
      );
      /* GeneXus formulas. */
   }

   protected byte Status ;
   protected byte GXv_int1[] ;
   protected short ErrCode ;
   protected int SessionCount ;
   protected int SessionHistoryCount ;
   protected int ApplicationCount ;
   protected int UserMemAppRoleCount ;
   protected int SessionHistoryLogAttCount ;
   protected String scmdbuf ;
   protected String sSchemaVar ;
   protected String sTableName ;
   protected String sMySchemaName ;
   protected String sMyColumnName ;
   protected boolean ntablename ;
   protected boolean nschemaname ;
   protected boolean ncolumnname ;
   protected String tablename ;
   protected String schemaname ;
   protected String columnname ;
   protected IDataStoreProvider pr_default ;
   protected int[] P00012_ASessionCount ;
   protected int[] P00023_ASessionHistoryCount ;
   protected int[] P00034_AApplicationCount ;
   protected int[] P00045_AUserMemAppRoleCount ;
   protected int[] P00056_ASessionHistoryLogAttCount ;
   protected String[] P00067_Atablename ;
   protected boolean[] P00067_ntablename ;
   protected String[] P00067_Aschemaname ;
   protected boolean[] P00067_nschemaname ;
   protected String[] P00078_Atablename ;
   protected boolean[] P00078_ntablename ;
   protected String[] P00078_Aschemaname ;
   protected boolean[] P00078_nschemaname ;
   protected String[] P00089_Atablename ;
   protected boolean[] P00089_ntablename ;
   protected String[] P00089_Aschemaname ;
   protected boolean[] P00089_nschemaname ;
   protected String[] P00089_Acolumnname ;
   protected boolean[] P00089_ncolumnname ;
}

final  class reorg__default extends DataStoreHelperBase implements ILocalDataStoreHelper
{
   public Cursor[] getCursors( )
   {
      return new Cursor[] {
          new ForEachCursor("P00012", "SELECT COUNT(*) FROM \"Session\" ",false, GX_NOMASK + GX_MASKLOOPLOCK, false, this,100,0,false )
         ,new ForEachCursor("P00023", "SELECT COUNT(*) FROM SessionHistory ",false, GX_NOMASK + GX_MASKLOOPLOCK, false, this,100,0,false )
         ,new ForEachCursor("P00034", "SELECT COUNT(*) FROM Application ",false, GX_NOMASK + GX_MASKLOOPLOCK, false, this,100,0,false )
         ,new ForEachCursor("P00045", "SELECT COUNT(*) FROM UserMemAppRole ",false, GX_NOMASK + GX_MASKLOOPLOCK, false, this,100,0,false )
         ,new ForEachCursor("P00056", "SELECT COUNT(*) FROM SessionHistoryHisLogAtt ",false, GX_NOMASK + GX_MASKLOOPLOCK, false, this,100,0,false )
         ,new ForEachCursor("P00067", "SELECT TABLE_NAME, USER FROM USER_TABLES WHERE (UPPER(TABLE_NAME) = UPPER(?)) AND (UPPER(USER) = UPPER(?)) ",false, GX_NOMASK + GX_MASKLOOPLOCK, false, this,100,0,false )
         ,new ForEachCursor("P00078", "SELECT VIEW_NAME, USER FROM USER_VIEWS WHERE (UPPER(VIEW_NAME) = UPPER(?)) AND (UPPER(USER) = UPPER(?)) ",false, GX_NOMASK + GX_MASKLOOPLOCK, false, this,100,0,false )
         ,new ForEachCursor("P00089", "SELECT TABLE_NAME, USER, COLUMN_NAME FROM USER_TAB_COLUMNS WHERE (UPPER(TABLE_NAME) = UPPER(?)) AND (UPPER(USER) = UPPER(?)) AND (UPPER(COLUMN_NAME) = UPPER(?)) ",false, GX_NOMASK + GX_MASKLOOPLOCK, false, this,100,0,false )
      };
   }

   public void getResults( int cursor ,
                           IFieldGetter rslt ,
                           Object[] buf ) throws SQLException
   {
      switch ( cursor )
      {
            case 0 :
               ((int[]) buf[0])[0] = rslt.getInt(1) ;
               return;
            case 1 :
               ((int[]) buf[0])[0] = rslt.getInt(1) ;
               return;
            case 2 :
               ((int[]) buf[0])[0] = rslt.getInt(1) ;
               return;
            case 3 :
               ((int[]) buf[0])[0] = rslt.getInt(1) ;
               return;
            case 4 :
               ((int[]) buf[0])[0] = rslt.getInt(1) ;
               return;
            case 5 :
               ((String[]) buf[0])[0] = rslt.getVarchar(1) ;
               ((String[]) buf[1])[0] = rslt.getVarchar(2) ;
               return;
            case 6 :
               ((String[]) buf[0])[0] = rslt.getVarchar(1) ;
               ((String[]) buf[1])[0] = rslt.getVarchar(2) ;
               return;
            case 7 :
               ((String[]) buf[0])[0] = rslt.getVarchar(1) ;
               ((String[]) buf[1])[0] = rslt.getVarchar(2) ;
               ((String[]) buf[2])[0] = rslt.getVarchar(3) ;
               return;
      }
   }

   public void setParameters( int cursor ,
                              IFieldSetter stmt ,
                              Object[] parms ) throws SQLException
   {
      switch ( cursor )
      {
            case 5 :
               stmt.setString(1, (String)parms[0], 255);
               stmt.setString(2, (String)parms[1], 255);
               return;
            case 6 :
               stmt.setString(1, (String)parms[0], 255);
               stmt.setString(2, (String)parms[1], 255);
               return;
            case 7 :
               stmt.setString(1, (String)parms[0], 255);
               stmt.setString(2, (String)parms[1], 255);
               stmt.setString(3, (String)parms[2], 255);
               return;
      }
   }

}

