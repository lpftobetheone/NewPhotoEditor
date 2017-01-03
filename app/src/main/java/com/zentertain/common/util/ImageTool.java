package com.zentertain.common.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;


import net.zenjoy.photoeditor.entity.GalleryAlbumEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageTool {
	
	private static ImageTool instance = null;  
	
	public static synchronized ImageTool getInstance() {  
	     if (instance == null) {  
	         instance = new ImageTool();  
	     }
	     return instance;  
	 }  
	
	public List<Uri> getCameraImages(Context context) {
    	Uri mUri = Uri.parse("content://media/external/images/media"); 
    	Uri mImageUri = null;
        // Set up an array of the Thumbnail Image ID column we want        	
    	final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    			null,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC");
    	
    	List<Uri> result = null;
    	if(cursor != null) {
    		Log.i("cursor.getCount()) :", cursor.getCount() + "");
    		result = new ArrayList<Uri>(cursor.getCount());    		
	        if (cursor.moveToFirst()) {
	            do {
	            	int ringtoneID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));            	
					mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
					Log.i ("mImageUri :",mImageUri.toString());
	                result.add(mImageUri);
	            } while (cursor.moveToNext());
	        }
	        cursor.close();
        } else {
        	UITool.toastCenter(context, "Sorry, can not read images from gallery.");
        }
        return result;
    }
	
	public List<Uri> getCameraImagesByAlbumId(Context context, int bucketId) {
		Uri mUri = Uri.parse("content://media/external/images/media");
		String[] projection = { MediaStore.Images.ImageColumns.DATA,
				MediaStore.Images.ImageColumns.DATE_TAKEN,
				MediaStore.Images.ImageColumns.DATE_ADDED,
				MediaStore.Images.Media._ID };
		String selection = MediaStore.Images.ImageColumns.BUCKET_ID + " = '"
				+ bucketId + "' and "
				+ MediaStore.Images.ImageColumns.DATE_TAKEN + " >= 0";
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
				selection, null, MediaStore.Images.Media.DATE_ADDED + " DESC");

		List<Uri> result = null;
		if (cursor != null) {
			result = new ArrayList<Uri>(cursor.getCount());
			if (cursor.moveToFirst()) {
				do {
					int ringtoneID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
					Uri mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
					Log.i("mImageUri :", mImageUri.toString());
					result.add(mImageUri);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} else {
			UITool.toastCenter(context, "Sorry, can not read images from gallery.");
		}	     	     
        return result;
    }
	
	public List<GalleryAlbumEntity> getAlbumList(Context context) {
		List<GalleryAlbumEntity> galleryList = null;
        ContentResolver cr = context.getContentResolver();
        String[] columns = {Images.Media._ID, Images.Media.DATA, Images.Media.BUCKET_ID, Images.Media.BUCKET_DISPLAY_NAME, "COUNT(1) AS count"};
        String selection = "0==0) GROUP BY (" + Images.Media.BUCKET_ID;
        String sortOrder = Images.Media.DATE_MODIFIED;
		try {
			Cursor cursor = cr.query(Images.Media.EXTERNAL_CONTENT_URI, columns, selection, null, sortOrder);

			if (cursor != null) {
				Log.i("cursor.getCount()) :", cursor.getCount() + "");
				galleryList = new ArrayList<GalleryAlbumEntity>(cursor.getCount());
				if (cursor.moveToFirst()) {
					do {
						int id_column = cursor.getColumnIndex(Images.Media._ID);
						int image_id_column = cursor.getColumnIndex(Images.Media.DATA);
						int bucket_id_column = cursor.getColumnIndex(Images.Media.BUCKET_ID);
						int bucket_name_column = cursor.getColumnIndex(Images.Media.BUCKET_DISPLAY_NAME);
						int count_column = cursor.getColumnIndex("count");

						// Get the field values
						int id = cursor.getInt(id_column);
						String image_path = cursor.getString(image_id_column);
						int bucket_id = cursor.getInt(bucket_id_column);
						String bucket_name = cursor.getString(bucket_name_column);
						int count = cursor.getInt(count_column);
						// Do something with the values.
						GalleryAlbumEntity gallery = new GalleryAlbumEntity(id, image_path, bucket_id, bucket_name, count);
						galleryList.add(gallery);
					} while (cursor.moveToNext());
				}
				cursor.close();
			} else {
				UITool.toastCenter(context, "Sorry, can not read images from gallery.");
			}
		} catch (Exception e) {
			UITool.toastCenter(context, "Sorry, please grant us SDcard read and write permission.");
		}
        return galleryList;               
    }
	
	public Uri addImageAsApplication(ContentResolver cr, String name,
			long dateTaken, String directory, String filename, Bitmap source,
			byte[] jpegData) {

		OutputStream outputStream = null;
		String filePath = directory + File.separator + filename;
		try {
			File dir = new File(directory);
			if (!dir.exists()) {
				dir.mkdirs();
				// Log.d(TAG, dir.toString() + " create");
			}
			File file = new File(directory, filename);
			if (file.createNewFile()) {
				outputStream = new FileOutputStream(file);
				if (source != null) {
					source.compress(CompressFormat.JPEG, 75, outputStream);
				} else {
					outputStream.write(jpegData);
				}
			}

		} catch (Exception ex) {
			// Log.w(TAG, ex);
			return null;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
				}
			}
		}

		ContentValues values = new ContentValues(7);
		values.put(Images.Media.TITLE, name);
		values.put(Images.Media.DISPLAY_NAME, filename);
		values.put(Images.Media.DATE_TAKEN, dateTaken);
		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		values.put(Images.Media.DATA, filePath);
