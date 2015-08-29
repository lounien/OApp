package cn.o.app.core.runtime;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class ArgumentsType implements ParameterizedType {

	protected Type[] mArguments;

	protected Type mOwnerType;

	protected Type mRawType;

	@Override
	public Type[] getActualTypeArguments() {
		return mArguments;
	}

	public void setActualTypeArguments(Type[] arguments) {
		mArguments = arguments;
	}

	public void setActualTypeArguments(List<Type> argments) {
		mArguments = (Type[]) argments.toArray();
	}

	@Override
	public Type getOwnerType() {
		return mOwnerType;
	}

	public void setOwnerType(Type ownerType) {
		mOwnerType = ownerType;
	}

	@Override
	public Type getRawType() {
		return mRawType;
	}

	public void setRawType(Type rawType) {
		mRawType = rawType;
	}

	public void clear() {
		mOwnerType = null;
		mRawType = null;
		if (mArguments != null) {
			Arrays.fill(mArguments, null);
			mArguments = null;
		}
	}

}