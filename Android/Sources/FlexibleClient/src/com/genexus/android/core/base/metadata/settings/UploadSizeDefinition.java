package com.genexus.android.core.base.metadata.settings;

import com.genexus.android.core.base.metadata.enums.ImageUploadModes;
import com.genexus.android.core.base.services.Services;

@SuppressWarnings({"InconsistentCapitalization", "checkstyle:MemberName"})
public class UploadSizeDefinition 
{
	// Default upload size and 512Kb
	public short SizeMode = SIZEINKB;
	public int SizeLimit = 512;
	public int UploadMode = ImageUploadModes.LARGE;
		
		
	// static size mode values
	public static final short SIZEINPX = 1;
	public static final short SIZEINKB = 2;
	
	public UploadSizeDefinition(int uploadMode, String valueString)
	{
		UploadMode = uploadMode;
		//parse value and set sizemode and sizelimit.
		if (Services.Strings.hasValue(valueString))
		{
			try
			{
				if (valueString.contains("x"))
				{
					//size in px
					SizeMode = SIZEINPX;
					String[] valuesString = valueString.split("x", -1);
					if (valuesString.length>0)
					{
						SizeLimit = Integer.parseInt(valuesString[0]);
					}
				}
				else
				{
					//size in Kb
					SizeMode = SIZEINKB;
					SizeLimit = Integer.parseInt(valueString);
				}
			}
			catch(NumberFormatException ex)
			{
			}
		}
	}
	
	
	
	

}
