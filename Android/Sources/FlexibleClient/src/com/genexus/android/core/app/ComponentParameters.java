package com.genexus.android.core.app;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.utils.NameMap;

@SuppressWarnings({"InconsistentCapitalization", "checkstyle:MemberName"})
public class ComponentParameters
{
	public final ComponentType Type;
	public IViewDefinition Object;
	public final short Mode;
	public final List<String> Parameters;
	public final NameMap<String> NamedParameters;
	public final String Url;

	public ComponentParameters(IViewDefinition object)
	{
		this(object, DisplayModes.VIEW, null);
	}
	
	public ComponentParameters(IViewDefinition object, short mode, List<String> parameters)
	{
		this(object, mode, parameters, null);
	}

	public ComponentParameters(IViewDefinition object, short mode, List<String> parameters, Map<String, String> namedParameters)
	{
		Type = ComponentType.Form;
		Object = object;
		Mode = mode;
		Parameters = (parameters != null ? parameters : Collections.emptyList());
		NamedParameters = new NameMap<>(namedParameters);
		Url = null;
	}
	
	public ComponentParameters(String url)
	{
		Type = ComponentType.Web;
		Object = null;
		Mode = DisplayModes.VIEW;
		Parameters = Collections.emptyList();
		NamedParameters = new NameMap<>();
		Url = url;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (Object != null)
		{
			sb.append(Object);
			if (Parameters.size() != 0)
			{
				sb.append("?");
				sb.append(Parameters.get(0));
				for (int i = 1; i < Parameters.size(); i++)
					sb.append(",").append(Parameters.get(i));
			}

			return sb.toString();
		}
		else if (Url != null)
		{
			return Url;
		}
		else
			return "<UNDEFINED>";
	}
}
