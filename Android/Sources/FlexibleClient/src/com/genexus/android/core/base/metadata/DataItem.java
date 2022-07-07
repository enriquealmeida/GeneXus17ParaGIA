package com.genexus.android.core.base.metadata;

import androidx.annotation.NonNull;

import java.io.Serializable;

import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.enums.ImageUploadModes;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class DataItem extends PropertiesObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String mName;
	private String mExternalName;
	private boolean mIsCollection;
	private String mJsonNullSerialization;

	private ITypeDefinition mBaseDataType = null;
	private String mControlType = null;
	private int mStorageType;

	// Cached properties (most accessed, or slowest).
	private Boolean mIsEnumeration;
	private String mInputPicture;

	public DataItem(ITypeDefinition attribute)
	{
		mBaseDataType = attribute;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public boolean isVariable() { return false; }

	public void setDataType(ITypeDefinition def) { mBaseDataType = def; }

	public ITypeDefinition getBaseType() { return mBaseDataType; }

	public <TypeT extends ITypeDefinition> TypeT getTypeInfo(Class<TypeT> tType)
	{
		// Go up the chain of base types until encountering the specified type, or not having any more.
		// Might be an indirect inheritance by way of a Domain.
		ITypeDefinition type = getBaseType();
		while (type != null && !tType.isInstance(type))
			type = type.getBaseType();

		return (type != null ? tType.cast(type) : null);
	}

	public boolean isCollection()
	{
		return mIsCollection;
	}

	public void setIsCollection(boolean value)
	{
		mIsCollection = value;
	}

	public String getJsonNullSerialization()
	{
		return mJsonNullSerialization;
	}

	void setJsonNullSerialization(String value)
	{
		mJsonNullSerialization = value;
	}

	public @NonNull String getName()
	{
		if (mName == null)
			mName = (String)getProperty("Name");

		return mName;
	}

	public String getCaption()
	{
		return Services.Language.getTranslation(optStringProperty("Caption"));
	}

	public void setName(String name)
	{
		setProperty("Name", name);
		mName = name;
	}

	@Override
	public Object getProperty(String propName)
	{
		Object localValue = super.getProperty(propName);

		if (localValue == null && mBaseDataType != null) // We don't have a value for this property so inherit from based on type
			localValue = mBaseDataType.getProperty(propName);

		return localValue;
	}

	public String getExternalName()
	{
		if (mExternalName == null)
		{
			String externalName = (String)getProperty("JsonName");
			if (Strings.hasValue(externalName))
				mExternalName = externalName;
			else
				mExternalName = getName();
		}

		return mExternalName;
	}

	public String getType()
	{
		if (mBaseDataType != null)
			return mBaseDataType.getType();

		return Strings.EMPTY;
	}

	public String getInputPicture()
	{
		if (mInputPicture == null)
			mInputPicture = super.optStringProperty("InputPicture");

		return mInputPicture;
	}

	public int getLength() {
		return super.optIntProperty("Length");
	}

	public int getDecimals() {
		return super.optIntProperty("Decimals");
	}

	public boolean getSigned() {
		return super.optBooleanProperty("Signed");
	}

	public boolean getReadOnly() {
		return super.optBooleanProperty("ReadOnly");
	}

	public boolean getAutoNumber() {
		return super.optBooleanProperty("AutoNumber");
	}

	public boolean getIsEnumeration()
	{
		if (mIsEnumeration == null)
			mIsEnumeration = super.optBooleanProperty("IsEnumeration");

		return mIsEnumeration;
	}

	public String getEnumerationType() {
		return super.optStringProperty("EnumerationType");
	}

	public boolean isDescription()
	{
		return super.optBooleanProperty("DescriptionAtt");
	}

	private DataTypeName mDataTypeName;

	public DataTypeName getDataTypeName() {
		if (mDataTypeName == null) {
			String dataName  = getType();
			ITypeDefinition parent = getBaseType();
			while (parent != null) {
				if (parent instanceof DomainDefinition) {
					if (DomainDefinition.isSpecialDomain(parent))
					{
						dataName = parent.getName();
						break;
					}
				}
				parent = parent.getBaseType();
			}
			mDataTypeName = new DataTypeName(dataName != null ? dataName : "unknown");
		}

		return mDataTypeName;
	}

	public boolean isKey() {
		return super.optBooleanProperty("IsKey");
	}

	public String getControlType()
	{
		if (mControlType != null)
			return mControlType;

		mControlType = getDataTypeName().getControlType();

		// If different from default use the domain control type.
		if (mControlType != null && !mControlType.equalsIgnoreCase(ControlTypes.TEXT_BOX))
			return mControlType;

		//Check if is enumeration
		if (getIsEnumeration())// && getEnumerationType().length()>0)
		{
			mControlType = ControlTypes.ENUM_COMBO;
			return mControlType;
		}

		//If not calculate it from m_Type
		String type = getType(); // Not m_Type, since it may be overriden by variable definition.
		mControlType = ControlTypes.TEXT_BOX;//Default TextBox.

		if (type.equals("int") || type.equals("numeric"))
			mControlType = ControlTypes.NUMERIC_TEXT_BOX;
		else if (type.equals(DataTypes.DATE) || type.equals(DataTypes.DTIME) || type.equals(DataTypes.TIME) || type.equals(DataTypes.DATETIME))
			mControlType = ControlTypes.DATE_BOX;
		else // This should be changed on metadata writers so all these cases come with value "image"
			if (type.equalsIgnoreCase("bits") || type.equalsIgnoreCase("bitmap"))
				mControlType = ControlTypes.PHOTO_EDITOR;
		else if (type.equalsIgnoreCase(DataTypes.BLOB) || type.equalsIgnoreCase(DataTypes.BLOBFILE))
			mControlType = ControlTypes.BINARY_BLOB;
		return mControlType;
	}

	public DataItem getCopy()
	{
		DataItem item = new DataItem(getBaseType());
		item.setInternalProperties(cloneProperties());
		return item;
	}

	public void merge(DataItem item)
	{
		getInternalProperties().putAll(item.getInternalProperties());
	}

	public Object getEmptyValue()
	{
		if (mBaseDataType != null)
			return mBaseDataType.getEmptyValue(isCollection());
		else
			return null;
	}

	public boolean isEmptyValue(Object value)
	{
		if (mBaseDataType != null)
			return mBaseDataType.isEmptyValue(value);
		else
			return (value == null || value.toString().length() == 0);
	}

	public int getStorageType() { return mStorageType; }
	protected void setStorageType(int value) { mStorageType = value; }

	public int getMaximumUploadSizeMode() {
		String maxUploadSize = super.optStringProperty("MaximumUploadSize");
		return ImageUploadModes.getModeFromString(maxUploadSize);
	}

	public boolean isMediaOrBlob()
	{
		String controlType = getControlType();
		if (controlType != null)
		{
			return (controlType.equals(ControlTypes.PHOTO_EDITOR) ||
					controlType.equals(ControlTypes.AUDIO_VIEW) ||
					controlType.equals(ControlTypes.VIDEO_VIEW) ||
					controlType.equals(ControlTypes.BINARY_BLOB) );
		}
		else
			return false;
	}
}
