package org.sqldroid;

import java.lang.reflect.Method;

@SuppressWarnings("checkstyle:IllegalCatch")
public class SQLDroidBlobFileHelper 
{
    
    private static final String CLAZZ_FULL_NAME = "com.genexus.db.SQLAndroidBlobFileHelper";
    
    public static void removeDeletedBlobsOnCommit()
    {
        try {
            Class<?> clazz = Class.forName(CLAZZ_FULL_NAME);
            Method method = clazz.getMethod("removeDeletedBlobsOnCommit");
            
            method.invoke(null);
            
        } catch ( Exception e) { }
    }
    
    public static void removeInsertedBlobsOnRollback()
    {
        try {
            Class<?> clazz = Class.forName(CLAZZ_FULL_NAME);
            Method method = clazz.getMethod("removeInsertedBlobsOnRollback");
            
            method.invoke(null);
            
        } catch ( Exception e) { }
        
    }
    
    
    
    
}
