package com.genexus.android.core.base.metadata.expressions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import com.genexus.android.core.base.metadata.expressions.Expression.Type;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.utils.MultiMap;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.GXutil;
import com.genexus.GxRegex;
import com.genexus.LocalUtil;
import com.genexus.PrivateUtilities;
import com.genexus.util.Encryption;

class FunctionHelper
{
	private static MultiMap<String, Function> sFunctions;
	private static HashMap<Class<?>, Object> sImplementationClasses;

	public static Value call(String functionName, List<Value> parameters)
	{
		initFunctionHelper();

		Function function = getFunction(functionName, ExpressionHelper.getTypesOfValues(parameters));
		if (function == null)
			throw new IllegalArgumentException(String.format("Unknown function %s/%s.", functionName, parameters.size()));

		return function.run(parameters);
	}

	private static Function getFunction(String name, List<Type> parameterTypes)
	{
		ArrayList<Function> overloads = new ArrayList<>(sFunctions.get(Strings.toLowerCase(name)));

		// Exclude overloads with incorrect number of arguments.
		ListIterator<Function> iterator = overloads.listIterator();
		while (iterator.hasNext())
		{
			if (iterator.next().mParameterTypes.size() != parameterTypes.size())
				iterator.remove();
		}

		// Exclude overloads with incorrect parameter types, but only if disambiguation is necessary.
		if (overloads.size() > 1)
		{
			iterator = overloads.listIterator();
			while (iterator.hasNext())
			{
				Function overload = iterator.next();
				for (int i = 0; i < overload.mParameterTypes.size(); i++)
				{
					if (overload.mParameterTypes.get(i) != parameterTypes.get(i))
					{
						iterator.remove();
						break;
					}
				}
			}
		}

		// There should be only one valid option here.
		if (overloads.size() == 1)
			return overloads.get(0);
		else
			return null;
	}

	private static synchronized void initFunctionHelper()
	{
		if (sFunctions == null)
		{
			// Register information about known functions.
			sFunctions = new MultiMap<>();
			registerFunctions();

			// Create implementors for non-static methods.
			sImplementationClasses = new HashMap<>();
			sImplementationClasses.put(LocalUtil.class, GXutilPlus.getLocalUtil());
		}
	}

