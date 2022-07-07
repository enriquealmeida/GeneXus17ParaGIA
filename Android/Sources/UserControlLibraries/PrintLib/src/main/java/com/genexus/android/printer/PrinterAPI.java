package com.genexus.android.printer;

import android.Manifest;
import android.os.Build;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import java.util.Arrays;

import androidx.annotation.RequiresApi;

public class PrinterAPI extends ExternalApi {
    public static final String OBJECT_NAME = "GeneXus.SD.Printer";

    // API Method Names
    private static final String PROPERTY_PRINTER_NAME = "PrinterName";
    private static final String PROPERTY_DPI = "DPI";
    private static final String PROPERTY_WIDTH = "Width";
    private static final String VALUE_ROTATION_NO = "0";
    private static final String VALUE_ROTATION_LEFT = "1";
    private static final String VALUE_ROTATION_RIGHT = "2";
    private static final String METHOD_GET_DEVICES = "GetDevices";
    private static final String METHOD_PRINT = "Print";
    private static final String METHOD_CUT_PAPER = "CutPaper";

    // BLUETOOTH_CONNECT permission is required for retrieving bonded devices in API 31
    @RequiresApi(api = Build.VERSION_CODES.S)
    private static final String[] PERMISSIONS_API31 = new String[]{Manifest.permission.BLUETOOTH_CONNECT};

    private Printer mPrinter;

    public PrinterAPI(ApiAction action) {
        super(action);
        mPrinter = new Printer(action.getContext());
        String[] permissions = getPermissionsApi31();
        addPropertyHandler(PROPERTY_PRINTER_NAME, mGetPrinterNameProperty, mSetPrinterNameProperty);
        addPropertyHandler(PROPERTY_DPI, mGetDpiProperty, mSetDpiProperty);
        addPropertyHandler(PROPERTY_WIDTH, mGetWidthProperty, mSetWidthProperty);
        addMethodHandlerRequestingPermissions(METHOD_GET_DEVICES, 0, permissions, mGetDevicesMethod);
        addMethodHandlerRequestingPermissions(METHOD_PRINT, 1, permissions, mPrintMethod);
        addMethodHandlerRequestingPermissions(METHOD_PRINT, 2, permissions, mPrintMethod);
        addMethodHandlerRequestingPermissions(METHOD_CUT_PAPER, 1, permissions, mCutPaperMethod);
    }

    private final IMethodInvoker mGetPrinterNameProperty = parameters ->
		ExternalApiResult.success(mPrinter.getName());

    private final IMethodInvoker mSetPrinterNameProperty = parameters -> {
        mPrinter.setName((String)parameters.get(0));
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetDpiProperty = parameters ->
		ExternalApiResult.success(mPrinter.getDpi());

    private final IMethodInvoker mSetDpiProperty = parameters -> {
        String dpi = (String)parameters.get(0);
        mPrinter.setDpi(Integer.parseInt(dpi));
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetWidthProperty = parameters ->
		ExternalApiResult.success(mPrinter.getWidth());

    private final IMethodInvoker mSetWidthProperty = parameters -> {
        String width = (String)parameters.get(0);
        mPrinter.setWidth(Integer.parseInt(width));
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private Printer.Rotation getRotationEnum(String rotation)
    {
        switch (rotation)
        {
            case VALUE_ROTATION_NO:
            default:
                return Printer.Rotation.No;
            case VALUE_ROTATION_LEFT:
                return Printer.Rotation.Left;
            case VALUE_ROTATION_RIGHT:
                return Printer.Rotation.Right;
        }
    }

    private final IMethodInvoker mGetDevicesMethod = parameters -> {
		ValueCollection collection = new ValueCollection(Expression.Type.STRING);
		collection.addAll(Arrays.asList(mPrinter.getDevices()));
		return ExternalApiResult.success(collection);
	};

    private final IMethodInvoker mPrintMethod = parameters -> {
        String filePath = (String)parameters.get(0);
        String rotation = parameters.size() > 1 ? (String)parameters.get(1) : VALUE_ROTATION_NO;
        mPrinter.print(filePath, getRotationEnum(rotation));
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mCutPaperMethod = parameters -> {
        mPrinter.cutPaper();
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private String[] getPermissionsApi31() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
            return new String[0];

        String[] permissions = new String[PERMISSIONS_API31.length];
        System.arraycopy(PERMISSIONS_API31, 0, permissions, 0, PERMISSIONS_API31.length);
        return permissions;
    }
}
