package com.grupomedios.desclub.desclubutil.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.MediaStore;

import java.util.List;

public class CameraUtils
{
	public static boolean hasHardwareCamera(Context context)
	{
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	public static boolean hasCameraApp(Context context)
	{
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
}