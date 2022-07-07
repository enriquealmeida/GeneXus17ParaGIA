package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.layout.ILayoutItem;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class DashboardItem extends PropertiesObject implements Serializable, ILayoutItem {
	private static final long serialVersionUID = 1L;

	private final ArrayList<DashboardItem> mItems = new ArrayList<>();
	private final List<ActionParameter> mParameters = new ArrayList<>();

	private String mObjectName;
	private String mImageName;
	private String mTitle;
	private String mName;
	private short mKind;
	private String mLinkType;
	private String mClass;
	private final IViewDefinition mViewDefinition;

	public DashboardItem(IViewDefinition definition) {
		mViewDefinition = definition;
	}

	@Override
	protected void internalDeserialize(INodeObject data) {
		super.internalDeserialize(data);

		// The base deserialization doesn't read any non-atomic nodes. So read "expression" here.
		INodeObject expression = data.getNode("expression");
		if (expression != null)
			setProperty("expression", expression);
		expression = data.getNode("assignExpression");
		if (expression != null)
			setProperty("assignExpression", expression);
	}

	public List<DashboardItem> getItems() {
		return mItems;
	}

	public String getTitle() {
		return Services.Language.getTranslation(mTitle);
	}

	public void setTitle(String value) {
		mTitle = value;
	}

	public void setKind(short s) {
		mKind = s;
	}

	public short getKind() {
		return mKind;
	}

	public boolean hasImage() {
		return Strings.hasValue(mImageName);
	}

	public String getImageName() {
		return mImageName;
	}

	public void setImage(String image) {
		mImageName = MetadataLoader.getAttributeName(image);
	}

	public void setObjectName(String attributeName) {
		mObjectName = attributeName;
	}

	public String getObjectName() {
		return mObjectName;
	}

	public List<ActionParameter> getParameters() {
		return mParameters;
	}

	public ActionDefinition getActionDefinition() {
		ActionDefinition def = new ActionDefinition(mViewDefinition);
		def.setInternalProperties(getInternalProperties());
		def.setGxObject(getObjectName());
		def.setGxObjectType(getKind());
		def.getParameters().addAll(mParameters);

		if (getItems().size() > 0) {
			for (int i = 0; i < getItems().size(); i++) {
				DashboardItem childItem = getItems().get(i);
				ActionDefinition childAction = childItem.getActionDefinition();
				def.getNextActions().add(childAction);

				//Put child childs action in definition
				//Not necessary each action has it childs.
				//addChildAction(def, childItem  );
			}
		}

		return def;
	}

	public void setName(String name) {
		mName = name;
	}

	@Override
	public String getName() {
		return mName;
	}

	public void setThemeClass(String mClass) {
		this.mClass = mClass;
	}

	public String getThemeClass() {
		return mClass;
	}

	public int getIndex()
	{
		if (mViewDefinition instanceof DashboardMetadata)
		{
			return ((DashboardMetadata) mViewDefinition).getItems().indexOf(this);
		}
		return -1;
	}

}
