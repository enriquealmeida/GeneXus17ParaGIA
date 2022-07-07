package com.genexus.android.live_editing.serializers;

import javax.inject.Singleton;

import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.live_editing.commands.CommandDesignStylesChanged;
import com.genexus.android.live_editing.commands.CommandDesignTokensChanged;
import com.genexus.android.live_editing.commands.IClientCommand;
import com.genexus.android.live_editing.commands.IServerCommand;
import com.genexus.android.live_editing.commands.CommandConnect;
import com.genexus.android.live_editing.commands.CommandImageChanged;
import com.genexus.android.live_editing.commands.CommandInspectUI;
import com.genexus.android.live_editing.commands.CommandKBDoesNotMatchGUID;
import com.genexus.android.live_editing.commands.CommandKbOk;
import com.genexus.android.live_editing.commands.CommandLayoutChanged;
import com.genexus.android.live_editing.commands.CommandMasterLayout;
import com.genexus.android.live_editing.commands.CommandServiceInfo;
import com.genexus.android.live_editing.commands.CommandThemeColorChanged;
import com.genexus.android.live_editing.commands.CommandThemeStyleChanged;
import com.genexus.android.live_editing.commands.CommandThemeTransformationChanged;
import com.genexus.android.live_editing.commands.CommandTranslationChanged;
import com.genexus.android.live_editing.inspector.ControlData;
import com.genexus.android.live_editing.inspector.MasterLayoutData;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ParsingModule {
	@Provides
	@Singleton
	public Gson providesGson() {
		RuntimeTypeAdapterFactory<IServerCommand> serverCommandAdapterFactory =
				RuntimeTypeAdapterFactory.of(IServerCommand.class, "Type")
						.registerSubtype(CommandImageChanged.class, "ImageChanged")
						.registerSubtype(CommandInspectUI.class, "InspectUI")
						.registerSubtype(CommandKBDoesNotMatchGUID.class, "KBDoesNotMatchGUID")
						.registerSubtype(CommandKbOk.class, "KBOK")
						.registerSubtype(CommandLayoutChanged.class, "LayoutChanged")
						.registerSubtype(CommandThemeColorChanged.class, "ThemeColorChanged")
						.registerSubtype(CommandThemeStyleChanged.class, "ThemeStyleChanged")
						.registerSubtype(CommandThemeTransformationChanged.class, "ThemeTransformChanged")
						.registerSubtype(CommandTranslationChanged.class, "TranslationChanged")
						.registerSubtype(CommandServiceInfo.class, "ServiceInfo")
						.registerSubtype(CommandDesignTokensChanged.class, "DesignTokensChanged")
						.registerSubtype(CommandDesignStylesChanged.class, "DesignStylesChanged");

		RuntimeTypeAdapterFactory<IClientCommand> clientCommandAdapterFactory =
				RuntimeTypeAdapterFactory.of(IClientCommand.class, "Type")
						.registerSubtype(CommandConnect.class, "Connect")
						.registerSubtype(CommandMasterLayout.class, "MasterLayout");

		return new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.registerTypeAdapter(ControlData.class, new ControlDataSerializer())
				.registerTypeAdapter(MasterLayoutData.class, new MasterLayoutDataSerializer())
				.registerTypeAdapter(INodeObject.class, new INodeObjectDeserializer())
				.registerTypeAdapter(INodeCollection.class, new INodeCollectionDeserializer())
				.registerTypeAdapterFactory(clientCommandAdapterFactory)
				.registerTypeAdapterFactory(serverCommandAdapterFactory)
				.create();
	}
}