//		/storage/emulated/0/PhotoCollage2/2015-04-22_05.06.18.jpg
		return cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	public Bitmap getBitmapBySize(Context context, Uri uri, int size) {
		int IMAGE_MAX_SIZE = size > 0 ? size : 460800; // 1MP
		InputStream in = null;
		try {
			// final int IMAGE_MAX_SIZE = 1200000; // 1.2MP

			in = context.getContentResolver().openInputStream(uri);
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
				scale++;
			}
			// Log.d(TAG, "scale = " + scale + ", orig-width: " + o.outWidth +
			// ", orig-height: " + o.outHeight);

			Bitmap b = null;
			in = context.getContentResolver().openInputStream(uri);
			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int height = b.getHeight();
				int width = b.getWidth();
				// Log.d(TAG, "1th scale operation dimenions - width: " + width
				// + ", height: " + height);

				double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, (int) y, true);
				b.recycle();
				b = scaledBitmap;
				scaledBitmap = null;

				System.gc();
			} else {
				b = BitmapFactory.decodeStream(in);
			}
			in.close();

			// fix image旋转
			ExifInterface exif = null;
			try {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					exif = new ExifInterface(getRealImagePathFromURIKITKAT(context, uri));
				} else {
					exif = new ExifInterface(getRealImagePathFromURI(context, uri));
				}
				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_UNDEFINED);
				b = rotateBitmap(b, orientation);
			} catch (Exception e) {
				Log.e("Exif error", "");
				return b;
			}

			// Log.d(TAG, "bitmap size - width: " +b.getWidth() + ", height: " +
			// b.getHeight());
			return b;
		} catch (IOException e) {
			// Log.e(TAG, e.getMessage(),e);
			return null;
		}
	}
	
	public Bitmap getBitmap(Context context, Uri uri, int imageCount) {
		int IMAGE_MAX_SIZE = 460800 + 460800 / imageCount; // 1MP
		InputStream in = null;
		try {
			// final int IMAGE_MAX_SIZE = 1200000; // 1.2MP

			in = context.getContentResolver().openInputStream(uri);
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
				scale++;
			}
			// Log.d(TAG, "scale = " + scale + ", orig-width: " + o.outWidth +
			// ", orig-height: " + o.outHeight);

			Bitmap b = null;
			in = context.getContentResolver().openInputStream(uri);
			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int height = b.getHeight();
				int width = b.getWidth();
				// Log.d(TAG, "1th scale operation dimenions - width: " + width
				// + ", height: " + height);

				double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, (int) y, true);
				b.recycle();
				b = scaledBitmap;
				scaledBitmap = null;

				System.gc();
			} else {
				b = BitmapFactory.decodeStream(in);
			}
			in.close();

			// fix image旋转
			ExifInterface exif = null;
			try {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					exif = new ExifInterface(getRealImagePathFromURIKITKAT(context, uri));
				} else {
					exif = new ExifInterface(getRealImagePathFromURI(context, uri));
				}
				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_UNDEFINED);
				b = rotateBitmap(b, orientation);
			} catch (Exception e) {
				Log.e("Exif error", "");
				return b;
			}

			// Log.d(TAG, "bitmap size - width: " +b.getWidth() + ", height: " +
			// b.getHeight());
			return b;
		} catch (Exception e) {
			// Log.e(TAG, e.getMessage(),e);
			return null;
		}
	}
	
	public Bitmap decodeSampledBitmapFromStream(Context context, Uri uri, int reqWidth, int reqHeight) {
		
		InputStream in = null;
		try {
			in = context.getContentResolver().openInputStream(uri);
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();
						
			in = context.getContentResolver().openInputStream(uri);
			o.inSampleSize = calculateInSampleSize(o, reqWidth, reqHeight);			
			// Decode bitmap with inSampleSize set
		    o.inJustDecodeBounds = false;
		    Bitmap b = BitmapFactory.decodeStream(in, null, o);
			in.close();

			// fix image旋转
			ExifInterface exif = null;
			try {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					exif = new ExifInterface(getRealImagePathFromURIKITKAT(context, uri));
				} else {
					exif = new ExifInterface(getRealImagePathFromURI(context, uri));
				}
				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_UNDEFINED);
				b = rotateBitmap(b, orientation);
			} catch (Exception e) {
				Log.e("Exif error", "");
				return b;
			}

