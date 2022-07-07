package com.genexus.android.core.base.metadata.enums;

import com.genexus.android.R;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;

public class DisplayModes
{
	public static final short VIEW = 0;
	public static final short EDIT = 1;
	public static final short DELETE = 2;
	public static final short INSERT = 3;

	private static final String VARIABLE_MODE = "Mode";
	
	public static boolean isEdit(short mode)
	{
		return (mode == INSERT || mode == EDIT || mode == DELETE);
	}

	/**
	 * Sets the "value" of the &Mode variable (as defined for use in GeneXus code, e.g. "INS")
	 * in the supplied Entity.
	 */
	public static void setVariable(Entity data, short mode)
	{
		if (data.getPropertyDefinition(VARIABLE_MODE) != null)
			data.setProperty(VARIABLE_MODE, getValue(mode));
	}
	
	/**
	 * Returns the "value" of the mode, as defined for use in &Mode variable or other GeneXus code (e.g. "INS").
	 */
	private static String getValue(short mode)
	{
		switch (mode)
		{
			case INSERT : return "INS";
			case EDIT : return "UPD";
			case DELETE : return "DLT";
			case VIEW : return "DSP";
			default : throw new IllegalArgumentException("Invalid mode");
		}		
	}

	/**
	 * Returns a user-visible description for the mode (e.g. "Insert").
	 */
	public static String getString(short mode)
	{
		return Services.Strings.getResource(getStringResource(mode));
	}
	
	private static int getStringResource(short mode)
	{
		switch (mode)
		{
			case VIEW : return R.string.GXM_view;
			case EDIT : return R.string.GXM_update;
			case DELETE : return R.string.GX_BtnDelete;
			case INSERT : return R.string.GXM_insert;
			default : return R.string.GXM_Unknown;
		}		
	}
	
	public static int getSuccessMessageResource(int mode)
	{
		switch (mode)
		{
			case INSERT :
				return R.string.GXM_recins;
			case EDIT :
				return R.string.GXM_recupd;
			case DELETE :
				return R.string.GXM_recdel;
			default :
				return R.string.GXM_recupd;
		}
	}
}