	private static void registerFunctions()
	{
		// Functions.

		registerFunction(new Function("AddMth", Type.DATETIME, new Type[] { Type.DATE, Type.INTEGER }, GXutil.class, "addmth"));
		registerFunction(new Function("AddYr", Type.DATETIME, new Type[] { Type.DATE, Type.INTEGER }, GXutil.class, "addyr"));
		registerFunction(new Function("Age", Type.INTEGER, new Type[] { Type.DATE }, GXutil.class, "age"));
		registerFunction(new Function("Age", Type.INTEGER, new Type[] { Type.DATE, Type.DATE }, GXutil.class, "age"));
		registerFunction(new Function("Asc", Type.INTEGER, new Type[] { Type.STRING }, GXutil.class, "asc"));
		registerFunction(new Function("CDoW", Type.STRING, new Type[] { Type.DATE, Type.STRING }, LocalUtil.class, "cdow"));
		registerFunction(new Function("CDoW", Type.STRING, new Type[] { Type.DATE }, LocalUtil.class, "cdow"));
		registerFunction(new Function("Chr", Type.STRING, new Type[] { Type.INTEGER }, GXutil.class, "chr"));
		registerFunction(new Function("CMonth", Type.STRING, new Type[] { Type.DATE, Type.STRING }, LocalUtil.class, "cmonth"));
		registerFunction(new Function("CMonth", Type.STRING, new Type[] { Type.DATE }, LocalUtil.class, "cmonth"));
		registerFunction(new Function("Color", Type.STRING, new Type[] { Type.STRING}, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("Concat", Type.STRING, new Type[] { Type.STRING, Type.STRING }, GXutil.class, "concat"));
		registerFunction(new Function("Concat", Type.STRING, new Type[] { Type.STRING, Type.STRING, Type.STRING }, GXutil.class, "concat"));
		registerFunction(new Function("CtoD", Type.DATE, new Type[] { Type.STRING }, LocalUtil.class, "ctod"));
		registerFunction(new Function("CtoT", Type.DATETIME, new Type[] { Type.STRING }, LocalUtil.class, "ctot"));
		registerFunction(new Function("Day", Type.INTEGER, new Type[] { Type.DATE }, GXutil.class, "day"));
		registerFunction(new Function("Decrypt64", Type.STRING, new Type[] { Type.STRING, Type.STRING }, Encryption.class, "decrypt64"));
		registerFunction(new Function("DoW", Type.INTEGER, new Type[] { Type.DATE }, GXutil.class, "dow"));
		registerFunction(new Function("DtoC", Type.STRING, new Type[] { Type.DATE }, GXutilPlus.class, "dtoc1"));
		registerFunction(new Function("Encrypt64", Type.STRING, new Type[] { Type.STRING, Type.STRING }, Encryption.class, "encrypt64"));
		registerFunction(new Function("EoM", Type.DATETIME, new Type[] { Type.DATETIME }, GXutil.class, "eom"));
		registerFunction(new Function("EoM", Type.DATE, new Type[] { Type.DATE }, GXutil.class, "eomdate"));
		registerFunction(new Function("FromBase64", Type.STRING, new Type[] { Type.STRING }, PrivateUtilities.class, "fromBase64"));
		registerFunction(new Function("GetEncryptionKey", Type.STRING, new Type[] { }, Encryption.class, "getNewKey"));
		registerFunction(new Function("Hour", Type.INTEGER, new Type[] { Type.DATETIME }, GXutil.class, "hour"));
		registerFunction(new Function("Int", Type.INTEGER, new Type[] { Type.DECIMAL }, GXutilPlus.class, "decimalToInteger"));
		registerFunction(new Function("Len", Type.INTEGER, new Type[] { Type.STRING }, GXutil.class, "len"));
		registerFunction(new Function("Lower", Type.STRING, new Type[] { Type.STRING }, GXutil.class, "lower"));
		registerFunction(new Function("LTrim", Type.STRING, new Type[] { Type.STRING }, GXutil.class, "ltrim"));
		registerFunction(new Function("Minute", Type.INTEGER, new Type[] { Type.DATETIME }, GXutil.class, "minute"));
		registerFunction(new Function("Month", Type.INTEGER, new Type[] { Type.DATE }, GXutil.class, "month"));
		registerFunction(new Function("NewLine", Type.STRING, new Type[] { }, GXutil.class, "newLine"));
		registerFunction(new Function("Now", Type.DATETIME, new Type[] { }, GXutil.class, "nowms"));
		registerFunction(new Function("PadL", Type.STRING, new Type[] { Type.STRING, Type.INTEGER, Type.STRING }, GXutil.class, "padl"));
		registerFunction(new Function("PadL", Type.STRING, new Type[] { Type.STRING, Type.INTEGER }, GXutilPlus.class, "padl2"));
		registerFunction(new Function("PadR", Type.STRING, new Type[] { Type.STRING, Type.INTEGER, Type.STRING }, GXutil.class, "padr"));
		registerFunction(new Function("PadR", Type.STRING, new Type[] { Type.STRING, Type.INTEGER }, GXutilPlus.class, "padr2"));
		registerFunction(new Function("Random", Type.DECIMAL, new Type[] { }, GXutil.class, "random"));
		registerFunction(new Function("RGB", Type.STRING, new Type[] { Type.INTEGER, Type.INTEGER, Type.INTEGER }, GXutilPlus.class, "functionRGB"));
		registerFunction(new Function("Round", Type.DECIMAL, new Type[] { Type.DECIMAL, Type.INTEGER }, GXutil.class, "roundDecimal"));
		registerFunction(new Function("RoundToEven", Type.DECIMAL, new Type[] { Type.DECIMAL, Type.INTEGER }, GXutil.class, "roundToEven"));
		registerFunction(new Function("RTrim", Type.STRING, new Type[] { Type.STRING }, GXutil.class, "rtrim"));
		registerFunction(new Function("Second", Type.INTEGER, new Type[] { Type.DATETIME }, GXutil.class, "second"));
		registerFunction(new Function("Millisecond", Type.INTEGER, new Type[] { Type.DATETIME }, GXutil.class, "millisecond"));
		registerFunction(new Function("ServerDate", Type.DATE, new Type[] { }, GXutil.class, "today"));
		registerFunction(new Function("ServerNow", Type.DATETIME, new Type[] { }, GXutil.class, "nowms"));
		registerFunction(new Function("ServerTime", Type.STRING, new Type[] { }, GXutil.class, "time"));
		registerFunction(new Function("Str", Type.STRING, new Type[] { Type.DECIMAL, Type.INTEGER, Type.INTEGER }, GXutil.class, "str"));
		registerFunction(new Function("Str", Type.STRING, new Type[] { Type.DECIMAL, Type.INTEGER }, GXutilPlus.class, "str2"));
		registerFunction(new Function("Str", Type.STRING, new Type[] { Type.DECIMAL }, GXutilPlus.class, "str1"));
		registerFunction(new Function("StrReplace", Type.STRING, new Type[] { Type.STRING, Type.STRING, Type.STRING }, GXutil.class, "strReplace"));
		registerFunction(new Function("StrSearch", Type.INTEGER, new Type[] { Type.STRING, Type.STRING, Type.INTEGER }, GXutil.class, "strSearch"));
		registerFunction(new Function("StrSearch", Type.INTEGER, new Type[] { Type.STRING, Type.STRING }, GXutilPlus.class, "strSearch2"));
		registerFunction(new Function("StrSearchRev", Type.INTEGER, new Type[] { Type.STRING, Type.STRING, Type.INTEGER }, GXutil.class, "strSearchRev"));
		registerFunction(new Function("StrSearchRev", Type.INTEGER, new Type[] { Type.STRING, Type.STRING }, GXutilPlus.class, "strSearchRev2"));
		registerFunction(new Function("SubStr", Type.STRING, new Type[] { Type.STRING, Type.INTEGER, Type.INTEGER }, GXutil.class, "substring"));
		registerFunction(new Function("SubStr", Type.STRING, new Type[] { Type.STRING, Type.INTEGER }, GXutilPlus.class, "substring2"));
		registerFunction(new Function("SysDate", Type.DATE, new Type[] { }, GXutil.class, "today"));
		registerFunction(new Function("SysTime", Type.STRING, new Type[] { }, GXutil.class, "time"));
		registerFunction(new Function("TAdd", Type.DATETIME, new Type[] { Type.DATETIME, Type.INTEGER }, GXutil.class, "dtadd"));
		registerFunction(new Function("TDiff", Type.INTEGER, new Type[] { Type.DATETIME, Type.DATETIME }, GXutil.class, "dtdiff"));
		registerFunction(new Function("Time", Type.STRING, new Type[] { }, GXutil.class, "time"));
		registerFunction(new Function("ToBase64", Type.STRING, new Type[] { Type.STRING }, PrivateUtilities.class, "toBase64"));
		registerFunction(new Function("Today", Type.DATE, new Type[] { }, GXutil.class, "today"));
		registerFunction(new Function("Trim", Type.STRING, new Type[] { Type.STRING }, GXutil.class, "trim"));
		registerFunction(new Function("Trunc", Type.DECIMAL, new Type[] { Type.DECIMAL, Type.INTEGER }, GXutil.class, "truncDecimal"));
		registerFunction(new Function("TtoC", Type.STRING, new Type[] { Type.DATETIME }, GXutilPlus.class, "ttoc1"));
		registerFunction(new Function("TtoC", Type.STRING, new Type[] { Type.DATETIME, Type.INTEGER }, GXutilPlus.class, "ttoc2"));
		registerFunction(new Function("TtoC", Type.STRING, new Type[] { Type.DATETIME, Type.INTEGER, Type.INTEGER }, GXutilPlus.class, "ttoc3"));
		registerFunction(new Function("Upper", Type.STRING, new Type[] { Type.STRING }, GXutil.class, "upper"));
		registerFunction(new Function("Val", Type.DECIMAL, new Type[] { Type.STRING, Type.STRING }, GXutilPlus.class, "strToNumber"));
		registerFunction(new Function("Val", Type.DECIMAL, new Type[] { Type.STRING }, GXutilPlus.class, "strToNumber"));
		registerFunction(new Function("Year", Type.INTEGER, new Type[] { Type.DATE }, GXutil.class, "year"));
		registerFunction(new Function("YMDHMStoT", Type.DATETIME, new Type[] { Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.INTEGER }, LocalUtil.class, "ymdhmsToT"));
		registerFunction(new Function("YMDHMStoT", Type.DATETIME, new Type[] { Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.INTEGER }, LocalUtil.class, "ymdhmsToT"));
		registerFunction(new Function("YMDHMStoT", Type.DATETIME, new Type[] { Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.INTEGER }, LocalUtil.class, "ymdhmsToT"));
		registerFunction(new Function("YMDHMStoT", Type.DATETIME, new Type[] { Type.INTEGER, Type.INTEGER, Type.INTEGER }, LocalUtil.class, "ymdhmsToT"));
		registerFunction(new Function("YMDtoD", Type.DATE, new Type[] { Type.INTEGER, Type.INTEGER, Type.INTEGER }, LocalUtil.class, "ymdtod"));
		registerFunction(new Function("CreateFromUrl", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "createFromUrl"));
		registerFunction(new Function("RemoveDiacritics", Type.STRING, new Type[] { Type.STRING }, GXutil.class, "removeDiacritics"));

		// Methods mapped to functions, but not functions by themselves.
		registerFunction(new Function("BOOLEAN::IsEmpty", Type.BOOLEAN, new Type[] { Type.BOOLEAN }, GXutilPlus.class, "isEmptyBoolean"));
		registerFunction(new Function("BOOLEAN::SetEmpty", Type.BOOLEAN, new Type[] { Type.BOOLEAN }, GXutilPlus.class, "setEmptyBoolean"));
		registerFunction(new Function("BOOLEAN::ToString", Type.STRING, new Type[] { Type.BOOLEAN }, GXutil.class, "booltostr"));
		registerFunction(new Function("DATETIME::AddDays", Type.DATETIME, new Type[] { Type.DATETIME, Type.INTEGER }, GXutilPlus.class, "addDays"));
		registerFunction(new Function("DATETIME::AddHours", Type.DATETIME, new Type[] { Type.DATETIME, Type.INTEGER }, GXutilPlus.class, "addHours"));
		registerFunction(new Function("DATETIME::AddMinutes", Type.DATETIME, new Type[] { Type.DATETIME, Type.INTEGER }, GXutilPlus.class, "addMinutes"));
		registerFunction(new Function("DATETIME::IsEmpty", Type.BOOLEAN, new Type[] { Type.DATETIME }, GXutilPlus.class, "isEmptyDate"));
		registerFunction(new Function("DATETIME::SetEmpty", Type.UNKNOWN, new Type[] { Type.DATETIME }, GXutilPlus.class, "setEmptyDate"));
		registerFunction(new Function("DATETIME::ToDate", Type.DATETIME, new Type[] { Type.DATETIME }, GXutil.class, "resetTime"));
		registerFunction(new Function("DATETIME::ToUniversalTime", Type.DATETIME, new Type[] { Type.DATETIME }, GXutil.class, "DateTimeToUTC"));
		registerFunction(new Function("DECIMAL::IsEmpty", Type.BOOLEAN, new Type[] { Type.DECIMAL }, GXutilPlus.class, "isEmptyNumber"));
		registerFunction(new Function("DECIMAL::SetEmpty", Type.DECIMAL, new Type[] { Type.DECIMAL }, GXutilPlus.class, "setEmptyNumber"));
		registerFunction(new Function("GEOPOINT::ToString", Type.STRING, new Type[] { Type.GEOPOINT }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("GEOPOINT::ToWKT", Type.STRING, new Type[] { Type.GEOPOINT }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("GEOLINE::ToString", Type.STRING, new Type[] { Type.GEOLINE }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("GEOPOLYGON::ToString", Type.STRING, new Type[] { Type.GEOPOLYGON }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("GEOGRAPHY::ToString", Type.STRING, new Type[] { Type.GEOGRAPHY }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("GUID::IsEmpty", Type.BOOLEAN, new Type[] { Type.GUID }, GXutilPlus.class, "isEmptyGuid"));
		registerFunction(new Function("GUID::SetEmpty", Type.GUID, new Type[] { Type.GUID }, GXutilPlus.class, "setEmptyGuid"));
		registerFunction(new Function("GUID::ToString", Type.STRING, new Type[] { Type.GUID }, GXutilPlus.class, "guidToString"));
		registerFunction(new Function("INTEGER::IsEmpty", Type.BOOLEAN, new Type[] { Type.INTEGER }, GXutilPlus.class, "isEmptyNumber"));
		registerFunction(new Function("INTEGER::SetEmpty", Type.INTEGER, new Type[] { Type.INTEGER }, GXutilPlus.class, "setEmptyNumber"));
		registerFunction(new Function("STRING::CharAt", Type.STRING, new Type[] { Type.STRING, Type.INTEGER }, GXutil.class, "charAt"));
		registerFunction(new Function("STRING::Contains", Type.BOOLEAN, new Type[] { Type.STRING, Type.STRING }, GXutil.class, "contains"));
		registerFunction(new Function("STRING::EndsWith", Type.BOOLEAN, new Type[] { Type.STRING, Type.STRING }, GXutil.class, "endsWith"));
		registerFunction(new Function("STRING::IsEmpty", Type.BOOLEAN, new Type[] { Type.STRING }, GXutilPlus.class, "isEmptyString"));
		registerFunction(new Function("STRING::IsMatch", Type.BOOLEAN, new Type[] { Type.STRING, Type.STRING }, GxRegex.class, "IsMatch"));
		registerFunction(new Function("STRING::ReplaceRegEx", Type.STRING, new Type[] { Type.STRING, Type.STRING, Type.STRING }, GxRegex.class, "Replace"));
		registerFunction(new Function("STRING::SetEmpty", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "setEmptyString"));
		registerFunction(new Function("STRING::StartsWith", Type.BOOLEAN, new Type[] { Type.STRING, Type.STRING }, GXutil.class, "startsWith"));
		registerFunction(new Function("STRING::ToString", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "strIdentity"));

		registerFunction(new Function("IMAGE::FromImage", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("IMAGE::FromURL", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("IMAGE::SetEmpty", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "setEmptyString"));
		registerFunction(new Function("IMAGE::IsEmpty", Type.BOOLEAN, new Type[] { Type.STRING }, GXutilPlus.class, "isEmptyString"));

		registerFunction(new Function("AUDIO::FromURL", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("AUDIO::IsEmpty", Type.BOOLEAN, new Type[] { Type.STRING }, GXutilPlus.class, "isEmptyString"));
		registerFunction(new Function("AUDIO::SetEmpty", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "setEmptyString"));

		registerFunction(new Function("VIDEO::FromURL", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("VIDEO::IsEmpty", Type.BOOLEAN, new Type[] { Type.STRING }, GXutilPlus.class, "isEmptyString"));
		registerFunction(new Function("VIDEO::SetEmpty", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "setEmptyString"));

		registerFunction(new Function("BLOBFILE::FromURL", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "strIdentity"));
		registerFunction(new Function("BLOBFILE::IsEmpty", Type.BOOLEAN, new Type[] { Type.STRING }, GXutilPlus.class, "isEmptyString"));
		registerFunction(new Function("BLOBFILE::SetEmpty", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "setEmptyString"));

		// Static methods
		registerFunction(new Function("STATIC.GUID::Empty", Type.GUID, new Type[] { }, GXutilPlus.class, "emptyGuid"));
		registerFunction(new Function("STATIC.GUID::NewGuid", Type.GUID, new Type[] { }, java.util.UUID.class, "randomUUID"));

		// "Functions" used by &Var.FromString()
		registerFunction(new Function("BOOLEAN::FROM_STRING", Type.BOOLEAN, new Type[] { Type.STRING }, GXutil.class, "boolval"));
		registerFunction(new Function("DATE::FROM_STRING", Type.DATE, new Type[] { Type.STRING }, LocalUtil.class, "ctod"));
		registerFunction(new Function("DATETIME::FROM_STRING", Type.DATETIME, new Type[] { Type.STRING }, LocalUtil.class, "ctot"));
		registerFunction(new Function("DECIMAL::FROM_STRING", Type.DECIMAL, new Type[] { Type.STRING }, GXutilPlus.class, "strToNumber"));
		registerFunction(new Function("GEOPOINT::FROM_STRING", Type.GEOPOINT, new Type[] { Type.STRING }, GXutilPlus.class, "strToGeoPoint"));
		registerFunction(new Function("GEOLINE::FROM_STRING", Type.GEOLINE, new Type[] { Type.STRING }, GXutilPlus.class, "strToGeoLine"));
		registerFunction(new Function("GEOPOLYGON::FROM_STRING", Type.GEOPOLYGON, new Type[] { Type.STRING }, GXutilPlus.class, "strToGeoPolygon"));
		registerFunction(new Function("GEOGRAPHY::FROM_STRING", Type.GEOGRAPHY, new Type[] { Type.STRING }, GXutilPlus.class, "strToGeography"));
		registerFunction(new Function("GUID::FROM_STRING", Type.GUID, new Type[] { Type.STRING }, GXutil.class, "strToGuid"));
		registerFunction(new Function("INTEGER::FROM_STRING", Type.INTEGER, new Type[] { Type.STRING }, GXutilPlus.class, "strToInteger"));
		registerFunction(new Function("STRING::FROM_STRING", Type.STRING, new Type[] { Type.STRING }, GXutilPlus.class, "strIdentity"));

		// FromWKT
		registerFunction(new Function("GEOPOINT::FromWKT", Type.GEOPOINT, new Type[] { Type.STRING }, GXutilPlus.class, "strToGeoPoint"));
		registerFunction(new Function("GEOLINE::FromWKT", Type.GEOLINE, new Type[] { Type.STRING }, GXutilPlus.class, "strToGeoLine"));
		registerFunction(new Function("GEOPOLYGON::FromWKT", Type.GEOPOLYGON, new Type[] { Type.STRING }, GXutilPlus.class, "strToGeoPolygon"));
		registerFunction(new Function("GEOGRAPHY::FromWKT", Type.GEOGRAPHY, new Type[] { Type.STRING }, GXutilPlus.class, "strToGeography"));
	}

	private static void registerFunction(Function function)
	{
		sFunctions.put(Strings.toLowerCase(function.mName), function);
	}

	private static class Function
	{
		// Function specification.
		private final String mName;
		private final Type mResultType;
		private final List<Type> mParameterTypes;
		private final Class<?> mImplementationClass;
		private final String mImplementationMethodName;
		// private final List<Object> mImplementationExtraParameters;

		// Cached information.
		private Object mImplementationObject;
		private Method mImplementationMethod;

		public Function(String name, Type resultType, Type[] parameterTypes, Class<?> implementationClass, String implementationMethod)
		{
			mName = name;
			mResultType = resultType;
			mParameterTypes = Arrays.asList(parameterTypes);

			mImplementationClass = implementationClass;
			mImplementationMethodName = implementationMethod;
			// mImplementationExtraParameters = (implementationExtraParameters != null ? Arrays.asList(implementationExtraParameters) : Collections.emptyList());
		}

		public Value run(List<Value> parameters)
		{
			// Get the class and method to execute.
			prepareReflection();

			// Prepare parameters.
			if (parameters.size() /* + mImplementationExtraParameters.size() */ != mParameterTypes.size())
				throw new IllegalArgumentException(String.format("Unexpected number of parameters for function %s (expected %s, received %s).", mImplementationMethodName, mParameterTypes.size(), parameters.size()));

			Object[] methodParameters = new Object[mParameterTypes.size() /* + mImplementationExtraParameters.size() */];
			for (int i = 0; i < parameters.size(); i++)
				methodParameters[i] = ExpressionHelper.valueToJavaObject(parameters.get(i), mParameterTypes.get(i));

			/*
			// Put extra parameters.
			for (int i = 0; i < mImplementationExtraParameters.size(); i++)
				methodParameters[i + parameters.size()] = mImplementationExtraParameters.get(i);
			*/

			try
			{
				// Execute function via reflection.
				Object result = mImplementationMethod.invoke(mImplementationObject, methodParameters);
				if (mResultType == Type.UNKNOWN) // Special case, it can be different types, copy it from input value
					return ExpressionHelper.javaObjectToValue(parameters.get(0).getType(), result);
				else
					return ExpressionHelper.javaObjectToValue(mResultType, result);
			}
			catch (IllegalAccessException | InvocationTargetException e)
			{
				throw new IllegalArgumentException(String.format("An exception occurred calling function '%s/%s' via reflection.", mName, mParameterTypes.size()), e);
			}
		}

		private void prepareReflection()
		{
			if (mImplementationMethod == null)
			{
				try
				{
					// Obtain the method via reflection and cache it.
					Class<?>[] parameterTypes = new Class[mParameterTypes.size() /* + mImplementationExtraParameters.size() */];
					for (int i = 0; i < mParameterTypes.size(); i++)
						parameterTypes[i] = ExpressionHelper.typeToJavaClass(mParameterTypes.get(i));

					/*
					for (int i = 0; i < mImplementationExtraParameters.size(); i++)
						parameterTypes[i + mParameterTypes.size()] = mImplementationExtraParameters.get(i).getClass();
					*/

					Method implementationMethod = mImplementationClass.getDeclaredMethod(mImplementationMethodName, parameterTypes);

					// Obtain an instance to run the method (unless it's static, in which case we don't need it).
					Object implementationObject = null;
					if (!Modifier.isStatic(implementationMethod.getModifiers()))
					{
						implementationObject = sImplementationClasses.get(mImplementationClass);
						if (implementationObject == null)
							throw new IllegalArgumentException(String.format("No object provided for non-static method '%s' in class '%s'.", mImplementationMethodName, mImplementationClass.getName()));
					}

					// All set.
					mImplementationObject = implementationObject;
					mImplementationMethod = implementationMethod;
				}
				catch (NoSuchMethodException e)
				{
					throw new IllegalArgumentException(String.format("Method '%s.%s()' (for function '%s/%s') could not be obtained via reflection.", mImplementationClass.getName(), mImplementationMethodName, mName, mParameterTypes.size()));
				}
			}
		}
	}
}