//			Log.d("", "bitmap size - width: " +b.getWidth() + ", height: " + b.getHeight());
			return b;
		} catch (IOException e) {
//			 Log.e("", e.getMessage(),e);
			return null;
		}
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight; // Always -1 WHY?
	    final int width = options.outWidth; // Always -1 WHY?
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	        if (width > height)
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        else
	            inSampleSize = Math.round((float)width / (float)reqWidth);

	        // This offers some additional logic in case the image has a strange
	        // aspect ratio. For example, a panorama may have a much larger
	        // width than height. In these cases the total pixels might still
	        // end up being too large to fit comfortably in memory, so we should
	        // be more aggressive with sample down the image (=larger
	        // inSampleSize).
	        final float totalPixels = width * height;

	        // Anything more than 2x the requested pixels we'll sample down
	        // further.
	        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

	        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
	            inSampleSize++;
	    }
	    for (int i=5; i>0; i--){
	        if (Math.pow(2, i)<=inSampleSize){
	            inSampleSize = (int) Math.pow(2, i);
	            break;
	        }
	    }
	    return inSampleSize;
	}
	

	public String getRealImagePathFromURI(Context context, Uri contentUri) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return getRealImagePathFromURIKITKAT(context, contentUri);
		} else {
			Cursor cursor = null;
			try {
				String[] proj = { MediaStore.Images.Media.DATA };
				cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
				if(cursor != null) {
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					return cursor.getString(column_index);
				} else {
					String path = contentUri.getPath(); // "file:///mnt/sdcard/FileName.mp3"
					File file = new File(path);
					return file.getAbsolutePath();
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
	}

	public Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

		Matrix matrix = new Matrix();
		switch (orientation) {
		case ExifInterface.ORIENTATION_NORMAL:
			return bitmap;
		case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
			matrix.setScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			matrix.setRotate(180);
			break;
		case ExifInterface.ORIENTATION_FLIP_VERTICAL:
			matrix.setRotate(180);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_TRANSPOSE:
			matrix.setRotate(90);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			matrix.setRotate(90);
			break;
		case ExifInterface.ORIENTATION_TRANSVERSE:
			matrix.setRotate(-90);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			matrix.setRotate(-90);
			break;
		default:
			return bitmap;
		}
		try {
			Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return bmRotated;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public String getRealImagePathFromURIKITKAT(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}
	
	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}

	
//	private String getFilePathByContentResolver(Context context, Uri uri) {
//		if (null == uri) {
//			return null;
//		}
//		Cursor c = context.getContentResolver().query(uri, null, null, null,
//				null);
//		String filePath = null;
//		if (null == c) {
//			throw new IllegalArgumentException("Query on " + uri
//					+ " returns null result.");
//		}
//		try {
//			if ((c.getCount() != 1) || !c.moveToFirst()) {
//			} else {
//				filePath = c.getString(c
//						.getColumnIndexOrThrow(MediaColumns.DATA));
//			}
//		} finally {
//			c.close();
//		}
//		return filePath;
//	}

	
	
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
}
