package com.genexus.android.core.actions;

import android.content.ContentResolver;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.artech.base.services.IPropertiesObject;
import com.genexus.android.media.MediaUtils;
import com.genexus.android.core.base.application.IBusinessComponent;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.expressions.ExpressionValueBridge;
import com.genexus.android.core.base.metadata.types.IStructuredDataType;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.FileUtils2;

import java.util.List;
import java.util.Set;

public class BCMethodHandler {
    private static final String METHOD_BC_LOAD = "Load";
    private static final String METHOD_BC_SAVE = "Save";
    private static final String METHOD_BC_DELETE = "Delete";
    private static final String METHOD_BC_INSERT = "Insert";
    private static final String METHOD_BC_INSERT_OR_UPDATE = "InsertOrUpdate";
	private static final String METHOD_BC_UPDATE = "Update";
	private static final String METHOD_BC_SUCCESS = "Success";
    private static final String METHOD_BC_FAIL = "Fail";
    private static final String METHOD_BC_GET_MESSAGES = "GetMessages";

    private static final String TAG_BC = "IBusinessComponent";

    public static Value eval(UIContext context, Value value, String method, List<Value> parameters) {
        Entity entity = value.coerceToEntity();
        StructureDefinition structureDef = ((IStructuredDataType) value.getDefinition().getBaseType()).getStructure();
        IApplicationServer server;
        if (structureDef.getConnectivitySupport() != Connectivity.Inherit)
            server = Services.Application.getApplicationServer(structureDef.getConnectivitySupport());
        else
            server = Services.Application.getApplicationServer(context.getConnectivitySupport());
        IBusinessComponent businessComponent = (IBusinessComponent)entity.getTag(TAG_BC);
        if (businessComponent == null)
            businessComponent = server.getBusinessComponent(structureDef.getName());
        OutputResult result;

        if (METHOD_BC_LOAD.equalsIgnoreCase(method)) {
            List<String> key = ExpressionValueBridge.convertValuesToString(parameters);
            result = businessComponent.load(entity, key);
            if (result.isOk())
                entity.setTag(TAG_BC, businessComponent); // So the update mode is not lost
        }
        else if (METHOD_BC_SAVE.equalsIgnoreCase(method)) {
            uploadBlobsFromEntity(server, businessComponent.getName(), entity);
            result = businessComponent.save(entity);
        }
        else if (METHOD_BC_DELETE.equalsIgnoreCase(method)) {
            result = businessComponent.delete();
        }
        else if (METHOD_BC_INSERT.equalsIgnoreCase(method)) {
            uploadBlobsFromEntity(server, businessComponent.getName(), entity);
            result = businessComponent.insert(entity);
        }
        else if (METHOD_BC_INSERT_OR_UPDATE.equalsIgnoreCase(method)) {
            uploadBlobsFromEntity(server, businessComponent.getName(), entity);
            result = businessComponent.insertOrUpdate(entity);
        }
		else if (METHOD_BC_UPDATE.equalsIgnoreCase(method)) {
			uploadBlobsFromEntity(server, businessComponent.getName(), entity);
			result = businessComponent.update(entity);
		}
        else if (METHOD_BC_SUCCESS.equalsIgnoreCase(method)) {
            return Value.newBoolean(businessComponent.success());
        }
        else if (METHOD_BC_FAIL.equalsIgnoreCase(method)) {
            return Value.newBoolean(!businessComponent.success());
        }
        else if (METHOD_BC_GET_MESSAGES.equalsIgnoreCase(method)) {
            return Value.newValue(businessComponent.getMessages());
        }
        else {
            throw new IllegalArgumentException(String.format("Unexpected BC method: '%s'", method));
        }

        if (result.isOk())
            return Value.UNKNOWN;
        else
            return Value.newFail(result);
    }

    private static final Set<String> URI_SCHEMES_FOR_UPLOAD = Strings.newSet(ContentResolver.SCHEME_CONTENT,
            ContentResolver.SCHEME_FILE, FileUtils2.SCHEME_GX_RESOURCE);

    private static ResultDetail<Void> uploadBlobsFromEntity(IApplicationServer server, String objectName, Entity entity) {
        return uploadBlobsFromEntity(server, objectName, 0, entity);
    }

    private static ResultDetail<Void> uploadBlobsFromEntity(IApplicationServer mServer, String objectName, int maxUploadSizeMode, Entity innerEntity) {
        for (DataItem def : innerEntity.getLevel().Items) {
            int innerMaxUploadSizeMode = maxUploadSizeMode == 0 ? maxUploadSizeMode : def.getMaximumUploadSizeMode();
            ResultDetail<Void> subResult = uploadBlobsFromContainer(mServer, objectName, innerMaxUploadSizeMode, innerEntity, def);
            if (!subResult.getResult())
                return subResult; // Abort on failure
        }
        return ResultDetail.ok(); // All ok
    }

	static @NonNull ResultDetail<Void> uploadBlobsFromContainer(IApplicationServer mServer, String objectName, int maxUploadSizeMode, IPropertiesObject container, DataItem def) {
        String controlName = def.getName();
        String controlType = def.getControlType();

        if (def.isMediaOrBlob()) {
            String valueString = container.optStringProperty(controlName);
            if (Strings.hasValue(valueString)) {
                Uri mediaUri = Uri.parse(valueString);

                if (ControlTypes.PHOTO_EDITOR.equalsIgnoreCase(controlType))
                    mediaUri = MediaUtils.translateGenexusImageUri(mediaUri);

                // avoid crash path with no schema. ie. server relative url
				if (mediaUri.getScheme()==null)
					return ResultDetail.ok(); // All ok

				if (URI_SCHEMES_FOR_UPLOAD.contains(mediaUri.getScheme())) {
                    ResultDetail<Void> uploadResult = MediaUtils.uploadMedia(Services.Application.getAppContext(),
                            mediaUri, controlName, controlType, maxUploadSizeMode, mServer,
                            container, objectName, null);

                    if (!uploadResult.getResult())
                        return uploadResult; // Abort on failure
                }
            }
        }
        else {
            Object valueObj = container.getProperty(controlName);
            if (valueObj instanceof Entity) {
                Entity innerEntity = (Entity) valueObj;

                ResultDetail<Void> subResult = uploadBlobsFromEntity(mServer, objectName, maxUploadSizeMode, innerEntity);
                if (!subResult.getResult())
                    return subResult; // Abort on failure
            }
            else if (valueObj instanceof EntityList) {
                EntityList innerEntityList = (EntityList) valueObj;
                for (Entity innerEntity : innerEntityList) {
                    ResultDetail<Void> subResult = uploadBlobsFromEntity(mServer, objectName, maxUploadSizeMode, innerEntity);
                    if (!subResult.getResult())
                        return subResult; // Abort on failure
                }
            }
        }

        return ResultDetail.ok(); // All ok
    }
}
