package com.genexus.android.core.base.metadata.expressions;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.utils.DoubleMap;

class MethodHelper
{
	private static DoubleMap<String, String, Method> sMethods;

	public static Expression.Value call(Expression.Value target, String methodName, List<Expression.Value> parameters)
	{
		initMethodHelper();

		Method method = getMethod(target.getType(), methodName);
		if (method == null)
			throw new IllegalArgumentException(String.format("Unknown method %s.%s()/%s.", target.getType(), methodName, parameters.size()));

		return method.run(target, parameters);
	}

	private static Method getMethod(Expression.Type targetType, String name)
	{
		return sMethods.get(targetType.toString(), name);
	}

	private static synchronized void initMethodHelper()
	{
		if (sMethods == null)
		{
			sMethods = DoubleMap.newStringMap();
			registerMethods();
		}
	}

	private static void registerMethods()
	{
		// BOOLEAN methods.
		registerMethod(new Method(Expression.Type.BOOLEAN, "IsEmpty", "BOOLEAN::IsEmpty"));
		registerMethod(new Method(Expression.Type.BOOLEAN, "SetEmpty", "BOOLEAN::SetEmpty"));
		registerMethod(new Method(Expression.Type.BOOLEAN, "ToString", "BOOLEAN::ToString"));

		// DATE/DATETIME methods.
		registerMethod(new Method(Expression.Type.DATETIME, "AddDays", "DATETIME::AddDays"));
		registerMethod(new Method(Expression.Type.DATETIME, "AddHours", "DATETIME::AddHours"));
		registerMethod(new Method(Expression.Type.DATETIME, "AddMinutes", "DATETIME::AddMinutes"));
		registerMethod(new Method(Expression.Type.DATETIME, "AddMonths", "AddMth"));
		registerMethod(new Method(Expression.Type.DATETIME, "AddSeconds", "TAdd"));
		registerMethod(new Method(Expression.Type.DATETIME, "AddYears", "AddYr"));
		registerMethod(new Method(Expression.Type.DATETIME, "Age", "Age"));
		registerMethod(new Method(Expression.Type.DATETIME, "Day", "Day"));
		registerMethod(new Method(Expression.Type.DATETIME, "DayOfWeek", "DoW"));
		registerMethod(new Method(Expression.Type.DATETIME, "DayOfWeekName", "CDoW"));
		registerMethod(new Method(Expression.Type.DATETIME, "Difference", "TDiff"));
		registerMethod(new Method(Expression.Type.DATETIME, "EndOfMonth", "EoM"));
		registerMethod(new Method(Expression.Type.DATETIME, "Hour", "Hour"));
		registerMethod(new Method(Expression.Type.DATETIME, "IsEmpty", "DATETIME::IsEmpty"));
		registerMethod(new Method(Expression.Type.DATETIME, "Minute", "Minute"));
		registerMethod(new Method(Expression.Type.DATETIME, "Month", "Month"));
		registerMethod(new Method(Expression.Type.DATETIME, "MonthName", "CMonth"));
		registerMethod(new Method(Expression.Type.DATETIME, "Second", "Second"));
		registerMethod(new Method(Expression.Type.DATETIME, "Millisecond", "Millisecond"));
		registerMethod(new Method(Expression.Type.DATETIME, "SetEmpty", "DATETIME::SetEmpty"));
		registerMethod(new Method(Expression.Type.DATETIME, "ToDate", "DATETIME::ToDate"));
		registerMethod(new Method(Expression.Type.DATETIME, "ToString", "TtoC"));
		registerMethod(new Method(Expression.Type.DATETIME, "ToUniversalTime", "DATETIME::ToUniversalTime"));
		registerMethod(new Method(Expression.Type.DATETIME, "Year", "Year"));

		// DECIMAL/INTEGER methods.
		registerMethod(new Method(Expression.Type.DECIMAL, "Integer", "Int"));
		registerMethod(new Method(Expression.Type.DECIMAL, "IsEmpty", "DECIMAL::IsEmpty"));
		registerMethod(new Method(Expression.Type.DECIMAL, "Round", "Round"));
		registerMethod(new Method(Expression.Type.DECIMAL, "RoundToEven", "RoundToEven"));
		registerMethod(new Method(Expression.Type.DECIMAL, "SetEmpty", "DECIMAL::SetEmpty"));
		registerMethod(new Method(Expression.Type.DECIMAL, "ToString", "Str"));
		registerMethod(new Method(Expression.Type.DECIMAL, "Truncate", "Trunc"));

		// GEOPOINT methods.
		registerMethod(new Method(Expression.Type.GEOPOINT, "ToString", "GEOPOINT::ToString"));
		registerMethod(new Method(Expression.Type.GEOPOINT, "ToWKT", "GEOPOINT::ToWKT"));
		registerMethod(new Method(Expression.Type.GEOPOINT, "FromWKT", true, "GEOPOINT::FromWKT"));
		// GEOLINE methods.
		registerMethod(new Method(Expression.Type.GEOLINE, "ToString", "GEOLINE::ToString"));
		registerMethod(new Method(Expression.Type.GEOLINE, "FromWKT", true, "GEOLINE::FromWKT"));
		// GEOPOLYGON methods
		registerMethod(new Method(Expression.Type.GEOPOLYGON, "ToString", "GEOPOLYGON::ToString"));
		registerMethod(new Method(Expression.Type.GEOPOLYGON, "FromWKT", true, "GEOPOLYGON::FromWKT"));
		// GEOGRAPHY methods
		registerMethod(new Method(Expression.Type.GEOGRAPHY, "ToString", "GEOGRAPHY::ToString"));
		registerMethod(new Method(Expression.Type.GEOGRAPHY, "FromWKT", true, "GEOGRAPHY::FromWKT"));

		// GUID methods.
		registerMethod(new Method(Expression.Type.GUID, "IsEmpty", "GUID::IsEmpty"));
		registerMethod(new Method(Expression.Type.GUID, "SetEmpty", "GUID::SetEmpty"));
		registerMethod(new Method(Expression.Type.GUID, "ToString", "GUID::ToString"));

		// INTEGER methods.
		registerMethod(new Method(Expression.Type.INTEGER, "IsEmpty", "INTEGER::IsEmpty"));
		registerMethod(new Method(Expression.Type.INTEGER, "SetEmpty", "INTEGER::SetEmpty"));

		// STRING methods.
		registerMethod(new Method(Expression.Type.STRING, "CharAt", "STRING::CharAt"));
		registerMethod(new Method(Expression.Type.STRING, "Contains", "STRING::Contains"));
		registerMethod(new Method(Expression.Type.STRING, "EndsWith", "STRING::EndsWith"));
		registerMethod(new Method(Expression.Type.STRING, "IndexOf", "StrSearch"));
		registerMethod(new Method(Expression.Type.STRING, "IsEmpty", "STRING::IsEmpty"));
		registerMethod(new Method(Expression.Type.STRING, "IsMatch", "STRING::IsMatch"));
		registerMethod(new Method(Expression.Type.STRING, "LastIndexOf", "StrSearchRev"));
		registerMethod(new Method(Expression.Type.STRING, "Length", "Len"));
		registerMethod(new Method(Expression.Type.STRING, "PadLeft", "PadL"));
		registerMethod(new Method(Expression.Type.STRING, "PadRight", "PadR"));
		registerMethod(new Method(Expression.Type.STRING, "Replace", "StrReplace"));
		registerMethod(new Method(Expression.Type.STRING, "ReplaceRegEx", "STRING::ReplaceRegEx"));
		registerMethod(new Method(Expression.Type.STRING, "SetEmpty", "STRING::SetEmpty"));
		registerMethod(new Method(Expression.Type.STRING, "StartsWith", "STRING::StartsWith"));
		registerMethod(new Method(Expression.Type.STRING, "Substring", "SubStr"));
		registerMethod(new Method(Expression.Type.STRING, "ToLower", "Lower"));
		registerMethod(new Method(Expression.Type.STRING, "ToNumeric", "Val"));
		registerMethod(new Method(Expression.Type.STRING, "ToString", "STRING::ToString"));
		registerMethod(new Method(Expression.Type.STRING, "ToUpper", "Upper"));
		registerMethod(new Method(Expression.Type.STRING, "Trim", "Trim"));
		registerMethod(new Method(Expression.Type.STRING, "TrimEnd", "RTrim"));
		registerMethod(new Method(Expression.Type.STRING, "TrimStart", "LTrim"));
		registerMethod(new Method(Expression.Type.STRING, "RemoveDiacritics", "RemoveDiacritics"));

		// IMAGE methods
		registerMethod(new Method(Expression.Type.IMAGE, "IsEmpty", "IMAGE::IsEmpty"));
		registerMethod(new Method(Expression.Type.IMAGE, "SetEmpty", "IMAGE::SetEmpty"));
		registerMethod(new Method(Expression.Type.IMAGE, "FromURL", true, "IMAGE::FromURL"));
		registerMethod(new Method(Expression.Type.IMAGE, "FromImage", true, "IMAGE::FromImage"));

		// AUDIO methods
		registerMethod(new Method(Expression.Type.AUDIO, "IsEmpty", "AUDIO::IsEmpty"));
		registerMethod(new Method(Expression.Type.AUDIO, "SetEmpty", "AUDIO::SetEmpty"));
		registerMethod(new Method(Expression.Type.AUDIO, "FromURL", true, "AUDIO::FromURL"));

		// VIDEO methods
		registerMethod(new Method(Expression.Type.VIDEO, "IsEmpty", "VIDEO::IsEmpty"));
		registerMethod(new Method(Expression.Type.VIDEO, "SetEmpty", "VIDEO::SetEmpty"));
		registerMethod(new Method(Expression.Type.VIDEO, "FromURL", true, "VIDEO::FromURL"));

		// BLOBFILE methods
		registerMethod(new Method(Expression.Type.BLOBFILE, "IsEmpty", "BLOBFILE::IsEmpty"));
		registerMethod(new Method(Expression.Type.BLOBFILE, "SetEmpty", "BLOBFILE::SetEmpty"));
		registerMethod(new Method(Expression.Type.BLOBFILE, "FromURL", true, "BLOBFILE::FromURL"));

		// Static methods.
		registerMethod(new Method(Expression.Type.GUID, "Empty", true, "STATIC.GUID::Empty"));
		registerMethod(new Method(Expression.Type.GUID, "NewGuid", true, "STATIC.GUID::NewGuid"));

		// Special case: "FromString" methods. These do not atually use the target value,
		// only the parameters. They are "kinda-sorta-static" methods.
		registerMethod(new Method(Expression.Type.BOOLEAN, "FromString", true, "BOOLEAN::FROM_STRING"));
		registerMethod(new Method(Expression.Type.DATE, "FromString", true, "DATE::FROM_STRING"));
		registerMethod(new Method(Expression.Type.DATETIME, "FromString", true, "DATETIME::FROM_STRING"));
		registerMethod(new Method(Expression.Type.DECIMAL, "FromString", true, "DECIMAL::FROM_STRING"));
		registerMethod(new Method(Expression.Type.GEOPOINT, "FromString", true, "GEOPOINT::FROM_STRING"));
		registerMethod(new Method(Expression.Type.GEOLINE, "FromString", true, "GEOLINE::FROM_STRING"));
		registerMethod(new Method(Expression.Type.GEOPOLYGON, "FromString", true, "GEOPOLYGON::FROM_STRING"));
		registerMethod(new Method(Expression.Type.GEOGRAPHY, "FromString", true, "GEOGRAPHY::FROM_STRING"));
		registerMethod(new Method(Expression.Type.GUID, "FromString", true, "GUID::FROM_STRING"));
		registerMethod(new Method(Expression.Type.INTEGER, "FromString", true, "INTEGER::FROM_STRING"));
		registerMethod(new Method(Expression.Type.STRING, "FromString", true, "STRING::FROM_STRING"));
	}

