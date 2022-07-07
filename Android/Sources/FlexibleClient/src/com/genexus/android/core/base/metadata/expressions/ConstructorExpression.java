package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.LevelDefinition;
import com.genexus.android.core.base.metadata.StructureDataType;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Constructor expression, i.e. "new X()" or just "new()".
 */
class ConstructorExpression implements Expression {
	static final String TYPE = "constructor";

	private final DataType mType; // Should be SDT, BC, or Collection.

	public ConstructorExpression(INodeObject node) {
		String constructedType = node.optString("@exprDataType");
		mType = ExpressionFactory.parseGxDataType(constructedType);

		if (mType.type != Type.SDT && mType.type != Type.BC && mType.type != Type.COLLECTION)
			throw new IllegalArgumentException("Unexpected @exprDataType in constructor expression: " + constructedType);
	}

	@Override
	public @NonNull Value eval(IExpressionContext context) {
		if (mType.type == Type.SDT) {
			StructureDataType sdtDefinition = Services.Application.getDefinition().getSDT(mType.typeInfo);
			if (sdtDefinition != null) {
				if (sdtDefinition.isCollection()) {
					EntityList entityList = new EntityList(sdtDefinition.getStructure());
					entityList.setItemType(Expression.Type.SDT);
					return Value.newCollection(entityList);
				}
				else {
					Entity sdt = EntityFactory.newEntity(sdtDefinition.getStructure());
					sdt.initialize(); // create inner members empty values
					return Value.newSdt(sdt);
				}
			}

			// This could be unified sometime with the thing done in DataTypes.getDataTypeOf()
			// Support any level.
			List<String> itemNameList = new ArrayList<>();
			String sdtName = mType.typeInfo;
			int separatorPos = sdtName.lastIndexOf(Strings.DOT);
			while (separatorPos != -1) {
				itemNameList.add(0, sdtName.substring(separatorPos + 1));
				sdtName = sdtName.substring(0, separatorPos);
				// Look from last dot to the begging because some dots may correspond to a module
				StructureDataType sdt = Services.Application.getDefinition().getSDT(sdtName);
				if (sdt != null) {
					LevelDefinition level = sdt.getRoot();
					for (String itemName : itemNameList) {
						level = level.getLevel(itemName);
						if (level == null)
							break;
					}
					if (level != null) {
						Entity sdtLevel = EntityFactory.newEntity(sdt.getStructure(), level);
						sdtLevel.initialize(); // create inner members empty values
						return Value.newSdt(sdtLevel);
					}
					// Level not found, continue searching because it may be another sdt in a parent module?
				}
				separatorPos = sdtName.lastIndexOf(Strings.DOT);
			}

			throw new IllegalArgumentException(String.format("SDT definition for '%s' is missing.", mType.typeInfo));
		}
		else if (mType.type == Type.BC) {
			Entity bc = EntityFactory.newBC(mType.typeInfo);
			return Value.newBC(bc);
		}
		else if (mType.type == Type.COLLECTION) {
			if (mType.typeParameter != null) {
				if (mType.typeParameter.type.isSimple()) {
					return Value.newCollection(new ValueCollection(mType.typeParameter.type));
				} else if (mType.typeParameter.type == Type.SDT) {
					StructureDataType sdtDefinition = Services.Application.getDefinition().getSDT(mType.typeParameter.typeInfo);
					if (sdtDefinition != null) {
						EntityList entityList = new EntityList(sdtDefinition.getStructure());
						entityList.setItemType(Expression.Type.SDT);
						return Value.newCollection(entityList);
					}
				}
			}
			return Value.newCollection(new EntityList());
		}
		else
			throw new IllegalArgumentException(String.format("Unexpected mType in constructor expression: '%s'.", mType));
	}

	@Override
	public void values(@NonNull HashMap<String, DataType> nameTypes) { }

	@Override
	public boolean needsBackgroundThread() {
		return false;
	}
}
