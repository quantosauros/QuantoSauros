package com.quantosauros.jpl.dto.underlying;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.jpl.dto.AbstractInfo;

public class UnderlyingInfo extends AbstractInfo {
	protected ModelType _modelType;
	
	public UnderlyingInfo() {
		// TODO Auto-generated constructor stub
	}
	
	
	public ModelType getModelType(){
		return _modelType;
	}	
}