	private static void registerMethod(Method method)
	{
		for (Expression.Type type : ExpressionHelper.getCompatibleTypesForType(method.mTargetType))
		{
			// Take care to not override more specific versions of this method for compatible types.
			// E.g. by registering DateTime.ToString() after Date.ToString().
			if (type == method.mTargetType ||
				sMethods.get(type.toString(), method.mMethodName) == null)
			{
				// Either exact type, or we didn't have one.
				sMethods.put(type.toString(), method.mMethodName, method);
			}
		}
	}

	private static class Method
	{
		private final Expression.Type mTargetType;
		private final String mMethodName;
		private final boolean mIsStatic;

		private final String mMappedFunction;

		private Method(Expression.Type targetType, String methodName, String mappedFunction)
		{
			this(targetType, methodName, false, mappedFunction);
		}

		private Method(Expression.Type targetType, String methodName, boolean isStatic, String mappedFunction)
		{
			mTargetType = targetType;
			mMethodName = methodName;
			mIsStatic = isStatic;
			mMappedFunction = mappedFunction;
		}

		public Expression.Value run(Expression.Value target, List<Expression.Value> parameters)
		{
			List<Expression.Value> functionParameters = new ArrayList<>();

			if (!mIsStatic)
				functionParameters.add(target);

			functionParameters.addAll(parameters);

			// Delegate to the corresponding function.
			return FunctionHelper.call(mMappedFunction, functionParameters);
		}
	}
}
