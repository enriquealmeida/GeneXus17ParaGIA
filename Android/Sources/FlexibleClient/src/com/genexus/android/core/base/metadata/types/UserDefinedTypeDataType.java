package com.genexus.android.core.base.metadata.types;

import com.genexus.android.core.base.metadata.DataTypeDefinition;
import com.genexus.android.core.base.metadata.ITypeDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApiFactory;

public class UserDefinedTypeDataType extends DataTypeDefinition implements ITypeDefinition {
    private final String mTypeName;
    public UserDefinedTypeDataType(String typeName) {
        super(null);
        mTypeName = typeName;
    }

    @Override
    public Object getEmptyValue(boolean isCollection) {
        ExternalApiFactory externalApiFactory = Services.Application.getExternalApiFactory();
        return externalApiFactory.getInstance(mTypeName, null);
    }
}
